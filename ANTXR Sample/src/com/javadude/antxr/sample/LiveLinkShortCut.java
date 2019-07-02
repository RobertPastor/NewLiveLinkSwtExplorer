package com.javadude.antxr.sample;

/**
 * this class manages the original node of a Livelink shortcut
 * @author t0007330
 *
 */
public class LiveLinkShortCut {
	
	private String originalNodeId = "";
	private String name = "";
	private String path = "";

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOriginalNodeId() {
		return originalNodeId;
	}

	public void setOriginalNodeId(String originalNodeId) {
		this.originalNodeId = originalNodeId;
	}

	public void dump() {
		System.out.println("LiveLinkShortCut: original node id: "+this.getOriginalNodeId());
		System.out.println("LiveLinkShortCut: name: "+this.getName());
		System.out.println("LiveLinkShortCut: path: "+this.getPath());
		
	}
	
}
