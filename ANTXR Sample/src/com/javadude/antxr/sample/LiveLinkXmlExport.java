package com.javadude.antxr.sample;


public class LiveLinkXmlExport {

	private String AppVersion = "";
	private String Src = "";
	private LiveLinkNode liveLinkNode = null;
	private LiveLinkVolume liveLinkVolume = null;
	
	public LiveLinkVolume getLiveLinkVolume() {
		return liveLinkVolume;
	}

	public void setLiveLinkVolume(LiveLinkVolume liveLinkVolume) {
		System.out.println("LiveLinkXmlExport: set LiveLinkVolume");
		this.liveLinkVolume = liveLinkVolume;
	}

	//===================================================
	public String getAppVersion() {
		return AppVersion;
	}

	public void setAppVersion(String appVersion) {
		AppVersion = appVersion.trim();
	}

	//==============================================
	public String getSrc() {
		return Src;
	}

	public void setSrc(String src) {
		Src = src.trim();
	}

	//==============================================
	public LiveLinkNode getLiveLinkNode() {
		return liveLinkNode;
	}

	public void setLiveLinkNode(LiveLinkNode liveLinkNode) {
		this.liveLinkNode = liveLinkNode;
	}
	
}
