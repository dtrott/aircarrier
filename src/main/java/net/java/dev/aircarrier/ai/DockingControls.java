package net.java.dev.aircarrier.ai;

import com.jme.scene.Spatial;
import com.jmex.terrain.TerrainPage;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.ai.RightingSteerer;
import net.java.dev.aircarrier.controls.PlaneControls;
import net.java.dev.aircarrier.controls.RollFilterControls;
import net.java.dev.aircarrier.controls.StoringPlaneControls;

/**
 * Controls consisting of various different input devices connected
 * to a tree of sub controls, implementing control for AI planes
 * 
 * @author goki
 */
public class DockingControls implements PlaneControls {
	
	DockingState state = DockingState.IDLE;
	
	Spatial dockingTarget;
	
	//Does steering during FLYING state
	TargetSeekingSteerer steering;
	
	//Stores the current controls
	StoringPlaneControls storing;
	
	//Rights plane when necessary
	RightingSteerer righting;
	
	//Filters righting controls
	RollFilterControls filtering;
	
	//Avoid terrain
	TerrainAvoidanceSteerer avoidance;
	
	//Don't hit other steerers
	GroupCollisionAvoidanceSteerer groupAvoidance;

	//Align ourself to docking target when we are docking
	RotationSeekSteerer rotation;
	
	Spatial assemblyBase;
	
	public DockingControls(
			Acobject object, 
			TerrainPage terrain) {
		
		steering = new TargetSeekingSteerer(object);
		
		storing = new StoringPlaneControls(2);
		
		righting = new RightingSteerer(object, 2);
		
		//filtering has quite a large dead zone, and no timeout, so that even if the
		//AI steering is always making small corrections, we will still get levelling.
		//This is important since AI may not level any other way
		//filtering = new RollFilterControls(righting, storing, 0.3f, 2, 0.3f, 0f);
		filtering = new RollFilterControls(righting, storing, 0.8f, 2, 0.6f, 0f);
		
		avoidance = new TerrainAvoidanceSteerer(object, terrain);

		//Set steerers to use prediction Spatial as target
		steering.setTarget(null);

		//Avoid collisions with steering multiplier 100 (very abrupt steering),
		//starting avoidance 4 seconds from collision, and applying full
		//steering 2 seconds from collision.
		groupAvoidance = new GroupCollisionAvoidanceSteerer(
				//assemblyBase, selfSphere, 16, 1f, 1.5f, 5f);
				//assemblyBase, selfSphere, 16, 2f, 5f, 20f);
				object, 16, 2f, 5f, 20f);
		
		//Rotate to align to docking Spatial when DOCKING
		rotation = new RotationSeekSteerer(object, 1);
		
	}
	
	public void update(float time) {

		//Update AI steering
		steering.update(time);
		
		//Copy steering to storing, and fire
		for (int i = 0; i < 3; i++) {
			storing.setAxis(i, steering.getAxis(i));
		}
		
		//Update storing (doesn't do anything, but might as well)
		storing.update(time);
		
		//Update righting
		righting.update(time);
		
		//We now have the main controls updated, and righting updated,
		//so we just update filter to choose appropriately
		filtering.update(time);
		
		//Update terrain avoidance
		avoidance.update(time);
		
		groupAvoidance.update(time);
		
		rotation.update(time);
	}

	public Acobject getDockingTarget() {
		return steering.getTarget();
	}

	public void setDockingTarget(Acobject dockingTarget) {
		//Set targets of steerers
		steering.setTarget(dockingTarget);
		rotation.setDesiredAcobject(dockingTarget);
	}

	/**
	 * @return
	 * 		Our group collision avoidance steerer. The main use of this is to
	 * 		make a list of steerers that should avoid each other, then set
	 * 		this list as the "steerers" property of each steerer.
	 */
	public GroupCollisionAvoidanceSteerer getGroupAvoidance() {
		return groupAvoidance;
	}

	//DELEGATION ########################################
	public void clearFiring() {
		filtering.clearFiring();
	}
	
	public float getAxis(int axis) {
		
		//Max speed when flying, -1 for min speed (should be stop) when not
		if (axis == PlaneControls.THROTTLE) {
			return (state == DockingState.FLYING) ? 1 : -1;
		} else {
			//When flying, use normal steering
			if (state == DockingState.FLYING) {
				//Blend controls in a super complicated way!
				//Basically, when we have both types of avoidance, we
				//blend between them, applying the blend according to the
				//maximum amount of either avoidance.
				//When we have only one avoidance type, it will just be blended with
				//plain steering according to intensity
				//This ensures that if either avoidance is at 1, the normal steering
				//will be completely removed, and the avoidances blended by intensity.
				//So in the worst case, where both are at 1, we at least get a 50/50 mix that
				//might get us out of trouble
				//FIXME put this logic into a PlaneControls that monitors a set of Controls which each
				//have intensity - needs interface for such a PlaneControls as well
				float a = avoidance.getIntensity();
				float ga = groupAvoidance.getIntensity();
				float max = Math.max(a, ga);
				if (max > 0.01) {
					float ra = a/(a + ga);
					float rga = ga/(a + ga);
					float blendAvoidance =  ra * avoidance.getAxis(axis) + (rga) * groupAvoidance.getAxis(axis);
				
					return max * blendAvoidance + (1 - max) * filtering.getAxis(axis);
				} else {
					return filtering.getAxis(axis);
				}
				
			//When docking, just steer to align to target
			} else if (state == DockingState.DOCKING) {
				return rotation.getAxis(axis);
			
			//When idle, don't steer
			} else {
				return 0;
			} 
		}
	}
	
	public int gunCount() {
		return filtering.gunCount();
	}
	public boolean isFiring(int gun) {
		return filtering.isFiring(gun);
	}
	public void moveAxis(int axis, float control) {
		filtering.moveAxis(axis, control);
	}
	public void setAxis(int axis, float control) {
		filtering.setAxis(axis, control);
	}
	public void setFiring(int gun, boolean firing) {
		filtering.setFiring(gun, firing);
	}

	public DockingState getState() {
		return state;
	}

	public void setState(DockingState state) {
		this.state = state;
	}
	
}
