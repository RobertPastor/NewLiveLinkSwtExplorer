package ToolVersion;


/**
 * this class provides the tool version written in the output EXCEL file
 * it contains also a TODO list
 * 
 * list of known problems:
 * ======================
 * 
 * 
 * 
 * 
 */ 
public class ToolVersion {

	

	private ToolChange[] ToolVersion = { 

			new ToolChange("initial Version",
					"V0.1 dated 10th April 2012") ,
			
			new ToolChange("error while accessing the Thales png file used in the Excel ReadMe sheet",
					"V0.2 dated 13th April 2012") ,
			
			new ToolChange("Help tab with a scroll composite, node counter starts at zero in EXCEL result sheet",
					"V0.3 dated 13th April 2012") ,
			
			new ToolChange("specific behaviour applicable to Livelink URL nodes",
					"V0.4 dated 16th April 2012") ,
			
			new ToolChange("recursive browser Tab",
					"V0.5 dated 6th May 2012") ,
			
			new ToolChange("recursive Excel file browser results",
					"V0.6 dated 12th May 2012") ,
			
			new ToolChange("recursive Excel file browser results",
					"V0.7 dated 12th May 2012") ,
			
			new ToolChange("generate recursive Indented LiveLink Index",
					"V0.8 dated 25th May 2012") ,			
			
			new ToolChange("retry when XML answer expected but not received",
					"V0.9 dated 1st June 2012") ,
			
			new ToolChange("add a table view - modify startup process",
					"V0.10 dated 5th July 2012") ,
			
			new ToolChange("use an ANTXR XML parser",
					"V0.11 dated 17th July 2012") ,
			
			new ToolChange("output Excel file is created on the user desk",
					"V0.12 dated 29th November 2012") ,
			
			new ToolChange("add a menu entry to set the root node",
					"V0.13 dated 25th March 2013") ,
			
			new ToolChange("add a virtual folder LL type and its corresponding image",
					"V0.14 dated 15th November 2013") ,	
					
			new ToolChange("use CtabFolder with image",
					"V0.15 dated 20th May 2016") ,
					
			new ToolChange("Team On Line version - change the livelink remote URL to ecm.corp.thales",
					"V0.16 dated 20th May 2016") ,
			
			new ToolChange("Livelink Help Tab with URL node image",
					"V0.17 dated 28th November 2016") ,
					
			new ToolChange("Livelink Node is an observable and there are several obervers",
					"V0.18 dated 6th December 2016") ,
					
			new ToolChange("New Main launched with Java Web Start - resource retrieved using the class loader",
					"V0.19 dated 19th April 2017") ,
					
			new ToolChange("filter XML with description field containing double quotes - mime type does not return XML Document ! ",
					"V0.20 dated 6th October 2017") ,
					
			new ToolChange("generate indented EXCEL output from a root LiveLink Node",
					"V0.21 dated 14th November 2017") ,
					
			new ToolChange("context menues in the File Explorer Tree",
				"V0.22 dated 15th November 2017") ,
					
			new ToolChange("Refresh Node context menu in Livelink Tree",
				"V0.23 dated 5th May 2019") ,
			
			new ToolChange("recursive file explorer redesign",
				"V0.24 dated 28th June 2019") ,
					

	};

	public String getToolVersion() {
		int index = this.ToolVersion.length-1;
		return ToolVersion[index].getVersion();
	}

	public String getToolVersionRationale() {
		int index = this.ToolVersion.length-1;
		return ToolVersion[index].getRationale();
	}
}

