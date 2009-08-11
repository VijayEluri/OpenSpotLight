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

import static org.openspotlight.common.util.Exceptions.logAndReturnNew;
import static org.openspotlight.federation.data.util.JcrNodeVisitor.withVisitor;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.jboss.dna.connector.filesystem.FileSystemSource;
import org.jboss.dna.jcr.JcrConfiguration;
import org.jboss.dna.jcr.JcrEngine;
import org.jboss.dna.jcr.SecurityContextCredentials;
import org.openspotlight.common.exception.ConfigurationException;
import org.openspotlight.federation.data.impl.ArtifactMapping;
import org.openspotlight.federation.data.impl.Bundle;
import org.openspotlight.federation.data.util.JcrNodeVisitor.NodeVisitor;

/**
 * Artifact loader that loads Artifact for file system using DNA File System
 * Connector.
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * 
 */
public class DnaFileSystemArtifactLoader extends
        AbstractArtifactLoader<DnaFileSystemArtifactLoader.LoadingContext> {
    
    /**
     * JCR visitor to fill all valid artifact names
     * 
     * @author Luiz Fernando Teston - feu.teston@caravelatech.com
     * 
     */
    protected static final class FillNamesVisitor implements NodeVisitor {
        final Set<String> names;
        
        /**
         * Constructor to initialize final fields
         * 
         * @param names
         */
        FillNamesVisitor(final Set<String> names) {
            this.names = names;
        }
        
        /**
         * 
         * {@inheritDoc}
         */
        public void visiting(final Node n) throws RepositoryException {
            this.names.add(n.getPath());
        }
        
    }
    
    /**
     * This {@link LoadingContext} will store all JCR data needed during the
     * processing, and after the processing it will shutdown all necessary
     * resources.
     * 
     * @author Luiz Fernando Teston - feu.teston@caravelatech.com
     * 
     *         FIXME starts only one configuration to improve performance
     * 
     */
    protected static final class LoadingContext {
        private static final String repositorySource = "repositorySource"; //$NON-NLS-1$
        private static final String repositoryName = "repository"; //$NON-NLS-1$
        
        private final Map<String, Session> mappingSessions = new ConcurrentHashMap<String, Session>();
        
        private final Map<String, JcrEngine> mappingEngines = new ConcurrentHashMap<String, JcrEngine>();
        
        /**
         * 
         * @param name
         * @return the jcr session
         * @throws Exception
         */
        public Session getSessionForMapping(final String name) throws Exception {
            Session session = this.mappingSessions.get(name);
            if (session == null) {
                final JcrEngine engine = this.mappingEngines.get(name);
                session = engine.getRepository(repositoryName).login(
                        new SecurityContextCredentials(
                                DefaultSecurityContext.READ_ONLY));
                this.mappingSessions.put(name, session);
            }
            return session;
        }
        
        /**
         * Setups all necessary resources.
         * 
         * @param rootPath
         * @param relativePaths
         * @throws Exception
         */
        public void setup(final String rootPath, final String... relativePaths)
                throws Exception {
            
            for (final String relative : relativePaths) {
                
                final JcrConfiguration configuration = new JcrConfiguration();
                configuration.repositorySource(repositorySource).usingClass(
                        FileSystemSource.class).setProperty(
                        "workspaceRootPath", rootPath).setProperty( //$NON-NLS-1$ 
                        "creatingWorkspacesAllowed", true).setProperty( //$NON-NLS-1$
                        "defaultWorkspaceName", relative); //$NON-NLS-1$
                
                configuration.repository(repositoryName).setSource(
                        repositorySource);
                configuration.save();
                final JcrEngine engine = configuration.build();
                engine.start();
                this.mappingEngines.put(relative, engine);
                
            }
        }
        
        /**
         * Finalizes all necessary resources
         */
        public void shutdown() {
            for (final Map.Entry<String, JcrEngine> entry : this.mappingEngines
                    .entrySet()) {
                entry.getValue().shutdown();
            }
            for (final Map.Entry<String, Session> entry : this.mappingSessions
                    .entrySet()) {
                entry.getValue().logout();
            }
        }
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    protected LoadingContext createCachedInformation() {
        return new LoadingContext();
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    protected Set<String> getAllArtifactNames(final Bundle bundle,
            final ArtifactMapping mapping, final LoadingContext context)
            throws ConfigurationException {
        try {
            final Session session = context.getSessionForMapping(mapping
                    .getRelative());
            final Set<String> names = new HashSet<String>();
            session.getRootNode().accept(
                    withVisitor(new FillNamesVisitor(names)));
            return names;
        } catch (final Exception e) {
            throw logAndReturnNew(e, ConfigurationException.class);
        }
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    protected byte[] loadArtifact(final Bundle bundle,
            final ArtifactMapping mapping, final String artifactName,
            final LoadingContext context) throws Exception {
        try {
            final Session session = context.getSessionForMapping(mapping
                    .getRelative());
            final Node node = session.getRootNode().getNode(artifactName);
            final InputStream is = node.getProperty("").getStream(); //$NON-NLS-1$
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int available;
            while ((available = is.read()) != 0) {
                baos.write(available);
            }
            return baos.toByteArray();
        } catch (final Exception e) {
            throw logAndReturnNew(e, ConfigurationException.class);
        }
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    protected void loadingStarted(final Bundle bundle,
            final LoadingContext context) throws ConfigurationException {
        
        final String initialLookup = bundle.getInitialLookup();
        final String[] relativePaths = bundle.getArtifactMappingNames()
                .toArray(new String[0]);
        try {
            context.setup(initialLookup, relativePaths);
        } catch (final Exception e) {
            throw logAndReturnNew(e, ConfigurationException.class);
        }
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    protected void loadingStopped(final Bundle bundle,
            final LoadingContext context) {
        context.shutdown();
        
    }
    
}
