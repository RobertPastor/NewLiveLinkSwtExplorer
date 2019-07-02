package Tests;


/*
 * ProgressBar example snippet: update a progress bar (from another thread)
 *
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class UpdateGuiThreadMain {

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		final ProgressBar bar = new ProgressBar(shell, SWT.SMOOTH);
		bar.setBounds(10, 10, 200, 32);
		shell.open();
		final int maximum = bar.getMaximum();
		
		UpdateGuiThread updateGuiThread = new UpdateGuiThread(display, maximum, bar) ;
		updateGuiThread.start();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
