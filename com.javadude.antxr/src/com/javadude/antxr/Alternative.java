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

/** Intermediate data class holds information about an alternative */
class Alternative {
    // Tracking alternative linked list
    AlternativeElement head;   // head of alt element list
    AlternativeElement tail;  // last element added

    // Syntactic predicate block if non-null
    protected SynPredBlock synPred;
    // Semantic predicate action if non-null
    protected String semPred;
    // Exception specification if non-null
    protected ExceptionSpec exceptionSpec;
    // Init action if non-null;
    protected Lookahead[] cache;	// lookahead for alt.  Filled in by
    // deterministic() only!!!!!!!  Used for
    // code gen after calls to deterministic()
    // and used by deterministic for (...)*, (..)+,
    // and (..)? blocks.  1..k
    protected int lookaheadDepth;	// each alt has different look depth possibly.
    // depth can be NONDETERMINISTIC too.
    // 0..n-1
// If non-null, Tree specification ala -> A B C (not implemented)
    protected Token treeSpecifier = null;
    // True of AST generation is on for this alt
    private boolean doAutoGen;


    public Alternative() {
		// do nothing
    }

    public Alternative(AlternativeElement firstElement) {
        addElement(firstElement);
    }

    public void addElement(AlternativeElement e) {
        // Link the element into the list
        if (head == null) {
            head = tail = e;
        }
        else {
            tail.next = e;
            tail = e;
        }
    }

    public boolean atStart() {
        return head == null;
    }

    public boolean getAutoGen() {
        // Don't build an AST if there is a tree-rewrite-specifier
        return doAutoGen && treeSpecifier == null;
    }

    public Token getTreeSpecifier() {
        return treeSpecifier;
    }

    public void setAutoGen(boolean doAutoGen_) {
        doAutoGen = doAutoGen_;
    }
}
