package net.java.dev.aircarrier.ai;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.controls.PlaneControls;

import com.jme.math.Vector2f;

/**
 * Steerer just aims towards a target Spatial.
 * 
 * @author goki
 */
public class TargetSeekingSteerer implements PlaneControls {

	Acobject self;
	
	Acobject target = null;
	
	Vector2f steer = new Vector2f();
	float steerMultiplier;

	public TargetSeekingSteerer(Acobject self) {
		this(self, 8f);
	}

	public TargetSeekingSteerer(Acobject self, float steerMultiplier) {
		super();
		this.self = self;
		this.steerMultiplier = steerMultiplier;
	}
	
	public void update(float time) {
		
		if (target != null) {
			AIUtilities.steeringTowards(
					self,
					target.getPosition(), 
					steer);

			steer.multLocal(steerMultiplier);
			if (steer.length() > 1) {
				steer.normalizeLocal();
			}
		//No target - zero steering 
		} else {
			steer.x = 0;
			steer.y = 0;
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
		if (axis == PlaneControls.YAW) {
			return -steer.x;
		} else if (axis == PlaneControls.PITCH) {
			return steer.y;
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

	public Acobject getSelf() {
		return self;
	}

}
