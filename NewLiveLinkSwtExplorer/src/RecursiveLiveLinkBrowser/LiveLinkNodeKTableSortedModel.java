package RecursiveLiveLinkBrowser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import LiveLinkCore.LiveLinkNode;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableSortedModel;
import de.kupzog.ktable.renderers.FixedCellRenderer;
import de.kupzog.ktable.renderers.TextCellRenderer;

/**
 * this class is responsible for managing a KTable with all the livelink nodes of a livelink folder.
 * this table is able to support sorting.
 * @author t0007330
 * @since December 2015
 *
 */
public class LiveLinkNodeKTableSortedModel extends KTableSortedModel {

	private static final Logger logger = Logger.getLogger(LiveLinkNodeKTableSortedModel.class.getName());

	private LiveLinkNode rootNode = null;
	final String[] columnsName = {   "Id" , "Name", "Object" , "Description" , "Size", "Date Created" ,"Date Modified" , "Owner" , "Mime type" };
	final int[] columnsWidth =   {    30  ,  300  ,    80    ,      150      ,  50   ,      150       ,     150        ,   100   ,    100};
	
	final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMMM-yyyy HH:mm:ss", Locale.ENGLISH);

	//private final FixedCellRenderer m_fixedRenderer = new FixedCellRenderer(FixedCellRenderer.STYLE_FLAT  | TextCellRenderer.INDICATION_FOCUS_ROW);
	private final TextCellRenderer m_textRenderer = new TextCellRenderer(TextCellRenderer.INDICATION_FOCUS_ROW);
	
	private KTableCellRenderer m_fixedRenderer = 
			new FixedCellRenderer(TextCellRenderer.STYLE_PUSH | 
					TextCellRenderer.INDICATION_SORT | 
					TextCellRenderer.INDICATION_FOCUS |
					TextCellRenderer.INDICATION_CLICKED | SWT.BOLD);

	public LiveLinkNodeKTableSortedModel(Composite parent, LiveLinkNode llNode) {
		//logger.setLevel(Level.OFF);
		logger.info("constructor");
		this.rootNode = llNode;

		// we don't want the default foreground color on text cells,
		// so we change it:
		m_textRenderer.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
		// needed to create the vector allowing to map the physical table to the sorted one shown to the reader.
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

	@Override
	public Object doGetContentAt(int col, int row) {
		//logger.info("doGetContentAt: for row: "+row+" col: "+col);
		return getContentAt(col,row);
	}

	public Object getContentAt(int columnIndex, int rowIndex) {
		// as the table might be sorted need to map row index to model
		rowIndex = this.mapRowIndexToModel(rowIndex);

		//logger.info("getContentAt: for columnIndex: "+columnIndex+" for rowIndex: "+rowIndex);
		if (rowIndex == 0) {
			return this.columnsName[columnIndex];
		}
		if (this.rootNode == null) {
			return null;
		}
		else {
			if (this.rootNode.hasChildren()) {
				// Robert - 23 mai 2016 - MINUS ONE because of table header 
				rowIndex = rowIndex - this.getFixedHeaderRowCount();
				// display the information related to the children of the selected node
				if (columnIndex == 0) {
					// return the row index -> some kind of counting id.
					return rowIndex + 1;
				}
				if (columnIndex == 1) {
					return this.rootNode.getChild(rowIndex).getName();
				}
				if (columnIndex == 2) {
					return this.rootNode.getChild(rowIndex).getObjectName();
				}
				if (columnIndex == 3) {
					return this.rootNode.getChild(rowIndex).getDescription();
				}
				if (columnIndex == 4) {
					return new Long(this.rootNode.getChild(rowIndex).getLongSize());
				}
				if (columnIndex == 5) {
					Date date = this.rootNode.getChild(rowIndex).getNodeCreationDate().getDate();

					String strDate = formatter.format(date);
					return strDate;
				}
				if (columnIndex == 6) {
					Date date = this.rootNode.getChild(rowIndex).getNodeModificationDate().getDate();

					String strDate = formatter.format(date);
					return strDate;
				}
				if (columnIndex == 7) {
					return this.rootNode.getChild(rowIndex).getOwnedByName();
				}
				if (columnIndex == 8) {
					return this.rootNode.getChild(rowIndex).getMimeType();
				}
			}
			else  {
				// node has no children
				// display only the information of the selected node
				if (columnIndex == 0) {
					return 1;
				}
				if (columnIndex == 1) {
					return this.rootNode.getName();
				}
				if (columnIndex == 2) {
					return this.rootNode.getObjectName();
				}
				if (columnIndex == 3) {
					return this.rootNode.getDescription();
				}
				if (columnIndex == 4) {
					return new Long(this.rootNode.getLongSize());
				}
				if (columnIndex == 5) {
					Date date = this.rootNode.getNodeCreationDate().getDate();

					String strDate = formatter.format(date);
					return strDate;
				}
				if (columnIndex == 6) {
					Date date = this.rootNode.getNodeModificationDate().getDate();

					String strDate = formatter.format(date);
					return strDate;
				}
				if (columnIndex == 7) {
					return this.rootNode.getOwnedByName();
				}
				if (columnIndex == 8) {
					return this.rootNode.getMimeType();
				}
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
		return null;
	}
	
	public KTableCellRenderer doGetCellRenderer(int colIndex, int rowIndex) {
		return getCellRenderer(colIndex, rowIndex);
	}
	
	public KTableCellRenderer getCellRenderer(int colIndex, int rowIndex) {

		if (rowIndex == 0) {
			return this.m_fixedRenderer;
		}
		return KTableCellRenderer.defaultRenderer;
	}

	@Override
	public int doGetRowCount() {
		return getRowCount();
	}
	/**
	 * number of rows + one for the header
	 */
	public int getRowCount() {
		// number of rows + one row for the header
		if (this.rootNode != null) {
			if (this.rootNode.hasChildren()) {
				//logger.info("getRowCount: "+rowCount);
				return this.rootNode.getChildCount()+getFixedHeaderRowCount();
			}
			return getFixedHeaderRowCount();
		}
		return getFixedHeaderRowCount();
	}

	@Override
	public int doGetColumnCount() {
		return getColumnCount();
	}

	@Override
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

	@Override
	public int getInitialRowHeight(int row) {
		return 20;
	}

	@Override
	public KTableCellEditor doGetCellEditor(int col, int row) {
		return null;
	}

	@Override
	public void doSetContentAt(int col, int row, Object value) {

	}

	public void setRootNode(LiveLinkNode llNode) {
		this.rootNode = llNode;
		this.resetRowMapping();
	}
	
	public LiveLinkNode getRootNode() {
		return this.rootNode;
	}
}
