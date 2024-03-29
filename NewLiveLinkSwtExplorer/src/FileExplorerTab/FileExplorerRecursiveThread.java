package FileExplorerTab;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;

import JExcelApi.Excel2003MaxRowsException;
import JExcelApi.WritableExcelFile;
import LiveLinkCore.ShellInformationMessage;

public class FileExplorerRecursiveThread extends Thread {

	private static final Logger logger = Logger.getLogger(FileExplorerRecursiveTab.class.getName()); 


	private final static int maxRowsExcel2003 = 1000;
	private Composite parentComposite = null;
	private Display display = null;
	private File initialFile = null;

	private StatusBarObserver analysisStatus = null;
	private Text locationText = null;
	private ProgressBar progressBar = null;
	private Text filesCountText = null;

	private ArrayList<File> browsedFiles = null;


	public FileExplorerRecursiveThread( final Composite _parentComposite, 
			final Display _display, final File _initialFile, final Text _locationText, 
			final StatusBarObserver _analysisStatus, 
			final ProgressBar _progressBar, final Text _filesCountText) {

		super();

		display= _display;
		this.parentComposite = _parentComposite;
		this.initialFile = _initialFile;
		this.locationText = _locationText;
		this.analysisStatus = _analysisStatus;
		this.progressBar = _progressBar;
		this.filesCountText = _filesCountText;

		this.browsedFiles = new ArrayList<>();
		// initial add
		this.browsedFiles.add(this.initialFile);
	}

	@Override
	public void run() {
		// run is called by thread start
		try {
			// =====================================
			// launch recursive file explorer
			// only way to leave the excursion is using Exception

			recursiveFileExplorerWrapper (this.initialFile);

		} catch (Excel2003MaxRowsException | IOException e) {
			logger.severe(e.getLocalizedMessage());

			this.display.asyncExec( new Runnable() {
				@Override
				public void run() {

					final String warning = "Number of browsed files exceeding max rows limit= " + (maxRowsExcel2003);

					new ShellInformationMessage(FileExplorerRecursiveThread.this.parentComposite.getDisplay(),
							FileExplorerRecursiveThread.this.parentComposite.getShell(),
							warning);
					// writing file with the above mentioned limits
					writeExcelFile();
				}
			});
		} catch ( EndOfRecursionException ex ) {
			writeExcelFile();
		}

	}

	/**
	 * This method allows to sleep during a number of milliseconds
	 * @param milliseconds
	 */
	private void sleepMilliseconds(final int milliseconds, final File file) {
		this.parentComposite.getDisplay().timerExec (milliseconds, new Runnable () {
			public void run () {

				FileObservable fileObservable = new FileObservable(file);
				fileObservable.addObserver(FileExplorerRecursiveThread.this.analysisStatus);
				fileObservable.notifyObservers();
			}
		});
	}


	private void writeExcelFile() {

		logger.info("write Excel File");
		WritableExcelFile writableExcelFile = new WritableExcelFile(this.parentComposite.getDisplay());
		if (writableExcelFile.create(this.initialFile)) {
			try {
				if ( writableExcelFile.writeFileExplorer(this.initialFile, this.analysisStatus, this.browsedFiles) ) {

					// creation is OK
					writableExcelFile.Close();

					final String excelFilePath = writableExcelFile.getExcelFilePath();
					logger.info(excelFilePath);

					this.display.asyncExec( new Runnable() {
						@Override
						public void run() {

							new ShellInformationMessage(FileExplorerRecursiveThread.this.parentComposite.getDisplay(),
									FileExplorerRecursiveThread.this.parentComposite.getShell(),excelFilePath);	
						}
					});
				}
			}
			catch (Excel2003MaxRowsException ex) {
				new ShellInformationMessage(this.parentComposite.getDisplay(),
						this.parentComposite.getShell(),
						"Error - EXCEL 2003 - row index exceeds max rows number");	
			}
		}	
	}

	/**
	 * recursive explorer => only one way to leave the recursion... throw exception
	 * @param file
	 * @throws Excel2003MaxRowsException
	 * @throws IOException
	 */
	private void recursiveFileExplorerWrapper (final File file) throws Excel2003MaxRowsException, EndOfRecursionException, IOException{

		// search sub folders and files
		File[] newFiles = file.listFiles();
		if (this.browsedFiles.size() > maxRowsExcel2003) {

			final String warning = "Number of browsed files exceeding max rows limit= " + (maxRowsExcel2003);
			logger.info(warning);
			throw new Excel2003MaxRowsException(warning);

		} else {
			if (newFiles != null) {

				for (int i = 0; i < newFiles.length; i++) {

					/**
					 *  write the data for the current file
					 *  logger.info(newFiles[i].getName() + " --- " + newFiles[i].isDirectory());
					 */

					File nextFile = newFiles[i];

					this.browsedFiles.add(nextFile);

					// let the other Thread update the GUI...
					doUpdate(nextFile.getCanonicalPath(), this.browsedFiles.size());

					if (nextFile.isDirectory()) {
						// write folder name in Status Bar

						// Recursive search of files from this folder
						recursiveFileExplorerWrapper (newFiles[i]);				
					} 
				}
			} else {
				String message = "it is finished - size= "+ this.browsedFiles.size();
				logger.info(message);
				//writeExcelFile();

				throw new EndOfRecursionException(message);

			}
		}
	}


	public void doUpdate( final String value, final int count) {

		this.display.asyncExec( new Runnable() {
			@Override
			public void run() {

				//logger.info("--------"+ value + "----------");
				if (! FileExplorerRecursiveThread.this.analysisStatus.isDisposed()) {
					FileExplorerRecursiveThread.this.analysisStatus.setText(value);
					FileExplorerRecursiveThread.this.analysisStatus.getParent().layout();
				}
				if (! FileExplorerRecursiveThread.this.filesCountText.isDisposed()) {
					FileExplorerRecursiveThread.this.filesCountText.setText(String.valueOf( count ));
					FileExplorerRecursiveThread.this.filesCountText.getParent().layout();
				}
			}
		});
	}
}
