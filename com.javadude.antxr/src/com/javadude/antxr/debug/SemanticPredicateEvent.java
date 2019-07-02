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

public class SemanticPredicateEvent extends GuessingEvent {
	private static final long serialVersionUID = 1L;
	public static final int VALIDATING=0;
	public static final int PREDICTING=1;
	private int condition;
	private boolean result;


	public SemanticPredicateEvent(Object source) {
		super(source);
	}
	public SemanticPredicateEvent(Object source, int type) {
		super(source, type);
	}
	public int getCondition() {
		return condition;
	}
	public boolean getResult() {
		return result;
	}
	void setCondition(int condition) {
		this.condition = condition;
	}
	void setResult(boolean result) {
		this.result = result;
	}
	/** This should NOT be called from anyone other than ParserEventSupport! */
	void setValues(int type, int condition, boolean result, int guessing) {
		super.setValues(type, guessing);
		setCondition(condition);
		setResult(result);
	}
	public String toString() {
		return "SemanticPredicateEvent [" + 
		       getCondition() + "," + getResult() + "," + getGuessing() + "]";
	}
}
