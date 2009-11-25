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

import java.math.BigDecimal;
import java.util.Date;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.openspotlight.common.util.Conversion;

/**
 * Test class for {@link Conversion}.
 * 
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 */
@SuppressWarnings("all")
public class ConversionTest {

	public static enum SomeEnum {
		A, B, C
	}

	@Test
	public void shouldConvertValidValues() throws Exception {
		final Long l = 3l;
		final String asString = Conversion.convert(l, String.class);
		final Integer asInt = Conversion.convert(asString, Integer.class);
		final BigDecimal asBd = Conversion.convert(l, BigDecimal.class);
		Assert.assertThat(asString, Is.is("3"));
		Assert.assertThat(asInt, Is.is(3));
		Assert.assertThat(asBd, Is.is(new BigDecimal("3")));
	}

	@Test
	public void shouldWorkWithClass() throws Exception {
		final Class clazz = String.class;
		final String asString = Conversion.convert(clazz, String.class);
		final Class asClass = Conversion.convert(asString, Class.class);
		Assert.assertThat(asClass == clazz, Is.is(true));
	}

	@Test
	public void shouldWorkWithDates() throws Exception {
		final Date d = new Date();
		final String asString = Conversion.convert(d, String.class);
		final Date asDate = Conversion.convert(asString, Date.class);
		Assert.assertThat(d.toString(), Is.is(asDate.toString()));
	}

	@Test
	public void shouldWorkWithEnums() throws Exception {
		final SomeEnum a = SomeEnum.A;
		final String asString = Conversion.convert(a, String.class);
		final SomeEnum asEnum = Conversion.convert(asString, SomeEnum.class);
		Assert.assertThat(a, Is.is(asEnum));
	}
}
