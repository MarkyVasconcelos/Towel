package test.awt;

import java.lang.reflect.Field;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.towel.awt.ann.Action;
import com.towel.awt.ann.ActionManager;
import com.towel.awt.ann.ActionSequence;
import com.towel.reflec.ClassIntrospector;
import com.towel.reflec.ClassIntrospector.AnnotatedElement;

public class ActionManagerTest {
	private String lastCall;
	@ActionSequence({ @Action(method = "first"), @Action(method = "second") })
	private JButton button;

	public ActionManagerTest() {
		JPanel panel = new JPanel();
		button = new JButton("Any");
		panel.add(button);

		List<AnnotatedElement<Field, ActionSequence>> sequences = new ClassIntrospector(
				this.getClass())
				.getAnnotatedDeclaredFields(ActionSequence.class);
		for (AnnotatedElement<Field, ActionSequence> ann : sequences) {
			for (Action action : ann.getAnnotation().value())
				System.out.println(action.method());
		}

		System.out.println(lastCall);
	}

	private void first() {
		lastCall = "first";
	}

	private void second() {
		lastCall = "second";
	}

	public static void main(String[] args) {
		new ActionManagerTest();
	}
}
