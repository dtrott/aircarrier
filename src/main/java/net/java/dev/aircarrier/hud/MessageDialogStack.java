package net.java.dev.aircarrier.hud;

import java.util.ArrayList;
import java.util.List;

import net.java.dev.aircarrier.util.FloatSpring;
import net.java.dev.aircarrier.util.VectorUtils;

import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.scene.Node;

/**
 * Node having a pair of MessageDialogs, which are
 * used to give the appearance of shuffling through 
 * a stack of messages.
 * 
 * @author goki
 */
public class MessageDialogStack extends Node {
	private static final long serialVersionUID = -3168006671582353572L;

	MessageDialog[] dialogs;
	
	//This stores the index of the target dialog - 
	//the one we are trying to display
	int targetDialogIndex = 0;
	
	//The messages we have received, in order
	List<String> messageStrings = new ArrayList<String>();

	//True when the dialogs are being switched round
	boolean switching = false;
	
	//The dialog index intended to be shown when the
	//current switch, if any, is complete
	int pendingSwitchIndex = -1;
	
	//The selected message index
	int selectedIndex = 0;
	
	//The spring that moves the dialogs on their switches,
	//by having target set to 0 or 1 to select the corresponding
	//dialog
	FloatSpring spring = new FloatSpring(300);
	
	//The positions for dialogs to be shown or hidden
	Vector3f showPosition;
	Vector3f hidePosition;
	
	public MessageDialogStack(
			MessageDialog dialog, 
			Vector3f showPosition, 
			Vector3f hidePosition) {
		
		this.showPosition = showPosition;
		this.hidePosition = hidePosition;
		
		dialogs = new MessageDialog[2];
		dialogs[0] = dialog;
		dialogs[1] = new MessageDialog(dialog);
		
		for (MessageDialog d : dialogs) {
			d.printMessage("");
			attachChild(d);
		}
		
		//initialise, we start with dialog 0 as the target dialog, being
		//shown. This requires spring and targetDialogIndex at 0
		spring.setVelocity(0);
		spring.setPosition(0);
		targetDialogIndex = 0;
	}
	
	public void addMessage(String message) {
		//When a message is added, switch to it
		messageStrings.add(message);
		setSelectedIndex(messageStrings.size() - 1);
	}
	
	public void setSelectedIndex(int index) {
		//We never switch instantly, just set the pending switch, this
		//will cause us to switch to the message index as soon as possible
		pendingSwitchIndex = index;
		selectedIndex = index;
	}
	
	public void previous() {
		if (selectedIndex > 0) setSelectedIndex(selectedIndex-1);
	}

	public void next() {
		if (selectedIndex < messageStrings.size() - 1) setSelectedIndex(selectedIndex+1);
	}
	
	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void update(float time) {
		
		//The basic system here is a "double buffer" where each buffer is a dialog.
		//When we are switching, we are waiting for the spring to haul the "old" dialog
		//offscreen, and the new one on screen. When this is complete, if we have a pending
		//switch we do it by changing the offscreen dialog to be the new message, then
		//start a new switch to make the offscreen dialog come onscreen
		
		//Update the spring, moving towards the target dialog index
		spring.update(targetDialogIndex, time);
		
		float pos = spring.getPosition();
		
		//Move each dialog. When spring is at 0, dialog 0 is shown, when it is at 1, dialog 1 is shown.
		VectorUtils.lerp(pos, showPosition, hidePosition, dialogs[0].getLocalTranslation());
		VectorUtils.lerp(pos, hidePosition, showPosition, dialogs[1].getLocalTranslation());
		
		//If we are switching, and spring is close enough to desired value, then we are finished
		if (switching) {
			if (FastMath.abs(pos - targetDialogIndex) < 0.05) {
				switching = false;
			}
		}

		//If we are not switching, deal with any pending switch
		if (!switching) {
			//If we have a pending switch
			if (pendingSwitchIndex >= 0) {
				
				//Nothing to do if index is invalid
				if (pendingSwitchIndex >= messageStrings.size()) {
					pendingSwitchIndex = -1;
					
				//valid index
				} else {
					//Offscreen dialog becomes the target dialog
					targetDialogIndex = 1 - targetDialogIndex;
					//Set up the new target dialog with the desired message
					dialogs[targetDialogIndex].printMessage(messageStrings.get(pendingSwitchIndex));
					//clear the pending switch
					pendingSwitchIndex = -1;
					//We are switching again
					switching = true;
				}
			}			
		}
				
	}
	
}
