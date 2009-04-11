package net.java.dev.aircarrier.planes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jme.bounding.BoundingSphere;
import com.jme.bounding.OrientedBoundingBox;
import com.jme.input.InputHandler;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.SceneElement;
import com.jme.scene.Spatial;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.material.Material;

import net.java.dev.aircarrier.acobject.MovableAcobject;
import net.java.dev.aircarrier.acobject.MovableAcobjectSpatialDelegate;
import net.java.dev.aircarrier.bullets.Bullet;
import net.java.dev.aircarrier.bullets.BulletManager;
import net.java.dev.aircarrier.bullets.EventShootableNode;
import net.java.dev.aircarrier.bullets.HitEffectPool;
import net.java.dev.aircarrier.bullets.ReusableSource;
import net.java.dev.aircarrier.bullets.ShootableNode;
import net.java.dev.aircarrier.bullets.ShotListener;
import net.java.dev.aircarrier.controls.PlaneControls;
import net.java.dev.aircarrier.controls.StoringPlaneControls;
import net.java.dev.aircarrier.physics.PhysicsSpaceExtended;
import net.java.dev.aircarrier.physics.PlanePhysicsHandler;
import net.java.dev.aircarrier.scene.CarrierTerrainPage;
import net.java.dev.aircarrier.scene.Debris;
import net.java.dev.aircarrier.scene.DebrisController;
import net.java.dev.aircarrier.util.NamedValuesBean;

/**
 * Sets up a plane with model, PlaneAssembly, physics, bullet bounds, etc.
 * @author goki
 */
public class Plane implements NamedValuesBean, MovableAcobject {

	BulletManager bulletManager;
	List<ShootableNode> shootNodes;
	CarrierTerrainPage terrain;
	PlaneModel planeModel;
	HitEffectPool hitEffectPool;
	ReusableSource<Bullet> bulletSource;
	PlaneNode planeNode;
	Node worldNode;
	PlaneAssembly planeAssembly;
	PlaneController planeController;
	PhysicsSpaceExtended physicsSpace;
	PlanePhysicsHandler physicsHandler;
	InputHandler input;
	DynamicPhysicsNode planePhysicsNode;
	
	List<DebrisController> debrisControllers;
	
	List<String> valueNames;
	
	float heading;
	
	Vector3f horizontalHeading = new Vector3f();

	MovableAcobjectSpatialDelegate movableDelegate;
	
	public Plane(
			Node worldNode,
			PhysicsSpaceExtended physicsSpace,
			InputHandler input,
			List<ShootableNode> shootNodes, 
			CarrierTerrainPage terrain, 
			PlaneModel planeModel,
			HitEffectPool hitEffectPool,
			ReusableSource<Bullet> bulletSource) throws IOException {
		
		this.worldNode = worldNode;
		this.physicsSpace = physicsSpace;
		this.input = input;
		this.shootNodes = shootNodes;
		this.terrain = terrain;
		this.planeModel = planeModel;
		this.hitEffectPool = hitEffectPool;
		this.bulletSource = bulletSource;
				
		//Make bullet manager
		bulletManager = new BulletManager();						
		bulletManager.setShootNodes(shootNodes);
		bulletManager.setTerrain(terrain);
		bulletManager.setHitEffectsSource(hitEffectPool);
		
		//Make plane node
		planeNode = new PlaneNode("Plane", planeModel, worldNode, bulletSource, bulletManager);

		//Make plane assembly
		planeAssembly = new PlaneAssembly("Plane Assembly", planeNode);
		
		movableDelegate = new MovableAcobjectSpatialDelegate(planeAssembly.getBase(), "plane", "plane", planeNode.getAvoidanceNode().getRadius());
		
		//Make plane controller - initially just static controls, we expect these to
		//be replaced
		planeController = new PlaneController(this, new StoringPlaneControls(0));

		//Make physics node to represent plane for non-bullet collision
		planePhysicsNode = makePhysicsNode(planeModel.getPhysicsBounds());
		
        //Line the physics node up with the plane assembly
        planePhysicsNode.getLocalTranslation().set(planeAssembly.getBase().getWorldTranslation());

        //Set up plane physics to be unnaffected by gravity and slippery
        planePhysicsNode.setAffectedByGravity(false);
		physicsSpace.setDefaultMaterial(Material.ICE);

		//Don't show the plane physics object
		planePhysicsNode.setCullMode(SceneElement.CULL_ALWAYS);

        //Make a new physics handler
        physicsHandler = 
        	new PlanePhysicsHandler(
        			input, 
        			physicsSpace, 
        			planePhysicsNode,
        			planeAssembly,
        			0.2f);

                
        EventShootableNode shootableNode = new EventShootableNode("Shootable node");

        //Make shootable nodes
        List<Node> bulletBounds = planeNode.getBulletBounds();
        for (Node b : bulletBounds) {
            b.setModelBound(new BoundingSphere());
            b.updateModelBound();
    		shootableNode.attachChild(b);
    		b.setCullMode(Spatial.CULL_ALWAYS);
        }

        //Attach the shootable node itself to the plane
        planeAssembly.getPlane().attachChild(shootableNode);

        //We only need to add the shootable node - pick will pick on its individual children
		shootNodes.add(shootableNode);
		
		//We don't want to shoot ourself
		bulletManager.setIgnoreShootNode(shootableNode);
		
		
		//Listen for shots
		shootableNode.addListener(new ShotListener() {
			public void shot(Bullet b, ShootableNode n) {
				planeNode.damage(b.getDamage());
				if (planeNode.getHealth() < 0) {
					respawn();
				}
			}
		});

        //Make debris for wreckage, then hold onto their controllers
        debrisControllers = new ArrayList<DebrisController>();
        for (Node w : planeModel.getWreckage()) {
        	Debris d = new Debris();
        	worldNode.attachChild(d);
        	d.getDebrisNode().attachChild(w);
        	DebrisController dc = new DebrisController(d);
        	dc.setShootNodes(shootNodes);
        	dc.setIgnoreShootNode(shootableNode);
        	dc.setTerrain(terrain);
        	
        	d.addController(dc);
        	
        	debrisControllers.add(dc);
        }     
		
        //Make list of value names
        valueNames = new ArrayList<String>();
        valueNames.add("speed");
        valueNames.add("health");
        
	}
	
