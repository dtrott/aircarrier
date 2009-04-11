package net.java.dev.aircarrier.util;

import com.jme.math.Vector3f;

public class FloatSpring3f {

	Vector3f position;
	
	float springK;
	
	float dampingK;
	
	Vector3f velocity;
	
	Vector3f target;
	
	Vector3f v;
	
	Vector3f damping;
	
	float speedMax = -1;
	
	/**
	 * Make a spring with given spring constant and damping constant
	 * @param springK
	 * 		Spring constant, the higher this is the "tighter" the spring,
	 * 		and the more force it will exert for a given extension
	 * @param dampingK
	 * 		Damping constant, the higher this is the stronger the damping,
	 * 		and the more "soggy" the movement.
	 */
	public FloatSpring3f(float springK, float dampingK) {
		super();
		this.position = new Vector3f();
		this.springK = springK;
		this.dampingK = dampingK;
		this.velocity = new Vector3f();
		target = new Vector3f();
		v = new Vector3f();
		damping = new Vector3f();
	}

	/**
	 * Create a critically damped spring (or near to critically damped)
	 * This spring will quickly move to its target without overshooting
	 * @param springK
	 * 		The spring constant - the higher this is, the more quickly the spring
	 * 		will reach its target. A value of 100 gives a reasonable response in 
	 * 		about a second, a higher value gives a faster response.
	 */
	public FloatSpring3f(float springK) {
		this (springK, (float)(2 * Math.sqrt(springK)));		
	}
	
	/**
	 * Update the position of the spring. This updates the "position" as if
	 * there were a damped spring stretched between the current position and
	 * the target position. That is, the spring will tend to pull the position towards the target,
	 * and if the spring is damped the position will eventually settle onto the target.
	 * @param time
	 * 		The elapsed time in seconds
	 */
	public void update(float time) {
		
		//Set v to target - position, this is the required movement
		v.set(position);
		v.subtractLocal(target);
		
		//Multiply displacement by spring constant to get spring force,
		//then subtract damping force
		v.multLocal(-springK);
		damping.set(velocity);
		damping.multLocal(dampingK);
		v.subtractLocal(damping);
		
		//v is now a force, so assuming unit mass it is also acceleration.
		//multiply by elapsed time to get velocity change
		v.multLocal(time);
		velocity.addLocal(v);
		
		//If velocity isn't valid, zero it
		if (!Vector3f.isValidVector(velocity)) {
			velocity.set(0, 0, 0);
		}
		
		//Cap velocity if required
		if (speedMax > 0) {
			if (velocity.lengthSquared() > speedMax * speedMax) {
				velocity.normalizeLocal();
				velocity.multLocal(speedMax);
			}
		}
		
		//Change the roll at the new velocity, for elapsed time
		v.set(velocity);
		v.multLocal(time);
		position.addLocal(v);
	}

	/**
	 * @return
	 * 		Damping constant, the higher this is the stronger the damping,
	 * 		and the more "soggy" the movement.
	 */
	public float getDampingK() {
		return dampingK;
	}

	/**
	 * @param dampingK
	 * 		Damping constant, the higher this is the stronger the damping,
	 * 		and the more "soggy" the movement.
	 */
	public void setDampingK(float dampingK) {
		this.dampingK = dampingK;
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

	/**
	 * @return
	 * 		The spring constant - the higher this is, the more quickly the spring
	 * 		will reach its target
	 */
	public float getSpringK() {
		return springK;
	}

	/**
	 * @param springK
	 * 		The spring constant - the higher this is, the more quickly the spring
	 * 		will reach its target
	 */
	public void setSpringK(float springK) {
		this.springK = springK;
	}

	/**
	 * @return
	 * 		The current velocity of the position
	 */
	public Vector3f getVelocity() {
		return velocity;
	}

	/**
	 * @param velocity
	 * 		A new value for the current velocity of the position
	 */
	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}

	public Vector3f getTarget() {
		return target;
	}

	public void setTarget(Vector3f target) {
		this.target = target;
	}

	public float getSpeedMax() {
		return speedMax;
	}

	public void setSpeedMax(float speedMax) {
		this.speedMax = speedMax;
	}
	
}
