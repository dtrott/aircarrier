package net.java.dev.aircarrier.ai;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.controls.PlaneControls;

import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

/**
 * Will steer in each axis so as to align the plane
 * to a given Node
 * 
 * @author goki
 */
public class RotationSeekSteerer implements PlaneControls {

	Acobject self;
	
	float rollMultiplier;

	Quaternion desiredRotation;
	Spatial desiredSpatial;
	Acobject desiredAcobject;
	
	float roll = 0;
	Vector2f steer = new Vector2f();
	
	Vector3f zAxis = new Vector3f();
	Vector3f yAxis = new Vector3f();
	
	public RotationSeekSteerer(Acobject self) {
		this(self, 4f);
	}

	public RotationSeekSteerer(Acobject self, float rollMultiplier) {
		super();
		this.self = self;
		this.rollMultiplier = rollMultiplier;
	}
	
	public void update(float time) {
		//In order of preference, use desired rotation, then
		//rotation of acobject, then rotation of spatial.
		//The first non-null object in that order will give the
		//rotation we aim for
		Quaternion rotation = desiredRotation;
		if ((rotation == null) && (desiredAcobject != null)) {
			rotation = desiredAcobject.getRotation();
		}
		if ((rotation == null) && (desiredSpatial != null)) {
			rotation = desiredSpatial.getWorldRotation();
		}
		
		//If we've got nothing, do nothing
		if (rotation == null) return;
		
		//Get target z and y axes
		rotation.getRotationColumn(2, zAxis);
		rotation.getRotationColumn(1, yAxis);
		
		//Work out steering (pitch and yaw) to point our z axis along target z axis
		AIUtilities.steeringInDirection(self, zAxis, steer);
		
		//Work out roll to point our y axis along target y axis
		roll = AIUtilities.rollTowardsUp(self, yAxis, rollMultiplier);
		
	}
	
	public void clearFiring() {
	}

	public float getAxis(int axis) {
		if (axis == PlaneControls.ROLL) {
			return roll;
		} else if (axis == PlaneControls.YAW) {
			return steer.x;
		} else if (axis == PlaneControls.PITCH) {
			return -steer.y;
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

	/**
	 * @return
	 * 		Desired rotation or null if not set
	 * 		Note that setting one of either the desired rotation or 
	 * 		desired spatial will clear (null) the other.
	 */
	public Quaternion getDesiredRotation() {
		return desiredRotation;
	}

	/**
	 * @param desiredRotation
	 * 		Set desired rotation, or null to clear
	 * 		Note that setting any of the "desired" targets
	 * 		(rotation, spatial and Acobject) will clear the others.
	 */
	public void setDesiredRotation(Quaternion desiredRotation) {
		clearTargets();
		this.desiredRotation = desiredRotation;
	}

	/**
	 * @return
	 * 		Spatial having desired rotation or null if not set
	 */
	public Spatial getDesiredSpatial() {
		return desiredSpatial;
	}

	/**
	 * @param desiredRotation
	 * 		Set spatial having desired rotation, or null to clear
	 * 		Note that setting any of the "desired" targets
	 * 		(rotation, spatial and Acobject) will clear the others.
	 */
	public void setDesiredSpatial(Spatial desiredSpatial) {
		clearTargets();
		this.desiredSpatial = desiredSpatial;
	}

	/**
	 * @return
	 * 		Acobject having desired rotation, or null to clear
	 */
	public Acobject getDesiredAcobject() {
		return desiredAcobject;
	}

	/**
	 * @param desiredAcobject
	 * 		Acobject having desired rotation, or null to clear
	 * 		Note that setting any of the "desired" targets
	 * 		(rotation, spatial and Acobject) will clear the others.
	 */
	public void setDesiredAcobject(Acobject desiredAcobject) {
		clearTargets();
		this.desiredAcobject = desiredAcobject;
	}
	
	/**
	 * Clear all "desired" targets - steerer will stop steering,
	 * until a non-null desired rotation, spatial or Acobject is set 
	 */
	public void clearTargets() {
		desiredAcobject = null;
		desiredSpatial = null;
		desiredRotation = null;
	}
}
