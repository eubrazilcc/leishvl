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

package io.leishvl.storage.mongodb.jackson;

import static io.leishvl.storage.mongodb.jackson.MongoJsonOptions.JSON_PRETTY_PRINTER;
import static java.util.Arrays.asList;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Binds Java objects to/from mongoDB using the Jackson JSON processor.
 * @author Erik Torres <ertorser@upv.es>
 */
public final class MongoJsonMapper {

	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	static {
		// apply general configuration
		JSON_MAPPER.setSerializationInclusion(Include.NON_NULL);
		JSON_MAPPER.setSerializationInclusion(Include.NON_EMPTY);
		JSON_MAPPER.setSerializationInclusion(Include.NON_DEFAULT);		
	}

	public static final String objectToJson(final Object obj, final MongoJsonOptions... options) throws JsonProcessingException {
		boolean pretty = false;
		if (options != null) {
			final List<MongoJsonOptions> optList = asList(options);
			pretty = optList.contains(JSON_PRETTY_PRINTER);
		}
		return !pretty ? JSON_MAPPER.writeValueAsString(obj) : JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);		
	}

}