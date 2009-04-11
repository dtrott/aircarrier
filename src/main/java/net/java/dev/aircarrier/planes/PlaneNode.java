/*
 *  $Id: PlaneNode.java,v 1.1 2007/04/28 23:13:54 shingoki Exp $
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
import java.util.ArrayList;
import java.util.List;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jmex.effects.particles.ParticleMesh;

import net.java.dev.aircarrier.bullets.Bullet;
import net.java.dev.aircarrier.bullets.BulletManager;
import net.java.dev.aircarrier.bullets.ReusableSource;
import net.java.dev.aircarrier.bullets.SimpleBulletSource;
import net.java.dev.aircarrier.scene.ApproximatelySphericalNode;
import net.java.dev.aircarrier.scene.SmokerFactory;
import net.java.dev.aircarrier.util.ListUtils;
import net.java.dev.aircarrier.weapons.Gun;

public class PlaneNode extends Node {
	private static final long serialVersionUID = -9210991335766328056L;
	
	BulletManager bulletManager;
	
	PlaneAssembly assembly;
	List<Gun> guns;
	PlaneModel model;
			
	List<Propeller> propellers = new ArrayList<Propeller>();
	List<SmokerAndFlamer> smokersAndFlamers = new ArrayList<SmokerAndFlamer>();
	
	float runTime = 0f;
	
	float maxHealth = 1f;
	float health = 1f;
	
	float highDamage = 0.7f;
	float midDamage = 0.4f;
	float fullDamage = 1f;
		
	int maxDamageEffects = 2;
	//int maxDamageEffects = 0;
	
	public PlaneNode(String name, PlaneModel model, Node bulletNode, ReusableSource<Bullet> source, BulletManager bulletManager) throws IOException {
		super(name);
		
		this.bulletManager = bulletManager;
		
		this.model = model;
		
		attachChild(model.getModel());
		updateGeometricState(0, true);
		
		//speed was 300
		SimpleBulletSource bulletSource0 = 
			new SimpleBulletSource(0.04f, 450, 5, new Vector3f(40,15f,15f), source, 0, 0);
		//SimpleBulletSource bulletSource1 = bulletSource0;
		SimpleBulletSource bulletSource1 = 
			new SimpleBulletSource(0.06f, 450, 5, new Vector3f(40f,22.5f,22.5f), source, 0, 0);
		
		guns = new ArrayList<Gun>();
		int gunIndex = 0;
		for (Node gunPosition : model.getGunPositions()) {
			Gun gun = new Gun(name + "Gun" + gunIndex, gunIndex<2 ? bulletSource0 : bulletSource1, bulletNode);
			gunPosition.attachChild(gun);
			guns.add(gun);
			//gun.setFireInterval(gunIndex<2 ? 0.8f : 1.05f);
			//gun.setFireInterval(gunIndex<2 ? 0.4f : 0.55f);
			//gun.setFireInterval(gunIndex<2 ? 0.2f : 0.55f);
			//gun.setFireInterval(gunIndex<2 ? 1.05f : 0.7f);
			gun.setFireInterval(gunIndex<2 ? 0.9f : 0.4f);

			gunIndex++;
		}
		
		
		LightState lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		for (Gun gun : guns) {
			lightState.attach(gun.getLight());
		}
		
		setRenderState(lightState);
		updateRenderState();
		
		for (Node propPosition : model.getPropPositions()) {
			Propeller prop = new Propeller(new Vector3f(0, -0.422f, -1));
			propPosition.attachChild(prop);
			propellers.add(prop);
		}

		
		for (Node smokerPosition : model.getDamagePositions()) {
			ParticleMesh smoker = SmokerFactory.makeSmoker();
			smokerPosition.attachChild(smoker);
			smoker.setReleaseRate(0);
			
			ParticleMesh flamer = SmokerFactory.makeFlamer();
			smokerPosition.attachChild(flamer);
			flamer.setReleaseRate(0);
			smokersAndFlamers.add(new SmokerAndFlamer(smoker, flamer));
		}

		//System.out.println("SmokersAndFlamers size " + smokersAndFlamers.size());
		
		respawn();
		
	}
	
	public void respawn() {
		
		shuffleSmokers();
		setHealth(getMaxHealth());
		
	}
	
	/**
	 * Shuffle smokers and flamers, so that they don't
	 * switch on in the same order every time
	 */
	private void shuffleSmokers() {
		ListUtils.shuffleList(smokersAndFlamers);
	}
	
	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
		
		//Use only as many effects as we have smokers and flamers
		int total = smokersAndFlamers.size();
		
		//limit number of effects to maxDamageEffects
		if (total > maxDamageEffects) total = maxDamageEffects;
		
		//If we have no damage points, just switch off the effects
		if (total == 0) {
			for (SmokerAndFlamer saf : smokersAndFlamers) {
				saf.getSmoker().setReleaseRate(0);
				saf.getFlamer().setReleaseRate(0);
			}
		}
		
		//Convert health into damage scaled from 0 to 1
		float damage = 1 - (health/maxHealth);
		if (damage > 1f) damage = 1f;
		if (damage < 0f) damage = 0f;

		//We will turn on smokers between mid and high damage, then turn
		//on flamers between high and full damage.
		//Note that we want to turn on the first smoker at mid damage exactly, then
		//the last smoker one "interval" BEFORE high damage, not AT high damage. Similarly for
		//flamers
		int fCount = 0;
		int sCount = 0;
		if (damage > midDamage) {
			fCount = 0;
			sCount = effectsFor(damage, midDamage, highDamage, total);
		}
		if (damage > highDamage) {
			sCount = total;
			fCount = effectsFor(damage, highDamage, fullDamage, total);
		}
		
		//System.out.println("Switch on " + sCount + " smokers and " + fCount + " flamers.");

		for (int i = 0; i < smokersAndFlamers.size(); i++) {
			smokersAndFlamers.get(i).getSmoker().setReleaseRate(i < sCount ? SmokerFactory.SMOKE_RATE : 0);
			smokersAndFlamers.get(i).getFlamer().setReleaseRate(i < fCount ? SmokerFactory.FLAME_RATE : 0);
		}

	}

	/**
	 * Calculate the number of effects to turn on at a given damage
	 * @param damage
	 * 		The current damage
	 * @param min
	 * 		The min level at which one effect is turned on
	 * @param max
	 * 		The max level, at which total+1 effects would be turned on
	 * @param total
	 * 		The total number of effects
	 * @return
	 * 		A number of effects, from 1 to total
	 */
	private int effectsFor(float damage, float min, float max, int total) {
		//System.out.println("Effects for damage " + damage + " min " + min + " max " + max + " total " + total);
		int count = (int)(((damage - min) / (max - min)) * total + 1);
		if (count < 1) count = 1;
		if (count > total) count = total;
		return count;
	}
	
	public float getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(float maxHealth) {
		this.maxHealth = maxHealth;
	}

	public List<Node> getBulletBounds() {
		return model.getBulletBounds();
	}
	
	public void damage(float damage) {
		setHealth(getHealth() - damage);
	}
	
	public void fire(int system) {
		if (system == 1) {
			addBullet(guns.get(0).trigger());
			addBullet(guns.get(1).trigger());
		} else {
			addBullet(guns.get(2).trigger());
			if (guns.size() > 3) {
				addBullet(guns.get(3).trigger());
			}
		}
	}
	
	private void addBullet(Bullet b) {
		if (b != null) {
			bulletManager.addBullet(b);
		}
	}
	
	public void update(float time) {
		
		bulletManager.update(time);
		
		for (Gun gun : guns) {
			gun.update(time);
		}
		
		for (Propeller prop : propellers) {
			prop.update(time);
		}
				
	}
	
	public ApproximatelySphericalNode getAvoidanceNode() {
		return model.getAvoidanceNode();
	}
	
	public Vector3f getCameraOffset() {
		return model.getCameraOffset();
	}
	
	private class SmokerAndFlamer {
		ParticleMesh smoker;
		ParticleMesh flamer;
		public SmokerAndFlamer(ParticleMesh smoker, ParticleMesh flamer) {
			super();
			this.smoker = smoker;
			this.flamer = flamer;
		}
		public ParticleMesh getFlamer() {
			return flamer;
		}
		public ParticleMesh getSmoker() {
			return smoker;
		}
	}
	
	public List<Gun> getGuns() {
		return guns;
	}
}
