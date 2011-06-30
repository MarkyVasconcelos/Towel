package test.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import test.model.Person;

import com.towel.collections.aggr.FuncConcat;
import com.towel.collections.aggr.FuncSum;
import com.towel.el.annotation.AnnotationResolver;
import com.towel.swing.table.JTableView;
import com.towel.swing.table.ObjectTableModel;

public class ObjectTableModelTest {
	public static void main(String[] args) {
		ObjectTableModel<Person> model = new ObjectTableModel<Person>(
				new AnnotationResolver(Person.class), "name,age,live");

		model.setEditableDefault(true);

		model.add(new Person("A", 10, true));
		model.add(new Person("B", 20, true));
		model.add(new Person("C", 30, false));
		model.add(new Person("D", 40, true));
		model.add(new Person("E", 50, true));
		
		JTable table = new JTable(model);
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
