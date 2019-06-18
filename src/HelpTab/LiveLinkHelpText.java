package HelpTab;

import ToolVersion.ToolVersion;

/**
 * This class contains the text that will be displayed
 * in the LiveLink help tab.
 * 
 * @author t0007330
 * @since November 2012
 */
public class LiveLinkHelpText {

	String text = "";
	
	public LiveLinkHelpText() {
		this.text = setText();
	}
	
	public String getText() {
		return this.text;
	}
	
	private String setText() {
		
		String text = "<html>";
		text += "<HEAD><TITLE>title</TITLE><STYLE TYPE=\"text/css\">";
		text += "<!--";
		text += "H1{color:#FF6342; text-align:center;font-family:Tahoma, sans-serif;}";
		text += "H2{color:#FF9473; font-family:Tahoma, sans-serif;}";
		text += "P{color:navy; font-size:10pt;   font-weight:600; letter-spacing;1px;   font-family:arial, sans-serif;";
		text += "margin-left:100;   margin-right:100;}";
		text += "-->";
		text += "</STYLE>";
		text += "</HEAD>";
		
		text += "<body>";
		text += " Tool Version: " ;
		ToolVersion toolVersion = new ToolVersion();
		text += toolVersion.getToolVersion()+ "<br>";
		text += "Changes rationale: ";
		text += toolVersion.getToolVersionRationale() + "<br>";

		text += "========"+ "<br>";
		text += "<title>Purpose</title>";
		text += "========"+ "<br>";
		
		text += "This tool allows to browse LiveLink folders.";
		text += "<br>";
		text += "The (recursive) content of the Livelink folders might be detailed and written in an EXCEL file that will be created on the local machine.";
		text += "<br>";
		text += "<br>";
		
		text += "<h1>==============================</h1>";
		text += "<h1>LiveLink Startup Process Steps</h1>";
		text += "<br>";
		
		text += "Step: 1 - If needed, provide Login and Password to the Single Sign On authentication web page";
		text += "<br>";
		text += "Step: 2 - In the navigation bar, click the <u><b>HOME</b></u> button until you got displayed, in the bottom right frame, the html <u><b>Thales Air Systems</b></u> TeamOnLine web page.";
		text += "<br>";
		text += "Step: 3 - click the <u><b>GO</b></u> button until you got displayed, in the bottom right frame, the XmlExport answer, in the left frame the LiveLink tree and in the upper right frame the table view of the selected LiveLink node.";
		text += "<br>";
		text += "<br>NB: the Single Sign On Authentication web page is displayed once.<br>";
		
		text += "<h1>=================</h1>";
		text += "<h1>Browsing LiveLink</h1>";
		text += "<h1>=================</h1><br>";

		text += "In order to browse a Livelink folder, you need to copy paste its URL in the Text Editor field.";
		text += "<br>";
		text += "Please notice that in order to browse a LiveLink folder, the action must be XMLExport (&objAction=XMLExport) instead of browse (&objAction=browse)<br>";
		text += "NB: the LiveLink command : XMLExport or Browse or Download is not case sensitive.<br>";
		text += "<br>";
		text += "LiveLink nodes (Folder / Project) are browsed in a recursive manner.";
		text += "<br>";
		text += "In order to browse recursively a subset of the livelink tree , you need to select a SPECIFIC node in the tree and right click to get the contextual menu.";
		
		text += "<br>";
		text += "The whole tree will be erased and the selected node will appear as root of the new tree.";
		text += "<br>";
		text += "During the recursive browsing, nodes are explored using the XML answers provided by the livelink server.";
		text += "<br>";
		text += "Nodes that are either Folders, Projects, etc. but not documents are populated and expanded if needed.";
		text += "<br>";
		text += "The whole recursive process stops either when a XML parsing errors occurs (wrong XML answer provided by Livelink) or when the selected root node is reached.";
		text += "<br>";
		text += "<br>";
		
		text += "<h2>========</h2><br>";
		text += "<h2>Up Button</h2>";
		text += "<h2>========</h2><br>";
		
		text += "The Up Button allows to open and browse the parent of the current root of the visualised tree.";
		text += "If the tree already has the root node at the top of the Livelink Database, the user will go a warning message: already at the top node !!!";
		
		text += "<br>";
		text += "<br>";
		
		text += "========"+ "<br>";
		text += "<h2>Home Button</h2>";
		text += "========"+ "<br>";
		
		text += "The Home Button allows to return to the hard-coded root folder.";
		text += "<br>";
		text += "<br>";
		
		text += "<h2>=========</h2>";
		text += "<h2>Go Button</h2>";
		text += "========"+ "<br>";
		
		text += "The Go Button allows to load the Livelink URL contained in the Location Text Editor widget.<br>";
		text += "It is possible to paste a livelink URL in the Location Text Editor widget.<br>";
		text += "In order to browse from this root node, set the objAction value to XmlExport (&objAction=XMLExport) in the Location Text Editor widget.<br>";
		
		text += "<br>";
		text += "<br>";
		
		text += "<h1>====================</h1>";
		text += "<h1>Excel Report Content</h1>";
		text += "========"+ "<br>";

		text += "The Excel Report contains a ReadMe sheet and a Result sheet.";
		text += "<br>";
		text += "<h2>=================</h2>";
		text += "<h2>The Read Me sheet</h2>";
		text += "<h2>=================</h2>";
		
		text += "<br>";
		text += "The ReadMe sheet contains the version of the tool and the date of the export.";
		text += "<br>";
		text += "The ReadMe sheet contains all information related to the selected Node in the tree.";
		text += "<br>";
		
		
		text += "The following information are displayed for the root node : node Name, <br>";
		text += " node Creation Date, node Modification Date, node Object Type, node Description, node Mime type,<br>";
		text += " the node Size in Bytes or the number of Files in a folder, the node id and parent node id.<br>";
		text += "<br>";
		text += "<h2>=================</h2>";
		text += "<h2>The Result sheet</h2>";
		text += "<h2>=================</h2>";
		
		text += "<br>";
		text += "The Result sheet contains information related to the children of the select node.<br>";
		text += "<br>";
		text += "The following information are displayed for each node : <br>";
		text += "<ul>";
		text += "<li>   the parent node name,</li>";
		text += "<li>   the current node Name,</li>";
		text += "<li>   the node Creation Date,</li>";
		text += "<li>   the node Modification Date,</li>";
		text += "<li>   the node Object Type,</li>";
		text += "<li>   the node Description,</li>";
		text += "<li>   the node Mime type,</li>";
		text += "<li>   the node Size in Bytes or the number of Files in a folder,</li>";
		text += "<li>   the node id and parent node id.</li>";
		text += "</ul>";
		text += "<br>";

		text += "<h1>===============</h1>";
		text += "<h1>Short Cut Nodes</h1>";
		text += "<h1>===============</h1>";

		text += "A Short Cut node is a link to another livelink node.";
		text += "<br>";
		text += "Right click on the short cut node and use the contextual menu to follow the link and discover the target LiveLink node.";
		text += "<br>";
		text += "<br>";

		text += "<h1>===============</h1>";
		text += "<h1>URL Nodes</h1>";
		text += "<h1>===============</h1>";

		text += "An URL node ";
		java.net.URL imageURL = LiveLinkHelpText.class.getResource("url.gif");
		text += "<img src=" + imageURL + " alt=\"URL picture\" ></img>";
		text += " is a node pointing to an internet / intranet web page (i.e. a web page that is not hosted by LiveLink).<br>";		
		text += "<br>";
		text += "Right click on the short cut node and use the contextual menu to follow the link and discover the target web page.";
		text += "<br>";
		text += "<br>";
		
		text += "<h1>===============</h1>";
		text += "<h1>Document Nodes</h1>";
		text += "<h1>===============</h1>";
		text += "<br>";
		
		text += "A Livelink document node is opened using the default mime type as known by the web browser.";
		text += "<br>";
		text +="Double click on the Document node to open it in the web browser.";
		text += "<br>";
		text +="Right click on the Document node and use the contextual menu to open / download the file on your machine.";
		text += "<br>";
		
		text += "<h1>===============</h1>";
		text += "<h1>Future Work</h1>";
		text += "<h1>===============</h1>";
		text += "<br>";
		
		text += "Improve the EXCEL exports (standard and recursive) providing statistics upon number of files or folders and size in Bytes...";
		text += "<br>";
		
		text += "</body>";
		text += "</html>";
		
		return text;
	}
}
