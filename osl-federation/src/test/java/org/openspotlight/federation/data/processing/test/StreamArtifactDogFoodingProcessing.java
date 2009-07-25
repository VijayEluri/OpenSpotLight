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
import static org.openspotlight.federation.data.util.ConfiguratonNodes.findAllNodesOfType;

import java.io.File;
import java.util.Set;

import org.junit.Test;
import org.openspotlight.federation.data.impl.ArtifactMapping;
import org.openspotlight.federation.data.impl.Bundle;
import org.openspotlight.federation.data.impl.Configuration;
import org.openspotlight.federation.data.impl.Included;
import org.openspotlight.federation.data.impl.JavaBundle;
import org.openspotlight.federation.data.impl.Project;
import org.openspotlight.federation.data.impl.Repository;
import org.openspotlight.federation.data.load.ArtifactLoaderGroup;
import org.openspotlight.federation.data.load.FileSystemArtifactLoader;
import org.openspotlight.federation.data.load.XmlConfigurationManager;

@SuppressWarnings("all")
public class StreamArtifactDogFoodingProcessing {
    
    private Configuration createValidConfiguration() throws Exception {
        final String basePath = new File("../").getCanonicalPath() + "/";
        final Configuration configuration = new Configuration();
        final Repository oslRepository = new Repository(configuration,
                "OSL Project");
        oslRepository.setActive(true);
        final Project oslRootProject = new Project(oslRepository,
                "OSL Root Project");
        oslRootProject.setActive(true);
        final Project oslCommonsProject = new Project(oslRootProject,
                "OSL Commons Library");
        oslCommonsProject.setActive(true);
        final Bundle oslCommonsJavaSourceBundle = new Bundle(oslCommonsProject,
                "java source for OSL Bundle");
        oslCommonsJavaSourceBundle.setActive(true);
        oslCommonsJavaSourceBundle.setInitialLookup(basePath);
        final ArtifactMapping oslCommonsArtifactMapping = new ArtifactMapping(
                oslCommonsJavaSourceBundle, "osl-common/");
        final Included oslCommonsIncludedJavaFilesForSrcMainJava = new Included(
                oslCommonsArtifactMapping, "src/main/java/**/*.java");
        final Included oslCommonsIncludedJavaFilesForSrcTestJava = new Included(
                oslCommonsArtifactMapping, "src/test/java/**/*.java");
        
        final Project oslFederationProject = new Project(oslRootProject,
                "OSL Federation Library");
        oslFederationProject.setActive(true);
        final Bundle oslFederationJavaSourceBundle = new JavaBundle(
                oslFederationProject, "java source for OSL Bundle");
        oslFederationJavaSourceBundle.setActive(true);
        
        oslFederationJavaSourceBundle.setInitialLookup(basePath);
        final ArtifactMapping oslFederationArtifactMapping = new ArtifactMapping(
                oslFederationJavaSourceBundle, "osl-federation/");
        final Included oslFederationIncludedJavaFilesForSrcMainJava = new Included(
                oslFederationArtifactMapping, "src/main/java/**/*.java");
        final Included oslFederationIncludedJavaFilesForSrcTestJava = new Included(
                oslFederationArtifactMapping, "src/test/java/**/*.java");
        
        final Project oslGraphProject = new Project(oslRootProject,
                "OSL Graph Library");
        oslGraphProject.setActive(true);
        final Bundle oslGraphJavaSourceBundle = new JavaBundle(oslGraphProject,
                "java source for OSL Bundle");
        oslGraphJavaSourceBundle.setActive(true);
        oslGraphJavaSourceBundle.setInitialLookup(basePath);
        final ArtifactMapping oslGraphArtifactMapping = new ArtifactMapping(
                oslGraphJavaSourceBundle, "osl-graph/");
        final Included oslGraphIncludedJavaFilesForSrcMainJava = new Included(
                oslGraphArtifactMapping, "src/main/java/**/*.java");
        new File("./target/test-data/StreamArtifactDogFoodingProcessing/")
                .mkdirs();
        final Included oslGraphIncludedJavaFilesForSrcTestJava = new Included(
                oslGraphArtifactMapping, "src/test/java/**/*.java");
        
        return configuration;
    }
    
    @Test
    public void shouldCreateValidXmlConfigurationForOslSourceCode()
            throws Exception {
        final XmlConfigurationManager configurationManager = new XmlConfigurationManager(
                "./target/test-data/StreamArtifactDogFoodingProcessing/dogfooding-osl-configuration.xml");
        final Configuration configuration = this.createValidConfiguration();
        configurationManager.save(configuration);
    }
    
    @Test
    public void shouldLoadAndLogAllArtifactsFromOslSourceCode()
            throws Exception {
        final ArtifactLoaderGroup group = new ArtifactLoaderGroup(
                new FileSystemArtifactLoader());
        final Configuration configuration = this.createValidConfiguration();
        final Set<Bundle> bundles = findAllNodesOfType(configuration,
                Bundle.class);
        for (final Bundle bundle : bundles) {
            group.loadArtifactsFromMappings(bundle);
            if (bundle instanceof JavaBundle) {
                assertThat(bundle.getStreamArtifacts().size() > 0, is(true));
            }
        }
        
    }
}
