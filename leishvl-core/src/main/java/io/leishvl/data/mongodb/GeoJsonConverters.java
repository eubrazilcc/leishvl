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

package io.leishvl.data.mongodb;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * A Spring Data converter that allows using the GeoJSON format to store geolocation coordinates and geometries
 * in MongoDB, thus adhering to the GeoJSON format. This class is needed when Spring Boot is used, since its 
 * autoconfiguration for MongoDB does not support custom conversion out of the box.
 * @author Erik Torres <ertorser@upv.es>
 * @see <a href="https://dzone.com/articles/using-geojson-spring-data">Using GeoJSON With Spring Data for MongoDB and Spring Boot</a>
 */
public final class GeoJsonConverters {

	public static List<Converter<?, ?>> getGeoJsonConvertersToRegister() {
		return asList(GeoJsonDBObjectToPointConverter.INSTANCE,
				GeoJsonDBObjectToPolygonConverter.INSTANCE,
				GeoJsonPointToDBObjectConverter.INSTANCE,
				GeoJsonPolygonToDBObjectConverter.INSTANCE);
	}

	@WritingConverter
	public static enum GeoJsonPointToDBObjectConverter implements Converter<GeoJsonPoint, DBObject> {
		INSTANCE;
		@Override
		public DBObject convert(final GeoJsonPoint source) {
			return new BasicDBObject("type", "Point").append("coordinates", new Double[]{ source.getX(), source.getY() });
		}
	}

	@ReadingConverter
	public static enum GeoJsonDBObjectToPointConverter implements Converter<DBObject, GeoJsonPoint> {
		INSTANCE;
		@Override
		public GeoJsonPoint convert(final DBObject source) {						
			final Double[] coordinates = ((BasicDBList) source.get("coordinates")).stream()
					.map(c -> (Double)c).toArray(Double[]::new);			
			return new GeoJsonPoint(coordinates[0], coordinates[1]);
		}
	}

	@WritingConverter
	public static enum GeoJsonPolygonToDBObjectConverter implements Converter<GeoJsonPolygon, DBObject> {
		INSTANCE;
		@Override
		public DBObject convert(final GeoJsonPolygon source) {
			final List<Double[]> coordinates = source.getPoints().stream().map(p -> {
				return new Double[]{ p.getX(), p.getY() };
			}).collect(Collectors.toList());
			return new BasicDBObject("type", "Polygon").append("coordinates", coordinates);			
		}
	}

	@ReadingConverter
	public static enum GeoJsonDBObjectToPolygonConverter implements Converter<DBObject, GeoJsonPolygon> {
		INSTANCE;
		@Override
		public GeoJsonPolygon convert(final DBObject source) {
			@SuppressWarnings("unchecked")
			final List<Point> coordinates = ((List<Double[]>) source.get("coordinates")).stream().map(c -> {
				return new Point(c[0], c[1]);
			}).collect(Collectors.toList());
			return new GeoJsonPolygon(coordinates);			
		}
	}

}