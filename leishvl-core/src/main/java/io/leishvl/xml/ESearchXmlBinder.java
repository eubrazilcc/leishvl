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

package io.leishvl.xml;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

import io.leishvl.ncbi.esearch.Count;
import io.leishvl.ncbi.esearch.ESearchResult;
import io.leishvl.ncbi.esearch.IdList;
import io.leishvl.ncbi.esearch.ObjectFactory;

/**
 * NCBI Entrez ESearch XML binding helper.
 * @author Erik Torres <ertorser@upv.es>
 */
public class ESearchXmlBinder extends XmlBinder {

	private static final Class<?>[] SUPPORTED_CLASSES = {
			ESearchResult.class
	};

	public static final ObjectFactory ESEARCH_XML_FACTORY = new ObjectFactory();

	public static final ESearchXmlBinder ESEARCH_XMLB = new ESearchXmlBinder();

	private ESearchXmlBinder() {
		super(SUPPORTED_CLASSES);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> JAXBElement<T> createType(final T obj) {
		Object element;
		Class<?> clazz = obj.getClass();
		if (clazz.equals(ESearchResult.class)) {
			element = ESEARCH_XML_FACTORY.createESearchResult();
		} else {
			throw new IllegalArgumentException("Unsupported type: " + clazz.getCanonicalName());
		}
		return (JAXBElement<T>) element;
	}

	public static int getCount(final ESearchResult result) {
		checkArgument(result != null, "Uninitialized result");
		int count = -1;
		final List<Object> elements = result.getCountOrRetMaxOrRetStartOrQueryKeyOrWebEnvOrIdListOrTranslationSetOrTranslationStackOrQueryTranslationOrERROR();
		if (elements != null) {
			for (int i = 0; i < elements.size() && count < 0; i++) {
				if (elements.get(i) instanceof Count) {
					count = Integer.parseInt(((Count)elements.get(i)).getvalue());
				}
			}
		}
		checkState(count >= 0, "Count node not found");
		return count;
	}

	public static List<String> getIds(final ESearchResult result) {
		checkArgument(result != null, "Uninitialized result");
		List<String> ids = null;
		final List<Object> elements = result.getCountOrRetMaxOrRetStartOrQueryKeyOrWebEnvOrIdListOrTranslationSetOrTranslationStackOrQueryTranslationOrERROR();
		if (elements != null) {
			for (int i = 0; i < elements.size() && ids == null; i++) {
				if (elements.get(i) instanceof IdList) {
					ids = ((IdList)elements.get(i)).getId().stream()
							.map(id -> id != null ? trimToNull(id.getvalue()) : null)
							.filter(idValue -> idValue != null)
							.collect(Collectors.toList());
				}
			}
		}
		if (ids == null) {
			ids = newArrayList();
		}
		return ids;
	}

}