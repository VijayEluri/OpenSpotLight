/*
 * OpenSpotLight - Open Source IT Governance Platform
 *
 * Copyright (c) 2009, CARAVELATECH CONSULTORIA E TECNOLOGIA EM INFORMATICA LTDA
 * or third-party contributors as indicated by the @author tags or express
 * copyright attribution statements applied by the authors.  All third-party
 * contributions are distributed under license by CARAVELATECH CONSULTORIA E
 * TECNOLOGIA EM INFORMATICA LTDA.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License  for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 ***********************************************************************
 * OpenSpotLight - Plataforma de Governança de TI de Código Aberto
 *
 * Direitos Autorais Reservados (c) 2009, CARAVELATECH CONSULTORIA E TECNOLOGIA
 * EM INFORMATICA LTDA ou como contribuidores terceiros indicados pela etiqueta
 * @author ou por expressa atribuição de direito autoral declarada e atribuída pelo autor.
 * Todas as contribuições de terceiros estão distribuídas sob licença da
 * CARAVELATECH CONSULTORIA E TECNOLOGIA EM INFORMATICA LTDA.
 *
 * Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free Software
 * Foundation.
 *
 * Este programa é distribuído na expectativa de que seja útil, porém, SEM NENHUMA
 * GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU ADEQUAÇÃO A UMA
 * FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral Menor do GNU para mais detalhes.
 *
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto com este
 * programa; se não, escreva para:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.openspotlight.bundle.db.processor;

import static org.openspotlight.common.util.Files.delete;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openspotlight.bundle.db.metamodel.link.ColumnDataType;
import org.openspotlight.bundle.db.metamodel.node.Catalog;
import org.openspotlight.bundle.db.metamodel.node.Column;
import org.openspotlight.bundle.db.metamodel.node.Database;
import org.openspotlight.bundle.db.metamodel.node.DatabaseConstraintForeignKey;
import org.openspotlight.bundle.db.metamodel.node.DatabaseConstraintPrimaryKey;
import org.openspotlight.bundle.db.metamodel.node.Schema;
import org.openspotlight.bundle.db.metamodel.node.Server;
import org.openspotlight.bundle.db.metamodel.node.TableView;
import org.openspotlight.bundle.db.metamodel.node.TableViewTable;
import org.openspotlight.bundle.db.metamodel.node.TableViewView;
import org.openspotlight.common.util.Collections;
import org.openspotlight.federation.context.DefaultExecutionContextFactory;
import org.openspotlight.federation.context.ExecutionContext;
import org.openspotlight.federation.context.ExecutionContextFactory;
import org.openspotlight.federation.domain.ArtifactFinderRegistry;
import org.openspotlight.federation.domain.ArtifactSourceMapping;
import org.openspotlight.federation.domain.BundleProcessorType;
import org.openspotlight.federation.domain.BundleSource;
import org.openspotlight.federation.domain.DatabaseType;
import org.openspotlight.federation.domain.DbArtifactSource;
import org.openspotlight.federation.domain.ExportedFk;
import org.openspotlight.federation.domain.GlobalSettings;
import org.openspotlight.federation.domain.Group;
import org.openspotlight.federation.domain.Repository;
import org.openspotlight.federation.domain.TableArtifact;
import org.openspotlight.federation.finder.ArtifactFinder;
import org.openspotlight.federation.finder.ArtifactFinderBySourceProvider;
import org.openspotlight.federation.finder.DatabaseCustomArtifactFinderBySourceProvider;
import org.openspotlight.federation.finder.db.DatabaseSupport;
import org.openspotlight.federation.scheduler.DefaultScheduler;
import org.openspotlight.federation.scheduler.GlobalSettingsSupport;
import org.openspotlight.graph.SLConsts;
import org.openspotlight.graph.SLContext;
import org.openspotlight.graph.SLLink;
import org.openspotlight.graph.SLNode;
import org.openspotlight.jcr.provider.DefaultJcrDescriptor;
import org.openspotlight.jcr.provider.JcrConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbTableArtifactBundleProcessorTest {

	private static class RepositoryData {
		public final GlobalSettings settings;
		public final Repository repository;
		public final Group group;
		public final DbArtifactSource artifactSource;

		public RepositoryData(final GlobalSettings settings,
				final Repository repository, final Group group,
				final DbArtifactSource artifactSource) {
			this.settings = settings;
			this.repository = repository;
			this.group = group;
			this.artifactSource = artifactSource;
		}
	}

	public static class SampleDbArtifactRegistry implements
			ArtifactFinderRegistry {

		public Set<ArtifactFinderBySourceProvider> getRegisteredArtifactFinderProviders() {
			return Collections
					.<ArtifactFinderBySourceProvider> setOf(new DatabaseCustomArtifactFinderBySourceProvider());
		}

	}

	private static ExecutionContextFactory contextFactory;
	private static RepositoryData data;
	private static DefaultScheduler scheduler;

	@AfterClass
	public static void closeResources() throws Exception {
		scheduler.stopScheduler();
		contextFactory.closeResources();
	}

	private static RepositoryData createRepositoryData() {
		final GlobalSettings settings = new GlobalSettings();
		settings.setDefaultSleepingIntervalInMilliseconds(1000);
		settings.setNumberOfParallelThreads(1);
		settings.setArtifactFinderRegistryClass(SampleDbArtifactRegistry.class);
		GlobalSettingsSupport.initializeScheduleMap(settings);
		final Repository repository = new Repository();
		repository.setName("sampleRepository");
		repository.setActive(true);
		final Group group = new Group();
		group.setName("sampleGroup");
		group.setRepository(repository);
		repository.getGroups().add(group);
		group.setActive(true);
		final DbArtifactSource artifactSource = new DbArtifactSource();
		repository.getArtifactSources().add(artifactSource);
		artifactSource.setRepository(repository);
		artifactSource.setName("h2");
		artifactSource.setActive(true);
		artifactSource.setUser("sa");
		artifactSource.setPassword("sa");
		artifactSource.setMaxConnections(4);
		artifactSource.setDatabaseName("db");
		artifactSource.setServerName("server name");
		artifactSource.setType(DatabaseType.H2);
		artifactSource
				.setInitialLookup("jdbc:h2:./target/test-data/DbTableArtifactBundleProcessorTest/h2/db;DB_CLOSE_ON_EXIT=FALSE");
		artifactSource.setDriverClass("org.h2.Driver");

		final ArtifactSourceMapping mapping = new ArtifactSourceMapping();
		mapping.setSource(artifactSource);
		artifactSource.getMappings().add(mapping);
		mapping.setFrom("*/");
		mapping.setTo("/databaseArtifacts");
		artifactSource.getMappings().add(mapping);
		mapping.getIncludeds().add("*");
		final BundleProcessorType commonProcessor = new BundleProcessorType();
		commonProcessor.setActive(true);
		commonProcessor.setGroup(group);
		commonProcessor.setType(DbTableArtifactBundleProcessor.class);
		group.getBundleTypes().add(commonProcessor);

		final BundleSource bundleSource = new BundleSource();
		commonProcessor.getSources().add(bundleSource);
		bundleSource.setBundleProcessorType(commonProcessor);
		bundleSource.setRelative("/databaseArtifacts");
		bundleSource.getIncludeds().add("*");

		return new RepositoryData(settings, repository, group, artifactSource);
	}

	@BeforeClass
	public static void setupResources() throws Exception {
		delete("./target/test-data/DbTableArtifactBundleProcessorTest"); //$NON-NLS-1$

		JcrConnectionProvider.createFromData(
				DefaultJcrDescriptor.TEMP_DESCRIPTOR)
				.closeRepositoryAndCleanResources();

		data = createRepositoryData();

		contextFactory = DefaultExecutionContextFactory.createFactory();

		final ExecutionContext context = contextFactory.createExecutionContext(
				"username", "password", DefaultJcrDescriptor.TEMP_DESCRIPTOR,
				data.repository.getName());

		context.getDefaultConfigurationManager().saveGlobalSettings(
				data.settings);
		context.getDefaultConfigurationManager()
				.saveRepository(data.repository);
		context.closeResources();

		scheduler = DefaultScheduler.INSTANCE;
		scheduler.initializeSettings(contextFactory, "user", "password",
				DefaultJcrDescriptor.TEMP_DESCRIPTOR);
		scheduler
				.refreshJobs(data.settings, Collections.setOf(data.repository));
		scheduler.startScheduler();

	}

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@After
	public void closeTestResources() {
		contextFactory.closeResources();
	}

	private void reloadArtifactsAndCallBundleProcessor() {
		scheduler.fireSchedulable("username", "password", data.artifactSource);
		scheduler.fireSchedulable("username", "password", data.group);
	}

	@Test
	public void shouldExecuteBundleProcessor() throws Exception {
		final Random r = new Random();
		final String tableSufix = r.nextInt(50) + "_" + r.nextInt(50) + "_"
				+ r.nextInt(50);
		final Connection connection = DatabaseSupport
				.createConnection(data.artifactSource);

		connection
				.prepareStatement(
						"create table exampleTable"
								+ tableSufix
								+ "(i int not null primary key, last_i_plus_2 int, s smallint, f float, dp double precision, v varchar(10) not null)")
				.execute();
		connection
				.prepareStatement(
						"create view exampleView"
								+ tableSufix
								+ " (s_was_i, dp_was_s, i_was_f, f_was_dp) as select i,s,f,dp from exampleTable"
								+ tableSufix).execute();
		connection.prepareStatement(
				"create table anotherTable" + tableSufix
						+ "(i int not null primary key, i_fk int)").execute();

		connection.prepareStatement(
				"alter table anotherTable" + tableSufix
						+ " add constraint example_fk" + tableSufix
						+ " foreign key(i_fk) references exampleTable"
						+ tableSufix + "(i)").execute();
		connection.close();

		reloadArtifactsAndCallBundleProcessor();

		final ExecutionContext executionContext = contextFactory
				.createExecutionContext("username", "password",
						DefaultJcrDescriptor.TEMP_DESCRIPTOR, data.repository
								.getName());

		final ArtifactFinder<TableArtifact> tableFinder = executionContext
				.getArtifactFinder(TableArtifact.class);
		final TableArtifact table = tableFinder
				.findByPath("/databaseArtifacts/PUBLIC/TABLE/DB/EXAMPLETABLE"
						+ tableSufix);
		boolean foundFkInsideArtifact = false;
		for (final org.openspotlight.federation.domain.Column c : table
				.getColumns()) {
			if (c.getName().equalsIgnoreCase("i")) {
				Assert.assertThat(c.getExportedFks().size() > 0, Is.is(true));
				for (final ExportedFk fk : c.getExportedFks()) {
					logger.info("        >>> getTableSchema   "
							+ fk.getTableSchema());
					logger.info("        >>> getTableCatalog  "
							+ fk.getTableCatalog());
					logger.info("        >>> getTableName     "
							+ fk.getTableName());
					logger.info("        >>> getColumnName    "
							+ fk.getColumnName());
					logger.info("        >>> getFkName        "
							+ fk.getFkName());
				}

				foundFkInsideArtifact = true;
			}
		}
		Assert.assertThat(foundFkInsideArtifact, Is.is(true));

		final SLContext groupContext = executionContext.getGraphSession()
				.getContext(SLConsts.DEFAULT_GROUP_CONTEXT);
		final SLNode groupNode = groupContext.getRootNode().getNode(
				data.group.getUniqueName());
		Assert.assertThat(groupNode, Is.is(IsNull.notNullValue()));
		final SLNode exampleServerNode = groupNode.getNode("server name");
		Assert.assertThat(exampleServerNode, Is.is(IsNull.notNullValue()));
		Assert.assertThat(exampleServerNode, Is.is(Server.class));
		final SLNode exampleDatabaseNode = exampleServerNode.getNode("db");
		Assert.assertThat(exampleDatabaseNode, Is.is(IsNull.notNullValue()));
		Assert.assertThat(exampleDatabaseNode, Is.is(Database.class));
		final SLNode exampleSchemaNode = exampleDatabaseNode.getNode("PUBLIC");
		Assert.assertThat(exampleSchemaNode, Is.is(IsNull.notNullValue()));
		Assert.assertThat(exampleSchemaNode, Is.is(Schema.class));
		final SLNode exampleCatalogNode = exampleSchemaNode.getNode("DB");
		Assert.assertThat(exampleCatalogNode, Is.is(IsNull.notNullValue()));
		Assert.assertThat(exampleCatalogNode, Is.is(Catalog.class));
		final SLNode exampleTableNode = exampleCatalogNode
				.getNode("EXAMPLETABLE" + tableSufix);
		Assert.assertThat(exampleTableNode, Is.is(IsNull.notNullValue()));
		Assert.assertThat(exampleTableNode, Is.is(TableViewTable.class));
		final SLNode exampleViewNode = exampleCatalogNode.getNode("EXAMPLEVIEW"
				+ tableSufix);
		Assert.assertThat(exampleViewNode, Is.is(IsNull.notNullValue()));
		Assert.assertThat(exampleViewNode, Is.is(TableViewView.class));
		final SLNode anotherTableNode = exampleCatalogNode
				.getNode("ANOTHERTABLE" + tableSufix);

		final Column exampleColumn = exampleTableNode
				.getNode(Column.class, "I");
		final Column anotherExampleColumn = anotherTableNode.getNode(
				Column.class, "I_FK");
		final Set<SLNode> pkNodes = exampleColumn.getNodes();
		final SLNode fk = anotherExampleColumn.getNode("EXAMPLE_FK"
				+ tableSufix);

		boolean foundPkConstraint = false;
		logger.info("     >>> column with fk: "
				+ anotherExampleColumn.getName()
				+ " "
				+ anotherExampleColumn.getID()
				+ " "
				+ Arrays.toString(anotherExampleColumn.getClass()
						.getInterfaces()));

		Assert.assertThat(exampleColumn.getLockObject() == anotherExampleColumn
				.getLockObject(), Is.is(true));

		synchronized (exampleColumn.getLockObject()) {
			for (final SLNode node : pkNodes) {
				if (node instanceof DatabaseConstraintPrimaryKey) {
					foundPkConstraint = true;
				}
			}
		}

		Assert.assertThat(foundPkConstraint, Is.is(true));
		Assert.assertThat(fk, Is.is(IsNull.notNullValue()));

	}

	@Test
	public void shouldIncludeNewColumnOnChangedTable() throws Exception {

		final Connection connection1 = DatabaseSupport
				.createConnection(data.artifactSource);

		connection1
				.prepareStatement(
						"create table exampleTable2(i int not null primary key, last_i_plus_2 int, s smallint, f float, dp double precision, v varchar(10) not null)")
				.execute();
		connection1.close();

		reloadArtifactsAndCallBundleProcessor();

		final ExecutionContext executionContext1 = contextFactory
				.createExecutionContext("username", "password",
						DefaultJcrDescriptor.TEMP_DESCRIPTOR, data.repository
								.getName());
		final SLContext groupContext1 = executionContext1.getGraphSession()
				.getContext(SLConsts.DEFAULT_GROUP_CONTEXT);
		final SLNode groupNode1 = groupContext1.getRootNode().getNode(
				data.group.getUniqueName());
		final SLNode exampleServerNode1 = groupNode1.getNode("server name");
		final SLNode exampleDatabaseNode1 = exampleServerNode1.getNode("db");
		final SLNode exampleSchemaNode1 = exampleDatabaseNode1
				.getNode("PUBLIC");
		final SLNode exampleCatalogNode1 = exampleSchemaNode1.getNode("DB");
		final SLNode exampleTableNode1 = exampleCatalogNode1
				.getNode("EXAMPLETABLE2");
		final Column exampleColumn1 = exampleTableNode1.getNode(Column.class,
				"I");
		Assert.assertThat(exampleColumn1, Is.is(IsNull.notNullValue()));
		final Column invalidColumn1 = exampleTableNode1.getNode(Column.class,
				"INVALID");
		Assert.assertThat(invalidColumn1, Is.is(IsNull.nullValue()));
		final Connection connection2 = DatabaseSupport
				.createConnection(data.artifactSource);

		connection2.prepareStatement(
				"alter table exampleTable2 add column invalid int").execute();
		connection2.close();
		reloadArtifactsAndCallBundleProcessor();

		final ExecutionContext executionContext2 = contextFactory
				.createExecutionContext("username", "password",
						DefaultJcrDescriptor.TEMP_DESCRIPTOR, data.repository
								.getName());
		final SLContext groupContext2 = executionContext2.getGraphSession()
				.getContext(SLConsts.DEFAULT_GROUP_CONTEXT);
		final SLNode groupNode2 = groupContext2.getRootNode().getNode(
				data.group.getUniqueName());
		final SLNode exampleServerNode2 = groupNode2.getNode("server name");
		final SLNode exampleDatabaseNode2 = exampleServerNode2.getNode("db");
		final SLNode exampleSchemaNode2 = exampleDatabaseNode2
				.getNode("PUBLIC");
		final SLNode exampleCatalogNode2 = exampleSchemaNode2.getNode("DB");
		final SLNode exampleTableNode2 = exampleCatalogNode2
				.getNode("EXAMPLETABLE2");
		final Column exampleColumn2 = exampleTableNode2.getNode(Column.class,
				"I");
		Assert.assertThat(exampleColumn2, Is.is(IsNull.notNullValue()));
		final Column invalidColumn2 = exampleTableNode2.getNode(Column.class,
				"INVALID");
		Assert.assertThat(invalidColumn2, Is.is(IsNull.notNullValue()));

	}

	@Test
	public void shouldMaintainInformationOnExtendedNode() throws Exception {
		final CountDownLatch latch = new CountDownLatch(1);

		final ExecutionContext context = contextFactory.createExecutionContext(
				"username", "password", DefaultJcrDescriptor.TEMP_DESCRIPTOR,
				data.repository.getName());
		final SLContext groupContext = context.getGraphSession().createContext(
				SLConsts.DEFAULT_GROUP_CONTEXT);
		final SLNode groupNode = groupContext.getRootNode().addNode(
				data.group.getUniqueName());

		new Thread(new Runnable() {

			public void run() {
				try {
					final TableView tableNode = groupNode.addNode(
							TableView.class, "table");
					final Column columnNode = tableNode.addNode(Column.class,
							"myColumn");
					columnNode
							.addNode(DatabaseConstraintForeignKey.class, "fk");
				} catch (final Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			}
		}).start();
		latch.await();
		final TableViewTable theSameTable = groupNode.addNode(
				TableViewTable.class, "table");
		final SLNode theSameColumn = theSameTable.getNode("myColumn");
		Assert.assertThat(theSameColumn, IsNull.notNullValue());
		final SLNode theSameFk = theSameColumn.getNode("fk");
		Assert.assertThat(theSameFk, IsNull.notNullValue());

	}

	@Test
	public void shouldRemoveDeletedColumns() throws Exception {

		final Connection connection1 = DatabaseSupport
				.createConnection(data.artifactSource);

		connection1
				.prepareStatement(
						"create table exampleTable3(i int not null primary key, last_i_plus_2 int, s smallint, f float, dp double precision, v varchar(10) not null)")
				.execute();
		connection1.close();

		reloadArtifactsAndCallBundleProcessor();

		final ExecutionContext executionContext1 = contextFactory
				.createExecutionContext("username", "password",
						DefaultJcrDescriptor.TEMP_DESCRIPTOR, data.repository
								.getName());
		final SLContext groupContext1 = executionContext1.getGraphSession()
				.getContext(SLConsts.DEFAULT_GROUP_CONTEXT);
		final SLNode groupNode1 = groupContext1.getRootNode().getNode(
				data.group.getUniqueName());
		final SLNode exampleServerNode1 = groupNode1.getNode("server name");
		final SLNode exampleDatabaseNode1 = exampleServerNode1.getNode("db");
		final SLNode exampleSchemaNode1 = exampleDatabaseNode1
				.getNode("PUBLIC");
		final SLNode exampleCatalogNode1 = exampleSchemaNode1.getNode("DB");
		final SLNode exampleTableNode1 = exampleCatalogNode1
				.getNode("EXAMPLETABLE3");
		final Column exampleColumn1 = exampleTableNode1.getNode(Column.class,
				"I");
		Assert.assertThat(exampleColumn1, Is.is(IsNull.notNullValue()));
		final Column invalidColumn1 = exampleTableNode1.getNode(Column.class,
				"LAST_I_PLUS_2");
		Assert.assertThat(invalidColumn1, Is.is(IsNull.notNullValue()));
		final Connection connection2 = DatabaseSupport
				.createConnection(data.artifactSource);

		connection2.prepareStatement(
				"alter table exampleTable3 drop column last_i_plus_2 ")
				.execute();
		connection2.close();
		reloadArtifactsAndCallBundleProcessor();

		final ExecutionContext executionContext2 = contextFactory
				.createExecutionContext("username", "password",
						DefaultJcrDescriptor.TEMP_DESCRIPTOR, data.repository
								.getName());
		final SLContext groupContext2 = executionContext2.getGraphSession()
				.getContext(SLConsts.DEFAULT_GROUP_CONTEXT);
		final SLNode groupNode2 = groupContext2.getRootNode().getNode(
				data.group.getUniqueName());
		final SLNode exampleServerNode2 = groupNode2.getNode("server name");
		final SLNode exampleDatabaseNode2 = exampleServerNode2.getNode("db");
		final SLNode exampleSchemaNode2 = exampleDatabaseNode2
				.getNode("PUBLIC");
		final SLNode exampleCatalogNode2 = exampleSchemaNode2.getNode("DB");
		final SLNode exampleTableNode2 = exampleCatalogNode2
				.getNode("EXAMPLETABLE3");
		final Column exampleColumn2 = exampleTableNode2.getNode(Column.class,
				"I");
		Assert.assertThat(exampleColumn2, Is.is(IsNull.notNullValue()));
		final Column invalidColumn2 = exampleTableNode2.getNode(Column.class,
				"LAST_I_PLUS_2");
		Assert.assertThat(invalidColumn2, Is.is(IsNull.nullValue()));
	}

	@Test
	public void shouldRemoveDeletedTables() throws Exception {

		final Connection connection1 = DatabaseSupport
				.createConnection(data.artifactSource);

		connection1
				.prepareStatement(
						"create table exampleTable4(i int not null primary key, last_i_plus_2 int, s smallint, f float, dp double precision, v varchar(10) not null)")
				.execute();
		connection1.close();

		reloadArtifactsAndCallBundleProcessor();

		final ExecutionContext executionContext1 = contextFactory
				.createExecutionContext("username", "password",
						DefaultJcrDescriptor.TEMP_DESCRIPTOR, data.repository
								.getName());
		final SLContext groupContext1 = executionContext1.getGraphSession()
				.getContext(SLConsts.DEFAULT_GROUP_CONTEXT);
		final SLNode groupNode1 = groupContext1.getRootNode().getNode(
				data.group.getUniqueName());
		final SLNode exampleServerNode1 = groupNode1.getNode("server name");
		final SLNode exampleDatabaseNode1 = exampleServerNode1.getNode("db");
		final SLNode exampleSchemaNode1 = exampleDatabaseNode1
				.getNode("PUBLIC");
		final SLNode exampleCatalogNode1 = exampleSchemaNode1.getNode("DB");
		final SLNode exampleTableNode1 = exampleCatalogNode1
				.getNode("EXAMPLETABLE4");
		Assert.assertThat(exampleTableNode1, Is.is(IsNull.notNullValue()));
		final Connection connection2 = DatabaseSupport
				.createConnection(data.artifactSource);

		connection2.prepareStatement("drop table exampleTable4").execute();
		connection2.close();
		reloadArtifactsAndCallBundleProcessor();

		final ExecutionContext executionContext2 = contextFactory
				.createExecutionContext("username", "password",
						DefaultJcrDescriptor.TEMP_DESCRIPTOR, data.repository
								.getName());
		final SLContext groupContext2 = executionContext2.getGraphSession()
				.getContext(SLConsts.DEFAULT_GROUP_CONTEXT);
		final SLNode groupNode2 = groupContext2.getRootNode().getNode(
				data.group.getUniqueName());
		final SLNode exampleServerNode2 = groupNode2.getNode("server name");
		final SLNode exampleDatabaseNode2 = exampleServerNode2.getNode("db");
		final SLNode exampleSchemaNode2 = exampleDatabaseNode2
				.getNode("PUBLIC");
		final SLNode exampleCatalogNode2 = exampleSchemaNode2.getNode("DB");
		final SLNode exampleTableNode2 = exampleCatalogNode2
				.getNode("EXAMPLETABLE4");
		Assert.assertThat(exampleTableNode2, Is.is(IsNull.nullValue()));
	}

	@Test
	public void shouldUpdateChangedDatatypesAndRemoveUnused() throws Exception {
		final Connection connection1 = DatabaseSupport
				.createConnection(data.artifactSource);

		connection1
				.prepareStatement(
						"create table exampleTable7(i int , last_i_plus_2 int, s smallint, f float, dp double precision, v varchar(10) not null)")
				.execute();
		connection1.close();

		reloadArtifactsAndCallBundleProcessor();

		final ExecutionContext executionContext1 = contextFactory
				.createExecutionContext("username", "password",
						DefaultJcrDescriptor.TEMP_DESCRIPTOR, data.repository
								.getName());
		final SLContext groupContext1 = executionContext1.getGraphSession()
				.getContext(SLConsts.DEFAULT_GROUP_CONTEXT);
		final SLNode groupNode1 = groupContext1.getRootNode().getNode(
				data.group.getUniqueName());
		final SLNode exampleServerNode1 = groupNode1.getNode("server name");
		final SLNode exampleDatabaseNode1 = exampleServerNode1.getNode("db");
		final SLNode exampleSchemaNode1 = exampleDatabaseNode1
				.getNode("PUBLIC");
		final SLNode exampleCatalogNode1 = exampleSchemaNode1.getNode("DB");
		final SLNode exampleTableNode1 = exampleCatalogNode1
				.getNode("EXAMPLETABLE7");

		final Column exampleColumn1 = exampleTableNode1.getNode(Column.class,
				"I");

		final Collection<SLLink> links1 = executionContext1.getGraphSession()
				.getUnidirectionalLinksBySource(exampleColumn1);
		String dataType1 = null;
		synchronized (exampleTableNode1.getLockObject()) {
			for (final SLLink link : links1) {
				if (link instanceof ColumnDataType) {
					dataType1 = link.getTarget().getName();
				}
			}
		}

		Assert.assertThat(dataType1, Is.is(IsNull.notNullValue()));

		final Connection connection2 = DatabaseSupport
				.createConnection(data.artifactSource);

		connection2.prepareStatement("drop table exampleTable7 ").execute();
		connection2
				.prepareStatement(
						"create table exampleTable7(i varchar(10) not null, last_i_plus_2 int, s smallint, f float, dp double precision, v varchar(10) not null)")
				.execute();
		connection2.close();

		reloadArtifactsAndCallBundleProcessor();

		final ExecutionContext executionContext2 = contextFactory
				.createExecutionContext("username", "password",
						DefaultJcrDescriptor.TEMP_DESCRIPTOR, data.repository
								.getName());
		final SLContext groupContext2 = executionContext2.getGraphSession()
				.getContext(SLConsts.DEFAULT_GROUP_CONTEXT);
		final SLNode groupNode2 = groupContext2.getRootNode().getNode(
				data.group.getUniqueName());
		final SLNode exampleServerNode2 = groupNode2.getNode("server name");
		final SLNode exampleDatabaseNode2 = exampleServerNode2.getNode("db");
		final SLNode exampleSchemaNode2 = exampleDatabaseNode2
				.getNode("PUBLIC");
		final SLNode exampleCatalogNode2 = exampleSchemaNode2.getNode("DB");
		final SLNode exampleTableNode2 = exampleCatalogNode2
				.getNode("EXAMPLETABLE7");

		final Column exampleColumn2 = exampleTableNode2.getNode(Column.class,
				"I");

		final Collection<SLLink> links2 = executionContext1.getGraphSession()
				.getUnidirectionalLinksBySource(exampleColumn2);
		String dataType2 = null;
		synchronized (exampleTableNode2.getLockObject()) {
			for (final SLLink link : links2) {
				if (link instanceof ColumnDataType) {
					dataType2 = link.getTarget().getName();
				}
			}
		}

		Assert.assertThat(dataType2, Is.is(IsNull.notNullValue()));
		Assert.assertThat(dataType2, Is.is(IsNot.not(dataType1)));

	}

	@Test
	public void shouldUpdateChangedFkInformation() throws Exception {

		final Connection connection1 = DatabaseSupport
				.createConnection(data.artifactSource);
		final Random r = new Random();
		final String tableSufix = r.nextInt(50) + "_" + r.nextInt(50) + "_"
				+ r.nextInt(50);
		connection1
				.prepareStatement(
						"create table exampleTable"
								+ tableSufix
								+ "(i int not null primary key, last_i_plus_2 int, s smallint, f float, dp double precision, v varchar(10) not null)")
				.execute();
		connection1.prepareStatement(
				"create table anotherTable" + tableSufix
						+ "(i int not null primary key, i_fk int)").execute();

		connection1.prepareStatement(
				"alter table anotherTable" + tableSufix
						+ " add constraint example_fk" + tableSufix
						+ " foreign key(i_fk) references exampleTable"
						+ tableSufix + "(i)").execute();
		connection1.close();

		reloadArtifactsAndCallBundleProcessor();

		final ExecutionContext executionContext1 = contextFactory
				.createExecutionContext("username", "password",
						DefaultJcrDescriptor.TEMP_DESCRIPTOR, data.repository
								.getName());
		final SLContext groupContext1 = executionContext1.getGraphSession()
				.getContext(SLConsts.DEFAULT_GROUP_CONTEXT);
		final SLNode groupNode1 = groupContext1.getRootNode().getNode(
				data.group.getUniqueName());
		final SLNode exampleServerNode1 = groupNode1.getNode("server name");
		final SLNode exampleDatabaseNode1 = exampleServerNode1.getNode("db");
		final SLNode exampleSchemaNode1 = exampleDatabaseNode1
				.getNode("PUBLIC");
		final SLNode exampleCatalogNode1 = exampleSchemaNode1.getNode("DB");
		final SLNode exampleTableNode1 = exampleCatalogNode1
				.getNode("EXAMPLETABLE" + tableSufix);
		final SLNode anotherTableNode1 = exampleCatalogNode1
				.getNode("ANOTHERTABLE" + tableSufix);

		final Column exampleColumn1 = exampleTableNode1.getNode(Column.class,
				"I");
		final Column anotherExampleColumn = anotherTableNode1.getNode(
				Column.class, "I_FK");
		final Set<SLNode> pkNodes1 = exampleColumn1.getNodes();
		final SLNode fkNode = anotherExampleColumn.getNode("EXAMPLE_FK"
				+ tableSufix);

		boolean foundPkConstraint1 = false;
		Assert.assertThat(
				exampleColumn1.getLockObject() == anotherExampleColumn
						.getLockObject(), Is.is(true));

		synchronized (exampleColumn1.getLockObject()) {
			for (final SLNode node : pkNodes1) {
				if (node instanceof DatabaseConstraintPrimaryKey) {
					foundPkConstraint1 = true;
				}
			}
		}

		final Connection connection2 = DatabaseSupport
				.createConnection(data.artifactSource);

		connection2.prepareStatement(
				"alter table anotherTable" + tableSufix
						+ " drop constraint example_fk" + tableSufix).execute();
		connection2.close();

		reloadArtifactsAndCallBundleProcessor();

		final ExecutionContext executionContext2 = contextFactory
				.createExecutionContext("username", "password",
						DefaultJcrDescriptor.TEMP_DESCRIPTOR, data.repository
								.getName());
		final SLContext groupContext2 = executionContext2.getGraphSession()
				.getContext(SLConsts.DEFAULT_GROUP_CONTEXT);
		final SLNode groupNode2 = groupContext2.getRootNode().getNode(
				data.group.getUniqueName());
		final SLNode exampleServerNode2 = groupNode2.getNode("server name");
		final SLNode exampleDatabaseNode2 = exampleServerNode2.getNode("db");
		final SLNode exampleSchemaNode2 = exampleDatabaseNode2
				.getNode("PUBLIC");
		final SLNode exampleCatalogNode2 = exampleSchemaNode2.getNode("DB");
		final SLNode exampleTableNode2 = exampleCatalogNode2
				.getNode("EXAMPLETABLE" + tableSufix);
		final SLNode anotherTableNode2 = exampleCatalogNode2
				.getNode("ANOTHERTABLE" + tableSufix);

		final Column exampleColumn2 = exampleTableNode2.getNode(Column.class,
				"I");
		final Column anotherExampleColumn2 = anotherTableNode2.getNode(
				Column.class, "I_FK");
		final Set<SLNode> pkNodes2 = exampleColumn2.getNodes();
		final Set<SLNode> fkNodes2 = anotherExampleColumn2.getNodes();

		boolean foundPkConstraint2 = false;
		boolean foundFkConstraint2 = false;

		synchronized (exampleColumn2.getLockObject()) {
			for (final SLNode node : pkNodes2) {
				if (node instanceof DatabaseConstraintPrimaryKey) {
					foundPkConstraint2 = true;
				}
			}
			for (final SLNode node : fkNodes2) {
				if (node instanceof DatabaseConstraintForeignKey) {
					foundFkConstraint2 = true;
				}
			}
		}

		Assert.assertThat(foundPkConstraint1, Is.is(true));
		Assert.assertThat(fkNode, Is.is(IsNull.notNullValue()));

		Assert.assertThat(foundFkConstraint2, Is.is(false));
		Assert.assertThat(foundPkConstraint2, Is.is(true));

	}

	@Test
	public void shouldUpdateChangedPkInformation() throws Exception {

		final Connection connection1 = DatabaseSupport
				.createConnection(data.artifactSource);

		connection1
				.prepareStatement(
						"create table exampleTable6(i int not null primary key, last_i_plus_2 int, s smallint, f float, dp double precision, v varchar(10) not null)")
				.execute();
		connection1.close();

		reloadArtifactsAndCallBundleProcessor();

		final ExecutionContext executionContext1 = contextFactory
				.createExecutionContext("username", "password",
						DefaultJcrDescriptor.TEMP_DESCRIPTOR, data.repository
								.getName());
		final SLContext groupContext1 = executionContext1.getGraphSession()
				.getContext(SLConsts.DEFAULT_GROUP_CONTEXT);
		final SLNode groupNode1 = groupContext1.getRootNode().getNode(
				data.group.getUniqueName());
		final SLNode exampleServerNode1 = groupNode1.getNode("server name");
		final SLNode exampleDatabaseNode1 = exampleServerNode1.getNode("db");
		final SLNode exampleSchemaNode1 = exampleDatabaseNode1
				.getNode("PUBLIC");
		final SLNode exampleCatalogNode1 = exampleSchemaNode1.getNode("DB");
		final SLNode exampleTableNode1 = exampleCatalogNode1
				.getNode("EXAMPLETABLE6");

		final Column exampleColumn1 = exampleTableNode1.getNode(Column.class,
				"I");
		final Set<SLNode> pkNodes1 = exampleColumn1.getNodes();

		boolean foundPkConstraint1 = false;

		synchronized (exampleTableNode1.getLockObject()) {
			for (final SLNode node : pkNodes1) {
				if (node instanceof DatabaseConstraintPrimaryKey) {
					foundPkConstraint1 = true;
				}
			}
		}

		Assert.assertThat(foundPkConstraint1, Is.is(true));

		final Connection connection2 = DatabaseSupport
				.createConnection(data.artifactSource);

		connection2.prepareStatement("drop table exampleTable6 ").execute();
		connection2
				.prepareStatement(
						"create table exampleTable6(i int not null, last_i_plus_2 int, s smallint, f float, dp double precision, v varchar(10) not null)")
				.execute();
		connection2.close();

		reloadArtifactsAndCallBundleProcessor();

		final ExecutionContext executionContext2 = contextFactory
				.createExecutionContext("username", "password",
						DefaultJcrDescriptor.TEMP_DESCRIPTOR, data.repository
								.getName());
		final SLContext groupContext2 = executionContext2.getGraphSession()
				.getContext(SLConsts.DEFAULT_GROUP_CONTEXT);
		final SLNode groupNode2 = groupContext2.getRootNode().getNode(
				data.group.getUniqueName());
		final SLNode exampleServerNode2 = groupNode2.getNode("server name");
		final SLNode exampleDatabaseNode2 = exampleServerNode2.getNode("db");
		final SLNode exampleSchemaNode2 = exampleDatabaseNode2
				.getNode("PUBLIC");
		final SLNode exampleCatalogNode2 = exampleSchemaNode2.getNode("DB");
		final SLNode exampleTableNode2 = exampleCatalogNode2
				.getNode("EXAMPLETABLE6");

		final Column exampleColumn2 = exampleTableNode2.getNode(Column.class,
				"I");
		final Set<SLNode> pkNodes2 = exampleColumn2.getNodes();

		boolean foundPkConstraint2 = false;

		synchronized (exampleColumn2.getLockObject()) {
			for (final SLNode node : pkNodes2) {
				if (node instanceof DatabaseConstraintPrimaryKey) {
					foundPkConstraint2 = true;
				}
			}
		}

		Assert.assertThat(foundPkConstraint2, Is.is(false));
	}

}
