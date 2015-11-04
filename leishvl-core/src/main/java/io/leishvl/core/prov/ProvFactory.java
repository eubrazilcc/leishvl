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

import static com.google.common.base.Preconditions.checkArgument;
import static io.leishvl.core.data.AnnotationCollectionUtils.getCollectionName;
import static io.leishvl.core.prov.Provenance.LVL_PREFIX;
import static io.leishvl.core.prov.Provenance.PROVENANCE;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;
import javax.sound.midi.Sequence;

import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Role;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasInformedBy;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.google.common.collect.Lists;

import io.leishvl.core.Citation;
import io.leishvl.core.security.User;

/**
 * Provides provenance factory methods.
 * @author Erik Torres <ertorser@upv.es>
 */
public final class ProvFactory {

	/* Document factory methods */

	public static Document newObjectImportProv(final ProvDataSource ds, final String lvlId, final @Nullable ProvGeocoding gc) {		
		final ProvDataSource ds2 = ProvDataSource.validate(ds);
		final String lvlId2 = parseParam(lvlId);
		final ProvGeocoding gc2 = (gc != null ? ProvGeocoding.validate(gc) : null);		

		final Document graph = newProvDocument();
		final Bundle bundle = getBundle(graph);
		final Agent system = getSystem(bundle);

		// import activity
		final Agent importer = PROVENANCE.softwareAgent(ds2.importer);
		bundle.getStatement().addAll(asList(importer,
				PROVENANCE.factory().newActedOnBehalfOf(null, importer.getId(), system.getId())
				));

		final Activity assignAct = PROVENANCE.factory().newActivity(PROVENANCE.qn("assign"));
		final WasAssociatedWith assignAssoc = PROVENANCE.factory().newWasAssociatedWith(null, assignAct.getId(), system.getId());
		final Activity importAct = PROVENANCE.factory().newActivity(PROVENANCE.qn("import"));
		final WasInformedBy importInf = PROVENANCE.factory().newWasInformedBy(null, assignAct.getId(), importAct.getId());
		final WasAssociatedWith importAssoc = PROVENANCE.factory().newWasAssociatedWith(null, importAct.getId(), importer.getId());		
		bundle.getStatement().addAll(asList(assignAct, assignAssoc, importAct, importInf, importAssoc));

		final Agent dataSource = PROVENANCE.factory().newAgent(PROVENANCE.qn(ds2.name), asList(new Attribute[] { 
				PROVENANCE.dataCatalogType(),
				PROVENANCE.urlAttr(ds2.url)
		}));
		final Entity entity = PROVENANCE.entity(LVL_PREFIX, ds2.type);
		final Entity ent1 = PROVENANCE.entity(LVL_PREFIX, ds2.objectId, PROVENANCE.datasetType());
		bundle.getStatement().addAll(asList(dataSource, entity, ent1,
				PROVENANCE.factory().newSpecializationOf(ent1.getId(), entity.getId()),
				PROVENANCE.factory().newWasAttributedTo(null, ent1.getId(), dataSource.getId()),
				PROVENANCE.factory().newUsed(null, importAct.getId(), ent1.getId())
				));

		// download activity
		final Agent downloader = PROVENANCE.softwareAgent(ds2.downloader);
		final Activity downloadAct = PROVENANCE.factory().newActivity(PROVENANCE.qn("download"));
		PROVENANCE.factory().addType(downloadAct, PROVENANCE.downloadActionType());
		final Entity sequenceFormat = PROVENANCE.entity(LVL_PREFIX, ds2.format, PROVENANCE.type(LVL_PREFIX, "DataFormat"));
		final Entity entRaw = PROVENANCE.entity(LVL_PREFIX, "file1");
		bundle.getStatement().addAll(asList(downloader, downloadAct, sequenceFormat, entRaw,
				PROVENANCE.factory().newActedOnBehalfOf(null, downloader.getId(), importer.getId()),
				PROVENANCE.factory().newWasAssociatedWith(null, downloadAct.getId(), downloader.getId()),
				PROVENANCE.factory().newUsed(null, downloadAct.getId(), ent1.getId()),
				PROVENANCE.factory().newSpecializationOf(entRaw.getId(), sequenceFormat.getId()),
				PROVENANCE.factory().newWasGeneratedBy(null, entRaw.getId(), downloadAct.getId(), PROVENANCE.factory().newTimeNow(), null)
				));

		// parse activity
		final Agent parser = PROVENANCE.softwareAgent(ds2.parser);
		final Activity parseAct = PROVENANCE.factory().newActivity(PROVENANCE.qn("parse"));
		final Entity entObj1 = PROVENANCE.entity(LVL_PREFIX, "obj1");
		bundle.getStatement().addAll(asList(parser, parseAct, entObj1,
				PROVENANCE.factory().newActedOnBehalfOf(null, parser.getId(), importer.getId()),
				PROVENANCE.factory().newWasAssociatedWith(null, parseAct.getId(), parser.getId()),
				PROVENANCE.factory().newUsed(null, parseAct.getId(), entRaw.getId()),
				PROVENANCE.factory().newWasGeneratedBy(null, entObj1.getId(), parseAct.getId())
				));

		Entity coord = null;
		if (gc2 != null) {
			// infer geographic coordinates (geocode) activity
			final Agent geocoder = PROVENANCE.softwareAgent(gc2.geocoder);
			final Activity geocodeAct = PROVENANCE.factory().newActivity(PROVENANCE.qn("geocode"));
			coord = PROVENANCE.entity(LVL_PREFIX, "coord1", PROVENANCE.locationAttr(gc2.point));
			bundle.getStatement().addAll(asList(geocoder, geocodeAct, coord,
					PROVENANCE.factory().newActedOnBehalfOf(null, geocoder.getId(), importer.getId()),
					PROVENANCE.factory().newWasAssociatedWith(null, geocodeAct.getId(), geocoder.getId()),
					PROVENANCE.factory().newUsed(null, geocodeAct.getId(), entObj1.getId()),
					PROVENANCE.factory().newWasGeneratedBy(null, coord.getId(), geocodeAct.getId())
					));
		}

		// create database record combining the different sources of information				
		final Activity combineAct = PROVENANCE.factory().newActivity(PROVENANCE.qn("combine"));		
		final Entity entObj2 = PROVENANCE.entity(LVL_PREFIX, "obj2");
		final List<Statement> statements = Lists.<Statement>newArrayList(
				combineAct, entObj2,
				PROVENANCE.factory().newWasAssociatedWith(null, combineAct.getId(), importer.getId()),
				PROVENANCE.factory().newUsed(null, combineAct.getId(), entObj1.getId()),						
				PROVENANCE.factory().newWasGeneratedBy(null, entObj2.getId(), combineAct.getId())
				);
		if (coord != null) statements.add(PROVENANCE.factory().newUsed(null, combineAct.getId(), coord.getId()));
		bundle.getStatement().addAll(statements);

		// add provenance information
		final Activity createProvAct = PROVENANCE.factory().newActivity(PROVENANCE.qn("createProv"));
		final Entity prov = PROVENANCE.entity(LVL_PREFIX, "prov1", PROVENANCE.bundleType());

		final Activity embedProvAct = PROVENANCE.factory().newActivity(PROVENANCE.qn("embedProv"));
		final Entity entObj3 = PROVENANCE.entity(LVL_PREFIX, "obj3");

		bundle.getStatement().addAll(asList(createProvAct, prov,
				PROVENANCE.factory().newWasAssociatedWith(null, createProvAct.getId(), importer.getId()),
				PROVENANCE.factory().newUsed(null, createProvAct.getId(), entObj2.getId()),
				PROVENANCE.factory().newWasGeneratedBy(null, prov.getId(), createProvAct.getId()),
				embedProvAct, entObj3,
				PROVENANCE.factory().newWasAssociatedWith(null, embedProvAct.getId(), importer.getId()),
				PROVENANCE.factory().newUsed(null, embedProvAct.getId(), entObj2.getId()),
				PROVENANCE.factory().newUsed(null, embedProvAct.getId(), prov.getId()),
				PROVENANCE.factory().newWasDerivedFrom(null, entObj3.getId(), entObj2.getId(), embedProvAct.getId(), null, null, null),
				PROVENANCE.factory().newWasDerivedFrom(null, entObj3.getId(), prov.getId(), embedProvAct.getId(), null, null, null),
				PROVENANCE.factory().newWasGeneratedBy(null, entObj3.getId(), embedProvAct.getId())
				));

		// insert document draft in the database
		final Agent collection = PROVENANCE.factory().newAgent(PROVENANCE.qn(ds2.collection), asList(new Attribute[] { 
				PROVENANCE.dataCatalogType(),
		}));
		final Agent dbClient = PROVENANCE.softwareAgent(ds2.dbObject);
		final Activity insertIntoDbAct = PROVENANCE.factory().newActivity(PROVENANCE.qn("register"));
		final Entity entityDocument = PROVENANCE.entity(LVL_PREFIX, "LvlDocument");
		final Entity entObj4 = PROVENANCE.entity(LVL_PREFIX, lvlId2, PROVENANCE.datasetType());		
		bundle.getStatement().addAll(asList(collection, dbClient, insertIntoDbAct, entityDocument, entObj4,
				PROVENANCE.factory().newActedOnBehalfOf(null, dbClient.getId(), importer.getId()),
				PROVENANCE.factory().newWasAssociatedWith(null, insertIntoDbAct.getId(), dbClient.getId()),
				PROVENANCE.factory().newUsed(null, insertIntoDbAct.getId(), entObj3.getId()),
				PROVENANCE.factory().newSpecializationOf(entObj4.getId(), entityDocument.getId()),
				PROVENANCE.factory().newWasAttributedTo(null, entObj4.getId(), collection.getId()),
				PROVENANCE.factory().newWasGeneratedBy(null, entObj4.getId(), importAct.getId()),
				PROVENANCE.factory().newWasDerivedFrom(null, entObj4.getId(), entObj3.getId(), insertIntoDbAct.getId(), null, null, null),
				PROVENANCE.factory().newWasEndedBy(null, importAct.getId(), entObj4.getId(), insertIntoDbAct.getId())				
				));
		return graph;
	}

