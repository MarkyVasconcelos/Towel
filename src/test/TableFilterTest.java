package test;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import test.model.Person;
import test.model.PreData;

import com.towel.el.annotation.AnnotationResolver;
import com.towel.swing.table.ObjectTableModel;
import com.towel.swing.table.TableFilter;

public class TableFilterTest {
	public static void main(String[] args) {
		ObjectTableModel<Person> model = new ObjectTableModel<Person>(
				new AnnotationResolver(Person.class), "name,age,live");

		model.setEditableDefault(true);

		JTable table = new JTable(model);

		new TableFilter(table);

		model.addAll(PreData.getSampleList());

		// JTableView view = new JTableView(model);
		// view.getFooterModel().setFunction(0, new FuncConcat("-"));
		// view.getFooterModel().setFunction(1, new FuncSum());

		JScrollPane pane = new JScrollPane();
		pane.setViewportView(table);

		JFrame frame = new JFrame();
		frame.getContentPane().add(pane);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
