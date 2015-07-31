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

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.geojson.Polygon;

import com.google.common.util.concurrent.ListenableFuture;

import io.leishvl.storage.Filters;
import io.leishvl.storage.mongodb.MongoCollectionStats;

/**
 * Operates on a specific subset of a collection.
 * @author Erik Torres <ertorser@upv.es>
 */
public interface CollectionOperator<T extends LeishvlObject> {

	/* Database operations */
	
	/**
	 * Loads a view of the collection that contains the elements in the specified range. The elements are sorted by their keys, in ascending order.
	 * Optionally, the database response can be filtered (if the filter is invalid, an empty view will be created).
	 * @param start - starting index
	 * @param size - maximum number of elements returned
	 * @param filter - (optional) the expression to be used to filter the collection
	 * @param sorting - (optional) sorting order
	 * @param projection - (optional) specifies the fields to return. Set the field name to <tt>true</tt> to include the field, <tt>false</tt>
	 *        to exclude the field. To return all fields in the matching document, omit this parameter
	 */	
	ListenableFuture<Integer> fetch(int start, int size, @Nullable Filters filters, @Nullable Map<String, Boolean> sorting, 
			@Nullable Map<String, Boolean> projections);

	/**
	 * Gets the elements that are within the specified distance (in meters) from the specified center point (using WGS84).
	 * @param point - longitude, latitude pair represented in WGS84 coordinate reference system (CRS)
	 * @param minDistance - minimum distance
	 * @param maxDistance - limits the results to those elements that fall within the specified distance (in meters) from the center point
	 * @return a collection of GeoJSON points with the location of the elements matching the query, annotated with the feature <tt>name</tt>
	 *         which contains the global identifier of the element.
	 */
	ListenableFuture<FeatureCollection> getNear(Point point, double minDistance, double maxDistance);

	/**
	 * Gets the elements that exist entirely within the defined polygon.
	 * @param polygon - geometric shape with at least four edges
	 * @return a collection of GeoJSON points with the location of the elements matching the query, annotated with the feature <tt>name</tt>
	 *         which contains the global identifier of the element.
	 */
	ListenableFuture<FeatureCollection> getWithin(Polygon polygon);

	/**
	 * Gets the total number of elements contained in the collection.
	 * @return
	 */
	ListenableFuture<Long> totalCount();	

	/**
	 * Searches a field for the specified query, returning a list of values that match the query.
	 * @param field - field to query against
	 * @param query - the query to match
	 * @param size - maximum number of elements returned
	 * @return a future whose response is the values that matches the query.
	 */
	ListenableFuture<List<String>> typeahead(String field, String query, int size);

	/**
	 * Collects statistics about this collection.
	 */
	ListenableFuture<MongoCollectionStats> stats();

	/**
	 * Drop the collection from the database.
	 */
	ListenableFuture<Void> drop();	
	
	/* general operations */
	
	/**
	 * Gets the target collection.
	 * @return the target collection.
	 */
	LeishvlCollection<T> collection();

	/**
	 * Gets the states excluded in this operator.
	 * @return the states excluded in this operator.
	 */
	@Nullable List<String> excludedStates();

}