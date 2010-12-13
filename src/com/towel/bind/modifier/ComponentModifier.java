package com.towel.bind.modifier;

import com.towel.el.FieldResolver;

public abstract class ComponentModifier {
	private FieldResolver resolver;

	public ComponentModifier(FieldResolver resolver) {
		this.resolver = resolver;
	}

	public abstract void updateModel(Object obj);

	public abstract void updateComponent(Object obj);

	protected FieldResolver getResolver() {
		return resolver;
	}
}
