/*
 *  $Id: WaterQuad.java,v 1.3 2006/07/21 23:59:54 shingoki Exp $
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

import java.nio.FloatBuffer;

import net.java.dev.aircarrier.util.TextureLoader;

import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.GLSLShaderObjectsState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.BufferUtils;

/**
 * Create a Quad with shaders to produce a reflect/refract water surface
 * 
 * @author shingoki
 * 
 */
public class WaterQuad extends Quad {
	private static final long serialVersionUID = 6251499689925767412L;
	
	private GLSLShaderObjectsState so;

	/**
	 * Create a water quad
	 * @param name
	 * 		Name for the quad
	 * @param reflectTex
	 * 		Reflect texture to use. This should be rendered to, with a view
	 * 		from a camera "reflected" in the water surface. Since this is
	 * 		difficult to achieve, a view from a camera which is just looking
	 * 		up from under the water is used instead. This gives an image
	 * 		which needs to be flipped in the x axis, this is done in the
	 * 		fragment shader.
	 * @param refractTex
	 * 		Reflect texture to use. This should be rendered to, with a view
	 * 		from the main camera.
	 */
	public WaterQuad(String name, Texture reflectTex, Texture refractTex) {
		this(name, 1, 1, reflectTex, refractTex);
	}
	
	/**
	 * Create a water quad
	 * @param name
	 * 		Name for the quad
	 * @param width
	 * 		Width of the quad
	 * @param height
	 * 		Height of the quad
	 * @param reflectTex
	 * 		Reflect texture to use. This should be rendered to, with a view
	 * 		from a camera "reflected" in the water surface. Since this is
	 * 		difficult to achieve, a view from a camera which is just looking
	 * 		up from under the water is used instead. This gives an image
	 * 		which needs to be flipped in the x axis, this is done in the
	 * 		fragment shader.
	 * @param refractTex
	 * 		Reflect texture to use. This should be rendered to, with a view
	 * 		from the main camera.
	 */
	public WaterQuad(String name, float width, float height, Texture reflectTex, Texture refractTex) {
		super(name, width, height);

		so = DisplaySystem.getDisplaySystem().getRenderer().createGLSLShaderObjectsState();

		// Check is GLSL is supported on current hardware.
		if (!so.isSupported()) {
			
			//Not supported, we should deal with this by falling back to a plain texture
			//FIXME fallback
			return;
		}

		so.load(WaterQuad.class.getClassLoader().getResource("resources/gokiwater.vert"), 
				WaterQuad.class.getClassLoader().getResource("resources/gokiwater.frag"));

		// Set the variable "reflection" to correspond to the first texture unit
		so.setUniform("reflection", 0); // second paramater is the texture unit

		// Set the variable "refraction" to correspond to the second texture
		// unit
		so.setUniform("refraction", 1);

		// Set the variable "normalMap" to correspond to the third texture unit
		so.setUniform("normalMap", 2);

		// Set the variable "dudvMap" to correspond to the fourth texture unit
		so.setUniform("dudvMap", 3);

		// Set the variable "depthMap" to correspond to the fifth texture unit
		so.setUniform("depthMap", 4);

		// Give the variable "waterColor" a blue color
		so.setUniform("waterColor", 0.1f, 0.2f, 0.4f, 1.0f);

		// Give the variable "lightPos" an initial position
		updateLightPosition(new Vector3f(0, 200, 1000));

		//Set camera position to origin
		updateCameraShaderPosition(new Vector3f(0,0,0));

		// Enable shader
		so.setEnabled(true);

		// Generate the quad
		setRenderState(so);

		FloatBuffer tbuf = BufferUtils.createVector2Buffer(4);
		tbuf.put(0).put(10);
		tbuf.put(0).put(0);
		tbuf.put(10).put(0);
		tbuf.put(10).put(10);
		//TODO check geom batch 0 is correct
		setTextureBuffer(0, tbuf);

		TextureState ts = (TextureState)getRenderState(RenderState.RS_TEXTURE);
		if (ts == null) {
			ts = DisplaySystem.getDisplaySystem().getRenderer()
					.createTextureState();
		}

		Texture dudv = TextureLoader.loadTexture("resources/dudvmap.bmp");
		dudv.setWrap(Texture.WM_WRAP_S_WRAP_T);
		Texture normal = TextureLoader.loadTexture("resources/normalmap.bmp");
		normal.setWrap(Texture.WM_WRAP_S_WRAP_T);

		// Turn on the first texture unit and bind the REFLECTION texture
		ts.setTexture(reflectTex, 0);

		// Turn on the second texture unit and bind the REFRACTION texture
		ts.setTexture(refractTex, 1);

		// Turn on the third texture unit and bind the NORMAL MAP texture
		ts.setTexture(normal, 2);

		// Turn on the fourth texture unit and bind the DUDV MAP texture
		ts.setTexture(dudv, 3);

		// Turn on the fifth texture unit and bind the DEPTH texture
		ts.setTexture(reflectTex, 4);

		ts.setEnabled(true);

		// Set the texture to the quad
		setRenderState(ts);

		//TODO check geom batch 0 is correct
		copyTextureCoords(0, 0, 1);
		copyTextureCoords(0, 0, 2);
		copyTextureCoords(0, 0, 3);
		copyTextureCoords(0, 0, 4);
	}

    public void updateCameraShaderPosition(Vector3f cameraPos) {
    	so.setUniform("cameraPos", cameraPos.x, cameraPos.y, cameraPos.z, 1.0f);     	
    }
    
    public void updateLightPosition(Vector3f lightPos) {
		so.setUniform("lightPos", lightPos.x, lightPos.y, lightPos.z, 1.0f);
    }
}
