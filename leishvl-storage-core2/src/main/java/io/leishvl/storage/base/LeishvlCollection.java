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

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.collect.Lists.newArrayList;
import static io.leishvl.core.util.CollectionUtils.collectionToString;
import static io.leishvl.core.util.CollectionUtils.mapToString;
import static io.leishvl.core.util.PaginationUtils.firstEntryOf;
import static io.leishvl.core.util.PaginationUtils.totalPages;
import static io.leishvl.storage.mongodb.jackson.MongoJsonMapper.objectToJson;
import static java.lang.Math.max;
import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.geojson.Polygon;
import org.slf4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.leishvl.core.FormattedQueryParam;
import io.leishvl.storage.Filters;
import io.leishvl.storage.Link;
import io.leishvl.storage.Linkable;
import io.leishvl.storage.mongodb.MongoCollectionConfigurer;
import io.leishvl.storage.mongodb.MongoCollectionStats;
import io.leishvl.storage.mongodb.jackson.MongoJsonOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * Any collection of {@link LeishvlObject}. This class provides an abstraction of the database where the elements are stored in a collection
 * for further processing. Changes can be saved to the database. The total count of records is provided to support classic server-side 
 * pagination. In addition, this class include HATEOAS links to previous, next, first and last pages, which allow clients using infinite 
 * scroll pagination.
 * @param <T> - the type of elements in this collection
 * @author Erik Torres <ertorser@upv.es>
 */
@JsonIgnoreProperties({ "page", "perPage", "query", "sort", "order", "pageFirstEntry", "totalPages" })
public abstract class LeishvlCollection<T extends LeishvlObject> implements Linkable, CollectionOperator<T> {

	@JsonIgnore
	protected final Vertx vertx;
	@JsonIgnore
	protected final JsonObject config;
	
	@JsonIgnore
	protected final Logger logger;
	@JsonIgnore
	private final Class<T> type;
	@JsonIgnore
	private final String collection;
	@JsonIgnore
	private final MongoCollectionConfigurer configurer;
	@JsonIgnore
	private final AllCollectionOperator<T> defaultOperator;

	public final static int PER_PAGE_MIN = 1;

	private int page;
	private int perPage = PER_PAGE_MIN;	
	private String sort;
	private String order;
	private String query; // query parameters

	private List<FormattedQueryParam> formattedQuery = newArrayList(); // formatted query parameters
	private int pageFirstEntry; // first entry of the current page
	private int totalPages; // total number of pages
	private int totalCount; // total number of elements

	private List<T> elements = newArrayList(); // elements of the current page

	public LeishvlCollection(final Vertx vertx, final JsonObject config, final String collection, final Class<T> type, final MongoCollectionConfigurer configurer, final Logger logger) {
		this.vertx = vertx;
		this.config = config;
		this.collection = collection;
		this.type = type;
		this.configurer = configurer;
		this.logger = logger;
		this.defaultOperator = new AllCollectionOperator<>(this);
	}
	
	public String getCollection() {
		return collection;
	}

	public Class<T> getType() {
		return type;
	}

	public MongoCollectionConfigurer getConfigurer() {
		return configurer;
	}

	/* Collection operator implementation */

	@Override
	public void fetch(final int start, final int size, final @Nullable Filters filters, final @Nullable Map<String, Boolean> sorting, 
			final @Nullable Map<String, Boolean> projections, final Handler<AsyncResult<Integer>> resultHandler) {
		defaultOperator.fetch(start, size, filters, sorting, projections, resultHandler);
	}

	@Override
	public void getNear(final Point point, final double minDistance, final double maxDistance, final Handler<AsyncResult<FeatureCollection>> resultHandler) {
		defaultOperator.getNear(point, minDistance, maxDistance, resultHandler);
	}

	@Override
	public void getWithin(final Polygon polygon, final Handler<AsyncResult<FeatureCollection>> resultHandler) {
		defaultOperator.getWithin(polygon, resultHandler);
	}

	@Override
	public void totalCount(final Handler<AsyncResult<Long>> resultHandler) {
		defaultOperator.totalCount(resultHandler);	
	}

	@Override
	public void typeahead(final String field, final String query, final int size, final Handler<AsyncResult<List<String>>> resultHandler) {
		defaultOperator.typeahead(field, query, size, resultHandler);
	}

