/*
 * Copyright (c) 2003-2006 jMonkeyEngine
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

package net.java.dev.aircarrier.scene.actree.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.java.dev.aircarrier.scene.CarrierSkyBox;
import net.java.dev.aircarrier.scene.actree.ACube;
import net.java.dev.aircarrier.scene.actree.AFace;
import net.java.dev.aircarrier.scene.actree.BVector3i;
import net.java.dev.aircarrier.scene.actree.CubeGrid;
import net.java.dev.aircarrier.scene.actree.OctoBox;

import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.WireframeState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * <code>TestAcubeFP</code>
 */
public class TestAcubeFP extends AcubeGame {

	CarrierSkyBox skyBox;
	OctoBox octoBox;
	Vector3f octoV = new Vector3f(
			0,
			0,
			0);

	Vector3f walkForward = new Vector3f();
	Vector3f walkLeft = new Vector3f();
	Vector3f worldUp = new Vector3f(0, 1, 0);

	Vector3f walkVector = new Vector3f();

	Vector3f cornerPos = new Vector3f();
	float speed = 3.5f;
	float jump = 6f;
	float g = 12f;

	/**
	 * Entry point for the test,
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.WARNING);

		TestAcubeFP app = new TestAcubeFP();
		app.samples = 4;

        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	@Override
	protected void simpleUpdate() {
		super.simpleUpdate();

		//Work out forward and left directions for movement based on camera and y axis
		Vector3f camForward = cam.getDirection();

		walkLeft.set(worldUp);
		walkLeft.crossLocal(camForward).normalizeLocal();

		walkForward.set(walkLeft);
		walkForward.crossLocal(worldUp).normalizeLocal();

		if (KeyBindingManager.getKeyBindingManager().isValidCommand("f", true)) {
		} else if (KeyBindingManager.getKeyBindingManager().isValidCommand("b", true)) {
			walkForward.multLocal(-1);
		} else {
			walkForward.set(0,0,0);
		}
		if (KeyBindingManager.getKeyBindingManager().isValidCommand("l", true)) {
		} else if (KeyBindingManager.getKeyBindingManager().isValidCommand("r", true)) {
			walkLeft.multLocal(-1);
		} else {
			walkLeft.set(0,0,0);
		}

		walkVector.set(0,0,0);
		walkVector.addLocal(walkLeft).addLocal(walkForward);
		walkVector.normalizeLocal().multLocal(speed);

		octoV.set(0, walkVector.get(0));
		octoV.set(2, walkVector.get(2));

		//If we are not standing on the ground, accelerate downwards
		if (!octoBox.getTouching()[0][1]) {
			octoV.set(1, octoV.get(1) - tpf * g);
		//Otherwise, vertical velocity cannot be negative (stop falling)
		} else {
			if (octoV.get(1) < 0) octoV.set(1, 0);
		}

		//If we hit our head, then vertical velocity cannot be positive
		if (octoBox.getTouching()[1][1]) {
			if (octoV.get(1) > 0) {
				octoV.set(1, 0);
			}
		}

        if (KeyBindingManager.getKeyBindingManager().isValidCommand("u", true)) {
        	octoV.set(1, speed);
        }
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("d", true)) {
        	octoV.set(1, -speed);
        }

        if (KeyBindingManager.getKeyBindingManager().isValidCommand("jump", false)
        		&& octoBox.getTouching()[0][1]) {
        	octoV.set(1, jump);
        }


		//Skybox follows camera
		skyBox.setLocalTranslation(cam.getLocation());

		//Shift the colliding box along
		octoBox.slideAlong(octoV, tpf);
		//octoBox.slide(octoV, tpf, null);
		box.getLocalTranslation().set(octoBox.getPosition());

		/*
		for (int xo = 0; xo < 2; xo++) {
			for (int yo = 0; yo < 2; yo++) {
				for (int zo = 0; zo < 2; zo++) {
					int i = xo + yo*2 + zo*4;
					cornerPos.setX(octoBox.getBounds()[xo][0] + 0.5f - xo);
					cornerPos.setY(octoBox.getBounds()[yo][1] + 0.5f - yo);
					cornerPos.setZ(octoBox.getBounds()[zo][2] + 0.5f - zo);
					corners[i].getLocalTranslation().set(cornerPos);
				}
			}
		}
		*/

