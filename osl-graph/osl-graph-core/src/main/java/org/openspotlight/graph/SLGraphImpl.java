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

import org.openspotlight.common.util.AbstractFactory;
import org.openspotlight.graph.persistence.SLPersistentNode;
import org.openspotlight.graph.persistence.SLPersistentTree;
import org.openspotlight.graph.persistence.SLPersistentTreeException;
import org.openspotlight.graph.persistence.SLPersistentTreeSession;

/**
 * The Class SLGraphImpl.
 * 
 * @author Vitor Hugo Chagas
 */
public class SLGraphImpl implements SLGraph {

    /** The tree. */
    private SLPersistentTree tree;

    /**
     * Instantiates a new sL graph impl.
     * 
     * @param tree the tree
     */
    public SLGraphImpl(
                        SLPersistentTree tree ) {
        this.tree = tree;
    }

    /**
     * {@inheritDoc}
     */
    public SLGraphSession openSession() throws SLGraphException {
        try {
            SLPersistentTreeSession treeSession = tree.openSession();
            SLGraphFactory factory = AbstractFactory.getDefaultInstance(SLGraphFactory.class);
            return factory.createGraphSession(treeSession);
        } catch (Exception e) {
            throw new SLGraphException("Could not open SL graph session.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void shutdown() {
        tree.shutdown();
    }

    /**
     * {@inheritDoc}
     */
    public void gc() throws SLPersistentTreeException {
        SLPersistentTreeSession treeSession = tree.openSession();
        if (SLCommonSupport.containsQueryCache(treeSession)) {
            SLPersistentNode pNode = SLCommonSupport.getQueryCacheNode(treeSession);
            pNode.remove();
        }
        treeSession.close();
    }
}
