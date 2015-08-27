package io.leishvl.storage;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.leishvl.core.xml.ncbi.pubmed.PubmedArticle;
import io.leishvl.storage.base.ObjectState;
import io.leishvl.storage.security.User;
import io.leishvl.test.vertx.MoreMatcherAssert;
import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.impl.Closeable;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.report.ReportOptions;
import org.geojson.Point;
import org.geojson.Polygon;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterables.frequency;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static io.leishvl.core.util.GeoJsons.lngLatAltBuilder;
import static io.leishvl.core.util.GeoJsons.pointBuilder;
import static io.leishvl.core.util.GeoJsons.polygonBuilder;
import static io.leishvl.core.xml.PubMedXmlBinder.PUBMED_XMLB;
import static io.leishvl.core.xml.PubMedXmlBinder.PUBMED_XML_FACTORY;
import static io.leishvl.core.xml.XmlHelper.prettyPrint;
import static io.leishvl.storage.base.ObjectState.DRAFT;
import static io.leishvl.storage.base.ObjectState.RELEASE;
import static io.leishvl.storage.mongodb.jackson.MongoJsonOptions.JSON_PRETTY_PRINTER;
import static io.leishvl.storage.prov.ProvFactory.newGeocoding;
import static io.leishvl.storage.prov.ProvFactory.newObjectImportProv;
import static io.leishvl.storage.prov.ProvFactory.newPubMedArticle;
import static io.vertx.core.Future.succeededFuture;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * Tests {@link Citation} collection.
 * @author Erik Torres <ertorser@upv.es>
 */
public class CitationCollectionTest {

    public static final String BASE_ID = "CITATION_";
    public static final String ID_0 = BASE_ID + 0;
    public static final String ID_1 = BASE_ID + 1;

    private Vertx vertx;

    @Test
    public void test() {
        System.out.println(" >> CitationCollectionTest.test() is starting");
        try {
            final TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console")).setTimeout(120000l);
            final TestSuite suite = TestSuite.create("io.leishvl.storage.CitationCollectionTest");

            suite.test("test1", context -> {
                System.out.println(" >> Test 1");
                // insert a new citation in the database
                final TestVerticle verticle = context.get("verticle");
                final TestDataset ds = verticle.ds;
                final EventBus eb = vertx.eventBus();
                final MoreMatcherAssert matcher = new MoreMatcherAssert(context);
                eb.send("insert.citation", 0, ar -> {
                    context.assertTrue(ar.succeeded(), "Citation0 was inserted into the database [error=" + ar.cause() + "].");
                    System.out.println(" >> Received reply: " + ar.result().body());
                    matcher.assertThat("inserted Id is not empty", trim(ds.citation0.getDbId()), allOf(notNullValue(), not(equalTo(""))));
                    matcher.assertThat("inserted version is empty", isBlank(ds.citation0.getVersion()), equalTo(true));
                    matcher.assertThat("last modified field is not null", ds.citation0.getLastModified(), notNullValue());
                    // uncomment for additional output
                    System.out.println(" >> Inserted citation (" + ds.citation0.getDbId() + "):\n" + ds.citation0.toJson(JSON_PRETTY_PRINTER));

                    // find citation by global id
                    eb.send("fetch.citation", 0, ar2 -> {
                        context.assertTrue(ar2.succeeded(), "Citation0 was fetched from the database [error=" + ar2.cause() + "].");
                        System.out.println(" >> Received reply: " + ar2.result().body());
                        matcher.assertThat("fetched citation coincides with expected", ds.citation2, equalTo(ds.citation0));
                        // uncomment for additional output
                        System.out.println(" >> Fetched citation (" + ds.citation2.getDbId() + "):\n" + ds.citation2.toJson(JSON_PRETTY_PRINTER));
                    });
                });
            });

            suite.test("test2", context -> {
                System.out.println(" >> Test 2");
                // insert a second citation in the database
                final TestVerticle verticle = context.get("verticle");
                final TestDataset ds = verticle.ds;
                final EventBus eb = vertx.eventBus();
                final MoreMatcherAssert matcher = new MoreMatcherAssert(context);
                eb.send("insert.citation", 1, ar -> {
                    context.assertTrue(ar.succeeded(), "Citation1 was inserted into the database [error=" + ar.cause() + "].");
                    System.out.println(" >> Received reply: " + ar.result().body());
                    matcher.assertThat("inserted Id is not empty", trim(ds.citation1.getDbId()), allOf(notNullValue(), not(equalTo(""))));
                    matcher.assertThat("inserted version is empty", isBlank(ds.citation1.getVersion()), equalTo(true));
                    matcher.assertThat("last modified field is not null", ds.citation1.getLastModified(), notNullValue());
                    // uncomment for additional output
                    System.out.println(" >> Inserted citation (" + ds.citation1.getDbId() + "):\n" + ds.citation1.toJson(JSON_PRETTY_PRINTER));

                    // find citation by global id
                    eb.send("fetch.citation", 1, ar2 -> {
                        context.assertTrue(ar2.succeeded(), "Citation1 was fetched from the database [error=" + ar2.cause() + "].");
                        System.out.println(" >> Received reply: " + ar2.result().body());
                        matcher.assertThat("fetched citation coincides with expected", ds.citation2, equalTo(ds.citation1));
                        // uncomment for additional output
                        System.out.println(" >> Fetched citation (" + ds.citation2.getDbId() + "):\n" + ds.citation2.toJson(JSON_PRETTY_PRINTER));
                    });
                });
            });

            suite.before(context -> {
                // create test verticle
                createTestVerticle(context);
                // create test data-set
                final TestVerticle verticle = context.get("verticle");
                verticle.setDs(new TestDataset(context, true));
                // uncomment for additional output
                System.out.println(" >> New test data-set created");
            }).after(context -> destroyTestVerticle(context)).run(options).awaitSuccess();
        } finally {
            System.out.println(" >> CitationCollectionTest.test() has finished");
        }
    }

