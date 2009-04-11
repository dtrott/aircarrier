/*
 *  $Id: MaximisingPlaneControls.java,v 1.2 2006/12/23 22:29:22 shingoki Exp $
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
 * A set of controls which tracks the maximum value it sees on each axis,
 * and passes these through to the delegate controller when updated
 * @author goki
 */
public class MaximisingPlaneControls implements PlaneControls {

	PlaneControls delegate;
	
	float[] pendingAxis = new float[]{0, 0, 0, 0};
	
	public MaximisingPlaneControls(PlaneControls delegate) {
		super();
		this.delegate = delegate;
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.planes.PlaneControls#setAxis(int, float)
	 */
	public void setAxis(int axis, float control) {
		if (Math.abs(control) > Math.abs(pendingAxis[axis])) {
			pendingAxis[axis] = control;
			if (pendingAxis[axis] > 1) pendingAxis[axis] = 1;
			if (pendingAxis[axis] < -1) pendingAxis[axis] = -1;
		}
	}

	public void update(float time) {
		for (int i = 0; i < pendingAxis.length; i++) {
			delegate.setAxis(i, pendingAxis[i]);
			pendingAxis[i] = 0;
		}
	}

	public float getAxis(int axis) {
		return delegate.getAxis(axis);
	}
	public boolean isFiring(int gun) {
		return delegate.isFiring(gun);
	}
	public void moveAxis(int axis, float control) {
		delegate.moveAxis(axis, control);
	}
	public void setFiring(int gun, boolean firing) {
		delegate.setFiring(gun, firing);
	}
	public void clearFiring() {
		delegate.clearFiring();
	}
	public int gunCount() {
		return delegate.gunCount();
	}

}
