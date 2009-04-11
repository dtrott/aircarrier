/*
 *  $Id: JoystickPlaneInput.java,v 1.5 2006/12/23 22:29:23 shingoki Exp $
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

import net.java.dev.aircarrier.controls.PlaneControls;

import com.jme.input.action.InputActionEvent;
import com.jme.input.joystick.Joystick;

/**
 * <code>JoystickPlaneInput</code> polls a joystick and passes the
 * input to a PlaneController 
 *  
 * @author shingoki
 */
public class JoystickPlaneInput {

    //the controller to use
    private PlaneControls controls;

    Joystick joystick;
    
    int yawAxis = 0;
    int pitchAxis = 1;
    int rollAxis = 3;
    int throttleAxis = 4;
    int primaryFireButton = 7;
    int secondaryFireButton = 6;

    /**
     * Constructor creates a new <code>NodeMouseLook</code> object. It takes
     * the mouse, node and speed of the looking.
     * 
     * @param controller
     *            the controller to use
     * @param joystick
     *            The joystick to poll
     */
    public JoystickPlaneInput(PlaneControls controls, Joystick joystick) {
        this.joystick = joystick;
        this.controls = controls;
    }

    /**
     * <code>performAction</code> checks for any movement of the mouse, and
     * calls the appropriate method to alter the node's orientation when
     * applicable.
     * 
     * @see com.jme.input.action.MouseInputAction#performAction(InputActionEvent) 
     */
    public void update(float time) {
    	if (joystick != null) {
	    	float x = joystick.getAxisValue(yawAxis);
	    	float y = joystick.getAxisValue(pitchAxis);
	    	float r = joystick.getAxisValue(rollAxis);
	    	float t = joystick.getAxisValue(throttleAxis);
	
	    	controls.setAxis(PlaneControls.YAW, x);
	    	controls.setAxis(PlaneControls.PITCH, y);
	    	controls.setAxis(PlaneControls.ROLL, r);
	    	controls.setAxis(PlaneControls.THROTTLE, -t);
	    	
	    	if (joystick.isButtonPressed(primaryFireButton)) {
	    		controls.setFiring(0, true);
	    	}
	    	if (joystick.isButtonPressed(secondaryFireButton)) {
	    		controls.setFiring(1, true);
	    	}
    	}
    }

	public int getPitchAxis() {
		return pitchAxis;
	}

	public void setPitchAxis(int pitchAxis) {
		this.pitchAxis = pitchAxis;
	}

	public int getPrimaryFireButton() {
		return primaryFireButton;
	}

	public void setPrimaryFireButton(int primaryFireButton) {
		this.primaryFireButton = primaryFireButton;
	}

	public int getRollAxis() {
		return rollAxis;
	}

	public void setRollAxis(int rollAxis) {
		this.rollAxis = rollAxis;
	}

	public int getSecondaryFireButton() {
		return secondaryFireButton;
	}

	public void setSecondaryFireButton(int secondaryFireButton) {
		this.secondaryFireButton = secondaryFireButton;
	}

	public int getYawAxis() {
		return yawAxis;
	}

	public void setYawAxis(int yawAxis) {
		this.yawAxis = yawAxis;
	}

}