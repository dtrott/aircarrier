/*
 *  $Id: CarrierGame.java,v 1.26 2007/08/13 16:34:42 shingoki Exp $
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

import java.util.ArrayList;
import java.util.logging.Logger;

import net.java.dev.aircarrier.hud.HudNode;
import net.java.dev.aircarrier.pass.AlternateCameraRenderPass;
import net.java.dev.aircarrier.physics.PhysicsSpaceExtended;
import net.java.dev.aircarrier.physics.PhysicsSpaceWrapper;

import com.jme.app.BaseGame;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.input.joystick.JoystickInput;
import com.jme.light.DirectionalLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.pass.BasicPassManager;
import com.jme.renderer.pass.Pass;
import com.jme.renderer.pass.RenderPass;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.WireframeState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.TextureManager;
import com.jme.util.Timer;
import com.jme.util.geom.Debugger;
import com.jmex.audio.AudioSystem;
import com.jmex.audio.RangedAudioTracker;
import com.jmex.physics.PhysicsSpace;

/**
 * <code>CarrierGame</code> provides camera, rootNode, etc.
 */
public abstract class CarrierGame extends BaseGame {

    private static final Logger logger = Logger.getLogger(CarrierGame.class.getName());

	/**
	 * Physics space
	 */
    private PhysicsSpaceExtended physicsSpace;

    /** The camera that we see through. */
    protected Camera cam;

    /** The root of our normal scene graph. */
    protected Node rootNode;

	Node worldNode;

	//Node skyNode;

	Node litWorldNode;

	Node reflectedNode;

	Node hudNode;

	Node orthoHudNode;

	CameraNode camNode;

    /** Handles our mouse/keyboard input. */
    //protected InputHandler input;

    /** High resolution timer for jME. */
    protected Timer timer;

    /** The root node of our text. */
    protected Node fpsNode;

    /** Displays all the lovely information at the bottom. */
    protected Text fps;

    /** Alpha bits to use for the renderer. */
    protected int alphaBits = 0;

    /** Depth bits to use for the renderer. */
    protected int depthBits = 8;

    /** Stencil bits to use for the renderer. */
    protected int stencilBits = 0;

    /** Number of samples to use for the multisample buffer. */
    protected int samples = 0;

    /**
     * Simply an easy way to get at timer.getTimePerFrame(). Also saves time so
     * you don't call it more than once per frame.
     */
    protected float tpf;

    /** True if the renderer should display the depth buffer. */
    protected boolean showDepth = false;

    /** True if the renderer should display bounds. */
    protected boolean showBounds = false;

    /** True if the rnederer should display normals. */
    protected boolean showNormals = false;

    /** A wirestate to turn on and off for the rootNode */
    protected WireframeState wireState;

    /** A lightstate to turn on and off for the rootNode */
    protected LightState lightState;

    /** Location of the font for jME's text at the bottom */
    public static String fontLocation = Text.DEFAULT_FONT;

    /** This is used to display print text. */
    protected StringBuffer updateBuffer = new StringBuffer(30);

    /** This is used to recieve getStatistics calls. */
    protected StringBuffer tempBuffer = new StringBuffer();

    protected boolean pause;

    float farView = 3000f;//2000;

    InputHandler input;

	float startTime = 0;

	protected BasicPassManager passManager;

    AudioSystem audio;
    ArrayList<RangedAudioTracker> trackers = new ArrayList<RangedAudioTracker>();


    /**
     * Updates the timer, sets tpf, updates the input and updates the fps
     * string. Also checks keys for toggling pause, bounds, normals, lights,
     * wire etc.
     *
     * @param interpolation
     *            unused in this implementation
     */
    protected void update(float interpolation) {
        /** Recalculate the framerate. */
        timer.update();
        /** Update tpf to time per frame according to the Timer. */
        tpf = timer.getTimePerFrame();

		//Cap frames to 0.1 seconds max. Prevents bizarre behaviour on prolonged glitches.
		if (tpf > 0.1f) tpf = 0.0f;

		startTime += tpf;
		if (startTime < 3) return;

        /** Check for key/mouse updates. */
        input.update(tpf);


        updateBuffer.setLength(0);
        updateBuffer.append("FPS: ").append((int) timer.getFrameRate()).append(
                " - ");

        // DJT TODO
        //updateBuffer.append(display.getRenderer().getStatistics(tempBuffer));
        // Send the fps to our fps bar at the bottom.
        fps.print(updateBuffer);


        //Handle key input for wireframe, pause, etc.
        handleKeys();

        if ( !pause ) {
            /** Call simpleUpdate in any derived classes of SimpleGame. */
            simpleUpdate();

            /** Update controllers/render states/transforms/bounds for rootNode. */
            rootNode.updateGeometricState(tpf, true);

            /** Update controllers/render states/transforms/bounds for hudNode. */
            hudNode.updateGeometricState(tpf, true);

            /** Update controllers/render states/transforms/bounds for orthoHudNode. */
            orthoHudNode.updateGeometricState(tpf, true);

            // update our audio system here:
            if (audio != null) audio.update();

        }


        if ( !pause ) {
            float tpf = this.tpf;
            if ( tpf > 1 || Float.isNaN( tpf ) ) {
            	logger.warning("Maximum physics update interval is 1 second - capped.");
                tpf = 1;
            }
            getPhysicsSpace().update( tpf );
        }

		passManager.updatePasses(tpf);

    }

