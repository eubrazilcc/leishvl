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

package io.leishvl.microservices;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.google.common.util.concurrent.AbstractIdleService;
import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;

import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

/**
 * Vert.x service.
 * @author Erik Torres <ertorser@upv.es>
 */
public class VertxService extends AbstractIdleService {

	private final static Logger LOGGER = getLogger(VertxService.class);

	private final VertxOptions vertxOptions;
	private final DeploymentOptions deploymentOptions;

	private Vertx vertx;

	private final List<Class<?>> verticles;	

	public VertxService(final List<Class<?>> verticles, final @Nullable VertxOptions vertxOptions, final @Nullable DeploymentOptions deploymentOptions) {
		this.verticles = verticles;
		this.vertxOptions = ofNullable(vertxOptions).orElse(new VertxOptions());
		this.deploymentOptions = ofNullable(deploymentOptions).orElse(new DeploymentOptions());
	}

	@Override
	protected void startUp() throws Exception {
		final Consumer<Vertx> runner = vertx -> {
			for (final Class<?> verticle : verticles) {
				vertx.deployVerticle(verticle.getCanonicalName(), deploymentOptions, new Handler<AsyncResult<String>>() {
					@Override
					public void handle(final AsyncResult<String> result) {
						if (result != null && result.succeeded()) LOGGER.info("New verticle deployed: [type=" + verticle.getSimpleName() 
						+ ", id=" + result.result() + "].");
						else LOGGER.error("Failed to deploy verticle: " + result.result(), result.cause());
					}
				});
			}
		};
		final CompletableFuture<Void> future = new CompletableFuture<>();		
		if (vertxOptions.isClustered()) {
			// configure Hazelcast		
			final Config hazelcastConfig = new ClasspathXmlConfig("leishvl-cluster.xml");
			requireNonNull(hazelcastConfig, "Failed to open default cluster configuration.");
			final NetworkConfig network = hazelcastConfig.getNetworkConfig();
			network.getJoin().getTcpIpConfig().setMembers(deploymentOptions.getConfig().getJsonArray("cluster.members")
					.stream()
					.map(new Function<Object, String>() {
						@Override
						public String apply(final Object input) {					
							return (String)input;
						}
					})
					.filter(member -> member != null)
					.collect(Collectors.toList()));
			network.getInterfaces().setInterfaces(newArrayList(vertxOptions.getClusterHost()));			
			final ClusterManager clusterManager = new HazelcastClusterManager(hazelcastConfig);
			vertxOptions.setClusterManager(clusterManager);
			Vertx.clusteredVertx(vertxOptions, res -> {
				if (res.succeeded()) {
					vertx = res.result();
					runner.accept(vertx);
				} else LOGGER.error("Failed to start Vert.x system.", res.cause());
				future.complete(null);
			});
		} else {
			vertx = Vertx.vertx(vertxOptions);
			runner.accept(vertx);
			future.complete(null);
		}
		try {
			future.get(30, TimeUnit.SECONDS);
		} catch (InterruptedException | TimeoutException ignore) {
			// silently ignored
		} catch (ExecutionException e) {
			throw (e.getCause() instanceof Exception ? (Exception)e.getCause() : e);
		}
	}

	@Override
	protected void shutDown() throws Exception {
		vertx.close(new Handler<AsyncResult<Void>>() {			
			@Override
			public void handle(final AsyncResult<Void> result) {
				if (result != null && result.succeeded()) LOGGER.info("Shutdown succeeded.");
				else LOGGER.info("Exited with error.", result.cause());
			}
		});
	}

}