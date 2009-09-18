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
package org.openspotlight.tool.dap.language.java.asm.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Model class that reprents a Method Declaration inside a {@link TypeDefinition}.
 * 
 * @author porcelli
 */
public class MethodDeclaration {

    /** The method name. */
    private String                          name             = null;

    /** The accessor. */
    private int                             access;

    /** The isPrivate indicates if method is private. */
    private boolean                         isPrivate        = false;

    /** The isConstructor defines if this method is a contructor. */
    private boolean                         isConstructor    = false;

    /** The method return type. */
    private TypeReference                   returnType       = null;

    /** The parameters. */
    private List<MethodParameterDefinition> parameters       = new LinkedList<MethodParameterDefinition>();

    /** The exceptions that can be throwed by this method. */
    private List<TypeReference>             thrownExceptions = new LinkedList<TypeReference>();

    /** The type parameters(related to generics). */
    private List<TypeParameter>             typeParameters   = new LinkedList<TypeParameter>();

    /**
     * Instantiates a new method declaration.
     */
    public MethodDeclaration() {
    }

    /**
     * Gets the method name.
     * 
     * @return the method name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the method name.
     * 
     * @param name the new method name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets the accessor.
     * 
     * @return the accessor
     */
    public int getAccess() {
        return access;
    }

    /**
     * Sets the accessor.
     * 
     * @param access the new accessor
     */
    public void setAccess( int access ) {
        this.access = access;
    }

    /**
     * Checks if is private.
     * 
     * @return true, if is private
     */
    public boolean isPrivate() {
        return isPrivate;
    }

    /**
     * Sets the private.
     * 
     * @param isPrivate indicates if it is a private method
     */
    public void setPrivate( boolean isPrivate ) {
        this.isPrivate = isPrivate;
    }

    /**
     * Checks if is constructor.
     * 
     * @return true, if is constructor
     */
    public boolean isConstructor() {
        return isConstructor;
    }

    /**
     * Sets the constructor.
     * 
     * @param isConstructor indicates if it is a constructor
     */
    public void setConstructor( boolean isConstructor ) {
        this.isConstructor = isConstructor;
    }

    /**
     * Gets the return type.
     * 
     * @return the return type
     */
    public TypeReference getReturnType() {
        return returnType;
    }

    /**
     * Sets the return type.
     * 
     * @param returnType the new return type
     */
    public void setReturnType( TypeReference returnType ) {
        this.returnType = returnType;
    }

    /**
     * Gets the parameters.
     * 
     * @return the parameters
     */
    public List<MethodParameterDefinition> getParameters() {
        return parameters;
    }

    /**
     * Sets the parameters.
     * 
     * @param parameters the new parameters
     */
    public void setParameters( List<MethodParameterDefinition> parameters ) {
        this.parameters = parameters;
    }

    /**
     * Gets the thrown exceptions.
     * 
     * @return the thrown exceptions
     */
    public List<TypeReference> getThrownExceptions() {
        return thrownExceptions;
    }

    /**
     * Sets the thrown exceptions.
     * 
     * @param thrownExceptions the new thrown exceptions
     */
    public void setThrownExceptions( List<TypeReference> thrownExceptions ) {
        this.thrownExceptions = thrownExceptions;
    }

    /**
     * Gets the type parameters. This data is related to Generics.
     * 
     * @return the type parameters
     */
    public List<TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    /**
     * Sets the type parameters. This data is related to Generics.
     * 
     * @param typeParameters the new type parameters
     */
    public void setTypeParameters( List<TypeParameter> typeParameters ) {
        this.typeParameters = typeParameters;
    }

}
