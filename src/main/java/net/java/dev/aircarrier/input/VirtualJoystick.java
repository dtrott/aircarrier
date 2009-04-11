/*
 *  $Id: VirtualJoystick.java,v 1.4 2006/11/05 12:42:17 shingoki Exp $
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

import com.jme.input.joystick.Joystick;

public class VirtualJoystick implements Joystick {

	float[] axes;

	boolean[] axisChanged;
	
	boolean[] buttons;
	
	String[] axisNames; 
	
	float decayRate = 2f;
	
	boolean alwaysRecentered = false;
	
	private final static String[] DEFAULT_NAMES = 
		new String[]{"X", "Y", "Throttle", "Rudder"};
	
	/**
	 * Create a virtual joystick with 4 axes,
	 * normally used as X, Y, Throttle and Rudder in that order,
	 * and 8 buttons
	 */
	public VirtualJoystick() {
		this(4, 8);
	}
	
	/**
	 * Create a virtual joystick
	 * @param axisCount
	 * 		Number of axes
	 */
	public VirtualJoystick(int axisCount, int buttonCount) {
		super();
		axes = new float[axisCount];
		axisChanged = new boolean[axisCount];
		buttons = new boolean[buttonCount];
		axisNames = new String[axisCount];
		for (int i = 0; i < axisNames.length; i++) {
			if (i < DEFAULT_NAMES.length) {
				axisNames[i] = DEFAULT_NAMES[i];
			} else {
				axisNames[i] = "Axis " + i;
			}
		}
	}

    /**
     * Look through the axis for a given name.
     * @param name the name of the axis we are looking for
     * @return the index of the matching axis or -1 if none.
     */
    public int findAxis(String name) {
    	int i = 0;
    	for (String checkName : axisNames) {
    		if (checkName.equals(name)) {
    			return i;
    		}
    		i++;
    	}
    	return -1;
    }
	
	
	public float getDecayRate() {
		return decayRate;
	}

	public void setDecayRate(float decayRate) {
		this.decayRate = decayRate;
	}

	public boolean isAlwaysRecentered() {
		return alwaysRecentered;
	}

	public void setAlwaysRecentered(boolean alwaysRecentered) {
		this.alwaysRecentered = alwaysRecentered;
	}

	/**
	 * Set an axis' position
	 * @param axis
	 * 		The axis to set
	 * @param value
	 * 		The position of the axis - will be clipped to (-1 to 1)
	 */
	public void setAxis(int axis, float value) {
		axes[axis] = value;
		axisChanged[axis] = true;
		clipRange(axis);
	}

	/**
	 * Clip axis value to (-1 to 1)
	 * @param axis
	 * 		The axis to clip
	 */
	void clipRange(int axis) {
		if (axes[axis] > 1) axes[axis] = 1;
		if (axes[axis] < -1) axes[axis] = -1;		
	}
	
	/**
	 * Move an axis by an increment
	 * @param axis
	 * 		The axis to change
	 * @param value
	 * 		The amount to add to the axis (can be negative)
	 * 		Axes go from -1 to 1
	 */
	public void changeAxis(int axis, float value) {
		axes[axis] += value;
		axisChanged[axis] = true;
		clipRange(axis);
	}
	
	/**
	 * Update the virtual joystic state
	 * @param time
	 * 		Elapsed time since last update
	 */
	public void update(float time) {
		for (int axis = 0; axis < axes.length; axis++) {
			
			//For each axis that has not been changed since last update,
			//decay the position towards zero
			if (!axisChanged[axis] || alwaysRecentered) {
				float p = axes[axis];
				if (p < 0) {
					p+= time * decayRate;
					if (p > 0) p = 0;
				}
				if (p > 0) {
					p-= time * decayRate;
					if (p < 0) p = 0;
				}
				axes[axis] = p;
			}
			
			//Reset change tracking
			axisChanged[axis] = false;
		}
	}

	public String[] getAxisNames() {
		return axisNames;
	}

	public int getAxisCount() {
		return axes.length;
	}

	public float getAxisValue(int axis) {
		return axes[axis];
	}

	public int getButtonCount() {
		return buttons.length;
	}

	public boolean isButtonPressed(int button) {
		return buttons[button];
	}

	public void setButtonPressed(int button, boolean pressed) {
		buttons[button] = pressed;
	}
	
	public String getName() {
		return "Virtual Joystick";
	}

	public void rumble(int axis, float intensity) {
	}
	
}
