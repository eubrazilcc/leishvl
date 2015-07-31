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

import static io.leishvl.core.conf.LogManager.LOG_MANAGER;
import static io.leishvl.storage.mock.CloserServiceMock.CLOSER_SERVICE_MOCK;
import static org.apache.commons.io.FilenameUtils.concat;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Groups the integration tests for their execution.
 * @author Erik Torres <ertorser@upv.es>
 */
@RunWith(Suite.class)
@SuiteClasses({ })
public class AllIntegrationTests {

	public static final String ANCHOR_FILENAME = "m2anchor";

	@BeforeClass
	public static void setup() {
		System.out.println("AllIntegrationTests.setup()");
		final URL anchorURL = AllIntegrationTests.class.getClassLoader().getResource(ANCHOR_FILENAME);
		File anchorFile = null;
		try {
			anchorFile = new File(anchorURL.toURI());
		} catch (Exception e) {
			anchorFile = new File(System.getProperty("user.dir"));
		}
		TEST_RESOURCES_PATH = concat(anchorFile.getParent(), "files");
		final File resDir = new File(TEST_RESOURCES_PATH);
		if (resDir != null && resDir.isDirectory() && resDir.canRead()) {
			try {
				TEST_RESOURCES_PATH = resDir.getCanonicalPath();
			} catch (IOException e) {
				// nothing to do
			}
		} else {
			throw new IllegalStateException("Invalid test resources pathname: " + TEST_RESOURCES_PATH);
		}
		System.out.println("Test resources pathname: " + TEST_RESOURCES_PATH);
		// load logging bridges
		LOG_MANAGER.preload();
		// system pre-loading
		CLOSER_SERVICE_MOCK.preload();
	}

	@AfterClass
	public static void release() {
		CLOSER_SERVICE_MOCK.close();
	}

	public static String TEST_RESOURCES_PATH;

}