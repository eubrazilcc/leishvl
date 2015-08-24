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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.leishvl.storage.base.LeishvlCollection;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Wraps a collection of {@link Citation}.
 * @author Erik Torres <ertorser@upv.es>
 */
public class Citations extends LeishvlCollection<Citation> {

	@JsonProperty("links")
	private Map<String, Link> links; // HATEOAS links

	public Citations(final Vertx vertx, final JsonObject config) {
		super(vertx, config, Citation.COLLECTION, Citation.class, Citation.CONFIGURER, getLogger(Citations.class));
	}

	@Override
	public Map<String, Link> getLinks() {
		return links;
	}

	@Override
	public void setLinks(final Map<String, Link> links) {
		this.links = (links != null ? newHashMap(links) : null);
	}

}