package net.java.dev.aircarrier.physics;

/**
 * Listens for notification that a PhysicsSpaceExtended is about to update
 * @author shingoki
 */
public interface PreUpdateListener {

	/**
	 * Called when a PhysicsSpaceExtended is about to update
	 * @param time
	 * 		Time update will be called with
	 * @param space
	 * 		Space update is about to be called on
	 */
	public void preUpdate(float time, PhysicsSpaceExtended space);
	
}
