package net.java.dev.aircarrier.scene;

import com.jme.math.Vector3f;

/**
 * Something that is very roughly spherical
 * @author goki
 */
public interface ApproximateSphere {
	
	/**
	 * @return
	 * 		Approximate radius of spherical thing
	 */
	public float getRadius();
	
	/**
	 * @return
	 * 		Approximate center of spherical thing
	 */
	public Vector3f getPosition();
}
