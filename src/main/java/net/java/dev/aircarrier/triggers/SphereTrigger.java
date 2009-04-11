package net.java.dev.aircarrier.triggers;

import java.util.ArrayList;
import java.util.List;

import net.java.dev.aircarrier.acobject.Acobject;

import com.jme.math.FastMath;
import com.jme.scene.Node;

public class SphereTrigger extends Node implements Trigger {
	private static final long serialVersionUID = 8745730107002662384L;
	
	List<TriggerListener> listeners = new ArrayList<TriggerListener>();
	float radius;
	float radiusSquared;
	
	/**
	 * Create a trigger
	 * @param radius
	 * 		The activiation radius of the trigger
	 */
	public SphereTrigger(float radius) {
		this("SphereTrigger", radius);
	}

	
	/**
	 * Create a trigger
	 * @param name
	 * 		The name of the trigger node
	 * @param radius
	 * 		The activiation radius of the trigger
	 */
	public SphereTrigger(String name, float radius) {
		super(name);
		setRadius(radius);
	}

	public void addTriggerListener(TriggerListener listener) {
		listeners.add(listener);
	}

	public void removeTriggerListener(TriggerListener listener) {
		listeners.remove(listener);
	}

	public void check(Acobject object) {
		float distanceSquared = getWorldTranslation().distanceSquared(object.getPosition());
		if (distanceSquared < radiusSquared) {
			fireTriggered(object);
			System.out.println(FastMath.sqrt(distanceSquared));
		}
	}

	public void update(float time) {
		//Nothing to do
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
