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

import static com.google.common.collect.Lists.newArrayList;
import static io.leishvl.storage.mongodb.MongoConnectors.createShared;

import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.geojson.Point;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.ImmutableMap;
import com.mongodb.client.model.IndexModel;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import static org.apache.commons.lang3.StringUtils.trim;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import io.leishvl.storage.mongodb.MongoConnector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.Closeable;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

/**
 * Dummy test.
 * @author Erik Torres <ertorser@upv.es>
 */
@RunWith(VertxUnitRunner.class)
public class BaseOperationsTest {

	private Vertx vertx;
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
		final DeploymentOptions deploymentOptions = new DeploymentOptions()
				.setConfig(new JsonObject(verticleConfig));
		// deploy verticle
		vertx = Vertx.vertx();
		verticle = new TestVerticle();
		vertx.deployVerticle(verticle, deploymentOptions, testCtxt.asyncAssertSuccess(deploymentID -> {
			this.deploymentID = deploymentID;
			System.out.println(" >> New verticle deployed: " + this.deploymentID);
		}));
	}

	@After
	public void after(final TestContext testCtxt) {
		vertx.close(testCtxt.asyncAssertSuccess());
	}

	@Test
	public void test(final TestContext testCtxt) {
		System.out.println("BaseOperationsTest.test()");
		try {
			// test open a database connection
			verticle.openConnection();
			testCtxt.assertNotNull(verticle.getMongoConnector(), "mongoDB connector is not null");

			// test create index
			verticle.getMongoConnector().createIndexes("test_col", newArrayList(new IndexModel(new Document("field", 1))), testCtxt.asyncAssertSuccess(ids -> {
				testCtxt.assertNotNull(ids, "result received");
				testCtxt.assertTrue(ids.size() == 1, "number of created indexes coincides with expected");
				// uncomment for additional output
				System.out.println(" >> Created indexes: " + ids);

				// test closing the verticle, the database connection and the testing context
				verticle.close(testCtxt.asyncAssertSuccess());
			}));

		} catch (Exception e) {
			e.printStackTrace(System.err);
			testCtxt.fail("BaseOperationsTest.test() failed: " + e.getMessage());
		} finally {
			System.out.println("BaseOperationsTest.test() has finished");
		}
	}

	/**
	 * Test verticle.
	 * @author Erik Torres <ertorser@upv.es>
	 */
	public static class TestVerticle extends AbstractVerticle implements Closeable {

		private MongoConnector conn;

		public MongoConnector getMongoConnector() {
			return conn;
		}

		public void openConnection() {
			conn = createShared(vertx, context.config());
		}

		@Override
		public void close(final Handler<AsyncResult<Void>> completionHandler) {
			conn.close(completionHandler);
		}

	}

}