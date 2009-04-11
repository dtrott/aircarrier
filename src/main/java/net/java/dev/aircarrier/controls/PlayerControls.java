package net.java.dev.aircarrier.controls;

import com.jme.input.joystick.Joystick;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.ai.GroupCollisionAvoidanceSteerer;
import net.java.dev.aircarrier.ai.RightingSteerer;
import net.java.dev.aircarrier.input.VirtualJoystick;
import net.java.dev.aircarrier.planes.input.JoystickPlaneInput;
import net.java.dev.aircarrier.planes.input.KeyJoystickInputHandler;
import net.java.dev.aircarrier.planes.input.MouseJoystickInputHandler;

/**
 * Controls consisting of various different input devices connected
 * to a tree of sub controls
 * 
 * @author goki
 */
public class PlayerControls implements PlaneControls {

	//Input straight from joystick
	JoystickPlaneInput joystickPlaneInput;
	
	//Input from virtual joystick driven by mouse
	VirtualJoystick mouseJoy;
	MouseJoystickInputHandler mouseInput;
	JoystickPlaneInput mouseJoystickPlaneInput;
	
	//Input from virtual joystick driven by keys
	VirtualJoystick keyJoy;
	KeyJoystickInputHandler keyInput;
	JoystickPlaneInput keyJoystickPlaneInput;
	
	//Picks the maximum input from different submissions (from key/mouse/joystick)
	MaximisingPlaneControls maximising;
	
	//Stores the current controls
	StoringPlaneControls storing;
	
	//Rights plane when necessary
	RightingSteerer righting;
	
	//Filters righting controls
	RollFilterControls filtering;
	
	//This is just here for AIControls steerers to use to avoid us
	GroupCollisionAvoidanceSteerer groupAvoidance;

	public PlayerControls(Acobject object, Joystick joystick, int gunCount) {
		
		storing = new StoringPlaneControls(gunCount);
		
		maximising = new MaximisingPlaneControls(storing);
		
		if (joystick != null) {
			joystickPlaneInput = new JoystickPlaneInput(maximising, joystick);
		} else {
			joystickPlaneInput = new JoystickPlaneInput(maximising, new VirtualJoystick(5, 8));
		}

		mouseJoy = new VirtualJoystick(5, 8);
		mouseJoy.setDecayRate(0.5f);
		mouseJoy.setAlwaysRecentered(true);
		mouseInput = new MouseJoystickInputHandler(mouseJoy, 0.005f);
		mouseJoystickPlaneInput = new JoystickPlaneInput(maximising, mouseJoy);
		mouseJoystickPlaneInput.setPrimaryFireButton(0);
		mouseJoystickPlaneInput.setSecondaryFireButton(1);
		//Third mouse button fires both guns - useful in general, and
		//specifically avoids the situation where niehter gun fires when
		//both buttons are pressed simultaneously on a linux system which
		//is set to emulate middle mouse button.
		mouseInput.getMouseLook().setThirdButtonBoth(true);
		
		keyJoy = new VirtualJoystick(5, 8);						
		keyInput = new KeyJoystickInputHandler(keyJoy);
		keyJoystickPlaneInput = new JoystickPlaneInput(maximising, keyJoy);
		keyJoystickPlaneInput.setPrimaryFireButton(0);
		keyJoystickPlaneInput.setSecondaryFireButton(1);

		righting = new RightingSteerer(object, 2);
		
		filtering = new RollFilterControls(righting, storing);
		
		//Avoid collisions with steering multiplier 100 (very abrupt steering),
		//starting avoidance 4 seconds from collision, and applying full
		//steering 2 seconds from collision.
		groupAvoidance = new GroupCollisionAvoidanceSteerer(
				//assemblyBase, selfSphere, 16, 2, 4, 0);
				object, 16, 2, 4, 0);

	}
	
	public void update(float time) {

		//Update mouse
		mouseInput.update(time);				//Get new input, push into virtual joystick
		mouseJoy.update(time);					//Update virtual joystick (e.g. decay input)
		mouseJoystickPlaneInput.update(time);	//Update controls based on joystick
		
		//update keys
		keyInput.update(time);					//Get new input, push into virtual joystick
		keyJoy.update(time);					//Update virtual joystick (e.g. decay input)
		keyJoystickPlaneInput.update(time);		//Update controls based on joystick

		//Real joystick to plane controller
		joystickPlaneInput.update(time);		//Update controls based on joystick

		//Make maximising pass biggest controls on to storing
		maximising.update(time);

		//Update storing (doesn't do anything, but might as well)
		storing.update(time);
		
		//Update righting
		righting.update(time);
		
		//We now have the main controls updated, and righting updated,
		//so we just update filter to choose appropriately
		filtering.update(time);
		
		groupAvoidance.update(time);
	}

	/**
	 * @return
	 * 		Our group collision avoidance steerer. The main use of this is to
	 * 		make a list of steerers that should avoid each other, then set
	 * 		this list as the "steerers" property of each steerer.
	 * 		The steerers are only really used by AIControls, so there is no need
	 * 		to set the steerers list for this steerer - it doesn't really need
	 * 		to avoid anything, just be avoided itself
	 */
	public GroupCollisionAvoidanceSteerer getGroupAvoidance() {
		return groupAvoidance;
	}	
	
	//DELEGATION ########################################
	public void clearFiring() {
		filtering.clearFiring();
	}
	public float getAxis(int axis) {
		return filtering.getAxis(axis);
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
	
}
