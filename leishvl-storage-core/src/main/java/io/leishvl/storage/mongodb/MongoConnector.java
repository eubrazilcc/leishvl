/*
 * Copyright 2014-2015 EUBrazilCC (EU‐Brazil Cloud Connect)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * 
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 */

package io.leishvl.storage.mongodb;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.ImmutableMap;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.*;
import com.mongodb.client.model.CountOptions;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.IndexModel;
import com.mongodb.client.model.UpdateOptions;
import io.leishvl.storage.Filter;
import io.leishvl.storage.Filters;
import io.leishvl.storage.LeishvlObjectNotFoundException;
import io.leishvl.storage.LeishvlObjectWriteException;
import io.leishvl.storage.base.LeishvlCollection;
import io.leishvl.storage.base.LeishvlObject;
import io.leishvl.storage.mongodb.jackson.MongoDateDeserializer;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.Shareable;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableLong;
import org.bson.*;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.geojson.FeatureCollection;
import org.geojson.Polygon;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayList;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.ReturnDocument.AFTER;
import static com.mongodb.client.model.Sorts.*;
import static io.leishvl.core.util.GeoJsons.featureBuilder;
import static io.leishvl.core.util.GeoJsons.featureCollectionWGS84;
import static io.leishvl.storage.Filters.LogicalType.LOGICAL_AND;
import static io.leishvl.storage.base.LeishvlObject.*;
import static io.leishvl.storage.base.StorageDefaults.TYPEAHEAD_MAX_ITEMS;
import static io.leishvl.storage.mongodb.jackson.MongoJsonMapper.JSON_MAPPER;
import static io.vertx.core.Future.failedFuture;
import static io.vertx.core.Future.succeededFuture;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.beanutils.PropertyUtils.getSimpleProperty;
import static org.apache.commons.lang3.StringUtils.*;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Data connector based on mongoDB. Access to file collections is provided through the GridFS specification.
 * @author Erik Torres <ertorser@upv.es>
 * @see <a href="https://www.mongodb.org/">mongoDB</a>
 * @see <a href="http://docs.mongodb.org/manual/core/gridfs/">GridFS</a>
 */
public class MongoConnector {

    private static final Logger LOGGER = getLogger(MongoConnector.class);

    private static final String DS_LOCAL_MAP_NAME = "__vertx.LeishVL.MongoConnector.datasources";

    private final Vertx vertx;
    protected final MongoClient mongo;
    private final MongoHolder holder;

    public MongoConnector(final Vertx vertx, final JsonObject config) {
        requireNonNull(vertx, "Vert.x Core API reference expected.");
        requireNonNull(config, "mongoDB configuration expected.");
        String dbName;
        checkArgument(isNotBlank(dbName = trimToNull(config.getString("db_name"))), "Database name expected.");
        this.vertx = vertx;
        this.holder = lookupHolder(dbName, config);
        this.mongo = holder.mongo();
    }

    public void close(final Handler<AsyncResult<Void>> completionHandler) {
        vertx.executeBlocking(future -> {
            holder.close();
            future.complete();
        }, completionHandler);
    }

    /**
     * Creates the specified indexes in the collection.
     * @param collection - the collection where the indexes will be created
     * @param indexes - indexes to be created
     * @param resultHandler - a handler which result indicates that the operation is successfully completed, or an exception when the method fails
     */
    public void createIndexes(final String collection, final List<IndexModel> indexes, final Handler<AsyncResult<List<String>>> resultHandler) {
        checkArgument(isNotBlank(collection), "Uninitialized or invalid collection");
        checkArgument(indexes != null, "Uninitialized or invalid indexes");
        final MongoCollection<Document> dbcol = holder.db.getCollection(collection);
        dbcol.createIndexes(indexes, wrapCallback(resultHandler));
    }

    /**
     * Saves the specified object to the database, overriding the active version or creating a new entry in case that there are no matches
     * for the object GUID. This method will fail when the active version cannot be overridden.
     * @param obj - object to save
     * @param resultHandler - a handler which result indicates that the operation is successfully completed, or an exception when the method fails
     * @param allowedTransitions - transitions between states
     */
    public <T extends LeishvlObject> void saveActive(final T obj, final Handler<AsyncResult<Void>> resultHandler, final String... allowedTransitions) {
        save(null, obj, resultHandler, allowedTransitions);
    }

