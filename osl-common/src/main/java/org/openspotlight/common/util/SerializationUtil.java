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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * The Class SerializationUtil.
 * 
 * @author Vitor Hugo Chagas
 */
public class SerializationUtil {
	
	/**
	 * Serialize.
	 * 
	 * @param object the object
	 * 
	 * @return the input stream
	 * 
	 * @throws SerializationUtilException the serialization util exception
	 */
	public static InputStream serialize(Object object) throws SerializationUtilException {
		PipedOutputStream out = null;
		PipedInputStream in = null;
		ObjectOutputStream oos = null;
		try {
	    	out = new PipedOutputStream();
	    	in = new PipedInputStream(out);
	    	oos = new ObjectOutputStream(out);
	    	oos.writeObject(object);
	    	oos.close();
	    	return in;
		}
		catch (IOException e) {
			throw new SerializationUtilException("Error on attempt to serialize object.", e);
		}
		finally {
			close(oos, out);
		}
	}
	
	/**
	 * Deserialize.
	 * 
	 * @param inputStream the input stream
	 * 
	 * @return the object
	 * 
	 * @throws SerializationUtilException the serialization util exception
	 */
	public static Object deserialize(InputStream inputStream) throws SerializationUtilException {
		ObjectInputStream ois = null; 
		try {
			ois = new ObjectInputStream(inputStream);
			return ois.readObject();
		}
		catch (Exception e) {
			throw new SerializationUtilException("Error on attempt to deserialize object.", e);
		}
		finally {
			close(ois);
		}
	}
	
	/**
	 * Close.
	 * 
	 * @param inputStreams the input streams
	 */
	private static void close(InputStream...inputStreams) {
		for (InputStream inputStream : inputStreams) {
			if (inputStream != null) {
				try {
					inputStream.close();
				}
				catch (IOException e) {}
			}
		}
	}
	
	/**
	 * Close.
	 * 
	 * @param outputStreams the output streams
	 */
	private static void close(OutputStream...outputStreams) {
		for (OutputStream outputStream : outputStreams) {
			if (outputStream != null) {
				try {
					outputStream.close();
				}
				catch (IOException e) {}
			}
		}
	}

}
