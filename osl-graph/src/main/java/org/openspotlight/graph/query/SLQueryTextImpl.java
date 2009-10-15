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
import java.util.Map;

import org.apache.log4j.Logger;
import org.openspotlight.graph.SLGraphSession;
import org.openspotlight.graph.SLNode;
import org.openspotlight.graph.persistence.SLPersistentTreeSession;

/**
 * The Class SLQueryTextImpl.
 * 
 * @author porcelli
 */
public class SLQueryTextImpl extends AbstractSLQuery implements SLQueryText {

    /** The Constant LOGGER. */
    static final Logger LOGGER = Logger.getLogger(SLQueryTextImpl.class);

    private SLQueryTextInternal   query;

    public SLQueryTextImpl(
                            SLGraphSession session, SLPersistentTreeSession treeSession, SLQueryTextInternal textQuery ) {
        super(session, treeSession);
        this.query = textQuery;
    }

    public String getOutputModelName() {
        return query.getOutputModelName();
    }

    public Collection<SLQLVariable> getVariables() {
        return query.getVariables();
    }

    public boolean hasOutputModel() {
        return query.hasOutputModel();
    }

    public boolean hasTarget() {
        return query.hasTarget();
    }

    public boolean hasVariables() {
        return query.hasVariables();
    }

    @Override
    public SLQueryResult execute( String[] inputNodesIDs,
                                  SortMode sortMode,
                                  boolean showSLQL )
        throws SLInvalidQueryElementException, SLQueryException, SLInvalidQuerySyntaxException {
        return execute(inputNodesIDs, null, sortMode, showSLQL);
    }

    public SLQueryResult execute( Map<String, ?> variableValues )
        throws SLInvalidQueryElementException, SLQueryException, SLInvalidQuerySyntaxException {
        return execute((String[])null, variableValues, SortMode.NOT_SORTED, false);
    }

    public SLQueryResult execute( Collection<SLNode> inputNodes,
                                  Map<String, ?> variableValues )
        throws SLInvalidQueryElementException, SLQueryException, SLInvalidQuerySyntaxException {
        return execute(SLQuerySupport.getNodeIDs(inputNodes), variableValues, SortMode.NOT_SORTED, false);
    }

    public SLQueryResult execute( String[] inputNodesIDs,
                                  Map<String, ?> variableValues )
        throws SLInvalidQueryElementException, SLQueryException, SLInvalidQuerySyntaxException {
        return execute(inputNodesIDs, variableValues, SortMode.NOT_SORTED, false);
    }

    public SLQueryResult execute( Map<String, ?> variableValues,
                                  SortMode sortMode,
                                  boolean showSLQL )
        throws SLInvalidQueryElementException, SLQueryException, SLInvalidQuerySyntaxException {
        return execute((String[])null, variableValues, sortMode, showSLQL);
    }

    public SLQueryResult execute( Collection<SLNode> inputNodes,
                                  Map<String, ?> variableValues,
                                  SortMode sortMode,
                                  boolean showSLQL )
        throws SLInvalidQueryElementException, SLQueryException, SLInvalidQuerySyntaxException {
        return execute(SLQuerySupport.getNodeIDs(inputNodes), variableValues, SortMode.NOT_SORTED, false);
    }

    public SLQueryResult execute( String[] inputNodesIDs,
                                  Map<String, ?> variableValues,
                                  SortMode sortMode,
                                  boolean showSLQL )
        throws SLInvalidQueryElementException, SLQueryException, SLInvalidQuerySyntaxException {
        return query.execute(session, variableValues, inputNodesIDs);
    }

    public SLQueryResult executeTarget() throws SLInvalidQueryElementException, SLInvalidQuerySyntaxException, SLQueryException {
        return executeTarget(SortMode.NOT_SORTED, false);
    }

    public SLQueryResult executeTarget( SortMode sortMode,
                                        boolean showSLQL )
        throws SLInvalidQueryElementException, SLInvalidQuerySyntaxException, SLQueryException {
        if (query.getTarget() != null) {
            return query.getTarget().execute(session, null, null);
        }
        return new SLQueryResultImpl(null);
    }
}
