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

package io.leishvl.core.test;

import static com.google.common.collect.Lists.newArrayList;
import static io.leishvl.core.LeishvlObjectState.DRAFT;
import static io.leishvl.core.jackson.JsonOptions.JSON_PRETTY_PRINTER;
import static io.leishvl.core.prov.ProvFactory.newGeocoding;
import static io.leishvl.core.prov.ProvFactory.newObjectImportProv;
import static io.leishvl.core.prov.ProvFactory.newPubMedArticle;
import static io.leishvl.core.xml.PubMedXmlBinder.PUBMED_XML_FACTORY;

import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import io.leishvl.core.Citation;
import io.leishvl.core.LeishvlArticle;
import io.leishvl.core.data.CitationRepository;
import io.leishvl.core.jackson.JsonProcessor;
import io.leishvl.core.ncbi.pubmed.PubmedArticle;
import io.leishvl.test.category.IntegrationTests;

/**
 * Tests {@link CitationRepository} class.
 * @author Erik Torres <ertorser@upv.es>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { IntegrationTestApplication.class })
@TestPropertySource(locations = { "classpath:test.properties" })
@IntegrationTest
public class CitationRepositoryTests {

	public static final String BASE_ID = "CITATION_";

	@Autowired
	private JsonProcessor jsonProcessor;

	// create geographic objects
	private final GeoJsonPoint bcnPoint = new GeoJsonPoint(2.1734034999999494d, 41.3850639d);
	private final GeoJsonPoint madPoint = new GeoJsonPoint(-3.7037901999999576d, 40.4167754d);
	private final GeoJsonPoint vlcPoint = new GeoJsonPoint(-0.3762881000000107d, 39.4699075d);
	private final GeoJsonPolygon polygon = new GeoJsonPolygon(new GeoJsonPoint(-4.0d, 30.0d), new GeoJsonPoint(3.0d, 30.0d),
			new GeoJsonPoint(3.0d, 42.0d), new GeoJsonPoint(-4.0d, 42.0d), new GeoJsonPoint(-4.0d, 30.0d));

	// create original PubMed articles
	private final PubmedArticle article0 = PUBMED_XML_FACTORY.createPubmedArticle()
			.withMedlineCitation(PUBMED_XML_FACTORY.createMedlineCitation()
					.withPMID(PUBMED_XML_FACTORY.createPMID().withvalue("EFGH5678"))
					.withDateCreated(PUBMED_XML_FACTORY.createDateCreated().withYear(PUBMED_XML_FACTORY.createYear().withvalue("2015")))
					.withArticle(PUBMED_XML_FACTORY.createArticle().withPubModel("Electronic-Print").withArticleTitle("The best paper in the world").withJournal(PUBMED_XML_FACTORY.createJournal().withTitle("Journal of Awesomeness")).withAbstract(PUBMED_XML_FACTORY.createAbstract().withAbstractText("This paper presents a text.")).withAuthorList(PUBMED_XML_FACTORY.createAuthorList().withAuthor(PUBMED_XML_FACTORY.createAuthor().withLastNameOrForeNameOrInitialsOrSuffixOrNameIDOrCollectiveName("John Doe"))).withPublicationTypeList(PUBMED_XML_FACTORY.createPublicationTypeList().withPublicationType(PUBMED_XML_FACTORY.createPublicationType().withvalue("Journal Article"))).withLanguage(PUBMED_XML_FACTORY.createLanguage().withvalue("eng")).withArticleDate(PUBMED_XML_FACTORY.createArticleDate().withYear(PUBMED_XML_FACTORY.createYear().withvalue("2015")))));

	private final PubmedArticle article1 = PUBMED_XML_FACTORY.createPubmedArticle()
			.withMedlineCitation(PUBMED_XML_FACTORY.createMedlineCitation()
					.withPMID(PUBMED_XML_FACTORY.createPMID().withvalue("ABCD1234"))
					.withDateCreated(PUBMED_XML_FACTORY.createDateCreated().withYear(PUBMED_XML_FACTORY.createYear().withvalue("2014")))
					.withArticle(PUBMED_XML_FACTORY.createArticle().withPubModel("Electronic-Print").withArticleTitle("Rocket science rocks").withJournal(PUBMED_XML_FACTORY.createJournal().withTitle("Journal of Awesomeness")).withAbstract(PUBMED_XML_FACTORY.createAbstract().withAbstractText("It rocks! There is no much to say.")).withAuthorList(PUBMED_XML_FACTORY.createAuthorList().withAuthor(PUBMED_XML_FACTORY.createAuthor().withLastNameOrForeNameOrInitialsOrSuffixOrNameIDOrCollectiveName("Jane Doe"))).withPublicationTypeList(PUBMED_XML_FACTORY.createPublicationTypeList().withPublicationType(PUBMED_XML_FACTORY.createPublicationType().withvalue("Journal Article"))).withLanguage(PUBMED_XML_FACTORY.createLanguage().withvalue("eng")).withArticleDate(PUBMED_XML_FACTORY.createArticleDate().withYear(PUBMED_XML_FACTORY.createYear().withvalue("2014")))));

	private final String pmid0 = article0.getMedlineCitation().getPMID().getvalue();
	private final String pmid1 = article1.getMedlineCitation().getPMID().getvalue();

	private final Citation citation0 = Citation.builder()
			.leishvlId(BASE_ID + 0)
			.leishvl(LeishvlArticle.builder().cited(newArrayList("SEQ_0", "SEQ_1")).build())
			.pubmed(article0)
			.location(bcnPoint)
			.state(DRAFT)
			.provenance(newObjectImportProv(newPubMedArticle("PMID-" + pmid0), "lvl-ci-ur-" + BASE_ID + 0, newGeocoding(bcnPoint)))
			.references(Maps.<String, List<String>>newHashMap(ImmutableMap.of("sequences", newArrayList("lvl-sf-gb-SEQ_0", "lvl-le-gb-SEQ_1"))))
			.build();

	private final Citation citation1 = Citation.builder()
			.leishvlId(BASE_ID + 1)
			.pubmed(article1)
			.location(madPoint)
			.build();

	private @Autowired CitationRepository repository;

	@Test @Category(IntegrationTests.class)
	public void readsFirstPageCorrectly() {
		repository.deleteAll();

		// save a couple of citations
		ImmutableList.of(citation0, citation1).stream().forEach(citation -> {
			repository.save(citation);
		});		

		// fetch all customers
		System.out.println("Citations found with findAll():");
		System.out.println("-------------------------------");
		repository.findAll().stream().forEach(citation -> {
			System.out.println(jsonProcessor.toJson(citation, JSON_PRETTY_PRINTER));
		});
		System.out.println();

		// fetch an individual customer
		System.out.println("Customer found with findByNamespace('Alice'):");
		System.out.println("--------------------------------");
		System.out.println(repository.findByNamespace("Alice", new PageRequest(0, 20, Sort.Direction.ASC, "pubmed.medlineCitation.pmid.value")));

		System.out.println("Customers found with findByLastName('Smith'):");
		System.out.println("--------------------------------");
		/* TODO for (final Citation citation : repository.findByLastName("Smith")) {
			System.out.println(citation);
		} */

		// Page<Person> persons = repository.findAll(new PageRequest(0, 10));
		// assertThat(persons.isFirstPage(), is(true));
	}

}