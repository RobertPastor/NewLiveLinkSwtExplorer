package DownLoadTab;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableDefaultModel;
import de.kupzog.ktable.renderers.TextCellRenderer;

public class HyperLinkTableModel extends KTableDefaultModel{

	private static final Logger logger = Logger.getLogger(HyperLinkTableModel.class.getName());
	private final TextCellRenderer m_textRenderer = new TextCellRenderer(TextCellRenderer.INDICATION_FOCUS_ROW);

	private Composite parent = null;
	public Composite getParent() {
		return parent;
	}

	private DownLoadedFileSet downLoadedFileSet = null;

	final String[] columnsName = { "Id"   , "Hyperlink"  , "LiveLink Node"  , "Object"  , "Mime Type" , "is Browsed" , "is Downloaded"};
	final int[] columnsWidth =   {    20  ,    500    ,      150            , 100       ,  100        ,  100          ,  100 };


	public HyperLinkTableModel(Composite parent,DownLoadedFileSet downLoadedFileSet) {
		this.parent = parent;
		this.downLoadedFileSet = downLoadedFileSet;
		//logger.setLevel(Level.OFF);
		logger.info("constructor");

		// we don't want the default foreground color on text cells,
		// so we change it:
		m_textRenderer.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));

	}

	public void setHyperLinks(DownLoadedFileSet downLoadedFileSet) {

		this.downLoadedFileSet = downLoadedFileSet;
		logger.log(Level.INFO," number of hyperlinks: "+this.downLoadedFileSet.size());

	}

	@Override
	public int getFixedHeaderRowCount() {
		return 1;
	}

	@Override
	public int getFixedSelectableRowCount() {
		return 0;
	}

	@Override
	public int getFixedHeaderColumnCount() {
		return columnsName.length;
	}

	@Override
	public int getFixedSelectableColumnCount() {
		return 0;
	}

	@Override
	public boolean isColumnResizable(int col) {
		return true;
	}

	@Override
	public boolean isRowResizable(int row) {
		return false;
	}

	@Override
	public int getRowHeightMinimum() {
		return 18;
	}

	@Override
	public int getInitialColumnWidth(int column) {

		return columnsWidth[column];
	}

	@Override
	public int getInitialRowHeight(int row) {

		return 18;
	}

	@Override
	public Object doGetContentAt(int col, int row) {
		//logger.log(Level.INFO,"col= "+col+"...row: "+row);
		if (row == 0) {
			return columnsName[col];
		}
		if (this.downLoadedFileSet != null) {
			// use row index minus one to skip row 0 which corresponds to the header row
			if (col == 0) {
				return row;
			}
			if (col == 1) {
				return this.downLoadedFileSet.getHyperLink(row-1);
			}
			if (col == 2) {
				if (this.downLoadedFileSet.getLiveLinkNode(row-1) != null) {
					return this.downLoadedFileSet.getLiveLinkNode(row-1).getName();
				}
				else {
					return "";
				}
			}
			if (col == 3) {
				if (this.downLoadedFileSet.getLiveLinkNode(row-1) != null) {
					return this.downLoadedFileSet.getLiveLinkNode(row-1).getObjectName();
				}
				else {
					return "";
				}
			}
			if (col == 4) {
				if (this.downLoadedFileSet.getLiveLinkNode(row-1) != null) {
					return this.downLoadedFileSet.getLiveLinkNode(row-1).getMimeType();
				}
				else {
					return "";
				}
			}
			if (col == 5) {
				if (this.downLoadedFileSet.getLiveLinkNode(row-1) != null) {
					return this.downLoadedFileSet.getLiveLinkNode(row-1).isBrowsed();
				}
				else {
					return "";
				}
			}
			if (col == 6) {
				if (this.downLoadedFileSet.getLiveLinkNode(row-1) != null) {
					return this.downLoadedFileSet.getLiveLinkNode(row-1).isDownLoaded();
				}
				else {
					return "";
				}
			}
		}
		return null;
	}

	@Override
	public KTableCellEditor doGetCellEditor(int col, int row) {
		return null;
	}

	@Override
	public void doSetContentAt(int col, int row, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public KTableCellRenderer doGetCellRenderer(int col, int row) {
		if (row == 0) {
			return KTableCellRenderer.defaultRenderer;
		}
		return KTableCellRenderer.defaultRenderer;
	}

	@Override
	public int doGetRowCount() {
		//logger.info("doGetRowCount");
		if (this.downLoadedFileSet != null) {
			int rowCount = (int) this.downLoadedFileSet.size()+1;
			//logger.info("doGetRowCount: "+rowCount);
			return rowCount;
		}
		return 0;
	}

	@Override
	public int doGetColumnCount() {
		return columnsName.length;
	}


}
