package com.towel.bind;

import java.awt.Component;
import java.awt.Container;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.text.JTextComponent;

import com.towel.bean.Formatter;
import com.towel.cfg.StringConfiguration;
import com.towel.collections.CollectionsUtil;
import com.towel.collections.filter.Filter;
import com.towel.el.FieldResolver;
import com.towel.exc.ExceptionCollecter;



/**
 * A component binder for JTextComponents. The Binder look for the parameters in
 * the getName method of each JTextComponent of the Container.
 * 
 * Use the setName in the components you want. You need at least the name of the
 * field you want to bind.
 * 
 * jTextComponent.setName("[name:attrName]")
 * 
 * If the field need a formatter you pass it as "[fmt:fmtName]" The fmtName must
 * match with one formatter passed in the constructor.
 * 
 * If you want a default value if in the object are blank you can use
 * "[dflt:Text]" if the value in the object is null or empty this text will
 * assume in the component
 * 
 * Binder is typed. And the Class argument need to be the class of the binded
 * objects will be.
 * 
 * To update the view you need to call the method updateView(Object obj) and to
 * update the model updateModel(Object target).
 * 
 * If you need more than one Binder in the Container maybe you need use the
 * prefix to shows to the binder the fields must be to each class. If you
 * setName with "[pfx:cads]" you need pass cads to map only the cads fields.
 * 
 * Example: jTextField.setName("[name:personName]")
 * jTextField2.setName("[name:age][fmt:int]"); binder = new Binder(container,
 * Person.class,new IntFormatter()); binder.updateView(personObject); //change
 * the fields values binder.updateModel(personObject);
 * 
 * jTextField.setName("[pfx:person][name:personName]")
 * jTextField2.setName("[pfx:cia][name:name]");
 * 
 * personBinder = new Binder("person",container,Person.class); ciaBinder = new
 * Binder("cia",container, Cia.class);
 * 
 * @author Marcos Vasconcelos
 */
public class NamedBinder implements Binder {
	private Map<JTextComponent, FieldResolver> binds;
	private Map<JTextComponent, Default> defaultValues;

	public NamedBinder(Container comp, Class<?> clazz, Formatter... formatters) {
		binds = new HashMap<JTextComponent, FieldResolver>();
		defaultValues = new HashMap<JTextComponent, Default>();
		for (Entry<JTextComponent, String> ent : mapComps(comp, null)
				.entrySet()) {
			String value = ent.getValue();
			StringConfiguration str = new StringConfiguration(value);
			final String formatter = str.getAttribute(FORMATTER);
			String name = str.getAttribute(NAME);
			FieldResolver resolver = new FieldResolver(clazz, name);
			if (!formatter.isEmpty()) {
				int fmtIdx = CollectionsUtil.firstIndexOf(formatters,
						new Filter<Formatter>() {
							@Override
							public boolean accept(Formatter obj) {
								return obj.getName().equals(formatter);
							}

						});
				if (fmtIdx == -1)
					throw new RuntimeException("Formatter name not found:"
							+ formatter);
				resolver.setFormatter(formatters[fmtIdx]);
			}
			binds.put(ent.getKey(), resolver);
			if (str.hasAttribute(DEFAULT)) {
				defaultValues
						.put(ent.getKey(), new Default(str
								.getAttribute(DEFAULT), str
								.getAttribute(DEFAULT_COND)));
			}
		}
	}

	public NamedBinder(String prefix, Container comp, Class<?> clazz,
			Formatter... formatters) {
		binds = new HashMap<JTextComponent, FieldResolver>();
		defaultValues = new HashMap<JTextComponent, Default>();
		for (Entry<JTextComponent, String> ent : mapComps(comp, null)
				.entrySet()) {
			String value = ent.getValue();
			StringConfiguration str = new StringConfiguration(value);
			if (str.getAttribute(PREFIX).equals(prefix)) {
				final String formatter = str.getAttribute(FORMATTER);
				String name = str.getAttribute(NAME);
				FieldResolver resolver = new FieldResolver(clazz, name);
				if (!formatter.isEmpty()) {
					int fmtIdx = CollectionsUtil.firstIndexOf(formatters,
							new Filter<Formatter>() {
								@Override
								public boolean accept(Formatter obj) {
									return obj.getName().equals(formatter);
								}

							});
					if (fmtIdx == -1)
						throw new RuntimeException("Formatter name not found:"
								+ formatter);
					resolver.setFormatter(formatters[fmtIdx]);
				}
				binds.put(ent.getKey(), resolver);
				if (str.hasAttribute(DEFAULT)) {
					defaultValues.put(ent.getKey(), new Default(str
							.getAttribute(DEFAULT), str
							.getAttribute(DEFAULT_COND)));
				}
			}
		}
	}

	private Map<JTextComponent, String> mapComps(Container cont,
			Map<JTextComponent, String> mapped) {
		if (mapped == null)
			mapped = new HashMap<JTextComponent, String>();

		for (Component comp : cont.getComponents()) {
			if (comp instanceof JTextComponent)
				if (comp.getName() != null && comp.getName().length() != 0)
					if (!comp.getName().startsWith("null"))
						mapped.put((JTextComponent) comp, comp.getName());
			if (comp instanceof Container)
				mapComps((Container) comp, mapped);
		}

		return mapped;
	}

	public void updateView(Object obj) {
		JTextComponent comp;
		FieldResolver field;
		ExceptionCollecter collecter = new ExceptionCollecter();
		for (Entry<JTextComponent, FieldResolver> ent : binds.entrySet()) {
			try {
				comp = ent.getKey();
				field = ent.getValue();
				String fieldValue = (String) field.getValue(obj);

				if (defaultValues.containsKey(comp)) {
					Default defaultValue = defaultValues.get(comp);
					fieldValue = defaultValue.getDefaultValue(fieldValue);
				}
				comp.setText(fieldValue);
			} catch (Throwable e) {
				collecter.collect(e);
			}
		}
		// if (!collecter.isEmpty())
		// collecter.throwException();
	}

	public void updateModel(Object obj) {
		JTextComponent comp;
		FieldResolver field;
		ExceptionCollecter collecter = new ExceptionCollecter();
		for (Entry<JTextComponent, FieldResolver> ent : binds.entrySet()) {
			comp = ent.getKey();
			field = ent.getValue();
			try {
				field.setValue(obj, comp.getText());
			} catch (Throwable e) {
				collecter.collect(e);
			}
		}
		// if (!collecter.isEmpty())
		// collecter.throwException();
	}

	public final static String FORMATTER = "fmt";
	public final static String DEFAULT = "dflt";
	public final static String DEFAULT_COND = "dflt_con";
	public final static String NAME = "name";
	public final static String PREFIX = "pfx";

	private static class Default {
		private String defaultValue = "";
		private String condition = "";
		private String conditionParam = "";

		public Default(String value, String condition) {
			defaultValue = value;
			if (!condition.isEmpty()) {
				this.condition = condition.substring(0, 1);
				conditionParam = condition.substring(1);
			}
		}

		public String getDefaultValue(String current) {
			if (current == null || current.isEmpty())
				return defaultValue;
			if (condition.equals("=")) {
				if (current.equals(conditionParam))
					return defaultValue;
			}
			return current;
		}
	}
}
