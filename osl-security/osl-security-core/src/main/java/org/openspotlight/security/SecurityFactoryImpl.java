/**
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

package org.openspotlight.security;

import org.openspotlight.security.authz.PolicyEnforcement;
import org.openspotlight.security.authz.graph.PolicyEnforcementGraphImpl;
import org.openspotlight.security.idm.SystemUser;
import org.openspotlight.security.idm.User;
import org.openspotlight.security.idm.auth.IdentityManager;
import org.openspotlight.security.idm.auth.IdentityManagerSimpleImpl;
import org.openspotlight.storage.StorageSession;

/**
 * The Class SecurityFactoryImpl.
 * 
 * @author porcelli
 */
public class SecurityFactoryImpl extends SecurityFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public PolicyEnforcement createGraphPolicyEnforcement(final StorageSession session) {
        return new PolicyEnforcementGraphImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdentityManager createIdentityManager(final StorageSession session) {
        return new IdentityManagerSimpleImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SystemUser createSystemUser() {
        return new SystemUser() {
            @Override
            public String getId() {
                return SecurityFactory.SYSTEM_USER_NAME;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createUser(final String id) {
        return new User() {

            @Override
            public String getId() {
                return id;
            }
        };
    }
}
