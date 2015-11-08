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

import static io.leishvl.core.data.mongodb.MapKeyConverters.escapeMongo;
import static io.leishvl.core.data.mongodb.MapKeyConverters.unescapeMongo;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropFramework.ProvFormat;
import org.openprovenance.prov.model.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import com.mongodb.DBObject;

/**
 * A Spring Data converter to convert W3C PROV data model representations.
 * @author Erik Torres <ertorser@upv.es>
 */
public class ProvConverters {

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
				// escape MongoDB's unsupported characters 
				return escapeMongo(os.toString(UTF_8.name()));
			} catch (Exception e) {
				throw new IllegalStateException("Failed to convert W3C PROV Document to MongoDB DBObject", e);
			}
		}
	}

	/**
	 * Un-escape MongoDB's unsupported characters and create a W3C PROV document using the interoperability 
	 * framework.
	 * @author Erik Torres <ertorser@upv.es>
	 */
	@ReadingConverter
	public static enum DBObjectToProvDocumentConverter implements Converter<DBObject, Document> {
		INSTANCE;
		@Override
		public Document convert(final DBObject source) {			
			return new InteropFramework().readDocument(new ByteArrayInputStream(unescapeMongo(source).getBytes()), 
					ProvFormat.JSON, null);			
		}
	}

}