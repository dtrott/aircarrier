/*
 *  $Id: ConcreteCubeModel.java,v 1.4 2007/08/19 10:34:14 shingoki Exp $
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

import com.jme.bounding.OrientedBoundingBox;
import com.jme.image.Texture;
import com.jme.scene.Node;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class ConcreteCubeModel {

	private Node model;
	
	public ConcreteCubeModel() throws IOException {
		JmeBinaryReader jbr = new JmeBinaryReader();
		jbr.setProperty("bound", "obb");
		model = jbr.loadBinaryFormat(ConcreteCubeModel.class.getClassLoader()
				.getResourceAsStream("resources/concreteCube.jme"));

		model.updateRenderState();
		
		model.updateGeometricState(0, true);
		
		Texture texture = TextureManager.loadTexture(GunbirdMesh.class
				.getClassLoader().getResource("resources/concrete_cube.jpg"),
				Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);

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

				ts.setEnabled(true);

				// Set the texture to the quad
				n.setRenderState(ts);

			}
		}
		
		model.setModelBound(new OrientedBoundingBox());
		model.updateModelBound();
				
	}

	public Node getModel() {
		return model;
	}

}
