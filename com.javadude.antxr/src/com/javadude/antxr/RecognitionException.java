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

public class RecognitionException extends ANTXRException {
	private static final long serialVersionUID = 1L;
	public String fileName;		// not used by treeparsers
    public int line;
    public int column;

    public RecognitionException() {
        super("parsing error");
        fileName = null;
        line = -1;
        column = -1;
    }

    /**
     * RecognitionException constructor comment.
     * @param s java.lang.String
     */
    public RecognitionException(String s) {
        super(s);
        fileName = null;
        line = -1;
        column = -1;
    }

    /** @deprecated As of ANTXR 2.7.2 use {@see #RecognitionException(char, String, int, int) } */
    public RecognitionException(String s, String fileName_, int line_) {
        this(s, fileName_, line_, -1);
    }
    
    /**
     * RecognitionException constructor comment.
     * @param s java.lang.String
     */
    public RecognitionException(String s, String fileName_, int line_, int column_) {
        super(s);
        fileName = fileName_;
        line = line_;
        column = column_;
    }

    public String getFilename() {
        return fileName;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    /** @deprecated As of ANTXR 2.7.0 */
    public String getErrorMessage() {
        return getMessage();
    }

    public String toString() {
        return FileLineFormatter.getFormatter().
            getFormatString(fileName, line, column) + getMessage();
    }
}
