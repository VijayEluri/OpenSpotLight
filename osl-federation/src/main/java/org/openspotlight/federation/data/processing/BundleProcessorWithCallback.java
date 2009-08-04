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

import org.openspotlight.federation.data.impl.Artifact;

/**
 * This interface will change a little bit the behavior of an
 * {@link BundleProcessor} implementation. When a bundle processor implement
 * this interface, the {@link BundleProcessor.GraphContext} used on a processing
 * task will be create for each artifact processing.
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * 
 * @param <T>
 *            artifact type
 */
public interface BundleProcessorWithCallback<T extends Artifact> extends
        BundleProcessor<T> {
    
    /**
     * Callback method to notify that the target artifact is finalized.
     * 
     * @param targetArtifact
     *            the artifact to be processed
     * @param bundleProcessingGroup
     *            with lists of all processed attributes and so on
     * @param graphContext
     *            with all convenient object for graph manipulation
     * @param returnStatus
     *            the {@link BundleProcessor.ProcessingAction} returned by
     *            {@link BundleProcessor#processArtifact(Artifact, BundleProcessingGroup, GraphContext)
     *            processing method}
     */
    public void artifactProcessingFinalized(T targetArtifact,
            BundleProcessingGroup<T> bundleProcessingGroup,
            GraphContext graphContext, ProcessingAction returnStatus);
    
    /**
     * Callback method to notify that the target artifact is about to be
     * processed.
     * 
     * @param targetArtifact
     *            the artifact to be processed
     * @param bundleProcessingGroup
     *            with lists of all processed attributes and so on
     * @param graphContext
     *            with all convenient object for graph manipulation
     * @throws BundleProcessingNonFatalException
     *             if a error on the current artifact has happened
     * @throws BundleProcessingFatalException
     *             if a fatal error has happened
     */
    public void artifactProcessingStarted(T targetArtifact,
            BundleProcessingGroup<T> bundleProcessingGroup,
            GraphContext graphContext)
            throws BundleProcessingNonFatalException,
            BundleProcessingFatalException;
    
}
