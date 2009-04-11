package net.java.dev.aircarrier.bullets;

import java.util.ArrayList;
import java.util.List;

/**
 * ShootableNode that fires an event to listeners when shot
 * @author shingoki
 */
public class EventShootableNode extends ShootableNode {
	private static final long serialVersionUID = 7291647501921207379L;
	
	List<ShotListener> listeners = new ArrayList<ShotListener>();
	
	public EventShootableNode() {
		super();
	}

	public EventShootableNode(String name) {
		super(name);
	}

	public void shoot(Bullet b) {
		for (ShotListener listener : listeners) {
			listener.shot(b, this);
		}
	}
	
	public void addListener(ShotListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ShotListener listener) {
		listeners.remove(listener);
	}

}
