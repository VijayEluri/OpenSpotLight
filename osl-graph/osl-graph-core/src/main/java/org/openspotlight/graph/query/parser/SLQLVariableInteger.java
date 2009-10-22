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
package org.openspotlight.graph.query.parser;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.openspotlight.graph.query.SLQLVariable;

/**
 * The Class SLQLVariableInteger. This class is a int typed SLQLVariable.
 * 
 * @author porcelli
 */
public class SLQLVariableInteger extends SLQLVariable {

    /** The domain value. */
    protected Set<Serializable> domainValue = null;

    /**
     * Instantiates a new sLQL variable integer.
     * 
     * @param name the name
     */
    public SLQLVariableInteger(
                                final String name ) {
        super(name);
        this.domainValue = new HashSet<Serializable>();
    }

    /**
     * {@inheritDoc}
     */

    public void addDomainValue( final Serializable value ) {
        if (this.isValidValue(value)) {
            this.domainValue.add((Integer)value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Serializable> getDomainValues() {
        return this.domainValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getValue() {
        return (Integer)this.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDomainValues() {
        if (this.domainValue.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */

    public boolean isValidDomainValue( final Serializable value ) {
        for (final Serializable activeValue : this.domainValue) {
            if (activeValue.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */

    public boolean isValidValue( final Serializable value ) {
        if (value == null) {
            return false;
        }
        if (value.getClass().getName().equals(int.class.getName())) {
            return true;
        }
        if (value instanceof Integer) {
            return true;
        }
        return false;
    }
}
