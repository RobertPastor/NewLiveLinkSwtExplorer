package DownLoadTab;

import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

import Antxr.LiveLinkParser;
import LiveLinkCore.LiveLinkNode;
import LiveLinkCore.LiveLinkURLObservable;
import LiveLinkCore.LiveLinkVolume;
import LiveLinkCore.ShellErrorMessage;
import LiveLinkCore.ShellInformationMessage;
import RecursiveLiveLinkBrowser.OleWebBrowser;

import com.javadude.antxr.RecognitionException;
import com.javadude.antxr.TokenStreamException;
import com.javadude.antxr.scanner.BasicKXml2XMLPullTokenStream;

import de.kupzog.ktable.KTable;



public class WebBrowserComposite {

	private static final Logger logger = Logger.getLogger(WebBrowserComposite.class.getName()); 

	private static final String CLSID_SHELLEXPLORER1 = "{EAB22AC3-30C1-11CF-A7EB-0000C05BAE0B}";

	private Composite parent = null;
	private OleFrame webFrame = null;
	private OleControlSite webControlSite = null;
	private OleAutomation oleAutomation = null;
	private OleWebBrowser oleWebBrowser = null;
	private DownLoadedFileSet downLoadedFileSet = null;

	private Composite lastRowComposite = null;
	private KTable hyperLinksTable = null;

	public OleWebBrowser getOleWebBrowser() {
		return oleWebBrowser;
	}

	private ProgressBar webProgress = null;
	private Label webStatus = null;

	private Button DownLoadLiveLinkDocumentsButton = null;

