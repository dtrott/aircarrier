package net.java.dev.aircarrier.ai;

/**
 * Simple sensor for AI - gives back a float value (normally from 0 to 1) indicating the degree to
 * which something is happening.
 * 
 * For example, the TerrainAvoidanceSteerer gives back a 0 if a node's heading keeps it over
 * minimum height at all distances checked, or a 1 if the node is already in the terrain.
 * 
 * Such sensors can be used to decide/blend between different steerings.
 * 
 * @author goki
 *
 */
public interface ScalarSensor {

	/**
	 * @return 
	 * 		A float value, normally in range 0 to 1, indicating the degree to
	 * 		which something is happening. 0 indicates that nothing is happening,
	 * 		1 that it could, like, not be happening any _more_. Dude.
	 */
	public float getIntensity();
	
}
