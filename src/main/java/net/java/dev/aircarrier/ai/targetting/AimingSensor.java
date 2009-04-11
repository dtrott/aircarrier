package net.java.dev.aircarrier.ai.targetting;

import com.jme.math.Vector3f;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.input.action.NodeTranslator;

/**
 * Returns a measure of whether the hunter is pointing towards 
 * the prey, as measured using dot of hunter's forward axis with 
 * offset vector from hunter to prey. Value is scaled
 * from 0 to 1 as this dot value moves from offsetDotZero to
 * offsetDotOne. Hence this follows cosine of steering angle from
 * hunter to prey.
 * 
 * @author shingoki
 */
public class AimingSensor implements TargetChoiceSensor<Acobject, Acobject> {

	float offsetDotZero;
	float offsetDotOne;
	float offsetGapInverse;
	
	Vector3f tempOffset = new Vector3f();
	Vector3f tempForwards = new Vector3f();

	/**
	 * Create a sensor
	 * @param self
	 * 		The spatial whose targetting we are monitoring
	 * @param offsetDotOne
	 * 		The offset to the prey is dotted with the hunter's forward axis.
	 * 		offsetDotOne is the smallest value of this dot where the sensor will read one - that is,
	 * 		if we have a dot of this size or larger, we will get a reading of 1, since we are well aimed.
	 * 		In other words, the minimum value of the sine of our steering
	 * 		angle to the target, to give a 1 reading on the sensor.
	 * @param offsetDotZero
	 * 		The offset to the prey is dotted with the hunter's forward axis.
	 * 		offsetDotZero is the value of this dot below which the sensor will read zero - that is,
	 * 		we are NOT well aimed at target.
	 * 		In other words, the minimum value of the cosine of our steering
	 * 		angle to the target, to give any reading on the sensor.
	 * 		Must be less than offsetDotOne - if not it will be set to offsetDotOne - 0.01f
	 */
	public AimingSensor(float offsetDotOne, float offsetDotZero) {
		this.offsetDotOne = offsetDotOne;
		this.offsetDotZero = offsetDotZero;
		offsetGapInverse = 1/(offsetDotOne - offsetDotZero);
	}
	
	public float getTargettingValue(Acobject hunter, Acobject prey) {
		
		tempOffset.set(prey.getPosition());
		tempOffset.subtractLocal(hunter.getPosition());
		tempOffset.normalizeLocal();
		NodeTranslator.makeTranslationVector(hunter.getRotation(), 2, 1, tempForwards);
		
		float dot = tempOffset.dot(tempForwards);
		
		return (dot - offsetDotZero) * offsetGapInverse;

	}

	public void update(float time) {
		//Not time dependent
	}

	public void targettingChange(Acobject hunter, Acobject oldPrey, Acobject newPrey) {
		//Not dependent on actual targetting
	}

}
