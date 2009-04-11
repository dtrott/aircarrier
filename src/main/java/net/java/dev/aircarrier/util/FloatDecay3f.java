package net.java.dev.aircarrier.util;

import com.jme.math.Vector3f;

public class FloatDecay3f {

	Vector3f position;
	
	float halfLife;
	
	Vector3f target;
	
	Vector3f v;
	
	ExponentialDecay decay;

	/**
	 * Make a decay with given half life
	 * @param halfLife
	 * 		The half life of the decay, time until the position moves half
	 * 		way to a fixed target, from an initial position at time 0
	 */
	public FloatDecay3f(float halfLife) {
		super();
		this.position = new Vector3f();
		this.halfLife = halfLife;
		target = new Vector3f();
		v = new Vector3f();
		decay = new ExponentialDecay(halfLife);
	}

	
	/**
	 * Update the position of the decay. This updates the "position" so that
	 * its offset from the target decays over time, and it approaches the target.
	 * It will actually never reach the target in theory, in practice it will get
	 * near enough to make no difference.
	 * @param time
	 * 		The elapsed time in seconds
	 */
	public void update(float time) {
		
		//Set v to target - position, this is the required movement
		v.set(position);
		v.subtractLocal(target);
		
		//Decay the offset
		v.multLocal(decay.evaluate(time));

		//Update the position to the new offset
		position.set(target);
		position.addLocal(v);
	}
	
	/**
	 * @return
	 * 		The current position of the simulated spring end point,
	 * 		changes as simulation is updated
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * @param position
	 * 		A new position for simulated spring end point
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getTarget() {
		return target;
	}

	public void setTarget(Vector3f target) {
		this.target = target;
	}

	/**
	 * @return
	 * 		The half life of the decay, time until the position moves half
	 * 		way to a fixed target, from an initial position at time 0
	 */
	public float getHalfLife() {
		return halfLife;
	}

	/**
	 * @param halfLife
	 * 		The half life of the decay, time until the position moves half
	 * 		way to a fixed target, from an initial position at time 0
	 */
	public void setHalfLife(float halfLife) {
		this.halfLife = halfLife;
		decay.setHalfLife(halfLife);
	}
}