    /**
     * Saves the specified object to the database, creating a new version. After the object is saved, this method will select the latest
     * modified record as the new active version. In case that the saved object is not the latest modified, this method will not fail.
     * @param version - new version to assign
     * @param obj - object to save
     * @param resultHandler - a handler which result indicates that the operation is successfully completed, or an exception when the method fails
     * @param allowedTransitions - transitions between states
     */
    public <T extends LeishvlObject> void saveAsVersion(final String version, final T obj, final Handler<AsyncResult<Void>> resultHandler, final String... allowedTransitions) {
        checkArgument(isNotBlank(version), "New object version cannot be empty");
        // insert the object in the collection as a new version
        save(version, obj, insertResult -> {
            // set the new active version to the latest modified record
            if (insertResult.succeeded()) {
                vertx.runOnContext(v -> activateLastModified(getCollection(obj), obj.getLeishvlId(), activateResult -> {
                    if (activateResult.succeeded()) {
                        final Document activeDoc = activateResult.result();
                        if (activeDoc != null) {
                            final String activeVersion = activeDoc.getString(LEISHVL_VERSION_FIELD);
                            final String thisVersion = obj.getVersion();
                            if (!thisVersion.equals(activeVersion)) {
                                LOGGER.info("The later modification '" + activeVersion + "' is the new active version instead of this '" + thisVersion
                                        + "' [collection= " + obj.getCollection() + ", lvlId=" + obj.getLeishvlId() + "].");
                            }
                            resultHandler.handle(succeededFuture());
                        } else
                            resultHandler.handle(failedFuture(new LeishvlObjectWriteException("No record was selected as the new active version.")));
                    } else resultHandler.handle(failedFuture(activateResult.cause()));
                }));
            } else resultHandler.handle(failedFuture(insertResult.cause()));
        }, allowedTransitions);
    }

    /**
     * Base method to save objects to the database.
     * <p><b>Since the current version of the {@link MongoCollection#findOneAndUpdate(Bson, Bson, FindOneAndUpdateOptions, SingleResultCallback)} method does not
     * provide support for index hints, this method will always use an index, like in the following example:</b></p>
     * <p>Using to match the active objects {@link #matchActive(LeishvlObject)} will not use the sparse index. Therefore, the following filter
     * {@link #matchActive(LeishvlObject)} is preferred which includes a search on the alternative index.</p>
     * @param version - set to a value different to <tt>null</tt> to override the active version of the object
     * @param obj - object to save
     * @param resultHandler - a handler which result indicates that the operation is successfully completed, or an exception when the method fails
     * @param allowedTransitions - transitions between states
     */
    private <T extends LeishvlObject> void save(final @Nullable String version, final T obj, final Handler<AsyncResult<Void>> resultHandler, final String... allowedTransitions) {
        requireNonNull(resultHandler, "Result handler expected.");
        final MongoCollection<Document> dbcol = getCollection(obj);
        final String version2 = trimToNull(version);
        final boolean overrideActive = (version2 == null);
        final List<String> states = (allowedTransitions != null ? asList(allowedTransitions) : Collections.<String>emptyList());
        // prepare query statement
        final Bson filter = (overrideActive ? (states.isEmpty() ? matchActive2(obj) : and(matchActive2(obj), matchStates(states)))
                : (states.isEmpty() ? and(matchGuid(obj), matchVersion(version)) : and(matchGuid(obj), or(matchVersion(version), not(matchStates(states))))));
        final Document update = new Document(ImmutableMap.<String, Object>of("$set", parseObject(obj, overrideActive),
                "$currentDate", new Document(LEISHVL_LAST_MODIFIED_FIELD, true),
                "$setOnInsert", new Document(LEISHVL_VERSION_FIELD, version)));
        final FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().projection(fields(include(LEISHVL_VERSION_FIELD, LEISHVL_LAST_MODIFIED_FIELD)))
                .returnDocument(AFTER)
                .upsert(true);
        // execute the operation in the database
        dbcol.findOneAndUpdate(filter, update, options, convertCallback(resultHandler, result -> {
            if (result != null) {
                obj.setDbId(result.get("_id", ObjectId.class).toHexString());
                obj.setVersion(result.getString(LEISHVL_VERSION_FIELD));
                obj.setLastModified(result.getDate(LEISHVL_LAST_MODIFIED_FIELD));
            } else
                throw new LeishvlObjectWriteException("No new records were inserted, no existing records were modified by the operation.");
            return null;
        }));
    }

