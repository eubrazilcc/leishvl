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

package io.leishvl.storage.mongodb;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.collect.Lists.newArrayList;
import static io.leishvl.storage.mongodb.jackson.MongoJsonMapper.JSON_MAPPER;
import static io.leishvl.storage.mongodb.jackson.MongoJsonMapper.objectToJson;
import static io.leishvl.storage.mongodb.jackson.MongoJsonOptions.JSON_PRETTY_PRINTER;
import static java.util.Collections.emptyList;

/**
 * Collection statistics.
 * @author Erik Torres <ertorser@upv.es>
 */
public class MongoCollectionStats {

    private String name;
    private long count;
    private List<String> indexes;

    public MongoCollectionStats() { }

    public MongoCollectionStats(final String name) {
        this.name = name;
        this.count = 0l;
        this.indexes = newArrayList();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(final long count) {
        this.count = count;
    }

    public List<String> getIndexes() {
        return indexes;
    }

    public void setIndexes(final List<String> indexes) {
        this.indexes = indexes;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof MongoCollectionStats)) {
            return false;
        }
        final MongoCollectionStats other = MongoCollectionStats.class.cast(obj);
        return Objects.equals(name, other.name)
                && Objects.equals(count, other.count)
                && Objects.equals(indexes, other.indexes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, count, indexes);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("name", name)
                .add("count", count)
                .add("indexes", (indexes != null ? indexes : emptyList()).stream().map(idx -> {
                    String str = null;
                    try {
                        final Object json = JSON_MAPPER.readValue((String)idx, Object.class);
                        str = objectToJson(json, JSON_PRETTY_PRINTER);
                    } catch (IOException e) {
                        // ignore
                    }
                    return str;
                }).filter(Objects::nonNull).collect(Collectors.toList()))
                .toString();
    }

}