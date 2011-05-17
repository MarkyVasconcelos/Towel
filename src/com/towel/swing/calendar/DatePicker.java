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
 * Componente que monta um calendario
 */
public class DatePicker extends JPanel {
	private CalendarView calendar;
	private BorderLabel lblMesAnte = new BorderLabel("<",
			BorderFactory.createEtchedBorder());
	private BorderLabel lblMesProx = new BorderLabel(">",
			BorderFactory.createEtchedBorder());
	private BorderLabel lblMesAno = new BorderLabel("",
			BorderFactory.createEtchedBorder());
	private JLabel lblDiasSemana = new JLabel(
			"  D    S    T    Q    Q     S    S ");
	private BorderLabel lblDias[] = new BorderLabel[42];
	private JButton btoHoje = new JButton("Today");
	private String data = "__/__/____";
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

		lblMesAnte.setFont(fontHeader);
		lblMesAnte.setBounds(0, 0, 15, 20);
		lblMesAnte.setHorizontalAlignment(SwingConstants.CENTER);
		lblMesAnte.setBackground(Color.lightGray);
		lblMesAnte.setOpaque(true);
		lblMesAnte.addMouseListener(listener);
		add(lblMesAnte);

		lblMesProx.setFont(fontHeader);
		lblMesProx.setBounds(125, 0, 15, 20);
		lblMesProx.setHorizontalAlignment(SwingConstants.CENTER);
		lblMesProx.setBackground(Color.lightGray);
		lblMesProx.setOpaque(true);
		lblMesProx.addMouseListener(listener);
		add(lblMesProx);

		lblMesAno.setFont(fontHeader);
		lblMesAno.setBounds(15, 0, 110, 20);
		lblMesAno.setHorizontalAlignment(SwingConstants.CENTER);
		lblMesAno.setBackground(Color.lightGray);
		lblMesAno.setOpaque(true);
		lblMesAno.addMouseListener(listener);
		lblMesAno.setText(nomeMesHoje);
		add(lblMesAno);

		lblDiasSemana.setFont(fontHeader);
		lblDiasSemana.setBounds(0, 20, 139, 15);
		lblDiasSemana.setHorizontalAlignment(SwingConstants.CENTER);
		lblDiasSemana.setBackground(new Color(63, 124, 124));
		lblDiasSemana.setOpaque(true);
		lblDiasSemana.addMouseListener(listener);
		add(lblDiasSemana);

		int x = 0;
		int y = 35;
		int col = 0;
		for (int i = 0; i < 42; i++) {
			lblDias[i] = new BorderLabel("", BorderFactory.createEtchedBorder());
			lblDias[i].setBounds(x, y, 20, 15);
			lblDias[i].setHorizontalAlignment(SwingConstants.CENTER);
			lblDias[i].setFont(fontCells);
			lblDias[i].setOpaque(false);
			lblDias[i].addMouseListener(listener);
			add(lblDias[i]);
			col++;
			if (col == 7) {
				col = 0;
				y += 15;
				x = 0;
			} else {
				x += 20;
			}
		}
		btoHoje.setBounds(0, 125, 140, 20);
		btoHoje.setMnemonic('H');
		btoHoje.setOpaque(true);
		btoHoje.addMouseListener(listener);
		add(btoHoje);
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
				lblDias[j].setText("");
			else {
				lblDias[j].setText(String.valueOf(actualMonth));
				if (diaHoje == actualMonth)
					i = j;

				actualMonth++;
			}
		}
		return i;
	}

	private void getMonthName(int month) {
		nomeMesHoje = monthNames[month - 1] + anoHoje;
		lblMesAno.setText(nomeMesHoje);
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

		data = "";
		if (diaSele < 10) {
			data += "0" + diaSelecionado + "/";
		} else {
			data += diaSelecionado + "/";
		}
		if (mesHoje < 10) {
			data += "0" + mesHoje + "/";
		} else {
			data += mesHoje + "/";
		}
		data += anoHoje;
		return data;
	}

	/**
	 * Method wich select the day (Highlight it)
	 */
	public void setSelectedDay(int x) {
		lblDias[x].setBackground(new Color(72, 164, 255));
		lblDias[x].setForeground(Color.white);
		corDiaSelecionado = x;
	}

	private class MouseListener extends MouseAdapter {
		/**
		 * Tratamento do click do mouse
		 */
		public void mouseClicked(MouseEvent e) {
			// Pressionou um dia
			for (int i = 1; i < 42; i++) {
				if (e.getSource() == lblDias[i]) {
					if (lblDias[i].getText() != "") {
						setSelectedDay(i);
						data = "";
						data += lblDias[i].getText() + "/" + mesHoje + "/"
								+ anoHoje;
						diaSelecionado = String.valueOf(lblDias[i].getText());
						calendar.removeCalendario(getData());
					}
				}
			}

			// Pressionou bot�o Hoje
			if (e.getSource() == btoHoje) {
				diaSelecionado = String.valueOf(getToday());
				mesHoje = getCurrentMonth();
				anoHoje = getCurrentYear();

				setSelectedDay(diaHoje + 1);
				calendar.removeCalendario(getData());
			}

			// Pressionou bot�o Pr�ximo Mes
			if (e.getSource() == lblMesProx) {
				lblDias[corDiaSelecionado].setBackground(Color.lightGray);
				lblDias[corDiaSelecionado].setForeground(Color.black);

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
			if (e.getSource() == lblMesAnte) {
				lblDias[corDiaSelecionado].setBackground(Color.lightGray);
				lblDias[corDiaSelecionado].setForeground(Color.black);

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

	class BorderLabel extends JLabel {
		public BorderLabel(String text, Border b) {
			super(text);
			setBorder(b);
			setHorizontalAlignment(SwingConstants.CENTER);
		}
	}
}
