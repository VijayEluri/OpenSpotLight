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

package org.openspotlight.federation.processing;

import org.openspotlight.federation.domain.Artifact;
import org.openspotlight.federation.domain.ArtifactSource;
import org.openspotlight.federation.domain.BundleProcessorType;
import org.openspotlight.federation.domain.GlobalSettings;
import org.openspotlight.federation.domain.Repository;
import org.openspotlight.federation.processing.internal.BundleProcessorExecution;
import org.openspotlight.jcr.provider.JcrConnectionDescriptor;
import org.openspotlight.security.idm.AuthenticatedUser;

// TODO: Auto-generated Javadoc
/**
 * The {@link BundleProcessorManager} is the class reposable to get an {@link GlobalSettings} and to process all {@link Artifact
 * artifacts} on this {@link GlobalSettings}. The {@link BundleProcessorManager} should get the {@link ArtifactSource bundle's}
 * {@link BundleProcessorType types} and find all the {@link BundleProcessor processors} for each {@link BundleProcessorType type}
 * . After all {@link BundleProcessor processors} was found, the {@link BundleProcessorManager} should distribute the processing
 * job in some threads obeying the {@link GlobalSettings#getNumberOfParallelThreads() number of threads} configured for this
 * {@link Repository}.
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 */
public enum BundleProcessorManager {

    INSTANCE;

    public void executeBundles( final AuthenticatedUser user,
                                final JcrConnectionDescriptor descriptor,
                                final GlobalSettings settings,
                                final Repository... repositories ) {
        new BundleProcessorExecution(descriptor, settings, repositories, user).execute();
    }

    public void executeBundlesInBackground( final AuthenticatedUser user,
                                            final JcrConnectionDescriptor descriptor,
                                            final GlobalSettings settings,
                                            final Repository... repositories ) {
        new Thread(new Runnable() {
            public void run() {
                new BundleProcessorExecution(descriptor, settings, repositories, user).execute();
            }
        }).start();
    }
}
