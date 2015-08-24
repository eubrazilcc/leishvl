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

import com.google.common.collect.ImmutableMap;
import static com.google.common.collect.Maps.newHashMap;
import com.google.common.collect.Maps;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.leishvl.core.xml.ncbi.pubmed.PubmedArticle;
import io.leishvl.storage.mongodb.MongoConnector;
import io.leishvl.storage.security.User;
import io.vertx.core.*;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.impl.Closeable;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.geojson.Point;
import org.geojson.Polygon;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.leishvl.storage.prov.ProvFactory.newGeocoding;
import static io.leishvl.storage.prov.ProvFactory.newObjectImportProv;
import static io.leishvl.storage.prov.ProvFactory.newPubMedArticle;

import static io.leishvl.storage.base.StorageDefaults.STO_OPERATION_TIMEOUT_SECS;
import static io.leishvl.storage.mongodb.MongoConnectors.createShared;
import static io.leishvl.storage.mongodb.jackson.MongoJsonOptions.JSON_PRETTY_PRINTER;
import static io.leishvl.core.xml.XmlHelper.prettyPrint;

import static io.leishvl.core.util.GeoJsons.pointBuilder;
import static io.leishvl.core.util.GeoJsons.lngLatAltBuilder;
import static io.leishvl.core.util.GeoJsons.polygonBuilder;

import static io.leishvl.core.xml.PubMedXmlBinder.PUBMED_XML_FACTORY;
import static io.leishvl.core.xml.PubMedXmlBinder.PUBMED_XMLB;

import io.leishvl.storage.base.ObjectState;
import static io.leishvl.storage.base.ObjectState.DRAFT;
import static io.leishvl.storage.base.ObjectState.RELEASE;
import static io.vertx.core.Future.succeededFuture;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;
import static com.google.common.collect.Iterables.frequency;
import static com.google.common.collect.Lists.newArrayList;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Tests {@link Citation} collection.
 * @author Erik Torres <ertorser@upv.es>
 */
@RunWith(VertxUnitRunner.class)
public class CitationCollectionTest {

    public static final String ID_0 = "CITATION_0";
    public static final String ID_1 = "CITATION_1";

    private Vertx vertx;
    private JsonObject dbConfig;
    private TestVerticle verticle;
    private String deploymentID;

    @Before
    public void before(final TestContext testCtxt) {
        // load default configuration
        final Config appConfig = ConfigFactory.load();
        // create deployment options
        final Map<String, Object> verticleConfig = ImmutableMap.<String, Object>builder()
                .put("db_name", appConfig.getString("leishvl.database.prefix") + "_collection")
                .put("hosts", appConfig.getConfigList("leishvl.database.hosts").stream().map(input ->
                                new JsonObject().put("host", input.getString("host")).put("port", input.getInt("port"))
                ).filter(host -> host != null).collect(Collectors.toList()))
                .put("replica_set", appConfig.getString("leishvl.database.replica-set"))
                .put("user", appConfig.getString("leishvl.database.security.user"))
                .put("pwd", appConfig.getString("leishvl.database.security.pwd"))
                .build();
        dbConfig = new JsonObject(verticleConfig);
        final DeploymentOptions deploymentOptions = new DeploymentOptions().setConfig(dbConfig);
        // deploy verticle
        vertx = Vertx.vertx();
        verticle = new TestVerticle();
        vertx.deployVerticle(verticle, deploymentOptions, testCtxt.asyncAssertSuccess(deploymentID -> {
            this.deploymentID = deploymentID;
            System.out.println(" >> New verticle deployed: " + this.deploymentID);
        }));
    }

