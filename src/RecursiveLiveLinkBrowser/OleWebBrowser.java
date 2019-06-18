package RecursiveLiveLinkBrowser;


/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/



import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.Variant;

/**
 * Wrapper for an OleAutomation object used to send commands
 * to a Win32 "Shell.Explorer" OLE control.
 * 
 * Instances of this class manage the setup, typical use and tear-down of
 * a simple web browser.
 */
public class OleWebBrowser {

	private String downloadURL = "";
	public String getDownloadURL() {
		return downloadURL;
	}

	private static final Logger logger = Logger.getLogger(OleWebBrowser.class.getName()); 

	/* See the Windows Platform SDK documentation for more information about the
	 * OLE control used here and its usage.
	 */
	// Generated from typelib filename: shdocvw.dll

	// Constants for WebBrowser CommandStateChange
	public static final int CSC_UPDATECOMMANDS = -1;
	public static final int CSC_NAVIGATEFORWARD = 1;
	public static final int CSC_NAVIGATEBACK = 2;

	// Constants for Web Browser ReadyState
	public static final int READYSTATE_UNINITIALIZED = 0;
	public static final int READYSTATE_LOADING = 1;
	public static final int READYSTATE_LOADED = 2;
	public static final int READYSTATE_INTERACTIVE = 3;
	public static final int READYSTATE_COMPLETE = 4;

	// Web Browser Control Events 
	public static final int BeforeNavigate        = 100; // Fired when a new hyper-link is being navigated to.
	public static final int NavigateComplete      = 101; // Fired when the document being navigated to becomes visible and enters the navigation stack.
	public static final int StatusTextChange      = 102; // Status bar text changed.
	public static final int Quit                  = 103; // Fired when application is quitting.
	public static final int DocumentComplete = 0x103;
	public static final int DownloadComplete      = 104; // Download of page complete.

	public static final int CommandStateChange    = 105; // The enabled state of a command changed
	public static final int DownloadBegin         = 106; // Download of a page started.
	public static final int NewWindow             = 107; // Fired when a new window should be created.
	public static final int ProgressChange        = 108; // Fired when download progress is updated.
	public static final int WindowMove            = 109; // Fired when window has been moved.
	public static final int WindowResize          = 110; // Fired when window has been sized.
	public static final int WindowActivate        = 111; // Fired when window has been activated.
	public static final int PropertyChange        = 112; // Fired when the PutProperty method has been called.
	public static final int TitleChange           = 113; // Document title changed.

	public static final int FrameBeforeNavigate   = 200; // Fired when a new hyper-link is being navigated to in a frame.
	public static final int FrameNavigateComplete = 201; // Fired when a new hyper-link is being navigated to in a frame.
	public static final int FrameNewWindow        = 204; // Fired when a new window should be created.

	//constant DISPID_FILEDOWNLOAD                 270           // Fired to indicate the File Download dialog is opening
	public static final int FileDownLoad          = 270;

	// Web Browser properties
	public static final int DISPID_READYSTATE = -525;

	private OleAutomation oleAutomation;

	/**
	 * Creates a Web browser control.
	 * <p>
	 * Typical use:<br>
	 * <code>
	 * OleControlSite oleControlSite = new OleControlSite(oleFrame, style, "Shell.Explorer");<br>
	 * OleAutomation oleAutomation = new OleAutomation(oleControlSite);<br>
	 * OleWebBrowser webBrowser = new OleWebBrowser(oleControlSite, oleAutomation);<br>
	 * </code>
	 * 
	 * @param oleAutomation the OleAutomation object for this control.
	 * @param oleControlSite the OleControlSite object for this control.
	 */
	public OleWebBrowser(OleAutomation oleAutomation) {
		this.oleAutomation = oleAutomation;
	}

	/**
	 * Disposes of the Web browser control.
	 */
	public void dispose() {
		if (oleAutomation != null) {
			oleAutomation.dispose();
		}
		oleAutomation = null;
	}

	/*
	 * Interact with the Control via OLE Automation
	 * 
	 * Note: You can hard code the DISPIDs if you know them beforehand
	 *       this is of course the fastest way, but you increase coupling
	 *       to the control.
	 */

	/**
	 * Returns the current web page title.
	 * 
	 * @return the current web page title String
	 */
	public String getLocationName() {
		// dispid=210, type=PROPGET, name="LocationName"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"LocationName"}); 
		int dispIdMember = rgdispid[0];
		Variant pVarResult = oleAutomation.getProperty(dispIdMember);
		if (pVarResult == null || pVarResult.getType() != OLE.VT_BSTR) return null;
		return pVarResult.getString();
	}

