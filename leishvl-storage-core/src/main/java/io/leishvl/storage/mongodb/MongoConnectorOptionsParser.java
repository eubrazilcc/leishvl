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

package io.leishvl.storage.mongodb;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.selector.LatencyMinimizingServerSelector;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static com.mongodb.MongoCredential.createMongoCRCredential;
import static com.mongodb.ReadPreference.nearest;
import static com.mongodb.WriteConcern.ACKNOWLEDGED;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Parses mongoDB client options.
 * @author Erik Torres <ertorser@upv.es>
 */
public class MongoConnectorOptionsParser {

    public static final long LAT_DIFF_MS = 2000l;

    private final JsonObject config;

    public MongoConnectorOptionsParser(final JsonObject config) {
        this.config = config;
    }

    public MongoClientSettings settings() {
        // cluster settings
        final List<ServerAddress> hosts = config.getJsonArray("hosts", defaultHosts()).stream().map(input -> {
            final JsonObject json = (JsonObject) input;
            return new ServerAddress(json.getString("host"), json.getInteger("port"));
        }).filter(host -> host != null).collect(Collectors.toList());
        final ClusterSettings clusterSettings = ClusterSettings.builder()
                .hosts(hosts)
                .serverSelector(new LatencyMinimizingServerSelector(LAT_DIFF_MS, MILLISECONDS))
                        // TODO .requiredClusterType(REPLICA_SET)
                        // TODO .requiredReplicaSetName(config.getString("replica_set", "leishvl"))
                .build();
        // authentication settings (requires mongoDB to be configured to support authentication)
        final List<MongoCredential> credentialList = newArrayList(createMongoCRCredential(config.getString("user"),
                config.getString("db_name"), config.getString("pwd", "").toCharArray()));
        return MongoClientSettings.builder()
                .readPreference(nearest())
                .writeConcern(ACKNOWLEDGED)
                .clusterSettings(clusterSettings)
                        // TODO .credentialList(credentialList)
                .build();
    }

    private static JsonArray defaultHosts() {
        return new JsonArray().add(new JsonObject().put("host", "127.0.0.1").put("port", "27017"));
    }

}