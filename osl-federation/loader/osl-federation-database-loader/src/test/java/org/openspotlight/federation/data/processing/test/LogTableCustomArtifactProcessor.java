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

import org.openspotlight.federation.data.impl.Artifact;
import org.openspotlight.federation.data.impl.Column;
import org.openspotlight.federation.data.impl.CustomArtifact;
import org.openspotlight.federation.data.impl.TableArtifact;
import org.openspotlight.federation.data.processing.BundleProcessingFatalException;
import org.openspotlight.federation.data.processing.BundleProcessingNonFatalException;
import org.openspotlight.federation.data.processing.CustomArtifactBundleProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public class LogTableCustomArtifactProcessor implements
        CustomArtifactBundleProcessor {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public void globalProcessingFinalized(
            final BundleProcessingGroup<? extends Artifact> bundleProcessingGroup,
            final GraphContext graphContext) {
        // nothing to do
        
    }
    
    public ProcessingStartAction globalProcessingStarted(
            final BundleProcessingGroup<CustomArtifact> bundleProcessingGroup,
            final GraphContext graphContext)
            throws BundleProcessingFatalException {
        
        return ProcessingStartAction.PROCESS_EACH_ONE_NEW;
    }
    
    public ProcessingAction processArtifact(
            final CustomArtifact targetArtifact,
            final BundleProcessingGroup<CustomArtifact> bundleProcessingGroup,
            final GraphContext graphContext)
            throws BundleProcessingNonFatalException,
            BundleProcessingFatalException {
        if (targetArtifact instanceof TableArtifact) {
            final TableArtifact table = (TableArtifact) targetArtifact;
            this.logger.warn(table.getTableName());
            for (final Column c : table.getColumns()) {
                this.logger.warn("    " + c.getName());
            }
            
        }
        return ProcessingAction.ARTIFACT_PROCESSED;
    }
    
}
