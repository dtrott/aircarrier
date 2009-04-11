package net.java.dev.aircarrier.util;

import java.util.List;

import com.jme.scene.Node;
import com.jme.scene.Spatial;

/**
 * Utility methods for walking a tree of spatials from a root,
 * and acting on each spatial found using a {@link SpatialAction}
 * @author goki
 */
public class SpatialWalker {


	/**
	 * Walk the tree of nodes/children under this spatial 
	 * (if it is a node - just this spatial otherwise)
	 * and apply the specified action to each one.
	 * The root is at level 0 in the tree.
	 * @param root
	 * 		The root spatial from which to start
	 * @param action
	 * 		The action to apply
	 */
	public static void actOnSpatialTree(Spatial root, SpatialAction action) {
		actOnSpatialTree(root, action, 0);
	}
	
	
	/**
	 * Walk the tree of nodes/children under this spatial 
	 * (if it is a node - just this spatial otherwise)
	 * and apply the specified action to each one
	 * @param spatial
	 * 		The spatial from which to start
	 * @param action
	 * 		The action to apply
	 * @param level
	 * 		The level reached in the tree 
	 */
	public static void actOnSpatialTree(Spatial spatial, SpatialAction action, int level) {
		
		//Do this actual node first
		action.actOnSpatial(spatial, level);
		
		//Do children next (if it is has them)
		if (spatial instanceof Node) {
			List<Spatial> children = ((Node)spatial).getChildren();
			if (children != null) {
				for (Spatial c : children) {
					actOnSpatialTree(c, action, level+1);
				}
			}
		}
	}
	
	/**
	 * Use {@link SpatialWalker.SpatialPrinter} to print the tree of spatials
	 * starting from this spatial
	 * @param spatial
	 * 		The root of the tree
	 */
	public static void printSpatialTree(Spatial spatial) {
		actOnSpatialTree(spatial, new SpatialPrinter());
	}
	
	/**
	 * Simple action, just prints the spatial with tab level from tree level
	 */
	public static class SpatialPrinter implements SpatialAction {
		public void actOnSpatial(Spatial spatial) {
			actOnSpatial(spatial, 0);
		}
		public void actOnSpatial(Spatial spatial, int level) {
			for (int i = 0; i < level; i++) System.out.print("\t");
			System.out.println(spatial);
		}
	}
	
}
