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

import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.List;
import java.util.Objects;

/**
 * Additional annotations provided by the LeishVL users to the citations.
 * @author Erik Torres <ertorser@upv.es>
 */
public class LeishvlArticle {

	private List<String> cited;

	public List<String> getCited() {
		return cited;
	}

	public void setCited(final List<String> cited) {
		this.cited = cited;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null || !(obj instanceof LeishvlArticle)) {
			return false;
		}
		final LeishvlArticle other = LeishvlArticle.class.cast(obj);
		return Objects.equals(cited, other.cited);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cited);
	}

	@Override
	public String toString() {
		return toStringHelper(this)
				.add("cited", cited)
				.toString();
	}

	/* Fluent API */

	public static Builder builder() {
		return new Builder();
	}	

	public static class Builder {

		private LeishvlArticle instance = new LeishvlArticle();

		public Builder cited(final List<String> cited) {
			instance.setCited(cited);
			return this;
		}

		public LeishvlArticle build() {
			return instance;
		}

	}

}