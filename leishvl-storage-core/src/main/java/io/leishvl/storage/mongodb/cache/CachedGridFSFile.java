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

package io.leishvl.storage.mongodb.cache;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Cached GridFS file.
 * @author Erik Torres <ertorser@upv.es>
 */
public class CachedGridFSFile {

	private String cachedFilename;
	private String md5;

	public String getCachedFilename() {
		return cachedFilename;
	}

	public void setCachedFilename(final String cachedFilename) {
		this.cachedFilename = cachedFilename;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(final String md5) {
		this.md5 = md5;
	}

	@Override
	public String toString() {
		return toStringHelper(this)
				.add("cachedFilename", cachedFilename)
				.add("md5", md5)
				.toString();
	}

	/* Fluent API */

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private final CachedGridFSFile instance = new CachedGridFSFile();

		public Builder cachedFilename(final String cachedFilename) {
			instance.setCachedFilename(cachedFilename);
			return this;
		}

		public Builder md5(final String md5) {
			instance.setMd5(md5);
			return this;
		}

		public CachedGridFSFile build() {
			return instance;
		}

	}

}