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
package org.openspotlight.graph.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.openspotlight.graph.SLCommonSupport;
import org.openspotlight.graph.SLLink;
import org.openspotlight.graph.SLLinkInvocationHandler;
import org.openspotlight.graph.SLNode;
import org.openspotlight.graph.SLNodeInvocationHandler;

/**
 * The Class ProxyUtil.
 * 
 * @author Vitor Hugo Chagas
 */
public class ProxyUtil {

	/**
	 * Creates the proxy.
	 * 
	 * @param iClass the i class
	 * @param target the target
	 * 
	 * @return the t
	 */
	public static <T> T createProxy(Class<T> iClass, Object target) {
		InvocationHandler handler = new SimpleInvocationHandler(target);
		return iClass.cast(Proxy.newProxyInstance(iClass.getClassLoader(), new Class<?>[] {iClass}, handler));
	}
	
	/**
	 * Creates the proxy.
	 * 
	 * @param iClass the i class
	 * @param handler the handler
	 * 
	 * @return the t
	 */
	public static <T> T createProxy(Class<T> iClass, InvocationHandler handler) {
		return iClass.cast(Proxy.newProxyInstance(iClass.getClassLoader(), new Class<?>[] {iClass}, handler));
	}
	
	/**
	 * Creates the link proxy.
	 * 
	 * @param linkType the link type
	 * @param link the link
	 * 
	 * @return the t
	 */
	public static <T extends SLLink> T createLinkProxy(Class<T> linkType, SLLink link) {
		InvocationHandler handler = new SLLinkInvocationHandler(link);
		return linkType.cast(Proxy.newProxyInstance(linkType.getClassLoader(), new Class<?>[] {linkType}, handler));
	}

	/**
	 * Creates the node proxy.
	 * 
	 * @param nodeType the node type
	 * @param node the node
	 * 
	 * @return the t
	 */
	public static <T extends SLNode> T createNodeProxy(Class<T> nodeType, SLNode node) {
		InvocationHandler handler = new SLNodeInvocationHandler(node);
		return nodeType.cast(Proxy.newProxyInstance(nodeType.getClassLoader(), new Class<?>[] {nodeType}, handler));
	}
	
	/**
	 * Creates the node proxy.
	 * 
	 * @param node the node
	 * 
	 * @return the sL node
	 */
	public static SLNode createNodeProxy(SLNode node) {
		SLNode proxyNode = null;
		if (node instanceof Proxy) {
			proxyNode = node;
		}
		else {
			InvocationHandler handler = new SLNodeInvocationHandler(node);
			Class<? extends SLNode> nodeType = SLCommonSupport.getNodeType(node);
			proxyNode = nodeType.cast(Proxy.newProxyInstance(nodeType.getClassLoader(), new Class<?>[] {nodeType}, handler));
		}
		return proxyNode;
	}

	/**
	 * Gets the link from proxy.
	 * 
	 * @param proxy the proxy
	 * 
	 * @return the link from proxy
	 */
	public static SLLink getLinkFromProxy(Object proxy) {
		SLLinkInvocationHandler handler = (SLLinkInvocationHandler) Proxy.getInvocationHandler(proxy);
		return handler.getLink();
	}
	
	/**
	 * Gets the node from proxy.
	 * 
	 * @param proxy the proxy
	 * 
	 * @return the node from proxy
	 */
	public static SLNode getNodeFromProxy(Object proxy) {
		SLNodeInvocationHandler handler = (SLNodeInvocationHandler) Proxy.getInvocationHandler(proxy);
		return handler.getNode();
	}
}
