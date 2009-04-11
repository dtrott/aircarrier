/*
 *  $Id: AFace.java,v 1.6 2008/06/29 00:00:39 shingoki Exp $
 *
 * 	Copyright (c) 2005-2006 shingoki
 *
 *  This file is part of AirCarrier, see http://aircarrier.dev.java.net/
 *
 *    AirCarrier is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.

 *    AirCarrier is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.

 *    You should have received a copy of the GNU General Public License
 *    along with AirCarrier; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package net.java.dev.aircarrier.scene.actree;

import java.nio.FloatBuffer;

import net.java.dev.aircarrier.util.TextureLoader;
import net.java.dev.aircarrier.util.occlusion.Transform;

import com.jme.image.Texture;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.TriMesh;
import com.jme.scene.TexCoords;
import com.jme.util.geom.BufferUtils;

/**
 * <code>AFace</code> forms one face of a cube with the correct geometry and methods to
 * be a leaf in an Actree.
 * The face has an odd geometry, with 4 tris , meeting at an extra vert in the
 * center of the face. This allows for diagonal shadows to be cast across the
 * face in either direction, as well as for slightly more detail in vertex lighting.
 * The faces have distinct vertices at the corners to allow for sharp edged shadows
 * via vertex colours.
 *
 * General systems used for coordinates etc. in this class:
 *
 * The cardinal directions are:
 * 0	+Z		Up		Top
 * 1	+X		East	Right
 * 2	+Y		North	Back
 * 3	-X		West	Left
 * 4	-Y		South	Front
 * 5	-Z		Down	Bottom
 *
 * The "Top" etc. descriptions refer to the faces of the cube that
 * have their normal along the cardinal direction.
 *
 * So for example, the cube's faces are numbered in this order
 *
 * On each face (for example for the purposes of texture mapping), there
 * is a face-local "up" or "local Y" vector, this is as given below:
 *
 * 0	Top		+Y
 * 1	Right	+Z
 * 2	Back	+Z
 * 3	Left	+Z
 * 4	Front	+Z
 * 5	Bottom	-Y
 *
 * So the faces containing the Z axis have Z as the "up" or "local Y" vector. The top and bottom
 * have "up" so that if the cube is rotated around the X axis in order to point the
 * face towards a viewer looking along the Y axis, the local "up" vector will then
 * be aligned with the Z axis.
 *
 * The "right" or "local X" vector for a face is as expected - it is the "up" vector crossed
 * with the face normal, so that the up, right and out vectors form a right handed set.
 *
 * When the four tris of a cube face are indexed, they are indexed as:
 *
 * 0	Right
 * 1	Up
 * 2	Left
 * 3	Down
 *
 * 	in the face local directions.
 *
 * Finally, when 2D cardinal and diagonal directions are required (e.g. for occlusion), we use:
 *
 * 0	+X		East			Right
 * 1	+X+Y	North-East		Up-Right
 * 2	+Y		North			Up
 * 3	-X+Y	North-West		Up-Left
 * 4	-X		West			Left
 * 5	-X-Y	South-West		Down-Left
 * 6	-Y		South			Down
 * 7	+X-Y	South-East		Down-Right
 *
 * When dealing with directions relative to a cube face, X and Y are obviously the face-local X and Y (right and up)
 *
 * @author shingoki
 * @version $Id: AFace.java,v 1.6 2008/06/29 00:00:39 shingoki Exp $
 */
public class AFace extends TriMesh {

	//Temp calc vector
	final static Vector3f x = new Vector3f(1,0,0);
	final static Vector3f y = new Vector3f(0,1,0);
	final static Vector3f z = new Vector3f(0,0,1);
	final static Vector3f nx = new Vector3f(-1,0,0);
	final static Vector3f ny = new Vector3f(0,-1,0);
	final static Vector3f nz = new Vector3f(0,0,-1);

