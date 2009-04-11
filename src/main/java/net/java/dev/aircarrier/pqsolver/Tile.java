package net.java.dev.aircarrier.pqsolver;

public enum Tile {

	EMPTY(false, false, "."),
	RED(true, false, "r"),
	YELLOW(true, false, "y"),
	GREEN(true, false, "g"),
	BLUE(true, false, "b"),
	SKULL(false, true, "s"),
	SKULL5(false, true, "5"),
	MONEY(false, false, "m"),
	XP(false, false, "x"),
	WILDCARD(false, false, "w");
	
	boolean gem;
	boolean skull;
	String letter;
	
	Tile(boolean gem, boolean skull, String letter) {
		this.gem = gem;
		this.skull = skull;
		this.letter = letter;
	}
	
	public boolean isGem() {
		return gem;
	}
	
	public boolean isSkull() {
		return skull;
	}
	
	public String getLetter() {
		return letter;
	}
	
	public boolean matches(Tile tile) {
		if (this == EMPTY || tile == EMPTY) return false;
		if (this == WILDCARD && tile.isGem()) return true;
		if (this.isGem() && tile == WILDCARD) return true;
		if (this.isSkull() && tile.isSkull()) return true;
		
		return (this == tile);
	}
}