    private void createTestVerticle(final TestContext context) {
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
        final JsonObject dbConfig = new JsonObject(verticleConfig);
        final DeploymentOptions deploymentOptions = new DeploymentOptions().setConfig(dbConfig);
        context.put("dbConfig", dbConfig);
        // deploy verticle
        vertx = Vertx.vertx();
        final TestVerticle verticle = new TestVerticle();
        context.put("verticle", verticle);
        vertx.deployVerticle(verticle, deploymentOptions, context.asyncAssertSuccess(deploymentID -> {
            context.put("deploymentID", deploymentID);
            System.out.println(" >> New verticle deployed: " + deploymentID);
        }));
    }

    private void destroyTestVerticle(final TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    private class TestDataset {
        private final TestContext context;

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
        private final Citation citation0;
        private final Citation citation1;
        private Citation citation2 = null;
        private final Citations citations;

        private Map<String, Integer> versions = newHashMap(ImmutableMap.of(ID_0, 1, ID_1, 1));

        public TestDataset(final TestContext context, final boolean verbose) {
            this.context = context;
            citation0 = Citation.builder()
                    .vertx(vertx)
                    .config(context.get("dbConfig"))
                    .leishvlId(ID_0)
                    .leishvl(LeishvlCitation.builder().cited(newArrayList("SEQ_0", "SEQ_1")).build())
                    .pubmed(article0)
                    .location(bcnPoint)
                    .state(DRAFT)
                    .provenance(newObjectImportProv(newPubMedArticle("PMID-" + pmid0), "lvl-ci-ur-" + ID_0, newGeocoding(bcnPoint)))
                    .references(Maps.<String, List<String>>newHashMap(ImmutableMap.of("sequences", newArrayList("lvl-sf-gb-SEQ_0", "lvl-le-gb-SEQ_1"))))
                    .build();
            citation1 = Citation.builder()
                    .vertx(vertx)
                    .config(context.get("dbConfig"))
                    .leishvlId(ID_1)
                    .pubmed(article1)
                    .location(madPoint)
                    .build();
            citations = new Citations(vertx, context.get("dbConfig"));
            if (verbose) {
                try {
                    System.out.println(" >> Original PubMed article:\n" + prettyPrint(PUBMED_XMLB.typeToXml(article0)));
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
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
            eb.consumer("insert.citation", message -> {
                System.out.println(" >> Message received: " + message.body());
                final int citationId = (Integer)message.body();
                final Citation citation = getCitation(citationId);
                citation.save(lookup -> {
                    if (lookup.succeeded()) {
                        message.reply("Citation " + citationId + " inserted");
                        System.out.println(" >> Sent back response to caller");
                    } else message.fail(1, lookup.cause().getMessage());
                });
            });
            eb.consumer("fetch.citation", message -> {
                System.out.println(" >> Message received: " + message.body());
                final int citationId = (Integer)message.body();
                ds.citation2 = Citation.builder().vertx(vertx).config(config()).leishvlId(BASE_ID + citationId).build();
                ds.citation2.fetch(lookup -> {
                    if (lookup.succeeded()) {
                        message.reply("Citation " + citationId + " found");
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

        private Citation getCitation(final int citationId) {
            Citation citation;
            switch (citationId) {
                case 0: {
                    citation = ds.citation0;
                    break;
                }
                case 1: {
                    citation = ds.citation1;
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Invalid citation Id: " + citationId);
                }
            }
            return citation;
        }

    }

}