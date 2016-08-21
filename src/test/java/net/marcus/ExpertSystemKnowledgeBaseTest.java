package net.marcus;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class ExpertSystemKnowledgeBaseTest {

	private ExpertSystemKnowledgeBase expertSystemKnowledgeBase;
	
	@Before
	public void setUp() throws Exception {
		expertSystemKnowledgeBase = new ExpertSystemKnowledgeBase();
	}

	@Test
	public void shouldReturnSingleNode() {
		try {
			Node node = expertSystemKnowledgeBase.initialLoad("[\"node.text\"]");
			assertThat(node, is(notNullValue()));
			assertThat(node.getNodeText(), is("node.text"));

			Node nodeYes = node.getNodeYes();
			assertThat(nodeYes, is(nullValue()));
			
			Node nodeNo = node.getNodeNo();
			assertThat(nodeNo, is(nullValue()));
		} catch (IOException e) {
			fail(e.getMessage());
		} 
	}
	
	@Test
	public void shouldReturnNullWhenMalformedTreeWithNoSubtreesBegining() throws Exception {
		try {
			Node node = expertSystemKnowledgeBase.initialLoad("node.text\"]");
			assertThat(node, is(nullValue()));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void shouldReturnNullWhenMalformedTreeWithNoSubtreesEnd() throws Exception {
		try {
			Node node = expertSystemKnowledgeBase.initialLoad("[\"node.text");
			assertThat(node, is(nullValue()));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void shouldReturnNodeAndTwoSubtrees() throws Exception {
		try {
			Node node = expertSystemKnowledgeBase.initialLoad("[\"node.text\":[\"YES.subnode.text\"],[\"NO.subnode.text\"]]");
			assertThat(node, is(notNullValue()));
			
			Node nodeYes = node.getNodeYes();
			assertThat(nodeYes, is(notNullValue()));
			assertThat(nodeYes.getNodeText(), is("YES.subnode.text"));

			Node nodeNo = node.getNodeNo();
			assertThat(nodeNo, is(notNullValue()));
			assertThat(nodeNo.getNodeText(), is("NO.subnode.text"));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void shouldReturnNullWhenMalformedSubtree() throws Exception {
		try {
			Node node = expertSystemKnowledgeBase.initialLoad("[\"node.text\":[\"YES.subnode.text\"][\"NO.subnode.text\"]]");
			assertThat(node, is(nullValue()));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void shouldReturnNodeAndThreeSubtrees() throws Exception {
		try {
			Node node = expertSystemKnowledgeBase.initialLoad("[\"node.text\":[\"YES.subnode.text\":[\"YES.YES.subnode.text\"],[\"YES.NO.subnode.text\"]],[\"NO.subnode.text\"]]");
			assertThat(node, is(notNullValue()));
			
			Node nodeYes = node.getNodeYes();
			assertThat(nodeYes, is(notNullValue()));
			assertThat(nodeYes.getNodeText(), is("YES.subnode.text"));
			
			Node nodeNo = node.getNodeNo();
			assertThat(nodeNo, is(notNullValue()));
			assertThat(nodeNo.getNodeText(), is("NO.subnode.text"));
			assertThat(nodeNo.getNodeYes(), is(nullValue()));
			assertThat(nodeNo.getNodeNo(), is(nullValue()));

			nodeNo = nodeYes.getNodeNo();
			assertThat(nodeNo, is(notNullValue()));
			assertThat(nodeNo.getNodeText(), is("YES.NO.subnode.text"));

			nodeYes = nodeYes.getNodeYes();
			assertThat(nodeYes, is(notNullValue()));
			assertThat(nodeYes.getNodeText(), is("YES.YES.subnode.text"));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void shouldDoTheThingWithTheThing() throws Exception {
		try {
			Node node = expertSystemKnowledgeBase.initialLoad("[\"Is the car silent when you turn the key?\":[\"Are the battery terminals corroded?\":[\"Clean terminals and try starting again.\"],[\"Replace cables and try again.\"]],[\"Does the car make a clicking noise?\":[\"Replace the battery.\"],[\"Does the car crank up but fail to start?\":[\"Check spark plug connections.\"],[\"Does the engine start and then die?\":[\"Does your car have fuel injection?\":[\"Get it in for service.\"],[\"Check to ensure the choke is opening and closing.\"]],[\"EMPTY\"]]]]]");
			assertThat(node.getNodeNo().getNodeNo().getNodeNo().getNodeYes().getNodeYes().getNodeText(), is("Get it in for service."));
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
}
