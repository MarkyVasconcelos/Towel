package com.towel.swing.calendar;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.Border;

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

	private JLabel beforeMonth;
	private JLabel nextMonth;
	private JLabel yearLabel;

	private JLabel weekDaysLabel = new JLabel(
			"  D    S    T    Q    Q     S    S ");
	private JLabel dayLabels[] = new JLabel[42];
	private JButton today = new JButton("Today");
	private String dateString = "__/__/____";
	private int diaHoje;
	private int mesHoje;
	private int anoHoje;
	private String nomeMesHoje;
	private String diaSelecionado;
	private int corDiaSelecionado;
	private MouseListener listener;

	public static String[] monthNames;

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
	}

	/**
	 * Construtor
	 * 
	 * @param Calendario
	 *            - referencia da classe Calendario
	 * @param x
	 *            - largura do Frame
	 * @param y
	 *            - altura do Frame
	 * @param int - dia se o dia for = 0 (zero) � mostrado a data de hoje
	 * @param int - mes
	 * @param int - ano
	 */
	public DatePicker(CalendarView cal, int dia, int mes, int ano) {
		calendar = cal;
		listener = new MouseListener();

		beforeMonth = createLabelWithBorder("<");
		nextMonth = createLabelWithBorder(">");
		yearLabel = createLabelWithBorder("");

		diaSelecionado = "0";

		if (dia == 0) {
			diaHoje = getToday();
			mesHoje = getCurrentMonth();
			anoHoje = getCurrentYear();
		} else {
			diaHoje = dia;
			mesHoje = mes;
			anoHoje = ano;
		}
		// Verifica o mes para pegar o nome do mes.
		getMonthName(mesHoje);

		jbInit();

		setSelectedDay(montaMes(anoHoje, mesHoje));
		this.setBounds(0, 0, 140, 145);
	}

	void jbInit() {
		setLayout(null);

		Font fontHeader = new Font("SansSerif", Font.BOLD, 11);
		Font fontCells = new Font("SansSerif", Font.PLAIN, 11);

		beforeMonth.setFont(fontHeader);
		beforeMonth.setBounds(0, 0, 15, 20);
		beforeMonth.setHorizontalAlignment(SwingConstants.CENTER);
		beforeMonth.setBackground(Color.lightGray);
		beforeMonth.setOpaque(true);
		beforeMonth.addMouseListener(listener);
		add(beforeMonth);

		nextMonth.setFont(fontHeader);
		nextMonth.setBounds(125, 0, 15, 20);
		nextMonth.setHorizontalAlignment(SwingConstants.CENTER);
		nextMonth.setBackground(Color.lightGray);
		nextMonth.setOpaque(true);
		nextMonth.addMouseListener(listener);
		add(nextMonth);

		yearLabel.setFont(fontHeader);
		yearLabel.setBounds(15, 0, 110, 20);
		yearLabel.setHorizontalAlignment(SwingConstants.CENTER);
		yearLabel.setBackground(Color.lightGray);
		yearLabel.setOpaque(true);
		yearLabel.addMouseListener(listener);
		yearLabel.setText(nomeMesHoje);
		add(yearLabel);

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

	private int montaMes(int ano, int mes) {
		Calendar now = Calendar.getInstance();
		now.clear(Calendar.DATE);
		now.set(ano, --mes, 1);
		int weekDay = now.get(Calendar.DAY_OF_WEEK);
		int monthDay = now.getActualMaximum(Calendar.DAY_OF_MONTH);
		int i = 0;

		int actualMonth = 1;

		for (int j = 0; j < 42; j++) {
			if ((j < weekDay - 1) || (j > (monthDay + weekDay - 2)))
				dayLabels[j].setText("");
			else {
				dayLabels[j].setText(String.valueOf(actualMonth));
				if (diaHoje == actualMonth)
					i = j;

				actualMonth++;
			}
		}
		return i;
	}

	private void getMonthName(int month) {
		nomeMesHoje = monthNames[month - 1] + anoHoje;
		yearLabel.setText(nomeMesHoje);
	}

	/**
	 * Retorna a data Selecionada
	 * 
	 * @return String - Data
	 */
	public String getData() {

		int diaSele = Integer.parseInt(diaSelecionado);

		if (diaSele == 0) {
			diaSelecionado = String.valueOf(diaHoje);
			diaSele = Integer.parseInt(diaSelecionado);
		}

		dateString = "";
		if (diaSele < 10) {
			dateString += "0" + diaSelecionado + "/";
		} else {
			dateString += diaSelecionado + "/";
		}
		if (mesHoje < 10) {
			dateString += "0" + mesHoje + "/";
		} else {
			dateString += mesHoje + "/";
		}
		dateString += anoHoje;
		return dateString;
	}

	/**
	 * Method wich select the day (Highlight it)
	 */
	public void setSelectedDay(int x) {
		dayLabels[x].setBackground(new Color(72, 164, 255));
		dayLabels[x].setForeground(Color.white);
		corDiaSelecionado = x;
	}

	private class MouseListener extends MouseAdapter {
		/**
		 * Tratamento do click do mouse
		 */
		public void mouseClicked(MouseEvent e) {
			// Pressionou um dia
			for (int i = 1; i < 42; i++) {
				if (e.getSource() == dayLabels[i]) {
					if (dayLabels[i].getText() != "") {
						setSelectedDay(i);
						dateString = "";
						dateString += dayLabels[i].getText() + "/" + mesHoje + "/"
								+ anoHoje;
						diaSelecionado = String.valueOf(dayLabels[i].getText());
						calendar.removeCalendario(getData());
					}
				}
			}

			// Pressionou bot�o Hoje
			if (e.getSource() == today) {
				diaSelecionado = String.valueOf(getToday());
				mesHoje = getCurrentMonth();
				anoHoje = getCurrentYear();

				setSelectedDay(diaHoje + 1);
				calendar.removeCalendario(getData());
			}

			// Pressionou bot�o Pr�ximo Mes
			if (e.getSource() == nextMonth) {
				dayLabels[corDiaSelecionado].setBackground(Color.lightGray);
				dayLabels[corDiaSelecionado].setForeground(Color.black);

				mesHoje++;

				if (mesHoje > 12) {
					mesHoje = 1;
					anoHoje++;
				}
				getMonthName(mesHoje);
				montaMes(anoHoje, mesHoje);
				setVisible(true);
			}

			// Pressionou bot�o Mes Anterior
			if (e.getSource() == beforeMonth) {
				dayLabels[corDiaSelecionado].setBackground(Color.lightGray);
				dayLabels[corDiaSelecionado].setForeground(Color.black);

				mesHoje--;
				if (mesHoje < 1) {
					mesHoje = 12;
					anoHoje--;
				}

				getMonthName(mesHoje);
				montaMes(anoHoje, mesHoje);
				setVisible(true);
			}
		}
	}

	private JLabel createLabelWithBorder(String text) {
		JLabel label = new JLabel(text);
		label.setBorder(BorderFactory.createEtchedBorder());
		label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}
}
