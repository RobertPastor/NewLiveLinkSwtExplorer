package JExcelApi;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jxl.Hyperlink;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import DownLoadTab.HyperLinkSet;


public class ReadableWorkbook {

	private static final Logger logger = Logger.getLogger(ReadableWorkbook.class.getName()); 

	private Workbook workbook = null;
	private File file = null;
	private String filePath = "";

	public String getFilePath() {
		return filePath;
	}

	public ReadableWorkbook(String filePath) {

		logger.log(Level.INFO,"filePath: "+filePath);
		this.filePath = filePath;
		this.file = new File(filePath);	

	}

	public ReadableWorkbook (File file) {
		logger.log(Level.INFO,file.getAbsolutePath());
		if (file.exists()) {
			this.file = file;
		}
	}

	public boolean open() {
		logger.log(Level.INFO,file.getAbsolutePath());
		if (this.file.exists()) {
			try {
				WorkbookSettings workbookSettings = new WorkbookSettings();
				this.workbook = Workbook.getWorkbook(file,workbookSettings);
				logger.log(Level.INFO,file.getAbsolutePath());
				return true;

			} catch (BiffException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void close () {

		logger.log(Level.INFO,file.getAbsolutePath());
		if (this.workbook != null) {
			this.workbook.close();
		}
	}

	public String[] getSheetNames() {
		if (this.workbook != null) {
			return this.workbook.getSheetNames();
		}
		return null;
	}

	public void getHyperLinks(String[] selectedSheetNames, HyperLinkSet hyperLinkSet) {
		logger.log(Level.INFO,file.getAbsolutePath());
		if (this.workbook != null) {

			for (String strSelectedSheetName : selectedSheetNames) {
				logger.log(Level.INFO,"selected sheet Name: "+strSelectedSheetName);
				Sheet sheet = this.workbook.getSheet(strSelectedSheetName);
				if (sheet != null) {

					logger.log(Level.INFO,"selected sheet Name equals a sheet: "+sheet.getName());
					Hyperlink[] hyperLinks = sheet.getHyperlinks();
					logger.log(Level.INFO,"number of hyperlinks found: "+hyperLinks.length);
					for (Hyperlink hyperLink : hyperLinks) {
						hyperLinkSet.add(hyperLink);
					}
				}
			}

		}
	}

}
