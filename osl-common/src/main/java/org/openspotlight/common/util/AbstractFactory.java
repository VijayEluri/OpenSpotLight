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
package org.openspotlight.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.openspotlight.common.exception.AbstractFactoryException;

/**
 * A factory for creating Abstract objects.
 */
public abstract class AbstractFactory {
	
	/** The factory map. */
	private static Map<Class<? extends AbstractFactory>, AbstractFactory> factoryMap = new HashMap<Class<? extends AbstractFactory>, AbstractFactory>();
	
	/**
	 * Gets the default instance.
	 * 
	 * @param clazz the clazz
	 * 
	 * @return the default instance
	 * 
	 * @throws AbstractFactoryException the abstract factory exception
	 */
	@SuppressWarnings("unchecked")
	public static <T extends AbstractFactory> T getDefaultInstance(Class<T> clazz) throws AbstractFactoryException {
		T factory = null;
		try {
			factory = (T) factoryMap.get(clazz);
			if (factory == null) {
				Properties props = loadProps(clazz);
				String implClassName = props.getProperty("defaultImpl");
				Class<? extends T> implClass = (Class<? extends T>) Class.forName(implClassName, true, clazz.getClassLoader());
				factory = implClass.newInstance();
				factoryMap.put(clazz, factory);
			}
		}
		catch (Exception e) {
			throw new AbstractFactoryException("Error on attempt to create the factory.", e);
		}
		return factory;
	}
	
	/**
	 * Load props.
	 * 
	 * @param clazz the clazz
	 * 
	 * @return the properties
	 * 
	 * @throws AbstractFactoryException the abstract factory exception
	 */
	private static Properties loadProps(Class<?> clazz) throws AbstractFactoryException {
		String resource = clazz.getName().replace('.', '/').concat(".properties");
		try {
			InputStream inputStream = AbstractFactory.class.getClassLoader().getResourceAsStream(resource);
			Properties props = new Properties();
			props.load(inputStream);
			return props;
		}
		catch (IOException e) {
			throw new AbstractFactoryException("Error on attempt to load factory properties file " + resource, e);
		}
	}
}
