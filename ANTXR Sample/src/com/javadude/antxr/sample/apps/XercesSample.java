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
package com.javadude.antxr.sample.apps;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.javadude.antxr.RecognitionException;
import com.javadude.antxr.TokenStreamException;
import com.javadude.antxr.sample.NoteParser1;
import com.javadude.antxr.scanner.BasicXercesXMLTokenStream;

/**
 * Sample executable that uses a Xerces scanner
 */
public class XercesSample {

	/**
	 * Run the sample.
	 * @param args command-line args
	 * @throws FileNotFoundException on error
	 * @throws TokenStreamException on error
	 * @throws RecognitionException on error
	 */
	public static void main(String[] args) 
			throws FileNotFoundException, 
			       RecognitionException, 
			       TokenStreamException {
		BasicXercesXMLTokenStream stream =
			new BasicXercesXMLTokenStream(new FileReader("sample1.xml"),
			                               NoteParser1.class,
			                               true,   // namespace-aware
			                               false,  // no validation
			                               false); // don't use schema for validation
		NoteParser1 parser = new NoteParser1(stream);
		String result = parser.document();
		System.out.println("Success!");
		System.out.println(result);
	}
}
