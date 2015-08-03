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
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static io.leishvl.storage.mongodb.MongoCollectionConfigurer.nonUniqueIndexModel;
import static io.leishvl.storage.mongodb.MongoCollectionConfigurer.textIndexModel;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

import io.leishvl.core.xml.ncbi.pubmed.PubmedArticle;
import io.leishvl.storage.base.LeishvlObject;
import io.leishvl.storage.mongodb.MongoCollectionConfigurer;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * Represents a publication citation, including the original PubMed article and additional annotations provided by the LeishVL users.
 * @author Erik Torres <ertorser@upv.es>
 */
public class Citation extends LeishvlObject {

	public static final String COLLECTION      = "citations";	
	public static final String PUBMED_PMID     = "pubmed.medlineCitation.pmid.value";
	public static final String PUBMED_TITLE    = "pubmed.medlineCitation.article.articleTitle";
	public static final String PUBMED_ABSTRACT = "pubmed.medlineCitation.article.abstract.abstractText";
	public static final String PUBMED_YEAR     = "pubmed.medlineCitation.dateCreated.year.value";

	public static final MongoCollectionConfigurer CONFIGURER = new MongoCollectionConfigurer(COLLECTION, true, newArrayList(
			nonUniqueIndexModel(PUBMED_PMID, false),
			nonUniqueIndexModel(PUBMED_YEAR, false),
			textIndexModel(ImmutableList.of(PUBMED_TITLE, PUBMED_ABSTRACT), COLLECTION)));

	@JsonProperty("links")
	private Map<String, Link> links; // HATEOAS links

	private LeishvlCitation leishvl; // LeishVL citation	
	private PubmedArticle pubmed; // original PubMed article

	public Citation(final Vertx vertx, final JsonObject config) {
		super(vertx, config, COLLECTION, CONFIGURER, getLogger(Citation.class));
	}

	@Override	
	public Map<String, Link> getLinks() {
		return links;
	}

	@Override
	public void setLinks(final Map<String, Link> links) {
		this.links = (links != null ? newHashMap(links) : null);		
	}

	public LeishvlCitation getLeishvl() {
		return leishvl;
	}

	public void setLeishvl(final LeishvlCitation leishvl) {
		this.leishvl = leishvl;
	}

	public PubmedArticle getPubmed() {
		return pubmed;
	}

	public void setPubmed(final PubmedArticle pubmed) {
		this.pubmed = pubmed;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null || !(obj instanceof Citation)) {
			return false;
		}
		final Citation other = Citation.class.cast(obj);
		return super.equals((LeishvlObject)other)
				&& Objects.equals(leishvl, other.leishvl);		
	}

	@Override
	public int hashCode() {
		return super.hashCode() + Objects.hash(leishvl);
	}

	@Override
	public String toString() {
		return toStringHelper(this)
				.add(LeishvlObject.class.getSimpleName(), super.toString())
				.add("lvl", leishvl)				
				.add("pubmed", "<<original PubMed article is not displayed>>")
				.toString();
	}

	/* Fluent API */

	public static Builder builder(final Vertx vertx, final JsonObject config) {
		return new Builder(vertx, config);
	}	

	public static class Builder extends LeishvlObject.Builder<Citation, Builder> {

		public Builder(final Vertx vertx, final JsonObject config) {
			super(new Citation(vertx, config));
			setBuilder(this);
		}

		public Builder leishvl(final LeishvlCitation leishvl) {
			instance.setLeishvl(leishvl);
			return this;
		}

		public Builder pubmed(final PubmedArticle pubmed) {
			instance.setPubmed(pubmed);
			return this;
		}

		public Citation build() {
			return instance;
		}

	}

}