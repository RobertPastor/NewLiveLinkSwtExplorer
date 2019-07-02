/* Copyright (c) 2002,2003, Stefan Haustein, Oberhausen, Rhld., Germany
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The  above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE. */

package org.kxml2.io;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import org.xmlpull.v1.*;

/** A simple, pull based XML parser. This class replaces the kXML 1
    XmlParser class and the corresponding event classes. */

public class KXmlParser implements XmlPullParser {

	private static final Logger logger = Logger.getLogger(KXmlParser.class.getName());
	
    private Object location_;
	static final private String UNEXPECTED_EOF = "Unexpected EOF";
    static final private String ILLEGAL_TYPE = "Wrong event type";
    static final private int LEGACY = 999;
    static final private int XML_DECL = 998;

    // general

    private String version_;
    private Boolean standalone_;

    private boolean processNsp_;
    private boolean relaxed_;
    private Hashtable entityMap_;
    private int depth_;
    private String[] elementStack_ = new String[16];
    private String[] nspStack_ = new String[8];
    private int[] nspCounts_ = new int[4];

    // source

    private Reader reader_;
    private String encoding_;
    private char[] srcBuf_;

    private int srcPos_;
    private int srcCount_;

    private int line_;
    private int column_;

    // txtbuffer

    private char[] txtBuf_ = new char[128];
    private int txtPos_;

    // Event-related

    private int type_;
    //private String text;
    private boolean isWhitespace_;
    private String namespace_;
    private String prefix_;
    private String name_;

    private boolean degenerated_;
    private int attributeCount_;
    private String[] attributes_ = new String[16];
    private int stackMismatch_ = 0;
    private String error_;

    /** 
     * A separate peek buffer seems simpler than managing
     * wrap around in the first level read buffer */

    private int[] peek_ = new int[2];
    private int peekCount_;
    private boolean wasCR_;

    private boolean unresolved_;
    private boolean token_;

    /**
     * Create an instance
     */
    public KXmlParser() {
        srcBuf_ =
            new char[Runtime.getRuntime().freeMemory() >= 1048576 ? 8192 : 128];
    }

    private final boolean isProp(String n1, boolean prop, String n2) {
    	logger.info("n1: "+n1+"...prop: "+prop+"...n2: "+n2);
        if (!n1.startsWith("http://xmlpull.org/v1/doc/"))
            return false;
        if (prop)
            return n1.substring(42).equals(n2);
        return n1.substring(40).equals(n2);
    }

    private final boolean adjustNsp() throws XmlPullParserException {

        boolean any = false;

        for (int i = 0; i < attributeCount_ << 2; i += 4) {
            // * 4 - 4; i >= 0; i -= 4) {

            String attrName = attributes_[i + 2];
            int cut = attrName.indexOf(':');
            String prefix1;

            if (cut != -1) {
                prefix1 = attrName.substring(0, cut);
                attrName = attrName.substring(cut + 1);
            }
            else if (attrName.equals("xmlns")) {
                prefix1 = attrName;
                attrName = null;
            }
            else
                continue;

            if (!prefix1.equals("xmlns")) {
                any = true;
            }
            else {
                int j = (nspCounts_[depth_]++) << 1;

                nspStack_ = ensureCapacity(nspStack_, j + 2);
                nspStack_[j] = attrName;
                nspStack_[j + 1] = attributes_[i + 3];

                if (attrName != null && attributes_[i + 3].equals(""))
                    error("illegal empty namespace");

                //  prefixMap = new PrefixMap (prefixMap, attrName, attr.getValue ());

                //System.out.println (prefixMap);

                System.arraycopy(
                    attributes_,
                    i + 4,
                    attributes_,
                    i,
                    ((--attributeCount_) << 2) - i);

                i -= 4;
            }
        }

        if (any) {
            for (int i = (attributeCount_ << 2) - 4; i >= 0; i -= 4) {

                String attrName = attributes_[i + 2];
                int cut = attrName.indexOf(':');

                if (cut == 0 && !relaxed_)
                    throw new RuntimeException(
                        "illegal attribute name: " + attrName + " at " + this);

                else if (cut != -1) {
                    String attrPrefix = attrName.substring(0, cut);

                    attrName = attrName.substring(cut + 1);

                    String attrNs = getNamespace(attrPrefix);

                    if (attrNs == null && !relaxed_)
                        throw new RuntimeException(
                            "Undefined Prefix: " + attrPrefix + " in " + this);

                    attributes_[i] = attrNs;
                    attributes_[i + 1] = attrPrefix;
                    attributes_[i + 2] = attrName;

                    /*
                                        if (!relaxed) {
                                            for (int j = (attributeCount << 2) - 4; j > i; j -= 4)
                                                if (attrName.equals(attributes[j + 2])
                                                    && attrNs.equals(attributes[j]))
                                                    exception(
                                                        "Duplicate Attribute: {"
                                                            + attrNs
                                                            + "}"
                                                            + attrName);
                                        }
                        */
                }
            }
        }

        int cut = name_.indexOf(':');

        if (cut == 0)
            error("illegal tag name: " + name_);

        if (cut != -1) {
            prefix_ = name_.substring(0, cut);
            name_ = name_.substring(cut + 1);
        }

        this.namespace_ = getNamespace(prefix_);

        if (this.namespace_ == null) {
            if (prefix_ != null)
                error("undefined prefix: " + prefix_);
            this.namespace_ = NO_NAMESPACE;
        }

        return any;
    }

