package net.java.dev.aircarrier.ai;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.controls.PlaneControls;
import net.java.dev.aircarrier.controls.PlaneControlsWithSensor;
import net.java.dev.aircarrier.input.action.NodeTranslator;

import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jmex.terrain.TerrainPage;

/**
 * Watches the height of terrain at some slightly randomised points in
 * front of self, and steers up (global up) when too close
 * 
 * @author goki
 */
public class TerrainAvoidanceSteerer implements PlaneControlsWithSensor {

	Acobject self;
	TerrainPage terrain;
	
	Vector2f steer = new Vector2f();
	float steerMultiplier;

	Vector3f position = new Vector3f();
	Vector3f check = new Vector3f();
	Vector3f ahead = new Vector3f();
	Vector3f turningCenter = new Vector3f();
	Vector3f globalY = new Vector3f(0, 1, 0);

	Vector3f planeUpAxis = new Vector3f();
	Vector3f idealWingAxis = new Vector3f();
	Vector3f idealUpAxis = new Vector3f();

	
	float lookDistance;
	
	int checkPoints;
	
	float spacing;
	
	float minimumHeight;
	
	float intensity;
	
	float turningRadius;

	float turningRadiusSquared;
	
	float radiusInterval;

	float turningRadiusMax;	

	float turningRadiusMaxSquared;	
	
	/**
	 * FIXME find turning radius based on turn rate and speed
	 * 
	 * Create steerer with default settings (steering factor 8, look 50 ahead, check 5 points)
	 * @param self
	 * 		The Spatial we are steering for
	 * @param terrain
	 * 		The terrain we are avoiding
	 */
	public TerrainAvoidanceSteerer(Acobject self, TerrainPage terrain) {
		this(self, terrain, 8f, 100f, 5, 10f, 120f, 10f);
		//this(self, terrain, 8f, 100f, 5, 10f, 10f, 10f);
	}

	/**
	 * Create steerer
	 * @param self
	 * 		The Spatial we are steering for
	 * @param terrain
	 * 		The terrain we are avoiding
	 * @param steerMultiplier
	 * 		The steering multiplier (steering is multiplied, then 
	 * 		limited to -1 to 1)
	 * @param lookDistance
	 * 		The maximum distance to look ahead for collisions
	 * @param checkPoints
	 * 		The number of terrain points to check per update
	 */
	public TerrainAvoidanceSteerer(
			Acobject self, 
			TerrainPage terrain, 
			float steerMultiplier, 
			float lookDistance, 
			int checkPoints,
			float minimumHeight,
			float turningRadius,
			float radiusInterval) {
		super();
		this.self = self;
		this.terrain = terrain;
		this.steerMultiplier = steerMultiplier;
		this.lookDistance = lookDistance;
		this.checkPoints = checkPoints;
		this.minimumHeight = minimumHeight; 
		this.turningRadius = turningRadius;
		this.radiusInterval = radiusInterval;
		
		//Precalculate some values
		//Spacing of check points
		spacing = lookDistance / ((float)checkPoints);
		
		//max and squared radii
		turningRadiusSquared = turningRadius * turningRadius;
		turningRadiusMax = turningRadius + radiusInterval;
		turningRadiusMaxSquared = turningRadiusMax * turningRadiusMax;
		
		//Set initial sensor intensity
		intensity = 0;
		
	}
	
	public void update(float time) {
		
		//Reset intensity
		intensity = 0;

		//Ahead vector
		NodeTranslator.makeTranslationVector(self.getRotation(), 2, 1, ahead);

		//Work out whether the plane is pointing nearly upwards or downwards - if so, do no steering
		if (FastMath.abs(ahead.dot(globalY)) > 0.999) {
			steer.set(0, 0);
			return;
		}

		//Current position
		position.set(self.getPosition());		

		//Get the axes. planeUpAxis is the plane's local up (axis of rudder), forward is the way the guns shoot ;)
		NodeTranslator.makeTranslationVector(self.getRotation(), 1, 1, planeUpAxis);
		
		//Work out the axis the wing should have, to be in level flight - this is cross of
		//the plane forward axis and world up axis
		globalY.cross(ahead, idealWingAxis);
		//idealWingAxis.normalizeLocal();
		
		//Work out the "ideal" up vector, if the plane had wings at ideal axis. This is the
		//vector to the center of the circle the plane will turn on, if this steerer acts.
		idealWingAxis.cross(ahead, idealUpAxis);
		idealUpAxis.normalizeLocal();
		
		//Calculate the center of our turning circle, it is turning radius above us (on idealUpAxis)
		turningCenter.set(position);
		idealUpAxis.multLocal(-turningRadius);
		turningCenter.addLocal(idealUpAxis);
		
		//make ahead horizontal
		ahead.set(ahead.x, 0, ahead.z);
		ahead.normalizeLocal();
		
		//Check height above terrain at different distances ahead
		for (int i = 0; i < checkPoints; i++) {
			//Set check to position plus randomised distance
			ahead.mult( (i + FastMath.rand.nextFloat() - 0.5f) * spacing, check );
			check.addLocal(position);
			
			//Work out height of terrain at check point
			float height = terrain.getHeight(check);		

			//If we got a terrain point, calculate turning requirement based on it
			if (!Float.isNaN(height)) {
				//Set check point to terrain point
				check.set(check.x, height + minimumHeight, check.z);
				
				//Work out check point radius squared
				float distSquared = check.distanceSquared(turningCenter);
				
				//Work out intensity this would give
				float thisIntensity = 0;
				if (distSquared < turningRadiusSquared) {
					thisIntensity = 1;
				} else if (distSquared > turningRadiusMaxSquared) {
					thisIntensity = 0;
				} else {
					//Calc based on squared value, should be close enough :)
					thisIntensity = (distSquared - turningRadiusSquared) / (turningRadiusMaxSquared - turningRadiusSquared);
				}
				
				//If this is more intense, keep it
				if (thisIntensity > intensity) {
					intensity = thisIntensity;
				}
			}
		}
		
		//If we have any intensity, then recommend steering up
		if (intensity > 0) {
			AIUtilities.steeringInDirection(self, globalY, steer);
			steer.normalizeLocal();
		//Otherwise no steering
		} else {
			steer.set(0, 0);
		}
		
	}

	public float getIntensity() {
		return intensity;
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
	public void clearFiring() {
	}

	public Vector3f getTurningCenter() {
		return turningCenter;
	}

	
	
}
