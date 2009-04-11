package net.java.dev.aircarrier.controls;

import net.java.dev.aircarrier.acobject.MovableAcobject;
import net.java.dev.aircarrier.input.action.NodeTranslator;
import net.java.dev.aircarrier.planes.PlaneSpecs;
import net.java.dev.aircarrier.util.FloatDecay3f;
import net.java.dev.aircarrier.util.FloatSpring;

import com.jme.math.Vector3f;
import com.jme.scene.Controller;

/**
 * Controls movement of a spatial along its z axis according to a throttle
 * setting in a set of SteeringControls.
 * @author shingoki
 */
public class PlaneSpecsThrottleController extends Controller {
	private static final long serialVersionUID = -2318389325022416457L;

	MovableAcobject s;

	SteeringControls controls;

	PlaneSpecs specs;
	
	Vector3f move = new Vector3f();
	Vector3f desiredVelocity = new Vector3f();
	
	float currentSpeed;

	FloatDecay3f velocityDecay;
	
	FloatSpring accelerationSpring;

	public PlaneSpecsThrottleController(
			MovableAcobject s, 
			SteeringControls controls, 
			PlaneSpecs specs) {
		super();
		this.s = s;
		this.controls = controls;
		this.specs = specs;
		
		this.accelerationSpring = new FloatSpring(specs.getThrottleSpringK(), specs.getThrottleDampingK());
		
		//Initialise speed spring to desired velocity, no spring velocity (spatial acceleration)
		accelerationSpring.setVelocity(0);
		accelerationSpring.setPosition(desiredSpeedForThrottle());
		
		//Make velocity decay
		velocityDecay = new FloatDecay3f(specs.getSlideHalfLife());
		
		//Current speed is desired speed
		currentSpeed = desiredSpeedForThrottle();
	}

	@Override
	public void update(float time) {
		
		//Update spring and decay
		accelerationSpring.setSpringK(specs.getThrottleSpringK());
		accelerationSpring.setDampingK(specs.getThrottleDampingK());
		velocityDecay.setHalfLife(specs.getSlideHalfLife());
		
		//Update for current speed
		accelerationSpring.update(desiredSpeedForThrottle(), time);
		currentSpeed = accelerationSpring.getPosition();
		
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
			desiredSpeed = throttle * specs.getMaxSpeed() + (1 - throttle) * specs.getMidSpeed(); 
		} else {
			desiredSpeed = -throttle * specs.getMinSpeed() + (1 + throttle) * specs.getMidSpeed();
		}
		return desiredSpeed;
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

	
}
