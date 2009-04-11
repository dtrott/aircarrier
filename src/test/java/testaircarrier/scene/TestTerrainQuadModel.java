package testaircarrier.scene;

import net.java.dev.aircarrier.input.action.NodeRotator;
import net.java.dev.aircarrier.scene.CarrierTerrainPage;
import net.java.dev.aircarrier.scene.TerrainQuadModel;

import com.jme.bounding.*;
import com.jme.image.*;
import com.jme.math.*;
import com.jme.scene.shape.*;
import com.jme.scene.state.*;
import com.jme.util.*;
import com.jmetest.physics.*;
import com.jmex.game.*;
import com.jmex.game.state.*;

/**
 * @author shingoki
 */
public class TestTerrainQuadModel {
	public static void main(String[] args) throws Exception {
		StandardGame game = new StandardGame("Test TerrainQuadModel");
		game.getSettings().clear();
		game.start();
		
		TextureState ts = game.getDisplay().getRenderer().createTextureState();
	    Texture t = TextureManager.loadTexture(TestStressPhysics.class.getClassLoader().getResource("jmetest/data/texture/cloud_land.jpg"), Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);
	    t.setWrap(Texture.WM_WRAP_S_WRAP_T);
	    ts.setTexture(t);
	    Box box = new Box("Box", new Vector3f(), 10.0f, 10.0f, 10.0f);
    	box.setModelBound(new BoundingBox());
    	box.updateModelBound();
    	box.setRenderState(ts);
    	BasicGameState debug = new BasicGameState("Basic");
    	//debug.getRootNode().attachChild(box);
    	
    	
    	TerrainQuadModel dunes = new TerrainQuadModel("resources/desert2.jme", "resources/dunes.jpg", "resources/detail.png");
    	debug.getRootNode().attachChild(dunes.getModel());
    	//CullState cullState = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
    	//cullState.setCullMode(CullState.CS_NONE);
    	//dunes.getModel().setRenderState(cullState);
    	
    	float shift = 5f;
    	float shiftUp = 0;//0.5f;
    	float rotate = FastMath.PI/8f;//FastMath.PI/4f;
    	float dunesScale = 5f;
    	int terrainTiles = 128;
    	
    	dunes.getModel().setLocalScale(dunesScale);
    	dunes.getModel().getLocalScale().setZ(-dunes.getModel().getLocalScale().getZ());
    	dunes.getModel().setLocalTranslation(shift, shiftUp, 0);
    	NodeRotator.rotate(dunes.getModel(), 0, rotate);
    	//NodeTranslator.translate(dunes.getModel(), 0, shift);

    	
    	/*
    	dunes = new TerrainQuadModel("resources/desert2.jme", "resources/dunes.png", "resources/detail.png");
    	debug.getRootNode().attachChild(dunes.getModel());
    	
    	dunes.getModel().setLocalScale(5);
    	dunes.getModel().setLocalTranslation(-shift, shiftUp, 0);
    	NodeRotator.rotate(dunes.getModel(), 0, rotate);
    	//NodeTranslator.translate(dunes.getModel(), 0, shift);
*/
    	
    	
    	
    	float terrainHeightScale = 1f/255f;// 1.5f;    	
    	float terrainHorizontalScale = 2 * dunesScale / terrainTiles;//0.07777777777f;

    	CarrierTerrainPage terrain = CarrierTerrainPage.create("Terrain",
				//"resources/higher_height_small.jpg",
				// "resources/canyonWaterHeight.jpg",
				//"resources/islands_height_orig.jpg",
//				"resources/nice_island_height.jpg",
				//"resources/nice_no_water_height.jpg",
				"resources/blackWhite.png",
			
				//"resources/nowaterHeight.jpg",
				//"resources/nowaterCroppedHeight.jpg",

				//"resources/higher_plain_lm_small.jpg",
				// "resources/canyonWaterTexture.jpg",
				//"resources/islands_LM_small.jpg",
				//"resources/islands_LM_small_sand.jpg",
				//"resources/nice_island_tex.jpg",
				
				//"resources/nice_no_water_TX.jpg",
				"resources/dunes_for_heightmap.jpg",

				
				//"resources/nowaterTexture.jpg",
				//"resources/nowaterCroppedTextureRetouched.png",

				"resources/detail.png", terrainTiles + 1,//129,//257,
				terrainHorizontalScale,
				terrainHeightScale,
				false);
    	
    	System.out.println(terrain.getHeight(-1, 0));
    	System.out.println(terrain.getHeight(1, 0));
    	
    	debug.getRootNode().attachChild(terrain);
    	//terrain.getLocalScale().setZ(-terrain.getLocalScale().getZ());
    	NodeRotator.rotate(terrain, 0, rotate);
    	//NodeTranslator.translate(terrain, 0, -shift);
    	terrain.setLocalTranslation(-shift, -shiftUp, 0);
    	
    	debug.getRootNode().updateRenderState();
    	
    	GameStateManager.getInstance().attachChild(debug);
    	debug.setActive(true);
		
	}
}