/*
 *  $Id: Grid.java,v 1.1 2007/03/18 22:44:19 shingoki Exp $
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

package net.java.dev.aircarrier.scene;

import java.nio.FloatBuffer;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.TriMesh;
import com.jme.scene.batch.TriangleBatch;
import com.jme.util.geom.BufferUtils;

/**
 * <code>Grid</code> defines a four sided, two dimensional  rectangular shape. 
 * The local height of the <code>Quad</code> defines it's size about the y-axis, while
 * the width defines the x-axis. The z-axis will always be 0.
 * The plane is subdivided into one or more sections along each axis, to form a grid
 * of smaller quads.
 * 
 * @author shingoki
 * @version $Id: Grid.java,v 1.1 2007/03/18 22:44:19 shingoki Exp $
 */
public class Grid extends TriMesh {

	int xQuads;
	int yQuads;
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor creates a new <code>Quade</code> object with the provided
	 * width and height.
	 * 
	 * @param name
	 * 		The name of the <code>Quad</code>.
	 * @param width
	 *		The width of the <code>Quad</code>.
	 * @param height
	 *		The height of the <code>Quad</code>.
	 * @param xQuads
	 * 		The number of quads along the x axis
	 * @param yQuads
	 * 		The number of quads along the y axis
	 * @param centered
	 * 		If true, grid is centered on 0,0,0, otherwise the
	 * 		grid is from 0, 0, 0 to width, height, 0
	 */
	public Grid(String name, float width, float height, int xQuads, int yQuads, boolean centered) {
		super(name);
		if ( (xQuads < 1) || (yQuads < 1) ) throw new IllegalArgumentException("xQuads and yQuads must be greater than 0.");
		initialize(width, height, xQuads, yQuads, centered);
	}
	
	/**
	 * 
	 * <code>initialize</code> builds the data for the <code>Grid</code>
	 * object.
	 * 
	 * 
	 * @param width
	 *            the width of the <code>Quad</code>.
	 * @param height
	 *            the height of the <code>Quad</code>.
	 * @param xQuads
	 * 		The number of quads along the x axis
	 * @param yQuads
	 * 		The number of quads along the y axis
	 * @param centered
	 * 		If true, grid is centered on 0,0,0, otherwise the
	 * 		grid is from 0, 0, 0 to width, height, 0
	 *            
	 */
	public void initialize(float width, float height, int xQuads, int yQuads, boolean centered) {
		
		this.xQuads = xQuads;
		this.yQuads = yQuads;
		
        TriangleBatch batch = getBatch(0);
        
        //We have one more vertex than the number of quads, along each axis 
		batch.setVertexCount((xQuads + 1) * (yQuads + 1));
		
		//We need enough vertices in the buffer
		batch.setVertexBuffer(BufferUtils.createVector3Buffer(batch.getVertexCount()));
		
		//One normal per vertex
		batch.setNormalBuffer(BufferUtils.createVector3Buffer(batch.getVertexCount()));
	
		//One UV position per vertex
        FloatBuffer tbuf = BufferUtils.createVector2Buffer(batch.getVertexCount());
        setTextureBuffer(0,tbuf);
        
        //Two triangles per quad
	    batch.setTriangleQuantity(2 * xQuads * yQuads);
	    
	    //3 indices per triangle
	    batch.setIndexBuffer(BufferUtils.createIntBuffer(batch.getTriangleCount() * 3));

	    //Set vertex positions, in reading order
	    float offset = centered ? -0.5f : 0;
		for (int x = 0; x < xQuads + 1; x++) {
			for (int y = 0; y < yQuads + 1; y++) {
				batch.getVertexBuffer().put(width * (offset + ((float)x) / ((float)xQuads))).put(height * (offset + ((float)y) / ((float)yQuads))).put(0);
			}
		}

		//All normals face along z axis
		for (int i = 0; i < batch.getVertexCount(); i++) {
			batch.getNormalBuffer().put(0).put(0).put(1);
		}

		//Textures are set evenly from 0 to 1 on each axis, vertices are in reading order,
		//so texture coords are the same
		for (int x = 0; x < xQuads + 1; x++) {
			for (int y = 0; y < yQuads + 1; y++) {
				tbuf.put(((float)x) / ((float)xQuads)).put(((float)y) / ((float)yQuads));
			}
		}

	    setDefaultColor(ColorRGBA.white);

	    //Set up triangles. x and y are the top left of the quad we are building,
	    //moving through all verts that are not on the right column or bottom row
	    //We put a pair of tris:
	    //        __
	    //   |\   \ |
	    //   |_\   \|
	    for (int x = 0; x < xQuads; x++) {
		    for (int y = 0; y < yQuads; y++) {
		    	int topLeft = x + y * (xQuads + 1);

		    	//first triangle
			    batch.getIndexBuffer().put(topLeft);					//top left
			    batch.getIndexBuffer().put(topLeft + (xQuads + 1));		//down a line of verts
			    batch.getIndexBuffer().put(topLeft + (xQuads + 1) + 1);	//down a line, and one to the right

		    	//second triangle
			    batch.getIndexBuffer().put(topLeft);					//top left
			    batch.getIndexBuffer().put(topLeft + (xQuads + 1) + 1);	//down a line, and one to the right
			    batch.getIndexBuffer().put(topLeft + 1);				//one to the right
		    }	    	
	    }
	}

	/**
	 * Move a point within the grid
	 * @param xIndex
	 * 		The x index of the point, from 0 to xQuads
	 * @param yIndex
	 * 		The y index of the point, from 0 to yQuads
	 * @param x
	 * 		The new x coordinate of the point
	 * @param y
	 * 		The new y coordinate of the point
	 */
	public void movePoint(int xIndex, int yIndex, float x, float y) {
        TriangleBatch batch = getBatch(0);
		batch.getVertexBuffer().position((xIndex + yIndex * (xQuads + 1)) * 3);
		batch.getVertexBuffer().put(x).put(y).put(0);
	}
	
	/**
	 * Move the texture coord of a point within the grid
	 * @param xIndex
	 * 		The x index of the point, from 0 to xQuads
	 * @param yIndex
	 * 		The y index of the point, from 0 to yQuads
	 * @param x
	 * 		The new texture x coordinate of the point
	 * @param y
	 * 		The new texture y coordinate of the point
	 */
	public void moveUV(int xIndex, int yIndex, float x, float y) {
        TriangleBatch batch = getBatch(0);
		batch.getTextureBuffer(0).position((xIndex + yIndex * (xQuads + 1)) * 2);
		batch.getTextureBuffer(0).put(x).put(y);
	}

}
