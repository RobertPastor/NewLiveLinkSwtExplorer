package FileExplorerTab;

import java.io.File;

import org.eclipse.swt.widgets.Display;

import JExcelApi.WritableExcelSheet;
import JExcelApi.WritableExcelSheet.Excel2003MaxRowException;

public class RecursiveFileExplorer {

	private File initialFile;
	private Display display;
	private WritableExcelSheet writableExcelSheet;
	
	public RecursiveFileExplorer(File initialFile, Display display, WritableExcelSheet writableExcelSheet) {
		this.initialFile = initialFile;
		this.display = display;
		this.writableExcelSheet = writableExcelSheet;
	}
	
	private void recursiveFileExplorerWrapper (File initialFile) throws Excel2003MaxRowException{

		// search sub folders and files
		File[] newFiles = initialFile.listFiles();
		if (newFiles != null) {

			// let the other Thread run...
			sleepMilliseconds(300);

			for (int i = 0; i < newFiles.length; i++) {
				// write the data for the current file
				//System.out.println(newFiles[i].getName() + " --- " + newFiles[i].isDirectory());
				File file = newFiles[i];
				FileObservable fileObservable = new FileObservable(file);
				this.writableExcelSheet.writeFileData(file);
				if (newFiles[i].isDirectory()) {
					// write folder name in Status Bar

					// Recursive search of files from this folder
					recursiveFileExplorerWrapper (newFiles[i]);				
				}
			}
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
	
	
}
