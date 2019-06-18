package RecursiveLiveLinkBrowser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
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
import LiveLinkCore.LiveLinkNode;
import LiveLinkCore.LiveLinkObjectImageFactory;
import LiveLinkCore.LiveLinkVolume;
import LiveLinkCore.ShellErrorMessage;
import LiveLinkCore.ShellInformationMessage;
import MimeType.LiveLinkMimeTypeSet;

import com.javadude.antxr.RecognitionException;
import com.javadude.antxr.TokenStreamException;
import com.javadude.antxr.scanner.BasicKXml2XMLPullTokenStream;


public class OleWebBrowserTab extends CTabItem {

	private static final String CLSID_SHELLEXPLORER1 = "{EAB22AC3-30C1-11CF-A7EB-0000C05BAE0B}";
	private static final Logger logger = Logger.getLogger(OleWebBrowserTab.class.getName()); 

	private final int maxNumberOfRetry = 3 ;
	private int numberOfRetry = 0;

	private Composite parent = null;
	private Composite contentPanel = null;

	//private static final int DISPID_AMBIENT_DLCONTROL = -5512;
	//private static final int DLCTL_NO_SCRIPTS = 0x80;
	static final int DLCTL_DOWNLOADONLY = 0x00000800;
	static final int DLCTL_SILENT = 0x40000000;
	static final int DLCTL_RESYNCHRONIZE = 0x00002000;

	private ProgressBar webProgress = null;
	private Label webStatus = null;

	private boolean activated = false;
	private boolean webBrowserInitialized = false;

	private OleFrame webFrame = null;
	private OleControlSite webControlSite = null;
	private OleWebBrowser oleWebBrowser = null;

	private SashForm mainSashForm = null;
	private SashForm secondarySashForm = null;

	private RecursiveLiveLinkTreeCompositeObserver recursiveLiveLinkTreeComposite = null;
	private LiveLinkMimeTypeSet llMimeTypeSet = null;
	private LiveLinkObjectImageFactory llObjectImageFactory = null;
	
	private LiveLinkNode rootNode = null;
	
	// these are the composites that are also observer of any changes to the reference LiveLinkNode
	private NavigationAreaCompositeObserver navigationAreaComposite = null;
	public NavigationAreaCompositeObserver getNavigationAreaComposite() {
		return navigationAreaComposite;
	}

	private LiveLinkNodeKTableCompositeObserver liveLinkNodeKTableComposite = null;
	
	private CTabFolder tabFolder = null;

	public OleWebBrowserTab(Shell shell ,
			CTabFolder tabFolder, 
			LiveLinkMimeTypeSet llMimeTypeSet,
			LiveLinkObjectImageFactory llObjectImageFactory,
			LiveLinkNode rootNode,
			String project_name) {
		
		// adding SWT.CLOSE allows this tab to be deleted
		super(tabFolder, SWT.CLOSE);

		this.parent = shell;
		this.rootNode = rootNode;
		this.setTabFolder(tabFolder);

		this.llMimeTypeSet = llMimeTypeSet;
		this.llObjectImageFactory = llObjectImageFactory;

		this.setText(project_name);

		// add an image to quickly associate the tab to the image
		InputStream in = OleWebBrowserTab.class.getResourceAsStream("enterprise.gif");
		if (in != null) {
			Image image = new Image(this.parent.getDisplay() ,in);
			this.setImage(image);
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.contentPanel = new Composite (tabFolder, SWT.NONE);

		GridLayout gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.numColumns = 1;
		this.contentPanel.setLayout(gridLayout);

		this.setControl(this.contentPanel);

		// first row with URL and navigation buttons
		buildNavigationAreaComposite();
		
		// sash form with Live Link tree and XML results
		initWebBrowserComposite();
		
		// create the status bar with the export button
		buildStatusProgressBarRow();
		
		// need to do this as the status Bar has been just initialized and LL composite needs to use it
		//this.recursiveLiveLinkTreeComposite.setStatusBar(this.webStatus);

		// navigate : in place activate the ActiveX control		
		this.activated = (this.webControlSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE) == OLE.S_OK);
		logger.log(Level.INFO,"Is the browser activated ? "+this.activated);
		if (this.activated) {

			logger.log(Level.INFO,"Browse to: " + this.rootNode.getBrowseURL().toExternalForm());
			this.oleWebBrowser.Navigate(this.rootNode.getBrowseURL().toExternalForm());

			// zoom text size
			TextSizeZoom();
		}
		this.navigationAreaComposite.updateOleWebBrowser(this.oleWebBrowser, this.recursiveLiveLinkTreeComposite);
	}

