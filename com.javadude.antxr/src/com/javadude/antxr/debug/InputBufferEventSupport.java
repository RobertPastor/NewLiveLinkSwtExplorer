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

public class InputBufferEventSupport {
	private Vector inputBufferListeners;
	private InputBufferEvent  inputBufferEvent;
	protected static final int CONSUME=0;
	protected static final int LA=1;
	protected static final int MARK=2;
	protected static final int REWIND=3;


	public InputBufferEventSupport(Object source) {
		inputBufferEvent = new InputBufferEvent(source);
	}
	public void addInputBufferListener(InputBufferListener l) {
		if (inputBufferListeners == null) inputBufferListeners = new Vector();
		inputBufferListeners.addElement(l);
	}
	public void fireConsume(char c) {
		inputBufferEvent.setValues(InputBufferEvent.CONSUME, c, 0);
		fireEvents(CONSUME, inputBufferListeners);		
	}
	public void fireEvent(int type, ListenerBase l) {
		switch(type) {
			case CONSUME: ((InputBufferListener)l).inputBufferConsume(inputBufferEvent); break;
			case LA:      ((InputBufferListener)l).inputBufferLA(inputBufferEvent); break;
			case MARK:    ((InputBufferListener)l).inputBufferMark(inputBufferEvent); break;
			case REWIND:  ((InputBufferListener)l).inputBufferRewind(inputBufferEvent); break;
			default:
				throw new IllegalArgumentException("bad type "+type+" for fireEvent()");
		}	
	}
	public void fireEvents(int type, Vector listeners) {
		Vector targets=null;
		ListenerBase l=null;
		
		synchronized (this) {
			if (listeners == null) return;
			targets = (Vector)listeners.clone();
		}
		
		if (targets != null)
			for (int i = 0; i < targets.size(); i++) {
				l = (ListenerBase)targets.elementAt(i);
				fireEvent(type, l);
			}
	}
	public void fireLA(char c, int la) {
		inputBufferEvent.setValues(InputBufferEvent.LA, c, la);
		fireEvents(LA, inputBufferListeners);
	}
	public void fireMark(int pos) {
		inputBufferEvent.setValues(InputBufferEvent.MARK, ' ', pos);
		fireEvents(MARK, inputBufferListeners);
	}
	public void fireRewind(int pos) {
		inputBufferEvent.setValues(InputBufferEvent.REWIND, ' ', pos);
		fireEvents(REWIND, inputBufferListeners);
	}
	public Vector getInputBufferListeners() {
		return inputBufferListeners;
	}
	protected void refresh(Vector listeners) {
		Vector v;
		synchronized (listeners) {
			v = (Vector)listeners.clone();
		}
		if (v != null)
			for (int i = 0; i < v.size(); i++)
				((ListenerBase)v.elementAt(i)).refresh();
	}
	public void refreshListeners() {
		refresh(inputBufferListeners);
	}
	public void removeInputBufferListener(InputBufferListener l) {
		if (inputBufferListeners != null)
			inputBufferListeners.removeElement(l);
	}
}
