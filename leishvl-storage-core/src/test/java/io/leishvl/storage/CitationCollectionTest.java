package io.leishvl.storage;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.leishvl.core.xml.ncbi.pubmed.PubmedArticle;
import io.leishvl.storage.base.LeishvlCollection;
import io.leishvl.storage.base.LeishvlObject;
import io.leishvl.storage.base.ObjectState;
import io.leishvl.storage.mongodb.MongoCollectionStats;
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
import org.geojson.FeatureCollection;
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
import static io.leishvl.core.util.GeoJsons.*;
import static io.leishvl.core.xml.PubMedXmlBinder.PUBMED_XML_FACTORY;
import static io.leishvl.storage.base.ObjectState.DRAFT;
import static io.leishvl.storage.base.ObjectState.RELEASE;
import static io.leishvl.storage.mongodb.jackson.MongoJsonMapper.JSON_MAPPER;
import static io.leishvl.storage.mongodb.jackson.MongoJsonOptions.JSON_PRETTY_PRINTER;
import static io.leishvl.storage.prov.ProvFactory.*;
import static io.vertx.core.Future.succeededFuture;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Tests {@link Citation} collection.
 * @author Erik Torres <ertorser@upv.es>
 */
public class CitationCollectionTest {

    public static final int[] EMPTY_ARRAY = {};

    public static final boolean VERBOSE = false;
    public static final String BASE_ID = "CITATION_";

    private Vertx vertx;

    @Test
    public void testSuiteDraftState() {
        final TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console").setFormat("simple")).setTimeout(120000l);
        final TestSuite suite = TestSuite.create(CitationCollectionTest.class.getCanonicalName() + "#testSuiteDraftState()");
        final TestScenario scenario = new TestScenario("TestDraft", TestState.ALL, new ObjectState[] { DRAFT, DRAFT });

        suite.test("search by proximity", context -> {
            report("Test", "Search by proximity: " + scenario.toString());
            final MoreMatcherAssert matcher = new MoreMatcherAssert(context);
            final Point vlc = new CitationFactory().vlcPoint();
            JsonObject vlcJson = null;
            try {
                vlcJson = new JsonObject(JSON_MAPPER.writeValueAsString(vlc));
            } catch (IOException e) {
                context.fail(e);
            }
            vertx.eventBus().send("get.near.citation", new JsonObject().put("point", vlcJson).put("minDistance", 0.0d).put("maxDistance", 1000000.0d).encode(), ar -> {
                context.assertTrue(ar.succeeded(), "Near citations were fetched from the database [error=" + ar.cause() + "].");
                report("Test", "Reply received: " + ar.result().body());



                // TODO
            });


            /* TODO
            final FeatureCollection features = op.getNear(ds.vlcPoint, 0.0d, 1000000.0d).get(TIMEOUT, SECONDS);
            matcher.assertThat("feature collection is not null", features, notNullValue());
            matcher.assertThat("feature collection list is not null", features.getFeatures(), notNullValue());
            matcher.assertThat("feature collection list is not empty", features.getFeatures().isEmpty(), equalTo(scenario.numItems() == 0));
            matcher.assertThat("number of features coincides with expected", features.getFeatures().size(), equalTo(scenario.numItems()));
            // uncomment for additional output
            try {
                report("Test", "Feature collection (geoNear):\n" + objectToJson(features, JSON_PRETTY_PRINTER));
            } catch (IOException e) {
                context.fail(e);
            } */

            // TODO
        });

        suite.before(context -> createTestVerticle(context)
        ).beforeEach(context -> createDataset(context)
        ).afterEach(context -> destroyTestDataset(context)
        ).after(context -> destroyTestVerticle(context)
        ).run(options).awaitSuccess();
    }

    @Test
    public void testSuiteReleaseState() {


        // TODO

    }

    @Test
    public void testSuiteObsoleteState() {



        // TODO

    }

    @Test
    public void testSuiteCompleteLifeCycle() {



        // TODO

    }

