package com.towel.el;

import java.util.HashMap;
import java.util.Map;

import com.towel.bean.DefaultFormatter;
import com.towel.bean.Formatter;
import com.towel.el.handler.FieldAccessHandler;
import com.towel.el.handler.FieldHandler;
import com.towel.reflec.ClassIntrospector;



/**
 * The class to access the field value.
 * 
 * @author Marcos Vasconcelos
 */
public class FieldResolver {
	private String fieldName;// The field Name.
	private String name;// A name for this field column.
	private Formatter formatter;
	private FieldAccessHandler method;
	private Class<?> owner;
	private static Class<? extends FieldAccessHandler> defaultHandler;
	private static Class<? extends Formatter> defaultFormatter;

	static {
		defaultHandler = FieldHandler.class;
		defaultFormatter = DefaultFormatter.class;
	}

	public static void setDefaultHandler(
			Class<? extends FieldAccessHandler> handler) {
		if (handler == null)
			throw new RuntimeException("Handler can not be null!");
		defaultHandler = handler;
	}

	public static void setDefaultFormatter(Class<? extends Formatter> formatter) {
		if (formatter == null)
			throw new RuntimeException("Formatter can not be null!");
		defaultFormatter = formatter;
	}

	public FieldResolver(Class<?> clazz, String fieldName, String name) {
		this(clazz, fieldName, name, null);
	}

	public FieldResolver(Class<?> clazz, String fieldName) {
		this(clazz, fieldName, "", null);
	}

	public FieldResolver(Class<?> clazz, String fieldName,
			FieldAccessHandler handler) {
		this(clazz, fieldName, "", handler);
	}

	public FieldResolver(Class<?> clazz, String fieldName, String name,
			FieldAccessHandler handler) {
		if (handler == null)
			try {
				handler = defaultHandler.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}

		owner = clazz;

		this.fieldName = fieldName;
		this.name = name;

		method = handler;
		method.resolveField(clazz, fieldName);

		try {
			setFormatter(defaultFormatter.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FieldResolver setFormatter(Formatter formatter) {
		if (formatter == null)
			throw new IllegalArgumentException("Formatter can't be null!");
		this.formatter = formatter;
		return this;
	}

	public void setValue(Object t, Object value) {
		method.setValue(t, value, formatter);
	}

	public Object getValue(Object t) {
		return method.getValue(t, formatter);
	}

	public String getName() {
		return name;
	}

	public Class<?> getFieldType() {
		Class<?> clazz;
		if (formatter instanceof DefaultFormatter)
			clazz = method.getFieldType();
		else {
			ClassIntrospector instro = new ClassIntrospector(
					formatter.getClass());
			clazz = instro.getMethodReturnClass("format", Object.class);
		}
		if (clazz.isPrimitive())
			return primitiveWrapers.get(clazz);
		return clazz;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Class<?> getOwnerClass() {
		return owner;
	}

	public Formatter getFormatter() {
		return formatter;
	}

	public Class<?> getTraceClassAt(int idx) {
		return method.getTraceClassAt(idx);
	}

	private static final Map<Class<?>, Class<?>> primitiveWrapers;
	static {
		primitiveWrapers = new HashMap<Class<?>, Class<?>>();
		primitiveWrapers.put(char.class, Character.class);
		primitiveWrapers.put(byte.class, Byte.class);
		primitiveWrapers.put(short.class, Short.class);
		primitiveWrapers.put(int.class, Integer.class);
		primitiveWrapers.put(long.class, Long.class);
		primitiveWrapers.put(float.class, Float.class);
		primitiveWrapers.put(double.class, Double.class);
		primitiveWrapers.put(boolean.class, Boolean.class);
	}
}