    @After
    public void after(final TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void test(final TestContext context) {
        System.out.println("CitationCollectionTest.test()");
        try {
            // create test data-set
            final TestDataset ds = new TestDataset(true);
            verticle.setDs(ds);
            // uncomment for additional output
            System.out.println(" >> New test data-set created");

            // insert a new citation in the database
            final EventBus eb = vertx.eventBus();
            eb.send("insert.citation0", "insert citation0", ar -> {
                context.assertTrue(ar.succeeded(), "Citation0 was inserted into the database");
                System.out.println(" >> Received reply: " + ar.result().body());
                assertThat("inserted Id is not empty", trim(ds.citation0.getDbId()), allOf(notNullValue(), not(equalTo(""))));
                assertThat("inserted version is empty", isBlank(ds.citation0.getVersion()), equalTo(true));
                assertThat("last modified field is not null", ds.citation0.getLastModified(), notNullValue());
                // uncomment for additional output
                System.out.println(" >> Inserted citation (" + ds.citation0.getDbId() + "):\n" + ds.citation0.toJson(JSON_PRETTY_PRINTER));
            });

            // find citation by global id
            eb.send("fetch.citation0", "fetch citation0", ar -> {
                context.assertTrue(ar.succeeded(), "Citation0 was fetched from the database");
                System.out.println(" >> Received reply: " + ar.result().body());
                assertThat("fetched citation coincides with expected", ds.citation2, equalTo(ds.citation0));
                // uncomment for additional output
                System.out.println(" >> Fetched citation (" + ds.citation2.getDbId() + "):\n" + ds.citation2.toJson(JSON_PRETTY_PRINTER));
            });


            // TODO
            Thread.sleep(10000l);
            // TODO

            /*

     // insert a second citation in the database
     ds.citation1.save().get(STO_OPERATION_TIMEOUT_SECS, SECONDS);
     assertThat("inserted Id is not empty", trim(ds.citation1.getDbId()), allOf(notNullValue(), not(equalTo(""))));
     assertThat("inserted version is empty", isBlank(ds.citation1.getVersion()), equalTo(true));
     assertThat("last modified field is not null", ds.citation1.getLastModified(), notNullValue());
     // uncomment for additional output
     System.out.println(" >> Inserted citation (" + ds.citation1.getDbId() + "):\n" + ds.citation1.toJson(JSON_PRETTY_PRINTER));

             */


        } catch (Exception e) {
            e.printStackTrace(System.err);
            fail("CitationCollectionTest.test() failed: " + e.getMessage());
        } finally {
            verticle.close(context.asyncAssertSuccess());
            System.out.println("CitationCollectionTest.test() has finished");
        }
    }

    /**
     * Tests creation, modification and search of objects which state is draft.
     *
     @Test
     public void test01DraftState(final TestContext testCtxt) {
     System.out.println("CitationCollectionTest.test01DraftState()");
     try {

     // TODO

     // operate on the test data-set
     final Resultset rs = new Resultset(new TestScenario[]{
     new TestScenario("TestDraft", TestState.ALL, new ObjectState[] { DRAFT, DRAFT }),
     new TestScenario("TestDraft", TestState.RELEASES, new ObjectState[] { DRAFT, DRAFT })
     });
     operateOnTestDatasets(ds, rs, true);

     } catch (Exception e) {
     e.printStackTrace(System.err);
     fail("CitationCollectionTest.test01DraftState() failed: " + e.getMessage());
     } finally {
     try {
     new Citations().drop().get(STO_OPERATION_TIMEOUT_SECS, SECONDS);
     } catch (Exception ignore) { }
     System.out.println("CitationCollectionTest.test01DraftState() has finished");
     }
     }

     private void operateOnTestDatasets(final TestDataset ds, final Resultset rs, final boolean testUpdate) throws Exception {
     // TODO
     }

     */

    private class TestDataset {
        // create users
        private final User user0 = User.builder().userid("user0").build();
        private final User user1 = User.builder().userid("user1").build();

        // create geographic objects
        private final Point bcnPoint = pointBuilder().coordinates(lngLatAltBuilder().coordinates( 2.1734034999999494d, 41.3850639d).build()).build();
        private final Point madPoint = pointBuilder().coordinates(lngLatAltBuilder().coordinates(-3.7037901999999576d, 40.4167754d).build()).build();
        private final Point vlcPoint = pointBuilder().coordinates(lngLatAltBuilder().coordinates(-0.3762881000000107d, 39.4699075d).build()).build();
        private final Polygon polygon = polygonBuilder().exteriorRing(
                lngLatAltBuilder().coordinates(-4.0d, 30.0d).build(),
                lngLatAltBuilder().coordinates( 3.0d, 30.0d).build(),
                lngLatAltBuilder().coordinates( 3.0d, 42.0d).build(),
                lngLatAltBuilder().coordinates(-4.0d, 42.0d).build(),
                lngLatAltBuilder().coordinates(-4.0d, 30.0d).build()).build();

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

        // create citations
        private final String pmid0 = article0.getMedlineCitation().getPMID().getvalue();
        private final Citation citation0 = Citation.builder()
                .vertx(vertx)
                .config(dbConfig)
                .leishvlId(ID_0)
                .leishvl(LeishvlCitation.builder().cited(newArrayList("SEQ_0", "SEQ_1")).build())
                .pubmed(article0)
                .location(bcnPoint)
                .state(DRAFT)
                .provenance(newObjectImportProv(newPubMedArticle("PMID-" + pmid0), "lvl-ci-ur-" + ID_0, newGeocoding(bcnPoint)))
                .references(Maps.<String, List<String>>newHashMap(ImmutableMap.of("sequences", newArrayList("lvl-sf-gb-SEQ_0", "lvl-le-gb-SEQ_1"))))
                .build();

        private final Citation citation1 = Citation.builder()
                .vertx(vertx)
                .config(dbConfig)
                .leishvlId(ID_1)
                .pubmed(article1)
                .location(madPoint)
                .build();

        private Citation citation2 = null;
        private final Citations citations = new Citations(vertx, dbConfig);

        private Map<String, Integer> versions = newHashMap(ImmutableMap.of(ID_0, 1, ID_1, 1));

        public TestDataset(final boolean verbose) throws IOException {
            if (verbose) {
                // uncomment for additional output
                System.out.println(" >> Original PubMed article:\n" + prettyPrint(PUBMED_XMLB.typeToXml(article0)));
            }
        }
    }

    private static class Resultset {

        private TestScenario[] scenarios;

        public Resultset(final TestScenario[] scenarios) {
            this.scenarios = scenarios;
        }

        public String toString() {
            return Arrays.toString(scenarios);
        }

    }

    private static class TestScenario {

        private final String name;
        private final TestState state;
        private final ObjectState[] states;

        public TestScenario(final String name, final TestState state, final ObjectState[] states) {
            this.name = name;
            this.state = state;
            this.states = states;
        }

        public int total() {
            return states.length;
        }

        public int numReleases() {
            return frequency(asList(states), RELEASE);
        }

        public int numItems(final Integer... excludes) {
            return numItems(null, excludes);
        }

        public int numItems(final int[] stateCount, final Integer... excludes) {
            int count = 0;
            switch (state) {
                case ALL:
                    count = total() + sum(stateCount) - (excludes != null ? excludes.length : 0);
                    break;
                case RELEASES:
                    count = numReleases() + (stateCount != null ? stateCount[1] : 0) - numExcluded(excludes, RELEASE);
                    break;
                default:
                    throw new IllegalStateException("Unknown test state");
            }
            return count;
        }

        private int numExcluded(final Integer[] excludes, final ObjectState state) {
            int numExcluded = 0;
            if (excludes != null) {
                for (final int pos : excludes) {
                    numExcluded += (pos < states.length && states[pos].equals(state) ? 1 : 0);
                }
            }
            return numExcluded;
        }

        private int sum(final int[] stateCount) {
            return stateCount != null ? Arrays.stream(stateCount).sum() : 0;
        }

        public String toString() {
            return "[name=" + this.name + ", state=" + this.state + "]";
        }

    }

    private enum TestState {
        ALL,
        RELEASES
    }

    /**
     * Test verticle.
     * @author Erik Torres <ertorser@upv.es>
     */
    public class TestVerticle extends AbstractVerticle implements Closeable {

        private TestDataset ds;

        public void setDs(final TestDataset ds) {
            this.ds = ds;
        }

        @Override
        public void start() throws Exception {
            final EventBus eb = vertx.eventBus();
            eb.consumer("insert.citation0", message -> {
                System.out.println(" >> Message received: " + message.body());
                ds.citation0.save(lookup -> {
                    if (lookup.succeeded()) {
                        message.reply("Citation0 inserted");
                        System.out.println(" >> Sent back response to caller");
                    } else message.fail(1, lookup.cause().getMessage());
                });
            });
            eb.consumer("fetch.citation0", message -> {
                System.out.println(" >> Message received: " + message.body());
                ds.citation2 = Citation.builder().vertx(vertx).config(config()).leishvlId(ID_0).build();
                ds.citation2.fetch(lookup -> {
                    if (lookup.succeeded()) {
                        message.reply("Citation0 found");
                        System.out.println(" >> Sent back response to caller");
                    } else message.fail(1, lookup.cause().getMessage());
                });
            });
            System.out.println("TestVerticle started");
        }

        @Override
        public void close(final Handler<AsyncResult<Void>> completionHandler) {
            vertx.runOnContext(v -> completionHandler.handle(succeededFuture(null)));
        }

    }

}