    private void activateLastModified(final MongoCollection<Document> dbcol, final String lvlId, final Handler<AsyncResult<Document>> resultHandler) {
        // find and deactivate all versions of the specified object in the database
        final BsonDocument update = new BsonDocument("$unset", new BsonDocument(newArrayList(
                new BsonElement(LEISHVL_SPARSE_IS_ACTIVE_FIELD, new BsonString("")),
                new BsonElement(LEISHVL_DENSE_IS_ACTIVE_FIELD, new BsonString("")))));
        final UpdateOptions options = new UpdateOptions().upsert(false);
        dbcol.updateMany(matchGuid(lvlId), update, options, (result, error) -> {
            // set the new active version
            if (error == null) {
                vertx.runOnContext(v -> {
                    final BsonDocument update2 = new BsonDocument("$set", new BsonDocument(newArrayList(
                            new BsonElement(LEISHVL_SPARSE_IS_ACTIVE_FIELD, new BsonString(lvlId)),
                            new BsonElement(LEISHVL_DENSE_IS_ACTIVE_FIELD, new BsonString(lvlId)))));
                    final FindOneAndUpdateOptions options2 = new FindOneAndUpdateOptions().sort(orderBy(descending(LEISHVL_LAST_MODIFIED_FIELD)))
                            .returnDocument(AFTER)
                            .upsert(false);
                    dbcol.findOneAndUpdate(matchGuid(lvlId), update2, options2, (document, error2) -> {
                        if (error2 == null) resultHandler.handle(succeededFuture(document));
                        else resultHandler.handle(failedFuture(error2));
                    });
                });
            } else resultHandler.handle(failedFuture(error));
        });
    }

    /**
     * Finds the active version of the specified object. In case that any of the object's versions are active, this method will return
     * <tt>null</tt> to the caller.
     * @param obj - the object to search for
     * @param type - the object type
     * @param resultHandler - a handler which result indicates that the operation is successfully completed, or an exception when the method fails
     */
    public <T extends LeishvlObject> void findActive(final LeishvlObject obj, final Class<T> type, final Handler<AsyncResult<LeishvlObject>> resultHandler) {
        checkArgument(obj != null && obj.getClass().isAssignableFrom(type), "Uninitialized or invalid object type.");
        checkArgument(isNotBlank(obj.getLeishvlId()), "Uninitialized or invalid primary key value.");
        final MongoCollection<Document> dbcol = getCollection(obj);
        dbcol.find(matchActive(obj)).modifiers(new BsonDocument("$hint", IS_ACTIVE_SPARSE_HINT)).sort(LAST_MODIFIED_SORT_DESC).first(convertCallback(resultHandler, result -> {
            if (result == null) throw new LeishvlObjectNotFoundException("Object not found.");
            else {
                try {
                    return parseDocument(result, type);
                } catch (Exception e) {
                    throw new IllegalStateException("Failed to parse result document.", e);
                }
            }
        }));
    }

