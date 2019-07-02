package RecursiveLiveLinkBrowser;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellResizeListener;
import de.kupzog.ktable.KTableCellSelectionListener;
import de.kupzog.ktable.KTableSortComparator;
import de.kupzog.ktable.KTableSortOnClick;
import de.kupzog.ktable.SWTX;
import LiveLinkCore.LiveLinkNode;

/**
 * Observer that is observing a livelinkURLObservable
 * @author PASTOR Robert
 *
 */
public class LiveLinkNodeKTableCompositeObserver extends Composite implements Observer {

	private static final Logger logger = Logger.getLogger(LiveLinkNodeKTableCompositeObserver.class.getName());

	Composite parent = null;

	// table used to show children nodes of a folder node
	private KTable llNodeTable = null;

	private KTableCellSelectionListener kTableCellSelectionListener = null;
	private LiveLinkNodeKTableSortedModel model = null;

	
	LiveLinkNodeKTableCompositeObserver(Composite _parent) {
		super(_parent, SWT.FILL | SWT.BORDER);
		this.parent = _parent;

		this.setLayout(new FillLayout());

		this.llNodeTable = new KTable (this, SWTX.FILL_WITH_LASTCOL|SWTX.AUTO_SCROLL);
		this.model = new LiveLinkNodeKTableSortedModel(this, null);
		this.llNodeTable.setModel(model);
		this.llNodeTable.setLayout(new FillLayout());
		
		//this.recursiveLiveLinkTreeComposite.setLiveLinkNodeTable(this.llNodeTable, this.model);
		// the table is shown only when it is filled , hence only after a successful xmlexport => browser is retrieving an XML response (instead of an HTML one)
		this.llNodeTable.setVisible(false);

		// change the cursor when the mouse is placed over the table
		final Cursor cursor = new Cursor(this.parent.getDisplay(), SWT.CURSOR_ARROW);
		this.llNodeTable.addListener(SWT.MouseHover , new Listener() {
			public void handleEvent(Event e) {
				llNodeTable.setCursor(cursor);
			}
		});

		// implement resorting when the user clicks on the table header:
		kTableCellSelectionListener = new KTableSortOnClick(
				this.llNodeTable, 
				new LiveLinkNodeKTableSortComparatorImp(this.model, -1, KTableSortComparator.SORT_NONE));
		this.llNodeTable.addCellSelectionListener(kTableCellSelectionListener);

		this.llNodeTable.addCellResizeListener (
				new KTableCellResizeListener() {
					public void columnResized(int col, int newWidth) {
						//System.out.println("Column "+col+" resized to "+newWidth);
					}
					public void rowResized(int row, int newHeight) {
						//System.out.println("Row "+row+" resized to "+newHeight);
					}
				});

		this.llNodeTable.addCellSelectionListener(
				new KTableCellSelectionListener()
				{
					public void cellSelected(int col, int row, int statemask) {
						// the idea is to map the row index back to the model index since the given row index
						// changes when sorting is done. 
						//int modelRow = model.mapRowIndexToModel(row);
						//System.out.println("Cell ["+col+";"+row+"] selected. - Model row: "+modelRow);
					}

					public void fixedCellSelected(int col, int row, int statemask) {
						//System.out.println("Header ["+col+";"+row+"] selected.");
					}
				}
				);
		// allow column to be automatically resized using double click
		initExcelLikeTable();

	}

	/**
	 * called when the user clicks again a node that has been already browsed
	 * @param llNode
	 */
	public void updateTableView(LiveLinkNode llNode) {
		
		logger.log(Level.INFO," New table root node = "+ llNode.getName() + " number of children = " + llNode.getChildCount());
		
		if (this.llNodeTable.getModel() instanceof LiveLinkNodeKTableSortedModel) {
			
			logger.log(Level.INFO," set new table root node = "+ llNode.getName());

			this.model = (LiveLinkNodeKTableSortedModel) this.llNodeTable.getModel();
			this.model.setRootNode(llNode);

			this.llNodeTable.setModel(this.model);
			this.llNodeTable.setVisible(true);
			
			// implement resorting when the user clicks on the table header:
			this.llNodeTable.removeCellSelectionListener(kTableCellSelectionListener);
			kTableCellSelectionListener = new KTableSortOnClick(
					this.llNodeTable, 
					new LiveLinkNodeKTableSortComparatorImp(this.model, -1, KTableSortComparator.SORT_NONE));
			this.llNodeTable.addCellSelectionListener(kTableCellSelectionListener);

		}

	}
	
	private void initExcelLikeTable() {
		/**
		 *  Set Excel-like table cursors
		 */
		if ( SWT.getPlatform().equals("win32") ) {

			// Cross
			Image crossCursor = SWTX.loadImageResource(this.llNodeTable.getDisplay(), "/icons/cross_win32.gif");	
			// Row Resize	
			Image row_resizeCursor = SWTX.loadImageResource(this.llNodeTable.getDisplay(), "/icons/row_resize_win32.gif");	
			// Column Resize	
			Image column_resizeCursor  = SWTX.loadImageResource(this.llNodeTable.getDisplay(), "/icons/column_resize_win32.gif");

			// we set the hot-spot to the center, so calculate the number of pixels from hot-spot to lower border:	
			Rectangle crossBound        = crossCursor.getBounds();
			Rectangle rowresizeBound    = row_resizeCursor.getBounds();
			Rectangle columnresizeBound = column_resizeCursor.getBounds();

			Point crossSize        = new Point(crossBound.width/2, crossBound.height/2);
			Point rowresizeSize    = new Point(rowresizeBound.width/2, rowresizeBound.height/2);
			Point columnresizeSize = new Point(columnresizeBound.width/2, columnresizeBound.height/2);

			this.llNodeTable.setDefaultCursor(new Cursor(this.llNodeTable.getDisplay(), crossCursor.getImageData(), crossSize.x, crossSize.y), crossSize);
			this.llNodeTable.setDefaultRowResizeCursor(new Cursor(this.llNodeTable.getDisplay(), row_resizeCursor.getImageData(), rowresizeSize.x, rowresizeSize.y));
			this.llNodeTable.setDefaultColumnResizeCursor(new Cursor(this.llNodeTable.getDisplay(), column_resizeCursor.getImageData(), columnresizeSize.x, columnresizeSize.y));

		} 
	}

	@Override
	public void update(Observable observable, Object object) {

		if (observable instanceof LiveLinkNode) {

			LiveLinkNode llNode = (LiveLinkNode) observable;
			logger.log(Level.INFO, " ---------- observer received a notification ------------- " + llNode.getName() );

			updateTableView(llNode);

		}
	} 


}
