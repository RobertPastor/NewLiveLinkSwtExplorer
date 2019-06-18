package RecursiveLiveLinkBrowser;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import LiveLinkCore.LiveLinkNode;
import de.kupzog.ktable.KTableSortComparator;
/**
 * This class is responsible for sorting the columns of the LiveLink Table.
 * @since December 2015
 * @author t0007330
 **/
public class LiveLinkNodeKTableSortComparatorImp extends KTableSortComparator {

	private static final Logger logger = Logger.getLogger(LiveLinkNodeKTableSortComparatorImp.class.getName());

	private LiveLinkNodeKTableSortedModel model = null;

	public LiveLinkNodeKTableSortComparatorImp(
			LiveLinkNodeKTableSortedModel _model,
			int columnIndex, 
			int direction) {
		
		super(_model, columnIndex, direction);
		this.model  = _model;
	}

	@Override
	public int doCompare(Object o1, Object o2, int row1, int row2) {

		row1 = (row1-1);
		row2 = (row2-1);
		int columnToSortOn = this.getColumnToSortOn();
		logger.log(Level.INFO, "col to sort= "+columnToSortOn+" row 1= "+ row1 + " row 2= "+ row2 + " o1= "+ o1 + " o2= "+ o2);

		LiveLinkNode rootNode = this.model.getRootNode();
		if (rootNode.hasChildren()) {
			if (columnToSortOn == 0) {
				long l1 = new Long(row1);
				long l2 = new Long(row2);

				if (l1<l2) return -1;
				if (l2<l1) return +1;
			}
			
			if (columnToSortOn == 1) {
				String name1 = rootNode.getChild(row1).getName();
				String name2 = rootNode.getChild(row2).getName();
				int res = String.CASE_INSENSITIVE_ORDER.compare(name1, name2);

				if (res < 0) return -1;
				if (res > 0) return +1;
			}
			if (columnToSortOn == 2) {
				String name1 = rootNode.getChild(row1).getObjectName();
				String name2 = rootNode.getChild(row2).getObjectName();
				int res = String.CASE_INSENSITIVE_ORDER.compare(name1, name2);

				if (res < 0) return -1;
				if (res > 0) return +1;
			}
			if (columnToSortOn == 3) {
				String name1 = rootNode.getChild(row1).getDescription();
				String name2 = rootNode.getChild(row2).getDescription();
				int res = String.CASE_INSENSITIVE_ORDER.compare(name1, name2);

				if (res < 0) return -1;
				if (res > 0) return +1;
			}
			if (columnToSortOn == 4) {
				long l1 = new Long(rootNode.getChild(row1).getLongSize());
				long l2 = new Long(rootNode.getChild(row2).getLongSize());
				
				if (l1<l2) return -1;
				if (l2<l1) return +1;
			}
			if (columnToSortOn == 5) {
				Date d1 = rootNode.getChild(row1).getNodeCreationDate().getDate();
				Date d2 = rootNode.getChild(row2).getNodeCreationDate().getDate();

				if (d1.before(d2)) return -1;
				if (d1.after(d2)) return +1;
			}
			if (columnToSortOn == 6) {
				Date d1 = rootNode.getChild(row1).getNodeModificationDate().getDate();
				Date d2 = rootNode.getChild(row2).getNodeModificationDate().getDate();

				if (d1.before(d2)) return -1;
				if (d1.after(d2)) return +1;
			}
			if (columnToSortOn == 7) {
				String name1 = rootNode.getChild(row1).getOwnedByName();
				String name2 = rootNode.getChild(row2).getOwnedByName();
				int res = String.CASE_INSENSITIVE_ORDER.compare(name1, name2);

				if (res < 0) return -1;
				if (res > 0) return +1;
			}
			if (columnToSortOn == 8) {
				String name1 = rootNode.getChild(row1).getMimeType();
				String name2 = rootNode.getChild(row2).getMimeType();
				int res = String.CASE_INSENSITIVE_ORDER.compare(name1, name2);

				if (res < 0) return -1;
				if (res > 0) return +1;
			}
		}
		return 0;
	}

}
