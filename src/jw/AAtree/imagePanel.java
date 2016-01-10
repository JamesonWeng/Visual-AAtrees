package jw.AAtree;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.*;


public class imagePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	BufferedImage image;
	
	public void setImage (BufferedImage i) {
		this.image = i;
	}
	
	@Override
	protected void paintComponent (Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}
}