	public static Document newCustomObjectProv(final User creator, final String lvlId) {		
		final String lvlId2 = parseParam(lvlId);
		checkArgument(creator != null, "Uninitialized creator");
		checkArgument(isNotBlank(creator.getUserid()), "Uninitialized user Id");
		checkArgument(isNotBlank(creator.getProvider()), "Uninitialized identity provider");

		final Document graph = newProvDocument();
		final Bundle bundle = getBundle(graph);
		final Agent system = getSystem(bundle);

		// create activity
		final Entity entObj1 = PROVENANCE.entity(LVL_PREFIX, lvlId2, PROVENANCE.datasetType());
		final Agent creatorAgent = PROVENANCE.personAgent(creator);
		final Activity createAct = PROVENANCE.factory().newActivity(PROVENANCE.qn("create"));
		bundle.getStatement().addAll(asList(entObj1, creatorAgent, createAct,
				PROVENANCE.factory().newUsed(null, createAct.getId(), entObj1.getId()),
				PROVENANCE.factory().newWasGeneratedBy(null, entObj1.getId(), createAct.getId()),
				PROVENANCE.factory().newActedOnBehalfOf(null, creatorAgent.getId(), system.getId()),
				PROVENANCE.factory().newWasAssociatedWith(null, createAct.getId(), creatorAgent.getId())
				));
		return graph;
	}

