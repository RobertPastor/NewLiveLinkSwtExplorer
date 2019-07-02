package FileExplorerTab;

import java.io.File;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.filechooser.FileSystemView;

import de.kupzog.ktable.KTableSortComparator;

public class KTableFilesSortComparatorImp extends KTableSortComparator {

	private static final Logger logger = Logger.getLogger(KTableFilesSortComparatorImp.class.getName());
	private FileKTableSortedModel model = null; 
	private FileSystemView fsview = null;

	public KTableFilesSortComparatorImp(FileKTableSortedModel model, int columnIndex, int direction) {
		// column Index is the initial sorting
		super(model, columnIndex, direction);
		this.model = model;
		fsview = FileSystemView.getFileSystemView();
		logger.setLevel(Level.OFF);
	}

	@Override
	public int doCompare(Object o1, Object o2, int row1, int row2) {

		row1 = (row1-1);
		row2 = (row2-1);
		logger.log(Level.INFO, "row 1= "+ row1 + " row2= "+ row2 + " o1= "+o1 + " o2= "+o2);
		int columnToSortOn = this.getColumnToSortOn();
		
		File[] files = this.model.getFiles();
		if (columnToSortOn == 0) {
			
			long l1 = new Long(row1);
			long l2 = new Long(row2);

			if (l1<l2) return -1;
			if (l2<l1) return +1;
			
		}
		if (columnToSortOn == 1) {
			String name1 = files[row1].getName();
			String name2 = files[row2].getName();
			int res = String.CASE_INSENSITIVE_ORDER.compare(name1, name2);

			if (res < 0) return -1;
			if (res > 0) return +1;
		}
		if (columnToSortOn == 2) {
			long l1 = new Long(files[row1].length());
			long l2 = new Long(files[row2].length());

			if (l1<l2) return -1;
			if (l2<l1) return +1;
		}
		if (columnToSortOn == 3) {
			Date d1 = new Date(files[row1].lastModified());
			Date d2 = new Date(files[row2].lastModified());

			if (d1.before(d2)) return -1;
			if (d1.after(d2)) return +1;
		}
		if (columnToSortOn == 4) {
			String name1 = getSystemTypeDescription(files[row1]);
			String name2 = getSystemTypeDescription(files[row2]);
			int res = String.CASE_INSENSITIVE_ORDER.compare(name1, name2);

			if (res < 0) return -1;
			if (res > 0) return +1;
		}
		return 0;
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

}
