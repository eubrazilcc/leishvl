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

/**
 * Facilitates common operations with collection operators, like creating new instances.
 * @author Erik Torres <ertorser@upv.es>
 */
public final class CollectionOperators {

	public static <T extends LeishvlObject> AllCollectionOperator<T> allOperator(final LeishvlCollection<T> leishvlCol) {
		return new AllCollectionOperator<>(leishvlCol);
	}

	public static <T extends LeishvlObject> ReleasesCollectionOperator<T> releasesOperator(final LeishvlCollection<T> leishvlCol) {
		return new ReleasesCollectionOperator<>(leishvlCol);
	}

}