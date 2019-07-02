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

public class NewLineEvent extends Event {
	private static final long serialVersionUID = 1L;
	private int line;


	public NewLineEvent(Object source) {
		super(source);
	}
	public NewLineEvent(Object source, int line) {
		super(source);
		setValues(line);
	}
	public int getLine() {
		return line;
	}
	void setLine(int line) {
		this.line = line;
	}
	/** This should NOT be called from anyone other than ParserEventSupport! */
	void setValues(int line) {
		setLine(line);
	}
	public String toString() {
		return "NewLineEvent [" + line + "]";
	}
}
