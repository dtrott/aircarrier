package net.java.dev.aircarrier.ai;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.input.action.NodeTranslator;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

public class AIUtilities {

	/**
	 * Ways of "normalising" a vector
	 */
	public enum NormaliseMethod {
		/**
		 * Normalise to length 1 as a vector
		 */
		NORMALISE,
		
		/**
		 * Scale vector so that the component of greatest magnitude
		 * is taken to 1 or -1, depending on original sign.
		 */
		SCALE_TO_UNIT,
		
		/**
		 * Do not normalise
		 */
		NONE
	}
	
	static Vector3f xAxis = new Vector3f();
	static Vector3f yAxis = new Vector3f();
	static Vector3f zAxis = new Vector3f();
	static Vector3f aim = new Vector3f();
	static Vector3f idealXAxis = new Vector3f();
	
	/**
	 * Work out the steering vector from a hunter node to a prey node,
	 * for a hunter that uses yaw (x) and pitch (y) controls, relative to its own
	 * x and y axes. For example, a plane with normal controls.
	 * @param hunter
	 * 		The hunter Spatial, steering towards prey
	 * @param prey
	 * 		The target for the hunter node
	 * @param normalise
	 * 		The means of normalising the steering vector.
	 * @param steer
	 * @return
	 */
	public static Vector2f steeringTowards(Spatial hunter, Vector3f prey, Vector2f steer) {
	
		//Get the normalized vector from hunter to prey
		prey.subtract(hunter.getWorldTranslation(), aim);
		aim.normalizeLocal();
		
		//Steer in the direction of the prey
		return steeringInDirection(hunter, aim, steer);
	}
	
	/**
	 * Work out the steering vector from a hunter node to a prey node,
	 * for a hunter that uses yaw (x) and pitch (y) controls, relative to its own
	 * x and y axes. For example, a plane with normal controls.
	 * @param hunter
	 * 		The hunter Spatial, steering towards prey
	 * @param prey
	 * 		The target for the hunter node
	 * @param normalise
	 * 		The means of normalising the steering vector.
	 * @param steer
	 * @return
	 */
	public static Vector2f steeringTowards(Acobject hunter, Vector3f prey, Vector2f steer) {
	
		//Get the normalized vector from hunter to prey
		prey.subtract(hunter.getPosition(), aim);
		aim.normalizeLocal();
		
		//Steer in the direction of the prey
		return steeringInDirection(hunter.getPosition(), hunter.getRotation(), aim, steer);
	}
	
	/**
	 * Work out the steering vector from a hunter node to a prey node,
	 * for a hunter that uses yaw (x) and pitch (y) controls, relative to its own
	 * x and y axes. For example, a plane with normal controls.
	 * @param hunter
	 * 		The hunter Spatial, steering towards prey
	 * @param prey
	 * 		The target for the hunter node
	 * @param normalise
	 * 		The means of normalising the steering vector.
	 * @param steer
	 * @return
	 */
	public static Vector2f steeringTowards(Vector3f hunterPosition, Quaternion hunterRotation, Vector3f prey, Vector2f steer) {
	
		//Get the normalized vector from hunter to prey
		prey.subtract(hunterPosition, aim);
		aim.normalizeLocal();
		
		//Steer in the direction of the prey
		return steeringInDirection(hunterPosition, hunterRotation, aim, steer);
	}
	
	/**
	 * Work out the steering vector in a given vector direction, for 
	 * a hunter that uses yaw (x) and pitch (y) controls, relative to its own
	 * x and y axes. For example, a plane with normal controls.
	 * @param hunter
	 * 		The hunter Spatial, steering in a direction
	 * @param direction
	 * 		The direction to steer in, should be normalised
	 * @param steer
	 * 		The vector to use to store yaw (x) and pitch (y) controls,
	 * 		a new vector is created if this is null
	 * @return
	 * 		The steering vector (steer)
	 */
	public static Vector2f steeringInDirection(Spatial hunter, Vector3f direction, Vector2f steer) {
		return steeringInDirection(hunter.getWorldTranslation(), hunter.getWorldRotation(), direction, steer);
/*
		//If we got a null vector, create one
		if (steer == null) {
			steer = new Vector2f();
		}

		//Get the Z-axis
		NodeTranslator.makeTranslationVector(hunter, 2, 1, zAxis);
		
		//Check if we are facing nearly exactly away from the target - in this case
		//we just steer right, to prevent "locking" when we are heading in exactly the
		//wrong direction. Hopefully this steering will soon cause us not to be heading
		//directly away, we will probably end up turning right all the way around to the
		//target, if it doesn't move.
		if (zAxis.dot(direction) > 0.999) {
			steer.set(1, 0);
			return steer;
		}
		
		//Get the axes
		NodeTranslator.makeTranslationVector(hunter, 0, 1, xAxis);
		NodeTranslator.makeTranslationVector(hunter, 1, 1, yAxis);
		
		//Project the aim onto x and y axes to give steering axes
		float xSteer = xAxis.dot(direction);
		float ySteer = yAxis.dot(direction);
				
		//Set vector to steer
		steer.set(xSteer, ySteer);
		
		return steer;
		*/
	}

