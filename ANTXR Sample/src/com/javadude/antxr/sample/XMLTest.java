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
package com.javadude.antxr.sample;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import com.javadude.antxr.TokenStream;
import com.javadude.antxr.scanner.BasicCrimsonXMLTokenStream;
import com.javadude.antxr.scanner.BasicKXml2XMLPullTokenStream;
import com.javadude.antxr.scanner.BasicXercesXMLTokenStream;

/**
 * A set of test cases for the ANTLR XML parser
 * @author scott
 *
 */
public class XMLTest extends TestCase {
	
	private static final String resultWithoutSchemaLocation = 
		"Note id: sampleNote\n" +
		"To: Tove\n" +
		"From: mikey\n" +
		"Subject: Reminder\n" +
		"----\n" +
		"Don't forget me this weekend!";
	private static final String resultWithSchemaLocation = 
		"xsi:schemaLocation = http://www.w3schools.com sample.xsd\n" + resultWithoutSchemaLocation;
	private static final Class[] tokenStreamParams = {TokenStream.class};
	private static final Class[] emptyParams = {};
	private static final Object[] emptyArgs = {};
	
	/**
	 * Run the test cases.
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		TestRunner.run(XMLTest.class);
	}	
	
	// Test naming conventions
	//   test[1|2|3][a|b|c][X|C|K][N][V][S][W]
	//     1 = sample1.xml  (namespace, no prefixes)
	//     2 = sample2.xml  (namespace, prefixes)
	//     3 = sample3.xml  (no namespace)
	//     a = NoteParser1  (namespace, no prefixes)
	//     b = NoteParser1a (namespace, prefixes)
	//     c = NoteParser1  (no namespace)
	//     X = xerces
	//     C = crimson
	//     K = KXml
	//     N = namespace on
	//     V = validation on
	//     S = schema on
	//     W = turn on high-water mark
	
	protected FileReader getFileReader(int n) throws FileNotFoundException {
		return new FileReader("sample" + n + ".xml");
	}
	
	/**
	 * The actual test code
	 * @param testChars The test name string (see above)
	 * @throws Exception if something goes wrong
	 */
	protected void doTest(String testChars) throws Exception {
		String fileName = "sample" + testChars.charAt(0) + ".xml";
		FileReader reader = new FileReader(fileName);
		boolean namespaceAware = testChars.indexOf('N') != -1;
		boolean validating = testChars.indexOf('V') != -1;
		boolean useSchema = testChars.indexOf('S') != -1;
		boolean useHighWaterMark = testChars.indexOf('W') != -1;

		Class parserClass = null;
		switch(testChars.charAt(1)) {
			case 'a':
				parserClass = NoteParser1.class;
				break;
				
			case 'b':
				parserClass = NoteParser2.class;
				break;

			case 'c':
				parserClass = NoteParser3.class;
				break;

			case 'd':
				parserClass = PeopleParser.class;
				break;
		}
		
		TokenStream stream = null;
		switch (testChars.charAt(2)) {
			case 'X':
				if (useHighWaterMark)
					stream = new BasicXercesXMLTokenStream(reader, parserClass, namespaceAware, validating, useSchema, 5, 2);
				else
					stream = new BasicXercesXMLTokenStream(reader, parserClass, namespaceAware, validating, useSchema);
				break;
				
			case 'C':
				if (useSchema)
					throw new IllegalArgumentException("Cannot use schema with Crimson");
				
				if (useHighWaterMark)
					stream = new BasicCrimsonXMLTokenStream(reader, parserClass, namespaceAware, validating, 5, 2);
				else
					stream = new BasicCrimsonXMLTokenStream(reader, parserClass, namespaceAware, validating);
				break;

			case 'K':
				if (useHighWaterMark)
					throw new IllegalArgumentException("Cannot use high water mark with kxml");
				if (validating)
					throw new IllegalArgumentException("Cannot use validation with kxml");
				if (useSchema)
					throw new IllegalArgumentException("Cannot use schema with kxml");
				stream = new BasicKXml2XMLPullTokenStream(reader, parserClass, namespaceAware);
				break;
		}

		Constructor constructor = parserClass.getConstructor(tokenStreamParams);
		Object object = constructor.newInstance(new Object[] {stream});
		Method method = parserClass.getMethod("document", emptyParams);
		String text = (String)method.invoke(object, emptyArgs);

		if (testChars.charAt(1) == 'c')
			assertEquals(resultWithoutSchemaLocation, text);
		else
			assertEquals(resultWithSchemaLocation, text);
	}
	
