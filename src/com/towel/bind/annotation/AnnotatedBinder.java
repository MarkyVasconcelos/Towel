package com.towel.bind.annotation;

import java.awt.Container;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.text.JTextComponent;

import com.towel.bind.Binder;
import com.towel.bind.modifier.ComponentModifier;
import com.towel.bind.modifier.JCheckBoxModifier;
import com.towel.bind.modifier.JTextComponentModifier;
import com.towel.el.FieldResolver;
import com.towel.el.annotation.AnnotationResolver;
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
public class AnnotatedBinder implements Binder {
	private List<ComponentModifier> comps;

	public AnnotatedBinder(Container comp) {
		comps = new ArrayList<ComponentModifier>();

		Class<?> form = comp.getClass();
		if (!form.isAnnotationPresent(Form.class))
			throw new IllegalArgumentException(
					"Class should implements com.towel.bind.annotation.Form");

		Class<?> clazz = form.getAnnotation(Form.class).value();

		try {
			for (Field f : form.getDeclaredFields()) {
				if (f.isAnnotationPresent(Bindable.class)) {
					f.setAccessible(true);
					Bindable bind = f.getAnnotation(Bindable.class);
					FieldResolver resolver = null;
					if (bind.resolvable()) {
						resolver = new AnnotationResolver(clazz)
								.resolveSingle(bind.field());
					} else {
						resolver = new FieldResolver(clazz, bind.field(), bind
								.handler().newInstance());
						resolver.setFormatter(bind.formatter().newInstance());
					}
					if (JTextComponent.class.isAssignableFrom(f.getType()))
						comps.add(new JTextComponentModifier((JTextComponent) f
								.get(comp), resolver));
					if (JCheckBox.class.isAssignableFrom(f.getType())) {
						comps.add(new JCheckBoxModifier(
								(JCheckBox) f.get(comp), resolver));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateView(Object obj) {
		ExceptionCollecter collecter = new ExceptionCollecter();
		for (ComponentModifier modifier : comps)
			try {
				modifier.updateComponent(obj);
			} catch (Exception e) {
				collecter.collect(e);
			}

		// if (!collecter.isEmpty())
		// collecter.throwException();
	}

	public void updateModel(Object obj) {
		ExceptionCollecter collecter = new ExceptionCollecter();
		for (ComponentModifier modifier : comps)
			try {
				modifier.updateModel(obj);
			} catch (Exception e) {
				collecter.collect(e);
			}
		// if (!collecter.isEmpty())
		// collecter.throwException();
	}

	public final static String FORMATTER = "fmt";
	public final static String DEFAULT = "dflt";
	public final static String DEFAULT_COND = "dflt_con";
	public final static String NAME = "name";
	public final static String PREFIX = "pfx";
}