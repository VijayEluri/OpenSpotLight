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
package org.openspotlight.federation.context;

import java.util.concurrent.ConcurrentHashMap;

import javax.jcr.Session;

import org.openspotlight.common.DisposingListener;
import org.openspotlight.common.concurrent.DefaultAtomicLazyResource;
import org.openspotlight.common.concurrent.LockContainer;
import org.openspotlight.common.util.AbstractFactory;
import org.openspotlight.federation.domain.Artifact;
import org.openspotlight.federation.domain.Repository;
import org.openspotlight.federation.finder.ArtifactFinder;
import org.openspotlight.federation.finder.JcrSessionArtifactFinder;
import org.openspotlight.federation.loader.ConfigurationManager;
import org.openspotlight.federation.loader.JcrSessionConfigurationManagerFactory;
import org.openspotlight.federation.log.JcrDetailedLogger;
import org.openspotlight.graph.SLGraph;
import org.openspotlight.graph.SLGraphFactory;
import org.openspotlight.graph.SLGraphSession;
import org.openspotlight.jcr.provider.DefaultJcrDescriptor;
import org.openspotlight.jcr.provider.JcrConnectionDescriptor;
import org.openspotlight.jcr.provider.JcrConnectionProvider;
import org.openspotlight.log.DetailedLogger;
import org.openspotlight.security.SecurityFactory;
import org.openspotlight.security.idm.AuthenticatedUser;
import org.openspotlight.security.idm.User;

/**
 * This class is an {@link ExecutionContext} which initialize all resources in a
 * lazy way, and also close it in a lazy way also.
 * 
 * @author feu
 * 
 */
public class DefaultExecutionContext implements ExecutionContext, LockContainer {

	private final class LazyConfigurationManagerProvider extends
			DefaultAtomicLazyResource<ConfigurationManager> {
		private LazyConfigurationManagerProvider(
				final LockContainer lockContainer) {
			super(lockContainer);
		}

		@Override
		protected ConfigurationManager createReference() {
			return JcrSessionConfigurationManagerFactory
					.createMutableUsingSession(JcrConnectionProvider
							.createFromData(descriptor).openSession());
		}
	}

	private final class LazyDetailedLoggerProvider extends
			DefaultAtomicLazyResource<DetailedLogger> {
		private LazyDetailedLoggerProvider(final LockContainer lockContainer) {
			super(lockContainer);
		}

		@Override
		protected DetailedLogger createReference() {
			return new JcrDetailedLogger(JcrConnectionProvider.createFromData(
					descriptor).openSession());
		}
	}

	private final class LazyGraphSessionProvider extends
			DefaultAtomicLazyResource<SLGraphSession> {
		private LazyGraphSessionProvider(final LockContainer lockContainer) {
			super(lockContainer);
		}

		@Override
		protected SLGraphSession createReference() throws Exception {
			final SLGraph graph = AbstractFactory.getDefaultInstance(
					SLGraphFactory.class).createGraph(descriptor);
			return graph.openSession(lazyAuthenticatedUserReference.get(),
					repositoryName);
		}
	}

	private final class LazyJcrConnectionProvider extends
			DefaultAtomicLazyResource<JcrConnectionProvider> {
		private LazyJcrConnectionProvider(final LockContainer lockContainer) {
			super(lockContainer);
		}

		@Override
		protected JcrConnectionProvider createReference() {
			return JcrConnectionProvider.createFromData(descriptor);
		}
	}

	private final String username;
	private final String password;
	private final JcrConnectionDescriptor descriptor;
	private final String repositoryName;
	private final DisposingListener<DefaultExecutionContext> listener;
	private final Object lock = new Object();
	private final ConcurrentHashMap<? extends Artifact, DefaultAtomicLazyResource<ArtifactFinder<? extends Artifact>>> artifactFinderReferences = new ConcurrentHashMap<Artifact, DefaultAtomicLazyResource<ArtifactFinder<? extends Artifact>>>();

	private final DefaultAtomicLazyResource<AuthenticatedUser> lazyAuthenticatedUserReference = new DefaultAtomicLazyResource<AuthenticatedUser>() {

		@Override
		protected AuthenticatedUser createReference() throws Exception {
			final SecurityFactory securityFactory = AbstractFactory
					.getDefaultInstance(SecurityFactory.class);
			final User simpleUser = securityFactory.createUser(username);
			final AuthenticatedUser user = securityFactory
					.createIdentityManager(DefaultJcrDescriptor.TEMP_DESCRIPTOR)
					.authenticate(simpleUser, password);
			return user;
		}
	};

	private final DefaultAtomicLazyResource<JcrConnectionProvider> lazyConnectionProviderReference = new LazyJcrConnectionProvider(
			this);

	private final DefaultAtomicLazyResource<ConfigurationManager> lazyConfigurationManagerReference = new LazyConfigurationManagerProvider(
			this);

	private final DefaultAtomicLazyResource<SLGraphSession> lazyGraphSessionReference = new LazyGraphSessionProvider(
			this);

	private final DefaultAtomicLazyResource<DetailedLogger> lazyDetailedLoggerReference = new LazyDetailedLoggerProvider(
			this);

	DefaultExecutionContext(final String username, final String password,
			final JcrConnectionDescriptor descriptor,
			final String repositoryName,
			final DisposingListener<DefaultExecutionContext> listener) {
		this.username = username;
		this.password = password;
		this.descriptor = descriptor;
		this.repositoryName = repositoryName;
		this.listener = listener;

	}

	public void closeResources() {
		synchronized (lock) {
			for (final DefaultAtomicLazyResource<ArtifactFinder<? extends Artifact>> lazyReference : artifactFinderReferences
					.values()) {
				lazyReference.closeResources();
			}
			lazyConfigurationManagerReference.closeResources();
			lazyDetailedLoggerReference.closeResources();
			lazyGraphSessionReference.closeResources();
			lazyConnectionProviderReference.closeResources();
			listener.didCloseResource(this);
		}
	}

	@SuppressWarnings("unchecked")
	public <A extends Artifact> ArtifactFinder<A> getArtifactFinder(
			final Class<A> type) {
		synchronized (lock) {
			DefaultAtomicLazyResource<ArtifactFinder<? extends Artifact>> lazyReference = artifactFinderReferences
					.get(type);
			if (lazyReference == null) {
				lazyReference = new DefaultAtomicLazyResource<ArtifactFinder<? extends Artifact>>() {
					@Override
					protected ArtifactFinder<? extends Artifact> createReference() {
						final Repository typedRepository = new Repository();
						typedRepository.setName(repositoryName);
						final ArtifactFinder<A> newFinder = JcrSessionArtifactFinder
								.<A> createArtifactFinder(
										type,
										typedRepository,
										(Session) lazyConnectionProviderReference
												.get().openSession());
						return newFinder;
					}
				};
			}
			return (ArtifactFinder<A>) lazyReference.get();
		}
	}

	public ConfigurationManager getDefaultConfigurationManager() {
		return lazyConfigurationManagerReference.get();
	}

	public JcrConnectionProvider getDefaultConnectionProvider() {
		return lazyConnectionProviderReference.get();
	}

	public SLGraphSession getGraphSession() {
		return lazyGraphSessionReference.get();
	}

	public Object getLockObject() {
		return lock;
	}

	public DetailedLogger getLogger() {
		return lazyDetailedLoggerReference.get();
	}

	public String getRepository() {
		return repositoryName;
	}

	public AuthenticatedUser getUser() {
		return lazyAuthenticatedUserReference.get();
	}

}
