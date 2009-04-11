package net.java.dev.aircarrier.scene.actree;

import com.jme.math.Vector3f;

public class PrintingCollisionReceiver implements CollisionReceiver {

	@Override
	public boolean acceptCollision(float elapsedTime, Vector3f position,
			int collisionAxis, Vector3f collisionCenter) {
		System.out.println("Collision at " + elapsedTime + "s, box at " + position + ", collision axis " + collisionAxis + ", center " + collisionCenter);
		return false;
	}

}
