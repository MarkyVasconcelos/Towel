package com.towel.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;

/**
 * Manager that maps an action to a button.
 * 
 * @author Marcos A. Vasconcelos Junior
 * @deprecated Replaced by class {@link com.towel.awt.ann.ActionManager}.
 */
@Deprecated
public class ActionManager implements ActionListener {
	private List<Action> before, after;
	private Map<AbstractButton, Action> map;

	/**
	 * Creates a new <code>ActionManager</code>.
	 */
	public ActionManager() {
		map = new HashMap<AbstractButton, Action>();
		before = new ArrayList<Action>();
		after = new ArrayList<Action>();
	}

	/**
	 * Associates a button with an action, that will be executed when the button
	 * has been pressed.
	 * 
	 * @param source
	 *            the button
	 * @param action
	 *            the action to execute
	 */
	public void manage(AbstractButton source, Action action) {
		map.put(source, action);
		source.addActionListener(this);
	}

	/**
	 * Adds a new action to be done when any mapped button is pressed. That
	 * actions will be executed before the specific button's action.
	 * 
	 * The actions registered with this method are called even if the specific
	 * button action fails to execute.
	 * 
	 * @param action
	 *            the action to execute
	 */
	public void doBefore(Action action) {
		before.add(action);
	}

	/**
	 * Adds a new action to be done when any mapped button is pressed. That
	 * actions will be executed after the specific button's action.
	 * 
	 * The actions registered with this method are called even if the specific
	 * button action fails to execute.
	 * 
	 * @param action
	 *            the action to execute
	 */
	public void doAfter(Action action) {
		after.add(action);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		for (Action action : before) {
			action.doAction();
		}
		try {
			map.get(arg0.getSource()).doAction();
		} catch (StopException e) {
			e.getCause().printStackTrace();
		}
		for (Action act : after) {
			act.doAction();
		}
	}

	/**
	 * Exception to encapsulate checked exceptions when implementing method
	 * {@link Action#doAction()}.
	 * 
	 * @author Marcos A. Vasconcelos Junior
	 */
	@SuppressWarnings("serial")
	public class StopException extends RuntimeException {
		/**
		 * The real cause of this exception.
		 */
		public Exception cause;

		/**
		 * Creates a new unchecked exception that encapsulates the given
		 * exception.
		 * 
		 * @param cause
		 *            the real cause
		 */
		public StopException(Exception cause) {
			this.cause = cause;
		}

		@Override
		public Exception getCause() {
			return cause;
		}
	}
}
