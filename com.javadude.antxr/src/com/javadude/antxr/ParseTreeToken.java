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
package com.javadude.antxr;

public class ParseTreeToken extends ParseTree {
	private static final long serialVersionUID = 1L;
	protected Token token;

	public ParseTreeToken(Token token) {
		this.token = token;
	}

	protected int getLeftmostDerivation(StringBuffer buf, int step) {
		buf.append(' ');
		buf.append(toString());
		return step; // did on replacements
	}

	public String toString() {
		if ( token!=null ) {
			return token.getText();
		}
		return "<missing token>";
	}
}
