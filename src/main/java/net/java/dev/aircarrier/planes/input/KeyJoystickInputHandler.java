/*
 *  $Id: KeyJoystickInputHandler.java,v 1.4 2006/12/23 22:29:23 shingoki Exp $
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

package net.java.dev.aircarrier.planes.input;

import net.java.dev.aircarrier.input.VirtualJoystick;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;

/**
 * <code>KeyJoystickInputHandler</code> monitors a keyboard 
 * and passes the input to a VirtualJoystick 
 *  
 * @author shingoki
 */
public class KeyJoystickInputHandler extends InputHandler {

	VirtualJoystick joystick;

    /**
     * Create an input
     * 
     * @param joystick
     *            the joystick to move
     */
    public KeyJoystickInputHandler(VirtualJoystick joystick) {
        setKeyBindings();
        setActions(joystick, 5f);
        this.joystick = joystick;
    }

    /**
     *
     * <code>setKeyBindings</code> binds the keys to use for the actions.
     */
    private void setKeyBindings() {
        KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();

        /*
        keyboard.set("throttleUp", KeyInput.KEY_R);
        keyboard.set("throttleDown", KeyInput.KEY_F);
        keyboard.set("lookUp", KeyInput.KEY_W);
        keyboard.set("lookDown", KeyInput.KEY_S);
        keyboard.set("turnRight", KeyInput.KEY_D);
        keyboard.set("turnLeft", KeyInput.KEY_A);
        keyboard.set("rudderRight", KeyInput.KEY_Q);
        keyboard.set("rudderLeft", KeyInput.KEY_E);
		*/
        
        keyboard.set("throttleUp", KeyInput.KEY_W);
        keyboard.set("throttleDown", KeyInput.KEY_S);
        keyboard.set("lookUp", KeyInput.KEY_I);
        keyboard.set("lookDown", KeyInput.KEY_K);
        keyboard.set("turnRight", KeyInput.KEY_L);
        keyboard.set("turnLeft", KeyInput.KEY_J);
        keyboard.set("rudderRight", KeyInput.KEY_A);
        keyboard.set("rudderLeft", KeyInput.KEY_D);
}

    /**
     *
     * <code>setActions</code> sets the keyboard actions with the corresponding
     * key command.
     * @param controller 
     * 		the controller to use
     * @param keySpeed 
     * 		action speed for key actions (rate of change of control axis, which goes from -1 to 1)
     */
    private void setActions(VirtualJoystick joystick, float keySpeed) {

    	addAction( new VirtualJoystickAction( joystick, 1, keySpeed ), 	"lookUp", 		true );
    	addAction( new VirtualJoystickAction( joystick, 1, -keySpeed ), "lookDown", 	true );
    	addAction( new VirtualJoystickAction( joystick, 0, keySpeed ), 	"turnRight", 	true );
    	addAction( new VirtualJoystickAction( joystick, 0, -keySpeed ), "turnLeft", 	true );
    	addAction( new VirtualJoystickAction( joystick, 4, -keySpeed ), "throttleUp", 	true );
    	addAction( new VirtualJoystickAction( joystick, 4, keySpeed ),  "throttleDown", true );
    	addAction( new VirtualJoystickAction( joystick, 3, keySpeed ), 	"rudderLeft", 	true );
    	addAction( new VirtualJoystickAction( joystick, 3, -keySpeed ), "rudderRight", 	true );

    }

	@Override
	public void update(float time) {
		super.update(time);
		
		joystick.setButtonPressed(0, KeyInput.get().isKeyDown(KeyInput.KEY_J));
		joystick.setButtonPressed(1, KeyInput.get().isKeyDown(KeyInput.KEY_K));
		joystick.setButtonPressed(2, KeyInput.get().isKeyDown(KeyInput.KEY_L));
	}


}