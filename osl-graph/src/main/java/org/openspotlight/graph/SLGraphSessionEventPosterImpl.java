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

import java.util.Collection;

/**
 * The Class SLGraphSessionEventPosterImpl.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLGraphSessionEventPosterImpl implements SLGraphSessionEventPoster {
	
	/** The listeners. */
	private Collection<SLGraphSessionEventListener> listeners;
	
	/**
	 * Instantiates a new sL graph session event poster impl.
	 * 
	 * @param listeners the listeners
	 */
	SLGraphSessionEventPosterImpl(Collection<SLGraphSessionEventListener> listeners) {
		this.listeners = listeners;
	}

	//@Override
	/* (non-Javadoc)
	 * @see org.openspotlight.graph.SLGraphSessionEventPoster#post(org.openspotlight.graph.SLGraphSessionEvent)
	 */
	public void post(SLGraphSessionEvent event) throws SLGraphSessionException {
		if (event.getType() == SLGraphSessionEvent.TYPE_BEFORE_SAVE) {
			for (SLGraphSessionEventListener listener : listeners) {
				listener.beforeSave(event);
			}
		}
	}

	//@Override
	/* (non-Javadoc)
	 * @see org.openspotlight.graph.SLGraphSessionEventPoster#post(org.openspotlight.graph.SLNodeEvent)
	 */
	public void post(SLNodeEvent event) throws SLGraphSessionException {
		for (SLGraphSessionEventListener listener : listeners) {
			if (event.getType() == SLNodeEvent.TYPE_NODE_ADDED) {
				listener.nodeAdded(event);
			}
		}
	}

	//@Override
	/* (non-Javadoc)
	 * @see org.openspotlight.graph.SLGraphSessionEventPoster#post(org.openspotlight.graph.SLLinkEvent)
	 */
	public void post(SLLinkEvent event) throws SLGraphSessionException {
		for (SLGraphSessionEventListener listener : listeners) {
			if (event.getType() == SLLinkEvent.TYPE_LINK_ADDED) {
				listener.linkAdded(event);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.openspotlight.graph.SLGraphSessionEventPoster#post(org.openspotlight.graph.SLNodePropertyEvent)
	 */
	public void post(SLNodePropertyEvent event) throws SLGraphSessionException {
		for (SLGraphSessionEventListener listener : listeners) {
			if (event.getType() == SLNodePropertyEvent.TYPE_NODE_PROPERTY_SET) {
				listener.nodePropertySet(event);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.openspotlight.graph.SLGraphSessionEventPoster#post(org.openspotlight.graph.SLLinkPropertyEvent)
	 */
	public void post(SLLinkPropertyEvent event) throws SLGraphSessionException {
		for (SLGraphSessionEventListener listener : listeners) {
			if (event.getType() == SLLinkPropertyEvent.TYPE_LINK_PROPERTY_SET) {
				listener.linkPropertySet(event);
			}
		}
	}
}
