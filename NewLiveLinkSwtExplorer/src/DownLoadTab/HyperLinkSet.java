package DownLoadTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jxl.Hyperlink;

/**
 * This class manages a set of hyper-links as extracted from a EXCEL sheet.
 * @since July 2012
 * @author Robert PASTOR
 *
 */
public class HyperLinkSet {

	private static final Logger logger = Logger.getLogger(HyperLinkSet.class.getName()); 

	/*
	 *  this is a map between a file hyperlink or an URL hyperlink
	 *   and the hyperlink as extracted from the sheet
	 *   both hyperlinks are JEXCEL API ones
	 */
	Map<String, Hyperlink> hyperLinkList = null;

	public Map<String, Hyperlink> getHyperLinkMap() {
		return this.hyperLinkList;
	}
	
	public int size() {
		if (this.hyperLinkList != null) {
			return this.hyperLinkList.size(); 
		}
		return 0;
	}

	public HyperLinkSet() {
		logger.setLevel(Level.SEVERE);
		this.hyperLinkList = new HashMap<String, Hyperlink>();
	}

	public void add(Hyperlink hyperlink) {
		if (this.hyperLinkList != null) {
			if (hyperlink.isFile()) {
				logger.log(Level.INFO,"hyperlink is file: "+hyperlink.getFile().getAbsolutePath());
				this.hyperLinkList.put(hyperlink.getFile().getAbsolutePath(),hyperlink);
			}
			if (hyperlink.isURL()) {
				logger.log(Level.INFO,"hyperlink is URL: "+hyperlink.getURL().toExternalForm());
				this.hyperLinkList.put(hyperlink.getURL().toExternalForm(),hyperlink);
			}
		}
	}

	public String getFileHyperLink (int index) {

		//logger.log(Level.INFO,"index: "+index);
		if (this.hyperLinkList != null) {
			Set<String> set = this.hyperLinkList.keySet();

			List<Object> list = new ArrayList<Object>(set);
			Object[] objects = list.toArray(new String[set.size()]);
			if ((index >= 0 ) && (index < objects.length)) {

				Object object = objects[index];
				if (object instanceof String) {
					Hyperlink hyperLink = this.hyperLinkList.get((String)object);
					if (hyperLink.isFile()) {
						return (String)object;
					}
				}
			}
		}
		return "";
	}

	public String getURLHyperLink (int index) {

		//logger.log(Level.INFO,"index: "+index);
		if (this.hyperLinkList != null) {
			Set<String> set = this.hyperLinkList.keySet();

			List<Object> list = new ArrayList<Object>(set);
			Object[] objects = list.toArray(new String[set.size()]);
			if ((index >= 0 ) && (index < objects.length)) {

				Object object = objects[index];
				if (object instanceof String) {
					Hyperlink hyperLink = this.hyperLinkList.get((String)object);
					if (hyperLink.isURL()) {
						return (String)object;
					}
				}
			}
		}
		return "";
	}
}
