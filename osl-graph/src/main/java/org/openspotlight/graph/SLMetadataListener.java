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


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openspotlight.common.exception.SLException;
import org.openspotlight.common.util.Equals;
import org.openspotlight.common.util.HashCodes;
import org.openspotlight.common.util.StringBuilderUtil;
import org.openspotlight.graph.annotation.SLDescription;
import org.openspotlight.graph.annotation.SLRenderHint;
import org.openspotlight.graph.annotation.SLRenderHints;
import org.openspotlight.graph.persistence.SLPersistentNode;
import org.openspotlight.graph.persistence.SLPersistentProperty;
import org.openspotlight.graph.persistence.SLPersistentQuery;
import org.openspotlight.graph.persistence.SLPersistentQueryResult;
import org.openspotlight.graph.persistence.SLPersistentTreeSession;
import org.openspotlight.graph.persistence.SLPersistentTreeSessionException;

/**
 * The listener interface for receiving SLMetadata events. The class that is
 * interested in processing a SLMetadata event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addSLMetadataListener<code> method. When
 * the SLMetadata event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see SLMetadataEvent
 * @author Vitor Hugo Chagas
 */
public class SLMetadataListener extends SLAbstractGraphSessionEventListener {

	/** The meta link node cache. */
	private final Map<LinkKey, SLPersistentNode> metaLinkNodeCache = new HashMap<LinkKey, SLPersistentNode>();
	
	/** The type pair node cache. */
	private final Map<String, SLPersistentNode> typePairNodeCache = new HashMap<String, SLPersistentNode>();
	
	/** The session node type cache. */
	private final Map<String, SLPersistentNode> metaNodeTypeCache = new HashMap<String, SLPersistentNode>();
	
	/** The node property name cache. */
	private final Set<String> nodePropertyNameCache = new HashSet<String>();
	
	/** The link property key cache. */
	private final Set<LinkPropertyKey> linkPropertyKeyCache = new HashSet<LinkPropertyKey>();

	/**
	 * Adds the description.
	 * 
	 * @param type the type
	 * @param pNode the node
	 * 
	 * @throws SLPersistentTreeSessionException the SL persistent tree session
	 * exception
	 */
	private void addDescription(final Class<?> type, final SLPersistentNode pNode) throws SLPersistentTreeSessionException {
		final SLDescription description = type.getAnnotation(SLDescription.class);
		if (description != null) {
			final String propName = SLCommonSupport.toInternalPropertyName(SLConsts.PROPERTY_NAME_DESCRIPTION);
			final SLPersistentProperty<String> prop = SLCommonSupport.getProperty(pNode, String.class, propName);
			if (prop == null) {
				pNode.setProperty(String.class, propName, description.value());
			}
		}
	}

	/**
	 * Adds the link node.
	 * 
	 * @param pairKeyNode the pair key node
	 * @param direction the direction
	 * 
	 * @return the sL persistent node
	 * 
	 * @throws SLPersistentTreeSessionException the SL persistent tree session
	 * exception
	 */
	private SLPersistentNode addLinkNode(final SLPersistentNode pairKeyNode, final int direction) throws SLPersistentTreeSessionException {
		final long linkCount = this.incLinkCount(pairKeyNode);
		final String name = SLCommonSupport.getLinkIndexNodeName(linkCount);
		final SLPersistentNode linkNode = pairKeyNode.addNode(name);
		linkNode.setProperty(Long.class, SLConsts.PROPERTY_NAME_LINK_COUNT, linkCount);
		linkNode.setProperty(Integer.class, SLConsts.PROPERTY_NAME_DIRECTION, direction);
		return linkNode;
	}

	/**
	 * Adds the render hints.
	 * 
	 * @param nodeType the node type
	 * @param pMetaNode the meta node
	 * 
	 * @throws SLPersistentTreeSessionException the SL persistent tree session
	 * exception
	 */
	private void addRenderHints(final Class<? extends SLNode> nodeType, final SLPersistentNode pMetaNode) throws SLPersistentTreeSessionException {
		final SLRenderHints renderHints = nodeType.getAnnotation(SLRenderHints.class);
		if (renderHints != null) {
			final SLRenderHint[] renderHintArr = renderHints.value();
			for (final SLRenderHint renderHint : renderHintArr) {
				final String propName = SLCommonSupport.toInternalPropertyName(SLConsts.PROPERTY_NAME_RENDER_HINT + "." + renderHint.name());
				final SLPersistentProperty<String> prop = SLCommonSupport.getProperty(pMetaNode, String.class, propName);
				if (prop == null) {
					pMetaNode.setProperty(String.class, propName, renderHint.value());
				}
			}
		}
	}

