/*
 *  $Id: DebrisController.java,v 1.2 2007/01/25 23:33:17 shingoki Exp $
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

import java.util.ArrayList;
import java.util.List;

import net.java.dev.aircarrier.bullets.ShootableNode;
import net.java.dev.aircarrier.input.action.NodeRotator;

import com.jme.intersection.BoundingPickResults;
import com.jme.intersection.PickData;
import com.jme.intersection.PickResults;
import com.jme.math.FastMath;
import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Node;

public class DebrisController extends Controller {
	private static final long serialVersionUID = 1942874346479142959L;

	List<ShootableNode> shootNodes = new ArrayList<ShootableNode>();
	
	PickResults results;
	
	CarrierTerrainPage terrain = null;

	ShootableNode ignoreShootNode = null;
	
	Debris debris;
	
	Vector3f velocity = new Vector3f();
	Vector3f movement = new Vector3f();
	Vector3f spinAxis = new Vector3f();
	float spinRate = 0f;
	
	Ray ray = new Ray();
	
	
	float velocityMax = 30;			
	float angularVelocityMax = 12;
	float gravity = -40;

	/*
	float velocityMax = 0;			
	float angularVelocityMax = 0;
	float gravity = 0;
	*/
	boolean active = false;
	
	float lifespan = 8f;
	float age = 0;
	
	public DebrisController(Debris debris) {
		super();
		this.debris = debris;
		results = new BoundingPickResults();
		results.setCheckDistance(true);
		
		//start with debris and ourselves deactivated
		die();
	}

	public List<ShootableNode> getShootNodes() {
		return shootNodes;
	}

	public void setShootNodes(List<ShootableNode> shootNodes) {
		this.shootNodes = shootNodes;
	}
		
	public ShootableNode getIgnoreShootNode() {
		return ignoreShootNode;
	}

	public void setIgnoreShootNode(ShootableNode ignoreShootNode) {
		this.ignoreShootNode = ignoreShootNode;
	}

	public CarrierTerrainPage getTerrain() {
		return terrain;
	}

	public void setTerrain(CarrierTerrainPage terrain) {
		this.terrain = terrain;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}

	public void respawn(Node emitter, Vector3f emitVelocity) {
		
		//Set active
		active = true;
		
		//reset age
		age = 0;
		
		//Restart the debris
		debris.setActive(true);
		
		//Set initial position
		debris.getLocalTranslation().set(emitter.getWorldTranslation());
		debris.getLocalRotation().set(emitter.getWorldRotation());
		debris.getLocalScale().set(emitter.getWorldScale());
		
		//Set a random initial velocity and spin
		velocity.set(
				FastMath.rand.nextFloat() * velocityMax*2 - velocityMax, 
				FastMath.rand.nextFloat() * velocityMax*2 - velocityMax, 
				FastMath.rand.nextFloat() * velocityMax*2 - velocityMax);
		velocity.addLocal(emitVelocity);
		
		//Make a normalised vector, always in the upper hemisphere.
		//since spin may be positive or negative, this hopefully allows
		//for any spin direction/angle
		spinAxis.set(
				FastMath.rand.nextFloat()-0.5f, 
				FastMath.rand.nextFloat()+0.01f, 
				FastMath.rand.nextFloat()-0.5f);
		spinAxis.normalizeLocal();
		spinRate = FastMath.rand.nextFloat() * angularVelocityMax*2 - angularVelocityMax;
						
	}
	
	public void die() {
		//Deactivate
		active = false;
		debris.setActive(false);
	}
	
	public void update(float time) {

		//Do nothing if inactive
		if (!active) return;
		
		//age
		age += time;
		
		//Update velocity
		velocity.addLocal(0, gravity * time, 0);
		
		boolean remove = false;

		if (terrain != null) {
			float y = terrain.getHeight(debris.getWorldTranslation());
			if (debris.getWorldTranslation().y < y) {
				remove = true;
			}
		}
		
		//Move the debris by its speed, updating the movement ray as we
		//do this.
		ray.getOrigin().set(debris.getWorldTranslation());
		
		//The vector we will move
		movement.set(velocity);
		movement.multLocal(time);
		
		//If movement is close to zero, ignore it
		if (movement.lengthSquared() < 0.0001) return;
		
		//Do the movement and rotation
		debris.getLocalTranslation().addLocal(movement);
		NodeRotator.rotate(debris, spinAxis, spinRate * time);
		
		//Set ray direction to normalised movement
		ray.getDirection().set(movement);
		ray.getDirection().normalizeLocal();
		
		//Find collisions
		for (ShootableNode n : shootNodes) {
			if (n != ignoreShootNode) {
				results.clear();
				n.calculatePick(ray, results);
				if (results.getNumber() > 0) {
					float lSquared = ray.getDirection().lengthSquared();
					for (int j = 0; j < results.getNumber(); j++) {
						PickData pData = results.getPickData(j);
						if (pData.getDistance()*pData.getDistance() < lSquared) {
							remove = true;
						}
					}
				}
			}
		}
		
		//If we hit anything or died of old age, kill the debris
		//Only do this if it has been alive for 0.1seconds (at least one update)
		if ((remove || age > lifespan) && age > 0.1f) {
			die();
		}
	}
	
}
