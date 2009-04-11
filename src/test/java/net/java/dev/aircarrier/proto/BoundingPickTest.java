/*
 *  $Id: BoundingPickTest.java,v 1.4 2007/02/14 20:09:22 shingoki Exp $
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

package net.java.dev.aircarrier.proto;

import java.net.URL;

import com.jme.app.SimpleGame;
import com.jme.bounding.OrientedBoundingBox;
import com.jme.image.Texture;
import com.jme.input.AbsoluteMouse;
import com.jme.input.MouseInput;
import com.jme.intersection.BoundingPickResults;
import com.jme.intersection.PickData;
import com.jme.intersection.PickResults;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.ShadeState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;

/**
 * Simpler test of bounding picking, derived
 * from TestOBBPick
 */
public class BoundingPickTest extends SimpleGame {

	// This will be my mouse
	AbsoluteMouse mouse;

	Box box;

	Node boxNode;

	public static void main(String[] args) {
		BoundingPickTest app = new BoundingPickTest();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	protected void simpleInitGame() {

		//Set root node flat shaded
		ShadeState ss = display.getRenderer().createShadeState();
		ss.setShadeMode(ShadeState.ShadeMode.Flat);
		rootNode.setRenderState(ss);

		// Create a new mouse.
		mouse = makeMouse();

		// Move the mouse to the middle of the screen to start with
		mouse.setLocalTranslation(new Vector3f(display.getWidth() / 2, display.getHeight() / 2, 0));

		// Assign the mouse to an input handler
		mouse.registerWithInputHandler( input );

        //Attach mouse to root node
		rootNode.attachChild(mouse);

		box = new Box("The Box", new Vector3f(0,0,0),1,1,1);
		box.setModelBound(new OrientedBoundingBox());
		box.updateModelBound();

		boxNode = new Node("The BoxNode");

		boxNode.attachChild(box);
		boxNode.updateWorldData(0);
		//FIXME find out what happened to this method
		//boxNode.updateCollisionTree();
		boxNode.updateWorldBound();
		boxNode.updateGeometricState(0, true);

		rootNode.attachChild(boxNode);

		// Deactivate the lightstate so we can see the per-vertex colors
		lightState.setEnabled(false);

		results.setCheckDistance(true);

	}

	AbsoluteMouse makeMouse() {
		AbsoluteMouse am = new AbsoluteMouse("The Mouse", display.getWidth(), display
				.getHeight());

		// Get a picture for my mouse.
		TextureState ts = display.getRenderer().createTextureState();
		URL cursorLoc;
		cursorLoc = BoundingPickTest.class.getClassLoader().getResource(
				"jmetest/data/cursor/cursor1.png");
		Texture t = TextureManager.loadTexture(cursorLoc, Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear);

     	ts.setTexture(t);
		am.setRenderState(ts);

		// Make the mouse's background blend with what's already there
		BlendState as = display.getRenderer().createBlendState();
		as.setBlendEnabled(true);
		as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		as.setTestEnabled(true);
		as.setTestFunction(BlendState.TestFunction.GreaterThan);
		am.setRenderState(as);

		return am;
	}

	PickResults results = new BoundingPickResults() {

		public void processPick() {
			System.err.println("PROCESSING");
			if (getNumber() > 0) {
				System.out.println("PROCESSING");
				for (int j = 0; j < getNumber(); j++) {
					PickData pData = getPickData(j);
					System.err.println(pData.getDistance());
					System.err.println(pData.getTargetMesh().getParent());
				}
			}
		}
	};


	// This is called every frame. Do changing of values here.
	protected void simpleUpdate() {

		// Is button 0 down? Button 0 is left click
		if (MouseInput.get().isButtonDown(0)) {
			Vector2f screenPos = new Vector2f();
			// Get the position that the mouse is pointing to
			screenPos.set(mouse.getHotSpotPosition().x, mouse.getHotSpotPosition().y);
			// Get the world location of that X,Y value
			Vector3f worldCoords = display.getWorldCoordinates(screenPos, 1.0f);
			// Create a ray starting from the camera, and going in the direction
			// of the mouse's location
			final Ray mouseRay = new Ray(cam.getLocation(), worldCoords
					.subtractLocal(cam.getLocation()));
			results.clear();

			//BufferUtils.setInBuffer(cam.getLocation(), l.getVertexBuffer(), 0);
			//BufferUtils.setInBuffer(worldCoords, l.getVertexBuffer(), 1);
			boxNode.calculatePick(mouseRay, results);

		}

	}
}