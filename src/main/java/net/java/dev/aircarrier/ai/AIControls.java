package net.java.dev.aircarrier.ai;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.jme.math.FastMath;
import com.jme.scene.Node;
import com.jmex.terrain.TerrainPage;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.ai.RightingSteerer;
import net.java.dev.aircarrier.controls.AlternateBehaviourControls;
import net.java.dev.aircarrier.controls.PlaneControls;
import net.java.dev.aircarrier.controls.PlaneControlsWithSensor;
import net.java.dev.aircarrier.controls.RollFilterControls;
import net.java.dev.aircarrier.controls.SeparateThrottleControls;
import net.java.dev.aircarrier.controls.SmoothedPlaneControls;
import net.java.dev.aircarrier.controls.StoringPlaneControls;

/**
 * Controls consisting of various different input devices connected
 * to a tree of sub controls, implementing control for AI planes
 * 
 * @author goki
 */
public class AIControls implements PlaneControls, TargettingControls {

	//Node to add targets etc.
	Node worldNode;
	
	//Track the target
	DeflectionShootingNode deflection;
	
	//Does AI steering
	TargetSeekingSteerer steering;
	
	//Stores the current controls
	StoringPlaneControls storing;
	
	//Rights plane when necessary
	RightingSteerer righting;
	
	//Filters righting controls
	RollFilterControls filtering;
	
	//Avoid terrain
	TerrainAvoidanceSteerer avoidance;
	
	//Maintain distance from target
	DistanceMaintenanceSteerer distance;
	
	//Don't hit other steerers
	GroupCollisionAvoidanceSteerer groupAvoidance;
	
	//Monitor targetting
	TargettingSensor targetting;

	//Monitor how we are being targetted
	TargettingSensor targetted;
	
	//Combine alternate controls with main controls
	AlternateBehaviourControls alternateSteering;

	//Combine throttle with everything else
	SeparateThrottleControls throttleSteering;
	
	//Smooth the final combined controls
	SmoothedPlaneControls smoothed;
	
	DecimalFormat format = new DecimalFormat("0.00");

	public AIControls(Acobject object, Node worldNode, int gunCount, TerrainPage terrain) {
		
		this.worldNode = worldNode;
		
		deflection = new DeflectionShootingNode(object, 450);
		worldNode.attachChild(deflection);
		//deflection.attachChild(new Sphere("deflection", 10, 10, 1));
		
		steering = new TargetSeekingSteerer(object);
		
		storing = new StoringPlaneControls(gunCount);
		
		righting = new RightingSteerer(object, 2);
				
		avoidance = new TerrainAvoidanceSteerer(object, terrain);

		distance = new DistanceMaintenanceSteerer(object, 90);

		//Monitor targetting and being targetted - we assess targetting between 22.5 and 45 degrees
		targetting = new TargettingSensor(object, FastMath.cos(FastMath.PI/8f), FastMath.cos(FastMath.PI/4f));
		targetted = new TargettingSensor(null, FastMath.cos(FastMath.PI/8f), FastMath.cos(FastMath.PI/4f));
		targetted.setTarget(object);

		//Set steerers and sensors to use prediction node as target
		steering.setTarget(deflection);
		distance.setTarget(deflection);
		targetting.setTarget(deflection);

		//Avoid collisions with steering multiplier 100 (very abrupt steering),
		//starting avoidance 4 seconds from collision, and applying full
		//steering 2 seconds from collision.
		groupAvoidance = new GroupCollisionAvoidanceSteerer(
				//assemblyBase, selfSphere, 16, 1f, 1.5f, 5f);
				//assemblyBase, selfSphere, 16, 2f, 5f, 20f);
				//assemblyBase, selfSphere, 16, 2f, 3f, 30f);
				object, 16, 2f, 3f, 30f);

		//Combine avoidance steering (alternate steering) into target seeking
		List<PlaneControlsWithSensor> alternateSteerings = new ArrayList<PlaneControlsWithSensor>();
		alternateSteerings.add(avoidance);
		alternateSteerings.add(groupAvoidance);
		alternateSteering = new AlternateBehaviourControls(steering, alternateSteerings);
		
		//Finally combine throttle control with alternate steering
		throttleSteering = new SeparateThrottleControls(distance, alternateSteering);
		
		//filtering has quite a large dead zone, and no timeout, so that even if the
		//AI steering is always making small corrections, we will still get levelling.
		//This is important since AI may not level any other way
		//filtering = new RollFilterControls(righting, storing, 0.3f, 2, 0.3f, 0f);
		filtering = new RollFilterControls(righting, throttleSteering, 0.8f, 2, 0.6f, 0f);

		//Finally, smooth the end result
		smoothed = new SmoothedPlaneControls(filtering, 0.075f);
	}
	
	public void update(float time) {

		//Update target prediction
		deflection.update(time);
		
		//Update AI main steering
		steering.update(time);

		//Update alternate steerings
		avoidance.update(time);
		groupAvoidance.update(time);

		//Update distance control
		distance.update(time);
		
		//Update AI alternate steering combined with main steering
		alternateSteering.update(time);
		
		//Update control with throttle
		throttleSteering.update(time);

		//Update righting
		righting.update(time);

		//We now have the main controls updated, and righting updated,
		//so we just update roll filter to choose appropriately
		filtering.update(time);
		
		//Smooth the final control results
		smoothed.update(time);
		
		//Copy smoothed to storing, and fire
		for (int i = 0; i < 3; i++) {
			storing.setAxis(i, smoothed.getAxis(i));
		}

		//Fire guns according to targetting
		if (targetting.getIntensity() > 0.1) storing.setFiring(0, true);
		if (targetting.getIntensity() > 0.7) storing.setFiring(1, true);
				
		//Update storing (doesn't do anything, but might as well)
		storing.update(time);
		
		targetting.update(time);
		targetted.update(time);
		
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.ai.TargettingControls#getTarget()
	 */
	public Acobject getTarget() {
		return deflection.getTarget();
	}

	/* (non-Javadoc)
	 * @see net.java.dev.aircarrier.ai.TargettingControls#setTarget(net.java.dev.aircarrier.acobject.Acobject)
	 */
	public void setTarget(Acobject target) {
		deflection.setTarget(target);
		
		//Sense being targetted back by the target
		targetted.setSelf(target);
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
		storing.clearFiring();
	}
	public float getAxis(int axis) {
		return storing.getAxis(axis);
	}
	public int gunCount() {
		return storing.gunCount();
	}
	public boolean isFiring(int gun) {
		return storing.isFiring(gun);
	}
	public void moveAxis(int axis, float control) {
		storing.moveAxis(axis, control);
	}
	public void setAxis(int axis, float control) {
		storing.setAxis(axis, control);
	}
	public void setFiring(int gun, boolean firing) {
		storing.setFiring(gun, firing);
	}
	
}
