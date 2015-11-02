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

import static io.leishvl.data.mongodb.GeoJsonConverters.getGeoJsonConvertersToRegister;
import static io.leishvl.data.mongodb.ProvConverters.getProvConvertersToRegister;
import static org.apache.commons.collections.ListUtils.union;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * Spring configuration.
 * @author Erik Torres <ertorser@upv.es>
 */
@Configuration
@EnableMongoRepositories(basePackages = "io.leishvl.data")
public class IntegrationTestApplicationConfig extends AbstractMongoConfiguration {

	@Autowired
	private Environment env;

	@Override
	public String getDatabaseName() {
		return env.getRequiredProperty("mongo.db.name");
	}

	@Override
	public Mongo mongo() throws Exception {
		return mongoClient();
	}

	@Bean
	protected MongoClient mongoClient() throws UnknownHostException {
		return new MongoClient(new MongoClientURI("mongodb://127.0.0.1:27017/" + getDatabaseName()));
	}

	@Override
	@Bean
	public SimpleMongoDbFactory mongoDbFactory() throws UnknownHostException {
		return new SimpleMongoDbFactory(mongoClient(), getDatabaseName());
	}

	@Override
	@Bean
	public CustomConversions customConversions() {		
		return new CustomConversions(union(getGeoJsonConvertersToRegister(), getProvConvertersToRegister()));
	}

	@Override
	@Bean
	public MappingMongoConverter mappingMongoConverter() throws Exception {
		final DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
		final MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext());
		converter.setCustomConversions(customConversions());
		converter.setMapKeyDotReplacement("\\+");
		return converter;
	}

}