	void respawn() {
		//Switch physics nodes back on, realign to starting positions on plane, set visible
		for (int i = 0; i < debrisControllers.size(); i++) {
			DebrisController dc = debrisControllers.get(i);
			
			//Set debris active - resets and restarts physics and particles
			Node startPosition = planeModel.getWreckageStartPositions().get(i);
			dc.respawn(startPosition, getVelocity());
		}
		
		//Set plane to new position and respawn
		setPosition(new Vector3f(50 * FastMath.rand.nextFloat() * 3, 550, 50 * FastMath.rand.nextFloat() * 3));
		//setPosition(new Vector3f(0, 500 + 50 * FastMath.rand.nextFloat() * 3, 0));
		System.out.println("DESTROYED!");
		
		planeNode.respawn();		
	}
	
	/**
	 * Make a dynamic physics node representing the specified spatial,
	 * using OBB bounds and computed mass, and attach that physics
	 * node to the worldNode
	 * @param bounds
	 * 		The spatial to attach to physics node, and to use to calculate
	 * 		OBB bounds for physics
	 * @return
	 * 		The dynamic physics node
	 */
	public DynamicPhysicsNode makePhysicsNode(Spatial bounds) {

		//Make physics node to represent plane for non-bullet collision
        DynamicPhysicsNode physicsNode = physicsSpace.createDynamicNode();
		bounds.setModelBound(new OrientedBoundingBox());
		bounds.updateModelBound();
		physicsNode.attachChild(bounds);
		physicsNode.generatePhysicsGeometry();
		
        worldNode.attachChild( physicsNode );
        physicsNode.computeMass();
		
        return physicsNode;
	}
	
	public void setPosition(Vector3f to) {
		planeAssembly.getBase().getLocalTranslation().set(to);
		//Realign physics node for sudden jump
        planePhysicsNode.getLocalTranslation().set(planeAssembly.getBase().getWorldTranslation());
        planeAssembly.getBase().updateWorldData(0);
	}
	
	public void update(float time) {
		
		planeController.getControls().update(time);
		planeController.update(time);
		planeNode.update(time);
		
		//Calculate heading
		
		//Get horizontal heading
		planeAssembly.getBase().getLocalRotation().getRotationColumn(2, horizontalHeading);
		horizontalHeading.setY(0);
		if (horizontalHeading.lengthSquared() < 0.01) return;
		horizontalHeading.normalizeLocal();

		//FIXME is there a faster way?
		heading = -FastMath.atan2(horizontalHeading.getX(), horizontalHeading.getZ());
		
	}
	
	public void setControls(PlaneControls controls) {
		planeController.setControls(controls);
	}
	
	public PlaneControls getControls() {
		return planeController.getControls();
	}

	public Node getCameraTarget() {
		return planeAssembly.getCameraTarget();
	}

	public PlaneController getPlaneController() {
		return planeController;
	}
	
	public float getHealth() {
		return planeNode.getHealth();
	}
	
	public float getCurrentSpeed() {
		return planeController.getCurrentSpeed();
	}
	
	public float getHeading() {
		return heading;
	}

	public PlaneNode getPlaneNode() {
		return planeNode;
	}
	
	public float getNamedValue(String name) {
		if (name.equals("speed")) return getCurrentSpeed();
		if (name.equals("health")) return getHealth();
		if (name.equals("heading")) return getHeading();
		return 0;
	}

	public List<String> getValueNames() {
		return valueNames;
	}

	public PlaneAssembly getPlaneAssembly() {
		return planeAssembly;
	}

	public Node getBase() {
		return planeAssembly.getBase();
	}
	
	public String getName() {
		return movableDelegate.getName();
	}

	public Vector3f getPosition() {
		return movableDelegate.getPosition();
	}

	public float getRadius() {
		return movableDelegate.getRadius();
	}

	public Quaternion getRotation() {
		return movableDelegate.getRotation();
	}

	public Vector3f getVelocity() {
		return movableDelegate.getVelocity();
	}

	public String getVisibleName() {
		return movableDelegate.getVisibleName();
	}

	public void moveGlobal(int axis, float distance) {
		movableDelegate.moveGlobal(axis, distance);
	}

	public void moveGlobal(Vector3f translation) {
		movableDelegate.moveGlobal(translation);
	}

	public void moveLocal(int axis, float distance) {
		movableDelegate.moveLocal(axis, distance);
	}

	public void moveLocal(Vector3f translation) {
		movableDelegate.moveLocal(translation);
	}

	public void rotateLocal(int axis, float angle) {
		movableDelegate.rotateLocal(axis, angle);
	}

	public void setRotation(Quaternion rotation) {
		movableDelegate.setRotation(rotation);
	}
	
}
