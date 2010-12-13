package com.towel.collections;

import java.util.ArrayList;
import java.util.List;

import com.towel.collections.aggr.AggregateFunc;
import com.towel.collections.filter.Filter;
import com.towel.el.FieldResolver;



@SuppressWarnings("unchecked")
public class CollectionsUtil {
	public static <T> List<T> filter(List<T> coll, Filter<T> filter) {
		List<T> result = new ArrayList<T>();
		for (T t : coll)
			if (filter.accept(t))
				result.add(t);
		return result;
	}

	public static <T> int firstIndexOf(List<T> coll, Filter<T> filter) {
		int i = coll.size();
		for (int j = 0; j < i; j++)
			if (filter.accept(coll.get(j)))
				return j;
		return -1;
	}

	public static <T> int lastIndexOf(List<T> coll, Filter<T> filter) {
		int i = coll.size();
		int idx = -1;
		for (int j = 0; j < i; j++)
			if (filter.accept(coll.get(j)))
				idx = j;
		return idx;
	}

	public static <T> int firstIndexOf(T[] obj, Filter<T> filter) {
		int i = obj.length;
		for (int j = 0; j < i; j++)
			if (filter.accept(obj[j]))
				return j;
		return -1;
	}

	/**
	 * @param <T>
	 * @param obj
	 * @param filter
	 * @return
	 */
	public static <T> int lastIndexOf(T[] obj, Filter<T> filter) {
		int i = obj.length;
		int idx = -1;
		for (int j = 0; j < i; j++)
			if (filter.accept(obj[j]))
				idx = j;
		return idx;
	}

	/**
	 * @param <T>
	 * @param list
	 * @param filter
	 * @return
	 */
	public static <T> Integer[] allMatchIndex(List<T> list, Filter<T> filter) {
		Integer[] result = new Integer[list.size()];
		int currentIdx = 0;
		for (int i = 0; i < list.size(); i++)
			if (filter.accept(list.get(i)))
				result[currentIdx++] = i;
		return (Integer[]) trim(result);
	}

	/**
	 * 
	 * Returns an array without null values.
	 * 
	 * @param obj
	 * @return
	 */
	public static Object[] trim(Object[] obj) {
		int nullIndex = -1;
		for (int i = 0; i < obj.length; i++)
			if (obj[i] == null)
				nullIndex = i;
		Object[] objs = new Object[++nullIndex];
		for (int i = 0; i < nullIndex; i++)
			objs[i] = obj[i];
		return objs;
	}

	/**
	 * Apply an aggregate function over all itens in the collection.
	 * 
	 * @param <T>
	 * @param func
	 * @param l
	 * @return
	 */
	public static <T> T aggregate(AggregateFunc<T> func, List<T> l) {
		func.init();
		for (T t : l)
			func.update(t);
		return func.getResult();
	}

	/**
	 * Apply an aggregate function over all itens in the collection.
	 * 
	 * If the list ins't the object with the function should be applied, this
	 * split the Object and apply in the result.
	 * 
	 * @param <T>
	 * @param func
	 * @param l
	 * @param field
	 * @return
	 */
	public static <T> T aggregate(AggregateFunc<T> func, List<?> l, String field) {
		return aggregate(func, (List<T>) split(l, field));
	}

	/**
	 * Returns a list with all values of a attribute of the object T.
	 * 
	 * @return
	 */
	public static <T> List<T> split(List<?> list, String fieldName) {
		List<T> result = new ArrayList<T>();
		if (list.isEmpty())
			return result;

		Class<?> clazz = list.get(0).getClass();
		FieldResolver resolver = new FieldResolver(clazz, fieldName);

		for (Object obj : list)
			result.add((T) resolver.getValue(obj));

		return result;
	}

}
