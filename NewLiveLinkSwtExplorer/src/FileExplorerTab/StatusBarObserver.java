package FileExplorerTab;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import RecursiveLiveLinkBrowser.LiveLinkNodeKTableCompositeObserver;


public class StatusBarObserver extends Composite implements Observer {

	private static final Logger logger = Logger.getLogger(LiveLinkNodeKTableCompositeObserver.class.getName());

	private  Composite parent = null;
	public Composite getParent() {
		return parent;
	}

	private Label fileExplorerStatus = null;

	public StatusBarObserver(Composite parent, int swtParameters) {
		
		super(parent, SWT.BORDER | SWT.FILL);
		this.parent = parent;
		
		GridLayout gridLayout = new GridLayout();
		// 4 columns = one column for the UP button, one for the HOME button , one the location text area and finally one for GO
		gridLayout.numColumns = 1;
		gridLayout.makeColumnsEqualWidth = false;
		this.setLayout(gridLayout);

		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.CENTER;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		this.setLayoutData(gridData);
		
		this.fileExplorerStatus = new Label(this, SWT.BORDER | SWT.FILL);
		
		gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.CENTER;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = false;
		
		this.fileExplorerStatus.setLayoutData(gridData);
		
		Color color = parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND);
		this.fileExplorerStatus.setBackground(color);
	}

	@Override
	public void update(Observable observable, Object arg) {

		if (observable instanceof FileObservable) {

			FileObservable fileObservable = (FileObservable) observable;
			logger.log(Level.INFO, " ---------- observer received a notification ------------- "  );
			logger.info(fileObservable.getFile().getName());
			
			final String fileName = fileObservable.getFile().getName();
			this.parent.getDisplay().syncExec(new Runnable() {
				public void run () {
					StatusBarObserver.this.fileExplorerStatus.setText(fileName );
					StatusBarObserver.this.fileExplorerStatus.update();
					StatusBarObserver.this.fileExplorerStatus.redraw();
					StatusBarObserver.this.update();
					StatusBarObserver.this.redraw();
					StatusBarObserver.this.parent.update();
					StatusBarObserver.this.parent.redraw();
				}
			});

		}
	}
	
	public void setText(final String text) {
		this.fileExplorerStatus.setText( text );
	}
}
