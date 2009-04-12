package net.java.dev.aircarrier.scene.actree.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme.app.AbstractGame;
import com.jme.app.BaseGame;
import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.input.MouseLookHandler;
import com.jme.input.joystick.JoystickInput;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.scene.Spatial;
import com.jme.scene.state.LightState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.WireframeState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jme.util.Timer;
import com.jme.util.geom.Debugger;

/**
 * <code>BaseSimpleGame</code> provides the simplest possible implementation of a
 * main game loop. Interpolation is used between frames for varying framerates.
 *
 * @author Joshua Slack, (javadoc by cep21)
 * @version $Id: AcubeGame.java,v 1.2 2008/09/19 22:04:29 shingoki Exp $
 */
public abstract class AcubeGame extends BaseGame {
    private static final Logger logger = Logger.getLogger(AcubeGame.class
            .getName());

    /**
     * The camera that we see through.
     */
    protected Camera cam;

    /**
     * The root of our normal scene graph.
     */
    protected Node rootNode;

    /**
     * Handles our mouse/keyboard input.
     */
    protected InputHandler input;

    /**
     * High resolution timer for jME.
     */
    protected Timer timer;

    /**
     * The root node of our text.
     */
    protected Node fpsNode;

    /**
     * Displays all the lovely information at the bottom.
     */
    protected Text fps;

    /**
     * Alpha bits to use for the renderer. Must be set in the constructor.
     */
    protected int alphaBits = 0;

    /**
     * Depth bits to use for the renderer. Must be set in the constructor.
     */
    protected int depthBits = 8;

    /**
     * Stencil bits to use for the renderer. Must be set in the constructor.
     */
    protected int stencilBits = 0;

    /**
     * Number of samples to use for the multisample buffer. Must be set in the constructor.
     */
    protected int samples = 0;

    /**
     * Simply an easy way to get at timer.getTimePerFrame(). Also saves time so
     * you don't call it more than once per frame.
     */
    protected float tpf;

    /**
     * True if the renderer should display the depth buffer.
     */
    protected boolean showDepth = false;

    /**
     * True if the renderer should display bounds.
     */
    protected boolean showBounds = false;

    /**
     * True if the rnederer should display normals.
     */
    protected boolean showNormals = false;

    /**
     * A wirestate to turn on and off for the rootNode
     */
    protected WireframeState wireState;

    /**
     * A lightstate to turn on and off for the rootNode
     */
    protected LightState lightState;

    /**
     * Location of the font for jME's text at the bottom
     */
    public static String fontLocation = Text.DEFAULT_FONT;

    /**
     * This is used to display print text.
     */
    protected StringBuffer updateBuffer = new StringBuffer( 30 );

    /**
     * This is used to recieve getStatistics calls.
     */
    protected StringBuffer tempBuffer = new StringBuffer();

    protected boolean pause;

    /**
     * Updates the timer, sets tpf, updates the input and updates the fps
     * string. Also checks keys for toggling pause, bounds, normals, lights,
     * wire etc.
     *
     * @param interpolation unused in this implementation
     * @see AbstractGame#update(float interpolation)
     */
    protected void update( float interpolation ) {
        /** Recalculate the framerate. */
        timer.update();
        /** Update tpf to time per frame according to the Timer. */
        tpf = timer.getTimePerFrame();

        /** Check for key/mouse updates. */
        updateInput();

        // Execute updateQueue item
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).execute();

        updateBuffer.setLength( 0 );
        updateBuffer.append( "FPS: " ).append( (int) timer.getFrameRate() ).append(
                " - " );

        // DJT TODO
        //updateBuffer.append( display.getRenderer().getStatistics( tempBuffer ) );
        /** Send the fps to our fps bar at the bottom. */
        fps.print( updateBuffer );

