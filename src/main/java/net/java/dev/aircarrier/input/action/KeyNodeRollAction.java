/*
 *  $Id: KeyNodeRollAction.java,v 1.2 2006/07/21 23:59:04 shingoki Exp $
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

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

/**
 * <code>KeyNodeRollLeftAction</code> rolls a node to the left. The axis
 * of rotation is dependant on the setting of the lock axis. If no lock axis is
 * set, the node rotates about it's z-axis. This will allow the node to roll.
 * However, to prevent rolling, setting the lock axis to the world's y-axis (or
 * any desired axis for that matter), will cause the node to rotate about the
 * world. The locking of the axis is particularly useful for control schemes
 * similar to first person shooters.
 * 
 * @author shingoki
 * @version $Id: KeyNodeRotateLeftAction.java,v 1.12 2004/08/22 02:00:34 cep21
 *          Exp $
 */
public class KeyNodeRollAction extends KeyInputAction {

	//the node to manipulate
    private Spatial node;
    //an optional axis to lock, preventing rolling on this axis.
    private Vector3f lockAxis;

    float factor = 1;
    
    /**
     * Constructor instantiates a new <code>KeyNodeRotateLeftAction</code>
     * object using the node and speed parameters for it's attributes.
     * 
     * @param node
     *            the node that will be affected by this action.
     * @param speed
     *            the speed at which the node can move.
     */
    public KeyNodeRollAction(Spatial node, float speed, boolean rotateLeft) {
        this.node = node;
        this.speed = speed;
        factor = rotateLeft ? 1 : -1;
    }

    /**
     * 
     * <code>setLockAxis</code> allows a certain axis to be locked, meaning
     * the camera will always be within the plane of the locked axis. For
     * example, if the node is a first person camera, the user might lock the
     * node's up vector. This will keep the node vertical with the ground.
     * 
     * @param lockAxis
     *            the axis to lock.
     */
    public void setLockAxis(Vector3f lockAxis) {
        this.lockAxis = lockAxis;
    }

    /**
     * <code>performAction</code> rolls the camera about it's forward vector or
     * lock axis at a speed of movement speed * time. Where time is the time
     * between frames and 1 corresponds to 1 second.
     * 
     * @see com.jme.input.action.KeyInputAction#performAction(InputActionEvent)
     */
    public void performAction(InputActionEvent evt) {
    	float angle = factor * speed * evt.getTime();
        if (lockAxis == null) {
        	NodeRotator.rotate(node, 2, angle);
        } else {
        	NodeRotator.rotate(node, lockAxis, angle);
        }
    }
}