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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openspotlight.common.util.AbstractFactory;
import org.openspotlight.graph.SLCommonSupport;
import org.openspotlight.graph.SLContext;
import org.openspotlight.graph.SLGraph;
import org.openspotlight.graph.SLGraphFactory;
import org.openspotlight.graph.SLGraphSession;
import org.openspotlight.graph.SLGraphSessionException;
import org.openspotlight.graph.SLNode;
import org.openspotlight.graph.persistence.SLPersistentTree;
import org.openspotlight.graph.persistence.SLPersistentTreeException;
import org.openspotlight.graph.persistence.SLPersistentTreeFactory;
import org.openspotlight.graph.persistence.SLPersistentTreeSession;
import org.openspotlight.graph.query.SLQuery.SortMode;
import org.openspotlight.graph.test.domain.JavaInterface;
import org.openspotlight.graph.test.domain.JavaType;
import org.openspotlight.graph.test.domain.JavaTypeMethod;
import org.openspotlight.graph.test.domain.MethodContainsParam;
import org.openspotlight.graph.test.domain.MethodParam;
import org.openspotlight.graph.test.domain.TypeContainsMethod;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
public class SLGraphQueryCacheTest {

    /** The Constant LOGGER. */
    static final Logger             LOGGER = Logger.getLogger(SLGraphQueryCacheTest.class);

    /** The graph. */
    private SLGraph                 graph;

    /** The session. */
    private SLGraphSession          session;

    private SLPersistentTreeSession treeSession;

    private SLQueryCacheImpl        queryCache;

