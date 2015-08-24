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

package io.leishvl.storage.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableList;
import io.leishvl.storage.Link;
import io.leishvl.storage.Linkable;
import io.leishvl.storage.mongodb.MongoCollectionConfigurer;
import io.leishvl.storage.mongodb.jackson.MongoJsonOptions;
import io.leishvl.storage.prov.jackson.ProvDocumentDeserializer;
import io.leishvl.storage.prov.jackson.ProvDocumentSerializer;
import io.leishvl.storage.security.User;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.SuppressPropertiesBeanIntrospector;
import org.geojson.Point;
import org.openprovenance.prov.model.Document;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.collect.Maps.newHashMap;
import static io.leishvl.core.util.NamingUtils.urlEncodeUtf8;
import static io.leishvl.storage.base.ObjectState.*;
import static io.leishvl.storage.mongodb.jackson.MongoJsonMapper.objectToJson;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;

/**
 * Classes should extend this class to support common features of the LeishVL, such as geolocalization (an optional GeoJSON point can be
 * included to georeference the object) and provenance (implementing W3C PROV). In addition, a database identifier is provided, as well 
 * as a global LeishVL identifier, unique for all instances of this application.
 * @author Erik Torres <ertorser@upv.es>
 * @see <a href="http://geojson.org/">GeoJSON open standard format for encoding geographic data structures</a>
 * @see <a href="http://www.w3.org/TR/prov-overview/">PROV-Overview: An Overview of the PROV Family of Documents</a>
 */
public abstract class LeishvlObject implements Linkable {

    public static final String LEISHVL_NAMESPACE_FIELD = "namespace";
    public static final String LEISHVL_ID_FIELD = "leishvlId";
    public static final String LEISHVL_VERSION_FIELD = "version";
    public static final String LEISHVL_LOCATION_FIELD = "location";
    public static final String LEISHVL_STATE_FIELD = "state";
    public static final String LEISHVL_LAST_MODIFIED_FIELD = "lastModified";
    public static final String LEISHVL_SPARSE_IS_ACTIVE_FIELD = "isActive";
    public static final String LEISHVL_DENSE_IS_ACTIVE_FIELD = "isActive2";

    @JsonIgnore
    protected Vertx vertx;
    @JsonIgnore
    protected JsonObject config;

    @JsonIgnore
    protected final Logger logger;
    @JsonIgnore
    private final String collection;
    @JsonIgnore
    private final MongoCollectionConfigurer configurer;
    @JsonIgnore
    private String dbId; // database identifier

    private Optional<String> namespace = empty(); // (optional) namespace
    private String leishvlId; // LeishVL unique identifier
    private String version; // version identifier

    private Optional<Point> location = empty(); // (optional) geospatial location
    @JsonSerialize(using = ProvDocumentSerializer.class) @JsonDeserialize(using = ProvDocumentDeserializer.class)
    private Optional<Document> provenance = empty(); // (optional) provenance
    private Optional<ObjectState> state = empty(); // (optional) state

    private Date lastModified; // last modification date
    private String isActive; // set to the GUID value in the active version (in most cases, the latest version)
    private String isActive2; // a copy of the field with an alternative index
    private Map<String, List<String>> references; // references to other documents

    @JsonIgnore
    protected String urlSafeNamespace;
    @JsonIgnore
    protected String urlSafeLeishvlId;

    @JsonIgnore
    private ObjectStateHandler<LeishvlObject> stateHandler;

    private static final List<String> FIELDS_TO_SUPPRESS = ImmutableList.of("logger", "collection", "configurer", "urlSafeNamespace",
            "urlSafeLvlId", "stateHandler");

    public LeishvlObject(final String collection, final MongoCollectionConfigurer configurer, final Logger logger) {
        this.collection = collection;
        this.configurer = configurer;
        this.logger = logger;
        this.references = newHashMap();
        this.stateHandler = new DraftStateHandler<>();
    }

    public void setVertx(final Vertx vertx) {
        this.vertx = vertx;
        this.stateHandler.setVertx(this.vertx);
    }

    public void setConfig(final JsonObject config) {
        this.config = config;
        this.stateHandler.setConfig(this.config);
    }

