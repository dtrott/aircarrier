package net.java.dev.aircarrier.cards.stack;

import java.util.ArrayList;
import java.util.List;

import net.java.dev.aircarrier.cards.Card;
import net.java.dev.aircarrier.cards.Deck;
import net.java.dev.aircarrier.cards.stack.CardPlacement.Facing;
import net.java.dev.aircarrier.cards.stack.undo.UndoStack;


public class StackActions {

	public static Stack makeStack(Deck deck, String name) {
		List<Card> deckList = new ArrayList<Card>(deck.getCards());
		List<CardPlacement> cards = new ArrayList<CardPlacement>();
		for (int i = 0; i < 13; i++) {
			cards.add(new CardPlacement(deckList.get(i), Facing.UP));			
		}
		Stack stack = new Stack(name, cards);
		stack.setSpread(true);
		return stack;
	}

	public static void main(String[] args) {
		Deck deck = Deck.getInstance();
		Stack a = makeStack(deck, "a");
		Stack b = makeStack(deck, "b");
		
		UndoStack undo = new UndoStack();
		
		System.out.println(a);
		System.out.println(b);
		System.out.println();
		
		ReverseCards rc = new ReverseCards(a, 2, 5);
		FlipCards f = new FlipCards(a, 2, 5);
		
		undo.keyPoint();
		undo.doAction(rc);
		undo.doAction(f);
		undo.keyPoint();
		
		System.out.println(a);
		System.out.println(b);
		System.out.println();

		undo.undo();
		
		System.out.println(a);
		System.out.println(b);
		System.out.println();
		
		/*
		Transfer t = new Transfer(a, 2, 4, b, 2);
		FlipCards f = new FlipCards(a, 2, 4);
		
		t.doAction();
		f.doAction();
		
		System.out.println(a);
		System.out.println(b);
		System.out.println();

		f.undoAction();
		t.undoAction();
		
		System.out.println(a);
		System.out.println(b);
		System.out.println();
		 */
	}
	
}
