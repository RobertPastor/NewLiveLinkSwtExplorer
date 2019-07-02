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
import java.io.PrintWriter;
import java.util.Map;

/**
 * Defines a strategy that can be used to manage the printwriter
 *   being used to write JavaCodeGenerator output
 *   
 * TODO generalize so all code gens could use?
 */
public interface JavaCodeGeneratorPrintWriterManager {
    public PrintWriter setupOutput(Tool tool, Grammar grammar) throws IOException;
    public PrintWriter setupOutput(Tool tool, String fileName) throws IOException;
    public void startMapping(int sourceLine);
	public void startSingleSourceLineMapping(int sourceLine);
    public void endMapping();
    public void finishOutput() throws IOException;
    public Map getSourceMaps();
}
