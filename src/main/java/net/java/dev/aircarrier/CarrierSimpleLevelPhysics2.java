/*
 *  $Id: CarrierSimpleLevelPhysics2.java,v 1.2 2006/07/21 23:59:32 shingoki Exp $
 *
 * 	Copyright (c) 2005-2006 shingoki
 *
 *  This file is part of AirCarrier, see http://aircarrier.dev.java.net/
 *
 *    AirCarrier is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.

 *    AirCarrier is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.

 *    You should have received a copy of the GNU General Public License
 *    along with AirCarrier; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */


package net.java.dev.aircarrier;

//import java.io.IOException;
//import java.util.logging.Level;
//
//import net.java.dev.aircarrier.bullets.BulletFactory;
//import net.java.dev.aircarrier.hud.Crosshair;
//import net.java.dev.aircarrier.hud.HudNode;
//import net.java.dev.aircarrier.input.VirtualJoystick;
//import net.java.dev.aircarrier.planes.DevilfishModel;
//import net.java.dev.aircarrier.planes.Plane;
//import net.java.dev.aircarrier.planes.PlaneAssembly;
//import net.java.dev.aircarrier.planes.PlaneController;
//import net.java.dev.aircarrier.planes.input.JoystickPlaneInput;
//import net.java.dev.aircarrier.planes.input.KeyJoystickInputHandler;
//import net.java.dev.aircarrier.planes.input.MouseJoystickInputHandler;
//import net.java.dev.aircarrier.scene.CarrierSkyBox;
//import net.java.dev.aircarrier.scene.CarrierTerrainPage;
//
//import com.jme.input.InputHandler;
//import com.jme.input.action.InputAction;
//import com.jme.input.action.InputActionEvent;
//import com.jme.input.joystick.Joystick;
//import com.jme.input.joystick.JoystickInput;
//import com.jme.input.util.SyntheticButton;
//import com.jme.math.Vector3f;
//import com.jme.renderer.ColorRGBA;
//import com.jme.renderer.Renderer;
//import com.jme.scene.CameraNode;
//import com.jme.scene.Node;
//import com.jme.scene.Skybox;
//import com.jme.scene.state.CullState;
//import com.jme.scene.state.FogState;
//import com.jme.util.LoggingSystem;
//import com.jmex.physics.DynamicPhysicsNode;
//import com.jmex.physics.PhysicsDebugger;
//import com.jmex.physics.PhysicsSpace;
//import com.jmex.physics.PhysicsUpdateCallback;
//import com.jmex.physics.StaticPhysicsNode;
//import com.jmex.physics.contact.ContactInfo;
//import com.jmex.physics.contact.MutableContactInfo;
//import com.jmex.physics.geometry.PhysicsBox;
//import com.jmex.physics.material.Material;

/**
 * <code>TestTerrainPage</code>
 * 
 * @author Mark Powell
 * @version $Id: CarrierSimpleLevelPhysics2.java,v 1.2 2006/07/21 23:59:32 shingoki Exp $
 */
