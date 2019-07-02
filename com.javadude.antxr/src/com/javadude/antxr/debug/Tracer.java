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

public class Tracer implements TraceListener {
	String indent=""; // TBD: should be StringBuffer


	protected void dedent() {
		if (indent.length() < 2)
			indent = "";
		else
			indent = indent.substring(2);
	}
	public void enterRule(TraceEvent e) {
		System.out.println(indent+e);
		indent();
	}
	public void exitRule(TraceEvent e) {
		dedent();
		System.out.println(indent+e);
	}
	protected void indent() {
		indent += "  ";
	}
	public void doneParsing(TraceEvent e) { /* do nothing */ }
	public void refresh() { /* do nothing */ }
}
