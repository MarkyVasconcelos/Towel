package test.swing;

import java.awt.Font;
import java.util.List;
import java.util.Locale;

import test.model.Person;
import test.model.PreData;

import com.towel.cfg.TowelConfig;
import com.towel.collections.paginator.ListPaginator;
import com.towel.el.annotation.AnnotationResolver;
import com.towel.swing.event.ObjectSelectListener;
import com.towel.swing.event.SelectEvent;
import com.towel.swing.table.ObjectTableModel;
import com.towel.swing.table.SelectTable;

public class SelectTableViewTest {
	public static void main(String[] args) {
		ObjectTableModel<Person> model = new ObjectTableModel<Person>(
				new AnnotationResolver(Person.class), "name:Extreme long name that will not fit the column header the last one fited this one wont,age:Age,live:Live");
		model.setEditableDefault(true);

		List<Person> list = PreData.getSampleList(100);

		TowelConfig.getInstance().setLocale(new Locale("pt","BR"));
		
		SelectTable<Person> st = new SelectTable<Person>(model,
				new ListPaginator<Person>(list, 20));
		st.setSelectionType(SelectTable.SINGLE);
//		st.setSize(400, 600);
		st.setFont(new Font("Arial", Font.ITALIC, 12));
		st.useTableFilter();
		st.fitColumnsToHeader();
		st.showSelectTable();

		st.addObjectSelectListener(new ObjectSelectListener() {
			@Override
			public void notifyObjectSelected(SelectEvent selectevent) {
				Person p = (Person) selectevent.getObject();
				System.out.println(p.getName());
			}
		});
	}
}
