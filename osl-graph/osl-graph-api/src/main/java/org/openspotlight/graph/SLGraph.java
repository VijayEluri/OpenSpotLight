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
 * OpenSpotLight - Plataforma de Governança de TI de Código Aberto 
 *
 * Direitos Autorais Reservados (c) 2009, CARAVELATECH CONSULTORIA E TECNOLOGIA 
 * EM INFORMATICA LTDA ou como contribuidores terceiros indicados pela etiqueta 
 * @author ou por expressa atribuição de direito autoral declarada e atribuída pelo autor.
 * Todas as contribuições de terceiros estão distribuídas sob licença da
 * CARAVELATECH CONSULTORIA E TECNOLOGIA EM INFORMATICA LTDA. 
 * 
 * Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob os 
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free Software 
 * Foundation. 
 * 
 * Este programa é distribuído na expectativa de que seja útil, porém, SEM NENHUMA 
 * GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU ADEQUAÇÃO A UMA
 * FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral Menor do GNU para mais detalhes.  
 * 
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto com este
 * programa; se não, escreva para: 
 * Free Software Foundation, Inc. 
 * 51 Franklin Street, Fifth Floor 
 * Boston, MA  02110-1301  USA
 */
package org.openspotlight.graph;

import org.openspotlight.graph.persistence.SLPersistentTreeException;
import org.openspotlight.security.idm.AuthenticatedUser;
import org.openspotlight.security.idm.User;

/**
 * The Interface SLGraph.
 * 
 * @author Vitor Hugo Chagas
 */
public interface SLGraph {

    /**
     * The Enum GraphState.
     * 
     * @author porcelli
     */
    public enum GraphState {

        /** The OPENED. */
        OPENED,

        /** The SHUTDOWN. */
        SHUTDOWN;
    }

    /**
     * Open repository session.
     * 
     * @param user the user
     * @param repositoryName the repository name
     * @return the sL graph session
     * @throws SLGraphException the SL graph exception
     * @throws SLInvalidCredentialsException the SL invalid credentials exception
     */
    public SLGraphSession openSession( AuthenticatedUser user,
                                       String repositoryName ) throws SLGraphException, SLInvalidCredentialException;

    /**
     * Open default repository session.
     * 
     * @param user the user
     * @return the sL graph session
     * @throws SLGraphException the SL graph exception
     * @throws SLInvalidCredentialsException the SL invalid credentials exception
     */
    public SLGraphSession openSession( AuthenticatedUser user ) throws SLGraphException, SLInvalidCredentialException;

    /**
     * Shutdown.
     */
    public void shutdown();

    /**
     * Runs the garbage collector on default repository.
     * 
     * @param user the user
     * @throws SLPersistentTreeException the SL persistent tree exception
     * @throws SLInvalidCredentialsException the SL invalid credentials exception
     */
    public void gc( AuthenticatedUser user ) throws SLPersistentTreeException, SLInvalidCredentialException;

    /**
     * Runs the garbage collector.
     * 
     * @param user the user
     * @param repositoryName the repository name
     * @throws SLPersistentTreeException the SL persistent tree exception
     * @throws SLInvalidCredentialsException the SL invalid credentials exception
     */
    public void gc( AuthenticatedUser user,
                    String repositoryName ) throws SLPersistentTreeException, SLInvalidCredentialException;

    /**
     * Gets the graph state.
     * 
     * @return the graph state
     */
    public GraphState getGraphState();

    /**
     * Gets the user.
     * 
     * @return the user
     */
    public User getUser();
}
