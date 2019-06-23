package JExcelApi;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;

import jxl.CellView;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableImage;
import jxl.write.WriteException;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import FileExplorerTab.FileObservable;
import FileExplorerTab.StatusBarObserver;
import LiveLinkCore.LiveLinkNode;
import LiveLinkCore.LiveLinkURLObservable;
import LiveLinkCore.ShellErrorMessage;
import ToolVersion.ToolVersion;


public class WritableExcelSheet  {

	

	private static final Logger logger = Logger.getLogger(WritableExcelSheet.class.getName());

	private static final double CELL_DEFAULT_HEIGHT = 17;
	private static final double CELL_DEFAULT_WIDTH = 64;

	protected jxl.write.WritableSheet sheet = null;
	private int RowIndex = 0;
	// EXCEL 2003 - max number of rows
	private final int MaxNumberOfRows = 65536 ;

	private LiveLinkNodeTreeSet liveLinkNodeTreeSet = null;

	private DateFormat customDateFormat = new DateFormat ("dd-MMM-yyyy hh:mm:ss");
	private WritableFont arial8BoldFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD);
	private WritableFont arial8NormalFont = new WritableFont(WritableFont.ARIAL, 8);
	private WritableFont arial8ItalicBlueFont = new WritableFont(WritableFont.ARIAL, 8, WritableFont.NO_BOLD, true,UnderlineStyle.SINGLE , Colour.BLUE);

	private Display display = null;
	private LiveLinkNode rootNode = null;

	public WritableExcelSheet(jxl.write.WritableSheet writableSheet, Display display) {
		this.sheet = writableSheet;
		this.display = display;
		this.RowIndex = 0;
	}

	public boolean writeStringValue (int columnIndex, int rowIndex, String strValue, boolean bold) {

		try {
			WritableFont arial8font = null;
			if (bold) {
				arial8font = arial8BoldFont;
			}
			else {
				arial8font = arial8NormalFont;
			} 
			WritableCellFormat arial8format = new WritableCellFormat (arial8font);
			arial8format.setBorder(Border.ALL, BorderLineStyle.THIN);
			if (bold) {
				arial8format.setBackground(Colour.YELLOW);
			}
			jxl.write.Label cell = new jxl.write.Label(columnIndex, rowIndex, strValue, arial8format);

			this.sheet.addCell(cell);
			return true;
		}
		catch (WriteException e1) {
			System.err.println("WritableSheet: "+e1.getMessage());
			return false;
		}
	}

	public boolean writeLongValue (int ColumnIndex, int RowIndex, long longValue, boolean bold) {

		try {
			WritableFont arial8font = null;
			if (bold) {
				arial8font = arial8BoldFont;
			}
			else {
				arial8font = arial8NormalFont;
			}
			WritableCellFormat arial8format = new WritableCellFormat (arial8font);
			arial8format.setBorder(Border.ALL, BorderLineStyle.THIN);
			if (bold) {
				arial8format.setBackground(Colour.YELLOW);
			}
			jxl.write.Number cell = new jxl.write.Number(ColumnIndex, RowIndex, longValue ,arial8format);
			this.sheet.addCell(cell);
			return true;
		}
		catch (WriteException e1) {
			System.err.println("WritableSheet: "+e1.getMessage());
			return false;
		}
	}

	public boolean writeTimeValue (int ColumnIndex, int RowIndex, Date dateValue, boolean bold) {

		try {
			WritableFont arial8font = null;
			if (bold==true) {
				arial8font = arial8BoldFont;
			}
			else {
				arial8font = arial8NormalFont;
			}
			WritableCellFormat arial8format = new WritableCellFormat (arial8font);
			arial8format.setBorder(Border.ALL, BorderLineStyle.THIN);
			if (bold==true) {
				arial8format.setBackground(Colour.YELLOW);
			}
			DateFormat customTimeFormat = new jxl.write.DateFormat ("HH-mm-ss");
			WritableCellFormat dateFormat = new WritableCellFormat (arial8font,customTimeFormat);
			dateFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

			DateTime dateCell = new DateTime(ColumnIndex, RowIndex, dateValue, dateFormat);

			this.sheet.addCell(dateCell);
			return true;
		}
		catch (WriteException e1) {
			System.err.println("WritableSheet: "+e1.getMessage());
			return false;
		}
	}

	public boolean writeDateValue (int ColumnIndex, int RowIndex, Date dateValue, boolean bold) {

		try {
			WritableFont arial8font = null;
			if (bold==true) {
				arial8font = arial8BoldFont;
			}
			else {
				arial8font = arial8NormalFont;
			}
			WritableCellFormat arial8format = new WritableCellFormat (arial8font);
			arial8format.setBorder(Border.ALL, BorderLineStyle.THIN);
			if (bold==true) {
				arial8format.setBackground(Colour.YELLOW);
			}

			WritableCellFormat dateCellFormat = new WritableCellFormat (arial8font,customDateFormat);
			dateCellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);

			DateTime dateCell = new DateTime(ColumnIndex, RowIndex, dateValue, dateCellFormat);

			this.sheet.addCell(dateCell);
			return true;
		}
		catch (WriteException e1) {
			System.err.println("WritableSheet: "+e1.getMessage());
			return false;
		}
	}


	public boolean writeHyperLink (int columnIndex, int rowIndex, URL url, boolean bold,String description) {

		try {
			WritableFont arial8font = null;
			if (bold == true) {
				arial8font = arial8BoldFont;
			}
			else {
				arial8font = arial8ItalicBlueFont;
			}
			WritableCellFormat arial8format = new WritableCellFormat (arial8font);

			WritableHyperlink hyperLinkCell = new WritableHyperlink(columnIndex, rowIndex, url);
			hyperLinkCell.setDescription(description);

			arial8format.setBorder(Border.ALL, BorderLineStyle.THIN);
			if (bold==true) {
				arial8format.setBackground(Colour.YELLOW);
			}

			this.sheet.addHyperlink(hyperLinkCell);

			Label label = new Label(columnIndex, rowIndex, description, arial8format);
			this.sheet.addCell(label);

			return true;
		}
		catch (WriteException e1) {
			System.err.println("WritableSheet: "+e1.getMessage());
			return false;
		}
	}

	/**
	 * Write the header of Node in the output EXCEL report
	 * @param llNode
	 * @param maxDepth 
	 * @param indentedEXCELresults 
	 */
	private void writeLiveLinkNodeHeader(LiveLinkNode llNode, boolean indentedEXCELresults, int maxDepth) {

		int ColumnIndex = 0;
		// write the header of the table
		writeStringValue(ColumnIndex++, RowIndex,"#",true);
		writeStringValue(ColumnIndex++, RowIndex,"parent",true);
		writeStringValue(ColumnIndex++, RowIndex, llNode.getNameAttribute(),true);
		if (indentedEXCELresults) {
			while (ColumnIndex < (maxDepth + 2)) {
				writeStringValue(ColumnIndex++, RowIndex, llNode.getNameAttribute(),true);
			}
			ColumnIndex = maxDepth + 2;
		}
		writeStringValue(ColumnIndex++, RowIndex, llNode.getCreatedDateAttribute(),true);
		writeStringValue(ColumnIndex++, RowIndex, llNode.getModifiedDateAttribute(),true);
		writeStringValue(ColumnIndex++, RowIndex, llNode.getObjectNameAttribute(),true);
		writeStringValue(ColumnIndex++, RowIndex, llNode.getDescriptionAttribute(),true);
		writeStringValue(ColumnIndex++, RowIndex, llNode.getMimeTypeAttribute(),true);
		writeStringValue(ColumnIndex++, RowIndex, llNode.getSizeAttribute(),true);
		writeStringValue(ColumnIndex++, RowIndex, llNode.getNodeIdAttribute(),true);
		writeStringValue(ColumnIndex++, RowIndex, llNode.getParentIdAttribute(),true);
	}


	private int writeLiveLinkNodeData(LiveLinkNode llNode, String parentNode, Boolean indentedEXCELresults, int maxDepth) {

		int ColumnIndex = 0;
		// write the item counter
		writeLongValue(ColumnIndex++, RowIndex, RowIndex, false);
		// write the name of the initial item = parent of the node list

		if (indentedEXCELresults) {
			ColumnIndex = llNode.getDepth()+1;
		}
		//writeStringValue(ColumnIndex++, RowIndex, parentNode, false);
		writeStringValue(ColumnIndex++, RowIndex, llNode.getName(), false);


		// these information are always written at the maxDepth + 2
		if (indentedEXCELresults) {
			ColumnIndex = maxDepth + 2;
		} 
		if (llNode.getNodeCreationDate() != null){
			writeDateValue(ColumnIndex++, RowIndex, llNode.getNodeCreationDate().getDate(), false);
		}
		else {
			writeStringValue(ColumnIndex++, RowIndex, "", false);
		}

		//writeStringValue(ColumnIndex++,RowIndex,llSubNode.getModifiedDate(),false);
		if (llNode.getNodeModificationDate() != null) {
			writeDateValue(ColumnIndex++, RowIndex, llNode.getNodeModificationDate().getDate(), false);
		}
		else {
			writeStringValue(ColumnIndex++,RowIndex,"",false);
		}

		writeStringValue(ColumnIndex++, RowIndex, llNode.getObjectName(),false);
		writeStringValue(ColumnIndex++, RowIndex, llNode.getDescription(),false);
		writeStringValue(ColumnIndex++, RowIndex, llNode.getMimeType(),false);
		writeLongValue(ColumnIndex++, RowIndex, llNode.getLongSize(),false);

		writeStringValue(ColumnIndex, RowIndex, llNode.getId(), false);
		LiveLinkURLObservable llurla = new LiveLinkURLObservable(llNode.getId());
		if (llNode.isDocument()) {
			writeHyperLink(ColumnIndex++, RowIndex,llurla.getDownloadURL(), false, llNode.getName());
		}
		else if (llNode.isFolder() || llNode.isProject()) {
			writeHyperLink(ColumnIndex++, RowIndex,llurla.getBrowseURL(), false, llNode.getName());
		}
		else {
			writeHyperLink(ColumnIndex++, RowIndex,llurla.getXmlExportURL(), false, llNode.getName());
		}

		writeStringValue(ColumnIndex, RowIndex, llNode.getParentId(), false);
		LiveLinkURLObservable llurlb = new LiveLinkURLObservable(llNode.getParentId());
		writeHyperLink(ColumnIndex++, RowIndex, llurlb.getBrowseURL(), false, parentNode);

		return ColumnIndex;
	}

	/**
	 * writes using JExcel API the content of the results sheet
	 * @param liveLinkNodeTree
	 * @return
	 * @throws Excel2003MaxRowsException 
	 */
	public boolean writeLiveLinkData(Tree liveLinkNodeTree) throws Excel2003MaxRowsException {

		this.RowIndex = 0;
		this.sheet.insertRow(0);

		if (liveLinkNodeTree.getSelectionCount() > 0) {
			TreeItem[] treeItems = liveLinkNodeTree.getSelection();
			if (treeItems != null) {
				TreeItem selectedItem = treeItems[0];
				if (selectedItem.getData() instanceof LiveLinkNode) {

					LiveLinkNode llNode = (LiveLinkNode)selectedItem.getData();
					// write the Header
					writeLiveLinkNodeHeader (llNode, false, 0);

					if (llNode.getChildren() != null) {
						Iterator<LiveLinkNode> iter = llNode.getChildren().iterator();
						int ColumnIndex = 0;
						while (iter.hasNext()) {
							LiveLinkNode llSubNode = iter.next();
							RowIndex++;
							if (RowIndex < MaxNumberOfRows) {
								this.sheet.insertRow(RowIndex);
							} else {
								throw new Excel2003MaxRowsException("EXCEL 2003 - row index exceeds max number of row > " + (MaxNumberOfRows) );
							}

							// write the node data
							ColumnIndex = writeLiveLinkNodeData(llSubNode, selectedItem.getText(), false, 0);
						}

						// auto size the columns		
						for (int j =0 ; j < ColumnIndex ; j++) {
							CellView cv = this.sheet.getColumnView(j);
							cv.setAutosize(true);
							this.sheet.setColumnView(j, cv);
						}
					}
				}
			}
		}
		return true;
	}

	public boolean writeReadMeSheet(File parentFile) {

		this.RowIndex = 5;
		this.sheet.insertRow(RowIndex);

		int ColumnIndex = 0;
		// write the header of the table
		writeStringValue(ColumnIndex++,RowIndex,"Tool Version",true);

		ToolVersion toolVersion = new ToolVersion();
		writeStringValue(ColumnIndex++,RowIndex,toolVersion.getToolVersion(),false);
		// start new row and start again in column 0
		ColumnIndex = 0;
		RowIndex++;
		writeStringValue(ColumnIndex++,RowIndex,"Date Time",true);

		// Get the current date and time from the Calendar object
		Date now = Calendar.getInstance().getTime();
		writeDateValue (ColumnIndex++,RowIndex,now,false);

		ColumnIndex = 0;
		RowIndex++;
		this.sheet.insertRow(RowIndex);

		ColumnIndex = 0;
		RowIndex++;
		this.sheet.insertRow(RowIndex);

		writeStringValue(ColumnIndex++,RowIndex,"parent",true);
		writeStringValue(ColumnIndex++,RowIndex,"File Name",true);
		writeStringValue(ColumnIndex++,RowIndex,"File Path",true);
		writeStringValue(ColumnIndex++,RowIndex,"Last Modified",true);
		writeStringValue(ColumnIndex++,RowIndex,"Mime Type",true);

		ColumnIndex = 0;
		RowIndex++;
		this.sheet.insertRow(RowIndex);
		writeStringValue(ColumnIndex++,RowIndex,parentFile.getParent(),false);
		writeStringValue(ColumnIndex++,RowIndex,parentFile.getName(),false);
		writeStringValue(ColumnIndex++,RowIndex,parentFile.getAbsolutePath(),false);
		writeDateValue(ColumnIndex++,RowIndex,new Date(parentFile.lastModified()),false);
		writeStringValue(ColumnIndex++,RowIndex,getSystemTypeDescription(parentFile),false);

		// auto size the columns
		for (int j =0 ; j < ColumnIndex ; j++) {
			CellView cv = this.sheet.getColumnView(j);
			cv.setAutosize(true);
			this.sheet.setColumnView(j, cv);
		}
		writeThalesImage();
		return true;
	}

	private String getSystemTypeDescription(File file) {
		String strMimeType = "";
		try {

			FileSystemView fsview = FileSystemView.getFileSystemView();
			strMimeType = fsview.getSystemTypeDescription(file);
		}  
		catch (InternalError e2) {
			strMimeType = "FileSystemView.getShellFolder: InternalError f= "+file.getAbsolutePath();
			System.err.println("FileSystemView.getShellFolder: InternalError f= "+file.getAbsolutePath());
			e2.printStackTrace();
		}
		return strMimeType;
	}

	public boolean writeReadMeSheet(LiveLinkNode llNode) {

		this.RowIndex = 5;
		this.sheet.insertRow(RowIndex);

		int ColumnIndex = 0;
		// write the header of the table
		writeStringValue(ColumnIndex++,RowIndex,"Tool Version",true);

		ToolVersion toolVersion = new ToolVersion();
		writeStringValue(ColumnIndex++,RowIndex,toolVersion.getToolVersion(),false);
		// start new row and start again in column 0
		ColumnIndex = 0;
		RowIndex++;
		writeStringValue(ColumnIndex++,RowIndex,"Date Time",true);

		// Get the current date and time from the Calendar object
		Date now = Calendar.getInstance().getTime();
		writeDateValue (ColumnIndex++,RowIndex,now,false);
		//writeTimeValue (ColumnIndex++,RowIndex,now,false);

		RowIndex++;
		this.sheet.insertRow(RowIndex);

		RowIndex++;
		this.sheet.insertRow(RowIndex);
		ColumnIndex = 0;
		// write the header of the table
		writeStringValue(ColumnIndex++,RowIndex, llNode.getNameAttribute(),true);
		writeStringValue(ColumnIndex++,RowIndex, llNode.getCreatedDateAttribute(),true);
		writeStringValue(ColumnIndex++,RowIndex, llNode.getModifiedDateAttribute(),true);
		writeStringValue(ColumnIndex++,RowIndex, llNode.getObjectNameAttribute(),true);
		writeStringValue(ColumnIndex++,RowIndex, llNode.getDescriptionAttribute(),true);
		writeStringValue(ColumnIndex++,RowIndex, llNode.getMimeTypeAttribute(),true);
		writeStringValue(ColumnIndex++,RowIndex, llNode.getSizeAttribute(),true);
		writeStringValue(ColumnIndex++,RowIndex, llNode.getCreatedByNameAttribute(),true);
		writeStringValue(ColumnIndex++,RowIndex, llNode.getOwnedByNameAttribute(),true);
		writeStringValue(ColumnIndex++,RowIndex, llNode.getNodeIdAttribute(),true);
		writeStringValue(ColumnIndex++,RowIndex, llNode.getParentIdAttribute(),true);

		RowIndex++;
		this.sheet.insertRow(RowIndex);
		ColumnIndex = 0;

		// write the name of the initial item
		writeStringValue(ColumnIndex++, RowIndex, llNode.getName(),false);

		//writeStringValue(ColumnIndex++,RowIndex,llNode.getLLcreatedDate(),false);
		try {
			writeStringValue(ColumnIndex++, RowIndex, llNode.getCreatedDate(),false);
			//writeStringValue(ColumnIndex++,RowIndex,llNode.getModifiedDate(),false);
			writeStringValue(ColumnIndex++, RowIndex, llNode.getModifiedDate() ,false);
		} catch (java.lang.NullPointerException e1) {

		}

		writeStringValue(ColumnIndex++, RowIndex, llNode.getObjectName(),false);
		writeStringValue(ColumnIndex++, RowIndex, llNode.getDescription(),false);
		writeStringValue(ColumnIndex++, RowIndex, llNode.getMimeType(),false);
		// write the size in Bytes if it is a document or the number of files if it is a folder
		writeLongValue(ColumnIndex++, RowIndex, llNode.getLongSize(),false);

		writeStringValue(ColumnIndex++,RowIndex,llNode.getCreatedByName(),false);
		writeStringValue(ColumnIndex++,RowIndex,llNode.getOwnedByName(),false);

		writeStringValue(ColumnIndex,RowIndex,llNode.getId(),false);

		LiveLinkURLObservable llurla = new LiveLinkURLObservable(llNode.getId());
		if (llNode.isDocument()) {
			writeHyperLink(ColumnIndex++,RowIndex,llurla.getDownloadURL(),false,llNode.getName());
		}
		else if (llNode.isFolder() || llNode.isProject()) {
			writeHyperLink(ColumnIndex++,RowIndex,llurla.getBrowseURL(),false,llNode.getName());
		}
		else {
			writeHyperLink(ColumnIndex++,RowIndex,llurla.getXmlExportURL(),false,llNode.getName());
		}

		writeStringValue(ColumnIndex,RowIndex,llNode.getParentId(),false);
		LiveLinkURLObservable llurlb = new LiveLinkURLObservable(llNode.getParentId());
		writeHyperLink(ColumnIndex++,RowIndex,llurlb.getXmlExportURL(),false,llNode.getParentId());

		// auto size the columns
		for (int j =0 ; j < ColumnIndex ; j++) {
			CellView cv = this.sheet.getColumnView(j);
			cv.setAutosize(true);
			this.sheet.setColumnView(j, cv);
		}

		writeThalesImage();
		return true;
	}

	public boolean writeReadMeSheet(Tree liveLinkNodeTree, boolean fromRoot) {

		this.RowIndex = 5;
		this.sheet.insertRow(RowIndex);

		int ColumnIndex = 0;
		// write the header of the table
		writeStringValue(ColumnIndex++,RowIndex,"Tool Version",true);

		ToolVersion toolVersion = new ToolVersion();
		writeStringValue(ColumnIndex++,RowIndex,toolVersion.getToolVersion(),false);
		// start new row and start again in column 0
		ColumnIndex = 0;
		RowIndex++;
		writeStringValue(ColumnIndex++,RowIndex,"Date Time",true);

		// Get the current date and time from the Calendar object
		Date now = Calendar.getInstance().getTime();
		writeDateValue (ColumnIndex++,RowIndex,now,false);
		//writeTimeValue (ColumnIndex++,RowIndex,now,false);

		RowIndex++;
		this.sheet.insertRow(RowIndex);

		TreeItem selectedItem = null;
		if (fromRoot == true) {
			selectedItem = liveLinkNodeTree.getItem(0);
		}
		else {
			selectedItem = liveLinkNodeTree.getSelection()[0];
		}
		if(selectedItem != null) {
			if (selectedItem.getData() instanceof LiveLinkNode) {

				LiveLinkNode llNode = (LiveLinkNode)selectedItem.getData();

				RowIndex++;
				this.sheet.insertRow(RowIndex);
				ColumnIndex = 0;
				// write the header of the table
				writeStringValue(ColumnIndex++,RowIndex,llNode.getNameAttribute(),true);
				writeStringValue(ColumnIndex++,RowIndex,llNode.getCreatedDateAttribute(),true);
				writeStringValue(ColumnIndex++,RowIndex,llNode.getModifiedDateAttribute(),true);
				writeStringValue(ColumnIndex++,RowIndex,llNode.getObjectNameAttribute(),true);
				writeStringValue(ColumnIndex++,RowIndex,llNode.getDescriptionAttribute(),true);
				writeStringValue(ColumnIndex++,RowIndex,llNode.getMimeTypeAttribute(),true);
				writeStringValue(ColumnIndex++,RowIndex,llNode.getSizeAttribute(),true);
				writeStringValue(ColumnIndex++,RowIndex,llNode.getCreatedByNameAttribute(),true);
				writeStringValue(ColumnIndex++,RowIndex,llNode.getOwnedByNameAttribute(),true);
				writeStringValue(ColumnIndex++,RowIndex,llNode.getNodeIdAttribute(),true);
				writeStringValue(ColumnIndex++,RowIndex,llNode.getParentIdAttribute(),true);

				RowIndex++;
				this.sheet.insertRow(RowIndex);
				ColumnIndex = 0;

				// write the name of the initial item
				writeStringValue(ColumnIndex++,RowIndex,llNode.getName(),false);

				//writeStringValue(ColumnIndex++,RowIndex,llNode.getLLcreatedDate(),false);
				writeDateValue(ColumnIndex++,RowIndex,llNode.getNodeCreationDate().getDate(),false);

				//writeStringValue(ColumnIndex++,RowIndex,llNode.getModifiedDate(),false);
				writeDateValue(ColumnIndex++,RowIndex,llNode.getNodeModificationDate().getDate(),false);

				writeStringValue(ColumnIndex++,RowIndex,llNode.getObjectName(),false);
				writeStringValue(ColumnIndex++,RowIndex,llNode.getDescription(),false);
				writeStringValue(ColumnIndex++,RowIndex,llNode.getMimeType(),false);
				// write the size in Bytes if it is a document or the number of files if it is a folder
				writeLongValue(ColumnIndex++,RowIndex,llNode.getLongSize(),false);

				writeStringValue(ColumnIndex++,RowIndex,llNode.getCreatedByName(),false);
				writeStringValue(ColumnIndex++,RowIndex,llNode.getOwnedByName(),false);

				writeStringValue(ColumnIndex,RowIndex,llNode.getId(),false);

				LiveLinkURLObservable llurla = new LiveLinkURLObservable(llNode.getId());
				if (llNode.isDocument()) {
					writeHyperLink(ColumnIndex++,RowIndex,llurla.getDownloadURL(),false,llNode.getName());
				}
				else if (llNode.isFolder() || llNode.isProject()) {
					writeHyperLink(ColumnIndex++,RowIndex,llurla.getBrowseURL(),false,llNode.getName());
				}
				else {
					writeHyperLink(ColumnIndex++,RowIndex,llurla.getXmlExportURL(),false,llNode.getName());
				}

				writeStringValue(ColumnIndex,RowIndex,llNode.getParentId(),false);
				LiveLinkURLObservable llurlb = new LiveLinkURLObservable(llNode.getParentId());
				writeHyperLink(ColumnIndex++,RowIndex,llurlb.getXmlExportURL(),false,llNode.getParentId());
			}

			// auto size the columns
			for (int j =0 ; j < ColumnIndex ; j++) {
				CellView cv = this.sheet.getColumnView(j);
				cv.setAutosize(true);
				this.sheet.setColumnView(j, cv);
			}
		}
		writeThalesImage();
		return true;

	}

	private boolean writeThalesImage () {
		InputStream in = null;
		boolean ret = false;
		try {
			in = WritableExcelFile.class.getResourceAsStream("Thales.png");
			if (in != null) {
				BufferedImage input = ImageIO.read(in);
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				ImageIO.write(input, "png", outputStream);

				//System.out.println("WritableExcelFile: Write Thales Image: width: "+input.getWidth()+" height: "+input.getHeight());
				this.sheet.addImage(new WritableImage(1,1,input.getWidth()/ CELL_DEFAULT_WIDTH,
						input.getHeight()/CELL_DEFAULT_HEIGHT ,outputStream.toByteArray()));
				ret =  true;
			}
			else {
				System.out.println("Writable Excel File: image file Thales png does not exist !!!");
			}
			in.close();
		}
		catch (IOException ex) {
			System.err.println("WritableExcelFile: "+ex.getLocalizedMessage());
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public void writeRecursiveLiveLinkData(Tree liveLinkNodeTree) throws Excel2003MaxRowsException {

		this.sheet.insertRow(RowIndex);
		TreeItem rootItem = liveLinkNodeTree.getItems()[0];
		if (rootItem != null) {
			if (rootItem.getData() instanceof LiveLinkNode) {
				LiveLinkNode rootNode = (LiveLinkNode) rootItem.getData();
				// write the Header
				writeLiveLinkNodeHeader (rootNode, false, 0);
				traverseLiveLinkNodeTree(liveLinkNodeTree.getItems());
			}
		}
	}

	private String getFullPath(final TreeItem item) {

		String fullPath = "";
		TreeItem localItem = item;
		if (item != null) {
			fullPath = item.getText();
			while (localItem.getParentItem() != null) {

				fullPath = localItem.getParentItem().getText() + "/" + fullPath;
				localItem = localItem.getParentItem(); 
			}
		}
		return fullPath;
	}

	/**
	 * traverse the tree and select a node tree
	 * such as the node is a folder or
	 * @param items
	 */
	private void traverseLiveLinkNodeTree(TreeItem[] items) throws Excel2003MaxRowsException {

		int ColumnIndex = 0;
		for (int i = 0; i < items.length; i++) {
			TreeItem item = items[i];

			if (item.getData() instanceof LiveLinkNode) {
				LiveLinkNode llNode = (LiveLinkNode) item.getData();
				RowIndex++;
				if (RowIndex < MaxNumberOfRows) {
					this.sheet.insertRow(RowIndex);
				} else {
					throw new Excel2003MaxRowsException("EXCEL 2003 - row index exceeds max number of row > " + (MaxNumberOfRows) );
				}

				// write the node data
				String strParentText = getFullPath(item);
				//if (item.getParentItem() != null) {
				//	strParentText += item.getParentItem().getText();
				//}
				ColumnIndex = writeLiveLinkNodeData(llNode, strParentText, false, 0);
			}
			traverseLiveLinkNodeTree(items[i].getItems());
		}

		// auto size the columns		
		for (int j =0 ; j < ColumnIndex ; j++) {
			CellView cv = this.sheet.getColumnView(j);
			cv.setAutosize(true);
			this.sheet.setColumnView(j, cv);
		}
	}

	private int writeFileExplorerHeader() {

		this.RowIndex = 0;
		int ColumnIndex = 0;
		this.sheet.insertRow(0);
		// write the name of the parent folder
		writeStringValue(ColumnIndex++,RowIndex,"#",true);
		writeStringValue(ColumnIndex++,RowIndex,"Parent",true);
		writeStringValue(ColumnIndex++,RowIndex,"Name",true);
		writeStringValue(ColumnIndex++,RowIndex,"Size in Bytes",true);
		writeStringValue(ColumnIndex++,RowIndex,"Date Modified",true);
		writeStringValue(ColumnIndex++,RowIndex,"Mime Type",true);
		writeStringValue(ColumnIndex++,RowIndex,"Hyper Link",true);

		return ColumnIndex;
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

	private void recursiveFileExplorer (File initialFile, final StatusBarObserver statusBarObserver) throws Excel2003MaxRowsException {

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
				// add observer to the watched object
				fileObservable.addObserver(statusBarObserver);
				fileObservable.setValue(file.getName());

				writeFileData(file);
				if (newFiles[i].isDirectory()) {
					// write folder name in Status Bar

					// Recursive search of files from this folder
					recursiveFileExplorer (newFiles[i], statusBarObserver);				
				}
			}
		}
	}

	public void writeFileExplorerRecursive(final File selectedFile, final StatusBarObserver statusBarObserver) throws Excel2003MaxRowsException {

		// compute number of columns
		int columnIndex = writeFileExplorerHeader();

		// recursively write in the EXCEL file
		recursiveFileExplorer(selectedFile, statusBarObserver);

		// auto size the columns		
		for (int j =0 ; j < columnIndex ; j++) {
			CellView cv = this.sheet.getColumnView(j);
			cv.setAutosize(true);
			this.sheet.setColumnView(j, cv);
		}
	}

	public void writeFileData (File file) throws Excel2003MaxRowsException {

		this.RowIndex++;
		if (this.RowIndex > MaxNumberOfRows){
			throw new Excel2003MaxRowsException("EXCEL 2003 - row index exceeds max number of row > " + (MaxNumberOfRows) );
		}
		int columnIndex = 0;
		this.sheet.insertRow(this.RowIndex);
		columnIndex = 0;
		//System.out.println("Writable Excel Sheet: writeFileData : "+file.getName());
		writeLongValue(columnIndex++,RowIndex,RowIndex,false);
		writeStringValue(columnIndex++,RowIndex,file.getParent(),false);
		writeStringValue(columnIndex++,RowIndex,file.getName(),false);
		writeLongValue(columnIndex++,RowIndex,file.length(),false);
		writeDateValue(columnIndex++,RowIndex,new Date(file.lastModified()),false);
		writeStringValue(columnIndex++,RowIndex,getSystemTypeDescription(file),false);
		try {
			writeHyperLink(columnIndex++,RowIndex,file.toURI().toURL(),false,file.getName());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}


	public boolean writeFileExplorer(File initialFile, final StatusBarObserver statusBarObserver) throws Excel2003MaxRowsException {

		int ColumnIndex = writeFileExplorerHeader();
		// search sub folders and files
		File[] newFiles = initialFile.listFiles();
		if (newFiles != null) {

			for (int i = 0; i < newFiles.length; i++) {
				// write the data for the current file
				File file = newFiles[i];
				FileObservable fileObservable = new FileObservable(file);
				// add observer to the watched object
				fileObservable.addObserver(statusBarObserver);
				fileObservable.setValue(file.getName());
				writeFileData(file);
			}
		}

		// auto size the columns
		for (int i=0 ; i <= ColumnIndex ; i++) {
			CellView cv = this.sheet.getColumnView(i);
			cv.setAutosize(true);
			this.sheet.setColumnView(i, cv);
		}
		return true;
	}

	public void generateLiveLinkTreeIndex(Tree liveLinkNodeTree) throws Excel2003MaxRowsException {

		this.RowIndex = 0;
		this.sheet.insertRow(RowIndex);
		LiveLinkNode rootNode = null;
		TreeItem rootItem = liveLinkNodeTree.getItems()[0];
		if (rootItem != null) {
			if (rootItem.getData() instanceof LiveLinkNode) {
				rootNode = (LiveLinkNode) rootItem.getData();
				liveLinkNodeTreeSet = new LiveLinkNodeTreeSet();
				buildLiveLinkNodeTreeCollection(liveLinkNodeTree.getItems());
			}
		}
		// write the Header according to the depth computed depth
		writeGeneratedIndexHeader(rootNode);
		// fill the EXCEL records
		writeGeneratedIndex();
	}

	private void writeGeneratedIndex() throws Excel2003MaxRowsException {

		this.RowIndex = 0;
		Iterator<LiveLinkNodeExcelRecord> iter = liveLinkNodeTreeSet.iterator();
		System.out.println("number of elements: "+liveLinkNodeTreeSet.size());
		if (liveLinkNodeTreeSet.size() > 64000) {
			new ShellErrorMessage(this.display,this.display.getActiveShell(),"Number of records exceed max EXCEL size !!!");
		}
		int MaxColumnIndex = 0;
		while (iter.hasNext() && (this.RowIndex < 64000) ) {

			LiveLinkNodeExcelRecord llXlsRecord = iter.next();
			//System.out.println("write output Excel: "+llXlsRecord.getLLNode().getName());
			this.RowIndex++;
			if (this.RowIndex > MaxNumberOfRows) {
				throw new Excel2003MaxRowsException("EXCEL 2003 - row index exceeds max number of row > " + (MaxNumberOfRows) );
			}
			this.sheet.insertRow(this.RowIndex);
			// start inserting data
			int ColumnIndex = 0;
			//System.out.println("Writable Excel Sheet: writeFileData : "+file.getName());
			// write the index of each item
			writeLongValue(ColumnIndex++,this.RowIndex, llXlsRecord.getIndex()+1, false);
			ColumnIndex = ColumnIndex + llXlsRecord.getTreeDepth();
			LiveLinkNode llNode = llXlsRecord.getLLNode();
			LiveLinkURLObservable llurla = new LiveLinkURLObservable(llNode.getId());
			if (llNode.isDocument()) {
				writeHyperLink(ColumnIndex++,this.RowIndex,llurla.getDownloadURL(), false, llNode.getName());
			}
			else if (llNode.isFolder() || llNode.isProject()) {
				writeHyperLink(ColumnIndex++,this.RowIndex,llurla.getBrowseURL(), false, llNode.getName());
			}
			else {
				writeHyperLink(ColumnIndex++,this.RowIndex,llurla.getBrowseURL(), false, llNode.getName());
			}
			MaxColumnIndex = Math.max(MaxColumnIndex, ColumnIndex+llXlsRecord.getTreeDepth());
			// this part of the excel reports is not indented :it is always aligned on the same starting column !!!
			ColumnIndex = liveLinkNodeTreeSet.getMaxDepth()+2;
			writeDateValue(ColumnIndex++,this.RowIndex,llNode.getNodeCreationDate().getDate(),false);
			writeDateValue(ColumnIndex++,this.RowIndex,llNode.getNodeModificationDate().getDate(),false);
			writeStringValue(ColumnIndex++,this.RowIndex,llNode.getDescription(),false);
			writeStringValue(ColumnIndex++,this.RowIndex,llNode.getObjectName(),false);
			writeStringValue(ColumnIndex++,this.RowIndex,llNode.getMimeType(),false);
			writeLongValue(ColumnIndex++,this.RowIndex,llNode.getLongSize(),false);
			writeStringValue(ColumnIndex++,this.RowIndex,llNode.getCreatedByName(),false);
			MaxColumnIndex = Math.max(MaxColumnIndex, ColumnIndex);
		}

		// auto size the columns		
		for (int j =0 ; j < MaxColumnIndex ; j++) {
			CellView cv = this.sheet.getColumnView(j);
			cv.setAutosize(true);
			this.sheet.setColumnView(j, cv);
		}
	}

	private void writeGeneratedIndexHeader(LiveLinkNode llNode) {

		this.RowIndex = 0;
		this.sheet.insertRow(this.RowIndex);
		int ColumnIndex = 0;
		// write the header of the table
		writeStringValue(ColumnIndex++,this.RowIndex,"#",true);
		for (int i = 0 ; i < liveLinkNodeTreeSet.getMaxDepth()+1 ;i++) {
			writeStringValue(ColumnIndex+i,this.RowIndex,"name",true);
		}
		ColumnIndex = liveLinkNodeTreeSet.getMaxDepth()+2;
		writeStringValue(ColumnIndex++,this.RowIndex,llNode.getCreatedDateAttribute(),true);
		writeStringValue(ColumnIndex++,this.RowIndex,llNode.getModifiedDateAttribute(),true);
		writeStringValue(ColumnIndex++,this.RowIndex,llNode.getDescriptionAttribute(),true);
		writeStringValue(ColumnIndex++,this.RowIndex,llNode.getObjectNameAttribute(),true);
		writeStringValue(ColumnIndex++,this.RowIndex,llNode.getMimeTypeAttribute(),true);
		writeStringValue(ColumnIndex++,this.RowIndex,llNode.getSizeAttribute(),true);
		writeStringValue(ColumnIndex++,this.RowIndex,llNode.getCreatedByNameAttribute(),true);

	}

	private int getTreeDepth(final TreeItem item) {

		int treeDepth = 0;
		TreeItem localItem = item;
		if (item != null) {
			while (localItem.getParentItem() != null) {
				treeDepth++;
				localItem = localItem.getParentItem(); 
			}
		}
		return treeDepth;
	}

	private void buildLiveLinkNodeTreeCollection(TreeItem[] items) throws Excel2003MaxRowsException {

		for (int i = 0; i < items.length; i++) {
			TreeItem item = items[i];

			if (item.getData() instanceof LiveLinkNode) {
				LiveLinkNode llNode = (LiveLinkNode) item.getData();
				// write the node data
				String strParentText = getFullPath(item);
				int treeDepth = getTreeDepth(item);
				this.RowIndex++;
				if (this.RowIndex > MaxNumberOfRows) {
					throw new Excel2003MaxRowsException("EXCEL 2003 - row index exceeds max number of row > " + (MaxNumberOfRows) );
				}
				LiveLinkNodeExcelRecord llXlsRecord = new LiveLinkNodeExcelRecord(this.RowIndex, treeDepth, strParentText, llNode);
				this.liveLinkNodeTreeSet.add(llXlsRecord);
			}
			buildLiveLinkNodeTreeCollection(items[i].getItems());
		}
	}

	public void generateLiveLinkNodeRecursiveIndex(LiveLinkNode llNode, boolean writeHeader, boolean indentedEXCELresults, int maxDepth) throws Excel2003MaxRowsException {

		int ColumnIndex = 0;
		if (writeHeader) {

			// store the root node => used to compute a depth
			this.rootNode = llNode;
			this.rootNode.computeDepth(true, 0);
			maxDepth = this.rootNode.computeMaxDepth();
			logger.log(Level.INFO , "Node= " + llNode.getName() + " --- max depth= " + maxDepth);

			// write the header
			writeLiveLinkNodeHeader (llNode, indentedEXCELresults, maxDepth);
			RowIndex++;
			if (RowIndex > MaxNumberOfRows) {
				throw new Excel2003MaxRowsException("EXCEL 2003 - row index exceeds max number of row > " + (MaxNumberOfRows) );
			}
			this.sheet.insertRow(RowIndex);

			// write the first item = root node
			ColumnIndex = writeLiveLinkNodeData(llNode, llNode.getName(), indentedEXCELresults, maxDepth);
		}

		if (llNode.getChildren() != null) {
			Iterator<LiveLinkNode> iter = llNode.getChildren().iterator();

			while (iter.hasNext()) {
				LiveLinkNode llSubNode = iter.next();
				RowIndex++;
				if (RowIndex > MaxNumberOfRows) {
					throw new Excel2003MaxRowsException("EXCEL 2003 - row index exceeds max number of row > " + (MaxNumberOfRows) );
				}
				this.sheet.insertRow(RowIndex);
				// write the node data
				ColumnIndex = writeLiveLinkNodeData(llSubNode, llSubNode.getName(), indentedEXCELresults, maxDepth);
				generateLiveLinkNodeRecursiveIndex(llSubNode, false, indentedEXCELresults, maxDepth);
			}

			// auto size the columns		
			for (int j =0 ; j < ColumnIndex ; j++) {
				CellView cv = this.sheet.getColumnView(j);
				cv.setAutosize(true);
				this.sheet.setColumnView(j, cv);
			}
		}		
	}

}
