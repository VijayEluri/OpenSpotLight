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
package org.openspotlight.graph.query.info;

import java.io.Serializable;

/**
 * The Class SLOrderByTypeInfo.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLOrderByTypeInfo implements Serializable {
	
	/**
	 * The Enum OrderType.
	 * 
	 * @author Vitor Hugo Chagas
	 */
	public static enum OrderType {
		
		/** The ASCENDING. */
		ASCENDING,
		
		/** The DESCENDING. */
		DESCENDING
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The type name. */
	private String typeName;
	
	/** The sub types. */
	private boolean subTypes;
	
	/** The property name. */
	private String propertyName;
	
	/** The order type. */
	private OrderType orderType = OrderType.ASCENDING; 
	
	/** The order by statement info. */
	private SLOrderByStatementInfo orderByStatementInfo;
	
	/**
	 * Gets the type name.
	 * 
	 * @return the type name
	 */
	public String getTypeName() {
		return typeName;
	}
	
	/**
	 * Sets the type name.
	 * 
	 * @param typeName the new type name
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	/**
	 * Checks if is sub types.
	 * 
	 * @return true, if is sub types
	 */
	public boolean isSubTypes() {
		return subTypes;
	}
	
	/**
	 * Sets the sub types.
	 * 
	 * @param subTypes the new sub types
	 */
	public void setSubTypes(boolean subTypes) {
		this.subTypes = subTypes;
	}
	
	/**
	 * Gets the property name.
	 * 
	 * @return the property name
	 */
	public String getPropertyName() {
		return propertyName;
	}
	
	/**
	 * Sets the property name.
	 * 
	 * @param propertyName the new property name
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * Gets the order type.
	 * 
	 * @return the order type
	 */
	public OrderType getOrderType() {
		return orderType;
	}

	/**
	 * Sets the order type.
	 * 
	 * @param orderType the new order type
	 */
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	/**
	 * Gets the order by statement info.
	 * 
	 * @return the order by statement info
	 */
	public SLOrderByStatementInfo getOrderByStatementInfo() {
		return orderByStatementInfo;
	}

	/**
	 * Sets the order by statement info.
	 * 
	 * @param orderByStatementInfo the new order by statement info
	 */
	public void setOrderByStatementInfo(SLOrderByStatementInfo orderByStatementInfo) {
		this.orderByStatementInfo = orderByStatementInfo;
	}
}
