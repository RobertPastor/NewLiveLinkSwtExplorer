package LiveLinkCore;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * this class shows a modal dialog 
 * for instance : with the path to the created EXCEL file
 * @author t0007330
 * @since March 2012
 */
public class ShellInformationMessage {

	private Display display = null;
	private Shell shell = null;

	public ShellInformationMessage(Display display,Shell shell,String message) {
		this.display = display;
		this.shell = shell;
		init(message);
	}

	private void init(String message) {

		final Shell s = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		s.setText("Information");
		GridLayout layout = new GridLayout(1, false);
		layout.verticalSpacing = 20;
		layout.marginHeight = layout.marginWidth = 10;
		s.setLayout(layout);
		
		//Link link = new Link(s, SWT.NONE);
		//link.setText("<a href=file://"+message+">Excel Output File</a>");
	
		Label label = new Label(s, SWT.NONE);
		label.setText(message);
		
		Button button = new Button(s, SWT.PUSH);
		button.setText("OK");
		GridData data = new GridData();
		data.horizontalAlignment = GridData.CENTER;
		button.setLayoutData(data);
		button.addListener(SWT.Selection, new Listener() {

			public void handleEvent(org.eclipse.swt.widgets.Event event) {
				s.dispose();
			}
		});
		s.pack();
		Rectangle parentBounds = shell.getBounds();
		Rectangle bounds = s.getBounds();
		int x = parentBounds.x + (parentBounds.width - bounds.width)
				/ 2;
		int y = parentBounds.y + (parentBounds.height - bounds.height)
				/ 2;
		s.setLocation(x, y);
		s.open();
		while (!s.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}
