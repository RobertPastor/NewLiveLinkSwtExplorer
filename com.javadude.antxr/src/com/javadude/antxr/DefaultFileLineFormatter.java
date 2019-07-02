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

public class DefaultFileLineFormatter extends FileLineFormatter {
    public String getFormatString(String fileName, int line, int column) {
        StringBuffer buf = new StringBuffer();

        if (fileName != null)
            buf.append(fileName + ":");

        if (line != -1) {
            if (fileName == null)
                buf.append("line ");

            buf.append(line);

            if (column != -1)
                buf.append(":" + column);

            buf.append(":");
        }

        buf.append(" ");

        return buf.toString();
    }
}
