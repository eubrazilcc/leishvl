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

import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.util.Locale;

import static java.util.Locale.getISOCountries;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Utilities to deal with language.
 * @author Erik Torres <ertorser@upv.es>
 */
public final class LocaleUtils {

    /** Unmodifiable list of available locales **/
    private static ImmutableList<Locale> availableLocaleList = null; // lazily created by availableLocaleList()

    /**
     * Obtains an unmodifiable list of installed locales. This method is a wrapper around
     * {@link Locale#getAvailableLocales()}, which caches the locales and uses generics.
     * @return country names.
     */
    public static ImmutableList<Locale> availableLocaleList() {
        if (availableLocaleList == null) {
            synchronized (LocaleUtils.class) {
                if (availableLocaleList == null) {
                    final ImmutableList.Builder<Locale> builder = new ImmutableList.Builder<>();
                    final String[] locales = getISOCountries();
                    for (final String countryCode : locales) {
                        builder.add(new Locale("", countryCode));
                    }
                    availableLocaleList = builder.build();
                }
            }
        }
        return availableLocaleList;
    }

    /**
     * Obtains the locale that best-matches with the specified string.
     * @param str input string.
     * @return the locale that best-matches with the specified string, or {@code null}.
     */
    public static @Nullable Locale getLocale(final @Nullable String str) {
        Locale locale = null;
        if (isNotBlank(str)) {
            final ImmutableList<Locale> locales = availableLocaleList();
            for (int i = 0; i < locales.size() && locale == null; i++) {
                final Locale item = locales.get(i);
                if (containsIgnoreCase(str, item.getDisplayCountry())) {
                    locale = item;
                }
            }
        }
        return locale;
    }

}