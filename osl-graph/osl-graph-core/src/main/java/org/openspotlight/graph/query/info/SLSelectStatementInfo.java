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

/**
 * The Class SLSelectStatementInfo.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLSelectStatementInfo extends SLSelectInfo {

    /** The Constant serialVersionUID. */
    private static final long        serialVersionUID = 1L;

    /** The type info list. */
    private List<SLSelectTypeInfo>   typeInfoList;

    /** The by link info list. */
    private List<SLSelectByLinkInfo> byLinkInfoList;

    /** The where statement info. */
    private SLWhereStatementInfo     whereStatementInfo;

    /** The order by statement info. */
    private SLOrderByStatementInfo   orderByStatementInfo;

    /** The all types info. */
    private SLAllTypesInfo           allTypesInfo;

    /** The collator strength. */
    private Integer                  collatorStrength;

    /**
     * Instantiates a new sL select statement info.
     */
    public SLSelectStatementInfo() {
        typeInfoList = new ArrayList<SLSelectTypeInfo>();
        byLinkInfoList = new ArrayList<SLSelectByLinkInfo>();
    }

    /**
     * Gets the all types.
     * 
     * @return the all types
     */
    public SLAllTypesInfo getAllTypes() {
        return allTypesInfo;
    }

    /**
     * Adds the all types.
     * 
     * @return the sL all types info
     */
    public SLAllTypesInfo addAllTypes() {
        if (allTypesInfo == null) {
            allTypesInfo = new SLAllTypesInfo();
        }
        return allTypesInfo;
    }

    /**
     * Adds the type.
     * 
     * @param name the name
     * @return the sL select type info
     */
    public SLSelectTypeInfo addType( String name ) {
        SLSelectTypeInfo typeInfo = new SLSelectTypeInfo(this, name);
        typeInfoList.add(typeInfo);
        return typeInfo;
    }

    /**
     * Adds the by link.
     * 
     * @param name the name
     * @return the sL select by link info
     */
    public SLSelectByLinkInfo addByLink( String name ) {
        SLSelectByLinkInfo byLinkInfo = new SLSelectByLinkInfo(name);
        byLinkInfoList.add(byLinkInfo);
        return byLinkInfo;
    }

    /**
     * Gets the by link info list.
     * 
     * @return the by link info list
     */
    public List<SLSelectByLinkInfo> getByLinkInfoList() {
        return byLinkInfoList;
    }

    /**
     * Gets the type info list.
     * 
     * @return the type info list
     */
    public List<SLSelectTypeInfo> getTypeInfoList() {
        return typeInfoList;
    }

    /**
     * Gets the where statement info.
     * 
     * @return the where statement info
     */
    public SLWhereStatementInfo getWhereStatementInfo() {
        return whereStatementInfo;
    }

    /**
     * Sets the where statement info.
     * 
     * @param whereStatementInfo the new where statement info
     */
    public void setWhereStatementInfo( SLWhereStatementInfo whereStatementInfo ) {
        this.whereStatementInfo = whereStatementInfo;
    }

    /**
     * Gets the collator strength.
     * 
     * @return the collator strength
     */
    public Integer getCollatorStrength() {
        return collatorStrength;
    }

    /**
     * Sets the collator strength.
     * 
     * @param collatorStrength the new collator strength
     */
    public void setCollatorStrength( Integer collatorStrength ) {
        this.collatorStrength = collatorStrength;
    }

    /* (non-Javadoc)
     * @see org.openspotlight.graph.query.info.SLSelectInfo#toString()
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();

        if (allTypesInfo == null) {
            buffer.append("\nSELECT\n");
        } else {
            if (allTypesInfo.isOnWhere()) {
                buffer.append("\nSELECT **\n");
            } else {
                buffer.append("\nSELECT *\n");
            }
        }

        // types ...
        for (int i = 0; i < typeInfoList.size(); i++) {
            SLSelectTypeInfo typeInfo = typeInfoList.get(i);
            if (i > 0) buffer.append(",\n");
            buffer.append('\t').append('"').append(typeInfo.getName());
            if (typeInfo.isSubTypes()) buffer.append(".*");
            buffer.append('"');
        }

        // where ...
        if (whereStatementInfo != null) {
            buffer.append(whereStatementInfo);
        }

        // order by...
        if (orderByStatementInfo != null) {
            buffer.append(orderByStatementInfo);
        }

        buffer.append("USE COLLATOR LEVEL ");
        buffer.append(collatorStrength);
        buffer.append('\n');

        buffer.append(super.toString());

        return buffer.toString();
    }

    public SLOrderByStatementInfo getOrderByStatementInfo() {
        return orderByStatementInfo;
    }

    public void setOrderByStatementInfo( SLOrderByStatementInfo orderByStatementInfo ) {
        this.orderByStatementInfo = orderByStatementInfo;
    }
}
