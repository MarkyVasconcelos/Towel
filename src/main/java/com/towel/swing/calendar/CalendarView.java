/*
 * @(#)CalendarView.java
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
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.Icon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import com.towel.awt.ann.Action;
import com.towel.awt.ann.ActionManager;

/**
 * <code>CalendarView</code> is a component which has a text field
 * associated to a date picker. 
 * 
 * @see com.towel.swing.calendar.DatePicker
 * 
 * @author Fabio Rener
 * @author Marcos Vasconcelos
 * @modified Eric Yuzo
 */
public class CalendarView extends JPanel {

	private DatePicker datePicker;
	private JTextField editor;
	@Action(method = "openPopup")
	private JButton button;

	private JPopupMenu popup;

	private String lastValidString;

	/**
	 * Creates a new <code>CalendarView</code> associated to default locale
	 */
	public CalendarView() {
		this(null, null);
	}

	/**
	 * Creates a new <code>CalendarView</code> associated to default locale
	 * and using specified date format pattern to format selected date.
	 * 
	 * @param pattern
	 *            the pattern describing the date format
	 */
	public CalendarView(String pattern) {
		this(null, new SimpleDateFormat(pattern));
	}

	/**
	 * Creates a new <code>CalendarView</code> associated to given locale
	 * and using specified date format to format selected date.
	 * 
	 * @param locale
	 *            the locale associated to the date picker
	 * @param dateFormat
	 *            the date format used to format selected date
	 */
	public CalendarView(Locale locale, DateFormat format) {
		datePicker = new DatePicker(locale, format);
		datePicker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String prop = evt.getPropertyName();
				if ("day".equals(prop) || "date".equals(prop)) {
					dateSelected(datePicker.getDate());
				}
			}
		});

		popup = new JPopupMenu();
		popup.add(datePicker);

		lastValidString = "";

		init();

		new ActionManager(this);
	}

	private void init() {
		setLayout(new BorderLayout());

		add(getEditor(), BorderLayout.CENTER);
		add(getButton(), BorderLayout.EAST);
	}

	private JTextField getEditor() {
		if (editor == null) {
			editor = new JTextField(10);
			editor.setInputVerifier(new DateInputVerifier());
		}
		return editor;
	}

	private JButton getButton() {
		if (button == null) {
			button = new JButton(". .");
			button.setMargin(new Insets(0, 5, 0, 5));
		}
		return button;
	}

	/**
	 * Sets the editor's text.
	 * 
	 * @param text
	 * 			the editor's new text
	 */
	public void setText(String text) {
		getEditor().setText(text);
		commitEdit();
	}

	/**
	 * Returns the editor's text.
	 * 
	 * @return the editor's text
	 */
	public String getText() {
		return getEditor().getText();
	}

	/**
     * Sets the button's icon.
     * 
     * @param icon
     * 			the button's new icon.
     */ 
	public void setIcon(Icon icon) {
		getButton().setIcon(icon);

		if (icon == null) {
			getButton().setText(". .");
			return;
		}
		getButton().setText("");
	}

	/**
	 * Returns a copy of selected <code>Calendar</code> or <code>null</code> if
	 * the text is empty.
	 * 
	 * @return a copy of selected <code>Calendar</code> or <code>null</code> if
	 *         the text is empty
	 */
	public Calendar getSelectedDate() {
		Calendar calendar = null;
		if (getText().length() > 0) {
			calendar = datePicker.getSelectedDate();
		}
		return calendar;
	}

	/**
	 * Sets the selected <code>Calendar</code>.
	 * 
	 * @param calendar
	 *            the new selected <code>Calendar</code>
	 */
	public void setSelectedDate(Calendar calendar) {
		datePicker.setSelectedDate(calendar);
	}

	/**
	 * Returns the locale associated to the date picker. The locale
	 * is used to get appropriate week day names and month names.
	 * 
	 * @return the locale associated to the date picker
	 */
	public Locale getLocale() {
		return datePicker.getLocale();
	}

	/**
	 * Changes the locale associated to the date picker. The locale
	 * is used to get appropriate week day names and month names.
	 * 
	 * @param locale
	 *            the new locale
	 */
	public void setLocale(Locale locale) {
		datePicker.setLocale(locale);
	}

	/**
	 * Returns the date format used to format selected date.
	 * 
	 * @return the date format used to format selected date
	 */
	public DateFormat getDateFormat() {
		return datePicker.getDateFormat();
	}

	/**
	 * Changes the date format used to format selected date.
	 * 
	 * @param locale
	 *            the new date format
	 */
	public void setDateFormat(DateFormat dateFormat) {
		datePicker.setDateFormat(dateFormat);
		if (lastValidString.length() > 0) {
			lastValidString = dateFormat.format(getSelectedDate().getTime());
		}
		commitEdit();
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
	 * Returns the background color for date picker's header labels
	 * used to select month and year.
	 * 
	 * @return the background color for date picker's header labels
	 */
	public Color getHeaderBackground() {
		return datePicker.getHeaderBackground();
	}

	/**
	 * Sets the background color for date picker's header labels
	 * used to select month and year.
	 * 
	 * @param headerBg
	 *            the background color for date picker's header labels
	 */
	public void setHeaderBackground(Color headerBackground) {
		datePicker.setHeaderBackground(headerBackground);
	}

	/**
	 * Returns the background color for date picker's week day labels.
	 * 
	 * @return the background color for date picker's week day labels
	 */
	public Color getWeekDaysBackground() {
		return datePicker.getWeekDaysBackground();
	}

	/**
	 * Sets the background color for date picker's week day labels.
	 * 
	 * @param weekDaysBg
	 *            the background color for date picker's week day labels
	 */
	public void setWeekDaysBackground(Color weekDaysBackground) {
		datePicker.setWeekDaysBackground(weekDaysBackground);
	}

	/**
	 * Returns the background color for date picker's day picker labels.
	 * 
	 * @return the background color for date picker's day picker labels
	 */
	public Color getDayPickerBackground() {
		return datePicker.getDayPickerBackground();
	}

	/**
	 * Sets the background color for date picker's day picker labels.
	 * 
	 * @param dayPickerBg
	 *            the background color for date picker's day picker labels
	 */
	public void setDayPickerBackground(Color dayPickerBackground) {
		datePicker.setDayPickerBackground(dayPickerBackground);
	}

	/**
	 * Returns the background color for date picker's selected day label.
	 * 
	 * @return the background color for date picker's selected day label
	 */
	public Color getSelectedDayBackground() {
		return datePicker.getSelectedDayBackground();
	}

	/**
	 * Sets the background color for date picker's selected day label.
	 * 
	 * @param selectedDayBg
	 *            the background color for date picker's selected day label
	 */
	public void setSelectedDayBackground(Color selectedDayBackground) {
		datePicker.setSelectedDayBackground(selectedDayBackground);
	}

	/**
	 * Returns the foreground color for date picker's header labels
	 * used to select month and year.
	 * 
	 * @return the foreground color for date picker's header labels
	 */
	public Color getHeaderForeground() {
		return datePicker.getHeaderForeground();
	}

	/**
	 * Sets the foreground color for date picker's header labels
	 * used to select month and year.
	 * 
	 * @param headerFg
	 *            the foreground color for date picker's header labels
	 */
	public void setHeaderForeground(Color headerForeground) {
		datePicker.setHeaderForeground(headerForeground);
	}

	/**
	 * Returns the foreground color for date picker's week day labels.
	 * 
	 * @return the foreground color for date picker's week day labels
	 */
	public Color getWeekDaysForeground() {
		return datePicker.getWeekDaysForeground();
	}

	/**
	 * Sets the foreground color for date picker's week day labels.
	 * 
	 * @param weekDaysFg
	 *            the foreground color for date picker's week day labels
	 */
	public void setWeekDaysForeground(Color weekDaysForeground) {
		datePicker.setWeekDaysForeground(weekDaysForeground);
	}

	/**
	 * Returns the foreground color for date picker's day picker labels.
	 * 
	 * @return the foreground color for date picker's day picker labels
	 */
	public Color getDayPickerForeground() {
		return datePicker.getDayPickerForeground();
	}

	/**
	 * Sets the foreground color for date picker's day picker labels.
	 * 
	 * @param dayPickerFg
	 *            the foreground color for date picker's day picker labels
	 */
	public void setDayPickerForeground(Color dayPickerForeground) {
		datePicker.setDayPickerForeground(dayPickerForeground);
	}

	/**
	 * Returns the foreground color for date picker's selected day label.
	 * 
	 * @return the foreground color for date picker's selected day label
	 */
	public Color getSelectedDayForeground() {
		return datePicker.getSelectedDayForeground();
	}

	/**
	 * Sets the foreground color for date picker's selected day label.
	 * 
	 * @param selectedDayFg
	 *            the foreground color for date picker's selected day label
	 */
	public void setSelectedDayForeground(Color selectedDayForeground) {
		datePicker.setSelectedDayForeground(selectedDayForeground);
	}

	/**
	 * Sets date picker's today button's text.
	 * 
	 * @param todayString
	 *            the text to be displayed in today button.
	 */
	public void setTodayString(String todayString) {
		datePicker.setTodayString(todayString);
	}

	/**
	 * Makes date picker's today button visible or invisible.
	 * 
	 * @param visible
	 *            true to make <today button visible; false, otherwise
	 */
	public void setTodayButtonVisible(boolean visible) {
		datePicker.setTodayButtonVisible(visible);
	}

	/**
	 * This is the action when button was pressed.
	 * 
	 * @see com.towel.awt.ann.Action
	 * @see com.towel.awt.ann.ActionManager
	 */
	@SuppressWarnings("unused")
	private void openPopup() {
		popup.show(button, button.getWidth()
				- datePicker.getPreferredSize().width, button.getHeight());

	} // ActionPerformed

	/**
	 * Sets the editor's text and makes the date picker invisible.
	 * 
	 * @param strDate
	 *            the editor's new text
	 */
	public void dateSelected(String strDate) {
		getEditor().setText(strDate);
		commitEdit();
		popup.setVisible(false);
	}

	@Override
	public void setEnabled(boolean enabled) {
		getEditor().setEnabled(enabled);
		getButton().setEnabled(enabled);
		super.setEnabled(enabled);
	}

	// commits editor's text if it's a valid string or brings back
	// the last valid string and sets the datePicker's selected date
	private void commitEdit() {
		String strDate = getText();
		if (strDate.isEmpty() || isValidDate(strDate)) {
			lastValidString = strDate;
		} else {
			getEditor().setText(lastValidString);
			strDate = lastValidString;
		}

		if (strDate.length() > 0) {
			try {
				Calendar cal = Calendar.getInstance();
				cal.setTime(getDateFormat().parse(strDate));
				datePicker.setSelectedDate(cal);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	// returns true if given string represents a valid date
	private boolean isValidDate(String strDate) {
		DateFormat format = getDateFormat();
		try {
			format.parse(strDate);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	// An input verifier to validate editor's text
	private class DateInputVerifier extends InputVerifier {

		// Commits editor's text and always returns true
		@Override
		public boolean shouldYieldFocus(JComponent input) {
			commitEdit();
			return true;
		}

		// Not used
		@Override
		public boolean verify(JComponent input) {
			String strDate = ((JTextField) input).getText();
			return strDate.isEmpty() || isValidDate(strDate);
		}

	}

}