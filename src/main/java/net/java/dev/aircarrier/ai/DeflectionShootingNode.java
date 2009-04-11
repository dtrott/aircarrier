package net.java.dev.aircarrier.ai;

import net.java.dev.aircarrier.acobject.Acobject;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;

/**
 * This node will position itself at the position a gun should fire, to hit a target node, 
 * some time in the future, allowing for movement of the target node, and a fixed
 * Must be attached to root node to work properly
 * @author goki
 */
public class DeflectionShootingNode extends Node implements Acobject {
	private static final long serialVersionUID = -8864273651002529351L;
	
	Acobject hunter;
	Acobject target;
	boolean newTarget = true;
	Vector3f lastPosition = new Vector3f();
	Vector3f v = new Vector3f();
	Vector3f w = new Vector3f();
	float bulletSpeed;
	
	Vector3f lastDeflectionPosition = new Vector3f();
	Vector3f deflectionVelocity = new Vector3f();
	
	public DeflectionShootingNode(Acobject hunter, float bulletSpeed) {
		super();
		this.hunter = hunter;
		this.bulletSpeed = bulletSpeed;
	}

	/**
	 * @return
	 * 		The Acobject to track, and whose movements we predict
	 */
	public Acobject getTarget() {
		return target;
	}

	/**
	 * Set new Acobject to track
	 * @param target
	 * 		The Acobject to track, and whose movements we predict
	 */
	public void setTarget(Acobject target) {
		this.target = target;
		newTarget = true;
	}

	public float getBulletSpeed() {
		return bulletSpeed;
	}

	public void setBulletSpeed(float bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}

	/**
	 * Update the prediction node - move it to the aiming position
	 * @param time
	 * 		The time since last update (used to work out target velocity)
	 */
	public void update(float time) {
		
		//If we have no target, just move to origin
		if (target == null) {
			getLocalTranslation().set(0, 0, 0);
			return;
		}
		
		//On first update, store last position of target, and move ourselves
		//onto it. Set our velocity to zero and store our position
		if (newTarget) {
			lastPosition.set(target.getPosition());
			getLocalTranslation().set(target.getPosition());
			newTarget = false;
			deflectionVelocity.set(0,0,0);
			lastDeflectionPosition.set(getPosition());
			return;
		}
		Vector3f targetPosition = target.getPosition();
		
		//Work out velocity from last update
		v.set(targetPosition);
		v.subtractLocal(lastPosition);
		v.divideLocal(time);

		//Work out offset to target
		w.set(targetPosition);
		w.subtractLocal(hunter.getPosition());
		
		//Work out quadratic terms
		float a = v.lengthSquared() - bulletSpeed*bulletSpeed;
		float b = 2 * w.dot(v);
		float c = w.lengthSquared();
		
		//Work out solution for time (the positive one)
		float t = (-b - FastMath.sqrt(b*b - 4*a*c))/(2*a);
		
		if (!Float.isNaN(t)) {
			//Work out our aim position, this is the target velocity * time of intercept,
			//added to current target position. If we aim here, we predict we will hit
			w.set(v);
			w.multLocal(t);
			w.addLocal(targetPosition);
			getLocalTranslation().set(w);
		} else {
			getLocalTranslation().set(targetPosition);
		}
		
		//Calculate our own (deflection node) velocity
		deflectionVelocity.set(getPosition());
		deflectionVelocity.subtractLocal(lastDeflectionPosition);
		deflectionVelocity.divideLocal(time);
		
		//Store new last positions
		lastPosition.set(targetPosition);
		lastDeflectionPosition.set(getPosition());
	}

	public Quaternion getRotation() {
		return getWorldRotation();
	}

	public Vector3f getVelocity() {
		return deflectionVelocity;
	}

	public String getVisibleName() {
		return "Targetting " + target.getVisibleName();
	}

	public Vector3f getPosition() {
		return getWorldTranslation();
	}

	public float getRadius() {
		return 1;
	}
}