    @Test
    public void testSuiteJsonMapping() {
        final TestOptions options = new TestOptions().addReporter(new ReportOptions().setTo("console").setFormat("simple")).setTimeout(120000l);
        final TestSuite suite = TestSuite.create(CitationCollectionTest.class.getCanonicalName() + "#testSuiteJsonMapping()");

        suite.test("serialization", context -> {
            // find citation by global id
            final MoreMatcherAssert matcher = new MoreMatcherAssert(context);
            vertx.eventBus().send("fetch.citation", 0, ar -> {
                context.assertTrue(ar.succeeded(), "Citation 0 was fetched from the database [error=" + ar.cause() + "].");
                report("Test", "Reply received: " + ar.result().body());

                final Citation citation2 = getObject(context, "__citation0");
                context.assertNotNull(citation2, "retrieved object is not null");

                // test Java object to JSON serialization
                final String payload = citation2.toJson(JSON_PRETTY_PRINTER);
                matcher.assertThat("serialized JSON object is not null", payload, notNullValue());
                matcher.assertThat("serialized JSON object is not empty", isNotBlank(payload), equalTo(true));
			    /* uncomment for additional output */
                report("JSON", "Serialized JSON object:\n" + payload, false);

                // test JSON to Java object deserialization
                Citation citation3 = null;
                try {
                    citation3 = JSON_MAPPER.readValue(payload, Citation.class);
                } catch (IOException e) {
                    context.fail(e);
                }
                matcher.assertThat("deserialized Java object is not null", citation3, notNullValue());
                matcher.assertThat("deserialized Java object coincides with expected", citation3, equalTo(citation2));
            });
        });

        suite.before(context -> createTestVerticle(context)
        ).beforeEach(context -> createDataset(context)
        ).afterEach(context -> destroyTestDataset(context)
        ).after(context -> destroyTestVerticle(context)
        ).run(options).awaitSuccess();
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
        final TestVerticle verticle = new TestVerticle(context);
        context.put("verticle", verticle);
        vertx.deployVerticle(verticle, deploymentOptions, context.asyncAssertSuccess(deploymentID -> {
            context.put("deploymentID", deploymentID);
            context.put("dataset", newHashMap());
            report("Verticle", "New verticle deployed: " + deploymentID);
        }));
    }