public class CarrierSimpleLevelPhysics2 {//extends CarrierGame {
//
//	private float terrainHeightScale = 2f;// 1.5f;
//	
//	private float terrainHorizontalScale = 7f;
//
//	Skybox skybox;
//	
//	NodeTracker tracker;
//
//	JoystickInput joystickInput;
//
//	Joystick joystick;
//
//	JoystickPlaneInput joystickPlaneInput;
//
//	JoystickPlaneInput mouseJoystickPlaneInput;
//	JoystickPlaneInput keyJoystickPlaneInput;
//	
//	VirtualJoystick mouseJoy;
//	VirtualJoystick keyJoy;
//	
//	PlaneController controller;
//
//	HudNode hudNode;
//
//	Plane plane;
//
//	BulletFactory bulletFactory;
//	
//	MouseJoystickInputHandler mouseInput;
//	KeyJoystickInputHandler keyInput;
//	
//	private PhysicsSpace physicsSpace;
//
//	DynamicPhysicsNode dynamicBox;
//	
//	PlaneAssembly assembly;
//	
//	float clearTime = 10f;
//	float minClearTime = 0.4f;
//	
//	Vector3f tempV = new Vector3f();
//	
//	Vector3f zeroVector = new Vector3f(0,0,0);
//	
//	static {
//		JoystickInput.setProvider(JoystickInput.INPUT_LWJGL);
//	}
//
//	/**
//	 * Entry point for the test,
//	 * 
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		
//		LoggingSystem.getLoggingSystem().setLevel(Level.OFF);
//		
//		CarrierSimpleLevelPhysics2 app = new CarrierSimpleLevelPhysics2();
//		app.setDialogBehaviour(ALWAYS_SHOW_PROPS_DIALOG);
//		app.start();
//	}
//
//	
//	@Override
//	protected void simpleUpdate() {
//		super.simpleUpdate();
//
//		//mouse and keyboard to virtual joystick, then that to the plane controller
//		mouseInput.update(tpf);
//		mouseJoy.update(tpf);
//		mouseJoystickPlaneInput.update(tpf);
//		
//		keyInput.update(tpf);
//		keyJoy.update(tpf);
//		keyJoystickPlaneInput.update(tpf);
//
//		//Real joystick to plane controller
//		joystickPlaneInput.update(tpf);
//		
//		//plane controller to plane
//		controller.update(tpf);
//		
//		//plane itself
//		plane.update(tpf);
//
//		//Camer tracks plane
//		tracker.update(tpf);
//
//		//Skybox follows camera
//		skybox.setLocalTranslation(cam.getLocation());
//
//		//UpdatePhysics
//		updatePhysics(tpf);
//		
//		// Because we are changing the scene (moving the skybox and player) we
//		// need to update
//		// the graph.
//		rootNode.updateGeometricState(tpf, true);
//
//	}
//	
//	void updatePhysics(float tpf) {
//		
//		//Update physics
//		physicsSpace.update(tpf);
//		
//		//Increase time that we have been clear of contact with plane
//		//(This is decreased to zero on contacts)
//		clearTime += tpf;
//		
//		if (clearTime < minClearTime) {
//			assembly.getBase().getLocalTranslation().set(dynamicBox.getLocalTranslation());
//
//			/*
//			//Work out the rotation required to get base facing in same direction as velocity of the
//			//dynamic box, and apply it
//			tempV = assembly.getBase().getWorldRotation().getRotationColumn(2);
//			
//	        // get angle between vectors
//	        float angle = tempV.angleBetween(dynamicBox.getLinearVelocity());
//
//	        //figure out rotation axis by taking cross product
//	        Vector3f rotAxis = tempV.crossLocal(dynamicBox.getLinearVelocity());
//
//	        // Build a rotation quat and apply current local rotation.
//	        Quaternion q = new Quaternion();
//	        q.fromAngleAxis(angle, rotAxis);
//	        q.mult(assembly.getBase().getLocalRotation(), assembly.getBase().getLocalRotation());
//	        */
//			
//		}
//		
//	}
//	
//	void buildWorld() {
//		physicsSpace = PhysicsSpace.create();
//		physicsSpace.setDirectionalGravity(new Vector3f());
//		
//		//Create material for ideal bounce collisions
//		Material idealBounce = new Material("idealBounce");
//		idealBounce.setDensity(5f);
//		
//		//Create a contact info for ideal bounce, and
//		//add it for any other material touching this one
//		//(hence "null" in put method)
//		//We will only use this material, since all contacts
//		//should bounce away (or explode on contact for bullets/missiles)
//		MutableContactInfo info = new MutableContactInfo();
//		info.setBounce(0.95f);
//		info.setMinimumBounceVelocity(0f);
//		info.setMu(0f);
//		info.setMuOrthogonal(0f);
//		idealBounce.putContactHandlingDetails(null, info);
//		
//		//Set ideal bounce material as default
//		physicsSpace.setDefaultMaterial(idealBounce);
//				
//		physicsSpace.addToUpdateCallbacks(new PhysicsUpdateCallback() {
//		
//			@Override
//			public void beforeStep(PhysicsSpace space, float time) {
//				dynamicBox.setAngularVelocity(zeroVector);
//				
//				//box.getLocalRotation().set(plane.getWorldRotation());
//				//dynamicBox.syncWithGraphical();
//				
//				plane.updateWorldVectors();
//				plane.updateGeometricState(0, true);
//		        dynamicBox.getLocalRotation().set(plane.getWorldRotation());
//		        
//		        dynamicBox.updateGeometricState(0, true);
//		        
//				if (clearTime > minClearTime) {
//					
//					//Work out the velocity required to make the
//					//physics box reach the position of the plane box,
//					//if it is update immediately
//					tempV = 
//						plane.getWorldTranslation().subtract(
//								dynamicBox.getWorldTranslation(), tempV).multLocal(100f);
//					
//					//Update plane bounding box from plane controller
//					dynamicBox.setLinearVelocity(tempV);
//					//box.getLocalTranslation().set(plane.getWorldTranslation());
//					//System.out.println(dynamicBox.getLinearVelocity());
//				}
//			}
//		
//			@Override
//			public void afterStep(PhysicsSpace space, float time) {
//			}
//		
//		});
//	}
//	
//	protected void simpleInitGame() {
//
//		buildWorld();
//		
//		//initSpheres();
//		
//		joystickInput = JoystickInput.get();
//
//		for (int i = 0; i < joystickInput.getJoystickCount(); i++) {
//			System.out.println(joystickInput.getJoystick(i).getName());
//		}
//
//		if (joystickInput.getJoystickCount() > 0) {
//			joystick = joystickInput.getJoystick(0);
//		} else {
//			joystick = null;
//		}
//
//		rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
//		fpsNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
//
//		ColorRGBA skyColor = new ColorRGBA(0.7f, 0.8f, 1f, 0.5f);
//
//		display.setTitle("Terrain Test");
//		display.getRenderer().setBackgroundColor(skyColor);
//
//		CullState cs = display.getRenderer().createCullState();
//		cs.setCullMode(CullState.CS_BACK);
//		cs.setEnabled(true);
//		rootNode.setRenderState(cs);
//
//		CarrierTerrainPage terrain = CarrierTerrainPage.create("Terrain",
//				//"resources/higher_height_small.jpg",
//				// "resources/canyonWaterHeight.jpg",
//				//"resources/islands_height_orig.jpg",
//				//"resources/nowaterHeight.jpg",
//				"resources/nowaterCroppedHeight.jpg",
//
//				//"resources/higher_plain_lm_small.jpg",
//				// "resources/canyonWaterTexture.jpg",
//				//"resources/islands_LM_small.jpg",
//				//"resources/nowaterTexture.jpg",
//				"resources/nowaterCroppedTexture.jpg",
//
//				"jmetest/data/texture/Detail.jpg", 257,
//				terrainHorizontalScale,
//				terrainHeightScale);
//
//		
//		worldNode.attachChild(terrain);
//
//		terrain.addPhysicsToNode(physicsSpace, rootNode);
//
//        // first we will create the floor
//        // as the floor can't move we create a _static_ physics node
//        StaticPhysicsNode staticNode = physicsSpace.createStaticNode();
//
//        // attach the node to the root node to have it updated each frame
//        rootNode.attachChild( staticNode );
//
//        // now we create a collision geometry for the floor - a box
//        PhysicsBox floorBox = staticNode.createBox( "floor" );
//
//        // the box is already attached to our static node
//        // it currently has height, width and depth of 1
//        // resize it to be 10x10 thin (0.5) floor
//        floorBox.getLocalScale().set( 100, 100f, 100 );
//        floorBox.getLocalTranslation().set(250, 250, 250);
//        
//
//		
//		FogState fs = display.getRenderer().createFogState();
//		fs.setDensity(0.5f);
//		fs.setEnabled(true);
//		fs.setColor(skyColor);
//		fs.setEnd(1000);
//		fs.setStart(50);
//		fs.setDensityFunction(FogState.DF_LINEAR);
//		fs.setApplyFunction(FogState.AF_PER_VERTEX);
//		worldNode.setRenderState(fs);
//
//		skybox = new CarrierSkyBox("Sky", "resources/asky", ".jpg");
//		skyNode.attachChild(skybox);
//
//		try {
//
//			BulletFactory bulletFactory = new BulletFactory();
//			
//			DevilfishModel planeModel = new DevilfishModel();
//			//HammerheadModel planeModel = new HammerheadModel();
//			
//			plane = new Plane("Plane", planeModel.getModel(), worldNode, bulletFactory);
//			
//			assembly = new PlaneAssembly("DevilFish", plane);
//
//			litWorldNode.attachChild(assembly.getBase());
//			assembly.getBase().setLocalTranslation(new Vector3f(0, 250, 0));
//			assembly.getBase().updateWorldData(0);
//
//			//This will be our proxy for the plane
//	        dynamicBox = physicsSpace.createDynamicNode();
//	        // also attach it to the scene
//	        rootNode.attachChild( dynamicBox );
//	        // create another collision geometry, now for the plane proxy box
//	        dynamicBox.createBox( "dynamicBox" );
//	        // Scale to match the plane
//	        dynamicBox.getLocalScale().set(14f, 2f, 5f);
//	        // Move to be centered on the plane
//	        dynamicBox.getLocalTranslation().set(0, 250, 0);
//				        
//	        SyntheticButton collisionEventHandler = dynamicBox.getCollisionEventHandler();
//	        input.addAction( new InputAction() {
//	            public void performAction( InputActionEvent evt ) {
//	                ContactInfo info = (ContactInfo) evt.getTriggerData();
//	    			clearTime = 0;
//
//	                
//	                //if ( info.getGeometry1() != floorGeom && info.getGeometry2() != floorGeom ) {
//	                //    System.out.println( evt.getTriggerData() );
//	                //}
//	            	
//	            }
//	        }, collisionEventHandler.getDeviceName(), collisionEventHandler.getIndex(), InputHandler.AXIS_NONE, false );
//	        
//			controller = new PlaneController(assembly);
//
//			joystickPlaneInput = new JoystickPlaneInput(controller, joystick);
//
//			mouseJoy = new VirtualJoystick();
//			mouseJoy.setDecayRate(0.5f);
//			mouseJoy.setAlwaysRecentered(true);
//			mouseInput = new MouseJoystickInputHandler(mouseJoy, 0.005f);
//			mouseJoystickPlaneInput = new JoystickPlaneInput(controller, mouseJoy);
//			mouseJoystickPlaneInput.setPrimaryFireButton(0);
//			mouseJoystickPlaneInput.setSecondaryFireButton(1);
//
//			keyJoy = new VirtualJoystick();						
//			keyInput = new KeyJoystickInputHandler(keyJoy);
//			keyJoystickPlaneInput = new JoystickPlaneInput(controller, keyJoy);
//			keyJoystickPlaneInput.setPrimaryFireButton(0);
//			keyJoystickPlaneInput.setSecondaryFireButton(1);
//
//			
//			Node camNode = new CameraNode("Camera Node", cam);
//			camNode.setLocalTranslation(new Vector3f(assembly.getCameraTarget()
//					.getWorldTranslation()));
//			worldNode.attachChild(camNode);
//			camNode.updateWorldData(0);
//
//			tracker = new NodeTracker(assembly.getCameraTarget(), camNode, 100);
//
//			hudNode = new HudNode(display, "Plane HUD");
//			Crosshair crosshair = new Crosshair();
//			hudNode.attachChild(crosshair);
//			crosshair.setLocalTranslation(new Vector3f(display.getWidth() / 2,
//					display.getHeight() / 2, 0));
//			hudNode.updateRenderState();
//
//			rootNode.attachChild(hudNode);
//
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		rootNode.updateRenderState();
//		rootNode.updateWorldData(0);
//		terrain.updateRenderState();
//		terrain.updateWorldData(0);
//	}
//	
//	protected void simpleRender() {
//        PhysicsDebugger.drawPhysics( physicsSpace, display.getRenderer() );
//	}
//	
}
