package net.java.dev.aircarrier.ai;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.controls.PlaneControls;
import net.java.dev.aircarrier.input.action.NodeTranslator;

import com.jme.math.Vector3f;

/**
 * This steerer will alter throttle control to try to maintain distance
 * from a target
 * @author goki
 */
public class DistanceMaintenanceSteerer implements PlaneControls {

	Acobject self;
	
	Acobject target = null;

	static Vector3f forwardAxis = new Vector3f();
	static Vector3f toTarget = new Vector3f();

	float throttle = 0;
	
	float desiredDistance;
	float distanceTolerance;
	
	float minThrottle;
	float maxThrottle;
	
	public DistanceMaintenanceSteerer(Acobject self, float desiredDistance) {
		this(self, desiredDistance, desiredDistance / 4f, -1, 0);
	}

	public DistanceMaintenanceSteerer(
			Acobject self, 
			float desiredDistance, float distanceTolerance, 
			float minThrottle, float maxThrottle) {
		super();
		this.self = self;
		this.desiredDistance = desiredDistance;
		this.distanceTolerance = distanceTolerance;
		this.minThrottle = minThrottle;
		this.maxThrottle = maxThrottle;
	}
	
	public void update(float time) {
		
		if (target != null) {
			
			//Find vector from self to target
			toTarget.set(target.getPosition());
			toTarget.subtractLocal(self.getPosition());
			
			//find distance to target
			float distance = toTarget.length();
			
			//Work out throttle - if we are out by mroe than tolerance above or below
			//desired distance, then use full throttle (-1 or 1 depending on which way we are)
			//If we are within tolerance, then scale from -1 to 1 depending on how close we are 
			//(0 for distance == desired distance)
			if (distance > desiredDistance + distanceTolerance) {
				throttle = 1;
			} else if (distance > desiredDistance - distanceTolerance) {
				throttle = (distance - desiredDistance) / distanceTolerance;
			} else {
				throttle = -1;
			}
			
			//Is target in front?
			NodeTranslator.makeTranslationVector(self.getRotation(), 2, 1, forwardAxis);
			float dot = forwardAxis.dot(toTarget);
						
			//If target is behind, invert throttle since if dist is too small, we
			//need to accelerate away instead of braking, etc.
			if (dot < 0) throttle = -throttle;
			
			//Constrain throttle
			if (throttle > maxThrottle) {
				throttle = maxThrottle;
			}			
			if (throttle < minThrottle) {
				throttle = minThrottle;
			}

		//No target - zero throttle 
		} else {
			throttle = 0;
		}

		
	}

	
	
	public Acobject getTarget() {
		return target;
	}

	public void setTarget(Acobject target) {
		this.target = target;
	}

	public void clearFiring() {
	}

	public float getAxis(int axis) {
		if (axis == PlaneControls.THROTTLE) {
			return throttle;
		} else {
			return 0;
		}
	}

	public boolean isFiring(int gun) {
		return false;
	}

	public void moveAxis(int axis, float control) {
	}

	public void setAxis(int axis, float control) {
	}

	public void setFiring(int gun, boolean firing) {
	}
	public int gunCount() {
		return 0;
	}

}
