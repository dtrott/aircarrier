/*
 *  $Id: MuzzleFlash.java,v 1.4 2007/08/19 10:34:14 shingoki Exp $
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
import com.jme.math.Quaternion;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * Loads a new instance of the plain bullet mesh
 */

public class MuzzleFlash {

	private static BlendState blendState;
	private static Texture texture;
	static TextureState textureState;
	static LightState noLight;
	static ZBufferState zBufferState;

	/**
	 * Create a node with muzzle flash geometry
	 * @throws IOException
	 * 		If model cannot be loaded
	 */
	public static Node createMuzzleFlash() throws IOException {

		if (blendState == null) {
			initialize();
		}

		//Load the model
		JmeBinaryReader jbr = new JmeBinaryReader();
		Node model = jbr.loadBinaryFormat(MuzzleFlash.class.getClassLoader()
				.getResourceAsStream("resources/muzzle_flash.jme"));
		model.updateGeometricState(0, true);

		model.getChild(0).setLocalRotation(new Quaternion(new float[]{(float)(0),0,0}));

        //Set the model's render states correctly
		model.setRenderState(textureState);
		model.setRenderState(blendState);
		model.setRenderState(noLight);
		model.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
		model.setRenderState(zBufferState);

		model.updateRenderState();

		//FIXME work out shared mesh and use
		return model;
	}

	static void initialize() {

		DisplaySystem display = DisplaySystem.getDisplaySystem();

		//Alpha state for all bullets adds the texture to anything behind it
		blendState = display.getRenderer().createBlendState();
		blendState.setBlendEnabled( true );
		blendState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		blendState.setDestinationFunction(BlendState.DestinationFunction.One);
		blendState.setTestEnabled(true);
		blendState.setEnabled( true );

		//Texture has an elongated tracer blur
		texture = TextureManager.loadTexture(MuzzleFlash.class
				.getClassLoader().getResource("resources/plain_bullet3.png"),
                Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear);

		//Texture state for all bullets
		textureState = display.getRenderer().createTextureState();
		textureState.setTexture(texture);

		//Light state for all bullets, has no lighting so the bullets
		//just glow
        noLight  = display.getRenderer().createLightState();
        noLight.setEnabled(false);

        //ZBufferState for all bullets, makes the z buffer non-writable,
        //so that faces of the bullet do not hide anything (since they
        //are additive this should never be fine, and since the transparent
        //render queue is used, this should work out).
        zBufferState = display.getRenderer().createZBufferState();
        zBufferState.setEnabled(true);
        zBufferState.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        zBufferState.setWritable(false);
	}

}
