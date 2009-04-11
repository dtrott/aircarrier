package net.java.dev.aircarrier.ai.targetting;

import java.util.ArrayList;
import java.util.List;

import net.java.dev.aircarrier.acobject.Acobject;

/**
 * 
 * A sensor that combines the values of other sensors, and passes through updates and targetting changes
 * to them.
 * 
 * Values are combined by combining each value with the previous value, whilst working through the list
 * of sensors, given a simple binary operator "combiner".
 * 
 * Examples of combiners would be multiplication, addition, minimum, maximum, etc. (any commutative operation,
 * or indeed any operation that has some sensible application).
 * 
 * Values can also be averaged if no combiner is used
 * 
 * The final output value will always be clipped to 0-1 range
 * 
 * @author shingoki
 *
 * @param <H>
 * 		Type of hunter for sensors
 * @param <P>
 * 		Type of prey for sensors
 */
public class CombiningTargetChoiceSensor<H extends Acobject, P extends Acobject> implements TargetChoiceSensor<H, P> {

	List<TargetChoiceSensor<H, P>> sensors;
	Combiner combiner;

	/**
	 * Create a CombiningTargetChoiceSensor with no sensors, you will need
	 * to add some to the sensors list to do anything useful.
	 * No Combiner is set, so sensor output is averaged 
	 */
	public CombiningTargetChoiceSensor() {
		this(null, new ArrayList<TargetChoiceSensor<H,P>>());
	}

	/**
	 * Create a CombiningTargetChoiceSensor with no sensors, you will need
	 * to add some to the sensors list to do anything useful.
	 * @param combiner
	 * 		Operation to use to combine sensor outputs 
	 */
	public CombiningTargetChoiceSensor(Combiner combiner) {
		this(combiner, new ArrayList<TargetChoiceSensor<H,P>>());
	}

	/**
	 * Create a CombiningTargetChoiceSensor
	 * @param combiner
	 * 		Operation to use to combine sensor outputs 
	 * @param sensors
	 * 		List of sensors to combine
	 */
	public CombiningTargetChoiceSensor(Combiner combiner, ArrayList<TargetChoiceSensor<H,P>> sensors) {
		this.combiner = combiner;
		this.sensors = sensors;
	}

	/**
	 * Create a CombiningTargetChoiceSensor 
	 * No Combiner is set, so sensor output is averaged 
	 * @param sensors
	 * 		List of sensors to combine
	 */
	public CombiningTargetChoiceSensor(ArrayList<TargetChoiceSensor<H,P>> sensors) {
		this(null, sensors);
	}
	
	public float getTargettingValue(H hunter, P prey) {
		//Just return 0 if no sensors
		if (sensors.isEmpty()) return 0;
		boolean first = true;
		float value = 0;
		for (TargetChoiceSensor<H, P> sensor : sensors) {
			if (combiner != null) {
				//Start from value of first sensor
				if (first) {
					value = sensor.getTargettingValue(hunter, prey);
					first = false;
				//For each subsequent sensor, combine the new sensor with the current running value
				} else {
					value = combiner.combine(value, sensor.getTargettingValue(hunter, prey));
				}
			} else {
				value += sensor.getTargettingValue(hunter, prey);
			}
		}
	
		if (combiner == null) {
			value /= (float)sensors.size();
		}

		//Clip value
		if (value < 0) value = 0;
		if (value > 1) value = 1;
		
		return value;
	}

	public void update(float time) {
		//Pass through to all sensors
		for (TargetChoiceSensor<H, P> sensor : sensors) {
			sensor.update(time);
		}
	}

	public void targettingChange(H hunter, P oldPrey, P newPrey) {
		//Pass through to all sensors
		for (TargetChoiceSensor<H, P> sensor : sensors) {
			sensor.targettingChange(hunter, oldPrey, newPrey);
		}
	}

	public Combiner getCombiner() {
		return combiner;
	}

	public void setCombiner(Combiner combiner) {
		this.combiner = combiner;
	}

	public List<TargetChoiceSensor<H, P>> getSensors() {
		return sensors;
	}

	public void setSensors(List<TargetChoiceSensor<H, P>> sensors) {
		this.sensors = sensors;
	}

}
