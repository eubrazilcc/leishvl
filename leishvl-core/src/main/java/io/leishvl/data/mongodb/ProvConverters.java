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

import static com.mongodb.util.JSON.parse;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.compile;
import static java.util.regex.Pattern.quote;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeJava;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropFramework.ProvFormat;
import org.openprovenance.prov.model.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.DBObject;

/**
 * A Spring Data converter to convert W3C PROV data model representations.
 * @author Erik Torres <ertorser@upv.es>
 */
public class ProvConverters {

	private static final Pattern DOLLAR_PATTERN = compile(quote("$"));
	private static final String DOLLAR_REPLACEMENT = quoteReplacement("\\") + "uff04";

	private static final Pattern UDOLLAR_PATTERN = compile(quote("\uff04"));
	private static final String UDOLLAR_REPLACEMENT = quoteReplacement("$");

	public static List<Converter<?, ?>> getProvConvertersToRegister() {
		return asList(DBObjectToProvDocumentConverter.INSTANCE,
				ProvDocumentToDBObjectConverter.INSTANCE);
	}

	@WritingConverter
	public static enum ProvDocumentToDBObjectConverter implements Converter<Document, DBObject> {
		INSTANCE;
		@Override
		public DBObject convert(final Document source) {
			try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
				// export W3C PROV document to JSON using the interoperability framework
				new InteropFramework().writeDocument(os, ProvFormat.JSON, source);
				// escape MongoDB special characters
				final String json = unescapeJava(DOLLAR_PATTERN.matcher(os.toString(UTF_8.name())).replaceAll(DOLLAR_REPLACEMENT));
				// read "raw" object with MongoDB's JSON parser and return the created object
				return (DBObject) parse(json);
			} catch (Exception e) {
				throw new IllegalStateException("Failed to convert W3C PROV Document to MongoDB DBObject", e);
			}
		}
	}

	@ReadingConverter
	public static enum DBObjectToProvDocumentConverter implements Converter<DBObject, Document> {
		INSTANCE;
		@Override
		public Document convert(final DBObject source) {
			// read "raw" object into a Java map
			final Map<?, ?> map = source.toMap();
			// write map to JSON using Gson
			final Type typeOfMap = new TypeToken<Map<String, Object>>(){}.getType();
			final String json = new Gson().toJson(map, typeOfMap);
			// unescape MongoDB special characters and create a W3C PROV document using the interoperability framework
			final String unescaped = UDOLLAR_PATTERN.matcher(json).replaceAll(UDOLLAR_REPLACEMENT);
			return new InteropFramework().readDocument(new ByteArrayInputStream(unescaped.getBytes()), ProvFormat.JSON, null);			
		}
	}

}