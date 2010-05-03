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
package org.openspotlight.federation.loader;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.hamcrest.core.Is;
import org.jredis.JRedis;
import org.junit.*;
import org.openspotlight.federation.context.DefaultExecutionContextFactoryModule;
import org.openspotlight.federation.domain.Group;
import org.openspotlight.federation.domain.Repository;
import org.openspotlight.federation.log.DetailedLoggerModule;
import org.openspotlight.federation.util.GroupDifferences;
import org.openspotlight.federation.util.GroupSupport;
import org.openspotlight.graph.guice.SLGraphModule;
import org.openspotlight.jcr.provider.DefaultJcrDescriptor;
import org.openspotlight.jcr.provider.JcrConnectionProvider;
import org.openspotlight.persist.guice.SimplePersistModule;
import org.openspotlight.persist.support.SimplePersistCapable;
import org.openspotlight.persist.support.SimplePersistFactory;
import org.openspotlight.storage.STStorageSession;
import org.openspotlight.storage.domain.SLPartition;
import org.openspotlight.storage.domain.node.STNodeEntry;
import org.openspotlight.storage.redis.guice.JRedisFactory;
import org.openspotlight.storage.redis.guice.JRedisStorageModule;
import org.openspotlight.storage.redis.util.ExampleRedisConfig;

import javax.jcr.Session;
import java.util.Set;

import static org.openspotlight.storage.STRepositoryPath.repositoryPath;

/**
 * The Class JcrSessionConfigurationManagerTest.
 */
public class JcrSessionConfigurationManagerTest extends AbstractConfigurationManagerTest {

    private static JcrConnectionProvider provider;

    private static SimplePersistCapable<STNodeEntry, STStorageSession> simplePersist;

    private static JRedis jredis;

    @BeforeClass
    public static void setupJcrRepo() throws Exception {
        provider = JcrConnectionProvider.createFromData(DefaultJcrDescriptor.TEMP_DESCRIPTOR);
        Injector injector = Guice.createInjector(new JRedisStorageModule(STStorageSession.STFlushMode.AUTO,
                ExampleRedisConfig.EXAMPLE.getMappedServerConfig(),
                repositoryPath("repository")),
                new SimplePersistModule(), new DetailedLoggerModule(),
                new DefaultExecutionContextFactoryModule(),
                new SLGraphModule(DefaultJcrDescriptor.TEMP_DESCRIPTOR));
        simplePersist = injector.getInstance(SimplePersistFactory.class).createSimplePersist(SLPartition.FEDERATION);
        jredis = injector.getInstance(JRedisFactory.class).getFrom(SLPartition.GRAPH);
    }

    private Session session;

    @Before
    public void clean() throws Exception {
        jredis.flushall();
    }

    @After
    public void closeSession() throws Exception {
        if (session != null && session.isLive()) {
            session.logout();
            session = null;
        }

    }

    @Override
    protected ConfigurationManager createNewConfigurationManager() {
        return ConfigurationManagerFactoryImpl.createMutableUsingSession(simplePersist);
    }

    @Before
    public void setupSession() throws Exception {
        session = provider.openSession();
    }

    @Test
    public void shouldFindGroupDeltas() throws Exception {

        final Repository repository = new Repository();
        repository.setName("newRepository");
        final Group group = new Group();
        group.setName("willBeRemoved");
        group.setRepository(repository);
        repository.getGroups().add(group);
        final ConfigurationManager manager1 = createNewConfigurationManager();
        manager1.saveRepository(repository);
        final Group group2 = new Group();
        group2.setName("new");
        group2.setRepository(repository);
        repository.getGroups().add(group2);
        repository.getGroups().remove(group);
        manager1.saveRepository(repository);

        final GroupDifferences differences = GroupSupport.getDifferences(simplePersist, repository.getName());
        final Set<String> added = differences.getAddedGroups();

        Assert.assertThat(added.contains("newRepository/new"), Is.is(true));

    }

}
