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

package io.leishvl.core.prov;

import static io.leishvl.core.DataSource.LEISHVL;
import static io.leishvl.core.DataSource.LEISHVL_SHORT;
import static io.leishvl.core.geospatial.GeoJsons.lngLat2Human;
import static java.util.Arrays.asList;
import static org.openprovenance.prov.interop.InteropFramework.newXMLProvFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import javax.annotation.Nullable;

import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Type;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.google.common.collect.Lists;

import io.leishvl.core.security.User;

/**
 * Definitions and other constants related to data provenance.
 * @author Erik Torres <ertorser@upv.es>
 */
public enum Provenance {

	PROVENANCE;

	public static final String LVL_NS = "http://eubrazilcc.eu/lvl#";
	public static final String LVL_PREFIX = LEISHVL_SHORT;

	/**
	 * The Schema.org Vocabulary is an RDF Schema vocabulary for expressing metadata about Internet data.
	 */
	public static final String SOV_NS = "http://schema.org/";
	public static final String SOV_PREFIX = "sov";	

	private final ProvFactory provFactory = newXMLProvFactory();
	private final Namespace ns = new Namespace();

	Provenance() {
		ns.addKnownNamespaces();
		ns.register(LVL_PREFIX, LVL_NS);
		ns.register(SOV_PREFIX, SOV_NS);		
	}

	public ProvFactory factory() {
		return provFactory;
	}

	public Namespace ns() {
		return ns;
	}

	public QualifiedName qn(final String name) {
		return qn(LVL_PREFIX, name);
	}

	public QualifiedName qn(final String prefix, final String name) {
		return ns.qualifiedName(prefix, name, provFactory);
	}

	/* Types */

	public Type type(final String prefix, final String type) {
		final QualifiedName qn = PROVENANCE.qn(prefix, type);
		return PROVENANCE.factory().newType(qn.getUri(), qn);
	}

	/* Types using PROV vocabulary */

	public Type bundleType() {
		return type("prov", "Bundle");
	}

	public Type collectionType() {
		return type("prov", "Collection");
	}

	public Type softwareAgentType() {
		return type("prov", "SoftwareAgent");
	}

	/* Types using Schema.org vocabulary */

	public Type dataCatalogType() {
		return type("sov", "DataCatalog");
	}

	public Type datasetType() {
		return type("sov", "Dataset");
	}

	public Type downloadActionType() {
		return type("sov", "DownloadAction");
	}

	public Type organizationType() {
		return type("sov", "Organization");
	}

	public Type personType() {
		return type("sov", "Person");
	}

	/* Application types */
	public Type approvalType() {
		return type(LVL_PREFIX, "Approval");
	}

	/* Attributes */

	public Attribute classAttr(final Class<?> clazz) {
		return (Attribute) PROVENANCE.factory().newOther(PROVENANCE.qn("class"), clazz.getCanonicalName(), PROVENANCE.factory().getName().XSD_STRING);
	}

	public Attribute identityProviderAttr(final String provider) {
		return (Attribute) PROVENANCE.factory().newOther(PROVENANCE.qn("idp"), provider, PROVENANCE.factory().getName().XSD_STRING);
	}

	public Attribute locationAttr(final @Nullable GeoJsonPoint point) {
		final String coord = (point != null) ? lngLat2Human(point) : "";
		return (Attribute) PROVENANCE.factory().newOther(PROVENANCE.qn("location"), coord, PROVENANCE.factory().getName().XSD_STRING);
	}

	public Attribute urlAttr(final String url) {
		URL url2 = null;
		try {
			url2 = new URL(url);
		} catch (MalformedURLException ignore) { }
		return urlAttr(url2);
	}

	public Attribute urlAttr(final URL url) {
		return (Attribute) PROVENANCE.factory().newOther(PROVENANCE.qn("url"), url, PROVENANCE.factory().getName().XSD_ANY_URI);
	}

	public Attribute revisionAttr(final String rev) {
		return (Attribute) PROVENANCE.factory().newOther(PROVENANCE.qn("rev"), rev, PROVENANCE.factory().getName().XSD_STRING);
	}

	/* Agents */

	public Agent lvlAgent() {
		final QualifiedName qn = PROVENANCE.qn(LEISHVL);
		return PROVENANCE.factory().newAgent(qn, asList(new Attribute[] {
				type(LVL_PREFIX, LEISHVL),
				revisionAttr("0.3.0")
		}));
	}

	public Agent organizationAgent(final String name) {
		return PROVENANCE.factory().newAgent(PROVENANCE.qn(name), asList(new Attribute[] {
				PROVENANCE.organizationType()
		}));
	}

	public Agent personAgent(final User user) {
		return PROVENANCE.factory().newAgent(PROVENANCE.qn(user.getUserid()), asList(new Attribute[] {
				PROVENANCE.personType(),
				PROVENANCE.identityProviderAttr(user.getProvider())
		}));
	}

	public Agent softwareAgent(final Class<?> clazz) {
		return PROVENANCE.factory().newAgent(PROVENANCE.qn(clazz.getSimpleName()), asList(new Attribute[] {
				PROVENANCE.softwareAgentType(),
				PROVENANCE.classAttr(clazz)
		}));
	}

	/* Entities */

	public Entity entity(final QualifiedName qn, final String entity, final Attribute... attrs) {
		return entity(qn.getPrefix(), entity, attrs);
	}

	public Entity entity(final String prefix, final String entity, final Attribute... attrs) {
		return entity(prefix, entity, attrs != null ? asList(attrs) : null);
	}

	public Entity entity(final String prefix, final String entity, final @Nullable Collection<Attribute> attrs) {
		final QualifiedName qn = PROVENANCE.qn(prefix, entity);
		return PROVENANCE.factory().newEntity(qn, attrs);
	}

	public Entity collection(final String collection) {
		return PROVENANCE.factory().newEntity(qn(collection), Lists.<Attribute>newArrayList(collectionType()));		
	}

}