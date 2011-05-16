package com.towel.reflec;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassIntrospector {
	private Class<?> clazz;

	public ClassIntrospector(Class<?> clazz) {
		this.clazz = clazz;
	}

	public <E extends Annotation> List<AnnotatedElement<Field, E>> getAnnotatedFields(
			Class<E> ann) {
		List<AnnotatedElement<Field, E>> list = new ArrayList<AnnotatedElement<Field, E>>();
		for (Field f : clazz.getFields())
			if (f.isAnnotationPresent(ann))
				list.add(new AnnotatedElement<Field, E>(f, f.getAnnotation(ann)));

		return list;
	}

	public <E extends Annotation> List<AnnotatedElement<Field, E>> getAnnotatedDeclaredFields(
			Class<E> ann) {
		List<AnnotatedElement<Field, E>> list = new ArrayList<AnnotatedElement<Field, E>>();
		for (Field f : clazz.getDeclaredFields())
			if (f.isAnnotationPresent(ann))
				list.add(new AnnotatedElement<Field, E>(f, f.getAnnotation(ann)));

		return list;
	}

	public <E extends Annotation> List<AnnotatedElement<Method, E>> getAnnotatedDeclaredMethods(
			Class<E> ann) {
		List<AnnotatedElement<Method, E>> list = new ArrayList<AnnotatedElement<Method, E>>();
		for (Method f : clazz.getDeclaredMethods())
			if (f.isAnnotationPresent(ann))
				list.add(new AnnotatedElement<Method, E>(f, f
						.getAnnotation(ann)));

		return list;
	}

	public <E extends Annotation> List<AnnotatedElement<Method, E>> getAnnotatedMethods(
			Class<E> ann) {
		List<AnnotatedElement<Method, E>> list = new ArrayList<AnnotatedElement<Method, E>>();
		for (Method f : clazz.getMethods())
			if (f.isAnnotationPresent(ann))
				list.add(new AnnotatedElement<Method, E>(f, f
						.getAnnotation(ann)));

		return list;
	}

	public Class<?> getMethodReturnClass(String string, Class<?> arg) {
		try {
			Method m = clazz.getDeclaredMethod(string, arg);
			return m.getReturnType();
		} catch (Exception e) {
			return String.class;
		}
	}

	/**
	 * This searchs for the declared field
	 * 
	 * @return an AccesibleField
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public Field getField(String fieldName) throws NoSuchFieldException {
		Field f = null;
		try {
			f = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			return getField(fieldName, clazz.getSuperclass());
		}
		
		f.setAccessible(true);
		return f;
	}

	/**
	 * This searchs for the declared field
	 * 
	 * @return an AccesibleField
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	private Field getField(String fieldName, Class<?> clazz)
			throws NoSuchFieldException {
		if (clazz == null)
			throw new NoSuchFieldException();

		Field f = null;
		try {
			f = clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			return getField(fieldName, clazz.getSuperclass());
		}

		f.setAccessible(true);
		return f;
	}

	public static class AnnotatedElement<T, K extends Annotation> {
		private T comp;
		private K annotation;

		public AnnotatedElement(T t, K k) {
			comp = t;
			annotation = k;
		}

		public T getElement() {
			return comp;
		}

		public K getAnnotation() {
			return annotation;
		}
	}

}
