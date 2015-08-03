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

package io.leishvl.core.util;

import static io.leishvl.core.geospatial.Wgs84Validator.WGS84_URN_CRS;

import java.util.Map;

import org.geojson.Crs;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.LngLatAlt;

/**
 * Utilities to work with GeoJSON objects.
 * @author Erik Torres <ertorser@upv.es>
 */
public final class GeoJsons {

	public static FeatureCollection featureCollectionWGS84() {
		final Crs crs = new Crs();
		crs.getProperties().put("name", WGS84_URN_CRS);
		final FeatureCollection featureCollection = new FeatureCollection();
		featureCollection.setCrs(crs);
		return featureCollection;
	}

	public static String lngLatAlt2Human(final LngLatAlt lngLatAlt) {
		return lngLatAlt.getLatitude() + "\u00b0" + " N " + lngLatAlt.getLongitude() + "\u00b0" + " W";
	}
	
	public static FeatureBuilder featureBuilder() {
		return new FeatureBuilder();
	}

	/* Inner classes */
	
	public static class FeatureBuilder {

		private Feature instance = new Feature();

		public FeatureBuilder geometry(final GeoJsonObject geometry) {
			instance.setGeometry(geometry);
			return this;
		}

		public FeatureBuilder properties(final Map<String, Object> properties) {
			instance.getProperties().putAll(properties);
			return this;
		}

		public FeatureBuilder property(final String key, final Object value) {
			instance.getProperties().put(key, value);
			return this;
		}

		public FeatureBuilder id(final String id) {
			instance.setId(id);
			return this;
		}

		public Feature build() {		
			return instance;
		}

	}

}