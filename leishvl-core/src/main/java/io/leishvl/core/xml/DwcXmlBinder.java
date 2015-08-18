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

package io.leishvl.core.xml;

import io.leishvl.core.xml.tdwg.dwc.ObjectFactory;
import io.leishvl.core.xml.tdwg.dwc.SimpleDarwinRecord;
import io.leishvl.core.xml.tdwg.dwc.SimpleDarwinRecordSet;

import javax.xml.bind.JAXBElement;

/**
 * TDWG Darwin Core data format standard for the exchange of species occurrence data - XML binding helper.
 * @author Erik Torres <ertorser@upv.es>
 * @see <a href="http://www.tdwg.org/activities/darwincore">DarwinCore Group - DwC</a>
 */
public class DwcXmlBinder extends XmlBinder {

	private static final Class<?>[] SUPPORTED_CLASSES = {
			SimpleDarwinRecordSet.class,
			SimpleDarwinRecord.class
	};

	public static final ObjectFactory DWC_XML_FACTORY = new ObjectFactory();

	public static final DwcXmlBinder DWC_XMLB = new DwcXmlBinder();

	private DwcXmlBinder() {
		super(SUPPORTED_CLASSES);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T> JAXBElement<T> createType(final T obj) {
		Object element;
		Class<?> clazz = obj.getClass();
		if (clazz.equals(SimpleDarwinRecordSet.class)) {
			element = DWC_XML_FACTORY.createSimpleDarwinRecordSet();
		} else if (clazz.equals(SimpleDarwinRecord.class)) {
			element = DWC_XML_FACTORY.createSimpleDarwinRecord();
		} else {
			throw new IllegalArgumentException("Unsupported type: " + clazz.getCanonicalName());
		}
		return (JAXBElement<T>) element;
	}

}