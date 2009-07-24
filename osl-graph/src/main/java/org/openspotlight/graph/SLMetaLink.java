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

import java.util.Collection;
import java.util.List;

/**
 * The Interface SLMetaLink.
 * 
 * @author Vitor Hugo Chagas
 */
public interface SLMetaLink extends SLMetaElement {
	
	/**
	 * Gets the meta link type.
	 * 
	 * @return the meta link type
	 * 
	 * @throws SLGraphSessionException the SL graph session exception
	 */
	public SLMetaLinkType getMetaLinkType() throws SLGraphSessionException;
	
	/**
	 * Gets the source type.
	 * 
	 * @return the source type
	 * 
	 * @throws SLGraphSessionException the SL graph session exception
	 */
	public Class<? extends SLNode> getSourceType() throws SLGraphSessionException;
	
	/**
	 * Gets the target type.
	 * 
	 * @return the target type
	 * 
	 * @throws SLGraphSessionException the SL graph session exception
	 */
	public Class<? extends SLNode> getTargetType() throws SLGraphSessionException;

	/**
	 * Gets the other side type.
	 * 
	 * @param sideType the side type
	 * 
	 * @return the other side type
	 * 
	 * @throws SLInvalidMetaLinkSideTypeException the SL invalid meta link side type exception
	 * @throws SLGraphSessionException the SL graph session exception
	 */
	public Class<? extends SLNode> getOtherSideType(Class<? extends SLNode> sideType) throws SLInvalidMetaLinkSideTypeException, SLGraphSessionException;
	
	/**
	 * Gets the side types.
	 * 
	 * @return the side types
	 * 
	 * @throws SLGraphSessionException the SL graph session exception
	 */
	public List<Class<? extends SLNode>> getSideTypes() throws SLGraphSessionException;
	
	/**
	 * Checks if is bidirectional.
	 * 
	 * @return true, if is bidirectional
	 * 
	 * @throws SLGraphSessionException the SL graph session exception
	 */
	public boolean isBidirectional() throws SLGraphSessionException;
	
	/**
	 * Gets the meta properties.
	 * 
	 * @return the meta properties
	 * 
	 * @throws SLGraphSessionException the SL graph session exception
	 */
	public Collection<SLMetaLinkProperty> getMetaProperties() throws SLGraphSessionException;
	
	/**
	 * Gets the meta property.
	 * 
	 * @param name the name
	 * 
	 * @return the meta property
	 * 
	 * @throws SLGraphSessionException the SL graph session exception
	 */
	public SLMetaLinkProperty getMetaProperty(String name) throws SLGraphSessionException;
}

