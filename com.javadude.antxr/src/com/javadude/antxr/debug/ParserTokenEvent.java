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

public class ParserTokenEvent extends Event {
	private static final long serialVersionUID = 1L;
	private int value;
	private int amount;
	public static int LA=0;
	public static int CONSUME=1;


	public ParserTokenEvent(Object source) {
		super(source);
	}
	public ParserTokenEvent(Object source, int type,
	                        int amount, int value) {
		super(source);
		setValues(type,amount,value);
	}
	public int getAmount() {
		return amount;
	}
	public int getValue() {
		return value;
	}
	void setAmount(int amount) {
		this.amount = amount;
	}
	void setValue(int value) {
		this.value = value;
	}
	/** This should NOT be called from anyone other than ParserEventSupport! */
	void setValues(int type, int amount, int value) {
		super.setValues(type);
		setAmount(amount);
		setValue(value);
	}
	public String toString() {
		if (getType()==LA)
			return "ParserTokenEvent [LA," + getAmount() + "," +
			       getValue() + "]"; 
		return "ParserTokenEvent [consume,1," +
		       getValue() + "]"; 
	}
}
