package HelpTab;

import java.awt.Font;

import javax.swing.JEditorPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class LiveLinkHelpPanel extends Composite {

	private Composite parent = null;

	public Composite getParent() {
		return parent;
	}

	public LiveLinkHelpPanel (Composite _parent) {

		super(_parent, SWT.EMBEDDED);
		this.parent = _parent;

		this.setLayout(new FillLayout());

		java.awt.Frame helpFrame = SWT_AWT.new_Frame(this);
		java.awt.ScrollPane panel = new java.awt.ScrollPane();
		helpFrame.add(panel);

		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.setContentType("text/html");
		
		//HTMLEditorKit editorKit = (HTMLEditorKit) editorPane.getEditorKit();

		editorPane.setText(new LiveLinkHelpText().getText());
		Font f = new Font("arial", Font.TRUETYPE_FONT, 11);
		editorPane.setFont(f);
        
        editorPane.setOpaque(false);
        editorPane.setBorder(null);
        editorPane.setEditable(false);

		panel.add(editorPane);
	}
	

	
}
