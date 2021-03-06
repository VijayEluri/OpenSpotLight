/**
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

package org.openspotlight.graph.test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openspotlight.common.util.SLCollections;
import org.openspotlight.graph.Context;
import org.openspotlight.graph.Element;
import org.openspotlight.graph.FullGraphSession;
import org.openspotlight.graph.GraphLocation;
import org.openspotlight.graph.GraphSessionFactory;
import org.openspotlight.graph.Link;
import org.openspotlight.graph.LinkDirection;
import org.openspotlight.graph.Node;
import org.openspotlight.graph.SimpleGraphSession;
import org.openspotlight.graph.TreeLineReference;
import org.openspotlight.graph.TreeLineReference.ArtifactLineReference;
import org.openspotlight.graph.TreeLineReference.SimpleLineReference;
import org.openspotlight.graph.TreeLineReference.StatementLineReference;
import org.openspotlight.graph.manipulation.GraphReader;
import org.openspotlight.graph.manipulation.GraphWriter;
import org.openspotlight.graph.test.link.AutoBidLink;
import org.openspotlight.graph.test.link.NonAutoBidLink;
import org.openspotlight.graph.test.link.TypeExtends;
import org.openspotlight.graph.test.node.JavaMember;
import org.openspotlight.graph.test.node.JavaMemberField;
import org.openspotlight.graph.test.node.JavaType;
import org.openspotlight.graph.test.node.JavaTypeClass;
import org.openspotlight.graph.test.node.JavaTypeInterface;

import com.google.inject.Injector;

public abstract class AbstractGraphTest {

    private static final String sampleLineRef1 = "sampleLineRef1", sampleLineRef2 = "sampleLineRef2",
                                               sampleLineRef3 = "sampleLineRef3", sampleArtifact1 =
                                               "sampleArtifact1", sampleArtifact2 = "sampleArtifact2";

    private static final int    sampleRef11beginLine = 1, sampleRef11endLine = 2, sampleRef11beginColumn = 3,
                                                     sampleRef11endColumn = 4,

                                                     sampleRef12beginLine = 1, sampleRef12endLine = 2,
                                                     sampleRef12beginColumn = 3, sampleRef12endColumn = 4,

                                                     sampleRef13beginLine = 5, sampleRef13endLine = 6,
                                                     sampleRef13beginColumn = 7, sampleRef13endColumn = 8,

                                                     sampleRef21beginLine = 1, sampleRef21endLine = 2,
                                                     sampleRef21beginColumn = 3, sampleRef21endColumn = 4;

    private boolean             firstRun             = true;

    private FullGraphSession    fullGraphSession;

    private SimpleGraphSession  simpleGraphSession;

    protected Injector          injector;

    private void createSampleLineRefs(
                                      final Element link1) {
        link1.createLineReference(sampleRef11beginLine, sampleRef11endLine, sampleRef11beginColumn, sampleRef11endColumn,
            sampleLineRef1, sampleArtifact1);
        link1.createLineReference(sampleRef12beginLine, sampleRef12endLine, sampleRef12beginColumn, sampleRef12endColumn,
            sampleLineRef2, sampleArtifact1);
        link1.createLineReference(sampleRef13beginLine, sampleRef13endLine, sampleRef13beginColumn, sampleRef13endColumn,
            sampleLineRef2, sampleArtifact1);
        link1.createLineReference(sampleRef21beginLine, sampleRef21endLine, sampleRef21beginColumn, sampleRef21endColumn,
            sampleLineRef3, sampleArtifact2);
    }

    private void testLineRefs(
                              final GraphReader reader,
                              final Element link) {
        final TreeLineReference lineRefs = reader.getTreeLineReferences(link);
        boolean hasArtifact1 = false, hasArtifact2 = false, hasLineRef1 = false, hasLineRef2 = false;
        boolean hasLineRef4 = false;

        for (final ArtifactLineReference ref: lineRefs.getArtifacts()) {
            if (sampleArtifact1.equals(ref.getArtifactId())) {
                if (hasArtifact1) {
                    fail();
                }
                hasArtifact1 = true;
                if (ref.getStatements().iterator().hasNext() == false) {
                    fail();
                }
                for (final StatementLineReference stm: ref.getStatements()) {
                    if (sampleLineRef1.equals(stm.getStatement())) {
                        if (stm.getLineReferences().iterator().hasNext() == false) {
                            fail();
                        }
                        for (final SimpleLineReference lineRef: stm.getLineReferences()) {
                            if (lineRef.getBeginLine() == sampleRef11beginLine
                                && lineRef.getEndLine() == sampleRef11endLine
                                && lineRef.getBeginColumn() == sampleRef11beginColumn
                                && lineRef.getEndColumn() == sampleRef11endColumn) {
                                if (hasLineRef1) {
                                    fail();
                                }
                                hasLineRef1 = true;
                            } else if (lineRef.getBeginLine() == sampleRef12beginLine
                                && lineRef.getEndLine() == sampleRef12endLine
                                && lineRef.getBeginColumn() == sampleRef12beginColumn
                                && lineRef.getEndColumn() == sampleRef12endColumn) {
                                if (hasLineRef2) {
                                    fail();
                                }
                                hasLineRef2 = true;
                            } else if (lineRef.getBeginLine() == sampleRef13beginLine
                                && lineRef.getEndLine() == sampleRef13endLine
                                && lineRef.getBeginColumn() == sampleRef13beginColumn
                                && lineRef.getEndColumn() == sampleRef13endColumn) {
                                if (hasLineRef2) {
                                    fail();
                                }
                                hasLineRef2 = true;
                            } else {
                                fail();
                            }
                        }
                    }
                }
            } else if (sampleArtifact2.equals(ref.getArtifactId())) {
                if (hasArtifact2) {
                    fail();
                }
                hasArtifact2 = true;
                if (ref.getStatements().iterator().hasNext() == false) {
                    fail();
                }
                for (final StatementLineReference stm: ref.getStatements()) {
                    if (sampleLineRef2.equals(stm.getStatement())) {
                        if (stm.getLineReferences().iterator().hasNext() == false) {
                            fail();
                        }
                        for (final SimpleLineReference lineRef: stm.getLineReferences()) {
                            if (lineRef.getBeginLine() == sampleRef21beginLine
                                && lineRef.getEndLine() == sampleRef21endLine
                                && lineRef.getBeginColumn() == sampleRef21beginColumn
                                && lineRef.getEndColumn() == sampleRef21endColumn) {
                                if (hasLineRef4) {
                                    fail();
                                }
                                hasLineRef4 = true;
                            } else {
                                fail();
                            }
                        }
                    }
                }
            } else {
                fail();
            }
        }
    }

    protected abstract void clearData()
        throws Exception;

    protected String context1() {
        return "context1";
    }

    protected String context2() {
        return "context2";
    }

    protected abstract Injector createInjector()
        throws Exception;

    protected GraphLocation location() {
        return GraphLocation.SERVER;
    }

    @Before
    public void beforeTest()
        throws Exception {
        if (firstRun) {
            injector = createInjector();
            final GraphSessionFactory sessionFactory = injector
                .getInstance(GraphSessionFactory.class);
            simpleGraphSession = sessionFactory.openSimple();
            fullGraphSession = sessionFactory.openFull();
            firstRun = false;
        }
        clearData();
    }

    @Test
    public void shouldaddAndFindOneNodeInManyWays() {
        final String nodeName = "nodeName";
        final String caption1 = "caption1";
        final String transientValue = "transientValue";
        final String typeName = "typeName";
        final boolean publicClass = true;

        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final GraphWriter writer = fullGraphSession.toServer();

        final JavaType node1 = writer.addNode(context1, JavaType.class, nodeName);
        node1.setCaption(caption1);
        node1.setPublicClass(publicClass);
        node1.setTypeName(typeName);
        node1.setTransientValue(transientValue);
        fullGraphSession.toServer().flush();

        final Iterable<JavaType> oneNode1 = simpleFromLocation.findNodesByName(
            JavaType.class, nodeName, true, context1);
        final Iterable<JavaType> oneNode3 = simpleFromLocation.findNodesByName(
            JavaType.class, nodeName, true, context1);
        final Iterable<JavaType> oneNode4 = simpleFromLocation.findNodesByName(
            JavaType.class, nodeName, true, context1);

        final Iterator<JavaType> itOneNode1 = oneNode1.iterator();
        final Iterator<JavaType> itOneNode3 = oneNode3.iterator();
        final Iterator<JavaType> itOneNode4 = oneNode4.iterator();

        assertThat(itOneNode1.hasNext(), is(true));
        assertThat(itOneNode3.hasNext(), is(true));
        assertThat(itOneNode4.hasNext(), is(true));

        final JavaType sameNode1 = itOneNode1.next();
        final JavaType sameNode3 = itOneNode3.next();
        final JavaType sameNode4 = itOneNode4.next();

        assertThat(sameNode1, is(notNullValue()));
        assertThat(sameNode3, is(notNullValue()));
        assertThat(sameNode4, is(notNullValue()));

        assertThat(sameNode1, is(sameNode3));
        assertThat(sameNode1, is(sameNode4));

        assertThat(sameNode1.getCaption(), is(caption1));
        assertThat(sameNode1.isPublicClass(), is(publicClass));
        assertThat(sameNode1.getTypeName(), is(typeName));
        assertThat(sameNode1.getTransientValue(), is(nullValue()));

        assertThat(sameNode3.getCaption(), is(caption1));
        assertThat(sameNode3.isPublicClass(), is(publicClass));
        assertThat(sameNode3.getTypeName(), is(typeName));
        assertThat(sameNode3.getTransientValue(), is(nullValue()));

        assertThat(sameNode4.getCaption(), is(caption1));
        assertThat(sameNode4.isPublicClass(), is(publicClass));
        assertThat(sameNode4.getTypeName(), is(typeName));
        assertThat(sameNode4.getTransientValue(), is(nullValue()));

        assertThat(simpleFromLocation.getContext(sameNode1), is(context1));
        assertThat(simpleFromLocation.getContext(sameNode3), is(context1));
        assertThat(simpleFromLocation.getContext(sameNode4), is(context1));
    }

    @Test
    public void shouldaddAndRetrieveBidirectionalLinksOnDiferentContext()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final Context context2 = simpleFromLocation.getContext(context2());
        final GraphWriter writer = fullGraphSession.toServer();
        final String rootClass1 = "rootClass1";
        final String rootClass2 = "rootClass2";
        final String rootClass3 = "rootClass3";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaType rootClass2Node = writer.addNode(context2, JavaType.class,
            rootClass2);
        final JavaType rootClass3Node = writer.addNode(context1, JavaType.class,
            rootClass3);
        final TypeExtends link1 = writer.addBidirectionalLink(TypeExtends.class,
            rootClass1Node, rootClass2Node);
        final TypeExtends link2 = writer.addBidirectionalLink(TypeExtends.class,
            rootClass1Node, rootClass3Node);

        final TypeExtends link3 = writer.addBidirectionalLink(TypeExtends.class,
            rootClass2Node, rootClass1Node);
        final TypeExtends link4 = writer.addBidirectionalLink(TypeExtends.class,
            rootClass3Node, rootClass1Node);

        writer.flush();

        assertThat(link1, is(link3));
        assertThat(link2, is(link4));

        final List<Link> twoLinks = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.BIDIRECTIONAL));
        assertThat(twoLinks.size(), is(2));
        assertThat(twoLinks.contains(link1), is(true));
        assertThat(twoLinks.contains(link2), is(true));

        final List<Link> emptyLinks = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.UNIDIRECTIONAL));
        assertThat(emptyLinks.size(), is(0));

        final List<Link> linkFromNode2 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass2Node, null, LinkDirection.BIDIRECTIONAL));
        assertThat(linkFromNode2.size(), is(1));
        assertThat(linkFromNode2.contains(link1), is(true));
        assertThat(linkFromNode2.contains(link2), is(false));

        final List<Link> linkFromNode3 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass3Node, null, LinkDirection.BIDIRECTIONAL));
        assertThat(linkFromNode3.size(), is(1));
        assertThat(linkFromNode3.contains(link2), is(true));
        assertThat(linkFromNode3.contains(link1), is(false));
    }

    @Test
    public void shouldaddAndRetrieveBidirectionalLinksOnSameContext()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final GraphWriter writer = fullGraphSession.toServer();
        final String rootClass1 = "rootClass1";
        final String rootClass2 = "rootClass2";
        final String rootClass3 = "rootClass3";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaType rootClass2Node = writer.addNode(context1, JavaType.class,
            rootClass2);
        final JavaType rootClass3Node = writer.addNode(context1, JavaType.class,
            rootClass3);
        final TypeExtends link1 = writer.addBidirectionalLink(TypeExtends.class,
            rootClass1Node, rootClass2Node);
        final TypeExtends link2 = writer.addBidirectionalLink(TypeExtends.class,
            rootClass1Node, rootClass3Node);

        final TypeExtends link3 = writer.addBidirectionalLink(TypeExtends.class,
            rootClass2Node, rootClass1Node);
        final TypeExtends link4 = writer.addBidirectionalLink(TypeExtends.class,
            rootClass3Node, rootClass1Node);

        writer.flush();

        assertThat(link1, is(link3));
        assertThat(link2, is(link4));

        final List<Link> emptyLinks = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.UNIDIRECTIONAL));
        assertThat(emptyLinks.size(), is(0));

        final List<Link> linkFromNode2 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass2Node, null, LinkDirection.BIDIRECTIONAL));
        assertThat(linkFromNode2.size(), is(1));
        assertThat(linkFromNode2.contains(link1), is(true));
        assertThat(linkFromNode2.contains(link2), is(false));

        final List<Link> linkFromNode3 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass3Node, null, LinkDirection.BIDIRECTIONAL));
        assertThat(linkFromNode3.size(), is(1));
        assertThat(linkFromNode3.contains(link2), is(true));
        assertThat(linkFromNode3.contains(link1), is(false));

        final List<Link> twoLinks2 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass2Node, null, LinkDirection.BIDIRECTIONAL));
        assertThat(twoLinks2.size(), is(1));
        assertThat(twoLinks2.contains(link1), is(true));
        assertThat(twoLinks2.contains(link2), is(false));

        final List<Link> twoLinks = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.BIDIRECTIONAL));
        assertThat(twoLinks.size(), is(2));
        assertThat(twoLinks.contains(link1), is(true));
        assertThat(twoLinks.contains(link2), is(true));

    }

    @Test
    public void shouldAddAndRetrieveLinksOnTwoPossibleOrdersOnDifferentContexts()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final Context context2 = simpleFromLocation.getContext(context2());
        final GraphWriter writer = fullGraphSession.toServer();
        final String rootClass1 = "rootClass1";
        final String rootClass2 = "rootClass2";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaType rootClass2Node = writer.addNode(context2, JavaType.class,
            rootClass2);
        final TypeExtends link1 = writer.addBidirectionalLink(TypeExtends.class,
            rootClass1Node, rootClass2Node);
        writer.flush();
        final List<Link> emptyLinks = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.UNIDIRECTIONAL));
        assertThat(emptyLinks.size(), is(0));
        final List<Link> emptyLinks2 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass2Node, null, LinkDirection.UNIDIRECTIONAL));
        assertThat(emptyLinks2.size(), is(0));

        final List<Link> linkFromNode1 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.BIDIRECTIONAL));
        assertThat(linkFromNode1.size(), is(1));
        assertThat(linkFromNode1.contains(link1), is(true));

        final List<Link> linkFromNode2 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass2Node, null, LinkDirection.BIDIRECTIONAL));
        assertThat(linkFromNode2.size(), is(1));
        assertThat(linkFromNode2.contains(link1), is(true));

        final List<Link> linkFromNode1any = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.ANY));
        assertThat(linkFromNode1any.size(), is(1));
        assertThat(linkFromNode1any.contains(link1), is(true));

        final List<Link> linkFromNode2Any = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.ANY));
        assertThat(linkFromNode2Any.size(), is(1));
        assertThat(linkFromNode2Any.contains(link1), is(true));

    }

    @Test
    public void shouldAddAndRetrieveLinksOnTwoPossibleOrdersOnSameContext()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final GraphWriter writer = fullGraphSession.toServer();
        final String rootClass1 = "rootClass1";
        final String rootClass2 = "rootClass2";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaType rootClass2Node = writer.addNode(context1, JavaType.class,
            rootClass2);
        final TypeExtends link1 = writer.addBidirectionalLink(TypeExtends.class,
            rootClass1Node, rootClass2Node);
        writer.flush();
        final List<Link> emptyLinks = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.UNIDIRECTIONAL));
        assertThat(emptyLinks.size(), is(0));
        final List<Link> emptyLinks2 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass2Node, null, LinkDirection.UNIDIRECTIONAL));
        assertThat(emptyLinks2.size(), is(0));

        final List<Link> linkFromNode1 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.BIDIRECTIONAL));

        final List<Link> linkFromNode2 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass2Node, null, LinkDirection.BIDIRECTIONAL));

        assertThat(linkFromNode1.size(), is(1));
        assertThat(linkFromNode1.contains(link1), is(true));
        assertThat(linkFromNode2.size(), is(1));
        assertThat(linkFromNode2.contains(link1), is(true));

        final List<Link> linkFromNode1any = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.ANY));
        assertThat(linkFromNode1any.size(), is(1));
        assertThat(linkFromNode1any.contains(link1), is(true));

        final List<Link> linkFromNode2Any = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.ANY));
        assertThat(linkFromNode2Any.size(), is(1));
        assertThat(linkFromNode2Any.contains(link1), is(true));

    }

    @Test
    public void shouldaddAndRetrieveUniAndBidirectionalLinksOnDiferentContext()
        throws Exception {

        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final Context context2 = simpleFromLocation.getContext(context2());
        final GraphWriter writer = fullGraphSession.toServer();

        final String rootClass1 = "rootClass1";
        final String rootClass2 = "rootClass2";
        final String rootClass3 = "rootClass3";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaType rootClass2Node = writer.addNode(context2, JavaType.class,
            rootClass2);
        final JavaType rootClass3Node = writer.addNode(context1, JavaType.class,
            rootClass3);
        final TypeExtends link1 = writer.addLink(TypeExtends.class, rootClass1Node,
            rootClass2Node);
        final TypeExtends link2 = writer.addLink(TypeExtends.class, rootClass1Node,
            rootClass3Node);

        final String rootClass1bid = "rootClass1bid";
        final String rootClass2bid = "rootClass2bid";
        final String rootClass3bid = "rootClass3bid";
        final JavaType rootClass1BidNode = writer.addNode(context1, JavaType.class,
            rootClass1bid);
        final JavaType rootClass2BidNode = writer.addNode(context1, JavaType.class,
            rootClass2bid);
        final JavaType rootClass3BidNode = writer.addNode(context1, JavaType.class,
            rootClass3bid);
        final TypeExtends link1Bid = writer.addBidirectionalLink(TypeExtends.class,
            rootClass1BidNode, rootClass2BidNode);
        final TypeExtends link2Bid = writer.addBidirectionalLink(TypeExtends.class,
            rootClass1BidNode, rootClass3BidNode);

        final TypeExtends link3Bid = writer.addBidirectionalLink(TypeExtends.class,
            rootClass2BidNode, rootClass1BidNode);
        final TypeExtends link4Bid = writer.addBidirectionalLink(TypeExtends.class,
            rootClass3BidNode, rootClass1BidNode);

        writer.flush();

        assertThat(link1Bid, is(link3Bid));
        assertThat(link2Bid, is(link4Bid));

        final List<Link> twoBidLinks = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1BidNode, null, LinkDirection.BIDIRECTIONAL));
        assertThat(twoBidLinks.size(), is(2));
        assertThat(twoBidLinks.contains(link1Bid), is(true));
        assertThat(twoBidLinks.contains(link2Bid), is(true));

        final List<Link> bidLinkFromNode2 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass2BidNode, null, LinkDirection.BIDIRECTIONAL));
        assertThat(bidLinkFromNode2.size(), is(1));
        assertThat(bidLinkFromNode2.contains(link1Bid), is(true));
        assertThat(bidLinkFromNode2.contains(link2Bid), is(false));

        final List<Link> bidLinkFromNode3 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass3BidNode, null, LinkDirection.BIDIRECTIONAL));
        assertThat(bidLinkFromNode3.size(), is(1));
        assertThat(bidLinkFromNode3.contains(link2Bid), is(true));
        assertThat(bidLinkFromNode3.contains(link1Bid), is(false));
        final List<Link> twoLinks = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.UNIDIRECTIONAL));
        assertThat(twoLinks.size(), is(2));
        assertThat(twoLinks.contains(link1), is(true));
        assertThat(twoLinks.contains(link2), is(true));

        final List<Link> emptyLinks = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.BIDIRECTIONAL));
        assertThat(emptyLinks.size(), is(0));

    }

    @Test
    public void shouldaddAndRetrieveUniAndBidirectionalLinksOnSameContext()
        throws Exception {

        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final GraphWriter writer = fullGraphSession.toServer();

        final String rootClass1 = "rootClass1";
        final String rootClass2 = "rootClass2";
        final String rootClass3 = "rootClass3";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaType rootClass2Node = writer.addNode(context1, JavaType.class,
            rootClass2);
        final JavaType rootClass3Node = writer.addNode(context1, JavaType.class,
            rootClass3);
        final TypeExtends link1 = writer.addLink(TypeExtends.class, rootClass1Node,
            rootClass2Node);
        final TypeExtends link2 = writer.addLink(TypeExtends.class, rootClass1Node,
            rootClass3Node);

        final String rootClass1bid = "rootClass1bid";
        final String rootClass2bid = "rootClass2bid";
        final String rootClass3bid = "rootClass3bid";
        final JavaType rootClass1BidNode = writer.addNode(context1, JavaType.class,
            rootClass1bid);
        final JavaType rootClass2BidNode = writer.addNode(context1, JavaType.class,
            rootClass2bid);
        final JavaType rootClass3BidNode = writer.addNode(context1, JavaType.class,
            rootClass3bid);
        final TypeExtends link1Bid = writer.addBidirectionalLink(TypeExtends.class,
            rootClass1BidNode, rootClass2BidNode);
        final TypeExtends link2Bid = writer.addBidirectionalLink(TypeExtends.class,
            rootClass1BidNode, rootClass3BidNode);

        final TypeExtends link3Bid = writer.addBidirectionalLink(TypeExtends.class,
            rootClass2BidNode, rootClass1BidNode);
        final TypeExtends link4Bid = writer.addBidirectionalLink(TypeExtends.class,
            rootClass3BidNode, rootClass1BidNode);

        writer.flush();

        assertThat(link1Bid, is(link3Bid));
        assertThat(link2Bid, is(link4Bid));

        final List<Link> bidLinkFromNode2 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass2BidNode, null, LinkDirection.BIDIRECTIONAL));
        assertThat(bidLinkFromNode2.size(), is(1));
        assertThat(bidLinkFromNode2.contains(link1Bid), is(true));
        assertThat(bidLinkFromNode2.contains(link2Bid), is(false));

        final List<Link> bidLinkFromNode3 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass3BidNode, null, LinkDirection.BIDIRECTIONAL));
        assertThat(bidLinkFromNode3.size(), is(1));
        assertThat(bidLinkFromNode3.contains(link2Bid), is(true));
        assertThat(bidLinkFromNode3.contains(link1Bid), is(false));
        final List<Link> twoLinks = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.UNIDIRECTIONAL));
        assertThat(twoLinks.size(), is(2));
        assertThat(twoLinks.contains(link1), is(true));
        assertThat(twoLinks.contains(link2), is(true));

        final List<Link> emptyLinks = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.BIDIRECTIONAL));
        assertThat(emptyLinks.size(), is(0));

        final List<Link> twoBidLinks = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1BidNode, null, LinkDirection.BIDIRECTIONAL));
        assertThat(twoBidLinks.size(), is(2));
        assertThat(twoBidLinks.contains(link1Bid), is(true));
        assertThat(twoBidLinks.contains(link2Bid), is(true));

    }

    @Test
    public void shouldaddAndRetrieveUnidirectionalLinksOnDiferentContext()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final Context context2 = simpleFromLocation.getContext(context2());
        final GraphWriter writer = fullGraphSession.toServer();
        final String rootClass1 = "rootClass1";
        final String rootClass2 = "rootClass2";
        final String rootClass3 = "rootClass3";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaType rootClass2Node = writer.addNode(context2, JavaType.class,
            rootClass2);
        final JavaType rootClass3Node = writer.addNode(context1, JavaType.class,
            rootClass3);
        final TypeExtends link1 = writer.addLink(TypeExtends.class, rootClass1Node,
            rootClass2Node);
        final TypeExtends link2 = writer.addLink(TypeExtends.class, rootClass1Node,
            rootClass3Node);

        writer.flush();

        final List<Link> twoLinks = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.UNIDIRECTIONAL));
        assertThat(twoLinks.size(), is(2));
        assertThat(twoLinks.contains(link1), is(true));
        assertThat(twoLinks.contains(link2), is(true));

        final List<Link> emptyLinks = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.BIDIRECTIONAL));
        assertThat(emptyLinks.size(), is(0));

    }

    @Test
    public void shouldaddAndRetrieveUnidirectionalLinksOnSameContext()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final GraphWriter writer = fullGraphSession.toServer();
        final String rootClass1 = "rootClass1";
        final String rootClass2 = "rootClass2";
        final String rootClass3 = "rootClass3";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaType rootClass2Node = writer.addNode(context1, JavaType.class,
            rootClass2);
        final JavaType rootClass3Node = writer.addNode(context1, JavaType.class,
            rootClass3);
        final TypeExtends link1 = writer.addLink(TypeExtends.class, rootClass1Node,
            rootClass2Node);
        final TypeExtends link2 = writer.addLink(TypeExtends.class, rootClass1Node,
            rootClass3Node);

        final TypeExtends link3 = writer.addLink(TypeExtends.class, rootClass3Node, rootClass2Node);

        writer.flush();

        final List<Link> twoLinks = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.UNIDIRECTIONAL));
        assertThat(twoLinks.size(), is(2));
        assertThat(twoLinks.contains(link1), is(true));
        assertThat(twoLinks.contains(link2), is(true));

        final List<Link> emptyLinks = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.BIDIRECTIONAL));
        assertThat(emptyLinks.size(), is(0));

        final List<Link> linkFromNode3 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass3Node, null, LinkDirection.UNIDIRECTIONAL));
        assertThat(linkFromNode3.size(), is(1));
        assertThat(linkFromNode3.contains(link3), is(true));
        assertThat(linkFromNode3.contains(link2), is(false));
        assertThat(linkFromNode3.contains(link1), is(false));

    }

    @Test
    public void shouldaddAnHierarchy()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final GraphWriter writer = fullGraphSession.toServer();

        final String rootClass1 = "rootClass1";
        final JavaTypeClass rootClass1Node = writer.addNode(context1,
            JavaTypeClass.class, rootClass1);
        final String child1 = "child1";
        final String child2 = "child2";
        final String child3 = "child3";
        final JavaMemberField child1Node = writer.addChildNode(rootClass1Node,
            JavaMemberField.class, child1);
        assertThat(child1Node.getParentId(), is(rootClass1Node.getId()));
        final JavaMemberField child2Node = writer.addChildNode(rootClass1Node,
            JavaMemberField.class, child2);
        assertThat(child2Node.getParentId(), is(rootClass1Node.getId()));
        final JavaTypeInterface child3Node = writer.addChildNode(rootClass1Node,
            JavaTypeInterface.class, child3);
        assertThat(child3Node.getParentId(), is(rootClass1Node.getId()));

        final String rootClass2 = "rootClass2";
        final JavaTypeClass rootClass2Node = writer.addNode(context1,
            JavaTypeClass.class, rootClass2);
        final String child4 = "child4";
        final String child5 = "child5";
        final String child6 = "child6";
        final JavaMemberField child4Node = writer.addChildNode(rootClass2Node,
            JavaMemberField.class, child4);
        final JavaMemberField child5Node = writer.addChildNode(rootClass2Node,
            JavaMemberField.class, child5);
        final JavaTypeInterface child6Node = writer.addChildNode(rootClass2Node,
            JavaTypeInterface.class, child6);

        writer.flush();

        final Iterable<JavaTypeClass> rootNodes = simpleFromLocation.findNodesByName(
            JavaTypeClass.class, "rootClass1", true, context1);

        final Iterator<JavaTypeClass> rootNodesIt = rootNodes.iterator();

        assertThat(rootNodesIt.hasNext(), is(true));
        final JavaTypeClass retrievedRootClass1 = rootNodesIt.next();
        assertThat(retrievedRootClass1, is(rootClass1Node));
        final List<JavaMember> children1AsSet = SLCollections.iterableToList(simpleFromLocation
            .getChildrenNodes(retrievedRootClass1, JavaMember.class));
        assertThat(children1AsSet.contains(child1Node), is(true));
        assertThat(children1AsSet.contains(child2Node), is(true));
        assertThat(children1AsSet.contains(child3Node), is(false));
        assertThat(children1AsSet.size(), is(2));

        final List<JavaType> children2AsSet = SLCollections.iterableToList(simpleFromLocation
            .getChildrenNodes(retrievedRootClass1, JavaType.class));
        assertThat(children2AsSet.contains(child1Node), is(false));
        assertThat(children2AsSet.contains(child2Node), is(false));
        assertThat(children2AsSet.contains(child3Node), is(true));
        assertThat(children2AsSet.size(), is(1));

        final Iterable<JavaTypeClass> rootNodes2 = simpleFromLocation
            .findNodesByName(JavaTypeClass.class, "rootClass2", true,
                context1);

        final Iterator<JavaTypeClass> rootNodes2It = rootNodes2.iterator();

        assertThat(rootNodes2It.hasNext(), is(true));
        final JavaTypeClass retrievedRootClass2 = rootNodes2It.next();
        assertThat(retrievedRootClass2, is(rootClass2Node));
        final List<JavaMember> children3AsSet = SLCollections.iterableToList(simpleFromLocation
            .getChildrenNodes(retrievedRootClass2, JavaMember.class));
        assertThat(children3AsSet.contains(child4Node), is(true));
        assertThat(children3AsSet.contains(child5Node), is(true));
        assertThat(children3AsSet.contains(child6Node), is(false));
        assertThat(children3AsSet.size(), is(2));

        final List<JavaType> children4AsSet = SLCollections.iterableToList(simpleFromLocation
            .getChildrenNodes(retrievedRootClass2, JavaType.class));
        assertThat(children4AsSet.contains(child4Node), is(false));
        assertThat(children4AsSet.contains(child5Node), is(false));
        assertThat(children4AsSet.contains(child6Node), is(true));
        assertThat(children4AsSet.size(), is(1));

    }

    @Test
    public void shouldAllowDifferentTypesAndSameNames()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final GraphWriter writer = fullGraphSession.toServer();
        final String rootClass1 = "rootClass1";
        final Node rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        final Node rootClass2Node = writer.addNode(context1, JavaMember.class,
            rootClass1);
        writer.flush();

        assertThat(rootClass1Node, is(not(rootClass2Node)));

        final List<JavaType> foundNodes =
            SLCollections.iterableToList(simpleFromLocation.findNodesByName(JavaType.class, rootClass1, true, context1));
        final List<JavaMember> foundNodes2 =
            SLCollections.iterableToList(simpleFromLocation.findNodesByName(JavaMember.class, rootClass1, true, context1));
        assertThat(foundNodes.size(), is(1));
        assertThat(foundNodes2.size(), is(1));
    }

    @Test
    public void shouldChangeNodeProperties()
        throws Exception {
        final String firstCaption = "firstCaption";
        final boolean firstPublicClass = true;
        final String firstTypeName = "firstTypeName";
        final String secondCaption = "secondCaption";
        final boolean secondPublicClass = !firstPublicClass;
        final String secondTypeName = "secondTypeName";

        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final GraphWriter writer = fullGraphSession.toServer();

        final String rootClass1 = "rootClass1";
        final JavaType rootClassNode1 = writer.addNode(context1, JavaType.class,
            rootClass1);
        rootClassNode1.setCaption(firstCaption);
        rootClassNode1.setPublicClass(firstPublicClass);
        rootClassNode1.setTypeName(firstTypeName);
        writer.flush();
        final JavaType firstFound = SLCollections.firstOf(simpleFromLocation
            .findNodesByName(JavaType.class, null, true, context1));
        assertThat(firstFound.getCaption(), is(firstCaption));
        assertThat(firstFound.getTypeName(), is(firstTypeName));
        assertThat(firstFound.isPublicClass(), is(firstPublicClass));
        firstFound.setCaption(secondCaption);
        firstFound.setPublicClass(secondPublicClass);
        firstFound.setTypeName(secondTypeName);
        simpleGraphSession.flushChangedProperties(firstFound);

        final JavaType secondFound = SLCollections.firstOf(simpleFromLocation
            .findNodesByName(JavaType.class, null, true, context1));
        assertThat(secondFound.getTypeName(), is(secondTypeName));
        assertThat(secondFound.isPublicClass(), is(secondPublicClass));
        assertThat(secondFound.getCaption(), is(secondCaption));

    }

    @Test
    public void shouldChangeNodeTypeWhenUsingValidNodeType()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final GraphWriter writer = fullGraphSession.toServer();

        final String rootClass1 = "rootClass1";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        writer.flush();

        final JavaType rootClass2Node = writer.addNode(context1, JavaTypeClass.class,
            rootClass1);
        writer.flush();
        assertThat(rootClass1Node, is(rootClass2Node));

        final JavaType foundNode1 = SLCollections.firstOf(simpleFromLocation
            .findNodesByName(JavaType.class, rootClass1, true, context1));
        assertThat(foundNode1, is(rootClass2Node));
        assertThat(foundNode1 instanceof JavaTypeClass, is(true));
        final JavaType rootClass3Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        assertThat(rootClass3Node instanceof JavaTypeClass, is(true));

        writer.flush();

    }

    @Test
    public void shouldCreateLineReferencesOnLinks()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final GraphWriter writer = fullGraphSession.toServer();
        final String rootClass1 = "rootClass1";
        final String rootClass2 = "rootClass2";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaType rootClass2Node = writer.addNode(context1, JavaType.class,
            rootClass2);
        final Element link1 = writer.addBidirectionalLink(TypeExtends.class,
            rootClass1Node, rootClass2Node);

        createSampleLineRefs(link1);
        createSampleLineRefs(link1);// call it one more time to test if has any duplicate item

        writer.flush();

        final List<Link> oneLink = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.BIDIRECTIONAL));
        assertThat(oneLink.size(), is(1));
        assertThat(oneLink.contains(link1), is(true));
        final Element link = oneLink.iterator().next();
        testLineRefs(simpleFromLocation, link);

    }

    @Test
    public void shouldCreateLineReferencesOnNodes()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final GraphWriter writer = fullGraphSession.toServer();
        final String rootClass1 = "rootClass1";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        createSampleLineRefs(rootClass1Node);
        createSampleLineRefs(rootClass1Node);// call it one more time to test if has any duplicate item

        writer.flush();

        final List<JavaType> oneLink =
            SLCollections.iterableToList(simpleFromLocation.findNodesByName(
                JavaType.class, rootClass1, true, context1));
        assertThat(oneLink.size(), is(1));
        assertThat(oneLink.contains(rootClass1Node), is(true));
        final Element link = oneLink.iterator().next();
        testLineRefs(simpleFromLocation, link);
    }

    @Test
    public void shouldHaveBiggerHeightsForInheritedTypes()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final Context context2 = simpleFromLocation.getContext(context2());
        final GraphWriter writer = fullGraphSession.toServer();

        final String rootClass1 = "rootClass1";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaTypeClass rootClass2Node = writer.addNode(context2,
            JavaTypeClass.class, rootClass1);
        writer.flush();
        assertThat(rootClass1Node.getNumericType().equals(
            rootClass2Node.getNumericType()), is(false));
        assertThat(rootClass1Node.getNumericType().compareTo(
            rootClass2Node.getNumericType()) < 0, is(true));
    }

    @Test
    public void shouldHaveDifferentWeightsForDifferentNodeTypes()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final Context context2 = simpleFromLocation.getContext(context2());
        final GraphWriter writer = fullGraphSession.toServer();

        final String rootClass1 = "rootClass1";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaMember rootClass2Node = writer.addNode(context2, JavaMember.class,
            rootClass1);
        writer.flush();
        assertThat(rootClass1Node.getNumericType().equals(
            rootClass2Node.getNumericType()), is(false));

    }

    @Test
    public void shouldHaveSameNodesOnDifferentContexts()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final Context context2 = simpleFromLocation.getContext(context2());
        final GraphWriter writer = fullGraphSession.toServer();

        final String rootClass1 = "rootClass1";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaType rootClass2Node = writer.addNode(context2, JavaType.class,
            rootClass1);
        writer.flush();

        final List<JavaType> result = SLCollections.iterableToList(simpleFromLocation
            .findNodesByName(JavaType.class, null, true, context1, context2));
        assertThat(result.size(), is(2));
        assertThat(result.contains(rootClass1Node), is(true));
        assertThat(result.contains(rootClass2Node), is(true));
        assertThat(rootClass1Node.equals(rootClass2Node), is(false));

    }

    @Test
    public void shouldInsertAndFindOneNodeAndNotFindInvalidOne()
        throws Exception {
        final String nodeName = "nodeName";
        final String invalidNodeName = "invalidNodeName";

        final String caption1 = "caption1";
        final String transientValue = "transientValue";
        final String typeName = "typeName";
        final boolean publicClass = true;

        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final GraphWriter writer = fullGraphSession.toServer();

        final JavaType node1 = writer.addNode(context1, JavaType.class, nodeName);
        node1.setCaption(caption1);
        node1.setPublicClass(publicClass);
        node1.setTypeName(typeName);
        node1.setTransientValue(transientValue);
        fullGraphSession.toServer().flush();

        final Iterable<Node> oneNode1 = simpleFromLocation.findNodesByName(nodeName,
            context1);

        final Iterator<Node> itOneNode1 = oneNode1.iterator();

        assertThat(itOneNode1.hasNext(), is(true));

        final JavaType sameNode1 = (JavaType) itOneNode1.next();

        assertThat(sameNode1, is(notNullValue()));

        assertThat(sameNode1.getCaption(), is(caption1));
        assertThat(sameNode1.isPublicClass(), is(publicClass));
        assertThat(sameNode1.getTypeName(), is(typeName));
        assertThat(sameNode1.getTransientValue(), is(nullValue()));

        assertThat(simpleFromLocation.getContext(sameNode1), is(context1));

        final Iterable<Node> empty1 = simpleFromLocation.findNodesByName(
            invalidNodeName, context1);
        final Iterator<Node> emptyIt1 = empty1.iterator();
        assertThat(emptyIt1.hasNext(), is(false));

    }

    @Test
    public void shouldNotFindInvalidNode()
        throws Exception {
        final String invalidNodeName = "invalidNodeName";
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final Iterable<Node> empty1 = simpleFromLocation.findNodesByName(
            invalidNodeName, context1);
        final Iterable<JavaType> empty3 = simpleFromLocation.findNodesByName(
            JavaType.class, invalidNodeName, true, context1);
        final Iterable<JavaType> empty4 = simpleFromLocation.findNodesByName(
            JavaType.class, invalidNodeName, true, context1);
        final Iterator<Node> emptyIt1 = empty1.iterator();
        final Iterator<JavaType> emptyIt3 = empty3.iterator();
        final Iterator<JavaType> emptyIt4 = empty4.iterator();

        assertThat(emptyIt1.hasNext(), is(false));
        assertThat(emptyIt3.hasNext(), is(false));
        assertThat(emptyIt4.hasNext(), is(false));
    }

    @Test
    public void shouldNotTransformNonAutoBidirectionalLinkOnDifferentContexts()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final Context context2 = simpleFromLocation.getContext(context2());
        final GraphWriter writer = fullGraphSession.toServer();
        final String rootClass1 = "rootClass1";
        final String rootClass2 = "rootClass2";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaType rootClass2Node = writer.addNode(context2, JavaType.class,
            rootClass2);
        final NonAutoBidLink link1 = writer.addLink(NonAutoBidLink.class,
            rootClass1Node, rootClass2Node);
        final NonAutoBidLink link2 = writer.addLink(NonAutoBidLink.class,
            rootClass2Node, rootClass1Node);

        writer.flush();

        assertThat(link1, is(not(link2)));

        final List<Link> oneLink = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.UNIDIRECTIONAL));

        final List<Link> oneLink2 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass2Node, null, LinkDirection.UNIDIRECTIONAL));
        assertThat(oneLink.size(), is(1));
        assertThat(oneLink.contains(link1), is(true));
        assertThat(oneLink2.size(), is(1));
        assertThat(oneLink2.contains(link2), is(true));
    }

    @Test
    public void shouldNotTransformNonAutoBidirectionalLinkOnSameContext()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final GraphWriter writer = fullGraphSession.toServer();
        final String rootClass1 = "rootClass1";
        final String rootClass2 = "rootClass2";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaType rootClass2Node = writer.addNode(context1, JavaType.class,
            rootClass2);
        final NonAutoBidLink link1 = writer.addLink(NonAutoBidLink.class,
            rootClass1Node, rootClass2Node);
        writer.flush();

        final NonAutoBidLink link2 = writer.addLink(NonAutoBidLink.class,
            rootClass2Node, rootClass1Node);

        writer.flush();

        assertThat(link1, is(not(link2)));

        final List<Link> oneLink = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.UNIDIRECTIONAL));
        assertThat(oneLink.size(), is(1));
        assertThat(oneLink.contains(link1), is(true));

        final List<Link> oneLink2 = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass2Node, null, LinkDirection.UNIDIRECTIONAL));
        assertThat(oneLink2.size(), is(1));
        assertThat(oneLink2.contains(link2), is(true));
    }

    @Test
    public void shouldRemoveChildNode()
        throws Exception {

        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final GraphWriter writer = fullGraphSession.toServer();

        final String rootClass1 = "rootClass1";
        final JavaType rootClassNode1 = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaType rootClassNode2 = writer.addChildNode(rootClassNode1,
            JavaType.class, rootClass1);
        writer.flush();
        final List<JavaType> nodes = SLCollections.iterableToList(simpleFromLocation.findNodesByName(
            JavaType.class, null, true, context1));

        assertThat(nodes.size(), is(2));
        assertThat(nodes.contains(rootClassNode1), is(true));
        assertThat(nodes.contains(rootClassNode2), is(true));
        writer.removeNode(rootClassNode2);
        writer.flush();
        final List<JavaType> nodes2 = SLCollections.iterableToList(simpleFromLocation
            .findNodesByName(JavaType.class, null, true, context1));

        assertThat(nodes2.size(), is(1));
        assertThat(nodes2.contains(rootClassNode1), is(true));
        assertThat(nodes2.contains(rootClassNode2), is(false));

    }

    @Test
    public void shouldRemoveNode()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final GraphWriter writer = fullGraphSession.toServer();

        final String rootClass1 = "rootClass1";
        final JavaType rootClassNode1 = writer.addNode(context1, JavaType.class,
            rootClass1);
        writer.flush();
        writer.removeNode(rootClassNode1);

        writer.flush();
        final Iterable<JavaType> empty = simpleFromLocation.findNodesByName(
            JavaType.class, null, true, context1);

        assertThat(empty.iterator().hasNext(), is(false));
    }

    @Test
    public void shouldRemoveParentAndChildNode()
        throws Exception {

        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final GraphWriter writer = fullGraphSession.toServer();

        final String rootClass1 = "rootClass1";
        final JavaType rootClassNode1 = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaType rootClassNode2 = writer.addChildNode(rootClassNode1,
            JavaType.class, rootClass1);
        writer.flush();
        final List<JavaType> nodes = SLCollections.iterableToList(simpleFromLocation.findNodesByName(
            JavaType.class, null, true, context1));

        assertThat(nodes.size(), is(2));
        assertThat(nodes.contains(rootClassNode1), is(true));
        assertThat(nodes.contains(rootClassNode2), is(true));
        writer.removeNode(rootClassNode1);
        writer.flush();
        final List<JavaType> nodes2 = SLCollections.iterableToList(simpleFromLocation
            .findNodesByName(JavaType.class, null, true, context1));

        assertThat(nodes2.contains(rootClassNode1), is(false));
        assertThat(nodes2.contains(rootClassNode2), is(false));
        assertThat(nodes2.size(), is(0));
    }

    @Test
    public void shouldTransformAutoBidirectionalLinkOnDifferentContexts()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final Context context2 = simpleFromLocation.getContext(context2());
        final GraphWriter writer = fullGraphSession.toServer();
        final String rootClass1 = "rootClass1";
        final String rootClass2 = "rootClass2";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaType rootClass2Node = writer.addNode(context2, JavaType.class,
            rootClass2);
        writer.addLink(AutoBidLink.class,
            rootClass1Node, rootClass2Node);
        writer.addLink(AutoBidLink.class,
            rootClass2Node, rootClass1Node);

        writer.flush();

        final List<Link> oneLink = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.ANY));
        final List<Link> anotherLink = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass2Node, null, LinkDirection.ANY));

        assertThat(oneLink.size(), is(1));
        assertThat(anotherLink.size(), is(1));
        assertThat(oneLink.iterator().next(), is(anotherLink.iterator().next()));
        assertThat(anotherLink.iterator().next().getDirection(), is(LinkDirection.BIDIRECTIONAL));
        assertThat(oneLink.iterator().next().getDirection(), is(LinkDirection.BIDIRECTIONAL));

        final AutoBidLink link1 = writer.addLink(AutoBidLink.class,
            rootClass1Node, rootClass2Node);
        final AutoBidLink link2 = writer.addLink(AutoBidLink.class,
            rootClass2Node, rootClass1Node);
        assertThat(link1.getDirection(), is(LinkDirection.BIDIRECTIONAL));
        assertThat(link2.getDirection(), is(LinkDirection.BIDIRECTIONAL));

        assertThat(link1, is(link2));
        assertThat(oneLink.contains(link1), is(true));
    }

    @Test
    public void shouldTransformAutoBidirectionalLinkOnSameContext()
        throws Exception {
        final GraphReader simpleFromLocation = simpleGraphSession.from(location());
        final Context context1 = simpleFromLocation.getContext(context1());
        final GraphWriter writer = fullGraphSession.toServer();
        final String rootClass1 = "rootClass1";
        final String rootClass2 = "rootClass2";
        final JavaType rootClass1Node = writer.addNode(context1, JavaType.class,
            rootClass1);
        final JavaType rootClass2Node = writer.addNode(context1, JavaType.class,
            rootClass2);
        writer.addLink(AutoBidLink.class,
            rootClass1Node, rootClass2Node);
        writer.addLink(AutoBidLink.class,
            rootClass2Node, rootClass1Node);

        writer.flush();

        final List<Link> oneLink = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass1Node, null, LinkDirection.ANY));
        final List<Link> anotherLink = SLCollections.iterableToList(simpleFromLocation.getLinks(
            rootClass2Node, null, LinkDirection.ANY));

        assertThat(oneLink.size(), is(1));
        assertThat(anotherLink.size(), is(1));
        assertThat(oneLink.iterator().next(), is(anotherLink.iterator().next()));
        assertThat(anotherLink.iterator().next().getDirection(), is(LinkDirection.BIDIRECTIONAL));
        assertThat(oneLink.iterator().next().getDirection(), is(LinkDirection.BIDIRECTIONAL));

        final AutoBidLink link1 = writer.addLink(AutoBidLink.class,
            rootClass1Node, rootClass2Node);
        final AutoBidLink link2 = writer.addLink(AutoBidLink.class,
            rootClass2Node, rootClass1Node);
        assertThat(link1.getDirection(), is(LinkDirection.BIDIRECTIONAL));
        assertThat(link2.getDirection(), is(LinkDirection.BIDIRECTIONAL));

        assertThat(link1, is(link2));
        assertThat(oneLink.contains(link1), is(true));

    }

}
