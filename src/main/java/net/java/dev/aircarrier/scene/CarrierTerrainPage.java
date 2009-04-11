/*
 *  $Id: CarrierTerrainPage.java,v 1.8 2007/04/09 18:31:15 shingoki Exp $
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

package net.java.dev.aircarrier.scene;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

import jmetest.terrain.TestTerrain;
import net.java.dev.aircarrier.jme.terrain.ImageHeightMap;
import net.java.dev.aircarrier.util.TextureLoader;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.terrain.TerrainPage;

/**
 * A suitably scaled terrain, can be created from a set of images, with
 * a given terrain (quad) size and height scale
 * @author shingoki
 */
public class CarrierTerrainPage extends TerrainPage {
	private static final long serialVersionUID = -3827476033253550051L;

	public static CarrierTerrainPage create(
			String name, 
			String heightImageResource, 
			String lightImageResource,
			String detailImageResource,
			int size, 
			float terrainHorizontalScale,
			float terrainHeightScale,
			boolean clod) {
		
		URL heightURL = CarrierTerrainPage.class.getClassLoader().getResource(
				heightImageResource);
		
		Image heightImage = 
			new ImageIcon(heightURL
					).getImage();
		
		ImageHeightMap heightMap = new ImageHeightMap(heightImage, size);
		
		Vector3f terrainScale = new Vector3f(terrainHorizontalScale, terrainHeightScale, terrainHorizontalScale);
		
		heightMap.setHeightScale(1f);
		
		CarrierTerrainPage page = new CarrierTerrainPage(name, 17, heightMap.getSize(),
				terrainScale, heightMap.getHeightMap(), clod);

		page.setDetailTexture(1, 64);
		
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setEnabled(true);
		Texture t1 = TextureLoader.loadTexture(lightImageResource);
		ts.setTexture(t1, 0);

		Texture t2 = TextureManager.loadTexture(TestTerrain.class
				.getClassLoader()
				.getResource(detailImageResource),
				Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);
		ts.setTexture(t2, 1);
		t2.setWrap(Texture.WM_WRAP_S_WRAP_T);

		t1.setApply(Texture.AM_COMBINE);
		t1.setCombineFuncRGB(Texture.ACF_MODULATE);
		t1.setCombineSrc0RGB(Texture.ACS_TEXTURE);
		t1.setCombineOp0RGB(Texture.ACO_SRC_COLOR);
		t1.setCombineSrc1RGB(Texture.ACS_PRIMARY_COLOR);
		t1.setCombineOp1RGB(Texture.ACO_SRC_COLOR);
		t1.setCombineScaleRGB(1.0f);

		t2.setApply(Texture.AM_COMBINE);
		t2.setCombineFuncRGB(Texture.ACF_ADD_SIGNED);
		t2.setCombineSrc0RGB(Texture.ACS_TEXTURE);
		t2.setCombineOp0RGB(Texture.ACO_SRC_COLOR);
		t2.setCombineSrc1RGB(Texture.ACS_PREVIOUS);
		t2.setCombineOp1RGB(Texture.ACO_SRC_COLOR);
		t2.setCombineScaleRGB(1.0f);
		page.setRenderState(ts);
		
		return page;
	}

	private CarrierTerrainPage(String name, int blockSize, int size,
			Vector3f stepScale, int[] heightMap, boolean clod) {
		super(name, blockSize, size, stepScale, heightMap, clod);
		System.out.println("Made terrain with clod? " + clod);
	}

}