	/**
	 * The 3D cardinal directions, in standard order
	 * 0	+Z		Up		Top
	 * 1	+X		East	Right
	 * 2	+Y		North	Back
	 * 3	-X		West	Left
	 * 4	-Y		South	Front
	 * 5	-Z		Down	Bottom
	 */
	public final static Vector3f[] threeDCardinalDirections = new Vector3f[] {
		z,
		x,
		y,
		nx,
		ny,
		nz
	};

	/**
	 * The 3D cardinal directions, in standard order, as integer vectors
	 * 0	+Z		Up		Top
	 * 1	+X		East	Right
	 * 2	+Y		North	Back
	 * 3	-X		West	Left
	 * 4	-Y		South	Front
	 * 5	-Z		Down	Bottom
	 */
	public final static Vector3i[] intThreeDCardinalDirections;
	static {
		intThreeDCardinalDirections = new Vector3i[6];
		for (int i = 0; i < threeDCardinalDirections.length; i++) {
			intThreeDCardinalDirections[i] = new BVector3i(threeDCardinalDirections[i]);
		}
	}



	/**
	 * The components of the 2D cardinal directions, in terms of
	 * right and up (x and y). Indexed as
	 * cardinalComponent[cardinalDirection][component]
	 * where component is 0 for x/right and 1 for y/up
	 */
	public final static int[][] twoDcardinalComponents = new int[][] {
	//	 R   U
		{1,	 0},	//Right
		{1,  1},	//Up-Right
		{0,  1},	//Up
		{-1, 1},	//Up-Left
		{-1, 0},	//Left
		{-1,-1},	//Down-Left
		{0, -1},	//Down
		{1, -1},	//Down-Right
	};

	/**
	 * Index in the second dimension of faceLocalAxes
	 * and intFaceLocalAxes arrays.
	 * Looks up the "right" direction
	 */
	public final static int FACE_LOCAL_RIGHT = 0;

	/**
	 * Index in the second dimension of faceLocalAxes
	 * and intFaceLocalAxes arrays.
	 * Looks up the "up" direction
	 */
	public final static int FACE_LOCAL_UP = 1;

	/**
	 * Index in the second dimension of faceLocalAxes
	 * and intFaceLocalAxes arrays.
	 * Looks up the "normal" direction
	 * (out from the face)
	 */
	public final static int FACE_LOCAL_NORMAL = 2;

	/**
	 * This stores the x, y, z local axes of each face,
	 * in standard face ordering
	 * This is indexed as:
	 * 		faceLocalAxes[faceIndex][directionIndex]
	 * where directionIndex is:
	 * 		0 for right
	 * 		1 for up
	 * 		2 for out (normal to face)
	 */
	public static Vector3f[][] faceLocalAxes;

	/**
	 * This stores the x, y, z local axes of each face,
	 * in standard face ordering. The axes are as integer
	 * vectors. For example, these can be used for finding the
	 * adjacent faces in a grid of cubes.
	 * This is indexed as:
	 * 		intFaceLocalAxes[faceIndex][directionIndex]
	 * where directionIndex is:
	 * 		0 for right in face-space
	 * 		1 for up in face-space
	 * 		2 for out in face-space (normal to face)
	 */
	public static Vector3i[][] intFaceLocalAxes;
	static {
		intFaceLocalAxes = new Vector3i[6][3];
		//The "up" and "out" vectors are filled in first - these follow
		//directly from the face-local-up list in the class javadoc, and
		//the cardinal direction list in the class javadoc
		faceLocalAxes = new Vector3f[][] {
			    {null, y, z},
			    {null, z, x},
			    {null, z, y},
			    {null, z, nx},
			    {null, z, ny},
			    {null, ny, nz},
			};

		//Now we calculate the face-local-right vector for
		//each face from the other two
		for (int face = 0; face < 6; face++) {
			Vector3f out = faceLocalAxes[face][2];
			Vector3f up = faceLocalAxes[face][1];

			//Work out cross of up and out, gives "right" vector
			Vector3f right = new Vector3f(up);
			right.crossLocal(out);

			//Put back into faceLocalAxes
			faceLocalAxes[face][0] = right;

			//Derive int versions from float versions
			for (int axis = 0; axis < 3; axis++) {
				intFaceLocalAxes[face][axis] = new BVector3i(faceLocalAxes[face][axis]);
			}
		}
	}

