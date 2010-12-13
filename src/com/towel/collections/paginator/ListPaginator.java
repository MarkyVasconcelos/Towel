package com.towel.collections.paginator;

import java.util.ArrayList;
import java.util.List;

import com.towel.collections.CollectionsUtil;
import com.towel.collections.filter.Filter;
import com.towel.el.FieldResolver;



public class ListPaginator<T> implements Paginator<T> {

	private List<T> original;
	private List<T> list;
	private int currentPagination;
	private int maxPage;
	private int pageResults;
	private int listSize;
	

	protected ListPaginator(int listSize, int resultsPerPage) {
		pageResults = resultsPerPage;
		this.listSize = listSize;
		currentPagination = 0;
		calcPages();
	}

	public ListPaginator(List<T> list) {
		this(list, list.size());
	}

	public ListPaginator(List<T> list, int resultsPerPage) {
		original = list;
		this.list = new ArrayList<T>(list);
		pageResults = resultsPerPage;
		calcPages();
	}

	private void calcPages() {
		currentPagination = 0;
		listSize = list.size();
		if (pageResults == 0) {
			return;
		}
		if (listSize % pageResults == 0) {
			maxPage = listSize / pageResults - 1;
		} else {
			maxPage = listSize / pageResults;
		}
	}

	public List<T> nextResult() {
		int toIndex = 0;
		toIndex = (currentPagination + 1) * pageResults;
		if (toIndex > this.list.size()) {
			toIndex = this.list.size();
		}
		List<T> list = this.list.subList(currentPagination * pageResults,
				toIndex);
		currentPagination++;
		return list;
	}

	public void setData(List<T> list) {
		this.list = list;
		calcPages();
	}

	@Override
	public List<T> getData() {
		return list;
	}

	public int getCurrentPage() {
		return currentPagination;
	}

	public void setCurrentPage(int page) {
		currentPagination = page;
	}

	public int getMaxPage() {
		return maxPage;
	}

	@Override
	public void filter(final String text, final FieldResolver field) {
		list = CollectionsUtil.filter(original, new Filter<T>() {
			@Override
			public boolean accept(T obj) {
				Object objR = field.getValue(obj);
				if (objR == null)
					return false;
				return objR.toString().toUpperCase().contains(
						text.toUpperCase());
			}
		});
	}

}