/*
 *  $Id: DialBody.java,v 1.2 2007/03/10 19:03:30 shingoki Exp $
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

import net.java.dev.aircarrier.util.TextureLoader;

import com.jme.image.Texture;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

/**
 * A Quad with dial body texture
 * @author shingoki
 */
public class DialBody extends Quad {
	private static final long serialVersionUID = 3583905755438577509L;

	public DialBody() {		
		this("DialBody", 128, 128);
	}

	public DialBody(String name, float width, float height) {
		super(name, width, height);
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		Texture t = TextureLoader.loadUncompressedTexture("resources/dialBody.png");
		ts.setEnabled(true);
		ts.setTexture(t);
		setRenderState(ts);
	}

}
