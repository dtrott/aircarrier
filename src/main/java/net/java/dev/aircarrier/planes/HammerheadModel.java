/*
 *  $Id: HammerheadModel.java,v 1.10 2007/05/31 21:55:51 shingoki Exp $
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

package net.java.dev.aircarrier.planes;

import java.io.IOException;

public class HammerheadModel extends SimplePlaneModel{

	public HammerheadModel() throws IOException {
		this(true);
	}
	
	public HammerheadModel(boolean mipmap) throws IOException {
		super(
				"resources/hammerhead2.jme", 
				"resources/hammerhead_twin_rudder_tex.jpg", 
				"resources/hammerhead_twin_rudder_tex.jpg", 
				1, mipmap);
	}

}
