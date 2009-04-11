package net.java.dev.aircarrier.scene;

import com.jme.scene.Node;
import com.jmex.effects.particles.ParticleMesh;

public class Debris extends Node {
	private static final long serialVersionUID = -7898481930160055344L;

	ParticleMesh smoker;
	//ParticleMesh flamer;
	ParticleMesh exploder;

	Node debrisNode;

	public Debris() {
		super();
    	smoker = SmokerFactory.makeSmoker();
    	exploder = SmokerFactory.makeExploder();
    	//flamer = SmokerFactory.makeFlamer();
    	attachChild(smoker);
    	attachChild(exploder);
    	debrisNode = new Node("Debris");
    	attachChild(debrisNode);
    	setActive(false);
	}

	//public ParticleMesh getFlamer() {
	//	return flamer;
	//}

	//public ParticleMesh getSmoker() {
	//	return smoker;
	//}

	public void setActive(boolean active) {
		if (active) {
			//Show debris and start particles
            debrisNode.setCullHint(CullHint.Inherit);
			smoker.setReleaseRate(SmokerFactory.SMOKE_RATE);
			exploder.forceRespawn();
			exploder.setCullHint(CullHint.Inherit);
			//flamer.setReleaseRate(SmokerFactory.FLAME_RATE);
		} else {
			//Hide debris and stop particles
			debrisNode.setCullHint(CullHint.Always);
			smoker.setReleaseRate(0);
			//flamer.setReleaseRate(3);
			exploder.setCullHint(CullHint.Always);
		}
	}

	public Node getDebrisNode() {
		return debrisNode;
	}

}
