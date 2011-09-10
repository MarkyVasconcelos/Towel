package test.swing;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import test.model.Person;

import com.towel.awt.ann.Action;
import com.towel.awt.ann.ActionManager;
import com.towel.bean.DynamicFormatter;
import com.towel.el.FieldResolver;
import com.towel.swing.combo.ObjectComboBoxModel;

public class ComboBoxDynamicFormatterTest extends JFrame {
	private ObjectComboBoxModel<Person> model;
	@Action(method = "showPerson")
	private JButton button;

	public ComboBoxDynamicFormatterTest() {
		super("ComboBoxModel");
		model = new ObjectComboBoxModel<Person>();

		DynamicFormatter<Person> formatter = new DynamicFormatter<Person>(
				Person.class, " - ");
		formatter.addField(new FieldResolver(Person.class, "name"));
		formatter.addField(new FieldResolver(Person.class, "age"));

		model.setFormatter(formatter);
		// Adicionado as classes Person no model
		model.add(new Person("A", 10.0));
		model.add(new Person("B", 20.0));
		model.add(new Person("C", 30.0));
		model.add(new Person("D", 40.0));
		model.add(new Person("E", 50.0));
		
		JComboBox combo = new JComboBox(model);
		JPanel cont = new JPanel();
		cont.add(combo);
		cont.add(button = new JButton("Show"));
		setContentPane(cont);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		
		new ActionManager(this);// Necessary to map @Action to the method
	}

	private void showPerson() {
		Person p = model.getSelectedObject();
		System.out.println(p.toString());
	}

	public static void main(String[] args) {
		new ComboBoxDynamicFormatterTest();
	}
}
