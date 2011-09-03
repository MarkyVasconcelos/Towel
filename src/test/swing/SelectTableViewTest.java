package test.swing;

import java.util.List;

import sandbox.NewSelectTable;
import sandbox.AdvancedJTableBuilder;
import test.model.Person;
import test.model.PreData;

import com.towel.el.annotation.AnnotationResolver;
import com.towel.swing.table.ObjectTableModel;

public class SelectTableViewTest {
	public static void main(String[] args) {
		ObjectTableModel<Person> model = new ObjectTableModel<Person>(
				new AnnotationResolver(Person.class), "name,age,live");

		model.setEditableDefault(true);
		
		List<Person> list = PreData.getSampleList(50);
		
		NewSelectTable<Person> st = new AdvancedJTableBuilder<Person>()
				.buildWithModel(model).withData(list).create();
		st.showSelectTable();
	}
}
