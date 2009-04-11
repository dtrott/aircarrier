package net.java.dev.aircarrier.cards.field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Field {

	List<StackPlacement> stackPlacements;
	List<StackPlacement> umStackPlacements;

	public Field() {
		stackPlacements = new ArrayList<StackPlacement>();
		umStackPlacements = Collections.unmodifiableList(stackPlacements);
	}
	
	public List<StackPlacement> getStackPlacements() {
		return umStackPlacements;
	}

	public void addStackPlacement(StackPlacement sp) {
		stackPlacements.add(sp);
	}
	
}
