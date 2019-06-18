package LiveLinkCore;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;




/**
 * This class manages the set of nodes.<br>
 * It uses a TreeSet to retrieve in a sorted order the nodes through the iterator
 * @author Robert PASTOR
 * @since January 2009
 *
 */
public class LiveLinkNodeSet {

	/**
	 * we use here a Tree Set to retrieve through the iterator only sorted values
	 */
	
	private static final Logger logger = Logger.getLogger(LiveLinkNodeSet.class.getName());

	private TreeSet<LiveLinkNode> children = null;
	private LiveLinkNode root = null;
	private LiveLinkVolume volume = null;
	
	public LiveLinkVolume getVolume() {
		return volume;
	}

	private class MyComparator implements Comparator<LiveLinkNode> {

		public int compare(LiveLinkNode o1, LiveLinkNode o2) {
			return o1.getName().compareTo(o2.getName());
		}
	}
	
	public LiveLinkNodeSet (LiveLinkNode root) {
		this.children = new TreeSet<LiveLinkNode>(new MyComparator());
		this.root = root;
	}
	
	public LiveLinkNodeSet (LiveLinkVolume aVolume) {
		this.children = new TreeSet<LiveLinkNode>(new MyComparator());
		this.volume = aVolume;
	}
	
	public int size() {
		return this.children.size();
	}
	
	public void add (LiveLinkNode node) {
		//System.out.println("LiveLinkNodeSet: add Node: "+node.getName());
		this.children.add(node);
	}
	
	public Iterator<LiveLinkNode> iterator() {
		return this.children.iterator();
	}
	
	public LiveLinkNode get(int index) {
		int i = 0;
		Iterator<LiveLinkNode> Iter = this.children.iterator();
		while (Iter.hasNext()) {
			LiveLinkNode node = Iter.next();
			if (i == index) {
				return node;
			}
			i++;
		}
		return null;
	}
	
	public int getIndexOfChild(LiveLinkNode child) {
		logger.log(Level.INFO," Child: "+child.getName());
		Iterator<LiveLinkNode> Iter = this.children.iterator();
		int i = 0;
		while (Iter.hasNext()) {
			LiveLinkNode node = Iter.next();
			if (node.getId().equalsIgnoreCase(child.getId()) ) {
				//System.out.println("----------getIndexOfChild-------- "+child.Name+" --- "+i+" ------");
				return i;
			}
			i++;
		}
		return 0;
	}
	
	/**
	 * return the parent of a LiveLinkNode
	 * @return
	 */

	public LiveLinkNode getParent() {
		return this.root;
	}
	
	public boolean hasSubFolders() {
		if (this.size() > 0) {		
			Iterator<LiveLinkNode> iter = this.children.iterator();
			while (iter.hasNext()) {
				LiveLinkNode llNode = iter.next();
				if (llNode.isLeaf() == false) {
					return true;
				}
			}
		}
		// if size is 0 then return false
		return false;
	}
	
	public void dump() {
		System.out.println("LiveLinkNodeSet: dump: size: "+this.children.size());
	}
	
}
