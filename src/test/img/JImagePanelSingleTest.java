package test.img;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.towel.swing.img.JImagePanel;

public class JImagePanelSingleTest {
	public static void main(String[] args) throws Throwable {
		JImagePanel panel = new JImagePanel(
				loadImage("/home/marcos/imgs/1.png"));
		
		panel.setFillType(JImagePanel.FillType.SIDE_BY_SIDE);

		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(100, 100));
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private static BufferedImage loadImage(String file) throws IOException {
		return ImageIO.read(new File(file));
	}
}
