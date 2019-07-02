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

public interface InputBufferListener extends ListenerBase {


	public void inputBufferConsume(InputBufferEvent e);
	public void inputBufferLA(InputBufferEvent e);
	public void inputBufferMark(InputBufferEvent e);
	public void inputBufferRewind(InputBufferEvent e);
}
