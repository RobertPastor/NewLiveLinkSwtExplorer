package LiveLinkCore;

import java.net.URL;


public class LiveLinkObject extends LiveLinkURLObservable {

	// the order of the object is linked to the Livelink Object type.
	private int[]    llObjectTypes = { 0        ,  1         , 140   ,  144  , 
			146          ,  207        , 3030202     , 3030339 };
	
	public int[] getLlObjectTypes() {
		return llObjectTypes;
	}

	public String[] getLLEnglishObjectNames() {
		return llEnglishObjectNamesSet;
	}
	
	private String [] llEnglishObjectNamesSet =       { 
			"Folder"        		, 	"Project"        	, 	"Shortcut"        
		, 	"Channel"           	,	"Community"         , 	"URL"         		, 	"Custom View"        
		, 	"Comm Expert Group" 	, 	"Comm Store"        ,	"Comm Store URL"	,	"News"  		, 	"Document" 
		, 	"Compound Document" 	, 	"Virtual Folder"	, 	"MailBox"
		
	};
	
	public LiveLinkObject() {
		super();
	}
	
	public LiveLinkObject(URL initialURL) {
		super(initialURL);
	}
	
	//==============================================
	private String objectNameAttribute = "objname";
	protected String objectName = "";

	public String getObjectNameAttribute() {
		return objectNameAttribute;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	//================================================
	/**
	 *   <?xml version="1.0" encoding="UTF-8" ?> 
	 *   - <livelink appversion="9.7.1" src="XmlExport">
	 *   - <llnode created="2008-08-06T11:29:08" createdby="12614" 
	 *   createdbyname="PINAULT alain" description="" id="18383350" 
	 *   modified="2009-05-07T16:13:18" name="EUROWIKAT" 
	 *   objname="URL" objtype="140" ownedby="12614" 
	 *   ownedbyname="PINAULT alain" parentid="3535723" size="0">
	 *     <Nickname domain="" /> 
	 *       <uri>http://wiki.corp.thales/display/ASDEurocat/Eurocat</uri> 
	 *         </llnode>
	 *           </livelink>
	 */
	protected LiveLinkRedirectURL llRedirectURL = null;

	public void setLiveLinkRedirectURL (LiveLinkRedirectURL llRedirectURL) {
		this.llRedirectURL = llRedirectURL;
	}

	public LiveLinkRedirectURL getLiveLinkRedirectURL() {
		return this.llRedirectURL;
	}

	public boolean isURL() {
		try {
			if (objectName.equalsIgnoreCase("URL")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	//==========================================================

	public boolean isChannel() {
		try {
			if (objectName.equalsIgnoreCase("Channel") || objectName.equalsIgnoreCase("Canal")) {
				return true;
			}
		} catch(Exception e) {
			return false;
		}
		return false;
	}

	public boolean isCommunity() {
		try {
			if (objectName.equalsIgnoreCase("Community") || objectName.equalsIgnoreCase("Communauté")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public boolean isCommunityExpertContainer() {
		try {
			if (objectName.equalsIgnoreCase("Comm Expert Container")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	// Community Expert Group 
	// Community Expert Group

	public boolean isCommunityExpertGroup() {
		try {
			if (objectName.equalsIgnoreCase("Comm Expert Group")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	// Community Store
	public boolean isCommunityStore() {
		try {
			if (objectName.equalsIgnoreCase("Comm Store")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public boolean isQuestion() {
		try {
			if (objectName.equalsIgnoreCase("Question")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public boolean isCustomView () {
		try {
			if (objectName.equalsIgnoreCase("Custom View") || objectName.equalsIgnoreCase("Affichage personnalisé")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public boolean isNews() {
		try {
			if (objectName.equalsIgnoreCase("News") || objectName.equalsIgnoreCase("Nouvelles")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public boolean isCompoundDocument() {
		try {
			if (objectName.equalsIgnoreCase("Compound Document") || objectName.equalsIgnoreCase("Document Composite")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false; 
	}

	public boolean isProjectTemplate() {
		try {
			if (objectName.equalsIgnoreCase("Project Template") || objectName.equalsIgnoreCase("Projet Template")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false; 
	}

	public boolean isDocument() {
		try {
			if (objectName.equalsIgnoreCase("Document")) {
				return true;
			}
		} catch (NullPointerException e){
            System.err.println ( e.getStackTrace() );
		}
		return false;
	}

	//==========================================================
	private LiveLinkShortCut llShortCut = null;

	public LiveLinkShortCut getLiveLinkShortCut() {
		return this.llShortCut;
	}

	public void setLiveLinkShortCut(LiveLinkShortCut llShortCut) {
		this.llShortCut = llShortCut;
	}

	public boolean isShortCut() {
		try {
			if (objectName.equalsIgnoreCase("Shortcut") || objectName.equalsIgnoreCase("Raccourci")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public String getShortCutNodeURL() {

		if (this.isShortCut()) {
			if (this.getLiveLinkShortCut() != null) {
				LiveLinkShortCut llShortCut = this.getLiveLinkShortCut();
				return llShortCut.getShortCutNodeURL();
			}
		}
		return "";
	}

	//===========================================================
	public boolean isProject() {
		try {
			if (objectName.equalsIgnoreCase("Project") || objectName.equalsIgnoreCase("Projet")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public boolean isFolder() {
		try {
			if (objectName.equalsIgnoreCase("Folder") || objectName.equalsIgnoreCase("Dossier")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public boolean isDiscussion() {
		try {
			if (objectName.equalsIgnoreCase("Discussion")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	// Comm URL Store
	public boolean isCommunityURLStore() {
		try {
			if (objectName.equalsIgnoreCase("Comm URL Store")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * this kind of node has no childrens.
	 * @return
	 */
	public boolean isLeaf() {

		// 23rd July if Custom View then it is a leaf...
		if (isDocument() || isURL() || isShortCut() || isCustomView()) {
			return true;
		}
		return false;
	}

}
