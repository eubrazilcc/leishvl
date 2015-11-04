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
import static com.google.common.collect.Sets.newHashSet;
import static io.leishvl.core.util.LocaleUtils.getLocale;
import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;

import com.google.common.collect.ImmutableMultimap;

import io.leishvl.core.ncbi.gb.GBFeature;
import io.leishvl.core.ncbi.gb.GBQualifier;
import io.leishvl.core.ncbi.gb.GBSeq;
import io.leishvl.core.ncbi.gb.GBSeqReferences;
import io.leishvl.core.ncbi.gb.GBSeqid;
import io.leishvl.core.ncbi.gb.GBSet;
import io.leishvl.core.ncbi.gb.ObjectFactory;

/**
 * NCBI GenBank Sequence XML binding helper.
 * @author Erik Torres <ertorser@upv.es>
 */
public final class GbSeqXmlBinder extends XmlBinder {

	protected final static Logger LOGGER = getLogger(GbSeqXmlBinder.class);

	public static final String SUBMITTER_BLOCK_TITLE = "Direct Submission";

	private static final Class<?>[] SUPPORTED_CLASSES = {
			GBSet.class,
			GBSeq.class
	};

	public static final ObjectFactory GBSEQ_XML_FACTORY = new ObjectFactory();

	public static final GbSeqXmlBinder GBSEQ_XMLB = new GbSeqXmlBinder();

