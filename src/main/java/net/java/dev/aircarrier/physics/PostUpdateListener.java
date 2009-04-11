package net.java.dev.aircarrier.physics;

/**
 * Listens for notification that a PhysicsSpaceExtended is about to update
 * @author shingoki
 */
public interface PostUpdateListener {

	/**
	 * Called when a PhysicsSpaceExtended has just updated
	 * @param time
	 * 		Time update was called with
	 * @param space
	 * 		Space update has just been called on
	 */
	public void postUpdate(float time, PhysicsSpaceExtended space);
	
}
