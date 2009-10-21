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
package org.openspotlight.graph.query;

import java.util.Collection;

import org.openspotlight.graph.SLGraphSession;
import org.openspotlight.graph.SLNode;
import org.openspotlight.graph.persistence.SLPersistentTreeSession;
import org.openspotlight.graph.query.SLQuery.SortMode;

/**
 * The Class AbstractSLQuery. Basic implemenation for SLQueries (api or text).
 * 
 * @author porcelli
 */
public abstract class AbstractSLQuery {

    /** The session. */
    protected SLGraphSession          session;

    /** The tree session. */
    protected SLPersistentTreeSession treeSession;

    /**
     * Instantiates a new sL query impl.
     * 
     * @param session the session
     * @param treeSession the tree session
     */
    public AbstractSLQuery(
                            SLGraphSession session, SLPersistentTreeSession treeSession ) {
        this.session = session;
        this.treeSession = treeSession;
    }

    /**
     * Execute.
     * 
     * @return the sL query result
     * @throws SLInvalidQuerySyntaxException the SL invalid query syntax exception
     * @throws SLInvalidQueryElementException the SL invalid query element exception
     * @throws SLQueryException the SL query exception
     */
    public SLQueryResult execute() throws SLInvalidQuerySyntaxException, SLInvalidQueryElementException, SLQueryException {
        return execute((String[])null, SortMode.NOT_SORTED, false, null, null);
    }

    /**
     * Execute.
     * 
     * @param limit the limit
     * @param offset the offset
     * @return the sL query result
     * @throws SLInvalidQuerySyntaxException the SL invalid query syntax exception
     * @throws SLInvalidQueryElementException the SL invalid query element exception
     * @throws SLQueryException the SL query exception
     */
    public SLQueryResult execute( Integer limit,
                                  Integer offset )
        throws SLInvalidQuerySyntaxException, SLInvalidQueryElementException, SLQueryException {
        return execute((String[])null, SortMode.NOT_SORTED, false, limit, offset);
    }

    /**
     * Execute.
     * 
     * @param inputNodes the input nodes
     * @return the sL query result
     * @throws SLInvalidQuerySyntaxException the SL invalid query syntax exception
     * @throws SLInvalidQueryElementException the SL invalid query element exception
     * @throws SLQueryException the SL query exception
     */
    public SLQueryResult execute( Collection<SLNode> inputNodes )
        throws SLInvalidQuerySyntaxException, SLInvalidQueryElementException, SLQueryException {
        return execute(SLQuerySupport.getNodeIDs(inputNodes), SortMode.NOT_SORTED, false, null, null);
    }

    /**
     * Execute.
     * 
     * @param inputNodes the input nodes
     * @param limit the limit
     * @param offset the offset
     * @return the sL query result
     * @throws SLInvalidQuerySyntaxException the SL invalid query syntax exception
     * @throws SLInvalidQueryElementException the SL invalid query element exception
     * @throws SLQueryException the SL query exception
     */
    public SLQueryResult execute( Collection<SLNode> inputNodes,
                                  Integer limit,
                                  Integer offset )
        throws SLInvalidQuerySyntaxException, SLInvalidQueryElementException, SLQueryException {
        return execute(SLQuerySupport.getNodeIDs(inputNodes), SortMode.NOT_SORTED, false, limit, offset);
    }

    /**
     * Execute.
     * 
     * @param inputNodesIDs the input nodes i ds
     * @return the sL query result
     * @throws SLInvalidQuerySyntaxException the SL invalid query syntax exception
     * @throws SLInvalidQueryElementException the SL invalid query element exception
     * @throws SLQueryException the SL query exception
     */
    public SLQueryResult execute( String[] inputNodesIDs )
        throws SLInvalidQuerySyntaxException, SLInvalidQueryElementException, SLQueryException {
        return execute(inputNodesIDs, SortMode.NOT_SORTED, false, null, null);
    }

    /**
     * Execute.
     * 
     * @param inputNodesIDs the input nodes i ds
     * @param limit the limit
     * @param offset the offset
     * @return the sL query result
     * @throws SLInvalidQuerySyntaxException the SL invalid query syntax exception
     * @throws SLInvalidQueryElementException the SL invalid query element exception
     * @throws SLQueryException the SL query exception
     */
    public SLQueryResult execute( String[] inputNodesIDs,
                                  Integer limit,
                                  Integer offset )
        throws SLInvalidQuerySyntaxException, SLInvalidQueryElementException, SLQueryException {
        return execute(inputNodesIDs, SortMode.NOT_SORTED, false, limit, offset);
    }

