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

import static org.openspotlight.common.util.Exceptions.catchAndLog;
import static org.openspotlight.common.util.Exceptions.logAndReturn;
import static org.openspotlight.common.util.Exceptions.logAndReturnNew;
import static org.openspotlight.common.util.Exceptions.logAndThrowNew;

import org.junit.Test;
import org.openspotlight.common.exception.ConfigurationException;
import org.openspotlight.common.util.Exceptions;

/**
 * Test class for {@link Exceptions}
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * 
 */
@SuppressWarnings("all")
public class ExceptionsTest {
    
    public void dangerousMethod() throws Exception {
        throw new ConfigurationException("Am I going to be thrown?"); //$NON-NLS-1$
    }
    
    @Test
    public void shouldCatchException() throws Exception {
        try {
            this.dangerousMethod();
        } catch (final Exception e) {
            catchAndLog(e);
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldCatchExceptionAndThrowAnotherKind() throws Exception {
        try {
            this.dangerousMethod();
        } catch (final Exception e) {
            logAndThrowNew(e, IllegalArgumentException.class);
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldCatchExceptionAndThrowAnotherKindThatReturned()
            throws Exception {
        try {
            this.dangerousMethod();
        } catch (final Exception e) {
            throw logAndReturnNew(e, IllegalArgumentException.class);
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldCatchExceptionAndThrowAnotherKindThatReturnedWithMessage()
            throws Exception {
        try {
            this.dangerousMethod();
        } catch (final Exception e) {
            throw logAndReturnNew("it was so dangerous!", e, //$NON-NLS-1$
                    IllegalArgumentException.class);
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldCatchExceptionAndThrowAnotherKindWithMessage()
            throws Exception {
        try {
            this.dangerousMethod();
        } catch (final Exception e) {
            logAndThrowNew("it was so dangerous!", e, //$NON-NLS-1$
                    IllegalArgumentException.class);
        }
    }
    
    @Test(expected = ConfigurationException.class)
    public void shouldCatchExceptionAndThrowTheSame()
            throws ConfigurationException {
        try {
            this.dangerousMethod();
        } catch (final Exception e) {
            logAndThrowNew(e, ConfigurationException.class);
        }
    }
    
    @Test(expected = ConfigurationException.class)
    public void shouldCatchExceptionAndThrowTheSameThatReturned()
            throws Exception {
        try {
            this.dangerousMethod();
        } catch (final Exception e) {
            throw logAndReturn(e);
        }
    }
    
}
