package net.java.dev.aircarrier.scene.actree;

import com.jme.math.Vector3f;

public class NextCollisionReceiver implements CollisionReceiver {

	Vector3f position = new Vector3f();
	Vector3f collisionCenter = new Vector3f();
	float elapsedTime;
	int collisionAxis;

	boolean collided = false;
	
	@Override
	public boolean acceptCollision(float elapsedTime, Vector3f position,
			int collisionAxis, Vector3f collisionCenter) {
		this.elapsedTime = elapsedTime;
		this.position.set(position);
		this.collisionAxis = collisionAxis;
		this.collisionCenter.set(collisionCenter);
		collided = true;
		return false;
	}

	public Vector3f getPosition() {
		return position;
	}
	public Vector3f getCollisionCenter() {
		return collisionCenter;
	}
	public float getElapsedTime() {
		return elapsedTime;
	}
	public int getCollisionAxis() {
		return collisionAxis;
	}
	public void reset() {
		collided = false;
	}
	public boolean collided() {
		return collided;
	}
	public void print() {
		if (collided) {
			System.out.println("Collision at " + elapsedTime + "s, box at " + position + ", collision axis " + collisionAxis + ", center " + collisionCenter);
		} else {
			System.out.println("No Collision");
		}
	}
}
