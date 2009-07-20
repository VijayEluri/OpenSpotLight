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

package org.openspotlight.federation.data.load.test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.openspotlight.common.util.Collections.setOf;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.openspotlight.common.exception.ConfigurationException;
import org.openspotlight.federation.data.impl.Bundle;
import org.openspotlight.federation.data.impl.Configuration;
import org.openspotlight.federation.data.impl.Project;
import org.openspotlight.federation.data.impl.Repository;
import org.openspotlight.federation.data.load.AbstractArtifactLoader;
import org.openspotlight.federation.data.load.ArtifactLoader;
import org.openspotlight.federation.data.test.AbstractNodeTest;

/**
 * Test for class {@link AbstractArtifactLoader}
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * 
 */
@SuppressWarnings("all")
public class AbstractArtifactLoaderTest extends AbstractNodeTest {
    
    protected ArtifactLoader artifactLoader;
    
    protected Configuration configuration;
    
    @Before
    public void createArtifactLoader() {
        this.artifactLoader = new AbstractArtifactLoader() {
            
            @Override
            protected Set<String> getAllArtifactNames(final Bundle bundle)
                    throws ConfigurationException {
                return setOf("1", "2", "3", "4", "5");
            }
            
            @Override
            protected byte[] loadArtifact(final Bundle bundle,
                    final String artifactName) throws Exception {
                return artifactName.getBytes();
            }
            
        };
    }
    
    @Before
    public void createConfiguration() {
        this.configuration = this.createSampleData();
    }
    
    @Test
    public void shouldLoadArtifacts() throws Exception {
        for (final Repository repository : this.configuration.getRepositories()) {
            for (final Project project : repository.getProjects()) {
                for (final Bundle bundle : project.getBundles()) {
                    this.artifactLoader.loadArtifactsFromMappings(bundle);
                    assertThat(bundle.getStreamArtifacts().size(), is(not(0)));
                }
            }
        }
    }
    
}
