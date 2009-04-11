/*
 *  $Id: GunbirdMesh.java,v 1.6 2007/08/19 10:34:14 shingoki Exp $
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

package net.java.dev.aircarrier;

import java.io.IOException;

import net.java.dev.aircarrier.model.XMLparser.JmeBinaryReader;

import com.jme.app.SimpleGame;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * <code>TestAutoClodMesh</code> shows off the use of the AreaClodMesh in jME.
 *
 * keys: L Toggle lights T Toggle Wireframe mode M Toggle Model or Disc
 *
 * @author Joshua Slack
 * @version $Id: GunbirdMesh.java,v 1.6 2007/08/19 10:34:14 shingoki Exp $
 */
public class GunbirdMesh extends SimpleGame {
	private Node model;

	// private boolean useModel = true;

	/**
	 * Entry point for the test,
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		GunbirdMesh app = new GunbirdMesh();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
		app.start();
	}

	protected void simpleUpdate() {
		// if (KeyBindingManager
		// .getKeyBindingManager()
		// .isValidCommand("switch_models", false)) {
		// useModel = !useModel;
		// iNode.setCullMode(useModel ? Spatial.CULL_ALWAYS :
		// Spatial.CULL_DYNAMIC);
		// iNode2.setCullMode(useModel ? Spatial.CULL_DYNAMIC :
		// Spatial.CULL_ALWAYS);
		// }
	}

	/**
	 * builds the trimesh.
	 *
	 * @see com.jme.app.SimpleGame#initGame()
	 */
	protected void simpleInitGame() {
		KeyBindingManager.getKeyBindingManager().set("switch_models",
				KeyInput.KEY_M);

		display.setTitle("Auto-Change Clod Test (using AreaClodMesh)");
		cam.setLocation(new Vector3f(0.0f, 0.0f, 25.0f));
		cam.update();

		/*
        PointLight light2 = new PointLight();
        light2.setDiffuse(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.75f));
        light2.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
        light2.setLocation(new Vector3f(0, 100, -100));
        light2.setEnabled(true);

        // Attach the light to a lightState and the lightState to rootNode.
        lightState.attach(light2);
        */

        rootNode.setRenderState(lightState);

		try {
			JmeBinaryReader jbr = new JmeBinaryReader();
			model = jbr.loadBinaryFormat(GunbirdMesh.class.getClassLoader()
					.getResourceAsStream("resources/airMine.jme"));
		} catch (IOException e) {

		}

		model.updateGeometricState(0, true);

		/*Texture bodyTexture = TextureManager.loadTexture(GunbirdMesh.class
				.getClassLoader().getResource("resources/gunbirdBody.bmp"),
				Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);

		Texture wingTexture = TextureManager.loadTexture(GunbirdMesh.class
				.getClassLoader().getResource("resources/hammerhead2.bmp"),
				Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);*/

		Texture texture = TextureManager.loadTexture(GunbirdMesh.class
				.getClassLoader().getResource("resources/airMine.png"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);

		Texture envTexture = TextureManager.loadTexture(GunbirdMesh.class
				.getClassLoader()
				.getResource("resources/sky_env_darker.jpg"),
                Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		envTexture.setEnvironmentalMapMode(Texture.EnvironmentalMapMode.SphereMap);
		envTexture.setApply(Texture.ApplyMode.Add);

		for (Object o : model.getChildren()) {
			System.out.println(o);
			if (o instanceof Node) {
				Node n = (Node) o;

				TextureState ts = (TextureState) n
						.getRenderState(RenderState.RS_TEXTURE);
				if (ts == null) {
					ts = DisplaySystem.getDisplaySystem().getRenderer()
							.createTextureState();
				}


				// Initialize the texture state
				/*
				if (n.getName().startsWith("b_")) {
					ts.setTexture(bodyTexture, 0);
				} else {
					ts.setTexture(wingTexture, 0);
				}*/
				ts.setTexture(texture);

				// Add shiny environment to shield, cowling and engines
				if (true){/*		n.getName().indexOf("cowling") > -1
						|| 	n.getName().indexOf("shield") > -1
						|| 	n.getName().indexOf("engine") > -1
						|| 	n.getName().indexOf("canopy") > -1
						|| 	n.getName().indexOf("gun") > -1
				) {*/
				      ts.setTexture( envTexture, 1 );
				}

				ts.setEnabled(true);

				// Set the texture to the quad
				n.setRenderState(ts);

			}
		}

		rootNode.attachChild(model);

		/*
		 * Spatial child = model.getChild(0); while(child instanceof Node) {
		 * child = ((Node)child).getChild(0); }
		 *
		 * iNode2 = new AreaClodMesh("model", (TriMesh)child, null);
		 * rootNode.attachChild(iNode2); iNode2.setDistanceTolerance( 0.0f);
		 * iNode2.setCullMode(Spatial.CULL_DYNAMIC); iNode2.setModelBound(new
		 * BoundingSphere()); iNode2.updateModelBound();
		 */

	}
}
