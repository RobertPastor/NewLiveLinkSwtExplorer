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

/** A CommonAST whose initialization copies hidden token
 *  information from the Token used to create a node.
 */
public class CommonASTWithHiddenTokens extends CommonAST {
	private static final long serialVersionUID = 1L;
	protected CommonHiddenStreamToken hiddenBefore, hiddenAfter; // references to hidden tokens

    public CommonASTWithHiddenTokens() {
        super();
    }

    public CommonASTWithHiddenTokens(Token tok) {
        super(tok);
    }

    public CommonHiddenStreamToken getHiddenAfter() {
        return hiddenAfter;
    }

    public CommonHiddenStreamToken getHiddenBefore() {
        return hiddenBefore;
    }

    public void initialize(AST t)
    {
	hiddenBefore = ((CommonASTWithHiddenTokens)t).getHiddenBefore();
	hiddenAfter = ((CommonASTWithHiddenTokens)t).getHiddenAfter();
	super.initialize(t);
    }

    public void initialize(Token tok) {
        CommonHiddenStreamToken t = (CommonHiddenStreamToken)tok;
        super.initialize(t);
        hiddenBefore = t.getHiddenBefore();
        hiddenAfter = t.getHiddenAfter();
    }
}
