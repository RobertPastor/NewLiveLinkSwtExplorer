package Main;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * this class displays a splash screen
 * @author Pastor
 *
 */

public class SplashWindowFrame extends JFrame {    
	
	private static final Logger logger = Logger.getLogger(SplashWindowFrame.class.getName()); 

	/**
	 * 
	 */
	private static final long serialVersionUID = -9137583838458479685L;

	private JProgressBar progressBar = null;   
	private int maxValue = 0;   

	public static URL getResourceURL(String jpgFileName) {
		java.net.URL base = SplashWindowFrame.class.getResource(jpgFileName);
		return base;
	}

	public SplashWindowFrame(String imageFileNamePath, int intProgressMaxValue) { 

		super();
		logger.log(Level.INFO,"Splash Window Frame creation");
		
		this.setTitle("LiveLink & Windows Explorer");
		// 	initialize progress value when to close the screen      
		this.maxValue = intProgressMaxValue;                    
		// add the progress bar    
		progressBar = new JProgressBar(0, intProgressMaxValue);   
		getContentPane().add(progressBar, BorderLayout.SOUTH);                 
		// add a label with the icon       

		JLabel label = new JLabel(new ImageIcon(getResourceURL(imageFileNamePath)),SwingConstants.CENTER);

		// add the label to the panel     
		getContentPane().add(label, BorderLayout.NORTH);   

		// center the splash screen    
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();     
		Dimension labelSize = label.getPreferredSize();   
		setLocation(screenSize.width / 2 - (labelSize.width / 2),     
				screenSize.height / 2 - (labelSize.height / 2));

		// hide the splash screen when some one clicks on it   
		addMouseListener(new MouseAdapter() {     
			@Override
			public void mousePressed(MouseEvent e) {      
				setVisible(false);               
				dispose();           
			}      
		});     
		// display the splash screen  
		pack();
		setVisible(true);   
	}

	/**
	 * change the value of the progress bar  
	 *
	 */
	public void setProgressValue(int intValue) {

		//logger.log(Level.INFO,"Splash Window Frame : set Progress Value : "+String.valueOf(intValue));

		progressBar.setValue(intValue);
		this.paint(this.getGraphics());

		// when getting to the max value
		// close the splash screen and launch the thread   
		if (intValue >= maxValue) {           
			try {               
				SwingUtilities.invokeAndWait(closerRunner);  
			}
			catch (InterruptedException e) { 
				e.printStackTrace();           
			} catch (InvocationTargetException e) {         
				e.printStackTrace();            
			}       
		}  
	}

	public void stop() {
		logger.log(Level.INFO,"Splash Window Frame stopping");
		SwingUtilities.invokeLater(closerRunner);  
	}
	
	// thread to close the splash screen   
	final Runnable closerRunner = new Runnable() {   
		public void run() {  
			setVisible(false);          
			dispose();        
		}  
	};
}
