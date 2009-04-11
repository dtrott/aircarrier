package net.java.dev.aircarrier.controls;

import net.java.dev.aircarrier.acobject.MovableAcobject;
import com.jme.scene.Controller;

/**
 * A controller which uses a set of SteeringControls to rotate a spatial around
 * its axes
 * @author shingoki
 */
public class SteeringController extends Controller {
	private static final long serialVersionUID = -2054614836408129440L;
	
	MovableAcobject s;
	float[] axisRates;
	SteeringControls controls;

	/**
	 * Create a controller for a MovableAcobject
	 * @param s
	 * 		The MovableAcobject to control
	 * @param controls
	 * 		The controls that set desired rotation rates 
	 * @param axisRates
	 * 		The maximum rates of rotation about each axis (indexed according to
	 * 		constants from SteeringControls, PITCH, YAW etc.)
	 */
	public SteeringController(MovableAcobject s, SteeringControls controls, float[] axisRates) {
		super();
		this.s = s;
		this.controls = controls;
		this.axisRates = axisRates;
	}

	@Override
	public void update(float time) {
		//Make the actual rotations based on pending axis controls, rates and elapsed time
		for (int i = 0; i < 3; i++) {
			s.rotateLocal(i, controls.getAxis(i) * axisRates[i] * time);
		}
	}

	public SteeringControls getControls() {
		return controls;
	}

	public void setControls(SteeringControls controls) {
		this.controls = controls;
	}

}
