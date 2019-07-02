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

/**BlockContext stores the information needed when creating an
 * alternative (list of elements).  Entering a subrule requires
 * that we save this state as each block of alternatives
 * requires state such as "tail of current alternative."
 */
class BlockContext {
    AlternativeBlock block; // current block of alternatives
    int altNum;				// which alt are we accepting 0..n-1
    BlockEndElement blockEnd; // used if nested


    public void addAlternativeElement(AlternativeElement e) {
        currentAlt().addElement(e);
    }

    public Alternative currentAlt() {
        return (Alternative)block.alternatives.elementAt(altNum);
    }

    public AlternativeElement currentElement() {
        return currentAlt().tail;
    }
}
