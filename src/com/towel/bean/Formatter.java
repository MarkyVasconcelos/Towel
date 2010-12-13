package com.towel.bean;

/**
 *Simple interface to convert a object to String and a String to a object.
 * 
 *@author Marcos Vasconcelos
 */
public interface Formatter {
	/**
	 * Format the object, should return String for most all uses.
	 */
	public abstract Object format(Object obj);

	/**
	 * Convert given object to this Object.
	 * In most cases the object parameter is String
	 */
	public abstract Object parse(Object s);

	/**
	 * Naming proposes only
	 */
	public abstract String getName();
}