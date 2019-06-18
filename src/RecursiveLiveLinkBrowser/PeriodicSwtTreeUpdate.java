package RecursiveLiveLinkBrowser;


import org.eclipse.swt.widgets.Composite;

import LiveLinkCore.LiveLinkNode;

public class PeriodicSwtTreeUpdate {

	LiveLinkNode llNode = null;
	private OleWebBrowser oleWebBrowser = null;
	private Composite parent = null;

	public PeriodicSwtTreeUpdate(int seconds, final LiveLinkNode _llNode, Composite _parent, OleWebBrowser _oleWebBrowser) {
		
		this.llNode = _llNode;
		this.parent = _parent;
		this.oleWebBrowser = _oleWebBrowser;
		
		System.out.println( "PeriodicSwtTreeUpdate - browse to node =  " + llNode.getName());

		System.out.println( "PeriodicSwtTreeUpdate - number of seconds = " +seconds );
		
		this.parent.getDisplay().timerExec(seconds * 1000, new Runnable() {
			public void run() {
				System.out.println(" PeriodicSwtTreeUpdate - it is time to run ");
				// bug in IE Explorer - stop before navigating to an XML document
				//PeriodicSwtTreeUpdate.this.oleWebBrowser.Stop();
				// launch browser with new location / new URL
				
				System.out.println("navigate to URL = " + PeriodicSwtTreeUpdate.this.llNode.getXmlExportURL().toExternalForm());
				PeriodicSwtTreeUpdate.this.oleWebBrowser.Navigate(PeriodicSwtTreeUpdate.this.llNode.getXmlExportURL().toExternalForm());
			}
		});
	}

}
