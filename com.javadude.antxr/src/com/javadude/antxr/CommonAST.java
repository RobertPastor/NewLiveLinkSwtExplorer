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

import com.javadude.antxr.collections.AST;

/** Common AST node implementation */
public class CommonAST extends BaseAST {
	private static final long serialVersionUID = 1L;
	int ttype = Token.INVALID_TYPE;
    String text;


    /** Get the token text for this node */
    public String getText() {
        return text;
    }

    /** Get the token type for this node */
    public int getType() {
        return ttype;
    }

    public void initialize(int t, String txt) {
        setType(t);
        setText(txt);
    }

    public void initialize(AST t) {
        setText(t.getText());
        setType(t.getType());
    }

    public CommonAST() {
		// nothing
    }

    public CommonAST(Token tok) {
        initialize(tok);
    }

    public void initialize(Token tok) {
        setText(tok.getText());
        setType(tok.getType());
    }

    /** Set the token text for this node */
    public void setText(String text_) {
        text = text_;
    }

    /** Set the token type for this node */
    public void setType(int ttype_) {
        ttype = ttype_;
    }
}
