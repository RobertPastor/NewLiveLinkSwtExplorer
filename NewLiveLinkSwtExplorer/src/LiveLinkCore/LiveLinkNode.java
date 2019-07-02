package LiveLinkCore;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * This class manages the specific data structure of a Livelink node<br>
 * and a set of Live Link nodes related to this one.
 * 
 * @author Robert PASTOR
 * @since April 2012
 * <br>
 *    <?xml version="1.0" encoding="UTF-8" ?> 
 *    - <livelink appversion="9.7.1" src="XmlExport">
 *      - <llnode created="2008-06-03T18:03:11" createdby="12368" createdbyname="HEINRICH corinne" 
 *      	description="" id="17080010" mimetype="video/x-msvideo" modified="2008-06-03T18:04:09" 
 *      	name="EUROCAT TOWER.avi" objname="Document" objtype="144" ownedby="12368" 
 *      	ownedbyname="HEINRICH corinne" parentid="2768663" size="283583470" versionnum="1">
 *      	<Nickname domain="" /> 
 *      </llnode>
 *     </livelink>
 *     
 *     - <llnode created="2011-08-05T11:43:33" createdby="12614" createdbyname="PINAULT alain" 
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
public class LiveLinkNode extends LiveLinkObject {

	private static final Logger logger = Logger.getLogger(LiveLinkNode.class.getName());

	private String strXML = "";
	private int depth = 0;
	//private Element element = null;

	public int getDepth() {
		return depth;
	}

	//=================================================================
	private String createdDateAttribute = "created";
	private String createdDate = "";
	private LiveLinkDate nodeCreationDate = null; 

	public void setCreatedDate(String _createdDate) {
		if (_createdDate != null) {
			this.createdDate = _createdDate;
			this.nodeCreationDate = new LiveLinkDate(this.createdDate);
		}
		else {
			this.createdDate = "";
			// initialize with a default date = now().
			this.nodeCreationDate = new LiveLinkDate();
		}
	}

	public String getCreatedDate() {
		return this.createdDate;
	}

	public LiveLinkDate getNodeCreationDate() {
		return this.nodeCreationDate;
	}

	public String getCreatedDateAttribute() {
		return this.createdDateAttribute;
	}

	//==================================================================

	private String createdByNameAttribute = "createdbyname";
	private String createdByName = "";

