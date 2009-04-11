package net.java.dev.aircarrier.controls;

import net.java.dev.aircarrier.controls.PlaneControls;

/**
 * This will wrap an existing set of controls, normally passing through all methods
 * to these main controls. However, when the main controls are not being used (and the
 * most recent use of controls was NOT to set a roll angle) the roll axis from a
 * separate set of roll controls will be used. These roll controls would normally
 * be something like RightingSteering, to realign the plane to the horizon when
 * controls are left untouched.
 *   
 * This is designed to prevent irritation from the plane "controlling itself", while
 * still righting the plane when not much is goin on, for newer players.
 * 
 * @author goki
 */
public class RollFilterControls implements PlaneControls{

	PlaneControls rollControls;
	PlaneControls mainControls;

	float deadSize;	
	boolean rollLocked = false;
	boolean levelling = false;
	
	//Amount of roll levelling passed through
	float levellingAmount = 0f;
	
	//Rate at which levelling amount changes (per second)
	float levellingAmountRate;

	//Factor to multiply roll controls
	float rollFactor;
	
	//Time to require before starting to level at all
	float levellingTimeOut;
	
	//Time we have been waiting to level (counts towards tiemout)
	float levellingTime = 0f;

	/**
	 * Create controls
	 * @param rollControls
	 * 		Controls used for rolling when main controls are "quiet"
	 * @param mainControls
	 * 		Controls used for everything else
	 * @param deadSize
	 * 		Size (radius) of dead region, where controls are considered to be "quiet"
	 * @param levellingAmountRate
	 * 		The amount per second at which we move from 0 levelling input to 1 (full) levelling input (levelling is roll)
	 * @param rollFactor
	 * 		The amount of roll control used (less for slower levelling)
	 * @param levellingTimeOut
	 * 		The time required with "quiet" controls before any levelling is applied
	 */
	public RollFilterControls(
			PlaneControls rollControls, 
			PlaneControls mainControls, 
			float deadSize, 
			float levellingAmountRate, 
			float rollFactor, 
			float levellingTimeOut) {
		super();
		this.rollControls = rollControls;
		this.mainControls = mainControls;
		this.deadSize = deadSize;
		this.levellingAmountRate = levellingAmountRate;
		this.rollFactor = rollFactor;
		this.levellingTimeOut = levellingTimeOut;
	}

	/**
	 * Create controls with suitable settings for a player
	 * @param rollControls
	 * 		Controls used for rolling when main controls are "quiet"
	 * @param mainControls
	 * 		Controls used for everything else
	 */
	public RollFilterControls(PlaneControls rollControls, PlaneControls mainControls) {
		this(rollControls, mainControls, 0.01f, 2, 0.5f, 1f);
	}

	public void update(float time) {
		
		//Check for roll lock - if we are rolling then we set roll lock.
		//If we are steering then we break roll lock
		boolean steering = 
			Math.abs(mainControls.getAxis(PlaneControls.PITCH)) > deadSize ||
			Math.abs(mainControls.getAxis(PlaneControls.YAW)) > deadSize;

		boolean rolling = 
			Math.abs(mainControls.getAxis(PlaneControls.ROLL)) > deadSize;

		if (rolling) rollLocked = true;
		if (steering) rollLocked = false;
			
		//If we are not rollLocked, and not steering, then do automatic camera levelling
		levelling = (!steering && !rollLocked);
		
		//If we should level, increment levellingTime
		if (levelling) {
			levellingTime += time;
			
		//Reset levelling time whenever we should not be levelling - we require
		//uninterrupted levellign conditions for entore timeout
		} else {
			levellingTime = 0f;
		}
		
		//Work out if we have timed out
		if (levellingTime > levellingTimeOut) {
			//Cap time
			levellingTime = levellingTimeOut + 1;
		//otherwise we are not really levelling
		} else {
			levelling = false;
		}
		
		//If we are REALLY levelling after all checks, increase levellingAmount, otherwise decrease
		levellingAmount += (levelling ? 1 : -1) * levellingAmountRate * time;
		if (levellingAmount > 1) levellingAmount = 1;
		else if (levellingAmount < 0) levellingAmount = 0;
		
	}

	public float getAxis(int axis) {
		if (axis == PlaneControls.ROLL) {
			//return roll controls blended with main controls according to levellingAmount
			return levellingAmount * rollControls.getAxis(PlaneControls.ROLL) * rollFactor
					+ (1-levellingAmount) * mainControls.getAxis(PlaneControls.ROLL);
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
