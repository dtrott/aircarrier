package net.java.dev.aircarrier.cards.stack.undo;

import java.util.Stack;

import net.java.dev.aircarrier.cards.stack.KeyPointAction;
import net.java.dev.aircarrier.cards.stack.StackAction;

public class UndoStack {

	Stack<StackAction> actions = new Stack<StackAction>();
	
	public void doAction(StackAction action) {
		actions.push(action);
		action.doAction();
	}
	
	public void keyPoint() {
		doAction(new KeyPointAction());
	}
	
	public void undoSingleAction() {
		actions.pop().undoAction();
	}
	
	public void undo() {
		if (actions.peek() instanceof KeyPointAction) {
			undoSingleAction();
		}
		
		while (!(actions.peek() instanceof KeyPointAction)) {
			undoSingleAction();
		}
	}
	
}
