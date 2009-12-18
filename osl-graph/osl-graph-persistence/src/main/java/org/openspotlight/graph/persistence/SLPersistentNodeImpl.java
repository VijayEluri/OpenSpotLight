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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.openspotlight.common.concurrent.Lock;
import org.openspotlight.common.util.Exceptions;
import org.openspotlight.jcr.util.JCRUtil;

/**
 * The Class SLPersistentNodeImpl.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLPersistentNodeImpl implements SLPersistentNode {

	/** The session. */
	private final SLPersistentTreeSession session;

	private final Lock lock;

	/** The jcr node. */
	private final Node jcrNode;

	/** The parent. */
	private final SLPersistentNode parent;

	/** The event poster. */
	private final SLPersistentEventPoster eventPoster;

	/**
	 * Instantiates a new sL persistent node impl.
	 * 
	 * @param session
	 *            the session
	 * @param parent
	 *            the parent
	 * @param jcrNode
	 *            the jcr node
	 * @param eventPoster
	 *            the event poster
	 * @throws RepositoryException
	 */
	public SLPersistentNodeImpl(final SLPersistentTreeSession session,
			final SLPersistentNode parent, final Node jcrNode,
			final SLPersistentEventPoster eventPoster) {
		this.session = session;
		this.parent = parent;
		this.jcrNode = jcrNode;
		this.eventPoster = eventPoster;
		lock = session.getLockObject();
	}

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openspotlight.graph.persistence.SLPersistentNode#addNode(java.lang
	 * .String)
	 */
	public SLPersistentNode addNode(final String name)
			throws SLPersistentTreeSessionException {
		synchronized (lock) {

			SLPersistentNode persistentNode = null;
			try {
				final Node jcrChildNode;
				jcrChildNode = jcrNode.addNode(name);
				jcrChildNode.addMixin("mix:referenceable");
				persistentNode = new SLPersistentNodeImpl(session, this,
						jcrChildNode, eventPoster);
				eventPoster.post(new SLPersistentNodeEvent(
						SLPersistentNodeEvent.TYPE_NODE_ADDED, persistentNode));

			} catch (final RepositoryException e) {
				Exceptions.catchAndLog(e);
				throw new SLPersistentTreeSessionException(
						"Couldn't add persistent node " + name, e);
			}
			return persistentNode;
		}
	}

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openspotlight.graph.persistence.SLPersistentNode#getID()
	 */
	public String getID() throws SLPersistentTreeSessionException {
		synchronized (lock) {
			try {
				return jcrNode.getUUID();
			} catch (final RepositoryException e) {
				throw new SLPersistentTreeSessionException(
						"Error on attempt to retrieve the persistent node ID.",
						e);
			}
		}
	}

	public Lock getLockObject() {
		return lock;
	}

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openspotlight.graph.persistence.SLPersistentNode#getName()
	 */
	public String getName() throws SLPersistentTreeSessionException {
		synchronized (lock) {
			try {
				return jcrNode.getName();
			} catch (final RepositoryException e) {
				throw new SLPersistentTreeSessionException(
						"Error on attempt to retrieve the persistent node name.",
						e);
			}
		}
	}

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openspotlight.graph.persistence.SLPersistentNode#getNode(java.lang
	 * .String)
	 */
	public SLPersistentNode getNode(final String name)
			throws SLPersistentTreeSessionException {
		SLPersistentNode childPersistentNode = null;
		synchronized (lock) {
			try {
				final Node jcrChildNode;
				jcrChildNode = jcrNode.getNode(name);
				childPersistentNode = new SLPersistentNodeImpl(session, this,
						jcrChildNode, eventPoster);
			} catch (final PathNotFoundException e) {
			} catch (final RepositoryException e) {
				throw new SLPersistentTreeSessionException(
						"Error on attempt to retrieve persistent node.", e);
			}
			return childPersistentNode;
		}
	}

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openspotlight.graph.persistence.SLPersistentNode#getNodes()
	 */
	public Set<SLPersistentNode> getNodes()
			throws SLPersistentTreeSessionException {
		synchronized (lock) {
			try {
				final Set<SLPersistentNode> persistentNodes = new HashSet<SLPersistentNode>();
				final NodeIterator iter = jcrNode.getNodes();
				while (iter.hasNext()) {
					final Node childNode = iter.nextNode();
					final SLPersistentNode childPersistentNode = new SLPersistentNodeImpl(
							session, this, childNode, eventPoster);
					persistentNodes.add(childPersistentNode);
				}
				return persistentNodes;
			} catch (final RepositoryException e) {
				throw new SLPersistentTreeSessionException(
						"Error on attempt to retrieve persistent child nodes.",
						e);
			}
		}
	}

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openspotlight.graph.persistence.SLPersistentNode#getNodes(java.lang
	 * .String)
	 */
	public Collection<SLPersistentNode> getNodes(final String name)
			throws SLPersistentTreeSessionException {
		final Collection<SLPersistentNode> pNodes = new ArrayList<SLPersistentNode>();
		synchronized (lock) {
			try {
				final NodeIterator nodeIter = jcrNode.getNodes(name);
				while (nodeIter.hasNext()) {
					final Node childNode = nodeIter.nextNode();
					final SLPersistentNode pNode = new SLPersistentNodeImpl(
							session, this, childNode, eventPoster);
					pNodes.add(pNode);
				}
			} catch (final RepositoryException e) {
				throw new SLPersistentTreeSessionException(
						"Error on attempt to retrieve persistent nodes.", e);
			}
			return pNodes;
		}
	}

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openspotlight.graph.persistence.SLPersistentNode#getParent()
	 */
	public SLPersistentNode getParent() throws SLPersistentTreeSessionException {
		return parent;
	}

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openspotlight.graph.persistence.SLPersistentNode#getPath()
	 */
	public String getPath() throws SLPersistentTreeSessionException {
		synchronized (lock) {
			try {
				return jcrNode.getPath();
			} catch (final RepositoryException e) {
				throw new SLPersistentTreeSessionException(
						"Error on attempt to retrieve persistent node path.", e);
			}
		}
	}

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openspotlight.graph.persistence.SLPersistentNode#getProperties(java
	 * .lang.String)
	 */
	public Set<SLPersistentProperty<Serializable>> getProperties(
			final String pattern) throws SLPersistentTreeSessionException {
		synchronized (lock) {
			try {
				final Set<SLPersistentProperty<Serializable>> persistentProperties = new HashSet<SLPersistentProperty<Serializable>>();
				final PropertyIterator iter = jcrNode.getProperties(pattern);
				while (iter.hasNext()) {
					final Property jcrProperty = iter.nextProperty();
					final SLPersistentProperty<Serializable> persistentProperty = new SLPersistentPropertyImpl<Serializable>(
							this, Serializable.class, jcrProperty, true,
							eventPoster);
					persistentProperties.add(persistentProperty);
				}
				return persistentProperties;
			} catch (final Exception e) {
				throw new SLPersistentTreeSessionException(
						"Error on attempt to retrieve persistent node properties.",
						e);
			}
		}
	}

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openspotlight.graph.persistence.SLPersistentNode#getProperty(java
	 * .lang.Class, java.lang.String)
	 */
	public <V extends Serializable> SLPersistentProperty<V> getProperty(
			final Class<V> clazz, final String name)
			throws SLPersistentPropertyNotFoundException,
			SLPersistentTreeSessionException {
		synchronized (lock) {
			SLPersistentProperty<V> persistentProperty = null;
			try {
				final Property jcrProperty = jcrNode.getProperty(name);
				persistentProperty = new SLPersistentPropertyImpl<V>(this,
						clazz, jcrProperty, true, eventPoster);
			} catch (final PathNotFoundException e) {
				throw new SLPersistentPropertyNotFoundException(name);
			} catch (final RepositoryException e) {
				throw new SLPersistentTreeSessionException(
						"Error on attempt to retrieve persistent property "
								+ name, e);
			}
			return persistentProperty;
		}
	}

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openspotlight.graph.persistence.SLPersistentNode#getSession()
	 */
	public SLPersistentTreeSession getSession() {
		return session;
	}

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openspotlight.graph.persistence.SLPersistentNode#remove()
	 */
	public void remove() throws SLPersistentTreeSessionException {
		synchronized (lock) {
			try {
				jcrNode.remove();
				eventPoster.post(new SLPersistentNodeEvent(
						SLPersistentNodeEvent.TYPE_NODE_REMOVED, this));
			} catch (final RepositoryException e) {
				throw new SLPersistentTreeSessionException(
						"Error on attempt to remove persistent node.", e);
			}
		}
	}

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openspotlight.graph.persistence.SLPersistentNode#save()
	 */
	public void save() throws SLPersistentTreeSessionException {
		synchronized (lock) {
			try {
				jcrNode.save();
			} catch (final Exception e) {
				Exceptions.catchAndLog(e);
				throw new SLPersistentTreeSessionException(
						"Error on attempt to save persistent node.", e);
			}
		}
	}

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openspotlight.graph.persistence.SLPersistentNode#setProperty(java
	 * .lang.Class, java.lang.String, java.io.Serializable)
	 */
	public <V extends Serializable> SLPersistentProperty<V> setProperty(
			final Class<V> clazz, final String name, final V value)
			throws SLPersistentTreeSessionException {
		synchronized (lock) {
			SLPersistentProperty<V> persistentProperty = null;
			try {

				Property jcrProperty;
				final Session session = jcrNode.getSession();
				if (value.getClass().isArray()) {
					final Value[] jcrValues = JCRUtil.createValues(session,
							value);
					jcrProperty = jcrNode.setProperty(name, jcrValues);
				} else {
					final Value jcrValue = JCRUtil.createValue(session, value);
					jcrProperty = jcrNode.setProperty(name, jcrValue);
				}

				persistentProperty = new SLPersistentPropertyImpl<V>(this,
						clazz, jcrProperty, false, eventPoster);
				eventPoster.post(new SLPersistentPropertyEvent(
						SLPersistentPropertyEvent.TYPE_PROPERTY_SET,
						persistentProperty));
			} catch (final Exception e) {
				Exceptions.catchAndLog(e);
				throw new SLPersistentTreeSessionException(
						"Error on attempt to set persistent property " + name,
						e);
			}
			return persistentProperty;
		}
	}

	// @Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		synchronized (lock) {
			return jcrNode.toString();
		}
	}
}
