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
package org.openspotlight.graph.query;

import java.util.Collection;
import java.util.List;

import org.openspotlight.common.exception.SLException;
import org.openspotlight.common.util.Sha1;
import org.openspotlight.graph.SLCommonSupport;
import org.openspotlight.graph.SLGraphSession;
import org.openspotlight.graph.SLGraphSessionException;
import org.openspotlight.graph.SLNode;
import org.openspotlight.graph.SLNodeNotFoundException;
import org.openspotlight.graph.persistence.SLPersistentNode;
import org.openspotlight.graph.persistence.SLPersistentTreeSession;
import org.openspotlight.graph.persistence.SLPersistentTreeSessionException;
import org.openspotlight.graph.query.SLQuery.SortMode;
import org.openspotlight.graph.util.ProxyUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class SLQueryCacheImpl. Default Implementations of {@link SLQueryCache}.
 * 
 * @author porcelli
 */
public class SLQueryCacheImpl implements SLQueryCache {

    /** The tree session. */
    private final SLPersistentTreeSession treeSession;

    /** The session. */
    private final SLGraphSession          session;

    /**
     * Instantiates a new query cache impl.
     * 
     * @param treeSession the tree session
     * @param session the session
     */
    public SLQueryCacheImpl(
                             SLPersistentTreeSession treeSession, SLGraphSession session ) {
        this.treeSession = treeSession;
        this.session = session;
    }

    /**
     * {@inheritDoc}
     */
    public String buildQueryId( final List<SLSelect> selects,
                                final Integer collatorStrength,
                                final String[] inputNodesIDs,
                                final SortMode sortMode,
                                final Integer limit,
                                final Integer offset ) throws SLException {
        StringBuilder sb = new StringBuilder();
        sb.append("select:[");
        for (SLSelect activeSelect : selects) {
            sb.append(activeSelect.toString());
        }
        sb.append("]|\ninput:[");
        if (inputNodesIDs != null) {
            for (String nodeId : inputNodesIDs) {
                sb.append(nodeId);
            }
        }
        sb.append("]|\ncolator:[").append(collatorStrength).append("]");
        sb.append("]|\nsort:[").append(sortMode).append("]");
        sb.append("]|\nlimit:[");
        if (limit != null) {
            sb.append(limit);
            sb.append("]|\noffset:[");
            if (offset != null) {
                sb.append(offset);
                sb.append("]");
            } else {
                sb.append("]");
            }
        } else {
            sb.append("]");
        }

        return Sha1.getSha1SignatureEncodedAsHexa(sb.toString());
    }

    /**
     * {@inheritDoc}
     */
    public SLQueryResultImpl getCache( final String queryId )
        throws SLPersistentTreeSessionException, SLNodeNotFoundException, SLGraphSessionException {
        SLPersistentNode pcacheRootNode = SLCommonSupport.getQueryCacheNode(treeSession);
        SLPersistentNode queryCache;
        try {
            queryCache = pcacheRootNode.getNode(queryId);
            if (queryCache != null) {
                SLNode[] nodes = new SLNode[queryCache.getNodes().size()];
                for (SLPersistentNode activeId : queryCache.getNodes()) {
                    SLNode node = session.getNodeByID(activeId.getName());
                    SLNode nodeProxy = ProxyUtil.createNodeProxy(SLNode.class, node);
                    nodes[activeId.getProperty(Integer.class, "order").getValue()] = nodeProxy;
                }
                return new SLQueryResultImpl(nodes, queryId);
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void add2Cache( final String queryId,
                           final Collection<PNodeWrapper> nodes )
        throws SLPersistentTreeSessionException, SLNodeNotFoundException, SLGraphSessionException {
        SLPersistentNode pcacheRootNode = SLCommonSupport.getQueryCacheNode(treeSession);
        SLPersistentNode queryCache = pcacheRootNode.addNode(queryId);
        int i = 0;
        for (PNodeWrapper pNodeWrapper : nodes) {
            SLPersistentNode refNode = queryCache.addNode(pNodeWrapper.getID());
            refNode.setProperty(Integer.class, "order", i);
            i++;
        }
    }
}
