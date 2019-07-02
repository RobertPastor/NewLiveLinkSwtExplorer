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

/**Contains a list of all places that reference
 * this enclosing rule.  Useful for FOLLOW computations.
 */
class RuleEndElement extends BlockEndElement {
    protected Lookahead[] cache;	// Each rule can cache it's lookahead computation.
    // The FOLLOW(rule) is stored in this cache.
    // 1..k
    protected boolean noFOLLOW;


    public RuleEndElement(Grammar g) {
        super(g);
        cache = new Lookahead[g.maxk + 1];
    }

    public Lookahead look(int k) {
        return grammar.theLLkAnalyzer.look(k, this);
    }

    public String toString() {
        //return " [RuleEnd]";
        return "";
    }
}
