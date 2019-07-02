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

public class InputBufferReporter implements InputBufferListener {


/**
 * doneParsing method comment.
 */
public void doneParsing(TraceEvent e) {
	/* do nothing */
}
	public void inputBufferChanged(InputBufferEvent e) {
		System.out.println(e);
	}
/**
 * charBufferConsume method comment.
 */
public void inputBufferConsume(InputBufferEvent e) {
	System.out.println(e);
}
/**
 * charBufferLA method comment.
 */
public void inputBufferLA(InputBufferEvent e) {
	System.out.println(e);
}
	public void inputBufferMark(InputBufferEvent e) {
		System.out.println(e);
	}
	public void inputBufferRewind(InputBufferEvent e) {
		System.out.println(e);
	}
/**
 * refresh method comment.
 */
public void refresh() {
	/* do nothing */
}
}
