/*
 *  $Id: SimpleMeshFactory.java,v 1.11 2007/08/19 10:34:14 shingoki Exp $
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

package net.java.dev.aircarrier.bullets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.java.dev.aircarrier.model.XMLparser.JmeBinaryReader;

import com.jme.image.Texture;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SharedMesh;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.VBOInfo;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * Provides meshes with states applied
 * Tries to use SharedMesh to reduce overhead
 */

public class SimpleMeshFactory {

	private static Logger logger = Logger.getLogger(SimpleMeshFactory.class.toString());

	List<TriMesh> meshPool = new ArrayList<TriMesh>(100);

	int chunkSize = 100;

	List<RenderState> renderStates;

	int meshIndex = 0;

	String modelResourceName;

	int renderQueueMode;

	Vector3f offset;

	float scale;

	/**
	 * Return a factory for bullets
	 */
	public static SimpleMeshFactory makeBulletMeshFactory(
			) throws IOException {
		return makeGlowMeshFactory("resources/plain_bullet.jme", "resources/plain_bullet3SmallDark2.png", new Vector3f(0, 0, 0.5f), 1.0f);
	}

	/**
	 * Return a factory for hit flares
	 */
	public static SimpleMeshFactory makeHitFlareMeshFactory(
			) throws IOException {
		//return makeGlowMeshFactory("resources/hitFlare.jme", "resources/hitFlareDark2.png", new Vector3f(), 2.0f);
		//return makeGlowMeshFactory("resources/hitFlare.jme", "resources/hitFlareRed.png", new Vector3f(), 2.0f);
		return makeGlowMeshFactory("resources/hitFlareRing.jme", "resources/hitFlareRingDark6Darker.png", new Vector3f(0, 0, -1f), 1.0f);
	}

	/**
	 * Return a factory for hit flare rings
	 */
	public static SimpleMeshFactory makeHitFlareRingMeshFactory(
			) throws IOException {
		//return makeGlowMeshFactory("resources/hitFlareRing.jme", "resources/hitFlareRingDark2.png", new Vector3f(), 2.0f);
		//return makeAlphaMeshFactory("resources/hitFlareRing.jme", "resources/smokeAlpha2.png", new Vector3f(), 2.0f);
		return makeAlphaMeshFactory("resources/hitFlare.jme", "resources/hitFlareRingDark6Darker.png", new Vector3f(0, 0, -1f), 1.0f);
	}

