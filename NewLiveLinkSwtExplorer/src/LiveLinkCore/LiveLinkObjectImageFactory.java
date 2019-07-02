package LiveLinkCore;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;


/**
 * this class manage a map of Images associated to each Livelink specific node
 * @author t0007330
 * @since July 2012
 * 
 */
public class LiveLinkObjectImageFactory {
	
	private static final Logger logger = Logger.getLogger(LiveLinkObjectImageFactory.class.getName()); 

	// note: there is no Image for the Livelink object Document 
	// as the corresponding image is the one of the underlying Windows file system
	
	private String [] llEnglishObjectNamesSet =       { 
				"Folder"        		, 	"Project"        	, 	"Shortcut"        
			, 	"Channel"           	,	"Community"         , 	"URL"         		, 	"Custom View"        
			, 	"Comm Expert Group" 	, 	"Comm Store"        , 	"Comm Store URL"	,	"News"  		, 	"Document" 
			, 	"Compound Document" 	, 	"Virtual Folder"	,	"MailBox"			,	"Wiki"
			
	};
	
	private String [] llFrenchObjectNameSet = { 
				"Dossier" 				, 	"Projet" 						, 	"Raccourci" 
			, 	"Canal"					,	"Communauté"					,	"URL"					, 	"Affichage personnalisé"
			, 	"Canal"					,	"Stockage Comm"					,	"Stockage Comm URL"		,	"Nouvelle"		, 	"Document"
			, 	"Document Composite" 	, 	"Dossier virtuel"				,	"Banque de messages"	,	"Wiki"
	
	};
	
	private String [] llObjectImagesFileNames = { 
				"Images/folder.gif" 				, "Images/project.gif" 				, 	"Images/shortcut.gif" 
			, 	"Images/channel.gif"     			, "Images/community.gif"  			, 	"Images/url.gif" 				, 	"Images/customview.gif" 
			, 	"Images/expertgroup.gif" 			, "Images/commstore.gif"  			, 	"Images/url.gif"				,	"Images/news.gif" 			, "Images/globe.gif" 
			, 	"Images/compound_document.gif" 		, "Images/virtualfolder.gif"		,	"Images/mailbox.gif"			,	"Images/wiki.gif"
			};

	public String[] getLLEnglishObjectNameSet() {
		return this.llEnglishObjectNamesSet;
	}
	
	// this is mapping between LiveLinkOjects and File Names containing the corresponding Icons
	Map<String, String> mapLLEnglishObjectFileNames = new HashMap<String, String>();
	Map<String, String> mapLLFrenchObjectFileNames = new HashMap<String, String>();
	
	// warning : it is about org.eclipse.swt.graphics.Image (not awt.Image)
	Map<String, Image> mapSWTFileNamesImages = new HashMap<String, Image>();
	
	// for a Jtree - Javax Swing tree
	Map<String, ImageIcon> mapSwingFileNamesImages = new HashMap<String , ImageIcon>();
	
	private Composite parent = null;
	
	public LiveLinkObjectImageFactory (Composite _parent) {
		this.parent = _parent;
		initObjectFileNames();
		initSWTImageMap();
		//initSwingImageMap();
	}
	
	/**
	 * dump the name of the LiveLink Objects
	 */
	public void dumpKeys() {
		for (String key : mapLLEnglishObjectFileNames.keySet()) {
		    logger.log(Level.INFO,key);
		}
	}
	
	private void initObjectFileNames () {
		for (int i=0 ; i<llEnglishObjectNamesSet.length ; i++) {
			this.mapLLEnglishObjectFileNames.put(llEnglishObjectNamesSet[i], llObjectImagesFileNames[i]);
		}
		for (int i=0 ; i<llFrenchObjectNameSet.length ; i++) {
			this.mapLLFrenchObjectFileNames.put(llFrenchObjectNameSet[i], llObjectImagesFileNames[i]);
		}
	}
	
	/**
	 * build a map between a file name - file path and an SWT image
	 */
	private void initSWTImageMap() {
		InputStream in = null;
		for (String llObjectName : this.llEnglishObjectNamesSet) {
			String llObjectFileName = this.mapLLEnglishObjectFileNames.get(llObjectName);
			try {
				//in = LiveLinkObjectImageFactory.class.getResourceAsStream(llObjectFileName);
				in = this.getClass().getClassLoader().getResourceAsStream(llObjectFileName);
				if (in != null) {
					Image image = new Image(this.parent.getDisplay(), in);
					this.mapSWTFileNamesImages.put(llObjectFileName, image);
					in.close();
				}
				else {
					logger.log(Level.SEVERE , " file does not exists= " + llObjectFileName );
				}
				//in.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void initSwingImageMap() {
		logger.log(Level.INFO,"start init Swing Image Map");
		for (String llObjectName : this.llEnglishObjectNamesSet) {
			
			String llObjectFileName = this.mapLLEnglishObjectFileNames.get(llObjectName);
			java.net.URL imgURL = LiveLinkObjectImageFactory.class.getResource(llObjectFileName);
			
			if (imgURL != null) {
				ImageIcon icon = new ImageIcon(imgURL);
				this.mapSwingFileNamesImages.put(llObjectFileName, icon);
			} else {
				logger.log(Level.SEVERE , "image does not exists= " + llObjectFileName);
			}
		}
	}
	
	public Image getSWTImage(LiveLinkNode llNode) {
		if (llNode != null) {
			String llObjectName = llNode.getObjectName();
			// check first if english object name found ?
			if (this.mapLLEnglishObjectFileNames.containsKey(llObjectName)) {
				String FileName = this.mapLLEnglishObjectFileNames.get(llObjectName);
				if (this.mapSWTFileNamesImages.containsKey(FileName)) {
					return this.mapSWTFileNamesImages.get(FileName);
				}
			}
			// if English object name not found -> check if french object name found ?
			if (this.mapLLFrenchObjectFileNames.containsKey(llObjectName)) {
				String FileName = this.mapLLFrenchObjectFileNames.get(llObjectName);
				if (this.mapSWTFileNamesImages.containsKey(FileName)) {
					return this.mapSWTFileNamesImages.get(FileName);
				}
			}
			
			System.out.println("Livelink Object Image Factory: "+ llObjectName + " object id: "+ llNode.getId() + " MimeType: "+llNode.getMimeType());
		}
		// if not found return the image of a Document
		String llObjectName = "Document";
		if (this.mapLLEnglishObjectFileNames.containsKey(llObjectName)) {
			String FileName = this.mapLLEnglishObjectFileNames.get(llObjectName);
			if (this.mapSWTFileNamesImages.containsKey(FileName)) {
				return this.mapSWTFileNamesImages.get(FileName);
			}
		}
		return null;
	}
	
	private ImageIcon getSwingImageIcon(LiveLinkNode llNode) {
		if (llNode != null) {
			String llObjectName = llNode.getObjectName();
			if (this.mapLLEnglishObjectFileNames.containsKey(llObjectName)) {
				String FileName = this.mapLLEnglishObjectFileNames.get(llObjectName);
				if (this.mapSWTFileNamesImages.containsKey(FileName)) {
					return this.mapSwingFileNamesImages.get(FileName);
				}
			}
			logger.log(Level.SEVERE,"no image for LiveLink Object: "+llObjectName+" MimeType: "+llNode.getMimeType());
		}
		return null;
	}
	
}
