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
package org.openspotlight.graph;

import static org.openspotlight.common.util.Exceptions.catchAndLog;

import org.openspotlight.common.util.AbstractFactory;
import org.openspotlight.common.util.Assertions;
import org.openspotlight.graph.SLGraphFactoryImpl.SLGraphClosingListener;
import org.openspotlight.graph.persistence.SLPersistentNode;
import org.openspotlight.graph.persistence.SLPersistentTree;
import org.openspotlight.graph.persistence.SLPersistentTreeException;
import org.openspotlight.graph.persistence.SLPersistentTreeSession;
import org.openspotlight.security.authz.Action;
import org.openspotlight.security.authz.EnforcementContext;
import org.openspotlight.security.authz.EnforcementException;
import org.openspotlight.security.authz.EnforcementResponse;
import org.openspotlight.security.authz.PolicyEnforcement;
import org.openspotlight.security.authz.graph.GraphElement;
import org.openspotlight.security.idm.AuthenticatedUser;
import org.openspotlight.security.idm.SystemUser;
import org.openspotlight.security.idm.User;
import org.openspotlight.security.idm.auth.IdentityManager;

/**
 * The Class SLGraphImpl.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLGraphImpl implements SLGraph {

    /** The tree. */
    private final SLPersistentTree       tree;

    /** The graph state. */
    private GraphState                   graphState;

    /** The listener. */
    private final SLGraphClosingListener listener;

    /** The user. */
    private final SystemUser             user;

    /** The policy enforcement. */
    private final PolicyEnforcement      policyEnforcement;

    private final IdentityManager        identityManager;

    /**
     * Instantiates a new sL graph impl.
     * 
     * @param tree the tree
     * @param listener the listener
     * @param policyEnforcement the policy enforcement
     * @param user the user
     * @param identityManager the identity manager
     * @throws SLInvalidCredentialsException the SL invalid credentials exception
     */
    public SLGraphImpl(
                        final SLPersistentTree tree, final SLGraphClosingListener listener,
                        final IdentityManager identityManager,
                        final PolicyEnforcement policyEnforcement,
                        final SystemUser user )
        throws SLInvalidCredentialException {
        Assertions.checkNotNull("tree", tree);
        Assertions.checkNotNull("identityManager", identityManager);
        Assertions.checkNotNull("policyEnforcement", policyEnforcement);
        Assertions.checkNotNull("user", user);

        if (!identityManager.isValid(user)) {
            throw new SLInvalidCredentialException("SystemUser is not valid.");
        }

        this.tree = tree;
        this.graphState = GraphState.OPENED;
        this.listener = listener;
        this.identityManager = identityManager;
        this.policyEnforcement = policyEnforcement;
        this.user = user;
    }

    /**
     * {@inheritDoc}
     */
    public void gc( AuthenticatedUser user,
                    String repositoryName ) throws SLPersistentTreeException, SLInvalidCredentialException {
        if (this.graphState != GraphState.SHUTDOWN) {

            Assertions.checkNotNull("repositoryName", repositoryName);
            Assertions.checkNotNull("user", user);

            if (!this.identityManager.isValid(user)) {
                throw new SLInvalidCredentialException("Invalid user.");
            }

            if (!hasPrivileges(user, repositoryName, Action.MANAGE)) {
                throw new SLInvalidCredentialException("User does not have privilegies to manage repository.");
            }

            final SLPersistentTreeSession treeSession = this.tree.openSession(repositoryName);
            if (SLCommonSupport.containsQueryCache(treeSession)) {
                final SLPersistentNode pNode = SLCommonSupport.getQueryCacheNode(treeSession);
                pNode.remove();
            }
            treeSession.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    public void gc( AuthenticatedUser user ) throws SLPersistentTreeException, SLInvalidCredentialException {
        this.gc(user, SLConsts.DEFAULT_REPOSITORY_NAME);
    }

    /**
     * {@inheritDoc}
     */
    public GraphState getGraphState() {
        return this.graphState;
    }

    /**
     * {@inheritDoc}
     */
    public SLGraphSession openSession( AuthenticatedUser user,
                                       String repositoryName ) throws SLGraphException, SLInvalidCredentialException {
        if (this.graphState == GraphState.SHUTDOWN) {
            throw new SLGraphException("Could not open SL graph session. Graph is already shutdown.");
        }

        Assertions.checkNotNull("repositoryName", repositoryName);
        Assertions.checkNotNull("user", user);

        if (!this.identityManager.isValid(user)) {
            throw new SLInvalidCredentialException("Invalid user.");
        }

        if (!hasPrivileges(user, repositoryName, Action.READ)) {
            throw new SLInvalidCredentialException("User does not have privilegies to access repository.");
        }

        try {
            final SLPersistentTreeSession treeSession = this.tree.openSession(repositoryName);
            final SLGraphFactory factory = AbstractFactory.getDefaultInstance(SLGraphFactory.class);
            return factory.createGraphSession(treeSession, policyEnforcement, user);
        } catch (final Exception e) {
            throw new SLGraphException("Could not open SL graph session.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public SLGraphSession openSession( AuthenticatedUser user ) throws SLGraphException, SLInvalidCredentialException {
        return this.openSession(user, SLConsts.DEFAULT_REPOSITORY_NAME);
    }

    /**
     * {@inheritDoc}
     */
    public void shutdown() {
        this.tree.shutdown();
        this.graphState = GraphState.SHUTDOWN;
        this.listener.graphClosed(this);
    }

    /**
     * {@inheritDoc}
     */
    public User getUser() {
        return user;
    }

    /**
     * Checks for privileges.
     * 
     * @param user the user
     * @param repositoryName the repository name
     * @param action the action
     * @return true, if successful
     */
    private boolean hasPrivileges( AuthenticatedUser user,
                                   String repositoryName,
                                   Action action ) {
        EnforcementContext enforcementContext = new EnforcementContext();
        enforcementContext.setAttribute("user", user);
        enforcementContext.setAttribute("graphElement", GraphElement.REPOSITORY);
        enforcementContext.setAttribute("repository", repositoryName);
        enforcementContext.setAttribute("action", action);

        try {
            EnforcementResponse response = policyEnforcement.checkAccess(enforcementContext);
            if (response.equals(EnforcementResponse.GRANTED)) {
                return true;
            }
            return false;
        } catch (EnforcementException e) {
            catchAndLog(e);
            return false;
        }
    }
}
