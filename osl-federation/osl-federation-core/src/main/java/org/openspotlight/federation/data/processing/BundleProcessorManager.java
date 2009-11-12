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
 * OpenSpotLight - Plataforma de Governan�a de TI de C�digo Aberto 
 *
 * Direitos Autorais Reservados (c) 2009, CARAVELATECH CONSULTORIA E TECNOLOGIA 
 * EM INFORMATICA LTDA ou como contribuidores terceiros indicados pela etiqueta 
 * @author ou por expressa atribui��o de direito autoral declarada e atribu�da pelo autor.
 * Todas as contribui��es de terceiros est�o distribu�das sob licen�a da
 * CARAVELATECH CONSULTORIA E TECNOLOGIA EM INFORMATICA LTDA. 
 * 
 * Este programa � software livre; voc� pode redistribu�-lo e/ou modific�-lo sob os 
 * termos da Licen�a P�blica Geral Menor do GNU conforme publicada pela Free Software 
 * Foundation. 
 * 
 * Este programa � distribu�do na expectativa de que seja �til, por�m, SEM NENHUMA 
 * GARANTIA; nem mesmo a garantia impl�cita de COMERCIABILIDADE OU ADEQUA��O A UMA
 * FINALIDADE ESPEC�FICA. Consulte a Licen�a P�blica Geral Menor do GNU para mais detalhes.  
 * 
 * Voc� deve ter recebido uma c�pia da Licen�a P�blica Geral Menor do GNU junto com este
 * programa; se n�o, escreva para: 
 * Free Software Foundation, Inc. 
 * 51 Franklin Street, Fifth Floor 
 * Boston, MA  02110-1301  USA
 */

package org.openspotlight.federation.data.processing;

