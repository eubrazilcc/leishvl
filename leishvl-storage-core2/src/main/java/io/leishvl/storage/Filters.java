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

package io.leishvl.storage;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.util.List;
import java.util.Objects;

/**
 * Groups collection filters.
 * @author Erik Torres <ertorser@upv.es>
 */
public class Filters {

	private LogicalType type;
	private List<Filter> filters;	

	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(final List<Filter> filters) {
		this.filters = filters;
	}	

	public LogicalType getType() {
		return type;
	}

	public void setType(final LogicalType type) {
		this.type = type;
	}	

	@Override
	public boolean equals(final Object obj) {
		if (obj == null || !(obj instanceof Filters)) {
			return false;
		}
		final Filters other = Filters.class.cast(obj);
		return Objects.equals(type, other.type)
				&& Objects.equals(filters, other.filters);		
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, filters);
	}

	@Override
	public String toString() {
		return toStringHelper(this)
				.add("type", type)				
				.add("filters", filters)
				.toString();
	}

	/* Inner classes */

	public static enum LogicalType {
		LOGICAL_AND,
		LOGICAL_OR
	}

	/* Fluent API */

	public static Builder builder() {
		return new Builder();
	}	

	public static class Builder {

		private final Filters instance = new Filters();

		public Builder type(final LogicalType type) {
			instance.setType(type);			
			return this;
		}

		public Builder filters(final List<Filter> filters) {
			instance.setFilters(filters);
			return this;
		}

		public Filters build() {
			return instance;
		}

	}

}