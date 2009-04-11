package net.java.dev.aircarrier.triggers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.input.action.NodeTranslator;
import net.java.dev.aircarrier.scene.Updatable;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;

/**
 * Triggered when an object passes through the z=0 plane, within
 * a certain radius of the Node's world translation.
 * @author shingoki
 *
 */
public class RingTrigger extends Node implements Trigger, Updatable {
	private static final long serialVersionUID = 8745730107002662384L;
	
	List<TriggerListener> listeners = new ArrayList<TriggerListener>();
	float radius;
	float radiusSquared;
	float minVelocityDot;
	
	//Store whether each tracked object was most recently on the positive Z side of
	//the node
	Map<Acobject, Boolean> sideMap = 
		new WeakHashMap<Acobject, Boolean>();
	
	Vector3f sideVector = new Vector3f();
	Vector3f tempOffset = new Vector3f();
	Vector3f normVelocity = new Vector3f();
	
	/**
	 * Create a trigger
	 * @param radius
	 * 		The activiation radius of the trigger
	 * @param minVelocityDot
	 * 		The minimum size of dot of normalized velocity
	 * 		to ring z axis. In other words, how "straight"
	 * 		you have to be going to trigger the ring,
	 * 		where just less than 1 would require the
	 * 		object to be going exactly in the z direction,
	 * 		and anything less than 0 would give no requirement.
	 * 		Since this is a dot, it is equal to cos of the
	 * 		maximum angle of the plane to the z axis. 
	 */
	public RingTrigger(float radius, float minVelocityDot) {
		this("RingTrigger", radius, minVelocityDot);
	}

	
	/**
	 * Create a trigger
	 * @param name
	 * 		The name of the trigger node
	 * @param radius
	 * 		The activiation radius of the trigger
	 * @param minVelocityDot
	 * 		The minimum size of dot of normalized velocity
	 * 		to ring z axis. In other words, how "straight"
	 * 		you have to be going to trigger the ring,
	 * 		where just less than 1 would require the
	 * 		object to be going exactly in the z direction,
	 * 		and anything less than 0 would give no requirement.
	 * 		Since this is a dot, it is equal to cos of the
	 * 		maximum angle of the plane to the z axis. 
	 */
	public RingTrigger(String name, float radius, float minVelocityDot) {
		super(name);
		setRadius(radius);
		this.minVelocityDot = minVelocityDot;
	}

	public void addTriggerListener(TriggerListener listener) {
		listeners.add(listener);
	}

	public void removeTriggerListener(TriggerListener listener) {
		listeners.remove(listener);
	}

	public void check(Acobject object) {

		//The side the object used to be on
		Boolean side = sideMap.get(object);

		//System.out.println(FastMath.sqrt(getWorldTranslation().distanceSquared(object.getPosition())) + ", " + sideDistance(object));

		
		//If the object is in the map already, we can look for a side
		//change
		if (side != null) {
	
			//See if the side changed
			if (side(object) != side) {
				
				//System.out.println("SWITCHED SIDES");
				//System.out.println(FastMath.sqrt(getWorldTranslation().distanceSquared(object.getPosition())) + ", " + sideDistance(object));
				
				//See if the object is in radius, and the velocity dot is ok
				float distanceSquared = getWorldTranslation().distanceSquared(object.getPosition());
				if (distanceSquared < radiusSquared) {
					normVelocity.set(object.getVelocity()).normalizeLocal();
					float dot = FastMath.abs(normVelocity.dot(sideVector));
					//System.out.println("dot " + dot + " minDot " + minVelocityDot);
					if (dot > minVelocityDot) {
						fireTriggered(object);
					}
				}
				
				//Put the correct side into the map
				sideMap.put(object, !side);
			}
			
		//No existing side, so create one
		} else {
			sideMap.put(object, side(object));
		}
	}

	/**
	 * Work out distance between ring and object, along ring z axis
	 * @param object
	 * 		The object to check
	 * @return
	 * 		distance
	 */
	public float sideDistance(Acobject object) {
		//Work out offset of the object from us
		tempOffset.set(object.getPosition());
		tempOffset.subtractLocal(getWorldTranslation());
		
		//Work out which side it is on
		return sideVector.dot(tempOffset);
	}
	
	/**
	 * Work out which side the object is on
	 * @param object
	 * 		The object to check
	 * @return
	 * 		True if the object is on the positive z side
	 * 		of this trigger
	 */
	public boolean side(Acobject object) {
		//Work out which side it is on
		return sideDistance(object) > 0;
	}
	
	public void update(float time) {
		//Update the side vector
		NodeTranslator.makeTranslationVector(this, 2, 1, sideVector);
	}

	/**
	 * @return
	 * 		The activation radius of the trigger
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * @param radius
	 * 		The activation radius of the trigger
	 */
	public void setRadius(float radius) {
		this.radius = radius;
		radiusSquared = radius * radius;
	}

	/**
	 * Fire triggered event to all listeners
	 * @param object
	 * 		The object we were triggered by
	 */
	private void fireTriggered(Acobject object) {
		for (TriggerListener listener : listeners) {
			listener.triggered(this, object);
		}
	}
	
}
