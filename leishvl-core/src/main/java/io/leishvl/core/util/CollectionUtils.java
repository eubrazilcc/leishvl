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

import static com.google.common.base.Joiner.on;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Iterables.elementsEqual;
import static com.google.common.collect.Maps.difference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Joiner.MapJoiner;

/**
 * Collection utilities.
 * @author Erik Torres <ertorser@upv.es>
 */
public final class CollectionUtils {

	public static <E> String collectionToString(final Collection<E> collection) {
		return Arrays.toString(from(collection != null ? collection : new ArrayList<E>()).transform(new Function<E, String>() {
			@Override
			public String apply(final E item) {
				return item != null ? item.toString() : null;
			}
		}).filter(notNull()).toArray(String.class));		
	}

	public static <K, V> String mapToString(final Map<K, V> map) {	
		final MapJoiner mapJoiner = on(',').withKeyValueSeparator("=");
		return mapJoiner.join(map != null ? map : new HashMap<K, V>());
	}

	public static <K, V> boolean equals(final Map<K, V> left, final Map<K, V> right) {
		final Equivalence<Object> equivalence = new Equivalence<Object>() {
			@Override
			protected boolean doEquivalent(final Object a, final Object b) {
				if (a instanceof Iterable && b instanceof Iterable) {
					return elementsEqual((Iterable<?>)a, (Iterable<?>)b);
				} else if (a instanceof Map && b instanceof Map) {
					return difference((Map<?, ?>)a, (Map<?, ?>)b, this).areEqual();
				} else return a.equals(b);					
			}
			@Override
			protected int doHash(final Object t) {
				return t.hashCode();
			}
		};
		return ((left == null && right == null) || difference(left, right, equivalence).areEqual());
	}

}