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

package io.leishvl.collection;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static java.lang.Runtime.getRuntime;

import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonController;
import org.apache.commons.daemon.DaemonInitException;

import com.google.common.util.concurrent.ServiceManager;

import io.leishvl.microservices.LeishvlDaemon;
import io.leishvl.microservices.VertxService;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;

/**
 * Driver daemon.
 * @author Erik Torres <ertorser@upv.es>
 */
public class AppDaemon extends LeishvlDaemon {

	public AppDaemon() {
		super(AppDaemon.class);		
	}

	public static void main(final String[] args) throws Exception {
		final AppDaemon daemon = new AppDaemon();
		getRuntime().addShutdownHook(new Thread() {
			public void run() {				
				try {
					daemon.stop();
				} catch (Exception e) {
					System.err.println("Failed to stop application: " + e.getMessage());
				}
				try {
					daemon.destroy();
				} catch (Exception e) {
					System.err.println("Failed to stop application: " + e.getMessage());
				}
			}
		});
		daemon.init(new DaemonContext() {			
			@Override
			public DaemonController getController() {
				return null;
			}			
			@Override
			public String[] getArguments() {
				return args;
			}
		});
		daemon.start();
	}

	@Override
	public void init(final DaemonContext daemonContext) throws DaemonInitException, Exception {
		// parse application arguments
		CommandLine cmd = null;
		try {
			cmd = parseParameters(daemonContext.getArguments(), new Options());
		} catch (Exception e) {			
			logger.error("Parsing options failed.", e);
			System.exit(1);
		}

		// load configuration properties
		loadConfig(cmd);

		// create service options from configuration
		final VertxOptions vertxOptions = new VertxOptions().setClustered(true)
				.setClusterHost(config.getString("leishvl.cluster.host"));
		final Map<String, Object> verticleConfig = newHashMap();
		verticleConfig.put("http.port", config.getInt("leishvl.http.port"));
		final DeploymentOptions deploymentOptions = new DeploymentOptions()				
				.setInstances(config.getInt("leishvl.http.instances"))
				.setConfig(new JsonObject(verticleConfig));
		serviceManager = new ServiceManager(newHashSet(new VertxService(newArrayList(CollectionRestServer.class), vertxOptions, deploymentOptions)));

		super.init(daemonContext);
	}

}