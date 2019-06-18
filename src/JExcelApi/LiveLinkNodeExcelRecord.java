package JExcelApi;

import LiveLinkCore.LiveLinkNode;

public class LiveLinkNodeExcelRecord implements Comparable<LiveLinkNodeExcelRecord> {


	private int index = 0;
	public int getIndex() {
		return index;
	}

	private int treeDepth = 0;
	public int getTreeDepth() {
		return treeDepth;
	}

	public String getStrParentPath() {
		return strParentPath;
	}

	public LiveLinkNode getLLNode() {
		return llNode;
	}

	private String strParentPath = "";
	private LiveLinkNode llNode = null;

	public LiveLinkNodeExcelRecord (int _index, int _treeDepth, String _strParentPath, LiveLinkNode _llNode) {
		this.index = _index;
		this.treeDepth = _treeDepth;
		this.strParentPath = _strParentPath;
		this.llNode = _llNode;

	}

	@Override
	public int compareTo(LiveLinkNodeExcelRecord o) {
		if (o instanceof LiveLinkNodeExcelRecord) {
			return (((LiveLinkNodeExcelRecord)o).index - this.index);
		}
		return 0;
	}
}
