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

package io.leishvl.storage;

import java.util.Map;

/**
 * Implement this interface to add get and set methods for the hyper-links needed to implement HATEOAS.
 * Each map entry contains a hyperlink. For example:
 * <ul>
 * <li>{ "self" : { "href" : "/rest/v1/sequences/sandflies/~/AC238558", "type" : "application/json" } }</li>
 * </ul>
 * @author Erik Torres <ertorser@upv.es>
 */
public interface Linkable {

	/**
	 * Gets the links associated to this class.
	 * @return the links associated to this class.
	 */
	Map<String, Link> getLinks();

	/**
	 * Sets the links associated to this class.
	 * @param links - links to be associated to this class
	 */
	void setLinks(Map<String, Link> links);

}