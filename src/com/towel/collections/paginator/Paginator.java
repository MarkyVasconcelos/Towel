package com.towel.collections.paginator;

import java.util.List;

import com.towel.el.FieldResolver;



public interface Paginator<T> {
	public List<T> nextResult();

	public int getCurrentPage();

	public void setCurrentPage(int page);

	public int getMaxPage();

	public void setData(List<T> list);

	public List<T> getData();

	public void filter(String text, FieldResolver field);
}
