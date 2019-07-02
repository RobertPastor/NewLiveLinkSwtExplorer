package FileExplorerTab;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import JExcelApi.Excel2003MaxRowsException;
import JExcelApi.WritableExcelFile;
import LiveLinkCore.ShellInformationMessage;
import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellResizeListener;
import de.kupzog.ktable.KTableCellSelectionListener;
import de.kupzog.ktable.KTableSortComparator;
import de.kupzog.ktable.KTableSortOnClick;
import de.kupzog.ktable.SWTX;

public class FileExplorerTab extends CTabItem {

	/**
	 * this tab allows to select Q-PCP files that were down-loaded from LiveLink
	 * Q-PCP files analysis retrieves the topic values in order to allow for insertion in the DATAMART
	 */
	private static final Logger logger = Logger.getLogger(FileExplorerTab.class.getName()); 

	private Composite parent = null;
	private Composite contentPanel = null;
	private Tree fileTree = null;
	private Composite mainSashFormComposite = null;
	private Composite locationComposite = null;
	private SashForm sashForm = null;
	private Composite tableComp = null;
	private Button setFolderButton = null;

	private Text locationText = null;
	private KTable fileKTable = null;
	private FileKTableSortedModel fileKTableModel = null;
	private File root = null;

	private TreeContextMenuManager treeContextMenuManager = null;
	private FileKTableMenuSelectionListener fileKTableMenuSelectionListener = null;
	private KTableCellSelectionListener kTableCellSelectionListener = null;
	private Menu exportMenu = null;
	private Menu tableMenu = null;

	private CTabFolder tabFolder = null;
	
	private FileExplorerRecursiveTab fileExplorerRecursivTab = null;

	private StatusBarObserver analysisStatus = null;
	private ProgressBar analysisProgressBar = null;
	private String userQpcpFolder = "";

