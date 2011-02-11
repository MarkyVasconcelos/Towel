package com.towel.bean;

/*
 * Copyright (c) 2011 Felipe Priuli
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *The above copyright notice and this permission notice shall be included at least in this file.
 *
 * The Software shall be used for Good, not Evil.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import java.util.ArrayList;
import java.util.List;

import com.towel.bean.Formatter;
import com.towel.el.FieldResolver;

/**
 * <code>DynamicFormatter</code> class implements
 * <code>com.towel.bean.Formatter</code>. This class is a dynamic formatter, can
 * be used to format many types of entitys.
 * 
 * To use, create a new isntance of this class passing the Class wich this will
 * format and the text that will be between each value from the fields. Then add
 * FieldResolvers, representing the fields wich will appear in the formatted
 * text.
 * 
 * Ex. 
 * DynamicFormatter<Person> formatter = new DynamicFormatter<Person>(Person.class, " - ");
 * formatter.addField(new FieldResolver(Person.class, "name");
 * formatter.addField(new FieldResolver(Person.class, "age");
 * 
 * String s = formatter.format(new Person("Marcos", 18));
 * The content on 's' will be 'Marcos - 18'
 * 
 * To single fields, the DefaultFormatter can be used alone.
 * 
 * 
 * @author Felipe Priuli
 * @author Marcos Vasconcelos
 * @version 0.2 beta 20/01/2011
 * @param <T>
 *            - Tipo da classe que representa este Formatter
 */
public class DynamicFormatter<T> implements Formatter {
	private Class<T> clazz;
	protected List<FieldResolver> fieldList;

	/**
	 * Text to separate the values if theres more than a field.
	 */
	protected String separator;

	/**
	 * Contrutor
	 * 
	 * @param t
	 *            - Class wich this formatter was created for
	 */
	public DynamicFormatter(Class<T> t) {
		this.clazz = t;
		this.fieldList = new ArrayList<FieldResolver>();
	}

	/**
	 * Constructor
	 * 
	 * @param t
	 *            - Class wich this formatter was created for
	 * @param separator
	 *            - Text to use between the fields values.
	 */
	public DynamicFormatter(Class<T> t, String separator) {
		this(t);
		this.clazz = t;
		// this.fieldList.add(new FieldResolver(t, fieldName));
		setSeparator(separator);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object format(final Object arg0) {
		if (this.separator == null) {
			this.separator = "";
		}
		if (arg0 == null) {
			return "";
		}

		T obj = ((T) arg0);
		try {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < this.fieldList.size(); i++) {
				sb.append(fieldList.get(i).getValue(obj));
				if (!((i + 1) == this.fieldList.size())) {
					sb.append(this.separator);
				}
			}

			return sb.toString();

		} catch (SecurityException e) {
			throw new IllegalArgumentException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(
					"Field is no pattern JavaBeans to acess method", e);
		}

	}

	@Override
	public String getName() {
		return clazz.getClass().getSimpleName().toLowerCase();
	}

	@Override
	public Object parse(Object arg0) {
		return null;// Never get invoked, JComboBox cannot be editable
	}

	/**
	 * Returns the text to use as separator
	 * 
	 * @return the separator
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * Get the text to use between the fields values.
	 * 
	 * @param separator
	 *            the separetor to set
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * @return the fieldList
	 */
	private List<FieldResolver> getFieldList() {
		return fieldList;
	}

	/**
	 * Add a field resolver to this DynamicFormatter
	 * 
	 * @param resolver
	 *            - The resolver to add in the list
	 */
	public void addField(FieldResolver resolver) {
		getFieldList().add(resolver);
	}

	/**
	 * Empty the FieldResolvers list Any call to format after this method and
	 * without adding new fields will result int a empty String
	 */
	public void clear() {
		getFieldList().clear();
	}
}