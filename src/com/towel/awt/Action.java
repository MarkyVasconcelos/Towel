package com.towel.awt;

/**
 * Defines what the {@link ActionManager} must to do when the associated
 * AbstractButton has been pressed.
 * 
 * @author Marcos A. Vasconcelos Junior
 * @deprecated Replaced by annotation {@link com.towel.awt.ann.Action}.
 */
@Deprecated
public interface Action {
	/**
	 * Code to execute when the associated button has been pressed.
	 */
	public void doAction();
}
