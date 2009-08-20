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

import org.openspotlight.graph.SLMetadata;
import org.openspotlight.graph.persistence.SLPersistentTreeSession;

/**
 * The Class SLSelectCommandDO.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLSelectCommandDO {
	
	/** The metadata. */
	private SLMetadata metadata;
	
	/** The node wrappers. */
	private Collection<PNodeWrapper> nodeWrappers;
	
	/** The previous node wrappers. */
	private Collection<PNodeWrapper> previousNodeWrappers;
	
	/** The tree session. */
	private SLPersistentTreeSession treeSession;
	
	/**
	 * Gets the metadata.
	 * 
	 * @return the metadata
	 */
	public SLMetadata getMetadata() {
		return metadata;
	}
	
	/**
	 * Sets the metadata.
	 * 
	 * @param metadata the new metadata
	 */
	public void setMetadata(SLMetadata metadata) {
		this.metadata = metadata;
	}
	
	/**
	 * Gets the node wrappers.
	 * 
	 * @return the node wrappers
	 */
	public Collection<PNodeWrapper> getNodeWrappers() {
		return nodeWrappers;
	}
	
	/**
	 * Sets the node wrappers.
	 * 
	 * @param nodeWrappers the new node wrappers
	 */
	public void setNodeWrappers(Collection<PNodeWrapper> nodeWrappers) {
		this.nodeWrappers = nodeWrappers;
	}
	
	/**
	 * Gets the tree session.
	 * 
	 * @return the tree session
	 */
	public SLPersistentTreeSession getTreeSession() {
		return treeSession;
	}
	
	/**
	 * Sets the tree session.
	 * 
	 * @param treeSession the new tree session
	 */
	public void setTreeSession(SLPersistentTreeSession treeSession) {
		this.treeSession = treeSession;
	}
	
	/**
	 * Gets the previous node wrappers.
	 * 
	 * @return the previous node wrappers
	 */
	public Collection<PNodeWrapper> getPreviousNodeWrappers() {
		return previousNodeWrappers;
	}
	
	/**
	 * Sets the previous node wrappers.
	 * 
	 * @param previousNodeWrappers the new previous node wrappers
	 */
	public void setPreviousNodeWrappers(Collection<PNodeWrapper> previousNodeWrappers) {
		this.previousNodeWrappers = previousNodeWrappers;
	}
}
