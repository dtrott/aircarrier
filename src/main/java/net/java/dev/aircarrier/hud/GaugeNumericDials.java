/*
 *  $Id: GaugeNumericDials.java,v 1.4 2007/06/13 23:16:56 shingoki Exp $
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
import java.util.ArrayList;
import java.util.List;

import net.java.dev.aircarrier.scene.NumberDial;

import com.jme.math.FastMath;
import com.jme.scene.Node;

/**
 * A Node carrying dials that rotate to show a digit per dial
 * @author shingoki
 */
public class GaugeNumericDials extends Node implements Gauge {
	private static final long serialVersionUID = 3583905755438577509L;

	private static float MAGIC_ANGLE = FastMath.TWO_PI/20f;
	
	int digitCount;
	
	float maxValue;

	List<Node> dials;
	
	float value;
	
	/**
	 * Create a gauge.
	 * @param name
	 * 		Name for the node
	 * @param digitCount
	 * 		Number of digits to display
	 * @throws IOException 
	 * 		If dial models cannot be loaded
	 */
	public GaugeNumericDials(
		String name,
		int digitCount,
		float value) throws IOException {		
		super(name);
		this.digitCount = digitCount;
		dials = new ArrayList<Node>();
		
		for (int i = 0; i < digitCount; i++) {
			Node dial = NumberDial.createNumberDial();
			attachChild(dial);
			dial.getLocalTranslation().set(-i, 0, 0);
			dials.add(dial);
		}
		
		//calc max displayable value
		maxValue = (float)Math.pow(10, digitCount) - 1f;
		
		setValue(value);
	}

	/**
	 * Rotate a single dial
	 * @param index
	 * 		The dial index, 0 is for units, 1 for 10s, etc. (i.e. index counts from
	 * 		the right to the left)
	 * @param value
	 * 		The value of rotation, integers 0 to 9 display the 
	 * 		corresponding digit centered on the dial, values in between
	 * 		will have the dial in between the values. This wraps modulo 10,
	 * 		e.g. 9.5 will be halfway between 9 and 0 (wrapping around the top). 
	 */
	private void rotateDial(int index, float value) {
		Node dial = dials.get(index);
		dial.getLocalRotation().fromAngles((-value * 2 - 1) * MAGIC_ANGLE, 0, 0);
	}
	
	/**
	 * @return
	 * 		The maximum value the dials can display:
	 * 			10^digitCount - 1
	 * 		Values provided to {@link #setValue(float)} are capped to this
	 */
	public float getMaxValue() {
		return maxValue;
	}

	public float getValue() {
		return value;
	}

	/**
	 * Display given value, down to unit accuracy (actually more - since the
	 * units dial rotates continuously, but the dial is only really intended
	 * to be readable down to units.)
	 * 
	 * However, since the dials are nicely "animated" for continuously increasing
	 * values, users may want to supply a continuously varying value. For example,
	 * if you wish to display an amount of ammunition, you might want to use a 
	 * FloatSpring to make the value displayed "spring" around after the integer
	 * value, so that the dial appears to have a small mechanical update time, 
	 * and the user will see it move rather than "flicking" to a new value.
	 * SpringyGaugeController does this.
	 * 
	 * @param value
	 * 		The value to set. Note this is capped to maxValue {@link #getMaxValue()}
	 */
	public void setValue(float value) {
		//Work out rotations
		
		//Restrict value to range displayable on dials
		if (value > maxValue) value = maxValue;
		if (value < 0) value = 0;
		
		//For each digit, work out rotation
		
		//For units, rotation is just modulo 10
		float unitValue = value % 10;
		rotateDial(0, unitValue);

		//If the units are wrapping, then start off with previous digit
		//wrapping. wrappingValue is how much of the wrap is complete (0 to 1)
		boolean previousDigitWrapping = (unitValue > 9.0f);
		float wrappingValue = unitValue - 9.0f;
		
		//Work out the non-unit digits
		int higherDigitSum = (int)value;
		higherDigitSum/=10;
		for (int i = 1; i < digitCount; i++) {
			//base value for digit is just the "correct" digit to display,
			//the base ten digit
			int intDigitValue = higherDigitSum % 10; 
			float digitValue = (float)intDigitValue; 
			
			//Now we just want to rotate smoothly to the next digit as
			//the dial below wraps, IF it is wrapping
			if (previousDigitWrapping) {
				digitValue += wrappingValue;
				
				//If we are moving up to 0 (currently on 9)
				//then we want to leave this true for the next iteration,
				//otherwise the wrapping ends at this digit
				if (intDigitValue != 9) {
					previousDigitWrapping = false;
				}
			}
			
			rotateDial(i, digitValue);
			higherDigitSum /= 10;
		}			
		
		//Remember new value
		this.value = value;
	}
	
}