    private final String[] ensureCapacity(String[] arr, int required) {
        if (arr.length >= required)
            return arr;
        String[] bigger = new String[required + 16];
        System.arraycopy(arr, 0, bigger, 0, arr.length);
        return bigger;
    }

    private final void error(String desc) throws XmlPullParserException {
        if (relaxed_) {
            if (error_ == null) {
            	// patch 7th July 2012 - Robert : avoid erroneous message when character & in the text entity
                //error_ = "ERR: " + desc;
            }
        }
        else
            exception(desc);
    }

    private final void exception(String desc) throws XmlPullParserException {
        throw new XmlPullParserException(
            desc.length() < 100 ? desc : desc.substring(0, 100) + "\n",
            this,
            null);
    }

    /** 
     * common base for next and nextToken. Clears the state, except from 
     * txtPos and whitespace. Does not set the type variable */

    private final void nextImpl() throws IOException, XmlPullParserException {

        if (reader_ == null)
            exception("No Input specified");

        if (type_ == END_TAG)
            depth_--;

        while (true) {
            attributeCount_ = -1;

			// degenerated needs to be handled before error because of possible
			// processor expectations(!)

			if (degenerated_) {
				degenerated_ = false;
				type_ = END_TAG;
				return;
			}


            if (error_ != null) {
                for (int i = 0; i < error_.length(); i++)
                    push(error_.charAt(i));
                //				text = error;
                error_ = null;
                type_ = COMMENT;
                return;
            }


            if (relaxed_
                && (stackMismatch_ > 0 || (peek(0) == -1 && depth_ > 0))) {
                int sp = (depth_ - 1) << 2;
                type_ = END_TAG;
                namespace_ = elementStack_[sp];
                prefix_ = elementStack_[sp + 1];
                name_ = elementStack_[sp + 2];
                if (stackMismatch_ != 1)
                    error_ = "missing end tag /" + name_ + " inserted";
                if (stackMismatch_ > 0)
                    stackMismatch_--;
                return;
            }

            prefix_ = null;
            name_ = null;
            namespace_ = null;
            //            text = null;

            type_ = peekType();

            switch (type_) {

                case ENTITY_REF :
                    pushEntity();
                    return;

                case START_TAG :
                    parseStartTag(false);
                    return;

                case END_TAG :
                    parseEndTag();
                    return;

                case END_DOCUMENT :
                    return;

                case TEXT :
                    pushText('<', !token_);
                    if (depth_ == 0) {
                        if (isWhitespace_)
                            type_ = IGNORABLE_WHITESPACE;
                        // make exception switchable for instances.chg... !!!!
                        //	else 
                        //    exception ("text '"+getText ()+"' not allowed outside root element");
                    }
                    return;

                default :
                    type_ = parseLegacy(token_);
                    if (type_ != XML_DECL)
                        return;
            }
        }
    }

