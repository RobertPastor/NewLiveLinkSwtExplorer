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

public class CommonHiddenStreamToken extends CommonToken {
    protected CommonHiddenStreamToken hiddenBefore;
    protected CommonHiddenStreamToken hiddenAfter;

    public CommonHiddenStreamToken() {
        super();
    }

    public CommonHiddenStreamToken(int t, String txt) {
        super(t, txt);
    }

    public CommonHiddenStreamToken(String s) {
        super(s);
    }

    public CommonHiddenStreamToken getHiddenAfter() {
        return hiddenAfter;
    }

    public CommonHiddenStreamToken getHiddenBefore() {
        return hiddenBefore;
    }

    protected void setHiddenAfter(CommonHiddenStreamToken t) {
        hiddenAfter = t;
    }

    protected void setHiddenBefore(CommonHiddenStreamToken t) {
        hiddenBefore = t;
    }
}
