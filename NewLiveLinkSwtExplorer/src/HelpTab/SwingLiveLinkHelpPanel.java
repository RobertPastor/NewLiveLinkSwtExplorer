package HelpTab;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SwingLiveLinkHelpPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5857437846290290872L;

	public SwingLiveLinkHelpPanel () {
		
		super(new GridBagLayout());
		
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.setContentType("text/html");
		
		//HTMLEditorKit editorKit = (HTMLEditorKit) editorPane.getEditorKit();

		editorPane.setText(new LiveLinkHelpText().getText());
		Font f = new Font("arial", Font.TRUETYPE_FONT, 11);
		editorPane.setFont(f);
        
        editorPane.setOpaque(true);
        editorPane.setBorder(null);
        editorPane.setEditable(false);
        
		GridBagConstraints c = new GridBagConstraints(); 

		c.fill=GridBagConstraints.BOTH;
		c.gridx=0;
		c.gridy=0;
		c.weightx=1; 
		c.weighty=2;
        
		this.add(new JScrollPane(editorPane),c);
	}
	

}
