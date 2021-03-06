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

package io.leishvl.test.rules;

import org.junit.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * Keeps a log of each passing and failing test.
 * @author Erik Torres <ertorser@upv.es>
 */
public class TestWatcher2 extends TestWatcher {	

	private static final String HEADER = " < LeishVL >>> ";

	private final TestPrinter pw;

	public TestWatcher2(final TestPrinter pw) {
		this.pw = pw;
	}

	@Override
	protected void succeeded(final Description description) {
		pw.println(new StringBuffer(HEADER).append(description.getDisplayName()).append(" ").append("success!").toString());		
	}

	@Override
	protected void failed(final Throwable e, final Description description) {
		pw.println(new StringBuffer(HEADER).append(description.getDisplayName()).append(" ").append(e.getClass().getSimpleName()).toString());
	}

	@Override
	protected void skipped(final AssumptionViolatedException e, final Description description) {
		pw.println(new StringBuffer(HEADER).append(description.getDisplayName()).append(" ").append(e.getClass().getSimpleName()).toString());
	}

	@Override
	protected void starting(final Description description) {
		pw.println(new StringBuffer(HEADER).append(description.getDisplayName()).append(" ").append("starting...").toString());
	}

}