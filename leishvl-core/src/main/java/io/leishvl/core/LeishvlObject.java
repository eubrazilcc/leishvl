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

package io.leishvl.core;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.collect.Maps.newHashMap;
import static io.leishvl.core.LeishvlObjectState.DRAFT;
import static io.leishvl.core.geospatial.GeoJsons.POINT_FUZZY_EQUALS;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import org.openprovenance.prov.model.Document;
import org.slf4j.Logger;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

/**
 * Classes should extend this class to support common features of the LeishVL, such as geolocalization (an optional GeoJSON point can be
 * included to georeference the object) and provenance (implementing W3C PROV). In addition, a database identifier is provided, as well 
 * as a global LeishVL identifier, unique for all instances of this application.
 * @author Erik Torres <ertorser@upv.es>
 * @see <a href="http://geojson.org/">GeoJSON open standard format for encoding geographic data structures</a>
 * @see <a href="http://www.w3.org/TR/prov-overview/">PROV-Overview: An Overview of the PROV Family of Documents</a>
 */
public abstract class LeishvlObject {

	@Transient
	protected final Logger logger;

	@Id
	private String id;

	private Optional<String> namespace = empty(); // (optional) namespace
	private String leishvlId; // LeishVL unique identifier
	private String version; // version identifier

	private Optional<GeoJsonPoint> location = empty(); // (optional) geospatial location
	private Optional<Document> provenance = empty(); // (optional) provenance
	private Optional<LeishvlObjectState> state = empty(); // (optional) state

	private Date lastModified; // last modification date
	private String isActive; // set to the GUID value in the active version, otherwise contains null
	private Map<String, List<String>> references; // references to other documents

	public LeishvlObject(final Logger logger) {
		this.logger = logger;
		this.references = newHashMap();
	}

	public @Nullable String getNamespace() {
		return namespace.orElse(null);
	}

	public void setNamespace(final @Nullable String namespace) {
		this.namespace = ofNullable(trimToNull(namespace));
	}

	public String getLeishvlId() {
		return leishvlId;
	}

	public void setLeishvlId(final String leishvlId) {
		this.leishvlId = trimToEmpty(leishvlId);
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(final String version) {
		this.version = trimToEmpty(version);
	}

	public @Nullable GeoJsonPoint getLocation() {
		return location.orElse(null);
	}

	public void setLocation(final @Nullable GeoJsonPoint location) {
		this.location = ofNullable(location);
	}

	public @Nullable Document getProvenance() {
		return provenance.orElse(null);
	}

	public void setProvenance(final @Nullable Document provenance) {
		this.provenance = ofNullable(provenance);
	}

	public LeishvlObjectState getState() {
		return state.orElse(null);
	}

	public void setState(final @Nullable LeishvlObjectState state) {
		this.state = ofNullable(state);
		switch (this.state.orElse(DRAFT)) {
		case RELEASE:
			break;
		case OBSOLETE:
			break;
		case DRAFT:
		default:
			break;
		}
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

	@Override
	public boolean equals(final Object obj) {
		if (obj == null || !(obj instanceof LeishvlObject)) {
			return false;
		}
		final LeishvlObject other = LeishvlObject.class.cast(obj);
		return Objects.equals(namespace.orElse(null), other.namespace.orElse(null))
				&& Objects.equals(leishvlId, other.leishvlId)
				&& Objects.equals(version, other.version)
				&& POINT_FUZZY_EQUALS.apply(location.orElse(null), other.location.orElse(null))
				// Objects.equals(location.orElse(null), other.location.orElse(null))
				&& (provenance.isPresent() == other.provenance.isPresent())
				&& Objects.equals(state.orElse(null), other.state.orElse(null))
				&& Objects.equals(lastModified, other.lastModified)
				&& Objects.equals(isActive, other.isActive)
				&& Objects.equals(references, other.references);
	}

	@Override
	public int hashCode() {
		return Objects.hash(namespace, leishvlId, version, location, provenance, state, lastModified, isActive, references);
	}

	@Override
	public String toString() {
		return toStringHelper(this)
				.add("id", id)
				.add("namespace", namespace.orElse(null))
				.add("leishvlId", leishvlId)
				.add("version", version)
				.add("location", location.orElse(null))
				.add("provenance", "<<not displayed>>")
				.add("state", state.orElse(DRAFT))
				.add("lastModified", lastModified)
				.add("isActive", isActive)
				.add("references", references)
				.toString();
	}

	/* Utilities */

	public static String randomVersion() {
		return random(8, true, true);
	}

	/* Fluent API */

	public abstract static class Builder<T extends LeishvlObject, B extends Builder<T, B>> {

		protected final T instance;
		private B builder;

		public Builder(final T instance) {
			this.instance = instance;
		}

		protected void setBuilder(final B builder) {
			this.builder = builder;
		}

		public B namespace(final @Nullable String namespace) {
			instance.setNamespace(namespace);
			return builder;
		}

		public B leishvlId(final String leishvlId) {
			instance.setLeishvlId(leishvlId);
			return builder;
		}

		public B location(final @Nullable GeoJsonPoint location) {
			instance.setLocation(location);
			return builder;
		}

		public B provenance(final @Nullable Document provenance) {
			instance.setProvenance(provenance);
			return builder;
		}

		public B state(final @Nullable LeishvlObjectState state) {
			instance.setState(state);
			return builder;
		}

		public B references(final @Nullable Map<String, List<String>> references) {
			instance.setReferences(references);
			return builder;
		}

	}

}