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

	private final Object lock;

	/** The internal query. */
	private final SLQueryTextInternal query;

	/**
	 * Instantiates a new SLQueryTextImpl.
	 * 
	 * @param session
	 *            the session
	 * @param treeSession
	 *            the tree session
	 * @param textQuery
	 *            the text query
	 */
	public SLQueryTextImpl(final SLGraphSession session,
			final SLPersistentTreeSession treeSession,
			final SLQueryTextInternal textQuery) {
		super(session, treeSession);
		this.lock = session.getLockObject();
		this.query = textQuery;
	}

	/**
	 * {@inheritDoc}
	 */
	public SLQueryResult execute(final Collection<SLNode> inputNodes,
			final Map<String, ?> variableValues)
			throws SLInvalidQueryElementException, SLQueryException,
			SLInvalidQuerySyntaxException {
		synchronized (this.lock) {
			return this.execute(SLQuerySupport.getNodeIDs(inputNodes),
					variableValues, SortMode.NOT_SORTED, false, null, null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SLQueryResult execute(final Collection<SLNode> inputNodes,
			final Map<String, ?> variableValues, final Integer limit,
			final Integer offset) throws SLInvalidQueryElementException,
			SLQueryException, SLInvalidQuerySyntaxException {
		synchronized (this.lock) {
			return this.execute(SLQuerySupport.getNodeIDs(inputNodes),
					variableValues, SortMode.NOT_SORTED, false, limit, offset);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SLQueryResult execute(final Collection<SLNode> inputNodes,
			final Map<String, ?> variableValues, final SortMode sortMode,
			final boolean showSLQL) throws SLInvalidQueryElementException,
			SLQueryException, SLInvalidQuerySyntaxException {
		synchronized (this.lock) {
			return this.execute(SLQuerySupport.getNodeIDs(inputNodes),
					variableValues, SortMode.NOT_SORTED, false, null, null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SLQueryResult execute(final Collection<SLNode> inputNodes,
			final Map<String, ?> variableValues, final SortMode sortMode,
			final boolean showSLQL, final Integer limit, final Integer offset)
			throws SLInvalidQueryElementException, SLQueryException,
			SLInvalidQuerySyntaxException {
		synchronized (this.lock) {
			return this.execute(SLQuerySupport.getNodeIDs(inputNodes),
					variableValues, SortMode.NOT_SORTED, false, limit, offset);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SLQueryResult execute(final Map<String, ?> variableValues)
			throws SLInvalidQueryElementException, SLQueryException,
			SLInvalidQuerySyntaxException {
		synchronized (this.lock) {
			return this.execute((String[]) null, variableValues,
					SortMode.NOT_SORTED, false, null, null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SLQueryResult execute(final Map<String, ?> variableValues,
			final Integer limit, final Integer offset)
			throws SLInvalidQueryElementException, SLQueryException,
			SLInvalidQuerySyntaxException {
		synchronized (this.lock) {
			return this.execute((String[]) null, variableValues,
					SortMode.NOT_SORTED, false, limit, offset);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SLQueryResult execute(final Map<String, ?> variableValues,
			final SortMode sortMode, final boolean showSLQL)
			throws SLInvalidQueryElementException, SLQueryException,
			SLInvalidQuerySyntaxException {
		synchronized (this.lock) {
			return this.execute((String[]) null, variableValues, sortMode,
					showSLQL, null, null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SLQueryResult execute(final Map<String, ?> variableValues,
			final SortMode sortMode, final boolean showSLQL,
			final Integer limit, final Integer offset)
			throws SLInvalidQueryElementException, SLQueryException,
			SLInvalidQuerySyntaxException {
		synchronized (this.lock) {
			return this.execute((String[]) null, variableValues, sortMode,
					showSLQL, limit, offset);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SLQueryResult execute(final String[] inputNodesIDs,
			final Map<String, ?> variableValues)
			throws SLInvalidQueryElementException, SLQueryException,
			SLInvalidQuerySyntaxException {
		synchronized (this.lock) {
			return this.execute(inputNodesIDs, variableValues,
					SortMode.NOT_SORTED, false, null, null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SLQueryResult execute(final String[] inputNodesIDs,
			final Map<String, ?> variableValues, final Integer limit,
			final Integer offset) throws SLInvalidQueryElementException,
			SLQueryException, SLInvalidQuerySyntaxException {
		synchronized (this.lock) {
			return this.execute(inputNodesIDs, variableValues,
					SortMode.NOT_SORTED, false, limit, offset);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SLQueryResult execute(final String[] inputNodesIDs,
			final Map<String, ?> variableValues, final SortMode sortMode,
			final boolean showSLQL) throws SLInvalidQueryElementException,
			SLInvalidQuerySyntaxException, SLQueryException {
		synchronized (this.lock) {
			return this.execute(inputNodesIDs, variableValues, sortMode,
					showSLQL, null, null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SLQueryResult execute(final String[] inputNodesIDs,
			final Map<String, ?> variableValues, final SortMode sortMode,
			final boolean showSLQL, final Integer limit, final Integer offset)
			throws SLInvalidQueryElementException, SLQueryException,
			SLInvalidQuerySyntaxException {
		synchronized (this.lock) {
			return this.query.execute(this.session, variableValues,
					inputNodesIDs, sortMode, showSLQL, limit, offset);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SLQueryResult execute(final String[] inputNodesIDs,
			final SortMode sortMode, final boolean showSLQL,
			final Integer limit, final Integer offset)
			throws SLInvalidQueryElementException, SLQueryException,
			SLInvalidQuerySyntaxException {
		synchronized (this.lock) {
			return this.execute(inputNodesIDs, null, sortMode, showSLQL, limit,
					offset);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SLQueryResult executeTarget() throws SLInvalidQueryElementException,
			SLInvalidQuerySyntaxException, SLQueryException {
		synchronized (this.lock) {
			return this.executeTarget(SortMode.NOT_SORTED, false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SLQueryResult executeTarget(final SortMode sortMode,
			final boolean showSLQL) throws SLInvalidQueryElementException,
			SLInvalidQuerySyntaxException, SLQueryException {
		synchronized (this.lock) {
			if (this.query.getTarget() != null) {
				return this.query.getTarget().execute(this.session, null, null,
						sortMode, showSLQL, null, null);
			}
			return new SLQueryResultImpl((SLNode[]) null, null);
		}
	}

	public Object getLockObject() {
		return this.lock;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getOutputModelName() {
		synchronized (this.lock) {
			return this.query.getOutputModelName();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<SLQLVariable> getVariables() {
		synchronized (this.lock) {
			return this.query.getVariables();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasOutputModel() {
		synchronized (this.lock) {
			return this.query.hasOutputModel();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasTarget() {
		synchronized (this.lock) {
			return this.query.hasTarget();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasVariables() {
		synchronized (this.lock) {
			return this.query.hasVariables();
		}
	}
}
