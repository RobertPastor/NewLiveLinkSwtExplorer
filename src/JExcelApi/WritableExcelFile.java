package JExcelApi;

import java.io.File;

import jxl.write.WritableSheet;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;

import FileExplorerTab.StatusBarObserver;
import JExcelApi.WritableExcelSheet.Excel2003MaxRowException;
import LiveLinkCore.LiveLinkNode;


/**
 * 
 * Folder Browser	V0.0.14 dated 24th January 2010	
 * Date:	11 avr. 2012	14:00:18
 * Initial Folder: 	D:\DataDrill Graph Exporter	
 * Number of Folders: 	0	
 * Number of Files: 	9	
 * Total Bytes size of Files: 	869209	
 * Total Kilo Bytes size of Files: 	848,8369141	

 * @author t0007330
 *
 */
public class WritableExcelFile {


	private WritableExcelWorkbook workbook = null;
	private WritableSheet readMeSheet = null;
	private WritableSheet resultSheet = null;
	private Display display = null;

	public WritableExcelFile(Display display) {
		this.display = display;
	}

	public boolean Create (File parentFile) {
		workbook = new WritableExcelWorkbook("FileExplorer");
		if (workbook.createWritableWorkbook()) {
			readMeSheet = workbook.createWritableSheet("ReadMe");
			WritableExcelSheet writableExcelSheet = new WritableExcelSheet(readMeSheet,this.display);
			writableExcelSheet.writeReadMeSheet(parentFile);
			return true;
		}
		return false;
	}
	
	public boolean writeReadMeSheet (Tree liveLinkNodeTree, boolean fromRootFromSelected) {
		workbook = new WritableExcelWorkbook("LiveLinkExplorer");
		if (workbook.createWritableWorkbook()) {
			readMeSheet = workbook.createWritableSheet("ReadMe");
			WritableExcelSheet writableExcelSheet = new WritableExcelSheet(readMeSheet,this.display);
			writableExcelSheet.writeReadMeSheet(liveLinkNodeTree,fromRootFromSelected);
			return true;
		}
		return false;
	}
	
	public boolean writeReadMeSheet (LiveLinkNode llNode) {
		workbook = new WritableExcelWorkbook("LiveLinkExplorer");
		if (workbook.createWritableWorkbook()) {
			readMeSheet = workbook.createWritableSheet("ReadMe");
			WritableExcelSheet writableExcelSheet = new WritableExcelSheet(readMeSheet, this.display);
			writableExcelSheet.writeReadMeSheet(llNode);
			return true;
		}
		return false;
	}
	
	public boolean WriteFileExplorerRecursive (final File selectedFile, final StatusBarObserver statusBarObserver) throws Excel2003MaxRowException {
		if (workbook != null) {
			resultSheet = workbook.createWritableSheet("File Explorer");
			WritableExcelSheet writableExcelSheet = new WritableExcelSheet(resultSheet,this.display);
			writableExcelSheet.writeFileExplorerRecursive(selectedFile, statusBarObserver);
			return true;
		}
		return false;
	}

	public boolean WriteFileExplorer (final File selectedFile, final StatusBarObserver statusBarObserver) throws Excel2003MaxRowException {

		if (workbook != null) {
			resultSheet = workbook.createWritableSheet("File Explorer");
			WritableExcelSheet writableExcelSheet = new WritableExcelSheet(resultSheet,this.display);
			writableExcelSheet.writeFileExplorer(selectedFile, statusBarObserver);
			return true;
		}
		return false;
	}

	public boolean writeLiveLinkData(Tree liveLinkNodeTree) throws Excel2003MaxRowException {

		if (workbook != null) {
			resultSheet = workbook.createWritableSheet("LiveLink Explorer");
			WritableExcelSheet writableExcelSheet = new WritableExcelSheet(resultSheet,this.display);
			writableExcelSheet.writeLiveLinkData(liveLinkNodeTree);
			return true;
		}
		return false;
	}

	public boolean generateLiveLinkTreeRecursiveIndex(Tree liveLinkTree) throws Excel2003MaxRowException {
		
		if (workbook != null) {
			resultSheet = workbook.createWritableSheet("LiveLink Explorer");
			WritableExcelSheet writableExcelSheet = new WritableExcelSheet(resultSheet, this.display);
			writableExcelSheet.writeRecursiveLiveLinkData(liveLinkTree);

			return true;
		}
		return false;
	}
	
	public boolean generateLiveLinkTreeIndentedIndex(Tree liveLinkTree) throws Excel2003MaxRowException {
		if (workbook != null) {
			resultSheet = workbook.createWritableSheet("LiveLink Explorer");
			WritableExcelSheet writableExcelSheet = new WritableExcelSheet(resultSheet, this.display);
			writableExcelSheet.generateLiveLinkTreeIndex(liveLinkTree);

			return true;
		}
		return false;
	}

	public void Close() {
		this.workbook.close();
	}

	public String getExcelFilePath() {
		return this.workbook.getExcelFilePath();
	}

	public boolean generateLiveLinkNodeRecursiveIndex(LiveLinkNode rootNode, Boolean indentedEXCELresults) throws Excel2003MaxRowException {
		if (workbook != null) {
			resultSheet = workbook.createWritableSheet("LiveLink Explorer");
			WritableExcelSheet writableExcelSheet = new WritableExcelSheet(resultSheet, this.display);
			writableExcelSheet.generateLiveLinkNodeRecursiveIndex(rootNode, true, indentedEXCELresults, 0);

			return true;
		}
		return false;
	}

}