    /**
     * Destroy test verticle
     * @param context - test context
     */
    private void destroyTestVerticle(final TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    private void createDataset(final TestContext context) {
        final MoreMatcherAssert matcher = new MoreMatcherAssert(context);
        final EventBus eb = vertx.eventBus();
        for (int i = 0; i < 2; i++) {
            final int citationId = i;
            final Async async = context.async();
            eb.send("create.citation", citationId, ar -> {
                // create new citation
                context.assertTrue(ar.succeeded(), "Citation " + citationId + " was created [error=" + ar.cause() + "].");
                report("Test", "Reply received: " + ar.result().body());

                final Citation citation = getObject(context, "citation" + citationId);
                context.assertNotNull(citation, "Citation " + citationId + " was created");

                // insert citation in the database
                eb.send("insert.citation", citationId, ar2 -> {
                    context.assertTrue(ar2.succeeded(), "Citation " + citationId + " was inserted in the database [error=" + ar2.cause() + "].");
                    report("Test", "Reply received: " + ar2.result().body());

                    matcher.assertThat("inserted Id is not empty", trim(citation.getDbId()), allOf(notNullValue(), not(equalTo(""))));
                    matcher.assertThat("inserted version is empty", isBlank(citation.getVersion()), equalTo(true));
                    matcher.assertThat("last modified field is not null", citation.getLastModified(), notNullValue());
                    report("Test", "Inserted citation (" + citation.getDbId() + "):\n" + citation.toJson(JSON_PRETTY_PRINTER), false);

                    // find citation by global id
                    eb.send("fetch.citation", citationId, ar3 -> {
                        context.assertTrue(ar3.succeeded(), "Citation " + citationId + " was fetched from the database [error=" + ar3.cause() + "].");
                        report("Test", "Reply received: " + ar3.result().body());

                        final Citation citation2 = getObject(context, "__citation" + citationId);
                        matcher.assertThat("fetched citation coincides with expected", citation2, equalTo(citation));
                        report("Test", "Fetched citation (" + citation2.getDbId() + "):\n" + citation2.toJson(JSON_PRETTY_PRINTER), false);

                        removeFromDataset(context, "__citation" + citationId);

                        // collect statistics about the collection
                        eb.send("stats", "collection stats", ar4 -> {
                            context.assertTrue(ar4.succeeded(), "Citations stats were gathered from the database [error=" + ar4.cause() + "].");
                            report("Test", "Reply received: " + ar4.result().body());

                            final MongoCollectionStats stats = getStats(context, "stats");
                            matcher.assertThat("collection statistics are not null", stats, notNullValue());
                            report("Test", "Collection statistics:\n" + stats.toString());

                            // complete operation
                            report("Dataset", "created");
                            async.complete();
                        });
                    });
                });
            });
        }
    }

    /**
     * Drop the collection holding the test data-set.
     * @param context - test context
     */
    private void destroyTestDataset(final TestContext context) {
        final Async async = context.async();
        final Citations citations = new CitationFactory().createCitations(context, vertx);
        citations.drop(lookup -> {
            context.assertTrue(lookup.succeeded(), "Collection dropped.");
            clearDataset(context);
            report("Dataset", "destroyed");
            async.complete();
        });
    }

    private void report(final String label, final String message) {
        report(label, message, true);
    }

    private void report(final String label, final String message, final boolean force) {
        if (VERBOSE || force) System.out.println(" >> " + label + ": " + message);
    }

    private <T extends LeishvlObject> void putObject(final TestContext context, final String key, final T obj) {
        ((Map)context.get("dataset")).put(key, obj);
    }

    private <T extends LeishvlObject> T getObject(final TestContext context, final String key) {
        return (T)((Map)context.get("dataset")).get(key);
    }

    private <T extends LeishvlCollection> void putCollection(final TestContext context, final String key, final T collection) {
        ((Map)context.get("dataset")).put(key, collection);
    }

    private <T extends LeishvlCollection> T getCollection(final TestContext context, final String key) {
        return (T)((Map)context.get("dataset")).get(key);
    }

    private void putStats(final TestContext context, final String key, final MongoCollectionStats stats) {
        ((Map)context.get("dataset")).put(key, stats);
    }

    private MongoCollectionStats getStats(final TestContext context, final String key) {
        return (MongoCollectionStats)((Map)context.get("dataset")).get(key);
    }

    private void removeFromDataset(final TestContext context, final String key) {
        ((Map)context.get("dataset")).remove(key);
    }

    private void clearDataset(final TestContext context) {
        ((Map)context.get("dataset")).clear();
    }

    /**
     * Test verticle.
     * @author Erik Torres <ertorser@upv.es>
     */
    public class TestVerticle extends AbstractVerticle implements Closeable {

        private final TestContext context;

        public TestVerticle(final TestContext context) {
            this.context = context;
        }

        @Override
        public void start() throws Exception {
            final EventBus eb = vertx.eventBus();
            eb.consumer("create.citation", message -> {
                report("Verticle", "Received create citation: " + message.body());
                final int citationId = (Integer) message.body();
                putObject(context, "citation" + citationId, new CitationFactory().createCitation(context, vertx, citationId));
                message.reply("Created citation: " + citationId);
                report("Verticle", "Sent back response to caller: citation created");
            });
            eb.consumer("insert.citation", message -> {
                report("Verticle", "Received insert citation: " + message.body());
                final int citationId = (Integer) message.body();
                getObject(context, "citation" + citationId).save(lookup -> {
                    if (lookup.succeeded()) {
                        message.reply("Inserted citation: " + citationId);
                        report("Verticle", "Sent back response to caller: citation inserted");
                    } else message.fail(1, lookup.cause().getMessage());
                });
            });
            eb.consumer("fetch.citation", message -> {
                report("Verticle", "Received fetch citation: " + message.body());
                final int citationId = (Integer) message.body();
                putObject(context, "__citation" + citationId, Citation.builder().vertx(vertx).config(config()).leishvlId(BASE_ID + citationId).build());
                getObject(context, "__citation" + citationId).fetch(lookup -> {
                    if (lookup.succeeded()) {
                        message.reply("Citation found: " + citationId);
                        report("Verticle", "Sent back response to caller: citation fetched");
                    } else message.fail(1, lookup.cause().getMessage());
                });
            });
            eb.consumer("get.near.citation", message -> {
                report("Verticle", "Received get near citation: " + message.body());
                final JsonObject json = new JsonObject((String) message.body());
                Point point = null;
                try {
                    point = JSON_MAPPER.readValue(json.getJsonObject("point").encode(), Point.class);
                } catch (IOException e) {
                    context.fail(e);
                }
                putCollection(context, "__citations", new CitationFactory().createCitations(context, vertx));
                getCollection(context, "__citations").getNear(point, json.getDouble("minDistance"), json.getDouble("maxDistance"), new Handler<AsyncResult<FeatureCollection>>() {
                    @Override
                    public void handle(final AsyncResult<FeatureCollection> lookup) {
                        if (lookup.succeeded()) {
                            message.reply("Near citations were found");
                            report("Verticle", "Sent back response to caller: near citation fetched");
                        } else message.fail(1, lookup.cause().getMessage());
                    }
                });
            });
            eb.consumer("stats", message -> {
                report("Verticle", "Received stats: " + message.body());
                putCollection(context, "__citations", new CitationFactory().createCitations(context, vertx));
                getCollection(context, "__citations").stats(new Handler<AsyncResult<MongoCollectionStats>>() {
                    @Override
                    public void handle(final AsyncResult<MongoCollectionStats> lookup) {
                        if (lookup.succeeded()) {
                            putStats(context, "stats", lookup.result());
                            message.reply("Stats gathered");
                            report("Verticle", "Sent back response to caller: stats gathered");
                        } else message.fail(1, lookup.cause().getMessage());
                    }
                });
            });
            report("Verticle", "TestVerticle started");
        }

        @Override
        public void close(final Handler<AsyncResult<Void>> completionHandler) {
            vertx.runOnContext(v -> completionHandler.handle(succeededFuture(null)));
        }
    }

    /**
     * Creates citations.
     * @author Erik Torres <ertorser@upv.es>
     */
    public class CitationFactory {

        // create users
        private final User user0 = User.builder().userid("user0").build();
        private final User user1 = User.builder().userid("user1").build();

        // create geographic objects
        private final Point bcnPoint = pointBuilder().coordinates(lngLatAltBuilder().coordinates(2.1734034999999494d, 41.3850639d).build()).build();
        private final Point madPoint = pointBuilder().coordinates(lngLatAltBuilder().coordinates(-3.7037901999999576d, 40.4167754d).build()).build();
        private final Point vlcPoint = pointBuilder().coordinates(lngLatAltBuilder().coordinates(-0.3762881000000107d, 39.4699075d).build()).build();
        private final Polygon polygon = polygonBuilder().exteriorRing(
                lngLatAltBuilder().coordinates(-4.0d, 30.0d).build(),
                lngLatAltBuilder().coordinates(3.0d, 30.0d).build(),
                lngLatAltBuilder().coordinates(3.0d, 42.0d).build(),
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

        public Citation createCitation(final TestContext context, final Vertx vertx, final int id) {
            Citation citation;
            switch (id) {
                case 0: {
                    citation = Citation.builder()
                            .vertx(vertx)
                            .config(context.get("dbConfig"))
                            .leishvlId(BASE_ID + id)
                            .leishvl(LeishvlCitation.builder().cited(newArrayList("SEQ_0", "SEQ_1")).build())
                            .pubmed(article0)
                            .location(bcnPoint)
                            .state(DRAFT)
                            .provenance(newObjectImportProv(newPubMedArticle("PMID-" + pmid0), "lvl-ci-ur-" + BASE_ID + id, newGeocoding(bcnPoint)))
                            .references(Maps.<String, List<String>>newHashMap(ImmutableMap.of("sequences", newArrayList("lvl-sf-gb-SEQ_0", "lvl-le-gb-SEQ_1"))))
                            .build();
                    break;
                }
                case 1: {
                    citation = Citation.builder()
                            .vertx(vertx)
                            .config(context.get("dbConfig"))
                            .leishvlId(BASE_ID + id)
                            .pubmed(article1)
                            .location(madPoint)
                            .build();
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Invalid citation Id: " + id);
                }
            }
            return citation;
        }

        public Citations createCitations(final TestContext context, final Vertx vertx) {
            return new Citations(vertx, context.get("dbConfig"));
        }

        public Point vlcPoint() {
            return vlcPoint;
        }

    }

    /**
     * Describes test scenarios.
     * @author Erik Torres <ertorser@upv.es>
     */
    private class TestScenario {

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
            int count;
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
            return Arrays.stream(stateCount != null ? stateCount : EMPTY_ARRAY).sum();
        }

        public String toString() {
            return "[name=" + this.name + ", state=" + this.state + "]";
        }

    }

    /**
     * Allowed test states.
     * @author Erik Torres <ertorser@upv.es>
     */
    private enum TestState {
        ALL,
        RELEASES
    }

}