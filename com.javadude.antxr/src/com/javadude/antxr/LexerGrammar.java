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

import java.io.IOException;

import com.javadude.antxr.collections.impl.BitSet;

/** Lexer-specific grammar subclass */
class LexerGrammar extends Grammar {
    // character set used by lexer
    protected BitSet charVocabulary;
    // true if the lexer generates literal testing code for nextToken
    protected boolean testLiterals = true;
    // true if the lexer generates case-sensitive LA(k) testing
    protected boolean caseSensitiveLiterals = true;
    /** true if the lexer generates case-sensitive literals testing */
    protected boolean caseSensitive = true;
    /** true if lexer is to ignore all unrecognized tokens */
    protected boolean filterMode = false;

    /** if filterMode is true, then filterRule can indicate an optional
     *  rule to use as the scarf language.  If null, programmer used
     *  plain "filter=true" not "filter=rule".
     */
    protected String filterRule = null;

    LexerGrammar(String className_, Tool tool_, String superClass) {
        super(className_, tool_, superClass);
		// by default, use 0..127 for ASCII char vocabulary
		BitSet cv = new BitSet();
		for (int i = 0; i <= 127; i++) {
			cv.add(i);
		}
		setCharVocabulary(cv);

        // Lexer usually has no default error handling
        defaultErrorHandler = false;
    }

    /** Top-level call to generate the code	 */
    public void generate() throws IOException {
        generator.gen(this);
    }

    public String getSuperClass() {
        // If debugging, use debugger version of scanner
        if (debuggingOutput)
            return "debug.DebuggingCharScanner";
        return "CharScanner";
    }

    // Get the testLiterals option value
    public boolean getTestLiterals() {
        return testLiterals;
    }

    /**Process command line arguments.
     * -trace			have all rules call traceIn/traceOut
     * -traceLexer		have lexical rules call traceIn/traceOut
     * -debug			generate debugging output for parser debugger
     */
    public void processArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-trace")) {
                traceRules = true;
                antxrTool.setArgOK(i);
            }
            else if (args[i].equals("-traceLexer")) {
                traceRules = true;
                antxrTool.setArgOK(i);
            }
            else if (args[i].equals("-debug")) {
                debuggingOutput = true;
                antxrTool.setArgOK(i);
            }
        }
    }

    /** Set the character vocabulary used by the lexer */
    public void setCharVocabulary(BitSet b) {
        charVocabulary = b;
    }

    /** Set lexer options */
    public boolean setOption(String key, Token value) {
        String s = value.getText();
        if (key.equals("buildAST")) {
            antxrTool.warning("buildAST option is not valid for lexer", getFilename(), value.getLine(), value.getColumn());
            return true;
        }
        if (key.equals("testLiterals")) {
            if (s.equals("true")) {
                testLiterals = true;
            }
            else if (s.equals("false")) {
                testLiterals = false;
            }
            else {
                antxrTool.warning("testLiterals option must be true or false", getFilename(), value.getLine(), value.getColumn());
            }
            return true;
        }
        if (key.equals("interactive")) {
            if (s.equals("true")) {
                interactive = true;
            }
            else if (s.equals("false")) {
                interactive = false;
            }
            else {
                antxrTool.error("interactive option must be true or false", getFilename(), value.getLine(), value.getColumn());
            }
            return true;
        }
        if (key.equals("caseSensitive")) {
            if (s.equals("true")) {
                caseSensitive = true;
            }
            else if (s.equals("false")) {
                caseSensitive = false;
            }
            else {
                antxrTool.warning("caseSensitive option must be true or false", getFilename(), value.getLine(), value.getColumn());
            }
            return true;
        }
        if (key.equals("caseSensitiveLiterals")) {
            if (s.equals("true")) {
                caseSensitiveLiterals = true;
            }
            else if (s.equals("false")) {
                caseSensitiveLiterals = false;
            }
            else {
                antxrTool.warning("caseSensitiveLiterals option must be true or false", getFilename(), value.getLine(), value.getColumn());
            }
            return true;
        }
        if (key.equals("filter")) {
            if (s.equals("true")) {
                filterMode = true;
            }
            else if (s.equals("false")) {
                filterMode = false;
            }
            else if (value.getType() == ANTXRTokenTypes.TOKEN_REF) {
                filterMode = true;
                filterRule = s;
            }
            else {
                antxrTool.warning("filter option must be true, false, or a lexer rule name", getFilename(), value.getLine(), value.getColumn());
            }
            return true;
        }
        if (key.equals("longestPossible")) {
            antxrTool.warning("longestPossible option has been deprecated; ignoring it...", getFilename(), value.getLine(), value.getColumn());
            return true;
        }
        if (key.equals("className")) {
            super.setOption(key, value);
            return true;
        }
        if (super.setOption(key, value)) {
            return true;
        }
        antxrTool.error("Invalid option: " + key, getFilename(), value.getLine(), value.getColumn());
        return false;
    }
}
