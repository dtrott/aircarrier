package net.java.dev.aircarrier.acobject;

import net.java.dev.aircarrier.input.action.NodeRotator;
import net.java.dev.aircarrier.input.action.NodeTranslator;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jme.scene.Spatial;

/**
 * Implements some methods of MovableAcobject, by moving
 * a spatial.
 * Note this assumes the spatial is attached to the root node, 
 * so that the spatial can be moced just by changing the local
 * translation
 * Also monitors the spatial position to report velocity,
 * this is just given by the movement of the spatial since
 * the last update of a controller registered on it.
 * 
 * @author shingoki
 */
public class MovableAcobjectSpatialDelegate extends Controller implements MovableAcobject {
	private static final long serialVersionUID = 3579965288056516212L;
	
	Spatial spatial;
	String name;
	String visibleName;
	float radius;

	Vector3f lastPosition = null;
	Vector3f velocity = new Vector3f();
	
	/**
	 * Create a delegate
	 * @param spatial
	 * 		The spatial to get/set position and rotation of
	 * @param name
	 * 		The name to report
	 * @param visibleName
	 * 		The visible name to report
	 * @param radius
	 * 		The radius to report
	 */
	public MovableAcobjectSpatialDelegate(Spatial spatial, String name, String visibleName, float radius) {
		super();
		this.spatial = spatial;
		this.name = name;
		this.visibleName = visibleName;
		this.radius = radius;
		spatial.addController(this);
	}

	public void moveGlobal(int axis, float distance) {
		spatial.getLocalTranslation().set(axis, spatial.getLocalTranslation().get(axis) + distance);
	}

	public void moveGlobal(Vector3f translation) {
		spatial.getLocalTranslation().addLocal(translation);
	}

	public void moveLocal(int axis, float distance) {
		NodeTranslator.translate(spatial, axis, distance);
	}

	public void moveLocal(Vector3f translation) {
		for (int i = 0; i < 3; i++) {
			moveLocal(i, translation.get(i));
		}
	}

	public void rotateLocal(int axis, float angle) {
		NodeRotator.rotate(spatial, axis, angle);
	}

	public void setRotation(Quaternion rotation) {
		spatial.getLocalRotation().set(rotation);
	}

	public Quaternion getRotation() {
		return spatial.getWorldRotation();
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public String getVisibleName() {
		return visibleName;
	}

	public Vector3f getPosition() {
		return spatial.getWorldTranslation();
	}

	public float getRadius() {
		return radius;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public void setVisibleName(String visibleName) {
		this.visibleName = visibleName;
	}

	@Override
	public void update(float time) {
		
		//On first update, just remember the position for next update,
		//velocity will stay as initial 0,0,0
		if (lastPosition == null) {
			lastPosition = new Vector3f();
			lastPosition.set(getPosition());
			return;
		}
		
		//Velocity
		velocity.set(getPosition());
		velocity.subtractLocal(lastPosition);
		velocity.divideLocal(time);
		
		//Remember position for next update
		lastPosition.set(getPosition());
	}

	public void setPosition(Vector3f position) {
		spatial.getLocalTranslation().set(position);
	}

}
