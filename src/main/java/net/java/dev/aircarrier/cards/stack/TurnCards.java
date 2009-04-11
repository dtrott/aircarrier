/**
 * 
 */
package net.java.dev.aircarrier.cards.stack;

/**
 * Reverse the order AND turn over the facing of cards in a stack.
 * This is hence like physically taking the section of the stack of cards out,
 * turning it over, and putting it back where the section was. If the whole stack
 * is turned, this is like turning it over as a unit. 
 */
public class TurnCards implements StackAction {

	FlipCards flip;
	ReverseCards reverse;
	
	public TurnCards(Stack stack) {
		this(stack, 0, stack.contents().size());
	}

	public TurnCards(Stack stack, int index) {
		this(stack, index, index+1);
	}

	public TurnCards(Stack stack, int startIndex, int endIndex) {
		super();
		flip = new FlipCards(stack, startIndex, endIndex);
		reverse = new ReverseCards(stack, startIndex, endIndex);
	}

	@Override
	public void doAction() {
		flip.doAction();
		reverse.doAction();
	}

	@Override
	public void undoAction() {
		reverse.undoAction();
		flip.undoAction();
	}
	
}