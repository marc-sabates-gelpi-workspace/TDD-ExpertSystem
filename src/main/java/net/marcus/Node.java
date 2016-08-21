package net.marcus;

public class Node {
	
	private String nodeText = null;
	private Node nodeYes = null;
	private Node nodeNo = null;

	public String getNodeText() {
		return nodeText;
	}

	public void setNodeText(String nodeText) {
		this.nodeText = nodeText;
	}

	public Node getNodeYes() {
		return nodeYes;
	}

	public void setNodeYes(Node nodeYes) {
		this.nodeYes = nodeYes;
	}

	public Node getNodeNo() {
		return nodeNo;
	}

	public void setNodeNo(Node nodeNo) {
		this.nodeNo = nodeNo;
	}

}