    /**
     * Execute.
     * 
     * @param sortMode the sort mode
     * @param showSLQL the show slql
     * @return the sL query result
     * @throws SLInvalidQuerySyntaxException the SL invalid query syntax exception
     * @throws SLInvalidQueryElementException the SL invalid query element exception
     * @throws SLQueryException the SL query exception
     */
    public SLQueryResult execute( SortMode sortMode,
                                  boolean showSLQL )
        throws SLInvalidQuerySyntaxException, SLInvalidQueryElementException, SLQueryException {
        return execute((String[])null, sortMode, showSLQL, null, null);
    }

    /**
     * Execute.
     * 
     * @param sortMode the sort mode
     * @param showSLQL the show slql
     * @param limit the limit
     * @param offset the offset
     * @return the sL query result
     * @throws SLInvalidQuerySyntaxException the SL invalid query syntax exception
     * @throws SLInvalidQueryElementException the SL invalid query element exception
     * @throws SLQueryException the SL query exception
     */
    public SLQueryResult execute( SortMode sortMode,
                                  boolean showSLQL,
                                  Integer limit,
                                  Integer offset )
        throws SLInvalidQuerySyntaxException, SLInvalidQueryElementException, SLQueryException {
        return execute((String[])null, sortMode, showSLQL, limit, offset);
    }

    /**
     * Execute.
     * 
     * @param inputNodes the input nodes
     * @param sortMode the sort mode
     * @param showSLQL the show slql
     * @return the sL query result
     * @throws SLInvalidQuerySyntaxException the SL invalid query syntax exception
     * @throws SLInvalidQueryElementException the SL invalid query element exception
     * @throws SLQueryException the SL query exception
     */
    public SLQueryResult execute( Collection<SLNode> inputNodes,
                                  SortMode sortMode,
                                  boolean showSLQL )
        throws SLInvalidQuerySyntaxException, SLInvalidQueryElementException, SLQueryException {
        return execute(SLQuerySupport.getNodeIDs(inputNodes), SortMode.NOT_SORTED, false, null, null);
    }

    /**
     * Execute.
     * 
     * @param inputNodes the input nodes
     * @param sortMode the sort mode
     * @param showSLQL the show slql
     * @param limit the limit
     * @param offset the offset
     * 
     * @return the sL query result
     * 
     * @throws SLInvalidQuerySyntaxException the SL invalid query syntax exception
     * @throws SLInvalidQueryElementException the SL invalid query element exception
     * @throws SLQueryException the SL query exception
     */
    public SLQueryResult execute( Collection<SLNode> inputNodes,
                                  SortMode sortMode,
                                  boolean showSLQL,
                                  Integer limit,
                                  Integer offset )
        throws SLInvalidQuerySyntaxException, SLInvalidQueryElementException, SLQueryException {
        return execute(SLQuerySupport.getNodeIDs(inputNodes), SortMode.NOT_SORTED, false, limit, offset);
    }

    /**
     * Execute.
     * 
     * @param inputNodesIDs the input nodes i ds
     * @param sortMode the sort mode
     * @param showSLQL the show slql
     * @return the sL query result
     * @throws SLInvalidQuerySyntaxException the SL invalid query syntax exception
     * @throws SLInvalidQueryElementException the SL invalid query element exception
     * @throws SLQueryException the SL query exception
     */
    public SLQueryResult execute( String[] inputNodesIDs,
                                  SortMode sortMode,
                                  boolean showSLQL )
        throws SLInvalidQuerySyntaxException, SLInvalidQueryElementException, SLQueryException {
        return execute(inputNodesIDs, SortMode.NOT_SORTED, false, null, null);
    }

    public abstract SLQueryResult execute( String[] inputNodesIDs,
                                           SortMode sortMode,
                                           boolean showSLQL,
                                           Integer limit,
                                           Integer offset )
        throws SLInvalidQuerySyntaxException, SLInvalidQueryElementException, SLQueryException;

}
