/*
 *  $Id: PlaneAssembly.java,v 1.7 2007/04/28 23:14:53 shingoki Exp $
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

import net.java.dev.aircarrier.input.action.NodeRotator;
import net.java.dev.aircarrier.scene.ApproximatelySphericalNode;

import com.jme.scene.Node;

public class PlaneAssembly {

	Node base;
	Node cameraTarget;
	PlaneNode plane;
	Node avoidanceSphere;
	
	float planeRoll = 0;
	
	public PlaneAssembly(String name, PlaneNode plane) {
		super();
		
		base = new Node(name + "Base"); 
		this.plane = plane;
		base.attachChild(plane);

		cameraTarget = new Node(name + "CameraTarget"); 
		base.attachChild(cameraTarget);
		//cameraTarget.setLocalTranslation(new Vector3f(0, 6, -10));
		//cameraTarget.setLocalTranslation(new Vector3f(0, 4, -2));
		cameraTarget.getLocalTranslation().set(plane.getCameraOffset());
		
		base.updateWorldData(0);
		
	}

	public Node getBase() {
		return base;
	}

	public Node getCameraTarget() {
		return cameraTarget;
	}

	public PlaneNode getPlane() {
		return plane;
	}

	public float getPlaneRoll() {
		return planeRoll;
	}

	public void setPlaneRoll(float planeRoll) {
		this.planeRoll = planeRoll;
		plane.getLocalRotation().set(0,0,0,1);
		NodeRotator.rotate(plane, 2, planeRoll);
	}

	/**
	 * @return
	 * 		Avoidance sphere (if set) or null otherwise
	 * 		This marks the approximate size and extent of the plane,
	 * 		e.g. for steerers wishing to avoid the plane
	 */
	public ApproximatelySphericalNode getAvoidanceSphere() {
		return plane.getAvoidanceNode();
	}

}
