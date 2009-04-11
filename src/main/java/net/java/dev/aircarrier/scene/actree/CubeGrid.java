package net.java.dev.aircarrier.scene.actree;

import java.util.HashMap;
import java.util.Map;

import jmetest.terrain.TestTerrain;
import net.java.dev.aircarrier.util.occlusion.IndexAndTransform;
import net.java.dev.aircarrier.util.occlusion.Occlusion;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * Stores a boolean presence, and a cube, for
 * each position in a 3d grid.
 * 
 * Note that setting presence does NOT create a cube,
 * but just marks the position so that tools can
 * create the cube later.
 * 
 * However, setting a cube DOES update presence accordingly - 
 * presence is true if cube is non-null.
 * 
 * Positions without a cube return null for the cube.
 * 
 * Geting cubes or presence outside the grid does not fail,
 * but simply returns no cube/presence. However setting
 * outside the grid DOES fail.
 */
public class CubeGrid {

	boolean[][][] presenceGrid;
	ACube[][][] cubeGrid;

	Octode octode;
	int size;
	
	public CubeGrid(int levels) {
		size = 1<<levels;
		presenceGrid = new boolean[size][size][size];
		cubeGrid = new ACube[size][size][size];
		octode = new Octode("Root", levels);
	}
	
	public int getSize() {
		return size;
	}

	public Octode getOctode() {
		return octode;
	}

	public boolean getPresence(Vector3i position) {
		try {
			return presenceGrid[position.getX()][position.getY()][position.getZ()];
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			return false;
		}
	}

	public void setPresence(Vector3i position, boolean present) {
		presenceGrid[position.getX()][position.getY()][position.getZ()] = present;
	}
	
	public ACube getCube(Vector3i position) {
		try {
			return cubeGrid[position.getX()][position.getY()][position.getZ()];
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			return null;
		}		
	}

	public void setCube(Vector3i position, ACube cube) {
		cubeGrid[position.getX()][position.getY()][position.getZ()] = cube;
		setPresence(position, (cube!=null));
	}

	public int size(int dimension) {
		if (dimension == 0) {
			return presenceGrid.length;
		} else if (dimension == 1) {
			return presenceGrid[0].length;
		} else {
			return presenceGrid[0][0].length;
		}
	}
	
	/**
	 * The iso coordinates of the view tris of the cube at (0,0,0).
	 * This also gives the offsets to apply to iso coordinates after
	 * the cube-center offset for cubes NOT at (0,0,0)
	 */
	public static int[][] isoOffsetsForViewTris = new int[][] {
		{-1,  0}, //0
		{ 0,  0}, //1
		{ 1,  0}, //2
		{ 0, -1}, //3
		{-1, -1}, //4
		{-2, -1}, //5
	};
	
	/**
	 * The iso offsets given by movement in each direction in 3D by one unit
	 */
	public static int[][] isoOffsets = new int[][] {
		{ 2,  0}, //x
		{ 0,  1}, //y
		{-2, -1}, //z
	};


	/**
	 * Work out the iso coordinate and layer of the given viewTri of a cube at a given
	 * position.
	 * The iso coordinates in iso x and iso y are returned as the first two coordinates
	 * of the vector, and the layer as the third (z).
	 * @param cubePos
	 * 		The position of the cube
	 * @param viewTri
	 * 		The tri of the cube in the view space
	 * @param target
	 * 		The target vector - if specified, it is changed to be the result, otherwise
	 * a new vector is made
	 * @return
	 * 		The iso and layer coordinates of the view tri as a vector. 
	 */
	public static BVector3i cubeViewTriToIso(BVector3i cubePos, int viewTri, BVector3i target) {
		
		//If we have no target, make one
		if (target == null) target = new BVector3i();
		
		//First work out which layer we are in, store in z component of target
		//Moving any integer vector in 3D moves you in the layers by the sum of the
		//components in each direction
		target.set(2, cubePos.getX() + cubePos.getY() + cubePos.getZ());
		
		//Now work out our iso offset for viewTri 0, given cube position,
		//store in x and y components of target (for x and y iso coords)
		for (int isoAxis = 0; isoAxis < 2; isoAxis++) {
			target.set(isoAxis, 0);
			for (int axis = 0; axis < 3; axis++) {
				target.set(isoAxis, target.get(isoAxis) + cubePos.get(axis) * isoOffsets[axis][isoAxis]);
			}
		}
		
		//Now add the iso offset for the view tri we want
		target.set(0, target.get(0) + isoOffsetsForViewTris[viewTri][0]);
		target.set(1, target.get(1) + isoOffsetsForViewTris[viewTri][1]);
		
		return target;
	}
	
