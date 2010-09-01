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
 * OpenSpotLight - Plataforma de Governança de TI de Código Aberto
 *
 * Direitos Autorais Reservados (c) 2009, CARAVELATECH CONSULTORIA E TECNOLOGIA
 * EM INFORMATICA LTDA ou como contribuidores terceiros indicados pela etiqueta
 * @author ou por expressa atribuição de direito autoral declarada e atribuída pelo autor.
 * Todas as contribuições de terceiros estão distribuídas sob licença da
 * CARAVELATECH CONSULTORIA E TECNOLOGIA EM INFORMATICA LTDA.
 *
 * Este programa é software livre; você pode redistribuí-lo e/ou modificá-lo sob os
 * termos da Licença Pública Geral Menor do GNU conforme publicada pela Free Software
 * Foundation.
 *
 * Este programa é distribuído na expectativa de que seja útil, porém, SEM NENHUMA
 * GARANTIA; nem mesmo a garantia implícita de COMERCIABILIDADE OU ADEQUAÇÃO A UMA
 * FINALIDADE ESPECÍFICA. Consulte a Licença Pública Geral Menor do GNU para mais detalhes.
 *
 * Você deve ter recebido uma cópia da Licença Pública Geral Menor do GNU junto com este
 * programa; se não, escreva para:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.openspotlight.graph.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openspotlight.graph.metadata.MetaNodeType;
import org.openspotlight.graph.metadata.Metadata;

/**
 * The Class SLQuerySupport.
 * 
 * @author Vitor Hugo Chagas
 */
public class QuerySupport {

   
    /**
     * Gets the hierarchy type names.
     * 
     * @param metadata the metadata
     * @param typeName the type name
     * @param subTypes the sub types
     * @return the hierarchy type names
     */
    static List<String> getHierarchyTypeNames( final Metadata metadata,
                                               final String typeName,
                                               final boolean subTypes ) throws MetaNodeTypeNotFoundException {
        final List<String> types = new ArrayList<String>();
        types.add(typeName);
        if (subTypes) {
            final MetaNodeType type = metadata.getMetaNodeType(typeName);
            for (final MetaNodeType currentType : type.getSubMetaNodeTypes()) {
                types.add(currentType.getTypeName());
            }
        }
        return types;
    }

    /**
     * Gets the node i ds.
     * 
     * @param nodes the nodes
     * @return the node i ds
     */
    static String[] getNodeIDs( final Collection<SLNode> nodes ) {
        if (nodes == null) {
            return null;
        }
        int count = 0;
        final String[] ids = new String[nodes.size()];
        for (final SLNode node : nodes) {
            ids[count++] = node.getID();
        }
        return ids;
    }

    
    /**
     * Gets the select info.
     * 
     * @param select the select
     * @return the select info
     */
    static SelectInfo getSelectInfo( final SLSelect select ) {
        final SLSelectInfoGetter getter = (SLSelectInfoGetter)select;
        return getter.getSelectInfo();
    }

    /**
     * Gets the select statement info.
     * 
     * @param select the select
     * @return the select statement info
     */
    static SelectStatementInfo getSelectStatementInfo( final SLSelect select ) {
        final SLSelectStatementInfoGetter getter = (SLSelectStatementInfoGetter)select;
        return getter.getSelectStatementInfo();
    }

    /**
     * Map nodes by type.
     * 
     * @param selectNodeWrappers the select node wrappers
     * @return the map< string, list
     *         < p node wrapper>
     *         >
     * @throws SLPersistentTreeSessionException the SL persistent tree session exception
     */
    static Map<String, List<PNodeWrapper>> mapNodesByType( final Collection<PNodeWrapper> selectNodeWrappers )
        throws SLPersistentTreeSessionException {
        final Map<String, List<PNodeWrapper>> nodeWrapperListMap = new HashMap<String, List<PNodeWrapper>>();
        for (final PNodeWrapper pNodeWrapper : selectNodeWrappers) {
            final String typeName = pNodeWrapper.getTypeName();
            List<PNodeWrapper> typeNodes = nodeWrapperListMap.get(typeName);
            if (typeNodes == null) {
                typeNodes = new ArrayList<PNodeWrapper>();
                nodeWrapperListMap.put(typeName, typeNodes);
            }
            typeNodes.add(pNodeWrapper);
        }
        return nodeWrapperListMap;
    }

    /**
     * Wrap link nodes.
     * 
     * @param pLinkNodes the link nodes
     * @return the collection
     *         < p link node wrapper>
     */
    static Collection<PLinkNodeWrapper> wrapLinkNodes( final Collection<SLPersistentNode> pLinkNodes ) {
        final Collection<PLinkNodeWrapper> pLinkNodeWrappers = new ArrayList<PLinkNodeWrapper>();
        for (final SLPersistentNode pLinkNode : pLinkNodes) {
            final PLinkNodeWrapper pNodeWrapper = new PLinkNodeWrapper(pLinkNode);
            pLinkNodeWrappers.add(pNodeWrapper);
        }
        return pLinkNodeWrappers;
    }


}
