package JExcelApi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import LiveLinkCore.LiveLinkNode;

/**
 * @since May 2012
 * @author t0007330 Robert Pastor
 *
 */

public class LiveLinkNodeTreeSet {


	private List<LiveLinkNodeExcelRecord> liveLinkNodeTreeSet = null;
	// this is used to compute the depth of the indented tree 
	// at output time in the EXCEL file
	private int MaxDepth = 0;
	private long CountOfDocuments = 0;

	private long sizeOfDocuments = 0;
	private long CountOfFolders = 0;
	
	public int getMaxDepth() {
		return MaxDepth;
	}
	
	public LiveLinkNodeTreeSet() {
		this.MaxDepth = 0;
		this.liveLinkNodeTreeSet = new ArrayList<LiveLinkNodeExcelRecord>();
	}
	
	public int size() {
		return this.liveLinkNodeTreeSet.size();
	}
	
	public void add (LiveLinkNodeExcelRecord excelRecord) {
		System.out.println("LiveLinkNodeTreeSet: add Node: "+excelRecord.getIndex()+" path: "+excelRecord.getStrParentPath());
		this.liveLinkNodeTreeSet.add(excelRecord);
		LiveLinkNode llNode = excelRecord.getLLNode();
		if (llNode.isLeaf() == true ) {
			this.CountOfDocuments++;
			this.sizeOfDocuments += llNode.getLongSize();
		}
		else {
			this.CountOfFolders++;
		}
		this.MaxDepth = Math.max(this.MaxDepth,excelRecord.getTreeDepth());
		//System.out.println("LiveLinkNodeTreeSet: add Node: "+b);
	}
	
	public Iterator<LiveLinkNodeExcelRecord> iterator() {
		return this.liveLinkNodeTreeSet.iterator();
	}
	/**
	 * returns the number of Documents that are in the set.
	 * @return
	 */
	public long getCountOfDocuments() {
		return CountOfDocuments;
	}
	
	/**
	 * returns the size in Bytes of the whole document set.
	 * @return
	 */
	public long getSizeOfDocuments() {
		return sizeOfDocuments;
	}

	/**
	 * returns the count of Folders 
	 * (Project,Folder,Community,Project Template, Compound Document) that are in the set.
	 * @return
	 */
	public long getCountOfFolders() {
		return CountOfFolders;
	}
}
