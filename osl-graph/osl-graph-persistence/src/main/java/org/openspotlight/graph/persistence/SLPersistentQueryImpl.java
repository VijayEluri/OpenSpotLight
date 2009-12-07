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
package org.openspotlight.graph.persistence;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

/**
 * The Class SLPersistentQueryImpl.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLPersistentQueryImpl implements SLPersistentQuery {
	
	/** The tree session. */
	private SLPersistentTreeSession treeSession;
	
	/** The session. */
	private Session session;
	
	/** The statement. */
	private String statement;
	
	/** The type. */
	private int type;
	
	/**
	 * Instantiates a new sL persistent query impl.
	 * 
	 * @param treeSession the tree session
	 * @param session the session
	 * @param statement the statement
	 * @param type the type
	 */
	public SLPersistentQueryImpl(SLPersistentTreeSession treeSession, Session session, String statement, int type) {
		this.treeSession = treeSession;
		this.session = session;
		this.statement = statement;
		this.type = type;
	}

	//@Override
	/* (non-Javadoc)
	 * @see org.openspotlight.graph.persistence.SLPersistentQuery#getStatement()
	 */
	public String getStatement() {
		return statement;
	}

	//@Override
	/* (non-Javadoc)
	 * @see org.openspotlight.graph.persistence.SLPersistentQuery#getType()
	 */
	public int getType() {
		return type;
	}
	
	//@Override
	/* (non-Javadoc)
	 * @see org.openspotlight.graph.persistence.SLPersistentQuery#execute()
	 */
	public SLPersistentQueryResult execute() throws SLPersistentTreeSessionException {
		try {
			Workspace workspace = session.getWorkspace();
			QueryManager queryManager = workspace.getQueryManager();
			Query query = queryManager.createQuery(statement , Query.XPATH);
			QueryResult result = query.execute();
			return new SLPersistentQueryResultImpl(treeSession, result);
		}
		catch (RepositoryException e) {
			throw new SLPersistentTreeSessionException("Error on attempt to execute query.", e);
		}
	}
}


