package net.java.dev.aircarrier.ai;

import net.java.dev.aircarrier.acobject.Acobject;

import com.jme.math.Vector3f;
import com.jme.scene.Node;

/**
 * This node will position itself at the predicted position of a target node, some time in the future
 * Must be attached to root node to work properly
 * @author goki
 */
public class PredictionNode extends Node {
	private static final long serialVersionUID = -8864273651002529351L;
	
	Acobject target;
	boolean newTarget = true;
	Vector3f lastPosition = new Vector3f();
	Vector3f movement = new Vector3f();
	float timeAhead = 1;
	
	/**
	 * @return
	 * 		The node to track, and whose movements we predict
	 */
	public Acobject getTarget() {
		return target;
	}

	/**
	 * Set new node to track - prediction will not be accurate until two
	 * updates have been made (we need to see the node move)
	 * @param target
	 * 		The node to track, and whose movements we predict
	 */
	public void setTarget(Acobject target) {
		this.target = target;
		newTarget = true;
	}

	/**
	 * @return
	 * 		We position ourself on each update, at the position we
	 * 		expect the target node to have this far in the future
	 */
	public float getTimeAhead() {
		return timeAhead;
	}

	/**
	 * Set amount of time to predict
	 * @param timeAhead
	 * 		We position ourself on each update, at the position we
	 * 		expect the target node to have this far in the future
	 */
	public void setTimeAhead(float timeAhead) {
		this.timeAhead = timeAhead;
	}

	/**
	 * Update the prediction node - move it to the predicted position of
	 * target node, timeAhead in the future
	 * @param time
	 * 		The time since last update (used to work out target velocity)
	 */
	public void update(float time) {
		
		//If we have no target, just move to origin
		if (target == null) {
			getLocalTranslation().set(0, 0, 0);
			return;
		}
		
		//On first update, store last position of target, and move ourselves
		//onto it.
		if (newTarget) {
			lastPosition.set(target.getPosition());
			getLocalTranslation().set(target.getPosition());
			newTarget = false;
			return;
		}
		
		//Work out velocity from last update
		movement.set(target.getPosition());
		movement.subtractLocal(lastPosition);
		movement.divideLocal(time);
		
		//Extrapolate to time ahead
		movement.multLocal(timeAhead);
		movement.addLocal(target.getPosition());
		
		//Work out our position from this
		getLocalTranslation().set(movement);
		
		//Store new last position
		lastPosition.set(target.getPosition());
	}
}
