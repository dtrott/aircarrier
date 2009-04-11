package net.java.dev.aircarrier.ai.targetting;

import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

import net.java.dev.aircarrier.acobject.Acobject;

/**
 * Discourages multiple targetting of a single prey by multiple hunters.
 * Produces a value of 1 for a given prey when less than a minimum number
 * of hunters are targetting it, and 0 when more than a maximum are targetting,
 * with values interpolating between.
 * @author shingoki
 */
public class AntiBullyingSensor implements TargetChoiceSensor<Acobject, Acobject> {

	float minHunters;
	float maxHunters;
	
	//Store a map from prey, to the hunter count
	WeakHashMap<Acobject, HunterCount> hunterCountMap = new WeakHashMap<Acobject, HunterCount>();

	/**
	 * Create a sensor with default values.
	 * This will return a value of 1 when nothing
	 * is targetting a prey, falling to 0 when 3
	 * hunters are targetting a prey.
	 */
	public AntiBullyingSensor() {
		this(0, 3);
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
	public AntiBullyingSensor(float minHunters, float maxHunters) {
		super();
		this.minHunters = minHunters;
		this.maxHunters = maxHunters;
	}

	/**
	 * @return 
	 * 		The value of targetting the prey. This is a value that decreases from
	 * 		1 as the number of hunters targetting the prey increases.
	 */
	public float getTargettingValue(Acobject hunter, Acobject prey) {
		
		if (prey != null) {
			HunterCount h = getHunterCount(prey);
			float count = (float)h.hunterCount();
			
			if (count < minHunters) {
				return 1;
			} else if (count > maxHunters) {
				return 0;
			} else {
				return (maxHunters - count)/(maxHunters - minHunters);
			}
		} else {
			return 0;
		}
	}

	public void update(float time) {
		//Not time dependent
	}

	public void targettingChange(Acobject hunter, Acobject oldPrey, Acobject newPrey) {
		//Remove the hunter from hunters of the old prey
		if (oldPrey != null) {
			getHunterCount(oldPrey).removeHunter(hunter);
		}
		
		//Add the hunter to hunters of new prey
		if (newPrey != null) {
			getHunterCount(newPrey).addHunter(hunter);
		}
	}

	/**
	 * Get the hunter count of a prey, this will be a new hunter count with
	 * no hunters if there is no previous hunter count.
	 * @param prey
	 * 		The prey for which to get hunter count
	 * @return
	 * 		The hunter count for the prey
	 */
	private HunterCount getHunterCount(Acobject prey) {
		HunterCount h = hunterCountMap.get(prey);
		if (h == null) {
			h = new HunterCount();
			hunterCountMap.put(prey, h);
		}
		return h;
	}
	
	/**
	 * Store the set of hunters targetting a prey,
	 * and count them
	 * @author shingoki
	 */
	private class HunterCount {
		Set<Acobject> hunters = new HashSet<Acobject>();

		public void addHunter(Acobject hunter) {
			hunters.add(hunter);
		}
		
		public void removeHunter(Acobject hunter) {
			hunters.remove(hunter);
		}
		
		public int hunterCount() {
			return hunters.size();
		}
	}
	
}
