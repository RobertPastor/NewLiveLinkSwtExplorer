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
package com.javadude.antxr.preprocessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;

import com.javadude.antxr.collections.impl.Vector;

/** Tester for the preprocessor */
public class Tool {
    protected Hierarchy theHierarchy;
    protected String grammarFileName;
    protected String[] args;
    protected int nargs;		// how many args in new args list
    protected Vector grammars;
    protected com.javadude.antxr.Tool antxrTool;

    public Tool(com.javadude.antxr.Tool t, String[] args) {
        antxrTool = t;
        processArguments(args);
    }

    public static void main(String[] args) {
		com.javadude.antxr.Tool antxrTool = new com.javadude.antxr.Tool();
        Tool theTool = new Tool(antxrTool, args);
        theTool.preprocess();
        String[] a = theTool.preprocessedArgList();
        for (int i = 0; i < a.length; i++) {
            System.out.print(" " + a[i]);
        }
        System.out.println();
    }

    public boolean preprocess() {
        if (grammarFileName == null) {
            antxrTool.toolError("no grammar file specified");
            return false;
        }
        if (grammars != null) {
            theHierarchy = new Hierarchy(antxrTool);
            for (Enumeration e = grammars.elements(); e.hasMoreElements();) {
                String f = (String)e.nextElement();
                try {
                    theHierarchy.readGrammarFile(f);
                }
                catch (FileNotFoundException fe) {
                    antxrTool.toolError("file " + f + " not found");
                    return false;
                }
            }
        }

        // do the actual inheritance stuff
        boolean complete = theHierarchy.verifyThatHierarchyIsComplete();
        if (!complete)
            return false;
        theHierarchy.expandGrammarsInFile(grammarFileName);
        GrammarFile gf = theHierarchy.getFile(grammarFileName);
        String expandedFileName = gf.nameForExpandedGrammarFile(grammarFileName);

        // generate the output file if necessary
        if (expandedFileName.equals(grammarFileName)) {
            args[nargs++] = grammarFileName;			// add to argument list
        }
        else {
            try {
                gf.generateExpandedFile(); 				// generate file to feed ANTXR
                args[nargs++] = antxrTool.getOutputDirectory() +
                    System.getProperty("file.separator") +
                    expandedFileName;		// add to argument list
            }
            catch (IOException io) {
                antxrTool.toolError("cannot write expanded grammar file " + expandedFileName);
                return false;
            }
        }
        return true;
    }

    /** create new arg list with correct length to pass to ANTXR */
    public String[] preprocessedArgList() {
        String[] a = new String[nargs];
        System.arraycopy(args, 0, a, 0, nargs);
        args = a;
        return args;
    }

    /** Process -glib options and grammar file.  Create a new args list
     *  that does not contain the -glib option.  The grammar file name
     *  might be modified and, hence, is not added yet to args list.
     */
    private void processArguments(String[] incomingArgs) {
		 this.nargs = 0;
		 this.args = new String[incomingArgs.length];
		 for (int i = 0; i < incomingArgs.length; i++) {
			 if ( incomingArgs[i].length() == 0 )
			 {
				 antxrTool.warning("Zero length argument ignoring...");
				 continue;
			 }
			 if (incomingArgs[i].equals("-glib")) {
				 // if on a pc and they use a '/', warn them
				 if (File.separator.equals("\\") &&
					  incomingArgs[i].indexOf('/') != -1) {
					 antxrTool.warning("-glib cannot deal with '/' on a PC: use '\\'; ignoring...");
				 }
				 else {
					 grammars = com.javadude.antxr.Tool.parseSeparatedList(incomingArgs[i + 1], ';');
					 i++;
				 }
			 }
			 else if (incomingArgs[i].equals("-o")) {
				 args[this.nargs++] = incomingArgs[i];
				 if (i + 1 >= incomingArgs.length) {
					 antxrTool.error("missing output directory with -o option; ignoring");
				 }
				 else {
					 i++;
					 args[this.nargs++] = incomingArgs[i];
					 antxrTool.setOutputDirectory(incomingArgs[i]);
				 }
			 }
			 else if (incomingArgs[i].charAt(0) == '-') {
				 args[this.nargs++] = incomingArgs[i];
			 }
			 else {
				 // Must be the grammar file
				 grammarFileName = incomingArgs[i];
				 if (grammars == null) {
					 grammars = new Vector(10);
				 }
				 grammars.appendElement(grammarFileName);	// process it too
				 if ((i + 1) < incomingArgs.length) {
					 antxrTool.warning("grammar file must be last; ignoring other arguments...");
					 break;
				 }
			 }
		 }
    }
}
