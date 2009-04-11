/*
 *  $Id: VirtualJoystickAction.java,v 1.2 2006/07/21 23:58:56 shingoki Exp $
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

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;

/**
 * <code>PlaneControlAction</code> sets a control axis of a plane
 * to a specified value at a specified rate, when performed
 * 
 * @author shingoki
 */
public class VirtualJoystickAction extends KeyInputAction {

	//the joystick to use
	VirtualJoystick joystick;

    int axis;

    /**
     * Create an action
     * @param joystick
     * 		The joystick to be adjusted
     * @param axis
     * 		The axis of control to adjust
     * @param speed
     * 		The rate the action will alter the value
     */
    public VirtualJoystickAction(VirtualJoystick joystick, int axis, float speed) {
		super();
		this.joystick = joystick;
		this.axis = axis;
		setSpeed(speed);
	}

    /**
     * Changes the controlled axis of the joystick at rate given by
     * speed.
     */
    public void performAction(InputActionEvent evt) {
    	float change = speed * evt.getTime();
    	joystick.changeAxis(axis, change);
    }
}