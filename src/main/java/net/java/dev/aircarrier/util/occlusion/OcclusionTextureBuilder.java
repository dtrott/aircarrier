package net.java.dev.aircarrier.util.occlusion;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class OcclusionTextureBuilder {

	public final static void main(String[] args) {
		
		//Load first image
		try {
			File directory = new File("/home/shingoki/cuberenders/resized");
			BufferedImage first = loadImage(directory, 0);
			int width = first.getWidth();
			int height = first.getHeight();
			
			int totalWidth = 8 * width;
			int totalHeight = 8 * height;
			
			//Make a buffered image for final texture
			BufferedImage texture = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
			
			Graphics2D g = (Graphics2D)texture.getGraphics();
			
			for (int i = 0; i < 51; i++) {
				BufferedImage tile = loadImage(directory, i);
				int x = (i%8)*width;
				int y = ((int)(i/8))*height;
				g.drawImage(tile, x, y, null);
			}
			
			//Write out the texture
			ImageIO.write(texture, "png", new File(directory, "occlusionTexture.png"));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static BufferedImage loadImage(File directory, int i) throws IOException {
		return ImageIO.read(new File(directory, format(i + 1, 4) + ".png"));
	}
	
	public static String format(int i, int digits) {
		String s = Integer.toString(i);
		while (s.length() < digits) s = "0" + s;
		return s;
	}
	
}
