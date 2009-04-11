/*
 *  $Id: Bullet.java,v 1.10 2006/12/30 16:37:22 shingoki Exp $
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

import net.java.dev.aircarrier.input.action.NodeTranslator;

import com.jme.math.Ray;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
/**
 * A bullet, having a Node for display/position/collision, 
 * and various attributes
 * 
 * @author shingoki
 */
public class Bullet extends Node implements Reusable {
	private static final long serialVersionUID = -6450758034478476422L;

	Node worldNode;

	float damage;
	float speed;
	float age;
	float lifetime;
	
	ReusablePool<Bullet> pool;

	Ray ray = new Ray();
	
	long categoryBits;
	long collideBits;
	
	/**
	 * Create a bullet
	 * @param name
	 * 		Name for this node
	 * @param damage
	 * 		Damage done on hitting something
	 * @param speed
	 * 		Distance travelled per second
	 * @param age
	 * 		Accumulated age of bullet
	 * @param lifetime
	 * 		Maximum age of bullet
	 * @param pool
	 * 		The pool this bullet will return itself to
	 * @param worldNode
	 * 		The node this bullet will attach itself to
	 * 		when fired
	 * @param physicsWorld
	 * 		The physics world this bullet will attach its
	 * 		physics to when fired
	 * @param categoryBits
	 * 		The category bits for this bullet's physics
	 * @param collideBits
	 * 		The collide bits for this bullet's physics
	 */
	protected Bullet(
			String name,
			float damage, float speed, float age, float lifetime, 
			ReusablePool<Bullet> pool,
			Node worldNode,
			long categoryBits,
			long collideBits,
			Spatial graphicalNode
			) {
		super(name);
		this.attachChild(graphicalNode);
		updateGeometricState(0, true);
		updateRenderState();
		this.damage = damage;
		this.speed = speed;
		this.age = age;
		this.lifetime = lifetime;
		this.pool = pool;
		//this.physicsWorld = physicsWorld;
		this.worldNode = worldNode;
		this.categoryBits = categoryBits;
		this.collideBits = collideBits;
	}
	
	public void fire(Node from) {
		worldNode.attachChild(this);
		getLocalTranslation().set(from.getWorldTranslation());
		getLocalRotation().set(from.getWorldRotation());
		
		//FIXME UNUPDATE?
		//updateWorldData(0);
		//updateGeometricState(0, true);
	}
	
	public void update(float time) {
		age += time;
		
		//Move the bullet by its speed, updating the movement ray as we
		//do this.
		ray.getOrigin().set(getWorldTranslation());
		NodeTranslator.translate(this, 2, speed * time);
		ray.getDirection().set(getWorldTranslation()).subtractLocal(ray.getOrigin());
		ray.getDirection().normalizeLocal();
	}
	
	public Ray getRay() {
		return ray;
	}

	public boolean expired() {
		return (age > lifetime);
	}
	
	public void die() {
		//Remove ourselves from our parent node and
		//return to factory
		removeFromParent();
		pool.returnReusable(this);		
	}
	
	public float getAge() {
		return age;
	}
	public void setAge(float age) {
		this.age = age;
	}
	public float getDamage() {
		return damage;
	}
	public void setDamage(float damage) {
		this.damage = damage;
	}
	public float getLifetime() {
		return lifetime;
	}
	public void setLifetime(float lifetime) {
		this.lifetime = lifetime;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public ReusablePool<Bullet> getPool() {
		return pool;
	}
	public void setPool(ReusablePool<Bullet> pool) {
		this.pool = pool;
	}
	public Node getWorldNode() {
		return worldNode;
	}
	public void setWorldNode(Node worldNode) {
		this.worldNode = worldNode;
	}
	public long getCategoryBits() {
		return categoryBits;
	}
	public void setCategoryBits(long categoryBits) {
		this.categoryBits = categoryBits;
	}
	public long getCollideBits() {
		return collideBits;
	}
	public void setCollideBits(long collideBits) {
		this.collideBits = collideBits;
	}
}
