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

public class CommonToken extends Token {
    // most tokens will want line and text information
    protected int line;
    protected String text = null;
    protected int col;

    public CommonToken() {
		// nothing
    }

    public CommonToken(int t, String txt) {
        type = t;
        setText(txt);
    }

    public CommonToken(String s) {
        text = s;
    }

    public int getLine() {
        return line;
    }

    public String getText() {
        return text;
    }

    public void setLine(int l) {
        line = l;
    }

    public void setText(String s) {
        text = s;
    }

    public String toString() {
        return "[\"" + getText() + "\",<" + type + ">,line=" + line + ",col=" + col + "]";
    }

    /** Return token's start column */
    public int getColumn() {
        return col;
    }

    public void setColumn(int c) {
        col = c;
    }
}
