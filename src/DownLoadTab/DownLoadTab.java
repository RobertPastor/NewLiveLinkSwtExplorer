package DownLoadTab;



import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import JExcelApi.ReadableWorkbook;
import LiveLinkCore.LiveLinkNode;
import LiveLinkCore.ShellErrorMessage;
import de.kupzog.ktable.KTable;
import de.kupzog.ktable.SWTX;


public class DownLoadTab {

	private static final Logger logger = Logger.getLogger(DownLoadTab.class.getName()); 

	private Composite parent = null;
	private TabItem tabItemDownLoad = null;

	private Composite contentPanel = null;
	private Group groupOne = null;
	private Group groupFour = null;
	
	private Composite secondRowComposite = null;
	private Composite thirdRowComposite = null;

	// text area to display the EXCEL file that contains the hyperlinks
	private Text locationText = null;
	// list box to select a set of sheet names
	private List listBox = null;
	private Button ExtractHyperLinksButton = null;
	private Button ExploreLiveLinkDocumentsButton = null;
	private Button DownLoadLiveLinkDocumentsButton = null;

	private FileDropTargetListener fileDropTargetListener = null;

	// table describing the details of the hyper links found in the selected sheets
	private KTable hyperLinksTable = null;
	private HyperLinkSet hyperLinkSet = null;
	private DownLoadedFileSet downLoadedFileSet = null;
	
	private WebBrowserComposite webBrowserComposite = null;

	/**
	 * constructor
	 * @param parent
	 * @param tabFolder
	 */
	public DownLoadTab (Composite parent, TabFolder tabFolder) {

		this.parent = parent;
		this.tabItemDownLoad = new TabItem (tabFolder, SWT.NULL);
		this.tabItemDownLoad.setText ("DownLoad");

		this.contentPanel = new Composite (tabFolder, SWT.BORDER);
		this.tabItemDownLoad.setControl(contentPanel);

		GridLayout gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.numColumns = 1;
		this.contentPanel.setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.BEGINNING;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		this.contentPanel.setLayoutData(gridData);

		initSwtTextAreaExcelDropTarget();
		initSheetNamesListBox();
		this.fileDropTargetListener.setListBox(this.listBox);
		initHyperLinksTable();
		initExportButton();
		
		this.webBrowserComposite = new WebBrowserComposite(this.groupFour,this.DownLoadLiveLinkDocumentsButton);
		this.webBrowserComposite.init(this.hyperLinksTable);

	}

