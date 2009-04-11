/*
 *  $Id: PlaneLandscapeBouncer.java,v 1.4 2007/04/28 23:15:30 shingoki Exp $
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

/*
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

import net.java.dev.aircarrier.scene.CarrierTerrainPage;
*/

/**
 * Update monitors a plane assembly, and a landscape heightmap,
 * and bounces the plane assembly off the landscape if any of its plane's collision
 * points pass into the landscape.
 * 
 * @author websterb
 */
public class PlaneLandscapeBouncer {
/*
	PlaneAssembly planeAssembly;
	CarrierTerrainPage terrain;
	
	Vector3f normal = new Vector3f();
	
	public PlaneLandscapeBouncer(PlaneAssembly planeAssembly, CarrierTerrainPage terrain) {
		super();
		this.planeAssembly = planeAssembly;
		this.terrain = terrain;
	}

	public void update(float time) {
		Node points = planeAssembly.getPlane().getCollisionPoints();
		
		float maxPenetration = 1;
		
		for (Object o : points.getChildren()) {
			Spatial point = (Spatial)o;
			
			//Find terrain height at point, and penetration of point into terrain
			Vector3f position = point.getWorldTranslation();
			float height = terrain.getHeight(position);
			float penetration = position.y - height;
			
			//If this is the deepest penetration, store the penetration and surface normal
			if (penetration < maxPenetration){
				maxPenetration = penetration;
				terrain.getSurfaceNormal(position, normal);
			}
		}
		
		//If we got any penetration (sounds rude) then bounce the plane
		if (maxPenetration < 0) {
			//Move plane above terrain
			planeAssembly.getBase().getLocalTranslation().addLocal(0, -maxPenetration, 0);
			
			//TODO add an impulse along the terrain normal, and/or steer plane in direction of normal,
			//etc. Also best to move the plane back to it's previous position, rather than moving
			//it above terrain, but this requires good handling of steering/impulse so that the plane
			//doesn't just wedge in the terrain
		}
		
	}
	*/

}
