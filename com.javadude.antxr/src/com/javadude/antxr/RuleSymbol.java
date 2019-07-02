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

import com.javadude.antxr.collections.impl.Vector;

class RuleSymbol extends GrammarSymbol {
    RuleBlock block;	// list of alternatives
    boolean defined;	// has the rule been defined yet?
    Vector references;	// list of all nodes referencing this rule
    // not strictly needed by generic symbol table
    // but we will almost always analyze/gen code
    String access;	// access specifier for this rule
    String comment;	// A javadoc comment if any.

    public RuleSymbol(String r) {
        super(r);
        references = new Vector();
    }

    public void addReference(RuleRefElement e) {
        references.appendElement(e);
    }

    public RuleBlock getBlock() {
        return block;
    }

    public RuleRefElement getReference(int i) {
        return (RuleRefElement)references.elementAt(i);
    }

    public boolean isDefined() {
        return defined;
    }

    public int numReferences() {
        return references.size();
    }

    public void setBlock(RuleBlock rb) {
        block = rb;
    }

    public void setDefined() {
        defined = true;
    }
}