	// Using xerces parser
	/** @throws Exception */
	public void test1aXNVS() throws Exception {
		doTest("1aXNVS");
	}
	/** @throws Exception */
	public void test1aXN() throws Exception {
		doTest("1aXN");
	}
	/** @throws Exception */
	public void test2aXNVS() throws Exception {
		doTest("2aXNVS");
	}
	/** @throws Exception */
	public void test2aXN() throws Exception {
		doTest("2aXN");
	}
	/** @throws Exception */
	public void test1bXNVS() throws Exception {
		doTest("1bXNVS");
	}
	/** @throws Exception */
	public void test1bXN() throws Exception {
		doTest("1bXN");
	}
	/** @throws Exception */
	public void test2bXNVS() throws Exception {
		doTest("2bXNVS");
	}
	/** @throws Exception */
	public void test2bXN() throws Exception {
		doTest("2bXN");
	}
	/** @throws Exception */
	public void test3cX() throws Exception {
		doTest("3cX");
	}
	/** @throws Exception */
	public void test1aXNVSW() throws Exception {
		doTest("1aXNVSW");
	}
	/** @throws Exception */
	public void test1aXNW() throws Exception {
		doTest("1aXNW");
	}
	/** @throws Exception */
	public void test2aXNVSW() throws Exception {
		doTest("2aXNVSW");
	}
	/** @throws Exception */
	public void test2aXNW() throws Exception {
		doTest("2aXNW");
	}
	/** @throws Exception */
	public void test1bXNVSW() throws Exception {
		doTest("1bXNVSW");
	}
	/** @throws Exception */
	public void test1bXNW() throws Exception {
		doTest("1bXNW");
	}
	/** @throws Exception */
	public void test2bXNVSW() throws Exception {
		doTest("2bXNVSW");
	}
	/** @throws Exception */
	public void test2bXNW() throws Exception {
		doTest("2bXNW");
	}
	/** @throws Exception */
	public void test3cXW() throws Exception {
		doTest("3cXW");
	}


	// Using crimson parser
	/** @throws Exception */
	public void test1aCN() throws Exception {
		doTest("1aCN");
	}
	/** @throws Exception */
	public void test2aCN() throws Exception {
		doTest("2aCN");
	}
	/** @throws Exception */
	public void test1bCN() throws Exception {
		doTest("1bCN");
	}
	/** @throws Exception */
	public void test2bCN() throws Exception {
		doTest("2bCN");
	}
	/** @throws Exception */
	public void test3cC() throws Exception {
		doTest("3cC");
	}
	/** @throws Exception */
	public void test1aCNW() throws Exception {
		doTest("1aCNW");
	}
	/** @throws Exception */
	public void test2aCNW() throws Exception {
		doTest("2aCNW");
	}
	/** @throws Exception */
	public void test1bCNW() throws Exception {
		doTest("1bCNW");
	}
	/** @throws Exception */
	public void test2bCNW() throws Exception {
		doTest("2bCNW");
	}
	/** @throws Exception */
	public void test3cCW() throws Exception {
		doTest("3cCW");
	}
	
	// Using KXml parser
	/** @throws Exception */
	public void test1aKN() throws Exception {
		doTest("1aKN");
	}
	/** @throws Exception */
	public void test2aKN() throws Exception {
		doTest("2aKN");
	}
	/** @throws Exception */
	public void test1bKN() throws Exception {
		doTest("1bKN");
	}
	/** @throws Exception */
	public void test2bKN() throws Exception {
		doTest("2bKN");
	}
	/** @throws Exception */
	public void test3cK() throws Exception {
		doTest("3cK");
	}	

	protected void doPersonTest(List list) throws Exception {
		assertEquals("Number of people created", 3, list.size());
		Person person = (Person)list.get(0);
		assertEquals("first name", "Terence", person.getFirstName()); 
		assertEquals("last name", "Parr", person.getLastName()); 
		assertEquals("ssn", "111-11-1111", person.getSsn()); 
		person = (Person)list.get(1);
		assertEquals("first name", "Scott", person.getFirstName()); 
		assertEquals("last name", "Stanchfield", person.getLastName()); 
		assertEquals("ssn", "222-22-2222", person.getSsn()); 
		person = (Person)list.get(2);
		assertEquals("first name", "James", person.getFirstName()); 
		assertEquals("last name", "Stewart", person.getLastName()); 
		assertEquals("ssn", "333-33-3333", person.getSsn()); 
	}	
	
	/** @throws Exception */
	public void testPeopleC() throws Exception {
		TokenStream tokenStream = new BasicCrimsonXMLTokenStream(new FileReader("people.xml"),PeopleParser.class,false,false);
		PeopleParser peopleParser = new PeopleParser(tokenStream);
		doPersonTest(peopleParser.document());
	}	
	/** @throws Exception */
	public void testPeopleX() throws Exception {
		TokenStream tokenStream = new BasicXercesXMLTokenStream(new FileReader("people.xml"),PeopleParser.class,false,false,false);
		PeopleParser peopleParser = new PeopleParser(tokenStream);
		doPersonTest(peopleParser.document());
	}	
	/** @throws Exception */
	public void testPeopleK() throws Exception {
		TokenStream tokenStream = new BasicKXml2XMLPullTokenStream(new FileReader("people.xml"),PeopleParser.class,false);
		PeopleParser peopleParser = new PeopleParser(tokenStream);
		doPersonTest(peopleParser.document());
	}	
	
}
