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
package org.openspotlight.graph;

/**
 * The Class SLInvalidLinkPropertyTypeException.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLInvalidLinkPropertyTypeException extends SLGraphSessionException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new sL invalid link property type exception.
	 * 
	 * @param name the name
	 * @param invalidType the invalid type
	 * @param allowedTypes the allowed types
	 */
	public SLInvalidLinkPropertyTypeException(String name, Class<?> invalidType, Class<?>... allowedTypes) {
		super(getMessage(name, invalidType, allowedTypes));
	}

	/**
	 * Instantiates a new sL invalid link property type exception.
	 * 
	 * @param message the message
	 */
	public SLInvalidLinkPropertyTypeException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new sL invalid link property type exception.
	 * 
	 * @param cause the cause
	 */
	public SLInvalidLinkPropertyTypeException(Throwable cause) {
		super(cause);
	}

	/**
	 * Gets the message.
	 * 
	 * @param name the name
	 * @param invalidType the invalid type
	 * @param allowedTypes the allowed types
	 * 
	 * @return the message
	 */
	private static String getMessage(String name, Class<?> invalidType, Class<?>... allowedTypes) {
		StringBuilder message = new StringBuilder();
		message.append("Value of property ")
			.append(name).append(" cannot be retrieved as ")
			.append(invalidType.getName()).append(". ");
		for (int i = 0; i < allowedTypes.length; i++) {
			if (i > 0) message.append(", ");
			message.append(allowedTypes[i].getName());
		}
		message.append(" or super type can be used instead.");
		return message.toString();
	}

}
