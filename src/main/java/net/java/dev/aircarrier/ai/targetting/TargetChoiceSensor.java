package net.java.dev.aircarrier.ai.targetting;

import net.java.dev.aircarrier.acobject.Acobject;

/**
 * A sensor which can assess the "value" of prey to a hunter.
 * It must be updated as time passes, and should be attached
 * as a listener to anything changing targets, so that it
 * can allow for targetting changes.
 * @author shingoki
 */
public interface TargetChoiceSensor<H extends Acobject, P extends Acobject> extends TargettingChangeListener<H, P>{

	/**
	 * Get the value of targetting prey to a hunter.
	 * This is independent of the value of the actual prey 
	 * - the value represents the likelihood that attacking the prey
	 * will yield a result. It may well be multiplied by a separate
	 * prey "value" to get a final result. (e.g. a 50% chance of
	 * getting prey work $100 would then work out the same as a 100% chance
	 * of getting a $50 target)
	 * This is scaled from 0 indicating no point in targetting
	 * prey (e.g. it is out of weapon range) to 1 indicating
	 * the prey is the best possible target.
	 * Obviously scaling is somewhat subjective, but the range
	 * should be respected. Something like an estimated 
	 * probability of achieving a "kill" in a period of 
	 * time would be reasonable, the period of time is obviously
	 * again a variable, but should be fairly constant for a game
	 * type, for example, a 10 second period might be reasonable,
	 * as long as all TargettingSensors use the same scale.
	 * @param hunter
	 * 		The hunter who might target the prey
	 * @param prey
	 * 		The prey which might be targetted
	 * @return
	 * 		The value of targetting the prey.
	 */
	public float getTargettingValue(H hunter, P prey);
	
	/**
	 * Update the targetting for the passage of time.
	 * This should be called BEFORE getting targetting values
	 * for a frame, once per frame, so that sensor is up to date.
	 * @param time
	 * 		Time since last update
	 */
	public void update(float time);
	
	
}
