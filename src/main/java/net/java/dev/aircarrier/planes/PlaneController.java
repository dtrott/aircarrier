/*
 *  $Id: PlaneController.java,v 1.24 2007/06/28 21:52:09 shingoki Exp $
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

import net.java.dev.aircarrier.controls.PlaneControls;
import net.java.dev.aircarrier.controls.SlidingThrottleController;
import net.java.dev.aircarrier.controls.SteeringController;
import net.java.dev.aircarrier.util.FloatSpring;

public class PlaneController {

	private final static float QUARTER_TURN = (float)Math.PI/2.5f;
		
	Plane plane;

	FloatSpring cosmeticRollSpring = new FloatSpring(30);

	PlaneControls controls;
	
	SteeringController steeringController;
	SlidingThrottleController throttleController;
	
	public PlaneController(Plane plane, PlaneControls controls) {
		this(plane, controls, new float[]{-1.15f, -1.15f, 2.5f},
				90, 90);			//A little fast?
				//70, 70);			//not sure
				//60, 60); //40;	//ORIGINAL
	}

	public PlaneController(Plane plane, PlaneControls controls, float[] axisRates, float speed, float accelerationK) {
		this(plane, controls, axisRates, 
				speed / 1.5f, 
				speed, 
				speed * 1.5f, accelerationK);
	}
	
	public PlaneController(Plane plane, PlaneControls controls, float[] axisRates, float minSpeed, float midSpeed, float maxSpeed, float accelerationK) {
		super();
		this.plane = plane;
		this.controls = controls;
		
		steeringController = new SteeringController(plane, controls, axisRates);

		/*
		throttleController = new SteeringThrottleController(
				plane, 
				controls, 
				minSpeed, midSpeed, maxSpeed, 
				new FloatSpring(accelerationK));
		 */
		
		throttleController = new SlidingThrottleController(
				plane, 
				controls, 
				minSpeed, midSpeed, maxSpeed, 
				new FloatSpring(accelerationK),
				//0.000001f);
				0.001f);

	}

	public void update(float time) {

		steeringController.update(time);
		
		//Make the cosmetic automatic roll, by "springing" the roll
		//of the assembly from its current value, to a range of -90 to 90 for
		//pending yaw axis control from -1 to 1
		updateCosmeticRoll(controls.getAxis(PlaneControls.YAW), time);
		
		throttleController.update(time);

		for (int gun = 0; gun < controls.gunCount(); gun++) {
			if (controls.isFiring(gun)) {
				plane.getPlaneNode().fire(gun);
			}
		}
		
		controls.clearFiring();
	}
	
	/**
	 * Update the cosmetic roll of the plane node about the base
	 * @param yaw
	 * 		The current yaw control, to produce cosmetic roll
	 * @param time
	 * 		The time elapsed, to determine progress of cosmetic roll
	 */
	private void updateCosmeticRoll(float yaw, float time) {
		cosmeticRollSpring.update(yaw * QUARTER_TURN, time);
		plane.getPlaneAssembly().setPlaneRoll(cosmeticRollSpring.getPosition());
	}

	public PlaneControls getControls() {
		return controls;
	}

	public void setControls(PlaneControls controls) {
		this.controls = controls;
		steeringController.setControls(controls);
		throttleController.setControls(controls);
	}

	public float getCurrentSpeed() {
		return throttleController.getCurrentSpeed();
	}

}
