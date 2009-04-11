/*
 *  $Id: TestSharedMesh.java,v 1.4 2007/08/12 10:04:10 shingoki Exp $
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


import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.SharedMesh;
import com.jme.scene.TriMesh;
import com.jme.scene.VBOInfo;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;

/**
 * <code>TestSharedMesh</code>
 *
 * @author Mark Powell
 * @version $Id: TestSharedMesh.java,v 1.4 2007/08/12 10:04:10 shingoki Exp $
 */
public class TestSharedMesh extends SimpleGame {

	private Quaternion rotQuat = new Quaternion();

	private float angle = 0;

	private Vector3f axis = new Vector3f(1, 1, 0);

	private TriMesh s;

	int frame = 0;

	TextureState ts;
	TextureState ts2;
	TextureState ts3;
	TextureState ts4;

	Node n1;
	Node n2;
	Node n3;
	Node n4;

	/**
	 * Entry point for the test,
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Logger.getLogger("").setLevel(Level.WARNING);

		TestSharedMesh app = new TestSharedMesh();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	protected void simpleUpdate() {
		if (tpf < 1) {
			angle = angle + (tpf * 1);
			if (angle > 360) {
				angle = 0;
			}
		}
		rotQuat.fromAngleAxis(angle, axis);
		s.setLocalRotation(rotQuat);

		frame ++;

		if (frame ==100) {
			for (int i = 0; i < 100; i++) {
				SharedMesh sm = new SharedMesh("Share" + i, s);
				sm.setLocalTranslation(new Vector3f(
						(float) Math.random() * 500 - 250,
						(float) Math.random() * 500 - 250,
						(float) Math.random() * 500 - 250));
				sm.setRenderState(ts2);
				sm.setLightCombineMode(SharedMesh.LightCombineMode.Off);
				n2.attachChild(sm);
			}
			n2.updateGeometricState(0, true);
			n2.updateRenderState();
		}

		if (frame == 200) {
			for (int i = 0; i < 100; i++) {
				SharedMesh sm = new SharedMesh("Share" + i, s);
				sm.setLocalTranslation(new Vector3f(
						(float) Math.random() * 500 - 250,
						(float) Math.random() * 500 - 250,
						(float) Math.random() * 500 - 250));
				sm.setRenderState(ts3);

				n3.attachChild(sm);
			}
			n3.updateGeometricState(0, true);
			n3.updateRenderState();

		}

		if (frame == 300) {
			for (int i = 0; i < 100; i++) {
				SharedMesh sm = new SharedMesh("Share" + i, s);
				sm.setLocalTranslation(new Vector3f(
						(float) Math.random() * 1000 - 500,
						(float) Math.random() * 1000 - 500,
						(float) Math.random() * 1000 - 500));
				sm.setRenderState(ts4);
				n4.attachChild(sm);
			}
			n4.updateGeometricState(0, true);
			n4.updateRenderState();

		}
	}

	/**
	 * builds the trimesh.
	 *
	 * @see com.jme.app.SimpleGame#initGame()
	 */
	protected void simpleInitGame() {
		display.setTitle("jME - Sphere");

		s = new Sphere("Sphere", 20, 20, 25);
		s.setModelBound(new BoundingBox());
		s.updateModelBound();
		s.setVBOInfo(new VBOInfo(true));

        CullState cs = display.getRenderer().createCullState();
        cs.setCullFace(CullState.Face.Back);
        cs.setEnabled(true);
        rootNode.setRenderState(cs);

		ts = display.getRenderer().createTextureState();
		ts.setEnabled(true);
		ts.setTexture(TextureManager.loadTexture(
				TestSharedMesh.class.getClassLoader().getResource(
						"jmetest/data/images/Monkey.jpg"),
            Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear));

		ts2 = display.getRenderer().createTextureState();
		ts2.setEnabled(true);
		ts2.setTexture(TextureManager.loadTexture(
				TestSharedMesh.class.getClassLoader().getResource(
						"jmetest/data/texture/grass.jpg"),
            Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear));

		ts3 = display.getRenderer().createTextureState();
		ts3.setEnabled(true);
		ts3.setTexture(TextureManager.loadTexture(
				TestSharedMesh.class.getClassLoader().getResource(
						"jmetest/data/texture/clouds.png"),
        Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear));

		ts4 = display.getRenderer().createTextureState();
		ts4.setEnabled(true);
		ts4.setTexture(TextureManager.loadTexture(
				TestSharedMesh.class.getClassLoader().getResource(
						"jmetest/data/texture/water.png"),
            Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear));



		n1 = new Node("n1");
		n2 = new Node("n2");
		n3 = new Node("n3");
		n4 = new Node("n4");
		n1.setLocalTranslation(new Vector3f(750, 0, 0));
		n2.setLocalTranslation(new Vector3f(750, 0, 750));
		n3.setLocalTranslation(new Vector3f(-750, 0, 750));
		n4.setLocalTranslation(new Vector3f(-750, 0, -750));

//		n1.setRenderState(ts);
//		n2.setRenderState(ts2);
//		n3.setRenderState(ts3);
//		n4.setRenderState(ts4);

		rootNode.attachChild(n1);
		rootNode.attachChild(n2);
		rootNode.attachChild(n3);
		rootNode.attachChild(n4);

		for (int i = 0; i < 100; i++) {
			SharedMesh sm = new SharedMesh("Share" + i, s);
			sm.setLocalTranslation(new Vector3f(
					(float) Math.random() * 500 - 250,
					(float) Math.random() * 500 - 250,
					(float) Math.random() * 500 - 250));
			sm.setRenderState(ts);
			n1.attachChild(sm);
		}



	}
}
