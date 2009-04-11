package net.java.dev.aircarrier.triggers;

import net.java.dev.aircarrier.acobject.Acobject;

public interface TriggerListener {

	/**
	 * Called when a trigger is activated
	 * @param trigger
	 * 		The trigger which was activated
	 * @param triggeredBy
	 * 		The object that activated it
	 */
	public void triggered(Trigger trigger, Acobject triggeredBy);
	
}
