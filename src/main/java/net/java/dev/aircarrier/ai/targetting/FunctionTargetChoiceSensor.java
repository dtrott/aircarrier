package net.java.dev.aircarrier.ai.targetting;

import net.java.dev.aircarrier.acobject.Acobject;

/**
 * A sensor that simply gets its values from another sensor, then
 * applies a function to them.
 * For example, this can multiply or offset values.
 * updates and targetting changes are passed through to the underlying function
 * @author shingoki
 *
 * @param <H>
 * 		The type of hunter
 * @param <P>
 * 		The type of prey
 */
public class FunctionTargetChoiceSensor <H extends Acobject, P extends Acobject> implements TargetChoiceSensor<H, P> {

	TargetChoiceSensor<H, P> sensor;
	OneDFloatFunction function;
	
	/**
	 * Create a sensor
	 * @param sensor
	 * 		This sensor will be used to get a targetting value when requested. This
	 * 		value will then have the function applied, the result is clipped to 0-1 range
	 * 		and returned
	 * @param function
	 * 		The function to apply to wrapped sensor values to get final sensor values
	 */
	public FunctionTargetChoiceSensor(TargetChoiceSensor<H, P> sensor, OneDFloatFunction function) {
		super();
		this.sensor = sensor;
		this.function = function;
	}

	public float getTargettingValue(H hunter, P prey) {
		float value = function.evaluate(sensor.getTargettingValue(hunter, prey));
		if (value < 0) value = 0;
		if (value > 1) value = 1;
		return value;
	}

	public void update(float time) {
		sensor.update(time);
	}

	public void targettingChange(H hunter, P oldPrey,
			P newPrey) {
		sensor.targettingChange(hunter, oldPrey, newPrey);
	}

}
