package FileExplorerTab;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import JExcelApi.Excel2003MaxRowsException;
import LiveLinkCore.ShellInformationMessage;
import RecursiveLiveLinkBrowser.NavigationAreaCompositeObserver;

public class FileExplorerRecursivTab extends CTabItem {

	private static final Logger logger = Logger.getLogger(FileExplorerRecursivTab.class.getName()); 

	private Composite parentComposite = null;
	private CTabFolder cTabFolder = null;
	private Composite contentPanel = null;
	private File initialFile = null;

	private StatusBarObserver analysisStatus = null;
	private ProgressBar analysisProgressBar = null;

	private ArrayList<File> browsedFiles = null;
	private Composite locationComposite = null;

	private Text locationText = null;

	private Button launchButton = null;


	public FileExplorerRecursivTab(final Composite parentComposite, final CTabFolder cTabFolder,  final File initialFile) {
		super(cTabFolder, SWT.NULL);

		this.parentComposite = parentComposite;
		this.cTabFolder = cTabFolder;
		this.initialFile = initialFile;
		this.setText ("Recursiv File Explorer");

		this.browsedFiles = new ArrayList<>();

		InputStream in = FileExplorerTab.class.getResourceAsStream("folder.gif");
		if (in != null) {
			Image image = new Image(this.parentComposite.getDisplay() , in);
			this.setImage(image);
		}
		try {
			in.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE , e.getLocalizedMessage() );
		}

		this.contentPanel = new Composite (cTabFolder, SWT.FILL|SWT.BORDER);

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

		// init status Bar and progress composite
		try {
			initLocationZoneComposite();
			initStatusBarAndProgressComposite();
			addLaunchButton();
			// activate this tab folder tab
			this.activate();
			
		} catch (Excel2003MaxRowsException e) {
			e.printStackTrace();
		}

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

		Color yellow = this.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
		this.locationText.setBackground(yellow);

		try {
			this.locationText.setText(this.initialFile.getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//=======================================================================

	}

	private void disableButtons() {
		this.launchButton.setEnabled(false);
	}

	private void enableButtons() {
		this.launchButton.setEnabled(true);
	}

	private void addLaunchButton() {

		//==============================================================
		this.launchButton = new Button(this.contentPanel, SWT.PUSH);
		this.launchButton.setText("Launch File Exploration");
		this.launchButton.setToolTipText("launch the file exploration");

		Color color = this.getDisplay().getSystemColor(SWT.COLOR_CYAN);
		this.launchButton.setBackground(color);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.BEGINNING;
		gridData.grabExcessHorizontalSpace = false;
		gridData.grabExcessVerticalSpace = false;
		launchButton.setLayoutData(gridData);

		launchButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				logger.log(Level.INFO,"===========Launch Button pressed==========");

				disableButtons();

				logger.log(Level.INFO,"===========Launch Button pressed==========");

				enableButtons();
			}
		});
	}

	private void initStatusBarAndProgressComposite() throws Excel2003MaxRowsException {

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
		this.analysisStatus.setText("... recursiv file explorer tracing area  ...");

		// Add a progress bar to display downloading progress information
		this.analysisProgressBar = new ProgressBar(lastRowComposite, SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		gridData.grabExcessHorizontalSpace = false;
		gridData.grabExcessVerticalSpace = false;
		this.analysisProgressBar.setLayoutData(gridData);	


	}


	private void startExploring() {

		// start
		this.parentComposite.getDisplay().asyncExec(new Runnable() {
			public void run() {			

				try {
					FileExplorerRecursivTab.this.recursiveFileExplorerWrapper(FileExplorerRecursivTab.this.initialFile);

				} catch (Excel2003MaxRowsException e) {
					logger.severe("EXCEL 2003 Max Rows Exceeded");
					new ShellInformationMessage(FileExplorerRecursivTab.this.parentComposite.getDisplay(),
							FileExplorerRecursivTab.this.parentComposite.getShell(),
							"EXCEL 2003 Max Rows Exceeded");				}
			}
		});

	}

	/**
	 * This method allows to sleep during a number of milliseconds
	 * @param milliseconds
	 */
	private void sleepMilliseconds(final int milliseconds, final File file) {
		this.parentComposite.getDisplay().timerExec (milliseconds, new Runnable () {
			public void run () {

				FileObservable fileObservable = new FileObservable(file);
				fileObservable.addObserver(FileExplorerRecursivTab.this.analysisStatus);
				fileObservable.notifyObservers();
			}
		});
	}

	private void recursiveFileExplorerWrapper (final File file) throws Excel2003MaxRowsException{

		// search sub folders and files
		File[] newFiles = file.listFiles();
		if (this.browsedFiles.size() > 65550) {
			throw new Excel2003MaxRowsException("Number of browsed files exceeding EXCEL 2003 limit of 65550 rows");
		}
		if (newFiles != null) {


			for (int i = 0; i < newFiles.length; i++) {
				// write the data for the current file
				logger.info(newFiles[i].getName() + " --- " + newFiles[i].isDirectory());
				File nextFile = newFiles[i];

				// let the other Thread run...
				sleepMilliseconds(300, nextFile);

				if (nextFile.isDirectory()) {
					// write folder name in Status Bar

					// Recursive search of files from this folder
					recursiveFileExplorerWrapper (newFiles[i]);				
				}
			}
		} else {
			System.out.println("it is finished");
		}
	}

	private void activate() {
		// activate this tab
		this.cTabFolder.setSelection(this);
	}

	private void start() throws Excel2003MaxRowsException {
		RecursiveFileExplorerBase recursiveFileExplorerBase = new RecursiveFileExplorerBase(this.initialFile, this.getDisplay());
		recursiveFileExplorerBase.start();
	}

}
