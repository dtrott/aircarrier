/*
 *  $Id: Reusable.java,v 1.2 2006/07/21 23:58:43 shingoki Exp $
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

package net.java.dev.aircarrier.bullets;

/**
 * An item which can be reused. It dies and may then
 * be reborn as a new item. This is used with ReusableSource.
 * 
 * The means of using this are to get a Reusable from a source,
 * use it, and then tell it to die() when finished with it. This
 * frees it to be reused later.
 * 
 * @author shingoki
 *
 */
public interface Reusable {

	/**
	 * Frees this Reusable to be reused
	 * Do NOT use this reusable again, lose all references to it,
	 * if you use it further you risk using it after it has been 
	 * handed out again, and is doing something else.
	 */
	public void die();
	
}
