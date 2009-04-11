/*
 *  $Id: Gauge180.java,v 1.6 2007/03/07 19:53:47 shingoki Exp $
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

import java.nio.FloatBuffer;

import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.scene.TexCoords;
import com.jme.system.DisplaySystem;

/**
 * A Quad with a gauge that fills around an arc of a circle not
 * greater than 180 degrees
 * @author shingoki
 */
public class Gauge180 extends Quad implements Gauge {
	private static final long serialVersionUID = 3583905755438577509L;

	float maxAngle;
	float startAngle;
	float value;
	boolean clockwise;
	boolean spinTexture;
	float size;

	/**
	 * Create a gauge.
	 * The center of the gauge (the point it "rotates" about to fill up)
	 * is at the center of the Spatial
	 * @param value
	 * 		The initial gauge value
	 * @param startAngle
	 * 		The angle the gauge starts at (measured clockwise from straight down for clockwise
	 * 		gauges, anticlockwise from straight down for anticlockwise gauges)
	 * @param maxAngle
	 * 		The maximum arc angle size (corresponds to value of 1.0f)
	 * 		The absolute value will be used, and capped to PI
	 * 		This is clockwise or anticlockwise according to "clockwise" property
	 * @param clockwise
	 * 		True to have gauge fill up from bottom, clockwise. False to have
	 * 		it fill up from bottom, counterclockwise.
	 * @param spinTexture
	 * 		True to spin the texture to display (the texture moves as value changes, the top of the gauge
	 * 		is the "fixed point" for the texture)
	 * 		False to clip the texture to display (the texture stays in place, but is
	 * 		clipped to a varying angle, the bottom of the texture is the "fixed point")
	 * @param size
	 * 		The size of the gauge (height of quad, width will be half this)
	 * @param t
	 * 		The texture to use for the gauge
	 */
	public Gauge180(
			String name,
			float value,
			float startAngle,
			float maxAngle,
			boolean clockwise,
			boolean spinTexture,
			float size, Texture t) {
		super(name, size/2, size);
		this.maxAngle = maxAngle;
		this.startAngle = startAngle;
		this.clockwise = clockwise;
		this.spinTexture = spinTexture;
		this.size = size;
		TextureState textureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		textureState.setEnabled(true);
		textureState.setTexture(t);
		setRenderState(textureState);
		maxAngle = FastMath.abs(maxAngle);
		if (maxAngle > FastMath.PI) maxAngle = FastMath.PI;
		if (clockwise) {
			rotateQuad(startAngle, size);
		} else {
			rotateQuad(FastMath.PI - startAngle, size);
		}
		setValue(value);
	}

	private void rotateTexture(float angle) {
        float xm = FastMath.sin(angle) * 0.5f;
        float ym = FastMath.cos(angle) * 0.5f;

        final TexCoords texCoords = getTextureCoords(0);
        final FloatBuffer buf = texCoords.coords;
        buf.clear();
        buf.put( 0.5f - xm - ym  ).put( 0.5f -ym + xm );	//0
		buf.put( 0.5f + xm - ym ).put( 0.5f + ym + xm );	//1
		buf.put( 0.5f + xm ).put( 0.5f + ym );			//2
		buf.put( 0.5f - xm ).put( 0.5f - ym );			//3
	}

	private void rotateQuad(float angle, float size) {
		getVertexBuffer().clear();
		float xm = FastMath.sin(angle) * 0.5f * size;
		float ym = FastMath.cos(angle) * 0.5f * size;
		getVertexBuffer().put( xm - ym  ).put( ym + xm ).put( 0 );	//0
		getVertexBuffer().put( -xm - ym ).put( -ym + xm ).put( 0 );	//1
		getVertexBuffer().put( -xm ).put( -ym ).put( 0 );				//2
		getVertexBuffer().put( xm ).put(  ym ).put( 0 );				//3
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		//Work out rotation
		if (value < 0) value = 0;
		if (value > 1) value = 1;
		if (spinTexture) {
			if (!clockwise) value = -value;
			rotateTexture(FastMath.PI + value * maxAngle );
		} else {
			if (clockwise) {
				rotateQuad(FastMath.PI + value * maxAngle + startAngle, size);
				rotateTexture(FastMath.PI - value * maxAngle);
			} else {
				rotateQuad( - value * maxAngle - startAngle, size);
				rotateTexture(FastMath.PI  + value * maxAngle);
			}
		}

		//Remember new value
		this.value = value;
	}

}
