package net.java.dev.aircarrier.cards;

/**
 * Card suits in canonical (bridge) order
 */
public enum Suit {

	SPADES("Spades", Colour.BLACK, 0),
	HEARTS("Hearts", Colour.RED, 1),
	DIAMONDS("Diamonds", Colour.RED, 2),
	CLUBS("Clubs", Colour.BLACK, 3);
	
	String name;
	Colour colour;
	int index;
	
	private Suit(String name, Colour colour, int index) {
		this.name = name;
		this.colour = colour;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public Colour getColour() {
		return colour;
	}

	public int getIndex() {
		return index;
	}
	
}