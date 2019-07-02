/*******************************************************************************
 * Copyright (c) 2005 Scott Stanchfield, http://javadude.com
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Based on the ANTLR parser generator by Terence Parr, http://antlr.org
 *******************************************************************************/
package com.javadude.antxr.debug;

public class ParserReporter extends Tracer implements ParserListener {


	public void parserConsume(ParserTokenEvent e) {
		System.out.println(indent+e);
	}
	public void parserLA(ParserTokenEvent e) {
		System.out.println(indent+e);
	}
	public void parserMatch(ParserMatchEvent e) {
		System.out.println(indent+e);
	}
	public void parserMatchNot(ParserMatchEvent e) {
		System.out.println(indent+e);
	}
	public void parserMismatch(ParserMatchEvent e) {
		System.out.println(indent+e);
	}
	public void parserMismatchNot(ParserMatchEvent e) {
		System.out.println(indent+e);
	}
	public void reportError(MessageEvent e) {
		System.out.println(indent+e);
	}
	public void reportWarning(MessageEvent e) {
		System.out.println(indent+e);
	}
	public void semanticPredicateEvaluated(SemanticPredicateEvent e) {
		System.out.println(indent+e);
	}
	public void syntacticPredicateFailed(SyntacticPredicateEvent e) {
		System.out.println(indent+e);
	}
	public void syntacticPredicateStarted(SyntacticPredicateEvent e) {
		System.out.println(indent+e);
	}
	public void syntacticPredicateSucceeded(SyntacticPredicateEvent e) {
		System.out.println(indent+e);
	}
}