    /**
     * Quick graph population.
     */
    @BeforeClass
    public void quickGraphPopulation() {
        try {
            SLGraphFactory factory = AbstractFactory.getDefaultInstance(SLGraphFactory.class);
            graph = factory.createTempGraph(true);
            session = graph.openSession();
            SLContext context = session.createContext("cacheTest");
            SLNode root = context.getRootNode();
            Set<Class<?>> types = getIFaceTypeSet();
            for (Class<?> type : types) {
                Method[] methods = type.getDeclaredMethods();
                LOGGER.info(type.getName() + ": " + methods.length + " methods");
                JavaInterface javaInteface = root.addNode(JavaInterface.class, type.getName());
                javaInteface.setProperty(String.class, "caption", type.getName());
                for (int i = 0; i < methods.length; i++) {
                    JavaTypeMethod javaMethod = javaInteface.addNode(JavaTypeMethod.class, methods[i].getName());
                    javaMethod.setProperty(String.class, "caption", methods[i].getName());
                    session.addLink(TypeContainsMethod.class, javaInteface, javaMethod, false);
                    Class<?>[] paramTypes = methods[i].getParameterTypes();
                    LOGGER.info("\t\t" + methods[i].getName() + ": " + paramTypes.length + " params");
                    for (int j = 0; j < paramTypes.length; j++) {
                        MethodParam methodParam = javaMethod.addNode(MethodParam.class, paramTypes[j].getName());
                        methodParam.setProperty(String.class, "caption", paramTypes[j].getName());
                        session.addLink(MethodContainsParam.class, javaMethod, methodParam, false);
                    }
                }
            }
            session.save();
            session.close();
            session = graph.openSession();

            final SLPersistentTreeFactory pFactory = AbstractFactory.getDefaultInstance(SLPersistentTreeFactory.class);
            final SLPersistentTree tree = pFactory.createTempPersistentTree(false);
            treeSession = tree.openSession();

            queryCache = new SLQueryCacheImpl(treeSession, session);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Finish.
     */
    @AfterClass
    public void finish() {
        session.close();
        graph.shutdown();
    }

    @Test
    public void selectTypes() throws SLGraphSessionException, SLInvalidQuerySyntaxException, SLPersistentTreeException {
        String queryId = null;
        assertThat(SLCommonSupport.containsQueryCache(treeSession), is(false));

        SLQueryApi query = session.createQueryApi();

        query
             .select()
             .type(JavaType.class.getName()).subTypes()
             .selectEnd();

        SLQueryResult result = query.execute(SortMode.SORTED, false);
        queryId = result.getQueryId();

        assertThat(SLCommonSupport.containsQueryCache(treeSession), is(true));
        assertThat(queryCache.getCache(result.getQueryId()), is(notNullValue()));

        QueryUtil.printResult(result.getNodes());

        final NodeWrapper[] wrappers = wrapNodes(result.getNodes());
        assertThat(wrappers.length, is(5));
        assertThat(new NodeWrapper(org.openspotlight.graph.test.domain.JavaInterface.class.getName(), "cacheTest", "java.util.Collection"), is(wrappers[0]));
        assertThat(new NodeWrapper(org.openspotlight.graph.test.domain.JavaInterface.class.getName(), "cacheTest", "java.util.List"), is(wrappers[1]));
        assertThat(new NodeWrapper(org.openspotlight.graph.test.domain.JavaInterface.class.getName(), "cacheTest", "java.util.Map"), is(wrappers[2]));
        assertThat(new NodeWrapper(org.openspotlight.graph.test.domain.JavaInterface.class.getName(), "cacheTest", "java.util.Set"), is(wrappers[3]));
        assertThat(new NodeWrapper(org.openspotlight.graph.test.domain.JavaInterface.class.getName(), "cacheTest", "java.util.SortedSet"), is(wrappers[4]));

        SLQueryApi query2 = session.createQueryApi();

        query2
              .select()
              .type(JavaType.class.getName()).subTypes()
              .selectEnd();

        SLQueryResult result2 = query2.execute(SortMode.SORTED, false);

        assertThat(result2.getQueryId(), is(queryId));

        final NodeWrapper[] wrappers2 = wrapNodes(result2.getNodes());
        assertThat(wrappers2.length, is(5));
        assertThat(new NodeWrapper(org.openspotlight.graph.test.domain.JavaInterface.class.getName(), "cacheTest", "java.util.Collection"), is(wrappers2[0]));
        assertThat(new NodeWrapper(org.openspotlight.graph.test.domain.JavaInterface.class.getName(), "cacheTest", "java.util.List"), is(wrappers2[1]));
        assertThat(new NodeWrapper(org.openspotlight.graph.test.domain.JavaInterface.class.getName(), "cacheTest", "java.util.Map"), is(wrappers2[2]));
        assertThat(new NodeWrapper(org.openspotlight.graph.test.domain.JavaInterface.class.getName(), "cacheTest", "java.util.Set"), is(wrappers2[3]));
        assertThat(new NodeWrapper(org.openspotlight.graph.test.domain.JavaInterface.class.getName(), "cacheTest", "java.util.SortedSet"), is(wrappers2[4]));

        graph.gc();
        assertThat(SLCommonSupport.containsQueryCache(treeSession), is(false));
    }

    @Test( dependsOnMethods = "selectTypes" )
    public void selectMethods() throws SLGraphSessionException, SLInvalidQuerySyntaxException, SLPersistentTreeException {
        String queryId = null;
        assertThat(SLCommonSupport.containsQueryCache(treeSession), is(false));

        SLQueryApi query = session.createQueryApi();

        query
             .select()
             .type(JavaTypeMethod.class.getName())
             .selectEnd();

        SLQueryResult result = query.execute();
        queryId = result.getQueryId();

        assertThat(SLCommonSupport.containsQueryCache(treeSession), is(true));
        assertThat(queryCache.getCache(result.getQueryId()), is(notNullValue()));

        SLQueryApi query2 = session.createQueryApi();

        query2
              .select()
              .type(JavaTypeMethod.class.getName())
              .selectEnd();

        SLQueryResult result2 = query2.execute();

        assertThat(result2.getQueryId(), is(queryId));

        graph.gc();
        assertThat(SLCommonSupport.containsQueryCache(treeSession), is(false));
    }

    /**
     * Gets the i face type set.
     * 
     * @return the i face type set
     */
    private Set<Class<?>> getIFaceTypeSet() {
        Set<Class<?>> set = new HashSet<Class<?>>();
        set.add(java.util.Collection.class);
        set.add(java.util.Map.class);
        set.add(java.util.List.class);
        set.add(java.util.Set.class);
        set.add(java.util.SortedSet.class);
        return set;
    }

    /**
     * Wrap nodes.
     * 
     * @param nodes the nodes
     * @return the node wrapper[]
     */
    protected NodeWrapper[] wrapNodes( List<SLNode> nodes ) {
        NodeWrapper[] wrappers = new NodeWrapper[nodes.size()];

        for (int i = 0; i < wrappers.length; i++) {
            wrappers[i] = new NodeWrapper(nodes.get(i));
        }
        return wrappers;
    }

}
