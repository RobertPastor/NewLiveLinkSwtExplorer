package RecursiveLiveLinkBrowser;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import LiveLinkCore.LiveLinkNode;
import LiveLinkCore.LiveLinkRedirectURL;

public class LiveLinkURLMenuSelectionListener implements SelectionListener {

	private static final Logger logger = Logger.getLogger(LiveLinkURLMenuSelectionListener.class.getName());

	private Composite parent = null;
	public Composite getParent() {
		return parent;
	}

	private Tree liveLinkNodeTree = null;
	private OleWebBrowser oleWebBrowser = null;
	
	public LiveLinkURLMenuSelectionListener(Composite parent, Tree liveLinkNodeTree ) {
		super();
		this.parent = parent;
		this.liveLinkNodeTree = liveLinkNodeTree;
		
	}
	
	public void setOleWebBrowser(OleWebBrowser oleWebBrowser) {
		this.oleWebBrowser = oleWebBrowser;
	}

	public void widgetSelected(SelectionEvent e) {
		logger.log(Level.INFO,"Selected URL Menu: "+e.toString());
		if (liveLinkNodeTree.getSelectionCount() > 0) {
			TreeItem selectedItem = liveLinkNodeTree.getSelection()[0];
			if (selectedItem.getData() instanceof LiveLinkNode) {
				LiveLinkNode llNode = (LiveLinkNode) selectedItem.getData();
				if (llNode.isURL()) {
					LiveLinkRedirectURL llRedirectURL = llNode.getLiveLinkRedirectURL();
					LiveLinkURLMenuSelectionListener.this.oleWebBrowser.Navigate(llRedirectURL.getURI());
				}
			}
		}
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		logger.log(Level.INFO,"widgetDefaultSelected: "+e.toString());

	}
	
	
}
