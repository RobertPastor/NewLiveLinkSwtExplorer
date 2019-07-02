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

public abstract class FileLineFormatter {

    private static FileLineFormatter formatter = new DefaultFileLineFormatter();

    public static FileLineFormatter getFormatter() {
        return formatter;
    }

    public static void setFormatter(FileLineFormatter f) {
        formatter = f;
    }

    /** @param fileName the file that should appear in the prefix. (or null)
     * @param line the line (or -1)
     * @param column the column (or -1)
     */
    public abstract String getFormatString(String fileName, int line, int column);
}
