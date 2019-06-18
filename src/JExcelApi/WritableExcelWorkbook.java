package JExcelApi;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.filechooser.FileSystemView;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;

public class WritableExcelWorkbook {

	private static final Logger logger = Logger.getLogger(WritableExcelWorkbook.class.getName()); 

	private String ExcelFilePath = "";
	private jxl.write.WritableWorkbook workbook = null;
	private java.io.File file = null;
	
	private String getCurrentDateTime() {

		Calendar cal = Calendar.getInstance(TimeZone.getDefault()); 
		String DATE_FORMAT = "dd_MMM_yyyy_HH_mm_ss"; 
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
		sdf.setTimeZone(TimeZone.getDefault()); 
		String currentTime = sdf.format(cal.getTime());
		return currentTime;
	}

	/**
	 * Constructor: takes as input the file path where the EXCEL file will be created
	 * @param excelFilePath
	 */
	public WritableExcelWorkbook(File f) {
		if (f != null && f.exists()) {
			this.file = f;
			this.ExcelFilePath = f.getAbsolutePath();
			this.workbook = helper(f);
		}
	}
	
	/**
	 * Constructor
	 */
	public WritableExcelWorkbook(String fileStart) {
		
		logger.log(Level.INFO,"System Get User Home property: "+System.getProperty("user.home"));
		FileSystemView fsv = FileSystemView.getFileSystemView();
		logger.log(Level.INFO,"Home Directory: "+fsv.getHomeDirectory());

		this.ExcelFilePath = fsv.getHomeDirectory()+"\\"+fileStart+"_"+getCurrentDateTime()+".xls";
		logger.log(Level.INFO,this.ExcelFilePath);
		
		// modification 29th November 2012
		//String filePath = "d:/"+fileStart+"_"+getCurrentDateTime()+".xls";
		//logger.log(Level.INFO,"file: "+filePath);
		//this.ExcelFilePath = filePath;
	}
	
	/**
	 * Create a writable EXCEL sheet in the writable EXCEL workbook
	 * @param SheetName
	 * @return the writableSheet
	 */
	public jxl.write.WritableSheet createWritableSheet(String SheetName) {

		if (this.workbook != null) {
			int index = this.workbook.getNumberOfSheets();
			//System.out.println("WritableExcelSheet: number of sheets: "+index);
			return this.workbook.createSheet(SheetName, index);
		}
		return null;
	}

	/**
	 * 
	 * @return true if the workbook is created correctly
	 */
	public boolean createWritableWorkbook() {

		if (this.ExcelFilePath.length() > 0) {
			this.file = new File(this.ExcelFilePath);
			if (this.file.exists() == false) {
				try {
					WorkbookSettings workbookSettings = new WorkbookSettings();
					workbookSettings.setUseTemporaryFileDuringWrite(true);
					workbookSettings.setSuppressWarnings(true);

					this.workbook = Workbook.createWorkbook(this.file,workbookSettings);
					return true;
				}
				catch (IOException e) {
					System.err.println("WritableExcelWorkbook: "+e.getMessage());
					return false;
				}
			}
		}
		return false;
	}
	
	private jxl.write.WritableWorkbook helper(File file) {
		
		if (this.file.exists() == true) {
			try {
				WorkbookSettings workbookSettings = new WorkbookSettings();
				workbookSettings.setSuppressWarnings(true);

				//this.workbook = Workbook.createWorkbook(this.file);
				jxl.Workbook aReadableWorkbook = Workbook.getWorkbook(this.file);
				this.workbook = Workbook.createWorkbook(this.file, aReadableWorkbook); 
				return this.workbook;
			}
			catch (BiffException e1) {
				System.err.println("WritableExcelWorkbook: "+e1.getMessage());
				return null;
			}
			catch (IOException e2) {
				System.err.println("WritableExcelWorkbook: "+e2.getMessage());
				return null;
			}
		}
		return null;
	}
	
	/**
	 * gets the workbook that has been created
	 * @return a WritableWorkbook
	 */
	public jxl.write.WritableWorkbook getWritableWorkbook() {
		if (this.ExcelFilePath.length() > 0) {
			this.file = new File(this.ExcelFilePath);
			return helper(file);
		}
		return null;
	}

	public String getExcelFilePath() {
		return ExcelFilePath;
	}

	public void setExcelFilePath(String excelFilePath) {
		ExcelFilePath = excelFilePath;
	}

	public jxl.write.WritableWorkbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(jxl.write.WritableWorkbook workbook) {
		this.workbook = workbook;
	}

	public java.io.File getFile() {
		return file;
	}

	public void setFile(java.io.File file) {
		this.file = file;
	}
	
	public void write() {
		try {
			this.workbook.write();
		}
		catch (IOException e2) {
			System.err.println("WritableExcelWorkbook: "+e2.getMessage());
		}
	}
	
	public void close() {
		try {
			this.workbook.write();
			this.workbook.close();
		}
		catch (WriteException e1) {
			System.err.println("WritableExcelWorkbook: "+e1.getMessage());
		}
		catch (IOException e2) {
			System.err.println("WritableExcelWorkbook: "+e2.getMessage());
		}
	}


	
}
