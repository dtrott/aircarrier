package net.java.dev.aircarrier.cards;

/**
 * Card colours 
 */
public enum Colour {
	RED(0),
	BLACK(1);
	
	int index;
	
	private Colour(int index) {
		this.index = index;
	}
}