    private final int parseLegacy(boolean push)
        throws IOException, XmlPullParserException {

        String req = "";
        int term;
        int result;
        int prev = 0;

        read(); // <
        int c = read();

        if (c == '?') {
            if ((peek(0) == 'x' || peek(0) == 'X')
                && (peek(1) == 'm' || peek(1) == 'M')) {

                if (push) {
                    push(peek(0));
                    push(peek(1));
                }
                read();
                read();

                if ((peek(0) == 'l' || peek(0) == 'L') && peek(1) <= ' ') {

                    if (line_ != 1 || column_ > 4) {
                    	logger.info("line: "+line_+" ... column: "+column_);
                    	// patch Robert 2 July 2012
                        error("PI must not start with xml");
                    }

                    parseStartTag(true);

                    if (attributeCount_ < 1 || !"version".equals(attributes_[2]))
                        error("version expected");

                    version_ = attributes_[3];

                    int pos = 1;

                    if (pos < attributeCount_
                        && "encoding".equals(attributes_[2 + 4])) {
                        encoding_ = attributes_[3 + 4];
                        pos++;
                    }

                    if (pos < attributeCount_
                        && "standalone".equals(attributes_[4 * pos + 2])) {
                        String st = attributes_[3 + 4 * pos];
                        if ("yes".equals(st))
                            standalone_ = new Boolean(true);
                        else if ("no".equals(st))
                            standalone_ = new Boolean(false);
                        else
                            error("illegal standalone value: " + st);
                        pos++;
                    }

                    if (pos != attributeCount_)
                        error("illegal xmldecl");

                    isWhitespace_ = true;
                    txtPos_ = 0;

                    return XML_DECL;
                }
            }

            /*            int c0 = read ();
                        int c1 = read ();
                        int */

            term = '?';
            result = PROCESSING_INSTRUCTION;
        }
        else if (c == '!') {
            if (peek(0) == '-') {
                result = COMMENT;
                req = "--";
                term = '-';
            }
            else if (peek(0) == '[') {
                result = CDSECT;
                req = "[CDATA[";
                term = ']';
                push = true;
            }
            else {
                result = DOCDECL;
                req = "DOCTYPE";
                term = -1;
            }
        }
        else {
            error("illegal: <" + c);
            return COMMENT;
        }

        for (int i = 0; i < req.length(); i++)
            read(req.charAt(i));

        if (result == DOCDECL)
            parseDoctype(push);
        else {
            while (true) {
                c = read();
                if (c == -1){
                    error(UNEXPECTED_EOF);
                    return COMMENT;
                }

                if (push)
                    push(c);

                if ((term == '?' || c == term)
                    && peek(0) == term
                    && peek(1) == '>')
                    break;

                prev = c;
            }

            if (term == '-' && prev == '-')
                error("illegal comment delimiter: --->");

            read();
            read();

            if (push && term != '?')
                txtPos_--;

        }
        return result;
    }

    /** precondition: &lt! consumed */

    private final void parseDoctype(boolean push)
        throws IOException, XmlPullParserException {

        int nesting = 1;
        boolean quoted = false;

        // read();

        while (true) {
            int i = read();
            switch (i) {

                case -1 :
                    error(UNEXPECTED_EOF);
                    return;

                case '\'' :
                    quoted = !quoted;
                    break;

                case '<' :
                    if (!quoted)
                        nesting++;
                    break;

                case '>' :
                    if (!quoted) {
                        if ((--nesting) == 0)
                            return;
                    }
                    break;
            }
            if (push)
                push(i);
        }
    }

    /* precondition: &lt;/ consumed */

    private final void parseEndTag()
        throws IOException, XmlPullParserException {

        read(); // '<'
        read(); // '/'
        name_ = readName();
        skip();
        read('>');

        int sp = (depth_ - 1) << 2;

        if (depth_ == 0) {
            error("element stack empty");
            type_ = COMMENT;
            return;
        }

        if (!name_.equals(elementStack_[sp + 3])) {
            error("expected: /" + elementStack_[sp + 3] + " read: " + name_);

			// become case insensitive in relaxed mode

            int probe = sp;
            while (probe >= 0 && !name_.toLowerCase().equals(elementStack_[probe + 3].toLowerCase())) {
                stackMismatch_++;
                probe -= 4;
            }

            if (probe < 0) {
                stackMismatch_ = 0;
                //			text = "unexpected end tag ignored";
                type_ = COMMENT;
                return;
            }
        }

        namespace_ = elementStack_[sp];
        prefix_ = elementStack_[sp + 1];
        name_ = elementStack_[sp + 2];
    }

