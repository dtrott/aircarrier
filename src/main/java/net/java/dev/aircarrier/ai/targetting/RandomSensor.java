package net.java.dev.aircarrier.ai.targetting;

import com.jme.math.FastMath;

import net.java.dev.aircarrier.acobject.Acobject;

/**
 * Returns a random value, from 0 to a specified amount
 * 
 * @author shingoki
 */
public class RandomSensor implements TargetChoiceSensor<Acobject, Acobject> {

	float amount;

	/**
	 * Create a sensor with 0.05 noise
	 */
	public RandomSensor() {
		this(0.05f);
	}

	/**
	 * Create a sensor
	 * @param amount
	 * 		The amount of random noise
	 */
	public RandomSensor(float amount) {
		this.amount = amount;
	}
	
	public float getTargettingValue(Acobject hunter, Acobject prey) {
		return FastMath.rand.nextFloat() * amount;
	}

	public void update(float time) {
		//Not time dependent
	}

	public void targettingChange(Acobject hunter, Acobject oldPrey, Acobject newPrey) {
		//Not dependent on actual targetting
	}

}
