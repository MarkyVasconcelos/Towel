package com.towel.io;

/**
 * Identifies a resource that can be closed.
 * 
 * @author Marcos A. Vasconcelos Junior
 */
public interface Closable {
	/**
	 * Must close the associated resource.
	 */
	public void close();
}
