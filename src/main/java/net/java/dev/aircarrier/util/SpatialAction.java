package net.java.dev.aircarrier.util;

import com.jme.scene.Spatial;

/**
 * Interface for an action on a spatial
 * @author goki
 *
 */
public interface SpatialAction {

	/**
	 * Apply the action to a spatial
	 * @param spatial
	 * 		The spatial to act on
	 */
	public void actOnSpatial(Spatial spatial);
	
	/**
	 * Apply the action to a spatial
	 * @param spatial
	 * 		The spatial to act on
	 * @param level
	 * 		The number of levels deep the spatial lies in a tree
	 * 		(starting from a root Node at level 0, and moving to its children
	 * 		at level 1, their children at level 2, etc.)
	 * 
	 */
	public void actOnSpatial(Spatial spatial, int level);

	
}
