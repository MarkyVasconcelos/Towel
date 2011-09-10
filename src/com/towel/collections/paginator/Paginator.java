package com.towel.collections.paginator;

import java.util.List;

import com.towel.el.FieldResolver;

/**
 * A Paginator<T> should return "pages" of results based on navigation.
 * 
 * @author Marcos Vasconcelos
 * @param <T>
 */
public interface Paginator<T> {
	/**
	 * 
	 * @return The actual content for the current page
	 */
	public List<T> nextResult();

	/**
	 * @return Return the current page index
	 */
	public int getCurrentPage();

	/**
	 * Components that uses a Paginator as it's adapter will call this passing
	 * the index for the page that will be shown soon.
	 */
	public void setCurrentPage(int page);

	/**
	 * Should return the index of the last page, components that uses a
	 * Paginator as it's adapter will never try to setCurrentPage beyond
	 * getMaxPage() that will be shown soon.
	 * 
	 * @see com.towel.collections.paginator.Paginator.setCurrentPage
	 * @return The index of the last page
	 */
	public int getMaxPage();

	/**
	 * The Paginator should use this List as the data for pagination
	 * 
	 * @param The
	 *            data that will be paginated
	 */
	public void setData(List<T> list);

	/**
	 * @return The Data of this adapter without pagination
	 */
	public List<T> getData();

	@Deprecated
	public void filter(String text, FieldResolver field);
}
