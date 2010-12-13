package com.towel.collections.filter;

public interface Filter<T> {
	public boolean accept(T obj);
}
