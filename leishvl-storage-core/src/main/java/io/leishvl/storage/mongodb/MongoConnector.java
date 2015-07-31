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

package io.leishvl.storage.mongodb;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;

import io.leishvl.storage.mongodb.client.MongoFileClient;
import io.leishvl.storage.mongodb.client.MongoTraceableObjectClient;

/**
 * Data connector based on mongoDB. Access to file collections is provided through the GridFS specification.
 * @author Erik Torres <ertorser@upv.es>
 * @see <a href="https://www.mongodb.org/">mongoDB</a>
 * @see <a href="http://docs.mongodb.org/manual/core/gridfs/">GridFS</a>
 */
public enum MongoConnector implements Closeable {

	MONGODB_CONN;

	private static final Logger LOGGER = getLogger(MongoConnector.class);

	private final MongoTraceableObjectClient traceableObjectClient = new MongoTraceableObjectClient();
	private final MongoFileClient fileClient = new MongoFileClient();

	public MongoTraceableObjectClient client() {
		return traceableObjectClient;
	}

	public MongoFileClient fsClient() {
		return fileClient;
	}

	@Override
	public void close() throws IOException {
		try {
			traceableObjectClient.close();
		} catch (Exception ignore) { }
		try {
			fileClient.close();
		} catch (Exception ignore) { }
		LOGGER.info("mongoDB connector shutdown successfully");
	}

}