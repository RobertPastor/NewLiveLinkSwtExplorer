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

import java.io.FileReader;

import javax.swing.JFrame;

import com.javadude.antxr.TokenStream;
import com.javadude.antxr.sample.GUIParser;
import com.javadude.antxr.scanner.BasicCrimsonXMLTokenStream;

/**
 * Demo of GUI xml parsing
 */
public class GUITest {

	/**
	 * Run the app
	 * @param args command-line args
	 * @throws Exception for demo purposes, dump all exceptions
	 */
	public static void main(String[] args) throws Exception {
		TokenStream tokenStream = 
			new BasicCrimsonXMLTokenStream(new FileReader("samplegui.xml"), GUIParser.class, false, false);
		
		
		GUIParser guiParser = new GUIParser(tokenStream);
		JFrame f = guiParser.document();
		f.setVisible(true);
	}
}
