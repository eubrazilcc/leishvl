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

package io.leishvl.storage.base;

import static io.leishvl.storage.base.LeishvlObject.randomVersion;
import static io.leishvl.storage.base.ObjectState.DRAFT;
import static io.leishvl.storage.base.ObjectState.RELEASE;
import static io.leishvl.storage.mongodb.MongoConnectors.createShared;
import static io.leishvl.storage.prov.ProvFactory.newReleaseProv;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import javax.annotation.Nullable;

import io.leishvl.storage.security.User;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * Behavior corresponding to the release state.
 * @author Erik Torres <ertorser@upv.es>
 */
public class ReleaseStateHandler<T extends LeishvlObject> extends ObjectStateHandler<T> {

	public ReleaseStateHandler(final Vertx vertx, final JsonObject config) {
		super(vertx, config);
	}

	@Override
	public void save(final T obj, final @Nullable User user, final Handler<AsyncResult<Void>> resultHandler, final SaveOptions... options) {		
		final String newVersion = randomVersion();
		if (user != null) obj.setProvenance(newReleaseProv(user, obj.getLeishvlId(), isNotBlank(obj.getVersion()) ? "|" + obj.getVersion() : "", "|" + newVersion));
		createShared(vertx, config).saveAsVersion(newVersion, obj, resultHandler, DRAFT.name(), RELEASE.name());
	}

}