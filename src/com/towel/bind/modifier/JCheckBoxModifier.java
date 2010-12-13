package com.towel.bind.modifier;

import javax.swing.JCheckBox;

import com.towel.el.FieldResolver;



public class JCheckBoxModifier extends ComponentModifier {
	private JCheckBox comp;

	public JCheckBoxModifier(JCheckBox comp, FieldResolver resolver) {
		super(resolver);
		this.comp = comp;
	}

	@Override
	public void updateComponent(Object obj) {
		comp.setSelected((Boolean) getResolver().getValue(obj));
	}

	@Override
	public void updateModel(Object obj) {
		getResolver().setValue(obj, comp.isSelected());
	}
}
