package HelpTab;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;


public class LiveLinkHelpTab extends CTabItem {
	
	//private static final Logger logger = Logger.getLogger(LiveLinkHelpTab.class.getName()); 

	private Composite parent = null;
	private ScrolledComposite contentPanel = null;

	public LiveLinkHelpTab(Composite _parent, CTabFolder tabFolder) {
		
		super(tabFolder, SWT.NULL);
		//logger.log(Level.INFO,"create Help tab");
		
		this.parent = _parent;
		this.setText ("LiveLink Help");
		
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

		this.contentPanel = new ScrolledComposite (tabFolder, SWT.BORDER);
		this.setControl(this.contentPanel);

		this.contentPanel.setLayout(new FillLayout());

		LiveLinkHelpPanel jEditHelpPanel = new LiveLinkHelpPanel(LiveLinkHelpTab.this.contentPanel);

		this.contentPanel.setContent(jEditHelpPanel);
		this.contentPanel.setExpandHorizontal(true);
		this.contentPanel.setExpandVertical(true);

	}

}
