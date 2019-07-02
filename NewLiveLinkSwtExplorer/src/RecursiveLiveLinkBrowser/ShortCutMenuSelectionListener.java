package RecursiveLiveLinkBrowser;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import LiveLinkCore.LiveLinkNode;

public class ShortCutMenuSelectionListener implements SelectionListener {

	private static final Logger logger = Logger.getLogger(ShortCutMenuSelectionListener.class.getName());

	private Composite parent = null;

	private Tree liveLinkNodeTree = null;
	private OleWebBrowser oleWebBrowser = null;
	
	public ShortCutMenuSelectionListener (Composite parent, Tree liveLinkNodeTree) {
		super();
		this.parent = parent;
		this.liveLinkNodeTree = liveLinkNodeTree;
	}
	
	public void setOleWebBrowser(OleWebBrowser oleWebBrowser) {
		this.oleWebBrowser = oleWebBrowser;
	}
	
	public void widgetSelected(SelectionEvent e) {
		logger.log(Level.INFO,"Selected Short Cut Menu: "+e.toString());
		if (liveLinkNodeTree.getSelectionCount() > 0) {
			TreeItem selectedItem = liveLinkNodeTree.getSelection()[0];
			if (selectedItem.getData() instanceof LiveLinkNode) {
				LiveLinkNode llNode = (LiveLinkNode) selectedItem.getData();
				if (llNode.isShortCut()) {
					String strShortCutURL = llNode.getShortCutNodeURL();
					logger.log(Level.INFO,strShortCutURL);
					ShortCutMenuSelectionListener.this.oleWebBrowser.Navigate(strShortCutURL);

				}
			}
		}
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		logger.log(Level.INFO,"widgetDefaultSelected: "+e.text);
	}

	public Composite getParent() {
		return parent;
	}

	public void setParent(Composite parent) {
		this.parent = parent;
	}
}