	/**
	 * Return a factory for glowing meshes (e.g. bullets, flashes)
	 */
	public static SimpleMeshFactory makeGlowMeshFactory(
			String modelResourceName,
			String textureResourceName,
			Vector3f offset,
			float scale) throws IOException {

		BlendState blendState;
		Texture texture;
		TextureState textureState;
		LightState noLight;
		ZBufferState zBufferState;

		DisplaySystem display = DisplaySystem.getDisplaySystem();

		//Alpha state for all bullets adds the texture to anything behind it

		blendState = display.getRenderer().createBlendState();
		blendState.setBlendEnabled( true );
		blendState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		blendState.setDestinationFunction(BlendState.DestinationFunction.One);
		blendState.setTestEnabled(true);
		blendState.setEnabled( true );

		/*
		BlendState = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		BlendState.setBlendEnabled(true);
		//BlendState.setSourceFunction(BlendState.SB_ZERO);
		//BlendState.setDestinationFunction(BlendState.DB_ONE_MINUS_SRC_COLOR);
		BlendState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		BlendState.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		BlendState.setTestEnabled(true);
		BlendState.setEnabled(true);
		*/

		//Texture has an elongated tracer blur
        texture = TextureManager.loadTexture(SimpleMeshFactory.class
				.getClassLoader().getResource(textureResourceName),
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

        List<RenderState> renderStates = new ArrayList<RenderState>();
        renderStates.add(blendState);
        renderStates.add(textureState);
        renderStates.add(noLight);
        renderStates.add(zBufferState);

        return new SimpleMeshFactory(modelResourceName, renderStates, Renderer.QUEUE_TRANSPARENT, offset, scale);
	}

	/**
	 * Return a factory for glowing meshes (e.g. bullets, flashes)
	 */
	public static SimpleMeshFactory makeAlphaMeshFactory(
			String modelResourceName,
			String textureResourceName,
			Vector3f offset,
			float scale) throws IOException {

		BlendState blendState;
		Texture texture;
		TextureState textureState;
		LightState noLight;
		ZBufferState zBufferState;

		DisplaySystem display = DisplaySystem.getDisplaySystem();

		//Alpha state for all bullets adds the texture to anything behind it

		blendState = display.getRenderer().createBlendState();
		blendState.setBlendEnabled( true );
		blendState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		blendState.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		blendState.setTestEnabled(true);
		blendState.setEnabled( true );

		/*
		BlendState = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		BlendState.setBlendEnabled(true);
		//BlendState.setSourceFunction(BlendState.SB_ZERO);
		//BlendState.setDestinationFunction(BlendState.DB_ONE_MINUS_SRC_COLOR);
		BlendState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		BlendState.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		BlendState.setTestEnabled(true);
		BlendState.setEnabled(true);
		*/

		//Texture has an elongated tracer blur
		texture = TextureManager.loadTexture(SimpleMeshFactory.class
				.getClassLoader().getResource(textureResourceName),
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

        List<RenderState> renderStates = new ArrayList<RenderState>();
        renderStates.add(blendState);
        renderStates.add(textureState);
        renderStates.add(noLight);
        renderStates.add(zBufferState);

        return new SimpleMeshFactory(modelResourceName, renderStates, Renderer.QUEUE_TRANSPARENT, offset, scale);
	}


	/**
	 * Create a plain bullet node factory
	 * @throws IOException
	 * 		If model cannot be loaded
	 */
	public SimpleMeshFactory(
			String modelResourceName,
			List<RenderState> renderStates,
			int renderQueueMode,
			Vector3f offset,
			float scale) throws IOException {

		this.modelResourceName = modelResourceName;
		this.renderStates = renderStates;
		this.renderQueueMode = renderQueueMode;
		this.offset = offset;
		this.scale = scale;
	}

	/**
	 * @return
	 * 		A mesh
	 */
	public TriMesh getMesh() {

		if (meshPool.isEmpty()) {
			try {
				addMeshes();
			} catch (IOException e) {
				logger.warning("Couldn't add meshes: " + e);
				return null;
			}
		}

		return meshPool.remove(0);

	}


	/**
	 * Add chunkSize meshes to the pool
	 * @throws IOException
	 */
	private void addMeshes() throws IOException {

		//Load the model
		JmeBinaryReader jbr = new JmeBinaryReader();
		jbr.setProperty("bound", "sphere");
		Node model = jbr.loadBinaryFormat(SimpleMeshFactory.class.getClassLoader()
				.getResourceAsStream(modelResourceName));
		model.updateGeometricState(0, true);

		//Extract the model's first trimesh child, going down two layers
		TriMesh triMesh = null;
		for (Spatial spatial : model.getChildren()) {
			if (spatial instanceof TriMesh) {
				triMesh = (TriMesh)spatial;
			} else if (spatial instanceof Node) {
				Node node = (Node)spatial;
				for (Spatial spatial2 : node.getChildren()) {
					if (spatial2 instanceof TriMesh) {
						triMesh = (TriMesh)spatial2;
					}
				}
			}
		}

		if (triMesh == null) {
			throw new IOException("No TriMesh found in model file (in first two layers of nesting)");
		}

		triMesh.setLocalRotation(new Quaternion(new float[]{0f, (float)(Math.PI/2), 0f}));
		triMesh.getLocalTranslation().set(offset);
		triMesh.setLocalScale(scale);

		triMesh.setVBOInfo(new VBOInfo(true));

		//Make lots of sharedmeshes
		for (int i = 0; i < chunkSize; i++) {
			//Make a shared mesh from the triMesh
			SharedMesh sm = new SharedMesh("Shared Mesh " + meshIndex, triMesh);
			meshIndex++;

			//Set and update render states
			for (RenderState rs : renderStates) {
				sm.setRenderState(rs);
			}
			sm.setLocalScale(scale);

			sm.setRenderQueueMode(renderQueueMode);
			sm.updateRenderState();

			meshPool.add(sm);
		}
	}

}
