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
package org.openspotlight.federation.scheduler.test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.hamcrest.number.IsCloseTo;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openspotlight.common.util.Files;
import org.openspotlight.federation.context.ExecutionContext;
import org.openspotlight.federation.context.TestExecutionContextFactory;
import org.openspotlight.federation.context.TestExecutionContextFactory.ArtifactFinderType;
import org.openspotlight.federation.domain.GlobalSettings;
import org.openspotlight.federation.domain.Group;
import org.openspotlight.federation.domain.Repository;
import org.openspotlight.federation.domain.Schedulable.SchedulableCommand;
import org.openspotlight.federation.domain.artifact.ArtifactSource;
import org.openspotlight.federation.scheduler.DefaultScheduler;
import org.openspotlight.federation.scheduler.SLScheduler;
import org.openspotlight.jcr.provider.DefaultJcrDescriptor;

public class SchedulerExecutionOrderTest {

	public static class SampleArtifactSourceSchedulableCommand implements
			SchedulableCommand<ArtifactSource> {

		public void execute(final GlobalSettings settigns,
				final ExecutionContext ctx, final ArtifactSource schedulable) {
			ctx.getUser();
		}

		public String getRepositoryNameBeforeExecution(
				final ArtifactSource schedulable) {
			return schedulable.getRepository().getName();
		}

	}

	public static class SampleGroupSchedulableCommand implements
			SchedulableCommand<Group> {

		private static AtomicBoolean wasExecuted = new AtomicBoolean();

		private static AtomicInteger counter = new AtomicInteger();

		public void execute(final GlobalSettings settigns,
				final ExecutionContext ctx, final Group schedulable) {
			ctx.getUser();
			System.out.println(schedulable.getName());
			wasExecuted.set(true);
			counter.incrementAndGet();
		}

		public String getRepositoryNameBeforeExecution(final Group schedulable) {
			return schedulable.getRepository().getName();
		}

	}

	private static SLScheduler scheduler = DefaultScheduler.INSTANCE;

	private static Set<Repository> repositories = new HashSet<Repository>();

	private static GlobalSettings settings = new GlobalSettings();

	@BeforeClass
	public static void setupScheduler() {
		final ArtifactSource source = new ArtifactSource();
		final String initialRawPath = Files
				.getNormalizedFileName(new File(".."));
		final String initial = initialRawPath.substring(0, initialRawPath
				.lastIndexOf('/'));
		source.setActive(true);
		source.setInitialLookup(initial);
		source.setName("sourceName");

		settings = new GlobalSettings();
		settings.getSchedulableCommandMap().clear();
		settings.getSchedulableCommandMap().put(ArtifactSource.class,
				SampleArtifactSourceSchedulableCommand.class);
		settings.getSchedulableCommandMap().put(Group.class,
				SampleGroupSchedulableCommand.class);
		final Repository repository = new Repository();
		repositories.add(repository);
		repository.setActive(true);
		repository.setName("repository");
		source.setRepository(repository);

		final Group group = new Group();
		group.setActive(true);
		group.setName("new group");
		group.setRepository(repository);
		group.setType("types");
		repository.getGroups().add(group);
		scheduler.initializeSettings(TestExecutionContextFactory.createFactory(
				ArtifactFinderType.FILESYSTEM, source), "user", "password",
				DefaultJcrDescriptor.TEMP_DESCRIPTOR);
		scheduler.refreshJobs(settings, repositories);
		scheduler.startScheduler();
	}

	@AfterClass
	public static void stopScheduler() {
		scheduler.stopScheduler();
	}

	@Before
	public void resetStatus() {
		SampleGroupSchedulableCommand.wasExecuted.set(false);
		SampleGroupSchedulableCommand.counter.set(0);
	}

	@Test
	public void shouldStartCronJobs() throws Exception {
		final Group group = repositories.iterator().next().getGroups()
				.iterator().next();
		try {
			group.getCronInformation().add("0/1 * * * * ?");
			scheduler.refreshJobs(settings, repositories);
			Thread.sleep(10000);
			Assert.assertThat((double) SampleGroupSchedulableCommand.counter
					.get(), IsCloseTo.closeTo(10d, 1d));

		} finally {
			group.getCronInformation().clear();
			scheduler.refreshJobs(settings, repositories);
		}
	}

	@Test
	public void shouldStartImediateJob() throws Exception {
		scheduler.fireSchedulable("username", "password", repositories
				.iterator().next().getGroups().iterator().next());
		for (int i = 0; i < 20; i++) {
			if (SampleGroupSchedulableCommand.wasExecuted.get()) {
				return;
			}
			Thread.sleep(1000);
		}
		Assert.fail("Didn't execute in 20 seconds!");
	}
}
