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
import java.util.ArrayList;
import java.util.Iterator;

import com.javadude.antxr.RecognitionException;
import com.javadude.antxr.TokenStreamException;
import com.javadude.antxr.sample.LiveLinkNode;
import com.javadude.antxr.sample.LiveLinkNodeSet;
import com.javadude.antxr.sample.LiveLinkParser;
import com.javadude.antxr.sample.LiveLinkXmlExport;
import com.javadude.antxr.sample.NoteParser1;
import com.javadude.antxr.scanner.BasicCrimsonXMLTokenStream;

/**
 * Sample executable that uses a Crimson scanner
 */
public class CrimsonSample {

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
		String fileName = "sample1.xml";
		fileName = "LiveLinkXmlParserError.xml";
		BasicCrimsonXMLTokenStream stream =
			new BasicCrimsonXMLTokenStream(new FileReader(fileName),
					LiveLinkParser.class,
			                               true,   // namespace-aware
			                               false); // no validation
		LiveLinkParser parser = new LiveLinkParser(stream);
		LiveLinkXmlExport llXmlExport = parser.document();
		System.out.println("Success!");
		System.out.println("LiveLink application version: "+llXmlExport.getAppVersion());
		System.out.println("LiveLink action: "+llXmlExport.getSrc());
		LiveLinkNode llNode = llXmlExport.getLiveLinkNode();

		llNode.dump();

		LiveLinkNodeSet nodesList = llNode.getChildren();
		Iterator<LiveLinkNode> Iter = nodesList.iterator();
		while (Iter.hasNext()) {
			System.out.println("===========");
			LiveLinkNode llSubNode = Iter.next();
			llSubNode.dump();
		}

	}
}
