package Tests;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

public class UpdateGuiThread extends Thread {

	private int maximum = 0;
	private Display display = null;
	private ProgressBar progressBar = null;
	
	public UpdateGuiThread(final Display _display, final int _maximum, final ProgressBar _progressBar) {
		super();
		this.display = _display;
		this.maximum = _maximum;
		this.progressBar = _progressBar;
	}
	
	
	@Override
	public void run() {
		for (final int[] i = new int[1]; i[0] <= maximum; i[0]++) {
			try {
				Thread.sleep(100);
			} catch (Throwable th) {
			}
			if (display.isDisposed()) {
				return;
			}
				
			display.asyncExec(new Runnable() {
				public void run() {
					if (UpdateGuiThread.this.progressBar.isDisposed()) {
						return;
					}
					UpdateGuiThread.this.progressBar.setSelection(i[0]);
				}
			});
		}
	}
}
