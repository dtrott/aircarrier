/*
 *  $Id: EllipsoidNode.java,v 1.2 2006/07/21 23:59:50 shingoki Exp $
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

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

/**
 * <code>EllipsoidNode</code> defines a node that always orients towards the
 * camera, but also rotates around the axis from it to the camera, and scales,
 * so that if it contains a square quad, that quad will appear to be a view of an
 * ellipsoid (assuming it is textured with a circle). A blurry circle with alpha
 * map on a quad will make a good looking ellipsoid glow particle.
 * 
 * @author Mark Powell
 * @author Joshua Slack
 * @version $Id: EllipsoidNode.java,v 1.2 2006/07/21 23:59:50 shingoki Exp $
 */
public class EllipsoidNode extends Node {
	private static final long serialVersionUID = -6161452447411476126L;

	private float lastTime;

    //private Matrix3f orient;

    private Vector3f look;

    //private Vector3f left;
    private Vector3f up;
    //private Vector3f temp;
    private Quaternion tempQ;

    /**
     * Constructor instantiates a new <code>BillboardNode</code>. The name of
     * the node is supplied during construction.
     * 
     * @param name
     *            the name of the node.
     */
    public EllipsoidNode(String name) {
        super(name);
        //orient = new Matrix3f();
        look = new Vector3f();
        //left = new Vector3f();
        up = new Vector3f();
        //temp = new Vector3f();
        tempQ = new Quaternion();
    }

    /**
     * <code>updateWorldData</code> defers the updating of the billboards
     * orientation until rendering. This keeps the billboard from being
     * needlessly oriented if the player can not actually see it.
     * 
     * @param time
     *            the time between frames.
     * @see com.jme.scene.Spatial#updateWorldData(float)
     */
    public void updateWorldData(float time) {
        lastTime = time;
        updateWorldBound();
    }

    /**
     * <code>draw</code> updates the billboards orientation then renders the
     * billboard's children.
     * 
     * @param r
     *            the renderer used to draw.
     * @see com.jme.scene.Spatial#draw(com.jme.renderer.Renderer)
     */
    public void draw(Renderer r) {
        Camera cam = r.getCamera();
        rotateBillboard(cam);

        super.draw(r);
    }

    /**
     * rotate the billboard based on the type set
     * 
     * @param cam
     *            Camera
     */
    public void rotateBillboard(Camera cam) {
        // get the scale, translation and rotation of the node in world space
        updateWorldVectors();

        rotateScreenAligned(cam);

        for (int i = 0, cSize = getChildren().size(); i < cSize; i++) {
            Spatial child = (Spatial) getChildren().get(i);
            if (child != null) {
                child.updateGeometricState(lastTime, false);
            }
        }
    }

    /**
     * Rotate the billboard so it points directly opposite the direction the
     * camera's facing
     * 
     * @param camera
     *            Camera
     */
    private void rotateScreenAligned(Camera camera) {
    	/*
    	// coopt diff for our in direction:
        look.set(camera.getDirection()).negateLocal();
        
        //Get the up direction from parent's rotation
        //getParent().getWorldRotation().getRotationColumn(2, up);
        up.set(0,0,1);
        
        //get a left vector perpendicular to look and up
        look.cross(up, left);

        //Make up vector perpendicular to look and left
        left.cross(look, up);
        
        // coopt loc for our left direction:
        orient.fromAxes(left, up, look);
        worldRotation.fromRotationMatrix(orient);
        */
        /*
        // coopt diff for our in direction:
        look.set(camera.getDirection()).negateLocal();
        // coopt loc for our left direction:
        left.set(camera.getLeft()).negateLocal();
        orient.fromAxes(camera.getUp(), left.negateLocal(), look);
        worldRotation.fromRotationMatrix(orient);
    	rotateUpTo()
    	*/

    	localRotation.set(0,0,0,1);
    	
        look.set(camera.getDirection()).negateLocal();

        //First figure out the current up vector.
        Vector3f upY = up.set(Vector3f.UNIT_Z);
        localRotation.multLocal(upY);

        // get angle between vectors
        float angle = upY.angleBetween(look);

        //figure out rotation axis by taking cross product
        Vector3f rotAxis = upY.crossLocal(look);

        // Build a rotation quat and apply current local rotation.
        Quaternion q = tempQ;
        q.fromAngleAxis(angle, rotAxis);
        q.mult(localRotation, localRotation);
    	
    	/*
        // coopt diff for our in direction:
        look.set(camera.getDirection()).negateLocal();
        left.set(camera.getLeft()).negateLocal();
        //left.set(1f,1f,0);
        getParent().getWorldRotation().getRotationColumn(2, left);
    	left.negateLocal();
    	
        look.cross(left, up);
        up.cross(look, left);
        //orient.fromAxes(left, up, look);
        //worldRotation.fromRotationMatrix(orient);
        worldRotation.fromAxes(left, up, look);
*/    	
    }

}