package com.towel.swing.separator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.UIManager;

public class TitledSeparator extends JLabel {
	public TitledSeparator(String text) {
		setText(text);
		setFocusable(false);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g.create();
		int startX = getFontMetrics(getTitleFont()).stringWidth(getText()) + 5;
		int half = getSize().height / 2;
		g2d.drawLine(startX, half, getSize().width, half);
		g2d.dispose();
	}

	@Override
	public void updateUI() {
		super.updateUI();
		Color foreground = getTitleColor();
		if (foreground != null) {
			setForeground(foreground);
		}
		setFont(getTitleFont());
	}

	private Color getTitleColor() {
		return UIManager.getColor("TitledBorder.titleColor");
	}

	private Font getTitleFont() {
		return UIManager.getFont("TitledBorder.font");
	}
}
