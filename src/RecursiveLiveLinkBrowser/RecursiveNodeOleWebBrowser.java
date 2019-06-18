package RecursiveLiveLinkBrowser;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.TCHAR;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.ole.win32.OLE;
import org.eclipse.swt.ole.win32.OleAutomation;
import org.eclipse.swt.ole.win32.OleControlSite;
import org.eclipse.swt.ole.win32.OleEvent;
import org.eclipse.swt.ole.win32.OleFrame;
import org.eclipse.swt.ole.win32.OleListener;
import org.eclipse.swt.ole.win32.Variant;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import Antxr.LiveLinkParser;
import JExcelApi.WritableExcelFile;
import JExcelApi.WritableExcelSheet.Excel2003MaxRowException;
import LiveLinkCore.LiveLinkNode;
import LiveLinkCore.LiveLinkVolume;
import LiveLinkCore.ShellErrorMessage;
import LiveLinkCore.ShellInformationMessage;

import com.javadude.antxr.RecognitionException;
import com.javadude.antxr.TokenStreamException;
import com.javadude.antxr.scanner.BasicKXml2XMLPullTokenStream;

public class RecursiveNodeOleWebBrowser extends CTabItem {

	private static final Logger logger = Logger.getLogger(RecursiveNodeOleWebBrowser.class.getName()); 

	private static final String CLSID_SHELLEXPLORER1 = "{EAB22AC3-30C1-11CF-A7EB-0000C05BAE0B}";

	static final int DLCTL_DOWNLOADONLY 	= 0x00000800;
	static final int DLCTL_SILENT 			= 0x40000000;
	static final int DLCTL_RESYNCHRONIZE 	= 0x00002000;
	
	private OleFrame webFrame = null;
	private LiveLinkNode rootNode = null;
	
	private Shell parent = null;
	private Composite contentPanel = null;

	private OleControlSite webControlSite = null;
	private OleWebBrowser oleWebBrowser = null;
	private static Label webStatus = null;
	private static ProgressBar webProgress = null;

	private boolean activated = false;
	private boolean webBrowserInitialized = false;

	private int numberOfRetry = 0;
	private final int maxNumberOfRetry = 3 ;
	
	private Boolean indentedEXCELresults = false;

