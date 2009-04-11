package net.java.dev.aircarrier.scene.actree;

import com.jme.math.Vector3f;

/**
 * Provides details of a collision
 */
public interface OctoCollision {

	/**
	 * Elapsed time at first contact (from start
	 * of scan at given velocity)
	 * @return
	 * 		elapsed time at contact
	 */
	public float getElapsedTime();
	
	/**
	 * The position of the {@link OctoBox} at first
	 * contact
	 * @return
	 * 		position at contact
	 */
	public Vector3f getPosition();
	
	/**
	 * The axis on which the collision occurred - 
	 * this is the index of the axis perpendicular to the
	 * colliding surfaces
	 * @return
	 * 		collision axis
	 */
	public int getCollisionAxis();
	
	/**
	 * The center of the collision surface, as an
	 * "average"
	 * @return
	 * 		collision center
	 */
	public Vector3f getCollisionCenter();
	
}
