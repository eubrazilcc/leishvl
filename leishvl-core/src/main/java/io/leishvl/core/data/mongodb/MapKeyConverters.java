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

package io.leishvl.core.data.mongodb;

import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.compile;
import static java.util.regex.Pattern.quote;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeJava;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Adapts JSON documents to attains MongoDB restrictions on field names: <em>Field names cannot contain 
 * dots (i.e. .) or null characters, and they must not start with a dollar sign (i.e. $).</em>
 * @see <a href="https://docs.mongodb.org/manual/reference/limits/#Restrictions-on-Field-Names">MongoDB restrictions on field names</a>
 * @author Erik Torres <ertorser@upv.es>
 */
public final class MapKeyConverters {

	/**
	 * Conversion function that replace MongoDB's unsupported characters by similar UTF-8 signs applying
	 * efficient pattern matching with regular expressions.
	 */
	private static final Function<String, String> KEY_ESCAPE = k -> {
		final StringBuffer sb = new StringBuffer();
		final Matcher matcher = compile("^" + quote("$")).matcher(k);
		if (matcher.find()) matcher.appendReplacement(sb, quoteReplacement("\\") + "uff04");
		matcher.usePattern(compile(quote(".")));
		while (matcher.find()) matcher.appendReplacement(sb, quoteReplacement("\\") + "uff0e");
		return unescapeJava(matcher.appendTail(sb).toString());
	};
	
	/**
	 * Conversion function that replace similar UTF-8 signs by MongoDB's unsupported characters applying
	 * efficient pattern matching with regular expressions.
	 */
	private static final Function<String, String> KEY_UNESCAPE = k -> {
		final StringBuffer sb = new StringBuffer();
		final Matcher matcher = compile("^" + quote("\uff04")).matcher(k);
		if (matcher.find()) matcher.appendReplacement(sb, quoteReplacement("$"));
		matcher.usePattern(compile(quote("\uff0e")));
		while (matcher.find()) matcher.appendReplacement(sb, quoteReplacement("."));
		return unescapeJava(matcher.appendTail(sb).toString());
	};

	/**
	 * Reads a JSON string into a Java map, escapes MongoDB's unsupported characters from the map 
	 * and creates a "raw" database object with MongoDB's JSON parser.
	 * @param json - a JSON string
	 * @return a database object where MongoDB's unsupported characters are mapped to valid substitutions.
	 */
	public static DBObject escapeMongo(final String json) {		
		final Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
		final Map<String, Object> map = new Gson().fromJson(json, mapType);		
		return new BasicDBObject(escapeMap(map, KEY_ESCAPE));
	}

	/**
	 * Reads a "raw" database object into a Java map, un-escapes MongoDB's unsupported characters in 
	 * the map and write it back to a JSON string using Gson.
	 * @param dbo - a database object
	 * @return a JSON string created from the source object where the MongoDB's unsupported characters
	 *         are mapped to their original values.
	 */
	public static String unescapeMongo(final DBObject dbo) {
		@SuppressWarnings("unchecked")
		final Map<String, Object> map = escapeMap(dbo.toMap(), KEY_UNESCAPE);
		final Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
		return new Gson().toJson(map, mapType);
	}

	/**
	 * Recursively iterates over a map created from a JSON document, applying a conversion function to 
	 * its keys. It guarantees that all possible inner maps are also converted.
	 * @param map - a map created from a JSON document
	 * @param keyConverter - a function that applies a conversion to the keys of the input map
	 * @return a new map where the keys where transformed using the conversion function.
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Object> escapeMap(final Map<String, Object> map, final Function<String, String> keyConverter) {
		return map.entrySet().stream().collect(Collectors.toMap(e -> keyConverter.apply(e.getKey()), e -> {
			return (e.getValue() instanceof Map) ? escapeMap((Map<String, Object>) e.getValue(), keyConverter) 
					: e.getValue();
		}));
	}

}