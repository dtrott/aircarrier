/*
 *  $Id: TestEllipsoidNode.java,v 1.2 2006/07/21 23:59:47 shingoki Exp $
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

import com.jme.app.SimpleGame;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;

/**
 * <code>TestLightState</code>
 *
 * @author Mark Powell
 * @version $Id: TestEllipsoidNode.java,v 1.2 2006/07/21 23:59:47 shingoki Exp $
 */
public class TestEllipsoidNode extends SimpleGame {

	/**
	 * Entry point for the test,
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		TestEllipsoidNode app = new TestEllipsoidNode();
		app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
	}

	/**
	 * builds the trimesh.
	 *
	 * @see com.jme.app.SimpleGame#initGame()
	 */
	protected void simpleInitGame() {
		display.setTitle("Ellipsoid test");
		cam.setLocation(new Vector3f(0.0f, 0.0f, 40.0f));
		cam.update();
		lightState.setEnabled(false); // we don't want lighting by default.

		Quad q = new Quad("Quad");
		q.initialize(3, 3);

		TextureState ts = display.getRenderer().createTextureState();
		ts.setEnabled(true);
		Texture t1 = TextureManager.loadTexture(
				TestEllipsoidNode.class.getClassLoader().getResource(
						"jmetest/data/images/Monkey.jpg"),
            Texture.MinificationFilter.BilinearNoMipMaps, Texture.MagnificationFilter.Bilinear);

		ts.setTexture(t1);
		q.setRenderState(ts);

		EllipsoidNode ellipsoid = new EllipsoidNode("Ellipsoid");
		ellipsoid.attachChild(q);

		rootNode.attachChild(ellipsoid);

		rootNode.attachChild(new Cylinder("cyl", 6, 6, 0.1f, 1f));
	}
}
