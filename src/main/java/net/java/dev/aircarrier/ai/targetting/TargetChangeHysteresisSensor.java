package net.java.dev.aircarrier.ai.targetting;

import java.util.WeakHashMap;

import net.java.dev.aircarrier.acobject.Acobject;

/**
 * Discourages rapid changing of targets, by returning a value
 * for the most recently targetted prey, and 0 for others. This
 * value decays as time passes since targetting of the prey.
 * If this is added to other sensor values, or the maximum value
 * is used between this sensor and other sensors, it can maintain
 * the value of a target as 1 for some time after it is first targetted,
 * so that the hunter will not switch away from it (given requirement
 * for strictly greater value in order to switch, which is sensible). 
 * @author shingoki
 */
public class TargetChangeHysteresisSensor implements TargetChoiceSensor<Acobject, Acobject> {

	float initialHysteresis;
	float hysteresisDecayRate;
	
	//Store a map from hunter, to the amount of hysteresis, and the prey it is associated with
	WeakHashMap<Acobject, Hysteresis> hysteresisMap = new WeakHashMap<Acobject, Hysteresis>();

	/**
	 * Create a sensor with default values.
	 * This will return a value of 1 for new prey, for 4 seconds 
	 * after a target change, which then decays to 0 over another
	 * 2 seconds. (initialHysteresis 3, hysteresisDecayRate 0.5)
	 */
	public TargetChangeHysteresisSensor() {
		this(3, 0.5f);
	}
	
	/**
	 * Create a sensor
	 * @param initialHysteresis	
	 * 		The initial hysteresis value assigned to the new prey when target is changed
	 * 		Should normally be positive.
	 * @param hysteresisDecayRate
	 * 		The rate of loss of hysteresis value with time, in units per second
	 * 		Should be positive in order for hysteresis to decrease with time.
	 */
	public TargetChangeHysteresisSensor(float initialHysteresis, float hysteresisDecayRate) {
		super();
		this.initialHysteresis = initialHysteresis;
		this.hysteresisDecayRate = hysteresisDecayRate;
	}

	/**
	 * @return 
	 * 		The value of targetting the prey. This is a hysteresis value for
	 * 		the most recently targetted prey (clipped to 0-1), 0 for other prey.
	 * 		Hysteresis value is set to an initial value when prey is targetted,
	 * 		then decreased as time passes. Hysteresis value only applies to most
	 * 		recently targetted prey.
	 */
	public float getTargettingValue(Acobject hunter, Acobject prey) {
		
		Hysteresis h = getHysteresis(hunter);
		if ((prey != null) && (prey == h.getPrey())) {
			return h.getClippedValue();
		} else {
			return 0;
		}
	}

	/**
	 * Update the sensor, by decaying all hysteresis values (for the most recent prey of all hunters)
	 * @param time
	 * 		Time passed since last update
	 */
	public void update(float time) {
		for (Hysteresis h : hysteresisMap.values()) {
			if (h.getValue() > 0) {
				h.setValue(h.getValue() - hysteresisDecayRate * time);
			}
		}
	}

	/**
	 * Update the hysteresis of the hunter to reflect the change to new prey
	 * @param hunter
	 * 		The hunter whose target has just changed
	 * @param oldPrey
	 * 		The old target of the hunter
	 * @param newPrey
	 * 		The new target of the hunter
	 */
	public void targettingChange(Acobject hunter, Acobject oldPrey, Acobject newPrey) {
		if (newPrey != oldPrey) {
			Hysteresis h = getHysteresis(hunter);
			h.setPrey(newPrey);
			h.setValue(initialHysteresis);
		}
	}

	/**
	 * Get the hysteresis of a hunter, this will be a new Hysteresis with
	 * null prey and value 0 if there is no previous hysteresis.
	 * @param hunter
	 * 		The hunter for which to get hysteresis
	 * @return
	 * 		The hysteresis for the hunter
	 */
	private Hysteresis getHysteresis(Acobject hunter) {
		Hysteresis h = hysteresisMap.get(hunter);
		if (h == null) {
			h = new Hysteresis();
			hysteresisMap.put(hunter, h);
		}
		return h;
	}
	
	/**
	 * Store the value of hysteresis, and which prety it is
	 * associated with
	 * @author shingoki
	 */
	private class Hysteresis {
		Acobject prey = null;
		float value = 0;
		
		public Acobject getPrey() {
			return prey;
		}
		public void setPrey(Acobject prey) {
			this.prey = prey;
		}
		public float getValue() {
			return value;
		}
		public void setValue(float value) {
			this.value = value;
		}
		
		/**
		 * @return value, clipped to 0-1 range
		 */
		public float getClippedValue() {
			if (value < 0) {
				return 0;
			} else if (value > 1) {
				return 1;
			} else {
				return value;
			}
		}
	}
	
}
