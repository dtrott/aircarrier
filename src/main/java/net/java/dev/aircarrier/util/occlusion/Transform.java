package net.java.dev.aircarrier.util.occlusion;

/**
 *	Represents a Transform, where the cubes are optionally flipped in
 *	the y axis, then rotated by a given number of 90 degree CCW increments. 
 */
public class Transform{
	public boolean flip;
	public int rotate;
	
	int hashCode;
	String toString;
	
	private static Transform[] ALL_TRANSFORMS = null;
	
	/**
	 * Create a Transform
	 * @param flip
	 * 		If true, the transform includes a flip in the y axis before the
	 * 		rotation
	 * @param rotate
	 * 		The number of 90 degree CCW rotations after the flip
	 */
	public Transform(boolean flip, int rotate) {
		super();
		this.flip = flip;
		this.rotate = rotate;
		
		hashCode = 4 * (flip ? 1 : 0) + rotate;
		toString = (flip ? "Flip, then r" : "R") + "otate by " + rotate;
	}

	/**
	 * @return
	 * 		An array of all 8 distinct transforms, with unflipped rotations
	 * 		in order, then flipped rotations.
	 */
	public static Transform[] allTransforms() {
		if (ALL_TRANSFORMS == null) {
			ALL_TRANSFORMS = new Transform[8];
			for (int i = 0; i < 4; i++) {
				ALL_TRANSFORMS[i] = new Transform(false, i);
				ALL_TRANSFORMS[4 + i] = new Transform(true, i);
			}
		}
		return ALL_TRANSFORMS;
	}
	
	/**
	 * @return
	 * 		If true, the transform includes a flip in the y axis before the
	 * 		rotation
	 */
	public boolean getFlip() {
		return flip;
	}
	
	/**
	 * @return
	 * 		The number of 90 degree CCW rotations after the flip
	 */
	public int getRotate() {
		return rotate;
	}

	/**
	 * Get the inverse of this transform
	 * @return
	 * 		inverse
	 */
	public Transform getInverse() {
		//If this is not a flip, just use negative rotation
		if (!getFlip()) {
			int r = (-getRotate())%4;
			if (r < 0) r+= 4;
			return new Transform(false, r);
			
		//Otherwise transform is its own inverse
		} else {
			return this;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Transform) {
			Transform other = (Transform) obj;
			return ((getFlip() == other.getFlip()) && 
					(getRotate() == other.getRotate()));
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public String toString() {
		return toString;
	}
	
	public final static void main(String[] args) {
	
		//Check all occlusions work with inverse
		
		for (int i = 0; i < 256; i++) {
			
			Occlusion o = new Occlusion(i);
			
			for (Transform t : allTransforms()) {
				Occlusion transformed = o.transform(t);
				Occlusion transformedBack = transformed.transform(t.getInverse());
				
				if (!o.equals(transformedBack)) {
					System.out.println("Failed!");
					System.out.println("Original:");
					System.out.print(o.toLine());
					System.out.println("Transformed back:");
					System.out.print(transformedBack.toLine());
					System.out.println();
				}
			}
		}
		
	}
	
}