	/**
	 * Work out the steering vector in a given vector direction, for 
	 * a hunter that uses yaw (x) and pitch (y) controls, relative to its own
	 * x and y axes. For example, a plane with normal controls.
	 * @param hunter
	 * 		The hunter Spatial, steering in a direction
	 * @param direction
	 * 		The direction to steer in, should be normalised
	 * @param steer
	 * 		The vector to use to store yaw (x) and pitch (y) controls,
	 * 		a new vector is created if this is null
	 * @return
	 * 		The steering vector (steer)
	 */
	public static Vector2f steeringInDirection(Acobject object, Vector3f direction, Vector2f steer) {
		return steeringInDirection(object.getPosition(), object.getRotation(), direction, steer);
	}

	
	/**
	 * Work out the steering vector in a given vector direction, for 
	 * a hunter that uses yaw (x) and pitch (y) controls, relative to its own
	 * x and y axes. For example, a plane with normal controls.
	 * @param hunter
	 * 		The hunter Spatial, steering in a direction
	 * @param direction
	 * 		The direction to steer in, should be normalised
	 * @param steer
	 * 		The vector to use to store yaw (x) and pitch (y) controls,
	 * 		a new vector is created if this is null
	 * @return
	 * 		The steering vector (steer)
	 */
	private static Vector2f steeringInDirection(Vector3f hunterPosition, Quaternion hunterRotation, Vector3f direction, Vector2f steer) {

		//If we got a null vector, create one
		if (steer == null) {
			steer = new Vector2f();
		}

		//Get the Z-axis
		NodeTranslator.makeTranslationVector(hunterRotation, 2, 1, zAxis);
		
		//Check if we are facing nearly exactly away from the target - in this case
		//we just steer right, to prevent "locking" when we are heading in exactly the
		//wrong direction. Hopefully this steering will soon cause us not to be heading
		//directly away, we will probably end up turning right all the way around to the
		//target, if it doesn't move.
		if (zAxis.dot(direction) < -0.999) {
			steer.set(1, 0);
			return steer;
		}
		
		//Get the axes
		NodeTranslator.makeTranslationVector(hunterRotation, 0, 1, xAxis);
		NodeTranslator.makeTranslationVector(hunterRotation, 1, 1, yAxis);
		
		//Project the aim onto x and y axes to give steering axes
		float xSteer = xAxis.dot(direction);
		float ySteer = yAxis.dot(direction);
				
		//Set vector to steer
		steer.set(xSteer, ySteer);
		
		return steer;
	}
	
