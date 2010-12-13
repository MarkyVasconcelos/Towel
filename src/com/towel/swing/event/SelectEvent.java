package com.towel.swing.event;

public class SelectEvent implements Cloneable {
	private static final long serialVersionUID = 0x93ea05b654f6951bL;
	private Object source;
	private Object object;
	private int action;

	public SelectEvent(Object source, Object obj) {
		this(source, obj, -1);
	}

	public SelectEvent(Object src, Object obj, int action) {
		source = src;
		object = obj;
		this.action = action;
	}

	public Object getObject() {
		return object;
	}

	public int getAction() {
		return action;
	}

	public Object getSource() {
		return source;
	}

	public SelectEvent clone() {
		try {
			super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return new SelectEvent(source, object, action);
	}
}