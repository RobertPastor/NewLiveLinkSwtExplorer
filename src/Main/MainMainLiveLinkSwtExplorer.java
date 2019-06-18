package Main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.filechooser.FileSystemView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import FileExplorerTab.FileExplorerTab;
import HelpTab.LiveLinkHelpTab;
import HelpTab.WindowsExplorerHelpTab;
import LiveLinkCore.LiveLinkNode;
import LiveLinkCore.LiveLinkObjectImageFactory;
import MimeType.LiveLinkMimeTypeSet;
import RecursiveLiveLinkBrowser.OleWebBrowserTab;

public class MainMainLiveLinkSwtExplorer {

	private static final Logger logger = Logger.getLogger(MainMainLiveLinkSwtExplorer.class.getName()); 

	private static LiveLinkMimeTypeSet llMimeTypeSet = null;
	private static LiveLinkObjectImageFactory llObjectImageFactory = null;

	private static boolean testLivelinkConnection(Shell shell, CTabFolder tabFolder) {

		boolean ret = false;
		try {
			// initial URL
			String strURL = "http://atmosphere.tatm.thales/livelink/livelink.exe?func=ll&objId=3536159&objAction=browse";
			//strURL = "http://atmosphere.tatm.thales/livelink/livelink.exe?func=ll&objId=54165667&objAction=browse&sort=name";
			strURL = "http://atmosphere.tatm.thales/livelink/livelink.exe?func=ll&objId=23287863&objAction=browse";
			strURL = "http://atmosphere.tatm.thales/livelink/livelink.exe?func=ll&objId=62656137&objAction=browse&sort=name";
			//strURL = "https://ecm.corp.thales/livelink/livelink.exe/fetch/2000/665102/8469300/19323766/98408325/-98408328/98408342/98408900/Portail%2520R%C3%A9f%C3%A9rentiel%2520Technique?func=ll&objId=98408900&objAction=browse";
			//strURL = "https://ecm.corp.thales/livelink/livelink.exe/fetch/2000/665102/Home%2520page%2520de%2520TR6?func=ll&objId=665102&objAction=browse";
			//strURL = "https://ecm.corp.thales/livelink/livelink.exe?func=ll&objId=98408342&objAction=browse&sort=name";
			LiveLinkNode rootNode = null;
			rootNode = new LiveLinkNode(new URL(strURL));
			OleWebBrowserTab oleWebBrowserTab = new OleWebBrowserTab(
					shell , 
					tabFolder, 
					llMimeTypeSet, 
					llObjectImageFactory, 
					rootNode, 
					"Livelink Connection");
			ret = oleWebBrowserTab.isConnected();
			logger.log(Level.INFO , "Livelink is connected : " + ret);

		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE , "Livelink is not connected : " + e.getLocalizedMessage());

		}
		return ret;
	}

	private static void createComposite(Shell shell) {

		Composite composite = new Composite(shell, SWT.BORDER);
		composite.setLayout(new FillLayout());

		logger.log(Level.INFO,"create Composite : init");
		logger.log(Level.INFO, "JRE Version :" + System.getProperty( "java.runtime.version" ) );
		logger.log(Level.INFO, "JVM Bit size: " + System.getProperty( "sun.arch.data.model" ) );

		// create the tabbed folder
		final CTabFolder tabFolder = new CTabFolder (composite, SWT.TOP );
		tabFolder.setMRUVisible(true);
		tabFolder.setUnselectedCloseVisible(false);
		tabFolder.setSimple(true);

		logger.log(Level.INFO,"Main Thread: System Get User Home property: "+System.getProperty("user.home"));
		FileSystemView fsv = FileSystemView.getFileSystemView();
		logger.log(Level.INFO,"Main Applet: Home Directory: "+fsv.getHomeDirectory());

		// initialize MIME type set
		setLlMimeTypeSet(new LiveLinkMimeTypeSet());
		// initialize LiveLink Object Images Factory
		setLlObjectImageFactory(new LiveLinkObjectImageFactory(composite));

		// create one Livelink explorer tab 
		testLivelinkConnection(shell, tabFolder);

		new LiveLinkHelpTab(composite, tabFolder);
		new FileExplorerTab(composite, tabFolder);
		new WindowsExplorerHelpTab(composite, tabFolder);

		// set first tab as selected
		tabFolder.setSelection(0);

		//validate();
		logger.log(Level.INFO,"==============Main Applet: stop the splash window frame====================");

		composite.setSize(1200, 700);
	}


	public static void main(String[] args) {

		Display display = new Display ();
		try {
			// create the shell
			final Shell shell = new Shell(display);
			try {
				shell.setLayout(new FillLayout());
				createComposite(shell);

				shell.setSize(1200, 700);
				shell.open();

				while (shell != null && !shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}	
			} finally {
				if (!shell.isDisposed()) {
					shell.dispose();
				}
			}
		} finally {
			display.dispose();
		}
		System.exit(0);
	}

	public static void setLlMimeTypeSet(LiveLinkMimeTypeSet _llMimeTypeSet) {
		llMimeTypeSet = _llMimeTypeSet;
	}

	public static void setLlObjectImageFactory(LiveLinkObjectImageFactory _llObjectImageFactory) {
		llObjectImageFactory = _llObjectImageFactory;
	}

}
