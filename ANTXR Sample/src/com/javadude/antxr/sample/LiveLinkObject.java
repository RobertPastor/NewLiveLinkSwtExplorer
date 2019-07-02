package com.javadude.antxr.sample;

public class LiveLinkObject {

	//==============================================
	private LiveLinkShortCut llShortCut = null;
	
	
	public LiveLinkShortCut getLiveLinkShortCut() {
		return this.llShortCut;
	}

	public void setLiveLinkShortCut(LiveLinkShortCut llShortCut) {
		this.llShortCut = llShortCut;
	}
	//================================================
	private LiveLinkRedirectURL llRedirectURL = null;
	
	public void setLiveLinkRedirectURL (LiveLinkRedirectURL llRedirectURL) {
		this.llRedirectURL = llRedirectURL;
	}
	
	public LiveLinkRedirectURL getLiveLinkRedirectURL() {
		return this.llRedirectURL;
	}
	
	public LiveLinkObject() {
		
	}
}
