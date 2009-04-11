/*
 *  $Id: CompassDial.java,v 1.4 2007/08/19 10:34:14 shingoki Exp $
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

import java.io.IOException;

import net.java.dev.aircarrier.model.XMLparser.JmeBinaryReader;

import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.scene.Node;
import com.jme.scene.state.CullState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * Loads a new instance of the compass dial mesh
 */

public class CompassDial {

	private static Texture defaultTexture;
	private static TextureState defaultTextureState;
	private static CullState cullState;

	/**
	 * Create a node with compass dial, using default texture state
	 * @throws IOException
	 * 		If model cannot be loaded
	 */
	public static Node createCompassDial() throws IOException {
		if (defaultTexture == null) {
			initialize();
		}
		return createCompassDial(defaultTextureState);
	}

	/**
	 * Create a node with compass dial, using specified texture state
	 * @param textureState
	 * 		Texture for the number dial
	 * @throws IOException
	 * 		If model cannot be loaded
	 */
	public static Node createCompassDial(TextureState textureState) throws IOException {

		//Load the model
		JmeBinaryReader jbr = new JmeBinaryReader();
		Node model = jbr.loadBinaryFormat(CompassDial.class.getClassLoader()
				.getResourceAsStream("resources/compassDial.jme"));
		model.updateGeometricState(0, true);

		model.getChild(0).getLocalRotation().fromAngles(0, 0, FastMath.PI/2f);

		System.out.println("Compass Dial: " + model.getChild(0));

        //Set the model's render states correctly
		model.setRenderState(textureState);
		model.setRenderState(cullState);
		model.updateRenderState();

		return model;
	}

	static void initialize() {

		DisplaySystem display = DisplaySystem.getDisplaySystem();

		//Texture has a default strip of numbers
		defaultTexture = TextureManager.loadTexture(CompassDial.class
				.getClassLoader().getResource("resources/compassDial.png"),
                Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear);

		//Default texture state
		defaultTextureState = display.getRenderer().createTextureState();
		defaultTextureState.setTexture(defaultTexture);

		//CullState
		cullState = display.getRenderer().createCullState();
		cullState.setCullFace(CullState.Face.Back);
	}



}

