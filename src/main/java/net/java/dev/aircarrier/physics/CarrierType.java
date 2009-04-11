/*
 *  $Id: CarrierType.java,v 1.3 2006/07/21 23:59:17 shingoki Exp $
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

package net.java.dev.aircarrier.physics;

import com.jme.scene.Node;

public interface CarrierType {
	
	public long PLAYER 			= 1;
	public long ENEMY 			= 2;
	public long LEVEL 			= 4;
	public long PLAYER_BULLET 	= 8;
	public long ENEMY_BULLET 	= 16;
	public long OTHER_SOLID		= 32;
	
	/**
	 * @return
	 * 		The category bits of carrier object
	 * 		Check against constants in this interface,
	 * 		and use for physics objects
	 */
	public long getCategoryBits();

	/**
	 * @return
	 * 		The collide bits of carrier object
	 * 		Check against constants in this interface,
	 * 		and use for physics objects
	 */
	public long getCollideBits();

	/**
	 * @return
	 * 		The node this carrier type is associated with
	 */
	public Node getNode();
}