	public void setCreatedByName(String _createdByName) {
		if (_createdByName != null) {
			this.createdByName = _createdByName;
		}
		else {
			this.createdByName = "";
		}
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public String getCreatedByNameAttribute() {
		return createdByNameAttribute;
	}

	//==================================================================
	private String descriptionAttribute = "description";
	private String description = "";

	public void setDescription(String _description) {
		if (_description != null) {
			this.description = _description.trim();
		}
		else {
			this.description = "";
		}
	}

	public String getDescriptionAttribute() {
		return descriptionAttribute;
	}

	public String getDescription() {
		return this.description;
	}

	//==================================================================
	private String idAttribute = "id";

	public String getIdAttribute() {
		return this.idAttribute;
	}

	public void setId(String _id) {
		if (_id != null) {
			this.LiveLinkObjectId = _id;
		}
		else {
			this.LiveLinkObjectId = "";
		}
	}

	public String getId() {
		return LiveLinkObjectId;
	}

	//==================================================================
	private String mimeTypeAttribute = "mimetype";
	private String mimeType = "";

	public void setMimeType(String _mimeType) {
		if (_mimeType != null) {
			this.mimeType = _mimeType;
		}
		else {
			this.mimeType = "";
		}
	}

	public String getMimeTypeAttribute() {
		return this.mimeTypeAttribute;
	}

	public String getMimeType() {
		return this.mimeType;
	}

	//==================================================================
	private String parentIdAttribute = "parentid";
	private String parentId = "";


	public void setParentId(String _parentId) {
		if (_parentId != null) {	
			this.parentId = _parentId;
		}
		else {
			this.parentId = "";
		}
	}

	public String getParentIdAttribute() {
		return parentIdAttribute;
	}

	public String getParentId() {
		return parentId;
	}

	//==================================================================
	private String nameAttribute = "name";
	private String name = "";

	public String getName() {
		return name;
	}

	public void setName(String _name) {
		if (_name != null) {
			this.name = _name.trim();
		}
		else {
			this.name = "";
		}
	}

	public String getNameAttribute() {
		return nameAttribute;
	}

	//==================================================================
	private String modifiedDateAttribute = "modified";
	private String modifiedDate = "";
	private LiveLinkDate nodeModificationDate = null;

	public void setModifiedDate(String _modifiedDate) {
		if (_modifiedDate != null) {
			this.modifiedDate = _modifiedDate;
			this.nodeModificationDate = new LiveLinkDate(this.modifiedDate);
		}
		else {
			this.modifiedDate = "";
			this.nodeModificationDate = new LiveLinkDate();
		}
	}

	public String getModifiedDateAttribute() {
		return modifiedDateAttribute;
	}

	public LiveLinkDate getNodeModificationDate() {
		return nodeModificationDate;
	}

	public String getModifiedDate() {
		return this.modifiedDate;
	}

	//==========================================================
	private String sizeAttribute = "size";
	private String size = "";

	public String getSize() {
		return size;
	}

	public void setSize(String _size) {
		if (_size != null) {
			this.size = _size;
		}
		else {
			this.size = "";
		}
	}

	public String getStringSize() {
		return this.size;
	}

	/**
	 * returns the size of a LiveLink node that is either a count of files or the size of a file in Bytes.
	 * @return
	 */
	public long getLongSize() {
		try {
			return Long.parseLong(this.size);
		}
		catch (NumberFormatException ex) {
			return 0;
		}
	}

	public String getSizeAttribute() {
		return this.sizeAttribute;
	}

	//==========================================================
	private String ownedByNameAttribute = "ownedbyname";
	private String ownedByName = "";

	public void setOwnedByName(String _ownedByName) {
		if (_ownedByName != null) {
			this.ownedByName = _ownedByName;
		}
		else {
			this.ownedByName = "";
		}
	}

	public String getOwnedByNameAttribute() {
		return this.ownedByNameAttribute;
	}

	public String getOwnedByName() {
		return this.ownedByName;
	}

	//==========================================================
	// children of this node
	private LiveLinkNodeSet children = null;

	public void setChildren(LiveLinkNodeSet _children) {
		//logger.log(Level.INFO,"number of children: "+String.valueOf(children.size()));

		if (_children != null) {
			this.children = _children;
		}
	}

	public void setChildren (LiveLinkNode llNode) {
		//logger.log(Level.INFO,llNode.name);
		if (this.isEqual(llNode)) {
			this.children = llNode.children;
			this.browsed = llNode.browsed;
			this.setStrXML(llNode.strXML);
		}
		else
		{
			Iterator<LiveLinkNode> Iter = this.children.iterator();
			while (Iter.hasNext()) {
				LiveLinkNode subNode = Iter.next();
				subNode.setChildren(llNode);
			}
		}
	}

	public boolean hasChild(LiveLinkNode llNode) {
		if (this.children != null) {
			return (this.children.getIndexOfChild(llNode)>0);
		}
		return false;
	}

	//===========================================================
	
	public LiveLinkNode(LiveLinkURLObservable llObservable) {
		// TODO Auto-generated constructor stub
		super(llObservable.getInitialURL());
		this.browsed = false;
		this.downloaded = false;
	}

	public LiveLinkNode (URL url) {

		super(url);
		this.browsed = false;
		this.downloaded = false;
	}

	public void setLiveLinkURL(String locationURL) {
		try {
			URL url = new URL(locationURL);
			this.setInitialURL(url);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	//========================================================

	public LiveLinkNode(String strXML, String strURL) {
		super();
		this.browsed = false;
		this.downloaded = false;
		this.strXML = strXML;

		try {
			URL initialURL = new URL(strURL);
			this.setInitialURL(initialURL);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.children = new LiveLinkNodeSet(this);
	}

	//========================================================

	public String getStrXML() {
		return strXML;
	}

	public void setStrXML(String strXML) {
		this.strXML = strXML;
	}

	public String getNodeIdAttribute() {
		return idAttribute;
	}

	//========================================================

	/*
	private LiveLinkNode(Element element) {
		super(element);
		initNode(element);
	}
	 */
	public LiveLinkNode () {
		super();
		this.browsed = false;
		this.downloaded = false;

	}

	/*
	private void initNode(Element element) {
		//System.out.println("LiveLinkNode: initialize Node ");
		if (element != null) {
			this.setObjectName(element);
			this.element = element;
			this.createdDate = element.getAttribute(createdDateAttribute);
			this.nodeCreationDate = new LiveLinkDate(this.createdDate);

			this.description = element.getAttribute(descriptionAttribute);
			this.id = element.getAttribute(idAttribute);
			this.mimeType = element.getAttribute(mimeTypeAttribute);

			this.parentId = element.getAttribute(parentIdAttribute);
			this.name = element.getAttribute(nameAttribute);
			this.modifiedDate = element.getAttribute(modifiedDateAttribute);
			this.nodeModificationDate = new LiveLinkDate(this.modifiedDate);

			this.createdByName = element.getAttribute(createdByNameAttribute);
			this.ownedByName = element.getAttribute(ownedByNameAttribute);

			this.size = element.getAttribute(sizeAttribute);
		}
	}

	public boolean parseXML() {

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setValidating(false);

			dbFactory.setFeature("http://apache.org/xml/features/continue-after-fatal-error", true);  

			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			dBuilder.setErrorHandler(new ErrorHandler() {

				public void warning(SAXParseException exception)
						throws SAXException {
					System.err.println("LiveLinkNodeTree: sax warning: "+exception.getLineNumber()+":"+exception.getColumnNumber()+" ex: "+exception.getLocalizedMessage());
				}

				public void error(SAXParseException exception)
						throws SAXException {
					System.err.println("LiveLinkNode: sax error: "+exception.getLineNumber()+":"+exception.getColumnNumber()+" ex: "+exception.getLocalizedMessage());
				}

				public void fatalError(SAXParseException exception)  {
					String errMessage = "LiveLinkNode: sax fatal: "+exception.getLineNumber()+":"+exception.getColumnNumber()+" ex: "+exception.getLocalizedMessage();
					System.err.println(errMessage);
					//ShellErrorMessage shellErrorMessage = new ShellErrorMessage();
				}
			});

			Document doc = dBuilder.parse(new InputSource(new StringReader(strXML)));
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("llnode");

			// sort the nodes to retrieve the natural order provided by Livelink
			sortNodeList(nodeList);

			Element element = null;
			for (int i = 0; i < nodeList.getLength(); i++) {
				element = (Element) nodeList.item( i );
				if (i == 0 ) {
					initNode(element);
				}
				else {
					LiveLinkNode llNode = new LiveLinkNode(element);
					this.children.add(llNode);
				}
			}
			return true;
		}
		catch (ParserConfigurationException e1) {
			System.err.println(e1.getLocalizedMessage());
		}
		catch (IOException e2) {
			System.err.println(e2.getLocalizedMessage());
		}
		catch (SAXException e3) {
			System.err.println(e3.getLocalizedMessage());
		}
		catch (java.lang.InternalError e4) {
			System.err.println(e4.getLocalizedMessage());
		}
		// case when the parser fails
		this.description = "Parser Error";
		this.id = this.liveLinkURL.getLiveLinkObjectId();
		return false;
	}

	private void sortNodeList(NodeList nodeList)
	{
		if (nodeList != null)
		{
			int len = nodeList.getLength();
			// start after first node i=1 and j=1
			for (int i = 1; i < len; i++)
				for (int j = 1; j < (len - 1); j++)
					if (getNodeName (nodeList.item(j)).compareTo(getNodeName (nodeList.item(j+1)))> 0)
						nodeList.item(j).getParentNode().insertBefore(nodeList.item(j+1),nodeList.item(j));
		}
	}

	private String getNodeName (Node node)
	{
		//System.out.println("getTextFromNode: "+node.getNodeName());
		if (node.getNodeName().equals("llnode"))
		{
			if (node instanceof Element) {
				//System.out.println("getNodeName: node is instance of Element");
				Element element = (Element)node;
				return element.getAttribute("name").toLowerCase();
			}
		}
		return "";
	}
	 */

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

	/*
	public String getShortCutNodeURL() {
		if (this.isShortCut()) {

			if (this.element.hasChildNodes()) {
				System.out.println("LiveLinkNode: Short Cut Node has child nodes ");

				NodeList nodeList = this.element.getElementsByTagName("originalnode");
				if (nodeList.getLength() == 1) {
					System.out.println("LiveLinkNode: Short Cut Node Name: "+nodeList.item(0).getNodeName());
					Node node = nodeList.item(0);
					System.out.println("LiveLinkNode: Short Cut Node Id: "+ node.getFirstChild().getNodeValue());

					LiveLinkURL llUrl = new LiveLinkURL(node.getFirstChild().getNodeValue());
					return llUrl.getXmlExportURL().toExternalForm();
				}
			}
		}
		return "";
	}
	 */

	

	/**
	 * any node that is not a leaf may have child nodes .
	 * @return
	 */
	public boolean hasSubFolders() {
		if (this.isLeaf() == false) {
			if (this.children != null) {
				return this.children.hasSubFolders();
			}
			else {
				return true;
			}
		}
		return false;
	}

	//=============================================
	private boolean browsed = false;
	public void setBrowsed(boolean b) {
		//String s = new Boolean(b).toString();
		//logger.log(Level.INFO,s);
		this.browsed = b;
		if( b == true ) {
			logger.log(Level.INFO , " ============= notify observers ================");
			this.setChanged();
			notifyObservers(this);		
		}
	}

	public boolean isBrowsed() {
		return this.browsed;
	}

	//=============================================
	private boolean downloaded = false;

	public void setDownLoaded(boolean b) {
		this.downloaded = b;
	}

	public boolean isDownLoaded() {
		return this.downloaded;
	}

	//====================================
	public int getChildCount() {
		//System.out.println("LiveLinkNode: getChildCount"); 
		if (this.isLeaf()) {
			return 0;
		}
		else {
			if (isBrowsed() == true) {
				if (this.children != null) {
					return this.children.size();
				}
			}
			else {
				return 0;
			}
		}
		return 0;
	}

	public LiveLinkNode getChild(int index) {

		//System.out.println("LiveLinkNode: getChild: "+index+" -----------------");
		if (this.children != null) {
			if (index >= 0 && index < this.children.size()) {
				return this.children.get(index);
			}
		}
		return null;
	}

	public int getIndexOfChild(LiveLinkNode child) {
		if (this.children != null) {
			return this.children.getIndexOfChild(child);
		}
		return 0;
	}

	public LiveLinkNodeSet getChildren() {
		return this.children;
	}

	public boolean hasChildren() {
		if (this.children == null) {
			return false;
		}
		return (this.children.size()>0);
	}

	public void dump() {
		logger.log(Level.INFO , "==========");
		logger.log(Level.INFO , this.getIdAttribute() +": " +this.getId());
		logger.log(Level.INFO , this.getNameAttribute()+": "+this.getName());

		logger.log(Level.INFO , this.getCreatedDateAttribute()+": "+this.getCreatedDate());
		DateFormat formatter = DateFormat.getDateTimeInstance();

		String strDate = formatter.format(this.getNodeCreationDate().getDate());
		logger.log(Level.INFO , strDate);

		logger.log(Level.INFO , this.getModifiedDateAttribute()+": "+this.getModifiedDate());
		strDate = formatter.format(this.getNodeModificationDate().getDate());
		logger.log(Level.INFO , strDate);

		logger.log(Level.INFO , this.getDescriptionAttribute()+": "+this.getDescription());
		logger.log(Level.INFO , this.getSizeAttribute()+": "+this.getSize());

		logger.log(Level.INFO , this.getCreatedByNameAttribute()+": "+this.getCreatedByName());
		logger.log(Level.INFO , this.getOwnedByNameAttribute()+": "+this.getOwnedByName());

		logger.log(Level.INFO , this.getMimeTypeAttribute()+": "+this.getMimeType());
		logger.log(Level.INFO , this.getDescriptionAttribute()+": "+this.getDescription());

		logger.log(Level.INFO , this.getObjectNameAttribute()+": "+this.getObjectName());
		logger.log(Level.INFO , this.getParentIdAttribute()+": "+this.getParentId());

		if (children != null) {
			logger.log(Level.INFO , "Size of sub node list: "+children.size());
			Iterator<LiveLinkNode> Iter = children.iterator();
			while (Iter.hasNext()) {
				logger.log(Level.INFO , "===========");
				LiveLinkNode llSubNode = Iter.next();
				llSubNode.dump();
			}
		}
	}

	public void update(LiveLinkNode llNode) {
		this.browsed = llNode.browsed;
		this.createdByName = llNode.createdByName;
		this.createdDate = llNode.createdDate;
		this.description = llNode.description;
		this.downloaded = llNode.downloaded;
		this.LiveLinkObjectId = llNode.LiveLinkObjectId;
		this.mimeType = llNode.mimeType;
		this.modifiedDate = llNode.modifiedDate;
		this.name = llNode.name;
		this.ownedByName = llNode.ownedByName;
		this.parentId = llNode.parentId;
		this.size = llNode.size;
		this.strXML = llNode.strXML;
		this.objectName = llNode.objectName;
		this.children = llNode.children;
	}

	/**
	 * LiveLink nodes are identical if they have the same Object Identifier
	 * @param rootNode
	 * @return
	 */

	public boolean isEqual(LiveLinkNode rootNode) {
		if (this.LiveLinkObjectId.equalsIgnoreCase(rootNode.LiveLinkObjectId)) {
			return true;
		}
		return false;
	}

	public void updateAndExtend(LiveLinkNode llNode) {

		if (this.LiveLinkObjectId.equalsIgnoreCase(llNode.getLiveLinkObjectId())) {

			this.update(llNode);

		} else {

			LiveLinkNode foundNode = this.findNode(this, llNode);
			if (foundNode != null) {
				logger.log(Level.INFO , "Node= " + llNode.getName() + " --- found --- ");
				foundNode.update(llNode);
			}
		}
	}

	/*
	 * compute the depth of each node below the current one
	 * a starting depth is provided => each group of children will have a depth = initial depth + 1
	 */
	public void computeDepth(boolean start, int _depth) {

		if (start == true) {
			this.depth = _depth;
			start = false;
		} else {
			this.depth = _depth;
		}
		if (this.hasChildren()) {
			_depth++;
			Iterator<LiveLinkNode> Iter = this.children.iterator();
			while (Iter.hasNext()) {
				LiveLinkNode llSubNode = Iter.next();
				llSubNode.computeDepth(start, _depth);
			}
		}
	}
	
	public int computeMaxDepth() {
		int maxDepth= this.depth;
		if (this.hasChildren()) {
			Iterator<LiveLinkNode> Iter = this.children.iterator();
			while (Iter.hasNext()) {
				LiveLinkNode llSubNode = Iter.next();
				maxDepth = Math.max(maxDepth, llSubNode.computeMaxDepth());
			}
		}
		return maxDepth;
	}

	private LiveLinkNode findNode(LiveLinkNode startNode , final LiveLinkNode searchedNode) {

		LiveLinkNode foundNode = null;
		Iterator<LiveLinkNode> Iter = startNode.children.iterator();
		while (Iter.hasNext()) {

			LiveLinkNode llSubNode = Iter.next();
			if (llSubNode.getLiveLinkObjectId().equalsIgnoreCase(searchedNode.getLiveLinkObjectId())) {
				return llSubNode;
			}
			// recursiv call
			foundNode = llSubNode.findNode(llSubNode, searchedNode);
			if (foundNode != null) {
				return foundNode;
			}
		}
		return foundNode;
	}

	public LiveLinkNode findNextToBeBrowsedNode(LiveLinkNode startNode) {

		LiveLinkNode foundNode = null;
		Iterator<LiveLinkNode> Iter = startNode.children.iterator();
		while (Iter.hasNext()) {

			LiveLinkNode llSubNode = Iter.next();
			if ((llSubNode.isLeaf() == false) && (llSubNode.isBrowsed() == false)) {
				return llSubNode;
			} else {
				foundNode = llSubNode.findNextToBeBrowsedNode(llSubNode);
				if (foundNode != null) {
					return foundNode;
				}
			}
		}
		return foundNode;
	}

}
