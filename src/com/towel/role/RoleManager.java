package com.towel.role;

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JMenu;

import com.towel.cfg.StringConfiguration;



public class RoleManager {
	private RoleMember member;

	public RoleManager(RoleMember role) {
		this.member = role;
	}

	public void manageAnnotated(Object instance) {
		Map<Field, Role> mapped = mapComps(instance.getClass());
		for (Entry<Field, Role> ent : mapped.entrySet()) {
			if (!ent.getValue().visibleTo().contains(member.getRoleName())) {
				try {
					Field f = ent.getKey();
					f.setAccessible(true);
					((Component) f.get(instance)).setVisible(false);
				} catch (Exception e) {
				}
			}
		}
	}

	public void manageNamedComps(Container cont) {
		Map<Component, String> mapped = mapComps(cont, null);
		for (Entry<Component, String> ent : mapped.entrySet()) {
			StringConfiguration cfg = new StringConfiguration(ent.getValue());
			if (!cfg.getAttribute("visibleTo").contains(member.getRoleName()))
				ent.getKey().setVisible(false);
		}
	}

	private Map<Component, String> mapComps(Container cont,
			Map<Component, String> mapped) {
		if (mapped == null)
			mapped = new HashMap<Component, String>();
		for (Component comp : cont.getComponents()) {
			if (comp instanceof Component)
				if (comp.getName() != null && comp.getName().length() != 0)
					if (!comp.getName().startsWith("null"))
						mapped.put((Component) comp, comp.getName());
			if (comp instanceof JMenu)
				mapJMenu((JMenu) comp, mapped);
			else if (comp instanceof Container)
				mapComps((Container) comp, mapped);

		}
		return mapped;
	}

	private void mapJMenu(JMenu menu, Map<Component, String> mapped) {
		for (Component comp : menu.getMenuComponents()) {
			if (comp instanceof Component)
				if (comp.getName() != null && comp.getName().length() != 0)
					if (!comp.getName().startsWith("null"))
						mapped.put((Component) comp, comp.getName());
			if (comp instanceof JMenu)
				mapJMenu((JMenu) comp, mapped);
			else if (comp instanceof Container)
				mapComps((Container) comp, mapped);
		}
	}

	private Map<Field, Role> mapComps(Class<?> view) {
		Map<Field, Role> mapped = new HashMap<Field, Role>();

		for (Field field : view.getDeclaredFields()) {
			if (Component.class.isAssignableFrom(field.getType())) {
				if (field.isAnnotationPresent(Role.class))
					mapped.put(field, field.getAnnotation(Role.class));
			}
		}

		return mapped;
	}
}