    private final int peekType() throws IOException {
        switch (peek(0)) {
            case -1 :
                return END_DOCUMENT;
            case '&' :
                return ENTITY_REF;
            case '<' :
                switch (peek(1)) {
                    case '/' :
                        return END_TAG;
                    case '?' :
                    case '!' :
                        return LEGACY;
                    default :
                        return START_TAG;
                }
            default :
                return TEXT;
        }
    }

    private final String get(int pos) {
        return new String(txtBuf_, pos, txtPos_ - pos);
    }

    /*
    private final String pop (int pos) {
    String result = new String (txtBuf, pos, txtPos - pos);
    txtPos = pos;
    return result;
    }
    */

    private final void push(int c) {

        isWhitespace_ &= c <= ' ';

        if (txtPos_ == txtBuf_.length) {
            char[] bigger = new char[txtPos_ * 4 / 3 + 4];
            System.arraycopy(txtBuf_, 0, bigger, 0, txtPos_);
            txtBuf_ = bigger;
        }

        txtBuf_[txtPos_++] = (char) c;
    }

    /** Sets name and attributes */

    private final void parseStartTag(boolean xmldecl)
        throws IOException, XmlPullParserException {

        if (!xmldecl)
            read();
        name_ = readName();
        attributeCount_ = 0;

        while (true) {
            skip();

            int c = peek(0);

            if (xmldecl) {
                if (c == '?') {
                    read();
                    read('>');
                    return;
                }
            }
            else {
                if (c == '/') {
                    degenerated_ = true;
                    read();
                    skip();
                    read('>');
                    break;
                }

                if (c == '>' && !xmldecl) {
                    read();
                    break;
                }
            }

            if (c == -1) {
                error(UNEXPECTED_EOF);
                //type = COMMENT;
                return;
            }

            String attrName = readName();

            if (attrName.length() == 0) {
                error("attr name expected");
               //type = COMMENT;
                break;
            }

            int i = (attributeCount_++) << 2;

            attributes_ = ensureCapacity(attributes_, i + 4);

            attributes_[i++] = "";
            attributes_[i++] = null;
            attributes_[i++] = attrName;

            skip();

            if (peek(0) != '=') {
				error("Attr.value missing f. "+attrName);
                attributes_[i] = "1";
            }
            else {
                read('=');
                skip();
                int delimiter = peek(0);

                if (delimiter != '\'' && delimiter != '"') {
                    error("attr value delimiter missing!");
                    delimiter = ' ';
                }
				else 
					read();
				
                int p = txtPos_;
                pushText(delimiter, true);

                attributes_[i] = get(p);
                txtPos_ = p;

                if (delimiter != ' ')
                    read(); // skip endquote
            }
        }

        int sp = depth_++ << 2;

        elementStack_ = ensureCapacity(elementStack_, sp + 4);
        elementStack_[sp + 3] = name_;

        if (depth_ >= nspCounts_.length) {
            int[] bigger = new int[depth_ + 4];
            System.arraycopy(nspCounts_, 0, bigger, 0, nspCounts_.length);
            nspCounts_ = bigger;
        }

        nspCounts_[depth_] = nspCounts_[depth_ - 1];

        /*
        		if(!relaxed){
                for (int i = attributeCount - 1; i > 0; i--) {
                    for (int j = 0; j < i; j++) {
                        if (getAttributeName(i).equals(getAttributeName(j)))
                            exception("Duplicate Attribute: " + getAttributeName(i));
                    }
                }
        		}
        */
        if (processNsp_)
            adjustNsp();
        else
            namespace_ = "";

        elementStack_[sp] = namespace_;
        elementStack_[sp + 1] = prefix_;
        elementStack_[sp + 2] = name_;
    }

    /** result: isWhitespace; if the setName parameter is set,
    the name of the entity is stored in "name" */

