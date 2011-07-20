package sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Locale;
import java.text.DateFormatSymbols;

/**
 * DatePicker is a Component with displays a grid of days to be selected.
 */
public class DatePicker extends JPanel {
	
	private CalendarView calendar;

	private JPanel monthPanel;
	private JPanel daysPanel;
	
	// Month navigation labels
	private JLabel previousMonthLabel;
	private JLabel nextMonthLabel;
	private JLabel monthLabel;
	
	// Year navigation labels
	private JLabel previousYearLabel;
	private JLabel nextYearLabel;
	private JLabel yearLabel;

	private JLabel weekDayLabels[] = new JLabel[7];
	private JLabel dayLabels[] = new JLabel[42];
	private JButton todayButton;

	private Calendar actualSelection;
	private String selectedDay;
	private int selectedDayIndex;
	private MouseListener listener;

	private String[] monthNames;
	private String[] weekDayNames;

	/**
	 * Construtor
	 * 
	 * @param cal
	 *            - CalendarView from where this is opening from
	 * @param day
	 *            Current day, use zero to show current day
	 * @param month
	 *            Current month (Can be zero if day is zero two)
	 * @param year
	 *            Current year (Can be zero if day is zero two)
	 */
	public DatePicker(CalendarView cal, int day, int month, int year) {
		calendar = cal;
		listener = new MouseListener();
		
		DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
		weekDayNames = symbols.getShortWeekdays();
		monthNames = symbols.getMonths();

		selectedDay = "0";

		actualSelection = Calendar.getInstance();
		if (day == 0) {
			actualSelection.set(Calendar.DAY_OF_MONTH, getToday());
			actualSelection.set(Calendar.MONTH, getCurrentMonth());
			actualSelection.set(Calendar.YEAR, getCurrentYear());
		} else {
			actualSelection.set(Calendar.DAY_OF_MONTH, day);
			actualSelection.set(Calendar.MONTH, month);
			actualSelection.set(Calendar.YEAR, year);
		}

		init();

		String monthName = getMonthName(actualSelection.get(Calendar.MONTH));
		monthLabel.setText(monthName);
		String currentYear = String.valueOf(actualSelection.get(Calendar.YEAR));
		yearLabel.setText(currentYear);

		setSelectedDay(populateCells());
	}

	private void init() {
		setLayout(new BorderLayout());
		add(getMonthPanel(), BorderLayout.NORTH);		
		add(getDaysPanel(), BorderLayout.CENTER);
		add(getTodayButton(), BorderLayout.SOUTH);
	}
	
	private JPanel getMonthPanel() {
		if (monthPanel == null) {
			monthPanel = new JPanel(new GridBagLayout());
			
			previousMonthLabel = createLabelWithBorder("<");
			previousMonthLabel.setBackground(Color.LIGHT_GRAY);
			previousMonthLabel.setOpaque(true);
			previousMonthLabel.addMouseListener(listener);
			monthPanel.add(previousMonthLabel);

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1.0;
			monthLabel = createLabelWithBorder("");
			monthLabel.setBackground(Color.LIGHT_GRAY);
			monthLabel.setOpaque(true);
			monthLabel.addMouseListener(listener);
			monthPanel.add(monthLabel, gbc);

			nextMonthLabel = createLabelWithBorder(">");
			nextMonthLabel.setBackground(Color.LIGHT_GRAY);
			nextMonthLabel.setOpaque(true);
			nextMonthLabel.addMouseListener(listener);
			monthPanel.add(nextMonthLabel);
			
			previousYearLabel = createLabelWithBorder("<");
			previousYearLabel.setBackground(Color.LIGHT_GRAY);
			previousYearLabel.setOpaque(true);
			previousYearLabel.addMouseListener(listener);
			monthPanel.add(previousYearLabel);

			yearLabel = createLabelWithBorder("");
			yearLabel.setBackground(Color.LIGHT_GRAY);
			yearLabel.setOpaque(true);
			yearLabel.addMouseListener(listener);
			monthPanel.add(yearLabel);

			nextYearLabel = createLabelWithBorder(">");
			nextYearLabel.setBackground(Color.LIGHT_GRAY);
			nextYearLabel.setOpaque(true);
			nextYearLabel.addMouseListener(listener);
			monthPanel.add(nextYearLabel);
		}
		return monthPanel;
	}
	
