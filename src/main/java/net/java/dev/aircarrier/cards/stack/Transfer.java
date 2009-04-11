/**
 * 
 */
package net.java.dev.aircarrier.cards.stack;

import java.util.List;

public class Transfer implements StackAction {
	Stack from;
	int startIndex;
	int endIndex;
	Stack to;
	int destIndex;
	
	public Transfer(Stack from, int startIndex, int endIndex, Stack to,
			int destIndex) {
		super();
		this.from = from;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.to = to;
		this.destIndex = destIndex;
	}

	@Override
	public void doAction() {
		List<CardPlacement> hold = from.contents().subList(startIndex, endIndex);
		to.contents().addAll(destIndex, hold);
		hold.clear();
	}

	@Override
	public void undoAction() {
		//Work out how many we added to the "to" stack
		int size = endIndex - startIndex;
		List<CardPlacement> hold = to.contents().subList(destIndex, destIndex + size);
		from.contents().addAll(startIndex, hold);
		hold.clear();
	}
	
}