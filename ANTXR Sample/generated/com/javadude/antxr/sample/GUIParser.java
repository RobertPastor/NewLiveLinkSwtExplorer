// $ANTXR : "GUIParser.antxr" -> "GUIParser.java"$
// GENERATED CODE - DO NOT EDIT!

package com.javadude.antxr.sample;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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


/**
 * A sample parser that generates a Java GUI based on an XML specification
 */
@SuppressWarnings("all")
public class GUIParser extends com.javadude.antxr.LLkParser       implements GUIParserTokenTypes
 {
	// ANTXR XML Mode Support
	private static Map<String, String> __xml_namespaceMap = new HashMap<String, String>();
	public static Map<String, String> getNamespaceMap() {return __xml_namespaceMap;}
	public static String resolveNamespace(String prefix) {
		if (prefix == null || "".equals(prefix))
			return "";
		return __xml_namespaceMap.get(prefix);
	}


protected GUIParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public GUIParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected GUIParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public GUIParser(TokenStream lexer) {
  this(lexer,1);
}

public GUIParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final JFrame  document() throws RecognitionException, TokenStreamException {
		JFrame f=null;
		
		
		try {      // for error handling
			f=__xml_frame();
			match(Token.EOF_TYPE);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return f;
	}
	
/**
 * Create a JFrame based on a frame tag
 * @return the generated JPanel
 */
	public final JFrame  __xml_frame() throws RecognitionException, TokenStreamException {
		JFrame f=null;
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(4);
			{
			f = new JFrame(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"title"));
			layout(f.getContentPane());
			
						f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
						f.pack();
					
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
		return f;
	}
	
/**
 * Recognizes layout managers
 * @param container the container to which we add the layout manager
 */
	public final void layout(
		Container container
	) throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case 7:
			{
				__xml_borderLayout(container);
				break;
			}
			case 13:
			{
				__xml_flowLayout(container);
				break;
			}
			case 14:
			{
				__xml_gridLayout(container);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
	}
	
/**
 * Create a JPanel based on a panel tag
 * @return the generated JPanel
 */
	public final JPanel  __xml_panel() throws RecognitionException, TokenStreamException {
		JPanel p=new JPanel();
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(6);
			{
			layout(p);
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		return p;
	}
	
/**
 * Create BorderLayout for the borderLayout tag
 * @param container the container to which we add the layout manager
 */
	public final void __xml_borderLayout(
		Container container
	) throws RecognitionException, TokenStreamException {
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(7);
			{
			container.setLayout(new BorderLayout());
			{
			_loop34:
			do {
				switch ( LA(1)) {
				case 8:
				{
					__xml_north(container);
					break;
				}
				case 9:
				{
					__xml_south(container);
					break;
				}
				case 10:
				{
					__xml_east(container);
					break;
				}
				case 11:
				{
					__xml_west(container);
					break;
				}
				case 12:
				{
					__xml_center(container);
					break;
				}
				default:
				{
					break _loop34;
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
	}
	
/**
 * Create FlowLayout for the flowLayout tag
 * @param container the container to which we add the layout manager
 */
	public final void __xml_flowLayout(
		Container container
	) throws RecognitionException, TokenStreamException {
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(13);
			{
			
					int alignment = FlowLayout.CENTER;
					String align = ((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"align");
					if (align != null)
						if ("RIGHT".equals(align))
							alignment = FlowLayout.RIGHT;
						else if ("LEFT".equals(align))
							alignment = FlowLayout.LEFT;
						else if ("CENTER".equals(align))
							alignment = FlowLayout.CENTER;
					
					container.setLayout(new FlowLayout(alignment));
						
					Component c;
				
			{
			_loop48:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					c=component();
					container.add(c);
				}
				else {
					break _loop48;
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
	}
	
/**
 * Create GridLayout for the gridLayout tag
 * @param container the container to which we add the layout manager
 */
	public final void __xml_gridLayout(
		Container container
	) throws RecognitionException, TokenStreamException {
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(14);
			{
			
					int rows = Integer.parseInt(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"rows"));
					int cols = Integer.parseInt(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"cols"));
					container.setLayout(new GridLayout(rows,cols));
					Component c;
				
			{
			_loop52:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					c=component();
					container.add(c);
				}
				else {
					break _loop52;
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
	}
	
/**
 * Add the nested component to the container at the "north" position
 * @param container the container to which we add the nested component
 */
	public final void __xml_north(
		Container container
	) throws RecognitionException, TokenStreamException {
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(8);
			{
			Component c;
			c=component();
			container.add(c, BorderLayout.NORTH);
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
	}
	
/**
 * Add the nested component to the container at the "south" position
 * @param container the container to which we add the nested component
 */
	public final void __xml_south(
		Container container
	) throws RecognitionException, TokenStreamException {
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(9);
			{
			Component c;
			c=component();
			container.add(c, BorderLayout.SOUTH);
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
	}
	
/**
 * Add the nested component to the container at the "east" position
 * @param container the container to which we add the nested component
 */
	public final void __xml_east(
		Container container
	) throws RecognitionException, TokenStreamException {
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(10);
			{
			Component c;
			c=component();
			container.add(c, BorderLayout.EAST);
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
	}
	
/**
 * Add the nested component to the container at the "west" position
 * @param container the container to which we add the nested component
 */
	public final void __xml_west(
		Container container
	) throws RecognitionException, TokenStreamException {
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(11);
			{
			Component c;
			c=component();
			container.add(c, BorderLayout.WEST);
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
	}
	
/**
 * Add the nested component to the container at the "center" position
 * @param container the container to which we add the nested component
 */
	public final void __xml_center(
		Container container
	) throws RecognitionException, TokenStreamException {
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(12);
			{
			Component c;
			c=component();
			container.add(c, BorderLayout.CENTER);
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
	}
	
/**
 * Recognize a component
 * @return the component created based on an xml spec
 */
	public final Component  component() throws RecognitionException, TokenStreamException {
		Component component=null;
		
		
		try {      // for error handling
			switch ( LA(1)) {
			case 15:
			{
				component=__xml_button();
				break;
			}
			case 16:
			{
				component=__xml_label();
				break;
			}
			case 17:
			{
				component=__xml_textField();
				break;
			}
			case 6:
			{
				component=__xml_panel();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		return component;
	}
	
/**
 * Create a JButton based on a "button" xml tag
 * @return the created JButton
 */
	public final JButton  __xml_button() throws RecognitionException, TokenStreamException {
		JButton b=new JButton();
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(15);
			{
			b.setText(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"text"));
			{
			switch ( LA(1)) {
			case 18:
			{
				__xml_printAction(b);
				break;
			}
			case XML_END_TAG:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		return b;
	}
	
/**
 * Create a JLabel based on a "label" xml tag
 * @return the created JLabel
 */
	public final JLabel  __xml_label() throws RecognitionException, TokenStreamException {
		JLabel l=new JLabel();
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(16);
			{
			l.setText(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"text"));
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		return l;
	}
	
/**
 * Create a JTextField based on a "textField" xml tag
 * @return the created JTextField
 */
	public final JTextField  __xml_textField() throws RecognitionException, TokenStreamException {
		JTextField t=new JTextField();
		
		Token  __xml_startTag = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(17);
			{
			t.setText(((XMLToken)__xml_startTag).getAttribute(resolveNamespace(""),"text"));
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
		return t;
	}
	
/**
 * Create an ActionListener that prints the data in a "printAction" xml tag
 * @param b the button to which we add the listener
 */
	public final void __xml_printAction(
		JButton b
	) throws RecognitionException, TokenStreamException {
		
		Token  __xml_startTag = null;
		Token  pcData = null;
		
		try {      // for error handling
			__xml_startTag = LT(1);
			match(18);
			{
			pcData = LT(1);
			match(PCDATA);
			
						final String value = pcData.getText();
						b.addActionListener(
							new ActionListener() {
								public void actionPerformed(ActionEvent e) {
								System.out.println(value);
						}});
					
			}
			match(XML_END_TAG);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"<frame>\"",
		"XML_END_TAG",
		"\"<panel>\"",
		"\"<borderLayout>\"",
		"\"<north>\"",
		"\"<south>\"",
		"\"<east>\"",
		"\"<west>\"",
		"\"<center>\"",
		"\"<flowLayout>\"",
		"\"<gridLayout>\"",
		"\"<button>\"",
		"\"<label>\"",
		"\"<textField>\"",
		"\"<printAction>\"",
		"PCDATA"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 32L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 229472L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 229440L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 7968L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	
	}
