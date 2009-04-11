package net.java.dev.aircarrier.ai;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.acobject.MovableAcobject;
import net.java.dev.aircarrier.controls.SteeringController;
import net.java.dev.aircarrier.controls.SteeringThrottleController;
import net.java.dev.aircarrier.util.FloatSpring;
import net.java.dev.aircarrier.util.FloatSpring3f;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Controller;
import com.jmex.terrain.TerrainPage;

public class DockingController extends Controller {
	private static final long serialVersionUID = 5497345991166142247L;

	DockingState state = DockingState.IDLE;
	
	MovableAcobject object;
	
	Acobject target = null;

	float dockingRadiusSquared;

	DockingControls dockingControls;
	
	TerrainPage terrain; 
	float dockingRadius;
	
	FloatSpring3f dockingSpring;
	
	SteeringThrottleController throttleController;
	SteeringController steeringController;

	Vector3f velocity = new Vector3f();
	
	public DockingController(
			MovableAcobject object, 
			TerrainPage terrain) {
		this(object, terrain,
				object.getRadius() * 8f,
				200,
				60,
				5f,
				150);
	}
	
	public DockingController(
			MovableAcobject object, 
			TerrainPage terrain, 
			float dockingRadius,
			float speed,
			float accelerationK,
			float steeringFactor,
			float dockingK) {
		super();
		this.object = object;
		dockingControls = new DockingControls(object, terrain);
		
		throttleController = new SteeringThrottleController(
				object, 
				dockingControls, 
				0,					//Slowest speed is stop 
				0, 
				speed, 
				new FloatSpring(accelerationK));
		steeringController = new SteeringController(
				object, 
				dockingControls, 
				new float[]{
						-1.15f * steeringFactor, 
						-1.15f * steeringFactor, 
						2.5f * steeringFactor});
		dockingSpring = new FloatSpring3f(dockingK);
		dockingSpring.setSpeedMax(speed);
		setDockingRadius(dockingRadius);
	}

	@Override
	public void update(float time) {
		
		//System.out.println("State " + state + " steering " + dockingControls.getAxis(0) + ", " + dockingControls.getAxis(1) + ", " + dockingControls.getAxis(2) + ", " + dockingControls.getAxis(3));
		
		dockingControls.update(time);
		steeringController.update(time);
		dockingSpring.update(time);
		
		
		//If we are flying and have reached docking radius, then switch to DOCKING
		if (state == DockingState.FLYING) {
			throttleController.update(time);
			float distSquared = object.getPosition().distanceSquared(target.getPosition());
			if (distSquared < dockingRadiusSquared) {
				setState(DockingState.DOCKING);
				//Initialise docking spring
				dockingSpring.getPosition().set(object.getPosition());
				dockingSpring.getVelocity().set(object.getVelocity());
				dockingSpring.getTarget().set(target.getPosition());
			}
		}
		
		//If we are docking, update position from spring
		if (state == DockingState.DOCKING) {
			object.setPosition(dockingSpring.getPosition());
		}

	}

	public Acobject getTarget() {
		return target;
	}

	public void setTarget(Acobject target) {
		//Go back to flying state if we have a target, IDLE if null
		setState((target==null) ? DockingState.IDLE : DockingState.FLYING);
		
		this.target = target;
		dockingControls.setDockingTarget(target);
	}
	
	public float getDockingRadius() {
		return FastMath.sqrt(dockingRadiusSquared);
	}

	public void setDockingRadius(float dockingRadius) {
		this.dockingRadiusSquared = dockingRadius * dockingRadius;
	}
	
	private void setState(DockingState state) {
		this.state = state;
		dockingControls.setState(state);
	}

}
