package net.java.dev.aircarrier.acobject;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * An Acobject that can be moved around in a convenient way,
 * using relative movement in local and global axes, 
 * and rotation around local axes, as well as 
 * direct setting of position an rotation.
 * @author shingoki
 */
public interface MovableAcobject extends Acobject{

	/**
	 * Move in the local coordinate system
	 * @param axis
	 * 		The axis along which to move
	 * @param distance
	 * 		The distance to move
	 */
	public void moveLocal(int axis, float distance);
	
	/**
	 * Move in the global coordinate system
	 * @param axis
	 * 		The axis along which to move
	 * @param distance
	 * 		The distance to move
	 */
	public void moveGlobal(int axis, float distance);
	
	/**
	 * Move in the local coordinate system
	 * @param translation
	 * 		The vector to move in local coordinates
	 */
	public void moveLocal(Vector3f translation);
	
	/**
	 * Move in the global coordinate system
	 * @param translation
	 * 		The vector to move in global coordinates
	 */
	public void moveGlobal(Vector3f translation);
	
	/**
	 * Set the rotation of the object directly
	 * (note this sets to the VALUE of the rotation,
	 * it doesn't use the actual quaternion supplied - 
	 * this is copied)
	 * @param rotation
	 * 		The desired rotation
	 */
	public void setRotation(Quaternion rotation);

	/**
	 * Set the position of the object directly
	 * (note this sets to the VALUE of the position,
	 * it doesn't use the actual vector supplied - 
	 * this is copied)
	 * @param position
	 * 		The desired position
	 */
	public void setPosition(Vector3f position);
	
	/**
	 * Rotate in the local coordinate system
	 * @param axis
	 * 		The axis around which to rotate
	 * @param angle
	 * 		The angle to rotate
	 */
	public void rotateLocal(int axis, float angle);
	
}
