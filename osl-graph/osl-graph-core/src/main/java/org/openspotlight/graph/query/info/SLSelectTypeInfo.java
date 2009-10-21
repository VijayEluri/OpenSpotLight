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

import org.openspotlight.common.util.Equals;
import org.openspotlight.common.util.HashCodes;

/**
 * The Class SLSelectTypeInfo.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLSelectTypeInfo {
	
	/** The name. */
	private String name;
	
	/** The sub types. */
	private boolean subTypes;
	
	/** The comma. */
	private boolean comma;
	
	/** The select statement info. */
	private SLSelectStatementInfo selectStatementInfo;
	
	/**
	 * Instantiates a new sL select type info.
	 * 
	 * @param selectStatementInfo the select statement info
	 * @param name the name
	 */
	public SLSelectTypeInfo(SLSelectStatementInfo selectStatementInfo, String name) {
		this.selectStatementInfo = selectStatementInfo;
		setName(name);
	}

	/**
	 * Instantiates a new sL select type info.
	 * 
	 * @param name the name
	 */
	public SLSelectTypeInfo(String name) {
		setName(name);
	}

	/**
	 * Gets the select statement info.
	 * 
	 * @return the select statement info
	 */
	public SLSelectStatementInfo getSelectStatementInfo() {
		return selectStatementInfo;
	}
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Checks if is comma.
	 * 
	 * @return true, if is comma
	 */
	public boolean isComma() {
		return comma;
	}
	
	/**
	 * Sets the comma.
	 * 
	 * @param comma the new comma
	 */
	public void setComma(boolean comma) {
		this.comma = comma;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return Equals.eachEquality(SLSelectTypeInfo.class, this, obj, "name");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return HashCodes.hashOf(name);
	}
}