	/**
	 * Returns the current URL.
	 * 
	 * @return the current URL String
	 */
	public String getLocationURL() {
		// dispid=211, type=PROPGET, name="LocationURL"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"LocationURL"}); 
		int dispIdMember = rgdispid[0];

		Variant pVarResult = oleAutomation.getProperty(dispIdMember);
		if (pVarResult == null || pVarResult.getType() != OLE.VT_BSTR) return null;
		return pVarResult.getString();
	}

	/**
	 * Returns the current state of the control.
	 * 
	 * @return the current state of the control, one of:
	 *         READYSTATE_UNINITIALIZED;
	 *         READYSTATE_LOADING;
	 *         READYSTATE_LOADED;
	 *         READYSTATE_INTERACTIVE;
	 *         READYSTATE_COMPLETE.
	 */
	public int getReadyState() {
		// dispid=4294966771, type=PROPGET, name="ReadyState"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"ReadyState"}); 
		int dispIdMember = rgdispid[0];

		Variant pVarResult = oleAutomation.getProperty(dispIdMember);
		if (pVarResult == null || pVarResult.getType() != OLE.VT_I4) return -1;

		return pVarResult.getInt();
	}

	/**
	 * Navigates backwards through previously visited web sites.
	 */
	public void GoBack() {

		// dispid=100, type=METHOD, name="GoBack"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"GoBack"}); 
		int dispIdMember = rgdispid[0];
		oleAutomation.invoke(dispIdMember);
	}

	/**
	 * Navigates backwards through previously visited web sites.
	 */
	public void GoForward() {

		// dispid=101, type=METHOD, name="GoForward"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"GoForward"}); 
		int dispIdMember = rgdispid[0];
		oleAutomation.invoke(dispIdMember);
	}

	/**
	 * Navigates to home page.
	 */
	public void GoHome() {
		// dispid=102, type=METHOD, name="GoHome"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"GoHome"}); 
		int dispIdMember = rgdispid[0];
		oleAutomation.invoke(dispIdMember);
	}

	/**
	 * Navigates to user-specified Web search gateway.
	 */
	public void GoSearch() {
		// dispid=103, type=METHOD, name="GoSearch"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"GoSearch"}); 
		int dispIdMember = rgdispid[0];
		oleAutomation.invoke(dispIdMember);
	}

	/**
	 * Navigates to a particular URL.
	 */
	public boolean Navigate(final String url) {

		this.downloadURL = url;
		//logger.log(Level.INFO, "navigate to URL: "+url);
		// dispid=104, type=METHOD, name="Navigate"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"Navigate", "URL"});

		int dispIdMember = rgdispid[0];

		Variant[] rgvarg = new Variant[1];
		rgvarg[0] = new Variant(url);
		int[] rgdispidNamedArgs = new int[1];
		rgdispidNamedArgs[0] = rgdispid[1]; // identifier of argument
		Variant pVarResult = oleAutomation.invoke(dispIdMember, rgvarg, rgdispidNamedArgs);
		if (pVarResult == null) return false;
		boolean result = pVarResult.getType() == OLE.VT_EMPTY;
		pVarResult.dispose();
		return result;
	}

	public boolean Navigate(final String url, String headers[]) {
		this.downloadURL = url;
		int count = 1;
		if (headers != null) count++;
		Variant[] rgvarg = new Variant[count];
		int[] rgdispidNamedArgs = new int[count];
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[] { "Navigate", "URL", "PostData", "Headers" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		int index = 0;
		rgvarg[index] = new Variant(url);
		rgdispidNamedArgs[index++] = rgdispid[1];

		if (headers != null) {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < headers.length; i++) {
				String current = headers[i];
				if (current != null) {
					int sep = current.indexOf(':');
					if (sep != -1) {
						String key = current.substring(0, sep).trim();
						String value = current.substring(sep + 1).trim();
						if (key.length() > 0 && value.length() > 0) {
							buffer.append(key);
							buffer.append(':');
							buffer.append(value);
							buffer.append("\r\n");
						}
					}
				}
			}
			rgvarg[index] = new Variant(buffer.toString());
			rgdispidNamedArgs[index++] = rgdispid[3];
		}

		Variant pVarResult = oleAutomation.invoke(rgdispid[0], rgvarg, rgdispidNamedArgs);

		for (int i = 0; i < count; i++) {
			rgvarg[i].dispose();
		}
		if (pVarResult == null) return false;
		boolean result = pVarResult.getType() == OLE.VT_EMPTY;
		pVarResult.dispose();
		return result;
	}

