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
package com.javadude.antxr.collections;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**A simple List interface that describes operations
 * on a list.
 */
public interface List {
    public void add(Object o); // can insert at head or append.

    public void append(Object o);

    public Object elementAt(int index) throws NoSuchElementException;

    public Enumeration elements();

    public boolean includes(Object o);

    public int length();
}
