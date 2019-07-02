package HelpTab;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class WindowsExplorerHelpTab extends CTabItem {
	
	private static final Logger logger = Logger.getLogger(WindowsExplorerHelpTab.class.getName()); 

	private Composite parent = null;
	private ScrolledComposite contentPanel = null;
	

	public WindowsExplorerHelpTab(Composite composite, CTabFolder tabFolder) {

		super(tabFolder, SWT.NULL);
		logger.log(Level.INFO,"create Help tab");
		
		this.parent = composite;
		this.setText ("Windows Help");
		
		InputStream in = LiveLinkHelpTab.class.getResourceAsStream("help.gif");
		if (in != null) {
			Image image = new Image(this.parent.getDisplay() ,in);
			this.setImage(image);
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		contentPanel = new ScrolledComposite (tabFolder, SWT.BORDER);
		this.setControl(contentPanel);

		this.contentPanel.setLayout(new FillLayout());

		WindowsExplorerHelpPanel jEditHelpPanel = new WindowsExplorerHelpPanel(WindowsExplorerHelpTab.this.contentPanel);

		this.contentPanel.setContent(jEditHelpPanel);
		this.contentPanel.setExpandHorizontal(true);
		this.contentPanel.setExpandVertical(true);

	}


}
