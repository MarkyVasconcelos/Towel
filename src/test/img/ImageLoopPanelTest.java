package test.img;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.towel.swing.img.ImageLoopPanel;

public class ImageLoopPanelTest {
	public static void main(String[] args) throws Throwable {
		ImageLoopPanel panel = new ImageLoopPanel(10, new BufferedImage[] {
				loadImage("/home/marcos/imgs/img1.png"),
				loadImage("/home/marcos/imgs/img2.png"),
				loadImage("/home/marcos/imgs/img3.png"),
				loadImage("/home/marcos/imgs/img4.png"),
				loadImage("/home/marcos/imgs/img5.png"),
				loadImage("/home/marcos/imgs/img6.png") });
		
		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(100,100));
		frame.add(panel);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		Thread t = new Thread(panel);
		t.setDaemon(true);//Necessario, por que se a aplicação fechar o loop também parar
		t.start();
	}

	private static BufferedImage loadImage(String file) throws IOException {
		return ImageIO.read(new File(file));
	}
}
