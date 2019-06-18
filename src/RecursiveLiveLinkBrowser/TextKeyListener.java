package RecursiveLiveLinkBrowser;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

public class TextKeyListener implements KeyListener {
	
	private static final Logger logger = Logger.getLogger(TextKeyListener.class.getName()); 

	private Composite parent = null;
	private Text locationText = null;

	private OleWebBrowser oleWebBrowser = null;
	private Tree liveLinkNodeTree = null;

	public TextKeyListener(Composite _parent , Text _locationText) {

		this.parent = _parent;
		this.locationText = _locationText;
	}


	public void setWebBrowser(OleWebBrowser _oleWebBrowser) {
		this.oleWebBrowser = _oleWebBrowser;
	}

	public void setLiveLinkNodeTree(Tree _liveLinkNodeTree) {
		this.liveLinkNodeTree = _liveLinkNodeTree;
	}

	public void keyPressed(KeyEvent e) {
		logger.log(Level.INFO,e.toString());
		if (e.keyCode == SWT.CR) {
			final String newLocation = locationText.getText();
			logger.log(Level.INFO,String.valueOf(e.keyCode));
			
			parent.getDisplay().asyncExec(new Runnable() {
				public void run() {
					// need to clear the tree - remove all items before re starting the browser
					if (TextKeyListener.this.liveLinkNodeTree != null) {
						TextKeyListener.this.liveLinkNodeTree.removeAll();
						// launch browser with new location / new URL
						logger.log(Level.INFO,"location: "+newLocation);
						
					
							if (TextKeyListener.this.oleWebBrowser != null) {
								TextKeyListener.this.oleWebBrowser.Navigate(newLocation);
							}
						
					}
				}
			});
		}
	}

	public void keyReleased(KeyEvent e) {

	}
}

