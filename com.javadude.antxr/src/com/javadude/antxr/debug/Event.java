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

import java.util.EventObject;

public abstract class Event extends EventObject {
	private int type;


	public Event(Object source) {
		super(source);
	}
	public Event(Object source, int type) {
		super(source);
		setType(type);
	}
	public int getType() {
		return type;
	}
	void setType(int type) {
		this.type = type;
	}
	/** This should NOT be called from anyone other than ParserEventSupport! */
	void setValues(int type) {
		setType(type);
	}
}
