/*
 *  $Id: HitEffectPool.java,v 1.2 2006/07/21 23:58:29 shingoki Exp $
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
import java.util.List;

import com.jme.scene.Node;
import com.jme.scene.TriMesh;

/**
 * A basic source of HitEffects
 */
public class HitEffectPool implements ReusablePool<HitEffect>{

	private final static float DEFAULT_LIFETIME = 0.6f; 
	
	Node worldNode;
	SimpleMeshFactory flareFactory;
	SimpleMeshFactory ringFactory;

	private List<HitEffect> effects = new ArrayList<HitEffect>(500);
	
	/**
	 * Create a factory
	 * 
	 * @param worldNode
	 * 		The world node the HitEffects will be given 
	 * @param flareFactory
	 * 		The factory to get HitEffect flare meshes
	 * @param ringFactory
	 * 		The factory to get HitEffect ring meshes
	 */
	public HitEffectPool(
			Node worldNode,
			SimpleMeshFactory flareFactory,
			SimpleMeshFactory ringFactory) {
		
		this.worldNode = worldNode;
		this.flareFactory = flareFactory;
		this.ringFactory = ringFactory;
	}
	
	/**
	 * Get a new Hit Effect
	 * @return
	 * 		A new plain HitEffect
	 */
	public HitEffect get() {

		if (!effects.isEmpty()) {
			HitEffect e = effects.remove(0);
			e.setAge(0);
			e.setLifetime(DEFAULT_LIFETIME);		
			e.setPool(this);
			return e;
		} else {
			return make();
		}

	}
	
	/**
	 * Give back an effect
	 * @param effect
	 * 		HitEffect, you must NOT use this any further, best to
	 * 		null the reference to make sure.
	 */
	public void returnReusable(HitEffect effect) {
		effects.add(effect);
	}
	
	/**
	 * Make a new HitEffect instance and return
	 * @return
	 * 		New HitEffect instance
	 */
	private HitEffect make() {

		//Make a shared mesh from the triMesh
		TriMesh flare = flareFactory.getMesh();
		TriMesh ring = ringFactory.getMesh();

		//Make a new effect
		HitEffect effect = new HitEffect(
				"hit effect", 
				0, DEFAULT_LIFETIME, 
				this, 
				worldNode, 
				flare,
				ring);
		//Return the effect
		return effect;
	}
	
}
