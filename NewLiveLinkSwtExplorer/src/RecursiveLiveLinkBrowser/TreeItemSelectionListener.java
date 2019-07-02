package RecursiveLiveLinkBrowser;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

import LiveLinkCore.LiveLinkNode;
import LiveLinkCore.LiveLinkURLObservable;

/**
 * This class manages the selection of one tree item.
 * @since July 2012
 * @author t0007330
 *
 */
public class TreeItemSelectionListener implements Listener {
	
	private static final Logger logger = Logger.getLogger(TreeItemSelectionListener.class.getName()); 

	private Composite parent = null;
		
	private OleWebBrowserTab oleWebBrowserTab = null;
	private RecursiveLiveLinkTreeCompositeObserver recursiveLiveLinkTreeComposite = null;
	
	public TreeItemSelectionListener (
			Composite _parent, 
			OleWebBrowserTab _oleWebBrowserTab, 
			RecursiveLiveLinkTreeCompositeObserver _recursiveLiveLinkTreeComposite) {
		
		this.parent = _parent;
		this.oleWebBrowserTab = _oleWebBrowserTab;
		this.recursiveLiveLinkTreeComposite  = _recursiveLiveLinkTreeComposite;
	}
	
	
	public void handleEvent(Event e) {
		logger.log(Level.INFO,"=================Tree Node Selection==================================");
		logger.log(Level.INFO,"node selection: "+e.item.toString());

		TreeItem item = (TreeItem) e.item;
		if (item.getItemCount() == 0) {
			if (item.getData() instanceof LiveLinkNode) {
				LiveLinkNode llNode = (LiveLinkNode)item.getData();
				// check if node is a document => not browsable or it is a folder => it is possible to open it to see its content
				if (llNode.isDocument() == false ) {

					logger.log(Level.INFO,"current Node id is: "+llNode.getId());
					final LiveLinkURLObservable liveLinkURL = new LiveLinkURLObservable(llNode.getId());
					
					this.recursiveLiveLinkTreeComposite.goURL(liveLinkURL.getXmlExportURL().toExternalForm(), false);
										
				}
				else {
					// it is a document ... may be possible to download it and analyze it as if it was a Q-PCP file
					logger.log(Level.INFO, "Selected Item= " + item.getText());
					String selectedFileName = item.getText();
					
					// document can be viewed by the browser (Internet explorer)
					// please use a double click to open it
					/**
					System.out.println("LiveLinkNodeTree: current Node id is: "+llNode.getNodeId());
					final LiveLinkURL liveLinkURL = new LiveLinkURL(llNode.getNodeId());
					LiveLinkTreeComposite.this.parent.getDisplay().syncExec(new Runnable() {
						public void run() {
							// launch browser to open the document
							LiveLinkTreeComposite.this.webBrowserComposite.getBrowser().setUrl(liveLinkURL.getOpenURL().toExternalForm());
						}
					});	
					 */
				}
			}
		}
		else {
			logger.log(Level.INFO,"selected node has already sub nodes!!!");
			if (item.getData() instanceof LiveLinkNode) {
				LiveLinkNode llNode = (LiveLinkNode)item.getData();
				llNode.addObserver(TreeItemSelectionListener.this.oleWebBrowserTab.getNavigationAreaComposite());
				llNode.setBrowsed(true);
				
			}
		}
	}

}
