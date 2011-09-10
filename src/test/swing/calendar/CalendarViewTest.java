package test.swing.calendar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.towel.swing.calendar.CalendarView;

public class CalendarViewTest {
	public CalendarViewTest() {
		JFrame frame = new JFrame("CalendarView");
		JPanel content = new JPanel();
		final CalendarView view = new CalendarView();
		JButton button = new JButton("X");
		content.add(view);
		content.add(button);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(view.getSelectedDate());
			}
		});

		frame.setContentPane(content);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new CalendarViewTest();
	}
}
