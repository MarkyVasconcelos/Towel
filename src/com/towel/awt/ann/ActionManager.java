package com.towel.awt.ann;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;

import com.towel.reflec.ClassIntrospector;
import com.towel.reflec.ClassIntrospector.AnnotatedElement;



public class ActionManager {
	private Object comp;
	private Class<?> clazz;

	public ActionManager(Object comp) {
		clazz = comp.getClass();
		this.comp = comp;
		List<AnnotatedElement<Field, ActionSequence>> sequences = new ClassIntrospector(
				clazz).getAnnotatedDeclaredFields(ActionSequence.class);
		for (AnnotatedElement<Field, ActionSequence> ann : sequences) {
			AbstractButton button;
			try {
				ann.getElement().setAccessible(true);
				button = (AbstractButton) (JButton) ann.getElement().get(comp);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			for (Action action : ann.getAnnotation().value())
				resolve(button, action);
		}

		List<AnnotatedElement<Field, Action>> annotateds = new ClassIntrospector(
				clazz).getAnnotatedDeclaredFields(Action.class);
		for (AnnotatedElement<Field, Action> ann : annotateds) {
			AbstractButton button;
			try {
				ann.getElement().setAccessible(true);
				button = (AbstractButton) (JButton) ann.getElement().get(comp);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			Action action = ann.getAnnotation();
			resolve(button, action);
		}
	}

	private void resolve(AbstractButton button, Action action) {
		Class<?> listener = action.listener();
		String method = action.method();

		if (listener.equals(ActionListener.class))
			try {
				button.addActionListener(new MethodInvokerListener(method));
			} catch (NoSuchMethodException e1) {
				throw new RuntimeException(e1);
			}
		else
			try {
				if ((listener.getModifiers() & Modifier.STATIC) != 0)
					button.addActionListener((ActionListener) listener
							.newInstance());
				else {
					Constructor<?> constr = listener
							.getDeclaredConstructor(clazz);
					constr.setAccessible(true);
					button.addActionListener((ActionListener) constr
							.newInstance(comp));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
	}

	private class MethodInvokerListener implements ActionListener {
		private Method method;

		public MethodInvokerListener(String method)
				throws NoSuchMethodException {
			this.method = clazz.getDeclaredMethod(method);
			this.method.setAccessible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				method.invoke(comp);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}
}
