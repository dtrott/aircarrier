package net.java.dev.aircarrier.ai.targetting;

import net.java.dev.aircarrier.acobject.Acobject;

/**
 * Combines a few sensors to give a reasonable approach to targetting, hopefully
 * @author shingoki
 */
public class SimpleTargetChoiceSensor implements TargetChoiceSensor<Acobject, Acobject> {

	AimingSensor aiming = new AimingSensor(0.1f, 0.8f);
	AntiBullyingSensor bullying = new AntiBullyingSensor(0, 2);
	DistanceSensor distance = new DistanceSensor(250, 100);
	RandomSensor random = new RandomSensor(0.05f);
	TargetChangeHysteresisSensor hysteresis = new TargetChangeHysteresisSensor(3f, 0.5f);
	
	CombiningTargetChoiceSensor<Acobject, Acobject> aimAndDistance = new CombiningTargetChoiceSensor<Acobject, Acobject>();
	FunctionTargetChoiceSensor<Acobject, Acobject> scaledAimAndDistance;
	
	CombiningTargetChoiceSensor<Acobject, Acobject> finalSensor;
	
	public SimpleTargetChoiceSensor() {
		//aimAndDistance averages aim and distance sensors
		aimAndDistance.getSensors().add(aiming);
		aimAndDistance.getSensors().add(distance);
		
		//scaledAimAndDistance scales them to half
		scaledAimAndDistance = new FunctionTargetChoiceSensor<Acobject, Acobject>(aimAndDistance, new LinearFunction(0.5f, 0.0f));
		
		//Final sensor adds bullying, hysteresis and random
		finalSensor = new CombiningTargetChoiceSensor<Acobject, Acobject>(Combiner.ADDITION);
		finalSensor.getSensors().add(scaledAimAndDistance);
		finalSensor.getSensors().add(hysteresis);
		finalSensor.getSensors().add(random);
		
	}
	
	public float getTargettingValue(Acobject hunter, Acobject prey) {
		return finalSensor.getTargettingValue(hunter, prey);
	}

	public void update(float time) {
		finalSensor.update(time);
	}

	public void targettingChange(Acobject hunter, Acobject oldPrey,
			Acobject newPrey) {
		finalSensor.targettingChange(hunter, oldPrey, newPrey);
	}

}
