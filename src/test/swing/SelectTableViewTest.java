package test.swing;

import java.util.ArrayList;
import java.util.List;

import sandbox.NewSelectTable;
import sandbox.NewSelectTableBuilder;
import test.model.Person;

import com.towel.el.annotation.AnnotationResolver;
import com.towel.swing.table.ObjectTableModel;

public class SelectTableViewTest {
	public static void main(String[] args) {
		ObjectTableModel<Person> model = new ObjectTableModel<Person>(
				new AnnotationResolver(Person.class), "name,age,live");

		model.setEditableDefault(true);

		List<Person> list = new ArrayList<Person>();

		list.add(new Person("A", 10, true));
		list.add(new Person("B", 20, true));
		list.add(new Person("C", 30, false));
		list.add(new Person("D", 40, true));
		list.add(new Person("E", 50, true));

		NewSelectTable<Person> st = new NewSelectTableBuilder<Person>()
				.buildWithModel(model).withData(list).create();
		st.showSelectTable();
	}
}
