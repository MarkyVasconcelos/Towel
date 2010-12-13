package com.towel.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;

@Deprecated
public class ActionManager implements ActionListener {
	private List<Action> before, after;
	private Map<AbstractButton, Action> map;

	public ActionManager() {
		map = new HashMap<AbstractButton, Action>();
		before = new ArrayList<Action>();
		after = new ArrayList<Action>();
	}

	public void manage(AbstractButton source, Action action) {
		map.put(source, action);
		source.addActionListener(this);
	}

	public void doBefore(Action action) {
		before.add(action);
	}

	public void doAfter(Action action) {
		after.add(action);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		for (Action action : before)
			action.doAction();
		try {
			map.get(arg0.getSource()).doAction();
		} catch (StopException e) {
			e.getCause().printStackTrace();
		}
		for (Action act : after)
			act.doAction();
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
