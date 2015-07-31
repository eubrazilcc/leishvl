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

package io.leishvl.test;

import static java.util.Collections.unmodifiableMap;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provides a common environment especially useful for integration tests.
 * @author Erik Torres <ertorser@upv.es>
 */
public class TestContext {

	private final String service;
	private final File testOutputDir;
	private final ObjectMapper jsonMapper;
	private final Map<String, TestCredential> credentials;

	public TestContext(final File testOutputDir, final String service, final ObjectMapper jsonMapper, 
			final Map<String, TestCredential> credentials) {
		this.testOutputDir = testOutputDir;
		this.service = service;
		this.jsonMapper = jsonMapper;
		this.credentials = (credentials != null ? unmodifiableMap(credentials) : Collections.<String, TestCredential>emptyMap());
	}
	
	public File testOutputDir() {
		return testOutputDir;
	}

	public String service() {
		return service;
	}

	public ObjectMapper jsonMapper() {
		return jsonMapper;
	}

	public Map<String, TestCredential> credentials() {
		return credentials;
	}

	public String token(final String id) {
		return credentials.get(id).getToken();
	}
	
	public String ownerid(final String id) {
		return credentials.get(id).getOwnerId();
	}

	public static class TestCredential {
		private final String ownerId;
		private final String token;

		public TestCredential(final String ownerId, final String token) {
			this.ownerId = ownerId;
			this.token = token;
		}

		public String getOwnerId() {
			return ownerId;
		}

		public String getToken() {
			return token;
		}		

	}

}