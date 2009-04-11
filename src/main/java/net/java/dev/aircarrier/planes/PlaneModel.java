package net.java.dev.aircarrier.planes;

import java.util.List;

import net.java.dev.aircarrier.scene.ApproximatelySphericalNode;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

public interface PlaneModel {

	public List<Node> getGunPositions();

	public List<Node> getPropPositions();

	public Node getModel();

	public List<Node> getDamagePositions();

	public List<Node> getForwardFlaps();

	public List<Node> getRearFlaps();

	public List<Node> getBulletBounds();

	public List<Node> getWreckage();
	
	public List<Node> getWreckageStartPositions();

	public ApproximatelySphericalNode getAvoidanceNode();
	
	public Spatial getPhysicsBounds();
	
	public Vector3f getCameraOffset();
}