	/**
	 * Gets the a class.
	 * 
	 * @param sourceClass the source class
	 * @param targetClass the target class
	 * 
	 * @return the a class
	 * 
	 * @throws SLException the SL exception
	 */
	private Class<?> getAClass(final Class<?> sourceClass, final Class<?> targetClass) throws SLException {
		return sourceClass.getName().compareTo(targetClass.getName()) < 0 ? sourceClass : targetClass;
	}

	/**
	 * Gets the b class.
	 * 
	 * @param sourceClass the source class
	 * @param targetClass the target class
	 * 
	 * @return the b class
	 * 
	 * @throws SLException the SL exception
	 */
	private Class<?> getBClass(final Class<?> sourceClass, final Class<?> targetClass) throws SLException {
		return sourceClass.getName().compareTo(targetClass.getName()) < 0 ? targetClass : sourceClass;
	}

	/**
	 * Gets the class pair key node.
	 * 
	 * @param treeSession the tree session
	 * @param linkClass the link class
	 * @param sourceClass the source class
	 * @param targetClass the target class
	 * @param typePairKey the type pair key
	 * 
	 * @return the class pair key node
	 * 
	 * @throws SLException the SL exception
	 */
	private SLPersistentNode getTypePairKeyNode(final SLPersistentTreeSession treeSession, final Class<? extends SLLink> linkClass, final Class<?> sourceClass, final Class<?> targetClass, String typePairKey) throws SLException {
		
		SLPersistentNode pairKeyNode = typePairNodeCache.get(typePairKey);
		
		if (pairKeyNode == null) {

			final Class<?> aClass = this.getAClass(sourceClass, targetClass);
			final Class<?> bClass = this.getBClass(sourceClass, targetClass);

			final SLPersistentNode linkClassNode = SLCommonSupport.getMetaLinkClassNode(treeSession, linkClass);
			pairKeyNode = linkClassNode.getNode(typePairKey);

			if (pairKeyNode == null) {
				pairKeyNode = linkClassNode.addNode(typePairKey);
				pairKeyNode.setProperty(String.class, SLConsts.PROPERTY_NAME_A_CLASS_NAME, aClass.getName());
				pairKeyNode.setProperty(String.class, SLConsts.PROPERTY_NAME_B_CLASS_NAME, bClass.getName());
				pairKeyNode.setProperty(Long.class, SLConsts.PROPERTY_NAME_LINK_COUNT, 0L);
			}
			
			typePairNodeCache.put(typePairKey, pairKeyNode);
		}
		return pairKeyNode;
	}

	/**
	 * Gets the meta link direction.
	 * 
	 * @param sourceClass the source class
	 * @param targetClass the target class
	 * @param bidirecional the bidirecional
	 * 
	 * @return the meta link direction
	 * 
	 * @throws SLException the SL exception
	 */
	private int getMetaLinkDirection(final Class<?> sourceClass, final Class<?> targetClass, final boolean bidirecional) throws SLException {
		if (bidirecional) {
			return SLConsts.DIRECTION_BOTH;
		}
		else {
			return this.getAClass(sourceClass, targetClass).equals(sourceClass) ? SLConsts.DIRECTION_AB : SLConsts.DIRECTION_BA;
		}
	}