    protected void handleKeys() {
        /** If toggle_pause is a valid command (via key p), change pause. */
        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
                "toggle_pause", false)) {
            pause = !pause;
        }

        /** If toggle_wire is a valid command (via key T), change wirestates. */
        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
                "toggle_wire", false)) {
            wireState.setEnabled(!wireState.isEnabled());
            rootNode.updateRenderState();
        }
        /** If toggle_lights is a valid command (via key L), change lightstate. */
        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
                "toggle_lights", false)) {
            lightState.setEnabled(!lightState.isEnabled());
            rootNode.updateRenderState();
        }
        /** If toggle_bounds is a valid command (via key B), change bounds. */
        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
                "toggle_bounds", false)) {
            showBounds = !showBounds;
        }
        /** If toggle_depth is a valid command (via key F3), change depth. */
        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
                "toggle_depth", false)) {
            showDepth = !showDepth;
        }

        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
                "toggle_normals", false)) {
            showNormals = !showNormals;
        }
        /** If camera_out is a valid command (via key C), show camera location. */
        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
                "camera_out", false)) {
            System.err.println("Camera at: "
                    + display.getRenderer().getCamera().getLocation());
        }

        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
                "screen_shot", false)) {
            display.getRenderer().takeScreenShot("SimpleGameScreenShot");
        }

        if (KeyBindingManager.getKeyBindingManager().isValidCommand(
                "parallel_projection", false)) {
            if (cam.isParallelProjection()) {
                cameraPerspective();
            } else {
                cameraParallel();
            }
        }

        if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit",
                false)) {
            finish();
        }
    }

    /**
     * Clears stats, the buffers and renders bounds and normals if on.
     *
     * @param interpolation
     *            unused in this implementation
     */
    protected void render(float interpolation) {
        Renderer r = display.getRenderer();
        /** Reset display's tracking information for number of triangles/vertexes */

        // TODO DJT
        //r.clearStatistics();
        /** Clears the previously rendered information. */
        r.clearBuffers();


        /** Have the PassManager render. */
        passManager.renderPasses(display.getRenderer());

        /** Call simpleRender() in any derived classes. */
        simpleRender();

        /*
        if (showDepth) {
            r.renderQueue();
            Debugger.drawBuffer(Texture.RTT_SOURCE_DEPTH, Debugger.NORTHEAST, r);
        }
        */


        doDebug(display.getRenderer());
    }

    protected void doDebug(Renderer r) {
        /**
         * If showing bounds, draw rootNode's bounds, and the bounds of all its
         * children.
         */
        if ( showBounds ) {
            Debugger.drawBounds( rootNode, r, true );
        }

        if ( showNormals ) {
            Debugger.drawNormals( rootNode, r );
        }
    }
    /**
     * Creates display, sets up camera, and binds keys. Called in
     * BaseGame.start() directly after the dialog box.
     *
     */
    protected void initSystem() {
        try {
            /**
             * Get a DisplaySystem acording to the renderer selected in the
             * startup box.
             */
            display = DisplaySystem.getDisplaySystem(settings.getRenderer());

            display.setMinDepthBits(depthBits);
            display.setMinStencilBits(stencilBits);
            display.setMinAlphaBits(alphaBits);
            display.setMinSamples(samples);

            /** Create a window with the startup box's information. */
            display.createWindow(settings.getWidth(), settings.getHeight(),
                    settings.getDepth(), settings.getFrequency(), settings
                    .isFullscreen());
            /**
             * Create a camera specific to the DisplaySystem that works with the
             * display's width and height
             */
            cam = display.getRenderer().createCamera(display.getWidth(),
                    display.getHeight());

        } catch (JmeException e) {
            /**
             * If the displaysystem can't be initialized correctly, exit
             * instantly.
             */
            e.printStackTrace();
            System.exit(1);
        }

        input = new InputHandler();

        /** Set a black background. */
        display.getRenderer().setBackgroundColor(ColorRGBA.black);

        /** Set up how our camera sees. */
        cameraPerspective();
        Vector3f loc = new Vector3f(0.0f, 0.0f, 25.0f);
        Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
        Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f dir = new Vector3f(0.0f, 0f, -1.0f);
        /** Move our camera to a correct place and orientation. */
        cam.setFrame(loc, left, up, dir);
        /** Signal that we've changed our camera's location/frustum. */
        cam.update();
        /** Assign the camera to this renderer. */
        display.getRenderer().setCamera(cam);

        /** Get a high resolution timer for FPS updates. */
        timer = Timer.getTimer();
        //timer = NanoTimer.getTimer();

        /** Sets the title of our display. */
        display.setTitle("Air Carrier");

        /**
         * Signal to the renderer that it should keep track of rendering
         * information.
         */
        // DJT TODO
        //display.getRenderer().enableStatistics(true);

        /** Assign key P to action "toggle_pause". */
        KeyBindingManager.getKeyBindingManager().set("toggle_pause",
                KeyInput.KEY_P);
        /** Assign key T to action "toggle_wire". */
        KeyBindingManager.getKeyBindingManager().set("toggle_wire",
                KeyInput.KEY_T);
        /** Assign key L to action "toggle_lights". */
        KeyBindingManager.getKeyBindingManager().set("toggle_lights",
                KeyInput.KEY_L);
        /** Assign key B to action "toggle_bounds". */
        KeyBindingManager.getKeyBindingManager().set("toggle_bounds",
                KeyInput.KEY_B);
        /** Assign key N to action "toggle_normals". */
        KeyBindingManager.getKeyBindingManager().set("toggle_normals",
                KeyInput.KEY_N);
        /** Assign key C to action "camera_out". */
        KeyBindingManager.getKeyBindingManager().set("camera_out",
                KeyInput.KEY_C);
        KeyBindingManager.getKeyBindingManager().set("screen_shot",
                KeyInput.KEY_F1);
        KeyBindingManager.getKeyBindingManager().set("exit",
                KeyInput.KEY_ESCAPE);
        KeyBindingManager.getKeyBindingManager().set("parallel_projection",
                KeyInput.KEY_F2);
        KeyBindingManager.getKeyBindingManager().set("toggle_depth",
                KeyInput.KEY_F3);
    }

    protected void cameraPerspective() {
        cam.setFrustumPerspective(60.0f, (float) display.getWidth()
                / (float) display.getHeight(), 1, farView);
        cam.setParallelProjection(false);
        cam.update();
    }

    protected void cameraParallel() {
        cam.setParallelProjection(true);
        float aspect = (float) display.getWidth() / display.getHeight();
        cam.setFrustum(-100, 1000, -50 * aspect, 50 * aspect, -50, 50);
        cam.update();
    }

    /**
     * Creates rootNode, lighting, statistic text, and other basic render
     * states. Called in BaseGame.start() after initSystem().
     */
    protected void initGame() {

        setPhysicsSpace( new PhysicsSpaceWrapper(PhysicsSpace.create()) );

        /** Create rootNode */
        rootNode = new Node("rootNode");

        /**
         * Create a wirestate to toggle on and off. Starts disabled with default
         * width of 1 pixel.
         */
        wireState = display.getRenderer().createWireframeState();
        wireState.setEnabled(false);
        rootNode.setRenderState(wireState);

        /**
         * Create a ZBuffer to display pixels closest to the camera above
         * farther ones.
         */
        ZBufferState buf = display.getRenderer().createZBufferState();
        buf.setEnabled(true);
        buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        rootNode.setRenderState(buf);

        // Then our font Text object.
        /** This is what will actually have the text at the bottom. */
        fps = Text.createDefaultTextLabel("FPS label");
        fps.setCullHint(Spatial.CullHint.Never);
        fps.setTextureCombineMode(Spatial.TextureCombineMode.Replace);

        // Finally, a stand alone node (not attached to root on purpose)
        fpsNode = new Node("FPS node");
        fpsNode.setRenderState(fps.getRenderState(RenderState.RS_BLEND));
        fpsNode.setRenderState(fps.getRenderState(RenderState.RS_TEXTURE));
        fpsNode.attachChild(fps);
        fps.setCullHint(Spatial.CullHint.Never);


		worldNode = new Node("worldNode");
		//skyNode = new Node("skyNode");

		reflectedNode = new Node("reflectedNode");


		//reflectedNode.attachChild(skyNode);
		rootNode.attachChild(reflectedNode);

		rootNode.attachChild(worldNode);

		litWorldNode = new Node("litWorldNode");
		worldNode.attachChild(litWorldNode);

        // ---- LIGHTS
        /** Set up a basic, default light. */
		DirectionalLight light = new DirectionalLight();
		light.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		light.setAmbient(new ColorRGBA(1f, 1f, 1f, 1.0f));
		light.setDirection(new Vector3f(0, -0.422f, -1));
		light.setEnabled(true);

		//SHADOW
        //light.setShadowCaster(true);

        /** Attach the light to a lightState and the lightState to rootNode. */
        lightState = display.getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.attach(light);

        light = new DirectionalLight();
		light.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
		light.setAmbient(new ColorRGBA(0.7f, 0.7f, 1f, 1.0f));
		light.setDirection(new Vector3f(0, -0.422f, 1));
		light.setEnabled(true);
		lightState.attach(light);

        litWorldNode.setRenderState(lightState);

        //Set up pass manager, get it to draw root and fps nodes
        passManager = new BasicPassManager();

		RenderPass rootPass = new RenderPass();
		rootPass.add( rootNode );
		passManager.add( rootPass );

		RenderPass fpsPass = new RenderPass();
		fpsPass.add( fpsNode );
        //DJT TODO
		//passManager.add( fpsPass );

		//Pass hudPass = new RenderPass();
		hudNode = new HudNode(display, "Plane HUD");
		hudNode.setLocalTranslation(-display.getRenderer().getWidth()/2, -display.getRenderer().getHeight()/2, 0);

		Pass hudPass = new AlternateCameraRenderPass();
		hudPass.add( hudNode );
		passManager.add( hudPass );


		orthoHudNode = new HudNode(display, "Plane ORTHO HUD", true);
		//hudNode.setLocalTranslation(-display.getRenderer().getWidth()/2, -display.getRenderer().getHeight()/2, 0);

		Pass orthoHudPass = new RenderPass();
		orthoHudPass.add( orthoHudNode );
		passManager.add( orthoHudPass );


		camNode = new CameraNode("Camera Node", cam);
		worldNode.attachChild(camNode);
		camNode.updateWorldData(0);

		audio = null;

        // grab a handle to our audio system.

		/*
		try {
			audio = AudioSystem.getSystemWithException();
	        // setup our ear tracker to track the camera's position and orientation.
	        audio.getEar().trackOrientation(cam);
	        audio.getEar().trackPosition(cam);
		} catch (AudioSystemException e) {
			e.printStackTrace();
		}
        */

		audio = AudioSystem.getSystem();
        // setup our ear tracker to track the camera's position and orientation.
        audio.getEar().trackOrientation(cam);
        audio.getEar().trackPosition(cam);


        /** Let derived classes initialize. */
        simpleInitGame();

        //Cull back faces on whole world
        CullState cs = display.getRenderer().createCullState();
        cs.setCullFace(CullState.Face.Back);
        cs.setEnabled(true);
        rootNode.setRenderState(cs);

        /**
         * Update geometric and rendering information for both the rootNode and
         * fpsNode.
         */
        rootNode.updateGeometricState(0.0f, true);
        rootNode.updateRenderState();
        fpsNode.updateGeometricState(0.0f, true);
        fpsNode.updateRenderState();

        //Get all textures loaded
		TextureManager.preloadCache(DisplaySystem.getDisplaySystem().getRenderer());

    }

    /**
     * Called near end of initGame(). Must be defined by derived classes.
     */
    protected abstract void simpleInitGame();

    /**
     * Can be defined in derived classes for custom updating. Called every frame
     * in update.
     */
    protected void simpleUpdate() {
    }

    /**
     * Can be defined in derived classes for custom rendering.
     * Called every frame in render.
     */
    protected void simpleRender() {
    }

    /**
     * unused
     */
    protected void reinit() {
    }

    /**
     * Cleans up the keyboard.
     */
    protected void cleanup() {
//    	LOGGINGFIX
        //LoggingSystem.getLogger().log(Level.INFO, "Cleaning up resources.");

        KeyInput.destroyIfInitalized();
        MouseInput.destroyIfInitalized();
        JoystickInput.destroyIfInitalized();

        physicsSpace.delete();
    }

    /**
     * @return the physics space for this simple game
     */
    public PhysicsSpaceExtended getPhysicsSpace() {
        return physicsSpace;
    }

    /**
     * @param physicsSpace The physics space for this simple game
     */
	protected void setPhysicsSpace(PhysicsSpaceExtended physicsSpace) {
		if ( physicsSpace != this.physicsSpace ) {
			if ( this.physicsSpace != null )
	       		this.physicsSpace.delete();
			this.physicsSpace = physicsSpace;
		}
	}

    /**
     * Calls the quit of BaseGame to clean up the display and then closes the JVM.
     */
    protected void quit() {
        super.quit();
        System.exit(0);
    }
}
