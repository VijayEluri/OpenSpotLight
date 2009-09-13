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
 * A bundle is a group of artifact sources such as source folders, database
 * tables and so on. The bundle should group similar artifacts (example: java
 * files).
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 */
@ThreadSafe
@StaticMetadata(propertyNames = { "description" }, propertyTypes = {
		Boolean.class, String.class }, keyPropertyName = "cronInformation", keyPropertyType = String.class, validParentTypes = {
		Group.class, Bundle.class })
public class ScheduleData implements ConfigurationNode {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6442133367229609182L;

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return this.instanceMetadata.getProperty(DESCRIPTION);
	}

	/**
	 * Sets the description.
	 * 
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.instanceMetadata.setProperty(DESCRIPTION, description);
	}

	/** The Constant DESCRIPTION. */
	private static final String DESCRIPTION = "description"; //$NON-NLS-1$

	/** The instance metadata. */
	private final InstanceMetadata instanceMetadata;

	/**
	 * creates a Schedule Data inside this project.
	 * 
	 * @param group the group
	 * @param cronInformation the cron information
	 */
	public ScheduleData(final Group group, final String cronInformation) {
		this.instanceMetadata = createWithKeyProperty(this, group,
				cronInformation);
		checkCondition("noScheduleData", //$NON-NLS-1$
				group.getBundleByName(cronInformation) == null);
		group.getInstanceMetadata().addChild(this);
	}

	/**
	 * creates a Schedule Data inside this bundle.
	 * 
	 * @param bundle the bundle
	 * @param cronInformation the cron information
	 */
	public ScheduleData(final Bundle bundle, final String cronInformation) {
		this.instanceMetadata = createWithKeyProperty(this, bundle,
				cronInformation);
		checkCondition("noScheduleData", //$NON-NLS-1$
				bundle.getBundleByName(cronInformation) == null);
		bundle.getInstanceMetadata().addChild(this);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public final int compareTo(final ConfigurationNode o) {
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
	 * Gets the cron information.
	 * 
	 * @return the cron information
	 */
	public String getCronInformation() {
		return (String) this.instanceMetadata.getKeyPropertyValue();
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
	 * {@inheritDoc}
	 */
	public InstanceMetadata getInstanceMetadata() {
		return instanceMetadata;
	}

}