    /**
     * Finds active elements in a collection. Since <tt>hint</tt> and <tt>text</tt> are mutually exclusive in a query expression, the sparse index will be
     * included only when no text search is involved in the query. Otherwise, the text index will be used.
     * @see <a href="http://docs.mongodb.org/manual/reference/method/cursor.hint/#behavior">mongoDB -- cursor.hint()</a>
     */
    public <T extends LeishvlObject> void findActive(final LeishvlCollection<T> collection, final Class<T> type, final int start,
                                                     final int size, final @Nullable Filters filters, final Map<String, Boolean> sorting,
                                                     final @Nullable Map<String, Boolean> projections, final @Nullable MutableLong totalCount,
                                                     final @Nullable List<String> excludedStates, final Handler<AsyncResult<List<T>>> resultHandler) {
        // parse input parameters
        final MutableBoolean isText = new MutableBoolean(false);
        final List<Bson> filterFields = newArrayList();
        if (filters != null && filters.getFilters() != null) {
            for (final Filter filter : filters.getFilters()) {
                final String value = trimToEmpty(filter.getValue());
                switch (filter.getType()) {
                    case FILTER_NOT:
                        throw new UnsupportedOperationException("The filter 'NOT' is currently unsupported.");
                    case FILTER_REGEX:
                        filterFields.add(regex(filter.getFieldName(), value));
                        break;
                    case FILTER_TEXT:
                        filterFields.add(text(value));
                        isText.setTrue();
                        break;
                    case FILTER_COMPARE:
                    default:
                        if (value.startsWith("<>")) {
                            filterFields.add(ne(filter.getFieldName(), value.substring(2).trim()));
                        } else if (value.startsWith(">=")) {
                            filterFields.add(gte(filter.getFieldName(), value.substring(2).trim()));
                        } else if (value.startsWith(">")) {
                            filterFields.add(gt(filter.getFieldName(), value.substring(1).trim()));
                        } else if (value.startsWith("<=")) {
                            filterFields.add(lte(filter.getFieldName(), value.substring(2).trim()));
                        } else if (value.startsWith("<")) {
                            filterFields.add(lt(filter.getFieldName(), value.substring(1).trim()));
                        } else if (value.startsWith("=")) {
                            filterFields.add(eq(filter.getFieldName(), value.substring(1).trim()));
                        } else {
                            throw new UnsupportedOperationException("The expression '" + value + "' is not a recognized filter.");
                        }
                        break;
                }
            }
        }
        final List<Bson> filterList = newArrayList(not(new BsonDocument(isText.isFalse() ? LEISHVL_SPARSE_IS_ACTIVE_FIELD : LEISHVL_DENSE_IS_ACTIVE_FIELD, BsonNull.VALUE)));
        if (excludedStates != null && !excludedStates.isEmpty()) filterList.add(not(matchStates(excludedStates)));
        if (!filterFields.isEmpty()) filterList.add(LOGICAL_AND.equals(ofNullable(filters).orElse(Filters.builder().build()).getType()) ? and(filterFields) : or(filterFields));
        final List<Bson> projectionFields = projections != null
                ? projections.entrySet().stream().map(p -> p.getValue() ? include(p.getKey()) : exclude(p.getKey())).collect(Collectors.toList())
                : newArrayList();
        final List<Bson> sortFields = sorting != null
                ? sorting.entrySet().stream().map(s -> s.getValue() ? descending(s.getKey()) : ascending(s.getKey())).collect(Collectors.toList())
                : newArrayList();
        // prepare to operate on the collection
        final MongoCollection<Document> dbcol = getCollection(collection);
        final List<T> page = newArrayList();
        // get total number of records
        totalCount(dbcol, true, excludedStates, countResult -> {
            if (countResult.succeeded()) {
                if (totalCount != null) totalCount.setValue(countResult.result());
                // find records and populate the page
                if (ofNullable(countResult.result()).orElse(0l) > 0l) {
                    vertx.runOnContext(v -> {
                        final FindIterable<Document> iterable = dbcol
                                .find(and(filterList))
                                .modifiers(isText.isFalse() ? new BsonDocument("$hint", IS_ACTIVE_SPARSE_HINT) : new BsonDocument())
                                .projection(fields(projectionFields))
                                .sort(orderBy(sortFields))
                                .skip(start)
                                .limit(size);
                        iterable.forEach((doc) -> {
                            try {
                                page.add(parseDocument(doc, type));
                            } catch (Exception e) {
                                LOGGER.error("Failed to parse result, [collection='" + collection.getCollection() + "', objectId='"
                                        + doc.get("_id", ObjectId.class).toHexString() + "']", e);
                            }
                        }, (findResult, findError) -> {
                            if (findError == null) resultHandler.handle(succeededFuture(page));
                            else resultHandler.handle(failedFuture(findError));
                        });
                    });
                } else resultHandler.handle(succeededFuture(page));
            } else resultHandler.handle(failedFuture(countResult.cause()));
        });
    }

    public <T extends LeishvlObject> void findVersions(final LeishvlObject obj, final Class<T> type, final Handler<AsyncResult<List<LeishvlObject>>> resultHandler) {
        checkArgument(obj != null && obj.getClass().isAssignableFrom(type), "Uninitialized or invalid object type.");
        checkArgument(isNotBlank(obj.getLeishvlId()), "Uninitialized or invalid primary key value.");
        final MongoCollection<Document> dbcol = getCollection(obj);
        final List<LeishvlObject> versions = newArrayList();
        final FindIterable<Document> iterable = dbcol.find(matchGuid(obj.getLeishvlId())).sort(descending(LEISHVL_LAST_MODIFIED_FIELD));
        iterable.forEach((Document doc) -> {
            try {
                versions.add(parseDocument(doc, type));
            } catch (Exception e) {
                LOGGER.error("Failed to parse result, [collection='" + obj.getCollection() + "', objectId='"
                        + doc.get("_id", ObjectId.class).toHexString() + "']", e);
            }
        }, convertCallback(resultHandler, result -> versions));
    }

