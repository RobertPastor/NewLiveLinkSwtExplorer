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

/**
 * Anything that goes wrong while generating a stream of tokens.
 */
public class TokenStreamException extends ANTXRException {
	private static final long serialVersionUID = 1L;

	public TokenStreamException() {
		// nothing
    }

    public TokenStreamException(String s) {
        super(s);
    }

	public TokenStreamException(String message, Throwable cause) {
		super(message, cause);
	}

	public TokenStreamException(Throwable cause) {
		super(cause);
	}
}
