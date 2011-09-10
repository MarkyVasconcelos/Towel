package com.towel.swing.img;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.towel.graphics.LoopImage;


@Deprecated
public class ImageLoopPanel extends JPanel implements Runnable {
	private LoopImage lImg;
	private long tick;

	public ImageLoopPanel(long tick, BufferedImage... imgs) {
		this.tick = tick;
		lImg = new LoopImage(tick, imgs);
		setMinimumSize(new Dimension(imgs[0].getWidth(), imgs[0].getHeight()));
		new Thread(this).start();
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		lImg.draw(g2d);
		g2d.dispose();
	}

	public void run() {
		while (true) {
			repaint();
			try {
				Thread.sleep(tick);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