	private void initExportButton() {

		groupFour = new Group (this.thirdRowComposite,SWT.SHADOW_ETCHED_IN);
		groupFour.setText("Step 4: Click the button to start downloading the Livelink documents");

		GridLayout gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.numColumns = 1;
		groupFour.setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		groupFour.setLayoutData(gridData);

		//==========add the button
		Composite buttonComposite = new Composite (groupFour,SWT.FILL);
		
		gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.numColumns = 2;
		buttonComposite.setLayout(gridLayout);

		 gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		buttonComposite.setLayoutData(gridData);
		
		this.ExploreLiveLinkDocumentsButton = new Button(buttonComposite, SWT.PUSH);
		// this button will be enabled only if at least one hyperlink is available in the hyperlink table
		this.ExploreLiveLinkDocumentsButton.setEnabled(false);
		this.ExploreLiveLinkDocumentsButton.setText("Explore LiveLink Documents");
		
		// add the button
		this.DownLoadLiveLinkDocumentsButton = new Button(buttonComposite, SWT.PUSH);
		// this button will be enabled only if at least one hyperlink is available in the hyperlink table
		this.DownLoadLiveLinkDocumentsButton.setEnabled(false);
		this.DownLoadLiveLinkDocumentsButton.setText("Download LiveLink Documents");

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.BEGINNING;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		this.ExploreLiveLinkDocumentsButton.setLayoutData(gridData);
		
		gridData.verticalAlignment = SWT.END;
		this.DownLoadLiveLinkDocumentsButton.setLayoutData(gridData);

		this.ExploreLiveLinkDocumentsButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				logger.log(Level.INFO,"===========Explore LiveLink Document nodes==========");
				if (DownLoadTab.this.hyperLinkSet != null) {
					if(DownLoadTab.this.hyperLinkSet.size() > 0) {
						DownLoadTab.this.downLoadedFileSet = new DownLoadedFileSet(DownLoadTab.this.hyperLinkSet);
						DownLoadTab.this.webBrowserComposite.setDownLoadedFileSet(DownLoadTab.this.downLoadedFileSet);
						
						URL url = DownLoadTab.this.downLoadedFileSet.getNextLiveLinkNodeToExplore();
						if (url != null) {
							DownLoadTab.this.webBrowserComposite.getOleWebBrowser().Navigate(url.toExternalForm());
						}
					}
				}
				logger.log(Level.INFO,"===========Explore LiveLink Document nodes==========");
			}
		});
		
		this.DownLoadLiveLinkDocumentsButton.addSelectionListener(new SelectionAdapter() {

			
			public void widgetSelected(SelectionEvent e) {
				
				File file = new File(DownLoadTab.this.locationText.getText());
				
				logger.log(Level.INFO,"===========Download LiveLink Document nodes==========");
				if (DownLoadTab.this.downLoadedFileSet != null) {
					logger.log(Level.INFO,"downLoaded File Set is not null");
					if(DownLoadTab.this.downLoadedFileSet.size() > 0) {
						logger.log(Level.INFO,"downLoaded File Set size is: "+DownLoadTab.this.downLoadedFileSet.size());
						LiveLinkNode llNode = DownLoadTab.this.downLoadedFileSet.getNextLiveLinkNodeToDownload();
						if (llNode != null) {
							String fileName = llNode.getName();
							final String[] Headers = {"Content-Disposition", "attachment;filename=\"" + fileName + "\""};
							logger.log(Level.INFO,Headers[0]);
							logger.log(Level.INFO,Headers[1]);
							DownLoadTab.this.webBrowserComposite.getOleWebBrowser().Navigate(llNode.getDownloadURL().toExternalForm(),Headers);
						}
						
					}
				}
				logger.log(Level.INFO,"===========Download LiveLink Document nodes==========");

			}
		});
	}

	private void initHyperLinksTable() {

		Group groupThree = new Group (this.secondRowComposite, SWT.SHADOW_ETCHED_IN);
		groupThree.setText("Step 3: Click the button to discover the hyper links found in the selected sheets");

		GridLayout gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.numColumns = 1;
		groupThree.setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		groupThree.setLayoutData(gridData);

		thirdRowComposite = new Composite(groupThree,SWT.FILL);

		gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.numColumns = 1;
		groupThree.setLayout(gridLayout);
		thirdRowComposite.setLayout(gridLayout);

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		thirdRowComposite.setLayoutData(gridData);

		//==========add the button
		this.ExtractHyperLinksButton = new Button(thirdRowComposite, SWT.PUSH);
		// this button will be enabled only if at least one sheet is selected in the List Box
		this.ExtractHyperLinksButton.setEnabled(false);

		this.ExtractHyperLinksButton.setText("Extract Hyperlinks from selected sheets");

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.BEGINNING;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		this.ExtractHyperLinksButton.setLayoutData(gridData);

		this.ExtractHyperLinksButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				logger.log(Level.INFO,"===========Extract HyperLinks from Selected Sheets==========");
				/**
				 * inputs for the table filling are : 
				 * an excel file or the corresponding work book
				 * the set of selected EXCEL sheets
				 **/ 
				int numberOfSelectedSheets = DownLoadTab.this.listBox.getSelectionCount();
				if ( numberOfSelectedSheets > 0) {
					logger.log(Level.INFO,"number of selected sheets: "+numberOfSelectedSheets);
					String[] selectedSheetNames = DownLoadTab.this.listBox.getSelection();

					for (int i=0 ; i < selectedSheetNames.length; i++) {
						logger.log(Level.INFO,"ListBox selection={" + selectedSheetNames[i] + "}");
					}

					if (selectedSheetNames.length > 0) {
						String FilePath = DownLoadTab.this.locationText.getText();
						ReadableWorkbook wb = new ReadableWorkbook(FilePath);

						if (wb.open()) {
							DownLoadTab.this.hyperLinkSet = new HyperLinkSet();
							wb.getHyperLinks(selectedSheetNames,DownLoadTab.this.hyperLinkSet);
							DownLoadTab.this.downLoadedFileSet = new DownLoadedFileSet(DownLoadTab.this.hyperLinkSet);
							// update the table
							if (DownLoadTab.this.hyperLinksTable.getModel() instanceof HyperLinkTableModel) {
								HyperLinkTableModel hyperLinksTableModel = (HyperLinkTableModel) DownLoadTab.this.hyperLinksTable.getModel();
								hyperLinksTableModel.setHyperLinks(DownLoadTab.this.downLoadedFileSet);
								DownLoadTab.this.hyperLinksTable.setModel(hyperLinksTableModel);
								DownLoadTab.this.hyperLinksTable.setVisible(true);
							}
							if (DownLoadTab.this.downLoadedFileSet.size() > 0) {
								DownLoadTab.this.ExploreLiveLinkDocumentsButton.setEnabled(true);
							}
						}
						wb.close();
					}
				}
				else {
					new ShellErrorMessage(DownLoadTab.this.parent.getDisplay(),
							DownLoadTab.this.parent.getShell(),"Please select a set of Sheets before using this button");
				}
				logger.log(Level.INFO,"===========Extract HyperLinks from Selected Sheets==========");
			}
		});

		this.hyperLinksTable = new KTable (thirdRowComposite, SWTX.FILL_WITH_LASTCOL|SWTX.AUTO_SCROLL);
		this.hyperLinksTable.setLayout(gridLayout);

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		this.hyperLinksTable.setLayoutData(gridData);

		this.hyperLinksTable.setVisible(false);

		final HyperLinkTableModel model = new HyperLinkTableModel(thirdRowComposite,null);
		this.hyperLinksTable.setModel(model);
		
		initTableLookAndFeel();

	}
	
	private void initTableLookAndFeel () {
		/**
		 *  Set Excel-like table cursors
		 */

		if ( SWT.getPlatform().equals("win32") ) {

			// Cross
			Image crossCursor = SWTX.loadImageResource(this.hyperLinksTable.getDisplay(), "/icons/cross_win32.gif");	
			// Row Resize	
			Image row_resizeCursor = SWTX.loadImageResource(this.hyperLinksTable.getDisplay(), "/icons/row_resize_win32.gif");	
			// Column Resize	
			Image column_resizeCursor  = SWTX.loadImageResource(this.hyperLinksTable.getDisplay(), "/icons/column_resize_win32.gif");

			// we set the hot-spot to the center, so calculate the number of pixels from hot-spot to lower border:	
			Rectangle crossBound        = crossCursor.getBounds();
			Rectangle rowresizeBound    = row_resizeCursor.getBounds();
			Rectangle columnresizeBound = column_resizeCursor.getBounds();

			Point crossSize        = new Point(crossBound.width/2, crossBound.height/2);
			Point rowresizeSize    = new Point(rowresizeBound.width/2, rowresizeBound.height/2);
			Point columnresizeSize = new Point(columnresizeBound.width/2, columnresizeBound.height/2);

			this.hyperLinksTable.setDefaultCursor(new Cursor(this.hyperLinksTable.getDisplay(), crossCursor.getImageData(), crossSize.x, crossSize.y), crossSize);
			this.hyperLinksTable.setDefaultRowResizeCursor(new Cursor(this.hyperLinksTable.getDisplay(), row_resizeCursor.getImageData(), rowresizeSize.x, rowresizeSize.y));
			this.hyperLinksTable.setDefaultColumnResizeCursor(new Cursor(this.hyperLinksTable.getDisplay(), column_resizeCursor.getImageData(), columnresizeSize.x, columnresizeSize.y));

		} else {

			// Cross
			Image crossCursor      = SWTX.loadImageResource(this.hyperLinksTable.getDisplay(), "/icons/cross.gif");
			Image crossCursor_mask = SWTX.loadImageResource(this.hyperLinksTable.getDisplay(), "/icons/cross_mask.gif");

			// Row Resize
			Image row_resizeCursor      = SWTX.loadImageResource(this.hyperLinksTable.getDisplay(), "/icons/row_resize.gif");
			Image row_resizeCursor_mask = SWTX.loadImageResource(this.hyperLinksTable.getDisplay(), "/icons/row_resize_mask.gif");

			// Column Resize

			Image column_resizeCursor      = SWTX.loadImageResource(this.hyperLinksTable.getDisplay(), "/icons/column_resize.gif");
			Image column_resizeCursor_mask = SWTX.loadImageResource(this.hyperLinksTable.getDisplay(), "/icons/column_resize_mask.gif");

			// we set the hotspot to the center, so calculate the number of pixels from hotspot to lower border:

			Rectangle crossBound        = crossCursor.getBounds();
			Rectangle rowresizeBound    = row_resizeCursor.getBounds();
			Rectangle columnresizeBound = column_resizeCursor.getBounds();

			Point crossSize        = new Point(crossBound.width/2, crossBound.height/2);
			Point rowresizeSize    = new Point(rowresizeBound.width/2, rowresizeBound.height/2);
			Point columnresizeSize = new Point(columnresizeBound.width/2, columnresizeBound.height/2);

			this.hyperLinksTable.setDefaultCursor(new Cursor(this.hyperLinksTable.getDisplay(), crossCursor_mask.getImageData(), crossCursor.getImageData(), crossSize.x, crossSize.y), crossSize);
			this.hyperLinksTable.setDefaultRowResizeCursor(new Cursor(this.hyperLinksTable.getDisplay(), row_resizeCursor_mask.getImageData(), row_resizeCursor.getImageData(), rowresizeSize.x, rowresizeSize.y));
			this.hyperLinksTable.setDefaultColumnResizeCursor(new Cursor(this.hyperLinksTable.getDisplay(), column_resizeCursor_mask.getImageData(), column_resizeCursor.getImageData(), columnresizeSize.x, columnresizeSize.y));

		}

	}

	private void initSheetNamesListBox() {

		Group groupTwo = new Group(this.groupOne, SWT.SHADOW_IN);
		groupTwo.setText("Step 2: Select here the EXCEL sheet(s) that contain the hyper links");

		GridLayout gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.numColumns = 1;
		groupTwo.setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		groupTwo.setLayoutData(gridData);

		secondRowComposite = new Composite(groupTwo,SWT.FILL);

		gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.numColumns = 1;
		secondRowComposite.setLayout(gridLayout);

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		secondRowComposite.setLayoutData(gridData);

		//=============================================
		this.listBox = new List(secondRowComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		this.listBox.setToolTipText("Select the sheets that contain the hyper links to analyse"); 

		this.listBox.setEnabled(true);
		this.listBox.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				logger.log(Level.INFO,"===========Sheet Selection==========");
				String strSelection = "";
				int [] selection = DownLoadTab.this.listBox.getSelectionIndices ();
				for (int i=0 ; i < selection.length; i++) {
					if (strSelection.length() > 0) {
						strSelection += " ";
					}
					strSelection += selection [i] + " ";
					strSelection += DownLoadTab.this.listBox.getItem(selection[i]);

					logger.log(Level.INFO,"ListBox selection={" + strSelection + "}");
				}
				if (DownLoadTab.this.listBox.getSelectionCount() > 0) {
					DownLoadTab.this.ExtractHyperLinksButton.setEnabled(true);
				}
				else {
					DownLoadTab.this.ExtractHyperLinksButton.setEnabled(false);
				}
			}
		});

		Color green = parent.getDisplay().getSystemColor(SWT.COLOR_GREEN);
		this.listBox.setBackground(green);

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.BEGINNING;

		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		this.listBox.setLayoutData(gridData);

	}

	private void initSwtTextAreaExcelDropTarget () {

		groupOne  = new Group(this.contentPanel, SWT.SHADOW_IN);
		groupOne.setText("Step 1: Drop here the EXCEL file that contains the LiveLink hyper links");

		GridLayout gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.numColumns = 1;
		groupOne.setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		groupOne.setLayoutData(gridData);

		Composite firstRowComposite = new Composite(groupOne,SWT.FILL);

		gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = false;
		gridLayout.numColumns = 1;
		firstRowComposite.setLayout(gridLayout);

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.BEGINNING;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		firstRowComposite.setLayoutData(gridData);

		//=========================================================================
		this.locationText = new Text(firstRowComposite, SWT.FILL|SWT.BORDER);
		this.locationText.setToolTipText("Drop here the EXCEL file"); 
		this.locationText.setEditable(true);
		this.locationText.setEnabled(true);

		Color yellow = parent.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
		this.locationText.setBackground(yellow);

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.BEGINNING;

		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		locationText.setLayoutData(gridData);

		//================================================================
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
		Transfer[] types = new Transfer[] {FileTransfer.getInstance()};

		DropTarget target = new DropTarget(locationText, operations );
		target.setTransfer(types);

		this.fileDropTargetListener = new FileDropTargetListener(this.parent,this.locationText);
		target.addDropListener(this.fileDropTargetListener);

	}

}
