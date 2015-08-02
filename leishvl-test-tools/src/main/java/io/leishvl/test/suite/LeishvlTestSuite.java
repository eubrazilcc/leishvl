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

package io.leishvl.test.suite;

import static java.io.File.separator;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Base test suite.
 * @author Erik Torres <ertorser@upv.es>
 */
public class LeishvlTestSuite {

	public static final String ANCHOR_FILENAME = "m2anchor";

	private String testResourcePath;

	public String getTestResourcePath() {
		return testResourcePath;
	}

	public void setTestResourcePath() {		
		final URL anchorURL = LeishvlTestSuite.class.getClassLoader().getResource(ANCHOR_FILENAME);
		File anchorFile = null;
		try {
			anchorFile = new File(anchorURL.toURI());
		} catch (Exception e) {
			anchorFile = new File(System.getProperty("user.dir"));
		}		
		try {
			final File resDir = new File(anchorFile.getParentFile(), "files");
			testResourcePath = resDir.getCanonicalPath();
			if (resDir.isDirectory() || !resDir.canRead()) {
				throw new IllegalStateException("Invalid test resources pathname: " + testResourcePath);
			}
		} catch (IOException ignore) {
			throw new IllegalStateException("Failed to parse test resources pathname.");
		}
		System.out.println("Test resources pathname: " + testResourcePath);		
	}

	public String[] getTestFiles(final String dirname, final String extension) {
		final File dir = new File(testResourcePath + separator + dirname);
		return dir.list((final File dirToFilter, final String filename) -> filename.endsWith(extension));
	}

}