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

public class TraceEvent extends GuessingEvent {
	private static final long serialVersionUID = 1L;
	private int ruleNum;
	private int data;
	public static int ENTER=0;
	public static int EXIT=1;
	public static int DONE_PARSING=2;


	public TraceEvent(Object source) {
		super(source);
	}
	public TraceEvent(Object source, int type, int ruleNum, int guessing, int data) {
		super(source);
		setValues(type, ruleNum, guessing, data);
	}
	public int getData() {
		return data;
	}
	public int getRuleNum() {
		return ruleNum;
	}
	void setData(int data) {
		this.data = data;
	}
	void setRuleNum(int ruleNum) {
		this.ruleNum = ruleNum;
	}
	/** This should NOT be called from anyone other than ParserEventSupport! */
	 void setValues(int type, int ruleNum, int guessing, int data) {
		super.setValues(type, guessing);
		setRuleNum(ruleNum);
		setData(data);
	}
	public String toString() {
		return "ParserTraceEvent [" + 
		       (getType()==ENTER?"enter,":"exit,") +
		       getRuleNum() + "," + getGuessing() +"]";
	}
}
