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

import org.openspotlight.graph.query.info.SLSelectStatementByLinkInfo;
import org.openspotlight.graph.query.info.SLSelectStatementInfo;
import org.openspotlight.graph.query.info.SLSelectStatementTypeInfo;

/**
 * The Class SLSelectStatementImpl.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLSelectStatementImpl implements SLSelectStatement, SLSelectStatementInfoGetter {
	
	/** The select info. */
	private SLSelectStatementInfo selectInfo;
	
	/** The types. */
	private List<SLSelectStatementType> types;
	
	/** The by links. */
	private List<SLSelectStatementByLink> byLinks;
	
	/** The select end. */
	private SLSelectStatementEnd selectEnd;
	
	/**
	 * Instantiates a new sL select statement impl.
	 */
	public SLSelectStatementImpl() {
		this.selectInfo = new SLSelectStatementInfo();
		this.types = new ArrayList<SLSelectStatementType>();
		this.byLinks = new ArrayList<SLSelectStatementByLink>();
		this.selectEnd = new SLSelectStatementEndImpl(selectInfo);
	}

	/* (non-Javadoc)
	 * @see org.openspotlight.graph.query.SLSelectStatement#type(java.lang.String)
	 */
	public SLSelectStatementType type(String typeName) {
		SLSelectStatementTypeInfo typeInfo = selectInfo.addType(typeName);
		SLSelectStatementType type = new SLSelectStatementTypeImpl(this, typeInfo);
		types.add(type);
		return type;
	}

	/* (non-Javadoc)
	 * @see org.openspotlight.graph.query.SLSelectStatement#byLink(java.lang.String)
	 */
	public SLSelectStatementByLink byLink(String typeName) {
		SLSelectStatementByLinkInfo byLinkInfo = selectInfo.addByLink(typeName);
		SLSelectStatementByLink byLink = new SLSelectStatementByLinkImpl(this, byLinkInfo);
		byLinks.add(byLink);
		return byLink;
	}

	/* (non-Javadoc)
	 * @see org.openspotlight.graph.query.SLSelectStatement#end()
	 */
	public SLSelectStatementEnd end() {
		verifyIfLastItemTerminatedWithComma();
		return selectEnd;
	}

	/* (non-Javadoc)
	 * @see org.openspotlight.graph.query.SLSelectStatementInfoGetter#getSelectInfo()
	 */
	public SLSelectStatementInfo getSelectInfo() {
		return selectInfo;
	}
	
	@Override
	public String toString() {
		return selectInfo.toString();
	}
	
	private void verifyIfLastItemTerminatedWithComma() {
		int commaCount = 0;
		for (SLSelectStatementTypeInfo typeInfo : selectInfo.getTypeInfoList()) {
			commaCount += typeInfo.isComma() ? 1 : 0;
		}
		for (SLSelectStatementByLinkInfo byLinkInfo : selectInfo.getByLinkInfoList()) {
			commaCount += byLinkInfo.isComma() ? 1 : 0;
		}
		if (commaCount == selectInfo.getTypeInfoList().size() + selectInfo.getByLinkInfoList().size()) {
			throw new SLInvalidQuerySyntaxRuntimeException("last SELECT clause item must not preceed comma.");
		}
	}
}

