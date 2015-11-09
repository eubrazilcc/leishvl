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
import static io.leishvl.core.data.mongodb.MapKeyConverters.escapeMongo;
import static io.leishvl.core.data.mongodb.MapKeyConverters.unescapeMongo;
import static io.leishvl.core.prov.ProvFactory.addEditProv;
import static io.leishvl.core.prov.ProvFactory.combineProv;
import static io.leishvl.core.prov.ProvFactory.newCustomObjectProv;
import static io.leishvl.core.prov.ProvFactory.newGenBankSequence;
import static io.leishvl.core.prov.ProvFactory.newObjectImportProv;
import static io.leishvl.core.prov.ProvFactory.newObsoleteProv;
import static io.leishvl.core.prov.ProvFactory.newPubMedArticle;
import static io.leishvl.core.prov.ProvFactory.newReleaseProv;
import static io.leishvl.core.prov.ProvWriter.provToFile;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.openprovenance.prov.model.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBObject;

import io.leishvl.core.security.User;
import io.leishvl.test.category.FunctionalGroupTests;
import io.leishvl.test.rules.TestPrinter;
import io.leishvl.test.rules.TestWatcher2;

/**
 * Tests common operations with W3C PROV specification.
 * @author Erik Torres <ertorser@upv.es>
 */
@Category(FunctionalGroupTests.class)
public class ProvenanceTests {

	@Rule
	public TemporaryFolder tmpFolder = new TemporaryFolder();

	@Rule
	public TestPrinter pw = new TestPrinter();

	@Rule
	public TestRule watchman = new TestWatcher2(pw);

