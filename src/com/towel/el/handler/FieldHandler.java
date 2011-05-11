package com.towel.el.handler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.towel.bean.Formatter;
import com.towel.el.NotResolvableFieldException;



/**
 * An implementation of FieldAccessHandler who use the direct access to the
 * field.
 * 
 *@see mark.utisl.el.handler.FieldAccessHandler
 * 
 *@author Marcos Vasconcelos
 */
public class FieldHandler implements FieldAccessHandler {
	private List<Class<?>> classesTrace;// A trace to the field
	private List<Field> fields;// The fields trace.

	public FieldHandler() {
		classesTrace = new ArrayList<Class<?>>();
		fields = new ArrayList<Field>();
	}

	@Override
	public void resolveField(Class<?> clazz, String expression) {
		if (clazz == null || expression == null)
			throw new IllegalArgumentException("Arguments can't be null!");
		classesTrace.add(clazz);
		String[] trace = expression.split("[.]");

		for (int i = 0; i < trace.length; i++)
			addField(trace[i]);
	}

	@Override
	public Object getValue(Object t, Formatter formatter) {
		if (t == null)
			return null;
		Object obj = null;
		try {
			obj = t;
			for (int i = 0; i < fields.size(); i++)
				obj = fields.get(i).get(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return formatter.format(obj);
	}

	@Override
	public void setValue(Object t, Object value, Formatter formatter) {
		if (t == null)
			return;
		Object obj = null;
		Field field = null;
		try {
			obj = t;
			int size = fields.size() - 1;
			if (size > -1) {
				for (int i = 0; i < size; i++)
					obj = fields.get(i).get(obj);
				field = fields.get(fields.size() - 1);
			} else
				field = fields.get(0);
			field.set(obj, formatter.parse(value));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void addField(String fieldName) {
		Class<?> clazz = classesTrace.get(classesTrace.size() - 1);
		Field f = getAcessibleField(clazz, fieldName);
		classesTrace.add(f.getType());
		fields.add(f);
	}

	private Field getAcessibleField(Class<?> clazz, String fieldName) {
		Field f = null;
		try {
			f = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			if(clazz.getSuperclass() == null){
				NotResolvableFieldException ex = new NotResolvableFieldException(
						fieldName, clazz);
				ex.setStackTrace(e.getStackTrace());
				throw ex;
			}
			
			return getAcessibleField(clazz.getSuperclass(), fieldName);
		}
		f.setAccessible(true);
		return f;
	}

	@Override
	public Class<?> getFieldType() {
		return fields.get(fields.size() - 1).getType();
	}

	@Override
	public Class<?> getTraceClassAt(int idx) {
		return classesTrace.get(idx);
	}

	public Field getField() {
		return fields.get(fields.size() - 1);
	}

	public Field getField(int idx) {
		return fields.get(idx);
	}

	public int getFieldTraceSize() {
		return fields.size();
	}
}
