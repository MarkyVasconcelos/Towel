package com.towel.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.UIManager;

public class ButtonLabel extends AbstractButton {
	private String text;
	private int height;

	public ButtonLabel(String text) {
		setText(text);
		addMouseListener(new MouseClick());
	}

	@Override
	public void paintComponent(Graphics g) {
		try {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setFont(UIManager.getFont("TitledBorder.font"));
			g2d.setColor(getColor());
			g2d.drawString(text, 0, (height + 6) / 2);
			g2d.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		updateSize();
	}

	private void updateSize() {
		FontMetrics metrics = getFontMetrics(UIManager
				.getFont("TitledBorder.font"));
		height = metrics.getHeight();
		setPreferredSize(new Dimension(metrics.stringWidth(getText()), height));
	}

	private class MouseClick extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent evnt) {
			for (ActionListener listener : getActionListeners())
				listener.actionPerformed(new ActionEvent(ButtonLabel.this, 0,
						""));
		}

		@Override
		public void mouseEntered(MouseEvent evnt) {
			if (getActionListeners().length == 0)
				return;
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		}

		@Override
		public void mouseExited(MouseEvent evnt) {
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	private Color getColor() {
		return UIManager.getColor("TitledBorder.titleColor");
	}
}