	private GbSeqXmlBinder() {
		super(SUPPORTED_CLASSES);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> JAXBElement<T> createType(final T obj) {
		Object element;
		Class<?> clazz = obj.getClass();
		if (clazz.equals(GBSet.class)) {
			element = GBSEQ_XML_FACTORY.createGBSet();
		} else if (clazz.equals(GBSeq.class)) {
			element = GBSEQ_XML_FACTORY.createGBSeq();
		} else {
			throw new IllegalArgumentException("Unsupported type: " + clazz.getCanonicalName());
		}
		return (JAXBElement<T>) element;
	}

	/**
	 * Gets the GenInfo identifier (gi) from a sequence.
	 * @param gbSeq - sequence to be analyzed
	 * @return if found, the GenInfo identifier (gi), otherwise {@code null}.
	 */
	public static @Nullable Integer getGenInfoIdentifier(final GBSeq gbSeq) {
		checkArgument(gbSeq != null && gbSeq.getGBSeqOtherSeqids() != null && gbSeq.getGBSeqOtherSeqids().getGBSeqid() != null,
				"Uninitialized or invalid sequence.");
		String gi = null;
		final List<GBSeqid> ids = gbSeq.getGBSeqOtherSeqids().getGBSeqid();
		for (int i = 0; i < ids.size() && gi == null; i++) {
			final Pattern pattern = compile("(gi\\|\\d+)");
			final Matcher matcher = pattern.matcher(ids.get(i).getvalue());
			if (matcher.find()) {
				gi = trimToNull(matcher.group().substring(3));
			}
		}
		return isNumeric(gi) ? parseInt(gi) : null;
	}

	/**
	 * Gets the country feature from a sequence.
	 * @param sequence - sequence to be analyzed
	 * @return the value of the country feature or {@code null}.
	 */
	public static @Nullable String countryFeature(final GBSeq sequence) {
		checkArgument(sequence != null, "Uninitialized or invalid sequence");
		String country = null;
		if (sequence.getGBSeqFeatureTable() != null && sequence.getGBSeqFeatureTable().getGBFeature() != null) {
			final List<GBFeature> features = sequence.getGBSeqFeatureTable().getGBFeature();
			for (int i = 0; i < features.size() && country == null; i++) {
				final GBFeature feature = features.get(i);
				if (feature.getGBFeatureQuals() != null && feature.getGBFeatureQuals().getGBQualifier() != null) {
					final List<GBQualifier> qualifiers = feature.getGBFeatureQuals().getGBQualifier();
					for (int j = 0; j < qualifiers.size() && country == null; j++) {
						final GBQualifier qualifier = qualifiers.get(j);
						if ("country".equals(qualifier.getGBQualifierName())) {
							country = qualifier.getGBQualifierValue();
						}
					}
				}
			}
		}
		return trimToNull(country);
	}

	/**
	 * Infers the possible countries of the species from which the DNA sequence was obtained and 
	 * returns a map of Java {@link Locale} where the key of the map is the GenBank field that was
	 * used to infer the country. The country is inferred from the annotations of the GenBank file 
	 * format, using the fields in the following order:
	 * <ol>
	 * <li>If a country entry exists in the features of the file, then this is returned to 
	 * the caller and no other check is performed;</li>
	 * <li>Definition field;</li>
	 * <li>Title field; or</li>
	 * <li>Check PubMed title and abstract fields.</li>
	 * </ol>
	 * @param sequence - sequence to be analyzed
	 * @return a map of Java {@link Locale} inferred from the input sequence, where the key of the map
	 *         is the GenBank field used to infer the country.
	 */
	public static ImmutableMultimap<String, Locale> inferCountry(final GBSeq sequence) {
		checkArgument(sequence != null, "Uninitialized or invalid sequence");
		final ImmutableMultimap.Builder<String, Locale> builder = new ImmutableMultimap.Builder<>();
		// infer from features
		final String countryFeature = countryFeature(sequence);
		Locale locale = isNotBlank(countryFeature) ? countryFeatureToLocale(countryFeature) : null;
		if (locale != null) {
			builder.put("features", locale);
		} else {
			// infer from definition
			// TODO

			// infer from title
			// TODO

			// infer from PubMed title and abstract fields
			// TODO
		}
		return builder.build();
	}

	/**
	 * Converts country feature to Java {@link Locale}. Java {@link Locale} allows latter to export the 
	 * country to several different formats, including a two-letter code compatible with ISO 3166-1 
	 * alpha-2 standard.
	 * @param countryFeature - value of country feature field
	 * @return a Java {@link Locale} inferred from the input sequence.
	 */
	public static Locale countryFeatureToLocale(final String countryFeature) {
		checkArgument(isNotBlank(countryFeature), "Uninitialized or invalid country feature");
		return getLocale(countryFeature.replace(":.*", ""));
	}

	/**
	 * Parses publication references from a GenBank entry. The submitter block as well as non-PubMed publications
	 * are excluded from the list of references returned by this method. A message will be written to the log system
	 * when a reference is excluded for any of the reasons mentioned before.
	 * @param sequence - GenBank sequence entry
	 * @return a list that contains the references to published work included in the input GenBank sequence that are 
	 *         included PubMed (has a valid PMID). Duplicated references are removed from the returned list.
	 */
	public static Set<String> getPubMedIds(final GBSeq sequence) {
		checkArgument(sequence != null, "Uninitialized or invalid sequence");
		final Set<String> references = newHashSet();
		final GBSeqReferences gbRefs = sequence.getGBSeqReferences();
		if (gbRefs != null && gbRefs.getGBReference() != null) {
			gbRefs.getGBReference().forEach(gbRef -> {
				if (gbRef != null) {
					final String pmid = trimToNull(gbRef.getGBReferencePubmed());
					if (pmid != null) {
						references.add(pmid);
					} else if (SUBMITTER_BLOCK_TITLE.equals(gbRef.getGBReferenceTitle())) {
						LOGGER.trace("Ignoring submitter block in GenBank sequence: " + sequenceId(sequence));
					} else {
						LOGGER.info("Ignoring non-PubMed publication in GenBank sequence: " + sequenceId(sequence));
					}
				}
			});
		}
		return references;
	}

	private static List<GBFeature> getFeatures(final GBSeq sequence) {
		checkArgument(sequence != null, "Uninitialized or invalid sequence");
		List<GBFeature> features = null;
		if (sequence.getGBSeqFeatureTable() != null && sequence.getGBSeqFeatureTable().getGBFeature() != null) {
			features = sequence.getGBSeqFeatureTable().getGBFeature();
		}
		return ofNullable(features).orElse(new ArrayList<>());
	}

	/**
	 * Parses gene name (and possible synonyms) found in a GenBank entry.
	 * @param sequence - GenBank sequence entry
	 * @return a deduplicated list of gene names.
	 */
	public static Set<String> getGeneNames(final GBSeq sequence) {
		checkArgument(sequence != null, "Uninitialized or invalid sequence");
		return getFeatures(sequence).stream()
				.map(feature -> {
					String name = null;
					if (feature != null && "gene".equals(feature.getGBFeatureKey()) && feature.getGBFeatureQuals() != null
							&& feature.getGBFeatureQuals().getGBQualifier() != null) {
						final List<GBQualifier> quals = feature.getGBFeatureQuals().getGBQualifier();
						for (int i = 0; i < quals.size() && name == null; i++) {
							final GBQualifier qualifier = quals.get(i);
							if (qualifier != null && "gene".equals(qualifier.getGBQualifierName()) && isNotBlank(qualifier.getGBQualifierValue())) {
								name = qualifier.getGBQualifierValue().trim();
							}
						}
					}
					return name;
				})
				.filter(name -> name != null)
				.collect(Collectors.toSet());
	}

	public static String sequenceId(final GBSeq gbSeq) {
		return "AC:" + gbSeq.getGBSeqPrimaryAccession() + ", GI:" + getGenInfoIdentifier(gbSeq);
	}

}