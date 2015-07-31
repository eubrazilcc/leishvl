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

import static com.google.common.util.concurrent.Futures.addCallback;
import static com.google.common.util.concurrent.Futures.transform;
import static io.leishvl.storage.base.DeleteOptions.DELETE_ACTIVE;
import static io.leishvl.storage.base.DeleteOptions.DELETE_ALL;
import static io.leishvl.storage.base.DeleteOptions.ON_DELETE_CASCADE;
import static io.leishvl.storage.base.DeleteOptions.ON_DELETE_NO_ACTION;
import static io.leishvl.storage.base.LeishvlObject.copyProperties;
import static io.leishvl.storage.mongodb.MongoConnector.MONGODB_CONN;
import static java.util.Arrays.asList;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import io.leishvl.storage.security.User;

/**
 * Provides different behaviors for different object states.
 * @author Erik Torres <ertorser@upv.es>
 */
public abstract class ObjectStateHandler<T extends LeishvlObject> {

	public abstract ListenableFuture<Void> save(T obj, User user, SaveOptions... options);

	public ListenableFuture<Void> fetch(final T obj, final FetchOptions... options) {
		final LeishvlObject __obj = obj;
		final ListenableFuture<LeishvlObject> findFuture = MONGODB_CONN.client().findActive(obj, obj.getClass());
		final SettableFuture<Void> foundFuture = SettableFuture.create();
		addCallback(findFuture, new FutureCallback<LeishvlObject>() {
			@Override
			public void onSuccess(final LeishvlObject result) {				
				try {
					copyProperties(result, __obj);						
					foundFuture.set(null);
				} catch (IllegalAccessException | InvocationTargetException e) {
					foundFuture.setException(e);
				}
			}
			@Override
			public void onFailure(final Throwable t) {				
				foundFuture.setException(t);
			}
		});
		return transform(findFuture, new AsyncFunction<LeishvlObject, Void>() {
			@Override
			public ListenableFuture<Void> apply(final LeishvlObject input) throws Exception {				
				return foundFuture;
			}
		});
	}

	public ListenableFuture<Boolean> delete(final T obj, final DeleteOptions... options) {
		final List<DeleteOptions> optList = (options != null ? asList(options) : Collections.<DeleteOptions>emptyList());
		return MONGODB_CONN.client().delete(obj, !optList.contains(DELETE_ACTIVE) && optList.contains(DELETE_ALL), 
				!optList.contains(ON_DELETE_NO_ACTION) && optList.contains(ON_DELETE_CASCADE));
	}

	public ListenableFuture<List<LeishvlObject>> versions(final T obj) {
		return MONGODB_CONN.client().findVersions(obj, obj.getClass());
	}

}