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
package com.javadude.antxr.collections.impl;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * A simple indexed vector: a normal vector except that you must
 * specify a key when adding an element.  This allows fast lookup
 * and allows the order of specification to be preserved.
 */
public class IndexedVector {
    protected Vector elements;
    protected Hashtable index;


    /**
     * IndexedVector constructor comment.
     */
    public IndexedVector() {
        elements = new Vector(10);
        index = new Hashtable(10);
    }

    /**
     * IndexedVector constructor comment.
     * @param size int
     */
    public IndexedVector(int size) {
        elements = new Vector(size);
        index = new Hashtable(size);
    }

    public synchronized void appendElement(Object key, Object value) {
        elements.appendElement(value);
        index.put(key, value);
    }

    /**
     * Returns the element at the specified index.
     * @param index the index of the desired element
     * @exception ArrayIndexOutOfBoundsException If an invalid
     * index was given.
     */
    public Object elementAt(int i) {
        return elements.elementAt(i);
    }

    public Enumeration elements() {
        return elements.elements();
    }

    public Object getElement(Object key) {
        Object o = index.get(key);
        return o;
    }

    /** remove element referred to by key NOT value; return false if not found. */
    public synchronized boolean removeElement(Object key) {
        Object value = index.get(key);
        if (value == null) {
            return false;
        }
        index.remove(key);
        elements.removeElement(value);
        return false;
    }

    public int size() {
        return elements.size();
    }
}
