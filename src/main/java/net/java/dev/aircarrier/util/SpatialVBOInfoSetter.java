package net.java.dev.aircarrier.util;

import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.VBOInfo;

/**
 * A spatial action which sets a new VBOInfo on any
 * TriMeshes, with a specified "enabled" value.
 * @author goki
 */
public class SpatialVBOInfoSetter implements SpatialAction {

	boolean enabled;
	
	public SpatialVBOInfoSetter(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void actOnSpatial(Spatial spatial) {
		if (spatial instanceof TriMesh) {
			((TriMesh)spatial).setVBOInfo(new VBOInfo(enabled));
			//System.out.println("Set TriMesh " + spatial + " VBOInfo: " + enabled);
		}
	}

	public void actOnSpatial(Spatial spatial, int level) {
		actOnSpatial(spatial);
	}

}
