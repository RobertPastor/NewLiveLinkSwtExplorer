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

import java.util.Enumeration;
import java.util.Hashtable;

import com.javadude.antxr.collections.impl.Vector;

class SimpleTokenManager implements TokenManager, Cloneable {
    protected int maxToken = Token.MIN_USER_TYPE;
    // Token vocabulary is Vector of String's
    protected Vector vocabulary;
    // Hash table is a mapping from Strings to TokenSymbol
    private Hashtable table;
    // the ANTXR tool
    protected Tool antxrTool;
    // Name of the token manager
    protected String name;

    protected boolean readOnly = false;

    SimpleTokenManager(String name_, Tool tool_) {
        antxrTool = tool_;
        name = name_;
        // Don't make a bigger vector than we need, because it will show up in output sets.
        vocabulary = new Vector(1);
        table = new Hashtable();

        // define EOF symbol
        TokenSymbol ts = new TokenSymbol("EOF");
        ts.setTokenType(Token.EOF_TYPE);
        define(ts);

        // define <null-tree-lookahead> but only in the vocabulary vector
        vocabulary.ensureCapacity(Token.NULL_TREE_LOOKAHEAD);
        vocabulary.setElementAt("NULL_TREE_LOOKAHEAD", Token.NULL_TREE_LOOKAHEAD);
    }

    public Object clone() {
        SimpleTokenManager tm;
        try {
            tm = (SimpleTokenManager)super.clone();
            tm.vocabulary = (Vector)this.vocabulary.clone();
            tm.table = (Hashtable)this.table.clone();
            tm.maxToken = this.maxToken;
            tm.antxrTool = this.antxrTool;
            tm.name = this.name;
        }
        catch (CloneNotSupportedException e) {
            antxrTool.fatalError("panic: cannot clone token manager");
            return null;
        }
        return tm;
    }

    /** define a token */
    public void define(TokenSymbol ts) {
        // Add the symbol to the vocabulary vector
        vocabulary.ensureCapacity(ts.getTokenType());
        vocabulary.setElementAt(ts.getId(), ts.getTokenType());
        // add the symbol to the hash table
        mapToTokenSymbol(ts.getId(), ts);
    }

    /** Simple token manager doesn't have a name -- must be set externally */
    public String getName() {
        return name;
    }

    /** Get a token symbol by index */
    public String getTokenStringAt(int idx) {
        return (String)vocabulary.elementAt(idx);
    }

    /** Get the TokenSymbol for a string */
    public TokenSymbol getTokenSymbol(String sym) {
        return (TokenSymbol)table.get(sym);
    }

    /** Get a token symbol by index */
    public TokenSymbol getTokenSymbolAt(int idx) {
        return getTokenSymbol(getTokenStringAt(idx));
    }

    /** Get an enumerator over the symbol table */
    public Enumeration getTokenSymbolElements() {
        return table.elements();
    }

    public Enumeration getTokenSymbolKeys() {
        return table.keys();
    }

    /** Get the token vocabulary (read-only).
     * @return A Vector of TokenSymbol
     */
    public Vector getVocabulary() {
        return vocabulary;
    }

    /** Simple token manager is not read-only */
    public boolean isReadOnly() {
        return false;
    }

    /** Map a label or string to an existing token symbol */
    public void mapToTokenSymbol(String theName, TokenSymbol sym) {
        // System.out.println("mapToTokenSymbol("+name+","+sym+")");
        table.put(theName, sym);
    }

    /** Get the highest token type in use */
    public int maxTokenType() {
        return maxToken - 1;
    }

    /** Get the next unused token type */
    public int nextTokenType() {
        return maxToken++;
    }

    /** Set the name of the token manager */
    public void setName(String name_) {
        name = name_;
    }

    public void setReadOnly(boolean ro) {
        readOnly = ro;
    }

    /** Is a token symbol defined? */
    public boolean tokenDefined(String symbol) {
        return table.containsKey(symbol);
    }
}
