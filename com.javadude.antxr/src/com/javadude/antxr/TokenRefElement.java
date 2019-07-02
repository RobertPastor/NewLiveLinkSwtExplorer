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

class TokenRefElement extends GrammarAtom {

    public TokenRefElement(Grammar g,
                           Token t,
                           boolean inverted,
                           int autoGenType) {
        super(g, t, autoGenType);
        not = inverted;
        TokenSymbol ts = grammar.tokenManager.getTokenSymbol(atomText);
        if (ts == null) {
            g.antxrTool.error("Undefined token symbol: " +
                         atomText, grammar.getFilename(), t.getLine(), t.getColumn());
        }
        else {
            tokenType = ts.getTokenType();
            // set the AST node type to whatever was set in tokens {...}
            // section (if anything);
            // Lafter, after this is created, the element option can set this.
            setASTNodeType(ts.getASTNodeType());
        }
        line = t.getLine();
    }

    public void generate() {
        grammar.generator.gen(this);
    }

    public Lookahead look(int k) {
        return grammar.theLLkAnalyzer.look(k, this);
    }
}