	private JPanel getDaysPanel() {
		if (daysPanel == null) {
			daysPanel = new JPanel(new GridLayout(7, 7));
			
			for (int i = 0; i < 7; i++) {
				weekDayLabels[i] = new JLabel(weekDayNames[i + 1]);
				weekDayLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
				weekDayLabels[i].setBackground(new Color(63, 124, 124));
				weekDayLabels[i].setForeground(Color.WHITE);
				weekDayLabels[i].setOpaque(true);
				weekDayLabels[i].addMouseListener(listener);
				daysPanel.add(weekDayLabels[i]);
			}

			for (int i = 0; i < 42; i++) {
				dayLabels[i] = createLabelWithBorder("");
				dayLabels[i].setOpaque(true);
				dayLabels[i].addMouseListener(listener);
				daysPanel.add(dayLabels[i]);
			}
		}
		return daysPanel;
	}
	
	private JButton getTodayButton() {
		if (todayButton == null) {
			todayButton = new JButton("Today");
			todayButton.setOpaque(true);
			todayButton.addMouseListener(listener);
		}
		return todayButton;
	}

	private int getToday() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	private int getCurrentMonth() {
		return Calendar.getInstance().get(Calendar.MONTH);
	}

	private int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	private int populateCells() {
		Calendar now = Calendar.getInstance();
		now.clear(Calendar.DATE);
		now.set(actualSelection.get(Calendar.YEAR),
				actualSelection.get(Calendar.MONTH), 1);
		int weekDay = now.get(Calendar.DAY_OF_WEEK);
		int monthDay = now.getActualMaximum(Calendar.DAY_OF_MONTH);
		int i = 0;

		int day = 1;

		for (int j = 0; j < 42; j++) {
			if ((j < weekDay - 1) || (j > (monthDay + weekDay - 2)))
				dayLabels[j].setText("");
			else {
				dayLabels[j].setText(String.valueOf(day));
				if (actualSelection.get(Calendar.DAY_OF_MONTH) == day)
					i = j;

				day++;
			}
		}
		return i;
	}

	private String getMonthName(int month) {
		return monthNames[month];
	}

	/**
	 * Retorna a data Selecionada
	 * 
	 * @return String - Data
	 */
	public String getDate() {

		StringBuilder result = new StringBuilder();

		int day = Integer.parseInt(selectedDay);

		if (day == 0) {
			selectedDay = String.valueOf(actualSelection
					.get(Calendar.DAY_OF_MONTH));
			day = Integer.parseInt(selectedDay);
		}

		if (day < 10)
			result.append("0");
		result.append(selectedDay).append("/");

		if (actualSelection.get(Calendar.MONTH) < 11)
			result.append("0");

		result.append(actualSelection.get(Calendar.MONTH) + 1).append("/")
				.append(actualSelection.get(Calendar.YEAR));
		return result.toString();
	}

	/**
	 * Method wich select the day (Highlight it)
	 */
	public void setSelectedDay(int x) {
		dayLabels[x].setBackground(Color.LIGHT_GRAY);
		selectedDayIndex = x;
	}

	private class MouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			for (int i = 1; i < 42; i++) {
				if (e.getSource() == dayLabels[i]) {
					if (dayLabels[i].getText() != "") {
						setSelectedDay(i);
						selectedDay = String.valueOf(dayLabels[i].getText());
						calendar.dateSelected(getDate());
					}
				}
			}

			if (e.getSource() == todayButton) {
				selectedDay = String.valueOf(getToday());
				actualSelection.set(Calendar.MONTH, getCurrentMonth());
				actualSelection.set(Calendar.YEAR, getCurrentYear());

				setSelectedDay(actualSelection.get(Calendar.DAY_OF_MONTH) + 1);
				calendar.dateSelected(getDate());
				return;
			}

			// If it's not 'today' pressed, then it is month 'next' or
			// 'previous'

			dayLabels[selectedDayIndex].setBackground(Color.LIGHT_GRAY);

			if (e.getSource() == nextMonthLabel)
				actualSelection.add(Calendar.MONTH, 1);

			if (e.getSource() == previousMonthLabel)
				actualSelection.add(Calendar.MONTH, -1);
			
			if (e.getSource() == nextYearLabel)
				actualSelection.add(Calendar.YEAR, 1);

			if (e.getSource() == previousYearLabel)
				actualSelection.add(Calendar.YEAR, -1);

			String monthName = getMonthName(actualSelection.get(Calendar.MONTH));
			monthLabel.setText(monthName);
			String currentYear = String.valueOf(actualSelection.get(Calendar.YEAR));
			yearLabel.setText(currentYear);
			populateCells();
		}
	}
	
	public void setTodayString(String todayString) {
		getTodayButton().setText(todayString);
	}
	
	public void setTodayButtonVisible(boolean visible) {
		getTodayButton().setVisible(visible);
	}

	private JLabel createLabelWithBorder(String text) {
		JLabel label = new JLabel(text);
		label.setBorder(BorderFactory.createEtchedBorder());
		label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}
	
}