    private final void pushEntity()
        throws IOException, XmlPullParserException {

        read(); // &

        int pos = txtPos_;

        while (true) {
            int c = read();
            if (c == ';')
                break;
            if (c < 128
                && (c < '0' || c > '9')
                && (c < 'a' || c > 'z')
                && (c < 'A' || c > 'Z')
                && c != '_'
                && c != '-'
                && c != '#') {
                error("unterminated entity ref");
                //; ends with:"+(char)c);           
                if (c != -1)
                    push(c);
                return;
            }
            push(c);
        }

        String code = get(pos);
        txtPos_ = pos;
        if (token_ && type_ == ENTITY_REF)
            name_ = code;

        if (code.charAt(0) == '#') {
            int c =
                (code.charAt(1) == 'x'
                    ? Integer.parseInt(code.substring(2), 16)
                    : Integer.parseInt(code.substring(1)));
            push(c);
            return;
        }

        String result = (String) entityMap_.get(code);

        unresolved_ = result == null;

        if (unresolved_) {
            if (!token_)
                error("unresolved: &" + code + ";");
        }
        else {
            for (int i = 0; i < result.length(); i++)
                push(result.charAt(i));
        }
    }

    /** types:
    '<': parse to any token (for nextToken ())
    '"': parse to quote
    ' ': parse to whitespace or '>'
    */

    private final void pushText(int delimiter, boolean resolveEntities)
        throws IOException, XmlPullParserException {

        int next = peek(0);
        int cbrCount = 0;

        while (next != -1 && next != delimiter) { // covers eof, '<', '"'

            if (delimiter == ' ')
                if (next <= ' ' || next == '>')
                    break;

            if (next == '&') {
                if (!resolveEntities)
                    break;

                pushEntity();
            }
            else if (next == '\n' && type_ == START_TAG) {
                read();
                push(' ');
            }
            else
                push(read());

            if (next == '>' && cbrCount >= 2 && delimiter != ']')
                error("Illegal: ]]>");

            if (next == ']')
                cbrCount++;
            else
                cbrCount = 0;

            next = peek(0);
        }
    }

    private final void read(char c)
        throws IOException, XmlPullParserException {
        int a = read();
        if (a != c)
            error("expected: '" + c + "' actual: '" + ((char) a) + "'");
    }

    private final int read() throws IOException {
        int result;

        if (peekCount_ == 0)
            result = peek(0);
        else {
            result = peek_[0];
            peek_[0] = peek_[1];
        }
        //		else {
        //			result = peek[0]; 
        //			System.arraycopy (peek, 1, peek, 0, peekCount-1);
        //		}
        peekCount_--;

        column_++;

        if (result == '\n') {

            line_++;
            column_ = 1;
        }

        return result;
    }

    /** Does never read more than needed */

    private final int peek(int pos) throws IOException {

        while (pos >= peekCount_) {

            int nw;

            if (srcBuf_.length <= 1)
                nw = reader_.read();
            else if (srcPos_ < srcCount_)
                nw = srcBuf_[srcPos_++];
            else {
                srcCount_ = reader_.read(srcBuf_, 0, srcBuf_.length);
                if (srcCount_ <= 0)
                    nw = -1;
                else
                    nw = srcBuf_[0];

                srcPos_ = 1;
            }

            if (nw == '\r') {
                wasCR_ = true;
                peek_[peekCount_++] = '\n';
            }
            else {
                if (nw == '\n') {
                    if (!wasCR_)
                        peek_[peekCount_++] = '\n';
                }
                else
                    peek_[peekCount_++] = nw;

                wasCR_ = false;
            }
        }

        return peek_[pos];
    }

    private final String readName()
        throws IOException, XmlPullParserException {

        int pos = txtPos_;
        int c = peek(0);
        if ((c < 'a' || c > 'z')
            && (c < 'A' || c > 'Z')
            && c != '_'
            && c != ':'
            && c < 0x0c0
            && !relaxed_)
            error("name expected");

        do {
            push(read());
            c = peek(0);
        }
        while ((c >= 'a' && c <= 'z')
            || (c >= 'A' && c <= 'Z')
            || (c >= '0' && c <= '9')
            || c == '_'
            || c == '-'
            || c == ':'
            || c == '.'
            || c >= 0x0b7);

        String result = get(pos);
        txtPos_ = pos;
        return result;
    }

    private final void skip() throws IOException {

        while (true) {
            int c = peek(0);
            if (c > ' ' || c == -1)
                break;
            read();
        }
    }

    //--------------- public part starts here... ---------------

