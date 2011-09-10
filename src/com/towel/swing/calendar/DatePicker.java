/*
 * @(#)DatePicker.java
 * 
 * Copyright 2011 Marcos Vasconcelos
 * 
 * Towel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Towel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.towel.swing.calendar;

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
import javax.swing.Timer;
import javax.swing.UIManager;

import com.towel.cfg.TowelConfig;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

/**
 * <code>DatePicker<code> is a component which allows you to select a date
 * 
 * @author Fabio Rener
 * @author Marcos Vasconcelos
 * @modified Eric Yuzo
 */
public class DatePicker extends JPanel {
	private static final String TODAY_TXT_ATTR = "today_txt";

	private CalendarView calendar;

	private Calendar selectedDate;

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

	private JLabel[] weekDayLabels;
	private JLabel[] dayLabels;

	private JButton todayButton;

	private Locale locale;
	private DateFormat dateFormat;

	private String[] monthNames;
	private String[] weekDayNames;

	// Background colors
	private Color headerBackground;
	private Color weekDaysBackground;
	private Color dayPickerBackground;
	private Color selectedDayBackground;
	// Foreground colors
	private Color headerForeground;
	private Color weekDaysForeground;
	private Color dayPickerForeground;
	private Color selectedDayForeground;

	/**
	 * Constructs a new <code>DatePicker</code> associated to default locale.
	 */
	public DatePicker() {
		this(null, null);
	}
	
	/**
	 * Creates a new <code>DatePicker</code> associated to default locale
	 * and using specified date format pattern to format selected date.
	 * 
	 * @param pattern
	 *            the pattern describing the date format
	 */
	public DatePicker(String pattern) {
		this(null, new SimpleDateFormat(pattern));
	}

	/**
	 * Constructs a new <code>DatePicker</code> associated to the given locale
	 * and using given <code>dateFormat</code>.
	 * 
	 * @param locale
	 *            the locale associated to this <code>DatePicker</code>
	 * @param dateFormat
	 *            the <code>DateFormat</code> used to format selected date
	 */
	public DatePicker(Locale locale, DateFormat dateFormat) {
		if (locale == null) {
			locale = Locale.getDefault();
		}
		this.locale = locale;

		if (dateFormat == null) {
			dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
		}
		dateFormat.setLenient(false);
		this.dateFormat = dateFormat;

		selectedDate = getToday();

		init();
		refresh();
	}

	/**
	 * Constructs a new <code>DatePicker</code>.
	 * 
	 * @param cal
	 *            this <code>DatePicker</code>'s owner
	 * @param day
	 *            selected day. This value must be greater than zero to be set
	 * @param month
	 *            selected month. This value is only set if day is greater than
	 *            zero
	 * @param year
	 *            selected year. This value is only set if day is greater than
	 *            zero
	 */
	public DatePicker(CalendarView cal, int day, int month, int year) {
		calendar = cal;
		locale = TowelConfig.getInstance().getDefaultLocale();

		selectedDate = getToday();
		if (day > 0) {
			selectedDate.set(Calendar.DAY_OF_MONTH, day);
			selectedDate.set(Calendar.MONTH, month - 1);
			selectedDate.set(Calendar.YEAR, year);
		}

		init();
		refresh();
	}
	
