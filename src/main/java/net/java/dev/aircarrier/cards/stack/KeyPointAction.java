/**
 * 
 */
package net.java.dev.aircarrier.cards.stack;

/**
 * Does nothing to cards, just marks a key point in the game, usually
 * a point where we stop when we are redoing/undoing
 */
public class KeyPointAction implements StackAction {

	public KeyPointAction() {
	}

	@Override
	public void doAction() {
	}

	@Override
	public void undoAction() {
	}
	
}