package RecursiveLiveLinkBrowser;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import JExcelApi.WritableExcelFile;
import JExcelApi.WritableExcelSheet.Excel2003MaxRowException;
import LiveLinkCore.LiveLinkNode;
import LiveLinkCore.LiveLinkObjectImageFactory;
import LiveLinkCore.LiveLinkURLObservable;
import LiveLinkCore.ShellErrorMessage;
import LiveLinkCore.ShellInformationMessage;
import MimeType.LiveLinkMimeTypeSet;

public class RecursiveLiveLinkTreeCompositeObserver extends Composite implements Observer {

	private static final Logger logger = Logger.getLogger(RecursiveLiveLinkTreeCompositeObserver.class.getName()); 

	private Composite parent = null;
	private CTabFolder cTabFolder = null;
	private Tree liveLinkNodeTree = null;
	private LiveLinkMimeTypeSet llMimeTypeSet = null;

	private TreeContextMenuManager treeContextMenuManager = null;
	private Menu shortCutMenu = null;
	private Menu downLoadMenu = null;
	private Menu UrlMenu = null;
	private Menu generateIndexMenu = null;

	private DownLoadMenuSelectionListener downLoadMenuSelectionListener = null;
	private ShortCutMenuSelectionListener shortCutMenuSelectionListener = null;
	private LiveLinkURLMenuSelectionListener liveLinkURLMenuSelectionListener = null;
	private GenerateIndentedIndexMenuSelectionListener generateIndentedIndexMenuSelectionListener = null;
	private GenerateStandardIndexMenuSelectionListener generateStandardIndexMenuSelectionListener = null;
	private SetAsRootNodeMenuSelectionListener setAsRootNodeMenuSelectionListener = null;
	// 5th May 2019 - Refresh node
	private RefreshNodeSelectionListener refreshNodeSelectionListener = null;

	private OleWebBrowser oleWebBrowser = null;
	// root node used when starting a recursive browsing

	private boolean recursiveBrowserStarted = false;
	private boolean generateIndentedIndex = false;

	private TreeItemSelectionListener treeItemSelectionListener = null;

	private LiveLinkNode rootNode = null;

	private LiveLinkObjectImageFactory llObjectImageFactory = null;

	private OleWebBrowserTab oleWebBrowserTab = null;

	public RecursiveLiveLinkTreeCompositeObserver(Composite _parent,
			CTabFolder _cTabFolder,
			LiveLinkMimeTypeSet llMimeTypeSet,
			LiveLinkObjectImageFactory llObjectImageFactory, 
			OleWebBrowserTab _oleWebBrowserTab,
			LiveLinkNode _rootNode) {

		// intialize a Composite
		super(_parent, SWT.FILL | SWT.BORDER );
		this.cTabFolder = _cTabFolder;

		this.parent = _parent;
		this.llMimeTypeSet = llMimeTypeSet;
		this.llObjectImageFactory = llObjectImageFactory;
		this.oleWebBrowserTab = _oleWebBrowserTab;
		this.rootNode = _rootNode;

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		this.setLayout(gridLayout);

		// single selection tree
		this.liveLinkNodeTree = new Tree(this, SWT.FILL | SWT.BORDER | SWT.SINGLE );
		this.liveLinkNodeTree.setToolTipText("click on a node to get its description !!!");

		GridData gridData = new GridData(SWT.FILL);
		gridData.verticalAlignment = SWT.FILL;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		this.liveLinkNodeTree.setLayoutData(gridData);

		this.treeContextMenuManager = new TreeContextMenuManager(
				this.liveLinkNodeTree,
				this.parent.getShell());
		this.liveLinkNodeTree.addMouseListener(treeContextMenuManager);

		// create the contextual menu items that will be available on each kind of LiveLink Tree node
		createTreeMenuItems();

		this.treeItemSelectionListener = new TreeItemSelectionListener(this.parent, this.oleWebBrowserTab, this);
		this.liveLinkNodeTree.addListener(SWT.Selection, treeItemSelectionListener);

	}