import static org.openspotlight.common.util.Arrays.andOf;
import static org.openspotlight.common.util.Arrays.of;
import static org.openspotlight.common.util.Assertions.checkCondition;
import static org.openspotlight.common.util.Assertions.checkNotNull;
import static org.openspotlight.common.util.Equals.eachEquality;
import static org.openspotlight.common.util.Exceptions.catchAndLog;
import static org.openspotlight.common.util.Exceptions.logAndReturn;
import static org.openspotlight.common.util.Exceptions.logAndReturnNew;
import static org.openspotlight.common.util.HashCodes.hashOf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.openspotlight.common.LazyType;
import org.openspotlight.common.MutableType;
import org.openspotlight.common.exception.ConfigurationException;
import org.openspotlight.common.util.AbstractFactory;
import org.openspotlight.federation.data.processing.BundleProcessor.BundleProcessingContext;
import org.openspotlight.federation.data.processing.BundleProcessor.BundleProcessingGroup;
import org.openspotlight.federation.data.processing.BundleProcessor.ProcessingAction;
import org.openspotlight.federation.data.processing.BundleProcessor.ProcessingStartAction;
import org.openspotlight.federation.domain.Artifact;
import org.openspotlight.federation.domain.ArtifactSource;
import org.openspotlight.federation.domain.BundleProcessorType;
import org.openspotlight.federation.domain.CustomArtifact;
import org.openspotlight.federation.domain.StreamArtifact;
import org.openspotlight.federation.loader.ConfigurationManager;
import org.openspotlight.federation.loader.ConfigurationManagerProvider;
import org.openspotlight.jcr.provider.JcrConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link BundleProcessorManager} is the class reposable to get an {@link Configuration} and to process all {@link Artifact
 * artifacts} on this {@link Configuration}. The {@link BundleProcessorManager} should get the {@link ArtifactSource bundle's}
 * {@link BundleProcessorType types} and find all the {@link BundleProcessor processors} for each {@link BundleProcessorType type}
 * . After all {@link BundleProcessor processors} was found, the {@link BundleProcessorManager} should distribute the processing
 * job in some threads obeying the {@link Configuration#getNumberOfParallelThreads() number of threads} configured for this
 * {@link Repository}.
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 */
public final class BundleProcessorManager {

    /**
     * This callable class is used to wrap a {@link BundleProcessor} instance and to group its artifacts by the
     * {@link BundleProcessor} return type. So, it will be possible to know if an artifact was processed, ignored, and so on.
     * 
     * @param <T> *
     * @author Luiz Fernando Teston - feu.teston@caravelatech.com
     */
    private static class BundleProcessorCallable<T extends Artifact> implements Callable<ProcessingAction> {

        /** The bundle processor. */
        private final BundleProcessor<T>             bundleProcessor;

        /** The call back. */
        private final BundleProcessorWithCallback<T> callBack;

        /** The graph context. */
        private final BundleProcessingContext        graphContext;

        /** The immutable processing group. */
        private final BundleProcessingGroup<T>       immutableProcessingGroup;

        /** The mutable processing group. */
        private final BundleProcessingGroup<T>       mutableProcessingGroup;

        /** The target artifact. */
        private final T                              targetArtifact;

        /**
         * Constructor to initialize final mandatory fields.
         * 
         * @param bundleProcessor the bundle processor
         * @param targetArtifact the target artifact
         * @param graphContext the graph context
         * @param immutableProcessingGroup the immutable processing group
         * @param mutableProcessingGroup the mutable processing group
         */
        public BundleProcessorCallable(
                                        final BundleProcessor<T> bundleProcessor, final T targetArtifact,
                                        final BundleProcessingContext graphContext,
                                        final BundleProcessingGroup<T> immutableProcessingGroup,
                                        final BundleProcessingGroup<T> mutableProcessingGroup ) {
            checkNotNull("bundleProcessor", bundleProcessor); //$NON-NLS-1$
            checkNotNull("targetArtifact", targetArtifact); //$NON-NLS-1$
            checkNotNull("graphContext", graphContext); //$NON-NLS-1$
            checkNotNull("immutableProcessingGroup", immutableProcessingGroup); //$NON-NLS-1$
            checkNotNull("mutableProcessingGroup", mutableProcessingGroup); //$NON-NLS-1$
            this.bundleProcessor = bundleProcessor;
            this.targetArtifact = targetArtifact;
            this.graphContext = graphContext;
            this.immutableProcessingGroup = immutableProcessingGroup;
            this.mutableProcessingGroup = mutableProcessingGroup;
            if (this.bundleProcessor instanceof BundleProcessorWithCallback) {
                this.callBack = (BundleProcessorWithCallback<T>)this.bundleProcessor;
            } else {
                this.callBack = null;
            }
        }

        /**
         * Starts the {@link BundleProcessor#processArtifact(Artifact, BundleProcessingGroup, BundleProcessingContext)} method.
         * After its processing, depending on return type this method will do the necessary manipulation on
         * {@link BundleProcessingGroup} object.
         * 
         * @return the {@link ProcessingAction} depending on {@link BundleProcessor} result
         * @throws Exception if anything goes wrong
         */
        public ProcessingAction call() throws Exception {
            if (this.callBack != null) {
                this.graphContext.processStarted();
                this.callBack.artifactProcessingStarted(this.targetArtifact, this.immutableProcessingGroup, this.graphContext);
            }
            ProcessingAction ret;
            try {
                ret = this.bundleProcessor.processArtifact(this.targetArtifact, this.immutableProcessingGroup, this.graphContext);
            } catch (final Exception e) {
                catchAndLog(e);
                ret = ProcessingAction.ERROR_PROCESSING_ARTIFACT;
            }
            if (ret == null) {
                ret = ProcessingAction.ERROR_PROCESSING_ARTIFACT;
            }
            this.mutableProcessingGroup.getNotProcessedArtifacts().remove(this.targetArtifact);
            switch (ret) {
                case ARTIFACT_IGNORED:
                    this.mutableProcessingGroup.getIgnoredArtifacts().add(this.targetArtifact);
                    break;
                case ARTIFACT_PROCESSED:
                    this.mutableProcessingGroup.getAlreadyProcessedArtifacts().add(this.targetArtifact);
                    break;
                case ERROR_PROCESSING_ARTIFACT:
                    this.mutableProcessingGroup.getArtifactsWithError().add(this.targetArtifact);
                    break;
            }

            if (this.callBack != null) {
                this.callBack.artifactProcessingFinalized(this.targetArtifact, this.immutableProcessingGroup, this.graphContext,
                                                          ret);
            }
            this.graphContext.processFinished();

            return ret;
        }

    }

    /**
     * This class groups the necessary data for processing each artifact and also the {@link FinalizationContext} for each bundle.
     * 
     * @param <T> *
     * @author Luiz Fernando Teston - feu.teston@caravelatech.com
     */
    private static class CreateProcessorActionsResult<T extends Artifact> {

        /** The finalization context. */
        FinalizationContext<T>                         finalizationContext;

        /** The processor callables. */
        private final List<BundleProcessorCallable<T>> processorCallables;

        /**
         * Instantiates a new creates the processor actions result.
         * 
         * @param processorCallables the processor callables
         * @param finalizationContext the finalization context
         */
        public CreateProcessorActionsResult(
                                             final List<BundleProcessorCallable<T>> processorCallables,
                                             final FinalizationContext<T> finalizationContext ) {
            super();
            this.processorCallables = processorCallables;
            this.finalizationContext = finalizationContext;
        }

        /**
         * Gets the finalization context.
         * 
         * @return the finalization context
         */
        public FinalizationContext<T> getFinalizationContext() {
            return this.finalizationContext;
        }

        /**
         * Gets the processor callables.
         * 
         * @return the processor callables
         */
        public List<BundleProcessorCallable<T>> getProcessorCallables() {
            return this.processorCallables;
        }

    }

    /**
     * Some necessary data for finalization callback methods.
     * 
     * @param <T> Artifact type
     * @author Luiz Fernando Teston - feu.teston@caravelatech.com
     */
    private static class FinalizationContext<T extends Artifact> {

        /** The bundle processing group. */
        private final BundleProcessingGroup<T> bundleProcessingGroup;

        /** The graph context. */
        private final BundleProcessingContext  graphContext;

        /** The processor. */
        private final BundleProcessor<T>       processor;

        /**
         * Instantiates a new finalization context.
         * 
         * @param graphContext the graph context
         * @param bundleProcessingGroup the bundle processing group
         * @param processor the processor
         */
        public FinalizationContext(
                                    final BundleProcessingContext graphContext,
                                    final BundleProcessingGroup<T> bundleProcessingGroup, final BundleProcessor<T> processor ) {
            checkNotNull("graphContext", graphContext); //$NON-NLS-1$
            checkNotNull("bundleProcessingGroup", bundleProcessingGroup); //$NON-NLS-1$
            checkNotNull("processor", processor); //$NON-NLS-1$
            this.graphContext = graphContext;
            this.bundleProcessingGroup = bundleProcessingGroup;
            this.processor = processor;
        }

        /**
         * Gets the bundle processing group.
         * 
         * @return the bundle processing group
         */
        public BundleProcessingGroup<T> getBundleProcessingGroup() {
            return this.bundleProcessingGroup;
        }

        /**
         * Gets the graph context.
         * 
         * @return the global graph context
         */
        public BundleProcessingContext getGraphContext() {
            return this.graphContext;
        }

        /**
         * Gets the processor.
         * 
         * @return the bundle processor
         */
        public BundleProcessor<T> getProcessor() {
            return this.processor;
        }

    }

    /**
     * This class is used to identify a processor by its artifact type. The processor type must have only interfaces.
     * 
     * @param <T> target artifact type
     * @author Luiz Fernando Teston - feu.teston@caravelatech.com
     */
    private static class MappedProcessor<T extends Artifact> {

        /** The artifact type. */
        private final Class<T>                            artifactType;

        /** The hashcode. */
        private final int                                 hashcode;

        /** The processor type. */
        private final Class<? extends BundleProcessor<T>> processorType;

        /**
         * Instantiates a new mapped processor.
         * 
         * @param artifactType the artifact type
         * @param processorType the processor type
         */
        public MappedProcessor(
                                final Class<T> artifactType, final Class<? extends BundleProcessor<T>> processorType ) {
            checkNotNull("artifactType", artifactType); //$NON-NLS-1$
            checkNotNull("processorType", processorType); //$NON-NLS-1$
            checkCondition("processorTypeIsInterface", processorType.isInterface()); //$NON-NLS-1$

            this.artifactType = artifactType;
            this.processorType = processorType;
            this.hashcode = hashOf(artifactType, processorType);
        }

        /* (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @SuppressWarnings( "unchecked" )
        @Override
        public boolean equals( final Object o ) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof MappedProcessor)) {
                return false;
            }
            final MappedProcessor that = (MappedProcessor)o;
            return eachEquality(of(this.artifactType, this.processorType), andOf(that.artifactType, that.processorType));
        }

        /**
         * Gets the artifact type.
         * 
         * @return the target artifact type
         */
        public Class<T> getArtifactType() {
            return this.artifactType;
        }

        /**
         * Gets the processor type.
         * 
         * @return bundle processor type for given artifact
         */
        public Class<? extends BundleProcessor<T>> getProcessorType() {
            return this.processorType;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return this.hashcode;
        }
    }

    /** The Constant emptyResult. */
    private static final CreateProcessorActionsResult<Artifact>   emptyResult = new CreateProcessorActionsResult<Artifact>(
                                                                                                                           new ArrayList<BundleProcessorCallable<Artifact>>(
                                                                                                                                                                            0),
                                                                                                                           null);

    /** The Constant processorRegistry. */
    private static final Set<MappedProcessor<? extends Artifact>> processorRegistry;

    /**
     * Here all maped processors are added to the processor regristry static attribute.
     */
    static {
        processorRegistry = new HashSet<MappedProcessor<? extends Artifact>>();
        processorRegistry.add(new MappedProcessor<StreamArtifact>(StreamArtifact.class, StreamArtifactBundleProcessor.class));
        processorRegistry.add(new MappedProcessor<CustomArtifact>(CustomArtifact.class, CustomArtifactBundleProcessor.class));
    }

    /**
     * This method fills the artifacts by change type.
     * 
     * @param bundle the bundle
     * @param allValidArtifacts the all valid artifacts
     * @param addedArtifacts the added artifacts
     * @param excludedArtifacts the excluded artifacts
     * @param modifiedArtifacts the modified artifacts
     */
    private static <T extends Artifact> void findArtifactsByChangeType( final ArtifactSource bundle,
                                                                        final Set<T> allValidArtifacts,
                                                                        final Set<T> addedArtifacts,
                                                                        final Set<T> excludedArtifacts,
                                                                        final Set<T> modifiedArtifacts ) {
        for (final T artifact : allValidArtifacts) {
            Status status = artifact.getStatus();
            if (status == null) {
                artifact.setStatus(Status.INCLUDED);
                status = Status.INCLUDED;
            }
            switch (status) {
                case ALREADY_PROCESSED:
                    break;
                case CHANGED:
                    modifiedArtifacts.add(artifact);
                    break;
                case EXCLUDED:
                    excludedArtifacts.add(artifact);
                    break;
                case INCLUDED:
                    addedArtifacts.add(artifact);
                    break;

                default:
                    throw logAndReturn(new IllegalStateException());
            }
        }
    }

    /**
     * This method looks for a {@link BundleProcessor} inside the {@link ArtifactSource} configuration.
     * 
     * @param bundle the bundle
     * @return a set of {@link BundleProcessor}
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception
     */
    private static Set<BundleProcessor<?>> findConfiguredBundleProcessors( final ArtifactSource bundle )
        throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        final Set<String> typeNames = bundle.getAllProcessorTypeNames();
        final Set<BundleProcessor<?>> processors = new HashSet<BundleProcessor<?>>();
        for (final String type : typeNames) {
            final BundleProcessor<?> processor = (BundleProcessor<?>)Class.forName(type).newInstance();
            processors.add(processor);
        }
        return processors;
    }

    /** To create {@link SLGraphSession sessions} when needed. */
    private final SLGraph                      graph;

    /** The provider. */
    private final JcrConnectionProvider        provider;

    /** The configuration manager provider. */
    private final ConfigurationManagerProvider configurationManagerProvider;

    /** The logger. */
    private final Logger                       logger = LoggerFactory.getLogger(this.getClass());

    private final AuthenticatedUser            authenticatedUser;

    /**
     * Constructor to initialize jcr connection provider.
     * 
     * @param provider the provider
     * @param configurationManagerProvider the configuration manager provider
     */
    public BundleProcessorManager(
                                   final AuthenticatedUser authenticatedUser, final JcrConnectionProvider provider,
                                   final ConfigurationManagerProvider configurationManagerProvider ) {
        checkNotNull("authenticatedUser", authenticatedUser); //$NON-NLS-1$
        checkNotNull("provider", provider); //$NON-NLS-1$
        checkNotNull("configurationManagerProvider", configurationManagerProvider); //$NON-NLS-1$
        this.authenticatedUser = authenticatedUser;

        this.provider = provider;
        this.configurationManagerProvider = configurationManagerProvider;
        try {
            this.graph = AbstractFactory.getDefaultInstance(SLGraphFactory.class).createGraph(provider.getData());
        } catch (final Exception e) {
            throw logAndReturnNew(e, ConfigurationException.class);
        }
    }

    /**
     * This method creates each {@link Callable} to call
     * {@link BundleProcessor#processArtifact(Artifact, BundleProcessingGroup, BundleProcessingContext)} . It make a few copies
     * because the decision to mark some artifact as ignored or processed should be done by each {@link BundleProcessor},
     * independent of the other {@link BundleProcessor} involved.
     * 
     * @param bundle the bundle
     * @param allValidArtifacts the all valid artifacts
     * @param addedArtifacts the added artifacts
     * @param excludedArtifacts the excluded artifacts
     * @param modifiedArtifacts the modified artifacts
     * @param notProcessedArtifacts the not processed artifacts
     * @param alreadyProcessedArtifacts the already processed artifacts
     * @param ignoredArtifacts the ignored artifacts
     * @param artifactsWithError the artifacts with error
     * @param processor the processor
     * @return a list of {@link Callable} processor action
     * @throws BundleProcessingFatalException the bundle processing fatal exception
     */
    @SuppressWarnings( "unchecked" )
    private <T extends Artifact> CreateProcessorActionsResult<T> createProcessorActions( final ArtifactSource bundle,
                                                                                         final Set<T> allValidArtifacts,
                                                                                         final Set<T> addedArtifacts,
                                                                                         final Set<T> excludedArtifacts,
                                                                                         final Set<T> modifiedArtifacts,
                                                                                         final Set<T> notProcessedArtifacts,
                                                                                         final Set<T> alreadyProcessedArtifacts,
                                                                                         final Set<T> ignoredArtifacts,
                                                                                         final Set<T> artifactsWithError,
                                                                                         final BundleProcessor<T> processor )
        throws BundleProcessingFatalException {
        BundleProcessingContext startingGraphContext = null;
        try {
            final Set<T> copyOfaddedArtifacts = new CopyOnWriteArraySet<T>(addedArtifacts);
            final Set<T> copyOfexcludedArtifacts = new CopyOnWriteArraySet<T>(excludedArtifacts);
            final Set<T> copyOfignoredArtifacts = new CopyOnWriteArraySet<T>(ignoredArtifacts);
            final Set<T> copyOfartifactsWithError = new CopyOnWriteArraySet<T>(artifactsWithError);
            final Set<T> copyOfmodifiedArtifacts = new CopyOnWriteArraySet<T>(modifiedArtifacts);
            final Set<T> copyOfallValidArtifacts = new CopyOnWriteArraySet<T>(allValidArtifacts);
            final Set<T> copyOfnotProcessedArtifacts = new CopyOnWriteArraySet<T>(notProcessedArtifacts);
            final Set<T> copyOfalreadyProcessedArtifacts = new CopyOnWriteArraySet<T>(alreadyProcessedArtifacts);

            final BundleProcessingGroup<T> mutableGroup = new BundleProcessingGroup<T>(bundle, copyOfaddedArtifacts,
                                                                                       copyOfexcludedArtifacts,
                                                                                       copyOfignoredArtifacts,
                                                                                       copyOfartifactsWithError,
                                                                                       copyOfmodifiedArtifacts,
                                                                                       copyOfallValidArtifacts,
                                                                                       copyOfnotProcessedArtifacts,
                                                                                       copyOfalreadyProcessedArtifacts,
                                                                                       MutableType.MUTABLE);
            final BundleProcessingGroup<T> immutableGroup = new BundleProcessingGroup<T>(bundle, copyOfaddedArtifacts,
                                                                                         copyOfexcludedArtifacts,
                                                                                         copyOfignoredArtifacts,
                                                                                         copyOfartifactsWithError,
                                                                                         copyOfmodifiedArtifacts,
                                                                                         copyOfallValidArtifacts,
                                                                                         copyOfnotProcessedArtifacts,
                                                                                         copyOfalreadyProcessedArtifacts,
                                                                                         MutableType.IMMUTABLE);
            startingGraphContext = new BundleProcessingContext(this.graph.openSession(this.authenticatedUser),
                                                               this.provider.openSession(), bundle.getRootGroup(),
                                                               this.configurationManagerProvider.getNewInstance());
            startingGraphContext.processStarted();
            final ProcessingStartAction start = processor.globalProcessingStarted(mutableGroup, startingGraphContext);
            switch (start) {
                case ALL_PROCESSING_ALREADY_DONE:
                case IGNORE_ALL:
                case FATAL_ERROR_ON_START_PROCESSING:
                    notProcessedArtifacts.clear();
                    copyOfnotProcessedArtifacts.clear();
                    processor.globalProcessingFinalized(immutableGroup, startingGraphContext);
                    return (CreateProcessorActionsResult<T>)emptyResult;
                case PROCESS_ALL_AGAIN:

                    copyOfnotProcessedArtifacts.clear();
                    copyOfnotProcessedArtifacts.addAll(allValidArtifacts);
                    break;
                case PROCESS_EACH_ONE_NEW:

                    copyOfnotProcessedArtifacts.clear();
                    copyOfnotProcessedArtifacts.addAll(addedArtifacts);
                    copyOfnotProcessedArtifacts.addAll(modifiedArtifacts);
                    break;
                case PROCESS_CUSTOMIZED_LIST:
                    break;
                default:
                    throw logAndReturn(new IllegalStateException("Unexpected return type for startProcessing")); //$NON-NLS-1$

            }
            final List<BundleProcessorCallable<T>> processActions = new ArrayList<BundleProcessorCallable<T>>(
                                                                                                              copyOfnotProcessedArtifacts.size());
            for (final T targetArtifact : copyOfnotProcessedArtifacts) {
                final BundleProcessingContext graphContext = new BundleProcessingContext(
                                                                                         this.graph.openSession(this.authenticatedUser),
                                                                                         this.provider.openSession(),
                                                                                         bundle.getRootGroup(),
                                                                                         this.configurationManagerProvider.getNewInstance());
                processActions.add(new BundleProcessorCallable(processor, targetArtifact, graphContext, mutableGroup,
                                                               immutableGroup));
            }
            final FinalizationContext<T> finalizationContext = new FinalizationContext<T>(startingGraphContext, immutableGroup,
                                                                                          processor);
            final CreateProcessorActionsResult<T> result = new CreateProcessorActionsResult<T>(processActions,
                                                                                               finalizationContext);
            return result;
        } catch (final Exception e) {
            throw logAndReturnNew(e, BundleProcessingFatalException.class);
        } finally {
            if (startingGraphContext != null) {
                startingGraphContext.processFinished();
            }
        }

    }

    /**
     * This method will look on static attribute {@link #processorRegistry} to map the processor interfaces and artifact types.
     * After that mapping, it will group all processing actions depending of the type, to be processed later.
     * 
     * @param mappedProcessor the mapped processor
     * @param allProcessActions the all process actions
     * @param finalizationContexts the finalization contexts
     * @param bundle the bundle
     * @param processors the processors
     * @throws BundleProcessingFatalException the bundle processing fatal exception
     */
    @SuppressWarnings( "unchecked" )
    private <T extends Artifact> void groupProcessingActionsByArtifactType( final MappedProcessor<T> mappedProcessor,
                                                                            final List<Callable<ProcessingAction>> allProcessActions,
                                                                            final List<FinalizationContext<? extends Artifact>> finalizationContexts,
                                                                            final ArtifactSource bundle,
                                                                            final Set<BundleProcessor<?>> processors )
        throws BundleProcessingFatalException {

        final Set<T> allValidArtifacts = findAllNodesOfType(bundle, mappedProcessor.getArtifactType());
        if (allValidArtifacts.size() == 0) {
            this.logger.warn("There's no artifact to process on bundle " + bundle.getInstanceMetadata().getPath());
        }

        final Set<T> addedArtifacts = new HashSet<T>();
        final Set<T> excludedArtifacts = new HashSet<T>();
        final Set<T> modifiedArtifacts = new HashSet<T>();
        final Set<T> notProcessedArtifacts = new CopyOnWriteArraySet<T>();
        final Set<T> alreadyProcessedArtifacts = new CopyOnWriteArraySet<T>();
        final Set<T> ignoredArtifacts = new CopyOnWriteArraySet<T>();
        final Set<T> artifactsWithError = new CopyOnWriteArraySet<T>();
        findArtifactsByChangeType(bundle, allValidArtifacts, addedArtifacts, excludedArtifacts, modifiedArtifacts);
        notProcessedArtifacts.addAll(addedArtifacts);
        notProcessedArtifacts.addAll(modifiedArtifacts);
        allValidArtifacts.removeAll(excludedArtifacts);
        for (final BundleProcessor<?> processor : processors) {
            if (mappedProcessor.getProcessorType().isInstance(processor)) {
                final CreateProcessorActionsResult<T> result = this.createProcessorActions(bundle, allValidArtifacts,
                                                                                           addedArtifacts, excludedArtifacts,
                                                                                           modifiedArtifacts,
                                                                                           notProcessedArtifacts,
                                                                                           alreadyProcessedArtifacts,
                                                                                           ignoredArtifacts, artifactsWithError,
                                                                                           (BundleProcessor<T>)processor);
                allProcessActions.addAll(result.getProcessorCallables());
                finalizationContexts.add(result.getFinalizationContext());
            }
        }

    }

    /**
     * Start to process this {@link ArtifactSource} and to distribute all the processing jobs for its {@link BundleProcessor
     * configured processors}.
     * 
     * @param bundles the bundles
     * @throws BundleProcessingFatalException if a fatal error occurs.
     */
    @SuppressWarnings( "boxing" )
    public synchronized void processBundles( final Collection<ArtifactSource> bundles ) throws BundleProcessingFatalException {
        checkNotNull("bundles", bundles); //$NON-NLS-1$
        checkNotNull("graph", this.graph); //$NON-NLS-1$
        final ConfigurationManager configurationManager = this.configurationManagerProvider.getNewInstance();
        try {

            final List<Callable<ProcessingAction>> allProcessActions = new ArrayList<Callable<ProcessingAction>>();
            final List<FinalizationContext<? extends Artifact>> finalizationContexts = new ArrayList<FinalizationContext<? extends Artifact>>(
                                                                                                                                              bundles.size());
            for (final ArtifactSource targetBundle : bundles) {
                if (!targetBundle.getActive()) {
                    continue;
                }
                final Configuration configuration = configurationManager.load(LazyType.LAZY);
                final ArtifactSource bundle = configurationManager.findNodeByUuidAndVersion(
                                                                                            configuration,
                                                                                            ArtifactSource.class,
                                                                                            targetBundle.getInstanceMetadata().getSavedUniqueId(),
                                                                                            null);//FIXME set version
                final Set<BundleProcessor<?>> processors = BundleProcessorManager.findConfiguredBundleProcessors(bundle);
                for (final MappedProcessor<? extends Artifact> mappedProcessor : processorRegistry) {
                    this.groupProcessingActionsByArtifactType(mappedProcessor, allProcessActions, finalizationContexts, bundle,
                                                              processors);
                }
            }
            final Integer numberOfParallelThreads = bundles.iterator().next().getRepository().getConfiguration().getNumberOfParallelThreads();
            final ExecutorService executor = Executors.newFixedThreadPool(numberOfParallelThreads);
            try {

                executor.invokeAll(allProcessActions);

                while (executor.awaitTermination(300, TimeUnit.MILLISECONDS)) {
                    this.wait();
                }

                for (final FinalizationContext<? extends Artifact> context : finalizationContexts) {
                    if (context != null) {

                        context.getProcessor().globalProcessingFinalized(context.getBundleProcessingGroup(),
                                                                         context.getGraphContext());
                    }
                }
            } finally {
                executor.shutdown();
            }
        } catch (final Exception e) {
            throw logAndReturnNew(e, BundleProcessingFatalException.class);
        } finally {
            configurationManager.closeResources();
        }
    }

}