package com.towel.collections;

import java.util.List;

public class ListNavigator<T> implements Navigator<T> {
	private int currentIndex;
	private List<T> list;

	public ListNavigator(List<T> list) {
		this.list = list;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public T get(int idx) {
		return list.get(idx);
	}

	@Override
	public T next() {
		currentIndex++;
		if (currentIndex == size())
			currentIndex = size() - 1;
		return get(currentIndex);
	}

	@Override
	public T previous() {
		currentIndex--;
		if (currentIndex == -1)
			currentIndex = 0;
		return get(currentIndex);
	}
}
