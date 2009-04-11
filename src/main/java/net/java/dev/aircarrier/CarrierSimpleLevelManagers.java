/*
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

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jmetest.audio.TestJmexAudio;
import jmetest.renderer.ShadowTweaker;

import net.java.dev.aircarrier.acobject.Acobject;
import net.java.dev.aircarrier.acobject.MovableAcobjectSpatialDelegate;
import net.java.dev.aircarrier.ai.AIControls;
import net.java.dev.aircarrier.ai.targetting.SimpleTargetChoiceSensor;
import net.java.dev.aircarrier.ai.targetting.TargetChoiceSensor;
import net.java.dev.aircarrier.ai.targetting.TeamTargettingManager;
import net.java.dev.aircarrier.bullets.Bullet;
import net.java.dev.aircarrier.bullets.BulletPool;
import net.java.dev.aircarrier.bullets.HitEffectPool;
import net.java.dev.aircarrier.bullets.JumpingShootableNode;
import net.java.dev.aircarrier.bullets.NullShootableNode;
import net.java.dev.aircarrier.bullets.ReusableSource;
import net.java.dev.aircarrier.bullets.ShootableNode;
import net.java.dev.aircarrier.bullets.SimpleMeshFactory;
import net.java.dev.aircarrier.controls.PlayerControls;
import net.java.dev.aircarrier.hud.CenteredCircularRadarController;
import net.java.dev.aircarrier.hud.CompassBezel;
import net.java.dev.aircarrier.hud.Crosshair;
import net.java.dev.aircarrier.hud.DialBody;
import net.java.dev.aircarrier.hud.FriendFoePingSymbol;
import net.java.dev.aircarrier.hud.Gauge180;
import net.java.dev.aircarrier.hud.GaugeController;
import net.java.dev.aircarrier.hud.GaugeNumericDials;
import net.java.dev.aircarrier.hud.MessageDialog;
import net.java.dev.aircarrier.hud.MessageDialogStack;
import net.java.dev.aircarrier.hud.AcobjectPingNode;
import net.java.dev.aircarrier.hud.PingNode;
import net.java.dev.aircarrier.hud.RadarNode;
import net.java.dev.aircarrier.hud.RadarScopeSymbol;
import net.java.dev.aircarrier.hud.SingleDial;
import net.java.dev.aircarrier.hud.SpringyGaugeController;
import net.java.dev.aircarrier.level.SimpleLevel;
import net.java.dev.aircarrier.planes.TargettingPlane;
import net.java.dev.aircarrier.planes.BullpupModel;
import net.java.dev.aircarrier.planes.ChimeraModel;
import net.java.dev.aircarrier.planes.ConcreteCubeModel;
import net.java.dev.aircarrier.planes.AirMineModel;
import net.java.dev.aircarrier.planes.Plane;
import net.java.dev.aircarrier.planes.PlaneModel;
import net.java.dev.aircarrier.planes.RingTraveller;
import net.java.dev.aircarrier.scene.CarrierSkyBox;
import net.java.dev.aircarrier.scene.CarrierTerrainPage;
import net.java.dev.aircarrier.scene.RingModel;
import net.java.dev.aircarrier.scene.TerrainQuadModel;
import net.java.dev.aircarrier.sound.SoundUtils;
import net.java.dev.aircarrier.triggers.RingTrigger;
import net.java.dev.aircarrier.triggers.Trigger;
import net.java.dev.aircarrier.triggers.TriggerListController;
import net.java.dev.aircarrier.triggers.TriggerListener;
import net.java.dev.aircarrier.util.TextureLoader;
import net.java.dev.aircarrier.weapons.FireListener;
import net.java.dev.aircarrier.weapons.Gun;

import com.jme.bounding.OrientedBoundingBox;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.joystick.Joystick;
import com.jme.input.joystick.JoystickInput;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.ShadowedRenderPass;
import com.jme.scene.Node;
import com.jme.scene.Skybox;
import com.jme.scene.Text;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.LightState;
import com.jme.system.DisplaySystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.RangedAudioTracker;
import com.jmex.effects.glsl.BloomRenderPass;
import com.jmex.effects.water.WaterRenderPass;
import com.jmex.physics.StaticPhysicsNode;
import com.jmex.physics.material.Material;

/**
 * <code>CarrierSimpleLevel</code>
 * 
 * 	Simple level setup, to fly around testing various bits of world
 * 
 * @author shingoki
 * @version $Id: CarrierSimpleLevelManagers.java,v 1.41 2008/03/07 21:53:40 shingoki Exp $
 */
public class CarrierSimpleLevelManagers extends CarrierGame {

