package HelpTab;

import java.awt.Font;

import javax.swing.JEditorPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import ToolVersion.ToolVersion;

public class WindowsExplorerHelpPanel extends Composite {

	
	private Composite parent = null;

	public Composite getParent() {
		return parent;
	}

	public WindowsExplorerHelpPanel (Composite _parent) {

		super(_parent, SWT.EMBEDDED);
		this.parent = _parent;

		this.setLayout(new FillLayout());

		java.awt.Frame helpFrame = SWT_AWT.new_Frame(this);
		java.awt.ScrollPane panel = new java.awt.ScrollPane();
		helpFrame.add(panel);

		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.setContentType("text/html");
		
		//HTMLEditorKit editorKit = (HTMLEditorKit) editorPane.getEditorKit();

		editorPane.setText(getText());
		Font f = new Font("arial", Font.TRUETYPE_FONT, 11);
		editorPane.setFont(f);
        
        editorPane.setOpaque(false);
        editorPane.setBorder(null);
        editorPane.setEditable(false);

		panel.add(editorPane);
	}
	
	private String getText() {
		
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
		
		text += "This tool allows to browse Windows folders and to extract their content description in an EXCEL file.";
		
		text += "<h1>=================</h1>";
		text += "<h1>Browsing Windows</h1>";
		text += "<h1>=================</h1>";

		text += "In order to browse a Windows folder, you need to click in the right hand tree on a node.<br>";
		text += "This will decollapse the node and display its content in the right hand located table.<br>";
		text += "<br>";
		text += "In order to extract the content of a folder, Right Click on the node and select one of the available commands.<br>";

		text += "<h2>=================</h2>";
		text += "<h2>Export from Selected Windows Explorer node</h2>";
		text += "<h2>=================</h2>";
		
		text += "This command creates an EXCEL file , named for instance <b><i>FileExplorer_13_déc._2012_16_25_09.xls</i></b> and <br>";
		text += "containing the first level of files and folders located beneath the selected node.<br>";
		
		text += "<h2>=================</h2>";
		text += "<h2>Recursive Export from Selected Windows Explorer node</h2>";
		text += "<h2>=================</h2>";
		
		text += "This command creates an EXCEL file , named for instance <b><i>FileExplorer_13_déc._2012_16_25_09.xls</i></b> and <br>";
		text += "containing the <b>RECURSIVE</b> content of all level of files and folders located beneath the selected node.<br>";

		text += "<h2>=================</h2>";
		text += "<h2>Content of the export file</h2>";
		text += "<h2>=================</h2>";
		
		text += "The structure of the result sheet is as follows: <br>";
		text += "<ul>";
		text += "<li>   the parent node name,</li>";
		text += "<li>   the current node Name,</li>";
		text += "<li>   the file's Size in Bytes or the number of Files in a folder,</li>";
		text += "<li>   the file Mime type,</li>";
		
		text += "<li>   an HyperLink to the node name</li>";
		
		text += "</ul>";
		
		text += "The header fields are : # || Parent || Name ||	Size in Bytes || Date Modified || Mime Type || Hyper Link<br>";
		
		
		return text;
	}
}
