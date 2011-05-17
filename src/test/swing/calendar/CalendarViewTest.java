package test.swing.calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.towel.swing.calendar.CalendarView;

public class CalendarViewTest {
	public CalendarViewTest() {
		JFrame frame = new JFrame("CalendarView");
		JPanel content=  new JPanel();
		CalendarView view = new CalendarView();
		content.add(view);
		
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
