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

import java.util.concurrent.atomic.AtomicInteger;

import org.openspotlight.federation.data.impl.StreamArtifact;
import org.openspotlight.federation.data.processing.BundleProcessingFatalException;
import org.openspotlight.federation.data.processing.BundleProcessingNonFatalException;
import org.openspotlight.federation.data.processing.StreamArtifactBundleProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example class for bundle processor.
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * 
 */
public class LogPrinterBundleProcessor implements StreamArtifactBundleProcessor {
    
    /**
     * counter to use on test
     */
    public static AtomicInteger count = new AtomicInteger(0);
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * 
     * {@inheritDoc}
     */
    public ProcessingAction processArtifact(
            final StreamArtifact targetArtifact,
            final BundleProcessingGroup<StreamArtifact> bundleProcessingGroup,
            final GraphContext graphContext)
            throws BundleProcessingNonFatalException,
            BundleProcessingFatalException {
        this.logger.warn("processing: " + targetArtifact.getRelativeName()); //$NON-NLS-1$
        count.incrementAndGet();
        return ProcessingAction.ARTIFACT_PROCESSED;
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    public ProcessingStartAction startProcessing(
            final BundleProcessingGroup<StreamArtifact> bundleProcessingGroup,
            final GraphContext graphContext)
            throws BundleProcessingFatalException {
        return ProcessingStartAction.PROCESS_EACH_ONE_NEW;
    }
    
}