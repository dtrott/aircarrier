/*
 *  $Id: HitEffect.java,v 1.5 2007/03/22 21:59:15 shingoki Exp $
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

import net.java.dev.aircarrier.input.action.NodeRotator;

import com.jme.scene.Node;
import com.jme.scene.Spatial;
/**
 * A hit effect, having a Node with some animating bits
 * 
 * @author shingoki
 */
public class HitEffect extends Node implements Reusable {
	private static final long serialVersionUID = 8283884265847270810L;

	private static float FLARE_SIZE = 2.5f;//5;
	private static float RING_SIZE = 1.5f; //3;
	
	Node worldNode;

	float age;
	float lifetime;
	
	ReusablePool<HitEffect> pool;
	
	Spatial flare;
	Spatial ring;

	float flareRotateSpeed = 2;
	float ringRotateSpeed = 2;
	
	/**
	 * Create a bullet
	 * @param name
	 * 		Name for this node
	 * @param age
	 * 		Accumulated age of bullet
	 * @param lifetime
	 * 		Maximum age of bullet
	 * @param pool
	 * 		The pool this bullet will return itself to
	 * @param worldNode
	 * 		The node this bullet will attach itself to
	 * 		when fired
	 */
	protected HitEffect(
			String name,
			float age, float lifetime, 
			ReusablePool<HitEffect> pool,
			Node worldNode,
			Spatial flare,
			Spatial ring
			) {
		super(name);
		this.attachChild(flare);
		this.attachChild(ring);
		this.flare = flare;
		this.ring = ring;
		updateGeometricState(0, true);
		updateRenderState();
		this.age = age;
		this.lifetime = lifetime;
		this.pool = pool;
		this.worldNode = worldNode;
	}
	
	public void fire(Node from) {
		worldNode.attachChild(this);
		getLocalTranslation().set(from.getWorldTranslation());
		getLocalRotation().set(from.getWorldRotation());
		
//		FIXME UNUPDATE?
		//updateWorldData(0);
		//updateGeometricState(0, true);
	}
	
	public void update(float time) {
		age += time;

		//Work out where we are through cycle
		float progress = 1 - age / lifetime;
		
		progress = 1-4*(progress-0.5f)*(progress-0.5f);
		
		//Spin and scale the nodes
		flare.setLocalScale(progress * FLARE_SIZE);
		ring.setLocalScale(progress * RING_SIZE);
		
		NodeRotator.rotate(flare, 0, time * flareRotateSpeed);
		NodeRotator.rotate(ring, 0, time * ringRotateSpeed);
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
	public float getLifetime() {
		return lifetime;
	}
	public void setLifetime(float lifetime) {
		this.lifetime = lifetime;
	}
	public ReusablePool<HitEffect> getPool() {
		return pool;
	}
	public void setPool(ReusablePool<HitEffect> pool) {
		this.pool = pool;
	}
	public Node getWorldNode() {
		return worldNode;
	}
	public void setWorldNode(Node worldNode) {
		this.worldNode = worldNode;
	}
}
