/*
 *  $Id: NodeRotator.java,v 1.4 2006/12/09 20:46:15 shingoki Exp $
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

package net.java.dev.aircarrier.input.action;

import com.jme.math.Matrix3f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

/**
 * Will rotate a node around an axis. Has its own temporary
 * variables for this.
 * 
 * @author shingoki
 */
public class NodeRotator {
	
    //temporary variables to handle rotation
    private static final Matrix3f incr = new Matrix3f();

    private static final Matrix3f tempMa = new Matrix3f();

    private static final Matrix3f tempMb = new Matrix3f();

    private static final Vector3f tempVa = new Vector3f();

    /**
     * Rotate a node around a lock axies
     * @param node
     * @param lockAxis
     */
    public static void rotate(Spatial node, Vector3f lockAxis, float angle) {
    	incr.fromAngleAxis(angle, lockAxis);
    	rotateByIncr(node);
    }
    
    /**
     * Rotate a node around one of its axes
     * @param node
     * 		The node to rotate
     * @param localAxisIndex
     * 		The index of the local axis around which to rotate
     */
    public static void rotate(Spatial node, int localAxisIndex, float angle) {
        incr.fromAngleNormalAxis(
        		angle, 
        		node.getLocalRotation().getRotationColumn(localAxisIndex,tempVa).normalizeLocal());
    	rotateByIncr(node);
    }
    
    /**
     * Rotate a node by the current setting of incr matrix
     * @param node
     * 		The node to rotate
     */
    private static void rotateByIncr(Spatial node) {
        node.getLocalRotation().fromRotationMatrix(
                incr.mult(node.getLocalRotation().toRotationMatrix(tempMa),
                        tempMb));
        node.getLocalRotation().normalize();
    }
}