	@Test
	public void testStandardFormatting() throws Exception {
		final File testOutputDir = tmpFolder.newFolder("testStandardFormatting");

		// create test dataset
		final User user1 = User.builder().userid("user1").build();
		final User user2 = User.builder().userid("user2").build();
		final List<Document> history = newArrayList();

		// citation imported from external data source (no coordinates provided)
		String testId = "prov-citation-pm-draft1";			
		Document prov = newObjectImportProv(newPubMedArticle("PMID-26148331"), "lvl-ci-pm-26148331", null);
		assertThat("prov document is not null", prov, notNullValue());
		assertThat("prov bundle is not null", prov.getStatementOrBundle(), notNullValue());
		assertThat("prov bundle is not empty", prov.getStatementOrBundle().isEmpty(), equalTo(false));
		File file = new File(testOutputDir, testId + ".json");
		provToFile(prov, file.getCanonicalPath());
		assertThat("prov JSON file exists", file.exists(), equalTo(true));
		assertThat("prov JSON file is not empty", file.length() > 0l, equalTo(true));
		file = new File(testOutputDir, testId + ".svg");
		provToFile(prov, file.getCanonicalPath());
		assertThat("prov SVG file exists", file.exists(), equalTo(true));
		assertThat("prov SVG file is not empty", file.length() > 0l, equalTo(true));
		history.add(prov);

		// draft modification
		testId = "prov-citation-pm-draft2";
		addEditProv(prov, user1, "lvl-ci-pm-26148331");
		file = new File(testOutputDir, testId + ".json");
		provToFile(prov, file.getCanonicalPath());
		assertThat("prov JSON file exists", file.exists(), equalTo(true));
		assertThat("prov JSON file is not empty", file.length() > 0l, equalTo(true));
		file = new File(testOutputDir, testId + ".svg");
		provToFile(prov, file.getCanonicalPath());
		assertThat("prov SVG file exists", file.exists(), equalTo(true));
		assertThat("prov SVG file is not empty", file.length() > 0l, equalTo(true));

		testId = "prov-citation-pm-draft3";
		addEditProv(prov, user2, "lvl-ci-pm-26148331");
		file = new File(testOutputDir, testId + ".json");
		provToFile(prov, file.getCanonicalPath());
		assertThat("prov JSON file exists", file.exists(), equalTo(true));
		assertThat("prov JSON file is not empty", file.length() > 0l, equalTo(true));
		file = new File(testOutputDir, testId + ".svg");
		provToFile(prov, file.getCanonicalPath());
		assertThat("prov SVG file exists", file.exists(), equalTo(true));
		assertThat("prov SVG file is not empty", file.length() > 0l, equalTo(true));			

		// new release
		testId = "prov-citation-pm-rel1";
		prov = newReleaseProv(user1, "lvl-ci-pm-26148331", "", "-rel1");
		assertThat("prov document is not null", prov, notNullValue());
		assertThat("prov bundle is not null", prov.getStatementOrBundle(), notNullValue());
		assertThat("prov bundle is not empty", prov.getStatementOrBundle().isEmpty(), equalTo(false));
		file = new File(testOutputDir, testId + ".json");
		provToFile(prov, file.getCanonicalPath());
		assertThat("prov JSON file exists", file.exists(), equalTo(true));
		assertThat("prov JSON file is not empty", file.length() > 0l, equalTo(true));
		file = new File(testOutputDir, testId + ".svg");
		provToFile(prov, file.getCanonicalPath());
		assertThat("prov SVG file exists", file.exists(), equalTo(true));
		assertThat("prov SVG file is not empty", file.length() > 0l, equalTo(true));
		history.add(prov);

		testId = "prov-citation-pm-rel2";
		prov = newReleaseProv(user1, "lvl-ci-pm-26148331", "-rel1", "-rel2");
		assertThat("prov document is not null", prov, notNullValue());
		assertThat("prov bundle is not null", prov.getStatementOrBundle(), notNullValue());
		assertThat("prov bundle is not empty", prov.getStatementOrBundle().isEmpty(), equalTo(false));
		file = new File(testOutputDir, testId + ".json");
		provToFile(prov, file.getCanonicalPath());
		assertThat("prov JSON file exists", file.exists(), equalTo(true));
		assertThat("prov JSON file is not empty", file.length() > 0l, equalTo(true));
		file = new File(testOutputDir, testId + ".svg");
		provToFile(prov, file.getCanonicalPath());
		assertThat("prov SVG file exists", file.exists(), equalTo(true));
		assertThat("prov SVG file is not empty", file.length() > 0l, equalTo(true));
		history.add(prov);			

		// record invalidation
		testId = "prov-citation-pm-inv";
		prov = newObsoleteProv(user1, "lvl-ci-pm-26148331");
		assertThat("prov document is not null", prov, notNullValue());
		assertThat("prov bundle is not null", prov.getStatementOrBundle(), notNullValue());
		assertThat("prov bundle is not empty", prov.getStatementOrBundle().isEmpty(), equalTo(false));
		file = new File(testOutputDir, testId + ".json");
		provToFile(prov, file.getCanonicalPath());
		assertThat("prov JSON file exists", file.exists(), equalTo(true));
		assertThat("prov JSON file is not empty", file.length() > 0l, equalTo(true));
		file = new File(testOutputDir, testId + ".svg");
		provToFile(prov, file.getCanonicalPath());
		assertThat("prov SVG file exists", file.exists(), equalTo(true));
		assertThat("prov SVG file is not empty", file.length() > 0l, equalTo(true));
		history.add(prov);			

		// combined record provenance
		testId = "prov-citation-pm-combined";
		prov = combineProv(history.toArray(new Document[history.size()]));
		file = new File(testOutputDir, testId + ".json");
		provToFile(prov, file.getCanonicalPath());
		assertThat("prov JSON file exists", file.exists(), equalTo(true));
		assertThat("prov JSON file is not empty", file.length() > 0l, equalTo(true));
		file = new File(testOutputDir, testId + ".svg");
		provToFile(prov, file.getCanonicalPath());
		assertThat("prov SVG file exists", file.exists(), equalTo(true));
		assertThat("prov SVG file is not empty", file.length() > 0l, equalTo(true));

		// user created citation
		testId = "prov-citation-ur";
		prov = newCustomObjectProv(user1, "lvl-ci-ur-MY_CIT");
		assertThat("prov document is not null", prov, notNullValue());
		assertThat("prov bundle is not null", prov.getStatementOrBundle(), notNullValue());
		assertThat("prov bundle is not empty", prov.getStatementOrBundle().isEmpty(), equalTo(false));
		file = new File(testOutputDir, testId + ".json");
		provToFile(prov, file.getCanonicalPath());
		assertThat("prov JSON file exists", file.exists(), equalTo(true));
		assertThat("prov JSON file is not empty", file.length() > 0l, equalTo(true));
		file = new File(testOutputDir, testId + ".svg");
		provToFile(prov, file.getCanonicalPath());
		assertThat("prov SVG file exists", file.exists(), equalTo(true));
		assertThat("prov SVG file is not empty", file.length() > 0l, equalTo(true));		
	}

