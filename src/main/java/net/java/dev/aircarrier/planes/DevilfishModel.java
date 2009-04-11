/*
 *  $Id: DevilfishModel.java,v 1.16 2007/08/19 10:34:14 shingoki Exp $
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

package net.java.dev.aircarrier.planes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jmetest.renderer.TestEnvMap;

import net.java.dev.aircarrier.GunbirdMesh;
import net.java.dev.aircarrier.model.XMLparser.JmeBinaryReader;
import net.java.dev.aircarrier.scene.ApproximatelySphericalNode;

import com.jme.bounding.BoundingSphere;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class DevilfishModel implements PlaneModel{

	private Node model;
	
	List<Node> propPositions = new ArrayList<Node>();
	List<Node> gunPositions = new ArrayList<Node>();
	
	List<Node> bulletBounds = new ArrayList<Node>();
	
	public DevilfishModel() throws IOException {
		JmeBinaryReader jbr = new JmeBinaryReader();
		jbr.setProperty("bound", "obb");
		model = jbr.loadBinaryFormat(GunbirdMesh.class.getClassLoader()
				.getResourceAsStream("resources/devilfish2.jme"));

		model.updateGeometricState(0, true);
		
		Texture bodyTexture = TextureManager.loadTexture(GunbirdMesh.class
				.getClassLoader().getResource("resources/DevilfishBody.bmp"),
				Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);
		bodyTexture.setMipmapState(Texture.MM_NONE);

		Texture wingTexture = TextureManager.loadTexture(GunbirdMesh.class
				.getClassLoader().getResource("resources/DevilfishWing.bmp"),
				Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);
		wingTexture.setMipmapState(Texture.MM_NONE);

		Texture envTexture = TextureManager.loadTexture(TestEnvMap.class
				.getClassLoader()
				.getResource("resources/sky_env.jpg"),
				Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);
		envTexture.setEnvironmentalMapMode(Texture.EM_SPHERE);
		envTexture.setApply(Texture.AM_ADD);

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
				if (n.getName().indexOf("body_") >= 0) {
					ts.setTexture(bodyTexture, 0);
				} else {
					ts.setTexture(wingTexture, 0);
				}

				// Add shiny environment to shield, cowling and engines
				if (n.getName().indexOf("env") >= 0) {
				      ts.setTexture( envTexture, 1 );					
				}
				
				ts.setEnabled(true);

				// Set the texture to the quad
				n.setRenderState(ts);

			}
		}
		
		gunPositions = SimplePlaneModel.extractNodes(model, "gun");
		propPositions = SimplePlaneModel.extractNodes(model, "prop");

		model.setModelBound(new BoundingSphere());
		model.updateModelBound();
	}

	public Node getModel() {
		return model;
	}

	public List<Node> getGunPositions() {
		return gunPositions;
	}

	public List<Node> getPropPositions() {
		return propPositions;
	}

	public List<Node> getBulletBounds() {
		return bulletBounds;
	}

	public List<Node> getDamagePositions() {
		return new ArrayList<Node>();
	}

	public List<Node> getForwardFlaps() {
		return new ArrayList<Node>();
	}

	public List<Node> getRearFlaps() {
		return new ArrayList<Node>();
	}

	public ApproximatelySphericalNode getAvoidanceNode() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Node> getWreckage() {
		return new ArrayList<Node>();
	}

	public Spatial getPhysicsBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Node> getWreckageStartPositions() {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector3f getCameraOffset() {
		// TODO Auto-generated method stub
		return null;
	}

}
