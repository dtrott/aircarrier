/*
 *  $Id: CarrierSkyBox.java,v 1.5 2007/05/30 22:31:04 shingoki Exp $
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

import com.jme.image.Texture;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * 
 * A skybox with textures loaded from carrier resources
 * 
 * @author shingoki
 * 
 */
public class CarrierSkyBox extends Skybox {
	private static final long serialVersionUID = -5939987896538415224L;

	/**
	 * Create a skybox
	 * 
	 * @param name
	 *            Name for the node
	 * @param resourceBase
	 *            Base name for texture resources
	 * @param resourceExtension
	 *            Extension for texture resources
	 */
	public CarrierSkyBox(String name, String resourceBase,
			String resourceExtension) {
		super(name, 300, 300, 300);

		Texture u = loadSkyTexture(resourceBase + "U" + resourceExtension);
		Texture n = loadSkyTexture(resourceBase + "N" + resourceExtension);
		Texture e = loadSkyTexture(resourceBase + "E" + resourceExtension);
		Texture s = loadSkyTexture(resourceBase + "S" + resourceExtension);
		Texture w = loadSkyTexture(resourceBase + "W" + resourceExtension);
		Texture d = loadSkyTexture(resourceBase + "D" + resourceExtension);

		setTexture(Skybox.UP, u);
		setTexture(Skybox.NORTH, n);
		setTexture(Skybox.EAST, e);
		setTexture(Skybox.SOUTH, s);
		setTexture(Skybox.WEST, w);
		setTexture(Skybox.DOWN, d);
		
		preloadTextures();
		
		CullState cullState = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
		cullState.setCullMode( CullState.CS_NONE );
		cullState.setEnabled( true );
		setRenderState( cullState );

		ZBufferState zState = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		zState.setEnabled( false );
		setRenderState( zState );

		FogState fs = DisplaySystem.getDisplaySystem().getRenderer().createFogState();
		fs.setEnabled( false );
		setRenderState( fs );

		setLightCombineMode( LightState.OFF );
		setCullMode( Spatial.CULL_NEVER );
		
		setTextureCombineMode( TextureState.REPLACE );
		updateRenderState();

		//lockBounds();
		//lockMeshes();
		
		
	}

	static Texture loadSkyTexture(String resourceName) {
		return TextureManager.loadTexture(
				CarrierSkyBox.class.getClassLoader().getResource(resourceName), 
				Texture.MM_LINEAR_LINEAR,
				Texture.FM_LINEAR, 
				//Make sure we don't use compression, which makes skyboxes look awful
				com.jme.image.Image.GUESS_FORMAT_NO_S3TC,		 
				1f, 
				true);
	}
}
