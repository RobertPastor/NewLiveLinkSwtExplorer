package com.javadude.antxr.sample;

public class LiveLinkRedirectURL {

	private String uri = "";
	
	public LiveLinkRedirectURL () {
		this.uri = "";
	}
	
	public void setURI (String uri) {
		this.uri = uri;
	}
	
	public String getURI () {
		return this.uri;
	}
	
	public void dump() {
		System.out.println("===========================");
		System.out.println("LiveLinkRedirectURL: uri: "+this.getURI());

		
	}
}
