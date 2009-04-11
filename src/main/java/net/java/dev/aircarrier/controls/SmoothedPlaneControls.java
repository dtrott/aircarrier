/*
 *  $Id: SmoothedPlaneControls.java,v 1.2 2007/08/12 10:03:05 shingoki Exp $
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

import net.java.dev.aircarrier.ai.ScalarSensor;
import net.java.dev.aircarrier.util.FloatDecay;

/**
 * Smooths an underlying set of controls using a decay function
 * Also passes through the controls' sensor, if they have one
 * @author goki
 */
public class SmoothedPlaneControls implements PlaneControlsWithSensor {

	PlaneControls controls;
	ScalarSensor sensor = null;
	FloatDecay[] decay;
	
	/**
	 * Create new controls
	 * @param controls
	 * 		The underlying controls, to be smoothed
	 * @param halflife
	 * 		The halflife of the smoothing
	 */
	public SmoothedPlaneControls(PlaneControls controls, float halflife) {
		this.controls = controls;
		decay = new FloatDecay[4];
		for (int i = 0; i < decay.length; i++) {
			decay[i] = new FloatDecay(halflife);
			decay[i].setPosition(controls.getAxis(i));
		}
		
		if (controls instanceof ScalarSensor) {
			sensor = (ScalarSensor)controls;
		}
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.planes.PlaneControls#getAxis(int)
	 */
	public float getAxis(int axis) {
		return decay[axis].getPosition();
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.planes.PlaneControls#fire(int)
	 */
	public void setFiring(int gun, boolean firing) {
		controls.setFiring(gun, firing);
	}
	
	public boolean isFiring(int gun) {
		return controls.isFiring(gun);
	}
	
	public void update(float time) {
		//Update our stored axes using decay
		for (int i = 0; i < decay.length; i++) {
			decay[i].update(controls.getAxis(i), time);
		}
	}

	public void clearFiring() {
		controls.clearFiring();
	}
	
	public int gunCount() {
		return controls.gunCount();
	}

	public void moveAxis(int axis, float control) {
		controls.moveAxis(axis, control);
	}

	public void setAxis(int axis, float control) {
		controls.setAxis(axis, control);
	}

	public float getIntensity() {
		if (sensor == null) {
			return 0;
		} else {
			return sensor.getIntensity();
		}
	}
}
