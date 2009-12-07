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
 * OpenSpotLight - Plataforma de Governança de TI de Código Aberto
 *
 * Direitos Autorais Reservados (c) 2009, CARAVELATECH CONSULTORIA E TECNOLOGIA
 * EM INFORMATICA LTDA ou como contribuidores terceiros indicados pela etiqueta
 * @author ou por expressa atribuição de direito autoral declarada e atribuída pelo autor.
 * Todas as contribuições de terceiros estão distribuídas sob licença da
 * CARAVELATECH CONSULTORIA E TECNOLOGIA EM INFORMATICA LTDA.
 *
 * Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free Software
 * Foundation.
 *
 * Este programa é distribuído na expectativa de que seja útil, porém, SEM NENHUMA
 * GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU ADEQUAÇÃO A UMA
 * FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral Menor do GNU para mais detalhes.
 *
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto com este
 * programa; se não, escreva para:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.openspotlight.graph.query.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.openspotlight.common.util.Assertions;
import org.openspotlight.common.util.Exceptions;
import org.openspotlight.graph.SLGraphSession;
import org.openspotlight.graph.query.SLInvalidQueryElementException;
import org.openspotlight.graph.query.SLInvalidQuerySyntaxException;
import org.openspotlight.graph.query.SLQLVariable;
import org.openspotlight.graph.query.SLQueryException;
import org.openspotlight.graph.query.SLQueryResult;
import org.openspotlight.graph.query.SLQueryTextInternal;
import org.openspotlight.graph.query.SLQuery.SortMode;

/**
 * The Class AbstractSLQueryTextInternal. This class is the base for dynamic bytecode generation.
 * 
 * @see SLQueryTextInternalBuilder
 * @author porcelli
 */
public abstract class AbstractSLQueryTextInternal implements SLQueryTextInternal {

    /** The Constant serialVersionUID. */
    private static final long           serialVersionUID = 5945900887330334999L;

    /** The variables. */
    protected Map<String, SLQLVariable> variables        = null;

    /** The string constants. */
    protected Map<Integer, String>      stringConstants  = null;

    /** The target query. */
    protected SLQueryTextInternal       targetQuery      = null;

    /** The id. */
    protected String                    id               = null;

    /** The output model name. */
    protected String                    outputModelName  = null;

    /** The target. */
    protected SLQueryTextInternal       target           = null;

    /**
     * Instantiates a new abstract sl query text internal.
     * 
     * @param id the id
     * @param variables the variables
     * @param outputModelName the output model name
     * @param target the target
     * @param stringConstants the string constants
     */
    public AbstractSLQueryTextInternal(
                                        final String id, final Set<SLQLVariable> variables, final String outputModelName,
                                        final SLQueryTextInternal target, final Map<Integer, String> stringConstants ) {
        Assertions.checkNotEmpty("id", id);

        this.id = id;
        this.target = target;
        this.stringConstants = stringConstants;

        if (outputModelName != null && outputModelName.trim().length() > 0) {
            this.outputModelName = outputModelName;
        }

        if (variables != null && variables.size() > 0) {
            this.variables = new HashMap<String, SLQLVariable>();
            for (final SLQLVariable slqlVariable : variables) {
                this.variables.put(slqlVariable.getName(), slqlVariable);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public abstract SLQueryResult execute( final SLGraphSession session,
                                           final Map<String, ?> variableValues,
                                           final String[] inputNodesIDs,
                                           SortMode sortMode,
                                           boolean showSLQL,
                                           Integer limit,
                                           Integer offset )
        throws SLInvalidQueryElementException, SLQueryException, SLInvalidQuerySyntaxException;

    /**
     * Returns the variables content. If variable not found, returns false.
     * 
     * @param variableName the variable name
     * @return the boolean value
     */
    protected Boolean getBooleanValue( final String variableName ) {
        if (this.variables.containsKey(variableName)) {
            return ((SLQLVariableBoolean)this.variables.get(variableName)).getValue();
        }
        return false;
    }

    /**
     * Returns the variables content. If variable not found, returns -1.
     * 
     * @param variableName the variable name
     * @return the dec value
     */
    protected Float getDecValue( final String variableName ) {
        if (this.variables.containsKey(variableName)) {
            return ((SLQLVariableFloat)this.variables.get(variableName)).getValue();
        }
        return new Float(-1);
    }

    /**
     * {@inheritDoc}
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns the variables content. If variable not found, returns -1.
     * 
     * @param variableName the variable name
     * @return the int value
     */
    protected Integer getIntValue( final String variableName ) {
        if (this.variables.containsKey(variableName)) {
            return ((SLQLVariableInteger)this.variables.get(variableName)).getValue();
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    public String getOutputModelName() {
        return this.outputModelName;
    }

    /**
     * Returns the variables content. If variable not found, returns empty string.
     * 
     * @param variableName the variable name
     * @return the string value
     */
    protected String getStringValue( final String variableName ) {
        if (this.variables.containsKey(variableName)) {
            return ((SLQLVariableString)this.variables.get(variableName)).getValue();
        }
        return "";
    }

    /**
     * Returns the String content.
     * 
     * @param variableName the variable name
     * @return the string value
     */
    protected String getStringValue( final int element ) {
        if (this.stringConstants.containsKey(element)) {
            return stringConstants.get(element);
        }
        return "";
    }

    /**
     * {@inheritDoc}
     */
    public SLQueryTextInternal getTarget() {
        return this.target;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<SLQLVariable> getVariables() {
        if (this.variables == null) {
            return null;
        }
        return new ArrayList<SLQLVariable>(this.variables.values());
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasOutputModel() {
        return this.outputModelName != null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasTarget() {
        return this.target != null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasVariables() {
        return this.variables != null;
    }

    /**
     * Validate and initializes the variables values.
     * 
     * @param session the session
     * @param variableValues the variable values
     * @param inputNodesIDs the input nodes i ds
     * @throws SLInvalidQueryElementException the SL invalid query element exception
     */
    protected void validateAndInit( final SLGraphSession session,
                                    final Map<String, ? extends Serializable> variableValues,
                                    final String[] inputNodesIDs ) throws SLInvalidQueryElementException {
        Assertions.checkNotNull("session", session);

        if (this.hasVariables()) {
            Assertions.checkNotEmpty("variableValues", variableValues);
        } else {
            Assertions.checkNullMandatory("variableValues", variableValues);
        }

        if (this.hasTarget()) {
            Assertions.checkNotEmpty("inputNodes", inputNodesIDs);
        } else {
            Assertions.checkNullMandatory("inputNodes", inputNodesIDs);
        }

        if (variableValues != null && variableValues.size() > 0) {
            for (final Entry<String, ? extends Serializable> activeVariableValue : variableValues.entrySet()) {
                if (this.variables.containsKey(activeVariableValue.getKey())) {
                    final SLQLVariable activeVar = this.variables.get(activeVariableValue.getKey());
                    if (activeVar.hasDomainValues() && !activeVar.isValidDomainValue(activeVariableValue.getValue())) {
                        Exceptions.logAndThrow(new SLInvalidQueryElementException("Variable value not Allowed"));
                    }
                    activeVar.setValue(activeVariableValue.getValue());
                } else {
                    Exceptions.logAndThrow(new SLInvalidQueryElementException("Variable Not Found"));
                }
            }
        }
    }
}
