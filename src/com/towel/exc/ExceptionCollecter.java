package com.towel.exc;

import java.util.ArrayList;
import java.util.List;

public class ExceptionCollecter {
	private List<Throwable> list;

	public ExceptionCollecter() {
		list = new ArrayList<Throwable>();
	}

	public void collect(Throwable t) {
		list.add(t);
	}

	public boolean isEmpty() {
		return list.size() == 0;
	}

	public void throwException() {
		throw new CollectedExceptions(list);
	}
}
