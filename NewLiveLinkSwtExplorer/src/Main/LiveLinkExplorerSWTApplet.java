package Main;


import java.applet.Applet;
import java.awt.Canvas;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.filechooser.FileSystemView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import FileExplorerTab.FileExplorerTab;
import HelpTab.LiveLinkHelpTab;
import HelpTab.WindowsExplorerHelpTab;
import LiveLinkCore.LiveLinkNode;
import LiveLinkCore.LiveLinkObjectImageFactory;
import MimeType.LiveLinkMimeTypeSet;
import RecursiveLiveLinkBrowser.OleWebBrowserTab;


/**
 * This class is the main Applet launching the application.
 * @author t0007330
 * @since March 2012
 *
 */
public class LiveLinkExplorerSWTApplet extends Applet {

	/**
	 * serial ID.
	 */
	private static final long serialVersionUID = -4581616920208657476L;

	private static final Logger logger = Logger.getLogger(LiveLinkExplorerSWTApplet.class.getName()); 

	private Display display;
	private Shell shell;
	private Canvas canvas;

	private static SplashWindowFrame splashWindowFrame = null;
	private static SplashWindowUpdateThread splashWindowUpdateThread = null;
	
	private LiveLinkMimeTypeSet llMimeTypeSet = null;
	private LiveLinkObjectImageFactory llObjectImageFactory = null;

	// the progress bar is used during the load of the Java Classes
	final static int MaxProgressBarValue = 3000;

	public void init() {

		logger.log(Level.INFO,"Main Applet: init");
		logger.log(Level.INFO, "JRE Version :" + System.getProperty( "java.runtime.version" ) );
        logger.log(Level.INFO, "JVM Bit size: " + System.getProperty( "sun.arch.data.model" ) );
	}
	
	public void start() {
		
		logger.log(Level.INFO,"Main Applet: start");
		
		// this is the image displayed in the splash screen     
		String imgName = "atm.gif";

		//display of the splash screen
		splashWindowFrame  = new SplashWindowFrame(imgName,MaxProgressBarValue);

		// start to update the splash screen
		splashWindowUpdateThread = new SplashWindowUpdateThread (splashWindowFrame);
		splashWindowUpdateThread.start();
		
    	logger.log(Level.INFO,"Main Applet: starting");
		Thread thread = new Thread(new Runnable() {
			
			public void run() {
				
		    	logger.log(Level.INFO,"==============Main Applet: thread launched====================");

				setLayout(new java.awt.GridLayout(1, 1));
				canvas = new Canvas();
				add(canvas);
				display = new Display();
			    
			    // create the shell
				shell = org.eclipse.swt.awt.SWT_AWT.new_Shell(display,	canvas);			
				shell.setLayout(new FillLayout());
				
				// create the tabbed folder
				final CTabFolder tabFolder = new CTabFolder (shell, SWT.TOP );
				tabFolder.setMRUVisible(true);
				tabFolder.setUnselectedCloseVisible(false);
				tabFolder.setSimple(true);

				
				logger.log(Level.INFO,"Main Thread: System Get User Home property: "+System.getProperty("user.home"));
				FileSystemView fsv = FileSystemView.getFileSystemView();
				logger.log(Level.INFO,"Main Applet: Home Directory: "+fsv.getHomeDirectory());

				// initialize MIME type set
				setLlMimeTypeSet(new LiveLinkMimeTypeSet());
				// initialize LiveLink Object Images Factory
				setLlObjectImageFactory(new LiveLinkObjectImageFactory(shell));
				
				//new LiveLinkBrowserTab(shell,tabFolder,llMimeTypeSet);
				//String strURL = "https://ecm.corp.thales/livelink/livelink.exe?func=ll&objId=10139015&objAction=XmlExport";

				// create one Livelink explorer tab 
				testLivelinkConnection(tabFolder);
				
				new LiveLinkHelpTab(shell, tabFolder);
				new FileExplorerTab(shell, tabFolder);
				new WindowsExplorerHelpTab(shell, tabFolder);
				
				//new DownLoadTab(shell,tabFolder);

				// initial size of the applet
				setSize(1200 , 650);
				
				// set first tab as selected
				tabFolder.setSelection(0);
				
				//validate();
		    	logger.log(Level.INFO,"==============Main Applet: stop the splash window frame====================");

				// stop the splash window as all the classes are now loaded.
				splashWindowFrame.stop();
				
				while (shell != null && !shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}			
			}
		});
    	logger.log(Level.INFO,"==============Main Applet: launch the thread====================");
		// launch the thread
		thread.start();
	}

	private boolean testLivelinkConnection(CTabFolder tabFolder) {

		boolean ret = false;
		try {
			// initial URL
			String strURL = "http://atmosphere.tatm.thales/livelink/livelink.exe?func=ll&objId=3536159&objAction=browse";
			strURL = "https://ecm.corp.thales/livelink/livelink.exe/fetch/2000/665102/8469300/19323766/98408325/-98408328/98408342/98408900/Portail%2520R%C3%A9f%C3%A9rentiel%2520Technique?func=ll&objId=98408900&objAction=browse";
			strURL = "https://ecm.corp.thales/livelink/livelink.exe/fetch/2000/665102/Home%2520page%2520de%2520TR6?func=ll&objId=665102&objAction=browse";
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
	
	public void stop() {

		logger.log(Level.INFO,"Main Applet: stopping");
		if (display != null && !display.isDisposed()) {
			/*
			display.syncExec(new Runnable() {
				public void run() {
					if (shell != null && !shell.isDisposed())
						shell.dispose();
					shell = null;
					display.dispose();
					display = null;
				}
			});
			 */
			remove(canvas);
			canvas = null;
		}
	}

	public LiveLinkMimeTypeSet getLlMimeTypeSet() {
		return llMimeTypeSet;
	}

	public void setLlMimeTypeSet(LiveLinkMimeTypeSet llMimeTypeSet) {
		this.llMimeTypeSet = llMimeTypeSet;
	}

	public LiveLinkObjectImageFactory getLlObjectImageFactory() {
		return llObjectImageFactory;
	}

	public void setLlObjectImageFactory(LiveLinkObjectImageFactory llObjectImageFactory) {
		this.llObjectImageFactory = llObjectImageFactory;
	}
}
