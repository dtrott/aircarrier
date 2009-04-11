package net.java.dev.aircarrier.util;

import com.jme.scene.Spatial;

/**
 * A spatial action which calls lockMesh on spatials
 * @author goki
 */
public class SpatialMeshLocker implements SpatialAction {

	public void actOnSpatial(Spatial spatial) {
		spatial.lockMeshes();
	}

	public void actOnSpatial(Spatial spatial, int level) {
		actOnSpatial(spatial);
	}

}
