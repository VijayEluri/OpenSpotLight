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

import java.io.Serializable;
import java.util.Collection;

import org.openspotlight.common.util.Exceptions;

/**
 * The Class SLQLVariable. This class represents a variable defined in slql external dsl.
 * 
 * @author porcelli
 */
/**
 * @author porcelli
 */
public abstract class SLQLVariable implements Serializable {

    private static final long serialVersionUID = 3345904857635022041L;

    /** The variable name. */
    protected String          name             = null;

    /** The display message. */
    protected String          displayMessage   = null;

    /** The value. */
    protected Serializable    value            = null;

    /**
     * Instantiates a new SLQL variable.
     * 
     * @param name the name
     */
    public SLQLVariable(
                         final String name ) {
        this.name = name;
        this.displayMessage = name;
    }

    /**
     * Adds the all domain value.
     * 
     * @param values the values
     */
    public void addAllDomainValue( final Collection<Serializable> values ) {
        for (final Serializable activeValue : values) {
            this.addDomainValue(activeValue);
        }
    }

    /**
     * Adds the domain value.
     * 
     * @param value the value
     */
    public abstract void addDomainValue( Serializable value );

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals( final Object obj ) {
        if (obj instanceof SLQLVariable) {
            return this.name.equalsIgnoreCase(((SLQLVariable)obj).getName());
        }
        return false;
    }

    /**
     * Gets the display message.
     * 
     * @return the display message
     */
    public String getDisplayMessage() {
        return this.displayMessage;
    }

    /**
     * Gets the domain values.
     * 
     * @return the domain values
     */
    public abstract Collection<Serializable> getDomainValues();

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public abstract Serializable getValue();

    /**
     * Checks for domain values.
     * 
     * @return true, if successful
     */
    public abstract boolean hasDomainValues();

    /**
     * Checks if is valid domain value.
     * 
     * @param value the value
     * @return true, if is valid domain value
     */
    public abstract boolean isValidDomainValue( Serializable value );

    /**
     * Checks if is valid value.
     * 
     * @param value the value
     * @return true, if is valid value
     */
    public abstract boolean isValidValue( Serializable value );

    /**
     * Sets the display message.
     * 
     * @param displayMessage the new display message
     */
    public void setDisplayMessage( final String displayMessage ) {
        this.displayMessage = displayMessage;
    }

    /**
     * Sets the value.
     * 
     * @param value the new value
     */
    public void setValue( final Serializable value ) {
        if (this.isValidValue(value)) {
            this.value = value;
        } else {
            Exceptions.logAndThrow(new IllegalArgumentException("Variable value invalid data type."));
        }
    }
}
