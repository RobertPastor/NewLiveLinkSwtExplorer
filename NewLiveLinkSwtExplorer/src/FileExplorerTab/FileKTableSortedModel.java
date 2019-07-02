package FileExplorerTab;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.filechooser.FileSystemView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableSortedModel;
import de.kupzog.ktable.renderers.FixedCellRenderer;
import de.kupzog.ktable.renderers.TextCellRenderer;

public class FileKTableSortedModel extends KTableSortedModel {

	private static final Logger logger = Logger.getLogger(FileKTableSortedModel.class.getName());

	private File[] files;
	private FileSystemView fsview = null;
	   

	final String[] columnsName = { "Id" , "Name"  ,  "Size in Bytes" , "Date Modified"  ,   "Mime type" };
	final int[] columnsWidth =   {  20  ,  300    ,      150          ,    150          ,       300            };

	final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMMM-yyyy HH:mm:ss", Locale.ENGLISH);

	//private final FixedCellRenderer m_fixedRenderer = new FixedCellRenderer(FixedCellRenderer.STYLE_FLAT  | TextCellRenderer.INDICATION_FOCUS_ROW);
	private final TextCellRenderer m_textRenderer = new TextCellRenderer(TextCellRenderer.INDICATION_FOCUS_ROW);
	private final TextCellRenderer m_blueTextRender = new TextCellRenderer(TextCellRenderer.INDICATION_FOCUS_ROW);

	private KTableCellRenderer m_fixedRenderer = 
		        new FixedCellRenderer(FixedCellRenderer.STYLE_PUSH | 
		            FixedCellRenderer.INDICATION_SORT | 
		            FixedCellRenderer.INDICATION_FOCUS |
		            FixedCellRenderer.INDICATION_CLICKED | SWT.BOLD);

	public FileKTableSortedModel(Composite parent, File[] files) {
		
		this.setFiles(files);
		logger.setLevel(Level.INFO);
		fsview = FileSystemView.getFileSystemView();
		// characterize the column where the user may click and launch the analysis
		m_blueTextRender.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_CYAN));
		
		// create a map between real indexes and sorted ones to allow sorting
        super.initialize();

	}
	
	public int getFixedSelectableColumnCount() {
		return 0;
	}

	public boolean isColumnResizable(int col) {
		return true;
	}

	public int getRowHeight(int row) {
		//logger.info("getRowHeight: for row: "+row);
		return 20;
	}

	public boolean isRowResizable(int row) {
		//logger.info("isRowResizable: for row: "+row);
		return false;
	}

	public int getRowHeightMinimum() {
		return 18;
	}

	public Object doGetContentAt(int col, int row) {
		//logger.info("doGetContentAt: for row: "+row+" col: "+col);
		return getContentAt(col, row);
	}

	public Object getContentAt(int columnIndex, int rowIndex) {
		// due to sorting -> need to map the indexes
		rowIndex = this.mapRowIndexToModel(rowIndex);
		//logger.info("getContentAt: for columnIndex: "+columnIndex+" for rowIndex: "+rowIndex);
		if (rowIndex == 0) {
			return columnsName[columnIndex];
		}
		if (this.files == null) {
			return null;
		}
		else {
			// rowIndex zero is the table header
			rowIndex = rowIndex - this.getFixedHeaderRowCount();
			if (columnIndex == 0) {
				// return the row index -> some kind of counting id.
				return rowIndex + 1;
			}
			if (columnIndex == 1) {
				// return the name
				return files[rowIndex].getName();
			}
			if (columnIndex == 2) {
				// return the size
				return new Long(files[rowIndex].length());
			}
			if (columnIndex == 3) {
				String strDate = formatter.format(new Date(files[rowIndex].lastModified()));
				return strDate;
			}
			if (columnIndex == 4) {
				return getSystemTypeDescription(files[rowIndex]);
			}
		}
		return null;
	}

	public int getFixedHeaderRowCount() {
		return 1;
	}

	public int getFixedSelectableRowCount() {
		return 0;
	}

	public String getTooltipAt(int col, int row) {
		if (row == 0){
			return " Please click on the header to sort the table according to this column content";
		}

		return null;
	}

	public KTableCellRenderer doGetCellRenderer(int col, int row) {
		if (row == 0) {
			return m_fixedRenderer;
		} 
		if (col == 0) {
			return m_blueTextRender;
		}
		return m_textRenderer;
	}

	public int doGetRowCount() {
		return getRowCount();
	}
	
	/**
	 * number of rows + one for the header
	 */
	public int getRowCount() {
		// number of rows + one row for the header
		int numberOfHeaderRows = this.getFixedHeaderRowCount();
		if (this.files != null) {
			return this.files == null ? 0 : this.files.length + this.getFixedHeaderRowCount();
		}
		return numberOfHeaderRows;
	}

	public int doGetColumnCount() {
		return getColumnCount();
	}

	public int getColumnCount() {
		return this.columnsName.length;
	}

	public int getFixedHeaderColumnCount() {
		return 0;
	}

	/**
	 * Returns the initial column width for the column
	 * index given. Note that if resize is enabled, this
	 * value might not be the real width of a column. The 
	 * value returned by <code>getColumnWidth()</code> corresponds
	 * to the real width used when painting the table!
	 * @param column The column index
	 * @return returns the initial width of the column.
	 */
	public int getInitialColumnWidth(int column) {
		return this.columnsWidth[column];
	}

	public int getInitialRowHeight(int row) {
		return 20;
	}

	public KTableCellEditor doGetCellEditor(int col, int row) {
        // no cell editors:   
		return null;
	}

	public void doSetContentAt(int col, int row, Object value) {
        // no editors, so not needed.   
	}

	private String getSystemTypeDescription(File file) {
		String strMimeType = "";
		try {
			strMimeType = fsview.getSystemTypeDescription(file);
		}  
		catch (InternalError e2) {
			strMimeType = "FileSystemView.getShellFolder: InternalError f= "+file.getAbsolutePath();
			System.err.println("FileSystemView.getShellFolder: InternalError f= "+file.getAbsolutePath());
			e2.printStackTrace();
		}
		return strMimeType;
	}

	public File[] getFiles() {
		return this.files;
	}

	public void setFiles(File[] _files) {
		this.files = _files;
	}

	public boolean isExcelFile(int row) {
		
		String strMimeType = getSystemTypeDescription(files[row]);
		logger.log(Level.INFO," selected row = "+ row + "-- file= " + files[row].getName() + " -- mime type= "+ strMimeType);
		if (strMimeType.toLowerCase().contains("excel")) {
			return true;
		}
		return false;
	}
}
