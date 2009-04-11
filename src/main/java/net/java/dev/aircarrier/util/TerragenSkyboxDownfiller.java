/*
 *  $Id: TerragenSkyboxDownfiller.java,v 1.2 2006/07/21 23:59:11 shingoki Exp $
 *
 * 	Copyright (c) 2005-2006 shingoki
 *
 *  This file is part of AirCarrier, see http://aircarrier.dev.java.net/
 *
 *    AirCarrier is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.

 *    AirCarrier is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.

 *    You should have received a copy of the GNU General Public License
 *    along with AirCarrier; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package net.java.dev.aircarrier.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class TerragenSkyboxDownfiller {

	int skipUp = 3;
	
	public TerragenSkyboxDownfiller(int skipUp) {
		super();
		this.skipUp = skipUp;
	}
	
	public void downfill(BufferedImage image) {
		
		//Make a buffered image from the image
		BufferedImage bi = image;
		//Scan down each column, to find the first black pixel
		for (int x = 0; x < bi.getWidth(); x++) {
			
			int firstBlankY = -1;
			
			for (int y = 0; y < bi.getHeight(); y++) {
				int p = bi.getRGB(x, y);
				Color c = new Color(p);
				
				if ((c.getRed() == 0) &&
					(c.getGreen() == 0) &&
					(c.getBlue() == 0) && 
					(firstBlankY == -1)) {
					firstBlankY = y;
				}
			}
			
			if (firstBlankY >= 0) {
				//Skip back up some pixels, get the color, and fill in the rest of the column the same
				//colour
				int xc = x;
				if (xc == 0) xc = 1;
				if (xc == bi.getWidth()-1) xc = bi.getWidth()-2; 
				int lastGoodColour = bi.getRGB(xc, firstBlankY - skipUp);/*averageColours(new int[]{
						bi.getRGB(xc, firstBlankY - skipUp),
						bi.getRGB(xc-1, firstBlankY - skipUp),
						bi.getRGB(xc+1, firstBlankY - skipUp),
				});*/
				
				for (int y = firstBlankY - skipUp; y < bi.getHeight(); y++) {
					bi.setRGB(x, y, lastGoodColour);
				}
			}
		}
	}

	public static int averageColours(int[] colours) {
		float r = 0;
		float g = 0;
		float b = 0;
		
		for (int i = 0; i < colours.length; i++) {
			Color c = new Color(colours[i]);
			r+=c.getRed();
			g+=c.getGreen();
			b+=c.getBlue();
		}
		float count = (float)(colours.length);
		r/=count;
		g/=count;
		b/=count;
		
		r = clip(r);
		g = clip(g);
		b = clip(b);
		
		return new Color(r,g,b).getRGB();
	}
	
	public static float clip(float v) {
		if (v < 0) {
			return 0;
		} else if (v > 1) {
			return 1;
		} else {
			return v;
		}
	}
	
	public final static void main(String[] args) {
		
		TerragenSkyboxDownfiller filler = new TerragenSkyboxDownfiller(4);
		
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Choose images to fill");
		chooser.setMultiSelectionEnabled(true);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			for (File f : chooser.getSelectedFiles()) {
				File outFile = new File(f.getParent(), "d_" + f.getName());
				try {
					BufferedImage read = ImageIO.read(f);
					filler.downfill(read);
					System.out.println(ImageIO.write(read, "bmp", outFile));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