	public boolean isConnected() {
		return this.activated && this.webBrowserInitialized;
	}

	/**
	 * when a livelink Node is selected, the navigation area is updated with its URL
	 */
	private void buildNavigationAreaComposite() {

		this.navigationAreaComposite = new NavigationAreaCompositeObserver(this.contentPanel, this.rootNode);
		this.rootNode.addObserver(this.navigationAreaComposite);
		
	}

	private void initWebBrowserComposite() {

		this.mainSashForm = new SashForm (this.contentPanel, SWT.FILL | SWT.BORDER);
		this.mainSashForm.setLayout(new GridLayout());

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		this.mainSashForm.setLayoutData(gridData);

		// create the composite that will contain the tree
		this.recursiveLiveLinkTreeComposite = new RecursiveLiveLinkTreeCompositeObserver( this.mainSashForm, this.tabFolder, this.llMimeTypeSet, this.llObjectImageFactory, this, this.rootNode);

		this.secondarySashForm = new SashForm(this.mainSashForm, SWT.FILL | SWT.BORDER);
		// the secondary sash form is build vertically : it has a table on the top and the browser frame on the bottom
		// the secondary sash form occupies the right panel of the main sash form
		this.secondarySashForm.setOrientation(SWT.VERTICAL);
		this.secondarySashForm.setLayout(new GridLayout());

		gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.heightHint = 120;
		gridData.grabExcessHorizontalSpace= true;
		gridData.grabExcessVerticalSpace = false;
		this.secondarySashForm.setLayoutData(gridData); 

		// the table composite showing all informations about the content of a node
		//tableComp = new Composite(this.secondarySashForm,SWT.FILL | SWT.BORDER);
		//tableComp.setLayout(new FillLayout());

		this.liveLinkNodeKTableComposite = new LiveLinkNodeKTableCompositeObserver(this.secondarySashForm);
		
		// initialize the web browser
		initWebBrowser();
		// need to wait until the web browser frame has been loaded into the secondary sash form before setting the weights
		this.secondarySashForm.setWeights(new int[] {50,50});

		this.recursiveLiveLinkTreeComposite.setOleWebBrowser(oleWebBrowser);
		this.mainSashForm.setWeights(new int[] {20,80});

	
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
			this.webFrame = new OleFrame(this.secondarySashForm, SWT.BORDER);
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
			// silent mode = node dialog

			//this.oleWebBrowser.ClearSessions();
			sleepMilliseconds(7000);
			this.oleWebBrowser.setSilent(false);

			GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
			this.webFrame.setLayoutData(gridData);
			
			this.webControlSite.addEventListener(OleWebBrowser.DocumentComplete , new OleListener() {

				public void handleEvent(OleEvent event) {
					//System.out.println("==================");
					//System.out.println("Ole Web Browser Tab: Document Complete event fired: "+event.toString());
					event.doit = true;
					
					//System.out.println("The Web Browser current location=  "+oleWebBrowser.getLocationName());
					//System.out.println("The Web Browser is currently viewing the URL=  "+oleWebBrowser.getLocationURL());
					
					//System.out.println("==================");
				}
			});

			this.webControlSite.addEventListener(OleWebBrowser.FileDownLoad, new OleListener() {

				public void handleEvent(OleEvent event) {
					//System.out.println("==================");
					//System.out.println("Ole Web Browser Tab: File DownLoad is beginning: "+event.toString());
					
					//System.out.println("The Web Browser is currently viewing the URL=  "+oleWebBrowser.getLocationName());
					//System.out.println("The Web Browser is currently viewing the URL=  "+oleWebBrowser.getLocationURL());
					
					event.doit = true;
					//System.out.println("==================");
				}
			});
			
			this.webControlSite.addEventListener(OleWebBrowser.DownloadBegin, new OleListener() {
				public void handleEvent(OleEvent event) {				
					//logger.log(Level.INFO , "OleWebBrowserTab: DownLoad is beginning: event type: "+event.type+" event detail: "+event.detail);
					event.doit = true;
				}
			});

			// Respond to ProgressChange events by updating the Progress bar
			this.webControlSite.addEventListener(OleWebBrowser.ProgressChange, new OleListener() {
				public void handleEvent(OleEvent event) {
					Variant progress = event.arguments[0];
					Variant maxProgress = event.arguments[1];
					if (progress == null || maxProgress == null)
						return;
					webProgress.setMaximum(maxProgress.getInt());
					webProgress.setSelection(progress.getInt());
					//System.out.println("OleWebBrowserTab: progress change: "+progress.getInt()+ " max progress= "+maxProgress.getInt());

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
						webStatus.setText(text);
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
						webStatus.setText("browser.State.Uninitialized.text");

						break;
					case OleWebBrowser.READYSTATE_LOADING:
						//logger.log(Level.INFO,"browser.State.Loading.text");
						webStatus.setText("browser.State.Loading.text");

						break;
					case OleWebBrowser.READYSTATE_LOADED:
						//logger.log(Level.INFO,"browser.State.Loaded.text");
						webStatus.setText("browser.State.Loaded.text");

						break;
					case OleWebBrowser.READYSTATE_INTERACTIVE:
						//logger.log(Level.INFO,"browser.State.Interactive.text");
						webStatus.setText("browser.State.Interactive.text");

						break;
					case OleWebBrowser.READYSTATE_COMPLETE:
						
						logger.log(Level.INFO,"browser.State.Complete.text");
						webStatus.setText("browser.State.Complete.text");
						
						String MimeType = oleWebBrowser.getMimeType();
						logger.log(Level.INFO,"Mime Type: "+MimeType);
						
						String LocationURL = oleWebBrowser.getLocationURL();
						logger.log(Level.INFO,"location URL: "+LocationURL);
						
						String answer = oleWebBrowser.getOuterXmlText();
						if (answer.startsWith("<?xml version=")) {
							logger.log(Level.INFO, "it is an XML answer");
						}

						// Analyze the answer provided by the browser
						analyseHttpAnswer();

						//enableNavigationButtons();
						logger.log(Level.INFO,"==========end of Analyse Http Answer===============");
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

	private void analyseHttpAnswer() {

		String locationURL = oleWebBrowser.getLocationURL();
		String MimeType = oleWebBrowser.getMimeType();
		
		String answer = oleWebBrowser.getOuterXmlText();
		if (answer.startsWith("<?xml version=")) {
			logger.log(Level.INFO, "it is an XML answer");
			MimeType = "XML Document";
		}

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
						this.parent.getShell(),"Please provide Login / Password and then click Home and then Go to start browsing");
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
							OleWebBrowserTab.this.recursiveLiveLinkTreeComposite.getLiveLinkNodeTree().removeAll();
							// launch browser with new location / new URL
							OleWebBrowserTab.this.oleWebBrowser.Navigate(rootNode.getBrowseURL().toExternalForm());
						}
					});
					// increment number of retry.
					this.numberOfRetry++;
				}
			}
		}
		else if (locationURL.toLowerCase().contains("xmlexport")) {
			logger.log(Level.INFO,"we are expecting an XML document");

			if (MimeType.equals("Document XML") || MimeType.equals("XML Document") || MimeType.toLowerCase().contains("xml")) {
				
				logger.log(Level.INFO,"Success: received document is an XML one - as expected!!!");
				// analyse the XML answer
				analyseXmlAnswer();

			}
			else {
				logger.log(Level.INFO,"location was expecting XML answer but did not get that kind of Mime Type....");
				// stop the browser before next retry
				//oleWebBrowser.Stop();
				sleepMilliseconds(1000);
				// next retry
				if (OleWebBrowserTab.this.numberOfRetry < maxNumberOfRetry) {
					//oleWebBrowser.ClearSessions();
					oleWebBrowser.Navigate(oleWebBrowser.getLocationURL());
					sleepMilliseconds(300);
					this.numberOfRetry++;
				}
				else {
					logger.log(Level.INFO,"End of retry process... stopping all....");
					String selectedTreeNodeName = OleWebBrowserTab.this.recursiveLiveLinkTreeComposite.getSelectedNodeTreeName();
					LiveLinkNode llNode = new LiveLinkNode(selectedTreeNodeName,oleWebBrowser.getLocationURL());
					this.recursiveLiveLinkTreeComposite.stopRecursiveBrowser(llNode);
				}
			}
		}
		else {
			/*
			 * location does not contain websso and location does not contain xmlexport
			 */
			if (MimeType.toLowerCase().contains("html") || MimeType.toLowerCase().contains("document") == true) {
				logger.log(Level.INFO,"Document is HTML one");
				
				this.rootNode.setBrowsed(true);
				
				//this.locationText.setText(this.rootNode.getXmlExportURL().toExternalForm());
				this.parent.getDisplay().syncExec(new Runnable() {
					// navigate again to the home location - in xml mode
					public void run() {
						// need to clear the tree before re starting the browser
						OleWebBrowserTab.this.recursiveLiveLinkTreeComposite.getLiveLinkNodeTree().removeAll();
						// launch browser with new location / new URL
						OleWebBrowserTab.this.oleWebBrowser.Navigate(rootNode.getXmlExportURL().toExternalForm());
					}
				});
			}
		}
	}

	/**
	 * analyse an XML answer obtained when browsing using xlmexport
	 */
	private void analyseXmlAnswer() {
		
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
				logger.log(Level.INFO,"XML parsing succeeded !");
				//logger.log(Level.INFO,"LiveLink application version: "+llXmlExport.getAppVersion());
				//logger.log(Level.INFO,"LiveLink action: "+llXmlExport.getSrc());
				final LiveLinkNode llNode = llXmlExport.getLiveLinkNode();
				if (llNode != null) {
					
					//logger.log(Level.INFO,strXML);
					llNode.setStrXML(strXML);
					llNode.setLiveLinkURL(locationURL);
					
					// this node is already browsed !!! => this triggers the observer(s) to proceed their update
					llNode.addObserver(this.navigationAreaComposite);
					llNode.addObserver(this.recursiveLiveLinkTreeComposite);
					llNode.addObserver(this.liveLinkNodeKTableComposite);
					// set browsed will notify the observers
					llNode.setBrowsed(true);
					
					// all is OK
					this.webBrowserInitialized = true;
					
				}
				else {
					LiveLinkVolume liveLinkVolume = llXmlExport.getLiveLinkVolume();
					if (liveLinkVolume != null) {
						logger.log(Level.INFO,"===========================================================================");
						logger.log(Level.INFO,"LiveLink export contains a llVolume: we should have reached the root of the Livelink tree!!!!!");
						logger.log(Level.INFO,"===========================================================================");
						new ShellInformationMessage(this.parent.getDisplay(),this.parent.getShell(), "you did reach the root of LiveLink");

						OleWebBrowserTab.this.parent.getDisplay().syncExec(new Runnable() {
							// navigate again to the home location - in browse mode
							public void run() {
								// need to clear the tree before re starting the browser
								recursiveLiveLinkTreeComposite.getLiveLinkNodeTree().removeAll();
								// launch browser with new location / new URL
								oleWebBrowser.GoBack();
							}
						});
					}
					else {
						logger.log(Level.SEVERE,"XML parser fails!!!");
						logger.log(Level.SEVERE,strXML);
						this.recursiveLiveLinkTreeComposite.stopRecursiveBrowser(llNode);

						new ShellErrorMessage(this.parent.getDisplay(),
								this.parent.getShell(),"XML Parser failed!!! on node "+locationURL+"...\r\n..."+strXML);
					}
				}
			} catch (RecognitionException e) {
				e.printStackTrace();

				logger.log(Level.SEVERE,"XML parser fails!!!");
				logger.log(Level.SEVERE,strXML);
				final LiveLinkNode llNode = new LiveLinkNode(strXML,locationURL);
				this.recursiveLiveLinkTreeComposite.stopRecursiveBrowser(llNode);

			} catch (TokenStreamException e) {
				e.printStackTrace();

				logger.log(Level.SEVERE,"XML parser fails!!!");
				final LiveLinkNode llNode = new LiveLinkNode(strXML,locationURL);
				this.recursiveLiveLinkTreeComposite.stopRecursiveBrowser(llNode);
			}
		}
	}
	
	private void TextSizeZoom() {
		/**
		 * check if zoom command is allowed		
		 * In Internet Explorer, you can increase or decrease the size of the text from the View menu, 
		 * but the WebBrowser control does not have a method to do this. 
		 * Instead, the control exposes this functionality through the IOleCommandTarget interface of the document. 
		 * Call IOleCommandTarget::Exec with OLECMDID_ZOOM and 
		 * pass a value in the range of 0 to 4 (where 0 is smallest) to indicate the desired scale of the font.
		 * 
		 *         COleVariant vaZoomFactor;   // input argument
		 *         V_VT(&vaZoomFactor) = VT_I4;
		 *         V_I4(&vaZoomFactor) = fontSize;   // 0 - 4

        // Change the text size.
        pCmdTarg->Exec(NULL,
                OLECMDID_ZOOM,
                OLECMDEXECOPT_DONTPROMPTUSER,
                &vaZoomFactor,
                NULL);

		 * 
		 * 
		 */
		int result = this.webControlSite.queryStatus(OLE.OLECMDID_ZOOM);
		logger.log(Level.INFO,"query status answer for Zoom enabled: "+result);
		if (((result & OLE.OLECMDF_ENABLED) == OLE.OLECMDF_ENABLED) || ((result & OLE.OLECMDF_SUPPORTED) == OLE.OLECMDF_SUPPORTED)) {
			logger.log(Level.INFO , "OleWebBrowserTab : Zoom is enabled or supported");
			Variant rgvarg = new Variant((int)1); // 1 size over the smallest size
			result = this.webControlSite.exec(OLE.OLECMDID_ZOOM, OLE.OLECMDEXECOPT_DONTPROMPTUSER , rgvarg, null);
			logger.log(Level.INFO,"answer for Zoom modification: "+result);
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

	private void buildStatusProgressBarRow() {

		Composite lastRowComposite = new Composite(this.contentPanel,SWT.FILL | SWT.BORDER);

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
		this.webStatus = new Label(lastRowComposite, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
		gridData.horizontalSpan = 2;
		// text grows horizontally with the frame
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		this.webStatus.setToolTipText("status message");
		this.webStatus.setLayoutData(gridData);
		Color greenColor = parent.getDisplay().getSystemColor(SWT.COLOR_GREEN);
		this.webStatus.setBackground(greenColor);

		// Add a progress bar to display downloading progress information
		webProgress = new ProgressBar(lastRowComposite, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		gridData.grabExcessHorizontalSpace = false;
		gridData.grabExcessVerticalSpace = false;
		this.webProgress.setLayoutData(gridData);		

	}

	private File getWebFile(String url, String localFilePath){
		try{

			InputStream in = new URL(url).openStream();
			File fileOut = new File(localFilePath);

			//For Overwrite the file.
			OutputStream out = new FileOutputStream(fileOut);

			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read(buffer)) > 0){
				out.write(buffer, 0, len);
			}
			in.close();
			out.flush();
			out.close();
			System.out.println("OleWebBrowserTab : File copied.");
			return fileOut;
		}
		catch(FileNotFoundException ex){
			System.out.println(ex.getMessage() + " in the specified directory.");
		}
		catch(IOException e){
			System.out.println(e.getMessage());			
		}
		return null;
	}

	public CTabFolder getTabFolder() {
		return this.tabFolder;
	}

	public void setTabFolder(CTabFolder tabFolder) {
		this.tabFolder = tabFolder;
	}

}
