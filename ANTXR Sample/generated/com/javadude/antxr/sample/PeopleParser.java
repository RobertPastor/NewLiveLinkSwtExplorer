// $ANTXR : "people.antxr" -> "PeopleParser.java"$
// GENERATED CODE - DO NOT EDIT!

package com.javadude.antxr.sample;

import java.util.List;
import java.util.ArrayList;

import com.javadude.antxr.TokenBuffer;
import com.javadude.antxr.TokenStreamException;
import com.javadude.antxr.TokenStreamIOException;
import com.javadude.antxr.ANTXRException;
import com.javadude.antxr.LLkParser;
import com.javadude.antxr.Token;
import com.javadude.antxr.TokenStream;
import com.javadude.antxr.RecognitionException;
import com.javadude.antxr.NoViableAltException;
import com.javadude.antxr.MismatchedTokenException;
import com.javadude.antxr.SemanticException;
import com.javadude.antxr.ParserSharedInputState;
import com.javadude.antxr.collections.impl.BitSet;

// ANTXR XML Mode Support
import com.javadude.antxr.scanner.XMLToken;
import com.javadude.antxr.scanner.Attribute;
import java.util.Map;
import java.util.HashMap;


public class PeopleParser extends com.javadude.antxr.LLkParser       implements PeopleParserTokenTypes
 {
	// ANTXR XML Mode Support
	private static Map __xml_namespaceMap = new HashMap();
	public static Map getNamespaceMap() {return __xml_namespaceMap;}
	public static String resolveNamespace(String prefix) {
		if (prefix == null || "".equals(prefix))
			return "";
		return (String)__xml_namespaceMap.get(prefix);
	}


protected PeopleParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public PeopleParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected PeopleParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public PeopleParser(TokenStream lexer) {
  this(lexer,1);
}

public PeopleParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final List  document() throws RecognitionException, TokenStreamException {
		List results = null;
		
		
		try {      // for error handling
			results=__xml_people();
			match(Token.EOF_TYPE);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return results;
	}
	
	public final List  __xml_people() throws RecognitionException, TokenStreamException {
		List results = new ArrayList() ;
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(4);
			{
			Person p;
			{
			_loop5:
			do {
				if ((LA(1)==6)) {
					p=__xml_person();
					results.add(p);
				}
				else {
					break _loop5;
				}
				
			} while (true);
			}
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return results;
	}
	
	public final Person  __xml_person() throws RecognitionException, TokenStreamException {
		Person p = new Person() ;
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(6);
			{
			
					String first, last;
					p.setSsn(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"ssn"));
				
			{
			_loop9:
			do {
				switch ( LA(1)) {
				case 7:
				{
					first=__xml_first_name();
					p.setFirstName(first);
					break;
				}
				case 9:
				{
					last=__xml_last_name();
					p.setLastName(last);
					break;
				}
				case OTHER_TAG:
				{
					otherTag();
					break;
				}
				default:
				{
					break _loop9;
				}
				}
			} while (true);
			}
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		return p;
	}
	
	public final String  __xml_first_name() throws RecognitionException, TokenStreamException {
		String value=null;
		
		Token  __xml_startTag = null;
		Token  pcdata = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(7);
			{
			pcdata = LT(1);
			match(PCDATA);
			value = pcdata.getText();
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		return value;
	}
	
	public final String  __xml_last_name() throws RecognitionException, TokenStreamException {
		String value=null;
		
		Token  __xml_startTag = null;
		Token  pcdata = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(9);
			{
			pcdata = LT(1);
			match(PCDATA);
			value = pcdata.getText();
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		return value;
	}
	
	public final void otherTag() throws RecognitionException, TokenStreamException {
		
		Token  other = null;
		Token  pcData = null;
		
		try {      // for error handling
			other = LT(1);
			match(OTHER_TAG);
			{
			_loop16:
			do {
				switch ( LA(1)) {
				case OTHER_TAG:
				{
					otherTag();
					break;
				}
				case PCDATA:
				{
					pcData = LT(1);
					match(PCDATA);
					break;
				}
				default:
				{
					break _loop16;
				}
				}
			} while (true);
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"<people>\"",
		"XML_END_TAG",
		"\"<person>\"",
		"\"<first-name>\"",
		"PCDATA",
		"\"<last-name>\"",
		"OTHER_TAG"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 96L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 1696L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 1952L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	
	}
