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

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.UnsignedLong;
import com.mongodb.client.model.IndexModel;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.hash.Hashing.murmur3_32;
import static io.leishvl.storage.base.LeishvlObject.*;
import static io.leishvl.storage.base.StorageDefaults.STO_OPERATION_TIMEOUT_SECS;
import static java.lang.Math.pow;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Applies configuration to collections.
 * @author Erik Torres <ertorser@upv.es>
 */
public class MongoCollectionConfigurer {

    private static final Logger LOGGER = getLogger(MongoCollectionConfigurer.class);

    private final String collection;
    private final List<IndexModel> indexes = newArrayList();

    private final BooleanSupplier preload;

    private final AtomicBoolean isConfigured = new AtomicBoolean(false);

    public MongoCollectionConfigurer(final String collection, final boolean geoIndex, final List<IndexModel> moreIndexes) {
        this(collection, geoIndex, moreIndexes, null);
    }

    public MongoCollectionConfigurer(final String collection, final boolean geoIndex, final List<IndexModel> moreIndexes,
                                     final BooleanSupplier preload) {
        this.collection = collection;
        // common indexes
        indexes.add(nonUniqueIndexModel(LEISHVL_NAMESPACE_FIELD, false));
        indexes.add(nonUniqueIndexModel(LEISHVL_ID_FIELD, false));
        indexes.add(indexModel(LEISHVL_ID_FIELD, LEISHVL_VERSION_FIELD));
        indexes.add(nonUniqueIndexModel(LEISHVL_LAST_MODIFIED_FIELD, true));
        indexes.add(sparseIndexModelWithUniqueConstraint(LEISHVL_SPARSE_IS_ACTIVE_FIELD, false));
        indexes.add(nonUniqueIndexModel(LEISHVL_DENSE_IS_ACTIVE_FIELD, false));
        indexes.add(nonUniqueIndexModel(LEISHVL_STATE_FIELD, false));
        // geospatial indexes
        if (geoIndex) indexes.add(geospatialIndexModel(LEISHVL_LOCATION_FIELD));
        // other indexes
        if (moreIndexes != null) indexes.addAll(moreIndexes);
        // preload operations
        this.preload = preload;
    }

    public void prepareCollection(final MongoConnector conn) {
        final boolean shouldRun = isConfigured.compareAndSet(false, true);
        if (shouldRun) {
            final List<CompletableFuture<Boolean>> futures = newArrayList();
            // create indexes
            if (indexes != null && !indexes.isEmpty()) {
                final CompletableFuture<Boolean> indexesFuture = new CompletableFuture<>();
                futures.add(indexesFuture);
                conn.createIndexes(collection, indexes, result -> {
                    if (result.succeeded()) {
                        LOGGER.info("Collection indexes were successfully created [collection=" + collection + "].");
                        indexesFuture.complete(true);
                    } else indexesFuture.completeExceptionally(result.cause());
                });
            }
            // run pre-load operations
            if (preload != null) {
                futures.add(CompletableFuture.supplyAsync(() -> preload.getAsBoolean()));
            }
            try {
                CompletableFuture.<Void>allOf(futures.toArray(new CompletableFuture[futures.size()])).get(STO_OPERATION_TIMEOUT_SECS, TimeUnit.SECONDS);
            } catch (InterruptedException | TimeoutException e) {
                throw new IllegalStateException("Given up to configure collection due to interruption or timeout [collection=" + collection + "].");
            } catch (ExecutionException e) {
                throw new IllegalStateException("Failed to configure collection [collection=" + collection + "].", e);
            }
        }
    }

    public void unconfigure() {
        isConfigured.set(false);
    }

    /**
     * Creates a new index model on a field and sorting the elements in ascending order. Indexes created with this method are created
     * in the background and stores unique elements.
     * @param field - field that is used to index the elements
     */
    public static IndexModel indexModel(final String... field) {
        return indexModel(asList(field), true, false);
    }

    /**
     * Creates an index model on a field. Indexes created with this method are created in the background and stores unique elements.
     * @param field - field that is used to index the elements
     * @param descending - sort the elements of the index in descending order
     */
    public static IndexModel indexModel(final String field, final boolean descending) {
        checkArgument(isNotBlank(field), "Uninitialized or invalid field.");
        return indexModel(ImmutableList.of(field), true, descending);
    }

    /**
     * Creates an index model on a set of fields, sorting the elements in ascending order. Indexes created with this method are created
     * in the background and stores unique elements.
     * @param fields - fields that are used to index the elements
     */
    public static IndexModel indexModel(final List<String> fields, final boolean descending) {
        return indexModel(fields, true, descending);
    }

