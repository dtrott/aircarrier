/*
 *  $Id: SingleDial.java,v 1.1 2007/03/10 19:03:30 shingoki Exp $
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

package net.java.dev.aircarrier.hud;

import java.io.IOException;

import net.java.dev.aircarrier.scene.CompassDial;

import com.jme.math.FastMath;
import com.jme.scene.Node;

/**
 * A Node carrying a dial that rotates to show a single value
 * @author shingoki
 */
public class SingleDial extends Node implements Gauge {
	private static final long serialVersionUID = 3583905755438577509L;

	Node dial;
	
	float value;
	
	/**
	 * Create a gauge.
	 * @param name
	 * 		Name for the node
	 * @param value
	 * 		The initial value
	 * @throws IOException 
	 * 		If compass dial model cannot be loaded
	 */
	public SingleDial(
		String name,
		float value) throws IOException {		
		super(name);

		dial = CompassDial.createCompassDial();
		attachChild(dial);
		
		setValue(value);
	}
	
	public float getValue() {
		return value;
	}

	/**
	 * Display given value, by rotating dial, with rotation equal to
	 * (value % 1) * 2 * PI
	 * 
	 * That is the dial makes one full revolution per unit displayed, wrapping
	 * around after that
	 * 
	 * @param value
	 * 		The value to set. 
	 */
	public void setValue(float value) {

		dial.getLocalRotation().fromAngles(0, (value % 1) * FastMath.TWO_PI, 0);
		
		//Remember new value
		this.value = value;
	}
	
}