	public WebBrowserComposite(Composite parent,final Button DownLoadLiveLinkDocumentsButton) {
		this.parent = parent;
		this.DownLoadLiveLinkDocumentsButton = DownLoadLiveLinkDocumentsButton;

		lastRowComposite = new Composite(this.parent,SWT.HORIZONTAL | SWT.BORDER);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = false;
		lastRowComposite.setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		// composite grows horizontally
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		lastRowComposite.setLayoutData(gridData);

		// Add a label for displaying status messages as they are received from the control
		this.webStatus = new Label(lastRowComposite, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);

		gridData = new GridData();
		gridData.horizontalSpan = 1;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		// text grows horizontally with the frame
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		this.webStatus.setToolTipText("status message");
		this.webStatus.setLayoutData(gridData);

		Color greenColor = parent.getDisplay().getSystemColor(SWT.COLOR_GREEN);
		this.webStatus.setBackground(greenColor);

		// Add a progress bar to display downloading progress information
		this.webProgress = new ProgressBar(lastRowComposite, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = false;
		gridData.grabExcessVerticalSpace = false;
		this.webProgress.setLayoutData(gridData);		

	}

	public void init(KTable hyperLinksTable) {

		this.hyperLinksTable = hyperLinksTable;
		Composite webBrowserComposite = new Composite(this.lastRowComposite,SWT.BORDER);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = false;
		webBrowserComposite.setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		webBrowserComposite.setLayoutData(gridData);

		try {
			this.webFrame = new OleFrame(webBrowserComposite, SWT.NONE);
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
			//Variant download = new Variant(DLCTL_DOWNLOADONLY | DLCTL_SILENT | DLCTL_RESYNCHRONIZE);		
			//this.webControlSite.setSiteProperty(DLCTL_DOWNLOADONLY | DLCTL_SILENT | DLCTL_RESYNCHRONIZE, download);

			// Create an Automation object for access to extended capabilities
			this.oleAutomation = new OleAutomation(this.webControlSite);
			this.oleWebBrowser = new OleWebBrowser(this.oleAutomation);

			this.oleWebBrowser.setSilent(false);

			gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			gridLayout.makeColumnsEqualWidth = false;
			this.webFrame.setLayout(gridLayout);

			gridData = new GridData();
			gridData.horizontalSpan = 2;
			gridData.horizontalAlignment = SWT.FILL;
			gridData.verticalAlignment = SWT.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.grabExcessVerticalSpace = true;
			this.webFrame.setLayoutData(gridData);

			this.webControlSite.setLayoutData(gridData);

			this.webControlSite.addEventListener(OleWebBrowser.FileDownLoad, new OleListener() {
				public void handleEvent(OleEvent event) {
					logger.log(Level.INFO,"File DownLoad is beginning: "+event.toString());
					event.doit = true;
					// the browsed Livelink URL was a document because it is downloadable...
					System.out.println("==================");
				}
			});

			webControlSite.addEventListener(OleWebBrowser.DownloadBegin, new OleListener() {
				public void handleEvent(OleEvent event) {				
					logger.log(Level.INFO,"DownLoad is beginning: event type: "+event.type+" event detail: "+event.detail);
					event.doit = true;
				}
			});

			webControlSite.addEventListener(OleWebBrowser.DownloadComplete, new OleListener() {
				public void handleEvent(OleEvent event) {				
					logger.log(Level.INFO,"DownLoad is completed: event type: "+event.type+" event detail: "+event.detail);
					event.doit = true;

					// Analyze the answer provided by the browser
					//String LocationURL = oleWebBrowser.getLocationURL();
					String downloadURL = oleWebBrowser.getDownloadURL();
					try {
						URL url = new URL(downloadURL);
						LiveLinkURLObservable llURL = new LiveLinkURLObservable(url);
						logger.log(Level.INFO,downloadURL);

						if (downloadURL.toLowerCase().endsWith("download")) {
							WebBrowserComposite.this.downLoadedFileSet.setLiveLinkNodeIsDownloaded(llURL.getXmlExportURL().toExternalForm());

							// update the corresponding table view
							if (WebBrowserComposite.this.hyperLinksTable.getModel() instanceof HyperLinkTableModel) {

								HyperLinkTableModel hyperLinkTableModel = (HyperLinkTableModel) WebBrowserComposite.this.hyperLinksTable.getModel();
								hyperLinkTableModel.setHyperLinks(WebBrowserComposite.this.downLoadedFileSet);

								WebBrowserComposite.this.hyperLinksTable.setModel(hyperLinkTableModel);
								WebBrowserComposite.this.hyperLinksTable.setVisible(true);
							}
							// resume downloading
							downloadNextLiveLinkNode();
						}

					} catch (MalformedURLException e) {
						logger.log(Level.SEVERE,e.getMessage());
					}
				}
			});

			// Respond to ProgressChange events by updating the Progress bar
			webControlSite.addEventListener(OleWebBrowser.ProgressChange, new OleListener() {
				public void handleEvent(OleEvent event) {
					Variant progress = event.arguments[0];
					Variant maxProgress = event.arguments[1];
					if (progress == null || maxProgress == null)
						return;
					webProgress.setMaximum(maxProgress.getInt());
					webProgress.setSelection(progress.getInt());
				}
			});

			// Respond to StatusTextChange events by updating the Status Text label
			webControlSite.addEventListener(OleWebBrowser.StatusTextChange, new OleListener() {
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
			// WebControlSitePropertyListener propListener = new WebControlSitePropertyListener(this.oleWebBrowser);

			webControlSite.addPropertyListener(OleWebBrowser.DISPID_READYSTATE, new OleListener() {
				public void handleEvent(OleEvent event) {
					String LocationURL = "";

					if (event.detail == OLE.PROPERTY_CHANGING) return;
					int state = oleWebBrowser.getReadyState();
					switch (state) {
					case OleWebBrowser.READYSTATE_UNINITIALIZED:
						logger.log(Level.INFO,"browser.State.Uninitialized.text");
						webStatus.setText("browser.State.Uninitialized.text");

						break;
					case OleWebBrowser.READYSTATE_LOADING:
						logger.log(Level.INFO,"browser.State.Loading.text");
						webStatus.setText("browser.State.Loading.text");

						break;
					case OleWebBrowser.READYSTATE_LOADED:
						logger.log(Level.INFO,"browser.State.Loaded.text");
						webStatus.setText("browser.State.Loaded.text");

						break;
					case OleWebBrowser.READYSTATE_INTERACTIVE:
						logger.log(Level.INFO,"browser.State.Interactive.text");
						webStatus.setText("browser.State.Interactive.text");

						// this is the end of the download interactive dialog
						/*
						LocationURL = oleWebBrowser.getLocationURL();
						WebBrowserComposite.this.downLoadedFileSet.setLiveLinkNodeIsDownloaded(LocationURL);

						final int time = 5000;
						final Runnable timer = new Runnable () {
							public void run () {
								final URL url = WebBrowserComposite.this.downLoadedFileSet.getNextLiveLinkNodeToExplore();

								// launch browser with new location / new URL
								//WebBrowserComposite.this.oleWebBrowser = new OleWebBrowser(WebBrowserComposite.this.oleAutomation);
								WebBrowserComposite.this.oleWebBrowser.Navigate(url.toExternalForm());					
							}
						};
						WebBrowserComposite.this.parent.getDisplay().timerExec (time, timer);
						 */

						break;

					case OleWebBrowser.READYSTATE_COMPLETE:
						logger.log(Level.INFO,"browser.State.Complete.text");
						webStatus.setText("browser.State.Complete.text");
						String MimeType = oleWebBrowser.getMimeType();
						logger.log(Level.INFO,"Mime Type: "+MimeType);
						LocationURL = oleWebBrowser.getLocationURL();
						logger.log(Level.INFO,"location URL: "+LocationURL);

						// Analyze the answer provided by the browser
						LocationURL = oleWebBrowser.getLocationURL();
						WebBrowserComposite.this.downLoadedFileSet.setLiveLinkNodeIsExplored(LocationURL);

						// analyse HTTP answer
						analyseHttpAnswer();

						logger.log(Level.INFO,oleWebBrowser.getOuterXmlText());
						System.out.println("=========================");
						break;
					}
				}
			});

			// Listen for changes to the active command states
			webControlSite.addEventListener(OleWebBrowser.CommandStateChange, new OleListener() {
				public void handleEvent(OleEvent event) {
					if (event.type != OleWebBrowser.CommandStateChange) return;
					final int commandID =
							(event.arguments[0] != null) ? event.arguments[0].getInt() : 0;
							final boolean commandEnabled =
									(event.arguments[1] != null) ? event.arguments[1].getBoolean() : false;

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
			return;
		}

		// navigate : in place activate the ActiveX control		
		boolean activated = (webControlSite.doVerb(OLE.OLEIVERB_INPLACEACTIVATE) == OLE.S_OK);
		logger.log(Level.INFO,"Is the browser activated ? "+activated);
		if (activated) {
			logger.log(Level.INFO,"Success: !!!!! the browser is activated !!!!"+activated);

		}
	}

	private void analyseHttpAnswer() {

		String locationURL = oleWebBrowser.getLocationURL();
		String MimeType = oleWebBrowser.getMimeType();

		if (locationURL.toLowerCase().contains("xmlexport")) {
			logger.log(Level.INFO,"we were expecting an XML document");

			if (MimeType.equals("Document XML")) {
				logger.log(Level.INFO,"Success: received document is an XML one - as expected!!!");

				//String strXML = oleWebBrowser.getInnerXmlText();
				String strXML = oleWebBrowser.getOuterXmlText();
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
						logger.log(Level.INFO,"XML parsing succeeded!");
						logger.log(Level.INFO,"LiveLink application version: "+llXmlExport.getAppVersion());
						logger.log(Level.INFO,"LiveLink action: "+llXmlExport.getSrc());
						LiveLinkNode llNode = llXmlExport.getLiveLinkNode();
						if (llNode != null) {
							//logger.log(Level.INFO,strXML);
							llNode.setStrXML(strXML);
							llNode.setLiveLinkURL(locationURL);

							//llNode.dump();
							llNode.setBrowsed(true);
							this.downLoadedFileSet.update(llNode);

							// update the corresponding table view
							if (this.hyperLinksTable.getModel() instanceof HyperLinkTableModel) {

								HyperLinkTableModel hyperLinkTableModel = (HyperLinkTableModel) this.hyperLinksTable.getModel();
								hyperLinkTableModel.setHyperLinks(this.downLoadedFileSet);
								logger.log(Level.INFO,"update llNode: "+llNode.getName());
								this.hyperLinksTable.setModel(hyperLinkTableModel);
								this.hyperLinksTable.setVisible(true);
							}
							navigateNextLiveLinkNode();

						}
						else {
							LiveLinkVolume liveLinkVolume = llXmlExport.getLiveLinkVolume();
							if (liveLinkVolume != null) {
								logger.log(Level.INFO,"===========================================================================");
								logger.log(Level.INFO,"LiveLink export contains a llVolume: should be at the root of the tree!!!!!");
								logger.log(Level.INFO,"===========================================================================");

							}
							else {
								logger.log(Level.SEVERE,"XML parser fails!!!");
								logger.log(Level.SEVERE,strXML);

								new ShellErrorMessage(this.parent.getDisplay(),
										this.parent.getShell(),"XML Parser failed!!! on node "+locationURL+"...\r\n..."+strXML);

							}
						}
					} catch (RecognitionException e) {
						e.printStackTrace();

						logger.log(Level.SEVERE,"XML parser fails!!!");
						logger.log(Level.SEVERE,strXML);

					} catch (TokenStreamException e) {
						e.printStackTrace();
						logger.log(Level.SEVERE,"XML parser fails!!!");
					}
				}
			}
			else {
				// stop the browser before next retry
				navigateNextLiveLinkNode();

			}
		}	
		else {
			if (MimeType.contains("HTML Document") == true) {
				logger.log(Level.INFO,"Document is HTML one");
				// if the HTML page is the SSO login one then let the user insert its login and password
				if (locationURL.toLowerCase().contains("websso") == false) {
					logger.log(Level.INFO,"URL location does not contain websso");
					// answer was not an XML file 
					if (locationURL.toLowerCase().contains("xmlexport")) {
						logger.log(Level.INFO,"http answer was not a XML document as expected .....");
						// however we should have got an XML answer

					}
					else {
						// does not contain websso and does not expect an XML answer
						//new ShellInformationMessage(OleWebBrowserTab.this.parent.getDisplay(),
						//		OleWebBrowserTab.this.parent.getShell(),"Please click Go to start browsing");
					}
				}
				else {
					// location contains WebSso : need to wait until the user has provided his/her LOGIN and PASSWORD.
					new ShellInformationMessage(this.parent.getDisplay(),
							this.parent.getShell(),"Please provide Login / Password and then click Go to start browsing");
				}
			}

		}
	}

	private void downloadNextLiveLinkNode () {
		// launch next LiveLink Node to explore
		final int timeMilliSeconds = 5000;
		//final URL url = WebBrowserComposite.this.downLoadedFileSet.getNextLiveLinkNodeToDownload();
		final LiveLinkNode llNode = this.downLoadedFileSet.getNextLiveLinkNodeToDownload();
		if (llNode != null) {
			final Runnable timer = new Runnable () {
				public void run () {
					String fileName = llNode.getName();
					final String[] Headers = {"Content-Disposition", "attachment;filename=\"" + fileName + "\""};

					// launch browser with new location / new URL
					WebBrowserComposite.this.oleWebBrowser.Navigate(llNode.getDownloadURL().toExternalForm(),Headers);
				}
			};
			WebBrowserComposite.this.parent.getDisplay().timerExec (timeMilliSeconds, timer);
		}
		else {
			// no more LL hyper links to explore
			new ShellInformationMessage(this.parent.getDisplay(),
					this.parent.getShell(),"======End of LiveLink download======");
			// allow here for downloading the explored files
			this.DownLoadLiveLinkDocumentsButton.setEnabled(true);
		}
	}

	private void navigateNextLiveLinkNode () {
		// launch next LiveLink Node to explore
		final int timeMilliSeconds = 1000;
		final URL url = WebBrowserComposite.this.downLoadedFileSet.getNextLiveLinkNodeToExplore();
		if (url != null) {
			final Runnable timer = new Runnable () {
				public void run () {
					// launch browser with new location / new URL
					WebBrowserComposite.this.oleWebBrowser.Navigate(url.toExternalForm());
				}
			};
			WebBrowserComposite.this.parent.getDisplay().timerExec (timeMilliSeconds, timer);
		}
		else {
			// no more LL hyper links to explore
			new ShellInformationMessage(this.parent.getDisplay(),
					this.parent.getShell(),"======End of LiveLink explore======");
			// allow here for downloading the explored files
			this.DownLoadLiveLinkDocumentsButton.setEnabled(true);
		}
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

	public void setDownLoadedFileSet(DownLoadedFileSet downLoadedFileSet) {
		this.downLoadedFileSet = downLoadedFileSet;
	}
}
