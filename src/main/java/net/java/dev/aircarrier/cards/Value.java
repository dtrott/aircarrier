package net.java.dev.aircarrier.cards;

/**
 * Card suits in canonical (bridge) order
 */
public enum Value {

	A("A", "Ace", 0),
	V2("2", "2", 1),
	V3("3", "3", 2),
	V4("4", "4", 3),
	V5("5", "5", 4),
	V6("6", "6", 5),
	V7("7", "7", 6),
	V8("8", "8", 7),
	V9("9", "9", 8),
	V10("10", "10", 9),
	J("J", "Jack", 10),
	Q("Q", "Queen", 11),
	K("K", "King", 12);

	String letter;
	String name;
	int index;
	
	private Value(String letter, String name, int index) {
		this.name = name;
		this.index = index;
	}

	public String getLetter() {
		return letter;
	}

	public String getName() {
		return name;
	}

	public int getIndex() {
		return index;
	}
	
}