	@Override
	public void stats(final Handler<AsyncResult<MongoCollectionStats>> resultHandler) {
		defaultOperator.stats(resultHandler);
	}

	@Override
	public void drop(final Handler<AsyncResult<Void>> resultHandler) {
		defaultOperator.drop(resultHandler);
	}

	@Override
	public LeishvlCollection<T> collection() {
		return this;
	}

	@Override
	public @Nullable List<String> excludedStates() {
		return defaultOperator.excludedStates();
	}

	/* Operate on the elements loaded in the current view */

	public List<T> getElements() {
		return elements;
	}

	public void setElements(final List<T> elements) {
		if (elements != null) {
			this.elements = newArrayList(elements);
		} else {
			this.elements.clear();
		}
	}

	public List<String> ids() {		
		return (elements != null ? elements : Collections.<T>emptyList()).stream()
				.filter(element -> element != null)
				.map(element -> element.getLeishvlId())
				.collect(Collectors.toList());
	}

	/**
	 * The view maintains a size property, counting the number of elements it contains.
	 * @return the number of elements loaded in the current view
	 */
	public int size() {
		return elements.size();
	}

	/**
	 * Gets from the view the element specified by index.
	 * @param index - index of the element within the view
	 * @return the element specified by index
	 */
	public T get(final int index) {
		return elements.get(index);
	}

	/**
	 * Search the view for the element specified by id.
	 * @param id - identifier whose associate value is to be returned
	 * @return the element specified by id, or {@code null} if the view contains no entry for the id.
	 */
	public T get(final String id) {
		requireNonNull(id, "Identifier expected.");
		return (elements != null ? elements : Collections.<T>emptyList()).stream()
				.filter(element -> element.getLeishvlId().equals(id))
				.findFirst().orElse(null);
	}

	/* Pagination */

	public int getPage() {
		return page;
	}

	public void setPage(final int page) {
		this.page = page;
		setPageFirstEntry(firstEntryOf(this.page, this.perPage));
	}

	public int getPerPage() {
		return perPage;
	}

	public void setPerPage(final int perPage) {
		this.perPage = max(PER_PAGE_MIN, perPage);
		setPageFirstEntry(firstEntryOf(this.page, this.perPage));
		setTotalPages(totalPages(this.totalCount, this.perPage));
	}

	public String getSort() {
		return sort;
	}

	public void setSort(final String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(final String order) {
		this.order = order;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(final String query) {
		this.query = query;
	}

	public List<FormattedQueryParam> getFormattedQuery() {
		return formattedQuery;
	}

	public void setFormattedQuery(final List<FormattedQueryParam> formattedQuery) {		
		if (formattedQuery != null) {
			this.formattedQuery = newArrayList(formattedQuery);
		} else {
			this.formattedQuery.clear();
		}
	}

	public int getPageFirstEntry() {
		return pageFirstEntry;
	}

	public void setPageFirstEntry(final int pageFirstEntry) {
		this.pageFirstEntry = pageFirstEntry;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(final int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(final int totalCount) {
		this.totalCount = totalCount;
		setTotalPages(totalPages(this.totalCount, this.perPage));
	}

	/* General methods */

	/**
	 * Returns a String containing the attributes of each element loaded in the current view.
	 * @param options - JSON parser options
	 * @return a String containing the attributes of each element loaded in the current view
	 */
	public String toJson(final MongoJsonOptions... options) {
		String payload = "";		
		try {
			payload = objectToJson(this, options);
		} catch (final JsonProcessingException e) {
			logger.error("Failed to export object to JSON", e);
		}
		return payload;
	}	

	@Override
	public String toString() {
		final Map<String, Link> links = getLinks();
		return toStringHelper(this)
				.add("page", page)
				.add("perPage", perPage)
				.add("sort", sort)
				.add("order", order)
				.add("query", query)
				.add("formattedQuery", collectionToString(formattedQuery))
				.add("pageFirstEntry", pageFirstEntry)
				.add("totalPages", totalPages)
				.add("totalCount", totalCount)
				.add("elements", collectionToString(elements))
				.add("links", links != null ? mapToString(links) : null)
				.toString();
	}

}