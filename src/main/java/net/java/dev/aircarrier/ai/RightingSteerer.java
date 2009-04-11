package net.java.dev.aircarrier.ai;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.controls.PlaneControls;

import com.jme.math.Vector3f;

/**
 * Will gently roll the plane to "right" it (make its vertical axis lie
 * in the plane containing its forward axis and the world vertical axis)
 * 
 * @author goki
 */
public class RightingSteerer implements PlaneControls {

	Acobject self;
	float roll = 0;
	float rollMultiplier;

	Vector3f upAxis = new Vector3f(0, 1, 0);
	
	public RightingSteerer(Acobject self) {
		this(self, 4f);
	}

	public RightingSteerer(Acobject self, float rollMultiplier) {
		super();
		this.self = self;
		this.rollMultiplier = rollMultiplier;
	}
	
	public void update(float time) {
		//Set the roll
		roll = AIUtilities.rollTowardsUp(self, upAxis, rollMultiplier);
	}
	
	public void clearFiring() {
	}

	public float getAxis(int axis) {
		if (axis == PlaneControls.ROLL) {
			return roll;
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