	public FileExplorerTab (Composite comp, CTabFolder tabFolder) {

		super(tabFolder, SWT.NULL);
		logger.log(Level.INFO,"File Explorer Tab creation");

		this.parent = comp;
		this.setTabFolder(tabFolder);

		this.setText ("File Explorer");

		InputStream in = FileExplorerTab.class.getResourceAsStream("folder.gif");
		if (in != null) {
			Image image = new Image(this.parent.getDisplay() , in);
			this.setImage(image);
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.contentPanel = new Composite (tabFolder, SWT.FILL|SWT.BORDER);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = layout.marginHeight = 0;
		layout.horizontalSpacing = layout.verticalSpacing = 1;
		layout.makeColumnsEqualWidth = true;
		//==========================
		this.contentPanel.setLayout(layout);
		//==========================

		GridData gridData = new GridData();
		gridData.horizontalSpan = 1;
		gridData.grabExcessHorizontalSpace  = true;
		gridData.grabExcessVerticalSpace  = true;
		//=========================
		this.contentPanel.setLayoutData(gridData);
		//=========================
		this.setControl(contentPanel);

		initLocationZoneComposite();
		initSashFormComposite();
		initShashForm();
		initFileTree();
		initTable();
		initTreeContextMenu();
		initStatusBarAndProgressComposite();
		// percentage of space on each part of the sash form
		this.sashForm.setWeights(new int[] {30,70});

	}


	public void initLocationZoneComposite() {

		this.locationComposite = new Composite(this.contentPanel, SWT.FILL|SWT.BORDER);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = layout.marginHeight = 0;
		layout.horizontalSpacing = layout.verticalSpacing = 1;
		layout.makeColumnsEqualWidth = false;
		this.locationComposite.setLayout(layout);

		//========================================================================

		GridData gridDataOne = new GridData();
		gridDataOne.horizontalAlignment = SWT.FILL;
		gridDataOne.verticalAlignment = SWT.BEGINNING;
		gridDataOne.grabExcessHorizontalSpace = true;
		gridDataOne.grabExcessVerticalSpace = false;
		this.locationComposite.setLayoutData(gridDataOne);

		//=======================================================================

		this.locationText = new Text(this.locationComposite, SWT.FILL | SWT.BORDER);
		this.locationText.setToolTipText("Please select a drive in the Tree..."); 
		this.locationText.pack(true);
		this.locationText.setEditable(false);
		this.locationText.setEnabled(true);

		GridData gridDataTwo = new GridData();
		gridDataTwo.horizontalAlignment = SWT.FILL;
		gridDataTwo.verticalAlignment = SWT.BEGINNING;

		gridDataTwo.grabExcessHorizontalSpace = true;
		gridDataTwo.grabExcessVerticalSpace = false;
		locationText.setLayoutData(gridDataTwo);

		Color yellow = parent.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
		this.locationText.setBackground(yellow);

		this.locationText.setText("Please select a drive in the Tree...");

		//=======================================================================

		//========================================================================
	}

	private void initSashFormComposite() {

		this.mainSashFormComposite = new Composite(this.contentPanel, SWT.FILL|SWT.BORDER);

		//========================================================================
		GridLayout layout = new GridLayout();

		layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = layout.marginHeight = 0;
		layout.horizontalSpacing = layout.verticalSpacing = 1;
		layout.makeColumnsEqualWidth = true;
		this.mainSashFormComposite.setLayout(layout);

		//========================================================================

		GridData gridData = new GridData(SWT.FILL|SWT.HORIZONTAL|SWT.VERTICAL);
		gridData.horizontalSpan = 1;
		gridData.grabExcessHorizontalSpace  = true;
		gridData.grabExcessVerticalSpace  = true;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		this.mainSashFormComposite.setLayoutData(gridData);

	}

	private void initShashForm() {

		this.sashForm = new SashForm(this.mainSashFormComposite, SWT.FILL|SWT.BORDER);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = layout.marginHeight = 0;
		layout.horizontalSpacing = layout.verticalSpacing = 1;
		layout.makeColumnsEqualWidth = true;
		this.sashForm.setLayout(layout);

		GridData gridData = new GridData(SWT.HORIZONTAL);
		gridData.horizontalSpan = 1;
		gridData.grabExcessHorizontalSpace  = true;
		gridData.grabExcessVerticalSpace  = true;

		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;

		this.sashForm.setLayoutData(gridData);

		sashForm.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (e.detail == SWT.DRAG)
					return;
				GridData data = (GridData) fileTree.getLayoutData();
				Rectangle trim = fileTree.computeTrim(0, 0, 0, 0);
				data.widthHint = e.x - trim.width;
				sashForm.layout(true);
			}
		});
	}

	private void initFileTree() {

		//==================================================================
		// allow only one selection in the tree
		this.fileTree = new Tree(this.sashForm,  SWT.FULL_SELECTION | SWT.FILL | SWT.BORDER | SWT.SINGLE);

		GridLayout layoutOne = new GridLayout();
		layoutOne.numColumns = 1;
		layoutOne.marginWidth = layoutOne.marginHeight = 0;
		layoutOne.horizontalSpacing = layoutOne.verticalSpacing = 1;
		layoutOne.makeColumnsEqualWidth = true;
		fileTree.setLayout(layoutOne);

		GridData gridDataOne = new GridData(SWT.VERTICAL);
		gridDataOne.horizontalSpan = 1;
		gridDataOne.horizontalAlignment = SWT.FILL;
		gridDataOne.verticalAlignment = SWT.FILL;
		gridDataOne.grabExcessHorizontalSpace  = true;
		gridDataOne.grabExcessVerticalSpace  = true;
		fileTree.setLayoutData(gridDataOne);

	}

	private void initTable() {
		//==========================
		tableComp = new Composite(this.sashForm, SWT.FILL | SWT.BORDER);

		GridLayout layoutTwo = new GridLayout();
		layoutTwo.numColumns = 1;
		layoutTwo.marginWidth = layoutTwo.marginHeight = 0;
		layoutTwo.horizontalSpacing = layoutTwo.verticalSpacing = 1;
		layoutTwo.makeColumnsEqualWidth = true;
		tableComp.setLayout(layoutTwo);

		GridData gridDataTwo = new GridData(SWT.VERTICAL);
		gridDataTwo.horizontalSpan = 1;
		gridDataTwo.horizontalAlignment = SWT.FILL;
		gridDataTwo.verticalAlignment = SWT.FILL;
		gridDataTwo.grabExcessHorizontalSpace  = true;
		gridDataTwo.grabExcessVerticalSpace  = true;
		tableComp.setLayoutData(gridDataTwo);

		this.fileKTable = new KTable(this.tableComp , SWTX.FILL_WITH_LASTCOL | SWTX.AUTO_SCROLL);

		GridLayout layoutThree = new GridLayout();
		layoutThree.numColumns = 1;
		layoutThree.marginWidth = layoutThree.marginHeight = 0;
		layoutThree.horizontalSpacing = layoutThree.verticalSpacing = 1;
		layoutThree.makeColumnsEqualWidth = true;
		this.fileKTable.setLayout(layoutThree);

		GridData gridDataThree = new GridData(SWT.VERTICAL);
		gridDataThree.horizontalSpan = 1;
		gridDataThree.horizontalAlignment = SWT.FILL;
		gridDataThree.verticalAlignment = SWT.FILL;
		gridDataThree.grabExcessHorizontalSpace  = true;
		gridDataThree.grabExcessVerticalSpace  = true;
		this.fileKTable.setLayoutData(gridDataThree);

		// allow to select only one row in the table

		// define the table model
		this.fileKTableModel = new FileKTableSortedModel(this.tableComp, null);
		this.fileKTable.setModel(this.fileKTableModel);

		this.fileKTable.setEnabled(false);
		this.fileKTable.setVisible(false);

		final Cursor cursor = new Cursor(this.parent.getDisplay(), SWT.CURSOR_ARROW);
		this.fileKTable.addListener(SWT.MouseHover , new Listener() {
			public void handleEvent(Event e) {
				fileKTable.setCursor(cursor);
			}
		});

		// implement resorting when the user clicks on the table header:
		kTableCellSelectionListener = new KTableSortOnClick(
				this.fileKTable, 
				new KTableFilesSortComparatorImp(this.fileKTableModel, -1, KTableSortComparator.SORT_NONE));
		this.fileKTable.addCellSelectionListener(kTableCellSelectionListener);

		this.fileKTable.addCellResizeListener (
				new KTableCellResizeListener() {
					public void columnResized(int col, int newWidth) {
						//System.out.println("Column "+col+" resized to "+newWidth);
					}
					public void rowResized(int row, int newHeight) {
						//System.out.println("Row "+row+" resized to "+newHeight);
					}
				});

		this.fileKTable.addCellSelectionListener(
				new KTableCellSelectionListener() 	{

					public void cellSelected(int col, int row, int statemask) {
						// the idea is to map the row index back to the model index since the given row index
						// changes when sorting is done.
						// because of sorting need to map rows

						if (col==0){
							disposeTableContextMenu();

							int modelRow = fileKTableModel.mapRowIndexToModel(row);
							logger.log(Level.INFO, "is excel file= " + (fileKTableModel.isExcelFile(modelRow-1) ? "true" : "false" ));
							if ( col == 0 && fileKTableModel.isExcelFile(modelRow-1)) {
								// context menu only in column one
								analysisStatus.setText(" ... right click on the file Name to access the context menu ....");
								addTableContextMenu(modelRow);
							}
							else {
								disposeTableContextMenu();
							}
						} else {
							disposeTableContextMenu();
						}
					}

					public void fixedCellSelected(int col, int row, int statemask) {
						//logger.log(Level.INFO , ("Header ["+col+";"+row+"] selected."));
					}
				});
	}

	private void disposeTableContextMenu() {
		if  (tableMenu != null) {
			if (tableMenu.isDisposed() == false) {
				tableMenu.dispose();
				tableMenu = null;
			}
		}
	}

	private void addTableContextMenu(int modelRow) {

		logger.log(Level.INFO,"================table context menu==============================================");
	}

	private void initTreeContextMenu() {

		//===========================
		this.treeContextMenuManager = new TreeContextMenuManager(this.fileTree, this.parent.getShell());
		this.fileTree.addMouseListener(this.treeContextMenuManager);

		//===========================
		// create the pop-up export EXCEL Menu
		this.exportMenu = new Menu(this.parent.getShell());
		MenuItem menuItemExport = new MenuItem(this.exportMenu,SWT.PUSH);
		menuItemExport.setText("Export from Selected Node");
		menuItemExport.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				logger.log(Level.INFO,"==============================================================");
				logger.log(Level.INFO,"Export from Selected Item: "+e.toString());
				if (FileExplorerTab.this.fileTree.getSelectionCount() > 0) {
					TreeItem selectedItem = FileExplorerTab.this.fileTree.getSelection()[0];
					logger.log(Level.INFO,"Export from Selected Item: "+selectedItem.getText());

					if (fileTree.getSelection()[0].getData() instanceof File) {
						File selectedFile = (File) fileTree.getSelection()[0].getData();

						//================================
						// here new tab from here
						WritableExcelFile writableExcelFile = new WritableExcelFile(FileExplorerTab.this.parent.getDisplay());
						if (writableExcelFile.create(selectedFile)) {
							try {
								if (writableExcelFile.writeFileExplorer(selectedFile, FileExplorerTab.this.analysisStatus)) {
									writableExcelFile.Close();
									new ShellInformationMessage(FileExplorerTab.this.parent.getDisplay(),
											FileExplorerTab.this.parent.getShell(),
											writableExcelFile.getExcelFilePath());					
								}
							} catch (Excel2003MaxRowsException ex) {
								new ShellInformationMessage(FileExplorerTab.this.parent.getDisplay(),
										FileExplorerTab.this.parent.getShell(),
										"Error - EXCEL 2003 - row index exceeds max rows number");	
							}
						}					
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		// create the pop-up recursive export EXCEL Menu
		MenuItem menuItemRecursiveExport = new MenuItem(this.exportMenu, SWT.PUSH);
		menuItemRecursiveExport.setText("Recursive Export from Selected Node");
		menuItemRecursiveExport.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				logger.log(Level.INFO,"==============================================================");
				logger.log(Level.INFO,"Recursive Export from Selected node: "+e.toString());
				if (FileExplorerTab.this.fileTree.getSelectionCount() > 0) {
					TreeItem selectedItem = FileExplorerTab.this.fileTree.getSelection()[0];
					logger.log(Level.INFO,"Recursive Export from Selected node: "+selectedItem.getText());

					if (fileTree.getSelection()[0].getData() instanceof File) {
						final File selectedFile = (File) fileTree.getSelection()[0].getData();

						//===========================
						// create a new tab from here
						//==========================
						
						FileExplorerTab.this.parent.getDisplay().syncExec(new Runnable() {
							// navigate again to the home location - in browse mode
							public void run() {
								
								FileExplorerTab.this.fileExplorerRecursivTab = new FileExplorerRecursiveTab(FileExplorerTab.this.parent, FileExplorerTab.this.tabFolder, selectedFile );
								
							}
						});
						
						
						/**
						WritableExcelFile writableExcelFile = new WritableExcelFile(FileExplorerTab.this.parent.getDisplay());
						if (writableExcelFile.Create(selectedFile)) {
							try {
								if (writableExcelFile.WriteFileExplorerRecursive(selectedFile, FileExplorerTab.this.analysisStatus)) {
									writableExcelFile.Close();
									new ShellInformationMessage(FileExplorerTab.this.parent.getDisplay(),
											FileExplorerTab.this.parent.getShell(),
											writableExcelFile.getExcelFilePath());					
								}
							}catch (Excel2003MaxRowException ex) {
								new ShellInformationMessage(FileExplorerTab.this.parent.getDisplay(),
										FileExplorerTab.this.parent.getShell(),
										"Error - EXCEL 2003 - row index exceeds max rows number");	
							}

						}
						*/					
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		// build the file tree
		File[] roots = File.listRoots();
		FileSystemView fsv = FileSystemView.getFileSystemView();
		for (int i = 0; i < roots.length; i++) {
			File file = roots[i];
			TreeItem treeItem = new TreeItem(fileTree, SWT.NONE);
			if (fsv.getSystemDisplayName(file).length() > 0) {
				treeItem.setText(fsv.getSystemDisplayName(file));
			}
			else {
				treeItem.setText(file.getAbsolutePath());
			}
			//
			treeItem.setData(file);
			if (file.isDirectory()) {
				// assign a context menu
				this.treeContextMenuManager.assignContextMenu(treeItem, exportMenu);
			}
			treeItem.setImage(this.getImage(file));
			new TreeItem(treeItem, SWT.NONE);
		}

		// if the user has a preferred folder in TA_MATURITY_USERS
		if (this.userQpcpFolder.length()>0) {
			// add a specific entry to the tree
			try {

				logger.log(Level.INFO ,  " path= " + this.userQpcpFolder);
				File file = new File(userQpcpFolder);
				if (file != null) {
					// add the user entry to the root of the file tree
					TreeItem treeItem = new TreeItem(fileTree, SWT.NONE);
					if (fsv.getSystemDisplayName(file).length() > 0) {
						treeItem.setText(fsv.getSystemDisplayName(file));
					}
					else {
						treeItem.setText(file.getAbsolutePath());
					}
					treeItem.setData(file);
					treeItem.setImage(this.getImage(file));
				}
			}
			catch (Exception e) {
				logger.log(Level.SEVERE, " error while adding preferred folder: " +e.getLocalizedMessage());
			}
		}

		// add a listener to react to user EXPAND clicks on the tree nodes => no changes to the table
		fileTree.addListener(SWT.Expand, new Listener() {
			public void handleEvent(Event e) {
				logger.log(Level.INFO ,  " event= EXPAND " );

				TreeItem item = (TreeItem) e.item;
				if (item == null)
					return;
				if (item.getItemCount() == 1) {
					TreeItem firstItem = item.getItems()[0];
					if (firstItem.getData() != null)
						return;
					firstItem.dispose();
				} else {
					return;
				}
				File root = (File) item.getData();
				File[] files = root.listFiles();
				if (files == null)
					return;
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isDirectory()) {
						TreeItem treeItem = new TreeItem(item, SWT.NONE);			
						treeItem.setText(file.getName());
						treeItem.setData(file);
						if (file.isDirectory()) {
							FileExplorerTab.this.treeContextMenuManager.assignContextMenu(treeItem, exportMenu);
						}
						treeItem.setImage(FileExplorerTab.this.getImage(file));
						new TreeItem(treeItem, SWT.NONE);
					}
				}
			}
		});

		// add listener to react to the selection of a node
		fileTree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				logger.log(Level.INFO ,  " event= Selection" );

				TreeItem item = (TreeItem) e.item;
				if (item == null)
					return;
				root = (File) item.getData();
				logger.log(Level.INFO ,  " event= Selection - root= " + root.getAbsolutePath());
				// select a node means update the table
				updateSelectedRootTable();
				// delete any context menu in the table
			}
		});

	}

	private void initStatusBarAndProgressComposite() {

		Composite lastRowComposite = new Composite(this.contentPanel,SWT.FILL | SWT.BORDER);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.makeColumnsEqualWidth = false;
		lastRowComposite.setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;

		// composite grows horizontally
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		lastRowComposite.setLayoutData(gridData);

		// Add a label for displaying status messages as they are received from the control
		this.analysisStatus = new StatusBarObserver(lastRowComposite, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);

		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
		gridData.horizontalSpan = 2;
		// text grows horizontally with the frame
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;

		this.analysisStatus.setToolTipText("status message");
		this.analysisStatus.setText("... In the Tree view, click on on Folder to open it ...");


		// Add a progress bar to display downloading progress information
		this.analysisProgressBar = new ProgressBar(lastRowComposite, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		gridData.grabExcessHorizontalSpace = false;
		gridData.grabExcessVerticalSpace = false;
		this.analysisProgressBar.setLayoutData(gridData);		

	}

	private Image getImage(File file) {
		return new Image(parent.getDisplay(), getImageData(file));
	}

	public ImageData getImageData(File file) {
		ImageIcon systemIcon = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(file);
		java.awt.Image image = systemIcon.getImage();
		if (image instanceof BufferedImage) {
			return convertToSWT((BufferedImage)image);
		}
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		return convertToSWT(bufferedImage);
	}

	private ImageData convertToSWT(BufferedImage bufferedImage) {
		if (bufferedImage.getColorModel() instanceof DirectColorModel) {
			DirectColorModel colorModel = (DirectColorModel)bufferedImage.getColorModel();
			PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask());
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					int rgb = bufferedImage.getRGB(x, y);
					int pixel = palette.getPixel(new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF)); 
					data.setPixel(x, y, pixel);
					if (colorModel.hasAlpha()) {
						data.setAlpha(x, y, (rgb >> 24) & 0xFF);
					}
				}
			}
			return data;            
		} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
			IndexColorModel colorModel = (IndexColorModel)bufferedImage.getColorModel();
			int size = colorModel.getMapSize();
			byte[] reds = new byte[size];
			byte[] greens = new byte[size];
			byte[] blues = new byte[size];
			colorModel.getReds(reds);
			colorModel.getGreens(greens);
			colorModel.getBlues(blues);
			RGB[] rgbs = new RGB[size];
			for (int i = 0; i < rgbs.length; i++) {
				rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
			}
			PaletteData palette = new PaletteData(rgbs);
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
			data.transparentPixel = colorModel.getTransparentPixel();
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					data.setPixel(x, y, pixelArray[0]);
				}
			}
			return data;
		}
		return null;
	}

	/**
	 * this function is used after a file down-load from LiveLink to refresh the currently displayed table
	 */
	public void updateSelectedRootTable() {

		disposeTableContextMenu();

		if (this.root != null) {
			logger.log(Level.INFO, " update root: "+ this.root.getAbsolutePath());
			FileExplorerTab.this.parent.getDisplay().syncExec(new Runnable() {

				public void run() {

					locationText.setText(root.getAbsolutePath());
					locationText.pack(true);
					fileKTableModel = new FileKTableSortedModel(tableComp, root.listFiles());
					fileKTable.setModel(fileKTableModel);

					fileKTable.setEnabled(true);
					fileKTable.setVisible(true);

					fileKTable.removeCellSelectionListener(kTableCellSelectionListener);
					kTableCellSelectionListener = new KTableSortOnClick(fileKTable, 
							new KTableFilesSortComparatorImp(
									fileKTableModel,
									-1,
									KTableSortComparator.SORT_NONE));
					fileKTable.addCellSelectionListener(kTableCellSelectionListener);

					// each time the model changes , need to inform the table menu selection listener
				}
			});
		}
	}


	public CTabFolder getTabFolder() {
		return this.tabFolder;
	}


	public void setTabFolder(CTabFolder tabFolder) {
		this.tabFolder = tabFolder;
	}


	public FileKTableMenuSelectionListener getFileKTableMenuSelectionListener() {
		return this.fileKTableMenuSelectionListener;
	}


	public void setFileKTableMenuSelectionListener(
			FileKTableMenuSelectionListener fileKTableMenuSelectionListener) {
		this.fileKTableMenuSelectionListener = fileKTableMenuSelectionListener;
	}

}