	private void createTreeMenuItems() {
		//===========================
		// create the URL Menu
		this.UrlMenu = new Menu(this.parent.getShell());
		MenuItem menuItemURL = new MenuItem(this.UrlMenu,SWT.PUSH);
		menuItemURL.setText("Follow Livelink URL");
		this.liveLinkURLMenuSelectionListener = new LiveLinkURLMenuSelectionListener(
				this.parent, 
				this.liveLinkNodeTree);
		menuItemURL.addSelectionListener(liveLinkURLMenuSelectionListener);

		//===================================
		// create the  Short Cut pop-up Menu
		this.shortCutMenu = new Menu(this.parent.getShell());
		MenuItem menuItemShortCut = new MenuItem(this.shortCutMenu, SWT.PUSH);
		menuItemShortCut.setText("Follow Livelink Short Cut");
		// register selection listener to menu item in pop-up menu
		this.shortCutMenuSelectionListener = new ShortCutMenuSelectionListener(
				this.parent, 
				this.liveLinkNodeTree);		
		menuItemShortCut.addSelectionListener(this.shortCutMenuSelectionListener);

		//=============================
		//create the download menu
		this.downLoadMenu = new Menu(this.parent.getShell());
		//=============================

		MenuItem menuItemDownLoad = new MenuItem(this.downLoadMenu, SWT.PUSH);
		menuItemDownLoad.setText("Download");
		// register selection listener to menu item in pop-up menu		
		this.downLoadMenuSelectionListener = new DownLoadMenuSelectionListener(
				this.parent,
				this.liveLinkNodeTree);
		menuItemDownLoad.addSelectionListener(this.downLoadMenuSelectionListener);

		//====================================
		// create menu to generate the index
		// either indented index or 
		// standard index
		this.generateIndexMenu = new Menu(this.parent.getShell());

		// menu entry to set as new root node
		MenuItem menuItemSetAsRootNode = new MenuItem(this.generateIndexMenu,SWT.PUSH);
		menuItemSetAsRootNode.setText("Set as root node");
		// register selection listener to menu item in pop-up menu	
		this.setAsRootNodeMenuSelectionListener = new SetAsRootNodeMenuSelectionListener(
				this.parent,
				this.liveLinkNodeTree);
		menuItemSetAsRootNode.addSelectionListener(this.setAsRootNodeMenuSelectionListener);

		//================
		// menu item generate indented index
		MenuItem menuItemGenerateIndentedIndex = new MenuItem(this.generateIndexMenu, SWT.PUSH);
		menuItemGenerateIndentedIndex.setText("Generate Indented Index");
		// register selection listener to menu item in pop-up menu	
		this.generateIndentedIndexMenuSelectionListener = new GenerateIndentedIndexMenuSelectionListener(
				this.parent,
				this.cTabFolder,
				this.liveLinkNodeTree,
				this);
		menuItemGenerateIndentedIndex.addSelectionListener(this.generateIndentedIndexMenuSelectionListener);

		//====================================
		// create menu to generate the index
		MenuItem menuItemGenerateNormalIndex = new MenuItem(this.generateIndexMenu, SWT.PUSH);
		menuItemGenerateNormalIndex.setText("Generate Standard Index");
		
		// register selection listener to menu item in pop-up menu	
		this.generateStandardIndexMenuSelectionListener = new GenerateStandardIndexMenuSelectionListener(
				this.parent,
				this.cTabFolder,
				this.liveLinkNodeTree, 
				this);
		menuItemGenerateNormalIndex.addSelectionListener(this.generateStandardIndexMenuSelectionListener);
		
		//============================
		// create the menu to refresh the current node
		MenuItem menuItemRefreshSelectedNpde  = new MenuItem(this.generateIndexMenu, SWT.PUSH);
		menuItemRefreshSelectedNpde.setText("Refresh Node and Children");
		
		// register selection listener to menu item in pop-up menu	
		this.refreshNodeSelectionListener = new RefreshNodeSelectionListener(
				this.parent,
				this.cTabFolder,
				this.liveLinkNodeTree);
		menuItemGenerateNormalIndex.addSelectionListener(this.refreshNodeSelectionListener);
		

	}

