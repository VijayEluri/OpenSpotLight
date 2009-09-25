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
package org.openspotlight.bundle.dap.language.java.asm.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Model class that represents a java type definition.
 * 
 * @author porcelli
 */
public class TypeDefinition {

    /**
     * Enum that represents all java types.
     * 
     * @author porcelli
     */
    public enum JavaTypes {

        /** The CLASS. */
        CLASS,

        /** The INTERFACE. */
        INTERFACE,

        /** The ENUM. */
        ENUM,

        /** The ANNOTATION. */
        ANNOTATION;
    }

    /** The package name. */
    private String                  packageName   = null;

    /** The type name. */
    private String                  typeName      = null;

    /** The type. */
    private JavaTypes               type          = null;

    /** The access. */
    private int                     access;

    /** The is private. */
    private boolean                 isPrivate     = false;

    /** The is inner class. */
    private boolean                 isInnerClass  = false;

    /** The extends def. */
    private TypeReference           extendsDef    = null;

    /** The implements def. */
    private List<TypeReference>     implementsDef = new LinkedList<TypeReference>();

    /** The fields. */
    private List<FieldDeclaration>  fields        = new LinkedList<FieldDeclaration>();

    /** The methods. */
    private List<MethodDeclaration> methods       = new LinkedList<MethodDeclaration>();

    /**
     * Gets the package name.
     * 
     * @return the package name
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Sets the package name.
     * 
     * @param packageName the new package name
     */
    public void setPackageName( String packageName ) {
        this.packageName = packageName;
    }

    /**
     * Gets the type name.
     * 
     * @return the type name
     */
    public String getTypeName() {
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
     * Gets the type.
     * 
     * @return the type
     */
    public JavaTypes getType() {
        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type the new type
     */
    public void setType( JavaTypes type ) {
        this.type = type;
    }

    /**
     * Gets the access.
     * 
     * @return the access
     */
    public int getAccess() {
        return access;
    }

    /**
     * Sets the access.
     * 
     * @param access the new access
     */
    public void setAccess( int access ) {
        this.access = access;
    }

    /**
     * Gets the extends def.
     * 
     * @return the extends def
     */
    public TypeReference getExtendsDef() {
        return extendsDef;
    }

    /**
     * Sets the extends def.
     * 
     * @param extendsDef the new extends def
     */
    public void setExtendsDef( TypeReference extendsDef ) {
        this.extendsDef = extendsDef;
    }

    /**
     * Gets the implements def.
     * 
     * @return the implements def
     */
    public List<TypeReference> getImplementsDef() {
        return implementsDef;
    }

    /**
     * Sets the implements def.
     * 
     * @param implementsDef the new implements def
     */
    public void setImplementsDef( List<TypeReference> implementsDef ) {
        this.implementsDef = implementsDef;
    }

    /**
     * Gets the fields.
     * 
     * @return the fields
     */
    public List<FieldDeclaration> getFields() {
        return fields;
    }

    /**
     * Sets the fields.
     * 
     * @param fields the new fields
     */
    public void setFields( List<FieldDeclaration> fields ) {
        this.fields = fields;
    }

    /**
     * Gets the methods.
     * 
     * @return the methods
     */
    public List<MethodDeclaration> getMethods() {
        return methods;
    }

    /**
     * Sets the methods.
     * 
     * @param methods the new methods
     */
    public void setMethods( List<MethodDeclaration> methods ) {
        this.methods = methods;
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
     * @param isPrivate the new private
     */
    public void setPrivate( boolean isPrivate ) {
        this.isPrivate = isPrivate;
    }

    /**
     * Checks if is inner class.
     * 
     * @return true, if is inner class
     */
    public boolean isInnerClass() {
        return isInnerClass;
    }

    /**
     * Sets the inner class.
     * 
     * @param isInnerClass the new inner class
     */
    public void setInnerClass( boolean isInnerClass ) {
        this.isInnerClass = isInnerClass;
    }
}
