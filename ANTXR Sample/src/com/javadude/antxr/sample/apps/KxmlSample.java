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
import java.util.Iterator;

import com.javadude.antxr.RecognitionException;
import com.javadude.antxr.TokenStreamException;
import com.javadude.antxr.sample.LiveLinkNode;
import com.javadude.antxr.sample.LiveLinkNodeSet;
import com.javadude.antxr.sample.LiveLinkParser;
import com.javadude.antxr.sample.LiveLinkRedirectURL;
import com.javadude.antxr.sample.LiveLinkShortCut;
import com.javadude.antxr.sample.LiveLinkVolume;
import com.javadude.antxr.sample.LiveLinkXmlExport;
import com.javadude.antxr.scanner.BasicKXml2XMLPullTokenStream;

/**
 * Sample executable that uses a Xerces scanner
 */
public class KxmlSample {

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
		String fileName = "LiveLinkCorporate.xml";
		fileName = "livelinkError CANAC2 Phase 1.xml";
		fileName = "LiveLinkXmlParserError.xml";
		fileName = "LiveLinkShortCut.xml";
		fileName = "LiveLinkURI.xml";
		fileName = "LiveLink Export with llvolume.xml";
		/*
		String filePath = 	KxmlSample.class.getResource(fileName).getPath();
		System.out.println(filePath);
		File xmlFile = new File(filePath);
		System.out.println(xmlFile.getAbsolutePath());
		InputSource in1 = new InputSource(KxmlSample.class.getResourceAsStream("COOPANS customView.xml"));


		System.out.println("fileName : "+fileName);
		fileName = "file:/F:/ANTXR Sample/"+fileName;
		System.out.println("fileName : "+fileName);
		InputStream in = KxmlSample.class.getResourceAsStream(fileName);
		if (in != null) {
			UnicodeReader unicodeReader = null;
			try {
				unicodeReader = new UnicodeReader(in,null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		 */
		BasicKXml2XMLPullTokenStream stream =
				new BasicKXml2XMLPullTokenStream(new FileReader(fileName),
						LiveLinkParser.class,
						false); // relaxed
		
		LiveLinkParser parser = new LiveLinkParser(stream);
		LiveLinkXmlExport llXmlExport = parser.document();
		System.out.println("Success!");
		System.out.println("LiveLink application version: "+llXmlExport.getAppVersion());
		System.out.println("LiveLink action: "+llXmlExport.getSrc());
		LiveLinkNode llNode = llXmlExport.getLiveLinkNode();

		if (llNode != null) {
			llNode.dump();
			if (llNode.getLiveLinkShortCut() != null) {
				LiveLinkShortCut llShortCut = llNode.getLiveLinkShortCut();
				llShortCut.dump();
			}
			if (llNode.getLiveLinkRedirectURL() != null) {
				LiveLinkRedirectURL  llRedirectURL = llNode.getLiveLinkRedirectURL();
				llRedirectURL.dump();
			}
			LiveLinkNodeSet children = llNode.getChildren();
			Iterator<LiveLinkNode> Iter = children.iterator();
			while (Iter.hasNext()) {
				System.out.println("===========");
				LiveLinkNode llSubNode = Iter.next();
				llSubNode.dump();
			}
		}
		else {
			System.out.println("LiveLink Volume");
			LiveLinkVolume llVolume = llXmlExport.getLiveLinkVolume();
			if (llVolume != null) {
				llVolume.dump();
			}
		}


	}

}