    /**
     * Creates an index model on a field. Indexes created with this method are created in the background and allow storing duplicated elements.
     * @param field - field that is used to index the elements
     * @param descending - sort the elements of the index in descending order
     */
    public static IndexModel nonUniqueIndexModel(final String field, final boolean descending) {
        return indexModel(ImmutableList.of(field), false, descending);
    }

    /**
     * Creates an index model on a set of fields. Indexes created with this method are created in the background and allow storing duplicated elements.
     * @param fields - fields that are used to index the elements
     * @param descending - sort the elements of the index in descending order
     */
    public static IndexModel nonUniqueIndexModel(final List<String> fields, final boolean descending) {
        return indexModel(fields, false, descending);
    }

    /**
     * Creates an index model on a set of fields. Indexes created with this method are created in the background and could stores unique elements.
     * @param fields - fields that are used to index the elements
     * @param descending - sort the elements of the index in descending order
     */
    public static IndexModel indexModel(final List<String> fields, final boolean unique, final boolean descending) {
        checkArgument(fields != null && !fields.isEmpty(), "Uninitialized or invalid fields.");
        return new IndexModel(new Document(fields.stream().collect(toMap(identity(), (input) -> {
            return (Integer)(descending ? -1 : 1);
        }))), new IndexOptions().unique(unique).background(true));
    }

    /**
     * Creates a new geospatial index model. Indexes created with this method are created in the background.
     * @param field - field that is used to index the elements
     */
    public static IndexModel geospatialIndexModel(final String field) {
        checkArgument(isNotBlank(field), "Uninitialized or invalid field.");
        return new IndexModel(new Document(field, "2dsphere"), new IndexOptions().background(true));
    }

    /**
     * Creates a text index model. Text indexes created with this method are created in the background and uses English as the default language.
     * @param fields - fields that are used to index the elements
     * @param prefix - prefix for the index name
     */
    public static IndexModel textIndexModel(final List<String> fields, final String prefix) {
        checkArgument(fields != null && !fields.isEmpty(), "Uninitialized or invalid fields.");
        checkArgument(isNotBlank(prefix), "Uninitialized or invalid prefix.");
        return new IndexModel(new Document(fields.stream().collect(toMap(identity(), (input) -> {
            return "text";
        }))), new IndexOptions().background(true).languageOverride("english").name(prefix + ".text_idx"));
    }

    /**
     * Creates a sparse, non unique, index model. Indexes created with this method are created in the background. <strong>Note:</strong> Do NOT use
     * compound indexes to create an sparse index, since the results are unexpected.
     * @param field - field that is used to index the elements
     * @param descending - sort the elements of the index in descending order
     * @see <a href="http://docs.mongodb.org/manual/core/index-sparse/">MongoDB: Sparse Indexes</a>
     */
    public static IndexModel sparseIndexModel(final String field, final boolean descending) {
        checkArgument(isNotBlank(field), "Uninitialized or invalid field.");
        return new IndexModel(new Document(field, descending ? -1 : 1), new IndexOptions().background(true).sparse(true));
    }

    /**
     * Creates a sparse index model with a unique constraint on a field. Indexes created with this method are created in the background.
     * <strong>Note:</strong> Do NOT use compound indexes to create an sparse index, since the results are unexpected.
     * @param field - field that is used to index the elements
     * @param descending - (optional) sort the elements of the index in descending order
     * @see <a href="http://docs.mongodb.org/manual/core/index-sparse/">MongoDB: Sparse Indexes</a>
     */
    public static IndexModel sparseIndexModelWithUniqueConstraint(final String field, final boolean descending) {
        checkArgument(isNotBlank(field), "Uninitialized or invalid field.");
        return new IndexModel(new Document(field, descending ? -1 : 1), new IndexOptions().background(true).sparse(true).unique(true));
    }

    /**
     * Computes a hash and projects it to a fixed number of buckets.
     * @param e - the input for which the hash will be computed
     * @param buckets - desired number of buckets
     * @return the bucket where the input is assigned.
     * @see <a href="http://stats.stackexchange.com/a/70884">How to uniformly project a hash to a fixed number of buckets</a>
     */
    public static int hash2bucket(final String e, final int buckets) {
        checkArgument(isNotBlank(e), "Uninitialized or invalid string.");
        checkArgument(buckets > 0l, "Invalid number of buckets.");
        // compute 32-bit hash using MurmurHash3 function
        final int hash = murmur3_32(1301081).newHasher().putBytes(e.getBytes()).hash().asInt();
        // convert computed hash to unsigned integer and cast to double
        final double unsignedHash = UnsignedLong.valueOf(hash & 0xffffffffL).doubleValue();
        final double projection = unsignedHash / pow(2.0d, 32.0d);
        for (int i = 0; i < buckets; i++) {
            if (((i / (double)buckets) <= projection) && ((i + 1) / (double)buckets > projection)) return i + 1;
        }
        return buckets;
    }

}