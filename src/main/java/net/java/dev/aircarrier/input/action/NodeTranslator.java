/*
 *  $Id: NodeTranslator.java,v 1.6 2007/06/03 10:46:04 shingoki Exp $
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

import net.java.dev.aircarrier.acobject.Acobject;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

/**
 * Will rotate a node around an axis. Has its own temporary
 * variables for this.
 * 
 * @author shingoki
 */
public class NodeTranslator {
	
    private static final Vector3f tempVa = new Vector3f();

    /**
     * Translate a node around along an axis, THIS ASSUMES IT
     * IS A CHILD OF THE ROOT NODE.
     * @param node
     * 		Node to move
     * @param axis
     * 		Axis to move along
     * @param distance
     * 		Distance to move
     */
    public static void translate(Spatial node, int axis, float distance) {
		Vector3f loc = node.getLocalTranslation();
        loc.addLocal(node.getLocalRotation().getRotationColumn(axis, tempVa)
                .multLocal(distance));
        node.setLocalTranslation(loc);
    }
    
    /**
     * Make a translation vector, along an axis of the specified spatial, by a specified distance
     * This does not assume the node is a child of the root node - world rotation is used.
     * @param node
     * 		The node to get axes from
     * @param axis
     * 		The axis index, 0,1,2 for x,y,z
     * @param distance
     * 		The distance along the axis
     * @param vector
     * 		The vector in which to store the result, if null a new vector will be made
     * @return
     * 		The translation vector - either that specified by vector parameter, or a new vector
     * 		if this is null
     */
    public static Vector3f makeTranslationVector(Spatial node, int axis, float distance, Vector3f vector) {
    	if (vector == null) {
    		vector = new Vector3f();
    	}
    	node.getWorldRotation().getRotationColumn(axis, vector);
    	vector.multLocal(distance);
    	return vector;
    }

    /**
     * Make a translation vector, along an axis of the specified rotation, by a specified distance
     * @param object
     * 		The object from which to get rotation
     * @param axis
     * 		The axis index, 0,1,2 for x,y,z
     * @param distance
     * 		The distance along the axis
     * @param vector
     * 		The vector in which to store the result, if null a new vector will be made
     * @return
     * 		The translation vector - either that specified by vector parameter, or a new vector
     * 		if this is null
     */
    public static Vector3f makeTranslationVector(Acobject object, int axis, float distance, Vector3f vector) {
    	return makeTranslationVector(object.getRotation(), axis, distance, vector);
    }
    
    /**
     * Make a translation vector, along an axis of the specified rotation, by a specified distance
     * @param rotation
     * 		The rotation to get axes from
     * @param axis
     * 		The axis index, 0,1,2 for x,y,z
     * @param distance
     * 		The distance along the axis
     * @param vector
     * 		The vector in which to store the result, if null a new vector will be made
     * @return
     * 		The translation vector - either that specified by vector parameter, or a new vector
     * 		if this is null
     */
    public static Vector3f makeTranslationVector(Quaternion rotation, int axis, float distance, Vector3f vector) {
    	if (vector == null) {
    		vector = new Vector3f();
    	}
    	rotation.getRotationColumn(axis, vector);
    	vector.multLocal(distance);
    	return vector;
    }
}