	private float terrainHeightScale = 1.5f;//0.5f;//1f;// 1.5f;
	private float terrainHorizontalScale = 28f;
	private int terrainTiles = 128;

	Skybox skybox;
	
	NodeTracker tracker;

	JoystickInput joystickInput;

	Joystick joystick;

	PlayerControls playerControls;
	
	//HudNode hudNode;
	
	Box box;
	
	List<TargettingPlane> planes = new ArrayList<TargettingPlane>();

	List<TargettingPlane> players = new ArrayList<TargettingPlane>();
	List<TargettingPlane> friends = new ArrayList<TargettingPlane>();
	List<TargettingPlane> enemies = new ArrayList<TargettingPlane>();

	Vector3f tempV = new Vector3f();
	
	JumpingShootableNode boxNode;
	
	ColorRGBA skyColor;
	
	CarrierTerrainPage terrain;
	
	List<ShootableNode> shootNodes;
	
	ReusableSource<Bullet> bulletSource;
	HitEffectPool hitEffectPool;
	
	Node mineNode;
	
	MessageDialogStack stack;
		
	private WaterRenderPass waterEffectRenderPass;
	private Quad waterQuad;
	
	private float farPlane = 5000.0f;
	private float textureScale = 0.01f;
	
	Vector3f transVec = new Vector3f();
	
	private BloomRenderPass bloomRenderPass;

	GaugeNumericDials dials;
	
	RangedAudioTracker audioTrackerL;
	RangedAudioTracker audioTrackerM;
	RangedAudioTracker audioTrackerWind;
	AudioTrack audioTrackG0;
	AudioTrack audioTrackG1;
	
	float lCycle = 0;
	float mCycle = 0;
	float lastTurning = 0;
	
	List<Acobject> ringPositions;
	
	TeamTargettingManager targettingManager;
	
	private static boolean gunSounds = true;
	
	static {
		JoystickInput.setProvider(JoystickInput.INPUT_LWJGL);
	}

	static ShadowedRenderPass sPass;
	
	static boolean shadows = false;
	
	RingTraveller traveller;
	
	/**
	 * Entry point for the test,
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		Logger.getLogger("").setLevel(Level.WARNING);
		
		CarrierSimpleLevelManagers app = new CarrierSimpleLevelManagers();
		
		if (shadows) {
			//we need a minimum stencil buffer at least.
			app.stencilBits = 4;
			
			sPass = new ShadowedRenderPass();
			new ShadowTweaker(sPass).setVisible(true);
		}
		
		app.setDialogBehaviour(ALWAYS_SHOW_PROPS_DIALOG);

		app.start();
	}

	@Override
	protected void simpleUpdate() {
		super.simpleUpdate();
				
		//update ai controls
		for (Plane pm : planes) {
			pm.update(tpf);
		}
		
		//Camera tracks plane
		tracker.update(tpf);

		//Skybox follows camera
		skybox.setLocalTranslation(cam.getLocation());
		
		//Move the mine around
		boxNode.update(tpf);
		
		//Update message dialog stack
		stack.update(tpf);
		
        // If add_message is a valid command (via key 0), add a message
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("add_message", false)) {
            stack.addMessage("New message, plane speed " + planes.get(0).getCurrentSpeed());
        }
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("prev_message", false)) {
            stack.previous();
        }
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("next_message", false)) {
            stack.next();
        }

        if (KeyBindingManager.getKeyBindingManager().isValidCommand("first_target", false)) {
            traveller.setTarget(ringPositions.get(0));
        }
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("last_target", false)) {
            traveller.setTarget(ringPositions.get(ringPositions.size()-1));
        }

        
        //update targetting
        targettingManager.update(tpf);
        
		//Move the water quad under airplane, with correct texture coords
		if (waterEffectRenderPass != null) {
			transVec.set( cam.getLocation().x, waterEffectRenderPass.getWaterHeight(), cam.getLocation().z );
			setTextureCoords( 0, transVec.x, -transVec.z, textureScale );
			//vertex coords
			setVertexCoords( transVec.x, transVec.y, transVec.z );
		}
		
		//Update sound
		//audioTracker.checkTrackAudible(planes.get(0).getBase().getWorldTranslation());		
		float l = 0;
		float m = 0;
		float speed = planes.get(0).getCurrentSpeed();

		if (speed < 40) {
			l = 1;
			m = 0;
		} else if (speed < 60) {
			l = (speed - 40f) / 40f + 0.5f;
			m = 0;
		} else if (speed < 90) {
			l = (90 - speed) / 30f;
			m = (speed - 60) / 30f;
		} else {
			l = 0;
			m = 0;
		}

		lCycle += tpf;
		mCycle += 1.242 * tpf;
		lCycle = lCycle % FastMath.TWO_PI;
		mCycle = mCycle % FastMath.TWO_PI;
		
		
		l += FastMath.sin(lCycle) / 5f;
		m += FastMath.sin(lCycle) / 5f;
		
		if (l < 0) l = 0;
		if (m < 0) m = 0;
		
		/*if (l < 0 || l > 1 || m < 0 || m > 1 || h < 0 || h > 1) {
			System.out.println(l + ", " + m + ", " + h);
		}*/