    public void setInput(Reader reader) throws XmlPullParserException {
        this.reader_ = reader;

        line_ = 1;
        column_ = 0;
        type_ = START_DOCUMENT;
        name_ = null;
        namespace_ = null;
        degenerated_ = false;
        attributeCount_ = -1;
        encoding_ = null;
        version_ = null;
        standalone_ = null;

        if (reader == null)
            return;

        srcPos_ = 0;
        srcCount_ = 0;
        peekCount_ = 0;
        depth_ = 0;

        entityMap_ = new Hashtable();
        entityMap_.put("amp", "&");
        entityMap_.put("apos", "'");
        entityMap_.put("gt", ">");
        entityMap_.put("lt", "<");
        entityMap_.put("quot", "\"");
    }

    public void setInput(InputStream is, String _enc)
        throws XmlPullParserException {

        srcPos_ = 0;
        srcCount_ = 0;
        String enc = _enc;

        if (is == null)
            throw new IllegalArgumentException();

        try {

            if (enc == null) {
                // read four bytes 

                int chk = 0;

                while (srcCount_ < 4) {
                    int i = is.read();
                    if (i == -1)
                        break;
                    chk = (chk << 8) | i;
                    srcBuf_[srcCount_++] = (char) i;
                }

                if (srcCount_ == 4) {
                    switch (chk) {
                        case 0x00000FEFF :
                            enc = "UTF-32BE";
                            srcCount_ = 0;
                            break;

                        case 0x0FFFE0000 :
                            enc = "UTF-32LE";
                            srcCount_ = 0;
                            break;

                        case 0x03c :
                            enc = "UTF-32BE";
                            srcBuf_[0] = '<';
                            srcCount_ = 1;
                            break;

                        case 0x03c000000 :
                            enc = "UTF-32LE";
                            srcBuf_[0] = '<';
                            srcCount_ = 1;
                            break;

                        case 0x0003c003f :
                            enc = "UTF-16BE";
                            srcBuf_[0] = '<';
                            srcBuf_[1] = '?';
                            srcCount_ = 2;
                            break;

                        case 0x03c003f00 :
                            enc = "UTF-16LE";
                            srcBuf_[0] = '<';
                            srcBuf_[1] = '?';
                            srcCount_ = 2;
                            break;

                        case 0x03c3f786d :
                            while (true) {
                                int i = is.read();
                                if (i == -1)
                                    break;
                                srcBuf_[srcCount_++] = (char) i;
                                if (i == '>') {
                                    String s = new String(srcBuf_, 0, srcCount_);
                                    int i0 = s.indexOf("encoding");
                                    if (i0 != -1) {
                                        while (s.charAt(i0) != '"'
                                            && s.charAt(i0) != '\'')
                                            i0++;
                                        char deli = s.charAt(i0++);
                                        int i1 = s.indexOf(deli, i0);
                                        enc = s.substring(i0, i1);
                                    }
                                    break;
                                }
                            }

                        default :
                            if ((chk & 0x0ffff0000) == 0x0FEFF0000) {
                                enc = "UTF-16BE";
                                srcBuf_[0] =
                                    (char) ((srcBuf_[2] << 8) | srcBuf_[3]);
                                srcCount_ = 1;
                            }
                            else if ((chk & 0x0ffff0000) == 0x0fffe0000) {
                                enc = "UTF-16LE";
                                srcBuf_[0] =
                                    (char) ((srcBuf_[3] << 8) | srcBuf_[2]);
                                srcCount_ = 1;
                            }
                            else if ((chk & 0x0ffffff00) == 0x0EFBBBF00) {
                                enc = "UTF-8";
                                srcBuf_[0] = srcBuf_[3];
                                srcCount_ = 1;
                            }
                    }
                }
            }

            if (enc == null)
                enc = "UTF-8";

            int sc = srcCount_;
            setInput(new InputStreamReader(is, enc));
            encoding_ = _enc;
            srcCount_ = sc;
        }
        catch (Exception e) {
            throw new XmlPullParserException(
                "Invalid stream or encoding: " + e.toString(),
                this,
                e);
        }
    }

    public boolean getFeature(String feature) {
    	logger.info(feature);
        if (XmlPullParser.FEATURE_PROCESS_NAMESPACES.equals(feature))
            return processNsp_;
        else if (isProp(feature, false, "relaxed"))
            return relaxed_;
        else
            return false;
    }

    public String getInputEncoding() {
        return encoding_;
    }

