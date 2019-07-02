package Main;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SplashWindowUpdateThread extends Thread {
	
	private static final Logger logger = Logger.getLogger(SplashWindowUpdateThread.class.getName()); 

	private int progressBarValue = 0;
	private boolean Continue = true;
	private SplashWindowFrame splashWindow = null;

	SplashWindowUpdateThread (SplashWindowFrame splashWindow) {
		super();
		logger.log(Level.INFO,"create the thread");
		this.splashWindow = splashWindow;
	}   

	@Override
	public void run() {
		
		logger.log(Level.INFO,"launch the thread");

		while (Continue) {	
			this.progressBarValue += 1;
			if (this.splashWindow == null) {
				this.Continue = false;
			}
			else {
				this.splashWindow.setProgressValue(progressBarValue);
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException uneException) {}
		}
	}
}

