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

import java.io.File;

import org.openspotlight.federation.data.impl.ArtifactMapping;
import org.openspotlight.federation.data.impl.Bundle;
import org.openspotlight.federation.data.impl.BundleProcessorType;
import org.openspotlight.federation.data.impl.Configuration;
import org.openspotlight.federation.data.impl.DbBundle;
import org.openspotlight.federation.data.impl.Included;
import org.openspotlight.federation.data.impl.JavaBundle;
import org.openspotlight.federation.data.impl.Project;
import org.openspotlight.federation.data.impl.Repository;
import org.openspotlight.federation.data.load.db.DatabaseType;

/**
 * Class with some example valid configurations
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * 
 */
@SuppressWarnings("all")
public class ConfigurationExamples {
    public static Configuration createDbConfiguration() throws Exception {
        final Configuration configuration = new Configuration();
        final Repository h2Repository = new Repository(configuration,
                "H2 Repository");
        configuration.setNumberOfParallelThreads(4);
        h2Repository.setActive(true);
        final Project h2Project = new Project(h2Repository, "h2 Project");
        h2Project.setActive(true);
        final DbBundle h2Bundle = new DbBundle(h2Project, "H2 Connection");
        h2Bundle.setActive(true);
        h2Bundle.setUser("sa");
        h2Bundle.setType(DatabaseType.H2);
        h2Bundle
                .setInitialLookup("jdbc:h2:./target/test-data/DbStreamArtifactProcessing/h2/db");
        h2Bundle.setDriverClass("org.h2.Driver");
        final ArtifactMapping h2ArtifactMapping = new ArtifactMapping(h2Bundle,
                "DB/PUBLIC/");
        final Included h2IncludedTrigger = new Included(h2ArtifactMapping,
                "trigger/*");
        final Included h2IncludedProcedure = new Included(h2ArtifactMapping,
                "procedure/*");
        final Included h2IncludedTable = new Included(h2ArtifactMapping,
                "table/*");
        final Included h2IncludedFunction = new Included(h2ArtifactMapping,
                "function/*");
        final Included h2IncludedView = new Included(h2ArtifactMapping,
                "view/*");
        final Included h2IncludedIndex = new Included(h2ArtifactMapping,
                "index/*");
        final BundleProcessorType h2CommonProcessor = new BundleProcessorType(
                h2Bundle,
                "org.openspotlight.federation.data.processing.test.LogPrinterBundleProcessor");
        final BundleProcessorType h2CustomProcessor = new BundleProcessorType(
                h2Bundle,
                "org.openspotlight.federation.data.processing.test.LogTableCustomArtifactProcessor");
        
        h2CommonProcessor.setActive(true);
        h2CustomProcessor.setActive(true);
        return configuration;
    }
    
    public static Configuration createOslValidConfiguration() throws Exception {
        final String basePath = new File("../").getCanonicalPath() + "/";
        final Configuration configuration = new Configuration();
        final Repository oslRepository = new Repository(configuration,
                "OSL Project");
        configuration.setNumberOfParallelThreads(4);
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
        final BundleProcessorType oslCommonProcessor = new BundleProcessorType(
                oslCommonsJavaSourceBundle,
                "org.openspotlight.federation.data.processing.test.LogPrinterBundleProcessor");
        oslCommonProcessor.setActive(true);
        final Project oslFederationProject = new Project(oslRootProject,
                "OSL Federation Library");
        oslFederationProject.setActive(true);
        final Bundle oslFederationJavaSourceBundle = new JavaBundle(
                oslFederationProject, "java source for OSL Bundle");
        oslFederationJavaSourceBundle.setActive(true);
        final BundleProcessorType oslFederationProcessor = new BundleProcessorType(
                oslFederationJavaSourceBundle,
                "org.openspotlight.federation.data.processing.test.LogPrinterBundleProcessor");
        oslFederationProcessor.setActive(true);
        
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
        final BundleProcessorType oslGraphProcessor = new BundleProcessorType(
                oslGraphJavaSourceBundle,
                "org.openspotlight.federation.data.processing.test.LogPrinterBundleProcessor");
        oslGraphProcessor.setActive(true);
        return configuration;
    }
    
}
