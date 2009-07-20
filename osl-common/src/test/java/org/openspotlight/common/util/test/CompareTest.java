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
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.openspotlight.common.util.Arrays.andOf;
import static org.openspotlight.common.util.Arrays.of;
import static org.openspotlight.common.util.Compare.compareAll;
import static org.openspotlight.common.util.Compare.npeSafeCompare;

import org.junit.Test;
import org.openspotlight.common.util.Compare;

/**
 * Test class for {@link Compare}
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * 
 */
@SuppressWarnings("all")
public class CompareTest {
    
    @SuppressWarnings("boxing")
    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionGettingDifferentParameterSize() {
        compareAll(of(1, 2, 3), andOf(1, 2));
    }
    
    @Test
    @SuppressWarnings("boxing")
    public void shouldVerifyEquality() {
        assertThat(compareAll(of(1, 2, 3), andOf(1, 2, 3)), is(0));
        assertThat(compareAll(of(1, 2, 5), andOf(1, 2, 3)), is(not(0)));
    }
    
    @Test
    @SuppressWarnings("boxing")
    public void shouldVerifyEqualityInANullPointerSafeWay() {
        assertThat(npeSafeCompare(null, 1), is(not(0)));
        assertThat(npeSafeCompare(1, null), is(not(0)));
        assertThat(npeSafeCompare(1, 1), is(0));
        assertThat(npeSafeCompare(null, null), is(0));
        assertThat(npeSafeCompare(2, 1), is(not(0)));
    }
    
    @Test
    @SuppressWarnings("boxing")
    public void shouldWorkWithNonComparables() {
        final Object o1 = new Object();
        final Object o2 = new Object();
        assertThat(o1.equals(o2), is(false));
        assertThat(npeSafeCompare(o1, o2), is(not(0)));
        assertThat(npeSafeCompare(o1, o1), is(0));
    }
    
    @Test
    @SuppressWarnings("boxing")
    public void shouldWorkWithNulls() {
        assertThat(compareAll(of(1, 2, null), andOf(1, 2, null)), is(0));
        assertThat(compareAll(of(1, 2, null), andOf(1, 2, 3)), is(not(0)));
    }
    
}
