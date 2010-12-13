package com.towel.el.handler;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import com.towel.bean.Formatter;
import com.towel.el.NotResolvableFieldException;



/**
 * An implementation of FieldAccessHandler who use the get and set methods
 * declared in the class of the field.
 * 
 *@see mark.utisl.el.handler.FieldAccessHandler
 * 
 *@author Marcos Vasconcelos
 */
public class MethodHandler implements FieldAccessHandler {
	private Method setter;
	private LinkedList<Method> getterTrace;
	private List<Class<?>> classesTrace;

	public MethodHandler() {
		classesTrace = new LinkedList<Class<?>>();
		getterTrace = new LinkedList<Method>();
	}

	@Override
	public Class<?> getFieldType() {
		return getterTrace.getLast().getReturnType();
	}

	@Override
	public Object getValue(Object t, Formatter formatter) {
		if (t == null)
			return null;
		Object obj = null;
		try {
			obj = getterTrace.get(0).invoke(t);
			for (int i = 1; i < getterTrace.size(); i++)
				obj = getterTrace.get(i).invoke(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (formatter != null)
			return formatter.format(obj);
		else
			return obj;
	}

	public void resolveField(Class<?> clazz, String expression) {
		classesTrace.add(clazz);
		String[] trace = expression.split("[.]");
		for (int i = 0; i < trace.length; i++)
			addField(trace[i]);
		if (getterTrace.size() != trace.length) {
			getterTrace.clear();
			classesTrace.clear();
			throw new RuntimeException("Impossible to resolve field.");
		}
		setter = getSetterMethod(classesTrace.get(classesTrace.size() - 2),
				trace[trace.length - 1]);
	}

	@Override
	public void setValue(Object t, Object value, Formatter formatter) {
		if (t == null)
			return;
		Object obj = null;
		try {
			obj = t;
			int size = getterTrace.size() - 1;
			if (size > -1)
				for (int i = 0; i < size; i++)
					obj = getterTrace.get(i).invoke(obj);

			setter.invoke(obj, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addField(String fieldName) {
		Class<?> clazz = classesTrace.get(classesTrace.size() - 1);
		// Field next = clazz.getDeclaredField(fieldName);
		Method m = getGetterMethod(clazz, fieldName);
		classesTrace.add(m.getReturnType());
		getterTrace.add(m);
	}

	/**
	 *Search in the class and return the getter method for the given fieldName
	 * 
	 */
	private Method getSetterMethod(Class<?> clazz, String fieldName) {
		Method m = null;
		try {
			m = clazz.getMethod("set"
					+ String.valueOf(fieldName.charAt(0)).toUpperCase()
					+ fieldName.substring(1), classesTrace.get(classesTrace
					.size() - 1));
		} catch (NoSuchMethodException e) {
			throw NotResolvableFieldException.create(e, fieldName, clazz);
		}
		return m;
	}

	/**
	 *Search in the class and return the getter method for the given fieldName
	 * If field is boolean the method search isFieldName if not the method
	 * search getFieldName
	 */
	private Method getGetterMethod(Class<?> clazz, String fieldName) {
		Method m = null;
		try {
			m = clazz.getMethod("get"
					+ String.valueOf(fieldName.charAt(0)).toUpperCase()
					+ fieldName.substring(1));
		} catch (NoSuchMethodException e) {
			try {
				m = clazz.getMethod("is"
						+ String.valueOf(fieldName.charAt(0)).toUpperCase()
						+ fieldName.substring(1));
			} catch (NoSuchMethodException ne) {
				throw NotResolvableFieldException.create(e, fieldName, clazz);
			}
		}
		return m;
	}

	@Override
	public Class<?> getTraceClassAt(int idx) {
		return classesTrace.get(idx);
	}
}
