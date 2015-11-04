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
import static java.util.Optional.ofNullable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Binds Java objects to/from JSON using the Jackson JSON processor.
 * @author Erik Torres <ertorser@upv.es>
 */
@Configurable
public class JsonProcessor {

	@Autowired
	private ObjectMapper objectMapper;

	public String objectToJson(final Object obj, final JsonOptions... options) throws JsonProcessingException {
		final boolean pretty = asList(ofNullable(options)
				.orElse(new JsonOptions[]{}))
				.contains(JSON_PRETTY_PRINTER);

		// TODO
		System.err.println("\n\n >> HERE: " + objectMapper + "\n");
		// TODO

		return !pretty ? objectMapper.writeValueAsString(obj) : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);		
	}

}