	final static Vector3f tempFaceCorner = new Vector3f();
	//The vertex positions for each of the shared vertices of each face
	final static Vector3f[][] faceVerts = new Vector3f[6][5];
	static {
		for (int face = 0; face < 6; face++) {

			Vector3f out = faceLocalAxes[face][2];
			Vector3f up = faceLocalAxes[face][1];
			Vector3f right = faceLocalAxes[face][0];

			//Work out the corner of the cube
			tempFaceCorner.set(out);
			tempFaceCorner.subtractLocal(up);
			tempFaceCorner.subtractLocal(right);
			tempFaceCorner.multLocal(0.5f);

			for (int i = 0; i < 5; i++) {
				faceVerts[face][i] = new Vector3f();
			}

			//Build face vertex positions, corners anticlockwise from bottom right, then middle last
			//Bottom right, 0
			faceVerts[face][0].set(tempFaceCorner).addLocal(right);
			//Top right, 1
			faceVerts[face][1].set(tempFaceCorner).addLocal(right).addLocal(up);
			//Top left, 2
			faceVerts[face][2].set(tempFaceCorner).addLocal(up);
			//Bottom left, 3
			faceVerts[face][3].set(tempFaceCorner);
			//middle, 4
			faceVerts[face][4].set(up);
			faceVerts[face][4].addLocal(right);
			faceVerts[face][4].multLocal(0.5f);
			faceVerts[face][4].addLocal(tempFaceCorner);
		}

	}

	//Default corners and uv directions
	final static Vector2f uvCorner = new Vector2f(0, 0);
	final static Vector2f uvRight = new Vector2f(1, 0);
	final static Vector2f uvUp = new Vector2f(0, 1);


	final static Vector2f[] faceUVs = new Vector2f[5];
	static {
		for (int i = 0; i < 5; i++) {
			faceUVs[i] = new Vector2f();
		}

		//UV coords are set according to corner and vectors
		//Bottom right, 0
		faceUVs[0].set(uvCorner).addLocal(uvRight);
		//Top right, 1
		faceUVs[1].set(uvCorner).addLocal(uvRight).addLocal(uvUp);
		//Top left, 2
		faceUVs[2].set(uvCorner).addLocal(uvUp);
		//Bottom left, 3
		faceUVs[3].set(uvCorner);
		//middle, 4
		faceUVs[4].set(uvUp);
		faceUVs[4].addLocal(uvRight);
		faceUVs[4].multLocal(0.5f);
		faceUVs[4].addLocal(uvCorner);
	}

	final static FloatBuffer[] transformedUVBuffers = new FloatBuffer[8];
	static {
		for (int i = 0; i < 8; i++) {
			BufferUtils.createVector2Buffer(4 * 3);


		}
	}

	public final static Texture[] occlusionTextures = new Texture[51];
	static {
		for (int i = 0; i < 51; i++) {
			String s = Integer.toString(i+1);
			while (s.length() < 4) s = "0" + s;
			occlusionTextures[i] = TextureLoader.loadUncompressedTexture("resources/occlusion/" + s + ".png");
			occlusionTextures[i].setWrap(Texture.WrapMode.Clamp);
		}
	}

