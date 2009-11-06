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
 * Metadata for database columns.
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * 
 */
@ThreadSafe
@StaticMetadata(keyPropertyName = "name", keyPropertyType = String.class, validParentTypes = { TableArtifact.class }, propertyNames = {
        "type", "nullable", "columnSize", "decimalSize" }, propertyTypes = {
        ColumnType.class, NullableSqlType.class, Integer.class,
        Integer.class })
public class Column implements ConfigurationNode {
    
    private static final String COLUMN_SIZE = "columnSize"; //$NON-NLS-1$
    
    private static final String DECIMAL_SIZE = "decimalSize"; //$NON-NLS-1$
    
    /**
     * 
     */
    private static final long serialVersionUID = 814911484835529535L;
    
    private static final String NULLABLE = "nullable"; //$NON-NLS-1$
    
    private static final String TYPE = "type"; //$NON-NLS-1$
    
    private final InstanceMetadata instanceMetadata;
    
    /**
     * Create a column inside a table
     * 
     * @param table
     * @param columnName
     */
    public Column(final TableArtifact table, final String columnName) {
        this.instanceMetadata = createWithKeyProperty(this, table, columnName);
        checkCondition("noColumn", //$NON-NLS-1$
                table.getColumnByName(columnName) == null);
        table.getInstanceMetadata().addChild(this);
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    public int compareTo(final ConfigurationNode o) {
        return this.instanceMetadata.compare(this, o);
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object obj) {
        return this.instanceMetadata.equals(obj);
    }
    
    /**
     * 
     * @return the column size
     */
    public Integer getColumnSize() {
        return this.instanceMetadata.getProperty(COLUMN_SIZE);
    }
    
    /**
     * 
     * @return the decimal size
     */
    public Integer getDecimalSize() {
        return this.instanceMetadata.getProperty(DECIMAL_SIZE);
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    public InstanceMetadata getInstanceMetadata() {
        return this.instanceMetadata;
    }
    
    /**
     * 
     * @return the name
     */
    public String getName() {
        return (String) this.instanceMetadata.getKeyPropertyValue();
    }
    
    /**
     * 
     * @return nullable property
     */
    public NullableSqlType getNullable() {
        return this.instanceMetadata.getProperty(NULLABLE);
    }
    
    /**
     * 
     * @return the column type
     */
    public ColumnType getType() {
        return this.instanceMetadata.getProperty(TYPE);
    }
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return this.instanceMetadata.hashCode();
    }
    
    /**
     * 
     * Sets the column size.
     * 
     * @param columnSize
     */
    public void setColumnSize(final Integer columnSize) {
        this.instanceMetadata.setProperty(COLUMN_SIZE, columnSize);
    }
    
    /**
     * Sets the decimal size.
     * 
     * @param decimalSize
     */
    public void setDecimalSize(final Integer decimalSize) {
        this.instanceMetadata.setProperty(DECIMAL_SIZE, decimalSize);
        
    }
    
    /**
     * Set nullable property.
     * 
     * @param nullable
     */
    public void setNullable(final NullableSqlType nullable) {
        this.instanceMetadata.setProperty(NULLABLE, nullable);
    }
    
    /**
     * Set the column type
     * 
     * @param type
     */
    public void setType(final ColumnType type) {
        this.instanceMetadata.setProperty(TYPE, type);
    }
    
}
