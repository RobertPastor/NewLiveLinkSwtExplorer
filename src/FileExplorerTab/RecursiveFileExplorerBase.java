package FileExplorerTab;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;

import JExcelApi.Excel2003MaxRowsException;
import JExcelApi.WritableExcelSheet.Excel2003MaxRowException;


public class RecursiveFileExplorerBase {

	private File initialFile = null;
	private Display display = null;
	private ArrayList<File> browsedFiles = null;
	
	public RecursiveFileExplorerBase(File initialFile, Display display) {
		this.initialFile = initialFile;
		this.display = display;
		this.browsedFiles = new ArrayList<>();
	}
	
	private void recursiveFileExplorerWrapper (final File file) throws Excel2003MaxRowsException{

		// search sub folders and files
		File[] newFiles = file.listFiles();
		if (this.browsedFiles.size() > 65550) {
			throw new Excel2003MaxRowsException("Number of browsed files exceeding EXCEL 2003 limit of 65550 rows");
		}
		if (newFiles != null) {

			// let the other Thread run...
			sleepMilliseconds(300);

			for (int i = 0; i < newFiles.length; i++) {
				// write the data for the current file
				System.out.println(newFiles[i].getName() + " --- " + newFiles[i].isDirectory());
				File nextfile = newFiles[i];
				FileObservable fileObservable = new FileObservable(nextfile);

				if (nextfile.isDirectory()) {
					// write folder name in Status Bar

					// Recursive search of files from this folder
					recursiveFileExplorerWrapper (newFiles[i]);				
				}
			}
		} else {
			System.out.println("it is finished");
		}
	}
	
	/**
	 * This method allows to sleep during a number of milliseconds
	 * @param milliseconds
	 */
	private void sleepMilliseconds(int milliseconds) {
		this.display.timerExec (milliseconds, new Runnable () {
			public void run () {
				//System.out.println ("100 milliseconds");
			}
		});
	}

	public void start() throws Excel2003MaxRowsException {
		recursiveFileExplorerWrapper(this.initialFile);
		
	}
	
	
}
