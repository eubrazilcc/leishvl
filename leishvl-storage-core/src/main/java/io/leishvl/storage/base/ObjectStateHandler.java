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

import io.leishvl.storage.security.User;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import static io.leishvl.storage.base.DeleteOptions.*;
import static io.leishvl.storage.base.LeishvlObject.copyProperties;
import static io.leishvl.storage.mongodb.MongoConnectors.createShared;
import static io.vertx.core.Future.failedFuture;
import static io.vertx.core.Future.succeededFuture;
import static java.util.Arrays.asList;

/**
 * Provides different behaviors for different object states.
 * @author Erik Torres <ertorser@upv.es>
 */
public abstract class ObjectStateHandler<T extends LeishvlObject> {

    protected Vertx vertx;
    protected JsonObject config;

    public ObjectStateHandler setVertx(final Vertx vertx) {
        this.vertx = vertx;
        return this;
    }

    public ObjectStateHandler setConfig(final JsonObject config) {
        this.config = config;
        return this;
    }

    public abstract void save(T obj, User user, Handler<AsyncResult<Void>> resultHandler, SaveOptions... options);

    public void fetch(final T obj, final Handler<AsyncResult<Void>> resultHandler, final FetchOptions... options) {
        final LeishvlObject __obj = obj;
        createShared(vertx, config).findActive(obj, obj.getClass(), event -> {
            if (event.succeeded()) {
                try {
                    copyProperties(event.result(), __obj);
                    resultHandler.handle(succeededFuture());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    resultHandler.handle(failedFuture(e));
                }
            } else {
                resultHandler.handle(failedFuture(event.cause()));
            }
        });
    }

    public void delete(final T obj, final Handler<AsyncResult<Boolean>> resultHandler, final DeleteOptions... options) {
        final List<DeleteOptions> optList = (options != null ? asList(options) : Collections.<DeleteOptions>emptyList());
        createShared(vertx, config).delete(obj, !optList.contains(DELETE_ACTIVE) && optList.contains(DELETE_ALL),
                !optList.contains(ON_DELETE_NO_ACTION) && optList.contains(ON_DELETE_CASCADE), resultHandler);
    }

    public void versions(final T obj, final Handler<AsyncResult<List<LeishvlObject>>> resultHandler) {
        createShared(vertx, config).findVersions(obj, obj.getClass(), resultHandler);
    }

}