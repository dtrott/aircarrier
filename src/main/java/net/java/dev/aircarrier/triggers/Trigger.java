package net.java.dev.aircarrier.triggers;

import net.java.dev.aircarrier.acobject.Acobject;

public interface Trigger {

	/**
	 * Check whether an object sets off the trigger
	 * Note that this may result in an asynchronous
	 * triggering when the Trigger is later updated.
	 * Triggering will only occur at the time of the
	 * check, or in the next update.
	 * @param object
	 * 		The object to check
	 */
	public void check(Acobject object);
	
	/**
	 * Update monitoring of triggering
	 * @param time
	 * 		Time passed in the world
	 */
	public void update(float time);

	/**
	 * @param listener
	 * 		To be notified when the trigger activates
	 */
	public void addTriggerListener(TriggerListener listener);

	/**
	 * @param listener
	 * 		No longer to be notified when the trigger activates
	 */
	public void removeTriggerListener(TriggerListener listener);

}