    public void defineEntityReplacementText(String entity, String value)
        throws XmlPullParserException {
        if (entityMap_ == null)
            throw new RuntimeException("entity replacement text must be defined after setInput!");
        entityMap_.put(entity, value);
    }

    public Object getProperty(String property) {
        if (isProp(property, true, "xmldecl-version"))
            return version_;
        if (isProp(property, true, "xmldecl-standalone"))
            return standalone_;
		if (isProp(property, true, "location"))            
			return location_ != null ? location_ : reader_.toString();
        return null;
    }

    public int getNamespaceCount(int nsDepth) {
        if (nsDepth > this.depth_)
            throw new IndexOutOfBoundsException();
        return nspCounts_[nsDepth];
    }

    public String getNamespacePrefix(int pos) {
        return nspStack_[pos << 1];
    }

    public String getNamespaceUri(int pos) {
        return nspStack_[(pos << 1) + 1];
    }

    public String getNamespace(String nsPrefix) {

        if ("xml".equals(nsPrefix))
            return "http://www.w3.org/XML/1998/namespace";
        if ("xmlns".equals(nsPrefix))
            return "http://www.w3.org/2000/xmlns/";

        for (int i = (getNamespaceCount(depth_) << 1) - 2; i >= 0; i -= 2) {
            if (nsPrefix == null) {
                if (nspStack_[i] == null)
                    return nspStack_[i + 1];
            }
            else if (nsPrefix.equals(nspStack_[i]))
                return nspStack_[i + 1];
        }
        return null;
    }

    public int getDepth() {
        return depth_;
    }

    public String getPositionDescription() {

        StringBuffer buf =
            new StringBuffer(type_ < TYPES.length ? TYPES[type_] : "unknown");
        buf.append(' ');

        if (type_ == START_TAG || type_ == END_TAG) {
            if (degenerated_)
                buf.append("(empty) ");
            buf.append('<');
            if (type_ == END_TAG)
                buf.append('/');

            if (prefix_ != null)
                buf.append("{" + namespace_ + "}" + prefix_ + ":");
            buf.append(name_);

            int cnt = attributeCount_ << 2;
            for (int i = 0; i < cnt; i += 4) {
                buf.append(' ');
                if (attributes_[i + 1] != null)
                    buf.append(
                        "{" + attributes_[i] + "}" + attributes_[i + 1] + ":");
                buf.append(attributes_[i + 2] + "='" + attributes_[i + 3] + "'");
            }

            buf.append('>');
        }
        else if (type_ == IGNORABLE_WHITESPACE) {
        	// do nothing
        }
        else if (type_ != TEXT)
            buf.append(getText());
        else if (isWhitespace_)
            buf.append("(whitespace)");
        else {
            String text = getText();
            if (text.length() > 16)
                text = text.substring(0, 16) + "...";
            buf.append(text);
        }

		buf.append("@"+line_ + ":" + column_);
		if(location_ != null){
			buf.append(" in ");
			buf.append(location_);
		}
		else if(reader_ != null){
			buf.append(" in ");
			buf.append(reader_.toString());
		}
        return buf.toString();
    }

    public int getLineNumber() {
        return line_;
    }

    public int getColumnNumber() {
        return column_;
    }

    public boolean isWhitespace() throws XmlPullParserException {
        if (type_ != TEXT && type_ != IGNORABLE_WHITESPACE && type_ != CDSECT)
            exception(ILLEGAL_TYPE);
        return isWhitespace_;
    }

    public String getText() {
        return type_ < TEXT
            || (type_ == ENTITY_REF && unresolved_) ? null : get(0);
    }

    public char[] getTextCharacters(int[] poslen) {
        if (type_ >= TEXT) {
            if (type_ == ENTITY_REF) {
                poslen[0] = 0;
                poslen[1] = name_.length();
                return name_.toCharArray();
            }
            poslen[0] = 0;
            poslen[1] = txtPos_;
            return txtBuf_;
        }

        poslen[0] = -1;
        poslen[1] = -1;
        return null;
    }

    public String getNamespace() {
        return namespace_;
    }

    public String getName() {
        return name_;
    }

    public String getPrefix() {
        return prefix_;
    }

    public boolean isEmptyElementTag() throws XmlPullParserException {
        if (type_ != START_TAG)
            exception(ILLEGAL_TYPE);
        return degenerated_;
    }

