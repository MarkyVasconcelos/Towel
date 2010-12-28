package com.towel.awt.ann;

import java.awt.event.ActionListener;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// @formatter:off
/**
 * Indicates that the given method must be called when the annotated object (an
 * instance of AbstractButton) has been pressed.
 * <p>
 * For example, the following code will map the button <code>ok</code> to
 * method <code>validateInput()</code>:
 * 
 * <pre>
 * &#064;Action(method = &quot;validateInput&quot;)
 * private JButton ok;
 * </pre>
 * 
 * <b>Note:</b> only one of the parameters must be set. If you set both, only
 * the <code>method</code> will be resolved.
 * 
 * @author Marcos A. Vasconcelos Junior
 * @see <a href="https://github.com/MarkyVasconcelos/Towel/wiki/ActionManager">ActionManager wiki</a>
 */
// @formatter:on
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Action {
	// FIXME the listener must works with not inner classes
	/**
	 * When the button is pressed, the manager creates an instance of this class
	 * and calls its <code>actionPerformed</code> method.
	 */
	Class<? extends ActionListener> listener() default ActionListener.class;

	/**
	 * When pressed, the button will call the method with this name. The method
	 * should not have parameters and can have any visibility modifier.
	 */
	String method() default "";
}
