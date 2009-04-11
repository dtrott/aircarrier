package net.java.dev.aircarrier.physics;

import net.java.dev.aircarrier.input.action.NodeTranslator;
import net.java.dev.aircarrier.planes.PlaneAssembly;

import com.jme.input.InputHandler;
import com.jme.input.action.InputAction;
import com.jme.input.action.InputActionEvent;
import com.jme.input.util.SyntheticButton;
import com.jme.math.Vector3f;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.contact.ContactInfo;

public class PlanePhysicsHandler extends InputAction implements PreUpdateListener, PostUpdateListener  {
	
	PhysicsSpaceExtended physics;
	DynamicPhysicsNode physicsNode;
	PlaneAssembly planeAssembly;
	float clearTime;
	float minClearTime;
	float initialTime;
	
	Vector3f zeroAngularVelocity = new Vector3f();
	Vector3f tempV = new Vector3f();
	Vector3f tempForce = new Vector3f();
	Vector3f tempPhysicsSpeed = new Vector3f();
	
	/**
	 * Create a handler
	 * @param physics
	 * 		The physics space we are working in
	 * @param physicsNode
	 * 		The physics node we are using
	 * @param planeAssembly
	 * 		The plane to control
	 * @param minClearTime
	 * 		The minimum clear time before control is restored to plane after collision
	 */
	public PlanePhysicsHandler(
			InputHandler input,
			PhysicsSpaceExtended physics,
			DynamicPhysicsNode physicsNode, 
			PlaneAssembly planeAssembly, 
			float minClearTime) {
		super();
		this.physicsNode = physicsNode;
		this.planeAssembly = planeAssembly;
		this.minClearTime = minClearTime;
		this.clearTime = minClearTime + 1;
		
		physics.addPreUpdateListener(this);
		physics.addPostUpdateListener(this);
		
        SyntheticButton collisionEventHandler = physics.getCollisionEventHandler();
        input.addAction(this, collisionEventHandler.getDeviceName(), collisionEventHandler.getIndex(), InputHandler.AXIS_NONE, false );
	}

	public void preUpdate(float time, PhysicsSpaceExtended space) {

		//Zero the physics node's angular velocity
		zeroAngularVelocity.set(0,0,0);
		physicsNode.setAngularVelocity(zeroAngularVelocity);
		
		//box.getLocalRotation().set(plane.getWorldRotation());
		//dynamicBox.syncWithGraphical();
		
		//Update the plane, and then set the physics node to the same rotation

		
        //physicsNode.getPhysicalEntity().setEnabled( true );
        //dynamicBox.getPhysicalEntity().setPosition(plane.getWorldTranslation());
        physicsNode.setLocalRotation(planeAssembly.getPlane().getWorldRotation());

        
		if (clearTime > minClearTime || initialTime < 1) {
			//Work out the velocity required to make the
			//physics box reach the position of the plane box,
			//if it is updated by the expected number of physics updates
			
			//Work out the time passed in increments of 50th of a second
			int updates = (int)(time / (100f/2f));
			//There is no harm in always assuming 1 update, since if we don't
			//update it doesn't matter what velocity we set.
			if (updates < 1) updates = 1;
			
			//Work out velocity, multiplying by 100f/2f to convert to
			//physics velocity, and dividing by updates to account for this velocity
			//being applied for multiple physics updates
			tempV = 
				planeAssembly.getPlane().getWorldTranslation().subtract(
						physicsNode.getWorldTranslation(), tempV).multLocal((100f/2f) / ((float)updates));//100f/2f);
			//tempV.set(0,0,0);
			physicsNode.setLinearVelocity(tempV);
			
		}

	}

	public void postUpdate(float time, PhysicsSpaceExtended space) {

		initialTime += time;
		
		//Update time since collision
		clearTime += time;
		
		//If we are in a collision, then set the plane assembly position to the
		//physics node position (allowing the physics node to "control" the plane
		if (clearTime < minClearTime && initialTime > 1) {
			planeAssembly.getBase().getLocalTranslation().set(physicsNode.getLocalTranslation());

			//Apply damping force
			
			//Apply a force to regulate speed
			float acceleration = 1000f;
			NodeTranslator.makeTranslationVector(planeAssembly.getBase(), 2, acceleration, tempForce);
			physicsNode.addForce(tempForce);

			//Work out desired plane speed
			//NodeTranslator.makeTranslationVector(planeAssembly.getBase(), 2, 40, tempForce);
			
			//physicsNode.setLinearVelocity(tempForce);
			
			/*
			//Convert to physics speed
			tempForce.multLocal(100f/2f);
			
			//Work out difference from actual speed
			physicsNode.getLinearVelocity(tempPhysicsSpeed);
			tempForce.subtract(tempPhysicsSpeed);
			
			//Work out force required to correct in next frame
			tempForce.multLocal(physicsNode.getMass()/75f);
			
			//Apply
			physicsNode.set
			physicsNode.addForce(tempForce);
*/
			
			/*
			//Work out the rotation required to get base facing in same direction as velocity of the
			//dynamic box, and apply it
			tempV = assembly.getBase().getWorldRotation().getRotationColumn(2);
			
	        // get angle between vectors
	        float angle = tempV.angleBetween(dynamicBox.getLinearVelocity());

	        //figure out rotation axis by taking cross product
	        Vector3f rotAxis = tempV.crossLocal(dynamicBox.getLinearVelocity());

	        // Build a rotation quat and apply current local rotation.
	        Quaternion q = new Quaternion();
	        q.fromAngleAxis(angle, rotAxis);
	        q.mult(assembly.getBase().getLocalRotation(), assembly.getBase().getLocalRotation());
	        */
			
		}

	}

	public void performAction(InputActionEvent evt) {
        ContactInfo info = (ContactInfo) evt.getTriggerData();
        if ( info.getNode1() == physicsNode || info.getNode2() == physicsNode ) {
        	clearTime = 0;
        }
	}
	
}
