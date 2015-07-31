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

package io.leishvl.core.util;

import static io.leishvl.core.util.NumberUtils.roundUp;

/**
 * Utility class to help with collection pagination.
 * @author Erik Torres <ertorser@upv.es>
 */
public final class PaginationUtils {

	/**
	 * Computes the position of the first entry of a page.
	 * @param page - current page
	 * @param perPage - number of entries per page
	 * @return The position of the first entry of the specified page.
	 */
	public static int firstEntryOf(final int page, final int perPage) {
		if (page > 0 && perPage > 0) {
			return page * perPage;
		}
		return 0;
	}
	
	/**
	 * Computes the total number of pages needed to display a collection of items
	 * with a fixed number of elements per page.
	 * @param totalEntries - total number of items in the collection to be displayed
	 * @param perPage - maximum number of items per page
	 * @return The total number of pages needed to display the collection.
	 */
	public static int totalPages(final int totalEntries, final int perPage) {		
		return roundUp(totalEntries, perPage);
	}
	
}