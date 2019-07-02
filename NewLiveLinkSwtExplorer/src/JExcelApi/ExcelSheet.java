package JExcelApi;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class ExcelSheet {

	protected String SheetName = "";
	protected String ExcelFilePath = "";
	protected jxl.write.WritableWorkbook workbook = null;
	protected jxl.write.WritableSheet writableSheet = null;

	public ExcelSheet (String excelFilePath,String aSheetName) {
		this.SheetName = aSheetName;
		this.ExcelFilePath = excelFilePath;
	}

	public jxl.Sheet getSheet (String aSheetName) {

		this.SheetName = aSheetName;
		if (this.ExcelFilePath.length() > 0) {
			java.io.File file = new File(this.ExcelFilePath);
			if (file.exists()) {
				try {
					WorkbookSettings workbookSettings = new WorkbookSettings();
					workbookSettings.setSuppressWarnings(true);
					Workbook workbook = Workbook.getWorkbook(file,workbookSettings);		
					Sheet sheet = workbook.getSheet(this.SheetName);
					if (sheet != null) {
						return sheet;
					}
				}
				catch (IOException e1) {
					System.out.println(e1.getMessage());
				}
				catch (BiffException e2) {
					System.out.println(e2.getMessage());
				}
			}
			else {
				System.err.println("Excel Sheet: file does not exist "+this.ExcelFilePath);
			}
		}
		else {
			System.err.println("Excel Sheet: empty file path!");
		}
		return null;
	}
	

	public long getLongCellValue (jxl.Sheet sheet,int ColumnIndex, int RowIndex) {

		long longValue = 0;
		Cell cell = null;
		CellType cellType = null;

		cell = sheet.getCell(ColumnIndex,RowIndex);
		cellType = cell.getType();

		if (cellType != CellType.EMPTY) {
			if ( CellType.NUMBER.toString().equalsIgnoreCase(cellType.toString())) {
				
				longValue = Long.parseLong(cell.getContents());
				System.out.println("Excel Sheet: long Value: "+ longValue);
			}
		}
		else {
			System.err.println("---------------------");
			System.err.println("Excel Sheet: Empty Cell!");
			System.err.println("Excel Sheet: error in cell: Row: "+RowIndex+" Column: "+ColumnIndex);
			System.err.println("---------------------");
			System.err.flush();
		}
		return longValue;
	}

	public String getStringCellValue (jxl.Sheet sheet,int ColumnIndex, int RowIndex) {

		String strValue = "";
		Cell cell = null;
		CellType cellType = null;

		cell = sheet.getCell(ColumnIndex,RowIndex);
		cellType = cell.getType();

		if (cellType != CellType.EMPTY) {
			if ( CellType.LABEL.toString().equalsIgnoreCase(cellType.toString())) {
				strValue = cell.getContents();
				System.out.println("Excel Sheet: String value: "+strValue);
			}
			else {
				System.err.println("---------------------");
				System.err.println("Excel Sheet: Cell has not expected LABEL format (not a String)!");
				System.err.println("Excel Sheet: Error in cell: Row: "+RowIndex+" Column: "+ColumnIndex);
				System.err.println("---------------------");
				System.err.flush();
			}
		}
		else {
			System.err.println("---------------------");
			System.err.println("Excel Sheet: Empty Cell!");
			System.err.println("Excel Sheet: error in cell: Row: "+RowIndex+" Column: "+ColumnIndex);
			System.err.println("---------------------");
			System.err.flush();

		}
		return strValue;
	}

	public boolean SheetExists(String aSheetName) {

		this.SheetName = aSheetName;
		if (this.ExcelFilePath.length() > 0) {
			java.io.File file = new File(this.ExcelFilePath);
			if (file.exists()) {
				try {
					WorkbookSettings workbookSettings = new WorkbookSettings();
					workbookSettings.setSuppressWarnings(true);
					Workbook workbook = Workbook.getWorkbook(file,workbookSettings);		
					Sheet sheet = workbook.getSheet(this.SheetName);
					if (sheet != null) {
						return true;
					}
				}
				catch (IOException e1) {
					System.out.println("=====================");
					System.out.println(e1.getMessage());
					System.out.println("=====================");

				}
				catch (BiffException e2) {
					System.out.println("=====================");

					System.out.println(e2.getMessage());
					System.out.println("=====================");

				}
			}
			else {
				System.err.println("Excel Sheet: file does not exist "+this.ExcelFilePath);
			}
		}
		else {
			System.err.println("Excel Sheet: empty file path!");
		}
		return false;
	}

}
