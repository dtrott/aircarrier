package net.java.dev.aircarrier.hud;

import net.java.dev.aircarrier.acobject.Acobject;

import com.jme.math.Vector3f;

/**
 * A PingNode that just tracks another Node
 * @author goki
 */
public class AcobjectPingNode extends PingNode {
	private static final long serialVersionUID = -7493907341725680566L;
	
	Acobject target;
	
	/**
	 * Create an instance
	 * @param target
	 * 		The target whose position is pinged
	 */
	public AcobjectPingNode(Acobject target) {
		this.target = target;
	}

	/**
	 * Create an instance
	 * @param target
	 * 		The target whose position is pinged
	 * @param name
	 * 		The name of this node (the PingNode)
	 */
	public AcobjectPingNode(Acobject target, String name) {
		super(name);
		this.target = target;
	}

	/**
	 * @return
	 * 		The target whose position is pinged
	 */
	public Acobject getTarget() {
		return target;
	}

	/**
	 * @param target
	 * 		A new target whose position will be pinged
	 */
	public void setTarget(Acobject target) {
		this.target = target;
	}

	/**
	 * Get the position of the target. DON'T change this vector, it
	 * is from target.getWorldTranslation()
	 */
	@Override
	public Vector3f getTargetPosition() {
		return target.getPosition();
	}

}
