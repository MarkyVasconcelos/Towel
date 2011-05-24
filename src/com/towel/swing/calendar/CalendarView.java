package com.towel.swing.calendar;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;

import com.towel.awt.ann.Action;
import com.towel.awt.ann.ActionManager;
import com.towel.time.DateUtils;

/**
 * A JComponent with a JTextField for dates and also has a DatePicker popup.
 * 
 * 
 * No metodo posicionaFoco(int i) (obrigatorio implementa-lo) o case para a data
 * sera sempre 99 case 99: cl.setFocus();
 * 
 * @author Fabio Rener
 * @modified Marcos Vasconcelos
 */
public class CalendarView extends JPanel {
	private DatePicker cal;
	@Action(method = "openPopup")
	private JButton button;
	private JTextField txt;
	private int cont = 0;

	private JWindow glassPane;

	/**
	 * 
	 * @param xCal
	 *            - largura Frame
	 * @paal - altura Applet Frame
	 * @param JPanel
	 *            - referencia do glassPane
	 */
	public CalendarView() {
		txt = new JTextField();
		button = new JButton();

		glassPane = new JWindow();

		add(txt);

		init();

		txt.addKeyListener(keyAdapter);
		txt.addFocusListener(focusAdapter);
		new ActionManager(this);
	}

	private void init() {
		txt.setPreferredSize(new Dimension(73, 21));
		txt.setSelectionEnd(10);
		txt.setMinimumSize(new Dimension(73, 21));
		txt.setMaximumSize(new Dimension(73, 21));
		txt.setText("__/__/____");
		txt.setColumns(10);
		button.setFont(new Font("SansSerif", 0, 12));
		button.setText("..");
		button.setFont(new Font("SansSerif", Font.BOLD, 12));
		add(button);
	}

	/**
	 * Metodo para setar o campo texto
	 * 
	 * @param String
	 *            - texto
	 */
	public void setText(String text) {
		txt.setText(text);
	}

	/**
	 * Metodo para retornar o campo texto
	 * 
	 * @return String - texto
	 */
	public String getText() {
		return txt.getText();
	}

	/**
	 * This is the action when button was pressed.
	 * 
	 * @see com.towel.awt.ann.Action
	 * @see com.towel.awt.ann.ActionManager
	 */
	@SuppressWarnings("unused")
	private void openPopup() {
		String strDia = txt.getText();
		if (DateUtils.isValidDate(strDia)) {
			int dia = Integer.parseInt(strDia.substring(0, 2));
			int mes = Integer.parseInt(strDia.substring(3, 5));
			int ano = Integer.parseInt(strDia.substring(6, 10));

			cal = new DatePicker(this, dia, mes - 1, ano);
		} else {
			cal = new DatePicker(this, 0, 0, 0);
		}

		JPanel content = new JPanel();
		content.setLayout(null);
		content.add(cal);

		glassPane.setContentPane(content);
		glassPane.setSize(140, 150);
		glassPane.setLocation(button.getLocationOnScreen());
		glassPane.setVisible(true);
	} // ActionPerformed

	/**
	 * Remove o calendario do glassPane e coloca a data no campo texto
	 * 
	 * @param String
	 *            - data
	 */
	public void dateSelected(String s) {
		txt.setText(s);
		glassPane.remove(cal);
		cal = null;
		glassPane.setVisible(false);
	}

	// ==================KEYLISTENER=================================================
	private KeyAdapter keyAdapter = new KeyAdapter() {
		/**
		 * Metodo que valida o que é digitado permitindo somente numeros
		 */
		public void keyTyped(KeyEvent k) {
			char c = k.getKeyChar();
			if ((getText().length() > 9) & (!getText().equals("__/__/____")))
				k.consume();
			else {
				if ((c < '0') | (c > '9'))// & (c != '/'))
					k.consume();
				else {
					if (cont == 0) {
						setText("");
						cont = 1;
					}
					switch (getText().length()) {
					case 2:
						setText(getText() + "/");
						break;
					case 5:
						setText(getText() + "/");
						break;
					}
				}
			}
		}
	};

	private FocusAdapter focusAdapter = new FocusAdapter() {
		public void focusLost(FocusEvent fe) {
			if (!getText().equals("__/__/____") & !getText().equals("")) {
				if (!DateUtils.isValidDate(getText())) {
					JOptionPane.showMessageDialog(null, "Data Inválida");
				}
			}
		}
	};

	/**
	 * Quando o componente perde o foco é validado a data
	 */

	public void setEnabled(boolean t) {
		txt.setEnabled(t);
		button.setEnabled(t);
		super.setEnabled(t);
	}
}
