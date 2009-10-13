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
package org.openspotlight.slql.parser;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.openspotlight.common.util.Assertions;
import org.openspotlight.graph.SLGraphSession;
import org.openspotlight.graph.SLNode;

public abstract class AbstractSLQLQuery implements SLQLQuery {

    private static final long              serialVersionUID = 5945900887330334999L;

    protected SLGraphSession               session          = null;
    protected Map<String, SLQLVariable<?>> variables        = null;
    protected SLQLQuery                    targetQuery      = null;

    public AbstractSLQLQuery(
                              SLGraphSession session,
                              Set<SLQLVariable<?>> variables ) {
        Assertions.checkNotNull("session", session);

        this.session = session;
        if (variables != null) {
            for (SLQLVariable<?> slqlVariable : variables) {
                this.variables.put(slqlVariable.getName(), slqlVariable);
            }
        }
    }

    public abstract Collection<SLNode> execute( Map<String, ?> variableValues,
                                                Collection<SLNode> inputNodes );

    public String getOutputModelName() {
        return null;
    }

    public boolean isValid() {
        return true;
    }

    public Collection<String> getVariables() {
        return null;
    }

    public boolean hasOutputModel() {
        return false;
    }

    public boolean isTarget() {
        return false;
    }

    public boolean hasTarget() {
        return false;
    }

    public boolean hasVariables() {
        return false;
    }

    protected void setupVariableValues( Map<String, ?> variableValues ) {
        for (Entry<String, ?> variableNameAndValue : variableValues.entrySet()) {
            SLQLVariable<?> variable = variables.get(variableNameAndValue.getKey());
            variable.setValue(variableNameAndValue.getValue());
        }
    }

    protected boolean getBooleanValue( String variableName ) {
        return false;
    }

    protected double getDecValue( String variableName ) {
        return 0;
    }

    protected int getIntValue( String variableName ) {
        return 0;
    }

    protected String getStringValue( String variableName ) {
        return null;
    }
}
