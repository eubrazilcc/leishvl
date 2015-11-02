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

package io.leishvl.geospatial;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Range.closed;

import com.google.common.collect.Range;

/**
 * Validates World Geodetic System 84 (WGS84) coordinates.
 * @author Erik Torres <ertorser@upv.es>
 * @see <a href="http://spatialreference.org/ref/epsg/4326/">epsg projection 4326 - wgs 84</a>
 * @see <a href="http://wiki.geojson.org/GeoJSON_draft_version_5">GeoJSON draft version 5</a>
 */
public final class Wgs84Validator {

	public static final String WGS84_LEGACY_CRS = "EPSG:3857";
	public static final String WGS84_URN_CRS = "urn:ogc:def:crs:OGC:1.3:CRS84";

	private static final Range<Double> LONGITUDE_RANGE = closed(-180.0d, 180.0d);
	private static final Range<Double> LATITUDE_RANGE = closed(-90.0d, 90.0d);

	public static final double checkLongitude(final double longitude) {
		checkArgument(LONGITUDE_RANGE.contains(longitude));
		return longitude;
	}

	public static final double checkLatitude(final double latitude) {
		checkArgument(LATITUDE_RANGE.contains(latitude));
		return latitude;
	}

}