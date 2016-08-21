package net.marcus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;

import org.antlr.runtime.RecognitionException;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.LexerInterpreter;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserInterpreter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.LexerGrammar;

public class ExpertSystemKnowledgeBase {
	
	private final LexerGrammar lg;
	private final Grammar g;

	public ExpertSystemKnowledgeBase() throws RecognitionException {
		lg = new LexerGrammar(
				"lexer grammar L;\n" +
				"NODE_OPENER : '[' ;\n" +
				"NODE_CLOSER : ']' ;\n" +
				"NODE_TEXT_BODY_SEPARATOR : ':' ;\n" +
				"SUBTREE_YES_NO_SEPARATOR : ',' ;\n" +
				"NODE_TEXT_DELIM : '\"' ;\n" +
				"NODE_TEXT : ('a'..'z'|'A'..'Z'|'0'..'9'|','|'.'|'?'|'!'|' ')+ ;\n" +
				"WS : [ \t]+ -> skip;\n");
		g = new Grammar(
				"parser grammar T;\n" +
				"node : NODE_OPENER node_text (NODE_TEXT_BODY_SEPARATOR node_body)? NODE_CLOSER ;\n" +
				"node_text : NODE_TEXT_DELIM NODE_TEXT NODE_TEXT_DELIM ;\n" +
				"node_body : node SUBTREE_YES_NO_SEPARATOR node ;\n",
				lg);
	}

	public Node initialLoad(String input) throws IOException {
		if (inputNotValid(input)) {
			return null;
		}
		return load(new ByteArrayInputStream(input.getBytes()));
	}

	private Node load(InputStream inputStream) throws IOException {
		Node node = new Node();
		node.setNodeText(extractNodeText(inputStream));
		if(thereAreSubtrees(inputStream)){
			node.setNodeYes(load(inputStream));
			node.setNodeNo(load(inputStream));
		}
		return node;
	}

	private boolean thereAreSubtrees(InputStream inputStream) throws IOException {
		int nextVal = inputStream.read();
		return nextVal > 0 && '[' == (char)nextVal;
	}

	private String extractNodeText(InputStream inputStream) throws IOException {
		char currentChar;
		StringBuffer extractedText = new StringBuffer();
		do {
			currentChar = (char)inputStream.read();
			if(notDelimCharacter(currentChar)){
				extractedText.append(currentChar);
			}
		} while(notEndOfNodeText(currentChar));
		
		return extractedText.toString();
	}

	private boolean notDelimCharacter(char character) {
		return character != '['
				&& character != '"'
				&& character != ','
				&& notEndOfNodeText(character);
	}

	private boolean notEndOfNodeText(char character) {
		return character != ':' && character != ']';
	}
	
	private boolean inputNotValid(String input) {
		boolean error = false;
		
		LexerInterpreter lexEngine = lg.createLexerInterpreter(new ANTLRInputStream(input));
		CommonTokenStream tokens = new CommonTokenStream(lexEngine);
		ParserInterpreter parser = g.createParserInterpreter(tokens);
		parser.addErrorListener(new ANTLRErrorListener () {
			@Override
			public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
					int charPositionInLine, String msg, org.antlr.v4.runtime.RecognitionException e) {
				if(e != null) {
					throw e;
				} else {
					throw new RuntimeException(new InputMismatchException(parser));//InputMismatchException(parser);
				}
			}

			@Override
			public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact,
					BitSet ambigAlts, ATNConfigSet configs) {
			}

			@Override
			public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
					BitSet conflictingAlts, ATNConfigSet configs) {
			}

			@Override
			public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
					int prediction, ATNConfigSet configs) {
			}
		});
		
		try {
			ParserRuleContext context = parser.parse(0);
			error = context.exception != null; 
		} catch (RuntimeException e) {
			if(e instanceof InputMismatchException || e.getCause() instanceof InputMismatchException){
				error = true;
			}
		}
		
		return error;
	}

}
