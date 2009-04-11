package net.java.dev.aircarrier.controls;

import java.util.List;

public class AlternateBehaviourControls implements PlaneControls {

	PlaneControls mainControls;
	List<PlaneControlsWithSensor> alternateControls;

	/**
	 * Create controls
	 * @param mainControls
	 * 		The main controls - thes represent the behaviour when none of the alternate controls
	 * 		have a high sensor value. This is often what the plane "wants" to do. It just does the
	 * 		alternate steering when the sensor value for that steering is high - for example, this
	 * 		might be an avoidance steerer reporting that avoidance is needed
	 * @param alternateControls
	 * 		The alternate controls - these are blended into the final output value when their sensor
	 * 		values are high.
	 */
	public AlternateBehaviourControls(PlaneControls mainControls, List<PlaneControlsWithSensor> alternateControls) {
		super();
		this.mainControls = mainControls;
		this.alternateControls = alternateControls;
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

	public void setFiring(int gun, boolean firing) {
		mainControls.setFiring(gun, firing);
	}

	public float getAxis(int axis) {
		//Blend controls in a super complicated way!
		//Basically, when we have more than one alternate steerer with
		//a non zero sensor, we blend between them, then blend
		//between the "alternate" steering and the main steering
		//according to the maximum amount of any alternate steering.
		//When we have only one alternate controls with a non-zero
		//sensor, it will just be blended with
		//plain steering according to intensity
		//This ensures that if any alternate steering is at 1, the normal steering
		//will be completely removed, and the alternate steering blended by intensity.
		//So in the worst case, say we have two alternate controls where both are 
		//at sensor level 1, we at least get a 50/50 mix that
		//might get us out of trouble
		
		//Make mean alternate axis value, weighted by intensity
		float alternateIntensitySum = 0;
		float alternateAxisSum = 0;
		float alternateIntensityMax = 0;
		for (PlaneControlsWithSensor alternate : alternateControls) {
			float intensity = alternate.getIntensity();
			alternateIntensitySum += intensity;
			alternateAxisSum += alternate.getAxis(axis) * intensity;
			if (intensity > alternateIntensityMax) alternateIntensityMax = intensity;
		}
		
		//If sum is large enough to make mean meaningful, use blend
		if (alternateIntensitySum > 0.01) {
			float alternateAxisMean = alternateAxisSum / alternateIntensitySum;
			
			//Blend this mean alternate axis with the main controls axis by the maximum alternate intensity.
			//So if any alternate controls have intensity 1, we will use just the mean alternate axis value,
			//if they all have intensity 0, we will use the main controls, with linear blending in between
			return alternateIntensityMax * alternateAxisMean + (1 - alternateIntensityMax) * mainControls.getAxis(axis);
			
		//Alternate intensity sum is too low, just ignore alternate steering
		} else {
			return mainControls.getAxis(axis);
		}


	}

	public void moveAxis(int axis, float control) {
		mainControls.moveAxis(axis, control);
	}

	public void setAxis(int axis, float control) {
		mainControls.setAxis(axis, control);
	}

	public void update(float time) {
		//The other controls update themselves
	}

}
