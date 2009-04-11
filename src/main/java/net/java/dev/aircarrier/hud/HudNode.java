/*
 *  $Id: HudNode.java,v 1.4 2007/04/28 23:11:55 shingoki Exp $
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

import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;

/**
 * A Node having appropriate settings to have hud nodes
 * attached to it (alpha, lighting, render queue, etc.)
 * @author shingoki
 */
public class HudNode extends Node {
	private static final long serialVersionUID = -7178931841896666077L;

	/**
	 * Make a hud node.
	 * Make sure to attach this to the root node, then attach hud nodes
	 * to it, and call updateRenderState() on this node afterwards to
	 * make sure child nodes inherit suitable render settings
	 * @param display
	 * 		The display system in use
	 */
	public HudNode(DisplaySystem display) {
		super();
		setupStates(display);
	}

	/**
	 * Make a hud node.
	 * Make sure to attach this to the root node, then attach hud nodes
	 * to it, and call updateRenderState() on this node afterwards to
	 * make sure child nodes inherit suitable render settings
	 * @param display
	 * 		The display system in use
	 * @param name
	 * 		Name for the node
	 */
	public HudNode(DisplaySystem display, String name) {
		super(name);
		setupStates(display);
	}

	/**
	 * Make a hud node.
	 * Make sure to attach this to the root node, then attach hud nodes
	 * to it, and call updateRenderState() on this node afterwards to
	 * make sure child nodes inherit suitable render settings
	 * @param display
	 * 		The display system in use
	 * @param name
	 * 		Name for the node
	 * @param ortho
	 * 		If true, hudnode is set to ORTHO render mode
	 */
	public HudNode(DisplaySystem display, String name, boolean ortho) {
		this(display, name);

		if (ortho) setRenderQueueMode(Renderer.QUEUE_ORTHO);
	}

	/**
	 * Set render states
	 * @param display
	 * 		The display system in use
	 */
	private void setupStates(DisplaySystem display) {

		//Create alpha state to blend using alpha
		BlendState as = display.getRenderer().createBlendState();
        as.setBlendEnabled(true);
        as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        as.setTestEnabled(false);
        as.setEnabled(true);

        //Create disabled light state, hud nodes will be displayed
        //as unlit colour
        LightState ls = display.getRenderer().createLightState();
        ls.setEnabled(false);

        setRenderState(as);
        setRenderState(ls);

        //Never cull hud items
        setCullHint(CullHint.Never);

	}

}
