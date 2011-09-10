package com.towel.swing;

import java.awt.Container;
import java.util.Map;

import com.towel.awt.ann.ActionManager;
import com.towel.bind.annotation.AnnotatedBinder;

public class Manager {
	private Object comp;
	private Map<Modules, Object> inits;

	public Manager(Object comp) {
		this.comp = comp;
	}

	public <T> T init(Modules module) {
		Object result = module.init(comp);
		inits.put(module, result);
		return (T) result;
	}

	public <T> T get(Modules module) {
		return (T) inits.get(module);
	}

	public enum Modules {
		AnnotatedBinder {
			public AnnotatedBinder init(Object comp) {
				return new AnnotatedBinder((Container) comp);
			}
		},
		ActionManager {
			public ActionManager init(Object comp) {
				return new ActionManager(comp);
			}
		};

		abstract Object init(Object comp);
	}
}
