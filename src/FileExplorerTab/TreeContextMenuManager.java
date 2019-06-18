package FileExplorerTab;


import java.util.HashMap;
import java.util.logging.Logger;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;



/**
 * This class manages a set of contextual menus attached to a node tree.
 * @author Robert PASTOR
 * @since April 2012
 *
 */
public class TreeContextMenuManager implements MouseListener {
	
	private static final Logger logger = Logger.getLogger(TreeContextMenuManager.class.getName()); 


	public enum TreeItemType {CONFIGURATION};

	private Tree tree = null;
	private Shell shell = null;

	public Shell getShell() {
		return shell;
	}

	private HashMap<TreeItem, Menu> contextMenues = null;

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
		logger.info("Tree Context Menu Manager : double click !!!");
		TreeItem item = tree.getItem(new Point(e.x, e.y));
		if (null != item) {
			logger.info(item.getText());
		}
	}

	/**
	 * 1 is left
	 * 2 is middle
	 * 3 is right mouse button
	 */
	public void mouseDown(MouseEvent e) {

		if (1 == e.button) {
			logger.info( "mouse button 1");
		}
		if (2 == e.button) {
			logger.info( "mouse button 2");
		}
		if (3 == e.button) {
			logger.info( "TreeContextMenuManager: MouseDown: Right click x: " + e.x + " y: " + e.y);

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
		logger.info( "TreeContextMenuManager Mouse-up");
	}

}
