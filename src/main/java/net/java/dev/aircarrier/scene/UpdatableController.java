/*
 *  $Id: UpdatableController.java,v 1.1 2007/04/28 23:19:07 shingoki Exp $
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

package net.java.dev.aircarrier.scene;

import com.jme.scene.Controller;

public class UpdatableController extends Controller {
	private static final long serialVersionUID = -4615440315885110964L;
	
	Updatable updatable;
	
	public UpdatableController(Updatable updatable) {
		super();
		this.updatable = updatable;
	}

	public void update(float time) {
		updatable.update(time);
	}
	
}
