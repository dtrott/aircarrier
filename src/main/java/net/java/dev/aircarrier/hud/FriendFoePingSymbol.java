package net.java.dev.aircarrier.hud;

import net.java.dev.aircarrier.util.TextureLoader;

import com.jme.image.Texture;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

public class FriendFoePingSymbol extends Quad {
	private static final long serialVersionUID = -4662279708062573339L;
	
	private static Texture tFriendly = TextureLoader.loadTexture("resources/radarDotFriendly.png");
	private static Texture tEnemy = TextureLoader.loadTexture("resources/radarDot.png");

	TextureState ts;
	
	boolean enemy = true;
	
	/**
	 *	Create a ping symbol, 8x8 pixels, named "PingSymbol" 
	 */
	public FriendFoePingSymbol() {		
		this("PingSymbol", 8, 8);
	}

	/**
	 * Create a ping symbol
	 * @param name
	 * 		Name of the Quad
	 * @param width
	 * 		Width of the quad
	 * @param height
	 * 		Height of the quad
	 */
	public FriendFoePingSymbol(String name, float width, float height) {
		super(name, width, height);
		ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setEnabled(true);
		ts.setTexture(tEnemy);
		setRenderState(ts);
	}

	/**
	 * @return
	 * 		True if the ping represents an enemy, false for a friend
	 */
	public boolean isEnemy() {
		return enemy;
	}

	/**
	 * Set enemy status
	 * @param enemy
	 * 		True if the ping represents an enemy, false for a friend
	 */
	public void setEnemy(boolean enemy) {
		this.enemy = enemy;
		if (enemy) {
			ts.setTexture(tEnemy);
		} else {
			ts.setTexture(tFriendly);
		}
	}

}
