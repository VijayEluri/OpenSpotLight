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

import java.util.ArrayList;
import java.util.List;

import org.openspotlight.graph.query.info.SLSelectByNodeTypeInfo;
import org.openspotlight.graph.query.info.SLSelectInfo;
import org.openspotlight.graph.query.info.SLWhereByNodeTypeInfo;
import org.openspotlight.graph.query.info.SLSelectByNodeTypeInfo.SLSelectTypeInfo;

/**
 * The Class SLSelectStatementImpl.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLSelectByNodeTypeImpl implements SLSelectByNodeType, SLSelectInfoGetter {
	
	/** The select info. */
	private SLSelectByNodeTypeInfo selectInfo;
	
	/** The types. */
	private List<Type> types;
	
	/** The select end. */
	private End selectEnd;
	
	/**
	 * Instantiates a new sL select statement impl.
	 */
	public SLSelectByNodeTypeImpl() {
		this.selectInfo = new SLSelectByNodeTypeInfo();
		this.types = new ArrayList<Type>();
		this.selectEnd = new EndImpl(selectInfo);
	}

	/* (non-Javadoc)
	 * @see org.openspotlight.graph.query.SLSelectStatement#type(java.lang.String)
	 */
	public Type type(String typeName) {
		SLSelectTypeInfo typeInfo = selectInfo.addType(typeName);
		Type type = new TypeImpl(this, typeInfo);
		types.add(type);
		return type;
	}


	/* (non-Javadoc)
	 * @see org.openspotlight.graph.query.SLSelectStatement#end()
	 */
	public End end() {
		verifyIfLastItemTerminatedWithComma();
		return selectEnd;
	}

	/* (non-Javadoc)
	 * @see org.openspotlight.graph.query.SLSelectStatementInfoGetter#getSelectInfo()
	 */
	public SLSelectInfo getSelectInfo() {
		return selectInfo;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return selectInfo.toString();
	}
	
	/**
	 * Verify if last item terminated with comma.
	 */
	private void verifyIfLastItemTerminatedWithComma() {
		int commaCount = 0;
		for (SLSelectTypeInfo typeInfo : selectInfo.getTypeInfoList()) {
			commaCount += typeInfo.isComma() ? 1 : 0;
		}
	}
	
	/**
	 * The Class TypeImpl.
	 * 
	 * @author Vitor Hugo Chagas
	 */
	public static class TypeImpl implements Type {
		
		/** The select by node type. */
		private SLSelectByNodeType selectByNodeType;
		
		/** The type info. */
		private SLSelectTypeInfo typeInfo;

		/**
		 * Instantiates a new type impl.
		 * 
		 * @param selectByNodeType the select by node type
		 * @param typeInfo the type info
		 */
		TypeImpl(SLSelectByNodeType selectByNodeType, SLSelectTypeInfo typeInfo) {
			this.selectByNodeType = selectByNodeType;
			this.typeInfo = typeInfo;
		}

		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLSelectByNodeType.Type#comma()
		 */
		public SLSelectByNodeType comma() {
			typeInfo.setComma(true);
			return selectByNodeType;
		}

		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLSelectByNodeType.Type#selectEnd()
		 */
		public End selectEnd() {
			return selectByNodeType.end();
		}

		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLSelectByNodeType.Type#subTypes()
		 */
		public Type subTypes() {
			typeInfo.setSubTypes(true);
			return this;
		}
		
	}
	
	/**
	 * The Class EndImpl.
	 * 
	 * @author Vitor Hugo Chagas
	 */
	public static class EndImpl implements End {

		/** The select info. */
		private SLSelectByNodeTypeInfo selectInfo;
		
		/** The where. */
		private SLWhereByNodeType where;
		
		/** The order by. */
		private SLOrderByStatement orderBy;
		
		/**
		 * Instantiates a new end impl.
		 * 
		 * @param selectInfo the select info
		 */
		EndImpl(SLSelectByNodeTypeInfo selectInfo) {
			this.selectInfo = selectInfo;
			this.orderBy = new SLOrderByStatementImpl();
		}

		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLSelectByNodeType.End#where()
		 */
		public SLWhereByNodeType where() {
			if (this.where == null) {
				SLWhereByNodeTypeInfo whereStatementInfo = new SLWhereByNodeTypeInfo(selectInfo);
				selectInfo.setWhereStatementInfo(whereStatementInfo);
				this.where = new SLWhereByNodeTypeImpl(orderBy, whereStatementInfo);
			}
			return where;
		}

		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLSelectByNodeType.End#orderBy()
		 */
		public SLOrderByStatement orderBy() {
			return orderBy;
		}

		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLSelectByNodeType.End#keepResult()
		 */
		public End keepResult() {
			selectInfo.setKeepResult(true);
			return this;
		}
		
		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLSelectByNodeType.End#executeXTimes()
		 */
		public End executeXTimes() {
			selectInfo.setXTimes(0);
			return this;
		}

		/* (non-Javadoc)
		 * @see org.openspotlight.graph.query.SLSelectByNodeType.End#executeXTimes(int)
		 */
		public End executeXTimes(int x) {
			selectInfo.setXTimes(x);
			return this;
		}
	}

}