    public String getCollection() {
        return collection;
    }

    public MongoCollectionConfigurer getConfigurer() {
        return configurer;
    }

    public String getDbId() {
        return dbId;
    }

    public void setDbId(final String dbId) {
        this.dbId = dbId;
    }

    public @Nullable String getNamespace() {
        return namespace.orElse(null);
    }

    public void setNamespace(final @Nullable String namespace) {
        this.namespace = ofNullable(trimToNull(namespace));
        setUrlSafeNamespace(urlEncodeUtf8(this.namespace.orElse("")));
    }

    public String getLeishvlId() {
        return leishvlId;
    }

    public void setLeishvlId(final String leishvlId) {
        this.leishvlId = trimToEmpty(leishvlId);
        setUrlSafeLeishvlId(urlEncodeUtf8(this.leishvlId));
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = trimToEmpty(version);
    }

    public @Nullable Point getLocation() {
        return location.orElse(null);
    }

    public void setLocation(final @Nullable Point location) {
        this.location = ofNullable(location);
    }

    public @Nullable Document getProvenance() {
        return provenance.orElse(null);
    }

    public void setProvenance(final @Nullable Document provenance) {
        this.provenance = ofNullable(provenance);
    }

    public ObjectState getState() {
        return state.orElse(null);
    }

