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

public class Utils {
	private static boolean useSystemExit = true;
	private static boolean useDirectClassLoading = false;
	static {
		if ("true".equalsIgnoreCase(System.getProperty("ANTXR_DO_NOT_EXIT", "false")))
			useSystemExit = false;
		if ("true".equalsIgnoreCase(System.getProperty("ANTXR_USE_DIRECT_CLASS_LOADING", "false")))
			useDirectClassLoading = true;
	}
	public static Class loadClass(String name) throws ClassNotFoundException {
		if (useDirectClassLoading)
			return Class.forName(name);
		return Thread.currentThread().getContextClassLoader().loadClass(name);
	}
	public static Object createInstanceOf(String name) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return loadClass(name).newInstance();
	}
	public static void error(String message) {
		if (useSystemExit)
			System.exit(1);
		throw new RuntimeException("ANTXR Panic: " + message);
	}
	public static void error(String message, Throwable t) {
		if (useSystemExit)
			System.exit(1);
		throw new RuntimeException("ANTXR Panic", t);
	}
}
