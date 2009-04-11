/**
 * 
 */
package net.java.dev.aircarrier.cards.stack;

import java.util.LinkedList;
import java.util.List;

/**
 * Reverse the order of cards in a stack, without flipping them over
 */
public class ReverseCards implements StackAction {
	Stack stack;
	int startIndex;
	int endIndex;
	
	public ReverseCards(Stack stack) {
		this(stack, 0, stack.contents().size());
	}

	public ReverseCards(Stack stack, int index) {
		this(stack, index, index+1);
	}

	public ReverseCards(Stack stack, int startIndex, int endIndex) {
		super();
		this.stack = stack;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	@Override
	public void doAction() {
		List<CardPlacement> hold = stack.contents().subList(startIndex, endIndex);
		List<CardPlacement> reversed = new LinkedList<CardPlacement>();
		for (CardPlacement cp : hold) {
			reversed.add(0, cp);
		}
		hold.clear();
		
		stack.contents().addAll(startIndex, reversed);
	}

	@Override
	public void undoAction() {
		doAction();
	}
	
}