	public void setOleWebBrowser(OleWebBrowser oleWebBrowser) {
		logger.log(Level.INFO,"set Ole Web Browser");

		this.oleWebBrowser = oleWebBrowser ;
		this.treeContextMenuManager.setOleWebBrowser(this.oleWebBrowser);
		this.downLoadMenuSelectionListener.setOleWebBrowser(this.oleWebBrowser);
		this.shortCutMenuSelectionListener.setOleWebBrowser(this.oleWebBrowser);
		this.liveLinkURLMenuSelectionListener.setOleWebBrowser(this.oleWebBrowser);
		this.setAsRootNodeMenuSelectionListener.setOleWebBrowser(this.oleWebBrowser);
	}

	public Tree getLiveLinkNodeTree() {
		return this.liveLinkNodeTree;
	}

	private void navigate(LiveLinkNode llNode) {

		final LiveLinkURLObservable liveLinkURL = new LiveLinkURLObservable(llNode.getId());

		Runnable longJob = new Runnable() {
			public void run() {
				// bug in IE Explorer - stop before navigating to an XML document
				//RecursiveLiveLinkTreeCompositeObserver.this.oleWebBrowser.Stop();
				// launch browser with new location / new URL
				RecursiveLiveLinkTreeCompositeObserver.this.oleWebBrowser.Navigate(liveLinkURL.getXmlExportURL().toExternalForm());
			}
		};
		
		this.parent.getDisplay().asyncExec( longJob );

	}
	/** 
	 * build or complete an existing SWT Tree.
	 * @param llNode
	 */
	private synchronized void buildTree(LiveLinkNode llNode) {

		logger.log(Level.INFO,"beginning Build SWT Tree");
		if (llNode == null) {
			TreeItem[] selectedItems = this.liveLinkNodeTree.getSelection();
			if (selectedItems != null) {
				if (selectedItems.length > 0) {
					logger.log(Level.INFO,"Selected Item: "+selectedItems[0].getText());
					TreeItem selectedItem = selectedItems[0];
					if (selectedItem != null) {
						new ShellErrorMessage(this.parent.getDisplay(),
								this.parent.getShell(),"Error while parsing XML export: "+selectedItem.getText());
						if (recursiveBrowserStarted == true) {
							recursiveBrowserStarted = false;
						}
					}
				}
			}
			return;
		}

		logger.log(Level.INFO, "build LiveLink Tree: "+ llNode.getName() + " childNodes: "+ llNode.getChildren().size());

		if (this.liveLinkNodeTree.getItemCount() == 0) {
			logger.log(Level.INFO,"Tree is empty!!!");
			// tree is empty
			TreeItem initialItem = null;
			initialItem = new TreeItem(this.liveLinkNodeTree, SWT.NONE);
			initialItem.setText(llNode.getName());
			initialItem.setData(llNode);
			setImageAddContextualMenus(initialItem,llNode);

			Iterator<LiveLinkNode> iter = llNode.getChildren().iterator();
			while (iter.hasNext()) {

				LiveLinkNode llSubNode = iter.next();
				TreeItem item = new TreeItem(initialItem,SWT.NONE);
				item.setText(llSubNode.getName());
				logger.log(Level.INFO, llSubNode.getName());
				item.setData(llSubNode);
				setImageAddContextualMenus(item,llSubNode);

			}
			// expand the initial item
			initialItem.setExpanded(true);
		}
		else {
			logger.log(Level.INFO,"Tree is not empty!!!");
			// tree is not empty
			TreeItem[] selectedItems = this.liveLinkNodeTree.getSelection();
			// An empty array indicates that no items are selected
			if ( (selectedItems != null) &&  (selectedItems.length > 0) ) {

				// in this part we extend the TREE but we have a selected TREE ITEM
				logger.log(Level.INFO, "Selected Item: " + selectedItems[0].getText());
				TreeItem selectedItem = selectedItems[0];
				if (selectedItem != null) {
					if (selectedItem.getItemCount() == 0) {
						// selected item has no children nodes

						// if node was a short cut it may change from now...
						if (selectedItem.getData() instanceof LiveLinkNode) {
							LiveLinkNode llNodeBefore = (LiveLinkNode)selectedItem.getData();
							if (llNodeBefore.isShortCut()) {
								// add image and contextual menus
								setImageAddContextualMenus(selectedItem,llNode);
							}
						}
						if (llNode.getName().length() > 0) {
							selectedItem.setText(llNode.getName());
						} else {
							selectedItem.setText(selectedItem.getText() + "...parser fails...");
						}
						// set the data => data is a llNode
						selectedItem.setData(llNode);

						// add the childrens
						Iterator<LiveLinkNode> iter = llNode.getChildren().iterator();
						while (iter.hasNext()) {

							LiveLinkNode llSubNode = iter.next();
							TreeItem item = new TreeItem(selectedItem,SWT.NONE);
							item.setText(llSubNode.getName());
							item.setData(llSubNode);
							setImageAddContextualMenus(item,llSubNode);

						}
					} else {
						// selected item has already children
						logger.log(Level.INFO, "Selected Item: " + selectedItems[0].getText() + " has already children -> delete and rebuild");
						
					}
					// expand the initial item
					if (recursiveBrowserStarted == false) {
						selectedItem.setExpanded(true);
					}
				}
			} else {
				logger.log(Level.INFO, " There are no selected items !!! => automatic and recursiv browse !!!");
				logger.log(Level.INFO, llNode.getName() + " - " + llNode.getId());
				//findAndExtendTreeItem(llNode);
			}
		}
		// resume recursive crawling 
		if (recursiveBrowserStarted == true) {
			logger.log(Level.INFO,"Recursive Browser resuming !!!");

			// search recursively through the tree...
			this.liveLinkNodeTree.deselectAll();
			TreeItem[] treeItems = this.liveLinkNodeTree.getItems();

			traverseTree(treeItems);
			NavigateToSelectedTreeNode();

		} 
		/*		else {
			// launch periodic update => in 3 seconds
			LiveLinkNode toBeBrowsedNode = getUnBrowsedLiveLinkNode();
			if (toBeBrowsedNode != null) {
				new PeriodicSwtTreeUpdate(3 , toBeBrowsedNode, this.parent, this.oleWebBrowser);
			}

		}*/
	}

