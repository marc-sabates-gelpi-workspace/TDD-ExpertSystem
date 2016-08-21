package net.marcus;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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
}
