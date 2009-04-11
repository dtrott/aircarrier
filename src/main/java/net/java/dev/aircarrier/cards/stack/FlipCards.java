/**
 * 
 */
package net.java.dev.aircarrier.cards.stack;

/**
 * Turn cards over individually, without altering their position in the stack
 */
public class FlipCards implements StackAction {
	Stack stack;
	int startIndex;
	int endIndex;

	public FlipCards(Stack stack) {
		this(stack, 0, stack.contents().size());
	}

	public FlipCards(Stack stack, int index) {
		this(stack, index, index+1);
	}

	public FlipCards(Stack stack, int startIndex, int endIndex) {
		super();
		this.stack = stack;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	@Override
	public void doAction() {
		for (int i = startIndex; i < endIndex; i++) {
			stack.contents().get(i).flip();
		}
	}

	@Override
	public void undoAction() {
		doAction();
	}
	
}