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

import static io.leishvl.storage.mongodb.jackson.MongoJsonMapper.JSON_MAPPER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.regex.Matcher.quoteReplacement;
import static java.util.regex.Pattern.compile;
import static java.util.regex.Pattern.quote;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeJava;
import static org.openprovenance.prov.interop.InteropFramework.ProvFormat.JSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Serialize W3C PROV documents from {@link Document} Java class to JSON.
 * @author Erik Torres <ertorser@upv.es>
 */
public final class ProvDocumentSerializer extends StdSerializer<Document> {

	private static final long serialVersionUID = -3747444365671923835L;

	private static final Pattern DOLLAR_PATTERN = compile(quote("$"));
	private static final String DOLLAR_REPLACEMENT = quoteReplacement("\\") + "uff04";

	protected ProvDocumentSerializer() {
		super(Document.class);
	}

	@Override
	public void serialize(final Document value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonProcessingException {
		if (value == null) jgen.writeNull();
		else {
			try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
				// export W3C PROV document to JSON using the interoperability framework
				new InteropFramework().writeDocument(os, JSON, value);
				// escape mongoDB special characters
				final String json = unescapeJava(DOLLAR_PATTERN.matcher(os.toString(UTF_8.name())).replaceAll(DOLLAR_REPLACEMENT));
				// read "raw" object with Jackson parser and write the created object to the JSON generator
				final Map<?, ?> map = JSON_MAPPER.getFactory().createParser(json).readValueAs(Map.class);
				jgen.writeObject(map);	
			}
		}
	}	

}