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

import static io.leishvl.storage.mongodb.MongoConnectors.createShared;
import static io.vertx.core.Future.failedFuture;
import static io.vertx.core.Future.succeededFuture;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import org.apache.commons.lang3.mutable.MutableLong;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.geojson.Polygon;

import io.leishvl.storage.Filters;
import io.leishvl.storage.mongodb.MongoCollectionStats;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * Base implementation of the {@link CollectionOperator}.
 * @author Erik Torres <ertorser@upv.es>
 */
public abstract class CollectionOperatorImpl<T extends LeishvlObject> implements CollectionOperator<T> {

	private final LeishvlCollection<T> leishvlCol;

	private Optional<List<String>> excludedStates = empty(); // (optional) objects in these states are excluded from the collection

	public CollectionOperatorImpl(final LeishvlCollection<T> leishvlCol, final @Nullable List<String> excludedStates) {
		this.leishvlCol = leishvlCol;
		this.excludedStates = ofNullable(excludedStates);
	}

	@Override
	public void fetch(final int start, final int size, final @Nullable Filters filters, final @Nullable Map<String, Boolean> sorting, 
			final @Nullable Map<String, Boolean> projections, final Handler<AsyncResult<Integer>> resultHandler) {
		final MutableLong totalCount = new MutableLong(0l);
		createShared(leishvlCol.vertx, leishvlCol.config).findActive(leishvlCol, leishvlCol.getType(), start, size, filters, sorting, projections, totalCount, excludedStates.orElse(null), new Handler<AsyncResult<List<T>>>(){
			@Override
			public void handle(final AsyncResult<List<T>> event) {
				if (event.succeeded()) {
					leishvlCol.setElements(event.result());
					leishvlCol.setTotalCount(totalCount.getValue().intValue());
					resultHandler.handle(succeededFuture(event.result() != null ? event.result().size() : 0));					
				} else {
					resultHandler.handle(failedFuture(event.cause()));
				}
			}			
		});
	}

	@Override
	public void getNear(final Point point, final double minDistance, final double maxDistance, final Handler<AsyncResult<FeatureCollection>> resultHandler) {
		createShared(leishvlCol.vertx, leishvlCol.config).fetchNear(leishvlCol, leishvlCol.getType(), point.getCoordinates().getLongitude(), point.getCoordinates().getLatitude(), 
				minDistance, maxDistance, excludedStates.orElse(null), resultHandler);
	}

	@Override
	public void getWithin(final Polygon polygon, final Handler<AsyncResult<FeatureCollection>> resultHandler) {
		createShared(leishvlCol.vertx, leishvlCol.config).fetchWithin(leishvlCol, leishvlCol.getType(), polygon, excludedStates.orElse(null), resultHandler);
	}

	@Override
	public void totalCount(final Handler<AsyncResult<Long>> resultHandler) {
		createShared(leishvlCol.vertx, leishvlCol.config).totalCount(leishvlCol, excludedStates.orElse(null), resultHandler);
	}

	@Override
	public void typeahead(final String field, final String query, final int size, final Handler<AsyncResult<List<String>>> resultHandler) {
		createShared(leishvlCol.vertx, leishvlCol.config).typeahead(leishvlCol, leishvlCol.getType(), field, query, size, excludedStates.orElse(null), resultHandler);
	}

	@Override
	public void stats(final Handler<AsyncResult<MongoCollectionStats>> resultHandler) {
		createShared(leishvlCol.vertx, leishvlCol.config).stats(leishvlCol, resultHandler);
	}

	@Override
	public void drop(final Handler<AsyncResult<Void>> resultHandler) {
		createShared(leishvlCol.vertx, leishvlCol.config).drop(leishvlCol, resultHandler);
	}

	@Override
	public LeishvlCollection<T> collection() {
		return leishvlCol;
	}

	@Override
	public List<String> excludedStates() {
		return excludedStates.orElse(null);
	}	

}