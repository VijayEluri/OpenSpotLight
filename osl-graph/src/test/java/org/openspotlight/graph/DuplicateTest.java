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
/**
 * 
 */
package org.openspotlight.graph;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openspotlight.graph.query.JavaClass;
import org.openspotlight.graph.query.JavaType;
import org.openspotlight.graph.query.SLQuery;
import org.openspotlight.graph.query.SLQueryResult;
import org.testng.annotations.Test;

/**
 * @author Luiz Fernando Teston - feu.teston@caravelatech.com
 */
public class DuplicateTest {

    @Test
    public void shouldNotInsertTwoEqualNodes() throws Exception {
        final SLGraphFactory factory = new SLGraphFactoryImpl();
        final SLGraph graph = factory.createTempGraph(true);
        SLGraphSession session = graph.openSession();
        final SLNode rootNode = session.createContext("tmp").getRootNode();
        final SLNode rootNode1 = session.createContext("tmp1").getRootNode();
        final JavaClass parent = rootNode.addNode(JavaClass.class, "parent");
        final JavaClass parent1 = rootNode1.addNode(JavaClass.class, "parent");
        final JavaType n1 = parent.addNode(JavaClass.class, "someName");
        n1.setCaption("someName");
        final JavaType n2 = parent.addNode(JavaType.class, "another");
        n2.setCaption("another");
        final JavaType n3 = parent.addNode(JavaType.class, "someName");
        n3.setCaption("someName");
        final JavaType n1_ = parent1.addNode(JavaClass.class, "someName");
        n1_.setCaption("someName");
        final JavaType n2_ = parent1.addNode(JavaType.class, "another");
        n2_.setCaption("another");
        final JavaType n3_ = parent1.addNode(JavaType.class, "someName");
        n3_.setCaption("someName");
        session.save();
        session.close();
        session = graph.openSession();
        final SLQuery query = session.createQuery();
        query.selectByNodeType().type(JavaType.class.getName()).subTypes().selectEnd().where().type(JavaType.class.getName()).each().property(
                                                                                                                                              "caption").equalsTo().value(
                                                                                                                                                                          "someName").typeEnd().whereEnd();
        final SLQueryResult result = query.execute();
        // aqui o map possui uma lista de nodes para cada id de contexto.
        final Map<String, List<SLNode>> resultMap = new HashMap<String, List<SLNode>>();
        resultMap.put("tmp", new ArrayList<SLNode>());
        resultMap.put("tmp1", new ArrayList<SLNode>());
        for (final SLNode n : result.getNodes()) {
            resultMap.get(n.getContext().getID()).add(n);
        }
        // aqui � verificado se cada id de contexto possui apenas um node
        for (final Map.Entry<String, List<SLNode>> entry : resultMap.entrySet()) {
            assertThat(entry.getValue().size(), is(1));
        }
    }
}