	public void shade(ColorRGBA light, ColorRGBA shade, ColorRGBA[] additional) {
		Map<Vector2i, Integer> closestLayer = new HashMap<Vector2i, Integer>();

		BVector3i pos = new BVector3i();
		BVector3i isoAndLayer = new BVector3i();
		BVector2i iso = new BVector2i();
		
		for (int x = 0; x < size(0); x++) {
			for (int y = 0; y < size(1); y++) {
				for (int z = 0; z < size(2); z++) {
					pos.set(x, y, z);
					
					if (getCube(pos) != null) {
						for (int viewTri = 0; viewTri < 6; viewTri++) {
							//Get the iso and layer we are in, and unpack iso into 2d vector
							isoAndLayer = cubeViewTriToIso(pos, viewTri, isoAndLayer);
							iso.setComponents(isoAndLayer.getX(), isoAndLayer.getY());
							
							//System.out.println(pos + ":" + viewTri + "->" + isoAndLayer);
							
							//Find current closest layer at this iso
							Integer closest = closestLayer.get(iso);
							
							//If there is no current layer, or we are closer, then use this one
							if ((closest == null) || (isoAndLayer.getZ() < closest)) {
								closestLayer.put(new BVector2i(iso), isoAndLayer.getZ());
							}
						}
					}
				}
			}
		}

		//We now have a mapping from each iso that actually contains a cube viewTri to the
		//closest layer on which there is such a viewTri. Hence the closest viewTri gets the
		//lighting, and the rest are dark
		//Scan back through and do this
		for (int x = 0; x < size(0); x++) {
			for (int y = 0; y < size(1); y++) {
				for (int z = 0; z < size(2); z++) {
					pos.set(x, y, z);

					ACube cube = getCube(pos);
					if (cube != null) {
						
						//Make all faces dark first
						for (int f = 0; f < 6; f++) {
							AFace face = cube.getFace(f);
							if (face != null) {
								face.setFaceColor(shade.add(additional[f]));
							}
						}
						
						for (int viewTri = 0; viewTri < 6; viewTri++) {
							
							//Get the iso and layer we are in, and unpack iso into 2d vector
							isoAndLayer = cubeViewTriToIso(pos, viewTri, isoAndLayer);
							iso.setComponents(isoAndLayer.getX(), isoAndLayer.getY());

							//Find current closest layer at this iso
							Integer closest = closestLayer.get(iso);
							
							//If there is no closest, this is an error
							if (closest == null) {
								System.out.println("ERROR - no closest layer for a cube");
								
							//Otherwise shade faces
							} else {
								//Only closest gets light
								if (isoAndLayer.getZ() == closest) {
									//Work out which face and which tris we are shading
									int[] indices = AFace.viewtriIndices[viewTri];
									int f = indices[0];
									AFace face = cube.getFace(f);
									if (face != null) {
										ColorRGBA actualLight = light.add(additional[f]);
										//ColorRGBA actualShade = shade.add(additional[f]);
										//ColorRGBA actualColor = ColorRGBA.
										face.setTriColor(indices[1], actualLight);
										face.setTriColor(indices[2], actualLight);
									}
								}
							}
						}						
					}
				}
			}
		}
	}
	
	private final static BVector3i temp1 = new BVector3i();

	//private final static ColorRGBA baseColor = new ColorRGBA(0.84f*244f/255f, 0.84f*236f/255f, 0.8f*222f/255f, 1f);
	//private final static ColorRGBA baseDarkColor = new ColorRGBA(0.73f*244f/255f, 0.73f*236f/255f, 0.8f*222f/255f, 1f);
	
	//restrained monochrome
	//private final static ColorRGBA baseColor = new ColorRGBA(0.7f, 0.7f, 0.7f, 1f);
	//private final static ColorRGBA baseDarkColor = new ColorRGBA(0.65f, 0.65f, 0.65f, 1f);

