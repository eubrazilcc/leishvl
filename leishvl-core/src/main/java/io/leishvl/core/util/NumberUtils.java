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

/**
 * Extra utilities for numeric types.
 * @author Erik Torres <ertorser@upv.es>
 */
public final class NumberUtils {

	/**
	 * Rounds an integer division to the nearest integer.
	 * @param number - value that is rounded
	 * @param base - divisor
	 * @return the nearest integer that multiplied by the base has the largest 
	 *         integer value
	 */
	public static int roundUp(final int number, final int base) {
		return (number + base - 1) / base;
	}
	
	/**
	 * Rounds an long division to the nearest long.
	 * @param number - value that is rounded
	 * @param base - divisor
	 * @return the nearest long that multiplied by the base has the largest 
	 *         long value
	 */
	public static long roundUp(final long number, final long base) {
		return (number + base - 1l) / base;
	}
	
}