	public static Document newReleaseProv(final User editor, final String lvlId, final String original, final String revision) {
		final String lvlId2 = parseParam(lvlId);
		checkArgument(editor != null, "Uninitialized editor");
		checkArgument(isNotBlank(editor.getUserid()), "Uninitialized user Id");
		checkArgument(isNotBlank(editor.getProvider()), "Uninitialized identity provider");

		final Document graph = newProvDocument();
		final Bundle bundle = getBundle(graph);
		final Agent system = getSystem(bundle);

		// edit activity
		final Agent curatorAgent = PROVENANCE.personAgent(editor);
		final Entity entObj1 = PROVENANCE.entity(LVL_PREFIX, lvlId2 + original);
		final Entity entObj2 = PROVENANCE.entity(LVL_PREFIX, lvlId2 + revision);
		final Activity editAct = PROVENANCE.factory().newActivity(PROVENANCE.qn("edit"));
		final Role curatorRole = PROVENANCE.factory().newRole("Curator", PROVENANCE.qn("Curator"));
		final Used usedObject = PROVENANCE.factory().newUsed(null, editAct.getId(), entObj1.getId());
		PROVENANCE.factory().addRole(usedObject, curatorRole);
		bundle.getStatement().addAll(asList(curatorAgent, editAct, entObj1, entObj2,
				PROVENANCE.factory().newActedOnBehalfOf(null, curatorAgent.getId(), system.getId()),
				PROVENANCE.factory().newWasAssociatedWith(null, editAct.getId(), curatorAgent.getId()),
				usedObject,
				PROVENANCE.factory().newWasInvalidatedBy(null, entObj1.getId(), editAct.getId()),
				PROVENANCE.factory().newWasGeneratedBy(null, entObj2.getId(), editAct.getId(), PROVENANCE.factory().newTimeNow(), null),
				PROVENANCE.factory().newWasDerivedFrom(null, entObj2.getId(), entObj1.getId(), editAct.getId(), null, null, null)
				));
		return graph;
	}

