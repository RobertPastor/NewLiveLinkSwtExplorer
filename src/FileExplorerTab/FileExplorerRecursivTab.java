package FileExplorerTab;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;

import JExcelApi.Excel2003MaxRowsException;

public class FileExplorerRecursivTab extends CTabItem {

	private static final Logger logger = Logger.getLogger(FileExplorerRecursivTab.class.getName()); 

	private Composite parentComposite = null;
	private CTabFolder cTabFolder = null;
	private Composite contentPanel = null;
	private File initialFile = null;
	
	private StatusBarObserver analysisStatus = null;
	private ProgressBar analysisProgressBar = null;


	public FileExplorerRecursivTab(final Composite comp, final CTabFolder cTabFolder,  final File initialFile) {
		super(cTabFolder, SWT.NULL);

		this.parentComposite = comp;
		this.cTabFolder = cTabFolder;
		this.initialFile = initialFile;
		this.setText ("Recursiv File Explorer");

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
			initStatusBarAndProgressComposite();
		} catch (Excel2003MaxRowsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		
		// activate
		this.activate();
		// start
		this.start();
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
