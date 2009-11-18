/**
 * 
 */
package org.openspotlight.federation.processing.internal;

import java.util.Set;

import org.openspotlight.common.concurrent.CautiousExecutor;
import org.openspotlight.common.util.AbstractFactory;
import org.openspotlight.federation.domain.Artifact;
import org.openspotlight.federation.domain.GlobalSettings;
import org.openspotlight.federation.domain.Repository;
import org.openspotlight.federation.finder.ArtifactTypeRegistry;
import org.openspotlight.federation.processing.BundleExecutionException;
import org.openspotlight.graph.SLGraph;
import org.openspotlight.graph.SLGraphFactory;
import org.openspotlight.jcr.provider.JcrConnectionDescriptor;
import org.openspotlight.jcr.provider.JcrConnectionProvider;

public class BundleProcessorExecution {
    private final JcrConnectionDescriptor  descriptor;
    private final GlobalSettings           settings;
    private final Repository[]             repositories;

    private JcrConnectionProvider          provider;

    private SLGraph                        graph;

    private CautiousExecutor               executor;

    private Set<Class<? extends Artifact>> artifactTypes;

    public BundleProcessorExecution(
                                     final JcrConnectionDescriptor descriptor, final GlobalSettings settings,
                                     final Repository[] repositories ) {
        this.descriptor = descriptor;
        this.settings = settings;
        this.repositories = repositories;
    }

    public void execute() throws BundleExecutionException {

    }

    public void setup() throws Exception {
        this.provider = JcrConnectionProvider.createFromData(this.descriptor);
        this.graph = AbstractFactory.getDefaultInstance(SLGraphFactory.class).createGraph(this.descriptor);
        this.artifactTypes = ArtifactTypeRegistry.INSTANCE.getRegisteredArtifactTypes();
    }

}