package net.java.dev.aircarrier.cards.stack;

import net.java.dev.aircarrier.cards.Card;

public class CardPlacement {

	public enum Facing {
		UP,
		DOWN
	}

	Card card;
	Facing facing;

	public CardPlacement(Card card, Facing facing) {
		super();
		this.card = card;
		this.facing = facing;
	}
	
	public Card getCard() {
		return card;
	}
	
	public Facing getFacing() {
		return facing;
	}
	
	public void flip() {
		if (facing == Facing.UP) {
			facing = Facing.DOWN;
		} else {
			facing = Facing.UP;
		}
	}

	@Override
	public String toString() {
		String s = (facing == Facing.DOWN) ? "#" : "";
		s = s + card.getValue().getName() + " " + card.getSuit().getName();
		return s;
	}
	
}
