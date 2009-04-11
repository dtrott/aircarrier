package net.java.dev.aircarrier.bullets;

/**
 * ShootableNode that does nothing when shot
 * @author shingoki
 */
public class NullShootableNode extends ShootableNode {
	private static final long serialVersionUID = 7291647501921207379L;
	
	public NullShootableNode() {
		super();
	}

	public NullShootableNode(String name) {
		super(name);
	}

	@Override
	public void shoot(Bullet b) {
	}

}
