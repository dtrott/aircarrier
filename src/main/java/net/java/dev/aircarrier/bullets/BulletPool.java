/*
 *  $Id: BulletPool.java,v 1.3 2006/07/21 23:58:36 shingoki Exp $
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

package net.java.dev.aircarrier.bullets;

import java.util.ArrayList;
import java.util.List;

import com.jme.scene.Node;
import com.jme.scene.TriMesh;

/**
 * A basic source of bullets
 */
public class BulletPool implements ReusablePool<Bullet>{

	Node worldNode;
	SimpleMeshFactory meshFactory;

	private List<Bullet> bullets = new ArrayList<Bullet>(500);
	
	/**
	 * Create a factory
	 * 
	 * @param worldNode
	 * 		The world node the bullets will be given 
	 * @param meshFactory
	 * 		The factory to get bullet meshes
	 */
	public BulletPool(
			Node worldNode,
			SimpleMeshFactory meshFactory) {
		
		this.worldNode = worldNode;
		this.meshFactory = meshFactory;
	}
	
	/**
	 * Get a node holding a shared mesh based on the bullet mesh, with
	 * appropriate texture, alpha, lighting, transparency and z-buffer states
	 * to render as a "tracer round"
	 * 
	 * @param damage
	 * 		Damage done on hitting something
	 * @param speed
	 * 		Distance travelled per second
	 * @param lifetime
	 * 		Maximum age of bullet (in seconds)
	 * @return
	 * 		A new plain bullet
	 */
	public Bullet get() {

		if (!bullets.isEmpty()) {
			Bullet b = bullets.remove(0);
			b.setDamage(0);
			b.setSpeed(0);
			b.setAge(0);
			b.setLifetime(10);		
			b.setPool(this);
			return b;
		} else {
			return make();
		}

	}
	
	/**
	 * Give back a bullet, save the planet by recycling lead!
	 * @param bullet
	 * 		Bullet, you must NOT use this any further, best to
	 * 		null the reference to make sure.
	 */
	public void returnReusable(Bullet bullet) {
		bullets.add(bullet);
	}
	
	/**
	 * Make a new bullet instance and return
	 * @return
	 * 		New bullet instance
	 */
	private Bullet make() {

		//Make a shared mesh from the triMesh
		TriMesh mesh = meshFactory.getMesh();

		//Make a new bullet
		Bullet bullet = new Bullet(
				"bullet", 
				0, 0, 0, 10, 
				this, 
				worldNode, 
				0,
				0,
				mesh);
		//Return the bullet
		return bullet;
	}
	
}