    public <T extends LeishvlObject> void delete(final T obj, final boolean includeVersions, final boolean deleteReferences,
                                                 final Handler<AsyncResult<Boolean>> resultHandler) {
        checkArgument(obj != null, "Uninitialized or invalid object.");
        checkArgument(isNotBlank(obj.getLeishvlId()), "Uninitialized or invalid primary key value.");
        final MutableBoolean deleted = new MutableBoolean(false);
        final MongoCollection<Document> dbcol = getCollection(obj);
        // find and delete records from the database
        final Bson filter = (includeVersions ? matchGuid(obj) : matchActive2(obj));
        dbcol.deleteMany(filter, (delResult, delError) -> {
            if (delError == null) {
                deleted.setValue(delResult.getDeletedCount() > 0l);
                // cascade delete
                if (deleteReferences && !ofNullable(obj.getReferences()).orElse(Collections.<String, List<String>>emptyMap()).isEmpty()) {
                    vertx.eventBus().send("mongodb.delete.references", obj.getReferences());
                }
                // set the new active version to the latest modified record
                if (deleted.isTrue() && !includeVersions) {
                    vertx.runOnContext(v -> activateLastModified(getCollection(obj), obj.getLeishvlId(), activateResult -> {
                        if (activateResult.succeeded()) {
                            if (LOGGER.isTraceEnabled()) {
                                final Document activeDoc = activateResult.result();
                                if (activeDoc != null)
                                    LOGGER.trace("The new active version is '" + activeDoc.getString(LEISHVL_VERSION_FIELD) + "' [collection= "
                                            + obj.getCollection() + ", lvlId=" + obj.getLeishvlId() + "]");
                                else LOGGER.trace("No record was selected as the new active version [collection= "
                                        + obj.getCollection() + ", lvlId=" + obj.getLeishvlId() + "]");
                            }
                            resultHandler.handle(succeededFuture(deleted.getValue()));
                        } else resultHandler.handle(failedFuture(activateResult.cause()));
                    }));
                } else resultHandler.handle(succeededFuture(deleted.getValue()));
            } else resultHandler.handle(failedFuture(delError));
        });
    }

    private void deleteReferences(final Map<String, List<String>> references) {
        // TODO : if (references != null) then retrieve object from database and invoke delete
        // TODO : someone has to subscribe to this event
    }

    /**
     * Counts the total number of active elements in the specified collection.
     */
    public <T extends LeishvlObject> void totalCount(final LeishvlCollection<T> collection, final @Nullable List<String> excludedStates,
                                                     final Handler<AsyncResult<Long>> resultHandler) {
        totalCount(getCollection(collection), true, excludedStates, resultHandler);
    }

    private void totalCount(final MongoCollection<Document> dbcol, final boolean onlyActive,
                            final @Nullable List<String> excludedStates, final Handler<AsyncResult<Long>> resultHandler) {
        requireNonNull(resultHandler, "Result handler expected.");
        final Bson activeFilter = (onlyActive ? not(new BsonDocument(LEISHVL_SPARSE_IS_ACTIVE_FIELD, BsonNull.VALUE)) : null);
        final Bson statesFilter = (excludedStates != null && !excludedStates.isEmpty()) ? not(matchStates(excludedStates)) : null;
        final Bson filter = (activeFilter != null && statesFilter != null) ? and(activeFilter, statesFilter)
                : (activeFilter != null ? activeFilter : (statesFilter != null ? statesFilter : new BsonDocument()));
        final CountOptions options = new CountOptions().hint(IS_ACTIVE_SPARSE_HINT);
        dbcol.count(filter, options, wrapCallback(resultHandler));
    }

    /**
     * Fetches the location of the active elements from the database, returning a collection of GeoJSON points to the caller. The points
     * are annotated with the GUID of the corresponding element.
     */
    public <T extends LeishvlObject> void fetchNear(final LeishvlCollection<T> collection, final Class<T> type, final double longitude, final double latitude,
                                                    final double minDistance, final double maxDistance, final @Nullable List<String> excludedStates, final Handler<AsyncResult<FeatureCollection>> resultHandler) {
        final FeatureCollection features = featureCollectionWGS84();
        final MongoCollection<Document> dbcol = getCollection(collection);
        // prepare query
        final List<Document> statements = newArrayList(new Document(LEISHVL_DENSE_IS_ACTIVE_FIELD, new BsonDocument("$ne", BsonNull.VALUE)));
        if ((excludedStates != null && !excludedStates.isEmpty())) statements.add(new Document(LEISHVL_STATE_FIELD,
                new Document("$not", new Document("$in", excludedStates))));
        final Document query = (statements.size() > 1 ? new Document("$and", statements) : statements.get(0));
        // prepare geolocation query
        final Document geoNear = new Document("$geoNear", new Document(ImmutableMap.<String, Object>builder()
                .put("spherical", true)
                .put("num", new BsonInt32(Integer.MAX_VALUE))
                .put("maxDistance", maxDistance)
                .put("query", query)
                .put("near", new Document("type", "Point").append("coordinates", new BsonArray(newArrayList(new BsonDouble(longitude), new BsonDouble(latitude)))))
                .put("distanceField", "_dist.calculated")
                .build()));

        // TODO
        System.err.println("\n\n >> GEONEAR: " + geoNear.toJson() + "\n");
        // TODO

        final Bson project = new SimplePipelineStage("$project", fields(include(LEISHVL_ID_FIELD, LEISHVL_LOCATION_FIELD, "_dist")));
        final Bson match = new SimplePipelineStage("$match", gte("_dist.calculated", new BsonDouble(minDistance)));
        final AggregateIterable<Document> iterable = dbcol.aggregate(newArrayList(geoNear, project, match));
        iterable.forEach((doc) -> {
            try {
                final T obj = parseDocument(doc, type);
                features.add(featureBuilder()
                        .property("name", obj.getLeishvlId())
                        .geometry(obj.getLocation())
                        .build());
            } catch (Exception e) {
                LOGGER.error("Failed to parse result, [collection='" + collection.getCollection() + "', objectId='"
                        + doc.get("_id", ObjectId.class).toHexString() + "'].", e);
            }
        }, convertCallback(resultHandler, result -> features));
    }

