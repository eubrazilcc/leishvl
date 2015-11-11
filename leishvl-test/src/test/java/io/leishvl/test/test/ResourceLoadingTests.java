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

package io.leishvl.test.test;

import static io.leishvl.test.util.ResourceLoadingUtils.getResourceFiles;
import static java.io.File.separator;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import io.leishvl.test.rules.TestPrinter;
import io.leishvl.test.rules.TestWatcher2;
import io.leishvl.test.util.ResourceLoadingUtils;

/**
 * Tests resource loading utilities provided by {@link ResourceLoadingUtils}.
 * @author Erik Torres <ertorser@upv.es>
 */
public class ResourceLoadingTests {

	@Rule
	public TestPrinter pw = new TestPrinter();

	@Rule
	public TestRule watchman = new TestWatcher2(pw);

	@Test
	public void testSimpleDirectoryHierarchy() {
		// test simple directory hierarchy
		final List<String> files = getResourceFiles("examples", new String[]{ ".txt", ".log" });
		assertThat("files list is not null", files, notNullValue());
		assertThat("number of files coincide with expected", files.size(), equalTo(3));
		final String filenames = files.stream().map(s -> s + "\n").reduce("", String::concat);
		assertThat("all expected files are included", filenames, allOf(containsString("example1.txt"),
				containsString("example2.txt"), containsString("example4.log")));
		pw.println("\n >> Files: " + files);
	}

	@Test
	public void testNestedDirectoryHierarchy() {
		// test complex directory hierarchy
		final List<String> files = getResourceFiles(new StringBuffer("examples").append(separator)
				.append("inner_examples").toString(), new String[]{ ".txt" });
		assertThat("files list is not null", files, notNullValue());
		assertThat("number of files coincide with expected", files.size(), equalTo(1));
		final String filenames = files.stream().map(s -> s + "\n").reduce("", String::concat);
		assertThat("all expected files are included", filenames, containsString("example.txt"));
		pw.println("\n >> Files: " + files);
	}

	@Test
	public void testInvalidDirectory() {
		// test empty directory
		final List<String> files = getResourceFiles("i_dont_exist", new String[]{ ".txt" });
		assertThat("files list is not null", files, notNullValue());
		assertThat("number of files coincide with expected", files.size(), equalTo(0));
	}

	@Test
	public void testEmptyExtensionList() {
		// test empty extension list
		final List<String> files = getResourceFiles("examples", null);
		assertThat("files list is not null", files, notNullValue());
		assertThat("number of files coincide with expected", files.size(), equalTo(0));		
	}

}