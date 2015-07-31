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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.newArrayList;
import static io.leishvl.storage.mongodb.MongoCollectionConfigurer.indexModel;
import static io.leishvl.storage.mongodb.MongoCollectionConfigurer.nonUniqueIndexModel;
import static io.leishvl.storage.mongodb.MongoConnector.MONGODB_CONN;
import static java.util.Collections.synchronizedSet;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Applies configuration to file collections.
 * @author Erik Torres <ertorser@upv.es>
 */
public class MongoFilesConfigurer {

	private static final Logger LOGGER = getLogger(MongoFilesConfigurer.class);

	public static final long TIMEOUT = 5l;

	private Set<String> buckets = synchronizedSet(Sets.<String>newHashSet());

	public void prepareFiles(final String bucket) {
		checkArgument(isNotBlank(bucket), "Uninitialized or invalid bucket");
		final boolean configured = buckets.contains(bucket);
		if (!configured) {
			/* create indexes: 1) enforce isolation property: each bucket has its own index where the filenames are unique;
			 * 2) create index to operate on metadata filename; and 3) create index to operate on open access links. */
			final ListenableFuture<List<String>> future = MONGODB_CONN.client().createIndexes(bucket + ".files", newArrayList(indexModel("filename"), 
					nonUniqueIndexModel("uploadDate", true), nonUniqueIndexModel("metadata.filename", false), 
					nonUniqueIndexModel("metadata.openAccess.secret", false)));
			try {
				future.get(TIMEOUT, SECONDS);
				buckets.add(bucket);
				LOGGER.info("Bucket configured: " + bucket);
			} catch (Exception e) {
				LOGGER.info("Failed to configure bucket: " + bucket, e);
			}
		}
	}	

}