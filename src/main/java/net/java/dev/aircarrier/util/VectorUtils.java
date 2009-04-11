package net.java.dev.aircarrier.util;

import com.jme.math.Vector3f;

public class VectorUtils {

	/**
	 * Linearly interpolate from one vector to another, according to
	 * an amount, and store in a third vector
	 * @param amount
	 * 		The amount - 0 will produce "from" vector, 1 will give
	 * 		"to" vector, in between will move linearly from one to 
	 * 		the other
	 * @param from
	 * 		The starting vector
	 * @param to
	 * 		The ending vector
	 * @param store
	 * 		The vector to store result - a new vector is created if
	 * 		this is null
	 * @return
	 * 		The calculated vector, either store, or a new Vector if 
	 * 		store was null
	 */
	public static Vector3f lerp(float amount, Vector3f from, Vector3f to, Vector3f store) {
		
		//Make new vector if we weren't given one
		if (store == null) store = new Vector3f();
		
		//lerp each coordinate
		store.setX((1-amount) * from.getX() + amount * to.getX());
		store.setY((1-amount) * from.getY() + amount * to.getY());
		store.setZ((1-amount) * from.getZ() + amount * to.getZ());
		
		return store;
	}
	
}
