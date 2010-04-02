package org.openspotlight.bundle.language.java.bundle;

import org.openspotlight.bundle.language.java.JavaConstants;
import org.openspotlight.common.util.Assertions;
import org.openspotlight.common.util.SLCollections;
import org.openspotlight.federation.context.ExecutionContext;
import org.openspotlight.federation.domain.artifact.Artifact;
import org.openspotlight.federation.domain.artifact.StreamArtifact;
import org.openspotlight.federation.domain.artifact.StringArtifact;
import org.openspotlight.federation.processing.*;
import org.openspotlight.graph.SLContext;
import org.openspotlight.graph.SLGraphSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.LinkedHashSet;
import java.util.Set;

public class JavaGlobalPhase implements BundleProcessorGlobalPhase<Artifact> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public void didFinishProcessing(final ArtifactChanges<Artifact> changes,
			final ExecutionContext context,
			final CurrentProcessorContext currentContext) {

	}

	@SuppressWarnings("unchecked")
	public Set<Class<? extends Artifact>> getArtifactTypes() {
		return SLCollections.<Class<? extends Artifact>> setOf(
				StreamArtifact.class, StringArtifact.class);
	}

	public SaveBehavior getSaveBehavior() {
		return SaveBehavior.PER_ARTIFACT;
	}

	@SuppressWarnings("unchecked")
	public void selectArtifactsToBeProcessed(
			final CurrentProcessorContext currentContext,
			final ExecutionContext context,
			final ArtifactChanges<Artifact> changes,
			final ArtifactsToBeProcessed<Artifact> toBeReturned)
			throws Exception {
		final String classpahtEntries = currentContext.getBundleProperties()
				.get(JavaConstants.JAR_CLASSPATH);
		final Set<String> contexts = new LinkedHashSet<String>();
		Session artifactSession = (Session) context
				.getPersistentArtifactManager().getPersistentEngine();
		if (classpahtEntries != null) {
			final String[] entries = classpahtEntries
					.split(JavaConstants.CLASSPATH_SEPARATOR_REGEXP);
			for (final String entry : entries) {
				final StreamArtifact artifact = context
						.getPersistentArtifactManager().findByPath(
								StreamArtifact.class, entry);
				Assertions.checkNotNull("artifact:" + entry, artifact);
				Assertions.checkCondition("artifactNameEndsWithJar:"
						+ artifact.getArtifactCompleteName(), artifact
						.getArtifactCompleteName().endsWith(".jar"));
				String ctxName = artifact.getUniqueContextName();
				if (ctxName == null) {
					ctxName = JavaBinaryProcessor.discoverContextName(artifact,
							artifactSession);
					if (logger.isDebugEnabled()) {
						logger.debug("context unique name for "
								+ artifact.getArtifactCompleteName() + " = "
								+ ctxName);
					}
				}
				Assertions.checkNotEmpty("ctxName", ctxName);
				contexts.add(ctxName);
			}
		}
		final String contextsEntries = currentContext.getBundleProperties()
				.get(JavaConstants.CONTEXT_CLASSPATH_ENTRY);
		if (contextsEntries != null) {
			final String[] entries = contextsEntries
					.split(JavaConstants.CLASSPATH_SEPARATOR_REGEXP);
			for (final String entry : entries) {
				contexts.add(entry);
			}
		}
		final SLGraphSession session = context.getGraphSession();
		for (final String entry : contexts) {
			final SLContext slContext = session.getContext(entry);
			Assertions.checkNotNull("slContext:" + entry, slContext);
		}

		synchronized (currentContext.getTransientProperties()) {

			if (currentContext.getTransientProperties().containsKey(
					JavaConstants.USING_CONTEXTS)) {
				final Set<String> existent = (Set<String>) currentContext
						.getTransientProperties().get(
								JavaConstants.USING_CONTEXTS);
				existent.addAll(contexts);
				if (logger.isDebugEnabled()) {
					logger.debug("current contexts: " + existent);
				}
			} else {
				currentContext.getTransientProperties().put(
						JavaConstants.USING_CONTEXTS, contexts);
				if (logger.isDebugEnabled()) {
					logger.debug("adding contexts: " + contexts);
				}
			}
		}
	}

}
