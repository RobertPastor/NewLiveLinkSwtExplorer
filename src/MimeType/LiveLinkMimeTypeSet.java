package MimeType;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Icon;

import org.eclipse.swt.graphics.ImageData;

/**
 * this class stores for each file extension or file type an Icon to be displayed.
 * @author Pastor Robert
 * @since August 2008
 *
 */
public class LiveLinkMimeTypeSet {

	private ArrayList<LiveLinkMimeType> LLmimeTypeSet = null;

	public LiveLinkMimeTypeSet () {

		this.LLmimeTypeSet = new ArrayList<LiveLinkMimeType>();
		init();
	}

	private void init () {

		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/java-archive","jar"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/msaccess","mdb"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/msword","doc"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/msword","docx"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/octet-stream","txt"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/pdf","pdf"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/ppt","ppt"));
		
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/vnd.ms-excel","xls"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","xlsx"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/vnd.ms-excel.sheet.macroEnabled.12","xlsm"));

		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/vnd.ms-powerpoint","ppt"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/vnd.ms-project","msp"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/x-asap","xxx"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/x-javascript","jsp"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/xml","xml"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/x-msdownload","msd"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/x-msmetafile","msm"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/x-ms-wmz","wmz"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/x-shockwave-flash","smf"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/x-troff-man","man"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/x-zip-compressed","zip"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("application/zip","zip"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("image/bmp","bmp"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("image/gif","gif"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("image/jpeg","jpeg"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("image/pjpeg","jpg"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("image/png","png"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("message/rfc822","txt"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("text/css","css"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("text/html","htm"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("text/html","html"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("text/plain","txt"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("text/xml","xml"));
		this.LLmimeTypeSet.add(new LiveLinkMimeType("video/x-msvideo","avi"));

		/**
		 *   <?xml version="1.0" encoding="UTF-8" ?> 
- <livelink appversion="9.7.1" src="XmlExport">
- <llnode created="2007-08-17T15:43:24" createdby="12095" 
createdbyname="BEDOUET pascale" description="" id="34635364" 
mimetype="text/html" modified="2011-08-04T10:30:25" 
name="customview.html" objname="Custom View" 
objtype="146" ownedby="12095" 
ownedbyname="BEDOUET pascale" 
parentid="-32990869" size="5890" versionnum="11">
  <Nickname domain="" /> 
  </llnode>
  </livelink>
		 */

	}

	public boolean contains(String searchedMimeType) {
		Iterator<LiveLinkMimeType> Iter = this.LLmimeTypeSet.iterator();
		while (Iter.hasNext()) {
			LiveLinkMimeType mimeType = Iter.next();
			if (mimeType.getLiveLinkMimeType().equalsIgnoreCase(searchedMimeType)) {
				return true;
			}
		}
		return false;
	}

	public String getDocumentSuffix (String searchedMimeType) {

		Iterator<LiveLinkMimeType> Iter = this.LLmimeTypeSet.iterator();
		while (Iter.hasNext()) {
			LiveLinkMimeType LLmimeType = Iter.next();
			if (LLmimeType.getLiveLinkMimeType().equalsIgnoreCase(searchedMimeType)) {
				return LLmimeType.getDocumentSuffix();
			}
		}
		return "";
	}

	public Icon getIcon (String searchedMimeType) {

		Iterator<LiveLinkMimeType> Iter = this.LLmimeTypeSet.iterator();
		while (Iter.hasNext()) {
			LiveLinkMimeType LLmimeType = Iter.next();
			if (LLmimeType.getLiveLinkMimeType().equalsIgnoreCase(searchedMimeType)) {
				return LLmimeType.getIcon();
			}
		}
		//System.err.println("LiveLinkMimeTypeSet: unknown Mime Type: "+searchedMimeType);
		return null;
	}
	
	public ImageData getImageData (String searchedMimeType) {

		Iterator<LiveLinkMimeType> Iter = this.LLmimeTypeSet.iterator();
		while (Iter.hasNext()) {
			LiveLinkMimeType LLmimeType = Iter.next();
			if (LLmimeType.getLiveLinkMimeType().equalsIgnoreCase(searchedMimeType)) {
				return LLmimeType.getImageData();
			}
		}
		//System.err.println("LiveLinkMimeTypeSet: unknown Mime Type: "+searchedMimeType);
		return null;
	}
	
	public ImageData getDefaultImageData() {
		String searchedMimeType = "application/octet-stream";
		Iterator<LiveLinkMimeType> Iter = this.LLmimeTypeSet.iterator();
		while (Iter.hasNext()) {
			LiveLinkMimeType LLmimeType = Iter.next();
			if (LLmimeType.getLiveLinkMimeType().equalsIgnoreCase(searchedMimeType)) {
				return LLmimeType.getImageData();
			}
		}
		return null;
	}
	
}
