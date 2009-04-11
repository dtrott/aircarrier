package net.java.dev.aircarrier.physics;

import java.util.ArrayList;
import java.util.List;

import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector3f;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.Joint;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.PhysicsSpace;
import com.jmex.physics.PhysicsUpdateCallback;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;

public class PhysicsSpaceWrapper implements PhysicsSpaceExtended {

	PhysicsSpace delegate;

	List<PreUpdateListener> preupdateListeners = new ArrayList<PreUpdateListener>();
	List<PostUpdateListener> postupdateListeners = new ArrayList<PostUpdateListener>();
	
	/**
	 * Create a physics space wrapper with the specified delegate.
	 * @param delegate
	 * 		This will have calls passed through to it as appropriate
	 */
	public PhysicsSpaceWrapper(PhysicsSpace delegate) {
		super();
		this.delegate = delegate;
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#addToUpdateCallbacks(com.jmex.physics.PhysicsUpdateCallback)
	 */
	public boolean addToUpdateCallbacks(PhysicsUpdateCallback value) {
		return delegate.addToUpdateCallbacks(value);
	}


	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#createDynamicNode()
	 */
	public DynamicPhysicsNode createDynamicNode() {
		return delegate.createDynamicNode();
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#createJoint()
	 */
	public Joint createJoint() {
		return delegate.createJoint();
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#createStaticNode()
	 */
	public StaticPhysicsNode createStaticNode() {
		return delegate.createStaticNode();
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#delete()
	 */
	public void delete() {
		delegate.delete();
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return delegate.equals(obj);
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#getCollisionEventHandler()
	 */
	public SyntheticButton getCollisionEventHandler() {
		return delegate.getCollisionEventHandler();
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#getDefaultMaterial()
	 */
	public Material getDefaultMaterial() {
		return delegate.getDefaultMaterial();
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#getDirectionalGravity(com.jme.math.Vector3f)
	 */
	public Vector3f getDirectionalGravity(Vector3f store) {
		return delegate.getDirectionalGravity(store);
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#getJoints()
	 */
	public List<? extends Joint> getJoints() {
		return delegate.getJoints();
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#getNodes()
	 */
	public List<? extends PhysicsNode> getNodes() {
		return delegate.getNodes();
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#hashCode()
	 */
	public int hashCode() {
		return delegate.hashCode();
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#removeAllFromUpdateCallbacks()
	 */
	public void removeAllFromUpdateCallbacks() {
		delegate.removeAllFromUpdateCallbacks();
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#removeFromUpdateCallbacks(com.jmex.physics.PhysicsUpdateCallback)
	 */
	public boolean removeFromUpdateCallbacks(PhysicsUpdateCallback value) {
		return delegate.removeFromUpdateCallbacks(value);
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#setDefaultMaterial(com.jmex.physics.material.Material)
	 */
	public void setDefaultMaterial(Material value) {
		delegate.setDefaultMaterial(value);
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#setDirectionalGravity(com.jme.math.Vector3f)
	 */
	public void setDirectionalGravity(Vector3f gravity) {
		delegate.setDirectionalGravity(gravity);
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#setupBinaryClassLoader(com.jme.util.export.binary.BinaryImporter)
	 */
	public void setupBinaryClassLoader(BinaryImporter importer) {
		delegate.setupBinaryClassLoader(importer);
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#toString()
	 */
	public String toString() {
		return delegate.toString();
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.physics.PhysicsSpaceExtended#update(float)
	 */
	public void update(float time) {
		for (PreUpdateListener listener : preupdateListeners) {
			listener.preUpdate(time, this);
		}
		delegate.update(time);
		for (PostUpdateListener listener : postupdateListeners) {
			listener.postUpdate(time, this);
		}
	}

	public void addPostUpdateListener(PostUpdateListener listener) {
		postupdateListeners.add(listener);
	}

	public void addPreUpdateListener(PreUpdateListener listener) {
		preupdateListeners.add(listener);
	}

	public void removePostUpdateListener(PostUpdateListener listener) {
		postupdateListeners.remove(listener);
	}

	public void removePreUpdateListener(PreUpdateListener listener) {
		preupdateListeners.remove(listener);
	}
	
}
