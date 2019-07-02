package RecursiveLiveLinkBrowser;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.TCHAR;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import LiveLinkCore.LiveLinkNode;
import LiveLinkCore.LiveLinkURLObservable;

public class DownLoadMenuSelectionListener implements SelectionListener {
	/**
	 * manage a file download menu and associated command
	 */

	private static final Logger logger = Logger.getLogger(DownLoadMenuSelectionListener.class.getName());

	private Composite parent = null;
	private Tree liveLinkNodeTree = null;
	private OleWebBrowser oleWebBrowser = null;

	public DownLoadMenuSelectionListener(Composite parent, Tree liveLinkNodeTree ) {
		super();
		this.parent = parent;
		this.liveLinkNodeTree = liveLinkNodeTree;

	}

	public void setOleWebBrowser(OleWebBrowser oleWebBrowser) {
		this.oleWebBrowser = oleWebBrowser;
	}

	public void widgetSelected(SelectionEvent e) {

		logger.log(Level.INFO,"Selected DownLoad Menu: "+e.toString());
		if (liveLinkNodeTree.getSelectionCount() > 0) {
			// get first selected node of the tree
			final TreeItem selectedItem = liveLinkNodeTree.getSelection()[0];
			if (selectedItem.getData() instanceof LiveLinkNode) {
				LiveLinkNode llNode = (LiveLinkNode) selectedItem.getData();
				if (llNode.isDocument()) {
					logger.log(Level.INFO,"Context Menu: Selected item: is a Document: "+selectedItem.getText());

					logger.log(Level.INFO,"current id is: "+llNode.getId());
					final LiveLinkURLObservable liveLinkURL = new LiveLinkURLObservable(llNode.getId());
					logger.log(Level.INFO,liveLinkURL.getDownloadURL().toExternalForm());

					logger.log(Level.INFO, "=============download directory===========");

					String strDownloadDirectory =  getDownloadDirectory();
					if (strDownloadDirectory.length()>0) {
						logger.log(Level.INFO, strDownloadDirectory);
						// set ole webbrowser in silent mode => should not show the 
						this.oleWebBrowser.setSilent(true);
					}

					logger.log(Level.INFO, "=============download directory===========");
					
					this.parent.getDisplay().asyncExec(new Runnable() {
						public void run() {
							
							logger.log(Level.INFO, "============= launch download ===========");

							// launch browser with new location / new URL
							DownLoadMenuSelectionListener.this.oleWebBrowser.Navigate(liveLinkURL.getDownloadURL().toExternalForm());

						}
					});	
					
				}
			}
		}
	}

	private String getDownloadDirectory() {

		String downloadDirectory = "";
		TCHAR key = new TCHAR (0, "Software\\Microsoft\\Internet Explorer", true);	//$NON-NLS-1$
		long /*long*/ [] phkResult = new long /*long*/ [1];
		if (OS.RegOpenKeyEx (OS.HKEY_CURRENT_USER, key, 0, OS.KEY_READ, phkResult) == 0) {
			int [] lpcbData = new int [1];
			TCHAR buffer = new TCHAR (0, "Download Directory", true); //$NON-NLS-1$
			int result = OS.RegQueryValueEx (phkResult [0], buffer, 0, null, (TCHAR) null, lpcbData);
			if (result == 0) {
				TCHAR lpData = new TCHAR (0, lpcbData [0] / TCHAR.sizeof);
				result = OS.RegQueryValueEx (phkResult [0], buffer, 0, null, lpData, lpcbData);
				if (result == 0) {
					downloadDirectory = lpData.toString (0, lpData.strlen ());
					//System.out.println("download directory is= " + downloadDirectory);
				}
			}
			OS.RegCloseKey (phkResult [0]);
		}
		return downloadDirectory;
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		logger.log(Level.INFO,"widgetDefaultSelected");
	}

}
