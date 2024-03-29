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

public class StringUtils {
    /** General-purpose utility function for removing
     * characters from back of string
     * @param s The string to process
     * @param c The character to remove
     * @return The resulting string
     */
    static public String stripBack(String s, char c) {
        while (s.length() > 0 && s.charAt(s.length() - 1) == c) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    /** General-purpose utility function for removing
     * characters from back of string
     * @param s The string to process
     * @param remove A string containing the set of characters to remove
     * @return The resulting string
     */
    static public String stripBack(String s, String remove) {
        boolean changed;
        do {
            changed = false;
            for (int i = 0; i < remove.length(); i++) {
                char c = remove.charAt(i);
                while (s.length() > 0 && s.charAt(s.length() - 1) == c) {
                    changed = true;
                    s = s.substring(0, s.length() - 1);
                }
            }
        } while (changed);
        return s;
    }

    /** General-purpose utility function for removing
     * characters from front of string
     * @param s The string to process
     * @param c The character to remove
     * @return The resulting string
     */
    static public String stripFront(String s, char c) {
        while (s.length() > 0 && s.charAt(0) == c) {
            s = s.substring(1);
        }
        return s;
    }

    /** General-purpose utility function for removing
     * characters from front of string
     * @param s The string to process
     * @param remove A string containing the set of characters to remove
     * @return The resulting string
     */
    static public String stripFront(String s, String remove) {
        boolean changed;
        do {
            changed = false;
            for (int i = 0; i < remove.length(); i++) {
                char c = remove.charAt(i);
                while (s.length() > 0 && s.charAt(0) == c) {
                    changed = true;
                    s = s.substring(1);
                }
            }
        } while (changed);
        return s;
    }

    /** General-purpose utility function for removing
     * characters from the front and back of string
     * @param s The string to process
     * @param head exact string to strip from head
     * @param tail exact string to strip from tail
     * @return The resulting string
     */
    public static String stripFrontBack(String src, String head, String tail) {
        int h = src.indexOf(head);
        int t = src.lastIndexOf(tail);
        if (h == -1 || t == -1) return src;
        return src.substring(h + 1, t);
    }
}