	/*private LiveLinkNode getUnBrowsedLiveLinkNode() {

		LiveLinkNode foundLLNode = null;
		TreeItem[] treeItems = this.liveLinkNodeTree.getItems();
		for (TreeItem treeItem : treeItems ) {
			TreeItem foundTreeItem = getUnBrowsedTreeItem(treeItem, false);
			if ( foundTreeItem != null) {
				if (foundTreeItem.getData() instanceof LiveLinkNode) {
					foundLLNode = (LiveLinkNode) foundTreeItem.getData();
					if (foundLLNode != null) {
						break;
					}
				}
			}
		}
		if (foundLLNode == null) {
			for (TreeItem treeItem : treeItems ) {
				TreeItem foundTreeItem = getUnBrowsedTreeItem(treeItem, true);
				if ( foundTreeItem != null) {
					if (foundTreeItem.getData() instanceof LiveLinkNode) {
						foundLLNode = (LiveLinkNode) foundTreeItem.getData();
						if (foundLLNode != null) {
							break;
						}
					}
				}
			}
		}
		System.out.println (" getUnBrowsedLiveLinkNode " + (foundLLNode == null ? "LLNode is null" : "LLNode is not null " + foundLLNode.getName()));
		return foundLLNode;
	}


	private TreeItem getUnBrowsedTreeItem(TreeItem currentTreeItem, boolean recursiv) {

		TreeItem[] treeItems = currentTreeItem.getItems();
		for (TreeItem treeItem : treeItems) {
			if (treeItem.getData() instanceof LiveLinkNode) {

				LiveLinkNode llNode = (LiveLinkNode) treeItem.getData();
				if ((llNode.isBrowsed() == false)  && (llNode.isLeaf() == false)) {
					return treeItem;
				} 
			}
			if (recursiv) {
				return getUnBrowsedTreeItem(treeItem, true);
			}
		}
		return null;
	}*/


