/*
 *  $Id: NodeJoystickLook.java,v 1.2 2006/07/21 23:59:05 shingoki Exp $
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
import com.jme.input.action.KeyNodeLookDownAction;
import com.jme.input.action.KeyNodeLookUpAction;
import com.jme.input.action.KeyNodeRotateLeftAction;
import com.jme.input.action.KeyNodeRotateRightAction;
import com.jme.input.joystick.Joystick;
import com.jme.input.joystick.JoystickInput;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

/**
 * <code>NodeMouseLook</code> defines a mouse action that detects mouse
 * movement and converts it into node rotations and node tilts.
 * 
 * @author Mark Powell
 * @version $Id: NodeJoystickLook.java,v 1.2 2006/07/21 23:59:05 shingoki Exp $
 */
public class NodeJoystickLook {

    //the actions that handle looking up, down, left and right.
    private KeyNodeLookDownAction lookDown;

    private KeyNodeLookUpAction lookUp;

    private KeyNodeRotateLeftAction rotateLeft;

    private KeyNodeRotateRightAction rotateRight;

    KeyNodeRollAction rollLeft;
    KeyNodeRollAction rollRight;
    
    //the axis to lock
    private Vector3f lockAxis;

    //the node to control
    private Spatial node;

    //the event to distribute to the look actions.
    private static InputActionEvent event;
    
    JoystickInput input;
    Joystick joystick;
    
    float rollSpeed;

    /**
     * Constructor creates a new <code>NodeMouseLook</code> object. It takes
     * the mouse, node and speed of the looking.
     * 
     * @param mouse
     *            the mouse to calculate view changes.
     * @param node
     *            the node to move.
     * @param speed
     *            the speed at which to alter the camera.
     */
    public NodeJoystickLook(    JoystickInput input, Joystick joystick, Spatial node, float speed, float rollSpeed) {
        this.input = input;
        this.joystick = joystick;
        this.node = node;
        this.rollSpeed = rollSpeed;

        lookDown = new KeyNodeLookDownAction(this.node, speed);
        lookUp = new KeyNodeLookUpAction(this.node, speed);
        rotateLeft = new KeyNodeRotateLeftAction(this.node, speed);
        rotateRight = new KeyNodeRotateRightAction(this.node, speed);
        rollLeft = new KeyNodeRollAction( node, rollSpeed, true );
        rollRight = new KeyNodeRollAction( node, rollSpeed, false );
        
        event = new InputActionEvent();
    }

    /**
     * 
     * <code>setLockAxis</code> sets the axis that should be locked down. This
     * prevents "rolling" about a particular axis. Typically, this is set to the
     * mouse's up vector.
     * 
     * @param lockAxis
     *            the axis that should be locked down to prevent rolling.
     */
    public void setLockAxis(Vector3f lockAxis) {
        this.lockAxis = lockAxis;
        rotateLeft.setLockAxis(lockAxis);
        rotateRight.setLockAxis(lockAxis);
    }

    /**
     * Returns the axis that is currently locked.
     * 
     * @return The currently locked axis
     * @see #setLockAxis(com.jme.math.Vector3f)
     */
    public Vector3f getLockAxis() {
        return lockAxis;
    }

    /**
     * <code>performAction</code> checks for any movement of the mouse, and
     * calls the appropriate method to alter the node's orientation when
     * applicable.
     * 
     * @see com.jme.input.action.MouseInputAction#performAction(InputActionEvent) 
     */
    public void update(float time) {
        
    	float x = joystick.getAxisValue(0);
    	float y = joystick.getAxisValue(1);
    	float r = joystick.getAxisValue(3);

        if (x > 0) {
            event.setTime(time * x);
            rotateRight.performAction(event);
        } else if (x < 0) {
            event.setTime(time * x * -1);
            rotateLeft.performAction(event);
        }
        if (y > 0) {
            event.setTime(time * y);
            lookUp.performAction(event);
        } else if (y < 0) {
            event.setTime(time * y * -1);
            lookDown.performAction(event);
        }
        if (r > 0) {
            event.setTime(time * r);
            rollLeft.performAction(event);
        } else if (r < 0) {
            event.setTime(time * r * -1);
            rollRight.performAction(event);
        }
        
    }


}