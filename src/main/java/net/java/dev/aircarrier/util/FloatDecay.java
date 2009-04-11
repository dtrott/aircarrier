/*
 *  $Id: FloatDecay.java,v 1.1 2007/06/25 22:58:54 shingoki Exp $
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

package net.java.dev.aircarrier.util;

/**
 * A simple decay in one dimension. This is similar to a spring,
 * but it has no velocity. This means that the movement is less complex,
 * and has no initial acceleration, but is also more stable than a spring,
 * and can be used anywhere between very slow changes of value and instantaneous
 * tracking, whereas a spring will never achieve stable instantaneous movement.
 * 
 * Useful for getting a property (for example a position) to
 * move from one value to another, in a smooth looking way.
 * To use, create a decay with appropriate time constant,
 * this is the time for the decay to get half way to its target,
 * from some initial position with a constant target
 * (e.g. new FloatDecay(0.3) is a reasonable value, this will get
 * half way to target in 0.3 seconds, and about 90% of the way there
 * in 1 second)
 * Then set decay position to the initial value, and update
 * each frame with target parameter as your desired value.
 * The position parameter will "snap to" the desired value.
 * @author goki
 */
public class FloatDecay {

	float position;
	
	float halfLife;
	
	ExponentialDecay decay;
	
	/**
	 * Make a spring with given spring constant and damping constant
	 * @param halfLife
	 * 		The half life of the decay, time until the position moves half
	 * 		way to a fixed target, from an initial position at time 0
	 */
	public FloatDecay(float halfLife) {
		super();
		this.position = 0;
		this.halfLife = halfLife;
		decay = new ExponentialDecay(halfLife);
	}
	
	/**
	 * Update the position of the decay. This updates the "position" so that
	 * its offset from the target decays over time, and it approaches the target.
	 * It will actually never reach the target in theory, in practice it will get
	 * near enough to make no difference.
	 * @param target
	 * 		The target towards which the position is decaying
	 * @param time
	 * 		The elapsed time in seconds
	 */
	public void update(float target, float time) {
		
		//Set v to target - position, this is the required movement
		float v = position - target;
		
		//Decay the offset
		v *= decay.evaluate(time);
		
		//Update the position to the new offset
		position = target + v;
		
	}

	/**
	 * @return
	 * 		The half life of the decay, time until the position moves half
	 * 		way to a fixed target, from an initial position at time 0
	 */
	public float getHalfLife() {
		return halfLife;
	}

	/**
	 * @param halfLife
	 * 		The half life of the decay, time until the position moves half
	 * 		way to a fixed target, from an initial position at time 0
	 */
	public void setHalfLife(float halfLife) {
		this.halfLife = halfLife;
		decay.setHalfLife(halfLife);
	}

	/**
	 * @return
	 * 		The current position of the simulated decaying position,
	 * 		changes as simulation is updated
	 */
	public float getPosition() {
		return position;
	}

	/**
	 * @param position
	 * 		A new position for simulated decaying position
	 */
	public void setPosition(float position) {
		this.position = position;
	}

}
