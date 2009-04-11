package net.java.dev.aircarrier.ai.targetting;

import net.java.dev.aircarrier.acobject.Acobject;

/**
 * A listener for changes of targetting.
 * Most often used by a TargettingManager to let TargetChoiceSensors
 * now about changes it makes to target assignments.
 * @author shingoki
 *
 * @param <H>
 * 		The type of hunter
 * @param <P>
 * 		The type of prey
 */
public interface TargettingChangeListener <H extends Acobject, P extends Acobject> {

	/**
	 * Called to indicate a change of targetting
	 * @param hunter
	 * 		The hunter whose target has just changed
	 * @param oldPrey
	 * 		The old target of the hunter
	 * @param newPrey
	 * 		The new target of the hunter
	 */
	public void targettingChange(H hunter, P oldPrey, P newPrey);
	
}
