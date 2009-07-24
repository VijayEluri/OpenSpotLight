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

import java.util.Date;

import org.openspotlight.graph.annotation.SLProperty;
import org.openspotlight.graph.annotation.SLRenderHint;
import org.openspotlight.graph.annotation.SLRenderHints;

//@SLTransient
//@CollatorLevel(IDENTICAL)
//@RenderHint(key="format", value="cube");
//@RenderHint(key="foreGroundColor" value="back");

/**
 * The Interface JavaClassNode.
 * 
 * @author Vitor Hugo Chagas
 */
@SLRenderHints({
	@SLRenderHint(name="format", value="cube"),
	@SLRenderHint(name="foreground", value="gold")
})
public interface JavaClassNode extends JavaElementNode {
	
	/** The Constant MODIFIER_PUBLIC. */
	public static final Integer MODIFIER_PUBLIC = 1;
	
	/** The Constant MODIFIER_PRIVATE. */
	public static final Integer MODIFIER_PRIVATE = 2;
	
	/** The Constant MODIFIER_PROTECTED. */
	public static final Integer MODIFIER_PROTECTED = 3;
	
	/** The Constant MODIFIER_DEFAULT. */
	public static final Integer MODIFIER_DEFAULT = 4;
	
	//@SLProperty(collatorLevel=IDENTICAL)
	/**
	 * Gets the class name.
	 * 
	 * @return the class name
	 * 
	 * @throws SLGraphSessionException the SL graph session exception
	 */
	@SLProperty
	public String getClassName() throws SLGraphSessionException;
	
	/**
	 * Sets the class name.
	 * 
	 * @param className the new class name
	 * 
	 * @throws SLGraphSessionException the SL graph session exception
	 */
	public void setClassName(String className) throws SLGraphSessionException;
	
	/**
	 * Gets the modifier.
	 * 
	 * @return the modifier
	 * 
	 * @throws SLGraphSessionException the SL graph session exception
	 */
	@SLProperty
	public Integer getModifier() throws SLGraphSessionException;
	
	/**
	 * Sets the modifier.
	 * 
	 * @param modifier the new modifier
	 * 
	 * @throws SLGraphSessionException the SL graph session exception
	 */
	public void setModifier(Integer modifier) throws SLGraphSessionException;
	
	/**
	 * Gets the creation time.
	 * 
	 * @return the creation time
	 * 
	 * @throws SLGraphSessionException the SL graph session exception
	 */
	@SLProperty
	public Date getCreationTime() throws SLGraphSessionException;
	
	/**
	 * Sets the creation time.
	 * 
	 * @param creationTime the new creation time
	 * 
	 * @throws SLGraphSessionException the SL graph session exception
	 */
	public void setCreationTime(Date creationTime) throws SLGraphSessionException;
}

