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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Hashtable;

import com.javadude.antxr.ANTXRException;
import com.javadude.antxr.TokenStreamException;
import com.javadude.antxr.Tool;
import com.javadude.antxr.collections.impl.IndexedVector;

public class Hierarchy {
    protected Grammar LexerRoot = null;
    protected Grammar ParserRoot = null;
    protected Grammar TreeParserRoot = null;
    protected Hashtable symbols;	// table of grammars
    protected Hashtable files;	// table of grammar files read in
    protected Tool antxrTool;

    public Hierarchy(Tool tool) {
        this.antxrTool = tool;
        LexerRoot = new Grammar(tool, "Lexer", null, null);
        ParserRoot = new Grammar(tool, "Parser", null, null);
        TreeParserRoot = new Grammar(tool, "TreeParser", null, null);
        symbols = new Hashtable(10);
        files = new Hashtable(10);

        LexerRoot.setPredefined(true);
        ParserRoot.setPredefined(true);
        TreeParserRoot.setPredefined(true);

        symbols.put(LexerRoot.getName(), LexerRoot);
        symbols.put(ParserRoot.getName(), ParserRoot);
        symbols.put(TreeParserRoot.getName(), TreeParserRoot);
    }

    public void addGrammar(Grammar gr) {
        gr.setHierarchy(this);
        // add grammar to hierarchy
        symbols.put(gr.getName(), gr);
        // add grammar to file.
        GrammarFile f = getFile(gr.getFileName());
        f.addGrammar(gr);
    }

    public void addGrammarFile(GrammarFile gf) {
        files.put(gf.getName(), gf);
    }

    public void expandGrammarsInFile(String fileName) {
        GrammarFile f = getFile(fileName);
        for (Enumeration e = f.getGrammars().elements(); e.hasMoreElements();) {
            Grammar g = (Grammar)e.nextElement();
            g.expandInPlace();
        }
    }

    public Grammar findRoot(Grammar g) {
        if (g.getSuperGrammarName() == null) {		// at root
            return g;
        }
        // return root of super.
        Grammar sg = g.getSuperGrammar();
        if (sg == null) return g;		// return this grammar if super missing
        return findRoot(sg);
    }

    public GrammarFile getFile(String fileName) {
        return (GrammarFile)files.get(fileName);
    }

    public Grammar getGrammar(String gr) {
        return (Grammar)symbols.get(gr);
    }

    public static String optionsToString(IndexedVector options) {
        String s = "options {" + System.getProperty("line.separator");
        for (Enumeration e = options.elements(); e.hasMoreElements();) {
            s += e.nextElement() + System.getProperty("line.separator");
        }
        s += "}" +
            System.getProperty("line.separator") +
            System.getProperty("line.separator");
        return s;
    }

    public void readGrammarFile(String file) throws FileNotFoundException {
        Reader grStream = new BufferedReader(new FileReader(file));
        addGrammarFile(new GrammarFile(antxrTool, file));

        // Create the simplified grammar lexer/parser
        PreprocessorLexer ppLexer = new PreprocessorLexer(grStream);
        ppLexer.setFilename(file);
        Preprocessor pp = new Preprocessor(ppLexer);
		pp.setTool(antxrTool);
        pp.setFilename(file);

        // populate the hierarchy with class(es) read in
        try {
            pp.grammarFile(this, file);
        }
        catch (TokenStreamException io) {
            antxrTool.toolError("Token stream error reading grammar(s):\n" + io);
        }
        catch (ANTXRException se) {
            antxrTool.toolError("error reading grammar(s):\n" + se);
        }
    }

    /** Return true if hierarchy is complete, false if not */
    public boolean verifyThatHierarchyIsComplete() {
        boolean complete = true;
        // Make a pass to ensure all grammars are defined
        for (Enumeration e = symbols.elements(); e.hasMoreElements();) {
            Grammar c = (Grammar)e.nextElement();
            if (c.getSuperGrammarName() == null) {
                continue;		// at root: ignore predefined roots
            }
            Grammar superG = c.getSuperGrammar();
            if (superG == null) {
                antxrTool.toolError("grammar " + c.getSuperGrammarName() + " not defined");
                complete = false;
                symbols.remove(c.getName()); // super not defined, kill sub
            }
        }

        if (!complete) return false;

        // Make another pass to set the 'type' field of each grammar
        // This makes it easy later to ask a grammar what its type
        // is w/o having to search hierarchy.
        for (Enumeration e = symbols.elements(); e.hasMoreElements();) {
            Grammar c = (Grammar)e.nextElement();
            if (c.getSuperGrammarName() == null) {
                continue;		// ignore predefined roots
            }
            c.setType(findRoot(c).getName());
        }

        return true;
    }

    public Tool getTool() {
        return antxrTool;
    }

    public void setTool(Tool antxrTool) {
        this.antxrTool = antxrTool;
    }
}
