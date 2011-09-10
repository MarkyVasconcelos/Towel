package com.towel.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class LoopImage {
	private BufferedImage imgs[];
	private int currentIndex;
	public long tick;
	private long lastTick;

	public LoopImage(long tick, BufferedImage... imgs) {
		this.imgs = imgs.clone();
		this.tick = tick;
		currentIndex = 0;
	}

	public void draw(Graphics2D g) {
		updateTick();
		Graphics2D g2d = (Graphics2D) g.create();
		BufferedImage current = imgs[currentIndex];
		g2d.drawImage(current, 0, 0, current.getWidth(), current.getHeight(),
				null);
		g2d.dispose();
	}

	private void updateTick() {
		long currentTick = System.currentTimeMillis();
		if (currentTick - lastTick > tick)
			currentIndex++;
		lastTick = currentTick;
		if (currentIndex == imgs.length)
			currentIndex = 0;
	}

	public BufferedImage getCurrent() {
		updateTick();
		return imgs[currentIndex];
	}
}