	public static Document newObsoleteProv(final User editor, final String lvlId) {
		final String lvlId2 = parseParam(lvlId);
		checkArgument(editor != null, "Uninitialized editor");
		checkArgument(isNotBlank(editor.getUserid()), "Uninitialized user Id");
		checkArgument(isNotBlank(editor.getProvider()), "Uninitialized identity provider");

		final Document graph = newProvDocument();
		final Bundle bundle = getBundle(graph);
		final Agent system = getSystem(bundle);

		// edit activity
		final Agent curatorAgent = PROVENANCE.personAgent(editor);
		final Entity entObj1 = PROVENANCE.entity(LVL_PREFIX, lvlId2);
		final Activity invalidateAct = PROVENANCE.factory().newActivity(PROVENANCE.qn("invalidate"));
		final Role curatorRole = PROVENANCE.factory().newRole("Curator", PROVENANCE.qn("Curator"));
		final Used usedObject = PROVENANCE.factory().newUsed(null, invalidateAct.getId(), entObj1.getId());
		PROVENANCE.factory().addRole(usedObject, curatorRole);
		bundle.getStatement().addAll(asList(curatorAgent, invalidateAct, entObj1,
				PROVENANCE.factory().newActedOnBehalfOf(null, curatorAgent.getId(), system.getId()),
				PROVENANCE.factory().newWasAssociatedWith(null, invalidateAct.getId(), curatorAgent.getId()),
				usedObject,
				PROVENANCE.factory().newWasInvalidatedBy(null, entObj1.getId(), invalidateAct.getId(), PROVENANCE.factory().newTimeNow(), null)
				));
		return graph;
	}

	public static Document combineProv(final Document... documents) {
		final Document graph = PROVENANCE.factory().newDocument();
		for (final Document doc : documents) {
			for (final StatementOrBundle item : doc.getStatementOrBundle()) {
				graph.getStatementOrBundle().add(item);
			}
		}
		graph.setNamespace(PROVENANCE.ns());
		return graph;
	}

	/* Type factory methods */

	public static ProvDataSource newGenBankSequence(final String objectId, final String collection) {
		ProvDataSource ds = null;
		try {
			ds = new ProvDataSource("GenBank", new URL("http://www.ncbi.nlm.nih.gov/genbank/"), objectId, Sequence.class.getSimpleName(),
					EntrezHelper.class, HttpClientProvider.class, GbSeqXmlBinder.class, "GBSeqXML", 
					Sequence.class, collection);
		} catch (MalformedURLException ignore) { }
		return ds;
	}

