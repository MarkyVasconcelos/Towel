package com.towel.other;

public class Pair<T, K> {
	private T first;
	private K second;

	public Pair(T f, K s) {
		first = f;
		second = s;
	}

	public T getFirst() {
		return first;
	}

	public K getSecond() {
		return second;
	}
}
