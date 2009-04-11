package net.java.dev.aircarrier.cards;

public class Card implements Comparable<Card>{

	private static Suit[] SUIT_ORDER = new Suit[]{Suit.SPADES, Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS};
	
	private int index;
	private Value value;
	private Suit suit;
	private Colour colour;
	private Integer indexInteger;
	
	public Card(int index) {
		if (index < 0 || index>= 52) {
			throw new IllegalArgumentException("Card index must be from 0 to 51");
		}
		this.index = index;
		this.indexInteger = index;
		value = Value.values()[index%13];
		int suitIndex = (index/13) % 4;
		suit = SUIT_ORDER[suitIndex];
		colour = suit.getColour();

	}

	public int getIndex() {
		return index;
	}

	public Value getValue() {
		return value;
	}

	public Suit getSuit() {
		return suit;
	}

	public Colour getColour() {
		return colour;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Card) {
			Card other = (Card) obj;
			return other.getIndex() == getIndex();
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return index;
	}

	@Override
	public String toString() {
		return value.getName() + " of " + suit.getName();
	}

	@Override
	public int compareTo(Card o) {
		return indexInteger.compareTo(o.getIndex());
	}
	
}
