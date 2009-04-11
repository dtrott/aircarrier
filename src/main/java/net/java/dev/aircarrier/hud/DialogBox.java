/*
 *  $Id: DialogBox.java,v 1.3 2007/04/28 23:11:23 shingoki Exp $
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

package net.java.dev.aircarrier.hud;

import net.java.dev.aircarrier.scene.Grid;

import com.jme.image.Texture;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

/**
 * A grid with 3x3 quads, textured so as to have nice corners.
 * That is, the uv coords correspond to a 32x32 image which has
 * a border of 1 blank pixel, with the remaining 30x30 image being
 * made up of 9 10x10 sections, which are the corners, edges and
 * middle of the dialog box. The image corners are then always drawn at 
 * the same scale in the corners of the dialog. The top and bottom
 * edges on the image are stretched along the x axis to match the
 * top and bottom of the dialog, similarly for the left and right edges.
 * The center is stretched in both axes.
 * 
 * The image used may also be a multiple of 32x32 pixels (or some other
 * size, but this will not look as neat).
 * 
 * @author shingoki
 */
public class DialogBox extends Grid {
	private static final long serialVersionUID = -2906219587285439289L;
	
	float imageBaseSize;
	float tileSize;
	float borderSize;
	Texture t;

	/**
	 * Create a dialog box, assumed to use a 
	 * 32x32 image with 1 pixel border, and 10x10 tiles
	 * @param name
	 * 		The name of the box
	 * @param t
	 * 		The texture
	 */
	public DialogBox(
			String name, 
			Texture t) {
		this(name, 32, 10, 1, t);
	}

	/**
	 * Create a dialog box
	 * @param name
	 * 		The name of the box
	 * @param imageBaseSize
	 * 		The size of the texture used for tiles, the border size
	 * 		is set to imageBaseSize/32, and the tile size to 
	 * 		(imageBaseSize/32) * 10. So it is a scaled version of
	 * 		a 32x32 image with 1 pixel border, and 10x10 tiles
	 * @param t
	 * 		The texture
	 */
	public DialogBox(
			String name, 
			float imageBaseSize,
			Texture t) {
		//Assume a border size of 1, hence tile size is image size - 2, divided into 3 tiles
		this(name, imageBaseSize, (imageBaseSize/32) * 10, imageBaseSize/32, t);
	}
	
	/**
	 * Create a copy of a dialog box
	 * @param toCopy
	 * 		The dialog box to copy
	 */
	public DialogBox(DialogBox toCopy) {
		this(toCopy.getName(), toCopy.imageBaseSize, toCopy.tileSize, toCopy.borderSize, toCopy.t);
	}
	
	/**
	 * Create a dialog box
	 * @param name
	 * 		The name of the box
	 * @param imageBaseSize
	 * 		The size of the image used for the texture
	 * @param tileSize
	 * 		The size of the tiles in the texture (corner, edge and middle tiles)
	 * @param borderSize
	 * 		The size of the border around the texture
	 * @param t
	 * 		The texture
	 */
	public DialogBox(
			String name, 
			float imageBaseSize,
			float tileSize,
			float borderSize,
			Texture t) {
		//Make a 3x3 grid, which is sized to have 1 unit per tile pixel on each quad
		super(name, tileSize * 3, tileSize * 3, 3, 3, false);
		
		this.imageBaseSize = imageBaseSize;
		this.tileSize = tileSize;
		this.borderSize = borderSize;
		this.t = t;
		
		//Move the texture coords appropriately
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				moveUV(x, y, (borderSize + tileSize * x)/imageBaseSize, (borderSize + tileSize * y)/imageBaseSize);
			}
		}
		
		//Set texture
		TextureState textureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		textureState.setEnabled(true);
		textureState.setTexture(t);
		setRenderState(textureState);
		
		//set initial dimension to 3x3 tiles
		setDimension(tileSize * 3, tileSize * 3);
	}

	/**
	 * Scale the dialog box
	 * @param width
	 * 		The box width
	 * @param height
	 * 		The box height
	 */
	public void setDimension(float width, float height) {
		
		//Smallest dimension is 0
		if (width < 0) width = 0;
		if (height < 0) height = 0;
		
		//Work out the tile size to use - if either width or height
		//are too small to fit the tile size twice (for corners) then
		//reduce the tile size we use to half that dimension
		float smallestDimension = Math.min(width, height);
		float useTileSize = tileSize;
		if (smallestDimension < tileSize * 2) {
			useTileSize = smallestDimension/2;
		}
		
		//We just need to set the second and third columns and rows
		float widthMinusTile = width - useTileSize;
		float heightMinusTile = height - useTileSize;

		//Move the vertex coords appropriately
		for (int xIndex = 0; xIndex < 4; xIndex++) {
			for (int yIndex = 0; yIndex < 4; yIndex++) {
				
				float x = 0;
				if (xIndex == 0) {
					x = 0;
				} else if (xIndex == 1) {
					x = useTileSize;
				} else if (xIndex == 2) {
					x = widthMinusTile;
				} else {
					x = width;
				}

				float y = 0;
				if (yIndex == 0) {
					y = 0;
				} else if (yIndex == 1) {
					y = useTileSize;
				} else if (yIndex == 2) {
					y = heightMinusTile;
				} else {
					y = height;
				}

				movePoint(xIndex, yIndex, x, y);
			}
		}
		
	}

	

}
