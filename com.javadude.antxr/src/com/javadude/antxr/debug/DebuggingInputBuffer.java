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

import java.util.Vector;

import com.javadude.antxr.CharStreamException;
import com.javadude.antxr.InputBuffer;

public class DebuggingInputBuffer extends InputBuffer {
	private InputBuffer buffer;
	private InputBufferEventSupport inputBufferEventSupport;
	private boolean debugMode = true;


	public DebuggingInputBuffer(InputBuffer buffer) {
		this.buffer = buffer;
		inputBufferEventSupport = new InputBufferEventSupport(this);
	}
	public void addInputBufferListener(InputBufferListener l) {
	  inputBufferEventSupport.addInputBufferListener(l);
	}
	public void consume() {
		char la = ' ';
		try {la = buffer.LA(1);}
		catch (CharStreamException e) { /* do nothing */ } // vaporize it...
		buffer.consume();
		if (debugMode)
			inputBufferEventSupport.fireConsume(la);
	}
	public void fill(int a) throws CharStreamException {
		buffer.fill(a);
	}
	public Vector getInputBufferListeners() {
		return inputBufferEventSupport.getInputBufferListeners();
	}
	public boolean isDebugMode() {
		return debugMode;
	}
	public boolean isMarked() {
		return buffer.isMarked();
	}
	public char LA(int i) throws CharStreamException {
		char la = buffer.LA(i);
		if (debugMode)
			inputBufferEventSupport.fireLA(la,i);
		return la;
	}
	public int mark() {
		int m = buffer.mark();
		inputBufferEventSupport.fireMark(m);
		return m;
	}
	public void removeInputBufferListener(InputBufferListener l) {
	  if (inputBufferEventSupport != null)
	    inputBufferEventSupport.removeInputBufferListener(l);
	}
	public void rewind(int mark) {
		buffer.rewind(mark);
		inputBufferEventSupport.fireRewind(mark);
	}
	public void setDebugMode(boolean value) {
		debugMode = value;
	}
}
