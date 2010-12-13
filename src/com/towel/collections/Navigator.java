package com.towel.collections;


public interface Navigator<T> {
	public int size();

	public T get(int idx);

	public T next();

	public T previous();
}