	public static ProvDataSource newPubMedArticle(final String objectId) {
		ProvDataSource ds = null;
		try {
			ds = new ProvDataSource("PubMed", new URL("http://www.ncbi.nlm.nih.gov/pubmed/"), objectId, Citation.class.getSimpleName(),
					EntrezHelper.class, HttpClientProvider.class, PubMedXmlBinder.class, "PubMedXML", 
					Citation.class, getCollectionName(Citation.class));
		} catch (MalformedURLException ignore) { }
		return ds;
	}

	public static ProvGeocoding newGeocoding(final GeoJsonPoint point) {
		return new ProvGeocoding(point, GeocodingHelper.class);
	}

	/* Document modifiers */

	public static void addEditProv(final Document graph, final User editor, final String lvlId) {
		final String lvlId2 = parseParam(lvlId);		
		checkArgument(editor != null, "Uninitialized editor");
		checkArgument(isNotBlank(editor.getUserid()), "Uninitialized user Id");
		checkArgument(isNotBlank(editor.getProvider()), "Uninitialized identity provider");		

		final Bundle bundle = getBundle(graph);
		final Agent system = getSystem(bundle);		

		// imported draft is open to future editions
		final QualifiedName editActQn = PROVENANCE.qn("edit");
		final QualifiedName revisedDraftQn = PROVENANCE.qn(LVL_PREFIX, "RevisedDraft");
		final Optional<Statement> revisedDraftOpt = ofNullable(bundle.getStatement().stream()
				.filter(stmt -> stmt instanceof Entity && revisedDraftQn.equals(((Entity)stmt).getId()))
				.findFirst().orElse(null));
		final Entity revisedDraft = (Entity) revisedDraftOpt.orElse(PROVENANCE.entity(LVL_PREFIX, "RevisedDraft"));
		if (!revisedDraftOpt.isPresent()) {
			final Activity editAct = PROVENANCE.factory().newActivity(editActQn);
			final QualifiedName objQn = PROVENANCE.qn(LVL_PREFIX, lvlId2);
			bundle.getStatement().addAll(asList(editAct, revisedDraft,
					PROVENANCE.factory().newUsed(null, editAct.getId(), objQn),
					PROVENANCE.factory().newWasDerivedFrom(null, revisedDraft.getId(), objQn),
					PROVENANCE.factory().newWasGeneratedBy(null, revisedDraft.getId(), editAct.getId())
					));
		}

		// add editor
		final QualifiedName editorQn = PROVENANCE.qn(LVL_PREFIX, editor.getUserid());		
		final Optional<Statement> editorAgentOpt = ofNullable(bundle.getStatement().stream()
				.filter(stmt -> stmt instanceof Agent && editorQn.equals(((Agent)stmt).getId()))
				.findFirst().orElse(null));		
		final Agent editorAgent = (Agent) editorAgentOpt.orElse(PROVENANCE.personAgent(editor));
		if (!editorAgentOpt.isPresent()) {			
			bundle.getStatement().addAll(asList(editorAgent,
					PROVENANCE.factory().newActedOnBehalfOf(null, editorAgent.getId(), system.getId()),
					PROVENANCE.factory().newWasAssociatedWith(null, editActQn, editorAgent.getId())
					));
		}
	}

	/* Auxiliary methods */

	private static Bundle getBundle(final Document graph) {
		checkArgument(graph != null, "Uninitialized provenance document");		
		final StatementOrBundle node = graph.getStatementOrBundle().stream().filter(stmt -> stmt != null).findFirst().orElse(null);
		checkArgument(node != null && node instanceof Bundle, "Uninitialized or invalid provenance bundle");
		return (Bundle)node;
	}

	private static Agent getSystem(final Bundle bundle) {
		final QualifiedName systemQn = PROVENANCE.lvlAgent().getId();		
		return (Agent) bundle.getStatement().stream()
				.filter(stmt -> stmt instanceof Agent && systemQn.equals(((Agent)stmt).getId()))
				.findFirst().orElse(null);
	}

