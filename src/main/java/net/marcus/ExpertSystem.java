package net.marcus;

import java.util.Scanner;

public class ExpertSystem {
	
	private static final String YES = "yes";
	private static final String NO = "no";
	private static Node knowledgeBase = new Node("Oh no, the only thing I know is I don't know anything!");
	private static enum Answer {YES, NO};
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String args[]){
		initialiseHardcodedKnowledgeBase();
		interrogateKnowledgeBase();
	}

	private static void interrogateKnowledgeBase() {
		Node currentNode = knowledgeBase;
		Answer answer;
		do {
			System.out.print(currentNode.getNodeText());
			if(thereIsFurtherKnowledge(currentNode)){
				answer = retrieveAnswer();
				if(Answer.YES.equals(answer)){
					currentNode = currentNode.getNodeYes();
				} else {
					currentNode = currentNode.getNodeNo();
				}
			} else {
				currentNode = null;
			}
		} while(currentNode != null);
	}

	private static boolean thereIsFurtherKnowledge(Node node) {
		return node.getNodeYes() != null || node.getNodeNo() != null;
	}
	
	private static Answer retrieveAnswer() {
		String input = ""; 
		while (!YES.equals(input) && !NO.equals(input)){
			System.out.print(" (yes or no) ");
			input = scanner.next();			
		};
		
		if(YES.equals(input)){
			return Answer.YES;
		} 

		return Answer.NO;
	}

	//XXX To be replaced by the ExpertSystemKnowledgeBase loader and therefore not worthwihle to refactor or make clean
	private static void initialiseHardcodedKnowledgeBase() {
		knowledgeBase = new Node("Is the car silent when you turn the key?");
		
		Node n1 = new Node("Clean terminals and try starting again."), n2 = new Node("Replace cables and try again."), np = new Node("Are the battery terminals corroded?");
		np.setNodeYes(n1);
		np.setNodeNo(n2);
		knowledgeBase.setNodeYes(np);
		
		n1 = new Node("Get it in for service.");
		n2 = new Node("Check to ensure the choke is opening and closing.");
		np = new Node("Does your car have fuel injection?");
		np.setNodeYes(n1);
		np.setNodeNo(n2);
		
		n2 = new Node("Does the engine start and then die?");
		n2.setNodeYes(np);
		n1 = new Node("Check spark plug connections.");
		np = new Node("Does the car crank up but fail to start?");
		np.setNodeYes(n1);
		np.setNodeNo(n2);
		
		n2 = np;
		n1 = new Node("Replace the battery");
		np = new Node("Does the car make a clicking noise?");
		np.setNodeYes(n1);
		np.setNodeNo(n2);
		
		knowledgeBase.setNodeNo(np);
	}

	public static Node getKnowledgeBase() {
		return ExpertSystem.knowledgeBase;
	}

	public static void setKnowledgeBase(Node knowledgeBase) {
		ExpertSystem.knowledgeBase = knowledgeBase;
	}

}
