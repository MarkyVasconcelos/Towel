package com.towel.el.handler;

import com.towel.bean.Formatter;
import com.towel.el.NotResolvableFieldException;

/**
 * 
 * An interface to especify the method to how to get and set the value of the
 * field.
 * 
 *@author Marcos Vasconcelos
 */
public interface FieldAccessHandler {
	/**
	 *An init method to resolve the field before can get and set the value.
	 * 
	 *@param clazz
	 *            Create a FieldAcessHandler to the given class.
	 *@param expression
	 *            The Field name.
	 */
	public void resolveField(Class<?> clazz, String expression)
			throws NotResolvableFieldException;

	/**
	 *@param t
	 *            The object to set this field value.
	 *@param value
	 *            The given value to this field.
	 *@param formatter
	 *            If this value needs to be converted to be set.
	 */
	public void setValue(Object t, Object value, Formatter formatter);

	/**
	 *@param t
	 *            The object to get this field value.
	 *@param formatter
	 *            If this value needs to be converted to be get.
	 *@return The value.
	 */
	public Object getValue(Object t, Formatter formatter);

	/**
	 *@return The resolved field type.
	 */
	public Class<?> getFieldType();

	/**
	 * @return The class at the current index.
	 */
	public Class<?> getTraceClassAt(int idx);
}
