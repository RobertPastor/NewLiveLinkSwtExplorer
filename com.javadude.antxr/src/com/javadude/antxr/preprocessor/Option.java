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
package com.javadude.antxr.preprocessor;

class Option {
    protected String name;
    protected String rhs;
    protected Grammar enclosingGrammar;

    public Option(String n, String rhs, Grammar gr) {
        name = n;
        this.rhs = rhs;
        setEnclosingGrammar(gr);
    }

    public Grammar getEnclosingGrammar() {
        return enclosingGrammar;
    }

    public String getName() {
        return name;
    }

    public String getRHS() {
        return rhs;
    }

    public void setEnclosingGrammar(Grammar g) {
        enclosingGrammar = g;
    }

    public void setName(String n) {
        name = n;
    }

    public void setRHS(String rhs) {
        this.rhs = rhs;
    }

    public String toString() {
        return "\t" + name + "=" + rhs;
    }
}
