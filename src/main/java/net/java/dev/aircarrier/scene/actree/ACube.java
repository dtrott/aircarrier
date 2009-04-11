package net.java.dev.aircarrier.scene.actree;

/**
 * A cube designed to manage (in addition to any other children)
 * 6 special {@link AFace} children, indexed by the standard
 * face direction indexing
 */
public class ACube extends Octode {
	private static final long serialVersionUID = 2332399670968259577L;
	
    /**
     * Default constructor.
     */
	public ACube() {
		super("ACube", 0);
	}

    /**
     * Constructor instantiates a new <code>ACube</code> with a default empty
     * list for containing children, and no faces set.
     * 
     * @param name
     *            the name of the scene element. This is required for
     *            identification and comparison purposes.
     */
	public ACube(String name) {
		super(name, 0);
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public Octode getChild(int x, int y, int z, boolean create) {
		throw new IllegalArgumentException();
	}

	@Override
	public Octode getChild(int x, int y, int z) {
		throw new IllegalArgumentException();
	}

	@Override
	public ACube getLeaf(int x, int y, int z, boolean create) {
		throw new IllegalArgumentException();
	}

	@Override
	public ACube getLeaf(int x, int y, int z) {
		throw new IllegalArgumentException();
	}

	@Override
	public int getLevel() {
		return 0;
	}

	@Override
	public void setLeaf(int x, int y, int z, ACube leaf) {
		throw new IllegalArgumentException();
	}

	/**
	 * The faces of the cube - null if face is not present
	 * (e.g. not built yet, or culled)
	 */
	AFace[] faces = new AFace[6];

	/**
	 * Attach a face to the cube.
	 * The face index of the face itself determines where the
	 * face is attached, and it will replace any face already 
	 * at that index.
	 * 
	 * @param face
	 * 		The face to attach
	 * @return
	 * 		The face that was replaced, or null if there was
	 * previously no face
	 */
	public AFace attachFace(AFace face) {
		AFace replaced = null;
		int f = face.getFace();
		if (faces[f] != null) {
			detachChild(faces[f]);
			replaced = faces[f];
		}
		faces[f] = face;
		super.attachChild(face);
		
		return replaced;
	}
	
	/**
	 * Detach the face at a given index (if one is
	 * present)
	 * @param f
	 * 		The index of the face to detach
	 * @return
	 * 		The face detached, or null if there
	 * was no face to detach
	 */
	public AFace detachFace(int f) {
		AFace detached = null;
		if (faces[f] != null) {
			detachChild(faces[f]);
			detached = faces[f];
		}
		faces[f] = null;
		
		return detached;
	}	
	
	/**
	 * Get the face at a given index
	 * @param f
	 * 		The face index
	 * @return
	 * 		The face at the index, or null if there
	 * is no such face
	 */
	public AFace getFace(int f) {
		return faces[f];
	}
	
	/**
	 * Check for a face at a given index
	 * @param f
	 * 		The face index
	 * @return
	 * 		True if there is a face at the index,
	 * or false if there is none
	 */
	public boolean hasFace(int f) {
		return faces[f] != null;
	}
	
}
