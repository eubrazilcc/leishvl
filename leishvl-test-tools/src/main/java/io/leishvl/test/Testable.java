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

package io.leishvl.test;

/**
 * Provides a common interface to inject a context in a test.
 * @author Erik Torres <ertorser@upv.es>
 */
public abstract class Testable {

	protected final TestContext testCtxt;
	private final Class<?> implementingClass;

	public Testable(final TestContext testCtxt, final Class<?> implementingClass) {
		this.testCtxt = testCtxt;
		this.implementingClass = implementingClass;
	}

	protected void setUp() {
		System.out.println("  >> " + implementingClass.getSimpleName() + ".test() is starting...");
	}

	protected void cleanUp() {
		System.out.println("  >> " + implementingClass.getSimpleName() + ".test() ends.");
	}

	protected abstract void test() throws Exception;

	public void runTest() throws Exception {
		setUp();
		try {
			test();
		} catch (Exception e) {
			throw e;
		} finally {
			cleanUp();
		}
	}

}