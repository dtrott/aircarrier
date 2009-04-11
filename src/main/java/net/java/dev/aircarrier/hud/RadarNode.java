/*
 *  $Id: RadarNode.java,v 1.1 2007/02/24 00:15:44 shingoki Exp $
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.jme.scene.Node;

/**
 * A Node having a set of "pings" on a 2D map
 * @author shingoki
 */
public class RadarNode extends Node {
	private static final long serialVersionUID = -7178931841896666077L;

	List<PingNode> pings;
	List<PingNode> unmodifiablePings;
	
	float radius;
	
	/**
	 * Make a Radar node.
	 * @param radius
	 * 		The display radius of the node, this is
	 * 		the distance in radar node local coordinates
	 * 		corresponding to a distance of 1 in a "radar space"
	 */
	public RadarNode(float radius) {
		this("Radar Node", radius);
	}

	/**
	 * Make a Radar node
	 * @param name
	 * 		Name for the node
	 * @param radius
	 * 		The display radius of the node, this is
	 * 		the distance in radar node local coordinates
	 * 		corresponding to a distance of 1 in a "radar space"
	 */
	public RadarNode(String name, float radius) {
		super(name);
		this.radius = radius;
		pings = new ArrayList<PingNode>();
		unmodifiablePings = Collections.unmodifiableList(pings);
	}

	/**
	 * @return
	 * 		An unmodifiable list of the pings being displayed by this radar
	 */
	public List<PingNode> getPings() {
		return unmodifiablePings;
	}
	
	/**
	 * @param ping
	 * 		To add to the radar
	 */
	public void addPing(PingNode ping) {
		pings.add(ping);
		attachChild(ping);
	}
	
	/**
	 * @param ping
	 * 		To remove from the radar
	 */
	public void removePing(PingNode ping) {
		pings.remove(ping);
		detachChild(ping);
	}

	/**
	 * @return
	 * 		The display radius of the node, this is
	 * 		the distance in radar node local coordinates
	 * 		corresponding to a distance of 1 in a "radar space"
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * @param radius
	 * 		The display radius of the node, this is
	 * 		the distance in radar node local coordinates
	 * 		corresponding to a distance of 1 in a "radar space"
	 */
	public void setRadius(float radius) {
		this.radius = radius;
	}

}
