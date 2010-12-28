package com.towel.awt.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// @formatter:off
/**
 * Indicates the {@link Action}s that must be called when the annotated object (an
 * instance of AbstractButton) has been pressed.
 * <p>
 * For example, assume the following code:
 * 
 * <pre>
 * &#064;ActionSequence({&#064;Action(method = &quot;openSession&quot;), &#064;Action(listener = AddListener.class), &#064;Action(method = &quot;closeSession&quot;)})
 * private JButton add;
 * </pre>
 * 
 * In this case, the following happens:
 * 
 * <ol>
 * 		<li>The openSession method is called</li>
 * 		<li>The actionPerformed of the AddListener class is called</li>
 * 		<li>The closeSession method is called.</li>
 * </ol>
 * 
 * @author Marcos A. Vasconcelos Junior
 * @see <a href="https://github.com/MarkyVasconcelos/Towel/wiki/ActionManager">ActionManager wiki</a>
 */
// @formatter:on
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ActionSequence {
	/**
	 * The array of <code>Action</code> that must be executed, in order.
	 */
	Action[] value();
}
