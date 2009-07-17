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
import static org.openspotlight.common.util.Strings.firstLetterToLowerCase;
import static org.openspotlight.common.util.Strings.firstLetterToUpperCase;
import static org.openspotlight.common.util.Strings.removeBegginingFrom;

import org.junit.Test;
import org.openspotlight.common.util.Strings;

/**
 * Test class for {@link Strings}
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 * 
 */
@SuppressWarnings("nls")
public class StringsTest {
    
    @Test
    public void shouldConvertFirstCharToLowerCaseWhenLengthIsMoreThanOneCharacter() {
        assertThat(firstLetterToLowerCase("BiggerCamelCasedWord"),
                is("biggerCamelCasedWord"));
    }
    
    @Test
    public void shouldConvertFirstCharToLowerCaseWhenLengthIsOneCharacter() {
        assertThat(firstLetterToLowerCase("I"), is("i"));
    }
    
    @Test
    public void shouldConvertFirstCharToUpperCaseWhenLengthIsMoreThanOneCharacter() {
        assertThat(firstLetterToUpperCase("biggerCamelCasedWord"),
                is("BiggerCamelCasedWord"));
    }
    
    @Test
    public void shouldConvertFirstCharToUpperCaseWhenLengthIsOneCharacter() {
        assertThat(firstLetterToUpperCase("i"), is("I"));
    }
    
    @Test
    public void shouldDoNothingWhenLowerLengthIsZero() {
        assertThat(firstLetterToLowerCase(""), is(""));
    }
    
    @Test
    public void shouldDoNothingWhenUpperLengthIsZero() {
        assertThat(firstLetterToUpperCase(""), is(""));
    }
    
    @Test
    public void shouldRemoveStartingString() {
        assertThat(
                removeBegginingFrom("beggining", "beggining will be removed"),
                is(" will be removed"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowErrorWhenGettingBiggerBeginning() {
        removeBegginingFrom("acb", "b");
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowErrorWhenGettingInvalidString() {
        removeBegginingFrom("a", "b");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorWhenGettingNulls() {
        removeBegginingFrom(null, "b");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenLowerTheFirstLetterIsWithInvalidParameter() {
        firstLetterToLowerCase(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUpperTheFirstLetterIsWithInvalidParameter() {
        firstLetterToUpperCase(null);
    }
    
}
