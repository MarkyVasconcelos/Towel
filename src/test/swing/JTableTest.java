package test.swing;

import test.model.Person;
import test.model.PreData;

import com.towel.collections.paginator.ListPaginator;
import com.towel.el.annotation.AnnotationResolver;
import com.towel.swing.table.ObjectTableModel;
import com.towel.swing.table.SelectTable;
import com.towel.swing.table.TableFilter;

public class JTableTest {
	public static void main(String[] args) {
		ObjectTableModel<Person> model = new ObjectTableModel<Person>(
				new AnnotationResolver(Person.class), "name,age,live");

		model.setEditableDefault(true);

		model.add(new Person("A", 10, true));
		model.add(new Person("B", 20, true));
		model.add(new Person("C", 30, false));
		model.add(new Person("D", 40, true));
		model.add(new Person("E", 50, true));

		SelectTable<Person> sel = new SelectTable<Person>(
				new ObjectTableModel<Person>(Person.class, "name,age,live"),
				new ListPaginator<Person>(PreData.getSampleList()));

		new TableFilter(sel.getTable().getTableHeader(), sel.getModel());

		sel.showSelectTable();
	}
}
