package net.java.dev.aircarrier.controls;

import net.java.dev.aircarrier.acobject.MovableAcobject;
import net.java.dev.aircarrier.planes.PlaneSpecs;

import com.jme.scene.Controller;

/**
 * A controller which uses a set of SteeringControls and PlaneSpecs 
 * to rotate a spatial around its axes
 * @author shingoki
 */
public class PlaneSpecsSteeringController extends Controller {
	private static final long serialVersionUID = -2054614836408129440L;
	
	MovableAcobject s;
	SteeringControls controls;
	PlaneSpecs specs;
	
	/**
	 * Create a controller for a MovableAcobject
	 * @param s
	 * 		The MovableAcobject to control
	 * @param controls
	 * 		The controls that set desired rotation rates 
	 * @param specs
	 * 		The plane specifications, used for maximum rates of rotation about each axis 
	 */
	public PlaneSpecsSteeringController(MovableAcobject s, SteeringControls controls, PlaneSpecs specs) {
		super();
		this.s = s;
		this.controls = controls;
		this.specs = specs;
	}

	@Override
	public void update(float time) {
		//Make the actual rotations based on pending axis controls, rates and elapsed time
		for (int i = 0; i < 3; i++) {
			s.rotateLocal(i, controls.getAxis(i) * specs.getAxisRates()[i] * time);
		}
	}

}
