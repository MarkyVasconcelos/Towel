package com.towel.swing.calendar;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.Border;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Componente que monta um calendario
 */
public class DatePicker extends JPanel implements MouseListener {
	CalendarView calendario;

	Calendar rightNow = Calendar.getInstance();

	BorderLabel lblMesAnte = new BorderLabel(" <",
			BorderFactory.createEtchedBorder());
	BorderLabel lblMesProx = new BorderLabel(" >",
			BorderFactory.createEtchedBorder());
	BorderLabel lblMesAno = new BorderLabel("",
			BorderFactory.createEtchedBorder());

	JLabel lblDiasSemana = new JLabel("  D    S    T    Q    Q     S    S ");
	BorderLabel lblDias[] = new BorderLabel[42];
	JButton btoHoje = new JButton("Hoje");

	String data = "__/__/____";
	int diaHoje;
	int mesHoje;
	int anoHoje;
	String nomeMesHoje;
	String diaSemana;
	Date dataTeste;
	String diaSelecionado;
	int corDiaSelecionado;
	String guardaUltimaData;

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
	public DatePicker(CalendarView cal, int x, int y, int dia, int mes, int ano) {
		calendario = cal;

		diaSelecionado = "0";

		if (dia == 0) {
			diaHoje = getDiaHoje();
			mesHoje = getMesHoje();
			anoHoje = getAnoHoje();
		} else {
			diaHoje = dia;
			mesHoje = mes;
			anoHoje = ano;
		}
		// Verifica o mes para pegar o nome do mes.
		getNomeMes(mesHoje);

		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		marcaDiaSelecionado(montaMes(anoHoje, mesHoje));
		this.setBounds(((x - 140) / 2), ((y - 145) / 2), 140, 145);
	}

	void jbInit() throws Exception {
		setLayout(null);

		Font F = new Font("SansSerif", Font.BOLD, 11);
		Font f = new Font("SansSerif", Font.PLAIN, 11);

		lblMesAnte.setFont(F);
		lblMesAnte.setBounds(0, 0, 15, 20);
		lblMesAnte.setHorizontalAlignment(SwingConstants.CENTER);
		lblMesAnte.setBackground(Color.lightGray);
		lblMesAnte.setOpaque(true);
		lblMesAnte.addMouseListener(this);
		add(lblMesAnte);

		lblMesProx.setFont(F);
		lblMesProx.setBounds(125, 0, 15, 20);
		lblMesProx.setHorizontalAlignment(SwingConstants.CENTER);
		lblMesProx.setBackground(Color.lightGray);
		lblMesProx.setOpaque(true);
		lblMesProx.addMouseListener(this);
		add(lblMesProx);

		lblMesAno.setFont(F);
		lblMesAno.setBounds(15, 0, 110, 20);
		lblMesAno.setHorizontalAlignment(SwingConstants.CENTER);
		lblMesAno.setBackground(Color.lightGray);
		lblMesAno.setOpaque(true);
		lblMesAno.addMouseListener(this);
		lblMesAno.setText(nomeMesHoje);
		add(lblMesAno);

		lblDiasSemana.setFont(F);
		lblDiasSemana.setBounds(0, 20, 139, 15);
		lblDiasSemana.setHorizontalAlignment(SwingConstants.CENTER);
		lblDiasSemana.setBackground(new Color(63, 124, 124));
		lblDiasSemana.setOpaque(true);
		lblDiasSemana.addMouseListener(this);
		add(lblDiasSemana);

		int x = 0;
		int y = 35;
		int col = 0;
		for (int i = 0; i < 42; i++) {
			lblDias[i] = new BorderLabel("", BorderFactory.createEtchedBorder());
			lblDias[i].setBounds(x, y, 20, 15);
			lblDias[i].setHorizontalAlignment(SwingConstants.CENTER);
			lblDias[i].setFont(f);
			lblDias[i].setOpaque(false);
			lblDias[i].addMouseListener(this);
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
		btoHoje.addMouseListener(this);
		add(btoHoje);
	}

	/**
	 * Tratamento do click do mouse
	 */
	public void mouseClicked(MouseEvent e) {
		// Pressionou um dia
		for (int i = 1; i < 42; i++) {
			if (e.getSource() == lblDias[i]) {
				if (lblDias[i].getText() != "") {
					marcaDiaSelecionado(i);
					data = "";
					data += lblDias[i].getText() + "/" + mesHoje + "/"
							+ anoHoje;
					diaSelecionado = String.valueOf(lblDias[i].getText());
					calendario.removeCalendario(getData());
				}
			}
		}

		// Pressionou bot�o Hoje
		if (e.getSource() == btoHoje) {
			diaSelecionado = String.valueOf(getDiaHoje());
			mesHoje = getMesHoje();
			anoHoje = getAnoHoje();

			marcaDiaSelecionado(diaHoje + 1);
			calendario.removeCalendario(getData());
		}

		// Pressionou bot�o Pr�ximo Mes
		if (e.getSource() == lblMesProx) {
			lblDias[corDiaSelecionado].setBackground(Color.lightGray);
			lblDias[corDiaSelecionado].setForeground(Color.black);

			mesHoje++;
			if (mesHoje < 1) {
				mesHoje = 12;
				anoHoje--;
			}

			if (mesHoje > 12) {
				mesHoje = 1;
				anoHoje++;
			}
			getNomeMes(mesHoje);
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

			if (mesHoje > 12) {
				mesHoje = 1;
				anoHoje++;
			}
			getNomeMes(mesHoje);
			montaMes(anoHoje, mesHoje);
			setVisible(true);
		}
	}

	private int getDiaHoje() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd");
		int strDia = Integer.parseInt(formatter.format(new Date()));
		return strDia;
	}

