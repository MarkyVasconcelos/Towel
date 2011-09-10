package com.towel.swing.img;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.towel.graphics.LoopImage;

/**
 * A panel that contains a background image. The background image is
 * automatically sized to fit in the panel.
 * 
 * @author Vinicius Godoy
 */
public class JImagePanel extends JPanel {
	private LoopImage images;
	// private BufferedImage image = null;
	private FillType fillType = FillType.RESIZE;

	/**
	 * Creates a new panel with the given background image.
	 * 
	 * @param img
	 *            The background image.
	 */
	public JImagePanel(BufferedImage img) {
		// setImage(img);
		images = new LoopImage(0, img);
	}

	/**
	 * Creates a new panel with the given background images looping at each tick
	 * interval.
	 * 
	 * @param tick
	 *            the time between swap the image
	 * @param imgs
	 *            The background images.
	 */
	public JImagePanel(long tick, BufferedImage... imgs) {
		images = new LoopImage(tick, imgs);
		new Looper().start();
	}

	/**
	 * Creates a new panel with the given background image.
	 * 
	 * @param img
	 *            The background image.
	 * @throws IOException
	 *             , if the image file is not found.
	 */
	public JImagePanel(File imgSrc) throws IOException {
		this(ImageIO.read(imgSrc));
	}

	/**
	 * Default constructor, should be used only for sub-classes
	 */
	protected JImagePanel() {

	}

	/**
	 * Creates a new panel with the given background image.
	 * 
	 * @param img
	 *            The background image.
	 * @throws IOException
	 *             , if the image file is not found.
	 */
	public JImagePanel(String fileName) throws IOException {
		this(new File(fileName));
	}

	/**
	 * Changes the image panel image.
	 * 
	 * @param img
	 *            The new image to set.
	 */
	public final void setImage(BufferedImage img) {
		if (img == null)
			throw new NullPointerException("Buffered image cannot be null!");

		this.images = new LoopImage(0, img);
		// this.image = img;
		invalidate();
	}

	/**
	 * Changes the image panel image.
	 * 
	 * @param img
	 *            The new image to set.
	 * @throws IOException
	 *             If the file does not exist or is invalid.
	 */
	public void setImage(File img) throws IOException {
		setImage(ImageIO.read(img));
	}

	/**
	 * Changes the image panel image.
	 * 
	 * @param img
	 *            The new image to set.
	 * @throws IOException
	 *             If the file does not exist or is invalid.
	 */
	public void setImage(String fileName) throws IOException {
		setImage(new File(fileName));
	}

	/**
	 * Returns the image associated with this image panel.
	 * 
	 * @return The associated image.
	 */
	public BufferedImage getImage() {
		return images.getCurrent();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		fillType.drawImage(this, g2d, images.getCurrent());
		g2d.dispose();
	}

	/**
	 * Returns the way this image fills itself.
	 * 
	 * @return The fill type.
	 */
	public FillType getFillType() {
		return fillType;
	}

	/**
	 * Changes the fill type.
	 * 
	 * @param fillType
	 *            The new fill type
	 * @throws IllegalArgumentException
	 *             If the fill type is null.
	 */
	public void setFillType(FillType fillType) {
		if (fillType == null)
			throw new IllegalArgumentException("Invalid fill type!");

		this.fillType = fillType;
		invalidate();
	}

	public static enum FillType {
		/**
		 * Make the image size equal to the panel size, by resizing it.
		 */
		RESIZE {
			@Override
			public void drawImage(JPanel panel, Graphics2D g2d,
					BufferedImage image) {
				g2d.drawImage(image, 0, 0, panel.getWidth(), panel.getHeight(),
						null);
			}
		},

		/**
		 * Centers the image on the panel.
		 */
		CENTER {
			@Override
			public void drawImage(JPanel panel, Graphics2D g2d,
					BufferedImage image) {
				int left = (panel.getWidth() - image.getWidth()) / 2;
				int top = (panel.getHeight() - image.getHeight()) / 2;
				g2d.drawImage(image, left, top, null);
			}

		},
		/**
		 * Makes several copies of the image in the panel, putting them side by
		 * side.
		 */
		SIDE_BY_SIDE {
			@Override
			public void drawImage(JPanel panel, Graphics2D g2d,
					BufferedImage image) {
				Paint p = new TexturePaint(image, new Rectangle2D.Float(0, 0,
						image.getWidth(), image.getHeight()));
				g2d.setPaint(p);
				g2d.fillRect(0, 0, panel.getWidth(), panel.getHeight());
			}
		};

		public abstract void drawImage(JPanel panel, Graphics2D g2d,
				BufferedImage image);
	}

	private class Looper extends Thread {
		public Looper() {
			setDaemon(true);
		}

		public void run() {
			while (true) {
				repaint();
				try {
					sleep(images.tick);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
