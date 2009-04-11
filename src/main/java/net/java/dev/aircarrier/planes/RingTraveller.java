package net.java.dev.aircarrier.planes;

import java.io.IOException;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.acobject.MovableAcobject;
import net.java.dev.aircarrier.acobject.MovableAcobjectSpatialDelegate;
import net.java.dev.aircarrier.ai.DockingController;
import net.java.dev.aircarrier.scene.ApproximateSphere;
import net.java.dev.aircarrier.scene.RingModel;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.terrain.TerrainPage;

public class RingTraveller extends Node implements MovableAcobject, ApproximateSphere {
	private static final long serialVersionUID = 8603901631197945924L;
	
	DockingController docking;
	MovableAcobjectSpatialDelegate movableDelegate;
	
	public RingTraveller(String name, TerrainPage terrain) throws IOException {
		super(name);
		movableDelegate = new MovableAcobjectSpatialDelegate(this, "Ring Traveller", "Ring Traveller", 12f);
		docking = new DockingController(this, terrain);
		this.addController(docking);
		this.attachChild(new RingModel().getModel());
	}

	public Acobject getTarget() {
		return docking.getTarget();
	}

	public void setTarget(Acobject target) {
		docking.setTarget(target);
	}

	public String getName() {
		return movableDelegate.getName();
	}

	public Vector3f getPosition() {
		return movableDelegate.getPosition();
	}

	public float getRadius() {
		return movableDelegate.getRadius();
	}

	public Quaternion getRotation() {
		return movableDelegate.getRotation();
	}

	public Vector3f getVelocity() {
		return movableDelegate.getVelocity();
	}

	public String getVisibleName() {
		return movableDelegate.getVisibleName();
	}

	public void moveGlobal(int axis, float distance) {
		movableDelegate.moveGlobal(axis, distance);
	}

	public void moveGlobal(Vector3f translation) {
		movableDelegate.moveGlobal(translation);
	}

	public void moveLocal(int axis, float distance) {
		movableDelegate.moveLocal(axis, distance);
	}

	public void moveLocal(Vector3f translation) {
		movableDelegate.moveLocal(translation);
	}

	public void rotateLocal(int axis, float angle) {
		movableDelegate.rotateLocal(axis, angle);
	}

	public void setRotation(Quaternion rotation) {
		movableDelegate.setRotation(rotation);
	}

	public void setPosition(Vector3f position) {
		movableDelegate.setPosition(position);
	}
	
}
