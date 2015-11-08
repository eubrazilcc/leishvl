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

package io.leishvl.core.prov;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;

/**
 * Exports provenance to several file formats.
 * @author Erik Torres <ertorser@upv.es>
 */
public class ProvWriter {

	/**
	 * Exports the specified document to a file. The file format is discovered from the file extension. <strong>Note </strong> that 
	 * Graphviz is needed locally to export to PROVN and SVG formats.
	 * @param document - provenance document to be exported
	 * @param file - output file
	 * @see <a href="http://www.graphviz.org/">Graphviz</a>
	 */
	public static void provToFile(final Document document, final String file) {
		final InteropFramework intF = new InteropFramework();
		intF.writeDocument(file, document);     
		// intF.writeDocument(System.out, ProvFormat.JSON, document);
		// intF.writeDocument(System.out, ProvFormat.PROVN, document);
	}

}