	/*private void findAndExtendTreeItem(LiveLinkNode llNode) {
		boolean found = false;
		TreeItem[] treeItems = this.liveLinkNodeTree.getItems();
		for (TreeItem treeItem : treeItems ) {
			found = findAndExtendTreeItem(found, treeItem, llNode, false);
			if (found) {
				break;
			}
		}
		if (found == false) {
			for (TreeItem treeItem : treeItems ) {
				if (findAndExtendTreeItem(found, treeItem, llNode, true)) {
					found = true;
				}
			}
		}
	}


	private boolean findAndExtendTreeItem(boolean found, TreeItem currentTreeItem , LiveLinkNode llNode, boolean recursiv) {

		TreeItem[] treeItems = currentTreeItem.getItems();
		for (TreeItem treeItem : treeItems) {
			if (treeItem.getData() instanceof LiveLinkNode) {
				LiveLinkNode llInternalNode = (LiveLinkNode) treeItem.getData();
				if (llInternalNode.getId().equalsIgnoreCase(llNode.getId())) {

					// update the current node
					treeItem.setData(llNode);

					// add the children
					Iterator<LiveLinkNode> iter = llNode.getChildren().iterator();
					while (iter.hasNext()) {

						LiveLinkNode llSubNode = iter.next();
						TreeItem item = new TreeItem(treeItem, SWT.NONE);
						item.setText(llSubNode.getName());
						item.setData(llSubNode);
						setImageAddContextualMenus(item, llSubNode);

					}
					treeItem.setExpanded(true);
					found = true;
					return found;
				}
			}
			if (recursiv) {
				return findAndExtendTreeItem(found, treeItem, llNode, recursiv);
			}
		}
		return found;
	}
	 */
	/**
	 * Assume that a tree item is selected => navigate to the corresponding URL
	 */
	private void NavigateToSelectedTreeNode() {

		// it is supposed here that a tree item is selected.
		recursiveBrowserStarted = false;

		if (this.liveLinkNodeTree.getSelectionCount() > 0) {
			TreeItem[] selectedItems = this.liveLinkNodeTree.getSelection();
			if (selectedItems != null) {
				if (selectedItems[0] != null) {
					logger.log(Level.INFO,"Selected Item: "+selectedItems[0].getText());
					TreeItem selectedItem = selectedItems[0];
					if (selectedItem != null) {
						if (selectedItem.getItemCount() == 0) {
							// selected item has no children nodes

							if (selectedItem.getData() instanceof LiveLinkNode) {
								LiveLinkNode llSelectedNode = (LiveLinkNode)selectedItem.getData();
								final LiveLinkURLObservable liveLinkURL = new LiveLinkURLObservable(llSelectedNode.getId());
								parent.getDisplay().syncExec(new Runnable() {
									public void run() {
										// launch browser with new location / new URL
										recursiveBrowserStarted = true;
										oleWebBrowser.Navigate(liveLinkURL.getXmlExportURL().toExternalForm());
									}
								});
							}						
						}
					}
				}
			}
		}
		if (recursiveBrowserStarted == false) {
			// end of the recursive process : write the recursive results
			WritableExcelFile writableExcelFile = new WritableExcelFile(this.getDisplay());
			if (writableExcelFile.writeReadMeSheet(this.liveLinkNodeTree,true)) {
				try {
					boolean b = false;
					if (this.generateIndentedIndex == true) {
						b = writableExcelFile.generateLiveLinkTreeIndentedIndex(this.liveLinkNodeTree);
					}
					else {
						b = writableExcelFile.generateLiveLinkTreeRecursiveIndex(this.liveLinkNodeTree);
					}
					if (b) {
						writableExcelFile.Close();
						new ShellInformationMessage(this.parent.getDisplay(),this.parent.getShell(),
								writableExcelFile.getExcelFilePath());
					}
				} catch (Excel2003MaxRowException ex) {
					new ShellInformationMessage(this.parent.getDisplay(),this.parent.getShell(),
							"Error - EXCEL 2003 - row index exceeds max row number");
				}
				
			}			
		}
	}

	/**
	 * traverse the tree and select a node tree
	 * such as the node is a folder or a project i.e. the node has sub folders.
	 * @param items
	 */
	private void traverseTree(TreeItem[] items) {

		//logger.log(Level.INFO,"Number of Items: "+items.length);
		for (int i = 0; i < items.length; i++) {
			TreeItem item = items[i];

			// if item has no sub items => but it is not leaf we will browse it !!!!
			if (item.getItemCount() == 0) {
				//logger.log(Level.INFO,"item: "+item.getText()+" has no sub items");
				if (item.getData() instanceof LiveLinkNode) {

					LiveLinkNode llNode = (LiveLinkNode) item.getData();
					//logger.log(Level.INFO,"item: "+item.getText()+" is of LiveLink object type: "+llNode.getObjectName());
					//logger.log(Level.INFO,"item: "+item.getText()+" is leaf: "+llNode.isLeaf());
					//logger.log(Level.INFO,"item: "+item.getText()+" is browsed: "+llNode.isBrowsed());
					if ((llNode.isLeaf() == false) && (llNode.isBrowsed() == false)) {
						this.liveLinkNodeTree.select(item);
						//this.liveLinkNodeTree.showItem(item);
					}
				}
			}
			String str = item.toString();
			while (item.getParentItem() != null) {
				str = "\t" + str;
				item = item.getParentItem();
			}
			//System.out.println(str);
			traverseTree(items[i].getItems());
		}
	}

