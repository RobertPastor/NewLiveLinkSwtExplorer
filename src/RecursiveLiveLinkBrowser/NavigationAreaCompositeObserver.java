package RecursiveLiveLinkBrowser;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import LiveLinkCore.LiveLinkNode;

public class NavigationAreaCompositeObserver extends Composite implements Observer {

	private static final Logger logger = Logger.getLogger(NavigationAreaCompositeObserver.class.getName()); 

	private Composite parent = null;
	private LiveLinkNode rootNode = null;
	
	private Button upButton = null;
	private Button homeButton = null;
	private Button goButton = null;
	
	private static OleWebBrowser oleWebBrowser = null;
	private static RecursiveLiveLinkTreeCompositeObserver recursiveLiveLinkTreeComposite;
	
	// text area to display the Live Link URL
	private Text locationText = null;
	// key listener for the location Text => change manually the URL
	private TextKeyListener textKeyListener = null;

	public NavigationAreaCompositeObserver(Composite _parent, LiveLinkNode _rootNode) {
		super(_parent, SWT.FILL | SWT.BORDER);
		this.parent = _parent;
		this.rootNode = _rootNode;

		GridLayout gridLayout = new GridLayout();
		// 4 columns = one column for the UP button, one for the HOME button , one the location text area and finally one for GO
		gridLayout.numColumns = 4;
		gridLayout.makeColumnsEqualWidth = false;
		this.setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.BEGINNING;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		this.setLayoutData(gridData);

		upButton = new Button(this, SWT.PUSH);
		upButton.setText("Up");

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.BEGINNING;
		gridData.grabExcessHorizontalSpace = false;
		gridData.grabExcessVerticalSpace = false;
		upButton.setLayoutData(gridData);
		upButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				logger.log(Level.INFO,"===========Begin Up Button ==========");
				disableButtons();
				
				NavigationAreaCompositeObserver.recursiveLiveLinkTreeComposite.goUp();
				
				enableButtons();
				logger.log(Level.INFO,"===========End Up Button==========");
			}
		});

		//===================================================================================
		homeButton = new Button(this, SWT.PUSH);
		homeButton.setText("Home");
		homeButton.setToolTipText("go home to ASE");

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.BEGINNING;
		gridData.grabExcessHorizontalSpace = false;
		gridData.grabExcessVerticalSpace = false;
		homeButton.setLayoutData(gridData);
		homeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				logger.log(Level.INFO,"===========Begin Home Button ==========");
				
				disableButtons();

				NavigationAreaCompositeObserver.recursiveLiveLinkTreeComposite.goHome();

				enableButtons();
				logger.log(Level.INFO,"===========End Home Button ==========");
			}
		});

		//=========================================================================
		this.locationText = new Text(this, SWT.BORDER);
		this.locationText.setToolTipText("Enter here the livelink URL"); 
		this.locationText.setEditable(true);
		this.locationText.setEnabled(true);

		Color yellow = parent.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
		this.locationText.setBackground(yellow);

		// add a key listener to modify the target URL
		this.textKeyListener = new TextKeyListener(this.parent, this.locationText);
		this.locationText.addKeyListener(textKeyListener);

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.BEGINNING;

		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		this.locationText.setLayoutData(gridData);
		this.locationText.setText(this.rootNode.getXmlExportURL().toExternalForm());

		//==============================================================
		goButton = new Button(this, SWT.PUSH);
		goButton.setText("Go");
		goButton.setToolTipText("go to selected location");

		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.BEGINNING;
		gridData.grabExcessHorizontalSpace = false;
		gridData.grabExcessVerticalSpace = false;
		goButton.setLayoutData(gridData);

		goButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				logger.log(Level.INFO,"===========Go Button pressed==========");
				
				disableButtons();
				
				NavigationAreaCompositeObserver.recursiveLiveLinkTreeComposite.goURL(locationText.getText(), true);
						
				logger.log(Level.INFO,"===========Go Button pressed==========");
				
				enableButtons();
			}
		});
	}
	
	public void updateOleWebBrowser(OleWebBrowser oleWebBrowser, RecursiveLiveLinkTreeCompositeObserver recursiveLiveLinkTreeComposite) {
		NavigationAreaCompositeObserver.oleWebBrowser = oleWebBrowser;
		this.textKeyListener.setWebBrowser(NavigationAreaCompositeObserver.oleWebBrowser);
		NavigationAreaCompositeObserver.recursiveLiveLinkTreeComposite = recursiveLiveLinkTreeComposite;
		this.textKeyListener.setLiveLinkNodeTree(NavigationAreaCompositeObserver.recursiveLiveLinkTreeComposite.getLiveLinkNodeTree());
	}

	private void disableButtons() {
		this.upButton.setEnabled(false);
		this.homeButton.setEnabled(false);
		this.goButton.setEnabled(false);
	}

	private void enableButtons() {
		this.upButton.setEnabled(true);
		this.homeButton.setEnabled(true);
		this.goButton.setEnabled(true);
	}

	@Override
	public void update(Observable observable, Object object) {

		//logger.log(Level.INFO , " ---------- observable notified ------------ " + (observable instanceof LiveLinkNode));
		if (observable instanceof LiveLinkNode) {

			logger.log(Level.INFO , " --------- observer - notification received -------  " + ((LiveLinkNode) observable).getName() );
			LiveLinkNode llNode = (LiveLinkNode) observable;
			this.rootNode = llNode;
			
			logger.log(Level.INFO , " --------- observer - location updated -------  " + this.rootNode.getXmlExportURL().toExternalForm() );
			this.locationText.setText(this.rootNode.getXmlExportURL().toExternalForm());
		}
	}


}
