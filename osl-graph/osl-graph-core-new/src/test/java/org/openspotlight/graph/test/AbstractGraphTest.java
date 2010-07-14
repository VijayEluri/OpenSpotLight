package org.openspotlight.graph.test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.openspotlight.graph.SLContext;
import org.openspotlight.graph.SLFullGraphSession;
import org.openspotlight.graph.SLGraphLocation;
import org.openspotlight.graph.SLNode;
import org.openspotlight.graph.SLSimpleGraphSession;
import org.openspotlight.graph.manipulation.SLGraphReader;
import org.openspotlight.graph.manipulation.SLGraphWriter;
import org.openspotlight.graph.test.node.JavaMember;
import org.openspotlight.graph.test.node.JavaMemberField;
import org.openspotlight.graph.test.node.JavaType;
import org.openspotlight.graph.test.node.JavaTypeClass;
import org.openspotlight.graph.test.node.JavaTypeInterface;

import com.google.common.collect.ImmutableSet;

public abstract class AbstractGraphTest {

	private static <T> Set<T> iterableToSet(Iterable<T> iterable) {
		ImmutableSet.Builder<T> builder = ImmutableSet.builder();
		for (T t : iterable) {
			builder.add(t);
		}
		return builder.build();
	}

	protected abstract SLFullGraphSession createFullSession() throws Exception;

	protected abstract SLSimpleGraphSession createSimpleGraphSession()
			throws Exception;

	protected abstract void clearData() throws Exception;

	protected abstract SLGraphLocation location();

	protected abstract String context1();

	protected abstract String context2();

	private boolean firstRun = true;

	private SLFullGraphSession fullGraphSession;

	private SLSimpleGraphSession simpleGraphSession;

	@Before
	public void beforeTest() throws Exception {
		if (firstRun) {
			simpleGraphSession = createSimpleGraphSession();
			fullGraphSession = createFullSession();
			firstRun = false;
		}
		clearData();
	}

