package test.img;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.towel.swing.img.JImagePanel;

public class JImagePanelLoopTest {
	public static void main(String[] args) throws Throwable {
		JImagePanel panel = new JImagePanel(10, new BufferedImage[] {
				loadImage("/home/marcos/imgs/1.png"),
				loadImage("/home/marcos/imgs/2.png"),
				loadImage("/home/marcos/imgs/3.png"),
				loadImage("/home/marcos/imgs/4.png"),
				loadImage("/home/marcos/imgs/5.png"),
				loadImage("/home/marcos/imgs/6.png"),
				loadImage("/home/marcos/imgs/7.png"),
				loadImage("/home/marcos/imgs/8.png"),
				loadImage("/home/marcos/imgs/9.png"),
				loadImage("/home/marcos/imgs/10.png"),
				loadImage("/home/marcos/imgs/11.png"),
				loadImage("/home/marcos/imgs/12.png") });

		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(100, 100));
		frame.add(panel);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private static BufferedImage loadImage(String file) throws IOException {
		return ImageIO.read(new File(file));
	}
}