	/**
	 * Refreshes the currently viewed page.
	 *
	 * @return the platform-defined result code for the "Refresh" method invocation
	 */
	public void Refresh(){
		// dispid= 4294966746, type=METHOD, name="Refresh"
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"Refresh"}); 
		int dispIdMember = rgdispid[0];
		oleAutomation.invokeNoReply(dispIdMember);
	}

	/**
	 * Aborts loading of the current page.
	 *
	 * @return the platform-defined result code for the "Stop" method invocation
	 */
	public void Stop() {

		// dispid=106, type=METHOD, name="Stop"
		logger.log(Level.INFO,"Stopping the browser!!!");
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[]{"Stop"}); 
		int dispIdMember = rgdispid[0];
		oleAutomation.invoke(dispIdMember);
	}

	/**
	 * Get document variant value of current navigating.
	 * 
	 * @return
	 */
	public Variant getDocument() {
		int htmlDocId[] = oleAutomation.getIDsOfNames(new String[] { "Document" });
		if (htmlDocId == null)
			return null;
		return oleAutomation.getProperty(htmlDocId[0]);
	}

	/**
	 * @return title of the web browser
	 */
	public String getTitle() {
		int rgdispid[] = oleAutomation.getIDsOfNames(new String[] { "LocationName" });
		Variant property = oleAutomation.getProperty(rgdispid[0]);
		return property.getString();
	}

	/**
	 * Set silent true or false
	 * 
	 * @param silent
	 */
	public void setSilent(boolean silent) {
		int rgdispid[] = oleAutomation.getIDsOfNames(new String[] { "Silent" });
		oleAutomation.setProperty(rgdispid[0], new Variant[] { new Variant(silent) });
	}

	/**
	 * Refresh the browser.
	 */
	public void refresh() {

		int rgdispid[] = oleAutomation.getIDsOfNames(new String[] { "Refresh" });
		oleAutomation.invoke(rgdispid[0]);
	}

	public String getMimeType() {

		/* get the document object */
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[] {"Document"});
		Variant pVarResult = oleAutomation.getProperty(rgdispid[0]);
		if (pVarResult == null || pVarResult.getType() == COM.VT_EMPTY) {
			logger.log(Level.SEVERE,"Error while fetching the document");
			if (pVarResult != null) pVarResult.dispose ();
			return ""; //$NON-NLS-1$
		}
		//IHTMLDocument2
		OleAutomation htmlDocument = pVarResult.getAutomation();
		pVarResult.dispose();

		rgdispid = htmlDocument.getIDsOfNames(new String[] {"mimeType"});
		// bug when document is adobe PDF = rgdispid is null
		if (rgdispid != null) {
			pVarResult = htmlDocument.getProperty(rgdispid[0]);
			if (pVarResult == null || pVarResult.getType() == COM.VT_EMPTY) {
				logger.log(Level.SEVERE,"Error while fetching the Mime Type");

				if (pVarResult != null) pVarResult.dispose ();
				return ""; //$NON-NLS-1$
			}
			//System.out.println("org.eclipse.swt.ie: The Mime Type is: "+pVarResult.getString());
			String strOuterXMLtext = getOuterXmlText();
			if (strOuterXMLtext.startsWith("<?xml")) {
				return "XML Document";
			}
			return pVarResult.getString();
		}
		return "";
	}

	/**
	 * return the core data following a navigation with xmlexport.
	 * @return
	 */
	public String getOuterXmlText() {

		/* get the document object */
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[] {"Document"});
		Variant pVarResult = oleAutomation.getProperty(rgdispid[0]);
		if (pVarResult == null || pVarResult.getType() == COM.VT_EMPTY) {
			logger.log(Level.SEVERE,"Error while fetching the document");
			if (pVarResult != null) pVarResult.dispose ();
			return ""; //$NON-NLS-1$
		}
		//IHTMLDocument2
		OleAutomation htmlDocument = pVarResult.getAutomation();
		pVarResult.dispose();

		rgdispid = htmlDocument.getIDsOfNames(new String[] { "body" });
		pVarResult = htmlDocument.getProperty(rgdispid[0]);
		if (pVarResult == null || pVarResult.getType() == COM.VT_EMPTY) {
			if (pVarResult != null) pVarResult.dispose ();
			return ""; //$NON-NLS-1$
		}

		// IHTMLElement
		OleAutomation htmlElement = pVarResult.getAutomation();
		rgdispid = htmlElement.getIDsOfNames(new String[] { "outerText" });
		pVarResult = htmlElement.getProperty(rgdispid[0]);
		if (pVarResult == null || pVarResult.getType() == COM.VT_EMPTY) {
			logger.log(Level.SEVERE,"Error while fetching the outer text");
			if (pVarResult != null) pVarResult.dispose ();
			return ""; //$NON-NLS-1$
		}

		String outerText = pVarResult.getString().trim();
		String newline = System.getProperty("line.separator");
		outerText = outerText.replaceAll(newline+"-",newline);
		outerText = outerText.replaceAll("<Unknown>","Unknown");
		outerText = outerText.replace("-*<","<");
		outerText = outerText.replace("&","&amp;");
		//outerText = outerText.replace("\"","&quot;");
		// suppress annoying characters in the XML prolog
		outerText = outerText.replaceFirst("^([\\W]+)<","<");

		outerText = filterErroneousCharactersInAllNodes(outerText);
		return (outerText);
	}

	private static String filterErroneousCharactersInAllNodes(String strXML) {

		String finalPurifiedXMLString = "";
		String[] parts = strXML.split("<llnode");
		Boolean firstPass = false;
		for (String part : parts) {
			if (firstPass == true){
				finalPurifiedXMLString += " <llnode ";
			} else {
				firstPass = true;
			}
			//System.out.println(part);
			part = filterErroneousCharacters(part);
			finalPurifiedXMLString += filterErroneousCharacters(part);
		}
		//System.out.println(finalPurifiedXMLString);
		return finalPurifiedXMLString;
	}


	private static String filterErroneousCharacters(String strXML) {

		String[] keyWords = {"created=" , "createdby=", "createdbyname=" , "description=" , "id=", "modified=", "name=", "objname=" , "parentid=" , "ownedbyname=" , "size="};
		Map<Integer, String> keyWordsMap = new TreeMap<Integer, String>();

		for (String strKeyWord : keyWords) {
			int position = strXML.indexOf(strKeyWord);
			if (position > 0) {
				keyWordsMap.put(position, strKeyWord);
			}
		}
		Boolean descriptionFound = false;
		Boolean nextFound = false;
		int beginOfDescription = 0;
		int beginOfNext = 0;
		for (Integer position : keyWordsMap.keySet()) {
			if ((descriptionFound == true) && (nextFound == false)) {
				beginOfNext = position;
				nextFound = true;
			}
			//System.out.println("position = " + (position) + " --- map string = " + keyWordsMap.get(position));
			if (keyWordsMap.get(position).equalsIgnoreCase("description=")) {
				descriptionFound = true;
				beginOfDescription = position;
			}
		}
		if (descriptionFound && nextFound){
			String correctedStrXML = strXML.substring(0, beginOfDescription);
			correctedStrXML += "description=" + "\"";
			correctedStrXML += strXML.substring(beginOfDescription + "description=".length(), beginOfNext).replaceAll("\""," ");
			correctedStrXML += "\"  ";
			correctedStrXML += strXML.substring(beginOfNext, strXML.length());
			//System.out.println(correctedStrXML);
			return correctedStrXML;
		}

		return strXML;
	}

	/*
	 * manage replacement of double quotes inside a STRING attribute
	 */
	/*	private static String filterXMLerroneousCharacters(String strXML) {

		boolean tokenWithDoubleQuoteFound = false;
		boolean firstDoubleQuoteFound = false;

		String previousToken = "";
		String currentToken = "";

		StringBuffer stringBuffer = new StringBuffer();
		String delimiterSpace = " ";
		String delimiterDoubleQuote = "\"";

		StringTokenizer stringTokenizer = new StringTokenizer(strXML,delimiterSpace);
		while (stringTokenizer.hasMoreTokens()) {

			// loop through tokens separated by SPACEs
			String token = stringTokenizer.nextToken();
			if (currentToken.length() == 0) {
				previousToken = currentToken;
				currentToken = token;
			}
			else {
				previousToken = currentToken;
				currentToken = token;
			}
			// name of the token following either description or name
			if (token.startsWith("id=") || token.startsWith("extendedData=") || token.startsWith("objname=") || token.startsWith("path=")) {
				if (tokenWithDoubleQuoteFound == true) {
					// end the previous description token with a double quote
					//System.out.println("=-=-=-=-=closing=-=-=-=-=-");
					tokenWithDoubleQuoteFound = false;
					firstDoubleQuoteFound = false;
					stringBuffer.append(delimiterDoubleQuote);
					stringBuffer.append(delimiterSpace);
				}
				// add the current token
				stringBuffer.append(token);
				stringBuffer.append(delimiterSpace);
			}
			else if (tokenWithDoubleQuoteFound == true)  {
				// until the end of the token all double quotes are replaced by SPAces
				//System.out.println("+++++replacing++++ double quotes+++++");
				stringBuffer.append(token.replace(delimiterDoubleQuote,delimiterSpace));
				stringBuffer.append(delimiterSpace);
			}
			else if (token.startsWith("description=") || token.startsWith("name=")) {
				if (token.startsWith("description=")) {
					//System.out.println("current token is description");
					currentToken = "description";
				}
				if (token.startsWith("name=")) {
					//System.out.println("current token is name");
					currentToken = "name";
				}
				if (token.equals("description=\"\"") || token.equals("name=\"\"")) {
					// description is empty = means followed by a pair of double quotes
					if (token.equals("description=\"\"")) {
						//System.out.println("---------description token is empty");
					}
					if (token.equals("name=\"\"")) {
						//System.out.println("name token is empty");
					}
					stringBuffer.append(token);
					stringBuffer.append(delimiterSpace);
				}
				else {
					// we have found either a description or a name token.
					// both are not empty
					// we are looking for the end of the attribute avoiding occurrence of double quotes inside the string
					//System.out.println("description or name are starting with a single double quote");
					tokenWithDoubleQuoteFound = true;
					StringTokenizer stringDescriptionTokenizer = new StringTokenizer(token,delimiterDoubleQuote);
					while (stringDescriptionTokenizer.hasMoreTokens()) {
						String content = stringDescriptionTokenizer.nextToken();
						//System.out.println("split: "+content);
						if (firstDoubleQuoteFound == false) {
							firstDoubleQuoteFound = true;
							stringBuffer.append(content);
							stringBuffer.append(delimiterDoubleQuote);
						}
						else {
							stringBuffer.append(content);
							stringBuffer.append(delimiterSpace);
						}
					}
				}
			}
			else {
				stringBuffer.append(token);
				stringBuffer.append(delimiterSpace);
			}
			//System.out.println("--------------");
			//System.out.println(stringBuffer);
		}
		//System.out.println(stringBuffer.toString().trim());
		return stringBuffer.toString().trim();
	}


	private String getInnerXmlText() {

		 get the document object 
		int[] rgdispid = oleAutomation.getIDsOfNames(new String[] {"Document"});
		Variant pVarResult = oleAutomation.getProperty(rgdispid[0]);
		if (pVarResult == null || pVarResult.getType() == COM.VT_EMPTY) {
			System.err.println("OleWebBrowser: error while fetching the document: ");
			if (pVarResult != null) pVarResult.dispose ();
			return ""; //$NON-NLS-1$
		}
		//IHTMLDocument2
		OleAutomation htmlDocument = pVarResult.getAutomation();
		pVarResult.dispose();

		rgdispid = htmlDocument.getIDsOfNames(new String[] { "body" });
		pVarResult = htmlDocument.getProperty(rgdispid[0]);
		if (pVarResult == null || pVarResult.getType() == COM.VT_EMPTY) {
			System.err.println("OleWebBrowser: error while fetching the body: ");
			if (pVarResult != null) pVarResult.dispose ();
			return ""; //$NON-NLS-1$
		}

		// IHTMLElement
		OleAutomation htmlElement = pVarResult.getAutomation();
		rgdispid = htmlElement.getIDsOfNames(new String[] { "innerText" });
		pVarResult = htmlElement.getProperty(rgdispid[0]);
		if (pVarResult == null || pVarResult.getType() == COM.VT_EMPTY) {
			System.err.println("OleWebBrowser: error while fetching the inner Text: ");
			if (pVarResult != null) pVarResult.dispose ();
			return ""; //$NON-NLS-1$
		}
		String innerText = pVarResult.getString().trim();
		 \n 	The newline (line feed) character ('\u000A'), replacement) 
		String newline = System.getProperty("line.separator");
		innerText = innerText.replaceAll(newline+"-",newline);
		innerText = innerText.replaceAll("<Unknown>","Unknown");
		innerText = innerText.replace("-*<","<");
		innerText = innerText.replace("&","&amp;"); 
		// suppress annoying characters in the XML prolog
		innerText = innerText.replaceFirst("^([\\W]+)<","<");

		return (innerText);
	}*/

	public void ClearSessions () {
		new Runnable() {
			public void run() {
				if (OS.IsPPC) return;
				OS.InternetSetOption (0, OS.INTERNET_OPTION_END_BROWSER_SESSION, 0, 0);
			}
		};

	}

}

