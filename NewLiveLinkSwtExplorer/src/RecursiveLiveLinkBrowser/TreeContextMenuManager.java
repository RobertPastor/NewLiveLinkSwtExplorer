package RecursiveLiveLinkBrowser;


import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;



import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import LiveLinkCore.LiveLinkNode;
import LiveLinkCore.LiveLinkURLObservable;


/**
 * This class manages a set of contextual menus attached to a node tree
 * @author t0007330
 * @since April 2012
 *
 */
public class TreeContextMenuManager implements MouseListener {

	private static final Logger logger = Logger.getLogger(TreeContextMenuManager.class.getName());

	public enum TreeItemType {CONFIGURATION};

	private Tree tree = null;
	private Shell shell = null;

	private HashMap<TreeItem,Menu> contextMenues = null;
	private OleWebBrowser oleWebBrowser = null;

	public TreeContextMenuManager(Tree _tree, Shell _shell) {
		this.tree = _tree;
		this.shell = _shell;

	}

	public void assignContextMenu(TreeItem _item, Menu _contextMenu) {
		if (null == contextMenues) {
			contextMenues = new HashMap<TreeItem,Menu>();
		}
		contextMenues.put(_item, _contextMenu);
	}

	public Tree getTree() {
		return tree;
	}

	public void mouseDoubleClick(MouseEvent e) {
		
		logger.log(Level.INFO," double click !!!");
		
		TreeItem item = tree.getItem(new Point(e.x, e.y));
		if (null != item) {
			if (item.getData() instanceof LiveLinkNode) {
				LiveLinkNode llNode = (LiveLinkNode) item.getData();
				logger.log(Level.INFO,"current Node id is: "+llNode.getId());

				if (llNode.isDocument()) {
					final LiveLinkURLObservable liveLinkURL = new LiveLinkURLObservable(llNode.getId());
					this.shell.getDisplay().syncExec(new Runnable() {
						public void run() {
							// launch browser to open the document
							
								if (oleWebBrowser != null) {
									oleWebBrowser.Navigate(liveLinkURL.getOpenURL().toExternalForm());
								}
							
						}
					});	
				}
			}
		}
	}

	/**
	 * 1 is left
	 * 2 is middle
	 * 3 is right mouse button
	 */
	public void mouseDown(MouseEvent e) {

		if (1 == e.button) {
			//System.out.println("1");
		}
		if (2 == e.button) {
			//System.out.println("2");
		}
		if (3 == e.button) {
			logger.log(Level.INFO,"TreeContextMenuManager: MouseDown: Right click x: " + e.x + " y: " + e.y);

			TreeItem item = tree.getItem(new Point(e.x, e.y));
			if (null != item) {
				handleSelection(item);
			}
		}
	}

	private void handleSelection(TreeItem _item) {

		if ((null == contextMenues) || (0 == contextMenues.size())) {
			return;
		}
		// resolve registration, display pop-up menu
		if (contextMenues.get(_item) != null) {
			contextMenues.get(_item).setVisible(true);
		}
	}

	public void mouseUp(MouseEvent e) {
		//System.out.println("Mouseup");
	}


	public void setOleWebBrowser(OleWebBrowser oleWebBrowser) {
		this.oleWebBrowser = oleWebBrowser;
	}

}
