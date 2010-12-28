package com.towel.bean;

/**
 * Default formatter that returns the same object passed as paramater to the
 * methods.
 * 
 * @author Marcos A. Vasconcelos Junior
 */
public class DefaultFormatter implements Formatter {
	@Override
	public Object format(Object obj) {
		return obj;
	}

	@Override
	public Object parse(Object obj) {
		return obj;
	}

	@Override
	public String getName() {
		return "obj_def";
	}
}
