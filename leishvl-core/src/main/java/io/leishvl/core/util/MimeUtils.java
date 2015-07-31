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

package io.leishvl.core.util;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.apache.tika.config.TikaConfig.getDefaultConfig;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.File;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.slf4j.Logger;

/**
 * Utilities to work with MIME types. This class uses Apache Tika to determine the MIME type of files or streams.
 * @author Erik Torres <ertorser@upv.es>
 * @see <a href="http://tika.apache.org/">Apache Tika</a>
 */
public class MimeUtils {

	private final static Logger LOGGER = getLogger(MimeUtils.class);

	/**
	 * Default value for unknown MIME types.
	 */
	public static final String BINARY_TYPE = "application/octet-stream";

	/**
	 * Checks whether a file is a text file.
	 * @param file - file to check
	 * @return {@code true} if the specified file is a text file. Otherwise, it returns {@code false}.
	 */
	public static boolean isTextFile(final File file) {
		final String mimeType = mimeType(file);
		return (isNotBlank(mimeType) && (mimeType.startsWith("text/") || mimeType.equals("application/xml")));
	}

	/**
	 * Performs an aggressive guess of MIME type by checking magic headers from an input file. File extension is ignored.
	 * @param file - file to check
	 * @return the base form of the MediaType, excluding any parameters, such as "text/plain" for "text/plain; charset=utf-8" 
	 *        of the file. If the MIME type is unknown, then {@link MimeUtils#BINARY_TYPE} is returned to the caller.
	 */
	public static String mimeType(final File file) {
		checkArgument(file != null, "Uninitialized file");
		String mimeType = null;
		try {
			final TikaConfig config = getDefaultConfig();
			final Detector detector = config.getDetector();
			TikaInputStream stream = null;
			try {
				stream = TikaInputStream.get(file);
				final MediaType mediaType = detector.detect(stream, new Metadata());
				mimeType = trimToNull(mediaType.getBaseType().toString());
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (Exception ignore) { }
				}			
			}
		} catch (Exception e) {
			LOGGER.info("Cannot find MIME type for file: '" + file.getPath() 
					+ "', '" + BINARY_TYPE + "' will be returned");
		} finally {
			if (isBlank(mimeType)) {
				mimeType = BINARY_TYPE;
			}
		}
		return mimeType;
	}

}