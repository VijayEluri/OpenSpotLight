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

import java.util.ArrayList;
import java.util.Collection;

import org.openspotlight.SLException;
import org.openspotlight.graph.persistence.SLPersistentNode;
import org.openspotlight.graph.persistence.SLPersistentQuery;
import org.openspotlight.graph.persistence.SLPersistentQueryResult;
import org.openspotlight.graph.persistence.SLPersistentTreeSession;

/**
 * The Class SLMetadataImpl.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLMetadataImpl implements SLMetadata {
	
	/** The tree session. */
	private SLPersistentTreeSession treeSession;
	
	/**
	 * Instantiates a new sL metadata impl.
	 * 
	 * @param treeSession the tree session
	 */
	public SLMetadataImpl(SLPersistentTreeSession treeSession) {
		this.treeSession = treeSession;
	}

	//@Override
	/* (non-Javadoc)
	 * @see org.openspotlight.graph.SLMetadata#getMetaNode(java.lang.Class)
	 */
	public SLMetaNode getMetaNode(Class<? extends SLNode> nodeClass) throws SLGraphSessionException {
		try {
			SLPersistentQuery query = treeSession.createQuery("//osl/metadata/types/" + nodeClass.getName(), SLPersistentQuery.TYPE_XPATH);
			SLPersistentQueryResult result = query.execute();
			SLMetaNode metaNode = null;
			if (result.getRowCount() == 1) {
				SLPersistentNode pMetaNode = result.getNodes().iterator().next();
				metaNode = new SLMetaNodeImpl(this, pMetaNode);
			}
			return metaNode;
		}
		catch (SLException e) {
			throw new SLGraphSessionException("Error on attempt to retrieve meta node.", e);
		}
	}

	//@Override
	/* (non-Javadoc)
	 * @see org.openspotlight.graph.SLMetadata#getMetaNodes()
	 */
	public Collection<SLMetaNode> getMetaNodes() throws SLGraphSessionException {
		try {
			Collection<SLMetaNode> metaNodes = new ArrayList<SLMetaNode>();
			SLPersistentQuery query = treeSession.createQuery("//osl/metadata/types/*", SLPersistentQuery.TYPE_XPATH);
			SLPersistentQueryResult result = query.execute();
			Collection<SLPersistentNode> pNodes = result.getNodes();
			for (SLPersistentNode pNode : pNodes) {
				SLMetaNode metaNode = new SLMetaNodeImpl(this, pNode);
				metaNodes.add(metaNode);
			}
			return metaNodes;
		}
		catch (SLException e) {
			throw new SLGraphSessionException("Error on attempt to retrieve node metadata.", e);
		}
	}

	//@Override
	/* (non-Javadoc)
	 * @see org.openspotlight.graph.SLMetadata#getMetaLinkType(java.lang.Class)
	 */
	public SLMetaLinkType getMetaLinkType(Class<? extends SLLink> linkType) throws SLGraphSessionException {
		try {
			StringBuilder statement = new StringBuilder();
			statement.append("//osl/metadata/links/").append(linkType.getName());
			SLPersistentQuery query = treeSession.createQuery(statement.toString(), SLPersistentQuery.TYPE_XPATH);
			SLPersistentQueryResult result = query.execute();
			SLMetaLinkType metaLinkType = null;
			if (result.getRowCount() == 1) {
				metaLinkType = new SLMetaLinkTypeImpl(this, result.getNodes().iterator().next());
			}
			return metaLinkType;
		}
		catch (SLException e) {
			throw new SLGraphSessionException("Error on attempt to retrieve meta link type.", e);
		}
	}

	//@Override
	/* (non-Javadoc)
	 * @see org.openspotlight.graph.SLMetadata#getMetaLinkTypes()
	 */
	public Collection<SLMetaLinkType> getMetaLinkTypes() throws SLGraphSessionException {
		try {
			Collection<SLMetaLinkType> metaLinkTypes = new ArrayList<SLMetaLinkType>();
			StringBuilder statement = new StringBuilder();
			statement.append("//osl/metadata/links/*");
			SLPersistentQuery query = treeSession.createQuery(statement.toString(), SLPersistentQuery.TYPE_XPATH);
			SLPersistentQueryResult result = query.execute();
			Collection<SLPersistentNode> linkTypeNodes = result.getNodes();
			for (SLPersistentNode linkTypeNode : linkTypeNodes) {
				SLMetaLinkType metaLinkType = new SLMetaLinkTypeImpl(this, linkTypeNode);
				metaLinkTypes.add(metaLinkType);
			}
			return metaLinkTypes;
		}
		catch (SLException e) {
			throw new SLGraphSessionException("Error on attempt to retrieve meta link type.", e);
		}
	}
}

