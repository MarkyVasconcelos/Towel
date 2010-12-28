package com.towel.bean;

/**
 * Empty formatter, that returns <code>null</code> in all methods.
 * 
 * @author Marcos A. Vasconcelos Junior
 */
public class FormatterAdapter implements Formatter {
	@Override
	public Object format(Object obj) {
		return null;
	}

	@Override
	public Object parse(Object s) {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}
}