	/**
	 * The hunter is a spatial which can "roll" around its z axis,
	 * and wishes to align its y axis as closely as possible to a
	 * given "up" axis.
	 * For example, picture a plane - it can roll around the axis of
	 * its body (the direction it would normally be travelling in
	 * flight). It wishes to "level" its wings, that is, to roll
	 * until its wings are perpendicular to the up axis, lined up with 
	 * the "horizon" (in this case, probably (0, 1, 0) ). This can
	 * be accomplished by rolling according to this function.
	 * @param hunter
	 * 		The spatial wishing to roll towards up
	 * @param upAxis
	 * 		The "up" axis that the spatial wants to roll its y-axis
	 * 		towards
	 * @param rollMultiplier
	 * 		A multiplier for roll. If set to 1, roll will roughly
	 * 		follow a sin function as the required roll angle increases
	 * 		to a quarter turn, sticking at 1 or -1 as the required
	 * 		angle goes past this (when the hunter is "upside down").
	 * 		Increasing the roll multiplier scales this roll, before
	 * 		capping it to magnitude 1. So a value of rollMultiplier
	 * 		greater than 1 will cause it to reach magnitude 1 at a 
	 * 		smaller required roll angle, resulting in more aggressive 
	 * 		rolling - using too high a value may result in oscillation.
	 * 		 A value below 1 will cause the roll to cap at 
	 * 		that magnitude. Negative values should roll away from
	 * 		up axis, but may oscillate - use a negated up axis instead.
	 * 		 
	 * @return
	 * 		The required roll towards up. Use for example with
	 * 		NodeRotator, rotating around axis 2, or with SteeringControls
	 * 		and SteeringController
	 */
	public static float rollTowardsUp(Spatial hunter, Vector3f upAxis, float rollMultiplier) {
		
		return rollTowardsUp(hunter.getWorldTranslation(), hunter.getWorldRotation(), upAxis, rollMultiplier);
		
		/*
		//Get the axes. 
		//y axis is the one to be pointed (as nearly as possible) along
		//the specified "up" vector, z axis is the axis we are rolling
		//around 
		//planeUpAxis is the plane's local up (axis of rudder), forward is the way the guns shoot ;)
		NodeTranslator.makeTranslationVector(hunter, 1, 1, yAxis);
		NodeTranslator.makeTranslationVector(hunter, 2, 1, zAxis);

		//Work out whether the z axis (axis of roll) is pointing nearly along the upAxis.
		//If this is the case, rolling will make little difference, so don't bother.
		if (FastMath.abs(zAxis.dot(upAxis)) > 0.99) return 0;
		
		//Work out the vector the x axis should have, to be in level flight - this is cross of
		//the plane forward axis and world up axis
		upAxis.cross(zAxis, idealXAxis);
		
		//Work out the dot of the plane up axis onto the ideal wing - ideally they should
		//be perpendicular, because the wings are along the ideal axis, and the plane up axis is obviously
		//perp to the wings. The more the plane is leaning, the higher the dot, until the plane is
		//actually upside down.
		float dot = idealXAxis.dot(yAxis);
		idealXAxis.normalizeLocal();
		
		//Check if the z axis is facing away from the upAxis - if so, set dot to magnitude 1 for max steering
		if (yAxis.dot(upAxis) < 0) {
			if (dot < 0) dot = -1;
			else dot = 1;
		}
		
		//Scale steering up, capping to size 1
		dot *= rollMultiplier;
		float size = Math.abs(dot);
		if (size > 1) {
			dot /= size;
		}
			
		//Set the roll
		return dot;*/
	}
	
