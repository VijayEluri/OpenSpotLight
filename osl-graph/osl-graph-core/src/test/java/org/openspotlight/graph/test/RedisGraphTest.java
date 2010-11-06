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

package org.openspotlight.graph.test;

import java.util.Map;

import org.openspotlight.graph.GraphModule;
import org.openspotlight.storage.Partition;
import org.openspotlight.storage.StorageSession;
import org.openspotlight.storage.domain.RegularPartitions;
import org.openspotlight.storage.redis.guice.JRedisFactory;
import org.openspotlight.storage.redis.guice.JRedisServerDetail;
import org.openspotlight.storage.redis.guice.JRedisStorageModule;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class RedisGraphTest extends AbstractGraphTest {

    private enum JRedisServerConfigExample implements JRedisServerDetail {

        GRAPH("localhost", 6379, 0, true),
        FEDERATION("localhost", 6379, 1, false),
        SYNTAX_HIGHLIGHT("localhost", 6379, 2, false),
        LINE_REFERENCE("localhost", 6379, 3, false),
        SECURITY("localhost", 6379, 4, false),
        LOG("localhost", 6379, 5, false);

        private JRedisServerConfigExample(final String serverName, final int serverPort,
                                          final int db, final boolean defaultConfig) {
            this.serverName = serverName;
            this.serverPort = serverPort;
            this.db = db;
            this.defaultConfig = defaultConfig;
        }

        private final String  serverName;

        private final boolean defaultConfig;

        private final int     db;

        @Override
        public int getDb() {
            return db;
        }

        @Override
        public String getPassword() {
            return null;
        }

        private final int serverPort;

        @Override
        public String getServerName() {
            return serverName;
        }

        @Override
        public int getServerPort() {
            return serverPort;
        }

        @Override
        public boolean isDefaultConfig() {
            return defaultConfig;
        }
    }

    final Map<Partition, JRedisServerDetail> mappedServerConfig = ImmutableMap
                                                                    .<Partition, JRedisServerDetail>builder()
                                                                    .put(

                                                                    RegularPartitions.FACTORY.getPartitionByName("graph"),
                                                                        JRedisServerConfigExample.GRAPH)
                                                                    .put(
                                                                        RegularPartitions.FEDERATION,
                                                                        JRedisServerConfigExample.FEDERATION)
                                                                    .put(
                                                                        RegularPartitions.LINE_REFERENCE,
                                                                        JRedisServerConfigExample.LINE_REFERENCE)
                                                                    .put(
                                                                        RegularPartitions.LOG, JRedisServerConfigExample.LOG)
                                                                    .put(
                                                                        RegularPartitions.SECURITY,
                                                                        JRedisServerConfigExample.SECURITY)
                                                                    .put(RegularPartitions.SYNTAX_HIGHLIGHT,
                                                                        JRedisServerConfigExample.SYNTAX_HIGHLIGHT).build();

    @Override
    protected void clearData()
        throws Exception {
        final JRedisFactory autoFlushFactory = injector
                .getInstance(JRedisFactory.class);
        autoFlushFactory.getFrom(RegularPartitions.FEDERATION).flushall();
        autoFlushFactory.getFrom(RegularPartitions.FEDERATION).save();
    }

    @Override
    protected Injector createInjector()
        throws Exception {
        return Guice.createInjector(new JRedisStorageModule(
                StorageSession.FlushMode.AUTO, mappedServerConfig, RegularPartitions.FACTORY),
                new GraphModule());
    }

}
