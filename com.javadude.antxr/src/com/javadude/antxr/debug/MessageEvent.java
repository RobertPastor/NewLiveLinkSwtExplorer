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

public class MessageEvent extends Event {
	private static final long serialVersionUID = 1L;
	private String text;
	public static int WARNING = 0;
	public static int ERROR = 1;


	public MessageEvent(Object source) {
		super(source);
	}
	public MessageEvent(Object source, int type, String text) {
		super(source);
		setValues(type,text);
	}
	public String getText() {
		return text;
	}
	void setText(String text) {
		this.text = text;
	}
	/** This should NOT be called from anyone other than ParserEventSupport! */
	void setValues(int type, String text) {
		super.setValues(type);
		setText(text);
	}
	public String toString() {
		return "ParserMessageEvent [" +
		       (getType()==WARNING?"warning,":"error,") +
		       getText() + "]";
	}
}
