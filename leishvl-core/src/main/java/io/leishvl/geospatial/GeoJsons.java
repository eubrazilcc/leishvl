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

import static com.google.common.math.DoubleMath.fuzzyEquals;

import java.util.function.BiFunction;

import org.springframework.data.geo.Point;

/**
 * Utilities to work with GeoJSON objects.
 * @author Erik Torres <ertorser@upv.es>
 */
public final class GeoJsons {

	public static final double TOLERANCE = 0.00000001d;

	public static final BiFunction<Point, Point, Boolean> POINT_FUZZY_EQUALS = (p1, p2) -> {
		if (p1 == p2) return true;
		else if (p1 == null || p2 == null) return false;
		if (p1.getX() == p2.getX() && p1.getY() == p2.getY()) return true;
		return fuzzyEquals(p1.getX(), p2.getX(), TOLERANCE)
				&& fuzzyEquals(p1.getY(), p2.getY(), TOLERANCE);
	};

	public static String lngLat2Human(final Point point) {
		return point.getY() + "\u00b0" + " N " + point.getX() + "\u00b0" + " W";
	}

}