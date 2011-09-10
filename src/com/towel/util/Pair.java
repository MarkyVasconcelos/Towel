package com.towel.util;

/**
 * Represents a pair of objects, in a single object.
 * 
 * @author Marcos A. Vasconcelos Junior
 * @param <T>
 *            the first object type
 * @param <K>
 *            the second object type
 */
public class Pair<T, K> {
	private T first;
	private K second;

	/**
	 * Creates a pair of objects.
	 * 
	 * @param f
	 *            the first one
	 * @param s
	 *            the second one
	 */
	public Pair(T f, K s) {
		first = f;
		second = s;
	}

	/**
	 * @return the first object
	 */
	public T getFirst() {
		return first;
	}

	/**
	 * @return the second object
	 */
	public K getSecond() {
		return second;
	}
}
