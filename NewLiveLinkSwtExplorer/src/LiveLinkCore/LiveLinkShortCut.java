package LiveLinkCore;

/**
 * this class manages the original node of a LiveLink shortcut
 * @author t0007330 Robert Pastor
 * @since July 2012
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
	
	/**
	 * 
	 * this method manages a LiveLink short cut.<br>
	 * 
	 *      - <llnode created="2011-08-05T11:43:33" createdby="12614" createdbyname="PINAULT alain" 
	 *     description="" id="34671158" modified="2011-08-05T11:43:33" name="SW WP Source" 
	 *     objname="Shortcut" objtype="1" ownedby="12614" ownedbyname="PINAULT alain" 
	 *     parentid="884642" size="0">
	 *       <Nickname domain="" /> 
	 *         <originalnode name="01. Guidelines & Procedures" path="THALES:Air Operations Division:
	 *         1-Business Lines - Domains:ATM (Air Traffic Management):
	 *         ATM Engineering:PI (Process & Tools):
	 *         Process:Industrial & Project Improvement:Books:Software Book:01. Guidelines & Procedures" subtype="0">483075</originalnode> 
	 *     </llnode>
	 */

	public String getShortCutNodeURL() {
		
		LiveLinkURLObservable llUrl = new LiveLinkURLObservable(this.originalNodeId);
		return llUrl.getXmlExportURL().toExternalForm();

	}
	
}