		cam.getLocation().set(octoBox.getPosition().getX(), octoBox.getPosition().getY() + 0.3f, octoBox.getPosition().getZ());
		cam.update();

	}

	public final static Texture sss;
	static {
		sss = TextureManager.loadTexture(TestAcubeFP.class.getClassLoader().getResource(
				"resources/blackBoxSSS.png"),
            Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear);
	}

	public final static Texture green;
	static {
		green = TextureManager.loadTexture(TestAcubeFP.class.getClassLoader().getResource(
				"resources/green.png"),
            Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear);
	}

	public final static Texture crete;
	private CubeGrid grid;
	private Box box;

	//private Box[] corners;

	static {
		crete = TextureManager.loadTexture(TestAcubeFP.class.getClassLoader().getResource(
				"resources/render.png"),
            Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear);
	}


	/**
	 * Set up systems
	 */
	protected void simpleInitGame() {

		display.setTitle("Terrain Test");
		cam.setLocation(new Vector3f());
		cam.update();

		FogState fs = display.getRenderer().createFogState();
		fs.setEnabled(false);
		rootNode.setRenderState(fs);

		CullState cs = display.getRenderer().createCullState();
		cs.setCullFace(CullState.Face.Back);
		cs.setEnabled(true);

        ColorRGBA color = new ColorRGBA(0.4f, 0.5f, 0.65f, 1f);
        display.getRenderer().setBackgroundColor(color);

		lightState.setEnabled(false);

        KeyBindingManager.getKeyBindingManager().set("f", KeyInput.KEY_W);
        KeyBindingManager.getKeyBindingManager().set("b", KeyInput.KEY_S);
        KeyBindingManager.getKeyBindingManager().set("l", KeyInput.KEY_A);
        KeyBindingManager.getKeyBindingManager().set("r", KeyInput.KEY_D);
        KeyBindingManager.getKeyBindingManager().set("u", KeyInput.KEY_Q);
        KeyBindingManager.getKeyBindingManager().set("d", KeyInput.KEY_Z);
        KeyBindingManager.getKeyBindingManager().set("jump", KeyInput.KEY_SPACE);


		/*
		lightState.setTwoSidedLighting(true);
		Debugger.AUTO_NORMAL_RATIO = .02f;

		((PointLight) lightState.get(0))
				.setLocation(new Vector3f(100, 500, 50));
*/

		rootNode.setRenderState(cs);

		skyBox = new CarrierSkyBox("Sky", "resources/bsky", ".jpg");
		rootNode.attachChild(skyBox);
		Quaternion skyQ = new Quaternion(new float[]{0,FastMath.PI * -0.75f,0});
		skyBox.setLocalRotation(skyQ);

		/*
		grid = new CubeGrid(8);
		Random r = new Random(101);
		BVector3i bv = new BVector3i(0,0,0);
		for (int i = 0; i < 2000; i++) {
			int direction = r.nextInt(6);
			bv.addLocal(AFace.intThreeDCardinalDirections[direction]);
			for (int j = 0; j < 3; j++) {
				if (bv.get(j) < 0) bv.set(j, 0);
				if (bv.get(j) >= grid.size(j)) bv.set(j, grid.size(j)-1);
			}
			grid.setPresence(bv, true);
		}
		*/



		grid = new CubeGrid(5);
		for (int x = 0; x < 20; x++) {
			for (int y = 0; y < 20; y++) {
				grid.setPresence(new BVector3i(x, y, 10), true);
			}
		}
		grid.setPresence(new BVector3i(2, 2, 9), true);
		grid.setPresence(new BVector3i(2, 2, 8), true);
		grid.setPresence(new BVector3i(2, 2, 7), true);

		grid.setPresence(new BVector3i(2, 3, 9), true);

		grid.setPresence(new BVector3i(2, 7, 9), true);

		grid.setPresence(new BVector3i(2, 9, 9), true);



		makeArch(grid, 9, 2, 9, false);
		makeArch(grid, 9, 5, 9, false);
		makeArch(grid, 9, 8, 9, false);
		makeArch(grid, 12, 2, 9, false);
		makeArch(grid, 12, 5, 9, false);
		makeArch(grid, 12, 8, 9, false);

		makeArch(grid, 9, 2, 6, true);

		//Node gridRoot = new Node("gridRoot");




		grid.buildAllCubes();


		for (int x = 0; x < 20; x++) {
			for (int y = 0; y < 20; y++) {
				for (int z = 0; z < 20; z++) {
					for (int f = 0; f < 6; f++) {
						ACube cube = grid.getCube(new BVector3i(x, y, z));
						if (cube != null) {
							AFace face = cube.getFace(f);
							if (face != null) {
								TextureState ts = (TextureState)face.getRenderState(RenderState.RS_TEXTURE);
								ts.setTexture(sss, 1);
							}
						}
					}
				}
			}
		}


		Node gridRoot = grid.getOctode();
		setupFog(gridRoot);
		gridRoot.updateRenderState();

		grid.shade(CubeGrid.DEFAULT_BASE_COLOR, CubeGrid.DEFAULT_DARK_COLOR, CubeGrid.DEFAULT_FACE_COLORS);

		rootNode.attachChild(gridRoot);
		//gridRoot.rotateUpTo(new Vector3f(0, 0, 1));
		gridRoot.updateModelBound();
        gridRoot.updateWorldBound(); // We do this to allow the camera setup access to the world bound in our setup code.

        gridRoot.lock();

		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture(crete, 0);
        box = new Box("box", new Vector3f(), 0.4f, 0.9f, 0.4f);
        box.setRenderState(ts);
		rootNode.attachChild(box);

		WireframeState wfState = DisplaySystem.getDisplaySystem().getRenderer().createWireframeState();
		wfState.setLineWidth(3);
		MaterialState mState = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
		mState.setDiffuse(ColorRGBA.blue);
		TextureState ts2 = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts2.setTexture(green, 0);

		/*
		corners = new Box[8];

		for (int i = 0; i < corners.length; i++) {
			corners[i] = new Box("corner " + i, new Vector3f(), 0.5f, 0.5f, 0.5f);
			rootNode.attachChild(corners[i]);
			corners[i].setRenderState(wfState);
			corners[i].setRenderState(mState);
			corners[i].setRenderState(ts2);
		}
		*/

		rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);

		fpsNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);

		octoBox = new OctoBox(grid, new Vector3f(0.4f, 0.9f, 0.4f), new Vector3f(10, 32, 10));

	}

	private void setupFog(Node node) {
		ColorRGBA skyColor = new ColorRGBA(0.956862745f, 0.945098039f, 0.917647058f, 0.5f);
		FogState fs = display.getRenderer().createFogState();
		fs.setDensity(1f);
		fs.setEnabled(true);
		fs.setColor(skyColor);
		fs.setEnd(35);
		fs.setStart(2);
		fs.setDensityFunction(FogState.DensityFunction.Linear);
		node.setRenderState(fs);
	}

	private void makeArch(CubeGrid grid, int x, int y, int z, boolean east) {
		int xo = east ? 1 : 0;
		int yo = 1 - xo;
		grid.setPresence(new BVector3i(x, y, z), true);
		grid.setPresence(new BVector3i(x, y, z-1), true);
		grid.setPresence(new BVector3i(x, y, z-2), true);
		grid.setPresence(new BVector3i(x, y, z-3), true);
		grid.setPresence(new BVector3i(x+1*xo, y+1*yo, z-3), true);
		grid.setPresence(new BVector3i(x+2*xo, y+2*yo, z-3), true);
		grid.setPresence(new BVector3i(x+3*xo, y+3*yo, z-3), true);
		grid.setPresence(new BVector3i(x+3*xo, y+3*yo, z-2), true);
		grid.setPresence(new BVector3i(x+3*xo, y+3*yo, z-1), true);
		grid.setPresence(new BVector3i(x+3*xo, y+3*yo, z), true);
	}

}
