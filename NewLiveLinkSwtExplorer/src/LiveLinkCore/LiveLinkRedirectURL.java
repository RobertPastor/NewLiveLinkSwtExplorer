package LiveLinkCore;

/**
 * Manages a specific variant of LiveLink node corresponding to an URL
 * This LiveLink node is pointing to an uri.
 * 
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
 * 
 * @since July 2012
 * @author t0007330
 *
 */
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