	/**
	 * The hunter is a spatial which can "roll" around its z axis,
	 * and wishes to align its y axis as closely as possible to a
	 * given "up" axis.
	 * For example, picture a plane - it can roll around the axis of
	 * its body (the direction it would normally be travelling in
	 * flight). It wishes to "level" its wings, that is, to roll
	 * until its wings are perpendicular to the up axis, lined up with 
	 * the "horizon" (in this case, probably (0, 1, 0) ). This can
	 * be accomplished by rolling according to this function.
	 * @param hunter
	 * 		The spatial wishing to roll towards up
	 * @param upAxis
	 * 		The "up" axis that the spatial wants to roll its y-axis
	 * 		towards
	 * @param rollMultiplier
	 * 		A multiplier for roll. If set to 1, roll will roughly
	 * 		follow a sin function as the required roll angle increases
	 * 		to a quarter turn, sticking at 1 or -1 as the required
	 * 		angle goes past this (when the hunter is "upside down").
	 * 		Increasing the roll multiplier scales this roll, before
	 * 		capping it to magnitude 1. So a value of rollMultiplier
	 * 		greater than 1 will cause it to reach magnitude 1 at a 
	 * 		smaller required roll angle, resulting in more aggressive 
	 * 		rolling - using too high a value may result in oscillation.
	 * 		 A value below 1 will cause the roll to cap at 
	 * 		that magnitude. Negative values should roll away from
	 * 		up axis, but may oscillate - use a negated up axis instead.
	 * 		 
	 * @return
	 * 		The required roll towards up. Use for example with
	 * 		NodeRotator, rotating around axis 2, or with SteeringControls
	 * 		and SteeringController
	 */
	public static float rollTowardsUp(Acobject object, Vector3f upAxis, float rollMultiplier) {
		return rollTowardsUp(object.getPosition(), object.getRotation(), upAxis, rollMultiplier);
	}

	
	/**
	 * The hunter is a spatial which can "roll" around its z axis,
	 * and wishes to align its y axis as closely as possible to a
	 * given "up" axis.
	 * For example, picture a plane - it can roll around the axis of
	 * its body (the direction it would normally be travelling in
	 * flight). It wishes to "level" its wings, that is, to roll
	 * until its wings are perpendicular to the up axis, lined up with 
	 * the "horizon" (in this case, probably (0, 1, 0) ). This can
	 * be accomplished by rolling according to this function.
	 * @param hunter
	 * 		The spatial wishing to roll towards up
	 * @param upAxis
	 * 		The "up" axis that the spatial wants to roll its y-axis
	 * 		towards
	 * @param rollMultiplier
	 * 		A multiplier for roll. If set to 1, roll will roughly
	 * 		follow a sin function as the required roll angle increases
	 * 		to a quarter turn, sticking at 1 or -1 as the required
	 * 		angle goes past this (when the hunter is "upside down").
	 * 		Increasing the roll multiplier scales this roll, before
	 * 		capping it to magnitude 1. So a value of rollMultiplier
	 * 		greater than 1 will cause it to reach magnitude 1 at a 
	 * 		smaller required roll angle, resulting in more aggressive 
	 * 		rolling - using too high a value may result in oscillation.
	 * 		 A value below 1 will cause the roll to cap at 
	 * 		that magnitude. Negative values should roll away from
	 * 		up axis, but may oscillate - use a negated up axis instead.
	 * 		 
	 * @return
	 * 		The required roll towards up. Use for example with
	 * 		NodeRotator, rotating around axis 2, or with SteeringControls
	 * 		and SteeringController
	 */
	public static float rollTowardsUp(Vector3f hunterPosition, Quaternion hunterRotation, Vector3f upAxis, float rollMultiplier) {
		
		//Get the axes. 
		//y axis is the one to be pointed (as nearly as possible) along
		//the specified "up" vector, z axis is the axis we are rolling
		//around 
		//planeUpAxis is the plane's local up (axis of rudder), forward is the way the guns shoot ;)
		NodeTranslator.makeTranslationVector(hunterRotation, 1, 1, yAxis);
		NodeTranslator.makeTranslationVector(hunterRotation, 2, 1, zAxis);

		//Work out whether the z axis (axis of roll) is pointing nearly along the upAxis.
		//If this is the case, rolling will make little difference, so don't bother.
		if (FastMath.abs(zAxis.dot(upAxis)) > 0.99) return 0;
		
		//Work out the vector the x axis should have, to be in level flight - this is cross of
		//the plane forward axis and world up axis
		upAxis.cross(zAxis, idealXAxis);
		
		//Work out the dot of the plane up axis onto the ideal wing - ideally they should
		//be perpendicular, because the wings are along the ideal axis, and the plane up axis is obviously
		//perp to the wings. The more the plane is leaning, the higher the dot, until the plane is
		//actually upside down.
		float dot = idealXAxis.dot(yAxis);
		idealXAxis.normalizeLocal();
		
		//Check if the z axis is facing away from the upAxis - if so, set dot to magnitude 1 for max steering
		if (yAxis.dot(upAxis) < 0) {
			if (dot < 0) dot = -1;
			else dot = 1;
		}
		
		//Scale steering up, capping to size 1
		dot *= rollMultiplier;
		float size = Math.abs(dot);
		if (size > 1) {
			dot /= size;
		}
			
		//Set the roll
		return dot;
	}
	
	public static void normaliseSteering(Vector2f steer, NormaliseMethod method) {
		//Normalise if requested
		if (method == NormaliseMethod.NORMALISE) {
			steer = steer.normalize();			
		//Clip to -1..1 if requested
		} else if (method == NormaliseMethod.SCALE_TO_UNIT){
			float biggestSteer = Math.max(Math.abs(steer.x), Math.abs(steer.y));
			if (biggestSteer > 0.0001) {
				steer.multLocal(1f/biggestSteer);
			}
		}
	}
	
}
