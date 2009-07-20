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

package org.openspotlight.common.util.test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.openspotlight.common.util.Assertions.checkCondition;
import static org.openspotlight.common.util.Assertions.checkEachParameterNotNull;
import static org.openspotlight.common.util.Assertions.checkNotEmpty;
import static org.openspotlight.common.util.Assertions.checkNotNull;
import static org.openspotlight.common.util.Assertions.checkNullMandatory;

import org.junit.Test;
import org.openspotlight.common.util.Assertions;

/**
 * Test class for {@link Assertions}
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * 
 */
@SuppressWarnings("all")
public class AssertionsTest {
    
    @Test
    public void shouldDoNotThrowExceptionWhenGettingNotEmptyParameter() {
        checkNotEmpty("notEmpty", "notEmpty"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    @Test
    public void shouldDoNotThrowExceptionWhenGettingNotNullParameter() {
        checkNotNull("notNullable", "notNullValue"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    @Test
    public void shouldDoNotThrowExceptionWhenGettingNullParameter() {
        checkNullMandatory("nullable", null); //$NON-NLS-1$
    }
    
    @Test
    public void shouldDoNotThrowExceptionWhenGettingValidConditionParameter() {
        checkCondition("valid", true); //$NON-NLS-1$
    }
    
    @Test
    public void shouldDoNotThrowExceptionWhenGettinOnlyNonNullParameters() {
        checkEachParameterNotNull("notNullable", "nonNull"); //$NON-NLS-1$ //$NON-NLS-2$
        checkEachParameterNotNull("notNullable", "nonNull", "anotherNonNull"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
    }
    
    @Test
    public void shouldGetCorrectErrorNessage() {
        try {
            checkNotNull("notNullable", null); //$NON-NLS-1$
        } catch (final IllegalArgumentException e) {
            assertThat(e.getMessage(),
                    is("Parameter named notNullable should be not null!")); //$NON-NLS-1$
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnCheckEmptyWhenGettingNullParameter() {
        checkNotEmpty("notEmpty", null); //$NON-NLS-1$
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGettinAllNullParameter() {
        checkEachParameterNotNull("notNullable", (Object) null); //$NON-NLS-1$
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGettingEmptyParameter() {
        checkNotEmpty("notEmpty", ""); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenGettingInvalidConditionParameter() {
        checkCondition("valid", false); //$NON-NLS-1$
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGettingNonNullParameter() {
        checkNullMandatory("nullable", "non null"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGettingNullParameter() {
        checkNotNull("notNullable", null); //$NON-NLS-1$
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGettingSpacedParameter() {
        checkNotEmpty("notEmpty", "    "); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGettinOneNullParameter() {
        checkEachParameterNotNull("notNullable", "nonNull", null); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
}
