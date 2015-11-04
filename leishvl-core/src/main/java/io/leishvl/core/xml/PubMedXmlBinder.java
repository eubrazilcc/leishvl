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

package io.leishvl.core.xml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;

import javax.annotation.Nullable;
import javax.xml.bind.JAXBElement;

import io.leishvl.core.ncbi.pubmed.ObjectFactory;
import io.leishvl.core.ncbi.pubmed.PubmedArticle;
import io.leishvl.core.ncbi.pubmed.PubmedArticleSet;
import io.leishvl.core.ncbi.pubmed.Year;

/**
 * NCBI PubMed article XML binding helper.
 * @author Erik Torres <ertorser@upv.es>
 */
public class PubMedXmlBinder extends XmlBinder {

	private static final Class<?>[] SUPPORTED_CLASSES = {
			PubmedArticleSet.class,
			PubmedArticle.class
	};

	public static final ObjectFactory PUBMED_XML_FACTORY = new ObjectFactory();

	public static final PubMedXmlBinder PUBMED_XMLB = new PubMedXmlBinder();

	private PubMedXmlBinder() {
		super(SUPPORTED_CLASSES);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> JAXBElement<T> createType(final T obj) {
		Object element;
		Class<?> clazz = obj.getClass();
		if (clazz.equals(PubmedArticleSet.class)) {
			element = PUBMED_XML_FACTORY.createPubmedArticleSet();
		} else if (clazz.equals(PubmedArticle.class)) {
			element = PUBMED_XML_FACTORY.createPubmedArticle();
		} else {
			throw new IllegalArgumentException("Unsupported type: " + clazz.getCanonicalName());
		}
		return (JAXBElement<T>) element;
	}

	/**
	 * Gets the PubMed identifier (PMID) from a PubMed article.
	 * @param article - PubMed article
	 * @return if found, the PubMed identifier (PMID), otherwise {@code null}.
	 */
	public static @Nullable String getPubMedId(final PubmedArticle article) {
		checkArgument(article != null && article.getMedlineCitation() != null && article.getMedlineCitation().getPMID() != null,
				"Uninitialized or invalid article.");
		return isNotBlank(article.getMedlineCitation().getPMID().getvalue()) ? article.getMedlineCitation().getPMID().getvalue().trim() : null;
	}

	public static int getYear(final PubmedArticle article) {
		checkArgument(article != null && article.getMedlineCitation() != null
				&& article.getMedlineCitation().getArticle() != null
				&& article.getMedlineCitation().getArticle().getJournal() != null
				&& article.getMedlineCitation().getArticle().getJournal().getJournalIssue() != null
				&& article.getMedlineCitation().getArticle().getJournal().getJournalIssue().getPubDate() != null
				&& article.getMedlineCitation().getArticle().getJournal().getJournalIssue().getPubDate().getYearOrMonthOrDayOrSeasonOrMedlineDate() != null
				&& !article.getMedlineCitation().getArticle().getJournal().getJournalIssue().getPubDate().getYearOrMonthOrDayOrSeasonOrMedlineDate().isEmpty(),
				"Uninitialized or invalid article");
		int year = -1;
		final List<Object> pubDate = article.getMedlineCitation().getArticle().getJournal().getJournalIssue().getPubDate()
				.getYearOrMonthOrDayOrSeasonOrMedlineDate();
		for (final Object obj : pubDate) {
			if (obj instanceof Year) {
				year = Integer.valueOf(((Year)obj).getvalue());
			}
		}
		checkState(year > 1900, "No valid year found");
		return year;
	}

	public static List<String> getSequences(final PubmedArticle article) {
		final List<String> sequences = newArrayList();
		checkArgument(article != null && article.getMedlineCitation() != null
				&& article.getMedlineCitation().getArticle() != null, "Uninitialized or invalid article");
		if (article.getMedlineCitation().getArticle().getDataBankList() != null
				&& article.getMedlineCitation().getArticle().getDataBankList().getDataBank() != null) {
			article.getMedlineCitation().getArticle().getDataBankList().getDataBank().forEach(db -> {
				if (db != null && "GENBANK".equalsIgnoreCase(db.getDataBankName()) && db.getAccessionNumberList() != null
						&& db.getAccessionNumberList().getAccessionNumber() != null) {
					db.getAccessionNumberList().getAccessionNumber().forEach(an -> {
						if (isNotBlank(an.getvalue())) {
							sequences.add(an.getvalue().trim());
						}
					});
				}
			});
		}
		return sequences;
	}

}