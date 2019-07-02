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

public class SyntacticPredicateEvent extends GuessingEvent {
	private static final long serialVersionUID = 1L;
	public SyntacticPredicateEvent(Object source) {
		super(source);
	}
	public SyntacticPredicateEvent(Object source, int type) {
		super(source, type);
	}
	/** This should NOT be called from anyone other than ParserEventSupport! */
	void setValues(int type, int guessing) {
		super.setValues(type, guessing);
	}
	public String toString() {
		return "SyntacticPredicateEvent [" + getGuessing() + "]";
	}
}
