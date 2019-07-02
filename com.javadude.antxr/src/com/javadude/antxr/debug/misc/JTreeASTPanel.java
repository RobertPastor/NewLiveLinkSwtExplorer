/*******************************************************************************
 * Copyright (c) 2005 Scott Stanchfield, http://javadude.com
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Based on the ANTLR parser generator by Terence Parr, http://antlr.org
 *******************************************************************************/
package com.javadude.antxr.debug.misc;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

public class JTreeASTPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	JTree tree;

    public JTreeASTPanel(TreeModel tm, TreeSelectionListener listener) {
        // use a layout that will stretch tree to panel size
        setLayout(new BorderLayout());

        // Create tree
        tree = new JTree(tm);

        // Change line style
        tree.putClientProperty("JTree.lineStyle", "Angled");

        // Add TreeSelectionListener
        if (listener != null)
            tree.addTreeSelectionListener(listener);

        // Put tree in a scrollable pane's viewport
        JScrollPane sp = new JScrollPane();
        sp.getViewport().add(tree);

        add(sp, BorderLayout.CENTER);
    }
}
