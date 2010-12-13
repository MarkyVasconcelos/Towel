package com.towel.cache;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import com.towel.el.FieldResolver;
import com.towel.el.annotation.AnnotationResolver;



public class FormatterCache {
	private static final Map<Class<?>, Map<String, Reference<FieldResolver>>> RESOLVERS = new WeakHashMap<Class<?>, Map<String, Reference<FieldResolver>>>();

	private FormatterCache() {
	}

	public static FieldResolver getResolver(Class<?> clazz, String resolverName) {
		if (clazz == null)
			throw new IllegalArgumentException();

		Map<String, Reference<FieldResolver>> clazzMap = RESOLVERS.get(clazz);
		if (clazzMap == null) {
			clazzMap = new WeakHashMap<String, Reference<FieldResolver>>();
			RESOLVERS.put(clazz, clazzMap);
		}

		WeakReference<FieldResolver> resolver = (WeakReference<FieldResolver>) clazzMap
				.get(resolverName);
		if (resolver == null) {
			resolver = new WeakReference<FieldResolver>(new AnnotationResolver(
					clazz).resolveSingle(resolverName));
			clazzMap.put(resolverName, resolver);
		}

		FieldResolver result = resolver.get();
		if (result == null) {
			resolver = new WeakReference<FieldResolver>(new AnnotationResolver(
					clazz).resolveSingle(resolverName));
			clazzMap.put(resolverName, resolver);
		}
		return result;
	}
}