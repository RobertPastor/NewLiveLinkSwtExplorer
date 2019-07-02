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
package com.javadude.antxr.scanner;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.javadude.antxr.Token;
import com.javadude.antxr.TokenStream;
import com.javadude.antxr.TokenStreamException;

/**
 * A simple implementation of XMLPullTokenStream that uses kxml as its
 * implementation
 */
public class BasicKXml2XMLPullTokenStream implements TokenStream {
	
	private XMLPullTokenStream tokenStream;

	private static final Class[] NO_PARAMETERS = new Class[] {};

	private static final Object[] NO_ARGUMENTS = new Object[] {};

	/**
	 * Creates an instance of the KXml token stream
	 * @param xmlToParse the xml stream to parse
	 * @param parserClass the generated parser class
	 * @param namespaceAware do we want a name space aware parse
	 * 
	 */
	public BasicKXml2XMLPullTokenStream(Reader xmlToParse, Class parserClass,
									    boolean namespaceAware,
									    boolean relaxed) {
		// Create the XmlPull parser (really part of the scanner)

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance("org.kxml2.io.KXmlParser", null);
			factory.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, namespaceAware);
			// 17th July 2012 : patch Robert
			factory.setFeature("http://xmlpull.org/v1/doc/features.html#relaxed", relaxed);

			XmlPullParser parser = factory.newPullParser();
			parser.setInput(xmlToParse);

			Field field = parserClass.getField("_tokenNames");
			String[] tokenNames = (String[])field.get(null);

			Method getNameSpaceMapMethod = parserClass.getMethod("getNamespaceMap", NO_PARAMETERS);
			Map namespaceMap = (Map)getNameSpaceMapMethod.invoke(null, NO_ARGUMENTS);

			// Create our scanner (using the xml pull parser)
			tokenStream = new XMLPullTokenStream(tokenNames, namespaceMap, parser);
		}
		catch (XmlPullParserException e) {
			throw new RuntimeException("Exception thrown setting up KXml parser. See nested exception.",e);
		}		
		catch (NoSuchFieldException e) {
			throw new IllegalArgumentException("Cannot find _tokenNames in the parser class -- is it an XML parser?");
		}
		catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Cannot find getNamespaceMap() in the parser class -- is it an XML parser?");
		}
		catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Cannot access _tokenNames or getNamespaceMap() in the parser class (they should be static)");
		}
		catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Cannot access _tokenNames or getNamespaceMap() in the parser class (they should be public)");
		}
		catch (InvocationTargetException e) {
			throw new RuntimeException("Exception thrown when running getNamespaceMap(). See nested exception.", e);
		}
		
		
	}

	/** {@inheritDoc} */
	public Token nextToken() throws TokenStreamException {
		return tokenStream.nextToken();
	}

}
