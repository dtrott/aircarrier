package net.java.dev.aircarrier.controls;

import net.java.dev.aircarrier.acobject.MovableAcobject;
import net.java.dev.aircarrier.input.action.NodeTranslator;
import net.java.dev.aircarrier.util.FloatDecay3f;
import net.java.dev.aircarrier.util.FloatSpring;

import com.jme.math.Vector3f;
import com.jme.scene.Controller;

/**
 * Controls movement of a spatial along its z axis according to a throttle
 * setting in a set of SteeringControls.
 * @author shingoki
 */
public class SlidingThrottleController extends Controller {
	private static final long serialVersionUID = -2318389325022416457L;

	MovableAcobject s;

	SteeringControls controls;

	float minSpeed;
	float midSpeed;
	float maxSpeed;
	
	Vector3f move = new Vector3f();
	Vector3f desiredVelocity = new Vector3f();
	
	float currentSpeed;

	FloatDecay3f velocityDecay;
	
	FloatSpring accelerationSpring;

	public SlidingThrottleController(
			MovableAcobject s, 
			SteeringControls controls, 
			float minSpeed, float midSpeed, float maxSpeed, 
			FloatSpring accelerationSpring,
			float velocityHalflife) {
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
		
		//Make velocity decay
		velocityDecay = new FloatDecay3f(velocityHalflife);
		
		//Current speed is desired speed
		currentSpeed = desiredSpeedForThrottle();
	}

	@Override
	public void update(float time) {
		
		//Update for current speed
		accelerationSpring.update(desiredSpeedForThrottle(), time);
		currentSpeed = accelerationSpring.getPosition();
		
		//s.moveLocal(2, currentSpeed * time);
		
		//Work out desired velocity, as target for decay
		NodeTranslator.makeTranslationVector(s.getRotation(), 2, currentSpeed, velocityDecay.getTarget());
		
		//Decay velocity towards desired
		velocityDecay.update(time);
		
		//Make the move
		move.set(velocityDecay.getPosition());
		move.multLocal(time);
		s.moveGlobal(move);
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
	public Vector3f getVelocity() {
		return velocityDecay.getPosition();
	}

	public SteeringControls getControls() {
		return controls;
	}

	public void setControls(SteeringControls controls) {
		this.controls = controls;
	}
	
}
