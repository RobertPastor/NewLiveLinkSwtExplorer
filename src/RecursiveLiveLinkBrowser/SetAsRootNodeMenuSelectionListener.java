package RecursiveLiveLinkBrowser;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import LiveLinkCore.LiveLinkNode;
import LiveLinkCore.LiveLinkURLObservable;

public class SetAsRootNodeMenuSelectionListener implements SelectionListener {
	
	private static final Logger logger = Logger.getLogger(SetAsRootNodeMenuSelectionListener.class.getName());
	
	private Composite parent = null;
	private Tree liveLinkNodeTree = null;
	private OleWebBrowser oleWebBrowser = null;
	
	public SetAsRootNodeMenuSelectionListener(Composite parent,
			Tree liveLinkNodeTree
			) {
		super();
		this.parent = parent;
		this.liveLinkNodeTree = liveLinkNodeTree;
		
	}
	
	public void setOleWebBrowser(OleWebBrowser oleWebBrowser) {
		this.oleWebBrowser = oleWebBrowser;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (liveLinkNodeTree.getSelectionCount() > 0) {
			TreeItem[] selectedItems = liveLinkNodeTree.getSelection();
			if (selectedItems != null) {
				TreeItem selectedItem = selectedItems[0];
				if (selectedItem != null) {
					
					logger.log(Level.INFO,"Selected Node: "+selectedItem.getText());
					
					if (selectedItem.getData() instanceof LiveLinkNode) {
						final LiveLinkNode llSelectedNode = (LiveLinkNode) selectedItem.getData();
						
						if (llSelectedNode.isLeaf() == false) {
							System.out.println("Selected Node is not a leaf: "+selectedItem.getText());
							
							final LiveLinkURLObservable liveLinkURL = new LiveLinkURLObservable(llSelectedNode.getId());
							
							this.parent.getDisplay().syncExec(new Runnable() {

								public void run() {
									// need to clear the tree before re starting the browser
									liveLinkNodeTree.clearAll(true);
									liveLinkNodeTree.removeAll();

									// launch browser with new location / new URL
									oleWebBrowser.Navigate(liveLinkURL.getXmlExportURL().toExternalForm());
								}
							});
						}
					}
				}
			}
		}
		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		logger.log(Level.INFO,"widgetDefaultSelected");
		
	}

}
