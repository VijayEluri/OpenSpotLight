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

package org.openspotlight.federation.data;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation will set up the {@link ConfigurationNode} behavior. Based on
 * this data the {@link InstanceMetadata} implementation will validade all data
 * passed to this.
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StaticMetadata {
    
    /**
     * In case of nodes that cold be identified by a key property(ies), this
     * parameter should be filled.
     * 
     * @return the key property name
     */
    public String keyPropertyName() default "";
    
    /**
     * In case of nodes that cold be identified by a key property(ies), this
     * parameter should be filled.
     * 
     * @return the key property types
     */
    public Class<? extends Serializable> keyPropertyType() default Serializable.class;
    
    /**
     * All non transient properties should be filled here.
     * 
     * @return the property names
     */
    public String[] propertyNames() default "";
    
    /**
     * All non transient properties should be filled here.
     * 
     * @return the property types
     */
    public Class<?>[] propertyTypes() default Serializable.class;
    
    /**
     * All valid children types should be filled here. In case of no children,
     * just do no fill this parameter.
     * 
     * @return valid children types
     */
    public Class<? extends ConfigurationNode>[] validChildrenTypes() default ConfigurationNode.class;
    
    /**
     * All valid parent types should be filled here. In case of no valid parent
     * (root node), just do not fill this field.
     * 
     * @return valid parent types
     */
    public Class<? extends ConfigurationNode>[] validParentTypes() default ConfigurationNode.class;
}
