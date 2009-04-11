package net.java.dev.aircarrier.scene;

import com.jme.math.Vector3f;
import com.jme.scene.Node;

/**
 * Simple node subclass, for nodes which can be very roughly
 * approximated by spheres
 * @author goki
 *
 */
public class ApproximatelySphericalNode extends Node implements
		ApproximateSphere {
	private static final long serialVersionUID = 7003166238018086669L;
	
	float radius;
	
	public ApproximatelySphericalNode(float radius) {
		super();
		this.radius = radius;
	}

	public ApproximatelySphericalNode(String name, float radius) {
		super(name);
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}

	public Vector3f getPosition() {
		return getWorldTranslation();
	}

}
