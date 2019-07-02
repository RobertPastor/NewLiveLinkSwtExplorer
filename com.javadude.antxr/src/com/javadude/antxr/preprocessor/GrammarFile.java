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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import com.javadude.antxr.Tool;
import com.javadude.antxr.collections.impl.IndexedVector;

/** Stores header action, grammar preamble, file options, and
 *  list of grammars in the file
 */
public class GrammarFile {
    protected String fileName;
    protected String headerAction = "";
    protected IndexedVector options;
    protected IndexedVector grammars;
    protected boolean expanded = false;	// any grammars expanded within?
	protected Tool tool;

    public GrammarFile(Tool tool, String f) {
        fileName = f;
        grammars = new IndexedVector();
        this.tool = tool;
    }

    public void addGrammar(Grammar g) {
        grammars.appendElement(g.getName(), g);
    }

    public void generateExpandedFile() throws IOException {
        if (!expanded) {
            return;	// don't generate if nothing got expanded
        }
        String expandedFileName = nameForExpandedGrammarFile(this.getName());

        // create the new grammar file with expanded grammars
        PrintWriter expF = tool.openOutputFile(expandedFileName);
        expF.println(toString());
        expF.close();
    }

    public IndexedVector getGrammars() {
        return grammars;
    }

    public String getName() {
        return fileName;
    }

    public String nameForExpandedGrammarFile(String f) {
        if (expanded) {
            // strip path to original input, make expanded file in current dir
            return "expanded" + tool.fileMinusPath(f);
        }
        return f;
    }

    public void setExpanded(boolean exp) {
        expanded = exp;
    }

    public void addHeaderAction(String a) {
        headerAction += a + System.getProperty("line.separator");
    }

    public void setOptions(IndexedVector o) {
        options = o;
    }

    public String toString() {
        String h = headerAction == null ? "" : headerAction;
        String o = options == null ? "" : Hierarchy.optionsToString(options);

        StringBuffer s = new StringBuffer(10000); s.append(h); s.append(o);
        for (Enumeration e = grammars.elements(); e.hasMoreElements();) {
            Grammar g = (Grammar)e.nextElement();
            s.append(g.toString());
        }
        return s.toString();
    }
}