	private static String parseParam(final String param) {
		String param2 = null;
		checkArgument((param2 = trimToNull(param)) != null, "Uninitialized or invalid parameter");
		return param2;
	}

	private static Document newProvDocument() {
		final Document graph = PROVENANCE.factory().newDocument();

		// system initialization
		final String provId = randomProvId();
		final Bundle bundle = PROVENANCE.factory().newNamedBundle(PROVENANCE.qn(provId), PROVENANCE.ns(), Lists.<Statement>newArrayList());
		final Agent system = PROVENANCE.lvlAgent();
		final Entity provBundle = PROVENANCE.entity(LVL_PREFIX, provId, PROVENANCE.bundleType());		
		bundle.getStatement().addAll(asList(system, provBundle,
				PROVENANCE.factory().newWasAttributedTo(PROVENANCE.qn("attr"), provBundle.getId(), system.getId())
				));

		graph.getStatementOrBundle().add(bundle);
		graph.setNamespace(PROVENANCE.ns());
		return graph;
	}

	private static String randomProvId() {
		return "PROV-" + randomUUID().toString().replace("-", "");
	}

	/* Inner types */

	public static class ProvDataSource {		
		private final String name;
		private final URL url;
		private final String objectId;
		private final String type;

		private final Class<?> importer;
		private final Class<?> downloader;
		private final Class<?> parser;
		private final String format;

		private final Class<?> dbObject;
		private final String collection;

		public ProvDataSource(final String name, final URL url, final String objectId, final String type,
				final Class<?> importer, final Class<?> downloader, final Class<?> parser, final String format,
				final Class<?> dbObject, final String collection) {
			this.name = name;
			this.url = url;
			this.objectId = objectId;
			this.type = type;
			this.importer = importer;
			this.downloader = downloader;
			this.parser = parser;
			this.format = format;
			this.dbObject = dbObject;
			this.collection = collection;
		}

		public static ProvDataSource validate(final ProvDataSource original) {
			checkArgument(original != null, "Uninitialized data source");
			String name2 = null;
			checkArgument((name2 = trimToNull(original.name)) != null, "Uninitialized or invalid name");
			checkArgument(original.url != null, "Uninitialized URL");
			String objectId2 = null;
			checkArgument((objectId2 = trimToNull(original.objectId)) != null, "Uninitialized or invalid object Id");
			String type2 = null;
			checkArgument((type2 = trimToNull(original.type)) != null, "Uninitialized or invalid type");
			checkArgument(original.importer != null, "Uninitialized importer");
			checkArgument(original.downloader != null, "Uninitialized downloader");
			checkArgument(original.parser != null, "Uninitialized parser");
			String format2 = null;
			checkArgument((format2 = trimToNull(original.format)) != null, "Uninitialized or invalid data format");
			checkArgument(original.dbObject != null, "Uninitialized database object");
			String collection2 = null;
			checkArgument((collection2 = trimToNull(original.collection)) != null, "Uninitialized or invalid collection");
			return new ProvDataSource(name2, original.url, objectId2, type2, 
					original.importer, original.downloader, original.parser, format2, 
					original.dbObject, collection2);
		}
	}

	public static class ProvGeocoding {
		private final GeoJsonPoint point;
		private final Class<?> geocoder;

		public ProvGeocoding(final GeoJsonPoint point, final Class<?> geocoder) {
			this.point = point;
			this.geocoder = geocoder;
		}

		public static ProvGeocoding validate(final ProvGeocoding original) {
			checkArgument(original != null, "Uninitialized geocoding");
			checkArgument(original.point != null, "Uninitialized location point");
			checkArgument(original.geocoder != null, "Uninitialized geocoder");
			return original;
		}
	}

	// TODO

	public static class EntrezHelper { } // TODO
	public static class HttpClientProvider { } // TODO
	public static class GbSeqXmlBinder { } // TODO
	public static class PubMedXmlBinder { } // TODO
	public static class GeocodingHelper { } // TODO

}