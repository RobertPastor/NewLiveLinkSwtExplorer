package RecursiveLiveLinkBrowser;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Manage a refresh node listener -> case when Livelink content is updated and User wants to refresh the content starting from the node
 * @since June 2019
 * @author T0007330
 *
 */
public class RefreshNodeSelectionListener implements SelectionListener {
	
	private static final Logger logger = Logger.getLogger(RefreshNodeSelectionListener.class.getName());


	private Composite parent = null;
	private CTabFolder cTabFolder = null;

	private Tree liveLinkNodeTree = null;
	private OleWebBrowser oleWebBrowser = null;
	
	
	public RefreshNodeSelectionListener(Composite parent, CTabFolder _cTabFolder,
			Tree liveLinkNodeTree) {
		
		super();
		this.parent = parent;
		this.cTabFolder = _cTabFolder;
		this.liveLinkNodeTree = liveLinkNodeTree;
	}
	
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		
		logger.log(Level.INFO,"Refresh Node Selection Listener");
		
		if (liveLinkNodeTree.getSelectionCount() > 0) {
			TreeItem[] selectedItems = liveLinkNodeTree.getSelection();
			if (selectedItems != null) {
				TreeItem selectedItem = selectedItems[0];
				if (selectedItem != null) {
					
					logger.log(Level.INFO,"Refresh Node from Selected Node: "+selectedItem.getText());
					
				}
			}
		}
	}



	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		logger.log(Level.INFO,"widgetDefaultSelected");

	}
}