	/**
	 * set an image on the node and add the contextual menus
	 * @param treeItem
	 * @param llNode
	 */
	private void setImageAddContextualMenus(TreeItem treeItem, LiveLinkNode llNode) {

		if ((llNode != null) && (treeItem != null)){
			try {
				treeItem.setImage(this.llObjectImageFactory.getSWTImage(llNode));

				if (llNode.isLeaf() == false) {
					// register the item to its pop-up menu in the mouse listener
					this.treeContextMenuManager.assignContextMenu(treeItem, this.generateIndexMenu);
				}
				if (llNode.isShortCut()) {
					// register the item to its pop-up menu in the mouse listener
					this.treeContextMenuManager.assignContextMenu(treeItem, this.shortCutMenu);
				}
				if (llNode.isURL()) {
					// register the item to its pop-up menu in the mouse listener
					this.treeContextMenuManager.assignContextMenu(treeItem, this.UrlMenu);		
				}
				// 23rd May 2012 : no more handles error - do not display document images
				if ((llNode.isDocument()) && (recursiveBrowserStarted == false)) {
					// register the item to its pop-up menu in the mouse listener
					this.treeContextMenuManager.assignContextMenu(treeItem, this.downLoadMenu);

					// add the Image related to the corresponding file
					String MimeType = llNode.getMimeType();
					if (this.llMimeTypeSet.contains(MimeType)){

						ImageData imageData = this.llMimeTypeSet.getImageData(MimeType);
						treeItem.setImage(new Image(this.parent.getDisplay(), imageData));	
					} else {
						ImageData imageData = this.llMimeTypeSet.getDefaultImageData();
						logger.log(Level.INFO , " get default image data ");
						treeItem.setImage(new Image(this.parent.getDisplay(), imageData));
					}
				}
			}
			catch (Exception ex) {
				logger.log(Level.SEVERE,"exception: "+ex.getLocalizedMessage());
			}
		} else {
			logger.log(Level.SEVERE,"llNode or TreItem are null");
		}
	}

	/**
	 * navigate to provided URL, clear or not the tree
	 * @param strURL
	 * @param clearTree
	 */
	public void goURL(final String strURL, final boolean clearTree) {

		this.parent.getDisplay().asyncExec(new Runnable() {

			public void run() {

				if (clearTree) {
					// need to clear the tree before re starting the browser
					RecursiveLiveLinkTreeCompositeObserver.this.getLiveLinkNodeTree().clearAll(true);
					RecursiveLiveLinkTreeCompositeObserver.this.getLiveLinkNodeTree().removeAll();
				}

				/*
				 * Bug in Internet Explorer.  For some reason, Navigating to an XML document before
				 * a previous Navigate has completed will leave the Browser in a bad state if the
				 * Navigate to the XML document does not complete.  This bad state causes a GP when
				 * the parent window is eventually disposed.  The workaround is to issue a Stop before
				 * navigating to any XML document. 
				 */
				//OleWebBrowserTab.this.oleWebBrowser.Stop();

				// launch browser with new location / new URL from the location Text widget
				RecursiveLiveLinkTreeCompositeObserver.this.oleWebBrowser.Navigate(strURL);
			}
		});
	}

	/**
	 * reset the tree to the initial root node
	 * @return
	 */

	public void goHome() {

		this.parent.getDisplay().syncExec(new Runnable() {

			public void run() {
				// need to clear the tree before re starting the browser
				RecursiveLiveLinkTreeCompositeObserver.this.getLiveLinkNodeTree().clearAll(true);
				RecursiveLiveLinkTreeCompositeObserver.this.getLiveLinkNodeTree().removeAll();
				//stop the browser before next navigation
				//oleWebBrowser.Stop();
				// launch browser with new location / new URL
				RecursiveLiveLinkTreeCompositeObserver.this.oleWebBrowser.Navigate(RecursiveLiveLinkTreeCompositeObserver.this.rootNode.getBrowseURL().toExternalForm());
			}
		});
	}

