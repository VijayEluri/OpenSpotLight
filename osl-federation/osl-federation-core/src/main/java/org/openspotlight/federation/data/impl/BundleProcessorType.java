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

package org.openspotlight.federation.data.impl;

import static org.openspotlight.common.util.Assertions.checkCondition;
import static org.openspotlight.federation.data.InstanceMetadata.Factory.createWithKeyProperty;
import net.jcip.annotations.ThreadSafe;

import org.openspotlight.federation.data.ConfigurationNode;
import org.openspotlight.federation.data.InstanceMetadata;
import org.openspotlight.federation.data.StaticMetadata;

/**
 * This {@link BundleProcessorType} describes all valid classes that can process this type of bundle. This should be done by
 * configuration, so for "java bundles" for instance is possible to add a few processor types. This {@link BundleProcessorType}
 * classes should implement the {@link BundleProcessor} interface.
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 */
@ThreadSafe
@StaticMetadata( propertyNames = {"active"}, propertyTypes = {Boolean.class}, keyPropertyName = "className", keyPropertyType = String.class, validParentTypes = {Bundle.class} )
public final class BundleProcessorType implements ConfigurationNode {

    private static final String    ACTIVE           = "active";             //$NON-NLS-1$

    private final InstanceMetadata instanceMetadata;

    static final long              serialVersionUID = -3606246260530743008L;

    /**
     * Creates a project within a other project.
     * 
     * @param bundle
     * @param className
     */
    public BundleProcessorType(
                                final Bundle bundle, final String className ) {
        this.instanceMetadata = createWithKeyProperty(this, bundle, className);
        checkCondition("noBundleProcessor", //$NON-NLS-1$
                       bundle.getInstanceMetadata().getChildByKeyValue(BundleProcessorType.class, className) == null);
        bundle.getInstanceMetadata().addChild(this);

    }

    /**
     * {@inheritDoc}
     */
    public final int compareTo( final ConfigurationNode o ) {
        return this.instanceMetadata.compare(this, o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals( final Object obj ) {
        return this.instanceMetadata.equals(obj);
    }

    /**
     * @return active property
     */
    public final Boolean getActive() {
        return this.instanceMetadata.getProperty(ACTIVE);
    }

    /**
     * {@inheritDoc}
     */
    public final InstanceMetadata getInstanceMetadata() {
        return this.instanceMetadata;
    }

    /**
     * The class name for a valid bundle processor for this byndle type.
     * 
     * @return the class name
     */
    public String getName() {
        return (String)this.instanceMetadata.getKeyPropertyValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return this.instanceMetadata.hashCode();
    }

    /**
     * Sets the active property.
     * 
     * @param active
     */
    public final void setActive( final Boolean active ) {
        this.instanceMetadata.setProperty(ACTIVE, active);
    }

}
