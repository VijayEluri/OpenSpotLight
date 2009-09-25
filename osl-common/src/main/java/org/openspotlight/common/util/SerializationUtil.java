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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.LinkedList;
import java.util.Queue;

import org.openspotlight.common.exception.SerializationUtilException;

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
	 * Clone.
	 * 
	 * @param x the x
	 * 
	 * @return the t
	 */
	public static <T> T clone(T x) {
		try {
			return cloneX(x);
		}
		catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Clone x.
	 * 
	 * @param x the x
	 * 
	 * @return the t
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException the class not found exception
	 */
	private static <T> T cloneX(T x) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		CloneOutput cout = new CloneOutput(bout);
		cout.writeObject(x);
		byte[] bytes = bout.toByteArray();

		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		CloneInput cin = new CloneInput(bin, cout);

		@SuppressWarnings("unchecked")
		// thanks to Bas de Bakker for the tip!
		T clone = (T) cin.readObject();
		return clone;
	}

	private static class CloneOutput extends ObjectOutputStream {
		Queue<Class<?>> classQueue = new LinkedList<Class<?>>();

		CloneOutput(OutputStream out) throws IOException {
			super(out);
		}

		@Override
		protected void annotateClass(Class<?> c) {
			classQueue.add(c);
		}

		@Override
		protected void annotateProxyClass(Class<?> c) {
			classQueue.add(c);
		}
	}

	private static class CloneInput extends ObjectInputStream {
		private final CloneOutput output;

		CloneInput(InputStream in, CloneOutput output) throws IOException {
			super(in);
			this.output = output;
		}

		@Override
		protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
			Class<?> c = output.classQueue.poll();
			String expected = osc.getName();
			String found = (c == null) ? null : c.getName();
			if (!expected.equals(found)) {
				throw new InvalidClassException("Classes desynchronized: " + "found " + found + " when expecting " + expected);
			}
			return c;
		}

		@Override
		protected Class<?> resolveProxyClass(String[] interfaceNames) throws IOException, ClassNotFoundException {
			return output.classQueue.poll();
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
