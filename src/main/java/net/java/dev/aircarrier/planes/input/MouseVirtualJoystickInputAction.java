/*
 *  $Id: MouseVirtualJoystickInputAction.java,v 1.5 2006/07/21 23:58:57 shingoki Exp $
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

import com.jme.input.Mouse;
import com.jme.input.MouseInput;
import com.jme.input.RelativeMouse;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.MouseInputAction;

/**
 * <code>PlaneControllerMouseLook</code> defines a mouse action that detects mouse
 * movement and converts it into control axis settings on a PlaneController
 * 
 * @author shingoki
 * @version $Id: MouseVirtualJoystickInputAction.java,v 1.5 2006/07/21 23:58:57 shingoki Exp $
 */
public class MouseVirtualJoystickInputAction extends MouseInputAction {

	VirtualJoystick joystick;
	
	boolean thirdButtonBoth = false;
	
    public MouseVirtualJoystickInputAction(
    		VirtualJoystick joystick, 
    		Mouse mouse, 
    		float speed) {
        this.mouse = (RelativeMouse) mouse;
        this.speed = speed;
        this.joystick = joystick;
    }
    
    public void performAction(InputActionEvent evt) {	
    	if (mouse.getLocalTranslation().x != 0) {
    		joystick.changeAxis(0, mouse.getLocalTranslation().x * speed );
    	}
    	if (mouse.getLocalTranslation().y != 0) {
    		joystick.changeAxis(1, mouse.getLocalTranslation().y * speed );
    	}
    	
    	for (int i = 0; i < 3; i++) {
    		joystick.setButtonPressed(i, MouseInput.get().isButtonDown(i));
    	}
    	
    	if (isThirdButtonBoth() && MouseInput.get().isButtonDown(2)) {
    		joystick.setButtonPressed(0, true);
    		joystick.setButtonPressed(1, true);
    	}
    }

	public boolean isThirdButtonBoth() {
		return thirdButtonBoth;
	}

	public void setThirdButtonBoth(boolean thirdButtonBoth) {
		this.thirdButtonBoth = thirdButtonBoth;
	}

}