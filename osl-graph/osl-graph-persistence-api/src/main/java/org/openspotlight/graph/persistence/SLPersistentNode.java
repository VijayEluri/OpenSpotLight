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
package org.openspotlight.graph.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import org.openspotlight.common.concurrent.LockContainer;

/**
 * The Interface SLPersistentNode.
 * 
 * @author Vitor Hugo Chagas
 */
public interface SLPersistentNode extends LockContainer {

	/**
	 * Adds the node.
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return the sL persistent node
	 * 
	 * @throws SLPersistentTreeSessionException
	 *             the SL persistent tree session exception
	 */
	public SLPersistentNode addNode(String name)
			throws SLPersistentTreeSessionException;

	/**
	 * Gets the iD.
	 * 
	 * @return the iD
	 * 
	 * @throws SLPersistentTreeSessionException
	 *             the SL persistent tree session exception
	 */
	public String getID() throws SLPersistentTreeSessionException;

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 * 
	 * @throws SLPersistentTreeSessionException
	 *             the SL persistent tree session exception
	 */
	public String getName() throws SLPersistentTreeSessionException;

	/**
	 * Gets the node.
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return the node
	 * 
	 * @throws SLPersistentTreeSessionException
	 *             the SL persistent tree session exception
	 */
	public SLPersistentNode getNode(String name)
			throws SLPersistentTreeSessionException;

	/**
	 * Gets the nodes.
	 * 
	 * @return the nodes
	 * 
	 * @throws SLPersistentTreeSessionException
	 *             the SL persistent tree session exception
	 */
	public Set<SLPersistentNode> getNodes()
			throws SLPersistentTreeSessionException;

	/**
	 * Gets the nodes.
	 * 
	 * @param name
	 *            the name
	 * 
	 * @return the nodes
	 * 
	 * @throws SLPersistentTreeSessionException
	 *             the SL persistent tree session exception
	 */
	public Collection<SLPersistentNode> getNodes(String name)
			throws SLPersistentTreeSessionException;

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 * 
	 * @throws SLPersistentTreeSessionException
	 *             the SL persistent tree session exception
	 */
	public SLPersistentNode getParent() throws SLPersistentTreeSessionException;

	/**
	 * Gets the path.
	 * 
	 * @return the path
	 * 
	 * @throws SLPersistentTreeSessionException
	 *             the SL persistent tree session exception
	 */
	public String getPath() throws SLPersistentTreeSessionException;

	/**
	 * Gets the properties.
	 * 
	 * @param pattern
	 *            the pattern
	 * 
	 * @return the properties
	 * 
	 * @throws SLPersistentTreeSessionException
	 *             the SL persistent tree session exception
	 */
	public Set<SLPersistentProperty<Serializable>> getProperties(String pattern)
			throws SLPersistentTreeSessionException;

	/**
	 * Gets the property.
	 * 
	 * @param clazz
	 *            the clazz
	 * @param name
	 *            the name
	 * 
	 * @return the property
	 * 
	 * @throws SLPersistentPropertyNotFoundException
	 *             the SL persistent property not found exception
	 * @throws SLPersistentTreeSessionException
	 *             the SL persistent tree session exception
	 */
	public <V extends Serializable> SLPersistentProperty<V> getProperty(
			Class<V> clazz, String name)
			throws SLPersistentPropertyNotFoundException,
			SLPersistentTreeSessionException;

	/**
	 * Gets the session.
	 * 
	 * @return the session
	 */
	public SLPersistentTreeSession getSession();

	/**
	 * Removes the.
	 * 
	 * @throws SLPersistentTreeSessionException
	 *             the SL persistent tree session exception
	 */
	public void remove() throws SLPersistentTreeSessionException;

	/**
	 * Save.
	 * 
	 * @throws SLPersistentTreeSessionException
	 *             the SL persistent tree session exception
	 */
	public void save() throws SLPersistentTreeSessionException;

	/**
	 * Sets the property.
	 * 
	 * @param clazz
	 *            the clazz
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 * 
	 * @return the sL persistent property< v>
	 * 
	 * @throws SLPersistentTreeSessionException
	 *             the SL persistent tree session exception
	 */
	public <V extends Serializable> SLPersistentProperty<V> setProperty(
			Class<V> clazz, String name, V value)
			throws SLPersistentTreeSessionException;

}
