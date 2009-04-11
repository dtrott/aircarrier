/*
 *  $Id: BulletManager.java,v 1.8 2006/11/05 12:44:33 shingoki Exp $
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.java.dev.aircarrier.scene.CarrierTerrainPage;

import com.jme.intersection.BoundingPickResults;
import com.jme.intersection.PickData;
import com.jme.intersection.PickResults;

public class BulletManager {

	List<Bullet> activeBullets = new ArrayList<Bullet>();

	List<ShootableNode> shootNodes = new ArrayList<ShootableNode>();

	List<HitEffect> hitEffects = new ArrayList<HitEffect>();
	
	PickResults results;
	
	CarrierTerrainPage terrain;
	
	ReusableSource<HitEffect> hitEffectsSource = null;

	ShootableNode ignoreShootNode = null;
	
	public BulletManager() {
		super();
		
		results = new BoundingPickResults();
		results.setCheckDistance(true);
	}

	public void addBullet(Bullet b) {
		if (activeBullets.contains(b)) {
			System.out.println("ADDED BULLET TO MANAGER WHICH IS ALREADY THERE");
		}
		activeBullets.add(b);
	}
	
	public List<ShootableNode> getShootNodes() {
		return shootNodes;
	}

	public void setShootNodes(List<ShootableNode> shootNodes) {
		this.shootNodes = shootNodes;
	}
		
	public ShootableNode getIgnoreShootNode() {
		return ignoreShootNode;
	}

	public void setIgnoreShootNode(ShootableNode ignoreShootNode) {
		this.ignoreShootNode = ignoreShootNode;
	}

	public CarrierTerrainPage getTerrain() {
		return terrain;
	}

	public void setTerrain(CarrierTerrainPage terrain) {
		this.terrain = terrain;
	}

	public ReusableSource<HitEffect> getHitEffectsSource() {
		return hitEffectsSource;
	}

	public void setHitEffectsSource(ReusableSource<HitEffect> hitEffectsSource) {
		this.hitEffectsSource = hitEffectsSource;
	}

	public void update(float time) {
		
		Iterator<HitEffect> hitEffectIterator = hitEffects.iterator();
		while (hitEffectIterator.hasNext()) {
			HitEffect effect = hitEffectIterator.next();
			effect.update(time);
			if (effect.expired()) {
				effect.die();
				hitEffectIterator.remove();
			}
		}
		
		Iterator<Bullet> iterator = activeBullets.iterator();
		while(iterator.hasNext()) {
			
			boolean remove = false;
			
			Bullet b = iterator.next();
			b.update(time);
			
			if (b.expired()) {
				remove = true;
			}

			if (terrain != null) {
				float y = terrain.getHeight(b.getWorldTranslation());
				if (b.getWorldTranslation().y < y) {
					//b.setSpeed(-Math.abs(b.getSpeed()));
					collideBullet(b);
					remove = true;
				}
			}
			
			for (ShootableNode n : shootNodes) {
				if (n != ignoreShootNode) {
					results.clear();
					n.calculatePick(b.getRay(), results);
					if (results.getNumber() > 0) {
						float lSquared = b.getRay().getDirection().lengthSquared();
						for (int j = 0; j < results.getNumber(); j++) {
							PickData pData = results.getPickData(j);
							if (pData.getDistance()*pData.getDistance() < lSquared) {
								//System.err.println(pData.getDistance());
								//System.err.println(pData.getTargetMesh().getParentGeom().getParent());
								//b.setSpeed(-Math.abs(b.getSpeed()));
								n.shoot(b);
								collideBullet(b);
								remove = true;
							}
						}
					}
				}
			}
			
			if (remove) {
				iterator.remove();
				b.die();
			}
			
		}		
	}
	
	private void collideBullet(Bullet b) {
		if (hitEffectsSource != null) {
			HitEffect effect = hitEffectsSource.get();
			effect.fire(b);
			hitEffects.add(effect);
		}
	}
	
}
