/*
 *  $Id: MouseJoystickInputHandler.java,v 1.3 2006/07/21 23:59:00 shingoki Exp $
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

package net.java.dev.aircarrier.planes.input;

import net.java.dev.aircarrier.input.VirtualJoystick;

import com.jme.input.InputHandler;
import com.jme.input.RelativeMouse;

/**
 * <code>MouseJoystickInputHandler</code> monitors a mouse 
 * and passes the input to a VirtualJoystick 
 *  
 * @author shingoki
 */
public class MouseJoystickInputHandler extends InputHandler {

	MouseVirtualJoystickInputAction mouseLook;
	
    /**
     * Create an input
     * 
     * @param joystick
     *            the joystick to move
     * @param mouseSpeed
     * 		The speed at which to move the joystick, relative to mouse
     */
    public MouseJoystickInputHandler(VirtualJoystick joystick, float mouseSpeed) {
        setUpMouse(joystick, mouseSpeed );
    }

    private void setUpMouse(VirtualJoystick joystick, float mouseSpeed ) {
    	
        RelativeMouse mouse = new RelativeMouse("Mouse Input");
        mouse.registerWithInputHandler( this );

        mouseLook = 
        	new MouseVirtualJoystickInputAction(joystick, mouse, mouseSpeed);
        mouseLook.setSpeed( mouseSpeed );
        addAction(mouseLook);
        
    }

	public MouseVirtualJoystickInputAction getMouseLook() {
		return mouseLook;
	}

}