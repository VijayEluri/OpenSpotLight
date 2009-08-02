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

import org.openspotlight.common.exception.SLRuntimeException;
import org.openspotlight.graph.persistence.SLPersistentNode;

/**
 * The Class SLContextImpl.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLContextImpl implements SLContext {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The session. */
	private SLGraphSession session;
	
	/** The root node. */
	private SLNode rootNode;
	
	/**
	 * Instantiates a new sL context impl.
	 * 
	 * @param session the session
	 * @param contextRootPersistentNode the context root persistent node
	 * @param eventPoster the event poster
	 */
	public SLContextImpl(SLGraphSession session, SLPersistentNode contextRootPersistentNode, SLGraphSessionEventPoster eventPoster) {
		this.session = session;
		this.rootNode = new SLNodeImpl(this, null, contextRootPersistentNode, eventPoster);
	}

	//@Override
	/* (non-Javadoc)
	 * @see org.openspotlight.graph.SLContext#getSession()
	 */
	public SLGraphSession getSession() {
		return session;
	}

	//@Override
	/* (non-Javadoc)
	 * @see org.openspotlight.graph.SLContext#getID()
	 */
	public Long getID() throws SLGraphSessionException {
		return new Long(rootNode.getName());
	}

	//@Override
	/* (non-Javadoc)
	 * @see org.openspotlight.graph.SLContext#getRootNode()
	 */
	public SLNode getRootNode() throws SLGraphSessionException {
		return rootNode;
	}
	
	//@Override
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		try {
			if (obj == null) return false;
			SLContext context = (SLContext) obj;
			return getID().equals(context.getID());
		}
		catch (SLGraphSessionException e) {
			throw new SLRuntimeException("Error on attempt to execute SLContextImpl.equals() method.", e);
		}
	}
}
