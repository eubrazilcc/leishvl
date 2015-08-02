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

package io.leishvl.storage.prov.jackson;

import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.compile;
import static java.util.regex.Pattern.quote;
import static org.openprovenance.prov.interop.InteropFramework.ProvFormat.JSON;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.regex.Pattern;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Deserialize W3C PROV documents from JSON to {@link Document} Java class.
 * @author Erik Torres <ertorser@upv.es>
 */
public final class ProvDocumentDeserializer extends StdDeserializer<Document> {

	private static final long serialVersionUID = 2413052050095160323L;

	private static final Pattern UDOLLAR_PATTERN = compile(quote("\uff04"));
	private static final String UDOLLAR_REPLACEMENT = quoteReplacement("$");

	protected ProvDocumentDeserializer() {
		super(Document.class);
	}

	@Override
	public Document deserialize(final JsonParser parser, final DeserializationContext context) throws IOException, JsonProcessingException {
		// read "raw" object with Jackson parser
		final Map<?, ?> map = parser.readValueAs(Map.class);
		// write map to JSON using Gson
		final Type typeOfMap = new TypeToken<Map<String, Object>>(){}.getType();
		final String json = new Gson().toJson(map, typeOfMap);
		// unescape mongoDB special characters and read with W3C PROV
		final String unescaped = UDOLLAR_PATTERN.matcher(json).replaceAll(UDOLLAR_REPLACEMENT);
		return new InteropFramework().readDocument(new ByteArrayInputStream(unescaped.getBytes()), JSON, null);
	}

}