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

import com.javadude.antxr.collections.impl.Vector;

/** Interface that describes the set of defined tokens */
interface TokenManager {
    public Object clone();

    /** define a token symbol */
    public void define(TokenSymbol ts);

    /** Get the name of the token manager */
    public String getName();

    /** Get a token string by index */
    public String getTokenStringAt(int idx);

    /** Get the TokenSymbol for a string */
    public TokenSymbol getTokenSymbol(String sym);

    public TokenSymbol getTokenSymbolAt(int idx);

    /** Get an enumerator over the symbol table */
    public Enumeration getTokenSymbolElements();

    public Enumeration getTokenSymbolKeys();

    /** Get the token vocabulary (read-only).
     * @return A Vector of Strings indexed by token type */
    public Vector getVocabulary();

    /** Is this token manager read-only? */
    public boolean isReadOnly();

    public void mapToTokenSymbol(String name, TokenSymbol sym);

    /** Get the highest token type in use */
    public int maxTokenType();

    /** Get the next unused token type */
    public int nextTokenType();

    public void setName(String n);

    public void setReadOnly(boolean ro);

    /** Is a token symbol defined? */
    public boolean tokenDefined(String symbol);
}
