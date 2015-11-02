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
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Objects;

import org.springframework.data.mongodb.core.mapping.Document;

import io.leishvl.ncbi.pubmed.PubmedArticle;

/**
 * Represents a publication citation, including the original PubMed article and additional annotations provided by the LeishVL users.
 * @author Erik Torres <ertorser@upv.es>
 */
@Document(collection="citations")
public class Citation extends LeishvlObject {

	private LeishvlArticle leishvl; // custom content added by LeishVL's users
	private PubmedArticle pubmed; // original PubMed article

	public Citation() {
		super(getLogger(Citation.class));
	}

	public PubmedArticle getPubmed() {
		return pubmed;
	}

	public void setPubmed(final PubmedArticle pubmed) {
		this.pubmed = pubmed;
	}

	public LeishvlArticle getLeishvl() {
		return leishvl;
	}

	public void setLeishvl(final LeishvlArticle leishvl) {
		this.leishvl = leishvl;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null || !(obj instanceof Citation)) {
			return false;
		}
		final Citation other = Citation.class.cast(obj);
		return super.equals(other)
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

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends LeishvlObject.Builder<Citation, Builder> {

		public Builder() {
			super(new Citation());
			setBuilder(this);
		}

		public Builder leishvl(final LeishvlArticle leishvl) {
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