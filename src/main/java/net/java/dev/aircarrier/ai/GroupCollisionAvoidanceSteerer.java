package net.java.dev.aircarrier.ai;

import java.util.List;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.controls.PlaneControls;
import net.java.dev.aircarrier.controls.PlaneControlsWithSensor;
import net.java.dev.aircarrier.tracks.ClosestPointOfApproach;

import com.jme.math.Line;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;

/**
 * This steerer is intended to allow a group of steering
 * entities to avoid hitting each other. The behaviour isn't
 * exactly cooperative, since each steerer only steers to avoid
 * collisions - it doesn't "communicate" with other steerers except
 * by checking their velocity and position, etc.
 * 
 * This is intended to be used by making a Steerer for each entity,
 * then making a list of all steerers, and setting the "steerers" property
 * of each steerer to that list. That will then cause each steerer to avoid
 * the other steerers on the list, and so nothing will hit each other.
 * However it is possible to use different lists per steerer, so that each
 * steerer can be avoiding any given other set of steerers.
 * 
 * Also a scalar sensor - reports the time to collision as a sensor output,
 * scaled by a min and max time value (reports 1 if a collision will occur in less than
 * min time, 0 if it will occur in more than max time)
 * 
 * @author goki
 *
 */
public class GroupCollisionAvoidanceSteerer implements PlaneControlsWithSensor {

	Acobject self;

	List<Acobject> others;
	
	Vector2f steer = new Vector2f();
	float steerMultiplier;

	Line us = new Line();
	Line them = new Line();
	
	//Vector3f velocity = new Vector3f();
	//Vector3f lastPosition;
	
	Vector3f ourPos = new Vector3f();
	Vector3f theirPos = new Vector3f();

	Vector3f collisionPosition = new Vector3f();
	
	float intensity = 0;
	
	float minTime;
	float maxTime;
	
	float minSeparation;
	
	//boolean report = false;
	
	/**
	 * Create a steerer with default steering scaling, min and max time set to
	 * switch sensor full on whenever there is a collision predicted (actually when there is a collision
	 * up to 10000 seconds in the future)
	 * @param self
	 * 		A Spatial to consider as "self". This is used to show where we are (in world space)
	 * 		and what direction we are facing (to convert steering into roll/pitch/yaw/throttle)
	 * @param selfSphere
	 * 		An approximate sphere showing where we are (must update as we move)
	 */
	public GroupCollisionAvoidanceSteerer(Acobject self) {
		this(self, 8f, 10000f, 20000f, 0f);
	}

	/*
	public boolean isReport() {
		return report;
	}

	public void setReport(boolean report) {
		this.report = report;
	}
	 */
	
	/**
	 * Create a steerer
	 * @param self
	 * 		A Spatial to consider as "self". This is used to show where we are (in world space)
	 * 		and what direction we are facing (to convert steering into roll/pitch/yaw/throttle)
	 * @param selfSphere
	 * 		An approximate sphere showing where we are (must update as we move)
	 * @param steerMultiplier
	 * 		Steering factor - steering is multiplied by this and then capped to unit. Higher
	 * 		values make steering more abrupt
	 */
	public GroupCollisionAvoidanceSteerer(
			Acobject self, 
			float steerMultiplier, 
			float minTime, 
			float maxTime,
			float minSeparation) {
		super();
		this.self = self;
		this.steerMultiplier = steerMultiplier;
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.minSeparation = minSeparation;
	}
	
	public void update(float time) {
		
		//Reset intensity and steering
		intensity = 0;
		steer.set(0, 0);
		
		//If there are other steerers to avoid, work out steering and intensity
		if (others != null && !others.isEmpty()) {
			
			//Our track
			us.setOrigin(self.getPosition());
			us.setDirection(self.getVelocity());

			boolean foundCollision = false;
			float firstCollisionTime = Float.MAX_VALUE;

			//For each other steerer, calculate closest approach time and positions
			for (Acobject other : others) {
				
				//Don't try to avoid ourself :)
				if (other != self) {
					
					//Their track
					them.setOrigin(other.getPosition());
					them.setDirection(other.getVelocity());
					
					//Calculate closes approach time
					float cpaTime = ClosestPointOfApproach.timeOfTrackClosestApproach(us, them);
					
					/*
					if (report) {
						System.out.println(velocity);
						System.out.println("Us " + lineString(us) + ", them " + lineString(them) + ": " + cpaTime + ", ");
					}
					*/
					
					//If collision time is a little in the future, we are approaching, so consider further
					if (cpaTime > 0.1f) {
						//Calculate where we will each be at collision
						ClosestPointOfApproach.trackAtTime(us, cpaTime, ourPos);
						ClosestPointOfApproach.trackAtTime(them, cpaTime, theirPos);
						
						//Calculate distance squared, compare to our sum of radii squared, 
						//if we will hit, consider further
						float sumOfRadii = self.getRadius() + other.getRadius() + minSeparation; 
						if (ourPos.distanceSquared(theirPos) < sumOfRadii * sumOfRadii) {
							
							//We will hit! Check if this is the soonest collision (we deal with the soonest collision
							//first)
							if (cpaTime < firstCollisionTime) {
								firstCollisionTime = cpaTime;
								collisionPosition.set(theirPos);
								foundCollision = true;
							}
						}
					}
				}
			}
			
			/*
			if (report) {
				System.out.println();
			}
			*/
			
			//We have checked all other steerers - if we have a collision, avoid it
			if (foundCollision) {
				
				//Get steering towards collision
				AIUtilities.steeringTowards(
						self.getPosition(),
						self.getRotation(),
						collisionPosition, 
						steer);
				
				//Scale and cap steering.
				//Note negative multiplier, so we steer AWAY from collision
				steer.multLocal(-steerMultiplier);
				if (steer.length() > 1) {
					steer.normalizeLocal();
				}

				//update sensor based on collision time
				if (firstCollisionTime < minTime) {
					intensity = 1;
				} else if (firstCollisionTime > maxTime) {
					intensity = 0;
				} else {
					intensity = 1 - ((firstCollisionTime - minTime) / (maxTime - minTime));
				}
				
				//System.out.println("Collision in " + firstCollisionTime + " intensity " + intensity);
				
			}
			/*else if (report){
				System.out.println("No collision positions");
			}*/

		}
		
	}
	
	public String lineString(Line l) {
		return l.getOrigin().x + "," + l.getOrigin().y + "," + l.getOrigin().z + "->" + 
				l.getDirection().x + "," + l.getDirection().y + "," + l.getDirection().z;		
	}
	
	public float getIntensity() {
		return intensity;
	}


	public List<Acobject> getOthers() {
		return others;
	}

	public void setOthers(List<Acobject> others) {
		this.others = others;
	}

	public void clearFiring() {
	}

	public float getAxis(int axis) {
		if (axis == PlaneControls.YAW) {
			return -steer.x;
		} else if (axis == PlaneControls.PITCH) {
			return steer.y;
		} else {
			return 0;
		}
	}

	public boolean isFiring(int gun) {
		return false;
	}

	public void moveAxis(int axis, float control) {
	}

	public void setAxis(int axis, float control) {
	}

	public void setFiring(int gun, boolean firing) {
	}
	public int gunCount() {
		return 0;
	}
}
