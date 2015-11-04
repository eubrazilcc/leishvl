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

package io.leishvl.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import io.leishvl.core.jackson.JsonProcessor;

/**
 * Spring configuration for Jackson JSON processor.
 * @author Erik Torres <ertorser@upv.es>
 */
@Configuration
@Profile("data")
public class JacksonConfiguration {

	@Bean @Primary
	public ObjectMapper objectMapper() {
		return new ObjectMapper()
				.setSerializationInclusion(Include.NON_NULL)
				.setSerializationInclusion(Include.NON_EMPTY)
				.setSerializationInclusion(Include.NON_ABSENT)
				.registerModule(new Jdk8Module());
	}
	
	@Bean @Lazy
	public JsonProcessor jsonProcessor() {
		return new JsonProcessor();
	}

}