    /**
     * Fetches the location of the active elements from the database, returning a collection of GeoJSON points to the caller. The points are
     * annotated with the GUID of the corresponding element.
     */
    public <T extends LeishvlObject> void fetchWithin(final LeishvlCollection<T> collection, final Class<T> type, final Polygon polygon,
                                                      final @Nullable List<String> excludedStates, final Handler<AsyncResult<FeatureCollection>> resultHandler) {
        checkArgument(polygon != null, "Uninitialized polygon.");
        String payload;
        try {
            payload = JSON_MAPPER.writeValueAsString(polygon);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse polygon.");
        }
        final FeatureCollection features = featureCollectionWGS84();
        final MongoCollection<Document> dbcol = getCollection(collection);
        final Document geoWithin = new Document(LEISHVL_LOCATION_FIELD, new Document(ImmutableMap.<String, Object>of("$geoWithin",
                new Document("$geometry", Document.parse(payload)))));
        final Bson activeFilter = not(new BsonDocument(LEISHVL_SPARSE_IS_ACTIVE_FIELD, BsonNull.VALUE));
        final Bson statesFilter = (excludedStates != null && !excludedStates.isEmpty()) ? not(matchStates(excludedStates)) : new BsonDocument();
        final FindIterable<Document> iterable = dbcol
                .find(and(geoWithin, activeFilter, statesFilter))
                .modifiers(new BsonDocument("$hint", IS_ACTIVE_SPARSE_HINT))
                .projection(fields(include(LEISHVL_ID_FIELD, LEISHVL_LOCATION_FIELD)));
        iterable.forEach((doc) -> {
            try {
                final T obj = parseDocument(doc, type);
                features.add(featureBuilder()
                        .property("name", obj.getLeishvlId())
                        .geometry(obj.getLocation())
                        .build());
            } catch (Exception e) {
                LOGGER.error("Failed to parse result, [collection='" + collection.getCollection() + "', objectId='"
                        + doc.get("_id", ObjectId.class).toHexString() + "'].", e);
            }
        }, convertCallback(resultHandler, result -> features));
    }

    /**
     * Searches for text elements within a collection.
     */
    public <T extends LeishvlObject> void typeahead(final LeishvlCollection<T> collection, final Class<T> type, final String field, final String query,
                                                    final int size, final @Nullable List<String> excludedStates, final Handler<AsyncResult<List<String>>> resultHandler) {
        String field2, query2;
        checkArgument(isNotBlank(field2 = trimToNull(field)), "Uninitialized or invalid field.");
        checkArgument(isNotBlank(query2 = trimToNull(query)), "Uninitialized or invalid query.");
        final MongoCollection<Document> dbcol = getCollection(collection);
        final int size2 = size > 0 ? size : TYPEAHEAD_MAX_ITEMS;
        final List<String> values = newArrayList();
        final Bson statesFilter = (excludedStates != null && !excludedStates.isEmpty()) ? not(matchStates(excludedStates)) : null;
        final Bson typeaheadFilter = regex(field2, query2, "i");
        final Bson filter = (statesFilter != null ? and(typeaheadFilter, statesFilter) : typeaheadFilter);
        final FindIterable<Document> iterable = dbcol
                .find(filter)
                .modifiers(new BsonDocument("$hint", IS_ACTIVE_SPARSE_HINT))
                .projection(fields(include(field2)))
                .sort(orderBy(ascending(field2)))
                .limit(size2);
        final String _field2 = field2;
        iterable.forEach((doc) -> {
            try {
                T obj = parseDocument(doc, type);
                final Object value = getSimpleProperty(obj, _field2);
                if (value != null) values.add(value.toString());
            } catch (Exception e) {
                LOGGER.error("Failed to parse result, [collection='" + collection.getCollection() + "', objectId='"
                        + doc.get("_id", ObjectId.class).toHexString() + "'].", e);
            }
        }, convertCallback(resultHandler, result -> values));
    }

