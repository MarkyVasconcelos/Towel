package com.towel.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.towel.bean.DefaultFormatter;
import com.towel.bean.Formatter;
import com.towel.el.handler.FieldAccessHandler;
import com.towel.el.handler.FieldHandler;



@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Bindable {
	String field();

	Class<? extends FieldAccessHandler> handler() default FieldHandler.class;

	Class<? extends Formatter> formatter() default DefaultFormatter.class;
	
	boolean resolvable() default false;
}
