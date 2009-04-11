package net.java.dev.aircarrier.planes;

import java.io.IOException;
import java.util.List;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.acobject.TargettingAcobject;
import net.java.dev.aircarrier.ai.TargettingControls;
import net.java.dev.aircarrier.bullets.Bullet;
import net.java.dev.aircarrier.bullets.HitEffectPool;
import net.java.dev.aircarrier.bullets.ReusableSource;
import net.java.dev.aircarrier.bullets.ShootableNode;
import net.java.dev.aircarrier.physics.PhysicsSpaceExtended;
import net.java.dev.aircarrier.scene.CarrierTerrainPage;

import com.jme.input.InputHandler;
import com.jme.scene.Node;

public class TargettingPlane extends Plane implements TargettingAcobject {

	TargettingControls aiControls;
	
	public TargettingPlane(Node worldNode, PhysicsSpaceExtended physicsSpace, InputHandler input, List<ShootableNode> shootNodes, CarrierTerrainPage terrain, PlaneModel planeModel, HitEffectPool hitEffectPool, ReusableSource<Bullet> bulletSource) throws IOException {
		super(worldNode, physicsSpace, input, shootNodes, terrain, planeModel,
				hitEffectPool, bulletSource);
	}

	public TargettingControls getTargettingControls() {
		return aiControls;
	}

	public void setTargettingControls(TargettingControls aiControls) {
		this.aiControls = aiControls;
		setControls(aiControls);
	}

	public Acobject getTarget() {
		if (aiControls == null) {
			return null;
		} else {
			return aiControls.getTarget();
		}
	}

	public void setTarget(Acobject target) {
		if (aiControls != null) {
			aiControls.setTarget(target);
		}
	}
	
	void respawn() {
		setTarget(null);
		super.respawn();
	}
}
