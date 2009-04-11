package net.java.dev.aircarrier.planes;

import net.java.dev.aircarrier.scene.Updatable;

import com.jme.bounding.BoundingSphere;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public class Propeller extends Node implements Updatable {
	private static final long serialVersionUID = 6184434057601056250L;
	
	private static int PART_COUNT = 3;
	
	Quad[] propQuads;
	float[] propAngles;
	float[] propVelocities;
	
	Vector3f lightVector;
	
	Vector3f tempX = new Vector3f();
	Vector3f tempX2 = new Vector3f();
	Vector3f tempZ = new Vector3f();
	
	public Propeller(Vector3f lightVector) {
		
		this.lightVector = new Vector3f(lightVector).normalizeLocal();
		
		AlphaState alphaState;
		Texture texture;
		TextureState textureState;
		LightState noLight;
		ZBufferState zBufferState;

		AlphaState shineAlphaState;
		Texture shineTexture;
		TextureState shineTextureState;

		//Prop is two sided
		CullState cullState = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
		cullState.setCullMode(CullState.CS_NONE);
		
		DisplaySystem display = DisplaySystem.getDisplaySystem();

		//Alpha state for all bullets adds the texture to anything behind it
		/*
		alphaState = display.getRenderer().createAlphaState();
		alphaState.setBlendEnabled( true );
		alphaState.setSrcFunction( AlphaState.SB_SRC_ALPHA );
		alphaState.setDstFunction( AlphaState.DB_ONE_MINUS_SRC_ALPHA );
		alphaState.setTestEnabled(true);
		alphaState.setEnabled( true );*/
		
		alphaState = DisplaySystem.getDisplaySystem().getRenderer().createAlphaState();
		alphaState.setBlendEnabled(true);
		//smokeAlphaState.setSrcFunction(AlphaState.SB_ZERO);
		//smokeAlphaState.setDstFunction(AlphaState.DB_ONE_MINUS_SRC_COLOR);
		alphaState.setSrcFunction(AlphaState.SB_SRC_ALPHA);
		alphaState.setDstFunction(AlphaState.DB_ONE_MINUS_SRC_ALPHA);			
		alphaState.setTestEnabled(true);
		alphaState.setEnabled(true);
		
		
		//texture = TextureLoader.loadTexture("resources/propBlur.png");
		texture = TextureManager.loadTexture(Propeller.class
				.getClassLoader().getResource("resources/propBlur.png"),
				Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);
		
		//Texture state for all bullets
		textureState = display.getRenderer().createTextureState();
		textureState.setTexture(texture);
		textureState.setEnabled(true);

		//Light state for all bullets, has no lighting so the bullets
		//just glow
        noLight  = display.getRenderer().createLightState();
        noLight.setEnabled(false);

        //ZBufferState for all bullets, makes the z buffer non-writable,
        //so that faces of the bullet do not hide anything (since they
        //are additive this should never be fine, and since the transparent
        //render queue is used, this should work out).
        zBufferState = display.getRenderer().createZBufferState();
        zBufferState.setEnabled(true);
        zBufferState.setFunction(ZBufferState.CF_LEQUAL);
        zBufferState.setWritable(false);
        
        
		//Alpha state for shine adds the texture to anything behind it
		shineAlphaState = display.getRenderer().createAlphaState();
		shineAlphaState.setBlendEnabled( true );
		shineAlphaState.setSrcFunction( AlphaState.SB_SRC_ALPHA );
		shineAlphaState.setDstFunction( AlphaState.DB_ONE );
		shineAlphaState.setTestEnabled(true);
		shineAlphaState.setEnabled( true );
		
		//shineTexture = TextureLoader.loadTexture("resources/propShine.png");
		shineTexture = TextureManager.loadTexture(Propeller.class
				.getClassLoader().getResource("resources/propShine.png"),
				Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);
		
		//Texture state for all bullets
		shineTextureState = display.getRenderer().createTextureState();
		shineTextureState.setEnabled(true);
		shineTextureState.setTexture(shineTexture);

        
        //Make array of prop quads
        propQuads = new Quad[PART_COUNT];
        propAngles = new float[PART_COUNT];
        propVelocities = new float[PART_COUNT];
        for (int i = 0; i < propQuads.length; i++) {
			Quad prop = new Quad("prop", 3.5f, 3.5f);
	
			prop.setRenderState(cullState);
			prop.setRenderState(alphaState);
			prop.setRenderState(textureState);
			prop.setRenderState(noLight);
			prop.setRenderState(zBufferState);
	
			prop.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);

			attachChild(prop);

			propQuads[i] = prop;
			
			propVelocities[i] = (2.523f + i * 1.3692f + FastMath.rand.nextFloat()/2f);

			propQuads[i].setLocalTranslation(new Vector3f(0f, 0f, -0.025f * i));
        }
        
		propQuads[PART_COUNT-1].setRenderState(shineTextureState);
		propQuads[PART_COUNT-1].setRenderState(shineAlphaState);
		propVelocities[PART_COUNT-1] = 0;
		propAngles[PART_COUNT-1] = 0;
		
		setModelBound(new BoundingSphere());
		updateModelBound();
	}
	
	public void update(float time) {
		for (int i = 0; i < propQuads.length; i++) {
			propAngles[i] += time * propVelocities[i];
			propQuads[i].getLocalRotation().fromAngles(0f, 0f, propAngles[i]);
		}
		
		//For the shine part, work out angle of shine based on our world rotation,
		//and the light vector (taken as a world vector). The model is that the
		//prop blades will shine when they are pointing along the cross product of
		//the light vector, and the "shaft" vector - that is when the light is shining
		//on the "edge" of the blades, rather than on the along the blades.
		//So we take the cross product of the light and shaft vectors, and work out the
		//rotation around the shaft vector from the node's "up" vector to point
		//the shine as along the cross product.
		
		//Get the prop vector, z axis, in world space
		getWorldRotation().getRotationColumn(2, tempZ);
		
		//Make the X axis from cross
		lightVector.cross(tempZ, tempX);
		
		//Find the angle between the prop world X axis and the X axis we found
		getWorldRotation().getRotationColumn(0, tempX2);
		float angle = tempX2.angleBetween(tempX);
		
		//rotate the quad along the axes
		propQuads[PART_COUNT-1].getLocalRotation().fromAngles(0f, 0f, angle);
		
	}
	
}
