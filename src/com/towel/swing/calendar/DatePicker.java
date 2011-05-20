package com.towel.swing.calendar;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DatePicker is a Component with displays a grid of days to be selected.
 */
public class DatePicker extends JPanel {
	private CalendarView calendar;

	private JLabel previousMonth;
	private JLabel nextMonth;
	private JLabel monthLabel;

	private JLabel weekDaysLabel;
	private JLabel dayLabels[] = new JLabel[42];
	private JButton today = new JButton("Today");
	private int actualDay;
	private int actualMonth;
	private int actualYear;
	private String selectedDay;
	private int selectedDayIndex;
	private MouseListener listener;

	public static String[] monthNames;
	public static String[] weekDays;

	static {
		monthNames = new String[12];

		monthNames[0] = "Janeiro/";
		monthNames[1] = "Fevereiro/";
		monthNames[2] = "Março/";
		monthNames[3] = "Abril/";
		monthNames[4] = "Maio/";
		monthNames[5] = "Junho/";
		monthNames[6] = "Julho/";
		monthNames[7] = "Agosto/";
		monthNames[8] = "Setembro/";
		monthNames[9] = "Outubro/";
		monthNames[10] = "Novembro/";
		monthNames[11] = "Dezembro/";

		weekDays = new String[7];
		weekDays[0] = "D";
		weekDays[1] = "S";
		weekDays[2] = "T";
		weekDays[3] = "Q";
		weekDays[4] = "Q";
		weekDays[5] = "S";
		weekDays[6] = "S";
	}

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
	public DatePicker(CalendarView cal, int dia, int mes, int ano) {
		calendar = cal;
		listener = new MouseListener();

		previousMonth = createLabelWithBorder("<");
		nextMonth = createLabelWithBorder(">");
		monthLabel = createLabelWithBorder("");

		selectedDay = "0";

		if (dia == 0) {
			actualDay = getToday();
			actualMonth = getCurrentMonth();
			actualYear = getCurrentYear();
		} else {
			actualDay = dia;
			actualMonth = mes;
			actualYear = ano;
		}

		init();

		String monthName = getMonthName(actualMonth);
		monthLabel.setText(monthName);

		setSelectedDay(populateCells());
		this.setBounds(0, 0, 140, 145);
	}

	private void init() {
		setLayout(null);

		Font fontHeader = new Font("SansSerif", Font.BOLD, 11);
		Font fontCells = new Font("SansSerif", Font.PLAIN, 11);

		previousMonth.setFont(fontHeader);
		previousMonth.setBounds(0, 0, 15, 20);
		previousMonth.setHorizontalAlignment(SwingConstants.CENTER);
		previousMonth.setBackground(Color.lightGray);
		previousMonth.setOpaque(true);
		previousMonth.addMouseListener(listener);
		add(previousMonth);

		nextMonth.setFont(fontHeader);
		nextMonth.setBounds(125, 0, 15, 20);
		nextMonth.setHorizontalAlignment(SwingConstants.CENTER);
		nextMonth.setBackground(Color.lightGray);
		nextMonth.setOpaque(true);
		nextMonth.addMouseListener(listener);
		add(nextMonth);

		monthLabel.setFont(fontHeader);
		monthLabel.setBounds(15, 0, 110, 20);
		monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
		monthLabel.setBackground(Color.lightGray);
		monthLabel.setOpaque(true);
		monthLabel.addMouseListener(listener);
		add(monthLabel);

		StringBuilder weekDays = new StringBuilder();
		for (String s : DatePicker.weekDays)
			weekDays.append(s).append("   ");
		weekDays.delete(weekDays.length() - 3, weekDays.length());

		weekDaysLabel = new JLabel(weekDays.toString());
		weekDaysLabel.setFont(fontHeader);
		weekDaysLabel.setBounds(0, 20, 139, 15);
		weekDaysLabel.setHorizontalAlignment(SwingConstants.CENTER);
		weekDaysLabel.setBackground(new Color(63, 124, 124));
		weekDaysLabel.setOpaque(true);
		weekDaysLabel.addMouseListener(listener);
		add(weekDaysLabel);

		int x = 0;
		int y = 35;
		int col = 0;
		for (int i = 0; i < 42; i++) {
			dayLabels[i] = createLabelWithBorder("");
			dayLabels[i].setBounds(x, y, 20, 15);
			dayLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
			dayLabels[i].setFont(fontCells);
			dayLabels[i].setOpaque(false);
			dayLabels[i].addMouseListener(listener);
			add(dayLabels[i]);
			col++;
			if (col == 7) {
				col = 0;
				y += 15;
				x = 0;
			} else {
				x += 20;
			}
		}
		today.setBounds(0, 125, 140, 20);
		today.setMnemonic('H');
		today.setOpaque(true);
		today.addMouseListener(listener);
		add(today);
	}

