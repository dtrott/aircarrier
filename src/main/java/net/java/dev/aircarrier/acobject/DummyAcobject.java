package net.java.dev.aircarrier.acobject;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * A class for testing Acobject, which just returns dummy values
 * @author shingoki
 *
 */
public class DummyAcobject implements Acobject {

	Quaternion rotation = new Quaternion();
	Vector3f position = new Vector3f();
	Vector3f velocity = new Vector3f();
	
	String name;

	/**
	 * Create a summy Acobject
	 * @param name
	 * 		The name for the object
	 */
	public DummyAcobject(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return "DummyAcobject";
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public String getVisibleName() {
		return "DummyAcobjectVisibleName";
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRadius() {
		return 10;
	}
	
	public String toString() {
		return "DummyAcobject name '" + getName() + "'";
	}

}