	@Test
	public void testMongoDBMapping() throws Exception {
		final File testOutputDir = tmpFolder.newFolder("testMongoDBMapping");

		// test with plain JSON string
		final String json1 = "{ \"$a1$\": \"va1\", \"_a2$\": \"va2\", \"$a3$\": { \"$b1_\": \"vb1\", \"_b2$\": \"vb2\", \"$b3_\": { \"$c1$\": \"vc1\", \"_c2_\": \"vc2\" } }, "
				+ "\"_a4_\": \"va4\", \"$a5_\": \"va5\", \".a6$\": \"va6\", \"$a7.\": { \".b1.\": { \"_c1.\": \"vc1\" } }, \"...\": \"va8\" }";					
		DBObject obj = escapeMongo(json1);
		assertThat("not null DB object is created", obj, notNullValue());
		String json2 = unescapeMongo(obj);
		assertThat("not empty JSON string is created", trim(json2), allOf(notNullValue(), not(equalTo(""))));
		final ObjectMapper om = new ObjectMapper();
		@SuppressWarnings("unchecked")
		final Map<String, Object> map1 = (Map<String, Object>)(om.readValue(json1, Map.class));
		@SuppressWarnings("unchecked")
		final Map<String, Object> map2 = (Map<String, Object>)(om.readValue(json2, Map.class));
		assertThat("not null map is created from original JSON", map1, notNullValue());
		assertThat("not null map is created from parsed JSON", map2, notNullValue());
		assertThat("JSON strings coincide", map1.equals(map2), equalTo(true));
		// additional output
		pw.println("\n >> -- Original JSON : " + json1);
		pw.println("\n >> Mapped DB Object : " + obj.toMap());
		pw.println("\n >> ---- Parsed JSON : " + json2);

		// test with provenance document
		final Document prov = newObjectImportProv(newGenBankSequence("gb.123", "Sandflies"), "lvl.sf.gb.123", null);
		assertThat("prov document is not null", prov, notNullValue());
		assertThat("prov bundle is not null", prov.getStatementOrBundle(), notNullValue());
		assertThat("prov bundle is not empty", prov.getStatementOrBundle().isEmpty(), equalTo(false));

		final File file = new File(testOutputDir, "seq-gb.123.json");
		provToFile(prov, file.getCanonicalPath());
		assertThat("prov JSON file exists", file.exists(), equalTo(true));
		assertThat("prov JSON file is not empty", file.length() > 0l, equalTo(true));

		final String provJson = readFileToString(file, UTF_8.name());
		assertThat("not empty JSON string is created", trim(provJson), allOf(notNullValue(), not(equalTo(""))));
		obj = escapeMongo(provJson);
		assertThat("not null DB object is created", obj, notNullValue());
		json2 = unescapeMongo(obj);
		assertThat("not empty JSON string is created", trim(json2), allOf(notNullValue(), not(equalTo(""))));
		// additional output
		pw.println("\n >> -- W3C Prov JSON : " + provJson);
		pw.println("\n >> Mapped DB Object : " + obj.toMap());
		pw.println("\n >> ---- Parsed JSON : " + json2 + "\n");		
	}

}