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
import java.util.NoSuchElementException;

/**An enumeration of a LList.  Maintains a cursor through the list.
 * bad things would happen if the list changed via another thread
 * while we were walking this list.
 */
final class LLEnumeration implements Enumeration {
    LLCell cursor;
    LList list;


    /**Create an enumeration attached to a LList*/
    public LLEnumeration(LList l) {
        list = l;
        cursor = list.head;
    }

    /** Return true/false depending on whether there are more
     * elements to enumerate.
     */
    public boolean hasMoreElements() {
        if (cursor != null)
            return true;
        return false;
    }

    /**Get the next element in the enumeration.  Destructive in that
     * the returned element is removed from the enumeration.  This
     * does not affect the list itself.
     * @return the next object in the enumeration.
     */
    public Object nextElement() {
        if (!hasMoreElements()) throw new NoSuchElementException();
        LLCell p = cursor;
        cursor = cursor.next;
        return p.data;
    }
}
