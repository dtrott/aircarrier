/*
 *  $Id: PlaneNodeHandler.java,v 1.2 2006/07/21 23:59:14 shingoki Exp $
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

package net.java.dev.aircarrier.input;

import net.java.dev.aircarrier.input.action.KeyNodeRollAction;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.RelativeMouse;
import com.jme.input.action.KeyNodeBackwardAction;
import com.jme.input.action.KeyNodeForwardAction;
import com.jme.input.action.KeyNodeLookDownAction;
import com.jme.input.action.KeyNodeLookUpAction;
import com.jme.input.action.KeyNodeRotateLeftAction;
import com.jme.input.action.KeyNodeRotateRightAction;
import com.jme.input.action.KeyNodeStrafeLeftAction;
import com.jme.input.action.KeyNodeStrafeRightAction;
import com.jme.input.action.NodeMouseLook;
import com.jme.scene.Spatial;

/**
 * <code>NodeHandler</code> defines an InputHandler that sets
 * a node that can be controlled via keyboard and mouse inputs. By default the
 * commands are, WSAD moves the node forward, backward and strafes. The
 * arrow keys rotate and tilt the node and the mouse also rotates and tilts
 * the node.
 * @author Mark Powell
 * @version $Id: PlaneNodeHandler.java,v 1.2 2006/07/21 23:59:14 shingoki Exp $
 */
public class PlaneNodeHandler extends InputHandler {

    /**
     * Constructor instantiates a new <code>NodeHandler</code> object. The
     * application is set for the use of the exit action. The node is set to
     * control, while the api defines which input api is to be used.
     * @param node the node to control.
     * @param api not used
     */
    public PlaneNodeHandler(Spatial node, String api) { //todo: remove parameter api

        setKeyBindings();
        setUpMouse(node, 1 );
        setActions(node, 0.5f, 0.01f, 0.01f );
    }

    /**
     * Constructor instantiates a new <code>NodeHandler</code> object. The
     * application is set for the use of the exit action. The node is set to
     * control, while the api defines which input api is to be used.
     * @param node 
     * 		the node to control.
     * @param moveSpeed 
     * 		action speed for key actions (move)
     * @param turnSpeed 
     * 		action speed for key actions (turn)
     * @param rollSpeed 
     * 		action speed for key actions (roll)
     * @param mouseSpeed 
     * 		action speed for mouse actions (rotate)
     */
    public PlaneNodeHandler(Spatial node, float moveSpeed, float turnSpeed, float rollSpeed, float mouseSpeed) {

        setKeyBindings();
        setUpMouse(node, mouseSpeed );
        setActions(node, moveSpeed, turnSpeed, rollSpeed );
    }

    /**
     *
     * <code>setKeyBindings</code> binds the keys to use for the actions.
     */
    private void setKeyBindings() {
        KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();

        keyboard.set("forward", KeyInput.KEY_W);
        keyboard.set("backward", KeyInput.KEY_S);
        keyboard.set("strafeLeft", KeyInput.KEY_A);
        keyboard.set("strafeRight", KeyInput.KEY_D);
        keyboard.set("lookUp", KeyInput.KEY_UP);
        keyboard.set("lookDown", KeyInput.KEY_DOWN);
        keyboard.set("turnRight", KeyInput.KEY_RIGHT);
        keyboard.set("turnLeft", KeyInput.KEY_LEFT);
        keyboard.set("rollRight", KeyInput.KEY_Q);
        keyboard.set("rollLeft", KeyInput.KEY_E);
    }

    /**
     *
     * <code>setUpMouse</code> sets the mouse look object.
     * @param node the node to use for rotations.
     * @param mouseSpeed
     */
    private void setUpMouse( Spatial node, float mouseSpeed ) {
        RelativeMouse mouse = new RelativeMouse("Mouse Input");
        mouse.registerWithInputHandler( this );

        NodeMouseLook mouseLook = new NodeMouseLook(mouse, node, 0.1f);
        mouseLook.setSpeed( mouseSpeed );
        addAction(mouseLook);
        
    }

    /**
     *
     * <code>setActions</code> sets the keyboard actions with the corresponding
     * key command.
     * @param node 
     * 		the node to control.
     * @param keySpeed 
     * 		action speed for key actions (move)
     * @param mouseSpeed 
     * 		action speed for mouse actions (rotate)
     * @param rollSpeed 
     * 		action speed for key actions (roll)
     */
    private void setActions( Spatial node, float moveSpeed, float turnSpeed, float rollSpeed ) {
        addAction( new KeyNodeForwardAction( node, moveSpeed ), "forward", true );
        addAction( new KeyNodeBackwardAction( node, moveSpeed ), "backward", true );
        addAction( new KeyNodeStrafeLeftAction( node, moveSpeed ), "strafeLeft", true );
        addAction( new KeyNodeStrafeRightAction( node, moveSpeed ), "strafeRight", true );
        addAction( new KeyNodeLookUpAction( node, turnSpeed ), "lookUp", true );
        addAction( new KeyNodeLookDownAction( node, turnSpeed ), "lookDown", true );
        KeyNodeRotateRightAction rotateRight = new KeyNodeRotateRightAction( node, turnSpeed );
        addAction( rotateRight, "turnRight", true );
        KeyNodeRotateLeftAction rotateLeft = new KeyNodeRotateLeftAction( node, turnSpeed );
        addAction( rotateLeft, "turnLeft", true );

        KeyNodeRollAction rollLeft = new KeyNodeRollAction( node, rollSpeed, true );
        addAction( rollLeft, "rollLeft", true );

        KeyNodeRollAction rollRight = new KeyNodeRollAction( node, -rollSpeed, false );
        addAction( rollRight, "rollRight", true );

    }
}
