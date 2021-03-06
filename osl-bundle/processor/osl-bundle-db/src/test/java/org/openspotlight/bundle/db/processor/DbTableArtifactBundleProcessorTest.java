/**
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

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.openspotlight.bundle.db.DBConstants;
import org.openspotlight.bundle.db.metamodel.link.ColumnDataType;
import org.openspotlight.bundle.db.metamodel.node.Column;
import org.openspotlight.bundle.db.metamodel.node.DatabaseConstraintForeignKey;
import org.openspotlight.bundle.db.metamodel.node.DatabaseConstraintPrimaryKey;
import org.openspotlight.common.util.SLCollections;
import org.openspotlight.bundle.context.DefaultExecutionContextFactoryModule;
import org.openspotlight.bundle.context.ExecutionContext;
import org.openspotlight.bundle.context.ExecutionContextFactory;
import org.openspotlight.federation.domain.artifact.db.DatabaseType;
import org.openspotlight.federation.finder.DatabaseCustomArtifactFinder;
import org.openspotlight.federation.finder.db.DatabaseSupport;
import org.openspotlight.federation.log.DetailedLoggerModule;
import org.openspotlight.bundle.scheduler.DefaultScheduler;
import org.openspotlight.bundle.scheduler.GlobalSettingsSupport;
import org.openspotlight.graph.SLContext;
import org.openspotlight.graph.SLLink;
import org.openspotlight.graph.Nodeimport org.openspotlight.graph.guice.SLGraphModule;
import org.openspotlight.jcr.provider.DefaultJcrDescriptor;
import org.openspotlight.jcr.provider.JcrConnectionProvider;
import org.openspotlight.persist.guice.SimplePersistModule;
import org.openspotlight.storage.StorageSessionimport org.openspotlight.storage.domain.RegularPartitionition;
import org.openspotlight.storage.redis.guice.JRedisFactory;
import org.openspotlight.storage.redis.guice.JRedisStorageModule;
import org.openspotlight.storage.redis.util.ExampleRedisConfig;

import java.sql.Connection;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

import static org.openspotlight.common.util.Files.delete;
import static org.openspotlight.storage.STRepositoryPath.repositoryPath;

public class DbTableArtifactBundleProcessorTest {

    private static class RepositoryData {
        public final GlobalSettings settings;
        public final Repository repository;
        public final Group group;
        public final DbArtifactSource artifactSource;

        public RepositoryData(
                final GlobalSettings settings, final Repository repository, final Group group,
                final DbArtifactSource artifactSource) {
            this.settings = settings;
            this.repository = repository;
            this.group = group;
            this.artifactSource = artifactSource;
        }
    }

    private static ExecutionContextFactory contextFactory;
    private static RepositoryData data;
    private static DefaultScheduler scheduler;

    @AfterClass
    public static void closeResources() throws Exception {
        contextFactory.closeResources();
    }

    private static RepositoryData createRepositoryData() {
        final GlobalSettings settings = new GlobalSettings();
        settings.setDefaultSleepingIntervalInMilliseconds(1000);

        GlobalSettingsSupport.initializeScheduleMap(settings);
        settings.setParallelThreads(1);
        settings.getLoaderRegistry().add(DatabaseCustomArtifactFinder.class);
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
        artifactSource.setInitialLookup("jdbc:h2:./target/test-data/DbTableArtifactBundleProcessorTest/h2/db;DB_CLOSE_ON_EXIT=FALSE");
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
        commonProcessor.setGlobalPhase(DbArtifactGlobalProcessor.class);
        commonProcessor.getArtifactPhases().add(DbTableArtifactProcessor.class);
        commonProcessor.getArtifactPhases().add(DbPrimaryKeyProcessor.class);
        commonProcessor.getArtifactPhases().add(DbForeignKeyProcessor.class);
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

        JcrConnectionProvider.createFromData(DefaultJcrDescriptor.TEMP_DESCRIPTOR).closeRepositoryAndCleanResources();

        data = createRepositoryData();

        Injector injector = Guice.createInjector(new JRedisStorageModule(StStorageSessionlushMode.AUTO,
                ExampleRedisConfig.EXAMPLE.getMappedServerConfig(),
                repositoryPath("repository")),
                new SimplePersistModule(), new DetailedLoggerModule(),
                new DefaultExecutionContextFactoryModule(), new SLGraphModule(DefaultJcrDescriptor.TEMP_DESCRIPTOR));

        injector.getInstance(JRedisFactory.class).getRegularPartitionrPartition.GRAPH).flushall();

        contextFactory = injector.getInstance(ExecutionContextFactory.class);

        final ExecutionContext context = contextFactory.createExecutionContext("username", "password",
                DefaultJcrDescriptor.TEMP_DESCRIPTOR,
                data.repository);

        context.getDefaultConfigurationManager().saveGlobalSettings(data.settings);
        context.getDefaultConfigurationManager().saveRepository(data.repository);
        context.closeResources();

        scheduler = DefaultScheduler.INSTANCE;
        scheduler.initializeSettings(contextFactory, "user", "password", DefaultJcrDescriptor.TEMP_DESCRIPTOR);
        scheduler.refreshJobs(data.settings, SLCollections.setOf(data.repository));
        scheduler.startScheduler();

    }

    @After
    public void closeTestResources() {
        contextFactory.closeResources();
    }

    private void reloadArtifactsAndCallBundleProcessor() {
        scheduler.fireSchedulable("username", "password", data.artifactSource);
        scheduler.fireSchedulable("username", "password", data.group);
    }

    @Test
    public void shouldIncludeNewColumnOnChangedTable() throws Exception {

        final Connection connection1 = DatabaseSupport.createConnection(data.artifactSource);

        connection1.prepareStatement(
                "newPair table exampleTable2(i int not null , last_i_plus_2 int, s smallint, f float, dp double precision, v varchar(10) not null)").execute();
        connection1.close();

        reloadArtifactsAndCallBundleProcessor();

        final ExecutionContext executionContext1 = contextFactory.createExecutionContext("username", "password",
                DefaultJcrDescriptor.TEMP_DESCRIPTOR,
                data.repository);
        
        final SLContext groupContext1 = executionContext1.getGraphSession().getContext(DBConstants.DB_ABSTRACT_CONTEXT);
        final NoNodempleServerNode1 = groupContext1.getRootNode().getNode("server name");
        final NodeNodeleDatabaseNode1 = exampleServerNode1.getNode("db");
        final Node eNodeSchemaNode1 = exampleDatabaseNode1.getNode("PUBLIC");
        final Node exaNodetalogNode1 = exampleSchemaNode1.getNode("DB");
        final Node exampNodeeNode1 = exampleCatalogNode1.getNode("EXAMPLETABLE2");
        final Column exampleColumn1 = exampleTableNode1.getChildNode(Column.class, "I");
        Assert.assertThat(exampleColumn1, Is.is(IsNull.notNullValue()));
        final Column invalidColumn1 = exampleTableNode1.getChildNode(Column.class, "INVALID");
        Assert.assertThat(invalidColumn1, Is.is(IsNull.nullValue()));
        final Connection connection2 = DatabaseSupport.createConnection(data.artifactSource);

        connection2.prepareStatement("alter table exampleTable2 add column invalid int").execute();
        connection2.close();
        reloadArtifactsAndCallBundleProcessor();

        final ExecutionContext executionContext2 = contextFactory.createExecutionContext("username", "password",
                DefaultJcrDescriptor.TEMP_DESCRIPTOR,
                data.repository);
        final SLContext groupContext2 = executionContext2.getGraphSession().getContext(DBConstants.DB_ABSTRACT_CONTEXT);
        final Node exampleNodeNode2 = groupContext2.getRootNode().getNode("server name");
        final Node exampleDaNodeNode2 = exampleServerNode2.getNode("db");
        final Node exampleScheNode2 = exampleDatabaseNode2.getNode("PUBLIC");
        final Node exampleCataloNode = exampleSchemaNode2.getNode("DB");
        final Node exampleTableNodNodexampleCatalogNode2.getNode("EXAMPLETABLE2");
        final Column exampleColumn2 = exampleTableNode2.getChildNode(Column.class, "I");
        Assert.assertThat(exampleColumn2, Is.is(IsNull.notNullValue()));
        final Column invalidColumn2 = exampleTableNode2.getChildNode(Column.class, "INVALID");
        Assert.assertThat(invalidColumn2, Is.is(IsNull.notNullValue()));

    }

    @Test
    public void shouldRemoveDeletedColumns() throws Exception {

        final Connection connection1 = DatabaseSupport.createConnection(data.artifactSource);

        connection1.prepareStatement(
                "newPair table exampleTable3(i int not null , last_i_plus_2 int, s smallint, f float, dp double precision, v varchar(10) not null)").execute();
        connection1.close();

        reloadArtifactsAndCallBundleProcessor();

        final ExecutionContext executionContext1 = contextFactory.createExecutionContext("username", "password",
                DefaultJcrDescriptor.TEMP_DESCRIPTOR,
                data.repository);
        final SLContext groupContext1 = executionContext1.getGraphSession().getContext(DBConstants.DB_ABSTRACT_CONTEXT);
        final Node groupNode1 = grouNodext1.getRootNode();
        final Node exampleServerNode1 NodepNode1.getNode("server name");
        final Node exampleDatabaseNode1 NodepleServerNode1.getNode("db");
        final Node exampleSchemaNode1 = exNodeatabaseNode1.getNode("PUBLIC");
        final Node exampleCatalogNode1 = exaNodehemaNode1.getNode("DB");
        final Node exampleTableNode1 = exampleNodegNode1.getNode("EXAMPLETABLE3");
        final Column exampleColumn1 = exampleTableNode1.getChildNode(Column.class, "I");
        Assert.assertThat(exampleColumn1, Is.is(IsNull.notNullValue()));
        final Column invalidColumn1 = exampleTableNode1.getChildNode(Column.class, "LAST_I_PLUS_2");
        Assert.assertThat(invalidColumn1, Is.is(IsNull.notNullValue()));
        final Connection connection2 = DatabaseSupport.createConnection(data.artifactSource);

        connection2.prepareStatement("alter table exampleTable3 drop column last_i_plus_2 ").execute();
        connection2.close();
        reloadArtifactsAndCallBundleProcessor();

        final ExecutionContext executionContext2 = contextFactory.createExecutionContext("username", "password",
                DefaultJcrDescriptor.TEMP_DESCRIPTOR,
                data.repository);
        final SLContext groupContext2 = executionContext2.getGraphSession().getContext(DBConstants.DB_ABSTRACT_CONTEXT);
        final Node groupNode2 = groupContext2.geNodeode();
        final Node exampleServerNode2 = groupNode2Nodede("server name");
        final Node exampleDatabaseNode2 = exampleSerNodee2.getNode("db");
        final Node exampleSchemaNode2 = exampleDatabasNode.getNode("PUBLIC");
        final Node exampleCatalogNode2 = exampleSchemaNoNodetNode("DB");
        final Node exampleTableNode2 = exampleCatalogNode2Nodede("EXAMPLETABLE3");
        final Column exampleColumn2 = exampleTableNode2.getChildNode(Column.class, "I");
        Assert.assertThat(exampleColumn2, Is.is(IsNull.notNullValue()));
        final Column invalidColumn2 = exampleTableNode2.getChildNode(Column.class, "LAST_I_PLUS_2");
        Assert.assertThat(invalidColumn2, Is.is(IsNull.nullValue()));
    }

    @Test
    public void shouldRemoveDeletedTables() throws Exception {

        final Connection connection1 = DatabaseSupport.createConnection(data.artifactSource);

        connection1.prepareStatement(
                "newPair table exampleTable4(i int not null , last_i_plus_2 int, s smallint, f float, dp double precision, v varchar(10) not null)").execute();
        connection1.close();

        reloadArtifactsAndCallBundleProcessor();

        final ExecutionContext executionContext1 = contextFactory.createExecutionContext("username", "password",
                DefaultJcrDescriptor.TEMP_DESCRIPTOR,
                data.repository);
        final SLContext groupContext1 = executionContext1.getGraphSession().getContext(DBConstants.DB_ABSTRACT_CONTEXT);
        final Node groupNode1 = groupContext1.getRootNode();Node   final Node exampleServerNode1 = groupNode1.getNode("seNodeame");
        final Node exampleDatabaseNode1 = exampleServerNode1.getNodedb");
        final Node exampleSchemaNode1 = exampleDatabaseNode1.getNoNodeBLIC");
        final Node exampleCatalogNode1 = exampleSchemaNode1.getNode(Node
        final Node exampleTableNode1 = exampleCatalogNode1.getNode("EXNodeABLE4");
        Assert.assertThat(exampleTableNode1, Is.is(IsNull.notNullValue()));
        final Connection connection2 = DatabaseSupport.createConnection(data.artifactSource);

        connection2.prepareStatement("drop table exampleTable4").execute();
        connection2.close();
        reloadArtifactsAndCallBundleProcessor();

        final ExecutionContext executionContext2 = contextFactory.createExecutionContext("username", "password",
                DefaultJcrDescriptor.TEMP_DESCRIPTOR,
                data.repository);
        final SLContext groupContext2 = executionContext2.getGraphSession().getContext(DBConstants.DB_ABSTRACT_CONTEXT);
        final Node groupNode2 = groupContext2.getRootNode();
        finNodee exampleServerNode2 = groupNode2.getNode("server name");Node   final Node exampleDatabaseNode2 = exampleServerNode2.getNode("db");
Node  final Node exampleSchemaNode2 = exampleDatabaseNode2.getNode("PUBLIC")Node    final Node exampleCatalogNode2 = exampleSchemaNode2.getNode("DB");
     Nodeal Node exampleTableNode2 = exampleCatalogNode2.getNode("EXAMPLETABLE4"Node     Assert.assertThat(exampleTableNode2, Is.is(IsNull.nullValue()));
    }

    @Test
    public void shouldUpdateChangedDatatypesAndRemoveUnused() throws Exception {
        final Connection connection1 = DatabaseSupport.createConnection(data.artifactSource);

        connection1.prepareStatement(
                "newPair table exampleTable7(i int , last_i_plus_2 int, s smallint, f float, dp double precision, v varchar(10) not null)").execute();
        connection1.close();

        reloadArtifactsAndCallBundleProcessor();

        final ExecutionContext executionContext1 = contextFactory.createExecutionContext("username", "password",
                DefaultJcrDescriptor.TEMP_DESCRIPTOR,
                data.repository);
        final SLContext groupContext1 = executionContext1.getGraphSession().getContext(DBConstants.DB_ABSTRACT_CONTEXT);
        final Node groupNode1 = groupContext1.getRootNode();
        final Node examNodeverNode1 = groupNode1.getNode("server name");
        finNodee exampleDatabaseNode1 = exampleServerNode1.getNode("db");
        finaNode exampleSchemaNode1 = exampleDatabaseNode1.getNode("PUBLIC");
        fiNodede exampleCatalogNode1 = exampleSchemaNode1.getNode("DB");
        final NodNodepleTableNode1 = exampleCatalogNode1.getNode("EXAMPLETABLE7");

        NodeColumn exampleColumn1 = exampleTableNode1.getChildNode(Column.class, "I");

        final Collection<SLLink> links1 = executionContext1.getGraphSession().getUnidirectionalLinksBySource(exampleColumn1);
        String dataType1 = null;
        synchronized (exampleTableNode1.getLockObject()) {
            for (final SLLink link : links1) {
                if (link instanceof ColumnDataType) {
                    dataType1 = link.getTarget().getName();
                }
            }
        }

        Assert.assertThat(dataType1, Is.is(IsNull.notNullValue()));

        final Connection connection2 = DatabaseSupport.createConnection(data.artifactSource);

        connection2.prepareStatement("drop table exampleTable7 ").execute();
        connection2.prepareStatement(
                "newPair table exampleTable7(i varchar(10) not null, last_i_plus_2 int, s smallint, f float, dp double precision, v varchar(10) not null)").execute();
        connection2.close();

        reloadArtifactsAndCallBundleProcessor();

        final ExecutionContext executionContext2 = contextFactory.createExecutionContext("username", "password",
                DefaultJcrDescriptor.TEMP_DESCRIPTOR,
                data.repository);
        final SLContext groupContext2 = executionContext2.getGraphSession().getContext(DBConstants.DB_ABSTRACT_CONTEXT);
        final Node groupNode2 = groupContext2.getRootNode();
        final Node exampleServerNodNoderoupNode2.getNode("server name");
        final Node examNodeabaseNode2 = exampleServerNode2.getNode("db");
        final Node exampNodemaNode2 = exampleDatabaseNode2.getNode("PUBLIC");
        final Node exaNodetalogNode2 = exampleSchemaNode2.getNode("DB");
        final Node exampleTabNode2 = exampleCatalogNode2.getNode("EXAMPLETABLE7");

        final ColumnNodeleColumn2 = exampleTableNode2.getChildNode(Column.class, "I");

        final Collection<SLLink> links2 = executionContext1.getGraphSession().getUnidirectionalLinksBySource(exampleColumn2);
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

        final Connection connection1 = DatabaseSupport.createConnection(data.artifactSource);
        final Random r = new Random();
        final String tableSufix = r.nextInt(50) + "_" + r.nextInt(50) + "_" + r.nextInt(50);
        connection1.prepareStatement(
                "newPair table exampleTable"
                        + tableSufix
                        + "(i int not null , last_i_plus_2 int, s smallint, f float, dp double precision, v varchar(10) not null)").execute();
        connection1.prepareStatement("newPair table anotherTable" + tableSufix + "(i int not null , i_fk int)").execute();

        connection1.prepareStatement(
                "alter table anotherTable" + tableSufix + " add constraint example_fk" + tableSufix
                        + " foreign key(i_fk) references exampleTable" + tableSufix + "(i)").execute();
        connection1.close();

        reloadArtifactsAndCallBundleProcessor();

        final ExecutionContext executionContext1 = contextFactory.createExecutionContext("username", "password",
                DefaultJcrDescriptor.TEMP_DESCRIPTOR,
                data.repository);
        final SLContext groupContext1 = executionContext1.getGraphSession().getContext(DBConstants.DB_ABSTRACT_CONTEXT);
        final Node groupNode1 = groupContext1.getRootNode();
        final Node exampleServerNode1 = groupNoNodetNode("server name");
        final Node exampleDatabaseNNode exampleServerNode1.getNode("db");
        boolean foundFkConstraint1 =Node;
        synchronized (exampleDatabaseNode1.getLockObject()) {

            final Set<Node> nodes = exampleDatabaseNode1.getNodes();
            for (final Node node : nodes) {
         Node if (node instanceof DatabaseConstraintForeignKey) {
             Node foundFkConstraint1 = true;
                }
            }
        }

        final Connection connection2 = DatabaseSupport.createConnection(data.artifactSource);

        connection2.prepareStatement("alter table anotherTable" + tableSufix + " drop constraint example_fk" + tableSufix).execute();
        connection2.close();

        reloadArtifactsAndCallBundleProcessor();

        final ExecutionContext executionContext2 = contextFactory.createExecutionContext("username", "password",
                DefaultJcrDescriptor.TEMP_DESCRIPTOR,
                data.repository);
        final SLContext groupContext2 = executionContext2.getGraphSession().getContext(DBConstants.DB_ABSTRACT_CONTEXT);
        final Node groupNode2 = groupContext2.getRootNode();
        final Node exampleServerNode2 = groupNode2.getNodNodever name");
        final Node exampleDatabaseNode2 = exaNoderverNode2.getNode("db");
        boolean foundFkConstraint2 = false;
  Nodesynchronized (exampleDatabaseNode2.getLockObject()) {

            final Set<Node> nodes = exampleDatabaseNode2.getNodes();
            for (final Node node : nodes) {
                if Nodeinstanceof DatabaseConstraintForeignKey) {
                    fouNodenstraint2 = true;
                }
            }
        }
        Assert.assertThat(foundFkConstraint1, Is.is(true));

        Assert.assertThat(foundFkConstraint2, Is.is(false));

    }

    @Test
    public void shouldUpdateChangedPkInformation() throws Exception {

        final Connection connection1 = DatabaseSupport.createConnection(data.artifactSource);

        connection1.prepareStatement(
                "newPair table exampleTable6(i int not null primary key, last_i_plus_2 int, s smallint, f float, dp double precision, v varchar(10) not null)").execute();
        connection1.close();

        reloadArtifactsAndCallBundleProcessor();

        final ExecutionContext executionContext1 = contextFactory.createExecutionContext("username", "password",
                DefaultJcrDescriptor.TEMP_DESCRIPTOR,
                data.repository);
        final SLContext groupContext1 = executionContext1.getGraphSession().getContext(DBConstants.DB_ABSTRACT_CONTEXT);
        final Node groupNode1 = groupContext1.getRootNode();
        final Node exampleServerNode1 = groupNode1.getNode("server Node;
        final Node exampleDatabaseNode1 = exampleServerNodegetNode("db");
        final Node exampleSchemaNode1 = exampleDatabaseNNodeetNode("PUBLIC");
        final Node exampleCatalogNode1 = exampleSchemaNodegetNode("DB");

        boolean foundPk = false;
        synchronized (exampNodelogNode1.getLockObject()) {

            final Set<Node> nodes = exampleCatalogNode1.getNodes();
            for (final Node node : nodes) {
                if (node instanceoNodebaseConstraintPrimaryKey) {
                    foundPk = true;
 Node             break;
                }
            }
        }
        Assert.assertThat(foundPk, Is.is(true));

        final Connection connection2 = DatabaseSupport.createConnection(data.artifactSource);

        connection2.prepareStatement("drop table exampleTable6 ").execute();
        connection2.prepareStatement(
                "newPair table exampleTable6(i int not null, last_i_plus_2 int, s smallint, f float, dp double precision, v varchar(10) not null)").execute();
        connection2.close();

        reloadArtifactsAndCallBundleProcessor();

        final ExecutionContext executionContext2 = contextFactory.createExecutionContext("username", "password",
                DefaultJcrDescriptor.TEMP_DESCRIPTOR,
                data.repository);
        final SLContext groupContext2 = executionContext2.getGraphSession().getContext(DBConstants.DB_ABSTRACT_CONTEXT);
        final Node groupNode2 = groupContext2.getRootNode();
        final Node exampleServerNode2 = groupNode2.getNode("server name");
      Nodel Node exampleDatabaseNode2 = exampleServerNode2.getNode(Node
        final Node exampleSchemaNode2 = exampleDatabaseNode2.getNode("Node");
        final Node exampleCatalogNode2 = exampleSchemaNode2.getNode(Node

        boolean foundPk2 = false;
        synchronized (exampleCatalogNodeNodeockObject()) {

            final Set<Node> nodes = exampleCatalogNode2.getNodes();
            for (final Node node : nodes) {
                if (node instanceof DatabaseConsNodePrimaryKey) {
                    foundPk2 = true;
              Nodebreak;
                }
            }
        }
        Assert.assertThat(foundPk2, Is.is(false));
    }

}
