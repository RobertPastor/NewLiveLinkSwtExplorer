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
package com.javadude.antxr;

class CharRangeElement extends AlternativeElement {
    String label;
    protected char begin = 0;
    protected char end = 0;
    protected String beginText;
    protected String endText;


    public CharRangeElement(LexerGrammar g, Token t1, Token t2, int autoGenType) {
        super(g);
        begin = (char)ANTXRLexer.tokenTypeForCharLiteral(t1.getText());
        beginText = t1.getText();
        end = (char)ANTXRLexer.tokenTypeForCharLiteral(t2.getText());
        endText = t2.getText();
        line = t1.getLine();
        // track which characters are referenced in the grammar
        for (int i = begin; i <= end; i++) {
            g.charVocabulary.add(i);
        }
        this.autoGenType = autoGenType;
    }

    public void generate() {
        grammar.generator.gen(this);
    }

    public String getLabel() {
        return label;
    }

    public Lookahead look(int k) {
        return grammar.theLLkAnalyzer.look(k, this);
    }

    public void setLabel(String label_) {
        label = label_;
    }

    public String toString() {
        if (label != null)
            return " " + label + ":" + beginText + ".." + endText;
        return " " + beginText + ".." + endText;
    }
}
