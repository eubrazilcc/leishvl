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

package io.leishvl.jackson;

import static io.leishvl.jackson.JsonOptions.JSON_PRETTY_PRINTER;
import static java.util.Arrays.asList;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Binds Java objects to/from JSON using the Jackson JSON processor.
 * @author Erik Torres <ertorser@upv.es>
 */
public enum JsonProcessor {

	JSON_PROCESSOR;

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	static {
		// apply general configuration
		OBJECT_MAPPER.setSerializationInclusion(Include.NON_NULL);
		OBJECT_MAPPER.setSerializationInclusion(Include.NON_EMPTY);
		// OBJECT_MAPPER.setSerializationInclusion(Include.NON_DEFAULT);
	}	

	public static final String objectToJson(final Object obj, final JsonOptions... options) throws JsonProcessingException {
		boolean pretty = false;
		if (options != null) {
			final List<JsonOptions> optList = asList(options);
			pretty = optList.contains(JSON_PRETTY_PRINTER);
		}
		return !pretty ? OBJECT_MAPPER.writeValueAsString(obj) : OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);		
	}
	
	public ObjectMapper mapper() {
		return OBJECT_MAPPER;
	}

}