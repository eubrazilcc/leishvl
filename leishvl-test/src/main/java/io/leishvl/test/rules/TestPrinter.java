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

import static java.util.Optional.ofNullable;

import javax.annotation.Nullable;

import org.junit.rules.ExternalResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Provides common methods for printing objects during tests execution.
 * @author Erik Torres <ertorser@upv.es>
 */
public class TestPrinter extends ExternalResource {

	private final boolean print;

	/**
	 * Default constructor gets its configuration from the system environment.
	 */
	public TestPrinter() {
		this(System.getProperty("leishvl.tests.print.out", "false").equals("true"));
	}

	public TestPrinter(final boolean print) {
		this.print = print;
	}

	public void println() {
		println("", OutputFormat.PLAIN);
	}

	public void println(final @Nullable Object obj) {
		println(obj, OutputFormat.PLAIN);
	}

	public void println(final String msg) {
		println(msg, OutputFormat.PLAIN);
	}

	public void printlnJson(final String msg) {
		println(msg, OutputFormat.JSON);
	}

	private void println(final @Nullable Object obj, final @Nullable OutputFormat format) {
		switch (ofNullable(format).orElse(OutputFormat.PLAIN)) {
		case JSON:
			if (print) System.out.println(GsonHelper.gson.toJson(obj));
			break;
		case PLAIN:
		default:
			if (print) System.out.println(obj);
			break;
		}
	}

	/**
	 * Supported output formats.
	 * @author Erik Torres <ertorser@upv.es>
	 */
	public enum OutputFormat {
		JSON,
		PLAIN
	}

	/**
	 * Lazy initialization of Gson-based JSON processor.
	 * @author Erik Torres <ertorser@upv.es>
	 */
	private static class GsonHelper {		
		public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	}

}