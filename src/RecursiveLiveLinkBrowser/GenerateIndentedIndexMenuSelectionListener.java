package RecursiveLiveLinkBrowser;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import LiveLinkCore.LiveLinkNode;

public class GenerateIndentedIndexMenuSelectionListener implements SelectionListener {
	
	private static final Logger logger = Logger.getLogger(GenerateIndentedIndexMenuSelectionListener.class.getName());
	
	private Composite parent = null;
	private CTabFolder cTabFolder = null;
	private Tree liveLinkNodeTree = null;
	private RecursiveLiveLinkTreeCompositeObserver parentClass = null;
	
	public GenerateIndentedIndexMenuSelectionListener(Composite parent,
			CTabFolder _cTabFolder,
			Tree liveLinkNodeTree,
			RecursiveLiveLinkTreeCompositeObserver parentClass
			) {
		super();
		this.parent = parent;
		this.cTabFolder = _cTabFolder;
		this.liveLinkNodeTree = liveLinkNodeTree;
		this.parentClass = parentClass;
		
	}
	
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		logger.log(Level.INFO,"generate Indented Index");
		
		this.parentClass.setGeneratedIndentedIndex(true);
		
		if (liveLinkNodeTree.getSelectionCount() > 0) {
			TreeItem[] selectedItems = liveLinkNodeTree.getSelection();
			if (selectedItems != null) {
				TreeItem selectedItem = selectedItems[0];
				if (selectedItem != null) {
					System.out.println("LiveLinkTreeComposite: recursive export from Selected Node: "+selectedItem.getText());
					if (selectedItem.getData() instanceof LiveLinkNode) {
						final LiveLinkNode llSelectedNode = (LiveLinkNode) selectedItem.getData();
						if (llSelectedNode.isLeaf() == false) {
							System.out.println("LiveLinkTreeComposite: Selected Node is not a leaf: "+selectedItem.getText());
							
							new RecursiveNodeOleWebBrowser(GenerateIndentedIndexMenuSelectionListener.this.parent.getShell(), 
									GenerateIndentedIndexMenuSelectionListener.this.cTabFolder, llSelectedNode, true);

						}
					}
				}
			}
		}
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		logger.log(Level.INFO,"widgetDefaultSelected");

	}

}
