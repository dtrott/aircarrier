package net.java.dev.aircarrier.acobject;

import net.java.dev.aircarrier.scene.ApproximateSphere;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public interface Acobject extends ApproximateSphere {

	public String getName();
	
	public String getVisibleName();
	
	public Quaternion getRotation();
	
	public Vector3f getVelocity();
	
}
