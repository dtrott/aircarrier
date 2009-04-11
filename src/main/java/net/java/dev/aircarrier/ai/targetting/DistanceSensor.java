package net.java.dev.aircarrier.ai.targetting;

import net.java.dev.aircarrier.acobject.Acobject;

/**
 * Return a value from 0 to 1 as distance moves from
 * zeroDistance to oneDistance. Capped to 0 or 1 outside
 * this range. Note that zeroDistance need not be less than
 * oneDistance.
 * Interpolation between the distances is done according to SQUARED
 * distance, not linear distance. This is faster, and probably works
 * as well for AI purposes ;)
 * @author shingoki
 *
 */
public class DistanceSensor implements TargetChoiceSensor<Acobject, Acobject> {

	//float zeroDistance;
	//float oneDistance;
	float zeroDistanceSq;
	float oneDistanceSq;
	float gapInverse;
	
	public DistanceSensor(float zeroDistance, float oneDistance) {
		super();
		//this.zeroDistance = zeroDistance;
		//this.oneDistance = oneDistance;
		zeroDistanceSq = zeroDistance * zeroDistance;
		oneDistanceSq = oneDistance * oneDistance;
		gapInverse = 1/(oneDistanceSq - zeroDistanceSq);
	}

	public float getTargettingValue(Acobject hunter, Acobject prey) {
		float distanceSq = hunter.getPosition().distanceSquared(prey.getPosition());
		float value = (distanceSq-zeroDistanceSq)*gapInverse;
		if (value < 0) {
			value = 0;
		} else if (value > 1) {
			value = 1;
		}
		return value;
	}

	public void update(float time) {
		//Not time dependent
	}

	public void targettingChange(Acobject hunter, Acobject oldPrey, Acobject newPrey) {
		//Not dependent on actual targetting
	}

}
