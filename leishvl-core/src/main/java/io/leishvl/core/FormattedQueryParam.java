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

import java.util.Objects;

/**
 * Formatted query parameter.
 * @author Erik Torres <ertorser@upv.es>
 */
public class FormattedQueryParam {

	private String term;
	private boolean valid;

	public String getTerm() {
		return term;
	}
	public void setTerm(final String term) {
		this.term = term;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(final boolean valid) {
		this.valid = valid;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null || !(obj instanceof FormattedQueryParam)) {
			return false;
		}
		final FormattedQueryParam other = FormattedQueryParam.class.cast(obj);
		return Objects.equals(term, other.term)
				&& Objects.equals(valid, other.valid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(term, valid);
	}

	@Override
	public String toString() {
		return toStringHelper(FormattedQueryParam.class)
				.add("term", term)
				.add("valid", valid)
				.toString();
	}

	/* Fluent API */

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private final FormattedQueryParam instance = new FormattedQueryParam();

		public Builder term(final String term) {
			instance.setTerm(term);
			return this;
		}

		public Builder valid(final boolean valid) {
			instance.setValid(valid);
			return this;
		}

		public FormattedQueryParam build() {
			return instance;
		}

	}

}