	//Slightly brighter, slightly sunny
	public final static ColorRGBA DEFAULT_BASE_COLOR = new ColorRGBA(0.87f, 0.87f, 0.8f, 1f);
	public final static ColorRGBA DEFAULT_DARK_COLOR = new ColorRGBA(0.45f, 0.45f, 0.5f, 1f);

	public final static ColorRGBA[] DEFAULT_FACE_COLORS = new ColorRGBA[] {

		//Up, North, East
		makeAdjustedColor(ColorRGBA.black, 0.01f),
	    makeAdjustedColor(ColorRGBA.black, 0.05f),
	    makeAdjustedColor(ColorRGBA.black, 0.09f),

		//West, South, Down
	    makeAdjustedColor(ColorRGBA.black, 0.13f),
	    makeAdjustedColor(ColorRGBA.black, 0.17f),
	    makeAdjustedColor(ColorRGBA.black, 0.21f),
	    
	};
	
	/**
	 * Make adjusted color
	 * @param baseColor
	 * 		Base color to which to add
	 * @param adjustment
	 * 		Amount to add in R, G and B channels
	 * @return
	 * 		The adjusted color
	 */
	private final static ColorRGBA makeAdjustedColor(ColorRGBA baseColor, float adjustment) {
		return baseColor.add(new ColorRGBA(adjustment, adjustment, adjustment, 1f));
	}
	
	private final static Texture detail;
	static {
		detail = TextureManager.loadTexture(TestTerrain.class.getClassLoader().getResource(
				//"resources/blackBoxSSS.png"),
				"resources/render.png"),
				Texture.MM_LINEAR_LINEAR,
				Texture.FM_LINEAR);
	}
	
	public void buildAllCubes() {
		for (int x = 0; x < size(0); x++) {
			for (int y = 0; y < size(1); y++) {
				for (int z = 0; z < size(2); z++) {
					BVector3i pos = new BVector3i(x, y, z);
					buildCube(pos);
				}
				
			}
		}
	}
	
	private void buildCube(BVector3i pos) {
		
		if (!getPresence(pos)) return;
		
		ACube cube = new ACube("Cube at " + pos);

        for (int i = 0; i < 6; i++) {

        	Vector3i normal = AFace.intFaceLocalAxes[i][AFace.FACE_LOCAL_NORMAL];
        	
        	//Skip the face if it is occluded
    		temp1.set(pos);
        	temp1.addLocal(normal);
        	if (getPresence(temp1)) continue;
        	
        	AFace face = new AFace("Face " + i + " of Cube at " + pos, i);
        	cube.attachFace(face);
        	
        	//Work out the occlusions around the cube face
        	Vector3i up = AFace.intFaceLocalAxes[i][AFace.FACE_LOCAL_UP];
        	Vector3i right = AFace.intFaceLocalAxes[i][AFace.FACE_LOCAL_RIGHT];

        	boolean[] tempOcclusion = new boolean[8];

        	for (int direction = 0; direction < tempOcclusion.length; direction++) {
        		temp1.set(pos);
	        	temp1.addLocal(normal);
	        	temp1.addLocalMult(right, AFace.twoDcardinalComponents[direction][0]);
	        	temp1.addLocalMult(up, AFace.twoDcardinalComponents[direction][1]);
	        	
	        	tempOcclusion[direction] = getPresence(temp1);
        	}

        	Occlusion o = new Occlusion(tempOcclusion);
        	
        	//Work out required transform and texture
        	IndexAndTransform iat = Occlusion.occlusionToTransformedUnique.get(o);
        	
        	//System.out.println("Face " + i + " using texture index " + iat.getIndex());
        	//System.out.println(o);
        	
        	face.setUVTransform(iat.getTransform());

        	TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
            ts.setEnabled(true);
            Texture t = AFace.occlusionTextures[iat.getIndex()];
            t.setCombineFuncRGB(Texture.ACF_MODULATE);
            ts.setTexture(t, 0);
            
            if (detail != null) ts.setTexture(detail, 1);
            face.setRenderState(ts);
        	
        }
        
        setCube(pos, cube);
        
        //rootNode.attachChild(cube);
        
        octode.setLeaf(pos.getX(), pos.getY(), pos.getZ(), cube);
        
        cube.setLocalTranslation(new Vector3f(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f));
        cube.setModelBound(new BoundingBox());
        cube.updateModelBound();
        cube.updateWorldBound(); // We do this to allow the camera setup access to the world bound in our setup code.
	}

	
	
}
