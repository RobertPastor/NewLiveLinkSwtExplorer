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

/** A circular buffer object used by CharBuffer */
public class CharQueue {
    /** Physical circular buffer of tokens */
    protected char[] buffer;
    /** buffer.length-1 for quick modulos */
    private int sizeLessOne;
    /** physical index of front token */
    private int offset;
    /** number of tokens in the queue */
    protected int nbrEntries;

    public CharQueue(int minSize) {
        // Find first power of 2 >= to requested size
        int size;
        if ( minSize<0 ) {
            init(16); // pick some value for them
            return;
        }
        // check for overflow
        if ( minSize>=(Integer.MAX_VALUE/2) ) {
            init(Integer.MAX_VALUE); // wow that's big.
            return;
        }
        for (size = 2; size < minSize; size *= 2) {
			// nothing
        }
        init(size);
    }

    /** Add token to end of the queue
     * @param tok The token to add
     */
    public final void append(char tok) {
        if (nbrEntries == buffer.length) {
            expand();
        }
        buffer[(offset + nbrEntries) & sizeLessOne] = tok;
        nbrEntries++;
    }

    /** Fetch a token from the queue by index
     * @param idx The index of the token to fetch, where zero is the token at the front of the queue
     */
    public final char elementAt(int idx) {
        return buffer[(offset + idx) & sizeLessOne];
    }

    /** Expand the token buffer by doubling its capacity */
    private final void expand() {
        char[] newBuffer = new char[buffer.length * 2];
        // Copy the contents to the new buffer
        // Note that this will store the first logical item in the
        // first physical array element.
        for (int i = 0; i < buffer.length; i++) {
            newBuffer[i] = elementAt(i);
        }
        // Re-initialize with new contents, keep old nbrEntries
        buffer = newBuffer;
        sizeLessOne = buffer.length - 1;
        offset = 0;
    }

    /** Initialize the queue.
     * @param size The initial size of the queue
     */
    public void init(int size) {
        // Allocate buffer
        buffer = new char[size];
        // Other initialization
        sizeLessOne = size - 1;
        offset = 0;
        nbrEntries = 0;
    }

    /** Clear the queue. Leaving the previous buffer alone.
     */
    public final void reset() {
        offset = 0;
        nbrEntries = 0;
    }

    /** Remove char from front of queue */
    public final void removeFirst() {
        offset = (offset + 1) & sizeLessOne;
        nbrEntries--;
    }
}