	/**
	 * Inc link count.
	 * 
	 * @param linkKeyPairNode the link key pair node
	 * 
	 * @return the long
	 * 
	 * @throws SLPersistentTreeSessionException the SL persistent tree session
	 * exception
	 */
	private long incLinkCount(final SLPersistentNode linkKeyPairNode) throws SLPersistentTreeSessionException {
		final SLPersistentProperty<Long> linkCountProp = linkKeyPairNode.getProperty(Long.class, SLConsts.PROPERTY_NAME_LINK_COUNT);
		final long linkCount = linkCountProp.getValue() + 1;
		linkCountProp.setValue(linkCount);
		return linkCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openspotlight.graph.SLAbstractGraphSessionEventListener#linkAdded
	 * (org.openspotlight.graph.SLLinkEvent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void linkAdded(final SLLinkEvent event) throws SLGraphSessionException {

		try {
			final SLLink link = event.getLink();
			final Class<? extends SLLink> linkType = link.getClass().getInterfaces()[0];
			
			final SLNode[] sides = link.getSides();
			final SLNode source = sides[0];
			final SLNode target = sides[1];
			final SLPersistentNode linkNode = event.getLinkNode();
			final SLPersistentTreeSession treeSession = linkNode.getSession();
			final Class<?> sourceClass = source.getClass().getInterfaces()[0];
			final Class<?> targetClass = target.getClass().getInterfaces()[0];
			String typePairKey = getTypePairKey(sourceClass, targetClass);
			final int direction = this.getMetaLinkDirection(sourceClass, targetClass, link.isBidirectional());
			
			LinkKey linkKey = new LinkKey(linkType.getName(), typePairKey, direction);
			
			if (metaLinkNodeCache.get(linkKey) == null) {
				
				final SLPersistentNode classPairKeyNode = this.getTypePairKeyNode(treeSession, linkType, sourceClass, targetClass, typePairKey);

				final StringBuilder statement = new StringBuilder();
				statement.append(classPairKeyNode.getPath()).append("/*[").append(SLConsts.PROPERTY_NAME_DIRECTION).append("=").append(direction).append(']');

				SLPersistentNode metaLinkNode = null;
				final SLPersistentQuery query = treeSession.createQuery(statement.toString(), SLPersistentQuery.TYPE_XPATH);
				final SLPersistentQueryResult result = query.execute();
				if (result.getRowCount() == 0) {
					metaLinkNode = this.addLinkNode(classPairKeyNode, direction);
					this.addDescription(linkType, metaLinkNode);
					this.metaLinkNodeCache.put(linkKey, metaLinkNode);
				}

			}
		}
		catch (final SLException e) {
			throw new SLGraphSessionException("Error on attempt to add meta link node.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openspotlight.graph.SLAbstractGraphSessionEventListener#linkPropertySet
	 * (org.openspotlight.graph.SLLinkPropertyEvent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void linkPropertySet(final SLLinkPropertyEvent event) throws SLGraphSessionException {

		try {

			SLLinkProperty<? extends Serializable> linkProperty = event.getProperty();
			
			SLLink link = linkProperty.getLink();
			final Class<? extends SLLink> linkType = link.getClass().getInterfaces()[0];
			final SLNode[] sides = link.getSides();
			final SLNode source = sides[0];
			final SLNode target = sides[1];
			final SLPersistentNode linkNode = event.getPersistentProperty().getNode();
			final SLPersistentTreeSession treeSession = linkNode.getSession();
			final Class<?> sourceClass = source.getClass().getInterfaces()[0];
			final Class<?> targetClass = target.getClass().getInterfaces()[0];
			String typePairKey = getTypePairKey(sourceClass, targetClass);
			final int direction = this.getMetaLinkDirection(sourceClass, targetClass, link.isBidirectional());
			
			LinkKey linkKey = new LinkKey(linkType.getName(), typePairKey, direction);
			LinkPropertyKey propertyKey = new LinkPropertyKey(linkKey, event.getProperty().getName());
			
			if (!linkPropertyKeyCache.contains(propertyKey)) {
				
				SLPersistentNode metaLinkNode = metaLinkNodeCache.get(linkKey);
				if (metaLinkNode == null) {
					
					SLPersistentNode classPairKeyNode = getTypePairKeyNode(treeSession, linkType, sourceClass, targetClass, typePairKey);
					
					final StringBuilder statement = new StringBuilder();
					statement.append(classPairKeyNode.getPath()).append("/*[").append(SLConsts.PROPERTY_NAME_DIRECTION).append("=").append(direction).append(']');

					final SLPersistentQuery query = treeSession.createQuery(statement.toString(), SLPersistentQuery.TYPE_XPATH);
					final SLPersistentQueryResult result = query.execute();
					if (result.getRowCount() == 1) {
						metaLinkNode = result.getNodes().iterator().next();
					}
				}
				
				String propName = SLCommonSupport.toUserPropertyName(linkProperty.getName());
				metaLinkNode.setProperty(String.class, propName, linkProperty.getValue().getClass().getName());
				linkPropertyKeyCache.add(propertyKey);
			}
		}
		catch (final SLException e) {
			throw new SLGraphSessionException("Error on attempt to set meta link property.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openspotlight.graph.SLAbstractGraphSessionEventListener#nodeAdded
	 * (org.openspotlight.graph.SLNodeEvent)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void nodeAdded(final SLNodeEvent event) throws SLGraphSessionException {
		
		try {
			
			final SLPersistentNode pNode = event.getPersistentNode();
			String typeName = SLCommonSupport.getNodeTypeName(pNode);
			
			final Class<? extends SLNode> nodeType = event.getNode().getClass().getInterfaces()[0];
			if (nodeType.equals(SLNode.class) || this.metaNodeTypeCache.get(typeName) != null) {
				return;
			}
			
			final SLPersistentTreeSession treeSession = pNode.getSession();
			SLPersistentNode pMetaNodeTypeParent = SLCommonSupport.getMetaTypesNode(treeSession);

			final Collection<Class<? extends SLNode>> nodeTypeHierarchy = this.getNodeTypeHierarchy(nodeType);
			for (Class<? extends SLNode> currentNodeType : nodeTypeHierarchy) {
			 	SLPersistentNode pMetaNodeType = pMetaNodeTypeParent.getNode(currentNodeType.getName());
			 	if (pMetaNodeType == null) {
			 		pMetaNodeTypeParent = pMetaNodeTypeParent.addNode(currentNodeType.getName());
			 		pMetaNodeTypeParent.setProperty(String.class, SLConsts.PROPERTY_NAME_NODE_TYPE, currentNodeType.getName());
					this.addRenderHints(nodeType, pMetaNodeTypeParent);
					this.addDescription(nodeType, pMetaNodeTypeParent);
			 	}
			 	else {
			 		pMetaNodeTypeParent = pMetaNodeType;
			 	}
			 	this.metaNodeTypeCache.put(currentNodeType.getName(), pMetaNodeTypeParent);
			}
		}
		catch (final SLPersistentTreeSessionException e) {
			throw new SLGraphSessionException("Error on attempt to add node metadata.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openspotlight.graph.SLAbstractGraphSessionEventListener#nodePropertySet
	 * (org.openspotlight.graph.SLNodePropertyEvent)
	 */
	@Override
	public void nodePropertySet(final SLNodePropertyEvent event) throws SLGraphSessionException {
		
		try {
			final SLPersistentProperty<? extends Serializable> pProperty = event.getPersistentProperty();
			final SLPersistentNode pNode = pProperty.getNode();
			String typeName = SLCommonSupport.getNodeTypeName(pNode);
			String propertyName = pProperty.getName();
			
			String fullPropertyName = typeName + "." + propertyName;
			if (!nodePropertyNameCache.contains(fullPropertyName)) {
				SLPersistentNode metaNodeType = getMetaNodeType(pNode.getSession(), typeName);
				if (metaNodeType != null) {
					metaNodeType.setProperty(String.class, propertyName, pProperty.getValue().getClass().getName());
					nodePropertyNameCache.add(fullPropertyName);
				}
			}
		}
		catch (final SLPersistentTreeSessionException e) {
			throw new SLGraphSessionException("Error on attempt to set meta node property.", e);
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public void sessionCleaned() {
		this.typePairNodeCache.clear();
		this.metaNodeTypeCache.clear();
		this.metaLinkNodeCache.clear();
		this.nodePropertyNameCache.clear();
		this.linkPropertyKeyCache.clear();
	}
	
	@Override
	public void beforeSave(SLGraphSessionEvent event) throws SLGraphSessionException {
		sessionCleaned();
	}
	
	/**
	 * Gets the node type hierarchy.
	 * 
	 * @param nodeType the node type
	 * 
	 * @return the node type hierarchy
	 */
	private Collection<Class<? extends SLNode>> getNodeTypeHierarchy(Class<? extends SLNode> nodeType) {
		List<Class<? extends SLNode>> nodeTypes = new ArrayList<Class<? extends SLNode>>();
		addNodeTypeInHierarchy(nodeTypes, nodeType);
		return nodeTypes;
	}

	
	/**
	 * Adds the node type in hierarchy.
	 * 
	 * @param nodeTypes the node types
	 * @param nodeType the node type
	 */
	@SuppressWarnings("unchecked")
	private void addNodeTypeInHierarchy(List<Class<? extends SLNode>> nodeTypes, Class<? extends SLNode> nodeType) {
		nodeTypes.add(0, nodeType);
		Class<? extends SLNode> superNodeType = nodeType.getInterfaces()[0];
		if (superNodeType != null && !superNodeType.equals(SLNode.class)) {
			addNodeTypeInHierarchy(nodeTypes, superNodeType);
		}
	}

	
	/**
	 * Gets the meta node type.
	 * 
	 * @param typeName the type name
	 * @param treeSession the tree session
	 * 
	 * @return the meta node type
	 * 
	 * @throws SLPersistentTreeSessionException the SL persistent tree session exception
	 */
	private SLPersistentNode getMetaNodeType(SLPersistentTreeSession treeSession, String typeName) throws SLPersistentTreeSessionException {
		SLPersistentNode metaNodeType = metaNodeTypeCache.get(typeName);
		if (metaNodeType == null) {
			StringBuilder statement = new StringBuilder("//osl/metadata/types//*");
			StringBuilderUtil.append(statement, '[', SLConsts.PROPERTY_NAME_NODE_TYPE, "='", typeName, "']");
			SLPersistentQuery query = treeSession.createQuery(statement.toString(), SLPersistentQuery.TYPE_XPATH);
			SLPersistentQueryResult result = query.execute();
			if (result.getRowCount() == 1) {
				metaNodeType = result.getNodes().iterator().next();
			}
		}
		return metaNodeType;
	}
	
	/**
	 * Gets the type pair key.
	 * 
	 * @param sourceClass the source class
	 * @param targetClass the target class
	 * 
	 * @return the type pair key
	 * 
	 * @throws SLException the SL exception
	 */
	private String getTypePairKey(Class<?> sourceClass, Class<?> targetClass) throws SLException {
		final Class<?> aClass = this.getAClass(sourceClass, targetClass);
		final Class<?> bClass = this.getBClass(sourceClass, targetClass);
		final StringBuilder pairKey = new StringBuilder();
		pairKey.append(aClass.getName()).append('.').append(bClass.getName());
		return pairKey.toString();
	}
}

class LinkKey implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String linkTypeName;
	private String typePairKey;
	private int direction;
	
	public LinkKey(String linkTypeName, String typePairKey, int direction) {
		this.linkTypeName = linkTypeName;
		this.typePairKey = typePairKey;
		this.direction = direction;
	}
	
	public String getLinkType() {
		return linkTypeName;
	}
	public void setLinkType(String linkTypeName) {
		this.linkTypeName = linkTypeName;
	}
	public String getTypePairKey() {
		return typePairKey;
	}
	public void setTypePairKey(String typePairKey) {
		this.typePairKey = typePairKey;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	@Override
	public int hashCode() {
		return HashCodes.hashOf(linkTypeName, typePairKey, direction);
	}

	@Override
	public boolean equals(Object obj) {
		LinkKey key = (LinkKey) obj;
		return Equals.eachEquality(new Object[] {linkTypeName, typePairKey, direction}, new Object[] {key.linkTypeName, key.typePairKey, key.direction});
	}
}

class LinkPropertyKey implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private LinkKey linkKey;
	private String name;
	
	public LinkPropertyKey(LinkKey linkKey, String name) {
		this.linkKey = linkKey;
		this.name = name;
	}
	
	public LinkKey getLinkKey() {
		return linkKey;
	}
	public void setLinkKey(LinkKey linkKey) {
		this.linkKey = linkKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		return HashCodes.hashOf(linkKey.getLinkType(), linkKey.getTypePairKey(), linkKey.getDirection(), name);
	}
	
	@Override
	public boolean equals(Object obj) {
		LinkPropertyKey key = (LinkPropertyKey) obj;
		return Equals.eachEquality(new Object[] {linkKey, name}, new Object[] {key.linkKey, key.name});
	}
}
