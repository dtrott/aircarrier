/*
 *  $Id: PlainShotMesh.java,v 1.6 2007/02/14 20:09:22 shingoki Exp $
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

import net.java.dev.aircarrier.bullets.Bullet;
import net.java.dev.aircarrier.bullets.BulletPool;
import net.java.dev.aircarrier.bullets.ReusableSource;
import net.java.dev.aircarrier.bullets.SimpleMeshFactory;

import com.jme.app.SimpleGame;
import com.jme.app.VariableTimestepGame;
import com.jme.math.Vector3f;

/**
 * <code>TestAutoClodMesh</code> shows off the use of the AreaClodMesh in jME.
 * 
 * keys: L Toggle lights T Toggle Wireframe mode M Toggle Model or Disc
 * 
 * @author Joshua Slack
 * @version $Id: PlainShotMesh.java,v 1.6 2007/02/14 20:09:22 shingoki Exp $
 */

public class PlainShotMesh extends SimpleGame {

	/**
	 * Entry point for the test,
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		PlainShotMesh app = new PlainShotMesh();
		app.setDialogBehaviour(VariableTimestepGame.ALWAYS_SHOW_PROPS_DIALOG);
		app.start();
	}

	protected void simpleUpdate() {
	}

	/**
	 * builds the trimesh.
	 * 
	 * @see com.jme.app.SimpleGame#initGame()
	 */
	protected void simpleInitGame() {

		display.setTitle("Test shot model");
		cam.setLocation(new Vector3f(0.0f, 0.0f, 25.0f));
		cam.update();
		


		//ColorRGBA skyColor = new ColorRGBA(0.7f, 0.8f, 1f, 0.5f);
		//display.getRenderer().setBackgroundColor(skyColor);
		
		try {
			SimpleMeshFactory meshFactory = SimpleMeshFactory.makeBulletMeshFactory();
			
			ReusableSource<Bullet> bulletSource = new BulletPool(rootNode, meshFactory);
			
			Bullet shot;
			
			for (int i = 0; i < 100; i++) {
				shot = bulletSource.get();			
				rootNode.attachChild(shot);
				shot.getLocalTranslation().set(
						(float)Math.random(), 
						(float)Math.random(),
						(float)Math.random());
				//shot.getNode().getLocalScale().set(2,1,1);
			}
			
			rootNode.updateRenderState();
			rootNode.updateWorldData(0);
			rootNode.updateGeometricState(0, true);
			//FIXME find out what happened to this method
			//rootNode.updateCollisionTree();
			rootNode.updateWorldBound();
			rootNode.updateWorldVectors();
			

		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
}
