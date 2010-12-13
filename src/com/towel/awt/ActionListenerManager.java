package com.towel.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;

public class ActionListenerManager implements ActionListener {
	private List<ActionListener> before, after;
	private Map<AbstractButton, ActionListener> map;

	public ActionListenerManager() {
		map = new HashMap<AbstractButton, ActionListener>();
		before = new ArrayList<ActionListener>();
		after = new ArrayList<ActionListener>();
	}

	public void manage(AbstractButton source, ActionListener action) {
		map.put(source, action);
		source.addActionListener(this);
	}

	public void doBefore(ActionListener action) {
		before.add(action);
	}

	public void doAfter(ActionListener action) {
		after.add(action);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		for (ActionListener action : before)
			action.actionPerformed(arg0);
		try {
			map.get(arg0.getSource()).actionPerformed(arg0);
		} catch (StopException e) {
			e.getCause().printStackTrace();
		}
		for (ActionListener act : after)
			act.actionPerformed(arg0);
	}

	public static class StopException extends RuntimeException {
		public Exception cause;

		public StopException(Exception cause) {
			this.cause = cause;
		}

		public Exception getCause() {
			return cause;
		}
	}
}