    /**
     * Collects statistics about the specified collection.
     * @param collection - collection from which the statistics are collected
     * @param resultHandler - a handler which result indicates that the operation is successfully completed, or an exception when the method fails
     */
    public <T extends LeishvlObject> void stats(final LeishvlCollection<T> collection, final Handler<AsyncResult<MongoCollectionStats>> resultHandler) {
        final MongoCollection<Document> dbcol = getCollection(collection);
        final MongoCollectionStats stats = new MongoCollectionStats(collection.getCollection());
        // get indexes
        dbcol.listIndexes().forEach(document -> stats.getIndexes().add(document.toJson()), (idxResult, idxError) -> {
            if (idxError == null) {
                // count elements
                vertx.runOnContext(v -> dbcol.count((countResult, countError) -> {
                    if (countError == null) {
                        stats.setCount(countResult);
                        resultHandler.handle(succeededFuture(stats));
                    } else resultHandler.handle(failedFuture(countError));
                }));
            } else resultHandler.handle(failedFuture(idxError));
        });
    }

    public <T extends LeishvlObject> void drop(final LeishvlCollection<T> collection, final Handler<AsyncResult<Void>> resultHandler) {
        final MongoCollection<Document> dbcol = getCollection(collection);
        dbcol.drop(convertCallback(resultHandler, result -> {
            collection.getConfigurer().unconfigure();
            return null;
        }));
    }

	/* Helper methods */

    private Bson matchGuid(final LeishvlObject obj) {
        return matchGuid(obj.getLeishvlId());
    }

    private Bson matchGuid(final String lvlId) {
        return eq(LEISHVL_ID_FIELD, lvlId);
    }

    private Bson matchVersion(final String version) {
        return eq(LEISHVL_VERSION_FIELD, version);
    }

    private Bson matchActive(final LeishvlObject obj) {
        return eq(LEISHVL_SPARSE_IS_ACTIVE_FIELD, obj.getLeishvlId());
    }

    private Bson matchActive2(final LeishvlObject obj) {
        return eq(LEISHVL_DENSE_IS_ACTIVE_FIELD, obj.getLeishvlId());
    }

    private Bson matchStates(final List<String> states) {
        return in(LEISHVL_STATE_FIELD, states);
    }

    private MongoCollection<Document> getCollection(final LeishvlObject obj) {
        requireNonNull(obj, "Uninitialized object.");
        vertx.executeBlocking(future -> {
            obj.getConfigurer().prepareCollection(this);
            future.complete();
        }, res -> LOGGER.info("Configurer successfully executed [collection=" + obj.getCollection() + "]."));
        return holder.db.getCollection(obj.getCollection());
    }

    private <T extends LeishvlObject> MongoCollection<Document> getCollection(final LeishvlCollection<T> collection) {
        requireNonNull(collection, "Uninitialized collection.");
        checkArgument(isNotBlank(collection.getCollection()), "Uninitialized or invalid collection.");
        vertx.executeBlocking(future -> {
            collection.getConfigurer().prepareCollection(this);
            future.complete();
        }, res -> LOGGER.info("Configurer successfully executed [collection=" + collection.getCollection() + "]."));
        return holder.db.getCollection(collection.getCollection());
    }

    private Document parseObject(final LeishvlObject obj, final boolean overrideActive) {
        checkArgument(obj != null, "Uninitialized object.");
        checkArgument(isNotBlank(obj.getLeishvlId()), "Uninitialized or invalid primary key value.");
        if (overrideActive) {
            obj.setIsActive(obj.getLeishvlId());
            obj.setIsActive2(obj.getLeishvlId());
        }
        final Document doc = Document.parse(obj.toJson());
        doc.remove(LEISHVL_LAST_MODIFIED_FIELD);
        if (!overrideActive) {
            doc.remove(LEISHVL_SPARSE_IS_ACTIVE_FIELD);
            doc.remove(LEISHVL_DENSE_IS_ACTIVE_FIELD);
            doc.remove(LEISHVL_VERSION_FIELD);
        }
        return doc;
    }

    private static <T extends LeishvlObject> T parseDocument(final Document doc, final Class<T> type) throws IOException {
        final T obj = JSON_MAPPER_COPY.readValue(doc.toJson(), type);
        obj.setDbId(doc.get("_id", ObjectId.class).toHexString());
        return obj;
    }

