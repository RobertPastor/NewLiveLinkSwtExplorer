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

class ExceptionSpec {
    // Non-null if this refers to a labeled rule
    // Use a token instead of a string to get the line information
    protected Token label;

    // List of ExceptionHandler (catch phrases)
    protected Vector handlers;


    public ExceptionSpec(Token label_) {
        label = label_;
        handlers = new Vector();
    }

    public void addHandler(ExceptionHandler handler) {
        handlers.appendElement(handler);
    }
}