	/**
	 * How the tris of the visible faces are indexed, when looking
	 * at a cube along the (1, 1, 1) axis.
	 *
	 * From this view, the 3, 4, and 5 faces of the cube are visible,
	 * with the 3 face in the lower right of view, 4 in the lower left,
	 * and 5 at the top.
	 *
	 *       / \
	 *      / 5 \
	 *     /\   /\
	 *     | \ / |
	 *     |  |  |
	 *     |4 | 3|
	 *      \ | /
	 *       \|/
	 *
	 * The cube looks like a hexagon. We can make this into 6 triangles
	 * (viewtris) in the 2d view, by splitting each face of the cube in two,
	 * from the shared vertex of the faces to the non-shared vertices:
	 *
	 *       /|\
	 *      /2|1\
	 *     /\ | /\
	 *     | \|/ |
	 *     |3/|\0|
	 *     |/ | \|
	 *      \4|5/
	 *       \|/
	 *
	 * These viewtris are numbered starting from the right at 0, and proceeding
	 * anti clockwise.
	 *
	 * So, this array contains the indices needed to find the actual 3D tris
	 * that form each of the viewtris, indexed as
	 * int[] indices = viewtriIndices[viewtri]
	 *
	 * Each viewtri actually contains a pair of 3D tris. So the indices array
	 * gives the cube face index as the first element, then the
	 * index within that face of the first 3D tri, then the index of the second
	 * 3D tri.
	 *
	 * So for example... viewtri 0 is in the cube face 3, and contains 3D tri
	 * 2 and 3 of that cube face. (this is the first entry in the array below)
	 * This diagram shows the 3d tri indices in each view tri
	 *
	 *       /|\
	 *      /0|3\
	 *     /\1|2/\
	 *     |3\|/3|
	 *     |0/|\2|
	 *     |/2|0\|
	 *      \1|1/
	 *       \|/
	 *
	 */
	public final static int viewtriIndices[][] = new int[][]
	{
		//cube face, first 3D tri index, second 3D tri index

		{3, 2, 3},	//viewtri 0
		{5, 2, 3},	//viewtri 1
		{5, 0, 1},	//viewtri 2
		{4, 0, 3},	//viewtri 3
		{4, 1, 2},	//viewtri 4
		{3, 0, 1},	//viewtri 5
	};


	/**
	 * Buffer for first texture UV coords
	 */
	FloatBuffer tbuf;
	/**
	 * Buffer for second texture UV coords
	 */
	FloatBuffer tbuf2;

	/**
	 * Buffer for color values
	 */
	FloatBuffer cbuf;

	/**
	 * The index of this face within a cube
	 */
	int face;

	private static final long serialVersionUID = 1L;


	/**
	 * The order in which the verts are used in each tri of the
	 * cube face, indexed as:
	 * 		vertOrder[triIndex][vertIndex]
	 * where triIndex is the index of the triangle within the cube
	 * face, and vertIndex is the index of the vert in that triangle.
	 */
	final static int[][] vertOrder =
		new int[][]
		          {
					{0, 1, 4},
					{1, 2, 4},
					{2, 3, 4},
					{3, 0, 4},
		          };



	/**
	 * Constructor creates a new <code>Acube</code> object, which is a unit cube
	 * centered on 0,0,0
	 *
	 * @param name
	 * 		The name of the <code>Quad</code>.
	 */
	public AFace(String name, int face) {
		super(name);
		this.face = face;
		initialize(face);
	}

	/**
	 * The index of this face within its cube
	 * @return
	 * 		face index
	 */
	public int getFace() {
		return face;
	}

	/**
	 * <code>initialize</code> builds the data for the {@link AFace}
	 * object.
	 */
	public void initialize(int face) {

        //Face has 4 entirely separate tris, with 3 vertices
		setVertexCount(4 * 3);

		//We need enough vertices in the buffer
		setVertexBuffer(BufferUtils.createVector3Buffer(getVertexCount()));

		//One normal per vertex
		setNormalBuffer(BufferUtils.createVector3Buffer(getVertexCount()));

		//One UV position per vertex
        tbuf = BufferUtils.createVector2Buffer(getVertexCount());
        setTextureCoords(new TexCoords(tbuf, 2), 0);

        tbuf2 = BufferUtils.createVector2Buffer(getVertexCount());
        setTextureCoords(new TexCoords(tbuf2, 2), 1);

        //4 tris per face
	    setTriangleQuantity(4);

	    //3 indices per triangle
	    setIndexBuffer(BufferUtils.createIntBuffer(getTriangleCount() * 3));

	    //Set up the indices first - we just run through the verts in order, since
	    //there are no shared verts, and verts are added in the order they are used in triangles
	    for (int i = 0; i < getTriangleCount() * 3; i++) {
			getIndexBuffer().put(i);
	    }

	    cbuf = BufferUtils.createFloatBuffer(4 * getVertexCount());
	    setColorBuffer(cbuf);

	    setFaceColor(new ColorRGBA(244f/255f, 236f/255f, 222f/255f, 1f));

	    //Set up each face in turn, this also fills out faceLocalAxes
	    buildCubeFace(face);

	}

