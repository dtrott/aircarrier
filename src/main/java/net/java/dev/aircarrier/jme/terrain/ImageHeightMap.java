/*
 *  $Id: ImageHeightMap.java,v 1.2 2006/07/21 23:58:54 shingoki Exp $
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

package net.java.dev.aircarrier.jme.terrain;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.jmex.terrain.util.AbstractHeightMap;

/**
 * Uses a scaled image to produce a heightmap.
 * @author shingoki
 */
public class ImageHeightMap extends AbstractHeightMap {

	Image image;

	/**
	 * Create a height map
	 * @param image
	 * 		Image to use
	 * @param size
	 * 		Size (along an edge)
	 */
	public ImageHeightMap(Image image, int size) {
		super();
		this.image = image;
		this.size = size;
		load();
	}

	/* (non-Javadoc)
	 * @see com.jmex.terrain.util.AbstractHeightMap#load()
	 */
	@Override
	public boolean load() {
		heightData = new float[size*size];

		//Make a scaled copy of the image
		BufferedImage scaled = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)scaled.getGraphics();

		g.drawImage(image, 0, 0, size, size, null);

		//Set the heightmap pixel by pixel
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int p = scaled.getRGB(x, size-1-y);
				Color c = new Color(p);
				setHeightAtPoint(c.getRed(), x, y);
			}
		}

		return true;
	}


}
