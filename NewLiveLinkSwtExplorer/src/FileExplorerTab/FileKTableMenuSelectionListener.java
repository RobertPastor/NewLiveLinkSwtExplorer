package FileExplorerTab;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;

public class FileKTableMenuSelectionListener implements SelectionListener {

	private static final Logger logger = Logger.getLogger(FileKTableMenuSelectionListener.class.getName());

	private Composite parent = null;
	private FileKTableSortedModel fileKTableModel = null;

	private int modelRow = 0;
	private File root = null;

	private CTabFolder tabFolder = null;

	public ProgressBar getqPcpAnalysisProgressBar() {
		return qPcpAnalysisProgressBar;
	}

	public CTabFolder getTabFolder() {
		return tabFolder;
	}

	private ProgressBar qPcpAnalysisProgressBar = null;

	public FileKTableMenuSelectionListener(Composite parent, 
			FileKTableSortedModel fileKTableModel , 
			File root, 
			CTabFolder tabFolder, 
			int modelRow ,
			ProgressBar qPcpAnalysisProgressBar
			) {

		super();
		this.setParent ( parent );
		this.fileKTableModel = fileKTableModel;
		this.modelRow = modelRow;
		this.root = root;
		this.tabFolder = tabFolder;
		this.qPcpAnalysisProgressBar  = qPcpAnalysisProgressBar;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {

		
		BusyIndicator.showWhile(this.parent.getDisplay() , new Runnable(){

		    public void run(){
		    	
		    	try {

					String selectedFileName =  fileKTableModel.getFiles()[modelRow-1].getName();
					logger.log(Level.INFO , selectedFileName ) ;

					String filePath = fileKTableModel.getFiles()[modelRow-1].getAbsolutePath();
					logger.log(Level.INFO, " file path = " + filePath);
					
					

				} catch (Exception ex) {
					logger.log(Level.SEVERE , ex.getLocalizedMessage());
				}
		    	
		    }
		    });
		
	}


	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	public FileKTableSortedModel getFileKTableModel() {
		return this.fileKTableModel;
	}

	public void setFileKTableModel(FileKTableSortedModel fileKTableModel) {
		this.fileKTableModel = fileKTableModel;
	}

	public Composite getParent() {
		return this.parent;
	}

	private void setParent(Composite parent) {
		this.parent = parent;
	}
	
	public File getRoot() {
		return this.root;
	}

	public void setRoot(File root) {
		this.root = root;
	}

}
