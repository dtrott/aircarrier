package net.java.dev.aircarrier.scene;

import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Controller;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.effects.particles.ParticleFactory;
import com.jmex.effects.particles.ParticleInfluence;
import com.jmex.effects.particles.ParticleMesh;
import com.jmex.effects.particles.SimpleParticleInfluenceFactory;
import com.jmex.effects.particles.SwarmInfluence;

public class SmokerFactory {

	public final static int SMOKE_RATE = 12;//15;//20;
	public final static int FLAME_RATE = 40;//40;//80;
	public final static int EXPLODER_RATE = 160;//300;

	static BlendState flameBlendState;
	static BlendState smokeBlendState;

	static TextureState smokeTextureState;
	static TextureState flameTextureState;

	static ZBufferState zState;
	static LightState noLight;

	static ParticleInfluence rise;
	static ParticleInfluence burn;

	static SwarmInfluence swarm;
	static ParticleInfluence drag;

	static int smokeParticleCount = 15;//20;//35;
	static int flameParticleCount = 5;//7;//15;
	static int exploderParticleCount = 32;//65;

	static ColorRGBA flameStartColor = new ColorRGBA(1f, 0.5f, 0, 1f);
	static ColorRGBA flameEndColor = new ColorRGBA(0.5f, 0, 0, 0f);

	static ColorRGBA exploderStartColor = new ColorRGBA(0.9f, 0.3f, 0, 0.7f); //(1f, 0.5f, 0, 1f);
	static ColorRGBA exploderEndColor = new ColorRGBA(0.6f, 0, 0, 0f);

	static ColorRGBA smokeStartColor = new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f);
	static ColorRGBA smokeEndColor = new ColorRGBA(0f, 0f, 0f, 0f);

	static float flameStartSize = 0.7f;
	static float flameEndSize = 3f;

	static float smokeStartSize = 1f;
	static float smokeEndSize = 3f;

	static float smokeMinLife = 900f;//1200f;
	static float smokeMaxLife = 1000f;//1400f;

	static float flameMinLife = 25;//50f;
	static float flameMaxLife = 50f;//100f;

	static float exploderMinLife = 770f;
	static float exploderMaxLife = 1630f;

	public static ParticleMesh makeSmoker() {
		//Create states if we have note done so already
		makeStates();
		return makeParticleMesh(
				smokeParticleCount,
				smokeStartColor, smokeEndColor,
				smokeStartSize, smokeEndSize,
				smokeBlendState, smokeTextureState,
				rise,
				smokeMinLife, smokeMaxLife);
	}

	public static ParticleMesh makeFlamer() {
		//Create states if we have note done so already
		makeStates();
		return makeParticleMesh(
				flameParticleCount,
				flameStartColor, flameEndColor,
				flameStartSize, flameEndSize,
				flameBlendState, flameTextureState,
				burn,
				flameMinLife, flameMaxLife);
	}

	public static ParticleMesh makeExploder() {
		//Create states if we have note done so already
		makeStates();
		ParticleMesh pMesh = makeParticleMesh(
				exploderParticleCount,
				exploderStartColor, exploderEndColor,
				flameStartSize, flameEndSize,
				flameBlendState, flameTextureState,
				drag,
				exploderMinLife, exploderMaxLife);
		//pMesh.addInfluence(swarm);
		pMesh.setReleaseRate(EXPLODER_RATE);
		pMesh.setInitialVelocity(0.006f);//.013f);
		pMesh.getParticleController().setRepeatType(Controller.RT_CLAMP);
		return pMesh;
	}


	public static ParticleMesh makeParticleMesh(
			int particleCount,
			ColorRGBA startColor, ColorRGBA endColor,
			float startSize, float endSize,
			BlendState blendState,
			TextureState textureState,
			ParticleInfluence influence,
			float minLife,
			float maxLife) {
		//Create states if we have note done so already
		makeStates();

		ParticleMesh pMesh = ParticleFactory.buildParticles("particles", particleCount);
		pMesh.setParticleSpinSpeed(2);
	    pMesh.addInfluence(influence);
	    pMesh.setEmissionDirection(new Vector3f(0,1,0));
	    pMesh.setInitialVelocity(.006f);
	    pMesh.setStartSize(startSize);
	    pMesh.setEndSize(endSize);
	    pMesh.setMinimumLifeTime(minLife);
	    pMesh.setMaximumLifeTime(maxLife);
	    pMesh.setStartColor(startColor);
	    pMesh.setEndColor(endColor);
	    pMesh.setMaximumAngle(360f * FastMath.DEG_TO_RAD);
	    pMesh.getParticleController().setControlFlow(true);
	    pMesh.setReleaseVariance(0.0f);
        pMesh.getParticleController().setSpeed(1.0f);
	    pMesh.getParticleController().setRepeatType(
                Controller.RT_WRAP);
	    pMesh.forceRespawn();
	    //pMesh.warmUp(60);


	    //FIXME fix the particle bounding behaviour, and put bounds back in
	    //pMesh.setModelBound(new BoundingSphere());
	    //pMesh.updateModelBound();
	    //pMesh.updateRenderState();
	    pMesh.setModelBound(new BoundingBox());
	    pMesh.updateModelBound();

	    pMesh.setRenderState(blendState);
	    pMesh.setRenderState(textureState);
	    pMesh.setRenderState(noLight);
		pMesh.setRenderState(zState);

	    pMesh.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);

	    return pMesh;
	}

	static void makeStates() {
		if (flameBlendState == null) {
			smokeBlendState = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
			smokeBlendState.setBlendEnabled(true);
			//smokeBlendState.setSourceFunction(BlendState.SB_ZERO);
			//smokeBlendState.setDestinationFunction(BlendState.DB_ONE_MINUS_SRC_COLOR);
			smokeBlendState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
            smokeBlendState.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
            smokeBlendState.setTestEnabled(true);
			smokeBlendState.setEnabled(true);

			flameBlendState = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
			flameBlendState.setBlendEnabled(true);
			flameBlendState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
			flameBlendState.setDestinationFunction(BlendState.DestinationFunction.One);
            flameBlendState.setTestEnabled(true);
            flameBlendState.setEnabled(true);


			smokeTextureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
			Texture smokeTexture = TextureManager.loadTexture(SmokerFactory.class
					.getClassLoader().getResource("resources/smokeAlpha4Small.png"),
                    Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
			smokeTextureState.setTexture(smokeTexture);
			smokeTextureState.setEnabled(true);

			flameTextureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
			Texture flameTexture = TextureManager.loadTexture(SmokerFactory.class
					.getClassLoader().getResource("resources/smokeAlpha2Small.png"),
                    Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
			flameTextureState.setTexture(flameTexture);
			flameTextureState.setEnabled(true);

			zState = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
			zState.setEnabled(true);
			zState.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
			zState.setWritable(false);

			//Light state for all flame/smoke, has no lighting so the particles just add or subtract their colour
	        noLight = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
	        noLight.setEnabled(false);

	        rise = SimpleParticleInfluenceFactory.createBasicWind(0.03f, new Vector3f(0,1,0), true, false);
	        burn = SimpleParticleInfluenceFactory.createBasicGravity(new Vector3f(0, 0.07f, 0), false);

	        swarm = new SwarmInfluence(new Vector3f(), 3.0f);
	        swarm.setMaxSpeed(0.2f);
	        swarm.setTurnSpeed(2.742f);
	        swarm.setDeviance(15f);

	        drag = SimpleParticleInfluenceFactory.createBasicDrag(1.0f);

		}
	}

}
