package net.java.dev.aircarrier.cards.stack;

public class StackIndex {

	Stack stack;
	int index;
	
	public StackIndex(Stack stack, int index) {
		super();
		this.stack = stack;
		this.index = index;
	}
	
	public Stack getStack() {
		return stack;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public String toString() {
		return "Card " + index + " in stack " + stack.getName();
	}
	
}
