package com.javadude.antxr.sample;

public class LiveLinkVolume {
	
	//=========================================================================
	private LiveLinkNodeSet children = null;
	public LiveLinkNodeSet getChildren() {
		return children;
	}

	public void setChildren(LiveLinkNodeSet children) {
		System.out.println("LiveLinkVolume: set Children");
		this.children = children;
	}
	
	//=========================================================================
	private String Name = "";
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}
	
	public void dump() {
		System.out.println("LiveLinkVolume: name: "+Name);
		if (children != null) {
			children.dump();
		}
	}
	

}
