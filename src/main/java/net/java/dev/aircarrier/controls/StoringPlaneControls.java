/*
 *  $Id: StoringPlaneControls.java,v 1.2 2006/12/23 22:29:22 shingoki Exp $
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

package net.java.dev.aircarrier.controls;

/**
 * Stores a set of controls. Intended to be delegated to
 * by a chain of controls, this actually provides the useful
 * ability actually to remember what is going on
 * @author goki
 */
public class StoringPlaneControls implements PlaneControls {

	float[] pendingAxis = new float[]{0, 0, 0, 0};
	boolean[] firePending;
	int gunCount;
	
	public StoringPlaneControls(int gunCount) {
		firePending = new boolean[gunCount];
		this.gunCount = gunCount;
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.planes.PlaneControls#setAxis(int, float)
	 */
	public void setAxis(int axis, float control) {
		pendingAxis[axis] = control;
		if (pendingAxis[axis] > 1) pendingAxis[axis] = 1;
		if (pendingAxis[axis] < -1) pendingAxis[axis] = -1;
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.planes.PlaneControls#getAxis(int)
	 */
	public float getAxis(int axis) {
		float toReturn = pendingAxis[axis];
		return toReturn;
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.planes.PlaneControls#moveAxis(int, float)
	 */
	public void moveAxis(int axis, float control) {
		pendingAxis[axis] += control;
		if (pendingAxis[axis] > 1) pendingAxis[axis] = 1;
		if (pendingAxis[axis] < -1) pendingAxis[axis] = -1;
	}
	
	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.planes.PlaneControls#fire(int)
	 */
	public void setFiring(int gun, boolean firing) {
		firePending[gun] = true;
	}
	
	public boolean isFiring(int gun) {
		return firePending[gun];
	}
	
	public void update(float time) {
	}

	public void clearFiring() {
		for (int i = 0; i < firePending.length; i++) {
			firePending[i] = false;
		}
	}
	public int gunCount() {
		return gunCount;
	}
}