	@Test
	public void shouldCreateAndFindOneNodeInManyWays() {
		String nodeName = "nodeName";
		String caption1 = "caption1";
		String transientValue = "transientValue";
		String typeName = "typeName";
		boolean publicClass = true;

		SLGraphReader simpleFromLocation = simpleGraphSession
				.location(location());
		SLContext context1 = simpleFromLocation.getContext(context1());
		SLGraphWriter writer = fullGraphSession.toSync();

		JavaType node1 = writer.createNode(context1.getRootNode(),
				JavaType.class, nodeName);
		node1.setCaption(caption1);
		node1.setPublicClass(publicClass);
		node1.setTypeName(typeName);
		node1.setTransientValue(transientValue);
		fullGraphSession.toSync().save();

		Iterable<SLNode> oneNode1 = simpleFromLocation.findNodes(nodeName,
				context1);
		Iterable<JavaType> oneNode3 = simpleFromLocation.findNodes(
				JavaType.class, nodeName, context1);
		Iterable<JavaType> oneNode4 = simpleFromLocation.findNodes(
				JavaType.class, context1);

		Iterator<SLNode> itOneNode1 = oneNode1.iterator();
		Iterator<JavaType> itOneNode3 = oneNode3.iterator();
		Iterator<JavaType> itOneNode4 = oneNode4.iterator();

		assertThat(itOneNode1.hasNext(), is(true));
		assertThat(itOneNode3.hasNext(), is(true));
		assertThat(itOneNode4.hasNext(), is(true));

		JavaType sameNode1 = itOneNode1.next().doCast(JavaType.class);
		JavaType sameNode3 = itOneNode3.next();
		JavaType sameNode4 = itOneNode4.next();

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
	public void shouldNotFindInvalidNode() throws Exception {
		String invalidNodeName = "invalidNodeName";
		SLGraphReader simpleFromLocation = simpleGraphSession
				.location(location());
		SLContext context1 = simpleFromLocation.getContext(context1());
		Iterable<SLNode> empty1 = simpleFromLocation.findNodes(invalidNodeName,
				context1);
		Iterable<JavaType> empty3 = simpleFromLocation.findNodes(
				JavaType.class, invalidNodeName, context1);
		Iterable<JavaType> empty4 = simpleFromLocation.findNodes(
				JavaType.class, context1);
		Iterator<SLNode> emptyIt1 = empty1.iterator();
		Iterator<JavaType> emptyIt3 = empty3.iterator();
		Iterator<JavaType> emptyIt4 = empty4.iterator();

		assertThat(emptyIt1.hasNext(), is(false));
		assertThat(emptyIt3.hasNext(), is(false));
		assertThat(emptyIt4.hasNext(), is(false));
	}

	@Test
	public void shouldInsertAndFindOneNodeAndNotFindInvalidOne()
			throws Exception {
		String nodeName = "nodeName";
		String invalidNodeName = "invalidNodeName";

		String caption1 = "caption1";
		String transientValue = "transientValue";
		String typeName = "typeName";
		boolean publicClass = true;

		SLGraphReader simpleFromLocation = simpleGraphSession
				.location(location());
		SLContext context1 = simpleFromLocation.getContext(context1());
		SLGraphWriter writer = fullGraphSession.toSync();

		JavaType node1 = writer.createNode(context1.getRootNode(),
				JavaType.class, nodeName);
		node1.setCaption(caption1);
		node1.setPublicClass(publicClass);
		node1.setTypeName(typeName);
		node1.setTransientValue(transientValue);
		fullGraphSession.toSync().save();

		Iterable<SLNode> oneNode1 = simpleFromLocation.findNodes(nodeName,
				context1);

		Iterator<SLNode> itOneNode1 = oneNode1.iterator();

		assertThat(itOneNode1.hasNext(), is(true));

		JavaType sameNode1 = itOneNode1.next().doCast(JavaType.class);

		assertThat(sameNode1, is(notNullValue()));

		assertThat(sameNode1.getCaption(), is(caption1));
		assertThat(sameNode1.isPublicClass(), is(publicClass));
		assertThat(sameNode1.getTypeName(), is(typeName));
		assertThat(sameNode1.getTransientValue(), is(nullValue()));

		assertThat(simpleFromLocation.getContext(sameNode1), is(context1));

		Iterable<SLNode> empty1 = simpleFromLocation.findNodes(invalidNodeName,
				context1);
		Iterator<SLNode> emptyIt1 = empty1.iterator();
		assertThat(emptyIt1.hasNext(), is(false));

	}

	@Test
	public void shouldCreateAnHierarchy() throws Exception {
		SLGraphReader simpleFromLocation = simpleGraphSession
				.location(location());
		SLContext context1 = simpleFromLocation.getContext(context1());
		SLGraphWriter writer = fullGraphSession.toSync();

		String rootClass1 = "rootClass1";
		JavaTypeClass rootClass1Node = writer.createNode(
				context1.getRootNode(), JavaTypeClass.class, rootClass1);
		String child1 = "child1";
		String child2 = "child2";
		String child3 = "child3";
		JavaMemberField child1Node = writer.createNode(rootClass1Node,
				JavaMemberField.class, child1);
		JavaMemberField child2Node = writer.createNode(rootClass1Node,
				JavaMemberField.class, child2);
		JavaTypeInterface child3Node = writer.createNode(rootClass1Node,
				JavaTypeInterface.class, child3);

		String rootClass2 = "rootClass2";
		JavaTypeClass rootClass2Node = writer.createNode(
				context1.getRootNode(), JavaTypeClass.class, rootClass2);
		String child4 = "child4";
		String child5 = "child5";
		String child6 = "child6";
		JavaMemberField child4Node = writer.createNode(rootClass2Node,
				JavaMemberField.class, child4);
		JavaMemberField child5Node = writer.createNode(rootClass2Node,
				JavaMemberField.class, child5);
		JavaTypeInterface child6Node = writer.createNode(rootClass2Node,
				JavaTypeInterface.class, child6);

		writer.save();

		Iterable<JavaTypeClass> rootNodes = simpleFromLocation.findNodes(
				JavaTypeClass.class, "rootClass1", context1);

		Iterator<JavaTypeClass> rootNodesIt = rootNodes.iterator();

		assertThat(rootNodesIt.hasNext(), is(true));
		JavaTypeClass retrievedRootClass1 = rootNodesIt.next();
		assertThat(retrievedRootClass1, is(rootClass1Node));
		Set<JavaMember> children1AsSet = iterableToSet(simpleFromLocation
				.getChildrenNodes(retrievedRootClass1, JavaMember.class));
		assertThat(children1AsSet.contains(child1Node), is(true));
		assertThat(children1AsSet.contains(child2Node), is(true));
		assertThat(children1AsSet.contains(child3Node), is(false));
		assertThat(children1AsSet.size(), is(2));

		Set<JavaType> children2AsSet = iterableToSet(simpleFromLocation
				.getChildrenNodes(retrievedRootClass1, JavaType.class));
		assertThat(children2AsSet.contains(child1Node), is(false));
		assertThat(children2AsSet.contains(child2Node), is(false));
		assertThat(children2AsSet.contains(child3Node), is(true));
		assertThat(children2AsSet.size(), is(1));

		Iterable<JavaTypeClass> rootNodes2 = simpleFromLocation.findNodes(
				JavaTypeClass.class, "rootClass2", context1);

		Iterator<JavaTypeClass> rootNodes2It = rootNodes2.iterator();

		assertThat(rootNodes2It.hasNext(), is(true));
		JavaTypeClass retrievedRootClass2 = rootNodes2It.next();
		assertThat(retrievedRootClass2, is(rootClass2Node));
		Set<JavaMember> children3AsSet = iterableToSet(simpleFromLocation
				.getChildrenNodes(retrievedRootClass2, JavaMember.class));
		assertThat(children3AsSet.contains(child4Node), is(true));
		assertThat(children3AsSet.contains(child5Node), is(true));
		assertThat(children3AsSet.contains(child6Node), is(false));
		assertThat(children3AsSet.size(), is(2));

		Set<JavaType> children4AsSet = iterableToSet(simpleFromLocation
				.getChildrenNodes(retrievedRootClass2, JavaType.class));
		assertThat(children4AsSet.contains(child4Node), is(false));
		assertThat(children4AsSet.contains(child5Node), is(false));
		assertThat(children4AsSet.contains(child6Node), is(true));
		assertThat(children4AsSet.size(), is(1));

	}

	@Test(expected = ClassCastException.class)
	public void shouldNotDoInvalidCasts() throws Exception {
		SLGraphReader simpleFromLocation = simpleGraphSession
				.location(location());
		SLContext context1 = simpleFromLocation.getContext(context1());
		SLGraphWriter writer = fullGraphSession.toSync();

		String rootClass1 = "rootClass1";
		JavaTypeClass rootClass1Node = writer.createNode(
				context1.getRootNode(), JavaTypeClass.class, rootClass1);
		rootClass1Node.doCast(JavaMember.class);
	}

	@Test
	public void shouldChangeNodeTypeWhenUsingValidNodeType() throws Exception {
		SLGraphReader simpleFromLocation = simpleGraphSession
				.location(location());
		SLContext context1 = simpleFromLocation.getContext(context1());
		SLGraphWriter writer = fullGraphSession.toSync();

		String rootClass1 = "rootClass1";
		JavaType rootClass1Node = writer.createNode(context1.getRootNode(),
				JavaType.class, rootClass1);
		writer.save();

		JavaType rootClass2Node = writer.createNode(context1.getRootNode(),
				JavaTypeClass.class, rootClass1);
		writer.save();
		assertThat(rootClass1Node, is(rootClass2Node));

		JavaType foundNode1 = simpleFromLocation.findUniqueNode(JavaType.class,
				rootClass1, context1);
		assertThat(foundNode1, is(rootClass2Node));
		assertThat(foundNode1 instanceof JavaTypeClass, is(true));
		JavaType rootClass3Node = writer.createNode(context1.getRootNode(),
				JavaType.class, rootClass1);
		assertThat(rootClass3Node instanceof JavaTypeClass, is(true));

		writer.save();

	}

	@Test(expected = ClassCastException.class)
	public void shouldNotChangeNodeTypeWhenUsingInvalidNodeType()
			throws Exception {
		SLGraphReader simpleFromLocation = simpleGraphSession
				.location(location());
		SLContext context1 = simpleFromLocation.getContext(context1());
		SLGraphWriter writer = fullGraphSession.toSync();

		String rootClass1 = "rootClass1";
		writer.createNode(context1.getRootNode(), JavaType.class, rootClass1);
		writer.save();

		writer.createNode(context1.getRootNode(), JavaMember.class, rootClass1);
	}

	@Test
	public void shouldRemoveNode() throws Exception {
		SLGraphReader simpleFromLocation = simpleGraphSession
				.location(location());
		SLContext context1 = simpleFromLocation.getContext(context1());
		SLGraphWriter writer = fullGraphSession.toSync();

		String rootClass1 = "rootClass1";
		JavaType rootClassNode1 = writer.createNode(context1.getRootNode(),
				JavaType.class, rootClass1);
		writer.save();
		writer.removeNode(rootClassNode1);

		writer.save();
		Iterable<JavaType> empty = simpleFromLocation.findNodes(JavaType.class,
				context1);

		assertThat(empty.iterator().hasNext(), is(false));
	}

	@Test
	public void shouldRemoveChildNode() throws Exception {

		SLGraphReader simpleFromLocation = simpleGraphSession
				.location(location());
		SLContext context1 = simpleFromLocation.getContext(context1());
		SLGraphWriter writer = fullGraphSession.toSync();

		String rootClass1 = "rootClass1";
		JavaType rootClassNode1 = writer.createNode(context1.getRootNode(),
				JavaType.class, rootClass1);
		JavaType rootClassNode2 = writer.createNode(rootClassNode1,
				JavaType.class, rootClass1);
		writer.save();
		Set<JavaType> nodes = iterableToSet(simpleFromLocation.findNodes(
				JavaType.class, context1));

		assertThat(nodes.size(), is(2));
		assertThat(nodes.contains(rootClassNode1), is(true));
		assertThat(nodes.contains(rootClassNode2), is(true));
		writer.removeNode(rootClassNode2);
		writer.save();
		Set<JavaType> nodes2 = iterableToSet(simpleFromLocation.findNodes(
				JavaType.class, context1));

		assertThat(nodes2.size(), is(1));
		assertThat(nodes2.contains(rootClassNode1), is(true));
		assertThat(nodes2.contains(rootClassNode2), is(false));

	}

	@Test
	public void shouldRemoveParentAndChildNode() throws Exception {

		SLGraphReader simpleFromLocation = simpleGraphSession
				.location(location());
		SLContext context1 = simpleFromLocation.getContext(context1());
		SLGraphWriter writer = fullGraphSession.toSync();

		String rootClass1 = "rootClass1";
		JavaType rootClassNode1 = writer.createNode(context1.getRootNode(),
				JavaType.class, rootClass1);
		JavaType rootClassNode2 = writer.createNode(rootClassNode1,
				JavaType.class, rootClass1);
		writer.save();
		Set<JavaType> nodes = iterableToSet(simpleFromLocation.findNodes(
				JavaType.class, context1));

		assertThat(nodes.size(), is(2));
		assertThat(nodes.contains(rootClassNode1), is(true));
		assertThat(nodes.contains(rootClassNode2), is(true));
		writer.removeNode(rootClassNode1);
		writer.save();
		Set<JavaType> nodes2 = iterableToSet(simpleFromLocation.findNodes(
				JavaType.class, context1));

		assertThat(nodes2.size(), is(0));
	}

	@Test
	public void shouldChangeNodeProperties() throws Exception {
		String firstCaption = "firstCaption";
		boolean firstPublicClass = true;
		String firstTypeName = "firstTypeName";
		String secondCaption = "secondCaption";
		boolean secondPublicClass = !firstPublicClass;
		String secondTypeName = "secondTypeName";

		SLGraphReader simpleFromLocation = simpleGraphSession
				.location(location());
		SLContext context1 = simpleFromLocation.getContext(context1());
		SLGraphWriter writer = fullGraphSession.toSync();

		String rootClass1 = "rootClass1";
		JavaType rootClassNode1 = writer.createNode(context1.getRootNode(),
				JavaType.class, rootClass1);
		rootClassNode1.setCaption(firstCaption);
		rootClassNode1.setPublicClass(firstPublicClass);
		rootClassNode1.setTypeName(firstTypeName);
		writer.save();
		JavaType firstFound = simpleFromLocation.findUniqueNode(JavaType.class,
				context1);
		assertThat(firstFound.getCaption(), is(firstCaption));
		assertThat(firstFound.getTypeName(), is(firstTypeName));
		assertThat(firstFound.isPublicClass(), is(firstPublicClass));
		firstFound.setCaption(secondCaption);
		firstFound.setPublicClass(secondPublicClass);
		firstFound.setTypeName(secondTypeName);
		simpleGraphSession.flushChangedProperties(firstFound);

		JavaType secondFound = simpleFromLocation.findUniqueNode(
				JavaType.class, context1);
		assertThat(secondFound.getCaption(), is(secondCaption));
		assertThat(secondFound.getTypeName(), is(secondTypeName));
		assertThat(secondFound.isPublicClass(), is(secondPublicClass));

	}

	@Test
	public void shouldHaveSameNodesOnDifferentContexts() throws Exception {
		SLGraphReader simpleFromLocation = simpleGraphSession
				.location(location());
		SLContext context1 = simpleFromLocation.getContext(context1());
		SLContext context2 = simpleFromLocation.getContext(context2());
		SLGraphWriter writer = fullGraphSession.toSync();

		String rootClass1 = "rootClass1";
		JavaType rootClass1Node = writer.createNode(context1.getRootNode(),
				JavaType.class, rootClass1);
		JavaType rootClass2Node = writer.createNode(context2.getRootNode(),
				JavaType.class, rootClass1);
		writer.save();

		Set<JavaType> result = iterableToSet(simpleFromLocation.findNodes(
				JavaType.class, context1, context2));
		assertThat(result.size(), is(2));
		assertThat(result.contains(rootClass1Node), is(true));
		assertThat(result.contains(rootClass2Node), is(true));
		assertThat(rootClass1Node.equals(rootClass2Node), is(false));

	}

	@Test
	public void shouldHaveDifferentWeightsForDifferentNodeTypes()
			throws Exception {
		SLGraphReader simpleFromLocation = simpleGraphSession
				.location(location());
		SLContext context1 = simpleFromLocation.getContext(context1());
		SLContext context2 = simpleFromLocation.getContext(context2());
		SLGraphWriter writer = fullGraphSession.toSync();

		String rootClass1 = "rootClass1";
		JavaType rootClass1Node = writer.createNode(context1.getRootNode(),
				JavaType.class, rootClass1);
		JavaMember rootClass2Node = writer.createNode(context2.getRootNode(),
				JavaMember.class, rootClass1);
		writer.save();
		assertThat(rootClass1Node.getWeight()
				.equals(rootClass2Node.getWeight()), is(false));

	}

	@Test
	public void shouldHaveBiggerHeightsForInheritedTypes() throws Exception {
		SLGraphReader simpleFromLocation = simpleGraphSession
				.location(location());
		SLContext context1 = simpleFromLocation.getContext(context1());
		SLContext context2 = simpleFromLocation.getContext(context2());
		SLGraphWriter writer = fullGraphSession.toSync();

		String rootClass1 = "rootClass1";
		JavaType rootClass1Node = writer.createNode(context1.getRootNode(),
				JavaType.class, rootClass1);
		JavaTypeClass rootClass2Node = writer.createNode(
				context2.getRootNode(), JavaTypeClass.class, rootClass1);
		writer.save();
		assertThat(rootClass1Node.getWeight()
				.equals(rootClass2Node.getWeight()), is(false));
		assertThat(rootClass1Node.getWeight().compareTo(
				rootClass2Node.getWeight()) < 0, is(true));
	}

}