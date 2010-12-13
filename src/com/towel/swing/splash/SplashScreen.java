package com.towel.swing.splash;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;

public class SplashScreen extends JWindow {
	private JProgressBar bar;
	private JLabel label;

	public SplashScreen(final BufferedImage img) {
		JPanel panel = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g.create();

				g2d.drawImage(img, 0, 0, img.getWidth(), img.getHeight(),
						SplashScreen.this);
			}
		};
		panel.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));

		Container content = getContentPane();
		content.setLayout(new BorderLayout());
		content.add(panel, BorderLayout.NORTH);
		content.add(label = new JLabel(), BorderLayout.CENTER);
		content.add(bar = new JProgressBar(), BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
	}

	public void setMessage(String msg) {
		label.setText(msg);
		pack();
	}

	public void setProgress(int prog) {
		bar.setValue(prog);
	}
	
	public void setIndeterminateProgress(boolean value){
		bar.setIndeterminate(value);
	}
}
