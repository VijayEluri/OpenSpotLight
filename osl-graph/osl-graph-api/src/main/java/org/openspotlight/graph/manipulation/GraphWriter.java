/*
 * OpenSpotLight - Open Source IT Governance Platform
 *
 *  Copyright (c) 2009, CARAVELATECH CONSULTORIA E TECNOLOGIA EM INFORMATICA LTDA
 *  or third-party contributors as indicated by the @author tags or express
 *  copyright attribution statements applied by the authors.  All third-party
 *  contributions are distributed under license by CARAVELATECH CONSULTORIA E
 *  TECNOLOGIA EM INFORMATICA LTDA.
 *
 *  This copyrighted material is made available to anyone wishing to use, modify,
 *  copy, or redistribute it subject to the terms and conditions of the GNU
 *  Lesser General Public License, as published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 *  See the GNU Lesser General Public License  for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this distribution; if not, write to:
 *  Free Software Foundation, Inc.
 *  51 Franklin Street, Fifth Floor
 *  Boston, MA  02110-1301  USA
 *
 * **********************************************************************
 *  OpenSpotLight - Plataforma de Governança de TI de Código Aberto
 *
 *  Direitos Autorais Reservados (c) 2009, CARAVELATECH CONSULTORIA E TECNOLOGIA
 *  EM INFORMATICA LTDA ou como contribuidores terceiros indicados pela etiqueta
 *  @author ou por expressa atribuição de direito autoral declarada e atribuída pelo autor.
 *  Todas as contribuições de terceiros estão distribuídas sob licença da
 *  CARAVELATECH CONSULTORIA E TECNOLOGIA EM INFORMATICA LTDA.
 *
 *  Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob os
 *  termos da Licença Pública Geral Menor do GNU conforme publicada pela Free Software
 *  Foundation.
 *
 *  Este programa é distribuído na expectativa de que seja útil, porém, SEM NENHUMA
 *  GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU ADEQUAÇÃO A UMA
 *  FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral Menor do GNU para mais detalhes.
 *
 *  Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto com este
 *  programa; se não, escreva para:
 *  Free Software Foundation, Inc.
 *  51 Franklin Street, Fifth Floor
 *  Boston, MA  02110-1301  USA
 */

package org.openspotlight.graph.manipulation;

import java.util.Collection;

import org.openspotlight.graph.Context;
import org.openspotlight.graph.Link;
import org.openspotlight.graph.Node;

/**
 * Created by User: feu - Date: Jun 29, 2010 - Time: 4:29:33 PM
 */
public interface GraphWriter {

    //TODO DO NOT FORGET TO USE THE ARTIFACT_ID DURRING CREATE METHODS

    /**
     * Adds the link.
     * 
     * @param linkClass the link class
     * @param source the source
     * @param target the target
     * @return the l
     */
    public <L extends Link> L createLink( Class<L> linkClass,
                                            Node source,
                                            Node target );

    /**
     * Adds the link.
     * 
     * @param linkClass the link class
     * @param source the source
     * @param target the target
     * @return the l
     */
    public <L extends Link> L createBidirectionalLink( Class<L> linkClass,
                                                         Node source,
                                                         Node target );

    /**
     * Save.
     */
    public void save();

    /**
     * Adds the node.
     * 
     * @param clazz the clazz
     * @param name the name
     * @return the t
     */
    public <T extends Node> T createNode( Node parent,
                                            Class<T> clazz,
                                             String name );

    /**
     * Adds the node.
     * 
     * @param clazz the clazz
     * @param name the name
     * @param linkTypesForLinkDeletion the link types for link deletion
     * @param linkTypesForLinkedNodeDeletion the link types for linked node deletion
     * @return the t
     */
    public <T extends Node> T createNode( Node parent,
                                            Class<T> clazz,
                                             String name,
                                             Collection<Class<? extends Link>> linkTypesForLinkDeletion,
                                             Collection<Class<? extends Link>> linkTypesForLinkedNodeDeletion );

    /**
     * Sets the caption.
     * 
     * @param caption the caption
     */
    public void setContextCaption( Context context,
                                   String caption );

    public void copyNode( Node node,
                          Context target );

    public void moveNode( Node node,
                          Context target );

    public void removeNode( Node node );

    public void removeLink( Link link );

    public void removeContext( Context context );

}