	public RecursiveNodeOleWebBrowser(Shell _shell, CTabFolder tabFolder,  LiveLinkNode _rootNode, boolean _indentedEXCELresults ) {
		
		super(tabFolder, SWT.CLOSE);
		
		this.parent = _shell;
		this.rootNode = _rootNode;
		this.setText(_rootNode.getLiveLinkObjectId() + '-' + _rootNode.getName());
		this.indentedEXCELresults = _indentedEXCELresults;
		this.contentPanel = new Composite (tabFolder, SWT.BORDER);
		
		// this tab is set as selected
		tabFolder.setSelection(this);
				
		GridLayout gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.numColumns = 1;
		
		buildStatusProgressBarRow();
		initWebBrowser();
		
		this.contentPanel.setLayout(gridLayout);
		this.setControl(this.contentPanel);

		// navigate : in place activate the ActiveX control		
		this.activated = (this.webControlSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE) == OLE.S_OK);
		logger.log(Level.INFO,"Is the browser activated ? "+this.activated);
		if (this.activated) {

			logger.log(Level.INFO,"Browse to: " + this.rootNode.getBrowseURL().toExternalForm());
			this.oleWebBrowser.Navigate(this.rootNode.getBrowseURL().toExternalForm());

		}
	}
	
	
	private void buildStatusProgressBarRow() {

		Composite lastRowComposite = new Composite(this.contentPanel, SWT.FILL | SWT.BORDER);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.makeColumnsEqualWidth = false;
		lastRowComposite.setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		// composite grows horizontally
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		lastRowComposite.setLayoutData(gridData);

		// Add a label for displaying status messages as they are received from the control
		RecursiveNodeOleWebBrowser.webStatus = new Label(lastRowComposite, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
		gridData.horizontalSpan = 2;
		// text grows horizontally with the frame
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		RecursiveNodeOleWebBrowser.webStatus.setToolTipText("status message");
		RecursiveNodeOleWebBrowser.webStatus.setLayoutData(gridData);
		Color greenColor = this.parent.getDisplay().getSystemColor(SWT.COLOR_GREEN);
		RecursiveNodeOleWebBrowser.webStatus.setBackground(greenColor);

		// Add a progress bar to display downloading progress information
		RecursiveNodeOleWebBrowser.webProgress = new ProgressBar(lastRowComposite, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		gridData.grabExcessHorizontalSpace = false;
		gridData.grabExcessVerticalSpace = false;
		RecursiveNodeOleWebBrowser.webProgress.setLayoutData(gridData);	
		
		lastRowComposite.redraw();
		lastRowComposite.update();

	}

	
	private String getShellExplorerProgId() {

		String progId = "Shell.Explorer"; //$NON-NLS-1$
		TCHAR key = new TCHAR(0, "Shell.Explorer\\CLSID", true); //$NON-NLS-1$
		long[] phkResult = new long[1];
		if (OS.RegOpenKeyEx(OS.HKEY_CLASSES_ROOT, key, 0, OS.KEY_READ, phkResult) == 0) {
			int[] lpcbData = new int[1];
			int result = OS.RegQueryValueEx(phkResult[0], null, 0, null, (TCHAR) null, lpcbData);
			if (result == 0) {
				TCHAR lpData = new TCHAR(0, lpcbData[0] / TCHAR.sizeof);
				result = OS.RegQueryValueEx(phkResult[0], null, 0, null, lpData, lpcbData);
				if (result == 0) {
					String clsid = lpData.toString(0, lpData.strlen());
					if (clsid.equals(CLSID_SHELLEXPLORER1)) {
						/*
						 * Shell.Explorer.1 is the default, ensure that Shell.Explorer.2 is available
						 */
						logger.log(Level.INFO,"Shell Explorer.1 is the default!!!!!!!!!!!!!!!");
						key = new TCHAR(0, "Shell.Explorer.2", true); //$NON-NLS-1$
						long[] phkResult2 = new long[1];
						if (OS.RegOpenKeyEx(OS.HKEY_CLASSES_ROOT, key, 0, OS.KEY_READ, phkResult2) == 0) {
							/* specify that Shell.Explorer.2 is to be used */
							OS.RegCloseKey(phkResult2[0]);
							progId = "Shell.Explorer.2"; //$NON-NLS-1$
						}
					}
				}
			}
			OS.RegCloseKey(phkResult[0]);
		}
		return progId;
	}

	
	private void initWebBrowser() {
		try {
			this.webFrame = new OleFrame(this.contentPanel, SWT.BORDER);
			/*
			 * Registry entry HKEY_CLASSES_ROOT\Shell.Explorer\CLSID indicates which version of Shell.Explorer to use by
			 * default. We usually want to use this value because it typically points at the newest one that is available.
			 * However it is possible for this registry entry to be changed by another application to point at some other
			 * Shell.Explorer version. The Browser depends on the Shell.Explorer version being at least Shell.Explorer.2. If
			 * it is detected in the registry to be Shell.Explorer.1 then change the progId that will be embedded to
			 * explicitly specify Shell.Explorer.2.
			 */
			this.webControlSite = new OleControlSite(this.webFrame, SWT.NONE, getShellExplorerProgId());

			//Variant download = new Variant(DLCTL_NO_SCRIPTS || DLCTL_SILENT);
			//Variant download = new Variant(DLCTL_NO_SCRIPTS);
			// DLCTL_DOWNLOADONLY: The page will only be downloaded, not displayed.
			Variant download = new Variant(DLCTL_DOWNLOADONLY | DLCTL_SILENT | DLCTL_RESYNCHRONIZE);		
			this.webControlSite.setSiteProperty(DLCTL_DOWNLOADONLY | DLCTL_SILENT | DLCTL_RESYNCHRONIZE, download);

			// Create an Automation object for access to extended capabilities
			OleAutomation oleAutomation = new OleAutomation(this.webControlSite);
			this.oleWebBrowser = new OleWebBrowser(oleAutomation);

			//this.oleWebBrowser.ClearSessions();
			this.oleWebBrowser.setSilent(false);

			GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
			this.webFrame.setLayoutData(gridData);

			this.webControlSite.addEventListener(OleWebBrowser.DocumentComplete , new OleListener() {

				public void handleEvent(OleEvent event) {
					//System.out.println("==================");
					logger.log(Level.INFO , "Ole Web Browser Tab: Document Complete event fired: "+event.toString());
					event.doit = true;

					//System.out.println("The Web Browser current location=  "+oleWebBrowser.getLocationName());
					//System.out.println("The Web Browser is currently viewing the URL=  "+oleWebBrowser.getLocationURL());

					//System.out.println("==================");
				}
			});

		
			// Respond to ProgressChange events by updating the Progress bar
			this.webControlSite.addEventListener(OleWebBrowser.ProgressChange, new OleListener() {
				public void handleEvent(OleEvent event) {
					Variant progress = event.arguments[0];
					Variant maxProgress = event.arguments[1];
					if (progress == null || maxProgress == null)
						return;
					//System.out.println("OleWebBrowserTab: progress change: "+progress.getInt()+ " max progress= "+maxProgress.getInt());
					RecursiveNodeOleWebBrowser.webProgress.setMaximum(maxProgress.getInt());
					RecursiveNodeOleWebBrowser.webProgress.setSelection(progress.getInt());
				}
			});

			// Respond to StatusTextChange events by updating the Status Text label
			this.webControlSite.addEventListener(OleWebBrowser.StatusTextChange, new OleListener() {
				public void handleEvent(OleEvent event) {
					Variant statusText = event.arguments[0];
					if (statusText == null)	return;
					String text = statusText.getString();
					if (text != null) {
						//System.out.println("OleWebBrowserTab: status Text Change: "+text);
						RecursiveNodeOleWebBrowser.webStatus.setText(text);
					}
				}
			});

			// Listen for changes to the ready state and print out the current state 
			//WebControlSitePropertyListener propListener = new WebControlSitePropertyListener(this.oleWebBrowser);

			this.webControlSite.addPropertyListener(OleWebBrowser.DISPID_READYSTATE, new OleListener() {
				public void handleEvent(OleEvent event) {
					if (event.detail == OLE.PROPERTY_CHANGING) return;
					int state = oleWebBrowser.getReadyState();
					switch (state) {
					case OleWebBrowser.READYSTATE_UNINITIALIZED:
						//logger.log(Level.INFO,"browser.State.Uninitialized.text");
						webStatus.setText("browser.State.READYSTATE_UNINITIALIZED");

						break;
					case OleWebBrowser.READYSTATE_LOADING:
						//logger.log(Level.INFO,"browser.State.Loading.text");
						webStatus.setText("browser.State.READYSTATE_LOADING");

						break;
					case OleWebBrowser.READYSTATE_LOADED:
						logger.log(Level.INFO,"browser.State.Loaded.text");
						webStatus.setText("browser.State.READYSTATE_LOADED");

						break;
					case OleWebBrowser.READYSTATE_INTERACTIVE:
						//logger.log(Level.INFO,"browser.State.Interactive.text");
						webStatus.setText("browser.State.READYSTATE_INTERACTIVE");

						break;
					case OleWebBrowser.READYSTATE_COMPLETE:
						webStatus.setText("browser.State.READYSTATE_COMPLETE");

						//logger.log(Level.INFO,"browser.State.Complete.text");

						String MimeType = oleWebBrowser.getMimeType();
						logger.log(Level.INFO,"Mime Type: "+MimeType);

						String LocationURL = oleWebBrowser.getLocationURL();
						logger.log(Level.INFO,"location URL: "+LocationURL);

						// Analyze the answer provided by the browser
						analyseHttpAnswer();

						//logger.log(Level.INFO,"==========end of Analyse Http Answer===============");
						break;
					}
				}
			});


			// Listen for changes to the active command states
			this.webControlSite.addEventListener(OleWebBrowser.CommandStateChange, new OleListener() {
				public void handleEvent(OleEvent event) {
					if (event.type != OleWebBrowser.CommandStateChange) return;
					final int commandID =
							(event.arguments[0] != null) ? event.arguments[0].getInt() : 0;
							final boolean commandEnabled =
									(event.arguments[1] != null) ? event.arguments[1].getBoolean() : false;
									//logger.log(Level.INFO, " command enabled: " + ((commandEnabled) ? "true" : "false" ));

									switch (commandID) {
									case OleWebBrowser.CSC_NAVIGATEBACK:
										//System.out.println("OleWebBrowserTab: webCommandBackward.setEnabled(commandEnabled)");
										//webCommandBackward.setEnabled(commandEnabled);
										break;
									case OleWebBrowser.CSC_NAVIGATEFORWARD:
										//System.out.println("OleWebBrowserTab: webCommandForward.setEnabled(commandEnabled)");
										//webCommandForward.setEnabled(commandEnabled);
										break;
									}
				}
			});

		} 
		catch (SWTException ex) {
			// Creation may have failed because control is not installed on machine
			Label label = new Label(webFrame, SWT.BORDER);
			label.setText("OleWebBrowserTab: error.CouldNotCreateBrowserControl");
		}
	}
	
	
	/**
	 * This method allows to sleep during a number of milliseconds
	 * @param milliseconds
	 */
	private void sleepMilliseconds(int milliseconds) {
		this.parent.getDisplay().timerExec (milliseconds, new Runnable () {
			public void run () {
				//System.out.println ("100 milliseconds");
			}
		});
	}
	
	private void analyseHttpAnswer() {

		String locationURL = oleWebBrowser.getLocationURL();
		String MimeType = oleWebBrowser.getMimeType();

		/**
		 *  Following cases might occur
		 *  1) URL (redirected) points to https websso
		 *  2) URL points to LiveLink 
		 *  	2.1) Mime Type = HTML Document => probably an error
		 *  	2.2) Mime Type = XML Document
		 *  
		 */

		if (locationURL.toLowerCase().contains("websso")) {
			// it is an authentication page 
			logger.log(Level.INFO,"location contains WebSSO: "+locationURL);
			
			if (MimeType.toLowerCase().contains("html") || MimeType.toLowerCase().contains("document") || MimeType.toLowerCase().contains("fichier") ) {
				
				logger.log(Level.INFO,"this is probably the authentication page...");
				//OleWebBrowserTab.this.oleWebBrowser.Refresh();
				
				//String webPageContent = this.oleWebBrowser.getOuterXmlText();

				// location contains Web sso : need to wait until the user has provided his/her LOGIN and PASSWORD.
				new ShellInformationMessage(this.parent.getDisplay(),
						this.parent,"Please provide Login / Password and then click Home and then Go to start browsing");
				// need to authenticate before browsing the URL
				this.webBrowserInitialized = false;
			}
			else {
			//if (MimeType.contains("HTML Document") == false) {
				// location text returns to the root node with xmlexport 
				if (this.numberOfRetry < maxNumberOfRetry) {

					this.parent.getDisplay().syncExec(new Runnable() {
						// navigate again to the home location - in browse mode
						public void run() {
							// need to clear the tree before re starting the browser
							// launch browser with new location / new URL
							RecursiveNodeOleWebBrowser.this.oleWebBrowser.Navigate(rootNode.getBrowseURL().toExternalForm());
						}
					});
					// increment number of retry.
					this.numberOfRetry++;
				}
			}
		}
		else if (locationURL.toLowerCase().contains("xmlexport")) {
			//logger.log(Level.INFO,"we are expecting an XML document");

			if (MimeType.equals("Document XML") || MimeType.equals("XML Document") || MimeType.toLowerCase().contains("xml")) {
				
				//logger.log(Level.INFO,"Success: received document is an XML one - as expected!!!");
				// analyse the XML answer
				analyseXmlAnswer();

			}
			else {
				logger.log(Level.INFO,"location was expecting XML answer but did not get that kind of Mime Type....");
				// stop the browser before next retry
				
				// next retry
				if (RecursiveNodeOleWebBrowser.this.numberOfRetry < maxNumberOfRetry) {
					//oleWebBrowser.ClearSessions();
					oleWebBrowser.Navigate(oleWebBrowser.getLocationURL());
					
					sleepMilliseconds(300);
					this.numberOfRetry++;
				}
				else {
					logger.log(Level.INFO,"End of retry process... stopping all....");
				}
			}
		}
		else {
			/*
			 * location does not contain websso and location does not contain xml export
			 */
			if (MimeType.toLowerCase().contains("html") || MimeType.toLowerCase().contains("document") == true) {
				logger.log(Level.INFO,"Document is HTML one");

				this.parent.getDisplay().syncExec(new Runnable() {
					// navigate again to the home location - in xml mode
					public void run() {
						// launch browser with new location / new URL
						RecursiveNodeOleWebBrowser.this.oleWebBrowser.Navigate(rootNode.getXmlExportURL().toExternalForm());
					}
				});
			}
		}
	}
	
	private void analyseXmlAnswer() {
		
		//logger.log (Level.INFO, " ---- analyse XML answer ---- ");
		String locationURL = this.oleWebBrowser.getLocationURL();

		// reset number of retry
		this.numberOfRetry = 0;

		//String strXML = oleWebBrowser.getInnerXmlText();
		String strXML = this.oleWebBrowser.getOuterXmlText();
		//logger.log(Level.INFO,strXML);
		if (strXML.length()>0) {
			try {
				BasicKXml2XMLPullTokenStream stream =
						new BasicKXml2XMLPullTokenStream(new StringReader(strXML),
								Antxr.LiveLinkParser.class,
								false,// namespace aware
								true); // relaxed

				LiveLinkParser parser = new LiveLinkParser(stream);
				LiveLinkCore.LiveLinkXmlExport llXmlExport = parser.document();
				//logger.log(Level.INFO,"XML parsing succeeded !");
				//logger.log(Level.INFO,"LiveLink application version: "+llXmlExport.getAppVersion());
				//logger.log(Level.INFO,"LiveLink action: "+llXmlExport.getSrc());
				final LiveLinkNode llNode = llXmlExport.getLiveLinkNode();
				if (llNode != null) {
					
					//logger.log(Level.INFO,strXML);
					llNode.setStrXML(strXML);
					llNode.setLiveLinkURL(locationURL);
					
					// this node is already browsed !!!
					llNode.setBrowsed(true);
					//llNode.dump();
					this.rootNode.updateAndExtend(llNode);
					
					final LiveLinkNode nextNode = this.rootNode.findNextToBeBrowsedNode(this.rootNode);
					if (nextNode != null) {
						this.parent.getDisplay().syncExec(new Runnable() {
							// navigate again to the home location - in xml mode
							public void run() {
								// launch browser with new location / new URL
								RecursiveNodeOleWebBrowser.this.oleWebBrowser.Navigate(nextNode.getXmlExportURL().toExternalForm());
							}
						});
					} else {
						logger.log(Level.INFO, " --------------- automatic browser - end --------------------- ");
						WritableExcelFile writableExcelFile = new WritableExcelFile(this.parent.getDisplay());
						if (writableExcelFile.writeReadMeSheet(this.rootNode)) {
							try {
								boolean b = writableExcelFile.generateLiveLinkNodeRecursiveIndex(this.rootNode, this.indentedEXCELresults);
								if (b) {
									writableExcelFile.Close();
									new ShellInformationMessage(this.parent.getDisplay(),this.parent,
											"EXCEL file generated sucessfully= " + writableExcelFile.getExcelFilePath());
								}
							} catch (Excel2003MaxRowException ex) {
								new ShellInformationMessage(this.parent.getDisplay(),this.parent,
										"EXCEL 2003 - row index exceeds max row number");
							}
							
						}
					}
					
					// all is OK
					this.webBrowserInitialized = true;
				}
				else {
					LiveLinkVolume liveLinkVolume = llXmlExport.getLiveLinkVolume();
					if (liveLinkVolume != null) {
						logger.log(Level.INFO,"===========================================================================");
						logger.log(Level.INFO,"LiveLink export contains a llVolume: should reached the root of the Livelink tree!!!!!");
						logger.log(Level.INFO,"===========================================================================");
						new ShellInformationMessage(this.parent.getDisplay(),this.parent, "you did reach the root of LiveLink");

						RecursiveNodeOleWebBrowser.this.parent.getDisplay().syncExec(new Runnable() {
							// navigate again to the home location - in browse mode
							public void run() {
								// launch browser with new location / new URL
								oleWebBrowser.GoBack();
							}
						});
					}
					else {
						logger.log(Level.SEVERE,"XML parser fails!!!");
						logger.log(Level.SEVERE,strXML);

						new ShellErrorMessage(this.parent.getDisplay(),
								this.parent,"XML Parser failed!!! on node "+locationURL+"...\r\n..."+strXML);
					}
				}
			} catch (RecognitionException e) {
				e.printStackTrace();

				logger.log(Level.SEVERE,"XML parser fails!!!");
				logger.log(Level.SEVERE,strXML);
				final LiveLinkNode llNode = new LiveLinkNode(strXML,locationURL);

			} catch (TokenStreamException e) {
				e.printStackTrace();

				logger.log(Level.SEVERE,"XML parser fails!!!");
				final LiveLinkNode llNode = new LiveLinkNode(strXML,locationURL);
			}
		}
	}
	
	public boolean isWebBrowserInitialized() {
		return webBrowserInitialized;
	}

}