        /** If toggle_pause is a valid command (via key p), change pause. */
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "toggle_pause", false ) ) {
            pause = !pause;
        }

        /** If step is a valid command (via key ADD), update scenegraph one unit. */
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "step", true ) ) {
        	simpleUpdate();
        	rootNode.updateGeometricState(tpf, true);
        }

        /** If toggle_wire is a valid command (via key T), change wirestates. */
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "toggle_wire", false ) ) {
            wireState.setEnabled( !wireState.isEnabled() );
            rootNode.updateRenderState();
        }
        /** If toggle_lights is a valid command (via key L), change lightstate. */
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "toggle_lights", false ) ) {
            lightState.setEnabled( !lightState.isEnabled() );
            rootNode.updateRenderState();
        }
        /** If toggle_bounds is a valid command (via key B), change bounds. */
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "toggle_bounds", false ) ) {
            showBounds = !showBounds;
        }
        /** If toggle_depth is a valid command (via key F3), change depth. */
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "toggle_depth", false ) ) {
            showDepth = !showDepth;
        }

        if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "toggle_normals", false ) ) {
            showNormals = !showNormals;
        }
        /** If camera_out is a valid command (via key C), show camera location. */
        if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "camera_out", false ) ) {
            logger.info( "Camera at: "
                    + display.getRenderer().getCamera().getLocation() );
        }

        if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "screen_shot", false ) ) {
            display.getRenderer().takeScreenShot( "SimpleGameScreenShot" );
        }

        if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "parallel_projection", false ) ) {
            if ( cam.isParallelProjection() ) {
                cameraPerspective();
            }
            else {
                cameraParallel();
            }
        }

        if ( KeyBindingManager.getKeyBindingManager().isValidCommand(
                "mem_report", false ) ) {
            long totMem = Runtime.getRuntime().totalMemory();
            long freeMem = Runtime.getRuntime().freeMemory();
            long maxMem = Runtime.getRuntime().maxMemory();

            logger.info("|*|*|  Memory Stats  |*|*|");
            logger.info("Total memory: "+(totMem>>10)+" kb");
            logger.info("Free memory: "+(freeMem>>10)+" kb");
            logger.info("Max memory: "+(maxMem>>10)+" kb");
        }

        if ( KeyBindingManager.getKeyBindingManager().isValidCommand( "exit",
                false ) ) {
            finish();
        }

        if ( !pause ) {
            /** Call simpleUpdate in any derived classes of SimpleGame. */
            simpleUpdate();

            /** Update controllers/render states/transforms/bounds for rootNode. */
            rootNode.updateGeometricState(tpf, true);
        }

    }

    /**
     * Check for key/mouse updates. Allow overriding this method to skip update in subclasses.
     */
    protected void updateInput() {
        input.update( tpf );
    }

    /**
     * Clears stats, the buffers and renders bounds and normals if on.
     *
     * @param interpolation unused in this implementation
     * @see AbstractGame#render(float interpolation)
     */
    protected void render( float interpolation ) {
        Renderer r = display.getRenderer();
        /** Reset display's tracking information for number of triangles/vertexes */

        // DJT TODO
        //r.clearStatistics();
        /** Clears the previously rendered information. */
        r.clearBuffers();

        // Execute renderQueue item
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER).execute();

        /** Draw the rootNode and all its children. */
        r.draw(rootNode);

        /** Call simpleRender() in any derived classes. */
        simpleRender();

        /** Draw the fps node to show the fancy information at the bottom. */
        r.draw(fpsNode);

        doDebug(r);

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
            Debugger.drawTangents( rootNode, r );
        }

        if (showDepth) {
            r.renderQueue();
            Debugger.drawBuffer(Texture.RenderToTextureType.Depth, Debugger.NORTHEAST, r);
        }

    }

    /**
     * Creates display, sets up camera, and binds keys. Called in
     * BaseGame.start() directly after the dialog box.
     *
     * @see AbstractGame#initSystem()
     */
    protected void initSystem() throws JmeException {
        logger.info(getVersion());
        try {
            /**
             * Get a DisplaySystem acording to the renderer selected in the
             * startup box.
             */
            display = DisplaySystem.getDisplaySystem(settings.getRenderer() );

            display.setMinDepthBits( depthBits );
            display.setMinStencilBits( stencilBits );
            display.setMinAlphaBits( alphaBits );
            display.setMinSamples( samples );

            /** Create a window with the startup box's information. */
            display.createWindow( settings.getWidth(), settings.getHeight(),
                    settings.getDepth(), settings.getFrequency(), settings.isFullscreen());

            logger.info("Running on: " + display.getAdapter()
                    + "\nDriver version: " + display.getDriverVersion() + "\n"
                    + display.getDisplayVendor() + " - "
                    + display.getDisplayRenderer() + " - "
                    + display.getDisplayAPIVersion());


            /**
             * Create a camera specific to the DisplaySystem that works with the
             * display's width and height
             */
            cam = display.getRenderer().createCamera( display.getWidth(),
                    display.getHeight() );

        } catch ( JmeException e ) {
            /**
             * If the displaysystem can't be initialized correctly, exit
             * instantly.
             */
            logger.log(Level.SEVERE, "Could not create displaySystem", e);
            System.exit( 1 );
        }

        /** Set a black background. */
        display.getRenderer().setBackgroundColor( ColorRGBA.black.clone() );

        /** Set up how our camera sees. */
        cameraPerspective();
        Vector3f loc = new Vector3f( 0.0f, 0.0f, 25.0f );
        Vector3f left = new Vector3f( -1.0f, 0.0f, 0.0f );
        Vector3f up = new Vector3f( 0.0f, 1.0f, 0.0f );
        Vector3f dir = new Vector3f( 0.0f, 0f, -1.0f );
        /** Move our camera to a correct place and orientation. */
        cam.setFrame( loc, left, up, dir );
        /** Signal that we've changed our camera's location/frustum. */
        cam.update();
        /** Assign the camera to this renderer. */
        display.getRenderer().setCamera( cam );

        /** Create a basic input controller. */
        /*FirstPersonHandler firstPersonHandler = new FirstPersonHandler( cam, 5,
                0.3f );
        input = firstPersonHandler;*/

        MouseLookHandler mouseLookHandler = new MouseLookHandler(cam, 0.3f);
        input = mouseLookHandler;

        /** Get a high resolution timer for FPS updates. */
        timer = Timer.getTimer();

        /** Sets the title of our display. */
        String className = getClass().getName();
        if ( className.lastIndexOf( '.' ) > 0 ) className = className.substring( className.lastIndexOf( '.' )+1 );
        display.setTitle( className );
        /**
         * Signal to the renderer that it should keep track of rendering
         * information.
         */

        // TODO DJT
        //display.getRenderer().enableStatistics( true );

        /** Assign key P to action "toggle_pause". */
        KeyBindingManager.getKeyBindingManager().set( "toggle_pause",
                KeyInput.KEY_P );
        /** Assign key ADD to action "step". */
        KeyBindingManager.getKeyBindingManager().set( "step",
                KeyInput.KEY_ADD );
        /** Assign key T to action "toggle_wire". */
        KeyBindingManager.getKeyBindingManager().set( "toggle_wire",
                KeyInput.KEY_T );
        /** Assign key L to action "toggle_lights". */
        KeyBindingManager.getKeyBindingManager().set( "toggle_lights",
                KeyInput.KEY_L );
        /** Assign key B to action "toggle_bounds". */
        KeyBindingManager.getKeyBindingManager().set( "toggle_bounds",
                KeyInput.KEY_B );
        /** Assign key N to action "toggle_normals". */
        KeyBindingManager.getKeyBindingManager().set( "toggle_normals",
                KeyInput.KEY_N );
        /** Assign key C to action "camera_out". */
        KeyBindingManager.getKeyBindingManager().set( "camera_out",
                KeyInput.KEY_C );
        KeyBindingManager.getKeyBindingManager().set( "screen_shot",
                KeyInput.KEY_F1 );
        KeyBindingManager.getKeyBindingManager().set( "exit",
                KeyInput.KEY_ESCAPE );
        KeyBindingManager.getKeyBindingManager().set( "parallel_projection",
                KeyInput.KEY_F2 );
        KeyBindingManager.getKeyBindingManager().set( "toggle_depth",
                KeyInput.KEY_F3 );
        KeyBindingManager.getKeyBindingManager().set("mem_report",
                KeyInput.KEY_R);
    }

    protected void cameraPerspective() {
        cam.setFrustumPerspective( 45.0f, (float) display.getWidth()
                / (float) display.getHeight(), 0.01f, 1000 );
        cam.setParallelProjection( false );
        cam.update();
    }

    protected void cameraParallel() {
        cam.setParallelProjection( true );
        float aspect = (float) display.getWidth() / display.getHeight();
        cam.setFrustum( -100, 1000, -50 * aspect, 50 * aspect, -50, 50 );
        cam.update();
    }

    /**
     * Creates rootNode, lighting, statistic text, and other basic render
     * states. Called in BaseGame.start() after initSystem().
     *
     * @see AbstractGame#initGame()
     */
    protected void initGame() {
        /** Create rootNode */
        rootNode = new Node( "rootNode" );

        /**
         * Create a wirestate to toggle on and off. Starts disabled with default
         * width of 1 pixel.
         */
        wireState = display.getRenderer().createWireframeState();
        wireState.setEnabled( false );
        rootNode.setRenderState( wireState );

        /**
         * Create a ZBuffer to display pixels closest to the camera above
         * farther ones.
         */
        ZBufferState buf = display.getRenderer().createZBufferState();
        buf.setEnabled( true );
        buf.setFunction( ZBufferState.TestFunction.LessThanOrEqualTo);
        rootNode.setRenderState( buf );

        // Then our font Text object.
        /** This is what will actually have the text at the bottom. */
        fps = Text.createDefaultTextLabel( "FPS label" );
        fps.setCullHint(Spatial.CullHint.Never);
        fps.setTextureCombineMode( Spatial.TextureCombineMode.Replace);

        // Finally, a stand alone node (not attached to root on purpose)
        fpsNode = new Node( "FPS node" );

        /*
        DJT
        fpsNode.setRenderState( fps.getRenderState( RenderState.RS_ALPHA ) );
        fpsNode.setRenderState( fps.getRenderState( RenderState.RS_TEXTURE ) );
         */
        fpsNode.attachChild( fps );
        fpsNode.setCullHint(Spatial.CullHint.Never);

        // ---- LIGHTS
        /** Set up a basic, default light. */
        PointLight light = new PointLight();
        light.setDiffuse( new ColorRGBA( 0.75f, 0.75f, 0.75f, 0.75f ) );
        light.setAmbient( new ColorRGBA( 0.5f, 0.5f, 0.5f, 1.0f ) );
        light.setLocation( new Vector3f( 100, 100, 100 ) );
        light.setEnabled( true );

        /** Attach the light to a lightState and the lightState to rootNode. */
        lightState = display.getRenderer().createLightState();
        lightState.setEnabled( true );
        lightState.attach( light );
        rootNode.setRenderState( lightState );

        /** Let derived classes initialize. */
        simpleInitGame();

        timer.reset();

        /**
         * Update geometric and rendering information for both the rootNode and
         * fpsNode.
         */
        rootNode.updateGeometricState( 0.0f, true );
        rootNode.updateRenderState();
        fpsNode.updateGeometricState( 0.0f, true );
        fpsNode.updateRenderState();

        timer.reset();
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
        //do nothing
    }

    /**
     * Can be defined in derived classes for custom rendering.
     * Called every frame in render.
     */
    protected void simpleRender() {
        //do nothing
    }

    /**
     * unused
     *
     * @see AbstractGame#reinit()
     */
    protected void reinit() {
        //do nothing
    }

    /**
     * Cleans up the keyboard.
     *
     * @see AbstractGame#cleanup()
     */
    protected void cleanup() {
        logger.info( "Cleaning up resources." );

        TextureManager.doTextureCleanup();
        if (display != null && display.getRenderer() != null)
            display.getRenderer().cleanup();
        KeyInput.destroyIfInitalized();
        MouseInput.destroyIfInitalized();
        JoystickInput.destroyIfInitalized();
    }

    /**
     * Calls the quit of BaseGame to clean up the display and then closes the JVM.
     */
    protected void quit() {
        super.quit();
        System.exit( 0 );
    }


}