	private int getToday() {
		return Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
	}

	private int getCurrentMonth() {
		return Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
	}

	private int getCurrentYear() {
		return Integer
				.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
	}

	private int populateCells() {
		Calendar now = Calendar.getInstance();
		now.clear(Calendar.DATE);
		now.set(actualYear, actualMonth - 1, 1);
		int weekDay = now.get(Calendar.DAY_OF_WEEK);
		int monthDay = now.getActualMaximum(Calendar.DAY_OF_MONTH);
		int i = 0;

		int day = 1;

		for (int j = 0; j < 42; j++) {
			if ((j < weekDay - 1) || (j > (monthDay + weekDay - 2)))
				dayLabels[j].setText("");
			else {
				dayLabels[j].setText(String.valueOf(day));
				if (actualDay == day)
					i = j;

				day++;
			}
		}
		return i;
	}

	private String getMonthName(int month) {
		return monthNames[month - 1] + actualYear;
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
			selectedDay = String.valueOf(actualDay);
			day = Integer.parseInt(selectedDay);
		}

		if (day < 10)
			result.append("0");
		result.append(selectedDay).append("/");

		if (actualMonth < 10)
			result.append("0");

		result.append(actualMonth).append("/").append(actualYear);
		return result.toString();
	}

	/**
	 * Method wich select the day (Highlight it)
	 */
	public void setSelectedDay(int x) {
		dayLabels[x].setBackground(new Color(72, 164, 255));
		dayLabels[x].setForeground(Color.white);
		selectedDayIndex = x;
	}

	private class MouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			for (int i = 1; i < 42; i++) {
				if (e.getSource() == dayLabels[i]) {
					if (dayLabels[i].getText() != "") {
						setSelectedDay(i);
						// dateString = "";
						// dateString += dayLabels[i].getText() + "/"
						// + actualMonth + "/" + actualYear;
						selectedDay = String.valueOf(dayLabels[i].getText());
						calendar.dateSelected(getDate());
					}
				}
			}

			if (e.getSource() == today) {
				selectedDay = String.valueOf(getToday());
				actualMonth = getCurrentMonth();
				actualYear = getCurrentYear();

				setSelectedDay(actualDay + 1);
				calendar.dateSelected(getDate());
				return;
			}

			// If it's not 'today' pressed, then it is month 'next' or
			// 'previous'

			dayLabels[selectedDayIndex].setBackground(Color.lightGray);
			dayLabels[selectedDayIndex].setForeground(Color.black);

			if (e.getSource() == nextMonth) {
				actualMonth++;

				if (actualMonth > 12) {
					actualMonth = 1;
					actualYear++;
				}
			}

			if (e.getSource() == previousMonth) {
				actualMonth--;
				if (actualMonth < 1) {
					actualMonth = 12;
					actualYear--;
				}
			}
			String monthName = getMonthName(actualMonth);
			monthLabel.setText(monthName);
			populateCells();
		}
	}

	private JLabel createLabelWithBorder(String text) {
		JLabel label = new JLabel(text);
		label.setBorder(BorderFactory.createEtchedBorder());
		label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}
}
