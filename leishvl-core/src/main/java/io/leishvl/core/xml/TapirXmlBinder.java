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

import io.leishvl.core.xml.tdwg.tapir.ObjectFactory;
import io.leishvl.core.xml.tdwg.tapir.RequestType;
import io.leishvl.core.xml.tdwg.tapir.ResponseType;
import io.leishvl.core.xml.tdwg.tapir.SearchTemplateType;

import javax.xml.bind.JAXBElement;

/**
 * TDWG TAPIR access protocol for information retrieval XML binding helper.
 * @author Erik Torres <ertorser@upv.es>
 * @see <a href="http://www.tdwg.org/activities/tapir/specification">TAPIR - TDWG Access Protocol for Information Retrieval</a>
 */
public class TapirXmlBinder extends XmlBinder {

    private static final Class<?>[] SUPPORTED_CLASSES = {
            SearchTemplateType.class,
            RequestType.class,
            ResponseType.class
    };

    public static final ObjectFactory TAPIR_XML_FACTORY = new ObjectFactory();

    public static final TapirXmlBinder TAPIR_XMLB = new TapirXmlBinder();

    private TapirXmlBinder() {
        super(SUPPORTED_CLASSES);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> JAXBElement<T> createType(final T obj) {
        Object element;
        Class<?> clazz = obj.getClass();
        if (clazz.equals(SearchTemplateType.class)) {
            element = TAPIR_XML_FACTORY.createSearchTemplate((SearchTemplateType)obj);
        } else if (clazz.equals(RequestType.class)) {
            element = TAPIR_XML_FACTORY.createRequest((RequestType)obj);
        } else if (clazz.equals(ResponseType.class)) {
            element = TAPIR_XML_FACTORY.createResponse((ResponseType)obj);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + clazz.getCanonicalName());
        }
        return (JAXBElement<T>) element;
    }

}