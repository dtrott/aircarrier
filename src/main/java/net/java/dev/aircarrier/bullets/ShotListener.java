package net.java.dev.aircarrier.bullets;

public interface ShotListener {

	/**
	 * Called when a shootable node being listened to is shot
	 * @param b
	 * 		The bullet that shot the node
	 * @param n
	 * 		The node that got shot.
	 */
	public void shot(Bullet b, ShootableNode n);
	
}
