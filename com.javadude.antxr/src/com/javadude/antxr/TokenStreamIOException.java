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

/**
 * Wraps an IOException in a TokenStreamException
 */
public class TokenStreamIOException extends TokenStreamException {
	private static final long serialVersionUID = 1L;
	public IOException io;

    /**
     * TokenStreamIOException constructor comment.
     * @param s java.lang.String
     */
    public TokenStreamIOException(IOException io) {
        super(io.getMessage());
        this.io = io;
    }
}
