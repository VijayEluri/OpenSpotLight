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
package org.openspotlight.graph.server.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.openspotlight.common.exception.SLException;
import org.openspotlight.graph.SLGraph;
import org.openspotlight.graph.GraphReaderpotlight.graph.Node;
import org.openspotlight.graph.client.RemoteGraphSessionFactory;
import org.openspotlight.graph.client.RemoteGraphSessionFactory.RemoteGraphFactoryConnectionData;
import org.openspotlight.graph.guice.SLGraphModule;
import org.openspotlight.graph.manipulation.GraphReader;
import org.openspotlight.graph.query.*;
import org.openspotlight.graph.query.SLQuery.SortMode;
import org.openspotlight.graph.server.RemoteGraphSessionServer;
import org.openspotlight.graph.test.domain.link.JavaInterfaceHierarchy;
import org.openspotlight.graph.test.domain.link.PackageContainsType;
import org.openspotlight.graph.test.domain.link.TypeContainsMethod;
import org.openspotlight.graph.test.domain.node.*;
import org.openspotlight.jcr.provider.DefaultJcrDescriptor;
import org.openspotlight.persist.guice.SimplePersistModule;
import org.openspotlight.remote.server.UserAuthenticator;
import org.openspotlight.storage.StorageSessionport org.openspotlight.storage.redis.guice.JRedisStorageModule;
import org.openspotlight.storage.redis.util.ExampleRedisConfig;

import java.text.Collator;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openspotlight.storage.STRepositoryPath.repositoryPath;

/**
 * The Class SLGraphQueryTest.
 *
 * @author Vitor Hugo Chagas
 */
@SuppressWarnings("unused")
public class SLRemoteGraphQueryTest extends AbstractGeneralQueryTest {

	@Override
	protected Callable<Void> createStartUpHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Callable<Void> createShutdownHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected GraphReader graphReader() {
		// TODO Auto-generated method stub
		return null;
	}
}