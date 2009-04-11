package net.java.dev.aircarrier.scene.actree;

import com.jme.math.Vector3f;

public interface CollisionReceiver {

	/**
	 * Called when a collision occurs
	 * @param elapsedTime
	 * 		Time of contact
	 * @param position
	 * 		Position of {@link OctoBox} at contact
	 * 		THIS MUST BE COPIED WITHIN THIS METHOD - it may
	 * (probably will) be mutated by the collider after {@link #acceptCollision(float, Vector3f, int, Vector3f)}
	 * returns
	 * 
	 * @param collisionAxis
	 * 		The axis perpendicular to the colliding faces
	 * @param collisionCenter
	 * 		The center (average) position of contact
	 * 		THIS MUST BE COPIED WITHIN THIS METHOD - it may
	 * (probably will) be mutated by the collider after {@link #acceptCollision(float, Vector3f, int, Vector3f)}
	 * returns
	 * 
	 * @return
	 * 		True if more collisions should be scanned for, false if the
	 * {@link OctoBox} should be left at this collision, and no more collisions
	 * scanned for
	 */
	public boolean acceptCollision(float elapsedTime, Vector3f position, int collisionAxis, Vector3f collisionCenter);
	
}
