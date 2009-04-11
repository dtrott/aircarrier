/*
 *  $Id: OldPlaneController.java,v 1.1 2007/05/08 23:15:44 shingoki Exp $
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

import com.jme.math.Vector3f;

import net.java.dev.aircarrier.controls.PlaneControls;
import net.java.dev.aircarrier.input.action.NodeRotator;
import net.java.dev.aircarrier.util.FloatSpring;

public class OldPlaneController {

	private final static float QUARTER_TURN = (float)Math.PI/2.5f;
		
    //temporary vector for the rotation
    private static final Vector3f tempVa = new Vector3f();

    PlaneNode plane;
    
	PlaneAssembly assembly;

	float[] axisRates;
	
	FloatSpring cosmeticRollSpring = new FloatSpring(30);
		
	float midSpeed;
	float maxSpeed;
	float minSpeed;
	
	Vector3f velocity = new Vector3f();
	
	PlaneControls controls;
	
	float currentSpeed;

	FloatSpring accelerationSpring;
		
	public OldPlaneController(PlaneAssembly assembly, PlaneControls controls) {
		this(assembly, controls, new float[]{-1.15f, -1.15f, 2.5f}, 60, 60); //40;
	}

	public OldPlaneController(PlaneAssembly assembly, PlaneControls controls, float[] axisRates, float speed, float accelerationK) {
		this(assembly, controls, axisRates, 0,//speed / 1.5f, 
				speed, speed * 1.5f, accelerationK);
	}
	
	public OldPlaneController(PlaneAssembly assembly, PlaneControls controls, float[] axisRates, float minSpeed, float midSpeed, float maxSpeed, float accelerationK) {
		super();
		this.plane = assembly.getPlane();
		this.assembly = assembly;
		this.axisRates = axisRates;
		this.midSpeed = midSpeed;
		this.maxSpeed = maxSpeed;
		this.minSpeed = minSpeed;
		this.controls = controls;
		accelerationSpring = new FloatSpring(accelerationK);
		
		currentSpeed = midSpeed;
	}

	public PlaneAssembly getAssembly() {
		return assembly;
	}
	
	public void update(float time) {
				
		//Make the actual rotations based on pending axis controls, rates and elapsed time
		for (int i = 0; i < 3; i++) {
			NodeRotator.rotate(assembly.getBase(), i, controls.getAxis(i) * axisRates[i] * time);
		}
		
		//Make the cosmetic automatic roll, by "springing" the roll
		//of the assembly from its current value, to a range of -90 to 90 for
		//pending yaw axis control from -1 to 1
		updateCosmeticRoll(controls.getAxis(PlaneControls.YAW), time);
		
		Vector3f loc = assembly.getBase().getLocalTranslation();
		
		float desiredSpeed;
		float throttle = controls.getAxis(PlaneControls.THROTTLE);
		if (throttle > 0) {
			desiredSpeed = throttle * maxSpeed + (1 - throttle) * midSpeed; 
		} else {
			desiredSpeed = -throttle * minSpeed + (1 + throttle) * midSpeed;
		}

		accelerationSpring.update(desiredSpeed, time);
		currentSpeed = accelerationSpring.getPosition();
		
		velocity.set(assembly.getBase().getLocalRotation().getRotationColumn(2, tempVa)
                .multLocal(currentSpeed));
		
        loc.addLocal(tempVa.multLocal(time));
        
        assembly.getBase().setLocalTranslation(loc);
		

		for (int gun = 0; gun < controls.gunCount(); gun++) {
			if (controls.isFiring(gun)) {
				plane.fire(gun);
			}
		}
		
		controls.clearFiring();
	}
	
	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
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
		assembly.setPlaneRoll(cosmeticRollSpring.getPosition());
	}

	public PlaneControls getControls() {
		return controls;
	}

	public void setControls(PlaneControls controls) {
		this.controls = controls;
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float getMidSpeed() {
		return midSpeed;
	}

	public void setMidSpeed(float midSpeed) {
		this.midSpeed = midSpeed;
	}

	public float getMinSpeed() {
		return minSpeed;
	}

	public void setMinSpeed(float minSpeed) {
		this.minSpeed = minSpeed;
	}

	public float getCurrentSpeed() {
		return currentSpeed;
	}
	
	
}
