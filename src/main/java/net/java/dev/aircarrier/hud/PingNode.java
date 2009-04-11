package net.java.dev.aircarrier.hud;

import com.jme.math.Vector3f;
import com.jme.scene.Node;

/**
 * A node for display on a radar, which tracks a particular target
 * position.
 * @author goki
 */
public abstract class PingNode extends Node {
	
	public PingNode() {
		super();
	}

	public PingNode(String name) {
		super(name);
	}

	/**
	 * @return
	 * 		The position of the pinged target, in world space
	 */
	public abstract Vector3f getTargetPosition();
	
}
