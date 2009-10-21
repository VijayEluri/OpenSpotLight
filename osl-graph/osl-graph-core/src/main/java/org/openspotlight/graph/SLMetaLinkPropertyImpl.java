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

import java.io.Serializable;

import org.openspotlight.graph.persistence.SLPersistentProperty;
import org.openspotlight.graph.persistence.SLPersistentTreeSessionException;

/**
 * The Class SLMetaLinkPropertyImpl.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLMetaLinkPropertyImpl implements SLMetaLinkProperty {
	
	/** The meta link. */
	private SLMetaLinkImpl metaLink;
	
	/** The p property. */
	private SLPersistentProperty<Serializable> pProperty;

	/**
	 * Instantiates a new sL meta link property impl.
	 * 
	 * @param metaLink the meta link
	 * @param pProperty the property
	 */
	public SLMetaLinkPropertyImpl(SLMetaLinkImpl metaLink, SLPersistentProperty<Serializable> pProperty) {
		this.metaLink = metaLink;
		this.pProperty = pProperty;
	}

	//@Override
	/* (non-Javadoc)
	 * @see org.openspotlight.graph.SLMetaElement#getMetadata()
	 */
	public SLMetadata getMetadata() throws SLGraphSessionException {
		return metaLink.getMetadata();
	}

	//@Override
	/* (non-Javadoc)
	 * @see org.openspotlight.graph.SLMetaLinkProperty#getMetaLink()
	 */
	public SLMetaLink getMetaLink() throws SLGraphSessionException {
		return metaLink;
	}

	//@Override
	/* (non-Javadoc)
	 * @see org.openspotlight.graph.SLMetaLinkProperty#getName()
	 */
	public String getName() throws SLGraphSessionException {
		try {
			return SLCommonSupport.toSimplePropertyName(pProperty.getName());
		}
		catch (SLPersistentTreeSessionException e) {
			throw new SLGraphSessionException("Error on attempt to retrieve meta link property name.", e);
		}
	}
	
	//@Override
	/* (non-Javadoc)
	 * @see org.openspotlight.graph.SLMetaLinkProperty#getType()
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends Serializable> getType() throws SLGraphSessionException {
		try {
			return (Class<? extends Serializable>) Class.forName((String) pProperty.getValue());
		}
		catch (Exception e) {
			throw new SLGraphSessionException("Error on attempt to retrieve meta link property type.", e);
		}
	}
}
