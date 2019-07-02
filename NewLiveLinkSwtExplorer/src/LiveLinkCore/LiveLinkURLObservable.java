package LiveLinkCore;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for managing the LiveLink specific URL formatting<br>
 * both for XML Export and for Download.
 * 
 * http://Livelink/livelink.exe?func=ll&objId=331786&amp;objAction=XMLExport&scope=one&amp;stylesheet=xsl/nameofstylesheet.xsl&amp;transform&requestcontext
 * http://atmosphere.tatm.thales/livelink/livelink.exe?func=ll&objId=5562136&objAction=xmlexport&stylesheet=36622805&transform
 * 
 * https://ecm.corp.thales/livelink/livelink.exe?func=ll&objId=9925318&objAction=browse&viewType=1
 * 
 * Observable - means that 
 * 
 * @author t0007330
 * @author Robert Pastor
 * @since March 2012
 * 
 */
public class LiveLinkURLObservable extends Observable {
	
	private static final Logger logger = Logger.getLogger(LiveLinkURLObservable.class.getName()); 

	protected URL initialURL = null;
	
	protected URL xmlExportURL = null;
	protected URL downloadURL = null;
	protected URL openURL = null;
	protected URL browseURL = null;
	
	protected String LiveLinkObjectId = "";
	
	public String getLiveLinkObjectId() {
		return this.LiveLinkObjectId;
	}

	protected String LiveLinkServer = "http://atmosphere.tatm.thales/livelink/livelink.exe?func=ll";
	//protected String LiveLinkServer = "https://ecm.corp.thales/livelink/livelink.exe?func=ll";
	
	final String objActionDownload  = "&objAction=Download";
	final String objActionXmlExport = "&objAction=XMLExport";
	final String objActionOpen      = "&objAction=Open";
	final String objActionBrowse    = "&objAction=Browse";
	
	public LiveLinkURLObservable(URL initialURL) {
		logger.setLevel(Level.SEVERE);
		this.initialURL = initialURL;
		logger.log(Level.INFO, "initial URL: "+this.initialURL.toExternalForm());
		this.LiveLinkObjectId = extractLLobjectId(this.initialURL.toString());
		initFromObjectId(this.LiveLinkObjectId);
		logger.log(Level.INFO,"llObjectId: "+this.LiveLinkObjectId);
	}
	
	/**
	 * initializes a LiveLink URL with an Object Identifier
	 * @param llObjectId
	 */
	public LiveLinkURLObservable(String llObjectId) {
		this.LiveLinkObjectId = llObjectId;
		initFromObjectId(this.LiveLinkObjectId);
		this.initialURL = this.xmlExportURL;
	}
	
	public LiveLinkURLObservable () {
		
	}
	
	public void setInitialURL(URL initialURL) {
		logger.setLevel(Level.SEVERE);
		this.initialURL = initialURL;
		logger.log(Level.INFO,"initial URL: "+this.initialURL.toExternalForm());
		this.LiveLinkObjectId = extractLLobjectId(this.initialURL.toString());
		initFromObjectId(this.LiveLinkObjectId);
		logger.log(Level.INFO,"llObjectId: "+this.LiveLinkObjectId);
	}
	
	public URL getInitialURL() {
		return this.initialURL;
	}
	
	private void initFromObjectId(String llObjectId) {
		try {
			String strXmlExportURL = LiveLinkServer+"&objId="+llObjectId+objActionXmlExport;
			String strDownloadURL  = LiveLinkServer+"&objId="+llObjectId+objActionDownload;
			String strOpenURL      = LiveLinkServer+"&objId="+llObjectId+objActionOpen;
			String strBrowseURL    = LiveLinkServer+"&objId="+llObjectId+objActionBrowse;
			
			this.xmlExportURL = new URL(strXmlExportURL);
			this.downloadURL = new URL(strDownloadURL);
			this.openURL = new URL(strOpenURL);
			this.browseURL = new URL(strBrowseURL);
		}
		
		catch (MalformedURLException ex) {
			System.err.println("LiveLinkURL: "+ex.getMessage());
		}
	}
	
	public URL getXmlExportURL() {
		if (this.xmlExportURL == null) {
			
			try {
				this.xmlExportURL = new URL(LiveLinkServer+"&objId="+LiveLinkObjectId+objActionXmlExport);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return this.xmlExportURL;
	}
	/**
	 * returns an URL telling Livelink to download the file
	 * @return
	 */
	public URL getDownloadURL() {
		return this.downloadURL;
	}
	
	public URL getBrowseURL () {
		return this.browseURL;
	}
	
	public URL getOpenURL() {
		return this.openURL;
	}

	/**
	 * extract the set of digits forming the LiveLink Object Id
	 * @param httpPath
	 * @return
	 */
	private String extractLLobjectId(String httpPath) {

		//System.out.println("extract LL Obj Id: "+httpPath);

		String LLobjectId = "";
		String searchStr1 = "&objId=";
		String searchStr2 = "&objAction";

		int pos1 = httpPath.indexOf(searchStr1);
		int pos2 = httpPath.indexOf(searchStr2);

		if ((pos1 > 0) && (pos2 >0)) {
			LLobjectId = httpPath.substring((pos1+searchStr1.length()), pos2);
			int index = 0;
			while (index < LLobjectId.length()) {
				int i = LLobjectId.charAt(index);
				if ((i < 48) || (i > 57)) {
					return "";
				}
				index++;
			}
			//System.out.println("LiveLinkObject: LL Object Id: "+LLobjectId);
			return LLobjectId;
		}
		else {
			searchStr1 = "&nodeId=";
			pos1 = httpPath.indexOf(searchStr1);
			if (pos1 >0) {
				//System.out.println("LL Object Id: node Id found "+pos1);
				String temp = httpPath.substring((pos1+searchStr1.length()));
				//System.out.println("LL Object Id: temp "+temp);
				int index = 0;
				while (index < temp.length()) {
					int i = temp.charAt(index);
					//System.out.println("LL Object Id: "+i);
					if ((i >= 48) && (i <= 57)) {
						LLobjectId = LLobjectId + temp.charAt(index);
					}
					if (temp.charAt(index) == '&') {
						break;
					}
					index++;
				}
			}
			//System.out.println("LiveLinkObject: LL Object Id: "+LLobjectId);
			return LLobjectId;
		}
	}


}

