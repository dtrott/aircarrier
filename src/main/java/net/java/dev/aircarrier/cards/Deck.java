package net.java.dev.aircarrier.cards;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class Deck {

	private static Deck INSTANCE = new Deck();
	
	private SortedSet<Card> cards;
	
	private Deck() {
		cards = new TreeSet<Card>();
		for (int i = 0; i < 52; i++) {
			cards.add(new Card(i));
		}
		cards = Collections.unmodifiableSortedSet(cards);
	}

	public SortedSet<Card> getCards() {
		return cards;
	}

	public static Deck getInstance() {
		return INSTANCE;
	}
	
}
