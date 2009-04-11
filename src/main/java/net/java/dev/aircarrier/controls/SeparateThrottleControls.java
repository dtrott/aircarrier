package net.java.dev.aircarrier.controls;

import net.java.dev.aircarrier.controls.PlaneControls;

/**
 * This will wrap an existing pair of controls, and get throttle from one,
 * all other controls from the other 
 * @author goki
 */
public class SeparateThrottleControls implements PlaneControls{

	PlaneControls throttleControls;
	PlaneControls mainControls;

	/**
	 * Create controls
	 * @param throttleControls
	 * 		Controls used for throttle
	 * @param mainControls
	 * 		Controls used for everything else
	 */
	public SeparateThrottleControls(
			PlaneControls throttleControls, 
			PlaneControls mainControls) {
		super();
		this.throttleControls = throttleControls;
		this.mainControls = mainControls;
	}

	public void update(float time) {
		//The controls each update themselves
	}

	public float getAxis(int axis) {
		if (axis == PlaneControls.THROTTLE) {
			return throttleControls.getAxis(axis);
		} else {
			return mainControls.getAxis(axis);
		}
	}

	public void clearFiring() {
		mainControls.clearFiring();
	}
	public int gunCount() {
		return mainControls.gunCount();
	}
	public boolean isFiring(int gun) {
		return mainControls.isFiring(gun);
	}
	public void moveAxis(int axis, float control) {
		mainControls.moveAxis(axis, control);
	}
	public void setAxis(int axis, float control) {
		mainControls.setAxis(axis, control);
	}
	public void setFiring(int gun, boolean firing) {
		mainControls.setFiring(gun, firing);
	}
	
}