	/**
	 * Set the color of a given triangle of the face
	 * @param tri
	 * 		The triangle
	 * @param color
	 * 		The color
	 */
	public void setTriColor(int tri, ColorRGBA color) {
		//There are 4 floats per vertex, and 3 vertices per tri
		cbuf.position(4 * 3 * tri);

		//Put the color into each of the three vertices, as r,g,b,a
		cbuf.put(color.r);
		cbuf.put(color.g);
		cbuf.put(color.b);
		cbuf.put(color.a);

		cbuf.put(color.r);
		cbuf.put(color.g);
		cbuf.put(color.b);
		cbuf.put(color.a);

		cbuf.put(color.r);
		cbuf.put(color.g);
		cbuf.put(color.b);
		cbuf.put(color.a);
	}

	/**
	 * Set the color of each triangle in the face
	 * @param color
	 * 		The color
	 */
	public void setFaceColor(ColorRGBA color) {
		for (int i = 0; i < 4; i++) {
			setTriColor(i, color);
		}
	}

	/**
	 * Add a face to the triangle and UV buffers
	 * 	@param face
	 * 		The direction of face to add
	 */
	private void buildCubeFace(int face) {

		//Build each Tri
		for (int i = 0; i < 4; i++) {
			buildTri(face, i);
		}

	}

	/**
	 * Set the UV positions to apply a transform to the display
	 * of the occlusion texture on the face
	 * @param transform
	 * 		The transform required for the occlusion texture
	 */
	public void setUVTransform(Transform transform){
		tbuf.rewind();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {

				//The index we use into the faceUVs array, given no
				//transform
				int vertIndex = vertOrder[i][j];

				//Now work out where we would be if transformed

				//Index 4 is never moved, since it is at the center
				//of the face
				if (vertIndex != 4) {

					//Flip vertically
					if (transform.getFlip()) {
						if (vertIndex == 0) {
							vertIndex = 1;
						} else if (vertIndex == 1) {
							vertIndex = 0;
						} else if (vertIndex == 2) {
							vertIndex = 3;
						} else { // 3
							vertIndex = 2;
						}
					}

					//Rotate mod 4
					//Use negative rotation since we are rotating the positions,
					//so the image rotates the other way, unless we are flipped,
					//in which case go the other way
					int r = transform.getRotate();
					if (transform.getFlip()) r = -r;
					vertIndex = (vertIndex - r) % 4;
					if (vertIndex < 0) vertIndex += 4;
				}

				//Get the UV positions from faceUVs array
				Vector2f faceUV = faceUVs[vertIndex];
				tbuf.put(faceUV.x).put(faceUV.y);
			}
		}
	}

	/**
	 * Put entries in the vertex and normal buffers of the batch (at current position)
	 * in order to build a tri according to the current contents of faceVerts
	 * @param face
	 * 		The cube face being built
	 * @param i
	 * 		The tri being built
	 */
	private void buildTri(int face, int i) {

		Vector3f normal = faceLocalAxes[face][2];

		for (int j = 0; j < 3; j++) {

			//The index we use into the faceVerts and faceUVs arrays
			int vertIndex = vertOrder[i][j];

			//Get the vert position from faceVerts array
			Vector3f faceVert = faceVerts[face][vertIndex];
			getVertexBuffer().put(faceVert.x).put(faceVert.y).put(faceVert.z);

			//Get the UV positions from faceUVs array
			Vector2f faceUV = faceUVs[vertIndex];
			tbuf.put(faceUV.x).put(faceUV.y);
			tbuf2.put(faceUV.x).put(faceUV.y);

			//Put the normal directly as specified
			getNormalBuffer().put(normal.x).put(normal.y).put(normal.z);
		}
	}



}