    public void setState(final @Nullable ObjectState state) {
        this.state = ofNullable(state);
        switch (this.state.orElse(DRAFT)) {
            case RELEASE:
                stateHandler = new ReleaseStateHandler<>();
                break;
            case OBSOLETE:
                stateHandler = new ObsoleteStateHandler<>();
                break;
            case DRAFT:
            default:
                stateHandler = new DraftStateHandler<>();
                break;
        }
        stateHandler.setVertx(vertx);
        stateHandler.setConfig(config);
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(final Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(final String isActive) {
        this.isActive = isActive;
    }

    public String getIsActive2() {
        return isActive2;
    }

    public void setIsActive2(final String isActive2) {
        this.isActive2 = isActive2;
    }

    public Map<String, List<String>> getReferences() {
        return references;
    }

    public void setReferences(final Map<String, List<String>> references) {
        if (references != null) {
            this.references = newHashMap(references);
        } else {
            this.references.clear();
        }
    }

    public String getUrlSafeNamespace() {
        return urlSafeNamespace;
    }

    public void setUrlSafeNamespace(final String urlSafeNamespace) {
        this.urlSafeNamespace = urlSafeNamespace;
    }

    public String getUrlSafeLeishvlId() {
        return urlSafeLeishvlId;
    }

    public void setUrlSafeLeishvlId(final String urlSafeLeishvlId) {
        this.urlSafeLeishvlId = urlSafeLeishvlId;
    }

    /**
     * Inserts or update an element in the database.
     * @param resultHandler - a handler which result indicates that the operation is successfully completed, or an exception when the method fails
     * @param options - operation options
     */
    public void save(final Handler<AsyncResult<Void>> resultHandler, final SaveOptions... options) {
        save(null, resultHandler, options);
    }

    /**
     * Inserts or update an element in the database.
     * @param user - identity of the user who is responsible for editing this object
     * @param resultHandler - a handler which result indicates that the operation is successfully completed, or an exception when the method fails
     * @param options - operation options
     */
    public void save(final @Nullable User user, final Handler<AsyncResult<Void>> resultHandler, final SaveOptions... options) {
        stateHandler.save(this, user, resultHandler, options);
    }

    /**
     * Gets from the database the element that has the key that coincides with this object.
     * @param resultHandler - a handler which result indicates that the operation is successfully completed, or an exception when the method fails
     * @param options - operation options
     */
    public void fetch(final Handler<AsyncResult<Void>> resultHandler, final FetchOptions... options) {
        stateHandler.fetch(this, resultHandler, options);
    }

    /**
     * Removes the element from the database.
     * @param resultHandler - a handler which result indicates that the operation is successfully completed, or an exception when the method fails
     * @param options - operation options
     */
    public void delete(final Handler<AsyncResult<Boolean>> resultHandler, final DeleteOptions... options) {
        stateHandler.delete(this, resultHandler, options);
    }

    public void versions(final Handler<AsyncResult<List<LeishvlObject>>> resultHandler) {
        stateHandler.versions(this, resultHandler);
    }

    /**
     * Returns a String containing the attributes of each element loaded in the current view.
     * @param options - JSON parser options
     * @return a String containing the attributes of each element loaded in the current view
     */
    public String toJson(final MongoJsonOptions... options) {
        String payload = "";
        try {
            payload = objectToJson(this, options);
        } catch (final JsonProcessingException e) {
            logger.error("Failed to export object to JSON", e);
        }
        return payload;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof LeishvlObject)) {
            return false;
        }
        final LeishvlObject other = LeishvlObject.class.cast(obj);
        return Objects.equals(namespace.orElse(null), other.namespace.orElse(null))
                && Objects.equals(leishvlId, other.leishvlId)
                && Objects.equals(version, other.version)
                && Objects.equals(location.orElse(null), other.location.orElse(null))
                && (provenance.isPresent() == other.provenance.isPresent())
                && Objects.equals(state.orElse(null), other.state.orElse(null))
                && Objects.equals(lastModified, other.lastModified)
                && Objects.equals(isActive, other.isActive)
                && Objects.equals(isActive2, other.isActive2)
                && Objects.equals(references, other.references);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dbId, namespace, leishvlId, version, location, provenance, state, lastModified, isActive, isActive2, references);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("dbId", dbId)
                .add("namespace", namespace.orElse(null))
                .add("leishvlId", leishvlId)
                .add("version", version)
                .add("location", location.orElse(null))
                .add("provenance", "<<not displayed>>")
                .add("state", state.orElse(DRAFT))
                .add("lastModified", lastModified)
                .add("isActive", isActive)
                .add("isActive2", isActive2)
                .add("references", references)
                .toString();
    }

	/* Utility methods */

    public static void copyProperties(final LeishvlObject orig, final LeishvlObject dest) throws IllegalAccessException, InvocationTargetException {
        final PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        propertyUtilsBean.addBeanIntrospector(new SuppressPropertiesBeanIntrospector(FIELDS_TO_SUPPRESS));
        final BeanUtilsBean beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean(), propertyUtilsBean);
        beanUtilsBean.copyProperties(dest, orig);
    }

    public static String randomVersion() {
        return random(8, true, true);
    }

	/* Fluent API */

    /**
     * Sets the status to {@link io.leishvl.storage.base.ObjectState#RELEASE}.
     * @return a reference to this class.
     */
    public LeishvlObject approve() {
        this.setState(RELEASE);
        return this;
    }

    /**
     * Sets the status to {@link io.leishvl.storage.base.ObjectState#OBSOLETE}.
     * @return a reference to this class.
     */
    public LeishvlObject invalidate() {
        this.setState(OBSOLETE);
        return this;
    }

    public abstract static class Builder<T extends LeishvlObject, B extends Builder<T, B>> {

        protected final T instance;
        private B builder;

        public Builder(final T instance) {
            this.instance = instance;
        }

        protected void setBuilder(final B builder) {
            this.builder = builder;
        }

        public B vertx(final Vertx vertx) {
            instance.setVertx(vertx);
            return builder;
        }

        public B config(final JsonObject config) {
            instance.setConfig(config);
            return builder;
        }

        public B links(final Map<String, Link> links) {
            instance.setLinks(links);
            return builder;
        }

        public B dbId(final String dbId) {
            instance.setDbId(dbId);
            return builder;
        }

        public B namespace(final @Nullable String namespace) {
            instance.setNamespace(namespace);
            return builder;
        }

        public B leishvlId(final String leishvlId) {
            instance.setLeishvlId(leishvlId);
            return builder;
        }

        public B location(final @Nullable Point location) {
            instance.setLocation(location);
            return builder;
        }

        public B provenance(final @Nullable Document provenance) {
            instance.setProvenance(provenance);
            return builder;
        }

        public B state(final @Nullable ObjectState state) {
            instance.setState(state);
            return builder;
        }

        public B references(final @Nullable Map<String, List<String>> references) {
            instance.setReferences(references);
            return builder;
        }

    }

}