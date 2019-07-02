// $ANTXR : "NoteParser3.antxr" -> "NoteParser3.java"$
// GENERATED CODE - DO NOT EDIT!

package com.javadude.antxr.sample;

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


public class NoteParser3 extends com.javadude.antxr.LLkParser       implements NoteParser3TokenTypes
 {
	// ANTXR XML Mode Support
	private static Map __xml_namespaceMap = new HashMap();
	public static Map getNamespaceMap() {return __xml_namespaceMap;}
	public static String resolveNamespace(String prefix) {
		if (prefix == null || "".equals(prefix))
			return "";
		return (String)__xml_namespaceMap.get(prefix);
	}


protected NoteParser3(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public NoteParser3(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected NoteParser3(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public NoteParser3(TokenStream lexer) {
  this(lexer,1);
}

public NoteParser3(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final String  document() throws RecognitionException, TokenStreamException {
		String text="";
		
		
		try {      // for error handling
			text=__xml_note();
			match(Token.EOF_TYPE);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return text;
	}
	
	public final String  __xml_note() throws RecognitionException, TokenStreamException {
		String text="";
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(4);
			{
			String t=null,f=null,h=null,b=null;
			t=__xml_to();
			f=__xml_from();
			h=__xml_heading();
			b=__xml_body();
			
						text += "Note id: " + ((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"id") + "\n";
						text += "To: " + t + "\n";
						text += "From: " + f + "\n";
						text += "Subject: " + h + "\n";
						text += "----" + "\n";
						text += b;
					
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return text;
	}
	
	public final String  __xml_to() throws RecognitionException, TokenStreamException {
		String value="";
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(6);
			{
			value = ((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"name");
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		return value;
	}
	
	public final String  __xml_from() throws RecognitionException, TokenStreamException {
		String value="";
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(7);
			{
			value = ((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"name");
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		return value;
	}
	
	public final String  __xml_heading() throws RecognitionException, TokenStreamException {
		String value="";
		
		Token  __xml_startTag = null;
		Token  pcData = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(8);
			{
			pcData = LT(1);
			match(PCDATA);
			value = pcData.getText();
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		return value;
	}
	
	public final String  __xml_body() throws RecognitionException, TokenStreamException {
		String value="";
		
		Token  __xml_startTag = null;
		Token  pcData = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(10);
			{
			pcData = LT(1);
			match(PCDATA);
			value = pcData.getText();
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
		return value;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"<note>\"",
		"XML_END_TAG",
		"\"<to>\"",
		"\"<from>\"",
		"\"<heading>\"",
		"PCDATA",
		"\"<body>\""
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 128L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 256L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 1024L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 32L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	
	}
