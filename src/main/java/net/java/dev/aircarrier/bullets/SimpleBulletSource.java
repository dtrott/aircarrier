/*
 *  $Id: SimpleBulletSource.java,v 1.6 2006/12/30 16:37:22 shingoki Exp $
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

import com.jme.math.Vector3f;

public class SimpleBulletSource implements ReusableSource<Bullet> {

	float damage;
	float speed;
	float lifetime;
	Vector3f size;
	ReusableSource<Bullet> source;
	long categoryBits;
	long collideBits;
	
	public SimpleBulletSource(
			float damage, 
			float speed, 
			float lifetime, 
			Vector3f size, 
			ReusableSource<Bullet> source,
			long categoryBits,
			long collideBits) {
		super();
		this.damage = damage;
		this.speed = speed;
		this.lifetime = lifetime;
		this.size = size;
		this.source = source;
		this.categoryBits = categoryBits;
		this.collideBits = collideBits;
	}

	public Bullet get() {
		Bullet b = source.get();
		b.setDamage(damage);
		b.setSpeed(speed);
		b.setLifetime(lifetime);
		b.setCategoryBits(categoryBits);
		b.setCollideBits(collideBits);
		b.getLocalScale().set(size);
		
//		FIXME UNUPDATE?
		//b.updateGeometricState(0, true);
		return b;
	}

}
