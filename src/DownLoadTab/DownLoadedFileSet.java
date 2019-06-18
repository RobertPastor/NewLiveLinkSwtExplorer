package DownLoadTab;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jxl.Hyperlink;
import LiveLinkCore.LiveLinkNode;

/**
 * Files that have to be downloaded are listed in the following collection
 * @author t0007330
 * @since July 2012
 */
public class DownLoadedFileSet {

	private static final Logger logger = Logger.getLogger(DownLoadedFileSet.class.getName()); 

	private Map<String,LiveLinkNode> downLoadedFileSet = null;

	public DownLoadedFileSet(HyperLinkSet hyperLinkSet) {
		
		this.downLoadedFileSet = new HashMap<String,LiveLinkNode>();
		for (Map.Entry<String,Hyperlink> entry : hyperLinkSet.getHyperLinkMap().entrySet()) {

			Hyperlink hyperlink = entry.getValue();
			if (hyperlink.isFile()) {
				this.downLoadedFileSet.put(hyperlink.getFile().getAbsolutePath(), null);
			}
			if (hyperlink.isURL()) {
				URL url = hyperlink.getURL();
				if (url.toExternalForm().toLowerCase().contains("livelink")) {
					LiveLinkNode llNode = new LiveLinkNode(url);
					this.downLoadedFileSet.put(llNode.getXmlExportURL().toExternalForm(), llNode);
				}
				else {
					// null value are not allowed in a Hash table
					this.downLoadedFileSet.put(url.toExternalForm(),null);
				}
			}
		}
	}

	public URL getNextLiveLinkNodeToExplore() {
		
		if (this.downLoadedFileSet != null) {
			logger.log(Level.INFO,"Set is not empty: set size: "+this.downLoadedFileSet.size());
			for (Map.Entry<String, LiveLinkNode> entry : this.downLoadedFileSet.entrySet()) {
				LiveLinkNode llNode = entry.getValue();
				if (llNode != null) {
					if (llNode.isBrowsed() == false) {
						URL url = llNode.getXmlExportURL();
						return url;
					}
				}
			}
		}
		return null;
	}

	public LiveLinkNode getNextLiveLinkNodeToDownload() {
		logger.info("get next Node to download");
		if (this.downLoadedFileSet != null) {
			for (Map.Entry<String, LiveLinkNode> entry : this.downLoadedFileSet.entrySet()) {
				LiveLinkNode llNode = entry.getValue();
				if (llNode != null) {
					logger.log(Level.INFO,"llNode found: "+llNode.getName()+"...is Browsed: "+llNode.isBrowsed()+"...is Document: "+llNode.isDocument()+"...is downloaded: "+llNode.isDownLoaded());
					
					if (llNode.isBrowsed() && llNode.isDocument() && !llNode.isDownLoaded()) {
						URL url = llNode.getDownloadURL();
						logger.log(Level.INFO,"url returned: "+url.toExternalForm());
						return llNode;
					}
				}
			}
		}
		return null;
	}

	public void setLiveLinkNodeIsDownloaded(String liveLinkURL) {
		logger.log(Level.INFO,liveLinkURL);
		if (this.downLoadedFileSet != null) {
			if (this.downLoadedFileSet.containsKey(liveLinkURL)) {
				logger.log(Level.INFO,"map contains : "+liveLinkURL);
				LiveLinkNode llNode = this.downLoadedFileSet.get(liveLinkURL);
				if (llNode != null) {
					llNode.setDownLoaded(true);
				}
				//this.downLoadedFileSet.remove(liveLinkURL);
				this.downLoadedFileSet.put(liveLinkURL, llNode);
			}
			else {
				logger.log(Level.SEVERE,"The map does not contain: "+liveLinkURL);
			}
		}
	}

	public long size() {
		if (this.downLoadedFileSet != null)
		{
			return this.downLoadedFileSet.size();
		}
		return 0;
	}

	public String getHyperLink (int index) {

		//logger.log(Level.INFO,"index: "+index);
		if (this.downLoadedFileSet != null) {
			Set<String> set = this.downLoadedFileSet.keySet();

			List<Object> list = new ArrayList<Object>(set);
			Object[] objects = list.toArray(new String[set.size()]);
			if ((index >= 0 ) && (index < objects.length)) {

				Object object = objects[index];
				if (object instanceof String) {

					return (String)object;
				}
			}
		}
		return "";
	}

	public LiveLinkNode getLiveLinkNode(int index) {
		//logger.log(Level.INFO,"index: "+index);
		if (this.downLoadedFileSet != null) {
			Set<String> set = this.downLoadedFileSet.keySet();

			List<Object> list = new ArrayList<Object>(set);
			Object[] objects = list.toArray(new String[set.size()]);
			if ((index >= 0 ) && (index < objects.length)) {

				Object object = objects[index];
				if (object instanceof String) {
					LiveLinkNode llNode = this.downLoadedFileSet.get((String)object);
					return llNode;

				}
			}
		}
		return null;
	}

	public void update(final LiveLinkNode llNode) {
		logger.log(Level.INFO,"llNode: "+llNode.getName()+" is browsed: "+llNode.isBrowsed());
		URL url = llNode.getXmlExportURL();
		String  strUrl = url.toExternalForm();
		if (this.downLoadedFileSet != null) {
			if (this.downLoadedFileSet.containsKey(strUrl)) {
				LiveLinkNode llNodeToUpdate = this.downLoadedFileSet.get(strUrl);
				llNodeToUpdate.update(llNode);
				logger.log(Level.INFO,"map contains: "+strUrl+"... llNode will be updated: "+llNodeToUpdate.getName()+"...is document: "+llNodeToUpdate.isDocument());
				
				logger.log(Level.INFO,"size is now: "+this.downLoadedFileSet.size());
				this.downLoadedFileSet.put(strUrl, llNodeToUpdate);
				this.downLoadedFileSet.put(strUrl, llNode);
			}
		}
	}
	
	public void setLiveLinkNodeIsExplored(String liveLinkURL) {

		logger.log(Level.INFO,liveLinkURL);
		if (this.downLoadedFileSet != null) {
			if (this.downLoadedFileSet.containsKey(liveLinkURL)) {
				logger.log(Level.INFO,"map contains : "+liveLinkURL);
				LiveLinkNode llNode = this.downLoadedFileSet.get(liveLinkURL);
				if (llNode != null) {
					llNode.setBrowsed(true);
				}
				//this.downLoadedFileSet.remove(liveLinkURL);
				this.downLoadedFileSet.put(liveLinkURL, llNode);
			}
			else {
				logger.log(Level.SEVERE,"The map does not contain: "+liveLinkURL);
			}
		}
	}

}
