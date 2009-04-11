package net.java.dev.aircarrier.ai;

import net.java.dev.aircarrier.controls.SteeringControls;
import net.java.dev.aircarrier.input.action.NodeTranslator;

import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;

/**
 * Sensor to detect a situation where a plane seems to be circling a target
 * 
 * @author goki
 */
public class CirclingSensor implements ScalarSensor {

	TargetSeekingSteerer steerer;
	float steeringNormMin;
	float offsetDotMax;
	float offsetDotMin;
	float value = 0;
	
	Vector3f tempOffset = new Vector3f();
	Vector3f tempForwards = new Vector3f();
	Vector2f steering = new Vector2f();

	/**
	 * Create a sensor
	 * @param steerer
	 * 		The steerer to watch
	 * @param steeringNormMin
	 * 		The minimum steering vector length. If the steering vector is
	 * 		longer than this, then we assume we are steering strongly,
	 * 		as we do in a circling motion. If we are not steering this strongly,
	 * 		we do not consider oursleves to be circling, even if target is
	 * 		"beside" us as determined with offsetDotMax.
	 * @param offsetDotMax
	 * 		The maximum dot product of the normalized vector of our offset to
	 * 		the target, with our forward vector.
	 * 		In other words, the maximum value of the sine of our steering
	 * 		angle to the target. So, if the other plane is within a small angle
	 * 		of our heading (and ahead of us), we will have a large value, and will not consider ourselves
	 * 		to be circling, even if we are steering strongly. (Note we are either being
	 * 		tailed, or tailing, successfully).
	 * @param offsetDotOne
	 * 		The minimum dot product of the normalized vector of our offset to
	 * 		the target, with our forward vector. Works similarly to the max, but can be used
	 * 		for example to make sure we are NOT considered to be circling when we are being tailed,
	 * 		e.g. if this is 0, then we will not sense circling whenever the target is behind us, which
	 * 		can be useful.
	 */
	public CirclingSensor(TargetSeekingSteerer steerer, float steeringNormMin, float offsetDotMax, float offsetDotMin) {
		this.steerer = steerer;
		this.steeringNormMin = FastMath.abs(steeringNormMin);
		this.offsetDotMax = offsetDotMax;
		this.offsetDotMin = offsetDotMin;
	}

	public void update(float time) {
		tempOffset.set(steerer.getTarget().getPosition());
		tempOffset.subtractLocal(steerer.getSelf().getPosition());
		tempOffset.normalizeLocal();
		NodeTranslator.makeTranslationVector(steerer.getSelf(), 2, 1, tempForwards);
		
		float dot = tempOffset.dot(tempForwards);
		
		steering.set(steerer.getAxis(SteeringControls.YAW), steerer.getAxis(SteeringControls.PITCH));
		if ((steering.lengthSquared() > steeringNormMin*steeringNormMin) 
				&& (dot < offsetDotMax) && (dot > offsetDotMin)) {
			value = 1;
		} else {
			value = 0;
		}		
	}

	public float getIntensity() {
		return value;
	}

}
