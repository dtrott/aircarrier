/*
 *  $Id: NodeTracker.java,v 1.4 2007/01/05 22:56:32 shingoki Exp $
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

import com.jme.math.Vector3f;
import com.jme.scene.Node;

/**
 * Make one node track another
 * @author shingoki
 */
public class NodeTracker {

	Vector3f velocity = new Vector3f();

	Node target;
	Node tracker;
	
	Vector3f v = new Vector3f();
	
	float springK;
	float dampingK;
	float resetDistanceSquared = 100;
	
	/**
	 * Create node tracker with spring constant and damping 
	 * @param target
	 * 		Target the tracking node should try to track
	 * @param tracker
	 * 		Node to be moved to track the target
	 * @param springK
	 * 		Spring constant of link between the target and
	 * 		tracker. Should be > 0
	 * @param dampingK
	 * 		Amount of damping - this is multiplied by velocity 
	 * 		to create a force opposing movement. Should be >= 0
	 */
	public NodeTracker(Node target, Node tracker, float springK, float dampingK) {
		super();
		this.target = target;
		this.tracker = tracker;
		this.springK = springK;
		this.dampingK = dampingK;
	}

	/**
	 * Create node tracker with spring constant, and damping calculated with
	 * dampingK = (2 * sqrt(springK))
	 * @param target
	 * 		Target the tracking node should try to track
	 * @param tracker
	 * 		Node to be moved to track the target
	 * @param springK
	 * 		Spring constant of link between the target and
	 * 		tracker
	 */
	public NodeTracker(Node target, Node tracker, float springK){
		this(target, tracker, springK, (float)(2 * Math.sqrt(springK)));
	}
	
	public void update(float time) {

		//Set v to target position - tracker position, this is the required movement
		//to get tracker to target
		tracker.getLocalTranslation().subtract(target.getWorldTranslation(), v);
		
		//If we are out of range, then just put the tracking node onto the tracked one,
		//and clear the speed
		if (false){//v.lengthSquared() > resetDistanceSquared) {
			//tracker.getLocalTranslation().addLocal(v);
			//tracker.setLocalTranslation(target.getWorldTranslation());
			velocity.zero();
			
		//We are in valid range, so apply springing
		} else {
			
			//Multiply displacement by spring constant to get spring force,
			//then subtract damping force
			v.multLocal(-springK).subtractLocal(velocity.x * dampingK,
	                velocity.y * dampingK, velocity.z * dampingK);
			
			//v is now a force, so assuming unit mass is is also acceleration.
			//multiply by elapsed time to get velocity change
			velocity.addLocal(v.multLocal(time));
			
			//If velocity isn't valid, zero it
			if (!Vector3f.isValidVector(velocity)) velocity.zero();
			
			//Move the tracker at the new velocity, for elapsed time
			tracker.getLocalTranslation().addLocal(
					velocity.x * time, 
					velocity.y * time, 
					velocity.z * time);
		}
		
		//Make tracker point same direction as target
		tracker.setLocalRotation(target.getWorldRotation());
	}
	
	public Node getTarget() {
		return target;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public Node getTracker() {
		return tracker;
	}

	public void setTracker(Node tracker) {
		this.tracker = tracker;
	}

}
