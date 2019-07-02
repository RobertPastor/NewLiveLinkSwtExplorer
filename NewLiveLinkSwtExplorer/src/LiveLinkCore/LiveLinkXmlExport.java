package LiveLinkCore;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LiveLinkXmlExport {

	private static final Logger logger = Logger.getLogger(LiveLinkXmlExport.class.getName()); 

	private String AppVersion = "";
	private String Src = "";
	private LiveLinkNode liveLinkNode = null;
	private LiveLinkVolume liveLinkVolume = null;

	
	public String getAppVersion() {
		return this.AppVersion;
	}

	public void setAppVersion(String appVersion) {
		this.AppVersion = appVersion;
	}

	public String getSrc() {
		return this.Src;
	}

	public void setSrc(String src) {
		this.Src = src;
	}

	
	public LiveLinkNode getLiveLinkNode() {
		return this.liveLinkNode;
	}

	public void setLiveLinkNode(LiveLinkNode liveLinkNode) {
		this.liveLinkNode = liveLinkNode;
	}
	
	
	public LiveLinkVolume getLiveLinkVolume() {
		return liveLinkVolume;
	}

	public void setLiveLinkVolume(LiveLinkVolume liveLinkVolume) {
		logger.log(Level.INFO,"set LiveLinkVolume:"+liveLinkVolume.getName());
		this.liveLinkVolume = liveLinkVolume;
	}
	
}
