/*
 * Copyright (c) 2003-2007 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.java.dev.aircarrier.pass;

import com.jme.app.SimplePassGame;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;

/**
 * <code>TestAlternateCameraRenderPass</code>
 * Test for the alternate camera render pass.
 *
 * @author shingoki
 */
public class TestAlternateCameraRenderPass extends SimplePassGame {
	private float farPlane = 10000.0f;

	public static void main( String[] args ) {
		TestAlternateCameraRenderPass app = new TestAlternateCameraRenderPass();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	protected void cleanup() {
		super.cleanup();
	}

	protected void simpleUpdate() {
	}

	protected void simpleInitGame() {
		display.setTitle( "Alternate Camera Render Pass Test" );
		cam.setFrustumPerspective( 45.0f, (float) display.getWidth() / (float) display.getHeight(), 1f, farPlane );
		cam.setLocation( new Vector3f( 100, 50, 100 ) );
		cam.lookAt( new Vector3f( 0, 0, 0 ), Vector3f.UNIT_Y );
		cam.update();

	    TextureState ts = display.getRenderer().createTextureState();
	    //Base texture, not environmental map.
	    Texture t0 = TextureManager.loadTexture(
	            TestAlternateCameraRenderPass.class.getClassLoader().getResource(
	            "jmetest/data/images/logo.jpg"),

            Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear);


	    ts.setTexture(t0);

		Quad hudQuad = new Quad("hudQuad", 128, 128);
		hudQuad.setLocalTranslation(0, 0, 200);
		hudQuad.setRenderState(ts);
		CullState cs = display.getRenderer().createCullState();
		cs.setCullFace(CullState.Face.Back);
		hudQuad.setRenderState(cs);

		Box box = new Box("box", new Vector3f(), 1, 1, 1);
	    //box.setRenderState(ts);

		rootNode.attachChild(box);

		Node hudNode = new Node("hudNode");
		LightState ls = display.getRenderer().createLightState();
		ls.setEnabled(false);

		hudNode.attachChild(hudQuad);

		RenderPass rootPass = new RenderPass();
		rootPass.add( rootNode );
		pManager.add( rootPass );

		AlternateCameraRenderPass altPass = new AlternateCameraRenderPass();
		altPass.add(hudNode);
		pManager.add(altPass);

		RenderPass fpsPass = new RenderPass();
        // DJT TODO
		//fpsPass.add( fpsNode );
		pManager.add( fpsPass );


		rootNode.setCullHint(Spatial.CullHint.Never);
		rootNode.setRenderQueueMode( Renderer.QUEUE_OPAQUE );
        // DJT TODO
		//fpsNode.setRenderQueueMode( Renderer.QUEUE_OPAQUE );

		hudNode.updateRenderState();
		hudNode.updateGeometricState(0, true);
	}

}