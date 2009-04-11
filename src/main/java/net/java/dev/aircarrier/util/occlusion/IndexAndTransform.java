package net.java.dev.aircarrier.util.occlusion;

/**
 * Stores the index of an occlusion texture, and the transform
 * to apply to it. This can then be used to display an occlusion
 * texture for a particular task - normally in order to look like
 * another untransformed occlusion. 
 * @author shingoki
 */
public class IndexAndTransform {
	int index;
	Transform transform;
	
	/**
	 * Make an instance
	 * @param index
	 * 		The index of the occlusion texture
	 * @param transform
	 * 		The transform to apply to it
	 */
	public IndexAndTransform(int index, Transform transform) {
		super();
		this.index = index;
		this.transform = transform;
	}
	
	/**
	 * The index of the occlusion texture
	 * @return
	 * 		index
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * The index of the occlusion texture
	 * @return
	 * 		transform
	 */
	public Transform getTransform() {
		return transform;
	}
	
	
	
}
