package com.towel.swing.scroll;

public class ScrollEvent {
	private Object source;
	private Object value;

	public ScrollEvent(Object value, Object source) {
		this.value = value;
		this.source = source;
	}

	public Object getSource() {
		return source;
	}

	public Object getValue() {
		return value;
	}
}
