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

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.fromNullable;
import static com.google.common.util.concurrent.Futures.addCallback;
import static com.google.common.util.concurrent.Futures.transform;
import static io.leishvl.storage.mongodb.MongoConnector.MONGODB_CONN;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableLong;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.geojson.Polygon;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import io.leishvl.storage.Filters;
import io.leishvl.storage.mongodb.MongoCollectionStats;

/**
 * Base implementation of the {@link CollectionOperator}.
 * @author Erik Torres <ertorser@upv.es>
 */
public abstract class CollectionOperatorImpl<T extends LeishvlObject> implements CollectionOperator<T> {

	private final LeishvlCollection<T> leishvlCol;

	private Optional<List<String>> excludedStates = absent(); // (optional) objects in these states are excluded from the collection

	public CollectionOperatorImpl(final LeishvlCollection<T> leishvlCol, final @Nullable List<String> excludedStates) {
		this.leishvlCol = leishvlCol;
		this.excludedStates = fromNullable(excludedStates);
	}	

	@Override
	public ListenableFuture<Integer> fetch(final int start, final int size, final @Nullable Filters filters, final @Nullable Map<String, Boolean> sorting, 
			final @Nullable Map<String, Boolean> projections) {
		final MutableLong totalCount = new MutableLong(0l);
		final ListenableFuture<List<T>> findFuture = MONGODB_CONN.client().findActive(leishvlCol, leishvlCol.getType(), start, size, filters, sorting, projections, totalCount, 
				excludedStates.orNull());
		final SettableFuture<Integer> countFuture = SettableFuture.create();
		addCallback(findFuture, new FutureCallback<List<T>>() {
			@Override
			public void onSuccess(final List<T> result) {
				leishvlCol.setElements(result);
				leishvlCol.setTotalCount(totalCount.getValue().intValue());
				countFuture.set(result != null ? result.size() : 0);
			}
			@Override
			public void onFailure(final Throwable t) {				
				countFuture.setException(t);
			}
		});
		return transform(findFuture, new AsyncFunction<List<T>, Integer>() {
			@Override
			public ListenableFuture<Integer> apply(final List<T> input) throws Exception {				
				return countFuture;
			}
		});
	}

	@Override
	public ListenableFuture<FeatureCollection> getNear(final Point point, final double minDistance, final double maxDistance) {
		return MONGODB_CONN.client().fetchNear(leishvlCol, leishvlCol.getType(), point.getCoordinates().getLongitude(), point.getCoordinates().getLatitude(), 
				minDistance, maxDistance, excludedStates.orNull());
	}

	@Override
	public ListenableFuture<FeatureCollection> getWithin(final Polygon polygon) {
		return MONGODB_CONN.client().fetchWithin(leishvlCol, leishvlCol.getType(), polygon, excludedStates.orNull());
	}

	@Override
	public ListenableFuture<Long> totalCount() {
		return MONGODB_CONN.client().totalCount(leishvlCol, excludedStates.orNull());		
	}

	@Override
	public ListenableFuture<List<String>> typeahead(final String field, final String query, final int size) {
		return MONGODB_CONN.client().typeahead(leishvlCol, leishvlCol.getType(), field, query, size, excludedStates.orNull());
	}

	@Override
	public ListenableFuture<MongoCollectionStats> stats() {
		return MONGODB_CONN.client().stats(leishvlCol);
	}

	@Override
	public ListenableFuture<Void> drop() {
		return MONGODB_CONN.client().drop(leishvlCol);
	}
	
	@Override
	public LeishvlCollection<T> collection() {
		return leishvlCol;
	}

	@Override
	public List<String> excludedStates() {
		return excludedStates.orNull();
	}	

}