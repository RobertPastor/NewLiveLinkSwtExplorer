package FileExplorerTab;

import java.io.File;
import java.util.Date;

import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;

public class FileTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5801759727160563118L;

	private File[] files;

	final String[] columnsName = { "Name", "Size in Bytes", "Date Modified" ,"Mime type" };

	public FileTableModel(File[] files) {
		this.files = files;
	}

	public int getColumnCount() {
		return columnsName.length;
	}

	public Class<?> getColumnClass(int col) {
		if (col == 1)
			return Long.class;
		if (col == 2)
			return Date.class;
		return String.class;
	}

	public int getRowCount() {
		return files == null ? 0 : files.length;
	}

	public Object getValueAt(int row, int col) {
		if (col == 0)
			return files[row].getName();
		if (col == 1)
			return new Long(files[row].length());
		if (col == 2)
			return new Date(files[row].lastModified());
		if (col == 3) {
			return getSystemTypeDescription(files[row]);
		}
		return "";
	}

	@Override
	public String getColumnName(int col) {
		return columnsName[col];
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
}

