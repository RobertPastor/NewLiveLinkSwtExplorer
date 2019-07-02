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
package com.javadude.antxr.sample;

/**
 * A simple Person bean
 */
public class Person {
	private String ssn;
	private String firstName;
	private String lastName;

	/**
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Set the first name
	 * @param firstName the new value
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Set the last name
	 * @param lastName the new value
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * @return the ssn
	 */
	public String getSsn() {
		return ssn;
	}
	
	/**
	 * Set the ssn
	 * @param ssn the new value
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	
	
}
