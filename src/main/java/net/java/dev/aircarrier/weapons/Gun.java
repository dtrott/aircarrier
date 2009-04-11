/*
 *  $Id: Gun.java,v 1.9 2007/03/16 22:40:31 shingoki Exp $
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

package net.java.dev.aircarrier.weapons;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.java.dev.aircarrier.bullets.Bullet;
import net.java.dev.aircarrier.bullets.ReusableSource;
import net.java.dev.aircarrier.scene.MuzzleFlash;

import com.jme.light.Light;
import com.jme.light.PointLight;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.CullState;
import com.jme.system.DisplaySystem;

/**
 * A gun. Can fire bullets at a certain interval,
 * taken from a certain source, allowing for an ammo count.
 * @author shingoki
 *
 */
public class Gun extends Node {
	private static final long serialVersionUID = 2368505542561777031L;

	float fireDelay = 0;
	float fireInterval = 1f;
	int ammoCount = -1;

	ReusableSource<Bullet> bulletSource;
	Node bulletNode;

	Node muzzleFlash;
	CullState muzzleCull;

	float muzzleRampTime = 0.01f;
	float muzzleDecayTime = 0.07f;
	float muzzleMaxSize = 3f;
	float muzzleTime = -1f;

	float maxLight = 0.5f;

	Light light;

	List<FireListener> listeners = new ArrayList<FireListener>();

	public Gun(String name, ReusableSource<Bullet> bulletSource, Node bulletNode) throws IOException {
		super(name);
		this.bulletSource = bulletSource;
		this.bulletNode = bulletNode;

		muzzleFlash = MuzzleFlash.createMuzzleFlash();
		muzzleCull = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
		muzzleCull.setCullFace(CullState.Face.FrontAndBack);
		muzzleCull.setEnabled(true);
		muzzleFlash.setRenderState(muzzleCull);
		muzzleFlash.updateRenderState();
		muzzleFlash.setLocalScale(0f);
		attachChild(muzzleFlash);
		updateGeometricState(0, true);

		light = new PointLight();
		light.setDiffuse(new ColorRGBA(0f, 0f, 0f, 1.0f));
		light.setSpecular(new ColorRGBA(0f, 0f, 0f, 1.0f));
		//light.setAttenuate(true);
		light.setLinear(0.1f);
		light.setQuadratic(0f);
		light.setConstant(0f);
		light.setEnabled(false);

	}

	/**
	 * Trigger the weapon. May or may not actually fire.
	 * This should be called on each update where the user
	 * would like to fire
	 * @return
	 * 		The bullet fired, or null if none was fired
	 */
	public Bullet trigger() {
		if (!readyToFire() || !hasAmmo() ) {
			return null;
		}

		Bullet b = bulletSource.get();
		b.fire(this);

		fireDelay = fireInterval;
		expendAmmo();


		muzzleTime = 0f;
		muzzleCull.setEnabled(false);

		fireFireEvent();

		return b;
	}

	public void addFireListener(FireListener listener) {
		listeners.add(listener);
	}

	public void removeFireListener(FireListener listener) {
		listeners.remove(listener);
	}

	private void fireFireEvent() {
		for (FireListener listener : listeners) {
			listener.fired(this);
		}
	}

	public void update(float time) {

		//If muzzle flash is in progress
		if (muzzleTime >= 0f) {
			//Move flash along
			muzzleTime += time;
			//Work out size
			float size = 0f;

			//We are still increasing size, so interpolate to max size
			if (muzzleTime < muzzleRampTime) {
				size = (muzzleTime / muzzleRampTime) * muzzleMaxSize;

			//We are past the ramp
			} else {

				//Work out where we are in decay
				float decayTime = muzzleTime - muzzleRampTime;
				if (decayTime < muzzleDecayTime) {
					size = ((muzzleDecayTime-decayTime) / muzzleDecayTime) * muzzleMaxSize;

				//We have finished flash completely, so hide it
				//and set time to -1 so we will ignore on update
				} else {
					muzzleCull.setEnabled(true);
					muzzleTime = -1f;
					size = -1;
				}
			}
			//Set size appropriately
			muzzleFlash.setLocalScale(size < 0 ? 0 : size);

			//Set light appropriately
			if (size > 0) {
				light.setDiffuse(new ColorRGBA(1.0f, 1.0f, 0.7f, 1.0f));
				light.setSpecular(new ColorRGBA(1.0f, 1.0f, 0.7f, 1.0f));
				light.getDiffuse().multLocal(size*maxLight/muzzleMaxSize);
				light.getDiffuse().multLocal(size*maxLight/muzzleMaxSize);
				light.setEnabled(true);
			} else {
				light.setDiffuse(new ColorRGBA(0f, 0f, 0f, 0f));
				light.setSpecular(new ColorRGBA(0f, 0f, 0f, 0f));
				light.setEnabled(false);
			}

		}

		//Update delay/interval
		if (fireDelay > -1) {
			fireDelay -= time;
		}


	}

	public Light getLight() {
		return light;
	}

	public boolean readyToFire() {
		return (fireDelay <= 0);
	}

	private void expendAmmo() {
		if (ammoCount > 0) ammoCount--;
	}

	public boolean hasAmmo() {
		return (ammoCount != 0);
	}

	public int getAmmoCount() {
		return ammoCount;
	}

	public void setAmmoCount(int ammoCount) {
		this.ammoCount = ammoCount;
	}

	public float getFireDelay() {
		return fireDelay;
	}

	public void setFireDelay(float fireDelay) {
		this.fireDelay = fireDelay;
	}

	public float getFireInterval() {
		return fireInterval;
	}

	public void setFireInterval(float fireInterval) {
		this.fireInterval = fireInterval;
	}

}
