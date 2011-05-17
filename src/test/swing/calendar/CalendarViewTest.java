package test.swing.calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.towel.swing.calendar.CalendarView;

public class CalendarViewTest {
	public CalendarViewTest() {
		JFrame frame = new JFrame("CalendarView");
		JPanel content=  new JPanel();
		content.setLayout(null);
		
		CalendarView view = new CalendarView(50, 50, 150, 150, new JPanel(), true);
		
		content.add(view);
		
		frame.setContentPane(content);
		frame.setSize(400,200);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new CalendarViewTest();
	}
}