    private static final DeserializationProblemHandler DOC_DESERIALIZATION_PROBLEM_HANDLER = new DeserializationProblemHandler() {
        public boolean handleUnknownProperty(final DeserializationContext ctxt, final JsonParser jp, final JsonDeserializer<?> deserializer,
                                             final Object beanOrClass, final String propertyName) throws IOException {
            if ("_id".equals(propertyName) || "_dist".equals(propertyName)) {
                LOGGER.trace("Skipping auxiliary property '" + propertyName + ":" + jp.getCurrentToken().toString() + "', in class: "
                        + beanOrClass.getClass().getCanonicalName());
                jp.skipChildren();
                return true;
            }
            return false;
        }
    };

    private static final SimpleModule JACKSON_MODULE = new SimpleModule("LeishVLModule", new Version(0, 3, 0, null, "io.leishvl", "leishvl-storage")).
            addDeserializer(Date.class, new MongoDateDeserializer());

    private static final ObjectMapper JSON_MAPPER_COPY = JSON_MAPPER.copy().registerModule(JACKSON_MODULE).addHandler(DOC_DESERIALIZATION_PROBLEM_HANDLER);

    private static final BsonDocument IS_ACTIVE_SPARSE_HINT = new BsonDocument(LEISHVL_SPARSE_IS_ACTIVE_FIELD, new BsonInt32(1));
    private static final Bson LAST_MODIFIED_SORT_DESC = orderBy(descending(LEISHVL_LAST_MODIFIED_FIELD));

    private static class SimplePipelineStage implements Bson {
        private final String name;
        private final Bson value;

        public SimplePipelineStage(final String name, final Bson value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public <TDocument> BsonDocument toBsonDocument(final Class<TDocument> documentClass, final CodecRegistry codecRegistry) {
            return new BsonDocument(name, value.toBsonDocument(documentClass, codecRegistry));
        }
    }

    private <T, R> SingleResultCallback<T> convertCallback(final Handler<AsyncResult<R>> resultHandler, final Function<T, R> converter) {
        return (result, error) -> vertx.runOnContext(v -> {
            if (error != null) {
                resultHandler.handle(failedFuture(error));
            } else {
                R converted = null;
                try {
                    converted = converter.apply(result);
                    resultHandler.handle(succeededFuture(converted));
                } catch (Exception e) {
                    resultHandler.handle(failedFuture(e));
                }
            }
        });
    }

    private <T> SingleResultCallback<T> wrapCallback(final Handler<AsyncResult<T>> resultHandler) {
        return (result, error) -> vertx.runOnContext(v -> {
            if (error != null) {
                resultHandler.handle(failedFuture(error));
            } else {
                resultHandler.handle(succeededFuture(result));
            }
        });
    }

    private void removeFromMap(final LocalMap<String, MongoHolder> map, final String dbName) {
        synchronized (vertx) {
            map.remove(dbName);
            if (map.isEmpty()) {
                map.close();
            }
        }
    }

    private MongoHolder lookupHolder(final String dbName, final JsonObject config) {
        synchronized (vertx) {
            final LocalMap<String, MongoHolder> map = vertx.sharedData().getLocalMap(DS_LOCAL_MAP_NAME);
            MongoHolder _holder = map.get(dbName);
            if (_holder == null) {
                _holder = new MongoHolder(config, () -> removeFromMap(map, dbName));
                map.put(dbName, _holder);
            } else {
                _holder.incRefCount();
            }
            return _holder;
        }
    }

    private static class MongoHolder implements Shareable {
        MongoClient mongo;
        MongoDatabase db;
        JsonObject config;
        Runnable closeRunner;
        int refCount = 1;

        public MongoHolder(final JsonObject config, final Runnable closeRunner) {
            this.config = config;
            this.closeRunner = closeRunner;
        }

        synchronized MongoClient mongo() {
            if (mongo == null) {
                final MongoConnectorOptionsParser parser = new MongoConnectorOptionsParser(config);
                mongo = MongoClients.create(parser.settings());
                final String dbName = config.getString("db_name");
                db = mongo.getDatabase(dbName);
            }
            return mongo;
        }

        synchronized void incRefCount() {
            refCount++;
        }

        synchronized void close() {
            if (--refCount == 0) {
                if (mongo != null) {
                    try {
                        mongo.close();
                    } finally {
                        mongo = null;
                        db = null;
                    }
                }
                if (closeRunner != null) {
                    try {
                        closeRunner.run();
                    } finally {
                        closeRunner = null;
                    }
                }
            }
        }
    }

}