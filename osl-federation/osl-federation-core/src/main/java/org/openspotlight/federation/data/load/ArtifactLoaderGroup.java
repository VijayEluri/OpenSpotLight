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

package org.openspotlight.federation.data.load;

import static org.openspotlight.common.util.Assertions.checkEachParameterNotNull;
import static org.openspotlight.common.util.Assertions.checkNotNull;

import org.openspotlight.common.exception.ConfigurationException;
import org.openspotlight.federation.domain.ArtifactSource;

/**
 * The {@link ArtifactLoaderGroup} class is itself a {@link ArtifactLoader} that groups all the valid Artifact loaders and execute
 * all of that in order.
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 */
public class ArtifactLoaderGroup implements ArtifactLoader {

    /** The artifact loaders. */
    private final ArtifactLoader[] artifactLoaders;

    /**
     * Constructor with varargs for mandatory Artifact loaders.
     * 
     * @param artifactLoaders
     */
    public ArtifactLoaderGroup(
                                final ArtifactLoader... artifactLoaders ) {
        checkEachParameterNotNull("artifactLoaders", artifactLoaders); //$NON-NLS-1$
        this.artifactLoaders = artifactLoaders;
    }

    /**
     * Executes each Artifact loader in order on the passed bundle.
     * 
     * @param bundle
     * @return a count for the artifact loading
     * @throws ConfigurationException
     */
    public ArtifactProcessingCount loadArtifactsFromMappings( final ArtifactSource bundle ) throws ConfigurationException {
        checkNotNull("bundle", bundle); //$NON-NLS-1$
        long loadCount = 0;
        long ignoreCount = 0;
        long errorCount = 0;
        for (final ArtifactLoader ArtifactLoader : this.artifactLoaders) {
            final ArtifactProcessingCount result = ArtifactLoader.loadArtifactsFromMappings(bundle);
            checkNotNull("result", result); //$NON-NLS-1$
            loadCount += result.getLoadCount();
            ignoreCount += result.getIgnoreCount();
            errorCount += result.getErrorCount();
        }
        return new ArtifactProcessingCount(loadCount, ignoreCount, errorCount);
    }
}