	private int getMesHoje() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		int strMes = Integer.parseInt(formatter.format(new Date()));
		return strMes;
	}

	private int getAnoHoje() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		int strAno = Integer.parseInt(formatter.format(new Date()));
		return strAno;
	}

	private int montaMes(int ano, int mes) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.clear(rightNow.DATE); // so doesn't override
		rightNow.set(ano, --mes, 1);
		int intDiaSemana = rightNow.get(rightNow.DAY_OF_WEEK);
		int intDiasMes = rightNow.getActualMaximum(rightNow.DAY_OF_MONTH);
		int intIndice = 0;

		int DiaAtualMes;
		DiaAtualMes = 1;

		for (int j = 0; j < 42; j++) {
			if ((j < intDiaSemana - 1) || (j > (intDiasMes + intDiaSemana - 2))) {
				lblDias[j].setText("");
			} else {
				lblDias[j].setText(String.valueOf(DiaAtualMes));
				if (diaHoje == DiaAtualMes) {
					intIndice = j;
				}

				DiaAtualMes++;
			}
		}
		return intIndice;
	}

	private void getNomeMes(int Mes) {
		switch (Mes) {
		case 1:
			nomeMesHoje = "Janeiro/" + anoHoje;
			lblMesAno.setText(nomeMesHoje);
			break;
		case 2:
			nomeMesHoje = "Fevereiro/" + anoHoje;
			lblMesAno.setText(nomeMesHoje);
			break;
		case 3:
			nomeMesHoje = "Mar�o/" + anoHoje;
			lblMesAno.setText(nomeMesHoje);
			break;
		case 4:
			nomeMesHoje = "Abril/" + anoHoje;
			lblMesAno.setText(nomeMesHoje);
			break;
		case 5:
			nomeMesHoje = "Maio/" + anoHoje;
			lblMesAno.setText(nomeMesHoje);
			break;
		case 6:
			nomeMesHoje = "Junho/" + anoHoje;
			lblMesAno.setText(nomeMesHoje);
			break;
		case 7:
			nomeMesHoje = "Julho/" + anoHoje;
			lblMesAno.setText(nomeMesHoje);
			break;
		case 8:
			nomeMesHoje = "Agosto/" + anoHoje;
			lblMesAno.setText(nomeMesHoje);
			break;
		case 9:
			nomeMesHoje = "Setembro/" + anoHoje;
			lblMesAno.setText(nomeMesHoje);
			break;
		case 10:
			nomeMesHoje = "Outubro/" + anoHoje;
			lblMesAno.setText(nomeMesHoje);
			break;
		case 11:
			nomeMesHoje = "Novembro/" + anoHoje;
			lblMesAno.setText(nomeMesHoje);
			break;
		case 12:
			nomeMesHoje = "Dezembro/" + anoHoje;
			lblMesAno.setText(nomeMesHoje);
			break;
		}
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
	 * Metodo que marca no calend�rio o dia selecionado
	 */
	public void marcaDiaSelecionado(int x) {
		lblDias[x].setBackground(new Color(72, 164, 255));
		lblDias[x].setForeground(Color.white);
		corDiaSelecionado = x;
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
}

class BorderLabel extends JLabel {
	public BorderLabel(String text, Border b) {
		super(text);
		setBorder(b);
		setHorizontalAlignment(SwingConstants.CENTER);
	}
}
