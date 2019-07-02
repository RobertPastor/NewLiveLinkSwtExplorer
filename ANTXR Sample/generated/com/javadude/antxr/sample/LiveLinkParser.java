// $ANTXR : "LiveLink.antxr" -> "LiveLinkParser.java"$
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


@SuppressWarnings("all")
public class LiveLinkParser extends com.javadude.antxr.LLkParser       implements LiveLinkParserTokenTypes
 {
	// ANTXR XML Mode Support
	private static Map<String, String> __xml_namespaceMap = new HashMap<String, String>();
	public static Map<String, String> getNamespaceMap() {return __xml_namespaceMap;}
	public static String resolveNamespace(String prefix) {
		if (prefix == null || "".equals(prefix))
			return "";
		return __xml_namespaceMap.get(prefix);
	}


protected LiveLinkParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public LiveLinkParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected LiveLinkParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public LiveLinkParser(TokenStream lexer) {
  this(lexer,1);
}

public LiveLinkParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final LiveLinkXmlExport  document() throws RecognitionException, TokenStreamException {
		LiveLinkXmlExport llexport = null;
		
		
		try {      // for error handling
			llexport=__xml_livelink();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return llexport;
	}
	
	public final LiveLinkXmlExport  __xml_livelink() throws RecognitionException, TokenStreamException {
		LiveLinkXmlExport llexport = new LiveLinkXmlExport();
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(4);
			{
				
					LiveLinkNode llnode=null;
					llexport.setSrc(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"src"));
					llexport.setAppVersion(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"appversion"));
					
				
			{
			_loop21:
			do {
				if ((LA(1)==6)) {
					llnode=__xml_llnode();
					llexport.setLiveLinkNode(llnode);
				}
				else {
					break _loop21;
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
		return llexport;
	}
	
	public final LiveLinkNode  __xml_llnode() throws RecognitionException, TokenStreamException {
		LiveLinkNode p = new LiveLinkNode() ;
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(6);
			{
			
					LiveLinkNode llnode = null;
					LiveLinkShortCut llShortCut = null;
					LiveLinkRedirectURL llRedirectURL = null;
					LiveLinkNodeSet children = new LiveLinkNodeSet(p);
					p.setChildren(children);
					p.setId(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"id"));
					p.setName(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"name"));
					p.setCreatedDate(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"created"));
					p.setModifiedDate(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"modified"));
					p.setDescription(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"description"));
					p.setCreatedByName(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"createdbyname"));
					p.setOwnedByName(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"ownedbyname"));
					p.setMimeType(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"mimetype"));
					p.setSize(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"size"));
					p.setObjectName(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"objname"));
					p.setParentId(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"parentid"));
				
			{
			_loop25:
			do {
				switch ( LA(1)) {
				case 6:
				{
					llnode=__xml_llnode();
					children.add(llnode);
					break;
				}
				case 7:
				{
					llShortCut=__xml_originalnode();
					p.setLiveLinkShortCut(llShortCut);
					break;
				}
				case 9:
				{
					llRedirectURL=__xml_uri();
					p.setLiveLinkRedirectURL(llRedirectURL);
					break;
				}
				case OTHER_TAG:
				{
					otherTag();
					break;
				}
				default:
				{
					break _loop25;
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
	
	public final LiveLinkShortCut  __xml_originalnode() throws RecognitionException, TokenStreamException {
		LiveLinkShortCut llShortcut = new LiveLinkShortCut() ;
		
		Token  __xml_startTag = null;
		Token  pcData = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(7);
			{
			
					String originalNodeId = "";
					llShortcut.setName(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"name"));
					llShortcut.setPath(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"path"));
				
			pcData = LT(1);
			match(PCDATA);
			
				originalNodeId = pcData.getText();
				llShortcut.setOriginalNodeId(originalNodeId);
				
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		return llShortcut;
	}
	
	public final LiveLinkRedirectURL  __xml_uri() throws RecognitionException, TokenStreamException {
		LiveLinkRedirectURL llRedirectURL = new LiveLinkRedirectURL() ;
		
		Token  __xml_startTag = null;
		Token  pcData = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(9);
			{
			
					String uri = "";
				
			pcData = LT(1);
			match(PCDATA);
			
					uri = pcData.getText();
					llRedirectURL.setURI(uri);
					
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
		return llRedirectURL;
	}
	
	public final void otherTag() throws RecognitionException, TokenStreamException {
		
		Token  other = null;
		Token  pcData = null;
		
		try {      // for error handling
			other = LT(1);
			match(OTHER_TAG);
			{
			_loop32:
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
					break _loop32;
				}
				}
			} while (true);
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"<livelink>\"",
		"XML_END_TAG",
		"\"<llnode>\"",
		"\"<originalnode>\"",
		"PCDATA",
		"\"<uri>\"",
		"OTHER_TAG"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 1760L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 2016L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	
	}
