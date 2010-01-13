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
package org.openspotlight.federation.processing.internal.task;

import static org.openspotlight.common.concurrent.Priority.createPriority;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.PriorityBlockingQueue;

import org.openspotlight.common.concurrent.Priority;
import org.openspotlight.common.util.Exceptions;
import org.openspotlight.federation.context.ExecutionContext;
import org.openspotlight.federation.domain.artifact.Artifact;
import org.openspotlight.federation.domain.artifact.ArtifactWithSyntaxInformation;
import org.openspotlight.federation.domain.artifact.ChangeType;
import org.openspotlight.federation.domain.artifact.LastProcessStatus;
import org.openspotlight.federation.finder.ArtifactFinder;
import org.openspotlight.federation.finder.ArtifactFinderWithSaveCapabilitie;
import org.openspotlight.federation.processing.BundleProcessorArtifactPhase;
import org.openspotlight.federation.processing.SaveBehavior;
import org.openspotlight.federation.processing.internal.domain.CurrentProcessorContextImpl;
import org.openspotlight.log.DetailedLogger.LogEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class _2_EachArtifactTask<T extends Artifact> implements ArtifactTask {

	private static final Object SAVE_LOCK = new Object();
	private final Priority priority;

	/** The bundle processor context. */
	private ExecutionContext bundleProcessorContext;

	private final CountDownLatch parentPhaseLatch;

	/** The artifact type. */
	private final Class<T> artifactType;

	/** The artifact. */
	private final T artifact;

	private final SaveBehavior saveBehavior;
	/** The bundle processor. */
	private final BundleProcessorArtifactPhase<T> bundleProcessor;
	private final CountDownLatch allPhaseTwoLatch;
	private final CountDownLatch thisArtifactLatch;
	/** The current context impl. */
	private final CurrentProcessorContextImpl currentContextImpl;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Instantiates a new artifact processing runnable.
	 * 
	 * @param currentCtx
	 *            the current ctx
	 * @param artifactQueue
	 *            the artifact queue
	 * @param artifact
	 *            the artifact
	 * @param bundleProcessor
	 *            the bundle processor
	 * @param artifactType
	 *            the artifact type
	 */
	public _2_EachArtifactTask(final CurrentProcessorContextImpl currentCtx,
			final T artifact,
			final BundleProcessorArtifactPhase<T> bundleProcessor,
			final Class<T> artifactType, final SaveBehavior saveBehavior,
			final int subpriority, final CountDownLatch parentPhaseLatch,
			final CountDownLatch allPhaseTwoLatch,
			final CountDownLatch thisArtifactLatch) {
		this.artifactType = artifactType;
		this.artifact = artifact;
		this.bundleProcessor = bundleProcessor;
		this.currentContextImpl = currentCtx;
		this.saveBehavior = saveBehavior;
		this.priority = createPriority(2, subpriority);
		this.parentPhaseLatch = parentPhaseLatch;
		this.allPhaseTwoLatch = allPhaseTwoLatch;
		this.thisArtifactLatch = thisArtifactLatch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@SuppressWarnings("unchecked")
	public void doTask() throws Exception {
		parentPhaseLatch.await();
		try {
			if (LastProcessStatus.EXCEPTION_DURRING_PROCESS
					.equals(this.artifact.getLastProcessStatus())
					|| LastProcessStatus.EXCEPTION_DURRING_PROCESS
							.equals(this.artifact.getLastProcessStatus())) {
				logger.info("ignoring " + this.artifact
						+ " due to its last process status: "
						+ this.artifact.getLastProcessStatus());
				return;
			}
			this.bundleProcessor.beforeProcessArtifact(this.artifact);
			LastProcessStatus result = null;
			try {
				if (this.artifact instanceof ArtifactWithSyntaxInformation) {
					final ArtifactWithSyntaxInformation artifactWithInfo = (ArtifactWithSyntaxInformation) this.artifact;
					artifactWithInfo.clearSyntaxInformationSet();
				}
				result = this.bundleProcessor.processArtifact(this.artifact,
						this.currentContextImpl, this.bundleProcessorContext);
				if (SaveBehavior.PER_ARTIFACT.equals(this.saveBehavior)) {
					this.bundleProcessorContext.getGraphSession().save();
				}
			} catch (final Exception e) {
				result = LastProcessStatus.EXCEPTION_DURRING_PROCESS;
				Exceptions.catchAndLog(e);
				this.bundleProcessorContext.getLogger().log(
						this.bundleProcessorContext.getUser(),
						LogEventType.ERROR,
						"Error during artifact processing on bundle processor "
								+ this.bundleProcessor.getClass().getName(),
						this.artifact);
				throw e;
			} finally {
				this.artifact.setLastProcessStatus(result);
				this.artifact.setLastProcessedDate(new Date());
				final ArtifactFinder<T> finder = this.bundleProcessorContext
						.getArtifactFinder(this.artifactType);
				Exception ex = null;
				if (finder instanceof ArtifactFinderWithSaveCapabilitie) {
					final ArtifactFinderWithSaveCapabilitie<T> finderWithSaveCapabilitie = (ArtifactFinderWithSaveCapabilitie<T>) finder;
					try {
						if (LastProcessStatus.PROCESSED.equals(artifact
								.getLastProcessStatus())
								|| LastProcessStatus.IGNORED.equals(artifact
										.getLastProcessStatus())) {
							synchronized (SAVE_LOCK) {
								if (ChangeType.EXCLUDED.equals(this.artifact
										.getChangeType())) {
									finderWithSaveCapabilitie
											.markAsRemoved(this.artifact);
								} else {
									finderWithSaveCapabilitie
											.addTransientArtifact(this.artifact);
								}
								finderWithSaveCapabilitie.save();
							}
						}

					} catch (final Exception e) {
						Exceptions.catchAndLog(e);
						ex = e;
					}

				}
				this.bundleProcessor.didFinishToProcessArtifact(this.artifact,
						result);
				if (ex != null) {
					throw ex;
				}
			}
		} finally {
			thisArtifactLatch.countDown();
			allPhaseTwoLatch.countDown();
		}
	}

	public String getArtifactName() {
		return artifact.getRepositoryName() + ":"
				+ artifact.getClass().getSimpleName() + " "
				+ artifact.getArtifactCompleteName();
	}

	public CurrentProcessorContextImpl getCurrentContext() {
		return this.currentContextImpl;
	}

	public Priority getPriority() {
		return priority;
	}

	public String getRepositoryName() {
		return this.artifact.getRepositoryName();
	}

	public void setBundleContext(final ExecutionContext context) {
		this.bundleProcessorContext = context;

	}

	public void setQueue(final PriorityBlockingQueue<ArtifactTask> queue) {
	}
}