	private void updateButtonTxt(Locale locale) {
		InputStream is = getClass().getResourceAsStream(
				"/res/strings_" + locale.toString() + ".properties");
		Properties props = new Properties();
		try {
			props.load(is);
			is.close();
			todayButton.setText(props.getProperty(TODAY_TXT_ATTR));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		headerBackground = Color.LIGHT_GRAY;
		weekDaysBackground = new Color(63, 124, 124);
		dayPickerBackground = UIManager.getColor("Label.background");
		selectedDayBackground = dayPickerBackground.darker();

		headerForeground = Color.BLACK;
		weekDaysForeground = Color.WHITE;
		dayPickerForeground = UIManager.getColor("Label.foreground");
		selectedDayForeground = UIManager.getColor("Label.foreground");

		setLayout(new BorderLayout());
		add(getMonthPanel(), BorderLayout.NORTH);
		add(getDaysPanel(), BorderLayout.CENTER);
		add(getTodayButton(), BorderLayout.SOUTH);

		updateWeekDays(locale);
		updateButtonTxt(locale);
	}

	private JPanel getMonthPanel() {
		if (monthPanel == null) {
			monthPanel = new JPanel(new GridBagLayout());

			previousMonthLabel = createLabelWithBorder("<");
			previousMonthLabel.setBackground(headerBackground);
			previousMonthLabel.setForeground(headerForeground);
			previousMonthLabel.addMouseListener(new NavigationListener() {
				@Override
				public void execute() {
					int oldMonth = selectedDate.get(Calendar.MONTH);
					selectedDate.add(Calendar.MONTH, -1);
					firePropertyChange("month", oldMonth, oldMonth - 1);
					if (oldMonth == 0) {
						int year = selectedDate.get(Calendar.YEAR);
						firePropertyChange("year", year + 1, year);
					}
					refresh();
				}
			});
			monthPanel.add(previousMonthLabel);

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1.0;
			monthLabel = createLabelWithBorder("");
			monthLabel.setBackground(headerBackground);
			monthLabel.setForeground(headerForeground);
			monthPanel.add(monthLabel, gbc);

			nextMonthLabel = createLabelWithBorder(">");
			nextMonthLabel.setBackground(headerBackground);
			nextMonthLabel.setForeground(headerForeground);
			nextMonthLabel.addMouseListener(new NavigationListener() {
				@Override
				public void execute() {
					int oldMonth = selectedDate.get(Calendar.MONTH);
					selectedDate.add(Calendar.MONTH, 1);
					firePropertyChange("month", oldMonth, oldMonth + 1);
					if (oldMonth == 11) {
						int year = selectedDate.get(Calendar.YEAR);
						firePropertyChange("year", year - 1, year);
					}
					refresh();
				}
			});
			monthPanel.add(nextMonthLabel);

			previousYearLabel = createLabelWithBorder("<");
			previousYearLabel.setBackground(headerBackground);
			previousYearLabel.setForeground(headerForeground);
			previousYearLabel.addMouseListener(new NavigationListener() {
				@Override
				public void execute() {
					int oldYear = selectedDate.get(Calendar.YEAR);
					selectedDate.add(Calendar.YEAR, -1);
					firePropertyChange("year", oldYear, oldYear - 1);
					refresh();
				}
			});
			monthPanel.add(previousYearLabel);

			yearLabel = createLabelWithBorder("");
			yearLabel.setBackground(headerBackground);
			yearLabel.setForeground(headerForeground);
			monthPanel.add(yearLabel);

			nextYearLabel = createLabelWithBorder(">");
			nextYearLabel.setBackground(headerBackground);
			nextYearLabel.setForeground(headerForeground);
			nextYearLabel.addMouseListener(new NavigationListener() {
				@Override
				public void execute() {
					int oldYear = selectedDate.get(Calendar.YEAR);
					selectedDate.add(Calendar.YEAR, 1);
					firePropertyChange("year", oldYear, oldYear + 1);
					refresh();
				}
			});
			monthPanel.add(nextYearLabel);
		}
		return monthPanel;
	}

	private JPanel getDaysPanel() {
		if (daysPanel == null) {
			daysPanel = new JPanel(new GridLayout(7, 7));

			weekDayLabels = new JLabel[7];
			for (int i = 0; i < 7; i++) {
				weekDayLabels[i] = new JLabel();
				weekDayLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
				weekDayLabels[i].setBackground(weekDaysBackground);
				weekDayLabels[i].setForeground(weekDaysForeground);
				weekDayLabels[i].setOpaque(true);
				daysPanel.add(weekDayLabels[i]);
			}

			dayLabels = new JLabel[42];
			for (int i = 0; i < 42; i++) {
				dayLabels[i] = createLabelWithBorder("");
				dayLabels[i].setBackground(dayPickerBackground);
				dayLabels[i].setForeground(dayPickerForeground);
				dayLabels[i].addMouseListener(new DaySelectionListener());
				daysPanel.add(dayLabels[i]);
			}
		}
		return daysPanel;
	}

	private JButton getTodayButton() {
		if (todayButton == null) {
			todayButton = new JButton("Today");
			todayButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					selectedDate = getToday();
					setSelectedDay(selectedDate.get(Calendar.DAY_OF_MONTH));
					firePropertyChange("day", 0,
							selectedDate.get(Calendar.DAY_OF_MONTH));
					if (calendar != null) {
						calendar.dateSelected(getDate());
					}
				}
			});
		}
		return todayButton;
	}

	private void updateWeekDays(Locale locale) {
		DateFormatSymbols symbols = new DateFormatSymbols(locale);
		weekDayNames = symbols.getShortWeekdays();
		monthNames = symbols.getMonths();
		for (int i = 0; i < 7; i++) {
			weekDayLabels[i].setText(weekDayNames[i + 1]);
		}
	}

	private String getMonthName(int month) {
		return monthNames[month];
	}

	private void refresh() {
		String monthName = getMonthName(selectedDate.get(Calendar.MONTH));
		monthLabel.setText(monthName);
		String currentYear = String.valueOf(selectedDate.get(Calendar.YEAR));
		yearLabel.setText(currentYear);
		populateCells();
		setSelectedDay(selectedDate.get(Calendar.DAY_OF_MONTH));
	}

	private void populateCells() {
		Calendar cal = getSelectedDate();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int weekDay = cal.get(Calendar.DAY_OF_WEEK);
		int monthDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		int day = 1;

		for (int i = 0; i < 42; i++) {
			if ((i < weekDay - 1) || (i > (monthDay + weekDay - 2))) {
				dayLabels[i].setText("");
			} else {
				dayLabels[i].setText(String.valueOf(day));
				day++;
			}
			dayLabels[i].setBackground(dayPickerBackground);
			dayLabels[i].setForeground(dayPickerForeground);
		}
	}

	private Calendar getToday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	/**
	 * Returns a string representing the selected date.
	 * 
	 * @return a string representing the selected date
	 */
	public String getDate() {
		return dateFormat.format(getSelectedDate().getTime());
	}

	/**
	 * Returns a copy of selected <code>Calendar</code>.
	 * 
	 * @return a copy of selected <code>Calendar</code>
	 */
	public Calendar getSelectedDate() {
		return (Calendar) selectedDate.clone();
	}

	/**
	 * Sets the selected <code>Calendar</code>.
	 * 
	 * @param calendar
	 *            the new selected <code>Calendar</code>
	 */
	public void setSelectedDate(Calendar calendar) {
		if (calendar != null) {
			Calendar oldDate = getSelectedDate();
			this.selectedDate = getToday();
			this.selectedDate.set(calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			firePropertyChange("date", oldDate, getSelectedDate());
			refresh();
		}
	}

	/**
	 * Changes the selected day.
	 * <p>
	 * <strong>Warning:</strong> It's not recommended to use this method. Prefer
	 * to use {@link #setSelectedDate(Calendar)}.
	 * 
	 * @param newDay
	 *            the day to select
	 */
	public void setSelectedDay(int newDay) {
		String day;
		for (int i = 0; i < 42; i++) {
			day = dayLabels[i].getText();
			if (day.equals(Integer.toString(newDay))) {
				selectedDate.set(Calendar.DAY_OF_MONTH, newDay);
				dayLabels[i].setBackground(selectedDayBackground);
				dayLabels[i].setForeground(selectedDayForeground);
				break;
			}
		}
	}

	/**
	 * Returns the locale associated to this <code>DatePicker</code>. The locale
	 * is used to get appropriate week day names and month names.
	 * 
	 * @return the locale associated to this <code>DatePicker</code>
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Changes the locale associated to this <code>DatePicker</code>. The locale
	 * is used to get appropriate week day names and month names.
	 * 
	 * @param locale
	 *            the new locale
	 */
	public void setLocale(Locale locale) {
		if (locale != null) {
			this.locale = locale;
			updateWeekDays(locale);
		}
	}

	/**
	 * Returns the <code>dateFormat</code> used to format selected date.
	 * 
	 * @return the <code>dateFormat</code> used to format selected date
	 */
	public DateFormat getDateFormat() {
		return dateFormat;
	}

	/**
	 * Changes the <code>dateFormat</code> used to format selected date.
	 * 
	 * @param locale
	 *            the new <code>dateFormat</code>
	 */
	public void setDateFormat(DateFormat dateFormat) {
		if (dateFormat != null) {
			this.dateFormat = dateFormat;
		}
	}
	
	/**
	 * Changes the date format's pattern.
	 * 
	 * @param pattern
	 *            the pattern describing the date format
	 */
	public void setPattern(String pattern) {
		setDateFormat(new SimpleDateFormat(pattern));
	}

	/**
	 * Returns the background color for header labels used to select month and
	 * year.
	 * 
	 * @return the background color for header labels
	 */
	public Color getHeaderBackground() {
		return headerBackground;
	}

	/**
	 * Sets the background color for header labels used to select month and
	 * year.
	 * 
	 * @param headerBg
	 *            the background color for header labels
	 */
	public void setHeaderBackground(Color headerBg) {
		if (headerBg != null) {
			this.headerBackground = headerBg;
			previousMonthLabel.setBackground(headerBg);
			nextMonthLabel.setBackground(headerBg);
			monthLabel.setBackground(headerBg);
			previousYearLabel.setBackground(headerBg);
			nextYearLabel.setBackground(headerBg);
			yearLabel.setBackground(headerBg);
		}
	}

	/**
	 * Returns the background color for week day labels.
	 * 
	 * @return the background color for week day labels
	 */
	public Color getWeekDaysBackground() {
		return weekDaysBackground;
	}

	/**
	 * Sets the background color for week day labels.
	 * 
	 * @param weekDaysBg
	 *            the background color for week day labels
	 */
	public void setWeekDaysBackground(Color weekDaysBg) {
		if (weekDaysBg != null) {
			this.weekDaysBackground = weekDaysBg;
			for (int i = 0; i < 7; i++) {
				weekDayLabels[i].setBackground(weekDaysBg);
			}
		}
	}

	/**
	 * Returns the background color for day picker labels.
	 * 
	 * @return the background color for day picker labels
	 */
	public Color getDayPickerBackground() {
		return dayPickerBackground;
	}

	/**
	 * Sets the background color for day picker labels.
	 * 
	 * @param dayPickerBg
	 *            the background color for day picker labels
	 */
	public void setDayPickerBackground(Color dayPickerBg) {
		if (dayPickerBg != null) {
			this.dayPickerBackground = dayPickerBg;
			refresh();
		}
	}

	/**
	 * Returns the background color for selected day label.
	 * 
	 * @return the background color for selected day label
	 */
	public Color getSelectedDayBackground() {
		return selectedDayBackground;
	}

	/**
	 * Sets the background color for selected day label.
	 * 
	 * @param selectedDayBg
	 *            the background color for selected day label
	 */
	public void setSelectedDayBackground(Color selectedDayBg) {
		if (selectedDayBg != null) {
			this.selectedDayBackground = selectedDayBg;
			refresh();
		}
	}

	/**
	 * Returns the foreground color for header labels used to select month and
	 * year.
	 * 
	 * @return the foreground color for header labels
	 */
	public Color getHeaderForeground() {
		return headerForeground;
	}

	/**
	 * Sets the foreground color for header labels used to select month and
	 * year.
	 * 
	 * @param headerFg
	 *            the foreground color for header labels
	 */
	public void setHeaderForeground(Color headerFg) {
		if (headerFg != null) {
			this.headerForeground = headerFg;
			previousMonthLabel.setForeground(headerFg);
			nextMonthLabel.setForeground(headerFg);
			monthLabel.setForeground(headerFg);
			previousYearLabel.setForeground(headerFg);
			nextYearLabel.setForeground(headerFg);
			yearLabel.setForeground(headerFg);
		}
	}

	/**
	 * Returns the foreground color for week day labels.
	 * 
	 * @return the foreground color for week day labels
	 */
	public Color getWeekDaysForeground() {
		return weekDaysForeground;
	}

	/**
	 * Sets the foreground color for week day labels.
	 * 
	 * @param weekDaysFg
	 *            the foreground color for week day labels
	 */
	public void setWeekDaysForeground(Color weekDaysFg) {
		if (weekDaysFg != null) {
			this.weekDaysForeground = weekDaysFg;
			for (int i = 0; i < 7; i++) {
				weekDayLabels[i].setForeground(weekDaysFg);
			}
		}
	}

	/**
	 * Returns the foreground color for day picker labels.
	 * 
	 * @return the foreground color for day picker labels
	 */
	public Color getDayPickerForeground() {
		return dayPickerForeground;
	}

	/**
	 * Sets the foreground color for day picker labels.
	 * 
	 * @param dayPickerFg
	 *            the foreground color for day picker labels
	 */
	public void setDayPickerForeground(Color dayPickerFg) {
		if (dayPickerFg != null) {
			this.dayPickerForeground = dayPickerFg;
			refresh();
		}
	}

	/**
	 * Returns the foreground color for selected day label.
	 * 
	 * @return the foreground color for selected day label
	 */
	public Color getSelectedDayForeground() {
		return selectedDayForeground;
	}

	/**
	 * Sets the foreground color for selected day label.
	 * 
	 * @param selectedDayFg
	 *            the foreground color for selected day label
	 */
	public void setSelectedDayForeground(Color selectedDayFg) {
		if (selectedDayFg != null) {
			this.selectedDayForeground = selectedDayFg;
			refresh();
		}
	}

	/**
	 * Sets <code>todayButton</code>'s text.
	 * 
	 * @param todayString
	 *            the text to be displayed in <code>todayButton</code>.
	 */
	public void setTodayString(String todayString) {
		getTodayButton().setText(todayString);
	}

	/**
	 * Makes <code>todayButton</code> visible or invisible.
	 * 
	 * @param visible
	 *            true to make <code>todayButton</code> visible; false,
	 *            otherwise
	 */
	public void setTodayButtonVisible(boolean visible) {
		getTodayButton().setVisible(visible);
	}

	private JLabel createLabelWithBorder(String text) {
		JLabel label = new JLabel(text);
		label.setBorder(BorderFactory.createEtchedBorder());
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setOpaque(true);
		return label;
	}

	// Listener for day selection labels
	private class DaySelectionListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			String day = ((JLabel) e.getSource()).getText();
			if (day.length() > 0) {
				setSelectedDay(Integer.parseInt(day));
				firePropertyChange("day", 0,
						selectedDate.get(Calendar.DAY_OF_MONTH));
				if (calendar != null) {
					calendar.dateSelected(getDate());
				}
			}
			refresh();
		}

	}

	// Listener for navigation labels.
	private class NavigationListener extends MouseAdapter {

		// Timer used to auto repeat execute() method whenever
		// the user holds one of the navigation labels.
		Timer timer;

		public NavigationListener() {
			timer = new Timer(100, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					execute();
				}
			});
			timer.setInitialDelay(500);
		}

		public void execute() {
			// This method must be implemented by subclasses
		}

		@Override
		public void mousePressed(MouseEvent e) {
			execute();
			timer.start();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			timer.stop();
		}

	}

}