package net.java.dev.aircarrier.util;

import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.lod.AreaClodMesh;

/**
 * Only acts on TriMesh spatials, when they are found they are 
 * removed from their parent, and an AreaClodMesh based on the
 * TriMesh is attached back on to the parent. 
 */
public class SpatialClodinator implements SpatialAction {

	float trisPerPixel;
	
	/**
	 * Create a clodinator with default of 0.05f trisPerPixel
	 */
	public SpatialClodinator() {
		this(0.05f);
	}
	
	/**
	 * Create a clodinator
	 * @param trisPerPixel
	 * 		trisPerPixel to set on the created clod meshes
	 */
	public SpatialClodinator(float trisPerPixel) {
		this.trisPerPixel = trisPerPixel;
	}

	/**
	 * If the spatial is a trimesh, remove it from its parent,
	 * and attach an AreaClodMesh based on the mesh back on to the parent.
	 */
	public void actOnSpatial(Spatial spatial) {

		if (spatial instanceof TriMesh) {
			
			Node parent = spatial.getParent();
			
			TriMesh mesh = (TriMesh)spatial;
			
			AreaClodMesh clodMesh = new AreaClodMesh(mesh.getName(), mesh, null);
			clodMesh.setTrisPerPixel(trisPerPixel);
			parent.detachChild(spatial);
			parent.attachChild(clodMesh);
			//System.out.println("Clodded TriMesh: " + spatial);
		}
	}

	public void actOnSpatial(Spatial spatial, int level) {
		actOnSpatial(spatial);
	}

}
