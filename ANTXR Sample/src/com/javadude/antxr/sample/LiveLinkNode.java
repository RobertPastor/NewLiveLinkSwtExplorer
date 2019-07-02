package com.javadude.antxr.sample;


public class LiveLinkNode extends LiveLinkObject {
	
	//=====================================================================
	private String idAttribute = "id";
	private String Id = "";
	
	public String getId() {
		return Id;
	}
	
	public String getNodeId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}
	
	public String getIdAttribute() {
		return this.idAttribute;
	}
	
	//======================================================================
	private String nameAttribute = "name";
	private String name = "";
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getNameAttribute() {
		return nameAttribute;
	}

	//======================================================================
	private String createdDateAttribute = "created";
	private String createdDate = "";
	private LiveLinkDate nodeCreationDate = null;
	
	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
		this.nodeCreationDate = new LiveLinkDate(this.createdDate);
	}
	
	public String getCreatedDateAttribute() {
		return createdDateAttribute;
	}

	public LiveLinkDate getNodeCreationDate() {
		return nodeCreationDate;
	}
	
	//======================================================================
	private String createdByNameAttribute = "createdbyname";
	private String createdByName = "";
	
	public String getCreatedByNameAttribute() {
		return createdByNameAttribute;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	//=========================================================================
	private String descriptionAttribute = "description";
	private String description = "";
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescriptionAttribute() {
		return descriptionAttribute;
	}

	//====================================================
	private String mimeTypeAttribute = "mimetype";
	private String mimeType = "";

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getMimeTypeAttribute() {
		return mimeTypeAttribute;
	}

	public String getMimeType() {
		return mimeType;
	}

	
	//====================================================
	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	//====================================================
	private String parentIdAttribute = "parentid";
	private String parentId = "";

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	//====================================================

	private LiveLinkDate nodeModificationDate = null;
	private String modifiedDateAttribute = "modified";
	private String modifiedDate = "";

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
		this.nodeModificationDate = new LiveLinkDate(this.modifiedDate);
	}
	
	public String getModifiedDateAttribute() {
		return modifiedDateAttribute;
	}
	
	public LiveLinkDate getNodeModificationDate() {
		return nodeModificationDate;
	}

	//====================================================
	private String sizeAttribute = "size";
	private String size = "";

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	public String getSizeAttribute() {
		return sizeAttribute;
	}

	//====================================================
	private String ownedByNameAttribute = "ownedbyname";
	private String ownedByName = "";

	public String getOwnedByName() {
		return ownedByName;
	}

	public void setOwnedByName(String ownedByName) {
		this.ownedByName = ownedByName;
	}
	
	public String getOwnedByNameAttribute() {
		return ownedByNameAttribute;
	}

	//====================================================
	private boolean browsed = false;
	public boolean isBrowsed() {
		return browsed;
	}

	public void setBrowsed(boolean browsed) {
		this.browsed = browsed;
	}

	//====================================================


	//====================================================
	
	public String getObjectNameAttribute() {
		return objectNameAttribute;
	}

	public String getParentIdAttribute() {
		return parentIdAttribute;
	}









	
	//====================================================
	private String objectNameAttribute = "objname";
	private String objectName = "";




	//=========================================================================
	private LiveLinkNodeSet children = null;
	public LiveLinkNodeSet getChildren() {
		return children;
	}

	public void setChildren(LiveLinkNodeSet children) {
		this.children = children;
	}
	
	//=========================================================================
	public void dump() {
		System.out.println("==========");
		System.out.println(this.getIdAttribute()+": "+this.getId());
		System.out.println(this.getNameAttribute()+": "+this.getName());
		
		System.out.println(this.getCreatedDateAttribute()+": "+this.getCreatedDate());
		System.out.println(this.getNodeCreationDate().getDate().toLocaleString());
		
		System.out.println(this.getModifiedDateAttribute()+": "+this.getModifiedDate());
		System.out.println(this.getNodeModificationDate().getDate().toLocaleString());

		System.out.println(this.getDescriptionAttribute()+": "+this.getDescription());
		System.out.println(this.getSizeAttribute()+": "+this.getSize());
		
		System.out.println(this.getCreatedByNameAttribute()+": "+this.getCreatedByName());
		System.out.println(this.getOwnedByNameAttribute()+": "+this.getOwnedByName());

		System.out.println(this.getMimeTypeAttribute()+": "+this.getMimeType());
		System.out.println(this.getDescriptionAttribute()+": "+this.getDescription());

		System.out.println(this.getObjectNameAttribute()+": "+this.getObjectName());
		System.out.println(this.getParentIdAttribute()+": "+this.getParentId());

		
		if (children != null) {
			System.out.println("Size of sub node list: "+children.size());
		}
	}






	

	public boolean isLeaf() {
		if (this.objectName.equalsIgnoreCase("Document")) {
			return true;
		}
		return false;
	}


}