		float w = 0;
		float turning = 0;
		for (int i = 0; i < 3; i++) {
			float axis = FastMath.abs(planes.get(0).getControls().getAxis(i));
			if (axis > turning) turning = axis;
		}
		
		w = turning - lastTurning;
		if (w < 0) w = 0;
		if (w > 1) w = 1;
		lastTurning = turning;
		
		//System.out.println(w);
		
		if (audio != null) {
			audioTrackerL.getAudioTrack().setVolume(0.02f * l);
			audioTrackerM.getAudioTrack().setVolume(0.02f * m);
			audioTrackerWind.getAudioTrack().setVolume(0.1f * turning);
					
			audioTrackerL.checkTrackAudible(planes.get(0).getPosition());
			audioTrackerM.checkTrackAudible(planes.get(0).getPosition());
			audioTrackerWind.checkTrackAudible(planes.get(0).getPosition());
			
			if (gunSounds) {
				audioTrackG0.setWorldPosition(planes.get(0).getPosition());
				audioTrackG1.setWorldPosition(planes.get(0).getPosition());
			}
		}
	}

	private void setupJoystick() {
		joystickInput = JoystickInput.get();

		for (int i = 0; i < joystickInput.getJoystickCount(); i++) {
			System.out.println(joystickInput.getJoystick(i).getName());
		}

		if (joystickInput.getJoystickCount() > 0) {
			joystick = joystickInput.getJoystick(0);
		} else {
			joystick = null;
		}		
	}

	private void setupNodes() {
		rootNode.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
		fpsNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);

		//skyColor = new ColorRGBA(0.7f, 0.8f, 1f, 0.5f);
		//skyColor = new ColorRGBA(0.96f, 0.97f, 0.98f, 0.5f);
		//skyColor = new ColorRGBA(0.83f, 0.84f, 0.86f, 0.5f);
		skyColor = new ColorRGBA(0.956862745f, 0.945098039f, 0.917647058f, 0.5f);

		display.setTitle("AirCarrier Simple Level");
		display.getRenderer().setBackgroundColor(skyColor);

		CullState cs = display.getRenderer().createCullState();
		cs.setCullMode(CullState.CS_BACK);
		cs.setEnabled(true);
		rootNode.setRenderState(cs);		
	}
	
	private void setupTerrain() {
		//Create terrain
		terrain = CarrierTerrainPage.create("Terrain",
				//"resources/higher_height_small.jpg",
				// "resources/canyonWaterHeight.jpg",
				//"resources/islands_height_orig.jpg",
//				"resources/nice_island_height.jpg",
				"resources/nice_no_water_height.jpg",
				//"resources/dunesHeight.png",
			
				//"resources/nowaterHeight.jpg",
				//"resources/nowaterCroppedHeight.jpg",

				//"resources/higher_plain_lm_small.jpg",
				// "resources/canyonWaterTexture.jpg",
				//"resources/islands_LM_small.jpg",
				//"resources/islands_LM_small_sand.jpg",
				//"resources/nice_island_tex.jpg",
				
				"resources/nice_no_water_TX.jpg",
				//"resources/dunes.jpg",

				
				//"resources/nowaterTexture.jpg",
				//"resources/nowaterCroppedTextureRetouched.png",

				"resources/detail.png", terrainTiles + 1,//129,//257,
				terrainHorizontalScale,
				terrainHeightScale,
				false);


		//Create physics for terrain
        final StaticPhysicsNode staticNode = getPhysicsSpace().createStaticNode();
        staticNode.setMaterial(Material.ICE);
        staticNode.attachChild( terrain );
        //worldNode.attachChild( staticNode );
        reflectedNode.attachChild( staticNode );
        staticNode.generatePhysicsGeometry();		


        //terrain.setCullMode(SceneElement.CULL_ALWAYS);
        
        //FIXME find out cause of the huge slowdown using this on some systems.
		//Increases startup time from <10 seconds to a few minutes, and can cause
		//heavy CPU use and/or lockups.
		//Locking does seem to give a small performance increase, around 10%, on 
		//systems it doesn't trash completely ;)
		terrain.lock();
	}
	
	private void setupFog(Node node) {
		FogState fs = display.getRenderer().createFogState();
		fs.setDensity(1f);
		fs.setEnabled(true);
		fs.setColor(skyColor);
		fs.setEnd(1500);
		fs.setStart(50);
		fs.setDensityFunction(FogState.DF_LINEAR);
		fs.setApplyFunction(FogState.AF_PER_VERTEX);
		node.setRenderState(fs);		
	}

	private void setupSky() {
		skybox = new CarrierSkyBox("Sky", "resources/bsky", ".jpg");
		
		//was skynode
		reflectedNode.attachChild(skybox);		
	}
	
	@SuppressWarnings("unused")
	private void setupDunes() throws IOException {
		TerrainQuadModel dunes = new TerrainQuadModel("resources/desert2.jme", "resources/dunes.png", "resources/detail.png");
		reflectedNode.attachChild(dunes.getModel());
		
		
		dunes.getModel().setLocalScale(terrainHorizontalScale * terrainTiles / 2f);
		//dunes.getModel().getLocalTranslation().setY();
		//dunes.getModel().getLocalScale().setY(y)
		
		dunes.getModel().getLocalTranslation().setZ(-4f*terrainHorizontalScale);
		
		//Flip in Z, for some reason terrain is upside down
    	dunes.getModel().getLocalScale().setZ(-dunes.getModel().getLocalScale().getZ());

    	dunes.getModel().getLocalScale().setY(terrainHorizontalScale * terrainTiles / 2f * 0.8f * terrainHeightScale);
		
		//NodeRotator.rotate(dunes.getModel(), 1, FastMath.PI);
		CullState cullState = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
		cullState.setCullMode(CullState.CS_NONE);
		dunes.getModel().setRenderState(cullState);
		dunes.getModel().updateRenderState();
		setupFog(dunes.getModel());
		//planes.get(0).getBase().attachChild(dunes.getModel());
	}
	
	private void setupAirMine() throws IOException {
		box = new Box("The Box", new Vector3f(0,0,0),5,5,5);
		box.setModelBound(new OrientedBoundingBox());
		box.updateModelBound();

		boxNode = new JumpingShootableNode("The BoxNode", terrain);
		
		mineNode = new AirMineModel().getModel();
		mineNode.setLocalScale(5);
		boxNode.attachChild(mineNode);
		
		boxNode.updateWorldData(0);
		//FIXME find out what happened to this method
		//boxNode.updateCollisionTree();
		boxNode.updateWorldBound();
		boxNode.updateGeometricState(0, true);
		boxNode.updateRenderState();
		
		litWorldNode.attachChild(boxNode);
		boxNode.setLocalTranslation(new Vector3f(0, 250, 300));
		
		shootNodes.add(boxNode);		
	}
	
	@SuppressWarnings("unused")
	private void setupWater() {
		
		waterEffectRenderPass = new WaterRenderPass( cam, 6, false, false );

		waterQuad = new Quad( "waterQuad", 1, 1 );
		FloatBuffer normBuf = waterQuad.getNormalBuffer( 0 );
		normBuf.clear();
		normBuf.put( 0 ).put( 1 ).put( 0 );
		normBuf.put( 0 ).put( 1 ).put( 0 );
		normBuf.put( 0 ).put( 1 ).put( 0 );
		normBuf.put( 0 ).put( 1 ).put( 0 );

		waterEffectRenderPass.setWaterEffectOnSpatial( waterQuad );
		worldNode.attachChild( waterQuad );
		waterEffectRenderPass.setWaterHeight(267f);

		//waterEffectRenderPass.setWaterColorStart(new ColorRGBA(0,0,0,0));
		//waterEffectRenderPass.setWaterColorEnd(new ColorRGBA(0,0,0,0));
		//waterEffectRenderPass.useFadeToFogColor(true);
		waterEffectRenderPass.setRenderScale(waterEffectRenderPass.getRenderScale() * 2);
		
		waterEffectRenderPass.setReflectedScene( reflectedNode );
		waterEffectRenderPass.setSkybox( skybox );
		passManager.add( waterEffectRenderPass);
		
	}
	
	@SuppressWarnings("unused")
	private void setupBloom() {
		bloomRenderPass = new BloomRenderPass(cam, 4);
		
        
	       if(!bloomRenderPass.isSupported()) {
	           Text t = new Text("Text", "GLSL Not supported on this computer.");
	           t.setRenderQueueMode(Renderer.QUEUE_ORTHO);
	           t.setLightCombineMode(LightState.OFF);
	           t.setLocalTranslation(new Vector3f(0, 20, 0));
	           fpsNode.attachChild(t);
	       } else {
	           bloomRenderPass.add(rootNode);
	           bloomRenderPass.setUseCurrentScene(true);
	           bloomRenderPass.setBlurIntensityMultiplier(0.7f);
	           bloomRenderPass.setExposurePow(bloomRenderPass.getExposurePow()*10);
	           passManager.add(bloomRenderPass);
	       }
		
	}
	
	private void addPlane(PlaneModel model) throws IOException {
		TargettingPlane newPlane = 
			new TargettingPlane(
				worldNode, 
				getPhysicsSpace(), 
				input, 
				shootNodes, 
				terrain, 
				model, 
				hitEffectPool, 
				bulletSource);
		
		planes.add(newPlane);		
		litWorldNode.attachChild(newPlane.getBase());
	}
	
	private void setupHudNode() throws IOException {
		Crosshair crosshair = new Crosshair();
		hudNode.attachChild(crosshair);
		crosshair.setLocalTranslation(new Vector3f(display.getWidth() / 2,
				display.getHeight() / 2, 0));

	
		Texture speedTexture = TextureLoader.loadUncompressedTexture("resources/speedGauge.png");
		Gauge180 speedGauge = new Gauge180(
				"SpeedGauge", 0, FastMath.PI / 6f, FastMath.PI * 2f/3f, 
				true, false,
				128, speedTexture);
		hudNode.attachChild(speedGauge);
		speedGauge.setLocalTranslation(new Vector3f(display.getWidth() / 2,
				display.getHeight() / 2, 0));
		speedGauge.addController(new GaugeController(speedGauge, planes.get(0), "speed", 1f/90f));

		Texture healthTexture = TextureLoader.loadUncompressedTexture("resources/healthGauge.png");
		Gauge180 healthGauge = new Gauge180(
				"HealthGauge", 0, FastMath.PI / 6f, FastMath.PI * 2f/3f, 
				false, false, 128, healthTexture);
		hudNode.attachChild(healthGauge);
		healthGauge.setLocalTranslation(new Vector3f(display.getWidth() / 2,
				display.getHeight() / 2, 0));
		healthGauge.addController(new GaugeController(healthGauge, planes.get(0), "health", 1f));
	
		Node radarOffsetNode = new Node();
		
		RadarScopeSymbol rs = new RadarScopeSymbol();
		radarOffsetNode.attachChild(rs);
		
		RadarNode radar = new RadarNode(60);
		radarOffsetNode.attachChild(radar);
		
		Acobject radarCenter = planes.get(0);
		CenteredCircularRadarController radarController = new CenteredCircularRadarController(radar, radarCenter, 1f/400f);
		radar.addController(radarController);
		
		//Add pings for other planes
		for (Plane plane : planes) {
			PingNode ping = new AcobjectPingNode(plane);
			radar.addPing(ping);
			ping.attachChild(new FriendFoePingSymbol());
		}
		
		hudNode.attachChild(radarOffsetNode);
		radarOffsetNode.getLocalTranslation().set(display.getWidth() - 64 - 16, display.getHeight() - 64 - 16, 0);

		Node dialsOffsetNode = new Node("dialsOffsetNode");
		
		dials = new GaugeNumericDials("dials", 3, 0);
		dialsOffsetNode.attachChild(dials);
		dials.setLocalScale(32f);
		dials.addController(new SpringyGaugeController(dials, planes.get(0), "health", 100f));

		DialBody dialBody = new DialBody();
		dialsOffsetNode.attachChild(dialBody);
		dialBody.getLocalTranslation().set(-32, 0, 0);

		hudNode.attachChild(dialsOffsetNode);
		dialsOffsetNode.getLocalTranslation().set(132,
				88, 0);

		Node compassOffsetNode = new Node("compassOffsetNode");

		/*
		Node compassDial = CompassDial.createCompassDial();
		compassOffsetNode.attachChild(compassDial);
		compassDial.setLocalScale(32f);
*/
		
		SingleDial compassDial = new SingleDial("compassDial", 0);
		compassOffsetNode.attachChild(compassDial);
		compassDial.setLocalScale(32f);
		compassDial.addController(
				new GaugeController(
						compassDial, planes.get(0), 
						"heading", -1f/FastMath.TWO_PI));
		
		CompassBezel compassBezel = new CompassBezel();
		compassOffsetNode.attachChild(compassBezel);
		compassBezel.getLocalTranslation().set(0, 0, 0);
		
		hudNode.attachChild(compassOffsetNode);
		compassOffsetNode.getLocalTranslation().set(display.getWidth()/2, 56, 0);

		
		/*
		Texture dialogTexture = TextureLoader.loadUncompressedTexture("resources/dialogArea.png");
		Grid grid = new Grid("grid", 32, 32, 10, 10, true);
		TextureState textureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		textureState.setEnabled(true);
		textureState.setTexture(dialogTexture);
		grid.setRenderState(textureState);
		hudNode.attachChild(grid);
		grid.getLocalTranslation().set(200,
				200, 0);
		 */

		/*
		Texture dialogTexture = TextureLoader.loadUncompressedTexture("resources/dialogArea.png");
		DialogBox box = new DialogBox("box", dialogTexture);
		box.setDimension(512, 128);
		hudNode.attachChild(box);
		box.getLocalTranslation().set(display.getWidth()/2 - 256,
				display.getHeight() - 16 - 128, 0);
		 */

		/*
		MessageDialog dialog = new MessageDialog(40, 5);
		
		dialog.printMessage(
			//0123456789012345678901234567890123456789	
			  "Hello world, this is a message which, " +
			  "with any luck, will be spread properly " +
			  "across multiple lines. Testing hyphen-" +
			  "ation. Testing testing testing testing " +
			  "tab\t1\t12\t123\t1234\tend...");
			  
		orthoHudNode.attachChild(dialog);
		dialog.getLocalTranslation().set((display.getWidth() - dialog.getWidth()) / 2,
				display.getHeight() - 16 - dialog.getHeight(), 0);
		*/
		
		MessageDialog dialog = new MessageDialog(40, 5);
		stack = new MessageDialogStack(dialog, new Vector3f(0, 0, 0), new Vector3f(0, dialog.getHeight() + 32, 0));
		orthoHudNode.attachChild(stack);
		stack.getLocalTranslation().set((display.getWidth() - dialog.getWidth()) / 2,
				display.getHeight() - 16 - dialog.getHeight(), 0);
		
		
		hudNode.updateRenderState();
		hudNode.updateGeometricState(0, true);

		orthoHudNode.updateRenderState();
		orthoHudNode.updateGeometricState(0, true);

	}
	
	protected void simpleInitGame() {		
		
		setupJoystick();
		setupNodes();
		setupTerrain();
		setupFog(worldNode);
		setupSky();

		
		setupFog(terrain);
		
		shootNodes = new ArrayList<ShootableNode>();
		
		try {
		
			setupAirMine();

			SimpleMeshFactory meshFactory = SimpleMeshFactory.makeBulletMeshFactory();
			
			bulletSource = new BulletPool(worldNode, meshFactory);
			
			//PlaneModel planeModelPlayer = new HammerheadModel(false);
			PlaneModel planeModelPlayer = new BullpupModel(false);
			
			//No visible damage on the player's model, it's irritating :)
			planeModelPlayer.getDamagePositions().clear();
			//PlaneModel planeModelPlayer = new ChimeraModel(false);


			SimpleMeshFactory flareFactory = SimpleMeshFactory.makeHitFlareMeshFactory();
			SimpleMeshFactory ringFactory = SimpleMeshFactory.makeHitFlareMeshFactory();
			hitEffectPool = new HitEffectPool(worldNode, flareFactory, ringFactory);

			addPlane(planeModelPlayer);
			
			for (int e = 0; e < 8; e++) {
				/*if (e % 3 == 0) {
					addPlane(new ChimeraModel());
					//addPlane(new HammerheadModel());
				} else if (e % 3 == 2) {
					addPlane(new HammerheadModel());
				} else {
					addPlane(new BullpupModel());
					//addPlane(new HammerheadModel());
				}*/
				if (e < 3) {
					addPlane(new BullpupModel());
				} else {
					addPlane(new ChimeraModel());
				}
			}
			
			int p = 0;
			for (Plane plane : planes) {
				plane.setPosition(new Vector3f(0 + 50 * p, 550, 0));
				p++;
			}

			if (audio != null) {
				//Set audio to track the plane
		        audio.getEar().trackOrientation(planes.get(0).getBase());
		        audio.getEar().trackPosition(planes.get(0).getBase());
	
				//Set up player engine noise
				audioTrackerL = SoundUtils.createSFXTracker(
						TestJmexAudio.class.getResource("/resources/sound/generic_prop_low.ogg"), 
						planes.get(0).getBase(), 120, 240, 0.1f);
				audioTrackerM = SoundUtils.createSFXTracker(
						TestJmexAudio.class.getResource("/resources/sound/generic_prop_mid.ogg"), 
						planes.get(0).getBase(), 120, 240, 0.1f);
				audioTrackerWind = SoundUtils.createSFXTracker(
						TestJmexAudio.class.getResource("/resources/sound/wind.ogg"), 
						planes.get(0).getBase(), 120, 240, 0.1f);
	
				if (gunSounds) {
					audioTrackG0 = SoundUtils.createSFXBasic(
							TestJmexAudio.class.getResource("/resources/sound/gun.ogg"));
					audioTrackG0.setLooping(false);
					audioTrackG0.setVolume(0.1f);		
					audioTrackG0.setTargetVolume(0.08f);
					//audioTrackG0.setVolumeChangeRate(10)
		
					audioTrackG1 = SoundUtils.createSFXBasic(
							TestJmexAudio.class.getResource("/resources/sound/gun_small.ogg"));
					audioTrackG1.setLooping(false);
					audioTrackG1.setVolume(0.1f);		
					audioTrackG1.setTargetVolume(0.05f);
				
					planes.get(0).getPlaneNode().getGuns().get(0).addFireListener(new FireListener() {
						public void fired(Gun gun) {
							//System.out.println("Gun 0");
							audioTrackG0.play();
						}
					});
		
					planes.get(0).getPlaneNode().getGuns().get(2).addFireListener(new FireListener() {
						public void fired(Gun gun) {
							//System.out.println("Gun 1");
							audioTrackG1.play();
						}
					});
				}
			}
			
			//Set up hud node
			setupHudNode();

			//Add key to add message
	        KeyBindingManager.getKeyBindingManager().set("add_message", KeyInput.KEY_0);
	        KeyBindingManager.getKeyBindingManager().set("prev_message", KeyInput.KEY_8);
	        KeyBindingManager.getKeyBindingManager().set("next_message", KeyInput.KEY_9);
	        KeyBindingManager.getKeyBindingManager().set("first_target", KeyInput.KEY_6);
	        KeyBindingManager.getKeyBindingManager().set("last_target", KeyInput.KEY_7);
	        
			//Set the player controls
			playerControls = new PlayerControls(planes.get(0), joystick, 2);
			planes.get(0).setControls(playerControls);

			//list of players
			players.add(planes.get(0));
			
			//Keep list of AI steerers
			List<Acobject> steerers = new ArrayList<Acobject>();

			//Add the player as a steerer to avoid,
			steerers.add(planes.get(0));
			
			//Set the AI controls
			for (int i = 1; i < planes.size(); i++) {
				AIControls aic = new AIControls(
						planes.get(i),
						worldNode, 
						2,	//2 guns 
						terrain);
				//aic.setTarget(boxNode);
				
				//aic.setTarget(planes.get(0));
				if (i < 4) {
					//aic.setTarget(planes.get(i+2));
					friends.add(planes.get(i));
				} else {
					//aic.setTarget(planes.get(i%3));
					enemies.add(planes.get(i));
				}
				planes.get(i).setTargettingControls(aic);
				
				//Add the steerer to list, and set the list as its list
				//Note that the list isn't full when we start, but is by the time we are finished and is mutable
				steerers.add(planes.get(i));
				aic.getGroupAvoidance().setOthers(steerers);
				
			}

			TargetChoiceSensor<Acobject, Acobject> friendsSensor = new SimpleTargetChoiceSensor();
			TargetChoiceSensor<Acobject, Acobject> enemiesOnFriendsSensor = new SimpleTargetChoiceSensor();
			TargetChoiceSensor<Acobject, Acobject> enemiesOnPlayersSensor = new SimpleTargetChoiceSensor();
			targettingManager = new TeamTargettingManager(
					players, friends, enemies, 
					friendsSensor, enemiesOnFriendsSensor, enemiesOnPlayersSensor);
			
						
			camNode.setLocalTranslation(new Vector3f(planes.get(0).getCameraTarget()
					.getWorldTranslation()));

			tracker = new NodeTracker(planes.get(0).getCameraTarget(), camNode, 300);
			//250)	//ORIGINAL
			;//100);
			
			//SHADOW
			if (shadows) {
		        sPass.add(rootNode);
		        sPass.addOccluder(planes.get(0).getPlaneNode());
		        //sPass.addOccluder(occluders);
		        sPass.setRenderShadows(true);
		        sPass.setLightingMethod(ShadowedRenderPass.ADDITIVE);
		        passManager.add(sPass);
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		/*
        for (int i = 0; i < 20; i++) {
            final DynamicPhysicsNode dynamicNode3 = getPhysicsSpace().createDynamicNode();
            Node meshBox3;
			try {
				//meshBox3 = new ConcreteCubeModel().getModel();
				meshBox3 = new ConcreteCubeModel().getModel();
		        dynamicNode3.attachChild( meshBox3 );
		        dynamicNode3.generatePhysicsGeometry();
		
		        worldNode.attachChild( dynamicNode3 );
		        dynamicNode3.computeMass();
		
		        dynamicNode3.getLocalTranslation().set(0, 288 + 6 * i, 200);
		        dynamicNode3.setLocalScale(2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
        }
        */


		//setupLevel();
		
		//setupRingTraveller();
        
        
        //setupWater();
        //setupBloom();
        
        /*
		try {
			setupDunes();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
*/
        
		/*
		rootNode.updateGeometricState(0, true);
		rootNode.updateModelBound();
		rootNode.updateCollisionTree();
		rootNode.updateWorldData(0);
		*/
		terrain.updateRenderState();
		terrain.updateWorldData(0);
		
		rootNode.updateRenderState();
		
	}

	protected void setupRingTraveller() {
		if (ringPositions == null) return;
    	try {
    		traveller = new RingTraveller("Ring marker 1", terrain);
    		worldNode.attachChild(traveller);
    		traveller.setLocalTranslation(new Vector3f(0, 500, 0));
		} catch (IOException e) {
		}
	}
	
	protected void setupLevel() {
        List<Acobject> triggerObjects = new ArrayList<Acobject>();
        triggerObjects.add(planes.get(0));

        try {

	        SimpleLevel level = new SimpleLevel("resources/terrainRings.jme", 64);
	        worldNode.attachChild(level.getRootNode());
	        
	        ringPositions = new ArrayList<Acobject>();
	        
	        int i = 0;
	        for (Node ringNode : level.getRingPositions()) {

	        	ringPositions.add(new MovableAcobjectSpatialDelegate(ringNode, "ring", "ring", 9));
	        	
	        	Node ring = new RingModel().getModel();
				ringNode.attachChild(ring);
	        	
				//Torus torus = new Torus("Trigger Torus", 16, 8, 1, 9);
				//ringNode.attachChild(torus);
				
				RingTrigger trigger = new RingTrigger(9, 0.3f);
				ringNode.attachChild(trigger);
				
				TriggerListController triggerController = 
					new TriggerListController(trigger, triggerObjects);
				trigger.addController(triggerController);
				final int triggerIndex = i;
				trigger.addTriggerListener(new TriggerListener() {
					public void triggered(Trigger trigger, Acobject triggeredBy) {
						//System.out.println("Triggered " + triggerIndex);
						stack.addMessage("Triggered " + triggerIndex);
						int index = triggerIndex + 1;
						if (index >= ringPositions.size()) index = 0;
						traveller.setTarget(ringPositions.get(index));
					}
				});
				
				
				i++;
	        }
	        
	        i = 0;
	        for (Node concreteCubeNode : level.getConcreteCubePositions()) {
	        	
	            final StaticPhysicsNode staticBoxNode = getPhysicsSpace().createStaticNode();
				Node box = new ConcreteCubeModel().getModel();
				ShootableNode shootableBox = new NullShootableNode("Shootable box " + i);
				shootableBox.attachChild(box);
		        //staticBoxNode.attachChild( box );
				staticBoxNode.attachChild( shootableBox );
		        staticBoxNode.setLocalScale(10);
		        staticBoxNode.generatePhysicsGeometry();
		
		        concreteCubeNode.attachChild( staticBoxNode );
		
		        //staticBoxNode.getLocalTranslation().set(-250 + i * 50, 250 + 20 , ((float)terrainTiles)/2f * terrainHorizontalScale);
		        //staticBoxNode.getLocalTranslation().set(-250 + i * 20, 250 + 20 , 200);
		        
		        shootNodes.add(shootableBox);

				i++;
	        }

	        
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void setVertexCoords( float x, float y, float z ) {
		FloatBuffer vertBuf = waterQuad.getVertexBuffer( 0 );
		vertBuf.clear();

		vertBuf.put( x - farPlane ).put( y ).put( z - farPlane );
		vertBuf.put( x - farPlane ).put( y ).put( z + farPlane );
		vertBuf.put( x + farPlane ).put( y ).put( z + farPlane );
		vertBuf.put( x + farPlane ).put( y ).put( z - farPlane );
	}

	private void setTextureCoords( int buffer, float x, float y, float textureScale ) {
		x *= textureScale * 0.5f;
		y *= textureScale * 0.5f;
		textureScale = farPlane * textureScale;
		FloatBuffer texBuf;
		texBuf = waterQuad.getTextureBuffer( 0, buffer );
		texBuf.clear();
		texBuf.put( x ).put( textureScale + y );
		texBuf.put( x ).put( y );
		texBuf.put( textureScale + x ).put( y );
		texBuf.put( textureScale + x ).put( textureScale + y );
	}

	protected void cleanup() {
		super.cleanup();
		if (waterEffectRenderPass != null)
			waterEffectRenderPass.cleanup();
        if (bloomRenderPass != null)
            bloomRenderPass.cleanup();
	}

}
