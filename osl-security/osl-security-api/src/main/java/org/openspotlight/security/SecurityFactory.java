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
package org.openspotlight.security;

import org.openspotlight.common.util.AbstractFactory;
import org.openspotlight.jcr.provider.JcrConnectionDescriptor;
import org.openspotlight.security.authz.PolicyEnforcement;
import org.openspotlight.security.idm.SystemUser;
import org.openspotlight.security.idm.User;
import org.openspotlight.security.idm.auth.IdentityManager;

/**
 * A factory for creating Security related components.
 */
public abstract class SecurityFactory extends AbstractFactory {

    /** The Constant SYSTEM_USER_NAME. */
    static final String SYSTEM_USER_NAME = "sysuser";

    /**
     * Creates a new Identity Manager.
     * 
     * @param jcrDescriptor the jcr descriptor
     * @return the identity manager
     */
    public abstract IdentityManager createIdentityManager( JcrConnectionDescriptor jcrDescriptor );

    /**
     * Creates a new specialized PolicyEnforcement for Graphs.
     * 
     * @param jcrDescriptor the jcr descriptor
     * @return the policy enforcement
     */
    public abstract PolicyEnforcement createGraphPolicyEnforcement( JcrConnectionDescriptor jcrDescriptor );

    /**
     * Creates a System User.
     * 
     * @return the system user
     */
    public abstract SystemUser createSystemUser();

    /**
     * Creates a simple User based on its id.
     * 
     * @param id the id
     * @return the user
     */
    public abstract User createUser( String id );

}
