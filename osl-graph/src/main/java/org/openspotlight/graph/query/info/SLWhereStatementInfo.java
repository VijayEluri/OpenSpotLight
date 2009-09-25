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

import java.util.ArrayList;
import java.util.List;

import org.openspotlight.common.util.StringBuilderUtil;

/**
 * The Class SLWhereStatementInfo.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLWhereStatementInfo {
	
	/** The select statement info. */
	private SLSelectStatementInfo selectStatementInfo;
	
	/** The where type info list. */
	private List<SLWhereTypeInfo> whereTypeInfoList = new ArrayList<SLWhereTypeInfo>();
	
	/** The where link type info list. */
	private List<SLWhereLinkTypeInfo> whereLinkTypeInfoList = new ArrayList<SLWhereLinkTypeInfo>();
	
	/**
	 * Instantiates a new sL where statement info.
	 * 
	 * @param selectStatementInfo the select statement info
	 */
	public SLWhereStatementInfo(SLSelectStatementInfo selectStatementInfo) {
		this.selectStatementInfo = selectStatementInfo;
	}
	
	/**
	 * Gets the where type info list.
	 * 
	 * @return the where type info list
	 */
	public List<SLWhereTypeInfo> getWhereTypeInfoList() {
		return whereTypeInfoList;
	}

	/**
	 * Sets the where type info list.
	 * 
	 * @param whereTypeInfoList the new where type info list
	 */
	public void setWhereTypeInfoList(List<SLWhereTypeInfo> whereTypeInfoList) {
		this.whereTypeInfoList = whereTypeInfoList;
	}

	/**
	 * Gets the where link type info list.
	 * 
	 * @return the where link type info list
	 */
	public List<SLWhereLinkTypeInfo> getWhereLinkTypeInfoList() {
		return whereLinkTypeInfoList;
	}

	/**
	 * Sets the where link type info list.
	 * 
	 * @param whereLinkTypeInfoList the new where link type info list
	 */
	public void setWhereLinkTypeInfoList(List<SLWhereLinkTypeInfo> whereLinkTypeInfoList) {
		this.whereLinkTypeInfoList = whereLinkTypeInfoList;
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
	 * Sets the select statement.
	 * 
	 * @param selectStatementInfo the new select statement
	 */
	public void setSelectStatement(SLSelectStatementInfo selectStatementInfo) {
		this.selectStatementInfo = selectStatementInfo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("\nWHERE\n");
		for (SLWhereTypeInfo typeInfo : whereTypeInfoList) {
			StringBuilderUtil.append(buffer, typeInfo.getTypeStatementInfo());
		}
		for (SLWhereLinkTypeInfo typeInfo : whereLinkTypeInfoList) {
			StringBuilderUtil.append(buffer, typeInfo.getLinkTypeStatementInfo());
		}
		return buffer.toString();
	}
}
