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

package io.leishvl.core;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import io.leishvl.core.config.JacksonConfiguration;
import io.leishvl.core.config.MongoConfiguration;

/**
 * Spring application which is mainly intended for use in demonstrations since this is a JAR library, not an executable.
 * @author Erik Torres <ertorser@upv.es>
 */
@SpringBootApplication
@Import(value={ JacksonConfiguration.class, MongoConfiguration.class })
public class LeishvlCoreApplication implements CommandLineRunner {

	public static void main(final String[] args) {
		SpringApplication.run(LeishvlCoreApplication.class, args);
	}

	@Override
	public void run(final String... arg) throws Exception {
		// do nothing		
	}

}