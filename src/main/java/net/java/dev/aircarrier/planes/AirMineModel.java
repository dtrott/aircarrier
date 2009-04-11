/*
 *  $Id: AirMineModel.java,v 1.7 2007/08/19 10:34:14 shingoki Exp $
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

import net.java.dev.aircarrier.GunbirdMesh;
import net.java.dev.aircarrier.model.XMLparser.JmeBinaryReader;

import com.jme.bounding.BoundingSphere;
import com.jme.image.Texture;
import com.jme.scene.Node;
import com.jme.scene.state.CullState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class AirMineModel {

	private Node model;

	public AirMineModel() throws IOException {

		JmeBinaryReader jbr = new JmeBinaryReader();
		jbr.setProperty("bound", "sphere");
		model = jbr.loadBinaryFormat(AirMineModel.class.getClassLoader()
				.getResourceAsStream("resources/airMine.jme"));


		/*
		model = (Node)BinaryImporter.getInstance().load(NewPlaneModel.class.getClassLoader()
				.getResourceAsStream("resources/chimera.jme"));

		System.out.println("MODEL START====================");
		printTree(model, 0);
		System.out.println("MODEL END====================");
		*/

		CullState cullState = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
		cullState.setCullFace(CullState.Face.None);
		model.setRenderState(cullState);

		model.updateRenderState();

		model.updateGeometricState(0, true);

		Texture texture = TextureManager.loadTexture(GunbirdMesh.class
                .getClassLoader().getResource("resources/airMine.png"),
            Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);

		Texture envTexture = TextureManager.loadTexture(AirMineModel.class
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
				ts.setTexture(texture, 0);

				// Add shiny environment to shield, cowling and engines
				if (		n.getName().indexOf("cowling") > -1
						|| 	n.getName().indexOf("shield") > -1
						|| 	n.getName().indexOf("engine") > -1
						|| 	n.getName().indexOf("canopy") > -1
						|| 	n.getName().indexOf("gun") > -1
						|| true
				) {
				      ts.setTexture( envTexture, 1 );
				}

				ts.setEnabled(true);

				// Set the texture to the quad
				n.setRenderState(ts);

			}
		}

		model.setModelBound(new BoundingSphere());
		model.updateModelBound();

	}

	public Node getModel() {
		return model;
	}

}
