package com.towel.bean;

/**
 * Used to convert an object to <code>String</code> and vice versa.
 * 
 * @author Marcos A. Vasconcelos Junior
 */
public interface Formatter {
	/**
	 * Formats the given object. Should return a <code>String</code>, in most
	 * cases.
	 * 
	 * @param obj
	 *            the object to format
	 * @return the formatted object
	 */
	public abstract Object format(Object obj);

	/**
	 * Parses the given parameter to an <code>Object</code>. In most cases, the
	 * parameter is a <code>String</code>.
	 * 
	 * @param s
	 *            the formatted object
	 * @return the parsed object
	 */
	public abstract Object parse(Object s);

	/**
	 * Should return a name for this <code>Formatter</code>. Used only
	 * internally (not visible to user).
	 * 
	 * @return the name of this <code>Formatter</code>
	 */
	public abstract String getName();
}