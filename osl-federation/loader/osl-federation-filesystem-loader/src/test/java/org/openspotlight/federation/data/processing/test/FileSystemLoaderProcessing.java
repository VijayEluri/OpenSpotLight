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

package org.openspotlight.federation.data.processing.test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.openspotlight.federation.data.processing.test.ConfigurationExamples.createOslValidConfiguration;
import static org.openspotlight.federation.data.util.ConfigurationNodes.findAllNodesOfType;

import java.util.Set;

import org.junit.Test;
import org.openspotlight.federation.data.impl.Bundle;
import org.openspotlight.federation.data.impl.Configuration;
import org.openspotlight.federation.data.impl.Repository;
import org.openspotlight.federation.data.impl.StreamArtifact;
import org.openspotlight.federation.data.load.ArtifactLoaderGroup;
import org.openspotlight.federation.data.load.FileSystemArtifactLoader;
import org.openspotlight.federation.data.processing.BundleProcessorManager;
import org.openspotlight.federation.data.processing.BundleProcessor.GraphContext;
import org.openspotlight.graph.SLGraph;
import org.openspotlight.graph.SLGraphSession;

@SuppressWarnings("all")
public class FileSystemLoaderProcessing {
    
    public static Configuration loadAllFilesFromThisConfiguration(
            final Configuration configuration) throws Exception {
        final ArtifactLoaderGroup group = new ArtifactLoaderGroup(
                new FileSystemArtifactLoader());
        final Set<Bundle> bundles = findAllNodesOfType(configuration,
                Bundle.class);
        for (final Bundle bundle : bundles) {
            group.loadArtifactsFromMappings(bundle);
            
        }
        return configuration;
        
    }
    

    
    @Test
    public void shouldLoadAllArtifactsFromOslSourceCode() throws Exception {
        final Configuration configuration = this
                .loadAllFilesFromThisConfiguration(createOslValidConfiguration("FileSystemLoaderProcessing"));
        final Set<Bundle> bundles = findAllNodesOfType(configuration,
                Bundle.class);
        for (final Bundle bundle : bundles) {
            assertThat(bundle.getStreamArtifacts().size() > 0, is(true));
        }
    }
    
    @Test
    public void shouldProcessAllValidOslSourceCode() throws Exception {
        final Configuration configuration = this
                .loadAllFilesFromThisConfiguration(createOslValidConfiguration("FileSystemLoaderProcessing"));
        final SLGraph graph = mock(SLGraph.class);
        final SLGraphSession session = mock(SLGraphSession.class);
        when(graph.openSession()).thenReturn(session);
        
        final BundleProcessorManager manager = new BundleProcessorManager(graph);
        final GraphContext graphContext = mock(GraphContext.class);
        final Set<StreamArtifact> artifacts = findAllNodesOfType(configuration,
                StreamArtifact.class);
        final Repository repository = configuration
                .getRepositoryByName("OSL Project");
        manager.processRepository(repository);
        assertThat(LogPrinterBundleProcessor.count.get(), is(artifacts.size()));
    }
    
}