	/**
	 * goes up from the root of the Tree
	 * @return
	 */
	public boolean goUp() {

		// need to have at least a root node in the tree
		if (this.liveLinkNodeTree.getItemCount() > 0) {
			TreeItem rootItem = this.liveLinkNodeTree.getItem(0);
			if (rootItem.getData() instanceof LiveLinkNode) {

				if (rootItem.getData() instanceof LiveLinkNode) {

					LiveLinkNode llRootNode = (LiveLinkNode) rootItem.getData();
					final LiveLinkURLObservable liveLinkURL = new LiveLinkURLObservable(llRootNode.getParentId());

					this.parent.getDisplay().syncExec(new Runnable() {

						public void run() {
							// need to clear the tree before re starting the browser
							RecursiveLiveLinkTreeCompositeObserver.this.liveLinkNodeTree.clearAll(true);
							RecursiveLiveLinkTreeCompositeObserver.this.liveLinkNodeTree.removeAll();
							//RecursiveLiveLinkTreeComposite.this.oleWebBrowser.Stop();
							// launch browser with new location / new URL
							RecursiveLiveLinkTreeCompositeObserver.this.oleWebBrowser.Navigate(liveLinkURL.getXmlExportURL().toExternalForm());
						}
					});
				}

			}
			return true;
		}
		else {
			new ShellErrorMessage(parent.getDisplay(),parent.getShell(),"Tree needs to have a root !!!");			
		}
		return false;
	}

	/**
	 * This method allows to sleep during a number of milliseconds
	 * @param milliseconds
	 */
	/**private void sleepMilliseconds(int milliseconds) {
		this.parent.getDisplay().timerExec (milliseconds, new Runnable () {
			public void run () {
				//System.out.println ("100 milliseconds");
			}
		});
	}**/

	public void stopRecursiveBrowser(LiveLinkNode llNode) {

		logger.log(Level.INFO,"Stopping Recursive browsing!!!");
		// raise error message
		if (this.liveLinkNodeTree.getSelectionCount() > 0) {
			TreeItem[] selectedItems = this.liveLinkNodeTree.getSelection();
			if (selectedItems != null) {
				TreeItem selectedItem = selectedItems[0];
				if (selectedItem != null) {
					logger.log(Level.INFO,"Recursive export from Selected Node: "+selectedItem.getText());
					new ShellErrorMessage(this.parent.getDisplay(),
							this.parent.getShell(),"Error while parsing XML export: "+selectedItem.getText());
				}
			}
		}
		// modified flag
		if (recursiveBrowserStarted == true) {
			recursiveBrowserStarted = false;
		}
	}

	public String getSelectedNodeTreeName() {
		if (this.liveLinkNodeTree.getSelectionCount() > 0) {
			TreeItem[] selectedItems = this.liveLinkNodeTree.getSelection();
			if (selectedItems != null) {
				TreeItem selectedItem = selectedItems[0];
				if (selectedItem != null) {
					logger.log(Level.INFO,"Recursive export from Selected Node: "+selectedItem.getText());
					return selectedItem.getText();
				}
			}
		}
		return "";
	}


	public void setGeneratedIndentedIndex(boolean b) {
		this.generateIndentedIndex = b;
	}
	public void setRecursiveBrowserStarted(boolean b) {
		this.recursiveBrowserStarted = b;
	}

	public LiveLinkNode getRootNode() {
		return this.rootNode;
	}
	public void setRootNode(LiveLinkNode llNode) {
		this.rootNode = llNode;
	}

	@Override
	public void update(Observable observable, Object arg) {

		logger.log(Level.INFO , " ---------- observable notified ------------ " + (observable instanceof LiveLinkNode));
		if (observable instanceof LiveLinkNode) {
			logger.log(Level.INFO , " --------- observer - notification received ------- = " + ((LiveLinkNode) observable).getName() );
			LiveLinkNode llNode = (LiveLinkNode) observable;
			this.buildTree(llNode);
		}
	}

}
