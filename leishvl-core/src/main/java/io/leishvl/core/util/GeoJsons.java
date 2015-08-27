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

import org.geojson.*;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.math.DoubleMath.fuzzyEquals;
import static io.leishvl.core.geospatial.Wgs84Validator.WGS84_URN_CRS;

/**
 * Utilities to work with GeoJSON objects.
 * @author Erik Torres <ertorser@upv.es>
 */
public final class GeoJsons {

    public static final double TOLERANCE = 0.00000001d;

    public static final BiFunction<Point, Point, Boolean> POINT_FUZZY_EQUALS = (p1, p2) -> {
        if (p1 == p2) return true;
        else if (p1 == null || p2 == null) return false;
        final LngLatAlt c1 = p1.getCoordinates();
        final LngLatAlt c2 = p2.getCoordinates();
        if (c1 == c2) return true;
        else if (c1 == null || c2 == null) return false;
        return fuzzyEquals(c1.getLongitude(), c2.getLongitude(), TOLERANCE)
                && fuzzyEquals(c1.getLatitude(), c2.getLatitude(), TOLERANCE)
                && fuzzyEquals(c1.getAltitude(), c2.getAltitude(), TOLERANCE);
    };

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

    public static LngLatAltBuilder lngLatAltBuilder() { return new LngLatAltBuilder(); }

    public static PointBuilder pointBuilder() { return new PointBuilder(); }

    public static PolygonBuilder polygonBuilder() { return new PolygonBuilder(); }

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

    public static class LngLatAltBuilder {

        private LngLatAlt instance = new LngLatAlt();

        public LngLatAltBuilder coordinates(final double longitude, final double latitude) {
            return longitude(longitude).latitude(latitude);
        }

        public LngLatAltBuilder coordinates(final double longitude, final double latitude, final double altitude) {
            return longitude(longitude).latitude(latitude).altitude(altitude);
        }

        public LngLatAltBuilder longitude(final double longitude) {
            instance.setLongitude(longitude);
            return this;
        }

        public LngLatAltBuilder latitude(final double latitude) {
            instance.setLatitude(latitude);
            return this;
        }

        public LngLatAltBuilder altitude(final double altitude) {
            instance.setAltitude(altitude);
            return this;
        }

        public LngLatAlt build() {
            return instance;
        }

    }

    public static class PointBuilder {

        private Point instance = new Point();

        public PointBuilder crs(final Crs crs) {
            instance.setCrs(crs);
            return this;
        }

        public PointBuilder coordinates(final LngLatAlt coordinates) {
            instance.setCoordinates(coordinates);
            return this;
        }

        public Point build() {
            checkState(instance.getCoordinates() != null, "Coordinates expected.");
            return instance;
        }

    }

    public static class PolygonBuilder {

        private Polygon instance = new Polygon();

        public PolygonBuilder exteriorRing(final LngLatAlt... coordinates) {
            return exteriorRing(newArrayList(coordinates));
        }

        public PolygonBuilder exteriorRing(final List<LngLatAlt> coordinates) {
            instance.getCoordinates().add(0, coordinates);
            return this;
        }

        public PolygonBuilder interiorRing(final LngLatAlt... coordinates) {
            return interiorRing(newArrayList(coordinates));
        }

        public PolygonBuilder interiorRing(final List<LngLatAlt> coordinates) {
            checkState(coordinates != null && !coordinates.isEmpty(), "No exterior ring defined.");
            instance.getCoordinates().add(coordinates);
            return this;
        }

        public Polygon build() {
            final List<List<LngLatAlt>> coordinates = instance.getCoordinates();
            checkState(coordinates != null && !coordinates.isEmpty(), "No exterior ring defined.");
            final List<LngLatAlt> exteriorRing = coordinates.get(0);
            checkState(exteriorRing.size() >= 4, "Exterior ring needs at least four coordinate pairs.");
            checkState(exteriorRing.get(0).equals(exteriorRing.get(exteriorRing.size() - 1)),
                    "The same position must be specified as the first and last coordinates.");
            return instance;
        }

    }

}