    public int getAttributeCount() {
        return attributeCount_;
    }

    public String getAttributeType(int index) {
        return "CDATA";
    }

    public boolean isAttributeDefault(int index) {
        return false;
    }

    public String getAttributeNamespace(int index) {
        if (index >= attributeCount_)
            throw new IndexOutOfBoundsException();
        return attributes_[index << 2];
    }

    public String getAttributeName(int index) {
        if (index >= attributeCount_)
            throw new IndexOutOfBoundsException();
        return attributes_[(index << 2) + 2];
    }

    public String getAttributePrefix(int index) {
        if (index >= attributeCount_)
            throw new IndexOutOfBoundsException();
        return attributes_[(index << 2) + 1];
    }

    public String getAttributeValue(int index) {
        if (index >= attributeCount_)
            throw new IndexOutOfBoundsException();
        return attributes_[(index << 2) + 3];
    }

    public String getAttributeValue(String attrNamespace, String attrName) {

        for (int i = (attributeCount_ << 2) - 4; i >= 0; i -= 4) {
            if (attributes_[i + 2].equals(attrName)
                && (attrNamespace == null || attributes_[i].equals(attrNamespace)))
                return attributes_[i + 3];
        }

        return null;
    }

    public int getEventType() throws XmlPullParserException {
        return type_;
    }

    public int next() throws XmlPullParserException, IOException {

        txtPos_ = 0;
        isWhitespace_ = true;
        int minType = 9999;
        token_ = false;

        do {
            nextImpl();
            if (type_ < minType)
                minType = type_;
            //	    if (curr <= TEXT) type = curr; 
        }
        while (minType > ENTITY_REF // ignorable
            || (minType >= TEXT && peekType() >= TEXT));

        type_ = minType;
        if (type_ > TEXT)
            type_ = TEXT;

        return type_;
    }

    public int nextToken() throws XmlPullParserException, IOException {

        isWhitespace_ = true;
        txtPos_ = 0;

        token_ = true;
        nextImpl();
        return type_;
    }

    //----------------------------------------------------------------------
    // utility methods to make XML parsing easier ...

    public int nextTag() throws XmlPullParserException, IOException {

        next();
        if (type_ == TEXT && isWhitespace_)
            next();

        if (type_ != END_TAG && type_ != START_TAG)
            exception("unexpected type");

        return type_;
    }

    public void require(int type, String namespace, String name)
        throws XmlPullParserException, IOException {

        if (type != this.type_
            || (namespace != null && !namespace.equals(getNamespace()))
            || (name != null && !name.equals(getName())))
            exception(
                "expected: " + TYPES[type] + " {" + namespace + "}" + name);
    }

    public String nextText() throws XmlPullParserException, IOException {
        if (type_ != START_TAG)
            exception("precondition: START_TAG");

        next();

        String result;

        if (type_ == TEXT) {
            result = getText();
            next();
        }
        else
            result = "";

        if (type_ != END_TAG)
            exception("END_TAG expected");

        return result;
    }

    public void setFeature(String feature, boolean value)
        throws XmlPullParserException {
    	logger.info("feature: "+feature+"...bool value: "+value);
        if (XmlPullParser.FEATURE_PROCESS_NAMESPACES.equals(feature))
            processNsp_ = value;
        else if (isProp(feature, false, "relaxed")) {
        	logger.info("setting relaxed to value true");
            relaxed_ = value;
        }
        else
            exception("unsupported feature: " + feature);
    }

    public void setProperty(String property, Object value)
        throws XmlPullParserException {
        if(isProp(property, true, "location"))
        	location_ = value;
        else
	        throw new XmlPullParserException("unsupported property: " + property);
    }

    /**
      * Skip sub tree that is currently porser positioned on.
      * <br>NOTE: parser must be on START_TAG and when funtion returns
      * parser will be positioned on corresponding END_TAG. 
     * @throws XmlPullParserException on error
     * @throws IOException on error
      */

    //	Implementation copied from Alek's mail... 

    public void skipSubTree() throws XmlPullParserException, IOException {
        require(START_TAG, null, null);
        int level = 1;
        while (level > 0) {
            int eventType = next();
            if (eventType == END_TAG) {
                --level;
            }
            else if (eventType == START_TAG) {
                ++level;
            }
        }
    }
}
