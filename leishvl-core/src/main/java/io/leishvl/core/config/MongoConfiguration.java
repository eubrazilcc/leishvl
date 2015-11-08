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

package io.leishvl.core.config;

import static io.leishvl.core.data.mongodb.GeoJsonConverters.getGeoJsonConvertersToRegister;
import static io.leishvl.core.data.mongodb.ProvConverters.getProvConvertersToRegister;
import static org.apache.commons.collections.ListUtils.union;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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

import io.leishvl.core.data.CitationRepository;

/**
 * Spring configuration for MongoDB database.
 * @author Erik Torres <ertorser@upv.es>
 */
@Configuration
@Profile("data")
@EnableMongoRepositories(basePackageClasses={ CitationRepository.class })
public class MongoConfiguration extends AbstractMongoConfiguration {

	@Autowired
	private Environment env;

	@Override
	public String getDatabaseName() {
		return env.getRequiredProperty("spring.data.mongodb.database");
	}

	@Override
	public Mongo mongo() throws Exception {
		return mongoClient();
	}

	@Bean
	protected MongoClient mongoClient() throws UnknownHostException {
		return new MongoClient(new MongoClientURI(env.getRequiredProperty("spring.data.mongodb.uri")));
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
		/* converter.setMapKeyDotReplacement("\\+"); is not needed since the custom conversions perform 
		 * this tasks in the required fields, avoiding unnecessary operations */
		return converter;
	}

}