package net.java.dev.aircarrier.physics;

import java.util.List;

import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector3f;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.PhysicsUpdateCallback;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;

/**
 * This is the interface that PhysicsSpace should have been (IMHO ;)
 * 
 * This allows for registering of callbacks that are called immediately
 * before and after update(), without the contract of not altering nodes, etc.
 * Also hopefully other extended functionality.
 * 
 * Use PhysicsSpaceWrapper on a PhysicsSpace as one way of getting an implementation.
 * 
 * @author shingoki
 *
 */
public interface PhysicsSpaceExtended {

	public void addPreUpdateListener(PreUpdateListener listener);
	public void addPostUpdateListener(PostUpdateListener listener);
	public void removePreUpdateListener(PreUpdateListener listener);
	public void removePostUpdateListener(PostUpdateListener listener);
	
	public abstract boolean addToUpdateCallbacks(PhysicsUpdateCallback value);

	//public abstract PhysicsCapsule createCapsule(String name, PhysicsNode node);

	//public abstract PhysicsCylinder createCylinder(String name, PhysicsNode node);

	public abstract DynamicPhysicsNode createDynamicNode();

	public abstract Joint createJoint();

	//public abstract PhysicsMesh createMesh(String name, PhysicsNode node);

	public abstract StaticPhysicsNode createStaticNode();

	public abstract void delete();

	public abstract boolean equals(Object obj);

	public abstract SyntheticButton getCollisionEventHandler();

	public abstract Material getDefaultMaterial();

	public abstract Vector3f getDirectionalGravity(Vector3f store);

	public abstract List<? extends Joint> getJoints();

	public abstract List<? extends PhysicsNode> getNodes();

	public abstract int hashCode();

	public abstract void removeAllFromUpdateCallbacks();

	public abstract boolean removeFromUpdateCallbacks(
			PhysicsUpdateCallback value);

	public abstract void setDefaultMaterial(Material value);

	public abstract void setDirectionalGravity(Vector3f gravity);

	public abstract void setupBinaryClassLoader(BinaryImporter importer);

	public abstract String toString();

	public abstract void update(float time);

}