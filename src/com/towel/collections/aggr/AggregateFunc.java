package com.towel.collections.aggr;

/**
 * Functions to work over an object, the object is of the type T
 * 
 * @author marcos.vasconcelos
 */
public interface AggregateFunc<T> {
	/**
	 * Init this Func with the initial values.
	 * This method is called when a new Calculation over a Collection is going to ve initiated.
	 * When implementing, reset all values to it initial value.
	 */
	public void init();
	/**
	 * Called over each value in a List.
	 * This method should calculate the new value with a previous value.
	 * @param obj
	 */
	public void update(T obj);
	/**
	 * Called when the iteration is over and the final value is done.
	 * @return The value of the function apllied over all objects passed in Func.update
	 */
	public T getResult();
}
