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

public class Vector implements Cloneable {
    protected Object[] data;
    protected int lastElement = -1;

    public Vector() {
        this(10);
    }

    public Vector(int size) {
        data = new Object[size];
    }

    public synchronized void appendElement(Object o) {
        ensureCapacity(lastElement + 2);
        data[++lastElement] = o;
    }

    /**
     * Returns the current capacity of the vector.
     */
    public int capacity() {
        return data.length;
    }

    public Object clone() {
        Vector v = null;
        try {
            v = (Vector)super.clone();
        }
        catch (CloneNotSupportedException e) {
            System.err.println("cannot clone Vector.super");
            return null;
        }
        v.data = new Object[size()];
        System.arraycopy(data, 0, v.data, 0, size());
        return v;
    }

    /**
     * Returns the element at the specified index.
     * @param index the index of the desired element
     * @exception ArrayIndexOutOfBoundsException If an invalid
     * index was given.
     */
    public synchronized Object elementAt(int i) {
        if (i >= data.length) {
            throw new ArrayIndexOutOfBoundsException(i + " >= " + data.length);
        }
        if (i < 0) {
            throw new ArrayIndexOutOfBoundsException(i + " < 0 ");
        }
        return data[i];
    }

    public synchronized Enumeration elements() {
        return new VectorEnumerator(this);
    }

    public synchronized void ensureCapacity(int minIndex) {
        if (minIndex + 1 > data.length) {
            Object oldData[] = data;
            int n = data.length * 2;
            if (minIndex + 1 > n) {
                n = minIndex + 1;
            }
            data = new Object[n];
            System.arraycopy(oldData, 0, data, 0, oldData.length);
        }
    }

    public synchronized boolean removeElement(Object o) {
        // find element
        int i;
        for (i = 0; i <= lastElement && data[i] != o; i++) {
            /* do nothing */
        }
        if (i <= lastElement) { // if found it
            data[i] = null;		// kill ref for GC
            int above = lastElement - i;
            if (above > 0) {
                System.arraycopy(data, i + 1, data, i, above);
            }
            lastElement--;
            return true;
        }
        return false;
    }

    public synchronized void setElementAt(Object obj, int i) {
        if (i >= data.length) {
            throw new ArrayIndexOutOfBoundsException(i + " >= " + data.length);
        }
        data[i] = obj;
        // track last element in the vector so we can append things
        if (i > lastElement) {
            lastElement = i;
        }
    }

    // return number of slots in the vector; e.g., you can set
    // the 30th element and size() will return 31.
    public int size() {
        return lastElement + 1;
    }
}
