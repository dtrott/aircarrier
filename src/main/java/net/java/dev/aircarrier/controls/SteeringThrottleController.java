package net.java.dev.aircarrier.controls;

import net.java.dev.aircarrier.acobject.MovableAcobject;
import net.java.dev.aircarrier.util.FloatSpring;

import com.jme.math.Vector3f;
import com.jme.scene.Controller;

/**
 * Controls movement of a spatial along its z axis according to a throttle
 * setting in a set of SteeringControls.
 * @author shingoki
 */
public class SteeringThrottleController extends Controller {
	private static final long serialVersionUID = -2318389325022416457L;

	MovableAcobject s;

	SteeringControls controls;

	float minSpeed;
	float midSpeed;
	float maxSpeed;
	
	Vector3f velocity = new Vector3f();
	
	float currentSpeed;

	FloatSpring accelerationSpring;

	public SteeringThrottleController(
			MovableAcobject s, 
			SteeringControls controls, 
			float minSpeed, float midSpeed, float maxSpeed, 
			FloatSpring accelerationSpring) {
		super();
		this.s = s;
		this.controls = controls;
		this.midSpeed = midSpeed;
		this.maxSpeed = maxSpeed;
		this.minSpeed = minSpeed;
		this.accelerationSpring = accelerationSpring;
		
		//Initialise speed spring to desired velocity, no spring velocity (spatial acceleration)
		accelerationSpring.setVelocity(0);
		accelerationSpring.setPosition(desiredSpeedForThrottle());
		
		//Current speed is desired speed
		currentSpeed = desiredSpeedForThrottle();
	}

	@Override
	public void update(float time) {
		accelerationSpring.update(desiredSpeedForThrottle(), time);
		currentSpeed = accelerationSpring.getPosition();
		s.moveLocal(2, currentSpeed * time);
	}

	/**
	 * @return
	 * 		The desired speed for the current throttle setting
	 */
	public float desiredSpeedForThrottle() {
		float desiredSpeed;
		float throttle = controls.getAxis(PlaneControls.THROTTLE);
		if (throttle > 0) {
			desiredSpeed = throttle * maxSpeed + (1 - throttle) * midSpeed; 
		} else {
			desiredSpeed = -throttle * minSpeed + (1 + throttle) * midSpeed;
		}
		return desiredSpeed;
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

	/**
	 * @return	Current velocity according to this throttle controller
	 */
	/*public Vector3f getVelocity() {
		return velocity;
	}*/

	public SteeringControls getControls() {
		return controls;
	}

	public void setControls(SteeringControls controls) {
		this.controls = controls;
	}
	
}
