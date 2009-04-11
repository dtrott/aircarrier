package net.java.dev.aircarrier.ai;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.input.action.NodeTranslator;

import com.jme.math.Vector3f;

/**
 * Sensor to monitor performance of a TargetSeekingSteerer, by
 * reporting 1 when a node is pointing within a given angle
 * of its target, 0 when it is more than a second given angle, and
 * interpolated according to cosine of misaiming angle in between.
 * 
 * @author goki
 */
public class TargettingSensor implements ScalarSensor {

	Acobject self;
	Acobject target;
	float offsetDotZero;
	float offsetDotOne;
	float value = 0;
	
	Vector3f tempOffset = new Vector3f();
	Vector3f tempForwards = new Vector3f();

	/**
	 * Create a sensor
	 * @param self
	 * 		The spatial whose targetting we are monitoring
	 * @param offsetDotOne
	 * 		The offset to the steerer's target is dotted with the steerer's self forward axis.
	 * 		offsetDotOne is the smallest value of this dot where the sensor will read one - that is,
	 * 		if we have a dot of this size or larger, we will get a reading of 1, since we are well aimed.
	 * 		In other words, the minimum value of the sine of our steering
	 * 		angle to the target, to give a 1 reading on the sensor.
	 * @param offsetDotZero
	 * 		The offset to the steerer's target is dotted with the steerer's self forward axis.
	 * 		offsetDotZero is the value of this dot below which the sensor will read zero - that is,
	 * 		we are NOT well aimed at target.
	 * 		In other words, the minimum value of the cosine of our steering
	 * 		angle to the target, to give any reading on the sensor.
	 * 		Must be less than offsetDotOne - if not it will be set to offsetDotOne - 0.01f
	 */
	public TargettingSensor(Acobject self,float offsetDotOne, float offsetDotZero) {
		this.self = self;
		this.target = null;
		this.offsetDotOne = offsetDotOne;
		this.offsetDotZero = offsetDotZero;
		if (offsetDotZero >= offsetDotOne) offsetDotZero = offsetDotOne-0.01f;
	}

	public Acobject getSelf() {
		return self;
	}
	public void setSelf(Acobject self) {
		this.self = self;
	}
	/**
	 * @return
	 * 		The spatial we are targetting
	 */
	public Acobject getTarget() {
		return target;
	}
	/**
	 * @param target
	 * 		The spatial we are targetting
	 */
	public void setTarget(Acobject target) {
		this.target = target;
	}


	public void update(float time) {

		//If no target or no self, then targetting is 0
		if (target == null || self == null) {
			value = 0;
			return;
		}
		
		tempOffset.set(target.getPosition());
		tempOffset.subtractLocal(self.getPosition());
		tempOffset.normalizeLocal();
		NodeTranslator.makeTranslationVector(self.getRotation(), 2, 1, tempForwards);
		
		float dot = tempOffset.dot(tempForwards);
		
		if (dot < offsetDotZero) {
			value = 0;
		} else if (dot > offsetDotOne) {
			value = 1;
		} else {
			value = (dot - offsetDotZero) / (offsetDotOne - offsetDotZero);
		}
	}

	public float getIntensity() {
		return value;
	}

}
