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

import net.jcip.annotations.ThreadSafe;

import org.openspotlight.federation.data.InstanceMetadata;
import org.openspotlight.federation.data.StaticMetadata;

/**
 * A bundle is a group of artifact sources such as source folders, database
 * tables and so on. The bundle should group similar artifacts (example: java
 * files).
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * 
 */
@ThreadSafe
@StaticMetadata(propertyNames = { "active", "initialLookup", "driverClass",
        "user", "password", "type", "maxConnections" }, propertyTypes = { Boolean.class,
        String.class, String.class, String.class, String.class,
        DatabaseType.class, Integer.class }, keyPropertyName = "name", keyPropertyType = String.class, validParentTypes = { Group.class }, validChildrenTypes = {
        BundleProcessorType.class, Group.class, StreamArtifactAboutToChange.class,
        CustomArtifact.class, ArtifactMapping.class })
public class DbArtifactSource extends ArtifactSource {
    
    /**
	 * 
	 */
    private static final long serialVersionUID = 1092283780730455977L;
    
    private static final String DRIVER_CLASS = "driverClass"; //$NON-NLS-1$
    
    private static final String USER = "user"; //$NON-NLS-1$
    
    private static final String TYPE = "type"; //$NON-NLS-1$

    private static final String PASSWORD = "password"; //$NON-NLS-1$

    private static final String MAX_CONNECTIONS = "maxConnections"; //$NON-NLS-1$
    
    private final InstanceMetadata instanceMetadata;
    
    /**
     * creates a bundle inside this project.
     * 
     * @param project
     * @param name
     */
    public DbArtifactSource(final Group project, final String name) {
        super(project, name);
        this.instanceMetadata = super.getInstanceMetadata();
    }
    
    /**
     * 
     * @return the driver class
     */
    public String getDriverClass() {
        return this.instanceMetadata.getProperty(DRIVER_CLASS);
    }
    
    /**
     * 
     * @return the db password
     */
    public String getPassword() {
        return this.instanceMetadata.getProperty(PASSWORD);
    }
    
    /**
     * 
     * @return the database type
     */
    public DatabaseType getType() {
        return this.instanceMetadata.getProperty(TYPE);
    }
    
    /**
     * 
     * @return db user name
     */
    public String getUser() {
        return this.instanceMetadata.getProperty(USER);
    }
    
    /**
     * Sets the driver class
     * 
     * @param driverClass
     */
    public void setDriverClass(final String driverClass) {
        this.instanceMetadata.setProperty(DRIVER_CLASS, driverClass);
    }
    
    /**
     * Sets the db password
     * 
     * @param password
     */
    public void setPassword(final String password) {
        this.instanceMetadata.setProperty(PASSWORD, password);
    }
    
    /**
     * Sets the database type
     * 
     * @param type
     */
    public void setType(final DatabaseType type) {
        this.instanceMetadata.setProperty(TYPE, type);
    }
    
    /**
     * Sets db username
     * 
     * @param user
     */
    public void setUser(final String user) {
        this.instanceMetadata.setProperty(USER, user);
    }

    /**
     * 
     * @return the maximum number of connections to open
     */
	public Integer getMaxConnections() {
		return this.instanceMetadata.getProperty(MAX_CONNECTIONS);
	}

	/**
	 * Sets the maximum number of connections to open.
	 * @param maxConnections
	 */
	public void setMaxConnections(Integer maxConnections) {
		this.instanceMetadata.setProperty(MAX_CONNECTIONS, maxConnections);
	}
    
}
