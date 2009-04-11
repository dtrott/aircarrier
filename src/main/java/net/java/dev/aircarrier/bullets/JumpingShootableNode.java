/*
 *  $Id: JumpingShootableNode.java,v 1.7 2007/06/03 10:41:11 shingoki Exp $
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

import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.scene.CarrierTerrainPage;

/**
 * @author shingoki
 *
 */
public class JumpingShootableNode extends ShootableNode implements Acobject {
	private static final long serialVersionUID = 3282372501603310387L;

	CarrierTerrainPage terrain;
	
	Vector3f oldPos = new Vector3f();
	Vector3f jump = new Vector3f();
	Vector2f slide = new Vector2f();
	float moveTimeElapsed = -1;
	float moveTimeTotal = 3;
	float jumpDistance = 400f;
	float flashTimeRemaining = -1;
	LightState lightState;
	float flashDuration = 0.5f;
	
	public JumpingShootableNode(String name, CarrierTerrainPage terrain) {
		super(name);
		this.terrain = terrain;
		lightState = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		lightState.setGlobalAmbient(new ColorRGBA(1,1,1,1));
		lightState.setEnabled(false);
		setRenderState(lightState);
		updateRenderState();
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.bullets.ShootableNode#shoot(net.java.dev.aircarrier.bullets.Bullet)
	 */
	@Override
	public void shoot(Bullet b) {
		
		if (flashTimeRemaining < 0) {
			flashTimeRemaining = flashDuration;
		} else {
			flashTimeRemaining += flashDuration;
		}
		
		if (moveTimeElapsed >= 0) {
			return;
		}
		
		float x = getWorldTranslation().x;
		float z = getWorldTranslation().z;
		float height = Float.NaN;

		while (Double.isNaN(height)) {
			float xc = ((float)Math.random()-0.5f);
			float zc = ((float)Math.random()-0.5f);
			
			slide.set(xc, zc).normalizeLocal().multLocal(jumpDistance).addLocal(x, z);
			height = terrain.getHeight(slide);
		}
		
		
		oldPos.set(getWorldTranslation());		
		jump.set(slide.x, height + 100f, slide.y).subtractLocal(oldPos).normalizeLocal().multLocal(jumpDistance);
		moveTimeElapsed = 0;
	}
	
	public void update(float time) {
		
		flashTimeRemaining -= time;
		if (flashTimeRemaining < -1) flashTimeRemaining = -1;
		
		lightState.setEnabled(flashTimeRemaining > 0);
		
		if (moveTimeElapsed < 0) {
			return;
		}
		moveTimeElapsed += time;
		float prop = moveTimeElapsed / moveTimeTotal;
		if (prop > 1) {
			prop = 1;
			moveTimeElapsed = -1;
		}
		
		getLocalTranslation().set(jump).multLocal(prop).addLocal(oldPos);
		
		float height = terrain.getHeight(getWorldTranslation());
		if (getLocalTranslation().y < height + 50) {
			getLocalTranslation().y = height + 50;
		}
		
//		FIXME UNUPDATE?
		//updateGeometricState(time, true);
	}

	public Quaternion getRotation() {
		return getWorldRotation();
	}

	public Vector3f getVelocity() {
		return null;
	}

	public String getVisibleName() {
		return "Airmine";
	}

	public Vector3f getPosition() {
		return getWorldTranslation();
	}

	public float getRadius() {
		return 1;
	}

}
