package FileExplorerTab;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import JExcelApi.Excel2003MaxRowsException;
import LiveLinkCore.ShellInformationMessage;

public class FileExplorerRecursivThread extends Thread {

	private static final Logger logger = Logger.getLogger(FileExplorerRecursivTab.class.getName()); 


	private Composite parentComposite = null;
	private Display display = null;
	private File initialFile = null;
	private StatusBarObserver analysisStatus = null;
	private Text locationText = null;

	private ArrayList<File> browsedFiles = null;


	public FileExplorerRecursivThread( final Composite _parentComposite, 
			final Display _display, final File _initialFile, final Text _locationText, final StatusBarObserver _analysisStatus) {

		display= _display;
		this.parentComposite = _parentComposite;
		this.initialFile = _initialFile;
		this.locationText = _locationText;
		this.analysisStatus = _analysisStatus;
		this.browsedFiles = new ArrayList<>();
	}

	/**
	 * This method allows to sleep during a number of milliseconds
	 * @param milliseconds
	 */
	private void sleepMilliseconds(final int milliseconds, final File file) {
		this.parentComposite.getDisplay().timerExec (milliseconds, new Runnable () {
			public void run () {

				FileObservable fileObservable = new FileObservable(file);
				fileObservable.addObserver(FileExplorerRecursivThread.this.analysisStatus);
				fileObservable.notifyObservers();
			}
		});
	}

	private void recursiveFileExplorerWrapper (final File file) throws Excel2003MaxRowsException, IOException{

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
				//sleepMilliseconds(300, nextFile);
				doUpdate(nextFile.getCanonicalPath());

				if (nextFile.isDirectory()) {
					// write folder name in Status Bar

					// Recursive search of files from this folder
					recursiveFileExplorerWrapper (newFiles[i]);				
				}
			}
		} else {
			logger.info("it is finished");
		}
	}

	private void startExploring(final File _initialFile) {

		// start
		this.parentComposite.getDisplay().asyncExec(new Runnable() {
			public void run() {			

				try {
					FileExplorerRecursivThread.this.recursiveFileExplorerWrapper(_initialFile);

				} catch (Excel2003MaxRowsException e) {
					logger.severe("EXCEL 2003 Max Rows Exceeded");
					new ShellInformationMessage(FileExplorerRecursivThread.this.parentComposite.getDisplay(),
							FileExplorerRecursivThread.this.parentComposite.getShell(),
							"EXCEL 2003 Max Rows Exceeded");				
				} catch (IOException e2) {
					new ShellInformationMessage(FileExplorerRecursivThread.this.parentComposite.getDisplay(),
							FileExplorerRecursivThread.this.parentComposite.getShell(),
							e2.getLocalizedMessage());	
				}
			}
		});

	}

	@Override
	public void start() {
		startExploring(this.initialFile);
	}


	public void doUpdate( final String value) {

		this.display.syncExec( new Runnable() {
			@Override
			public void run() {
				logger.info("--------"+ value + "----------");
				if (!locationText.isDisposed()) {
					locationText.setText(value);
					locationText.getParent().layout();
				}
			}
		});
	}
}
