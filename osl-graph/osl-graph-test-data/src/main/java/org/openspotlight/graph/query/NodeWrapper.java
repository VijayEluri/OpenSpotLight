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

import org.openspotlight.common.exception.SLRuntimeException;
import org.openspotlight.common.util.HashCodes;
import org.openspotlight.graph.SLGraphSessionException;
import org.openspotlight.graph.SLNode;

/**
 * The Class NodeWrapper.
 * 
 * @author Vitor Hugo Chagas
 */
public class NodeWrapper {

    /** The node. */
    private SLNode node;

    /** The type name. */
    private String typeName;

    /** The name. */
    private String name;

    /** The parent name. */
    private String parentName;

    /**
     * Instantiates a new node wrapper.
     * 
     * @param typeName the type name
     * @param parentName the parent name
     * @param name the name
     */
    public NodeWrapper(
                        String typeName, String parentName, String name ) {
        this.typeName = typeName;
        this.parentName = parentName;
        this.name = name;
    }

    /**
     * Instantiates a new node wrapper.
     * 
     * @param node the node
     */
    public NodeWrapper(
                        SLNode node ) {
        this.node = node;
    }

    /**
     * Gets the type name.
     * 
     * @return the type name
     * @throws SLGraphSessionException the SL graph session exception
     */
    public String getTypeName() throws SLGraphSessionException {
        if (typeName == null) {
            typeName = node.getTypeName();
        }
        return typeName;
    }

    /**
     * Sets the type name.
     * 
     * @param typeName the new type name
     */
    public void setTypeName( String typeName ) {
        this.typeName = typeName;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     * @throws SLGraphSessionException the SL graph session exception
     */
    public String getName() throws SLGraphSessionException {
        if (name == null) {
            name = node.getName();
        }
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets the parent name.
     * 
     * @return the parent name
     * @throws SLGraphSessionException the SL graph session exception
     */
    public String getParentName() throws SLGraphSessionException {
        if (parentName == null) {
            parentName = node.getParent().getName();
        }
        return parentName;
    }

    /**
     * Sets the parent name.
     * 
     * @param parentName the new parent name
     */
    public void setParentName( String parentName ) {
        this.parentName = parentName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( Object obj ) {
        return hashCode() == obj.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        try {
            return HashCodes.hashOf(getTypeName(), getParentName(), getName());
        } catch (SLGraphSessionException e) {
            throw new SLRuntimeException(e);
        }
    }
}
