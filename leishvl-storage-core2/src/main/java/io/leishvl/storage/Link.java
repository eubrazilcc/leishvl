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
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

/**
 * Represents a hyperlink with a format inspired by HAL. However, since compatibility with HAL is not 100%, the MIME type "application/hal+json"
 * should <strong>NOT</strong> be applied to the JSON documents that include this link. Examples:
 * <ul>
 * <li>{ "href" : "/rest/v1/sequences/sandflies/~/AC238558", "type" : "application/json" }</li>
 * </ul>               
 * @author Erik Torres <ertorser@upv.es>
 * @see <a href="http://stateless.co/hal_specification.html">HAL - Hypertext Application Language</a>
 * @see <a href="https://tools.ietf.org/html/draft-kelly-json-hal-07">JSON Hypertext Application Language</a>
 */
public class Link {

	private String href;
	private Optional<String> type = empty();

	public String getHref() {
		return href;
	}

	public void setHref(final String href) {
		this.href = href;
	}

	public @Nullable String getType() {
		return type.orElse(null);
	}

	public void setType(final @Nullable String type) {
		this.type = ofNullable(type);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null || !(obj instanceof Link)) {
			return false;
		}
		final Link other = Link.class.cast(obj);
		return Objects.equals(href, other.href)
				&& Objects.equals(type, other.type);
	}

	@Override
	public int hashCode() {
		return Objects.hash(href, type);
	}

	@Override
	public String toString() {
		return toStringHelper(this)
				.add("href", href)
				.add("type", type.orElse(null))
				.toString();
	}

	/* Fluent API */

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private final Link instance = new Link();		

		public Builder href(final String href) {
			instance.setHref(href);			
			return this;
		}

		public Builder type(final @Nullable String type) {
			instance.setType(type);			
			return this;
		}

		public Link build() {
			return instance;
		}

	}

}