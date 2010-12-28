package com.towel.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;

/**
 * Manager that maps an {@link ActionListener} to a button.
 * 
 * @author Marcos A. Vasconcelos Junior
 */
public class ActionListenerManager implements ActionListener {
	private List<ActionListener> before, after;
	private Map<AbstractButton, ActionListener> map;

	/**
	 * Creates a new <code>ActionListenerManager</code>.
	 */
	public ActionListenerManager() {
		map = new HashMap<AbstractButton, ActionListener>();
		before = new ArrayList<ActionListener>();
		after = new ArrayList<ActionListener>();
	}

	/**
	 * Associates a button with an <code>ActionListener</code>, that will have
	 * its {@link ActionListener#actionPerformed(ActionEvent) actionPerformed}
	 * method called when the button has been pressed.
	 * 
	 * @param source
	 *            the button
	 * @param action
	 *            the listener that will be called
	 */
	public void manage(AbstractButton source, ActionListener action) {
		map.put(source, action);
		source.addActionListener(this);
	}

	/**
	 * Adds a new <code>ActionListener</code> that will be called when any
	 * mapped button is pressed. That <code>ActionListener</code>s will be
	 * called before the specific button's action.
	 * 
	 * The <code>ActionListener</code>s registered with this method are called
	 * even if the specific button <code>ActionListener</code> fails to execute.
	 * 
	 * @param action
	 *            the <code>ActionListener</code> that will be called
	 */
	public void doBefore(ActionListener action) {
		before.add(action);
	}

	/**
	 * Adds a new <code>ActionListener</code> that will be called when any
	 * mapped button is pressed. That <code>ActionListener</code>s will be
	 * called after the specific button's action.
	 * 
	 * The <code>ActionListener</code>s registered with this method are called
	 * even if the specific button <code>ActionListener</code> fails to execute.
	 * 
	 * @param action
	 *            the <code>ActionListener</code> that will be called
	 */
	public void doAfter(ActionListener action) {
		after.add(action);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		for (ActionListener action : before) {
			action.actionPerformed(arg0);
		}
		try {
			map.get(arg0.getSource()).actionPerformed(arg0);
		} catch (StopException e) {
			e.getCause().printStackTrace();
		}
		for (ActionListener act : after) {
			act.actionPerformed(arg0);
		}
	}

	/**
	 * Exception to encapsulate checked exceptions when implementing method
	 * {@link Action#doAction()}.
	 * 
	 * @author Marcos A. Vasconcelos Junior
	 */
	@SuppressWarnings("serial")
	public static class StopException extends RuntimeException {
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
